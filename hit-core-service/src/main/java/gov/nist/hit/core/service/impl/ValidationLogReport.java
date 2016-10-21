package gov.nist.hit.core.service.impl;


import gov.nist.healthcare.unified.exceptions.ConversionException;
import gov.nist.healthcare.unified.exceptions.NotFoundException;
import gov.nist.healthcare.unified.model.*;
import gov.nist.hit.core.domain.TestContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
 *
 * Created by mcl1 on 10/21/16.
 *
 * The ValidationLogReport is a minimalist version of the EnhancedReport that contains
 * only the information we need to display the validation logs.
 *
 */
public class ValidationLogReport {

    private String messageId;
    private String format;
    private int errorCount = 0;
    private int warningCount = 0;
    private Date date;
    private Map<String, Integer> errorCountInSegment;
    private boolean validationResult = true;

    /*
     * This constructor parses the EnhancedReport to take only the information we need from it.
     */
    public ValidationLogReport(TestContext testContext, EnhancedReport report) {
        this.date = new Date();
        Detections detections = report.getDetections();
        //Loop on the classifications (Affirmative, Warning or Error)
        for (Classification classification : detections.classes()) {
            if (classification.getName().equals("Affirmative")) {
                //No need to display any log here
            } else if (classification.getName().equals("Warning")) {
                //Get the warning count
                this.warningCount = classification.keys().size();
            } else if (classification.getName().equals("Error")) {
                //Get the error count
                this.errorCount = classification.keys().size();
                if (this.errorCount > 0) {
                    //If there is more than 0 errors, then the validation failed
                    this.validationResult = false;
                    this.errorCountInSegment = new HashMap<>();
                    //Loop on the errors
                    for (String key : classification.keys()) {
                        Collection collection = null;
                        try {
                            collection = classification.getArray(key);
                            if(collection!=null && collection.size()>0) {
                                for (int i = 0; i < collection.size(); i++) {
                                    Section section = collection.getObject(i);
                                    //Identify the path of the error
                                    String path = section.getString("path");
                                    if (path != null && !"".equals(path)) {
                                        path = path.split("\\[")[0];
                                        int segmentErrorCount = 1;
                                        //If there was already at least 1 error for this segment, then increment its error count.
                                        if (this.errorCountInSegment.containsKey(path)) {
                                            segmentErrorCount = this.errorCountInSegment.get(path) + 1;
                                        }
                                        //Add or update the segment's error count
                                        this.errorCountInSegment.put(path, segmentErrorCount);
                                    }
                                }
                            }
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        } catch (ConversionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        //Parse the test context
        if(testContext != null){
            this.format = (testContext.getFormat()!=null?"":testContext.getFormat());
            this.messageId = (testContext.getType()!=null?"":testContext.getType());
        }
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(int warningCount) {
        this.warningCount = warningCount;
    }

    public String getDate(String format) {
        //Format the date as specified
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        if(this.date==null){
            this.date = new Date();
        }
        return simpleDateFormat.format(this.date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, Integer> getErrorCountInSegment() {
        return errorCountInSegment;
    }

    public void setErrorCountInSegment(Map<String, Integer> errorCountInSegment) {
        this.errorCountInSegment = errorCountInSegment;
    }

    public boolean isValidationResult() {
        return validationResult;
    }

    public void setValidationResult(boolean validationResult) {
        this.validationResult = validationResult;
    }
}
