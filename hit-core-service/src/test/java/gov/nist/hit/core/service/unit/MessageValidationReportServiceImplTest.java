package gov.nist.hit.core.service.unit;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import gov.nist.hit.core.service.MessageValidationReportService;
import gov.nist.hit.core.service.impl.MessageValidationReportServiceImpl;

public class MessageValidationReportServiceImplTest {

  MessageValidationReportService service = new MessageValidationReportServiceImpl();
  static final String outputsFolder = "src/test/resources/outputs";

  @Test
  public void testGenerateMessageValidationHtml() throws Exception {
    String xmlMessageValidationReport =
        IOUtils.toString(MessageValidationReportServiceImplTest.class
            .getResourceAsStream("/inputs/1-Message-ValidationReport.xml"));
    assertNotNull(xmlMessageValidationReport);
    InputStream io = IOUtils.toInputStream(service.generateHtml(xmlMessageValidationReport));
    assertNotNull(io);
  }

  @Test
  public void testGenerateMessageValidationPdf() throws Exception {
    String xmlMessageValidationReport =
        IOUtils.toString(MessageValidationReportServiceImplTest.class
            .getResourceAsStream("/inputs/1-Message-ValidationReport.xml"));
    assertNotNull(xmlMessageValidationReport);
    InputStream io = service.generatePdf(xmlMessageValidationReport);
    assertNotNull(io);
    saveToFile(new File(outputsFolder + "/1-Message-ValidationReport.pdf"), io);
  }


  private void saveToFile(File f, String content) throws FileNotFoundException {
    // PrintWriter pw = new PrintWriter(f);
    // pw.append(content);
    // pw.close();
  }

  private void saveToFile(File f, InputStream content) throws IOException {
    // FileOutputStream out = new FileOutputStream(f);
    // FileCopyUtils.copy(content, out);
    // out.close();
  }
}
