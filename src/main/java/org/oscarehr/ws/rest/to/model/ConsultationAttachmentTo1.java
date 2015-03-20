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
package org.oscarehr.ws.rest.to.model;

import java.io.Serializable;

public class ConsultationAttachmentTo1 implements Serializable {
	public static final String TYPE_DOC = "D";
	public static final String TYPE_EFORM = "E";
	public static final String TYPE_LAB = "L";
	public static final boolean ATTACHED = true;

	private static final long serialVersionUID = 1L;
	
	private int documentNo;
	private String documentType;
	private String documentTypeDisplay;
	private boolean attached;
	private String displayName;
	private String shortName;
	private String url;
	
	
	public ConsultationAttachmentTo1() {}
	
	public ConsultationAttachmentTo1(int documentNo, String documentType, boolean attached, String displayName, String url) {
		setDocumentNo(documentNo);
		setDocumentType(documentType);
		setAttached(attached);
		setDisplayName(displayName);
		setUrl(url);
	}

	public int getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(int documentNo) {
		this.documentNo = documentNo;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
		
		if (TYPE_DOC.equals(documentType)) documentTypeDisplay = "Document";
		else if (TYPE_EFORM.equals(documentType)) documentTypeDisplay = "EForm";
		else if (TYPE_LAB.equals(documentType)) documentTypeDisplay = "Lab Result";
	}

	public String getDocumentTypeDisplay() {
		return documentTypeDisplay;
	}
	
	public void setDocumentTypeDisplay(String documentTypeDisplay) {
		this.documentTypeDisplay = documentTypeDisplay;
	}

	public boolean isAttached() {
		return attached;
	}

	public void setAttached(boolean attached) {
		this.attached = attached;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		shortName = (displayName!=null && displayName.length()>20) ? displayName.substring(0, 17)+"..." : displayName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}