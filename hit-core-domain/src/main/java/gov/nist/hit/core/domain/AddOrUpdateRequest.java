package gov.nist.hit.core.domain;


public class AddOrUpdateRequest {

	private Long id;
	private String zip;
	private String dplScope;
	
	public AddOrUpdateRequest() {
		super();
	}
	
	public AddOrUpdateRequest(Long id, String zip, String dplScope) {
		super();
		this.id = id;
		this.zip = zip;
		this.dplScope = dplScope;
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

	public String getScope() {
		return dplScope;
	}

	public void setScope(String scope) {
		this.dplScope = scope;
	}

}
