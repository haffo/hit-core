package gov.nist.hit.core.service.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.hibernate.jpa.boot.spi.Bootstrap;

public class FileUtil {

	public static String getContent(String path) {
		try {
			return IOUtils.toString(new BOMInputStream(Bootstrap.class
					.getResourceAsStream(path)));
		} catch (RuntimeException e) {
			return null;
		} catch (Exception e) {
			return null;
		}

	}

	public static byte[] getByteArray(String path) throws IOException {
		return IOUtils.toByteArray(FileUtil.class.getResourceAsStream(path));
	}
}
