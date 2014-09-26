/**
 * This software was developed at the National Institute of Simport java.util.HashSet;
import java.util.List;
import java.util.Set;
e course of their official duties. Pursuant to title 17 Section 105 of the
 * United States Code this software is not subject to copyright protection and is in the public domain.
 * This is an experimental system. NIST assumes no responsibility whatsoever for its use by other parties,
 * and makes no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.
 * We would appreciate acknowledgement if the software is used. This software can be redistributed and/or
 * modified freely provided that any derivative works bear some notice that they are derived from it, and any
 * modified versions bear some notice that they have been modified.
 */
package gov.nist.healthcare.tools.core.models.vocabulary;

import java.util.Set;

/**
 * 
 * @author Harold Affo
 * 
 */
public class VocabularyCollection {

	protected String name;

	protected Long id;

	public VocabularyCollection() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VocabularyCollection(String name,
			Set<? extends VocabularyLibrary> libraries) {
		super();
		this.name = name;
	}
}