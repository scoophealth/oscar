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


package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "publicKeys")
public class PublicKey extends AbstractModel<String> implements Serializable {

	@Id
	private String service;

	private String type;

	@Column(name = "pubKey")
	private String base64EncodedPublicKey;
	
	@Column(name = "privateKey")
	private String base64EncodedPrivateKey;
	
	
	/**
	 * This field is meant to be a corresponding professionalSpecialist entry.
	 * The purpose is to do 2-way communications, i.e. publicKeys is what some one
	 * uses to contact "our" server, if we want to sent back information, we would
	 * then look up the corresponding professional specialist entry to know what
	 * the contact information for their server is.
	 * 
	 * This value can be null. Null means we have no way to contact them back.
	 */
	private Integer matchingProfessionalSpecialistId=null;

	@Override
	public String getId() {
		return (service);
	}

	public String getService() {
    	return service;
    }

	/**
	 * The public key service name is this objects id
	 */
	public void setService(String service) {
    	this.service = service;
    }

	public String getBase64EncodedPublicKey() {
		return base64EncodedPublicKey;
	}

	public void setBase64EncodedPublicKey(String publicKey) {
		this.base64EncodedPublicKey = publicKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getMatchingProfessionalSpecialistId() {
    	return matchingProfessionalSpecialistId;
    }

	public void setMatchingProfessionalSpecialistId(Integer matchingProfessionalSpecialistId) {
    	this.matchingProfessionalSpecialistId = matchingProfessionalSpecialistId;
    }

	public String getBase64EncodedPrivateKey() {
    	return base64EncodedPrivateKey;
    }

	public void setBase64EncodedPrivateKey(String base64EncodedPrivateKey) {
    	this.base64EncodedPrivateKey = base64EncodedPrivateKey;
    }

}
