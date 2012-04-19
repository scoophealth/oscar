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


public class SatelliteClinic
{
	public Integer clinicId;
	public String clinicName;
	public String clinicAddress;
	public String clinicCity;
	public String clinicProvince;
	public String clinicPostal;
	public String clinicPhone;
	public String clinicFax;
	public String getClinicAddress()
	{
		return clinicAddress;
	}
	public void setClinicAddress(String clinicAddress)
	{
		this.clinicAddress = clinicAddress;
	}
	public String getClinicCity()
	{
		return clinicCity;
	}
	public void setClinicCity(String clinicCity)
	{
		this.clinicCity = clinicCity;
	}
	public String getClinicFax()
	{
		return clinicFax;
	}
	public void setClinicFax(String clinicFax)
	{
		this.clinicFax = clinicFax;
	}
	public String getClinicName()
	{
		return clinicName;
	}
	public void setClinicName(String clinicName)
	{
		this.clinicName = clinicName;
	}
	public String getClinicPhone()
	{
		return clinicPhone;
	}
	public void setClinicPhone(String clinicPhone)
	{
		this.clinicPhone = clinicPhone;
	}
	public String getClinicPostal()
	{
		return clinicPostal;
	}
	public void setClinicPostal(String clinicPostal)
	{
		this.clinicPostal = clinicPostal;
	}
	public String getClinicProvince()
	{
		return clinicProvince;
	}
	public void setClinicProvince(String clinicProvince)
	{
		this.clinicProvince = clinicProvince;
	}
	public Integer getClinicId()
	{
		return clinicId;
	}
	public void setClinicId(Integer clinicId)
	{
		this.clinicId = clinicId;
	}

}
