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

package org.oscarehr.ws.transfer_objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.common.model.Measurement;
import org.springframework.beans.BeanUtils;

public final class MeasurementTransfer {

	private Integer id;
	private String type;
	private Integer demographicId;
	private String providerNo;
	private String dataField;
	private String measuringInstruction;
	private String comments;
	private Date dateObserved;
	private Integer appointmentNo;
	private Date createDate;

	public static MeasurementTransfer toTransfer(Measurement measurement) {
		if (measurement == null) return (null);

		MeasurementTransfer measurementTransfer = new MeasurementTransfer();
		BeanUtils.copyProperties(measurement, measurementTransfer);

		return (measurementTransfer);
	}

	public static MeasurementTransfer[] toTransfers(List<Measurement> measurements) {
		ArrayList<MeasurementTransfer> results = new ArrayList<MeasurementTransfer>();

		for (Measurement measurement : measurements) {
			results.add(toTransfer(measurement));
		}

		return (results.toArray(new MeasurementTransfer[0]));
	}

	public void copyTo(Measurement measurement)
	{
		// ID should not be copied, nor createDate
		
		measurement.setAppointmentNo(appointmentNo);
		measurement.setComments(comments);
		measurement.setDataField(dataField);
		measurement.setDateObserved(dateObserved);
		measurement.setDemographicId(demographicId);
		measurement.setMeasuringInstruction(measuringInstruction);
		measurement.setProviderNo(providerNo);
		measurement.setType(type);
	}

	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return (type);
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDemographicId() {
		return (demographicId);
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public String getProviderNo() {
		return (providerNo);
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getDataField() {
		return (dataField);
	}

	public void setDataField(String dataField) {
		this.dataField = dataField;
	}

	public String getMeasuringInstruction() {
		return (measuringInstruction);
	}

	public void setMeasuringInstruction(String measuringInstruction) {
		this.measuringInstruction = measuringInstruction;
	}

	public String getComments() {
		return (comments);
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getDateObserved() {
		return (dateObserved);
	}

	public void setDateObserved(Date dateObserved) {
		this.dateObserved = dateObserved;
	}

	public Integer getAppointmentNo() {
		return (appointmentNo);
	}

	public void setAppointmentNo(Integer appointmentNo) {
		this.appointmentNo = appointmentNo;
	}

	public Date getCreateDate() {
		return (createDate);
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
