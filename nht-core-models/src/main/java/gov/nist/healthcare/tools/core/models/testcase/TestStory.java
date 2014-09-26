/**
 * This software was developed at the National Institute of Standards and Technology by employees
 * of the Federal Government in the course of their official duties. Pursuant to title 17 Section 105 of the
 * United States Code this software is not subject to copyright protection and is in the public domain.
 * This is an experimental system. NIST assumes no responsibility whatsoever for its use by other parties,
 * and makes no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.
 * We would appreciate acknowledgement if the software is used. This software can be redistributed and/or
 * modified freely provided that any derivative works bear some notice that they are derived from it, and any
 * modified versions bear some notice that they have been modified.
 */
package gov.nist.healthcare.tools.core.models.testcase;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TestStory {
	private String comments;

	private String notes;

	private String description;

	private String comment;

	private String preCondition;

	private String postCondition;

	private String testObjectives;

	private String noteToTesters;

	private String umlDiagramCode;

	public TestStory(String description, String comments, String preCondition,
			String postCondition, String testObjectives, String notes,
			String umlDiagramCode) {
		super();
		this.description = description;
		this.comments = comments;
		this.preCondition = preCondition;
		this.postCondition = postCondition;
		this.testObjectives = testObjectives;
		this.notes = notes;
		this.umlDiagramCode = umlDiagramCode;
	}

	public TestStory() {
		super();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPreCondition() {
		return preCondition;
	}

	public void setPreCondition(String preCondition) {
		this.preCondition = preCondition;
	}

	public String getPostCondition() {
		return postCondition;
	}

	public void setPostCondition(String postCondition) {
		this.postCondition = postCondition;
	}

	public String getTestObjectives() {
		return testObjectives;
	}

	public void setTestObjectives(String testObjectives) {
		this.testObjectives = testObjectives;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getNoteToTesters() {
		return noteToTesters;
	}

	public void setNoteToTesters(String noteToTesters) {
		this.noteToTesters = noteToTesters;
	}

	public String getUmlDiagramCode() {
		return umlDiagramCode;
	}

	public void setUmlDiagramCode(String umlDiagramCode) {
		this.umlDiagramCode = umlDiagramCode;
	}

	@Override
	public boolean equals(Object that) {
		return EqualsBuilder.reflectionEquals(this, that, "description",
				"comment", "preCondition", "postCondition", "testObjectives",
				"noteToTesters");
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "description",
				"comment", "preCondition", "postCondition", "testObjectives",
				"noteToTesters");
	}

}
