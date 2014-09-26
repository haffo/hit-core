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
package gov.nist.healthcare.tools.core.models.vocabulary;

/**
 * 
 * @author Harold Affo
 * 
 */
public class VocabularySearchResultItem {

	private String codeSys;
	private String typeOrId;// 3
	private String oid;// 4
	private String tableCodeSys;// 5
	private String comment;// 6
	private String name;
	private String code;
	private String displayName;

	public String getCodeSys() {
		return codeSys;
	}

	public void setCodeSys(String codeSys) {
		this.codeSys = codeSys;
	}

	public String getTypeOrId() {
		return typeOrId;
	}

	public void setTypeOrId(String typeOrId) {
		this.typeOrId = typeOrId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTableCodeSys() {
		return tableCodeSys;
	}

	public void setTableCodeSys(String tableCodeSys) {
		this.tableCodeSys = tableCodeSys;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public VocabularySearchResultItem(String codeSys, String typeOrId,
			String name, String code, String displayName) {
		super();
		this.codeSys = codeSys;
		this.typeOrId = typeOrId;
		this.name = name;
		this.code = code;
		this.displayName = displayName;
	}

	public VocabularySearchResultItem(String codeSys, String typeOrId,
			String name) {
		this(codeSys, typeOrId, name, null, null);
	}

	public VocabularySearchResultItem(String codeSys, String id, String name,
			String comment) {
		this(codeSys, id, name, null, null);
		this.comment = comment;
	}

	public VocabularySearchResultItem(String code, String displayName,
			String codeSys, String typeOrId, String oid, String tableCodeSys,
			String comment) {
		this(codeSys, typeOrId, null, code, displayName);
		this.comment = comment;
		this.tableCodeSys = tableCodeSys;
		this.oid = oid;
	}

	public void loadValueSetCodeSearchResult() {

	}

}
