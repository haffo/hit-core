/**
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of their
 * official duties. Pursuant to title 17 Section 105 of the United States Code
 * this software is not subject to copyright protection and is in the public
 * domain. This is an experimental system. NIST assumes no responsibility
 * whatsoever for its use by other parties, and makes no guarantees, expressed
 * or implied, about its quality, reliability, or any other characteristic. We
 * would appreciate acknowledgement if the software is used. This software can
 * be redistributed and/or modified freely provided that any derivative works
 * bear some notice that they are derived from it, and any modified versions
 * bear some notice that they have been modified.
 */
package gov.nist.hit.core.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Message implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	protected String messageName;

	@Column(nullable = true)
	protected String messageDescription;

	@Column(columnDefinition = "LONGTEXT")
	protected String messageContent;

	public Message() {
	}

	public Message(String name, String description, String content) {
		super();
		this.messageName = name;
		this.messageDescription = description;
		this.messageContent = content;
	}

	public Message(String content) {
		super();
		this.messageName = null;
		this.messageDescription = null;
		this.messageContent = content;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(getMessageName()).append(", ");
		sb.append("Description: ").append(getMessageDescription()).append(", ");
		sb.append("Content: ").append(getMessageContent());
		return sb.toString();
	}

	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	public String getMessageDescription() {
		return messageDescription;
	}

	public void setMessageDescription(String messageDescription) {
		this.messageDescription = messageDescription;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

}
