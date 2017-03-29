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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.io.FileUtils;

/**
 *
 * @author jackson
 */
@Entity
@Table(name = "document")
@NamedQueries({
    @NamedQuery(name = "Document.findAll", query = "SELECT d FROM Document d"),
    @NamedQuery(name = "Document.findByDocumentNo", query = "SELECT d FROM Document d WHERE d.documentNo = :documentNo"),
    @NamedQuery(name = "Document.findByDoctype", query = "SELECT d FROM Document d WHERE d.doctype = :doctype"),
    @NamedQuery(name = "Document.findByDocdesc", query = "SELECT d FROM Document d WHERE d.docdesc = :docdesc"),
    @NamedQuery(name = "Document.findByDocfilename", query = "SELECT d FROM Document d WHERE d.docfilename = :docfilename"),
    @NamedQuery(name = "Document.findByDoccreator", query = "SELECT d FROM Document d WHERE d.doccreator = :doccreator"),
    @NamedQuery(name = "Document.findByContentdatetime", query = "SELECT d FROM Document d WHERE d.contentdatetime = :contentdatetime"),
    @NamedQuery(name = "Document.findByResponsible", query = "SELECT d FROM Document d WHERE d.responsible = :responsible"),
    @NamedQuery(name = "Document.findBySource", query = "SELECT d FROM Document d WHERE d.source = :source"),
    @NamedQuery(name = "Document.findByProgramId", query = "SELECT d FROM Document d WHERE d.programId = :programId"),
    @NamedQuery(name = "Document.findByUpdatedatetime", query = "SELECT d FROM Document d WHERE d.updatedatetime = :updatedatetime"),
    @NamedQuery(name = "Document.findByStatus", query = "SELECT d FROM Document d WHERE d.status = :status"),
    @NamedQuery(name = "Document.findByContenttype", query = "SELECT d FROM Document d WHERE d.contenttype = :contenttype"),
    @NamedQuery(name = "Document.findByPublic1", query = "SELECT d FROM Document d WHERE d.public1 = :public1"),
    @NamedQuery(name = "Document.findByObservationdate", query = "SELECT d FROM Document d WHERE d.observationdate = :observationdate"),
    @NamedQuery(name = "Document.findByReviewer", query = "SELECT d FROM Document d WHERE d.reviewer = :reviewer"),
    @NamedQuery(name = "Document.findByReviewdatetime", query = "SELECT d FROM Document d WHERE d.reviewdatetime = :reviewdatetime"),
    @NamedQuery(name = "Document.findByNumberOfPages", query = "SELECT d FROM Document d WHERE d.numberofpages = :numberofpages"),
    @NamedQuery(name = "Document.findPhotosByAppointmentNo", query = "SELECT d FROM Document d WHERE d.appointmentNo = :appointmentNo and d.doctype='photo'")})
public class Document extends AbstractModel<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;
   
    public static final char STATUS_ACTIVE='A';
    public static final char STATUS_DELETED='D';
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "document_no")
    private Integer documentNo;

   // private Integer id;
    @Column(name = "doctype")
    private String doctype;

    private String docClass;

    private String docSubClass;

    @Basic(optional = false)
    @Column(name = "docdesc")
    private String docdesc;
    @Lob
    @Column(name = "docxml")
    private String docxml;
    @Basic(optional = false)
    @Column(name = "docfilename")
    private String docfilename;
    @Basic(optional = false)
    @Column(name = "doccreator")
    private String doccreator;
    @Basic(optional = false)
    @Column(name = "responsible")
    private String responsible;
    @Column(name = "source")
    private String source;
    private String sourceFacility;
    @Column(name = "program_id")
    private Integer programId;
    @Column(name = "updatedatetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedatetime;
    @Basic(optional = false)
    @Column(name = "status")
    private char status;
    @Basic(optional = false)
    @Column(name = "contenttype")
    private String contenttype;
    @Column(name = "contentdatetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contentdatetime;    
    @Basic(optional = false)
    @Column(name = "public1")
    private int public1;
    @Column(name = "observationdate")
    @Temporal(TemporalType.DATE)
    private Date observationdate;
    @Column(name = "reviewer")
    private String reviewer;
    @Column(name = "reviewdatetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewdatetime;    
    @Column(name = "number_of_pages")
    private Integer numberofpages;
    @Column(name="appointment_no")
    private Integer appointmentNo;

    private Boolean restrictToProgram=false;
    
    private String fileSignature;
    
    public Document() {
    }

    public Document(Integer documentNo) {
        this.documentNo = documentNo;
       // this.id=this.documentNo;
    }

    public Document(Integer documentNo, String docdesc, String docfilename, String doccreator, String responsible, char status, String contenttype, int public1, int numberOfPages) {
        this.documentNo = documentNo;
        this.docdesc = docdesc;
        this.docfilename = docfilename;
        this.doccreator = doccreator;
        this.responsible = responsible;
        this.status = status;
        this.contenttype = contenttype;
        this.public1 = public1;
        this.numberofpages = numberOfPages;
//        this.id=this.documentNo;
    }

    public Integer getId(){
        return documentNo;
    }
    public Integer getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(Integer documentNo) {
        this.documentNo = documentNo;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getDocdesc() {
        return docdesc;
    }

    public void setDocdesc(String docdesc) {
        this.docdesc = docdesc;
    }

    public String getDocxml() {
        return docxml;
    }

    public void setDocxml(String docxml) {
        this.docxml = docxml;
    }

    public String getDocfilename() {
        return docfilename;
    }

    public void setDocfilename(String docfilename) {
        this.docfilename = docfilename;
    }

    public String getDoccreator() {
        return doccreator;
    }

    public void setDoccreator(String doccreator) {
        this.doccreator = doccreator;
    }
    
    public Date getContentdatetime() {
        return contentdatetime;
    }

    public void setContentdatetime(Date contentdatetime) {
        this.contentdatetime = contentdatetime;
    }
    
    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public Date getUpdatedatetime() {
        return updatedatetime;
    }

    public void setUpdatedatetime(Date updatedatetime) {
        this.updatedatetime = updatedatetime;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public int getPublic1() {
        return public1;
    }

    public void setPublic1(int public1) {
        this.public1 = public1;
    }

    public Date getObservationdate() {
        return observationdate;
    }

    public void setObservationdate(Date observationdate) {
        this.observationdate = observationdate;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public Date getReviewdatetime() {
        return reviewdatetime;
    }

    public void setReviewdatetime(Date reviewdatetime) {
        this.reviewdatetime = reviewdatetime;
    }

    public Integer getNumberofpages() {
        return numberofpages;
    }

    public void setNumberofpages(int numberOfPages) {
        this.numberofpages = numberOfPages;
    }

	public Integer getAppointmentNo() {
		return appointmentNo;
	}

	public void setAppointmentNo(Integer appointmentNo) {
		this.appointmentNo = appointmentNo;
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

	public String getSourceFacility() {
    	return sourceFacility;
    }

	public void setSourceFacility(String sourceFacility) {
    	this.sourceFacility = sourceFacility;
    }

	/**
	 * @returns a string representing the path of the file on disk, i.e. document_dir+'/'+filename
	 */
	public String getDocumentFileFullPath()
	{
		String docDir = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		return(docDir+'/'+docfilename);
	}
	
	public byte[] getDocumentFileContentsAsBytes() throws IOException
	{
		return(FileUtils.readFileToByteArray(new File(getDocumentFileFullPath())));
	}
	
	@PrePersist
	@PreUpdate
	protected void jpaUpdateDate() {
		this.updatedatetime = new Date();
	}

	public Boolean isRestrictToProgram() {
		return restrictToProgram;
	}

	public void setRestrictToProgram(Boolean restrictToProgram) {
		this.restrictToProgram = restrictToProgram;
	}

	public String getFileSignature() {
		return fileSignature;
	}

	public void setFileSignature(String fileSignature) {
		this.fileSignature = fileSignature;
	}
	
	
	
}
