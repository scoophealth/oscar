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


package org.oscarehr.common.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class is a log table. It is used to keep track of what documents a provider has sent or retrieved from a remote system. The purpose is that we need to be able to audit what has or has not been sent or seen by the provider. In general no on will
 * ever look in this table so it's going to be modeled like a write-only log. Little to no consideration will be made for read/reporting purposes, that's not the intent for this table. In general code should no be reading from this table, you should not
 * attempt to use this table as a cache for remote data. You should not use this to determine what data has been synchronised with a server. This log should be written to each time data is retrieved from a remote server, not if it's retrieved / viewed from
 * a local cache like say if it's stored in the session space or an in memory cache.
 */
@Entity
public class RemoteDataLog extends AbstractModel<Long> {
	public static enum Action {
		SEND, RETRIEVE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String providerNo;

	/**
	 * Value is defaulted to the time the object is instantiated.
	 */
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar actionDate = new GregorianCalendar();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Action action;

	/**
	 * What ever uniquely identifies the document. An example maybe <remoteServerUrl>:<documentType>:<documentId> like "myoscar.myserver.org:8080:message:1234". The general idea is that oscar can view documents from multiple sources, not just 1 myoscar
	 * server, also documentId's are not always unique as there can be imageId=444 and messageId=444. This fields is a string so it allows massive flexibility in shaping this key to be unique for the server the data is retrieved from. For data being sent,
	 * this could be something like "myoscar.myserver.org:8080:message:", the url can still signify where it went and in the case of messages there is no local persistence of it so there's no id, if the example were a note, then the note id could be put there like "myoscar.myserver.org:8080:note:442"
	 */
	@Column(nullable = false)
	private String documentId;

	/**
	 * This is any rendition of the remote document. One suggestion is to store an xml rendition of the information. It is up to the caller to pick a rendition that is restoreable, i.e. xml can be viewed in plain text or base64 can be decoded, but java
	 * object serialisation can not be if the original class no longer exists. toString().getBytes() may suffice if the string rendition captures all the information.
	 */
	@Column(nullable = false)
	private byte[] documentContents;

	@PreUpdate
	@PreRemove
	protected void jpaPreventModification() {
		throw (new UnsupportedOperationException("Remove/update is not allowed for this type of item."));
	}

	@Override
	public Long getId() {
		return (id);
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Calendar getActionDate() {
		return (actionDate);
	}

	public void setActionDate(Calendar actionDate) {
		this.actionDate = actionDate;
	}

	public byte[] getDocumentContents() {
		return (documentContents);
	}

	public void setDocumentContents(byte[] documentContents) {
		this.documentContents = documentContents;
	}

	/**
	 * This is a convenience method for when your document contents are provided as a string, it just converts the string to bytes
	 */
	public void setDocumentContents(String remoteDocumentContents) {
		setDocumentContents(remoteDocumentContents.getBytes());
	}

	public String getDocumentId() {
		return (documentId);
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public Action getAction() {
		return (action);
	}

	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * This is a convenience method for building a documentId string
	 */
	public void setDocumentId(String myOscarServerUrl, String documentType, Object objectId) {
		if (myOscarServerUrl == null || documentType == null) throw (new NullPointerException("All url and type parameters are required, myOscarServerUrl=" + myOscarServerUrl + ", documentType=" + documentType + ", objectId=" + objectId));

		setDocumentId(myOscarServerUrl + ':' + documentType + ':' + objectId);
	}
}
