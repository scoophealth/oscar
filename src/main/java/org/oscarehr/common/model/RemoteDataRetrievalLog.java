package org.oscarehr.common.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class is a log table. It is used to keep track of what documents a provider has seen from a remote system. The purpose is that we need to be able to audit what has or has not been seen by the provider as he may make decisions based on what was
 * seen. In general no on will ever look in this table so it's going to be modeled like a write-only log. Little to no consideration will be made for read/reporting purposes, that's not the intent for this table. In general code should no be reading from
 * this table, you should not attempt to use this table as a cache for remote data. You can not gurantee that a remote server will return the same thing for the same key/request so duplicate entries for the same key must be logged. As an example if the
 * remote system has a bug that returns a random document for a single identical request... it should be noted here that a different document was returned. If some one is thinking of caching data for remote requests, it should be done aside from this log.
 * This log should be written to each time data is retrieved from a remote server, not if it's retrieved / viewed from a local cache like say if it's stored in the session space or an in memory cache.
 */
@Entity
public class RemoteDataRetrievalLog extends AbstractModel<Long> {
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
	private Calendar retrievalDate = new GregorianCalendar();

	/**
	 * what ever uniquely identifies the document on the remote server. It can be a numeric id or maybe <documentType>:<id>, like "message:1234", or more preferably <remoteServer>:<documentType>:<documentId> like "myoscar.myserver.org:message:1234". The
	 * general idea is that oscar can view documents from multiple sources, not just 1 myoscar server, also documentId's are not always unique as there can be imageId=444 and messageId=444. This fields is a string so it allows massive flexibility in
	 * shaping this key to be unique for the server the data is retrieved from. This field will be foreign keyed to the RemoteDataViewLogDocumentArchive table which should hold the actual document.
	 */
	@Column(nullable = false)
	private String remoteDocumentId;

	/**
	 * This is any rendition of the remote document. One suggestion is to store an xml rendition of the information. It is up to the caller
	 * to pick a rendition that is restoreable, i.e. xml can be viewed in plain text or base64 can be decoded, but java object serialisation can not be if the original class no longer exists.
	 */
	@Column(nullable = false)
	private byte[] remoteDocumentContents;

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

	public Calendar getRetrievalDate() {
		return retrievalDate;
	}

	public void setRetrievalDate(Calendar retrievalDate) {
		this.retrievalDate = retrievalDate;
	}

	public byte[] getRemoteDocumentContents() {
		return remoteDocumentContents;
	}

	public void setRemoteDocumentContents(byte[] remoteDocumentContents) {
		this.remoteDocumentContents = remoteDocumentContents;
	}

	/**
	 * This is a convenience method for when your document contents are provided as a string, it just converts the string to bytes
	 */
	public void setRemoteDocumentContents(String remoteDocumentContents) {
		setRemoteDocumentContents(remoteDocumentContents.getBytes());
	}

	public String getRemoteDocumentId() {
		return remoteDocumentId;
	}
	
	public void setRemoteDocumentId(String remoteDocumentId) {
		this.remoteDocumentId = remoteDocumentId;
	}
	
	/**
	 * This is a convenience method for MyOscar code which does the same thing as setRemoteDocumentId().
	 */
	public void setMyOscarRemoteDocumentId(String myOscarServerUrl, String documentType, Long objectId) {
		if (myOscarServerUrl==null || documentType==null || objectId==null) throw(new NullPointerException("All 3 parameters are required, myOscarServerUrl="+myOscarServerUrl+", documentType="+documentType+", objectId="+objectId));
		
		this.remoteDocumentId = myOscarServerUrl+':'+documentType+':'+objectId;
	}
}
