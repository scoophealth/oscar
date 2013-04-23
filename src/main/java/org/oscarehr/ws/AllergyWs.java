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


package org.oscarehr.ws;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.managers.AllergyManager;
import org.oscarehr.ws.transfer_objects.AllergyTransfer;
import org.oscarehr.ws.transfer_objects.DataIdTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
@GZIP(threshold=AbstractWs.GZIP_THRESHOLD)
public class AllergyWs extends AbstractWs {
	@Autowired
	private AllergyManager allergyManager;
	
	public AllergyTransfer getAllergy(Integer allergyId)
	{
		Allergy allergy=allergyManager.getAllergy(allergyId);
		return(AllergyTransfer.toTransfer(allergy));
	}

	/**
	 * Get a list of DataIdTransfer objects for allergies starting with the passed in Id.
	 */
	public DataIdTransfer[] getAllergyDataIds(Boolean active, Integer startIdInclusive, int itemsToReturn)
	{
		Boolean archived=null;
		if (active!=null) archived=!active;
		
		List<Allergy> allergies=allergyManager.getAllergiesByIdStart(archived, startIdInclusive, itemsToReturn);
		
		DataIdTransfer[] results=new DataIdTransfer[allergies.size()];
		for (int i=0; i<allergies.size(); i++)
		{
			results[i]=getDataIdTransfer(allergies.get(i));
		}
		
		return(results);
	}
	
	private DataIdTransfer getDataIdTransfer(Allergy allergy)
	{
		DataIdTransfer result=new DataIdTransfer();
		
		// techincally incorrect but that's the best we've got from the current object fields
		Calendar cal=new GregorianCalendar();
		cal.setTime(allergy.getLastUpdateDate());
		result.setCreateDate(cal);
		
		result.setCreatorProviderId(allergy.getProviderNo());
		result.setDataId(allergy.getId().toString());
		result.setDataType(Allergy.class.getSimpleName());
		result.setOwnerDemographicId(allergy.getDemographicNo());
		
		return(result);
	}
}
