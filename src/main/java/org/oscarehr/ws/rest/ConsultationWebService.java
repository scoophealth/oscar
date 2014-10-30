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
package org.oscarehr.ws.rest;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.sf.json.JSONObject;

import org.oscarehr.consultations.ConsultationSearchFilter;
import org.oscarehr.consultations.ConsultationSearchFilter.SORTDIR;
import org.oscarehr.consultations.ConsultationSearchFilter.SORTMODE;
import org.oscarehr.managers.ConsultationManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.oscarehr.ws.rest.to.model.ConsultationSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/consults")
@Component("consultationWebService")
public class ConsultationWebService extends AbstractServiceImpl {

	Pattern namePtrn = Pattern.compile("sorting\\[(\\w+)\\]");
	
	@Autowired
	ConsultationManager consultationManager;
	
	@Autowired
	SecurityInfoManager securityInfoManager;
	
	
	@POST
	@Path("/search")
	@Produces("application/json")
	@Consumes("application/json")
	public AbstractSearchResponse<ConsultationSearchResult> search(JSONObject json) {
		AbstractSearchResponse<ConsultationSearchResult> response = new AbstractSearchResponse<ConsultationSearchResult>();

		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_con", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		//List<ConsultationSearchResult> results = new ArrayList<ConsultationSearchResult>();
		
				
		int count = consultationManager.getConsultationCount(convertJSON(json));

		if(count>0) {
			List<ConsultationSearchResult> items =  consultationManager.search(getLoggedInInfo(), convertJSON(json));
			//convert items to a ConsultationSearchResult object
			response.setContent(items);
			response.setTotal(count);
		}
		
		return response;
	}
	
	private Date convertJSONDate(String val) {
		try {
			return javax.xml.bind.DatatypeConverter.parseDateTime(val).getTime();
		}catch(Exception e) {
			MiscUtils.getLogger().warn("Error parsing date - " + val);
		}
		return null;
	}
	
	private ConsultationSearchFilter convertJSON(JSONObject json) {
		ConsultationSearchFilter filter = new ConsultationSearchFilter();
		
		filter.setAppointmentEndDate(convertJSONDate((String)json.get("appointmentEndDate")));
		filter.setAppointmentStartDate(convertJSONDate((String)json.get("appointmentStartDate")));
		filter.setDemographicNo((Integer)json.get("demographicNo"));
		filter.setNumToReturn((Integer)json.get("numToReturn"));
		filter.setReferralEndDate(convertJSONDate((String)json.get("referralEndDate")));
		filter.setReferralStartDate(convertJSONDate((String)json.get("referralStartDate")));
		filter.setStartIndex((Integer)json.get("startIndex"));
		filter.setStatus((String)json.get("status"));
		filter.setTeam((String)json.get("team"));
		
		JSONObject params = json.getJSONObject("params");
		if(params != null) {
			for(Object key:params.keySet()) {
				Matcher nameMtchr = namePtrn.matcher((String)key);
				if (nameMtchr.find()) {
				   String var = nameMtchr.group(1);
				   filter.setSortMode(SORTMODE.valueOf(var));
				   filter.setSortDir(SORTDIR.valueOf(params.getString((String)key)));
				}
			}
		}
		return filter;
	}
	
}
