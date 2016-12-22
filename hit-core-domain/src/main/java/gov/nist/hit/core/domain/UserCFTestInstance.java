package gov.nist.hit.core.domain;


import java.io.Serializable;

import javax.persistence.Entity;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value="UserCFTestInstance", description="Data Model representing user context-free test case")
public class UserCFTestInstance extends CFTestInstance implements Serializable {

  private static final long serialVersionUID = 880596750847898512L;


  public UserCFTestInstance() {
    super();
  }




}
