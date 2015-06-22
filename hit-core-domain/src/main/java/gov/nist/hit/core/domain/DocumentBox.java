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
  
  private List<Document> documents = new ArrayList<Document>();

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Document> getDocuments() {
    return documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }
  
 
}
