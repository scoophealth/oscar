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
package org.oscarehr.sharingcenter.model;

import java.io.Serializable;

import javax.persistence.*;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.MappingCodeType;

import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.util.MiscUtils;

@Entity
@Table(name = "sharing_mapping_edoc")
public class EDocMapping extends AbstractModel<Integer> implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "affinity_domain")
    private int affinityDomain;

    @Column(name = "doc_type")
    private String docType;

    @Column(name = "source")
    private String source;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "class_code")
    private MappingCode classCode;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "type_code")
    private MappingCode typeCode;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "format_code")
    private MappingCode formatCode;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "content_type_code")
    private MappingCode contentTypeCode;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "event_code_list")
    private MappingCode eventCodeList;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "folder_code_list")
    private MappingCode folderCodeList;

    public EDocMapping() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getAffinityDomain() {
        return affinityDomain;
    }

    public void setAffinityDomain(int affinityDomain) {
        this.affinityDomain = affinityDomain;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MappingCode getClassCode() {
        return classCode;
    }

    public void setClassCode(MappingCode classCode) {
        this.classCode = classCode;
    }

    public MappingCode getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(MappingCode typeCode) {
        this.typeCode = typeCode;
    }

    public MappingCode getFormatCode() {
        return formatCode;
    }

    public void setFormatCode(MappingCode formatCode) {
        this.formatCode = formatCode;
    }

    public MappingCode getContentTypeCode() {
        return contentTypeCode;
    }

    public void setContentTypeCode(MappingCode contentTypeCode) {
        this.contentTypeCode = contentTypeCode;
    }

    public MappingCode getEventCodeList() {
        return eventCodeList;
    }

    public void setEventCodeList(MappingCode eventCodeList) {
        this.eventCodeList = eventCodeList;
    }

    public MappingCode getFolderCodeList() {
        return folderCodeList;
    }

    public void setFolderCodeList(MappingCode folderCodeList) {
        this.folderCodeList = folderCodeList;
    }
    
    public CodeValue getCode(MappingCodeType codeType) {
        CodeValue retVal = null;
        
        if (codeType.equals(MappingCodeType.ClassCode) && classCode != null) {
            retVal = classCode.generateCodeValue();
        } else if (codeType.equals(MappingCodeType.ContentTypeCode) && contentTypeCode != null) {
            retVal = contentTypeCode.generateCodeValue();
        } else if (codeType.equals(MappingCodeType.EventCodeList) && eventCodeList != null) {
            retVal = eventCodeList.generateCodeValue();
        } else if (codeType.equals(MappingCodeType.FolderCodeList) && folderCodeList != null ) {
            retVal = folderCodeList.generateCodeValue();
        } else if (codeType.equals(MappingCodeType.FormatCode) && formatCode != null) {
            retVal = formatCode.generateCodeValue();
        } else if (codeType.equals(MappingCodeType.TypeCode) && typeCode != null) {
            retVal = typeCode.generateCodeValue();
        } else {
            MiscUtils.getLogger().warn("Unsupported Code " + codeType.getCode());
            retVal = new CodeValue("Unknown Code " + codeType.getCode(), "Unknown CodeSystem", "Unknown");
        }
        
        return retVal;
    }

}
