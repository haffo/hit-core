package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.ManualValidationResult;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.repo.TestStepValidationReportRepository;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.exception.ValidationReportException;
import gov.nist.hit.core.service.util.HtmlUtil;
import gov.nist.hit.core.service.util.ReportUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import nu.xom.Attribute;
import nu.xom.Document;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.ibm.icu.util.Calendar;

@Service
public class TestStepValidationReportServiceImpl implements TestStepValidationReportService {

  private final static Logger logger = Logger.getLogger(TestStepValidationReportServiceImpl.class);
  private static final String PDF_XSL = "/report/teststep-validation-report-pdf.xsl";
  private static final String HTML_XSL = "/report/teststep-validation-report-html.xsl";
  protected static final String CSS = "/report/report.css";
  protected static final String JS = "/report/report.js";

  protected String css = "";
  protected String javascript = "";

  @Autowired
  protected TestStepValidationReportRepository validationReportRepository;



  public TestStepValidationReportServiceImpl() {
    try {
      css = IOUtils.toString(TestStepValidationReportServiceImpl.class.getResourceAsStream(CSS));
      javascript =
          IOUtils.toString(TestStepValidationReportServiceImpl.class.getResourceAsStream(JS));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public TestStepValidationReport save(TestStepValidationReport report) {
    return validationReportRepository.saveAndFlush(report);
  }


  @Override
  public void delete(Long id) {
    try {
      validationReportRepository.delete(id);
    } catch (RuntimeException e) {

    } catch (Exception e) {

    }
  }

  @Override
  public TestStepValidationReport findOneByIdAndUser(Long reportId, Long userId) {
    return validationReportRepository.findOneByIdAndUser(reportId, userId);
  }

  @Override
  public void delete(List<TestStepValidationReport> reports) {
    try {
      validationReportRepository.delete(reports);
    } catch (RuntimeException e) {

    } catch (Exception e) {

    }
  }

  @Override
  public void save(List<TestStepValidationReport> reports) {
    validationReportRepository.save(reports);
  }

  @Override
  public TestStepValidationReport findOneByTestStepAndUser(Long testStepId, Long userId) {
    return validationReportRepository.findOneByTestStepAndUser(userId, testStepId);
  }

  @Override
  public List<TestStepValidationReport> findAllByTestCaseAndUser(Long testCaseId, Long userId) {
    return validationReportRepository.findAllByTestCaseAndUser(userId, testCaseId);
  }

  @Override
  public List<TestStepValidationReport> findAllByUser(Long userId) {
    return validationReportRepository.findAllByUser(userId);
  }

  @Override
  public TestStepValidationReport findOne(Long id) {
    return validationReportRepository.findOne(id);
  }



  @Override
  public String generateHtml(String xml) throws ValidationReportException {
    try {
      if (StringUtils.isNotEmpty(xml)) {
        String xslt =
            IOUtils.toString(TestCaseValidationReportServiceImpl.class
                .getResourceAsStream(HTML_XSL));
        TransformerFactory transFact = TransformerFactory.newInstance();
        transFact.setURIResolver(new XsltURIResolver());
        Transformer transformer =
            transFact.newTransformer(new StreamSource(new StringReader(xslt)));
        StreamSource source = new StreamSource(new StringReader(xml));
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(resultStream);
        transformer.transform(source, result);
        String htmlReport = HtmlUtil.repairStyle(new String(resultStream.toByteArray()));
        logger.info("HTML validation report generated");
        return addCss(htmlReport);
      }
      return null;
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }


  @Override
  public String generateXhtml(String xml) throws ValidationReportException {
    try {
      if (StringUtils.isNotEmpty(xml)) {
        String xslt =
            IOUtils
                .toString(TestCaseValidationReportServiceImpl.class.getResourceAsStream(PDF_XSL));
        TransformerFactory transFact = TransformerFactory.newInstance();
        transFact.setURIResolver(new XsltURIResolver());
        Transformer transformer =
            transFact.newTransformer(new StreamSource(new StringReader(xslt)));
        StreamSource source = new StreamSource(new StringReader(xml));
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(resultStream);
        transformer.transform(source, result);
        String html = HtmlUtil.repairStyle(new String(resultStream.toByteArray()));
        logger.info("XHTML validation report generated");
        return addCss(html);
      }
      return null;
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }

  @Override
  public InputStream generatePdf(String xml) throws ValidationReportException {
    try {
      if (StringUtils.isNotEmpty(xml)) {
        String xhtml = generateXhtml(xml).replaceAll("<br>", "<br/>");
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(xhtml);
        renderer.layout();
        File temp = File.createTempFile("TestStepValidationReport", ".pdf");
        temp.deleteOnExit();
        OutputStream os;
        os = new FileOutputStream(temp);
        renderer.createPDF(os);
        os.close();
        return new FileInputStream(temp);
      }
      return null;
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }


  // @Override
  // public InputStream zipReports(String folderName, HashMap<String, InputStream> reports)
  // throws ValidationReportException {
  // try {
  // if (reports != null) {
  // Path path = Files.createTempDirectory(null);
  // File rootFolder = path.toFile();
  // if (!rootFolder.exists()) {
  // rootFolder.mkdir();
  // }
  // String folderToZipName = rootFolder.getAbsolutePath() + File.separator + folderName;
  // File folderToZipFile = new File(folderToZipName);
  // if (!folderToZipFile.exists()) {
  // folderToZipFile.mkdir();
  // }
  //
  // for (Entry<String, InputStream> entry : reports.entrySet()) {
  // createFile(entry.getValue(), entry.getKey(), folderToZipName);
  // }
  // String zipFilename = rootFolder.getAbsolutePath() + File.separator + folderName + ".zip";
  // zipDir(zipFilename, folderToZipName);
  // FileInputStream io = new FileInputStream(new File(zipFilename));
  // return io;
  // }
  // } catch (IOException e) {
  // throw new ValidationReportException(e);
  // } catch (Exception e) {
  // throw new ValidationReportException(e);
  // }
  // return null;
  // }
  //
  // private void zipDir(String zipFileName, String dir) throws Exception {
  // File dirObj = new File(dir);
  // ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
  // addFile(dirObj, out, dir);
  // out.close();
  // }
  //
  // private void addFile(File dirObj, ZipOutputStream out, String folderToZipPath) throws
  // IOException {
  // File[] files = dirObj.listFiles();
  // for (int i = 0; i < files.length; i++) {
  // if (files[i].isDirectory()) {
  // addFile(files[i], out, folderToZipPath);
  // continue;
  // }
  // String absolutePath = files[i].getAbsolutePath();
  // FileInputStream in = new FileInputStream(absolutePath);
  // String localPath =
  // absolutePath.substring(absolutePath.indexOf(folderToZipPath) + folderToZipPath.length()
  // + 1);
  // ZipEntry zipEntry = new ZipEntry(localPath);
  // out.putNextEntry(zipEntry);
  // out.write(IOUtils.toByteArray(in));
  // out.closeEntry();
  // }
  // }


  // private void createFile(InputStream report, String fileName, String folderToZip)
  // throws IOException {
  // String filename = folderToZip + File.separator + fileName;
  // File file = new File(filename);
  // if (!file.exists()) {
  // file.createNewFile();
  // }
  // FileUtils.copyInputStreamToFile(report, file);
  // }

  public String addCss(String htmlReport) throws IOException {
    StringBuffer sb = new StringBuffer();
    sb.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
    sb.append("<head>");
    sb.append("<title>Test Step Validation Report</title>");
    sb.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
    sb.append("<style>");
    sb.append(css);
    sb.append("</style>");
    sb.append("<style type='text/javascript'>");
    sb.append(javascript);
    sb.append("</style>");
    sb.append("</head><body>");
    sb.append(htmlReport);
    sb.append("</body></html>");
    return sb.toString();
  }

  @Override
  public List<TestStepValidationReport> findAllByTestStepAndUser(Long testStepId, Long userId) {
    return validationReportRepository.findAllByTestStepAndUser(userId, testStepId);
  }

  @Override
  public String generateManualXml(ManualValidationResult validationResult)
      throws ValidationReportException {
    try {
      nu.xom.Element report = new nu.xom.Element("manualvalidationreport:ManualValidationReport");
      report.addNamespaceDeclaration("manualvalidationreport",
          "http://www.nist.gov/healthcare/validation/manual/report");

      nu.xom.Element headerReport =
          new nu.xom.Element("manualvalidationreport:HeaderReport",
              "http://www.nist.gov/healthcare/validation/manual/report");
      report.appendChild(headerReport);

      nu.xom.Element serviceProvider =
          new nu.xom.Element("manualvalidationreport:ServiceProvider",
              "http://www.nist.gov/healthcare/validation/manual/report");
      serviceProvider.appendChild("NIST");
      headerReport.appendChild(serviceProvider);

      nu.xom.Element validationType =
          new nu.xom.Element("manualvalidationreport:ValidationType",
              "http://www.nist.gov/healthcare/validation/manual/report");
      validationType.appendChild("Context-Based");
      headerReport.appendChild(validationType);

      nu.xom.Element type =
          new nu.xom.Element("manualvalidationreport:Type",
              "http://www.nist.gov/healthcare/validation/manual/report");
      type.appendChild("Manual");
      headerReport.appendChild(type);

      nu.xom.Element dateOfTest =
          new nu.xom.Element("manualvalidationreport:DateOfTest",
              "http://www.nist.gov/healthcare/validation/manual/report");
      dateOfTest
          .appendChild(ReportUtil.dateOfTest(validationResult.getDate() != null ? validationResult
              .getDate() : Calendar.getInstance().getTime()));
      headerReport.appendChild(dateOfTest);


      Map<String, String> nav = validationResult.getNav();
      if (nav != null && !nav.isEmpty()) {
        nu.xom.Element testCaseReference =
            new nu.xom.Element("manualvalidationreport:TestCaseReference",
                "http://www.nist.gov/healthcare/validation/manual/report");
        report.appendChild(testCaseReference);

        nu.xom.Element testCase =
            new nu.xom.Element("manualvalidationreport:TestCase",
                "http://www.nist.gov/healthcare/validation/manual/report");
        testCase.appendChild(nav.get("testCase"));
        testCaseReference.appendChild(testCase);

        nu.xom.Element testPlan =
            new nu.xom.Element("manualvalidationreport:TestPlan",
                "http://www.nist.gov/healthcare/validation/manual/report");
        testPlan.appendChild(nav.get("testPlan"));
        testCaseReference.appendChild(testPlan);

        nu.xom.Element testStep =
            new nu.xom.Element("manualvalidationreport:TestStep",
                "http://www.nist.gov/healthcare/validation/manual/report");
        testStep.appendChild(nav.get("testStep"));
        testCaseReference.appendChild(testStep);

        nu.xom.Element testGroup =
            new nu.xom.Element("manualvalidationreport:TestGroup",
                "http://www.nist.gov/healthcare/validation/manual/report");
        testGroup.appendChild(nav.get("testGroup"));
        testCaseReference.appendChild(testGroup);
      }

      nu.xom.Element specificReport =
          new nu.xom.Element("manualvalidationreport:SpecificReport",
              "http://www.nist.gov/healthcare/validation/manual/report");
      report.appendChild(specificReport);
      specificReport.addAttribute(new Attribute("Result", validationResult.getValue()));
      nu.xom.Element comments =
          new nu.xom.Element("manualvalidationreport:Comments",
              "http://www.nist.gov/healthcare/validation/manual/report");
      specificReport.appendChild(comments);
      comments.appendChild(validationResult.getComments());
      return report.toXML();
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }



  @Override
  public String generateXmlTestStepValidationReport(String xmlMessageValidationReport,
      TestStepValidationReport report) throws ValidationReportException {
    try {
      String xml =
          ReportUtil.generateXmlTestStepValidationReport(xmlMessageValidationReport, report);
      return xml;
    } catch (RuntimeException e) {
      throw new ValidationReportException(e);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    }
  }

  @Override
  public String updateXmlTestValidationReportElement(TestStepValidationReport report)
      throws ValidationReportException {
    try {
      nu.xom.Element element = updateElement(report);
      return element.toXML();
    } catch (RuntimeException e) {
      throw new ValidationReportException(e);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    }
  }

  private nu.xom.Element updateElement(TestStepValidationReport result)
      throws ValidationReportException {
    try {
      Document report = ReportUtil.getDocument(result.getXml());
      nu.xom.Element rootElement = report.getRootElement();
      nu.xom.Elements headerElements = rootElement.getChildElements();
      nu.xom.Element headerElement = headerElements.get(0);
      nu.xom.Element cmtElement = headerElement.getChildElements().get(1);
      cmtElement.removeChildren();
      cmtElement.appendChild(result.getComments());
      if (headerElement.getAttribute("Result") != null) {
        headerElement.removeAttribute(headerElement.getAttribute("Result"));
      }
      headerElement.addAttribute(new Attribute("Result", ReportUtil.getTestCaseResult(result
          .getResult())));
      return report.getDocument().getRootElement();
    } catch (RuntimeException e) {
      throw new ValidationReportException(e);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    }
  }
}
