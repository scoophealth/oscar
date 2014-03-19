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
@Table(name = "sharing_clinic_info")
public class ClinicInfoDataObject extends AbstractModel<Integer> implements Serializable {

	/**
	 * This comparator sorts EForm ascending based on the id
	 */
	public static final Comparator<ClinicInfoDataObject> FORM_NAME_COMPARATOR = new Comparator<ClinicInfoDataObject>() {
		public int compare(ClinicInfoDataObject first, ClinicInfoDataObject second) {
			return (first.id.compareTo(second.id));
		}
	};
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="org_oid")
	private String oid;
	
	@Column(name="name")
	private String name;
	
	@Column(name="application_name")
	private String localAppName;
	
	@Column(name="facility_name")
	private String facilityName;
	
	@Column(name="universal_id")
	private String universalId;
	
	@Column(name="namespace")
	private String namespaceId;
	
	@Column(name="source_id")
	private String sourceId;
		
	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Integer getId() {
	    // TODO Auto-generated method stub
	    return id;
	}

	public String getLocalAppName() {
	    return localAppName;
    }

	public void setLocalAppName(String localAppName) {
	    this.localAppName = localAppName;
    }

	public String getFacilityName() {
	    return facilityName;
    }

	public void setFacilityName(String facilityName) {
	    this.facilityName = facilityName;
    }

	public String getUniversalId() {
	    return universalId;
    }

	public void setUniversalId(String universalId) {
	    this.universalId = universalId;
    }

	public String getNamespaceId() {
	    return namespaceId;
    }

	public void setNamespaceId(String namespaceId) {
	    this.namespaceId = namespaceId;
    }

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	

}
