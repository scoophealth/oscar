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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "cdx_attachment")
public class CdxAttachment extends AbstractModel<Integer> implements Serializable {

    public static final String pdf = "PDF";
    public static final String rtf = "RTF";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "document")
    private Integer document;
    @Basic(optional = false)
    @Column(name = "reference")
    private String reference;
    @Basic(optional = false)
    @Column(name = "attachment_type")
    private String attachmentType;
    @Basic(optional = false)
    @Lob @Column(name = "content")
    private byte[] content;


    public CdxAttachment() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer internalId) {
        this.id = internalId;
    }

    public Integer getDocument() {
        return document;
    }

    public void setDocument(Integer document) {
        this.document = document;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}