package com.quatro.model;

public class LookupTableDefValue {
   String moduleId;
   String moduleName;
   String tableId;
   String tableName;
   String description;
   boolean active;
   boolean readonly;
   boolean tree;
   int treeCodeLength;
   boolean hasActive;
   boolean hasDisplayOrder;

   public boolean isReadonly() {
	return readonly;
}

public void setReadonly(boolean readonly) {
	this.readonly = readonly;
}

public boolean isActive() {
	 return active;
   }
   
   public void setActive(boolean active) {
	 this.active = active;
   }
   
   public String getDescription() {
	 return description;
   }
   
   public void setDescription(String description) {
	 this.description = description;
   }
   
   public String getTableId() {
	 return tableId;
   }
   
   public void setTableId(String tableId) {
	 this.tableId = tableId;
   }
   
   public String getTableName() {
	 return tableName;
   }
   
   public void setTableName(String tableName) {
	 this.tableName = tableName;
   }
   
   public int getTreeCodeLength() {
	 return treeCodeLength;
   }
   
   public void setTreeCodeLength(int treeCodeLength) {
	 this.treeCodeLength = treeCodeLength;
   }

	public String getModuleName() {
		return moduleName;
	}
	
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getModuleId() {
		return moduleId;
	}
	
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public boolean isTree() {
		return tree;
	}

	public void setTree(boolean tree) {
		this.tree = tree;
	}

	public boolean isHasActive() {
		return hasActive;
	}

	public void setHasActive(boolean hasActive) {
		this.hasActive = hasActive;
	}

	public boolean isHasDisplayOrder() {
		return hasDisplayOrder;
	}

	public void setHasDisplayOrder(boolean hasDisplayOrder) {
		this.hasDisplayOrder = hasDisplayOrder;
	}

}
