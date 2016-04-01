package gov.nist.hit.core.service.unit;

import static org.junit.Assert.assertNotNull;
import gov.nist.hit.core.domain.TestResult;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.TestingType;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.impl.TestStepValidationReportServiceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

public class TestStepValidationReportServiceImplTest {

  TestStepValidationReportService service = new TestStepValidationReportServiceImpl();

  @Test
  public void testGenerateTestStepValidationHtml() throws Exception {
    String xmlMessageValidationReport =
        IOUtils.toString(TestStepValidationReportServiceImplTest.class
            .getResourceAsStream("/reports/1-Message-ValidationReport.xml"));
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
    File f = new File("src/test/resources/5-TestStepMessageValidationReport.xml");
    OutputStream os;
    os = new FileOutputStream(f);
    FileCopyUtils.copy(IOUtils.toInputStream(xml), os);
    os.close();

    InputStream io = IOUtils.toInputStream(service.generateHtml(xml));
    assertNotNull(io);
    f = new File("src/test/resources/5-TestStepMessageValidationReport.html");
    os = new FileOutputStream(f);
    FileCopyUtils.copy(io, os);
    os.close();
  }


  @Test
  public void testGenerateTestStepValidationPdf() throws Exception {
    String xmlMessageValidationReport =
        IOUtils.toString(TestStepValidationReportServiceImplTest.class
            .getResourceAsStream("/reports/1-Message-ValidationReport.xml"));
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
    File f = new File("src/test/resources/1-TestStepMessageValidationReport.xml");
    OutputStream os;
    os = new FileOutputStream(f);
    FileCopyUtils.copy(IOUtils.toInputStream(xml), os);
    os.close();

    InputStream io = service.generatePdf(xml);
    assertNotNull(io);
    f = new File("src/test/resources/1-TestStepMessageValidationReport.pdf");
    os = new FileOutputStream(f);
    FileCopyUtils.copy(io, os);
    os.close();
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
    File f = new File("src/test/resources/2-TestStepManualValidationReport.xml");
    OutputStream os;
    os = new FileOutputStream(f);
    FileCopyUtils.copy(IOUtils.toInputStream(xml), os);
    os.close();

    InputStream io = service.generatePdf(xml);
    assertNotNull(io);
    f = new File("src/test/resources/2-TestStepManualValidationReport.pdf");
    os = new FileOutputStream(f);
    FileCopyUtils.copy(io, os);
    os.close();
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
    File f = new File("src/test/resources/3-TestStepManualValidationReport.xml");
    OutputStream os;
    os = new FileOutputStream(f);
    FileCopyUtils.copy(IOUtils.toInputStream(xml), os);
    os.close();
    r3.setXml(xml);
    r3.setComments("Updated TestStep3 comments");
    r3.setResult(TestResult.FAILED_NOT_SUPPORTED);

    xml = service.updateXmlTestValidationReportElement(r3);
    assertNotNull(xml);

    InputStream io = service.generatePdf(xml);
    assertNotNull(io);

    f = new File("src/test/resources/3-TestStepManualValidationReport.pdf");
    os = new FileOutputStream(f);
    FileCopyUtils.copy(io, os);
    os.close();
  }

  @Test
  public void testUpdateMessageValidationPdf() throws Exception {
    String xmlMessageValidationReport =
        IOUtils.toString(TestStepValidationReportServiceImplTest.class
            .getResourceAsStream("/reports/1-Message-ValidationReport.xml"));
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
    File f = new File("src/test/resources/4-TestStepManualValidationReport.xml");
    OutputStream os;
    os = new FileOutputStream(f);
    FileCopyUtils.copy(IOUtils.toInputStream(xml), os);
    os.close();
    r3.setXml(xml);
    r3.setComments("Updated TestStep4 comments");
    r3.setResult(TestResult.FAILED_NOT_SUPPORTED);

    xml = service.updateXmlTestValidationReportElement(r3);
    assertNotNull(xml);

    InputStream io = service.generatePdf(xml);
    assertNotNull(io);

    f = new File("src/test/resources/4-TestStepMesssageValidationReport.pdf");
    os = new FileOutputStream(f);
    FileCopyUtils.copy(io, os);
    os.close();
  }



}
