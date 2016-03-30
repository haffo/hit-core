package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.repo.TestStepValidationReportRepository;
import gov.nist.hit.core.service.MessageValidationReportService;
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
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class MessageValidationReportServiceImpl implements MessageValidationReportService {

  private final static Logger logger = Logger.getLogger(MessageValidationReportServiceImpl.class);
  private static final String HTML_XSL = "/report/message-validation-report-html.xsl";
  private static final String PDF_XSL = "/report/message-validation-report-pdf.xsl";

  protected static final String CSS = "/report/report.css";
  protected static final String JS = "/report/report.js";

  protected String css = "";
  protected String javascript = "";

  @Autowired
  protected TestStepValidationReportRepository validationReportRepository;



  public MessageValidationReportServiceImpl() {
    try {
      css = IOUtils.toString(MessageValidationReportServiceImpl.class.getResourceAsStream(CSS));
      javascript =
          IOUtils.toString(TestStepValidationReportServiceImpl.class.getResourceAsStream(JS));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public void delete(TestStepValidationReport report) {
    validationReportRepository.delete(report);
  }

  @Override
  public TestStepValidationReport save(TestStepValidationReport report) {
    return validationReportRepository.saveAndFlush(report);
  }


  @Override
  public void delete(Long id) {
    validationReportRepository.delete(id);
  }

  @Override
  public TestStepValidationReport findOneByIdAndUser(Long reportId, Long userId) {
    return validationReportRepository.findOneByIdAndUser(reportId, userId);
  }

  @Override
  public void delete(List<TestStepValidationReport> reports) {
    validationReportRepository.delete(reports);
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
      logger.info("HTML validation report generated");
      return addCssAndJs(htmlReport);
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
      logger.info("XHTML validation report generated");
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


  public String addCssAndJs(String htmlReport) throws IOException {
    StringBuffer sb = new StringBuffer();
    sb.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
    sb.append("<head>");
    sb.append("<title>Message Validation Report</title>");
    sb.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
    sb.append("<style type='text/css'>");
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

  public String addCss(String htmlReport) throws IOException {
    StringBuffer sb = new StringBuffer();
    sb.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
    sb.append("<head>");
    sb.append("<title>Message Validation Report</title>");
    sb.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
    sb.append("<style type='text/css'>");
    sb.append(css);
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



}
