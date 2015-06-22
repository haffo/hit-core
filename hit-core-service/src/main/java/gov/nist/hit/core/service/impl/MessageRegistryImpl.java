package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.service.MessageRegistry;

import java.util.LinkedHashMap;
import java.util.Map;

public class MessageRegistryImpl implements MessageRegistry {

	Map<String, String> incomings = new LinkedHashMap<String, String>();
	Map<String, String> outgoings = new LinkedHashMap<String, String>();

	public MessageRegistryImpl() {
	}

	public Map<String, String> getIncomings() {
		return incomings;
	}

	public void setIncomings(Map<String, String> incomings) {
		this.incomings = incomings;
	}

	public Map<String, String> getOutgoings() {
		return outgoings;
	}

	public void setOutgoings(Map<String, String> outgoings) {
		this.outgoings = outgoings;
	}

	@Override
	public void addIncoming(String userKey, String message) {
		incomings.put(userKey, message);
	}

	@Override
	public void addOutgoing(String userKey, String message) {
		outgoings.put(userKey, message);
	}

	@Override
	public String getIncoming(String userKey) {
		return incomings.get(userKey);
	}

	@Override
	public String getOutgoing(String userKey) {
		return outgoings.get(userKey);
	}

	@Override
	public void removeIncoming(String userKey) {
		incomings.remove(userKey);
	}

	@Override
	public void removeOutgoing(String userKey) {
		outgoings.remove(userKey);
	}

	@Override
	public void clear(String userKey) {
		removeIncoming(userKey);
		removeOutgoing(userKey);
	}

}
