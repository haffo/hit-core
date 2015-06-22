package gov.nist.hit.core.domain;

import java.util.HashSet;
import java.util.Set;

public class ValueSetDefinitions implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  protected Set<ValueSetDefinition> valueSetDefinitions = new HashSet<ValueSetDefinition>();

  public Set<ValueSetDefinition> getValueSetDefinitions() {
    return this.valueSetDefinitions;
  }

  public void setValueSetDefinitions(Set<ValueSetDefinition> valueSetDefinitions) {
    this.valueSetDefinitions = valueSetDefinitions;
  }

  public void addValueSet(ValueSetDefinition td) {
    getValueSetDefinitions().add(td);
  }

}
