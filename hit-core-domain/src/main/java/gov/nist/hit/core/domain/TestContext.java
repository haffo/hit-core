package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class TestContext implements Serializable { 

 
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;


  @OneToOne (cascade=CascadeType.ALL)
  @JoinColumn(unique= true, nullable=false, insertable=true, updatable=true)  
  @JsonProperty(value = "profile")
  protected ConformanceProfile conformanceProfile;

  @ManyToOne
  protected VocabularyLibrary vocabularyLibrary;

  @OneToOne (cascade=CascadeType.ALL)
  @JoinColumn(unique= true, nullable=true, insertable=true, updatable=true)  
  protected Message message;

  @JsonIgnore
  @ManyToOne
  protected Constraints constraints; 
  
  @JsonIgnore
  @OneToOne (cascade=CascadeType.ALL)
  @JoinColumn(unique= true, nullable=true, insertable=true, updatable=true)  
  protected Constraints addditionalConstraints;
  

  public TestContext() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

 
  public ConformanceProfile getConformanceProfile() {
    return conformanceProfile;
  }

  public void setConformanceProfile(ConformanceProfile conformanceProfile) {
    this.conformanceProfile = conformanceProfile;
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
