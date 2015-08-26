package gov.nist.hit.core.service.unit;

import gov.nist.hit.core.service.util.PdfGeneratorUtil;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class PdfGeneratorUtilTest {

  @Test
  public void testGenerate() throws Exception {
    String html =
        IOUtils.toString(PdfGeneratorUtil.class.getResourceAsStream("/JurorDocument.html"));
    InputStream io = PdfGeneratorUtil.genereate("JurorDocument", "UnitTest", html);
    File f = new File("JurorDocument.pdf");
    System.out.println(f.getAbsolutePath());
    PrintWriter pw = new PrintWriter(f);
    pw.print(IOUtils.toString(io));
    pw.close();
  }

}
