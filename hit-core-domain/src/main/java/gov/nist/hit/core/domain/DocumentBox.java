package gov.nist.hit.core.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Harold Affo
 *
 */
public class DocumentBox {
  
  private String title;
  
  private List<TestCaseDocument> documents = new ArrayList<TestCaseDocument>();

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<TestCaseDocument> getDocuments() {
    return documents;
  }

  public void setDocuments(List<TestCaseDocument> documents) {
    this.documents = documents;
  }
  
 
}
