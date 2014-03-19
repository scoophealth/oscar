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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.common.model.Demographic;

/**
 * Represents an export document of a patient
 * 
 * @author oscar
 * 
 */
@Entity
@Table(name = "sharing_document_export")
public class DemographicExport extends AbstractModel<Integer> implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "document_type")
	private String documentType;
	
	@Column(name = "document")
	@Lob()
	private byte[] document;
	
	@OneToOne
	@JoinColumn(name = "demographic_no")
	private Demographic demographic;
	
	/**
	 * Initializes a new instance of a DemographicExport.
	 */
	public DemographicExport() {
	}

	/**
	 * Gets the id.
	 * @return Returns the id of the document.
	 */
	@Override
	public Integer getId() {
		return id;
	}
	
	/**
	 * Gets the document type of the document.
	 * @return Returns the document type of the document.
	 */
	public String getDocumentType() {
		return documentType;
	}

	/**
	 * Sets the document type.
	 * @param documentType The document type.
	 */
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	/**
	 * Gets the document.
	 * @return Returns the document.
	 */
	public byte[] getDocument() {
		return document;
	}

	/**
	 * Sets the document.
	 * @param document The document.
	 */
	public void setDocument(byte[] document) {
		this.document = document;
	}

	/**
	 * Gets the demographic for the document.
	 * @return Returns the Demographic object for the document.
	 */
	public Demographic getDemographic() {
		return demographic;
	}

	/**
	 * Sets the demographic for the document.
	 * @param demographic The demographic.
	 */
	public void setDemographic(Demographic demographic) {
		this.demographic = demographic;
	}
}
