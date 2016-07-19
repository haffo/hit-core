package gov.nist.hit.core.service.unit;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import gov.nist.hit.core.domain.TestResult;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.TestingType;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.impl.TestStepValidationReportServiceImpl;

public class TestStepValidationReportServiceImplTest {

  TestStepValidationReportService service = new TestStepValidationReportServiceImpl();

  static final String outputsFolder = "src/test/resources/outputs";

  @Test
  public void testGenerateTestStepValidationHtml() throws Exception {
    String xmlMessageValidationReport =
        IOUtils.toString(TestStepValidationReportServiceImplTest.class
            .getResourceAsStream("/inputs/1-Message-ValidationReport.xml"));
    assertNotNull(xmlMessageValidationReport);

    TestStepValidationReport report = new TestStepValidationReport();
    TestStep ts = new TestStep();
    ts.setName("Record an adverse reaction");
    ts.setTestingType(TestingType.SUT_INITIATOR);
    ts.setPosition(3);
    report.setTestStep(ts);
    report.setComments("TestStep1 comments");
    report.setResult(TestResult.PASSED_NOTABLE_EXCEPTION);

    String xml = service.generateXmlTestStepValidationReport(xmlMessageValidationReport, report);
    assertNotNull(xml);
    saveToFile(new File(outputsFolder + "/5-TestStepMessageValidationReport.xml"), xml);

    InputStream io = IOUtils.toInputStream(service.generateHtml(xml));
    assertNotNull(io);
    saveToFile(new File(outputsFolder + "/5-TestStepMessageValidationReport.html"), io);

  }


  @Test
  public void testGenerateTestStepValidationPdf() throws Exception {
    String xmlMessageValidationReport =
        IOUtils.toString(TestStepValidationReportServiceImplTest.class
            .getResourceAsStream("/inputs/1-Message-ValidationReport.xml"));
    assertNotNull(xmlMessageValidationReport);

    TestStepValidationReport report = new TestStepValidationReport();
    TestStep ts = new TestStep();
    ts.setName("Record an adverse reaction");
    ts.setTestingType(TestingType.SUT_INITIATOR);
    ts.setPosition(3);
    report.setTestStep(ts);
    report.setComments("TestStep1 comments");
    report.setResult(TestResult.PASSED_NOTABLE_EXCEPTION);

    String xml = service.generateXmlTestStepValidationReport(xmlMessageValidationReport, report);
    assertNotNull(xml);
    saveToFile(new File(outputsFolder + "/1-TestStepMessageValidationReport.xml"), xml);


    InputStream io = service.generatePdf(xml);
    assertNotNull(io);
    saveToFile(new File(outputsFolder + "/1-TestStepMessageValidationReport.pdf"), io);

  }

  @Test
  public void testGenerateManualValidationPdf() throws Exception {
    TestStepValidationReport r3 = new TestStepValidationReport();
    TestStep t3 = new TestStep();
    t3.setName("Record an adverse reaction");
    t3.setTestingType(TestingType.SUT_MANUAL);
    t3.setPosition(3);
    r3.setTestStep(t3);
    r3.setComments("TestStep3 comments");
    r3.setResult(TestResult.PASSED_NOTABLE_EXCEPTION);
    String xml = service.generateXmlTestStepValidationReport(null, r3);

    assertNotNull(xml);
    saveToFile(new File(outputsFolder + "/2-TestStepManualValidationReport.xml"), xml);


    InputStream io = service.generatePdf(xml);
    assertNotNull(io);
    saveToFile(new File(outputsFolder + "/2-TestStepManualValidationReport.pdf"), io);

  }

  @Test
  public void testUpdateManualValidationPdf() throws Exception {
    TestStepValidationReport r3 = new TestStepValidationReport();
    TestStep t3 = new TestStep();
    t3.setName("Record an adverse reaction");
    t3.setTestingType(TestingType.SUT_MANUAL);
    t3.setPosition(3);
    r3.setTestStep(t3);
    r3.setComments("TestStep3 comments");
    r3.setResult(TestResult.PASSED_NOTABLE_EXCEPTION);
    String xml = service.generateXmlTestStepValidationReport(null, r3);
    assertNotNull(xml);
    saveToFile(new File(outputsFolder + "/3-TestStepMesssageValidationReport.xml"), xml);
    r3.setXml(xml);
    r3.setComments("Updated TestStep3 comments");
    r3.setResult(TestResult.FAILED_NOT_SUPPORTED);

    xml = service.updateXmlTestValidationReportElement(r3);
    assertNotNull(xml);

    InputStream io = service.generatePdf(xml);
    assertNotNull(io);
    saveToFile(new File(outputsFolder + "/3-TestStepMesssageValidationReport.pdf"), io);
  }

  @Test
  public void testUpdateMessageValidationPdf() throws Exception {
    String xmlMessageValidationReport =
        IOUtils.toString(TestStepValidationReportServiceImplTest.class
            .getResourceAsStream("/inputs/1-Message-ValidationReport.xml"));
    assertNotNull(xmlMessageValidationReport);
    TestStepValidationReport r3 = new TestStepValidationReport();
    TestStep t3 = new TestStep();
    t3.setName("Record an adverse reaction");
    t3.setTestingType(TestingType.SUT_INITIATOR);
    t3.setPosition(3);
    r3.setTestStep(t3);
    r3.setComments("TestStep3 comments");
    r3.setResult(TestResult.PASSED_NOTABLE_EXCEPTION);
    String xml = service.generateXmlTestStepValidationReport(xmlMessageValidationReport, r3);
    assertNotNull(xml);
    saveToFile(new File(outputsFolder + "/4-TestStepMesssageValidationReport.xml"), xml);
    r3.setXml(xml);
    r3.setComments("Updated TestStep4 comments");
    r3.setResult(TestResult.FAILED_NOT_SUPPORTED);

    xml = service.updateXmlTestValidationReportElement(r3);
    assertNotNull(xml);

    InputStream io = service.generatePdf(xml);
    assertNotNull(io);
    saveToFile(new File(outputsFolder + "/4-TestStepMesssageValidationReport.pdf"), io);
  }


  private void saveToFile(File f, String content) throws FileNotFoundException {
    // PrintWriter pw = new PrintWriter(f);
    // pw.append(content);
    // pw.close();
  }

  private void saveToFile(File f, InputStream content) throws IOException {
    // FileOutputStream out = new FileOutputStream(f);
    // FileCopyUtils.copy(content, out);
    // out.close();
  }



}
