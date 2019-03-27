/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.integration.cdx.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "cdx_document")
public class CdxDocument extends AbstractModel<Integer> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "document_no")
    private Integer documentNo;
    @Basic(optional = false)
    @Column(name = "template_id")
    private String templateId;
    @Basic(optional = false)
    @Column(name = "template_name")
    private String templateName;
    @Basic(optional = false)
    @Column(name = "document_oid")
    private String documentOid;
    @Basic(optional = false)
    @Column(name = "loinc_code")
    private String loincCode;
    @Basic(optional = false)
    @Column(name = "loinc_name")
    private String loincName;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Column(name = "authoring_time")
    private Date authoringTime;
    @Column(name = "device")
    private String device;
    @Column(name = "effective_time")
    private Date effectiveTime;
    @Column(name = "custodian")
    private String custodian;
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "status_code")
    private String StatusCode;
    @Column(name = "observation_date")
    private Date observationDate;
    @Column(name = "procedure_name")
    private String procedureName;
    @Column(name = "parent_doc_id")
    private String parentDocId;
    @Column(name = "patient_encounter_id")
    private String patientEncounterId;
    @Column(name = "admission_date")
    private Date admissionDate;
    @Column(name = "discharge_date")
    private Date dischargeDate;
    @Column(name = "disposition")
    private String disposition;
    @Column(name = "contents")
    private String contents;


    public CdxDocument() {
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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDocumentOid() {
        return documentOid;
    }

    public void setDocumentOid(String documentOid) {
        this.documentOid = documentOid;
    }

    public String getLoincCode() {
        return loincCode;
    }

    public void setLoincCode(String loincCode) {
        this.loincCode = loincCode;
    }

    public String getLoincName() {
        return loincName;
    }

    public void setLoincName(String loincName) {
        this.loincName = loincName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getAuthoringTime() {
        return authoringTime;
    }

    public void setAuthoringTime(Date authoringTime) {
        this.authoringTime = authoringTime;
    }

    public String getAuthoringTimeAsString() {

        if (authoringTime == null)
            return "UNK time";
        else return authoringTime.toString();
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getEffectiveTimeAsString() {

        if (effectiveTime == null)
            return "UNK time";
        else return effectiveTime.toString();
    }

    public String getCustodian() {
        return custodian;
    }

    public void setCustodian(String custodian) {
        this.custodian = custodian;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public Date getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(Date observationDate) {
        this.observationDate = observationDate;
    }

    public String getObservationDateAsString() {

        if (observationDate == null)
            return "UNK date";
        else return observationDate.toString();
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getParentDocId() {
        return parentDocId;
    }

    public void setParentDocId(String parentDocId) {
        this.parentDocId = parentDocId;
    }

    public String getPatientEncounterId() {
        return patientEncounterId;
    }

    public void setPatientEncounterId(String patientEncounterId) {
        this.patientEncounterId = patientEncounterId;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public String getAdmissionDateAsString() {

        if (admissionDate == null)
            return "UNK date";
        else return admissionDate.toString();
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getDischargeDateAsString() {

        if (dischargeDate == null)
            return "UNK date";
        else return dischargeDate.toString();
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

}

