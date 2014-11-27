package gov.nist.healthcare.tools.core.transport;

public interface TransportClient {

	/**
	 * 
	 * @param endpoint
	 * @param soapMessage
	 * @param token
	 * @return
	 * @throws TransportClientException
	 */
	String send(String meessage, Object... arguments)
			throws TransportClientException;
}
