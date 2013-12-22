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
import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.oscarehr.common.Gender;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.PHRVerification;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.ws.transfer_objects.DemographicTransfer;
import org.oscarehr.ws.transfer_objects.PhrVerificationTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
@GZIP(threshold=AbstractWs.GZIP_THRESHOLD)
public class DemographicWs extends AbstractWs {
	@Autowired
	private DemographicManager demographicManager;
	
	public DemographicTransfer getDemographic(Integer demographicId)
	{
		Demographic demographic=demographicManager.getDemographic(demographicId);
		return(DemographicTransfer.toTransfer(demographic));
	}

	public DemographicTransfer getDemographicByMyOscarUserName(String myOscarUserName)
	{
		Demographic demographic=demographicManager.getDemographicByMyOscarUserName(myOscarUserName);
		return(DemographicTransfer.toTransfer(demographic));
	}
	
	public DemographicTransfer[] searchDemographicByName(String searchString, int startIndex, int itemsToReturn)
	{
		List<Demographic> demographics=demographicManager.searchDemographicByName(searchString, startIndex, itemsToReturn);
		return(DemographicTransfer.toTransfers(demographics));
	}
	
	public PhrVerificationTransfer getLatestPhrVerificationByDemographic(Integer demographicId)
	{
		PHRVerification phrVerification=demographicManager.getLatestPhrVerificationByDemographicId(demographicId);
		return(PhrVerificationTransfer.toTransfer(phrVerification));
	}
	
	/**
	 * This method should only return true if the demographic passed in is "phr verified" to a sufficient level to allow a provider to send this phr account messages.
	 */
	public boolean isPhrVerifiedToSendMessages(Integer demographicId)
	{
		boolean result=demographicManager.isPhrVerifiedToSendMessages(demographicId);
		return(result);
	}

	/**
	 * This method should only return true if the demographic passed in is "phr verified" to a sufficient level to allow a provider to send this phr account medicalData.
	 */
	public boolean isPhrVerifiedToSendMedicalData(Integer demographicId)
	{
		boolean result=demographicManager.isPhrVerifiedToSendMedicalData(demographicId);
		return(result);		
	}
	
	/**
	 * @see DemographicManager.searchDemographicsByAttributes for parameter details
	 */
	public DemographicTransfer[] searchDemographicsByAttributes(String hin, String firstName, String lastName, Gender gender, Calendar dateOfBirth, String city, String province, String phone, String email, String alias, int startIndex, int itemsToReturn) {
		List<Demographic> demographics=demographicManager.searchDemographicsByAttributes(hin, firstName, lastName, gender, dateOfBirth, city, province, phone, email, alias, startIndex, itemsToReturn);
		return(DemographicTransfer.toTransfers(demographics));	
	}
}
