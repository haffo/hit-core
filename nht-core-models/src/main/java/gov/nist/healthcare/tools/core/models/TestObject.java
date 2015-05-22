package gov.nist.healthcare.tools.core.models;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TestObject {

	protected String name;
	protected String objDescription;
	protected TestType type;
	protected TestCategory category;
	protected Stage stage;
	protected int position;
	protected TestStory testStory = new TestStory();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObjDescription() {
		return objDescription;
	}

	public void setObjDescription(String objDescription) {
		this.objDescription = objDescription;
	}

	public TestType getType() {
		return type;
	}

	public void setType(TestType type) {
		this.type = type;
	}

	public TestCategory getCategory() {
		return category;
	}

	public void setCategory(TestCategory category) {
		this.category = category;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public TestStory getTestStory() {
		return testStory;
	}

	public void setTestStory(TestStory testStory) {
		this.testStory = testStory;
	}

}
