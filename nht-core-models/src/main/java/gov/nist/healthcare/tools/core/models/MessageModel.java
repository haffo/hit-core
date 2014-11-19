package gov.nist.healthcare.tools.core.models;

import java.util.ArrayList;
import java.util.List;

public class MessageModel {

	protected List<MessageElement> elements;

	public List<MessageElement> getElements() {
		return elements;
	}

	public void setElements(List<MessageElement> elements) {
		this.elements = elements;
	}

	/**
	 * @param type
	 * @param content
	 */
	public MessageModel() {
		elements = new ArrayList<MessageElement>();
	}
}
