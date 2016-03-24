package gov.nist.hit.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author Harold Affo
 * 
 */
@ApiModel(value="Json", description="Data Model containing a json object")
public class Json {

  @ApiModelProperty(required=true, value="json object")
  private final String value;

  public Json(String value) {
    this.value = value;
  }

  @JsonValue
  @JsonRawValue
  public String value() {
    return value;
  }
}
