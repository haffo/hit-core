/**
 * This software was developed at the National Institute of Standards and Technology by employees
 * of the Federal Government in the course of their official duties. Pursuant to title 17 Section 105 of the
 * United States Code this software is not subject to copyright protection and is in the public domain.
 * This is an experimental system. NIST assumes no responsibility whatsoever for its use by other parties,
 * and makes no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.
 * We would appreciate acknowledgement if the software is used. This software can be redistributed and/or
 * modified freely provided that any derivative works bear some notice that they are derived from it, and any
 * modified versions bear some notice that they have been modified.
 */
package gov.nist.hit.core.service.impl;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * 
 * @author Harold Affo (NIST)
 * 
 */
/**
 * Simple JAXB Wrapper
 */
public class JaxbWrapper<T> {
	private JAXBContext jaxbContext = null;

	private Schema schema;

	/**
	 * @param schemaFile
	 * @param clazz
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public JaxbWrapper(Source schemaSource, Class<?>... clazz)
			throws JAXBException, SAXException {
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		this.schema = schemaFactory.newSchema(schemaSource);
		this.jaxbContext = JAXBContext.newInstance(clazz);
	}

	public JaxbWrapper(Class<?>... clazz) throws JAXBException, SAXException {
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		this.schema = schemaFactory.newSchema();
		this.jaxbContext = JAXBContext.newInstance(clazz);
	}

	/**
	 * Converts the given object to XML
	 * 
	 * @param t
	 * @return
	 * @throws JAXBException
	 */
	public String objectToXml(T t) throws JAXBException {
		StringWriter writer = new StringWriter();
		objectToXml(t, writer);
		return writer.toString();
	}

	/**
	 * Converts the given object to XML
	 * 
	 * @param t
	 *            the object to convert to xml
	 * @param writer
	 *            the output will be written to this writer
	 * @throws JAXBException
	 */
	public void objectToXml(T t, Writer writer) throws JAXBException {
		createMarshaller().marshal(t, writer);
	}

	/**
	 * Converts the given object to XML
	 * 
	 * @param t
	 *            the object to convert to xml
	 * @param outputStream
	 *            the output will be written to this output stream
	 * @throws JAXBException
	 */
	public void objectToXml(T t, OutputStream outputStream)
			throws JAXBException {
		createMarshaller().marshal(t, outputStream);
	}

	/**
	 * Converts the given object to XML
	 * 
	 * @param t
	 *            the object to convert to xml
	 * @param file
	 *            the output will be written to this file
	 * @throws JAXBException
	 */
	public void objectToXml(T t, File file) throws JAXBException {
		createMarshaller().marshal(t, file);
	}

	/**
	 * validates the object against the schema, throws an Exception if
	 * validation fails
	 * 
	 * @param t
	 *            the object to validate
	 * @throws JAXBException
	 *             when schema validation fails
	 */
	public void validate(T t) throws JAXBException {
		createMarshaller().marshal(t, new StringWriter());
	}

	/**
	 * converts xml form to a java object
	 * 
	 * @param is
	 *            InputStream which points to a valid XML content
	 * @return the Java object representing the xml
	 * @throws JAXBException
	 *             if jaxb unmarshalling fails. Common reason is schema
	 *             incompatibility
	 */
	@SuppressWarnings("unchecked")
	public T xmlToObject(InputStream is) throws JAXBException {
		return (T) createUnmarshaller().unmarshal(is);
	}

	/**
	 * converts xml form to a java object
	 * 
	 * @param xml
	 *            a valid XML string
	 * @return the Java object representing the xml
	 * @throws JAXBException
	 *             if jaxb unmarshalling fails. Common reason is schema
	 *             incompatibility
	 */
	@SuppressWarnings("unchecked")
	public T xmlToObject(String xml) throws JAXBException {
		StringReader stringReader = new StringReader(xml);
		return (T) createUnmarshaller().unmarshal(stringReader);
	}

	/**
	 * Creates a marshaller. Clients generally don't need to call this method,
	 * hence the method is protected. If you want use features not already
	 * exposed, you can subclass and call this method.
	 * 
	 * @return
	 * @throws JAXBException
	 * @throws PropertyException
	 */
	protected Marshaller createMarshaller() throws JAXBException,
			PropertyException {
		Marshaller marshaller = jaxbContext.createMarshaller();
		// marshaller.setSchema(schema);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(
				true));
		return marshaller;
	}

	/**
	 * Creates a Marshaller. Clients generally don't need to call this method,
	 * hence the method is protected. If you want use features not already
	 * exposed, you can subclass and call this method.
	 * 
	 * @return
	 * @throws JAXBException
	 */
	protected Unmarshaller createUnmarshaller() throws JAXBException {
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		// unmarshaller.setSchema(schema);
		return unmarshaller;
	}

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

}
