package gov.nist.hit.core.service.impl;

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;



public class XsltURIResolver implements URIResolver {

  @Override
  public Source resolve(String href, String base) throws TransformerException {
    try {
      InputStream inputStream =
          this.getClass().getClassLoader().getResourceAsStream("report/" + href);
      return new StreamSource(inputStream);
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
}
