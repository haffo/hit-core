package gov.nist.hit.core.domain;

public class Predicate extends Constraint {

  public Predicate(String id, String description) {
    super(id, description);
  }

  public Predicate(String id, String description, String trueUsage, String falseUsage) {
    super(id, description);
    this.trueUsage = trueUsage;
    this.falseUsage = falseUsage;
  }

  private static final long serialVersionUID = 1L;
  protected String trueUsage;
  protected String falseUsage;

  public String getTrueUsage() {
    return trueUsage;
  }

  public void setTrueUsage(String trueUsage) {
    this.trueUsage = trueUsage;
  }

  public String getFalseUsage() {
    return falseUsage;
  }

  public void setFalseUsage(String falseUsage) {
    this.falseUsage = falseUsage;
  }

  @Override
  protected Predicate clone() throws CloneNotSupportedException {
    Predicate pred = new Predicate(id,   description,   trueUsage,   falseUsage);
    return pred;
  }

  @Override
  public int hashCode() {
     int result = super.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
     return super.equals(obj);
  } 
  
  
 

}
