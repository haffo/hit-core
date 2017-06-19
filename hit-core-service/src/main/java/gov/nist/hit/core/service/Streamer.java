package gov.nist.hit.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.Document;
import gov.nist.hit.core.domain.MessageElement;
import gov.nist.hit.core.domain.MessageModel;
import gov.nist.hit.core.domain.MessageValidationResult;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.ValidationResult;

public interface Streamer {

  public void stream(OutputStream os, MessageModel model) throws IOException;

  public void stream(OutputStream os, MessageValidationResult model) throws IOException;

  void stream(OutputStream os, TestStepValidationReport report) throws IOException;

  void stream(OutputStream os, String content) throws IOException;

  void stream(OutputStream os, InputStream content) throws IOException;

  // void stream(OutputStream os, TestPlan testPlan) throws IOException;
  //
  // void stream(OutputStream os, TestCase testCase) throws IOException;
  //
  // void stream(OutputStream os, TestStep testStep) throws IOException;
  //
  // void stream(OutputStream os, CFTestStep testStep) throws IOException;
  //
  // void stream(OutputStream os, CFTestPlan testStep) throws IOException;
  //
  // void stream(OutputStream os, TestContext testContext) throws IOException;

  void stream2(OutputStream os, List<CFTestPlan> testPlans) throws IOException;

  void stream(OutputStream os, List<TestPlan> testPlans) throws IOException;

  void streamDocs(OutputStream os, List<Document> documents) throws IOException;

  void stream(OutputStream os, Document document) throws IOException;

  void stream(OutputStream os, TestCaseDocumentation document) throws IOException;

  // void stream(OutputStream os, Json json) throws IOException;

  void stream(OutputStream os, TestArtifact artifact) throws IOException;

  void stream(OutputStream os, Map<String, Object> artifacts) throws IOException;

  void streamMap(OutputStream os, Map<String, String> map) throws IOException;

  public void stream(OutputStream os, TestCaseGroup testCaseGroup) throws IOException;

  public void stream(OutputStream os, Transaction transaction) throws IOException;

  void stream(OutputStream os, ValidationResult result) throws IOException;

  void streamElements(OutputStream os, List<MessageElement> elements) throws IOException;


}
