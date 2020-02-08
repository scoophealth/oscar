/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */


package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.oscarehr.integration.fhir.interfaces.ContactInterface;
import org.oscarehr.integration.fhir.resources.constants.ContactRelationship;
import org.oscarehr.integration.fhir.resources.constants.ContactType;

/**
 *
 * @author Jay Gallagher
 */
@Entity
@Table(name="clinic")
public class Clinic extends AbstractModel<Integer> implements Serializable, ContactInterface {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="clinic_no")
    private Integer id;
	@Column(name="clinic_name")
    private String clinicName;
	@Column(name="clinic_address")
    private String clinicAddress;
	@Column(name="clinic_city")
    private String clinicCity;
	@Column(name="clinic_postal")
    private String clinicPostal;
	@Column(name="clinic_phone")
    private String clinicPhone;
	@Column(name="clinic_fax")
    private String clinicFax;
	@Column(name="clinic_location_code")
    private String clinicLocationCode;
    private String status;
    @Column(name="clinic_province")
    private String clinicProvince;
    @Column(name="clinic_delim_phone")
    private String clinicDelimPhone;
    @Column(name="clinic_delim_fax")
    private String clinicDelimFax;

    @Transient
    private ContactRelationship contactRelationship;
    
    @Transient
    private ContactType contactType;
    
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /** Creates a new instance of UserProperty */
    public Clinic() {
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getClinicCity() {
        return clinicCity;
    }

    public void setClinicCity(String clinicCity) {
        this.clinicCity = clinicCity;
    }

    public String getClinicPostal() {
        return clinicPostal;
    }

    public void setClinicPostal(String clinicPostal) {
        this.clinicPostal = clinicPostal;
    }

    public String getClinicPhone() {
        return clinicPhone;
    }

    public void setClinicPhone(String clinicPhone) {
        this.clinicPhone = clinicPhone;
    }

    public String getClinicFax() {
        return clinicFax;
    }

    public void setClinicFax(String clinicFax) {
        this.clinicFax = clinicFax;
    }

    public String getClinicLocationCode() {
        return clinicLocationCode;
    }

    public void setClinicLocationCode(String clinicLocationCode) {
        this.clinicLocationCode = clinicLocationCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClinicProvince() {
        return clinicProvince;
    }

    public void setClinicProvince(String clinicProvince) {
        this.clinicProvince = clinicProvince;
    }

    public String getClinicDelimPhone() {
        return clinicDelimPhone;
    }

    public void setClinicDelimPhone(String clinicDelimPhone) {
        this.clinicDelimPhone = clinicDelimPhone;
    }

    public String getClinicDelimFax() {
        return clinicDelimFax;
    }

    public void setClinicDelimFax(String clinicDelimFax) {
        this.clinicDelimFax = clinicDelimFax;
    }

	@Override
	public void setContactRelationship(ContactRelationship contactRelationship) {
		this.contactRelationship = contactRelationship;		
	}

	@Override
	public ContactRelationship getContactRelationship() {
		return this.contactRelationship;
	}

	@Override
	public void setContactType(ContactType contactType) {
		this.contactType = contactType;		
	}

	@Override
	public ContactType getContactType() {
		return this.contactType;
	}

	@Override
	public void setLocationCode(String locationCode) {
		setClinicLocationCode(locationCode);		
	}

	@Override
	public String getLocationCode() {
		return getClinicLocationCode();
	}

	@Override
	public void setFirstName(String firstName) {
		// unused		
	}

	@Override
	public String getFirstName() {
		return null;
	}

	@Override
	public void setLastName(String lastName) {
		// unused		
	}

	@Override
	public String getLastName() {
		return null;
	}

	@Override
	public void setOrganizationName(String organizationName) {
		setClinicName(organizationName);	
	}

	@Override
	public String getOrganizationName() {
		return getClinicName();
	}

	@Override
	public void setAddress(String address) {
		setClinicAddress(address);		
	}

	@Override
	public String getAddress() {
		return getClinicAddress();
	}

	@Override
	public void setAddress2(String address2) {
		// unused
	}

	@Override
	public String getAddress2() {
		return null;
	}

	@Override
	public void setCity(String city) {
		this.setClinicCity(city);
		
	}

	@Override
	public String getCity() {
		return this.getClinicCity();
	}

	@Override
	public void setProvince(String province) {
		this.setClinicProvince(province);
	}

	@Override
	public String getProvince() {
		return this.getClinicProvince();
	}

	@Override
	public void setPostal(String postal) {
		this.setClinicPostal(postal);
	}

	@Override
	public String getPostal() {
		return this.getClinicPostal();
	}

	@Override
	public void setFax(String fax) {
		this.setClinicFax(fax);
	}

	@Override
	public String getFax() {
		return this.getClinicFax();
	}

	@Override
	public void setWorkPhone(String workphone) {
		this.setClinicPhone(workphone);
	}

	@Override
	public String getWorkPhone() {
		return this.getClinicPhone();
	}

	@Override
	public void setProviderCpso(String providerCPSO) {
		// unused
	}

	@Override
	public String getProviderCpso() {
		return null;
	}
	
	@Override
	public void setPhone(String phone) {
		this.setClinicPhone(phone);
	}

	@Override
	public String getPhone() {
		return this.getClinicPhone();
	}

    public String toString(){
    	 return ReflectionToStringBuilder.toString(this);
    }
}
