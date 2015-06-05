package gov.nist.hit.core.service.exception;


public class XmlFormatterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XmlFormatterException(String message) {
		super(message);
	}

	public XmlFormatterException(RuntimeException exception) {
		super(exception);
	}

	public XmlFormatterException(Exception e) {
		super(e);
	}
}
