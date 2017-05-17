package gov.nist.hit.core.service.util;

import java.util.Map;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import gov.nist.healthcare.unified.model.EnhancedReport;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.service.impl.ValidationLogReport;

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
 * <p/>
 * Created by Maxence Lefort on 6/9/16.
 */
@Configuration
@ComponentScan(basePackages = "gov.nist.*")
@PropertySource(value = {"classpath:app-config.properties"})
public class ValidationLogUtil implements EnvironmentAware {

  public static Environment environment;

  private static String DEFAULT_LOG_FORMAT =
      "%date [Validation - %testingStage] %format - %messageId %validationResult [%errorCount errors %errorsInSegments, %warningCount warnings]";
  private static String DEFAULT_LOG_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

  @Override
  public void setEnvironment(final Environment environment) {
    this.environment = environment;
  }

  public static String generateValidationLog(TestContext testContext, EnhancedReport report) {
    return generateValidationLog(new ValidationLogReport(testContext, report));
  }

  public static String generateValidationLog(ValidationLogReport validationLogReport) {
    // Read environment properties related to the validation logs
    String logFormat =
        (environment == null || environment.getProperty("validation.logs.format") == null
            || "".equals(environment.getProperty("validation.logs.format")) ? DEFAULT_LOG_FORMAT
                : environment.getProperty("validation.logs.format"));
    String logDateFormat =
        (environment == null || environment.getProperty("validation.logs.date.format") == null
            || "".equals(environment.getProperty("validation.logs.date.format"))
                ? DEFAULT_LOG_DATE_FORMAT : environment.getProperty("validation.logs.date.format"));
    // Replace the fields in log
    if (logFormat.contains("%date")) {
      logFormat = logFormat.replace("%date", validationLogReport.getDate(logDateFormat));
    }
    if (logFormat.contains("%testingStage")) {
      logFormat = logFormat.replace("%testingStage", validationLogReport.getTestingStage());
    }
    if (logFormat.contains("%format")) {
      logFormat = logFormat.replace("%format", validationLogReport.getFormat());
    }
    if (logFormat.contains("%messageId")) {
      logFormat = logFormat.replace("%messageId", validationLogReport.getMessageId());
    }
    if (logFormat.contains("%errorCount")) {
      logFormat =
          logFormat.replace("%errorCount", String.valueOf(validationLogReport.getErrorCount()));
    }
    if (logFormat.contains("%errorsInSegments")) {
      // Generate the segment errors log
      // TODO Make the format a parameter?
      if (validationLogReport.getErrorCountInSegment().size() > 0) {
        StringBuilder errorsInSegments = new StringBuilder();
        errorsInSegments.append("(");
        boolean isFirst = true;
        for (Map.Entry<String, Integer> segment : validationLogReport.getErrorCountInSegment()
            .entrySet()) {
          if (!isFirst) {
            errorsInSegments.append(", ");
          }
          isFirst = false;
          errorsInSegments.append(segment.getKey());
          errorsInSegments.append(" (");
          errorsInSegments.append(segment.getValue());
          errorsInSegments.append(" errors)");
        }
        errorsInSegments.append(")");
        logFormat = logFormat.replace("%errorsInSegments", errorsInSegments.toString());
      } else {
        logFormat = logFormat.replace("%errorsInSegments", "");
      }
    }
    if (logFormat.contains("%warningCount")) {
      logFormat =
          logFormat.replace("%warningCount", String.valueOf(validationLogReport.getWarningCount()));
    }
    if (logFormat.contains("%validationResult")) {
      if (validationLogReport.isValidationResult()) {
        logFormat = logFormat.replace("%validationResult", "SUCCESS");
      } else {
        logFormat = logFormat.replace("%validationResult", "FAILURE");
      }
    }
    // return the populated log
    return logFormat;
  }

}
