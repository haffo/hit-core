package gov.nist.hit.core.domain;

import java.util.List;

public class SessionTestCases {
	
	private List<CFTestPlan> preloaded;
	private List<CFTestPlan> user;
	

	public List<CFTestPlan> getPreloaded() {
		return preloaded;
	}
	public void setPreloaded(List<CFTestPlan> preloaded) {
		this.preloaded = preloaded;
	}
	public List<CFTestPlan> getUser() {
		return user;
	}
	public void setUser(List<CFTestPlan> user) {
		this.user = user;
	}
	
}
