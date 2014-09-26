package gov.nist.healthcare.tools.core.services.validation.soap;

import gov.nist.healthcare.tools.core.services.soap.SoapException;
import gov.nist.healthcare.tools.core.services.validation.ValidationReportException;

public class SoapValidationReportException extends ValidationReportException {
	private static final long serialVersionUID = 1L;

	public SoapValidationReportException(String message) {
		super(message);
	}

	public SoapValidationReportException(RuntimeException exception) {
		super(exception);
	}

	public SoapValidationReportException(ValidationReportException exception) {
		super(exception);
	}

}