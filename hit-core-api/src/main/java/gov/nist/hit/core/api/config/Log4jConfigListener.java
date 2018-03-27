package gov.nist.hit.core.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author Harold Affo (NIST)
 */
@WebListener
public class Log4jConfigListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			Properties p = new Properties();
			String configPath = System.getenv("HIT_LOGGING_CONFIG");
			InputStream log4jFile = null;
			if (configPath != null) {
				log4jFile = new FileInputStream(new File(configPath));
			} else {
				log4jFile = Log4jConfigListener.class.getResourceAsStream("/app-log4j.properties");
			}
			p.load(log4jFile);
			String logDir = System.getenv("HIT_LOGGING_DIR");
			if (logDir == null) {
				logDir = sce.getServletContext().getRealPath("/logs");
			}

			File f = new File(logDir);
			if (!f.exists()) {
				f.mkdir();
			}
			p.put("log.dir", logDir);
			PropertyConfigurator.configure(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}

}
