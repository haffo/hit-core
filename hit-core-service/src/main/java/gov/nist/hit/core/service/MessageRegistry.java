package gov.nist.hit.core.service;

public interface MessageRegistry {

	public void addIncoming(String userKey, String message);

	public void addOutgoing(String userKey, String message);

	public String getIncoming(String userKey);

	public String getOutgoing(String userKey);

	public void removeIncoming(String userKey);

	public void removeOutgoing(String userKey);

	public void clear(String userKey);

}
