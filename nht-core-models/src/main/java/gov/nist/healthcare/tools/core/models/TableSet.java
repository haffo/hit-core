package gov.nist.healthcare.tools.core.models;

import java.util.HashSet;
import java.util.Set;

public class TableSet implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected Set<TableDefinition> tableDefinitions = new HashSet<TableDefinition>();

	public Set<TableDefinition> getTableDefinitions() {
		return this.tableDefinitions;
	}

	public void setTableDefinitions(Set<TableDefinition> tableDefinitions) {
		this.tableDefinitions = tableDefinitions;
	}

	public void addTableDefinition(TableDefinition td) {
		getTableDefinitions().add(td);
	}

}
