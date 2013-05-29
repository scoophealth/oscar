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
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.common.model.MeasurementMap;
import org.springframework.beans.BeanUtils;

public final class MeasurementMapTransfer {

	private Integer id;
	private String loincCode;
	private String identCode;
	private String name;
	private String labType;

	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoincCode() {
		return (loincCode);
	}

	public void setLoincCode(String loincCode) {
		this.loincCode = loincCode;
	}

	public String getIdentCode() {
		return (identCode);
	}

	public void setIdentCode(String identCode) {
		this.identCode = identCode;
	}

	public String getName() {
		return (name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabType() {
		return (labType);
	}

	public void setLabType(String labType) {
		this.labType = labType;
	}

	public static MeasurementMapTransfer toTransfer(MeasurementMap measurementMap) {
		if (measurementMap == null) return (null);

		MeasurementMapTransfer measurementMapTransfer = new MeasurementMapTransfer();
		BeanUtils.copyProperties(measurementMap, measurementMapTransfer);

		return (measurementMapTransfer);
	}

	public static MeasurementMapTransfer[] toTransfers(List<MeasurementMap> measurementMaps)
	{
		ArrayList<MeasurementMapTransfer> tempResults=new ArrayList<MeasurementMapTransfer>();
		
		for (MeasurementMap measurementMap : measurementMaps)
		{
			tempResults.add(toTransfer(measurementMap));
		}
		
		return(tempResults.toArray(new MeasurementMapTransfer[0]));
	}
	
	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
