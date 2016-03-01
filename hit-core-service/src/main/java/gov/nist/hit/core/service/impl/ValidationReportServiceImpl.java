package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.ManualValidationResult;
import gov.nist.hit.core.domain.ManualValidationResultType;
import gov.nist.hit.core.domain.ValidationReport;
import gov.nist.hit.core.repo.ValidationReportRepository;
import gov.nist.hit.core.service.ValidationReportService;
import gov.nist.hit.core.service.exception.ValidationReportException;
import gov.nist.hit.core.service.util.HtmlUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import nu.xom.Attribute;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.ibm.icu.util.Calendar;

@Service
public class ValidationReportServiceImpl implements ValidationReportService {

  private final static Logger logger = Logger.getLogger(ValidationReportServiceImpl.class);
  private static final String HTML_XSL = "/report/report-html.xsl";
  private static final String PDF_XSL = "/report/report-pdf.xsl";
  protected static final String CSS = "/report.css";
  protected String css = "";


  @Autowired
  protected ValidationReportRepository validationReportRepository;


  public ValidationReportServiceImpl() {
    try {
      css = IOUtils.toString(ValidationReportServiceImpl.class.getResourceAsStream(CSS));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public void delete(ValidationReport report) {
    validationReportRepository.delete(report);
  }

  @Override
  public ValidationReport save(ValidationReport report) {
    return validationReportRepository.saveAndFlush(report);
  }


  @Override
  public void delete(Long id) {
    validationReportRepository.delete(id);
  }

  @Override
  public ValidationReport findOneByIdAndUser(Long reportId, Long userId) {
    return validationReportRepository.findOneByIdAndUser(reportId, userId);
  }

  @Override
  public void delete(List<ValidationReport> reports) {
    validationReportRepository.delete(reports);
  }

  @Override
  public void save(List<ValidationReport> reports) {
    validationReportRepository.save(reports);
  }

  @Override
  public ValidationReport findOneByTestStepAndUser(Long testStepId, Long userId) {
    return validationReportRepository.findOneByTestStepAndUser(userId, testStepId);
  }

  @Override
  public List<ValidationReport> findAllByTestCaseAndUser(Long testCaseId, Long userId) {
    return validationReportRepository.findAllByTestCaseAndUser(userId, testCaseId);
  }

  @Override
  public List<ValidationReport> findAllByUser(Long userId) {
    return validationReportRepository.findAllByUser(userId);
  }

  @Override
  public ValidationReport findOne(Long id) {
    return validationReportRepository.findOne(id);
  }



  @Override
  public String toAutoHTML(String xml) throws ValidationReportException {
    try {
      Transformer transformer =
          TransformerFactory.newInstance().newTransformer(
              new StreamSource(new StringReader(IOUtils.toString(ValidationReportServiceImpl.class
                  .getResourceAsStream(getHtmlCss())))));
      StreamSource source = new StreamSource(new StringReader(xml));
      ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
      StreamResult result = new StreamResult(resultStream);
      transformer.transform(source, result);
      String htmlReport = HtmlUtil.repairStyle(new String(resultStream.toByteArray()));
      logger.info("HTML validation report generated");
      return addCss(htmlReport);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }


  @Override
  public String toAutoXHTML(String xml) throws ValidationReportException {
    try {
      Transformer transformer =
          TransformerFactory.newInstance().newTransformer(
              new StreamSource(new StringReader(IOUtils.toString(ValidationReportServiceImpl.class
                  .getResourceAsStream(getPdfCss())))));
      StreamSource source = new StreamSource(new StringReader(xml));
      ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
      StreamResult result = new StreamResult(resultStream);
      transformer.transform(source, result);
      String html = HtmlUtil.repairStyle(new String(resultStream.toByteArray()));
      logger.info("XHTML validation report generated");
      return addCss(html);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }

  @Override
  public InputStream toAutoPDF(String xml) throws ValidationReportException {
    try {
      String xhtml = toAutoXHTML(xml).replaceAll("<br>", "<br/>");
      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(xhtml);
      renderer.layout();
      File temp = File.createTempFile("MessageValidationReport", ".pdf");
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


  @Override
  public InputStream zipReports(String folderName, HashMap<String, InputStream> reports)
      throws ValidationReportException {
    try {
      if (reports != null) {
        Path path = Files.createTempDirectory(null);
        File rootFolder = path.toFile();
        if (!rootFolder.exists()) {
          rootFolder.mkdir();
        }
        String folderToZipName = rootFolder.getAbsolutePath() + File.separator + folderName;
        File folderToZipFile = new File(folderToZipName);
        if (!folderToZipFile.exists()) {
          folderToZipFile.mkdir();
        }

        for (Entry<String, InputStream> entry : reports.entrySet()) {
          createFile(entry.getValue(), entry.getKey(), folderToZipName);
        }
        String zipFilename = rootFolder.getAbsolutePath() + File.separator + folderName + ".zip";
        zipDir(zipFilename, folderToZipName);
        FileInputStream io = new FileInputStream(new File(zipFilename));
        return io;
      }
    } catch (IOException e) {
      throw new ValidationReportException(e);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    }
    return null;
  }

  private void zipDir(String zipFileName, String dir) throws Exception {
    File dirObj = new File(dir);
    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
    addFile(dirObj, out, dir);
    out.close();
  }

  private void addFile(File dirObj, ZipOutputStream out, String folderToZipPath) throws IOException {
    File[] files = dirObj.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        addFile(files[i], out, folderToZipPath);
        continue;
      }
      String absolutePath = files[i].getAbsolutePath();
      FileInputStream in = new FileInputStream(absolutePath);
      String localPath =
          absolutePath.substring(absolutePath.indexOf(folderToZipPath) + folderToZipPath.length()
              + 1);
      ZipEntry zipEntry = new ZipEntry(localPath);
      out.putNextEntry(zipEntry);
      out.write(IOUtils.toByteArray(in));
      out.closeEntry();
    }
  }


  private void createFile(InputStream report, String fileName, String folderToZip)
      throws IOException {
    String filename = folderToZip + File.separator + fileName;
    File file = new File(filename);
    if (!file.exists()) {
      file.createNewFile();
    }
    FileUtils.copyInputStreamToFile(report, file);
  }

  public String addCss(String htmlReport) throws IOException {
    StringBuffer sb = new StringBuffer();
    sb.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
    sb.append("<head>");
    sb.append("<title>Message Validation Report</title>");
    sb.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
    sb.append("<style>");
    sb.append(css);
    sb.append("</style></head><body>");
    sb.append(htmlReport);
    sb.append("</body></html>");
    return sb.toString();
  }

  public String getHtmlCss() {
    return HTML_XSL;
  }

  public String getPdfCss() {
    return PDF_XSL;
  }


  @Override
  public List<ValidationReport> findAllByTestStepAndUser(Long testStepId, Long userId) {
    return validationReportRepository.findAllByTestStepAndUser(userId, testStepId);
  }


  private static final String MANUAL_HTML_XSL = "/report/manual-report-html.xsl";
  private static final String MANUAL_PDF_XSL = "/report/manual-report-pdf.xsl";


  @Override
  public InputStream toManualPDF(String xml) throws ValidationReportException {
    try {
      String xhtml = toManualXHTML(xml).replaceAll("<br>", "<br/>");
      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(xhtml);
      renderer.layout();
      File temp = File.createTempFile("ManualValidationReport", ".pdf");
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

  @Override
  public String toManualXML(ManualValidationResult validationResult)
      throws ValidationReportException {
    try {
      nu.xom.Element report = new nu.xom.Element("ManualValidationReport");
      nu.xom.Element headerReport = new nu.xom.Element("HeaderReport");
      report.appendChild(headerReport);

      nu.xom.Element serviceProvider = new nu.xom.Element("ServiceProvider");
      serviceProvider.appendChild("NIST");
      headerReport.appendChild(serviceProvider);

      nu.xom.Element validationType = new nu.xom.Element("ValidationType");
      validationType.appendChild("Context-Based");
      headerReport.appendChild(validationType);

      nu.xom.Element type = new nu.xom.Element("Type");
      type.appendChild("Manual");
      headerReport.appendChild(type);

      nu.xom.Element dateOfTest = new nu.xom.Element("DateOfTest");
      dateOfTest.appendChild(dateOfTest(validationResult.getDate() != null ? validationResult
          .getDate() : Calendar.getInstance().getTime()));
      headerReport.appendChild(dateOfTest);


      Map<String, String> nav = validationResult.getNav();
      if (nav != null && !nav.isEmpty()) {
        nu.xom.Element testCaseReference = new nu.xom.Element("TestCaseReference");
        report.appendChild(testCaseReference);

        nu.xom.Element testCase = new nu.xom.Element("TestCase");
        testCase.appendChild(nav.get("testCase"));
        testCaseReference.appendChild(testCase);

        nu.xom.Element testPlan = new nu.xom.Element("TestPlan");
        testPlan.appendChild(nav.get("testPlan"));
        testCaseReference.appendChild(testPlan);

        nu.xom.Element testStep = new nu.xom.Element("TestStep");
        testStep.appendChild(nav.get("testStep"));
        testCaseReference.appendChild(testStep);

        nu.xom.Element testGroup = new nu.xom.Element("TestGroup");
        testGroup.appendChild(nav.get("testGroup"));
        testCaseReference.appendChild(testGroup);
      }

      nu.xom.Element specificReport = new nu.xom.Element("SpecificReport");
      report.appendChild(specificReport);
      specificReport.addAttribute(new Attribute("Result", ManualValidationResultType.valueOf(
          validationResult.getValue()).value()));
      nu.xom.Element comments = new nu.xom.Element("Comments");
      specificReport.appendChild(comments);
      comments.appendChild(validationResult.getComments());
      return report.toXML();
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }

  private String dateOfTest(Date date) throws ParseException, DatatypeConfigurationException {
    DateFormat df = new SimpleDateFormat("MM dd yyyy', 'HH:mm:ss.SSSXXX");
    return df.format(date);
  }


  public String addManualCss(String htmlReport) throws IOException {
    StringBuffer sb = new StringBuffer();
    sb.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
    sb.append("<head>");
    sb.append("<title>Manual Validation Report</title>");
    sb.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
    sb.append("<style>");
    sb.append(css);
    sb.append("</style></head><body>");
    sb.append(htmlReport);
    sb.append("</body></html>");
    return sb.toString();
  }



  @Override
  public String toManualHTML(String xml) throws ValidationReportException {
    try {
      Transformer transformer =
          TransformerFactory.newInstance().newTransformer(
              new StreamSource(new StringReader(IOUtils.toString(ValidationReportServiceImpl.class
                  .getResourceAsStream(MANUAL_HTML_XSL)))));
      StreamSource source = new StreamSource(new StringReader(xml));
      ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
      StreamResult result = new StreamResult(resultStream);
      transformer.transform(source, result);
      String htmlReport = HtmlUtil.repairStyle(new String(resultStream.toByteArray()));
      logger.info("HTML validation report generated");
      return addManualCss(htmlReport);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }


  @Override
  public String toManualXHTML(String xml) throws ValidationReportException {
    try {
      Transformer transformer =
          TransformerFactory.newInstance().newTransformer(
              new StreamSource(new StringReader(IOUtils.toString(ValidationReportServiceImpl.class
                  .getResourceAsStream(MANUAL_PDF_XSL)))));
      StreamSource source = new StreamSource(new StringReader(xml));
      ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
      StreamResult result = new StreamResult(resultStream);
      transformer.transform(source, result);
      String html = HtmlUtil.repairStyle(new String(resultStream.toByteArray()));
      logger.info("XHTML validation report generated");
      return addManualCss(html);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }



}
