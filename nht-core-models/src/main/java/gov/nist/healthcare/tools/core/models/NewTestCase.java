package gov.nist.healthcare.tools.core.models;

public abstract class NewTestCase {

	protected String name;
	protected String description;
	protected Integer version;
	protected TestType type;
	protected TestStory testCaseStory = new TestStory();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public TestType getType() {
		return type;
	}

	public void setType(TestType type) {
		this.type = type;
	}

	public TestStory getTestCaseStory() {
		return testCaseStory;
	}

	public void setTestCaseStory(TestStory testCaseStory) {
		this.testCaseStory = testCaseStory;
	}

}
