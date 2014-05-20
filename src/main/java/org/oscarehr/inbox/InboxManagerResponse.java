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
package org.oscarehr.inbox;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import oscar.oscarLab.ca.on.LabResultData;

public class InboxManagerResponse {
	private Integer pageNum;
	private Hashtable docType;
	private Hashtable patientDocs;
	private String providerNo;
	private String searchProviderNo;
	private Hashtable patientIdNames;
	private Hashtable docStatus;
	private String patientIdStr;
	private Hashtable<String,List<String>> typeDocLab;
	private Integer demographicNo;
	private String ackStatus;
	private List<LabResultData> labdocs;
	private Hashtable patientNumDoc;
	private Integer totalDocs;
	private Integer totalHl7;
	private List<String> normals;
	private List<String> abnormals;
	private Integer totalNumDocs;
	private String patientIdNamesStr;
	private Date oldestLab;
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Hashtable getDocType() {
		return docType;
	}
	public void setDocType(Hashtable docType) {
		this.docType = docType;
	}
	public Hashtable getPatientDocs() {
		return patientDocs;
	}
	public void setPatientDocs(Hashtable patientDocs) {
		this.patientDocs = patientDocs;
	}
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public String getSearchProviderNo() {
		return searchProviderNo;
	}
	public void setSearchProviderNo(String searchProviderNo) {
		this.searchProviderNo = searchProviderNo;
	}
	public Hashtable getPatientIdNames() {
		return patientIdNames;
	}
	public void setPatientIdNames(Hashtable patientIdNames) {
		this.patientIdNames = patientIdNames;
	}
	public Hashtable getDocStatus() {
		return docStatus;
	}
	public void setDocStatus(Hashtable docStatus) {
		this.docStatus = docStatus;
	}
	public String getPatientIdStr() {
		return patientIdStr;
	}
	public void setPatientIdStr(String patientIdStr) {
		this.patientIdStr = patientIdStr;
	}
	public Hashtable<String, List<String>> getTypeDocLab() {
		return typeDocLab;
	}
	public void setTypeDocLab(Hashtable<String, List<String>> typeDocLab) {
		this.typeDocLab = typeDocLab;
	}
	public Integer getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}
	public String getAckStatus() {
		return ackStatus;
	}
	public void setAckStatus(String ackStatus) {
		this.ackStatus = ackStatus;
	}
	public List<LabResultData> getLabdocs() {
		return labdocs;
	}
	public void setLabdocs(List<LabResultData> labdocs) {
		this.labdocs = labdocs;
	}
	public Hashtable getPatientNumDoc() {
		return patientNumDoc;
	}
	public void setPatientNumDoc(Hashtable patientNumDoc) {
		this.patientNumDoc = patientNumDoc;
	}
	public Integer getTotalDocs() {
		return totalDocs;
	}
	public void setTotalDocs(Integer totalDocs) {
		this.totalDocs = totalDocs;
	}
	public Integer getTotalHl7() {
		return totalHl7;
	}
	public void setTotalHl7(Integer totalHl7) {
		this.totalHl7 = totalHl7;
	}
	public List<String> getNormals() {
		return normals;
	}
	public void setNormals(List<String> normals) {
		this.normals = normals;
	}
	public List<String> getAbnormals() {
		return abnormals;
	}
	public void setAbnormals(List<String> abnormals) {
		this.abnormals = abnormals;
	}
	public Integer getTotalNumDocs() {
		return totalNumDocs;
	}
	public void setTotalNumDocs(Integer totalNumDocs) {
		this.totalNumDocs = totalNumDocs;
	}
	public String getPatientIdNamesStr() {
		return patientIdNamesStr;
	}
	public void setPatientIdNamesStr(String patientIdNamesStr) {
		this.patientIdNamesStr = patientIdNamesStr;
	}
	public Date getOldestLab() {
		return oldestLab;
	}
	public void setOldestLab(Date oldestLab) {
		this.oldestLab = oldestLab;
	}	
}