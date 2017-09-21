package org.oscarehr.common.model;
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

import java.util.Date;

/**
 * An interface to be used with any object model that references data for an immunization.
 * 
 * The getImmunizationProperty method can be used in conjunction with the ImmunizationProperty enum
 * to fetch key:value pairs from an entity class. 
 * 
 * This interface is currently being used in FHIR mapping.
 */
public interface ImmunizationInterface<T> {
	
	public enum ImmunizationProperty{lot,location,route,dose,comments,neverReason,manufacture,name}
	
	/**
	 * Get an immunization data value by ImmunizationProperty key
	 */
	public String getImmunizationProperty( ImmunizationProperty immunizationProperty );
		
	public String getLotNo();
	public void setLotNo(String lotNo);

	public String getSite();
	public void setSite( String site );
	
	public String getRoute();
	public void setRoute(String route);
	
	public String getDose();
	public void setDose(String dose);
	
	public String getComment();
	public void setComment(String comment);
	
	public void setImmunizationRefused(boolean refused);
	public boolean getImmunizationRefused();
	
	public String getImmunizationRefusedReason();
	public void setImmunizationRefusedReason(String reason);
	
	public String getManufacture();
	public void setManufacture(String manufacture);
	
	public String getName();
	public void setName(String name);
	
	public String getImmunizationType();
	public void setImmunizationType(String immunizationType);
	
	public Date getImmunizationDate();
	public void setImmunizationDate(Date immunizationDate);
	
}
