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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class FilterDefinition {
	private static Logger logger = MiscUtils.getLogger();

	String filterClassName = null;
	Map<String,String> params = new  HashMap<String,String>();
	
	
	public String getFilterClassName() {
		return filterClassName;
	}
	public void setFilterClassName(String filterClassName) {
		this.filterClassName = filterClassName;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public static FilterDefinition fromXml(Node node){
		FilterDefinition filterDefinition = new FilterDefinition();
		filterDefinition.setFilterClassName(node.getTextContent());
		logger.debug("FilterDefinition "+filterDefinition.getFilterClassName());
		
		NamedNodeMap attributes = node.getAttributes();
		
		if(attributes != null){
			filterDefinition.params = new HashMap<String,String>();
			for (int i = 0; i < attributes.getLength(); i++){
				filterDefinition.params.put(attributes.item(i).getNodeName(),attributes.item(i).getNodeValue());
				logger.debug(filterDefinition.getFilterClassName()+"--"+attributes.item(i).getNodeName()+"--"+attributes.item(i).getNodeValue());
			}	
		}
		return filterDefinition;
	}
	
}
