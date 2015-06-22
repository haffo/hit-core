package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.ValueSetLibrary;

public interface ValueSetLibrarySerializer {

	public String toXml(ValueSetLibrary valueSetLibrary);

	public ValueSetLibrary toObject(String xml);

}
