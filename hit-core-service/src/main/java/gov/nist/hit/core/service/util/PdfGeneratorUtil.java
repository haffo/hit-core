package gov.nist.hit.core.service.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.apache.commons.io.FileUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class PdfGeneratorUtil {


  public static InputStream genereate(String type, String name, String html) throws IOException,
      DocumentException {

    if (html != null) {
      // com.itextpdf.text.Document document =
      // new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4);
      //
      // File tmpPdfFile = File.createTempFile(type, ".pdf");

      // FileOutputStream fos = new FileOutputStream(tmpPdfFile);
      // com.itextpdf.text.pdf.PdfWriter pdfWriter =
      // com.itextpdf.text.pdf.PdfWriter.getInstance(document, fos);
      // document.open();
      // document.addAuthor("NIST");
      // document.addSubject(name);
      // document.addCreationDate();
      // document.addTitle(name);
      // HTMLWorker htmlWorker = new HTMLWorker(document);
      // htmlWorker.parse(new StringReader(html));
      // document.close();

      // File tmpHtmlFile = File.createTempFile(type, ".html");
      // FileUtils.writeStringToFile(tmpHtmlFile, html);
      com.itextpdf.text.Document document = new com.itextpdf.text.Document();
      File tmpPdfFile = File.createTempFile(type, ".pdf");
      PdfWriter writer = PdfWriter.getInstance(document, FileUtils.openOutputStream(tmpPdfFile));
      document.open();
      XMLWorkerHelper helper = XMLWorkerHelper.getInstance();
      // html = Jsoup.parse(html, "US-ASCII").html();
      helper.parseXHtml(writer, document, new StringReader(html));
      document.close();

      return FileUtils.openInputStream(tmpPdfFile);

    }
    return null;

  }



}
