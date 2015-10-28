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

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "hl7TextInfo")
public class Hl7TextInfo extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "lab_no")
	/** This is also referred to as segmentId in parts of the code... */
	private int labNumber;

	@Column(name = "sex")
	private String sex;

	@Column(name = "health_no")
	private String healthNumber;

	@Column(name = "result_status")
	private String resultStatus;

	@Column(name = "final_result_count")
	private int finalResultCount;

	@Column(name = "obr_date")
	private String obrDate;

	private String priority;

	@Column(name = "requesting_client")
	private String requestingProvider;

	private String discipline;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "report_status")
	private String reportStatus;

	@Column(name = "accessionNum")
	private String accessionNumber;
	
	@Column(name = "filler_order_num")
	private String fillerOrderNum;

	@Column(name = "sending_facility")
	private String sendingFacility;

	private String label;
	
	@Override
	public Integer getId() {
		return (id);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = StringUtils.trimToNull(firstName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = StringUtils.trimToNull(lastName);
	}

	public int getLabNumber() {
		return labNumber;
	}

	public void setLabNumber(int labNumber) {
		this.labNumber = labNumber;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = StringUtils.trimToNull(sex);
	}

	public String getHealthNumber() {
		return healthNumber;
	}

	public void setHealthNumber(String healthNumber) {
		this.healthNumber = StringUtils.trimToNull(healthNumber);
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = StringUtils.trimToNull(resultStatus);
	}

	public int getFinalResultCount() {
		return finalResultCount;
	}

	public void setFinalResultCount(int finalResultCount) {
		this.finalResultCount = finalResultCount;
	}

	public String getObrDate() {
		return obrDate;
	}

	public void setObrDate(String obrDate) {
		this.obrDate = StringUtils.trimToNull(obrDate);
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = StringUtils.trimToNull(priority);
	}

	public String getRequestingProvider() {
		return requestingProvider;
	}

	public void setRequestingProvider(String requestingProvider) {
		this.requestingProvider = StringUtils.trimToNull(requestingProvider);
	}

	public String getDiscipline() {
		return discipline;
	}

	public void setDiscipline(String discipline) {
		this.discipline = StringUtils.trimToNull(discipline);
	}

	public String getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(String reportStatus) {
		//report_status may not be null
		this.reportStatus = StringUtils.trimToEmpty(reportStatus);
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = StringUtils.trimToNull(accessionNumber);
	}
	
	public String getFillerOrderNum() {
    	return fillerOrderNum;
    }

	public void setFillerOrderNum(String fillerOrderNum) {
    	this.fillerOrderNum = fillerOrderNum;
    }

	public String getSendingFacility() {
    	return sendingFacility;
    }

	public void setSendingFacility(String sendingFacility) {
    	this.sendingFacility = sendingFacility;
    }

	public String getLabel() {
    	return label;
    }

	public void setLabel(String label) {
    	this.label = label;
    }
	
	@Transient
	public String getLabelOrDiscipline(){
		if (label == null || label.equals("")){
			return  discipline;
		}else{
			return label;    
		}
	}
	
	
	
}
