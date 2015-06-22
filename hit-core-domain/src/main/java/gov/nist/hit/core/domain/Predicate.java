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

}
