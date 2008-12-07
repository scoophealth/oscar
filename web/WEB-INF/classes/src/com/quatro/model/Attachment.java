package com.quatro.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Attachment implements Serializable {
	private static final long serialVersionUID = 1L;

	private int hashCode = Integer.MIN_VALUE;// primary key

	private Integer id;

	private String subject;

	private String privacyCd;

	private String providerNo;

	private Integer checkOut;

	private String checkoutUserId;

	private Calendar checkOutDate;

	private String docType;

	private String fileName;

	private Integer moduleId;

	private String refNo;

	private String fileType;

	private String viewId;

	private String viewRefNo;

	private Calendar revDate;
	private Integer fileSize;
	private AttachmentText attText;

	private String providerDesc;
	
	private String checkoutUserDesc;
	private String docDesc;
	private String moduleDesc;
	private Integer refProgramId;
	private Integer fileCount;
	public Integer getFileCount() {
		return fileCount;
	}

	public void setFileCount(Integer fileCount) {
		this.fileCount = fileCount;
	}

	public Integer getRefProgramId() {
		return refProgramId;
	}

	public void setRefProgramId(Integer refProgramId) {
		this.refProgramId = refProgramId;
	}

	public String getCheckoutUserDesc() {
		return checkoutUserDesc;
	}

	public void setCheckoutUserDesc(String checkoutUserDesc) {
		this.checkoutUserDesc = checkoutUserDesc;
	}
    public Date getRevDt(){
     return revDate.getTime(); 
    }
	public String getDocDesc() {
		return docDesc;
	}

	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}

	public String getModuleDesc() {
		return moduleDesc;
	}

	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}

	public String getProviderDesc() {
		return providerDesc;
	}

	public void setProviderDesc(String providerDesc) {
		this.providerDesc = providerDesc;
	}

	public AttachmentText getAttText() {
		return attText;
	}

	public void setAttText(AttachmentText attText) {
		this.attText = attText;
	}

	public Integer getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Integer checkOut) {
		this.checkOut = checkOut;
	}

	public Calendar getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Calendar checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getCheckoutUserId() {
		return checkoutUserId;
	}

	public void setCheckoutUserId(String checkoutUserId) {
		this.checkoutUserId = checkoutUserId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getPrivacyCd() {
		return privacyCd;
	}

	public void setPrivacyCd(String privacyCd) {
		this.privacyCd = privacyCd;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public Calendar getRevDate() {
		return revDate;
	}

	public void setRevDate(Calendar revDate) {
		this.revDate = revDate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getViewRefNo() {
		return viewRefNo;
	}

	public void setViewRefNo(String viewRefNo) {
		this.viewRefNo = viewRefNo;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof Attachment))
			return false;
		else {
			Attachment attObj = (Attachment) obj;
			if (null == this.getId() || null == attObj.getId())
				return false;
			else
				return (this.getId().equals(attObj.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":"
						+ this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

}
