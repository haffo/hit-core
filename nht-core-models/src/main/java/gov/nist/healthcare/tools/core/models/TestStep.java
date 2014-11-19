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
package gov.nist.healthcare.tools.core.models;

public class TestStep implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected int sequenceNum;

	protected String type;

	protected Transaction transaction;

	protected String description;

	protected VendorDataSheet vendorDataSheet;

	public TestStep(Long id, int sequenceNum, String type,
			Transaction transaction, String description) {
		super();
		this.sequenceNum = sequenceNum;
		this.type = type;
		this.transaction = transaction;
		this.description = description;
	}

	public TestStep() {
		super();
	}

	public int getSequenceNum() {
		return sequenceNum;
	}

	public void setSequenceNum(int sequenceNum) {
		this.sequenceNum = sequenceNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public VendorDataSheet getVendorDataSheet() {
		return vendorDataSheet;
	}

	public void setVendorDataSheet(VendorDataSheet vendorDataSheet) {
		this.vendorDataSheet = vendorDataSheet;
	}

}
