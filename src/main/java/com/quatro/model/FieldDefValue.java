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
import org.caisi.model.BaseObject;

import com.quatro.common.KeyConstants;

public class FieldDefValue extends BaseObject{
		private String tableId;
	    private String fieldName;
	    private String fieldDesc;
	    private String fieldType;
	    private String lookupTable;           
	    private String fieldSQL;
	    private boolean editable;
	    private boolean auto;
	    private boolean unique;
	    private int genericIdx;
	    private int fieldIndex;
	    private Integer fieldLength;
	    
	    private String val = "";
	    private String valDesc = "";
	    
		public String getValDesc() {
			return valDesc;
		}

		public void setValDesc(String valDesc) {
			this.valDesc = valDesc;
		}

		public String getVal() {
			return val;
		}

		public void setVal(String val) {
			this.val = val;
		}

		public FieldDefValue() {
		}
	    
		public String getFieldDesc() {
			return fieldDesc;
		}
		public void setFieldDesc(String fieldDesc) {
			this.fieldDesc = fieldDesc;
		}
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public String getFieldSQL() {
			return fieldSQL;
		}
		public void setFieldSQL(String fieldSQL) {
			this.fieldSQL = fieldSQL;
		}
		public String getFieldType() {
			return fieldType;
		}
		public void setFieldType(String fieldType) {
			this.fieldType = fieldType;
		}
		public String getLookupTable() {
			return lookupTable;
		}
		public void setLookupTable(String lookupTable) {
			this.lookupTable = lookupTable;
		}
		public String getTableId() {
			return tableId;
		}
		public void setTableId(String tableId) {
			this.tableId = tableId;
		}

		public boolean isEditable() {
			return editable;
		}
		
		public void setEditable(boolean editable) {
			this.editable = editable;
		}

		public int getFieldIndex() {
			return fieldIndex;
		}

		public void setFieldIndex(int fieldIndex) {
			this.fieldIndex = fieldIndex;
		}

		public boolean isAuto() {
			return auto;
		}

		public void setAuto(boolean auto) {
			this.auto= auto;
		}

		public int getGenericIdx() {
			return genericIdx;
		}

		public void setGenericIdx(int genericIdx) {
			this.genericIdx = genericIdx;
		}

		public boolean isUnique() {
			return unique;
		}

		public void setUnique(boolean unique) {
			this.unique = unique;
		}

		public Integer getFieldLength() {
			return fieldLength;
		}

		public void setFieldLength(Integer fieldLength) {
			this.fieldLength = fieldLength;
		}
		public String getFieldLengthStr() {
			String result;
			if(fieldLength == null){
				result = KeyConstants.DEFAULT_FIELD_LENGTH_STRING;
			}else{
				result = fieldLength.toString();
			}
			return result;
		}
	}
