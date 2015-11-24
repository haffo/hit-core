package gov.nist.hit.core.domain;

public class SegmentRef {

  private String id;

//  private String type;
//
//  private String name;

  public SegmentRef(String id) {
    super();
    this.id = id;
//    this.type = type;
//    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

//  public String getType() {
//    return type;
//  }
//
//  public void setType(String type) {
//    this.type = type;
//  }

//  public String getName() {
//    return name;
//  }
//
//  public void setName(String name) {
//    this.name = name;
//  }

}
