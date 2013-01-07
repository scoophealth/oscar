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

/**
 * This is an object that contains data related to the document table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * 
 *  table="document"
 */

public class Document implements Serializable {

	public static String REF = "Document";
	public static String PROP_STATUS = "status";
	public static String PROP_CONTENTTYPE = "contenttype";
	public static String PROP_OBSERVATIONDATE = "observationdate";
	public static String PROP_UPDATEDATETIME = "updatedatetime";
	public static String PROP_DOCXML = "docxml";
	public static String PROP_PUBLIC = "public";
	public static String PROP_DOCDESC = "docdesc";
	public static String PROP_DOCCREATOR = "doccreator";
	public static String PROP_DOCFILENAME = "docfilename";
	public static String PROP_ID = "id";
	public static String PROP_DOCTYPE = "doctype";
	public static String PROP_NUMBEROFPAGES = "numberOfPages";

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String doctype;
	private java.lang.String docdesc;
	private java.lang.String docxml;
	private java.lang.String docfilename;
	private java.lang.String doccreator;
	private java.util.Date updatedatetime;
	private java.lang.String status;
	private java.lang.String contenttype;
	private java.lang.Byte m_public; //
	private java.util.Date observationdate;
	private Integer numberOfPages; //
	private String source;
	private java.util.Date reviewdatetime;
	private int appointmentNo;
	private String docClass;
	private String docSubClass;
	private String responsible;
	private String sourceFacility;
	private int programId;
	private String reviewer;

	// constructors
	public Document() {
	}

	/**
	 * Constructor for primary key
	 */
	public Document(java.lang.Long id) {
		this.setId(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Document(java.lang.Long id, java.lang.String docdesc, java.lang.String docfilename, java.lang.String doccreator, java.lang.String status, java.lang.String contenttype, java.lang.Byte m_public) {
		this.setId(id);
		this.setDocdesc(docdesc);
		this.setDocfilename(docfilename);
		this.setDoccreator(doccreator);
		this.setStatus(status);
		this.setContenttype(contenttype);
		this.setPublic(m_public);
	}

	public java.lang.Byte getM_public() {
		return m_public;
	}

	public void setM_public(java.lang.Byte m_public) {
		this.m_public = m_public;
	}

	public String getDocClass() {
		return docClass;
	}

	public void setDocClass(String docClass) {
		this.docClass = docClass;
	}

	public String getDocSubClass() {
		return docSubClass;
	}

	public void setDocSubClass(String docSubClass) {
		this.docSubClass = docSubClass;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public String getSourceFacility() {
		return sourceFacility;
	}

	public void setSourceFacility(String sourceFacility) {
		this.sourceFacility = sourceFacility;
	}

	public int getProgramId() {
		return programId;
	}

	public void setProgramId(int programId) {
		this.programId = programId;
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public java.util.Date getReviewdatetime() {
		return reviewdatetime;
	}

	public void setReviewdatetime(java.util.Date reviewdatetime) {
		this.reviewdatetime = reviewdatetime;
	}

	public int getAppointmentNo() {
		return appointmentNo;
	}

	public void setAppointmentNo(int appointmentNo) {
		this.appointmentNo = appointmentNo;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Return the unique identifier of this class
	 * 
	 *  generator-class="native"
	 *  column="document_no"
	 */
	public java.lang.Long getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId(java.lang.Long id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: doctype
	 */
	public java.lang.String getDoctype() {
		return doctype;
	}

	/**
	 * Set the value related to the column: doctype
	 * @param doctype the doctype value
	 */
	public void setDoctype(java.lang.String doctype) {
		this.doctype = doctype;
	}

	/**
	 * Return the value associated with the column: docdesc
	 */
	public java.lang.String getDocdesc() {
		return docdesc;
	}

	/**
	 * Set the value related to the column: docdesc
	 * @param docdesc the docdesc value
	 */
	public void setDocdesc(java.lang.String docdesc) {
		this.docdesc = docdesc;
	}

	/**
	 * Return the value associated with the column: docxml
	 */
	public java.lang.String getDocxml() {
		return docxml;
	}

	/**
	 * Set the value related to the column: docxml
	 * @param docxml the docxml value
	 */
	public void setDocxml(java.lang.String docxml) {
		this.docxml = docxml;
	}

	/**
	 * Return the value associated with the column: docfilename
	 */
	public java.lang.String getDocfilename() {
		return docfilename;
	}

	/**
	 * Set the value related to the column: docfilename
	 * @param docfilename the docfilename value
	 */
	public void setDocfilename(java.lang.String docfilename) {
		this.docfilename = docfilename;
	}

	/**
	 * Return the value associated with the column: doccreator
	 */
	public java.lang.String getDoccreator() {
		return doccreator;
	}

	/**
	 * Set the value related to the column: doccreator
	 * @param doccreator the doccreator value
	 */
	public void setDoccreator(java.lang.String doccreator) {
		this.doccreator = doccreator;
	}

	/**
	 * Return the value associated with the column: updatedatetime
	 */
	public java.util.Date getUpdatedatetime() {
		return updatedatetime;
	}

	/**
	 * Set the value related to the column: updatedatetime
	 * @param updatedatetime the updatedatetime value
	 */
	public void setUpdatedatetime(java.util.Date updatedatetime) {
		this.updatedatetime = updatedatetime;
	}

	/**
	 * Return the value associated with the column: status
	 */
	public java.lang.String getStatus() {
		return status;
	}

	/**
	 * Set the value related to the column: status
	 * @param status the status value
	 */
	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	/**
	 * Return the value associated with the column: contenttype
	 */
	public java.lang.String getContenttype() {
		return contenttype;
	}

	/**
	 * Set the value related to the column: contenttype
	 * @param contenttype the contenttype value
	 */
	public void setContenttype(java.lang.String contenttype) {
		this.contenttype = contenttype;
	}

	/**
	 * Return the value associated with the column: public
	 */
	public java.lang.Byte getPublic() {
		return m_public;
	}

	/**
	 * Set the value related to the column: public
	 * @param m_public the public value
	 */
	public void setPublic(java.lang.Byte m_public) {
		this.m_public = m_public;
	}

	/**
	 * Return the value associated with the column: observationdate
	 */
	public java.util.Date getObservationdate() {
		return observationdate;
	}

	/**
	 * Set the value related to the column: observationdate
	 * @param observationdate the observationdate value
	 */
	public void setObservationdate(java.util.Date observationdate) {
		this.observationdate = observationdate;
	}

	/**
	 * Return the value associated with the column: number_of_pages
	 */
	public Integer getNumberOfPages() {
		return numberOfPages;
	}

	/**
	 * Set the value related to the column: number_of_pages
	 * @param numberOfPages the numberOfPages value
	 */
	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public String toString() {
		return "doctype " + doctype + " docdesc " + docdesc + " docxml " + docxml + " docfilename " + docfilename + " doccreator " + doccreator + " updatedatetime " + updatedatetime + " status " + status + " contenttype " + contenttype + " m_public " + m_public + " observationdate " + observationdate + " source " + source;
	}

	public boolean equals(Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof Document)) return false;
		else {
			Document document = (Document) obj;
			if (null == this.getId() || null == document.getId()) return false;
			else return (this.getId().equals(document.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}
}
