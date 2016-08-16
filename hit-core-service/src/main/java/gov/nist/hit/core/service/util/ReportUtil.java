package gov.nist.hit.core.service.util;

import gov.nist.hit.core.domain.TestResult;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.service.exception.ValidationReportException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import com.ibm.icu.util.Calendar;

public class ReportUtil {
  public static String dateOfTest(Date date) throws ParseException, DatatypeConfigurationException {
    DateFormat df = new SimpleDateFormat("MM dd yyyy', 'HH:mm:ss.SSSXXX");
    return df.format(date);
  }

  public static nu.xom.Node getXmlElement(String fragment) throws IOException, SAXException,
      InstantiationException, IllegalAccessException, ValidityException, ParsingException {
    nu.xom.Document doc = getDocument(fragment);
    return doc.getRootElement().copy();
  }

  public static nu.xom.Document getDocument(String fragment) throws IOException, SAXException,
      InstantiationException, IllegalAccessException, ValidityException, ParsingException {
    Builder builder = new Builder();
    nu.xom.Document doc = builder.build(IOUtils.toInputStream(fragment));
    return doc;
  }

  public static nu.xom.Element generateTestStepValidationReportElement(
      String xmlMessageOrManualValidationReport, TestStepValidationReport result)
      throws ValidationReportException {
    try {
      nu.xom.Element report =
          new nu.xom.Element("teststepvalidationreport:TestStepValidationReport",
              "http://www.nist.gov/healthcare/validation/teststep/report");
      nu.xom.Element headerReport =
          new nu.xom.Element("teststepvalidationreport:TestStepValidationReportHeader",
              "http://www.nist.gov/healthcare/validation/teststep/report");
      report.appendChild(headerReport);
      nu.xom.Element type =
          new nu.xom.Element("teststepvalidationreport:Type",
              "http://www.nist.gov/healthcare/validation/teststep/report");
      type.appendChild(result.getTestStep() != null
          && result.getTestStep().getTestingType() != null ? result.getTestStep().getTestingType()
          .toString() : "");
      headerReport.appendChild(type);

      nu.xom.Element comments =
          new nu.xom.Element("teststepvalidationreport:Comments",
              "http://www.nist.gov/healthcare/validation/teststep/report");
      comments.appendChild(result.getComments());
      headerReport.appendChild(comments);
      headerReport.addAttribute(new Attribute("Result", getTestCaseResult(result.getResult())));
      nu.xom.Element name =
          new nu.xom.Element("teststepvalidationreport:Name",
              "http://www.nist.gov/healthcare/validation/teststep/report");
      name.appendChild(result.getTestStep() != null ? result.getTestStep().getName() : "");
      headerReport.appendChild(name);

      nu.xom.Element position =
          new nu.xom.Element("teststepvalidationreport:Position",
              "http://www.nist.gov/healthcare/validation/teststep/report");
      position.appendChild(result.getTestStep() != null ? result.getTestStep().getPosition() + ""
          : "");
      headerReport.appendChild(position);

      nu.xom.Element dateOfTest =
          new nu.xom.Element("teststepvalidationreport:DateOfTest",
              "http://www.nist.gov/healthcare/validation/teststep/report");
      dateOfTest.appendChild(ReportUtil.dateOfTest(Calendar.getInstance().getTime()));
      headerReport.appendChild(dateOfTest);

      nu.xom.Element reportContent =
          new nu.xom.Element("teststepvalidationreport:TestStepValidationReportBody",
              "http://www.nist.gov/healthcare/validation/teststep/report");
      report.appendChild(reportContent);
      if (StringUtils.isNotEmpty(xmlMessageOrManualValidationReport)) {
        xmlMessageOrManualValidationReport = removeNonPrintableCharacters(xmlMessageOrManualValidationReport,false);
        reportContent.appendChild(ReportUtil.getXmlElement(xmlMessageOrManualValidationReport));
      }
      return report;
    } catch (RuntimeException e) {
      throw new ValidationReportException(e);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    }
  }

    public static String removeNonPrintableCharacters(String message, boolean showHex){
        StringBuilder cleanedMessage = new StringBuilder();
        for(char c : message.toCharArray()){
            if(isPrintable(c))
                cleanedMessage.append(c);
            else {
                if(showHex){
                    String hex = String.format("%04x", (int) c);
                    cleanedMessage.append("[x");
                    cleanedMessage.append(hex);
                    cleanedMessage.append("]");
                }
            }
        }
        return cleanedMessage.toString();
    }

    public static boolean isPrintable(char c){
        int i = (int) c;
        return i == 10 || i < 0 || i > 31;
    }

  public static String generateXmlTestStepValidationReport(
      String xmlMessageOrManualValidationReport, TestStepValidationReport report)
      throws ValidationReportException {
    try {
      nu.xom.Element element =
          ReportUtil.generateTestStepValidationReportElement(xmlMessageOrManualValidationReport,
              report);
      return element.toXML();
    } catch (RuntimeException e) {
      throw new ValidationReportException(e);
    } catch (Exception e) {
      throw new ValidationReportException(e);
    }
  }



  public static String getTestCaseResult(TestResult result) throws ValidationReportException {
    return result != null ? result.getTitle() : "";
  }





}
