/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */
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
