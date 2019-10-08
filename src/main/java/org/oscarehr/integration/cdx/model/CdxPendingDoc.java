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




package org.oscarehr.integration.cdx.model;

import org.oscarehr.common.model.AbstractModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cdx_pending_docs")
public class CdxPendingDoc extends AbstractModel<Integer> implements Serializable {

    public static final String error = "ERR";
    public static final String deleted = "DEL";
    public static final String status = "STS";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "doc_id")
    private String docId;
    @Basic(optional = false)
    @Column(name = "timestamp")
    private Date timestamp;
    @Basic(optional = false)
    @Column(name = "reason_code")
    private String reasonCode;
    @Basic(optional = false)
    @Column(name = "explanation")
    private String explanation;


    public CdxPendingDoc() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public static CdxPendingDoc createPendingDocStatus(String docId) {
        CdxPendingDoc pendDoc = new CdxPendingDoc();

        pendDoc.setTimestamp(new Date());
        pendDoc.setDocId(docId);
        pendDoc.setReasonCode(CdxPendingDoc.status);
        pendDoc.setExplanation("Pending distribution status.");

        return pendDoc;
    }
}