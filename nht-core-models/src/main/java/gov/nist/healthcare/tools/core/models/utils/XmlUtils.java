package gov.nist.healthcare.tools.core.models.utils;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.located.LocatedJDOMFactory;
import org.jdom2.output.XMLOutputter;

public class XmlUtils {

	public static String format(String xml) throws TransformerException,
			JDOMException, IOException {
		return prettyFormat(xml, 2);
	}

	public static String prettyFormat(String input, int indent)
			throws TransformerException, JDOMException, IOException {

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
		Document doc = toDocument(input);
		String res = new XMLOutputter().outputString(doc.getRootElement());

		return res;
	}

	public static Document toDocument(String content) throws JDOMException,
			IOException {
		SAXBuilder builder = new SAXBuilder();
		builder.setJDOMFactory(new LocatedJDOMFactory());
		builder.setExpandEntities(false);
		return builder.build(IOUtils.toInputStream(content));
	}

	public static String toString(Element element) {
		return new XMLOutputter().outputString(element);
	}

}
