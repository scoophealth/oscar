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
import java.util.HashMap;

import org.oscarehr.util.XmlUtils;
import org.oscarehr.ws.rest.to.model.AppointmentTypeTransfer;
import org.w3c.dom.Node;

public final class AppointmentType{
	private Long id;
	private String name;
	private int defaultDurationMinutes;
	private Integer mappingOscarApptType;
	private HashMap<String, Integer> roleDurations = new HashMap<String, Integer>();

	public Long getId(){
		return(id);
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getName(){
		return(name);
	}

	public void setName(String name){
		this.name = name;
	}

	public int getDefaultDurationMinutes(){
		return(defaultDurationMinutes);
	}

	public void setDefaultDurationMinutes(int defaultDurationMinutes){
		this.defaultDurationMinutes = defaultDurationMinutes;
	}

	public HashMap<String, Integer> getRoleDurations(){
		return(roleDurations);
	}

	public void setRoleDurations(HashMap<String, Integer> roleDurations){
		this.roleDurations = roleDurations;
	}

	public static AppointmentType fromXml(Node node){
		AppointmentType result = new AppointmentType();

		result.id = XmlUtils.getChildNodeLongContents(node, "id");
		result.name = XmlUtils.getChildNodeTextContents(node, "name");
		

		try{
			result.defaultDurationMinutes = XmlUtils.getChildNodeIntegerContents(node, "default_duration_minutes");
		}catch (Exception e){
			//may not have a duration
		}
		try{
			result.mappingOscarApptType = XmlUtils.getChildNodeIntegerContents(node, "mappingOscarApptType");
		}catch (Exception e){
			//may not have a duration
		}
		
		ArrayList<Node> roles = XmlUtils.getChildNodes(node, "role");
		for (Node roleNode : roles){
			String roleName = XmlUtils.getChildNodeTextContents(roleNode, "role_name");
			Integer durationMinutes = XmlUtils.getChildNodeIntegerContents(roleNode, "duration_minutes");

			result.roleDurations.put(roleName, durationMinutes);
		}

		return(result);
	}

	public static AppointmentType fromXml2(Node node){
		AppointmentType result = new AppointmentType();

		result.id = Long.parseLong(XmlUtils.getAttributeValue(node, "id"));
		result.name = XmlUtils.getAttributeValue(node, "name");
		try{
			result.mappingOscarApptType =  Integer.parseInt(XmlUtils.getAttributeValue(node, "mappingOscarApptType")); 
		}catch (Exception e){
			//may not have a duration
		}
			
		return(result);
	}

	
	public static AppointmentType fromAppointmentTypeTransfer(AppointmentTypeTransfer apptNode) {
		AppointmentType result = new AppointmentType();

		result.id = apptNode.getId();
		result.name = apptNode.getName();
		result.mappingOscarApptType = apptNode.getMappingOscarApptType();

		return(result);
	}

	public Integer getMappingOscarApptType() {
		return mappingOscarApptType;
	}

	public void setMappingOscarApptType(Integer mappingOscarApptType) {
		this.mappingOscarApptType = mappingOscarApptType;
	}
	
}
