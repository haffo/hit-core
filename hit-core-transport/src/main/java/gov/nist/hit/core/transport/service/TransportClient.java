package gov.nist.hit.core.transport.service;

import gov.nist.hit.core.transport.exception.TransportClientException;

public interface TransportClient {

	/**
	 * 
	 * @param endpoint
	 * @param soapMessage
	 * @param token
	 * @return
	 * @throws TransportClientException
	 */
	String send(String meessage, String... arguments)
			throws TransportClientException;
}
