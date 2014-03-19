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
import java.util.Date;

import javax.persistence.*;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "sharing_exported_doc")
public class ExportedDocument extends AbstractModel<Integer> implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "affinity_domain")
	private int affinityDomain;

	@Column(name = "demographic_no")
	private int demographicNo;

	@Column(name = "local_doc_id")
	private int localDocId;

	@Column(name = "document_type")
	private String documentType;

	@Column(name = "document_uid")
	private String documentUID;

	@Column(name = "document_uuid")
	private String documentUUID;

	@Column(name = "date_exported")
	@Temporal(TemporalType.DATE)
	private Date dateExported = new Date();

	public ExportedDocument() {
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

	public int getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}

	public int getLocalDocId() {
		return localDocId;
	}

	public void setLocalDocId(int localDocId) {
		this.localDocId = localDocId;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentUID() {
		return documentUID;
	}

	public void setDocumentUID(String documentUID) {
		this.documentUID = documentUID;
	}

	public String getDocumentUUID() {
		return documentUUID;
	}

	public void setDocumentUUID(String documentUUID) {
		this.documentUUID = documentUUID;
	}

	public Date getDateExported() {
		return dateExported;
	}

}
