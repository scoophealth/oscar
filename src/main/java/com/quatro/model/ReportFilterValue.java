/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.model;

import org.caisi.model.BaseObject;

public class ReportFilterValue extends BaseObject{
    private int reportNo;
    private int fieldNo;
    private String fieldName;
    private String fieldDesc;
    private String fieldType;
    private String lookupTable;           
    private String op;
    private Boolean isTree;
    private String fieldSQL;
    private String note;

	public ReportFilterValue() {
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
	public int getFieldNo() {
		return fieldNo;
	}
	public void setFieldNo(int fieldNo) {
		this.fieldNo = fieldNo;
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
	public Boolean isTree() {
		return isTree;
	}
	public void setTree(Boolean isTree) {
		this.isTree = isTree;
	}
	public String getLookupTable() {
		return lookupTable;
	}
	public void setLookupTable(String lookupTable) {
		this.lookupTable = lookupTable;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public int getReportNo() {
		return reportNo;
	}
	public void setReportNo(int reportNo) {
		this.reportNo = reportNo;
	}
    
}
