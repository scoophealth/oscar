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
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "eform_data")
public class EFormData extends AbstractModel<Integer> implements Serializable {

	/**
	 * This comparator sorts EFormData ascending based on the formDate
	 */
	public static final Comparator<EFormData> FORM_DATE_COMPARATOR=new Comparator<EFormData>()
	{
		public int compare(EFormData o1, EFormData o2) {
			return(o1.formDate.compareTo(o2.formDate));
		}	
	};

	/**
	 * This comparator sorts EFormData ascending based on the formName
	 */
	public static final Comparator<EFormData> FORM_NAME_COMPARATOR=new Comparator<EFormData>()
	{
		public int compare(EFormData o1, EFormData o2) {
			return(o1.formName.compareTo(o2.formName));
		}	
	};

	/**
	 * This comparator sorts EFormData ascending based on the formName
	 */
	public static final Comparator<EFormData> FORM_SUBJECT_COMPARATOR=new Comparator<EFormData>()
	{
		public int compare(EFormData o1, EFormData o2) {
			return(o1.subject.compareTo(o2.subject));
		}	
	};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fdid")
	private Integer id;

	@Column(name = "fid")
	private Integer formId;

	@Column(name = "form_name")
	private String formName;

	private String subject;

	@Column(name = "demographic_no")
	private Integer demographicId;

	@Column(name = "status")
	private boolean current;

	@Column(name = "form_date")
	@Temporal(TemporalType.DATE)
	private Date formDate=new Date();

	@Column(name = "form_time")
	@Temporal(TemporalType.TIME)
	private Date formTime=formDate;

	@Column(name = "form_provider")
	private String providerNo;

	@Column(name = "form_data")
	private String formData;

	private boolean showLatestFormOnly;

	@Column(name = "patient_independent")
	private boolean patientIndependent;

	private String roleType;

	@Override
	public Integer getId() {
		return (id);
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFormId() {
		return formId;
	}

	public void setFormId(Integer formId) {
		this.formId = formId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public boolean isCurrent() {
    	return current;
    }

	public void setCurrent(boolean current) {
    	this.current = current;
    }

	public Date getFormDate() {
		return formDate;
	}

	public void setFormDate(Date formDate) {
		this.formDate = formDate;
	}

	public Date getFormTime() {
		return formTime;
	}

	public void setFormTime(Date formTime) {
		this.formTime = formTime;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}

	public boolean isShowLatestFormOnly() {
		return showLatestFormOnly;
	}

	public void setShowLatestFormOnly(boolean showLatestFormOnly) {
		this.showLatestFormOnly = showLatestFormOnly;
	}

	public boolean isPatientIndependent() {
		return patientIndependent;
	}

	public void setPatientIndependent(boolean patientIndependent) {
		this.patientIndependent = patientIndependent;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	@PreRemove
	protected void jpa_preventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}
}
