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

import java.util.ArrayList;

import com.quatro.util.KeyValueBean;

public class ReportTempCriValue {
    private int counter;
    private int templateNo;
    private String relation;
    private int fieldNo;
    private String op;
    private String val;
    private String valDesc;
//    private String ops;
    private Boolean required = false;
    private String fieldName;
    private ReportFilterValue filter;
    private ArrayList<KeyValueBean> operatorList;
    
    public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
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
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
//	public String getOps() {
//		return ops;
//	}
//	public void setOps(String ops) {
//		this.ops = ops;
//	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public Boolean getRequired() {
		return required;
	}
	public Boolean isRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public int getTemplateNo() {
		return templateNo;
	}
	public void setTemplateNo(int templateNo) {
		this.templateNo = templateNo;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getValDesc() {
		return valDesc;
	}
	public void setValDesc(String valDesc) {
		this.valDesc = valDesc;
	}
	public ArrayList<KeyValueBean> getOperatorList() {
		if(operatorList==null) operatorList= new ArrayList<KeyValueBean>();
		return operatorList;
	}
	public void setOperatorList(ArrayList<KeyValueBean> operatorList) {
		this.operatorList = operatorList;
	}
	public ReportFilterValue getFilter() {
		return filter;
	}
	public void setFilter(ReportFilterValue filter) {
		this.filter = filter;
	}
    
}
