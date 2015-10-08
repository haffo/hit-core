package gov.nist.hit.core.domain.util;

import gov.nist.hit.core.domain.XMLCoordinate;

import java.io.IOException;
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
import org.jdom2.located.Located;
import org.jdom2.located.LocatedJDOMFactory;
import org.jdom2.output.XMLOutputter;

public class XmlUtil {

  public static String format(String xml) throws TransformerException, JDOMException, IOException {
    return prettyFormat(xml, 2);
  }

  public static String prettyFormat(String input, int indent) throws TransformerException,
      JDOMException, IOException {

    // Source xmlInput = new StreamSource(new StringReader(input));
    // StringWriter stringWriter = new StringWriter();
    // StreamResult xmlOutput = new StreamResult(stringWriter);
    // TransformerFactory transformerFactory = TransformerFactory
    // .newInstance();
    // Transformer transformer = transformerFactory.newTransformer();
    // transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    // // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
    // // "yes");
    // transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    // transformer.setOutputProperty(
    // "{http://xml.apache.org/xslt}indent-amount", "4");
    // transformer.transform(xmlInput, xmlOutput);
    // String res = xmlOutput.getWriter().toString();
    // System.out.println(res);
    // System.setProperty("javax.xml.parsers.SAXParserFactory",
    // "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
    Document doc = toDocument(input);
    String res = toString(doc.getRootElement());

    return res;
  }

  public static Document toDocument(String content) throws JDOMException, IOException {
    SAXBuilder builder = new SAXBuilder();
    builder.setJDOMFactory(new LocatedJDOMFactory());
    builder.setExpandEntities(false);
    return builder.build(IOUtils.toInputStream(content));
  }

  public static String toString(Element element) {
    return new XMLOutputter().outputString(element);
  }

  public static String prettyPrint(String doc) throws IOException, TransformerException {
    Source xmlInput = new StreamSource(new StringReader(doc));
    StringWriter stringWriter = new StringWriter();
    StreamResult xmlOutput = new StreamResult(stringWriter);
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

  public static XMLCoordinate getStartCoordinate(Element element) {
    Located locatedElement = (Located) element;
    return new XMLCoordinate(locatedElement.getLine(), 0);
  }

  public static XMLCoordinate getEndCoordinate(Element element) {
    return new XMLCoordinate(getEndLine(element), 1000);
  }

  private static int getNumberOfLine(Element element) {
    String content = XmlUtil.toString(element);
    String[] lines = content.split(System.getProperty("line.separator"));
    return lines.length;
  }

  public static int getEndLine(Element element) {
    return getStartLine(element) + getNumberOfLine(element) - 1;
  }

  public static int getStartLine(Element element) {
    return ((Located) element).getLine();
  }
}
