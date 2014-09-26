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

import java.util.Set;

public class MutiStepTestCase extends TestCase {

	protected Set<TestStep> testSteps;

	public MutiStepTestCase() {
		super();
	}

	public MutiStepTestCase(String name, String description,
			Set<TestStep> testSteps, TestStory testStory) {
		super(name, description, testStory);
		this.testSteps = testSteps;
	}

	public Set<TestStep> getTestSteps() {
		return testSteps;
	}

	public void setTestSteps(Set<TestStep> testSteps) {
		this.testSteps = testSteps;
	}

}
