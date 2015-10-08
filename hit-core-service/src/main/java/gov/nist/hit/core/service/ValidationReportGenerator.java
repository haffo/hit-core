/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.service;

import gov.nist.hit.core.service.exception.ValidationReportException;
import gov.nist.hit.core.service.util.HtmlUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * @author Harold Affo (NIST)
 */
public abstract class ValidationReportGenerator {

  private final static Logger logger = Logger.getLogger(ValidationReportGenerator.class);

  /**
   * convert xml to pdf
   * 
   * @param xmlReport
   * @return
   */
  public InputStream toPDF(String xml) throws ValidationReportException {
    try {
      String xhtml = toXHTML(xml).replaceAll("<br>", "<br/>");
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

  /**
   * convert xml to html report
   * 
   * @param validationReport
   * @return
   * @throws Exception
   */
  public String toHTML(String xml) throws ValidationReportException {
    try {
      Transformer transformer =
          TransformerFactory.newInstance().newTransformer(
              new StreamSource(new StringReader(getHtmlConversionXslt())));
      StreamSource source = new StreamSource(new StringReader(xml));
      ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
      StreamResult result = new StreamResult(resultStream);
      transformer.transform(source, result);
      String htmlReport = HtmlUtil.repairStyle(new String(resultStream.toByteArray()));
      logger.info("HTML validation report generated");
      return addStyleSheet(htmlReport);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }

  /**
   * convert xml to xhtml report
   * 
   * @param xmlReport
   * @return
   * @throws Exception
   */
  public String toXHTML(String xml) throws ValidationReportException {
    try {
      StringBuffer bf = new StringBuffer();
      bf.append("<?xml version='1.0' encoding='UTF-8'?>");
      bf.append(xml);
      Transformer transformer =
          TransformerFactory.newInstance().newTransformer(
              new StreamSource(new StringReader(getPdfConversionXslt())));
      StreamSource source = new StreamSource(new StringReader(bf.toString()));
      ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
      StreamResult result = new StreamResult(resultStream);
      transformer.transform(source, result);
      String html = HtmlUtil.repairStyle(new String(resultStream.toByteArray()));
      logger.info("XHTML validation report generated");
      return addStyleSheet(html);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new ValidationReportException(e.getMessage());
    }
  }

  /**
   * @param htmlReport
   * @return
   */
  public abstract String addStyleSheet(String htmlReport);

  /**
   * return the xstl path for the conversion of xml to pdf
   * 
   * @return
   */
  public abstract String getPdfConversionXslt();

  /**
   * return the xstl path for the conversion of xml to html
   * 
   * @return
   */
  public abstract String getHtmlConversionXslt();

}
