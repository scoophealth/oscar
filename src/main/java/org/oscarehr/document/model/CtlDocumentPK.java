/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.document.model;

import java.io.Serializable;


public  class CtlDocumentPK implements Serializable {

	protected int hashCode = Integer.MIN_VALUE;

	private java.lang.Integer documentNo;
	private java.lang.String module;


	public CtlDocumentPK () {}
	
	public CtlDocumentPK (
		java.lang.Integer documentNo,
		java.lang.String module) {

		this.setDocumentNo(documentNo);
		this.setModule(module);
	}


	/**
	 * Return the value associated with the column: document_no
	 */
	public java.lang.Integer getDocumentNo () {
		return documentNo;
	}

	/**
	 * Set the value related to the column: document_no
	 * @param documentNo the document_no value
	 */
	public void setDocumentNo (java.lang.Integer documentNo) {
		this.documentNo = documentNo;
	}



	/**
	 * Return the value associated with the column: module
	 */
	public java.lang.String getModule () {
		return module;
	}

	/**
	 * Set the value related to the column: module
	 * @param module the module value
	 */
	public void setModule (java.lang.String module) {
		this.module = module;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof CtlDocumentPK)) return false;
		else {
			CtlDocumentPK mObj = (CtlDocumentPK) obj;
			if (null != this.getDocumentNo() && null != mObj.getDocumentNo()) {
				if (!this.getDocumentNo().equals(mObj.getDocumentNo())) {
					return false;
				}
			}
			else {
				return false;
			}
			if (null != this.getModule() && null != mObj.getModule()) {
				if (!this.getModule().equals(mObj.getModule())) {
					return false;
				}
			}
			else {
				return false;
			}
			return true;
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			String sb = new String();
			if (null != this.getDocumentNo()) {
				sb=sb+new Integer(this.getDocumentNo().hashCode()).toString();
				sb=sb+":";
			}
			else {
				return super.hashCode();
			}
			if (null != this.getModule()) {
				sb+=new Integer(this.getModule().hashCode()).toString();
				sb+=":";
			}
			else {
				return super.hashCode();
			}
			this.hashCode = sb.hashCode();
		}
		return this.hashCode;
	}


}
