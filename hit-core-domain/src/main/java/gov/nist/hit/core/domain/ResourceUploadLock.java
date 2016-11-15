package gov.nist.hit.core.domain;

public class ResourceUploadLock {

	private boolean enabled;

	
	public ResourceUploadLock() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResourceUploadLock(boolean enabled) {
		super();
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
