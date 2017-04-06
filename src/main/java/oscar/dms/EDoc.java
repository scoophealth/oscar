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

package oscar.dms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.oscarehr.common.model.CtlDocument;
import org.oscarehr.common.model.CtlDocumentPK;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;

import oscar.MyDateFormat;
import oscar.OscarProperties;
import oscar.oscarTags.TagObject;
import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;

public class EDoc extends TagObject implements Comparable<EDoc> {
	private static final Logger logger = MiscUtils.getLogger();

	private String docId;
	private Integer remoteFacilityId;
	private String description = "";
	private String dateTimeStamp = "";
	private Date dateTimeStampAsDate = null;
	private String type = "";
	private String docClass = "";
	private String docSubClass = "";
	private String fileName = "";
	private String html = "";

	private String creatorId = "";
	private String responsibleId = "";
	private String source = "";
	private String sourceFacility = "";
	private Integer programId = -1;
	private char status;
	private String module = "";
	private String moduleId = "";
	private String docPublic = "0";
	private String contentType = "";
        private Date contentDateTime=null;
	private String observationDate = "";
	private String reviewerId = "";
	private String reviewDateTime = null;
	private Date reviewDateTimeDate = null;
	private String indivoIdx = null;
	private boolean indivoRegistered = false;
	private int numberOfPages = 0;
	private Integer appointmentNo = -1;
	private boolean restrictToProgram=false;
	private String fileSignature;
	
	private org.oscarehr.common.model.Document document;
	private CtlDocument ctlDocument;
	

	/** Creates a new instance of EDoc */
	public EDoc() {
	}

	public EDoc(String description, String type, String fileName, String html, String creatorId, String responsibleId, String source, char status, String observationDate, String reviewerId, String reviewDateTime, String module, String moduleId) {
		this.setDescription(description.trim());
		this.setType(type.trim());
		this.setFileName(fileName.trim());
		this.setHtml(html);
		this.setCreatorId(creatorId);
		this.setResponsibleId(responsibleId);
		this.setSource(source);
		this.setStatus(status);
		this.setModule(module.trim());
		this.setModuleId(moduleId.trim());
		this.setObservationDate(observationDate);
		this.setReviewerId(reviewerId);
		this.setReviewDateTime(reviewDateTime);
		preliminaryProcessing();
	}

	public EDoc(String description, String type, String fileName, String html, String creatorId, String responsibleId, String source, char status, String observationDate, String reviewerId, String reviewDateTime, String module, String moduleId, int numberOfPages) {
		this.setDescription(description.trim());
		this.setType(type.trim());
		this.setFileName(fileName.trim());
		this.setHtml(html);
		this.setCreatorId(creatorId);
		this.setResponsibleId(responsibleId);
		this.setSource(source);
		this.setStatus(status);
		this.setModule(module.trim());
		this.setModuleId(moduleId.trim());
		this.setObservationDate(observationDate);
		this.setReviewerId(reviewerId);
		this.setReviewDateTime(reviewDateTime);
		this.setNumberOfPages(numberOfPages);
		preliminaryProcessing();
	}

	public EDoc(String description, String type, String fileName, String html, String creatorId, String responsibleId, String source, char status, String observationDate, String reviewerId, String reviewDateTime, String module, String moduleId, boolean updateFilename) {
		this.setDescription(description.trim());
		this.setType(type.trim());
		this.setFileName(fileName.trim());
		this.setHtml(html);
		this.setCreatorId(creatorId);
		this.setResponsibleId(responsibleId);
		this.setSource(source);
		this.setStatus(status);
		this.setModule(module.trim());
		this.setModuleId(moduleId.trim());
		this.setObservationDate(observationDate);
		this.setReviewerId(reviewerId);
		this.setReviewDateTime(reviewDateTime);

		if (updateFilename) {
			preliminaryProcessing();
		}
	}

	/**
	 *Comparable based on document id
	 */
	public int compareTo(EDoc o) {
		EDoc doc = o;
		int ret;
		int id1 = Integer.parseInt(docId);
		int id2 = Integer.parseInt(doc.getDocId());

		if (id1 < id2) ret = -1;
		else if (id1 > id2) ret = 1;
		else ret = 0;

		return ret;
	}

	
	@Override
		public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docId == null) ? 0 : docId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		EDoc other = (EDoc) obj;
		if (docId == null) {
			if (other.docId != null) return false;
		} else if (!docId.equals(other.docId)) return false;
		return true;
	}

	private void preliminaryProcessing() {
		this.dateTimeStamp = EDocUtil.getDmsDateTime();
		this.setDateTimeStampAsDate(EDocUtil.getDmsDateTimeAsDate());
                this.setContentDateTime(EDocUtil.getDmsDateTimeAsDate());
		if (fileName.length() != 0) {
			String filenamePrefix = UtilDateUtilities.DateToString(new Date(), "yyyyMMdd") + UtilDateUtilities.DateToString(new Date(), "HHmmss");
			this.fileName = filenamePrefix + fileName;
		}
	}

	public String getFilePath() {
		String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		return (path + "/" + this.getFileName());

	}

	public OutputStream getFileOutputStream() throws FileNotFoundException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(getFilePath());
		} catch (FileNotFoundException fnfe) {
			logger.error("Could not write to the document container", fnfe);
			throw fnfe;
		}
		return os;
	}

	public byte[] getFileBytes() throws IOException {
		return (FileUtils.readFileToByteArray(new File(getFilePath())));
	}

	public void writeLocalFile(FormFile docFile, String fileName) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = docFile.getInputStream();
			os = this.getFileOutputStream();
			byte[] buf = new byte[128 * 1024];
			int i = 0;
			while ((i = is.read(buf)) != -1) {
				os.write(buf, 0, i);
			}
		} catch (IOException ioe) {
			MiscUtils.getLogger().error("Error", ioe);
			if (is != null) is.close();
			if (os != null) os.close();
			throw ioe;
		}

		if (is != null) is.close();
		if (os != null) os.close();

	}

	// Getter/Setter methods...

	public String getDocId() {
		return docId;
	}

	public Integer getRemoteFacilityId() {
		return (remoteFacilityId);
	}

	public void setRemoteFacilityId(Integer remoteFacilityId) {
		this.remoteFacilityId = remoteFacilityId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getDescription() {
		return description;
	}

	public Date getReviewDateTimeDate() {
		return (reviewDateTimeDate);
	}

	public void setReviewDateTimeDate(Date reviewDateTimeDate) {
		this.reviewDateTimeDate = reviewDateTimeDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDateTimeStamp() {
		return dateTimeStamp;
	}

	public void setDateTimeStamp(String dateTimeStamp) {
		this.dateTimeStamp = dateTimeStamp;
	}
        
        public Date getContentDateTime() {
		return (contentDateTime);
	}

	public void setContentDateTime(Date contentDateTime) {
		this.contentDateTime = contentDateTime;
	}
        
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public String getCreatorName() {
		String creatorName = EDocUtil.getProviderName(creatorId);
		return creatorName;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getResponsibleId() {
		return responsibleId;
	}

	public String getResponsibleName() {
		String responsibleName = EDocUtil.getProviderName(responsibleId);
		return responsibleName;
	}

	public void setResponsibleId(String responsibleId) {
		this.responsibleId = responsibleId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceFacility() {
		return sourceFacility;
	}

	public void setSourceFacility(String sourceFacility) {
		this.sourceFacility = sourceFacility;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getDocPublic() {
		return docPublic;
	}

	// docPublic = "checked" for the edoc to be public
	public void setDocPublic(String docPublic) {
		if (docPublic.equalsIgnoreCase("checked")) this.docPublic = "1";
		else if (docPublic == null || docPublic.length() == 0) this.docPublic = "0";
		else this.docPublic = docPublic;
	}

	/**
	 *Returns true if document a PDF.
	 */
	public boolean isPDF() {
		if (this.contentType != null && this.contentType.contains("/pdf")) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if this document's content type is that of an image.	
	 * @return true if document is an image and false otherwise
	 */
	public boolean isImage() {
		return this.contentType != null && !isPDF() && this.contentType.toLowerCase().contains("image/");
	}

	/**
	 * Returns true if this document is printable to PDF format.
	 * @return true if this document is printable to PDF format and false otherwise
	 */
	public boolean isPrintable() {
		// At this time only PDF  and image files are supported.
		return isPDF() || isImage();
	}

	public String getObservationDate() {
		return observationDate;
	}

	public void setObservationDate(String observationDate) {
		this.observationDate = observationDate;
	}

	public void setObservationDate(Date observationDate) {
		String formattedDate = UtilDateUtilities.DateToString(observationDate, EDocUtil.DMS_DATE_FORMAT);
		this.observationDate = formattedDate;
	}

	public void setIndivoIdx(String idx) {
		indivoIdx = idx;
	}

	public String getIndivoIdx() {
		return indivoIdx;
	}

	public void registerIndivo() {
		indivoRegistered = true;
	}

	public boolean isInIndivo() {
		return indivoRegistered;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public Integer getAppointmentNo() {
		return appointmentNo;
	}

	public void setAppointmentNo(Integer appointmentNo) {
		this.appointmentNo = appointmentNo;
	}

	public Date getDateTimeStampAsDate() {
		return dateTimeStampAsDate;
	}

	public void setDateTimeStampAsDate(Date dateTimeStampAsDate) {
		this.dateTimeStampAsDate = dateTimeStampAsDate;
	}

	public String getReviewerId() {
		return reviewerId;
	}

	public String getReviewerName() {
		String reviewerName = EDocUtil.getProviderName(reviewerId);
		return reviewerName;
	}

	public String getReviewerOhip() {
		Provider provider = EDocUtil.getProvider(reviewerId);
		if (provider != null) {
			return provider.getOhipNo();
		}
		return "";
	}

	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}

	public String getReviewDateTime() {
		return reviewDateTime;
	}

	public void setReviewDateTime(String reviewDateTime) {
		this.reviewDateTime = reviewDateTime;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int n) {
		this.numberOfPages = n;
	}

	public String getFileSignature() {
		return fileSignature;
	}

	public void setFileSignature(String fileSignature) {
		this.fileSignature = fileSignature;
	}

	public boolean isRestrictToProgram() {
		return restrictToProgram;
	}

	public void setRestrictToProgram(boolean restrictToProgram) {
		this.restrictToProgram = restrictToProgram;
	}

	public Document getDocument() {
		
		document = new Document();
		document.setDoctype(this.getType());
		document.setDocClass(this.getDocClass());
		document.setDocSubClass(this.getDocSubClass());
		document.setDocdesc(this.getDescription());
		document.setDocxml(this.getHtml());
		document.setDocfilename(this.getFileName());
		document.setDoccreator(this.getCreatorId());
		document.setSource(this.getSource());
		document.setSourceFacility(this.getSourceFacility());
		document.setResponsible(this.getResponsibleId());
		document.setProgramId(this.getProgramId());
		document.setUpdatedatetime(this.getDateTimeStampAsDate());
        document.setContentdatetime(this.getContentDateTime());
		document.setStatus(this.getStatus());
		document.setContenttype(this.getContentType());
		document.setPublic1(ConversionUtils.fromIntString(this.getDocPublic()));
		document.setObservationdate(MyDateFormat.getSysDate(this.getObservationDate()));
		document.setNumberofpages(this.getNumberOfPages());
		document.setAppointmentNo(this.getAppointmentNo());
		document.setRestrictToProgram(this.isRestrictToProgram());
		document.setFileSignature( getFileSignature() );
		return document;
	}

	public CtlDocument getCtlDocument() {
		
		ctlDocument = new CtlDocument();
		CtlDocumentPK cdpk = new CtlDocumentPK();
		cdpk.setModule( this.getModule() );
		cdpk.setModuleId( Integer.parseInt( this.getModuleId() ) );
		
		ctlDocument.setId(cdpk);
		ctlDocument.setStatus( String.valueOf( this.getStatus() ) );

		return ctlDocument;
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
