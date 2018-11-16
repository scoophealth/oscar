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
package org.oscarehr.appointment.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.XmlUtils;
import org.oscarehr.ws.rest.to.model.AllowedAppointmentTypeTransfer;
import org.oscarehr.ws.rest.to.model.BookingProviderTransfer;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Provider {
	
	private static Logger logger = MiscUtils.getLogger();
	String messageUserId;
	String providerNo = null;
	@Transient
	String lastName;
	@Transient
	String firstName;
	Map<String, Character[]> appointmentTypes = null;
	Map<Long, Integer> appointmentDurations = null;
	List<Provider> teamMembers = null;
	List<FilterDefinition> filters = null;
	String role = null;

	private ArrayList<Map<String, String>> suggestedRelationships;
	
	public String getProviderNo(){
		return providerNo;
	}

	public List<FilterDefinition> getFilter(){
		return filters;
	}

	public Map<Long, Integer> getAppointmentDurations(){
		return appointmentDurations;
	}

	public Map<String, Character[]> getAppointmentTypes(){
		return appointmentTypes;
	}

	public List<Provider> getTeamMembers(){
		return teamMembers;
	}
	
    public String getMessageUserId(){	
    	return messageUserId;	
    }	

    public ArrayList<Map<String, String>> getSuggestedRelationships(){	
    	return suggestedRelationships;
    }	
	
	public static List<FilterDefinition> getFilterArray(Node node){
		List<Node> filterNodes = XmlUtils.getChildNodes(node, "filter");
		List<FilterDefinition> returnVal = new ArrayList<FilterDefinition>();
		for(Node n:filterNodes){
			returnVal.add(FilterDefinition.fromXml(n));
		}	
		return returnVal;
	}
	
	public static Provider fromXml(Node node){
		Provider provider = new Provider();
		provider.providerNo = XmlUtils.getAttributeValue(node, "providerNo");
		provider.role = XmlUtils.getAttributeValue(node, "role");
		List<Node> apptNodes = XmlUtils.getChildNodes(node, "allowed_appointment");
		provider.messageUserId = XmlUtils.getChildNodeTextContents(node,"messageUserId");

		provider.filters = getFilterArray(node);
		
		provider.appointmentTypes = getAllowedAppointments(apptNodes);
		provider.appointmentDurations = getAppointmentDurations(apptNodes);

		provider.teamMembers = new ArrayList<Provider>();
		if(XmlUtils.getChildNode(node, "team") != null){
			List<Node> members = XmlUtils.getChildNodes(XmlUtils.getChildNode(node, "team"), "member");
			for (Node memberNode : members){
				Provider providerMember = new Provider();
				providerMember.providerNo = XmlUtils.getAttributeValue(memberNode, "providerNo");
				providerMember.role = XmlUtils.getAttributeValue(memberNode, "role");
				List<Node> memberApptNodes = XmlUtils.getChildNodes(memberNode, "allowed_appointment");
	
				if (XmlUtils.getChildNodesTextContents(memberNode, "filter") != null && XmlUtils.getChildNodesTextContents(memberNode, "filter").size() > 0){
					providerMember.filters = getFilterArray(memberNode); 
				}else{
					providerMember.filters = getFilterArray(node); 
				}
	
				providerMember.appointmentTypes = getAllowedAppointments(memberApptNodes);
				providerMember.appointmentDurations = getAppointmentDurations(memberApptNodes);
				provider.teamMembers.add(providerMember);
			}
		}
		
		provider.suggestedRelationships = new ArrayList<Map<String,String>>();
		List<Node> userForRelationshipNodes = XmlUtils.getChildNodes(node, "suggestedRelationship");
		for (Node userForRelationship:userForRelationshipNodes) {	
			NamedNodeMap attributes = userForRelationship.getAttributes();
			Map<String, String> attMap = new HashMap<String,String>();	
			if(attributes != null){	
				for (int i = 0; i < attributes.getLength(); i++){	
					String attName = attributes.item(i).getNodeName();
					String attValue = attributes.item(i).getNodeValue();	
					attMap.put(attName, attValue);	
				}	
				provider.suggestedRelationships.add(attMap);	
			}	
		}	
		
		return provider;
	}

	private static Map<Long, Integer> getAppointmentDurations(List<Node> apptNodes){
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		for (Node allowedAppointment : apptNodes){
			String apptId = null;
			String apptDuration = null;
			try{
				apptId = XmlUtils.getAttributeValue(allowedAppointment, "id");
				apptDuration = XmlUtils.getAttributeValue(allowedAppointment, "duration");
				if(apptId != null && apptDuration != null){
					map.put(Long.parseLong(apptId), Integer.parseInt(apptDuration));
				}
			}catch (Exception e){
				logger.debug("Not a Integer  id " + apptId + " dur " + apptDuration, e);
			}
		}
		return map;
	}

	private static Map<String, Character[]> getAllowedAppointments(List<Node> apptNodes){
		Map<String, Character[]> map = new HashMap<String, Character[]>();

		for (Node allowedAppointment : apptNodes){
			String apptId = XmlUtils.getAttributeValue(allowedAppointment, "id");
			String apptCodesTemp = XmlUtils.getAttributeValue(allowedAppointment, "appointment_codes");
			String[] tempSplit = apptCodesTemp.split(",");

			Character[] allowableTimeCodes = new Character[tempSplit.length];
			int count = 0;
			for (String s : tempSplit){
				s = StringUtils.trimToNull(s);
				if (s != null && s.length() > 0){
					allowableTimeCodes[count] = s.charAt(0);
					count++;
				}
			}
			Arrays.sort(allowableTimeCodes);
			map.put(apptId, allowableTimeCodes);
		}
		return map;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public static Provider fromProvider(BookingProviderTransfer provider) {
		Provider returnProvider = new Provider();
		returnProvider.appointmentDurations =  new HashMap<Long,Integer>();
		returnProvider.appointmentTypes = new HashMap<String,Character[]>();
		List<AllowedAppointmentTypeTransfer> allowedAppts =  provider.getAppointmentTypes();
		for(AllowedAppointmentTypeTransfer allowedAppt:allowedAppts){
			returnProvider.appointmentTypes.put(""+allowedAppt.getId(), allowedAppt.getCodes());
			if(allowedAppt.getDuration() > 0){  
				returnProvider.appointmentDurations.put(allowedAppt.getId(),allowedAppt.getDuration());
			}
		}
		
		returnProvider.messageUserId = provider.getMessageUserId();
		returnProvider.providerNo = provider.getProviderNo();
		returnProvider.teamMembers = new ArrayList<Provider>();
		if(provider.getTeamMembers() != null){
			for(BookingProviderTransfer p: provider.getTeamMembers()){
				returnProvider.teamMembers.add(Provider.fromProvider(p));
			}
		}
		returnProvider.filters = new ArrayList<FilterDefinition>();
		if(provider.isFilterFAF()){
			FilterDefinition fd = new FilterDefinition();
			fd.setFilterClassName("org.oscarehr.appointment.search.filters.FutureApptFilter");
			Map<String,String> params = new HashMap<String,String>();
			params.put("buffer",""+provider.getFilterFAFbuffer());
			fd.setParams(params);
			returnProvider.filters.add(fd);
		}
		if(provider.isFilterEAF()){
			FilterDefinition fd = new FilterDefinition();
			fd.setFilterClassName("org.oscarehr.appointment.search.filters.ExistingAppointmentFilter");
			returnProvider.filters.add(fd);
		}
		if(provider.isFilterMUF()){
			FilterDefinition fd = new FilterDefinition();
			fd.setFilterClassName("org.oscarehr.appointment.search.filters.MultiUnitFilter");
			returnProvider.filters.add(fd);
		}
		if(provider.isFilterOAF()){
			FilterDefinition fd = new FilterDefinition();
			fd.setFilterClassName("org.oscarehr.appointment.search.filters.OpenAccessFilter");
			Map<String,String> params = new HashMap<String,String>();
			params.put("codes",""+provider.getFilterOAFCodes());
			fd.setParams(params);
			returnProvider.filters.add(fd);
		}
		if(provider.isFilterSCTF()){
			FilterDefinition fd = new FilterDefinition();
			fd.setFilterClassName("org.oscarehr.appointment.search.filters.SufficientContiguousTimeFilter");
			returnProvider.filters.add(fd);
		}
				
		return returnProvider;
	}
}
