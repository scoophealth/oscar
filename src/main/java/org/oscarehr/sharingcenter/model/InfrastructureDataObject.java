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
import java.util.Comparator;

import javax.persistence.*;

import org.oscarehr.common.model.AbstractModel;


@Entity
@Table(name="sharing_infrastructure")
public class InfrastructureDataObject extends AbstractModel<Integer> implements Serializable{

	/**
	 * This comparator sorts EForm ascending based on the id
	 */
	public static final Comparator<InfrastructureDataObject> FORM_NAME_COMPARATOR = new Comparator<InfrastructureDataObject>() {
		public int compare(InfrastructureDataObject first, InfrastructureDataObject second) {
			return (first.id.compareTo(second.id));
		}
	};
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="alias")
	private String alias;
	
	@Column(name="common_name")
	private String commonName;
	
	@Column(name="organizational_unit")
	private String organizationalUnit;
	
	@Column(name="organization")
	private String organization;
	
	@Column(name="locality")
	private String locality;
	
	@Column(name="state")
	private String state;
	
	@Column(name="country")
	private String country;

	@Column(name="public_key")
	private String publicKey;
	
	@Column(name="private_key")
	private String privateKey;
	
	
	
	@Override
    public Integer getId() {
	    // TODO Auto-generated method stub
	    return this.id;
    }



	public String getAlias() {
	    return alias;
    }



	public void setAlias(String alias) {
	    this.alias = alias;
    }



	public String getCommonName() {
	    return commonName;
    }



	public void setCommonName(String commonName) {
	    this.commonName = commonName;
    }



	public String getOrganizationalUnit() {
	    return organizationalUnit;
    }



	public void setOrganizationalUnit(String organizationalUnit) {
	    this.organizationalUnit = organizationalUnit;
    }



	public String getOrganization() {
	    return organization;
    }



	public void setOrganization(String organization) {
	    this.organization = organization;
    }



	public String getLocality() {
	    return locality;
    }



	public void setLocality(String locality) {
	    this.locality = locality;
    }



	public String getState() {
	    return state;
    }



	public void setState(String state) {
	    this.state = state;
    }



	public String getCountry() {
	    return country;
    }

	public void setCountry(String country) {
	    this.country = country;
    }

	public String getBase64EncodedPublicKey() {
	    return publicKey;
    }
	
	public void setBase64EncodedPublicKey(String base64EncodedPublicKey) {
	    this.publicKey = base64EncodedPublicKey;
    }
	
	public String getBase64EncodedPrivateKey() {
	    return privateKey;
    }
	
	public void setBase64EncodedPrivateKey(String base64EncodedPrivateKey) {
	    this.privateKey = base64EncodedPrivateKey;
    }
	
	
	
}
