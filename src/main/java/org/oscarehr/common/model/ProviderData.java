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
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "provider")
public class ProviderData extends AbstractModel<String> implements Serializable {

	@Id
	@Column(name = "provider_no")
	private String id = null;
	@Column(name = "last_name")
	private String lastName = null;
	@Column(name = "first_name")
	private String firstName = null;
	@Column(name = "provider_type")
	private String providerType = null;
	@Column(name = "specialty")
	private String specialty = null;
	@Column(name = "team")
	private String team = null;
	@Column(name = "sex")
	private String sex = null;
	@Column(name = "dob")
	@Temporal(TemporalType.DATE)
	private Date dob = null;
	@Column(name = "address")
	private String address = null;
	@Column(name = "phone")
	private String phone = null;
	@Column(name = "work_phone")
	private String workPhone = null;
	@Column(name = "ohip_no")
	private String ohipNo = null;
	@Column(name = "rma_no")
	private String rmaNo = null;
	@Column(name = "billing_no")
	private String billingNo = null;
	@Column(name = "hso_no")
	private String hsoNo = null;
	@Column(name = "status")
	private String status = null;
	@Column(name = "comments")
	private String comments = null;
	@Column(name = "provider_activity")
	private String providerActivity = null;
	@Column(name = "practitionerNo")
	private String practitionerNo = null;
	@Column(name = "init")
	private String init = null;
	@Column(name = "job_title")
	private String jobTitle = null;
	@Column(name = "email")
	private String email = null;
	@Column(name = "title")
	private String title = null;
	@Column(name = "lastUpdateUser")
	private String lastUpdateUser = null;
	@Column(name = "lastUpdateDate")
	@Temporal(TemporalType.DATE)
	private Date lastUpdateDate = null;
	@Column(name = "signed_confidentiality")
	@Temporal(TemporalType.DATE)
	private Date signedConfidentiality = null;
        @Column(name = "supervisor")
        private String supervisor;
      
	public ProviderData() {}
	
        @Override
	public String getId() {
		return id;
	}
	public void set(String providerNo) {
		id = providerNo;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String s) {
		lastName = s;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String s) {
		firstName = s;
	}

	public String getProviderType() {
		return providerType;
	}
	public void setProviderType(String s) {
		providerType = s;
	}

	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String s) {
		specialty = s;
	}

	public String getTeam() {
		return team;
	}
	public void setTeam(String s) {
		team = s;
	}

	public String getSex() {
		return sex;
	}
	public void setSex(String s) {
		sex = s;
	}

	public Date getDob() {
		return dob;
	}
	public void setDob(Date d) {
		dob = d;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String s) {
		address = s;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String s) {
		phone = s;
	}

	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String s) {
		workPhone = s;
	}

	public String getOhipNo() {
		return ohipNo;
	}
	
	public void setOhipNo(String s) {
		ohipNo = s;
	}

	public String getRmaNo() {
		return rmaNo;
	}
	public void setRmaNo(String s) {
		rmaNo = s;
	}

	public String getBillingNo() {
		return billingNo;
	}
	public void setBillingNo(String s) {
		billingNo = s;
	}

	public String getHsoNo() {
		return hsoNo;
	}
	public void setHsoNo(String s) {
		hsoNo = s;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String s) {
		status = s;
	}

	public String getComments() {
		return comments;
	}
	public void setComments(String s) {
		comments = s;
	}

	public String getProviderActivity() {
		return providerActivity;
	}
	public void setProviderActivity(String s) {
		providerActivity = s;
	}

	public String getPractitionerNo() {
		return practitionerNo;
	}
	public void setPractitionerNo(String s) {
		practitionerNo = s;
	}

	public String getInit() {
		return init;
	}
	public void setInit(String s) {
		init = s;
	}

	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String s) {
		jobTitle = s;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String s) {
		email = s;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String s) {
		title = s;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}
	public void setLastUpdateUser(String s) {
		lastUpdateUser = s;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date d) {
		lastUpdateDate = d;
	}

	public Date getSignedConfidentiality() {
		return signedConfidentiality;
	}
	public void setSignedConfidentiality(Date d) {
		signedConfidentiality = d;
	}
	 
        public String getSupervisor() {
            return supervisor;
        }
        
        public void setSupervisor(String supervisor) {
            this.supervisor = supervisor;
        }
        
        public boolean equals(Object object) {
            if( !(object instanceof ProviderData) ) {
                return false;
            }
            
            ProviderData other = (ProviderData) object;
            return ProviderData.ProviderNoComparator.compare(this, other) == 0;
        }
        
	public static final Comparator<ProviderData> LastNameComparator = new Comparator<ProviderData>() {
        public int compare(ProviderData pd1, ProviderData pd2) {
        	return pd1.getLastName().compareTo(pd2.getLastName());
        }
    }; 
	public static final Comparator<ProviderData> FirstNameComparator = new Comparator<ProviderData>() {
        public int compare(ProviderData pd1, ProviderData pd2) {
        	return pd1.getFirstName().compareTo(pd2.getFirstName());
        }
    }; 
	public static final Comparator<ProviderData> ProviderNoComparator = new Comparator<ProviderData>() {
        public int compare(ProviderData pd1, ProviderData pd2) {
        	return pd1.getId().compareTo(pd2.getId());
        }
    };
	
}
