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


package org.oscarehr.eyeform.model;

import java.text.ParseException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.util.MiscUtils;

@Entity
public class EyeformSpecsHistory extends AbstractModel<Integer>{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private int demographicNo;
	private String provider;
	private String status;
	@Temporal(TemporalType.DATE)
	private Date date;
	private String doctor;
	private String type;
	private String odSph;
	private String odCyl;
	private String odAxis;
	private String odAdd;
	private String odPrism;
	private String osSph;
	private String osCyl;
	private String osAxis;
	private String osAdd;
	private String osPrism;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	private int appointmentNo;
	private String note;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public EyeformSpecsHistory() {
		status="A";
	}

	public int getAppointmentNo() {
		return appointmentNo;
	}
	public void setAppointmentNo(int appointmentNo) {
		this.appointmentNo = appointmentNo;
	}
	public String getOsSph() {
		return osSph;
	}
	public void setOsSph(String osSph) {
		this.osSph = osSph;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOdSph() {
		return odSph;
	}
	public void setOdSph(String odSph) {
		this.odSph = odSph;
	}
	public String getOdCyl() {
		return odCyl;
	}
	public void setOdCyl(String odCyl) {
		this.odCyl = odCyl;
	}
	public String getOdAxis() {
		return odAxis;
	}
	public void setOdAxis(String odAxis) {
		this.odAxis = odAxis;
	}
	public String getOdAdd() {
		return odAdd;
	}
	public void setOdAdd(String odAdd) {
		this.odAdd = odAdd;
	}
	public String getOdPrism() {
		return odPrism;
	}
	public void setOdPrism(String odPrism) {
		this.odPrism = odPrism;
	}

	public String getOsCyl() {
		return osCyl;
	}
	public void setOsCyl(String osCyl) {
		this.osCyl = osCyl;
	}
	public String getOsAxis() {
		return osAxis;
	}
	public void setOsAxis(String osAxis) {
		this.osAxis = osAxis;
	}
	public String getOsAdd() {
		return osAdd;
	}
	public void setOsAdd(String osAdd) {
		this.osAdd = osAdd;
	}
	public String getOsPrism() {
		return osPrism;
	}
	public void setOsPrism(String osPrism) {
		this.osPrism = osPrism;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDateStr() {
		if(getDate()==null) return "";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(getDate());
	}

	public void setDateStr(String d) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		try {
				if(d == null){
					setDate(null);
				}else if(d == ""){
					setDate(null);
				}else{
					setDate(sdf.parse(d));
				}
		}catch(ParseException e) {
			MiscUtils.getLogger().error("Error",e);
		}
	}

	@Override
	public String toString() {
		return toString3("<br/>");
	}

	public String toString2() {
		StringBuilder sb = new StringBuilder();
        sb.append(getOdSph() == null ? "" : getOdSph());
        sb.append(getOdCyl() == null ? "" : getOdCyl());
        if (getOdAxis() != null && getOdAxis().trim().length() != 0)
        	sb.append("x" + getOdAxis());
        if (getOdAdd() != null && getOdAdd().trim().length() != 0)
        	sb.append("add" + getOdAdd());
        if (getOdPrism() != null && getOdPrism().trim().length() != 0)
        	sb.append("prism" + getOdPrism());
        sb.append(" " + getType() + " " + getDoctor());
        sb.append("\n");
        sb.append(getOsSph() == null ? "" : getOsSph());
        sb.append(getOsCyl() == null ? "" : getOsCyl());
        if (getOsAxis() != null && getOsAxis().trim().length() != 0)
        	sb.append("x" + getOsAxis());
        if (getOsAdd() != null && getOsAdd().trim().length() != 0)
        	sb.append("add" + getOsAdd());
        if (getOsPrism() != null && getOsPrism().trim().length() != 0)
        	sb.append("prism" + getOsPrism());
		return sb.toString();
	}

	public String toString3() {
		return toString3("   ");
	}
	public String toString3(String deliminator) {
		StringBuilder sb = new StringBuilder();
        sb.append(getOdSph() == null ? "" : getOdSph());
        sb.append(getOdCyl() == null ? "" : getOdCyl());
        if (getOdAxis() != null && getOdAxis().trim().length() != 0)
        	sb.append("x" + getOdAxis());
        if (getOdAdd() != null && getOdAdd().trim().length() != 0)
        	sb.append(" add " + getOdAdd());
        if (getOdPrism() != null && getOdPrism().trim().length() != 0)
        	sb.append(" prism " + getOdPrism());
        sb.append(deliminator);
        sb.append(getOsSph() == null ? "" : getOsSph());
        sb.append(getOsCyl() == null ? "" : getOsCyl());
        if (getOsAxis() != null && getOsAxis().trim().length() != 0)
        	sb.append("x" + getOsAxis());
        if (getOsAdd() != null && getOsAdd().trim().length() != 0)
        	sb.append(" add " + getOsAdd());
        if (getOsPrism() != null && getOsPrism().trim().length() != 0)
        	sb.append(" prism " + getOsPrism());
		return sb.toString();
	}
}
