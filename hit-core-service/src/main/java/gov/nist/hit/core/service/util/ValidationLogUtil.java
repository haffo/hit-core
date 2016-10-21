package gov.nist.hit.core.service.util;

import gov.nist.healthcare.unified.model.EnhancedReport;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.service.impl.ValidationLogReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Map;

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
public class ValidationLogUtil {

    @Autowired
    private static Environment env;

    private static String DEFAULT_LOG_FORMAT = "%date [Validation] %format - %messageId [%errorCount errors (%errorsInSegments), %warningCount warnings]";
    private static String DEFAULT_LOG_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static String generateValidationLog(TestContext testContext, EnhancedReport report){
        return generateValidationLog(new ValidationLogReport(testContext,report));
    }

    public static String generateValidationLog(ValidationLogReport validationLogReport) {
        //Read environment properties related to the validation logs
        String logFormat = (env.getProperty("validation.logs.format").equals("")?env.getProperty("validation.logs.format"):DEFAULT_LOG_FORMAT);
        String logDateFormat = (env.getProperty("validation.logs.date.format").equals("")?env.getProperty("validation.logs.date.format"):DEFAULT_LOG_DATE_FORMAT);
        //Replace the fields in log
        if (logFormat.contains("%date")) {
            logFormat.replace("%date", validationLogReport.getDate(logDateFormat));
        }
        if (logFormat.contains("%format")) {
            logFormat.replace("%format", validationLogReport.getFormat());
        }
        if (logFormat.contains("%messageId")) {
            logFormat.replace("%messageId", validationLogReport.getMessageId());
        }
        if (logFormat.contains("%errorCount")) {
            logFormat.replace("%errorCount", String.valueOf(validationLogReport.getErrorCount()));
        }
        if (logFormat.contains("%errorsInSegments")) {
            //Generate the segment errors log
            //TODO Make the format a parameter?
            if (validationLogReport.getErrorCountInSegment().size() > 0) {
                StringBuilder errorsInSegments = new StringBuilder();
                errorsInSegments.append("[");
                boolean isFirst = true;
                for (Map.Entry<String, Integer> segment : validationLogReport.getErrorCountInSegment().entrySet()) {
                    if(!isFirst){
                        errorsInSegments.append(", ");
                    }
                    isFirst = false;
                    errorsInSegments.append(segment.getKey());
                    errorsInSegments.append(" (");
                    errorsInSegments.append(segment.getValue());
                    errorsInSegments.append(" errors)");
                }
                errorsInSegments.append("]");
                logFormat.replace("%errorsInSegments", errorsInSegments.toString());
            } else {
                logFormat.replace("%errorsInSegments", "No errors");
            }
        }
        if (logFormat.contains("%warningCount")) {
            logFormat.replace("%warningCount", String.valueOf(validationLogReport.getWarningCount()));
        }
        //return the populated log
        return logFormat;
    }

}
