package gov.nist.hit.core.service.unit;

import static org.junit.Assert.assertNotNull;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseValidationResult;
import gov.nist.hit.core.domain.TestResult;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.TestingType;
import gov.nist.hit.core.service.TestCaseValidationReportService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.impl.TestCaseValidationReportServiceImpl;
import gov.nist.hit.core.service.impl.TestStepValidationReportServiceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

public class TestCaseValidationReportServiceImplTest {

  TestCaseValidationReportService service = new TestCaseValidationReportServiceImpl();
  TestStepValidationReportService testStepService = new TestStepValidationReportServiceImpl();



  private TestCaseValidationResult get() throws Exception {
    TestCaseValidationResult result = new TestCaseValidationResult();
    List<TestStepValidationReport> testStepReports = new ArrayList<TestStepValidationReport>();

    result.getNav().put("testCase", "TestCase-Unit-Testing");
    result.getNav().put("testCaseGroup", "TestCaseGroup-Unit-Testing");
    result.getNav().put("testPlan", "TestPlan-Unit-Testing");
    result.setComments("TC comments");
    result.setResult(TestResult.FAILED_NOT_SUPPORTED);

    result.setTestStepReports(testStepReports);
    TestStepValidationReport r1 = new TestStepValidationReport();
    String xmlMessageValidationReport =
        IOUtils.toString(TestCaseValidationReportServiceImplTest.class
            .getResourceAsStream("/reports/3-Manual-ValidationReport.xml"));

    TestStep t1 = new TestStep();
    t1.setName("Transmit the immunization report to the Immunization Registry");
    t1.setPosition(1);
    r1.setTestStep(t1);
    t1.setTestingType(TestingType.SUT_INITIATOR);
    r1.setComments("TestStep1 comments");
    r1.setResult(TestResult.FAILED);
    String xml =
        testStepService.generateXmlTestStepValidationReport(xmlMessageValidationReport, r1);
    r1.setXml(xml);

    testStepReports.add(r1);

    TestStepValidationReport r2 = new TestStepValidationReport();
    xmlMessageValidationReport =
        IOUtils.toString(TestCaseValidationReportServiceImplTest.class
            .getResourceAsStream("/reports/2-Message-ValidationReport.xml"));
    TestStep t2 = new TestStep();
    t2.setName("Receive Acknowledgement");
    t2.setTestingType(TestingType.TA_RESPONDER);
    t2.setPosition(2);
    r2.setTestStep(t2);
    r2.setComments("TestStep2 comments");
    r2.setResult(TestResult.FAILED_NOT_SUPPORTED);

    xml = testStepService.generateXmlTestStepValidationReport(xmlMessageValidationReport, r2);
    r2.setXml(xml);

    testStepReports.add(r2);

    TestStepValidationReport r3 = new TestStepValidationReport();
    xmlMessageValidationReport =
        IOUtils.toString(TestCaseValidationReportServiceImplTest.class
            .getResourceAsStream("/reports/2-Message-ValidationReport.xml"));
    TestStep t3 = new TestStep();
    t3.setName("Record an adverse reaction");
    t3.setTestingType(TestingType.TA_RESPONDER);
    t3.setPosition(3);
    r3.setTestStep(t3);
    r3.setComments("TestStep3 comments");
    r3.setResult(TestResult.PASSED_NOTABLE_EXCEPTION);
    xml = testStepService.generateXmlTestStepValidationReport(xmlMessageValidationReport, r3);
    r3.setXml(xml);

    testStepReports.add(r3);
    TestCase testCase = new TestCase();
    result.setTestCase(testCase);
    return result;
  }

  @Test
  public void testGenerateXml() throws Exception {
    String xml = service.generateXml(get());
    assertNotNull(xml);
    File f = new File("src/test/resources/TestCaseValidationReport.xml");
    System.out.println(f.getAbsolutePath());
    PrintWriter pw = new PrintWriter(f);
    pw.print(xml);
    pw.close();
  }

  @Test
  public void testGenerateHtml() throws Exception {
    String html = service.generateHtml(get());
    assertNotNull(html);
    File f = new File("src/test/resources/TestCaseValidationReport.html");
    System.out.println(f.getAbsolutePath());
    PrintWriter pw = new PrintWriter(f);
    pw.print(html);
    pw.close();
  }


  @Test
  public void testGeneratePdf() throws Exception {
    InputStream io = service.generatePdf(get());
    assertNotNull(io);
    File f = new File("src/test/resources/TestCaseValidationReport.pdf");
    OutputStream os = new FileOutputStream(f);
    FileCopyUtils.copy(io, os);
    os.close();
  }

}
