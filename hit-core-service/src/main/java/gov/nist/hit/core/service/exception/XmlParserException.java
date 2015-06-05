package gov.nist.hit.core.service.exception;


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
