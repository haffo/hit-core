package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TestContext implements Serializable {
 
  private static final long serialVersionUID = -7669461668488662066L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @JsonIgnore
  @ManyToOne
  protected Profile profile;

  @ManyToOne
  @JsonIgnore
  protected VocabularyLibrary vocabularyLibrary;

  @ManyToOne
  @JsonIgnore
  protected Message message;

  @JsonIgnore
  @ManyToOne
  protected Constraints constraints; 
  
  @JsonIgnore
  @ManyToOne
  protected Constraints addditionalConstraints;
  

  public TestContext() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }

 
 
  public Message getMessage() {
    return message;
  }

  public void setMessage(Message message) {
    this.message = message;
  }

  public Constraints getConstraints() {
    return constraints;
  }

  public void setConstraints(Constraints constraints) {
    this.constraints = constraints;
  }

  public VocabularyLibrary getVocabularyLibrary() {
    return vocabularyLibrary;
  }

  public void setVocabularyLibrary(VocabularyLibrary vocabularyLibrary) {
    this.vocabularyLibrary = vocabularyLibrary;
  }

  public Constraints getAddditionalConstraints() {
    return addditionalConstraints;
  }

  public void setAddditionalConstraints(Constraints addditionalConstraints) {
    this.addditionalConstraints = addditionalConstraints;
  }

  public void setId(Long id) {
    this.id = id;
  }
 
  
  
  

}
