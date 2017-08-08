package gov.nist.hit.core.domain.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.located.LocatedJDOMFactory;
import org.jdom2.output.XMLOutputter;

public class XmlUtil {

  public static String format(String xml) throws TransformerException, JDOMException, IOException {
    return prettyFormat(xml, 2);
  }

  public static String prettyFormat(String input, int indent)
      throws TransformerException, JDOMException, IOException {
    return toString(toDocument(input).getRootElement());
  }

  public static Document toDocument(String content) throws JDOMException, IOException {
    SAXBuilder builder = new SAXBuilder();
    builder.setJDOMFactory(new LocatedJDOMFactory());
    builder.setExpandEntities(false);
    try {
      InputStreamReader isr = new InputStreamReader(IOUtils.toInputStream(content));
      return builder.build(isr);
    } catch (IOException e) {
      throw new IOException("Could not parse the xml content. Is the encoding UTF-8 or UTF-16 ?");
    }

  }

  public static InputStream toInputStream(String content) throws IOException {
    InputStream res = null;
    try {
      res = toInputStream(content, "UTF-8");
    } catch (Exception e) {
      res = toInputStream(content, "UTF-16");
    }
    return res;
  }

  public static InputStream toInputStream(String content, String encoding) throws IOException {
    return IOUtils.toInputStream(content, encoding);
  }

  public static String toString(Element element) {
    return new XMLOutputter().outputString(element);
  }

  public static String prettyPrint(String doc) throws IOException, TransformerException {
    Source xmlInput = new StreamSource(new StringReader(doc));
    StringWriter stringWriter = new StringWriter();
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = tf.newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "10");
    transformer.transform(xmlInput, new StreamResult(stringWriter));
    return stringWriter.toString();
  }

  public static String toString(Document document) {
    return new XMLOutputter().outputString(document.getRootElement());
  }


}
