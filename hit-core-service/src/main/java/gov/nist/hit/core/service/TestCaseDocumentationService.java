package gov.nist.hit.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestScope;

public interface TestCaseDocumentationService {

  public List<TestCaseDocumentation> generate(TestScope scope, String domain) throws IOException;

  public InputStream zipContextfreeMessages(TestScope scope, String domain) throws Exception;

  public InputStream zipContextbasedMessages(TestScope scope, String domain) throws Exception;

  public InputStream zipContextbasedTestPackages(TestScope scope, String domain) throws Exception;

  List<TestCaseDocumentation> generate(TestScope scope, String domain, String username)
      throws IOException;


}
