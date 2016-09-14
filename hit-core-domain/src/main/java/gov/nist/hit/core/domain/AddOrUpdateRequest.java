package gov.nist.hit.core.domain;

public class AddOrUpdateRequest {
	private String url;
	private Long id;
	private String zip;
	
	public AddOrUpdateRequest() {
		super();
	}
	
	public AddOrUpdateRequest(String url, Long id) {
		super();
		this.url = url;
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	
}
