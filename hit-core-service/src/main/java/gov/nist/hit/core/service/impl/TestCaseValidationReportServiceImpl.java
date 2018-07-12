package gov.nist.hit.core.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.ibm.icu.util.Calendar;

import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseValidationResult;
import gov.nist.hit.core.domain.TestResult;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.repo.TestStepValidationReportRepository;
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.TestCaseValidationReportService;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.exception.ValidationReportException;
import gov.nist.hit.core.service.util.HtmlUtil;
import gov.nist.hit.core.service.util.ReportUtil;
import nu.xom.Attribute;

@PropertySource(value = {"classpath:app-config.properties"})
@Service
public class TestCaseValidationReportServiceImpl implements TestCaseValidationReportService {

  private final static Logger logger = Logger.getLogger(TestCaseValidationReportServiceImpl.class);
  private static final String HTML_XSL = "/report/testcase-validation-report-html.xsl";
  private static final String PDF_XSL = "/report/testcase-validation-report-pdf.xsl";
  protected static final String CSS = "/report/report.css";
  protected String css = "";

  @Value("${app.header}")
  private String appName;

  @Value("${app.version}")
  private String appVersion;


  @Autowired
  private TestStepValidationReportService testStepValidationReportService;

  @Autowired
  private TestStepService testStepService;


  @Autowired
  private TestCaseService testCaseService;

  @Autowired
  protected TestStepValidationReportRepository testStepValidationReportRepository;

  @Override
  public void deleteByTestCaseAndUser(Long userId, Long testCaseId) {
    List<TestStepValidationReport> results =
        findTestStepReportsByTestCaseAndUser(userId, testCaseId);
    if (results != null && !results.isEmpty()) {
      testStepValidationReportRepository.delete(results);
    }
  }


  @Override
  public InputStream generatePdf(TestCase testCase, Long userId, String result, String comments,
      String testPlan, String testGroup) throws ValidationReportException {
    try {
      return generatePdf(generateXml(testCase, userId, result, comments, testPlan, testGroup));
    } catch (Exception e) {
      throw new ValidationReportException("Failed to generate the test case report");
    }
  }

  public TestCaseValidationReportServiceImpl() {
    try {
      css = IOUtils.toString(TestStepValidationReportServiceImpl.class.getResourceAsStream(CSS));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String generateXml(TestCaseValidationResult result) throws ValidationReportException {
    try {
      nu.xom.Element report =
          new nu.xom.Element("testcasevalidationreport:TestCaseValidationReport",
              "http://www.nist.gov/healthcare/validation/testcase/report");
      // report.addNamespaceDeclaration("testcasevalidationreport",
      // "http://www.nist.gov/healthcare/validation/testcase/report");
      nu.xom.Element headerReport =
          new nu.xom.Element("testcasevalidationreport:TestCaseValidationReportHeader",
              "http://www.nist.gov/healthcare/validation/testcase/report");
      report.appendChild(headerReport);

      nu.xom.Element serviceProvider =
          new nu.xom.Element("testcasevalidationreport:ServiceProvider",
              "http://www.nist.gov/healthcare/validation/testcase/report");
      serviceProvider.appendChild("NIST");
      headerReport.appendChild(serviceProvider);

      nu.xom.Element toolName = new nu.xom.Element("testcasevalidationreport:ToolName",
          "http://www.nist.gov/healthcare/validation/testcase/report");
      toolName.appendChild(appName);
      headerReport.appendChild(toolName);

      nu.xom.Element toolVersion = new nu.xom.Element("testcasevalidationreport:ToolVersion",
          "http://www.nist.gov/healthcare/validation/testcase/report");
      toolVersion.appendChild(appVersion);
      headerReport.appendChild(toolVersion);


      nu.xom.Element validationType = new nu.xom.Element("testcasevalidationreport:ValidationType",
          "http://www.nist.gov/healthcare/validation/testcase/report");
      validationType.appendChild("Context-Based");
      headerReport.appendChild(validationType);

      nu.xom.Element type = new nu.xom.Element("testcasevalidationreport:Type",
          "http://www.nist.gov/healthcare/validation/testcase/report");
      type.appendChild("Test Case Validation");
      headerReport.appendChild(type);

      nu.xom.Element dateOfTest = new nu.xom.Element("testcasevalidationreport:DateOfTest",
          "http://www.nist.gov/healthcare/validation/testcase/report");
      dateOfTest.appendChild(ReportUtil.dateOfTest(
          result.getDate() != null ? result.getDate() : Calendar.getInstance().getTime()));
      headerReport.appendChild(dateOfTest);

      headerReport.addAttribute(new Attribute("Result", getTestCaseResult(result.getResult())));
      nu.xom.Element comments = new nu.xom.Element("testcasevalidationreport:Comments",
          "http://www.nist.gov/healthcare/validation/testcase/report");
      headerReport.appendChild(comments);
      comments.appendChild(result.getComments());



      nu.xom.Element testCaseReportContent =
          new nu.xom.Element("testcasevalidationreport:TestCaseValidationReportBody",
              "http://www.nist.gov/healthcare/validation/testcase/report");
      report.appendChild(testCaseReportContent);

      for (int i = 1; i <= result.getTestStepReports().size(); i++) {
        for (TestStepValidationReport testStepReport : result.getTestStepReports()) {
          TestStep testStep = testStepService.findOne(testStepReport.getTestStepId());
          if (testStep != null && testStep.getPosition() == i) {
            String xml = testStepValidationReportService.generateXmlTestStepValidationReport(
                testStepReport.getXml(), testStepReport, testStep);
            testCaseReportContent.appendChild(ReportUtil.getXmlElement(xml));
          }
        }
      }


      Map<String, String> nav = result.getNav();
      if (nav != null && !nav.isEmpty()) {
        if (nav.containsKey("testCase") && nav.get("testCase") != null) {
          nu.xom.Element testCase = new nu.xom.Element("testcasevalidationreport:TestCase",
              "http://www.nist.gov/healthcare/validation/testcase/report");
          testCase.appendChild(nav.get("testCase"));
          headerReport.appendChild(testCase);
        }

        if (nav.containsKey("testPlan") && nav.get("testPlan") != null) {
          nu.xom.Element testPlan = new nu.xom.Element("testcasevalidationreport:TestPlan",
              "http://www.nist.gov/healthcare/validation/testcase/report");
          testPlan.appendChild(nav.get("testPlan"));
          headerReport.appendChild(testPlan);
        }

        if (nav.containsKey("testGroup") && nav.get("testGroup") != null) {

          nu.xom.Element testGroup = new nu.xom.Element("testcasevalidationreport:TestGroup",
              "http://www.nist.gov/healthcare/validation/testcase/report");
          testGroup.appendChild(nav.get("testGroup"));
          headerReport.appendChild(testGroup);
        }

      }


      String xml = report.toXML();
      // File f = new File("src/main/resources/TestCaseValidationReport.xml");
      // System.out.println(f.getAbsolutePath());
      // PrintWriter pw = new PrintWriter(f);
      // pw.print(xml);
      // pw.close();

      return xml;
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }


  public String getTestCaseResult(TestResult result) throws ValidationReportException {
    return result != null ? result.getTitle() : "";
  }


  @Override
  public String generateHtml(String xml) throws ValidationReportException {
    try {
      String xslt =
          IOUtils.toString(TestCaseValidationReportServiceImpl.class.getResourceAsStream(HTML_XSL));

      TransformerFactory transFact = TransformerFactory.newInstance();
      transFact.setURIResolver(new XsltURIResolver());
      Transformer transformer = transFact.newTransformer(new StreamSource(new StringReader(xslt)));
      StreamSource source = new StreamSource(new StringReader(xml));
      ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
      StreamResult result = new StreamResult(resultStream);
      transformer.transform(source, result);
      String htmlReport = HtmlUtil.repairStyle(new String(resultStream.toByteArray()));
      logger.info("HTML TestCase validation report generated");
      return addCss(htmlReport);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }

  @Override
  public String generateXhtml(String xml) throws ValidationReportException {
    try {
      String xslt =
          IOUtils.toString(TestCaseValidationReportServiceImpl.class.getResourceAsStream(PDF_XSL));
      TransformerFactory transFact = TransformerFactory.newInstance();
      transFact.setURIResolver(new XsltURIResolver());
      Transformer transformer = transFact.newTransformer(new StreamSource(new StringReader(xslt)));
      StreamSource source = new StreamSource(new StringReader(xml));
      ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
      StreamResult result = new StreamResult(resultStream);
      transformer.transform(source, result);
      String html = HtmlUtil.repairStyle(new String(resultStream.toByteArray()));
      logger.info("XHTML TestCase Validation report generated");
      return addCss(html);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }

  @Override
  public InputStream generatePdf(String xml) throws ValidationReportException {
    try {
      String xhtml = generateXhtml(xml).replaceAll("<br>", "<br/>");
      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(xhtml);
      renderer.layout();
      File temp = File.createTempFile("TestCaseValidationReport", ".pdf");
      temp.deleteOnExit();
      OutputStream os;
      os = new FileOutputStream(temp);
      renderer.createPDF(os);
      os.close();
      return new FileInputStream(temp);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }

  public String addCss(String htmlReport) throws IOException {
    StringBuffer sb = new StringBuffer();
    sb.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
    sb.append("<head>");
    sb.append("<title>TestCase Validation Report</title>");
    sb.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
    sb.append("<style>");
    sb.append(css);
    sb.append("</style></head><body>");
    sb.append(htmlReport);
    sb.append("</body></html>");
    return sb.toString();
  }



  private List<TestStepValidationReport> findTestStepReportsByTestCaseAndUser(Long userId,
      Long testCaseId) {
    TestCase testCase = testCaseService.findOne(testCaseId);
    List<TestStepValidationReport> reports = null;
    if (testCase != null) {
      Set<TestStep> testSteps = testCase.getTestSteps();
      if (testSteps != null) {
        reports = new ArrayList<TestStepValidationReport>();
        for (TestStep testStep : testSteps) {
          List<TestStepValidationReport> tmp =
              testStepValidationReportService.findAllByTestStepAndUser(testStep.getId(), userId);
          if (tmp != null && !tmp.isEmpty())
            reports.addAll(tmp);

        }
      }
    }
    return reports;
  }


  @Override
  public String generateHtml(TestCaseValidationResult result) throws ValidationReportException {
    return generateHtml(generateXml(result));
  }


  @Override
  public InputStream generatePdf(TestCaseValidationResult result) throws ValidationReportException {
    return generatePdf(generateXml(result));
  }


  @Override
  public String generateXhtml(TestCaseValidationResult result) throws ValidationReportException {
    return generateXhtml(generateXml(result));
  }



  @Override
  public String generateXml(TestCase testCase, Long userId, String res, String comments,
      String testPlan, String testGroup) throws ValidationReportException {
    try {
      List<TestStepValidationReport> testStepReports =
          findTestStepReportsByTestCaseAndUser(userId, testCase.getId());
      TestCaseValidationResult result =
          new TestCaseValidationResult(testCase, testStepReports, testPlan, testGroup);
      result.setResult(res != null ? TestResult.valueOf(res) : null);
      result.setComments(comments);
      return generateXml(result);
    } catch (Exception e) {
      throw new ValidationReportException("Failed to download the reports");
    }
  }

  @Override
  public String generateXml(TestCase testCase, Long userId, String res, String comments)
      throws ValidationReportException {
    try {
      List<TestStepValidationReport> testStepReports =
          findTestStepReportsByTestCaseAndUser(userId, testCase.getId());
      TestCaseValidationResult result = new TestCaseValidationResult(testCase, testStepReports);
      result.setResult(res != null ? TestResult.valueOf(res) : null);
      result.setComments(comments);
      return generateXml(result);
    } catch (Exception e) {
      throw new ValidationReportException("Failed to download the reports");
    }
  }



  @Override
  public String generateHtml(TestCase testCase, Long userId, String result, String comments,
      String testPlan, String testGroup) throws ValidationReportException {
    try {
      return generateHtml(generateXml(testCase, userId, result, comments, testPlan, testGroup));
    } catch (Exception e) {
      throw new ValidationReportException("Failed to generate the test case report");
    }
  }


  @Override
  public String generateHtml(TestCase testCase, Long userId, String result, String comments)
      throws ValidationReportException {
    try {
      return generateHtml(generateXml(testCase, userId, result, comments));
    } catch (Exception e) {
      throw new ValidationReportException("Failed to generate the test case report");
    }
  }

}
