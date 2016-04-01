package gov.nist.hit.core.service.unit;

import static org.junit.Assert.assertNotNull;
import gov.nist.hit.core.service.MessageValidationReportService;
import gov.nist.hit.core.service.impl.MessageValidationReportServiceImpl;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

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
  }

  @Test
  public void testGenerateMessageValidationPdf() throws Exception {
    String xmlMessageValidationReport =
        IOUtils.toString(MessageValidationReportServiceImplTest.class
            .getResourceAsStream("/reports/1-Message-ValidationReport.xml"));
    assertNotNull(xmlMessageValidationReport);
    InputStream io = service.generatePdf(xmlMessageValidationReport);
    assertNotNull(io);
  }



}
