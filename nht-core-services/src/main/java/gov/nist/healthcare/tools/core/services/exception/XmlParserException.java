package gov.nist.healthcare.tools.core.services.exception;


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
