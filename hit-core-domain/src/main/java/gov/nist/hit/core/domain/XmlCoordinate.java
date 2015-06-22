package gov.nist.hit.core.domain;

public class XmlCoordinate {
  int line;
  int index;

  public XmlCoordinate() {
    super();
    this.line = 0;
    this.index = 0;
  }

  public XmlCoordinate(int line, int index) {
    super();
    this.line = line;
    this.index = index;
  }

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public XmlCoordinate plus(XmlCoordinate two) {
    XmlCoordinate sum = new XmlCoordinate();
    sum.setLine(Math.max(two.getLine(), this.getLine()));
    if (two.getLine() > this.getLine()) {
      sum.setIndex(two.getIndex());
    } else {
      sum.setIndex(this.getIndex());
    }
    return sum;
  }

  @Override
  public String toString() {
    return "Coordinate [line=" + line + ", index=" + index + "]";
  }

}
