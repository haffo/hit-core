package gov.nist.hit.core.domain;

public class ResourceUploadStatus {
	private ResourceUploadAction action;
	private ResourceUploadResult status;
	private ResourceType type;
	private String id;
	private String message;
	
	
	public ResourceUploadStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public ResourceUploadStatus(ResourceUploadAction action,
			ResourceUploadResult status, ResourceType type, String id,
			String message) {
		super();
		this.action = action;
		this.status = status;
		this.type = type;
		this.id = id;
		this.message = message;
	}


	public ResourceUploadAction getAction() {
		return action;
	}
	public void setAction(ResourceUploadAction action) {
		this.action = action;
	}
	public ResourceUploadResult getStatus() {
		return status;
	}
	public void setStatus(ResourceUploadResult status) {
		this.status = status;
	}
	public ResourceType getType() {
		return type;
	}
	public void setType(ResourceType type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id+"";
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
