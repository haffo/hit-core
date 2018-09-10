package gov.nist.hit.core.domain;

import java.util.Map;

import gov.nist.auth.hit.core.domain.TestingType;

public class SaveConfigRequest {
	private Long userId;
	private Map<String, String> config;
	private TestingType type;
	private String protocol;
	private String domain;
	private String name;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Map<String, String> getConfig() {
		return config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}

	public TestingType getType() {
		return type;
	}

	public void setType(TestingType type) {
		this.type = type;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
