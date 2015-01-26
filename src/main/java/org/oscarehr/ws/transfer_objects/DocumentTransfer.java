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

package org.oscarehr.ws.transfer_objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.CtlDocument;
import org.oscarehr.common.model.Document;
import org.oscarehr.managers.DocumentManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.BeanUtils;

public final class DocumentTransfer {
	private static Logger logger = MiscUtils.getLogger();

	private Integer documentNo;
	private String doctype;
	private String docClass;
	private String docSubClass;
	private String docdesc;
	private String docxml;
	private String docfilename;
	private String doccreator;
	private String responsible;
	private String source;
	private String sourceFacility;
	private Integer programId;
	private Date updatedatetime;
	private char status;
	private String contenttype;
	private int public1;
	private Date observationdate;
	private String reviewer;
	private Date reviewdatetime;
	private Integer numberofpages;
	private Integer appointmentNo;

	private String ctlModule;
	private Integer ctlModuleId;
	private String ctlStatus;

	private byte[] fileContents;

	public Integer getDocumentNo() {
		return (documentNo);
	}

	public void setDocumentNo(Integer documentNo) {
		this.documentNo = documentNo;
	}

	public String getDoctype() {
		return (doctype);
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	public String getDocClass() {
		return (docClass);
	}

	public void setDocClass(String docClass) {
		this.docClass = docClass;
	}

	public String getDocSubClass() {
		return (docSubClass);
	}

	public void setDocSubClass(String docSubClass) {
		this.docSubClass = docSubClass;
	}

	public String getDocdesc() {
		return (docdesc);
	}

	public void setDocdesc(String docdesc) {
		this.docdesc = docdesc;
	}

	public String getDocxml() {
		return (docxml);
	}

	public void setDocxml(String docxml) {
		this.docxml = docxml;
	}

	public String getDocfilename() {
		return (docfilename);
	}

	public void setDocfilename(String docfilename) {
		this.docfilename = docfilename;
	}

	public String getDoccreator() {
		return (doccreator);
	}

	public void setDoccreator(String doccreator) {
		this.doccreator = doccreator;
	}

	public String getResponsible() {
		return (responsible);
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public String getSource() {
		return (source);
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceFacility() {
		return (sourceFacility);
	}

	public void setSourceFacility(String sourceFacility) {
		this.sourceFacility = sourceFacility;
	}

	public Integer getProgramId() {
		return (programId);
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public Date getUpdatedatetime() {
		return (updatedatetime);
	}

	public void setUpdatedatetime(Date updatedatetime) {
		this.updatedatetime = updatedatetime;
	}

	public char getStatus() {
		return (status);
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getContenttype() {
		return (contenttype);
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}

	public int getPublic1() {
		return (public1);
	}

	public void setPublic1(int public1) {
		this.public1 = public1;
	}

	public Date getObservationdate() {
		return (observationdate);
	}

	public void setObservationdate(Date observationdate) {
		this.observationdate = observationdate;
	}

	public String getReviewer() {
		return (reviewer);
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public Date getReviewdatetime() {
		return (reviewdatetime);
	}

	public void setReviewdatetime(Date reviewdatetime) {
		this.reviewdatetime = reviewdatetime;
	}

	public Integer getNumberofpages() {
		return (numberofpages);
	}

	public void setNumberofpages(Integer numberofpages) {
		this.numberofpages = numberofpages;
	}

	public Integer getAppointmentNo() {
		return (appointmentNo);
	}

	public void setAppointmentNo(Integer appointmentNo) {
		this.appointmentNo = appointmentNo;
	}

	public String getCtlModule() {
		return (ctlModule);
	}

	public void setCtlModule(String ctlModule) {
		this.ctlModule = ctlModule;
	}

	public Integer getCtlModuleId() {
		return (ctlModuleId);
	}

	public void setCtlModuleId(Integer ctlModuleId) {
		this.ctlModuleId = ctlModuleId;
	}

	public String getCtlStatus() {
		return (ctlStatus);
	}

	public void setCtlStatus(String ctlStatus) {
		this.ctlStatus = ctlStatus;
	}

	public byte[] getFileContents() {
		return (fileContents);
	}

	public void setFileContents(byte[] fileContents) {
		this.fileContents = fileContents;
	}

	/**
	 * ctlDocument can be null
	 */
	public static DocumentTransfer toTransfer(Document document, CtlDocument ctlDocument) throws IOException {
		if (document == null) return (null);

		DocumentTransfer documentTransfer = new DocumentTransfer();

		BeanUtils.copyProperties(document, documentTransfer);

		if (ctlDocument != null) {
			documentTransfer.setCtlModule(ctlDocument.getId().getModule());
			documentTransfer.setCtlModuleId(ctlDocument.getId().getModuleId());
			documentTransfer.setCtlStatus(ctlDocument.getStatus());
		}

		documentTransfer.setFileContents(document.getDocumentFileContentsAsBytes());

		return (documentTransfer);
	}

	public static DocumentTransfer[] getTransfers(LoggedInInfo loggedInInfo, List<Document> documents) {
		DocumentManager documentManager = SpringUtils.getBean(DocumentManager.class);

		ArrayList<DocumentTransfer> results = new ArrayList<DocumentTransfer>();

		for (Document document : documents) {
			try {
				CtlDocument ctlDocument = documentManager.getCtlDocumentByDocumentId(loggedInInfo, document.getId());
				DocumentTransfer transfer = DocumentTransfer.toTransfer(document, ctlDocument);
				results.add(transfer);
			} catch (IOException e) {
				logger.error("Unexpected error", e);
			}
		}

		return (results.toArray(new DocumentTransfer[0]));
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
