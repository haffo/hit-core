package gov.nist.healthcare.tools.core.models;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TestObject {

	protected String name;
	protected String objDescription;
	@Enumerated(EnumType.STRING)
	protected TestType type;
	@Enumerated(EnumType.STRING)
	protected TestCategory category;
	@Enumerated(EnumType.STRING)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((objDescription == null) ? 0 : objDescription.hashCode());
		result = prime * result + position;
		result = prime * result + ((stage == null) ? 0 : stage.hashCode());
		result = prime * result
				+ ((testStory == null) ? 0 : testStory.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestObject other = (TestObject) obj;
		if (category != other.category)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (objDescription == null) {
			if (other.objDescription != null)
				return false;
		} else if (!objDescription.equals(other.objDescription))
			return false;
		if (position != other.position)
			return false;
		if (stage != other.stage)
			return false;
		if (testStory == null) {
			if (other.testStory != null)
				return false;
		} else if (!testStory.equals(other.testStory))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
