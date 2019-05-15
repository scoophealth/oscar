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

import ca.uvic.leadlab.obibconnector.facades.receive.IDocument;
import org.oscarehr.common.model.AbstractModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cdx_provenance")
public class CdxProvenance extends AbstractModel<Integer> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "doc_id")
    private String documentId;
    @Column(name = "msg_id")
    private String msgId;
    @Column(name = "version")
    private int version;
    @Basic(optional = false)
    @Column(name = "effective_time")
    private Date effectiveTime;
    @Column(name = "parent_doc")
    private String parentDoc;
    @Column(name = "set_id")
    private String setId;
    @Column(name = "in_fulfillment_of_id")
    private String inFulfillmentOfId;
    @Basic(optional = false)
    @Column(name = "kind")
    private String kind;
    @Basic(optional = false)
    @Column(name = "action")
    private String action;
    @Column(name = "log")
    private long log;
    @Column(name = "payload")
    private String payload;
    @Column(name = "document_no")
    private Integer documentNo;

    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    @Column(name = "warnings")
    private String warnings;

    public Integer getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(Integer documentNo) {
        this.documentNo = documentNo;
    }

    public CdxProvenance() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getParentDoc() {
        return parentDoc;
    }

    public void setParentDoc(String parentDoc) {
        this.parentDoc = parentDoc;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getInFulfillmentOfId() {
        return inFulfillmentOfId;
    }

    public void setInFulfillmentOfId(String inFulfillmentOfId) {
        this.inFulfillmentOfId = inFulfillmentOfId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getLog() {
        return log;
    }

    public void setLog(long log) {
        this.log = log;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void populate(IDocument doc) {
        this.documentId = doc.getDocumentID();
        this.version = doc.getVersion();
        this.effectiveTime = doc.getEffectiveTime();
        this.parentDoc = doc.getParentDocumentID();
        this.setId = doc.getSetId();
        this.inFulfillmentOfId = doc.getInFulFillmentOfId();
        this.kind = doc.getLoincCodeDisplayName();
        this.payload = doc.getContents();
    }
}

