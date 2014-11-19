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
package gov.nist.healthcare.tools.core.services.vocabulary;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import gov.nist.healthcare.tools.core.models.VocabularyLibrary;

 
/**
 * This class loads the table library definitions for different code systems
 * 
 * @author Harold Affo (NIST)
 */
public abstract class TableLibraryUnmarshaller {

	private static Logger logger = Logger
			.getLogger(TableLibraryUnmarshaller.class);

	protected String schemaLocation;

	public VocabularyLibrary unmarshall(InputStream iStream) {
		try {
			return processUnmarshalling(iStream,
					new JaxbWrapper<VocabularyLibrary>(VocabularyLibrary.class));
		} catch (FileNotFoundException e) {
			logger.error(e, e);
		} catch (JAXBException e) {
			logger.error(e, e);
		} catch (SAXException e) {
			logger.error(e, e);
		}
		return null;
	}

	public static VocabularyLibrary processUnmarshalling(InputStream istream,
			JaxbWrapper<VocabularyLibrary> jaxbWrapper)
			throws FileNotFoundException, JAXBException {
		return jaxbWrapper.xmlToObject(istream);
	}

	public String getSchemaLocation() {
		return schemaLocation;
	}

	public void setSchemaLocation(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}

}
