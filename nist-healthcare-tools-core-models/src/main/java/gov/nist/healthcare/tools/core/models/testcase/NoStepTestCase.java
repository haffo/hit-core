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

import gov.nist.healthcare.tools.core.models.profile.Profile;
import gov.nist.healthcare.tools.core.models.tablelibrary.TableLibrary;
import gov.nist.healthcare.tools.core.models.validationcontext.ValidationContext;

import java.util.HashSet;
import java.util.Set;

public class NoStepTestCase extends TestCase {

	protected Profile profile;

	protected Set<TableLibrary> tableLibraries;

	protected ValidationContext validationContext;

	public NoStepTestCase(String name, String description, TestStory testStory,
			Profile profile, Set<TableLibrary> tableLibraries,
			ValidationContext validationContext) {
		super();
		this.name = name;
		this.description = description;
		this.testStory = testStory;
		this.profile = profile;
		this.tableLibraries = tableLibraries;
		this.validationContext = validationContext;
	}

	public NoStepTestCase() {
		super();
	}

	public Profile getProfile() {
		return profile;
	}

	public Set<TableLibrary> getTableLibraries() {
		return tableLibraries;
	}

	public ValidationContext getValidationContext() {
		return validationContext;
	}

	public void addTableLibrary(TableLibrary tableLibrary) {
		if (tableLibrary == null)
			throw new NullPointerException("Can't add null table library");
		if (tableLibraries == null)
			tableLibraries = new HashSet<TableLibrary>();
		tableLibraries.add(tableLibrary);
	}

	public void setValidationContext(ValidationContext validationContext) {
		if (validationContext == null)
			throw new NullPointerException("Can't add null Validation Context");
		this.validationContext = validationContext;
	}

	public void setProfile(Profile profile) {
		if (profile == null)
			throw new NullPointerException("Can't add null profile");
		this.profile = profile;
	}

}
