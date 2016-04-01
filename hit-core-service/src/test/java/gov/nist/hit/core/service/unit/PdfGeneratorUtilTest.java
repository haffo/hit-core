package gov.nist.hit.core.service.unit;

import gov.nist.hit.core.service.util.PdfGeneratorUtil;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class PdfGeneratorUtilTest {

  @Test
  public void testGenerate() throws Exception {
    String html =
        IOUtils.toString(PdfGeneratorUtil.class.getResourceAsStream("/JurorDocument.html"));
    InputStream io = PdfGeneratorUtil.genereate("JurorDocument", "UnitTest", html);
  }

}
