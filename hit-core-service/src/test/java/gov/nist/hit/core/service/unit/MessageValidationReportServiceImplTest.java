package gov.nist.hit.core.service.unit;

import static org.junit.Assert.assertNotNull;
import gov.nist.hit.core.service.MessageValidationReportService;
import gov.nist.hit.core.service.impl.MessageValidationReportServiceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

public class MessageValidationReportServiceImplTest {

  MessageValidationReportService service = new MessageValidationReportServiceImpl();

  @Test
  public void testGenerateMessageValidationHtml() throws Exception {
    String xmlMessageValidationReport =
        IOUtils.toString(MessageValidationReportServiceImplTest.class
            .getResourceAsStream("/reports/1-Message-ValidationReport.xml"));
    assertNotNull(xmlMessageValidationReport);
    InputStream io = IOUtils.toInputStream(service.generateHtml(xmlMessageValidationReport));
    assertNotNull(io);
    File f = new File("src/test/resources/6-MessageValidationReport.html");
    FileOutputStream os = new FileOutputStream(f);
    FileCopyUtils.copy(io, os);
    os.close();
  }



}
