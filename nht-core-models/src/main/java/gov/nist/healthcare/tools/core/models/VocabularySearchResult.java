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

import java.util.Collection;

/**
 * @author Harold Affo (NIST)
 */
public class VocabularySearchResult {
	private String searchTable;
	private String searchTableValues;
	private String tableNumber;
	private String valueSetName;
	private String tableType;
	private String selectCriteria;
	private String searchString;
	private TableModel selectedTable;
	private Collection<TableModel> searchTablesResults;
	private Collection<VocabularySearchResultItem> tableContent;
	private Collection<VocabularySearchResultItem> searchTablesDetailContent;
	private Collection<VocabularySearchResultItem> searchTablesNameContent;
	protected int searchResultsColumnCount = 0;
	private String valueSetCode;
	private String valueSetOID;
	private String comment;

	public String getSearchTable() {
		return searchTable;
	}

	public void setSearchTable(String searchTable) {
		this.searchTable = searchTable;
	}

	public String getSearchTableValues() {
		return searchTableValues;
	}

	public void setSearchTableValues(String searchTableValues) {
		this.searchTableValues = searchTableValues;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public String getValueSetName() {
		return valueSetName;
	}

	public void setValueSetName(String valueSetName) {
		this.valueSetName = valueSetName;
	}

	public String getValueSetCode() {
		return valueSetCode;
	}

	public void setValueSetCode(String valueSetCode) {
		this.valueSetCode = valueSetCode;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getSelectCriteria() {
		return selectCriteria;
	}

	public void setSelectCriteria(String selectCriteria) {
		this.selectCriteria = selectCriteria;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public TableModel getSelectedTable() {
		return selectedTable;
	}

	public void setSelectedTable(TableModel selectedTable) {
		this.selectedTable = selectedTable;
	}

	public Collection<TableModel> getSearchTablesResults() {
		return searchTablesResults;
	}

	public void setSearchTablesResults(
			Collection<TableModel> searchTablesResults) {
		this.searchTablesResults = searchTablesResults;
	}

	public Collection<VocabularySearchResultItem> getTableContent() {
		return tableContent;
	}

	public void setTableContent(
			Collection<VocabularySearchResultItem> tableContent) {
		this.tableContent = tableContent;
	}

	public Collection<VocabularySearchResultItem> getSearchTablesDetailContent() {
		return searchTablesDetailContent;
	}

	public void setSearchTablesDetailContent(
			Collection<VocabularySearchResultItem> searchTablesDetailContent) {
		this.searchTablesDetailContent = searchTablesDetailContent;
	}

	public Collection<VocabularySearchResultItem> getSearchTablesNameContent() {
		return searchTablesNameContent;
	}

	public void setSearchTablesNameContent(
			Collection<VocabularySearchResultItem> searchTablesNameContent) {
		this.searchTablesNameContent = searchTablesNameContent;
	}

	public int getSearchResultsColumnCount() {
		return searchResultsColumnCount;
	}

	public void setSearchResultsColumnCount(int searchResultsColumnCount) {
		this.searchResultsColumnCount = searchResultsColumnCount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getValueSetOID() {
		return valueSetOID;
	}

	public void setValueSetOID(String valueSetOID) {
		this.valueSetOID = valueSetOID;
	}

	public void clearContent() {
		this.tableContent = null;
		this.valueSetName = null;
		this.tableNumber = null;
		this.tableType = null;
		this.valueSetCode = null;
		this.valueSetOID = null;
		this.comment = null;
	}

}
