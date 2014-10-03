package gov.nist.healthcare.tools.core.services.xml;

import gov.nist.healthcare.tools.core.services.message.MessageParserException;

public class XmlParserException extends MessageParserException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XmlParserException(String message) {
		super(message);
	}

	public XmlParserException(RuntimeException exception) {
		super(exception);
	}

	public XmlParserException(Exception e) {
		super(e);
	}
}
