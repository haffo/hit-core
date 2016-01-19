package gov.nist.hit.core.service.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.hibernate.jpa.boot.spi.Bootstrap;
import org.springframework.core.io.Resource;

public class FileUtil {

  public static String getContent(String path) {
    try {
      return IOUtils.toString(new BOMInputStream(Bootstrap.class.getResourceAsStream(path)));
    } catch (RuntimeException e) {
      return null;
    } catch (Exception e) {
      return null;
    }
  }


  public static byte[] getByteArray(String path) throws IOException {
    return getByteArray(FileUtil.class.getResourceAsStream(path));
  }

  public static byte[] getByteArray(InputStream io) throws IOException {
    return IOUtils.toByteArray(io);
  }

  public static String getContent(Resource resource) {
    try {
      if (resource != null)
        return IOUtils.toString(resource.getInputStream());
      return null;
    } catch (RuntimeException e) {
      return null;
    } catch (Exception e) {
      return null;
    }

  }


}
