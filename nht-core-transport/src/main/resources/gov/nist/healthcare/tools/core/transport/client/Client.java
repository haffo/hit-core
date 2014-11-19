package gov.nist.healthcare.tools.core.transport.client;

import gov.nist.healthcare.tools.core.transport.client.exception.ClientException;

public interface Client {

	/**
	 * 
	 * @param endpoint
	 * @param soapMessage
	 * @param token
	 * @return
	 * @throws ClientException
	 */
	String send(String endpoint, String soapMessage) throws ClientException;
}
