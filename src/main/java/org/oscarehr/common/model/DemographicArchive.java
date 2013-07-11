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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "demographicArchive")
public class DemographicArchive extends AbstractModel<Long> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id = null;
	@Column(name = "demographic_no")
	private Integer demographicNo = null;
	@Column(name = "title")
	private String title = null;
	@Column(name = "last_name")
	private String lastName = null;
	@Column(name = "first_name")
	private String firstName = null;
	@Column(name = "address")
	private String address = null;
	@Column(name = "city")
	private String city = null;
	@Column(name = "province")
	private String province = null;
	@Column(name = "postal")
	private String postal = null;
	@Column(name = "phone")
	private String phone = null;
	@Column(name = "phone2")
	private String phone2 = null;
	@Column(name = "email")
	private String email = null;
	private String myOscarUserName = null;
	@Column(name = "year_of_birth")
	private String yearOfBirth = null;
	@Column(name = "month_of_birth")
	private String monthOfBirth = null;
	@Column(name = "date_of_birth")
	private String dateOfBirth = null;
	@Column(name = "hin")
	private String hin = null;
	@Column(name = "ver")
	private String ver = null;
	@Column(name = "roster_status")
	private String rosterStatus = null;
	@Column(name = "roster_date")
	@Temporal(TemporalType.DATE)
	private Date rosterDate = null;
	@Column(name = "roster_termination_date")
	@Temporal(TemporalType.DATE)
	private Date rosterTerminationDate = null;
	@Column(name = "roster_termination_reason")
	private String rosterTerminationReason = null;
	@Column(name = "patient_status")
	private String patientStatus = null;
	@Column(name = "patient_status_date")
	@Temporal(TemporalType.DATE)
	private Date patientStatusDate = null;
	@Column(name = "date_joined")
	@Temporal(TemporalType.DATE)
	private Date dateJoined = null;
	@Column(name = "chart_no")
	private String chartNo = null;
	@Column(name = "official_lang")
	private String officialLanguage = null;
	@Column(name = "spoken_lang")
	private String spokenLanguage = null;
	@Column(name = "provider_no")
	private String providerNo = null;
	@Column(name = "sex")
	private String sex = null;
	@Column(name = "end_date")
	@Temporal(TemporalType.DATE)
	private Date endDate = null;
	@Column(name = "eff_date")
	@Temporal(TemporalType.DATE)
	private Date effDate = null;
	@Column(name = "pcn_indicator")
	private String pcnIndicator = null;
	@Column(name = "hc_type")
	private String hcType = null;
	@Column(name = "hc_renew_date")
	@Temporal(TemporalType.DATE)
	Date hcRenewDate = null;
	@Column(name = "family_doctor")
	private String familyDoctor = null;
	@Column(name = "alias")
	private String alias = null;
	@Column(name = "previousAddress")
	private String previousAddress = null;
	@Column(name = "children")
	private String children = null;
	@Column(name = "sourceOfIncome")
	private String sourceOfIncome = null;
	@Column(name = "citizenship")
	private String citizenship = null;
	@Column(name = "sin")
	private String sin = null;
	@Column(name = "country_of_origin")
	private String countryOfOrigin = null;
	@Column(name = "newsletter")
	private String newsletter = null;
	@Column(name = "anonymous")
	private String anonymous = null;
	@Column(name = "lastUpdateUser")
	private String lastUpdateUser = null;
	@Column(name = "lastUpdateDate")
	@Temporal(TemporalType.DATE)
	private Date lastUpdateDate = null;

	public DemographicArchive() {}
	
	public DemographicArchive(Demographic demographic) {
		this.address = demographic.getAddress();
		this.alias = demographic.getAlias();
		this.anonymous = demographic.getAnonymous();
		this.chartNo = demographic.getChartNo();
		this.children = demographic.getChildren();
		this.citizenship = demographic.getCitizenship();
		this.city = demographic.getCity();
		this.countryOfOrigin = demographic.getCountryOfOrigin();
		this.dateJoined = demographic.getDateJoined();
		this.dateOfBirth = demographic.getDateOfBirth();
		this.demographicNo = demographic.getDemographicNo();
		this.effDate = demographic.getEffDate();
		this.email = demographic.getEmail();
		this.endDate = demographic.getEndDate();
		this.familyDoctor = demographic.getFamilyDoctor();
		this.firstName = demographic.getFirstName();
		this.hcRenewDate = demographic.getHcRenewDate();
		this.hcType = demographic.getHcType();
		this.hin = demographic.getHin();
		this.lastName = demographic.getLastName();
		this.lastUpdateDate = demographic.getLastUpdateDate();
		this.lastUpdateUser = demographic.getLastUpdateUser();
		this.monthOfBirth = demographic.getMonthOfBirth();
		this.myOscarUserName = demographic.getMyOscarUserName();
		this.newsletter = demographic.getNewsletter();
		this.officialLanguage = demographic.getOfficialLanguage();
		this.patientStatus = demographic.getPatientStatus();
		this.patientStatusDate = demographic.getPatientStatusDate();
		this.pcnIndicator = demographic.getPcnIndicator();
		this.phone = demographic.getPhone();
		this.phone2 = demographic.getPhone2();
		this.postal = demographic.getPostal();
		this.previousAddress = demographic.getPreviousAddress();
		this.providerNo = demographic.getProviderNo();
		this.province = demographic.getProvince();
		this.rosterDate = demographic.getRosterDate();
		this.rosterStatus = demographic.getRosterStatus();
		this.rosterTerminationDate = demographic.getRosterTerminationDate();
		this.rosterTerminationReason = demographic.getRosterTerminationReason();
		this.sex = demographic.getSex();
		this.sin = demographic.getSin();
		this.sourceOfIncome = demographic.getSourceOfIncome();
		this.spokenLanguage = demographic.getSpokenLanguage();
		this.title = demographic.getTitle();
		this.ver = demographic.getVer();
		this.yearOfBirth = demographic.getYearOfBirth();
	}

	public Integer getDemographicNo() {
	    return this.demographicNo;
	}
	public void setDemographicNo(Integer i) {
	    this.demographicNo = i;
	}

	public String getTitle() {
        return this.title;
    }
	public void setTitle(String s) {
        this.title = s;
    }

		public String getLastName() {
		    return this.lastName;
		}
		public void setLastName(String s) {
		    this.lastName = s;
		}

		public String getFirstName() {
		    return this.firstName;
		}
		public void setFirstName(String s) {
		    this.firstName = s;
		}

		public String getAddress() {
		    return this.address;
		}
		public void setAddress(String s) {
		    this.address = s;
		}

		public String getCity() {
		    return this.city;
		}
		public void setCity(String s) {
		    this.city = s;
		}

		public String getProvince() {
		    return this.province;
		}
		public void setProvince(String s) {
		    this.province = s;
		}

		public String getPostal() {
		    return this.postal;
		}
		public void setPostal(String s) {
		    this.postal = s;
		}

		public String getPhone() {
		    return this.phone;
		}
		public void setPhone(String s) {
		    this.phone = s;
		}

		public String getPhone2() {
		    return this.phone2;
		}
		public void setPhone2(String s) {
		    this.phone2 = s;
		}

		public String getEmail() {
		    return this.email;
		}
		public void setEmail(String s) {
		    this.email = s;
		}

		public String getMyOscarUserName() {
			return (myOscarUserName);
		}

		public void setMyOscarUserName(String myOscarUserName) {
			this.myOscarUserName = myOscarUserName;
		}

		public String getYearOfBirth() {
		    return this.yearOfBirth;
		}
		public void setYearOfBirth(String s) {
		    this.yearOfBirth = s;
		}

		public String getMonthOfBirth() {
		    return this.monthOfBirth;
		}
		public void setMonthOfBirth(String s) {
		    this.monthOfBirth = s;
		}

		public String getDateOfBirth() {
		    return this.dateOfBirth;
		}
		public void setDateOfBirth(String s) {
		    this.dateOfBirth = s;
		}

		public String getHin() {
		    return this.hin;
		}
		public void setHin(String s) {
		    this.hin = s;
		}

		public String getVer() {
		    return this.ver;
		}
		public void setVer(String s) {
		    this.ver = s;
		}

		public String getRosterStatus() {
		    return this.rosterStatus;
		}
		public void setRosterStatus(String s) {
		    this.rosterStatus = s;
		}

		public Date getRosterDate() {
		    return this.rosterDate;
		}
		public void setRosterDate(Date d) {
		    this.rosterDate = d;
		}

		public Date getRosterTerminationDate() {
		    return this.rosterTerminationDate;
		}
		public void setRosterTerminationDate(Date d) {
		    this.rosterTerminationDate = d;
		}

		public String getRosterTerminationReason() {
		    return this.rosterTerminationReason;
		}
		public void setRosterTerminationReason(String s) {
		    this.rosterTerminationReason = s;
		}

		public String getPatientStatus() {
		    return this.patientStatus;
		}
		public void setPatientStatus(String s) {
		    this.patientStatus = s;
		}

		public Date getPatientStatusDate() {
		    return this.patientStatusDate;
		}
		public void setPatientStatusDate(Date d) {
		    this.patientStatusDate = d;
		}

		public Date getDateJoined() {
		    return this.dateJoined;
		}
		public void setDateJoined(Date d) {
		    this.dateJoined = d;
		}

		public String getChartNo() {
		    return this.chartNo;
		}
		public void setChartNo(String s) {
		    this.chartNo = s;
		}


		public String getOfficialLanguage() {
        	return officialLanguage;
        }

		public void setOfficialLanguage(String officialLanguage) {
        	this.officialLanguage = officialLanguage;
        }

		public String getSpokenLanguage() {
        	return spokenLanguage;
        }

		public void setSpokenLanguage(String spokenLanguage) {
        	this.spokenLanguage = spokenLanguage;
        }

		public String getProviderNo() {
		    return this.providerNo;
		}
		public void setProviderNo(String s) {
		    this.providerNo = s;
		}

		public String getSex() {
		    return this.sex;
		}
		public void setSex(String s) {
		    this.sex = s;
		}

		public Date getEndDate() {
		    return this.endDate;
		}
		public void setEndDate(Date d) {
		    this.endDate = d;
		}

		public Date getEffDate() {
		    return this.effDate;
		}
		public void setEffDate(Date d) {
		    this.effDate = d;
		}

		public String getPcnIndicator() {
		    return this.pcnIndicator;
		}
		public void setPcnIndicator(String s) {
		    this.pcnIndicator = s;
		}

		public String getHcType() {
		    return this.hcType;
		}
		public void setHcType(String s) {
		    this.hcType = s;
		}

		public Date getHcRenewDate() {
		    return this.hcRenewDate;
		}
		public void setHcRenewDate(Date d) {
		    this.hcRenewDate = d;
		}

		public String getFamilyDoctor() {
		    return this.familyDoctor;
		}
		public void setFamilyDoctor(String s) {
		    this.familyDoctor = s;
		}

		public String getAlias() {
		    return this.alias;
		}
		public void setAlias(String s) {
		    this.alias = s;
		}

		public String getPreviousAddress() {
		    return this.previousAddress;
		}
		public void setPreviousAddress(String s) {
		    this.previousAddress = s;
		}

		public String getChildren() {
		    return this.children;
		}
		public void setChildren(String s) {
		    this.children = s;
		}

		public String getSourceOfIncome() {
		    return this.sourceOfIncome;
		}
		public void setSourceOfIncome(String s) {
		    this.sourceOfIncome = s;
		}

		public String getCitizenship() {
		    return this.citizenship;
		}
		public void setCitizenship(String s) {
		    this.citizenship = s;
		}

		public String getSin() {
		    return this.sin;
		}
		public void setSin(String s) {
		    this.sin = s;
		}

		public String getCountryOfOrigin() {
		    return this.countryOfOrigin;
		}
		public void setCountryOfOrigin(String s) {
		    this.countryOfOrigin = s;
		}

		public String getNewsletter() {
		    return this.newsletter;
		}
		public void setNewsletter(String s) {
		    this.newsletter = s;
		}

		public String getAnonymous() {
		    return this.anonymous;
		}
		public void setAnonymous(String s) {
		    this.anonymous = s;
		}

		public String getLastUpdateUser() {
		    return this.lastUpdateUser;
		}
		public void setLastUpdateUser(String s) {
		    this.lastUpdateUser = s;
		}

		public Date getLastUpdateDate() {
		    return this.lastUpdateDate;
		}
		public void setLastUpdateDate(Date d) {
		    this.lastUpdateDate = d;
		}

		@Override
		public Long getId() {
		    return this.id;
		}
		public void setId(Long id) {
		    this.id = id;
		}
}
