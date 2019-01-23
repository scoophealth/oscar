package org.oscarehr.ws.rest.to.model;
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
import java.util.HashMap;
import java.util.Map;

import org.oscarehr.appointment.search.FilterDefinition;


public class FilterDefinitionTransfer {
	
	String filterClassName = null;
	boolean on = false;
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
	public static FilterDefinitionTransfer fromFilterDefinition(FilterDefinition f) {
		FilterDefinitionTransfer fdt = new FilterDefinitionTransfer();
		
		if("org.oscarehr.appointment.search.filters.FutureApptFilter".equals(f.getFilterClassName())){
			fdt.filterClassName = "FAF";
		}else if("org.oscarehr.appointment.search.filters.ExistingAppointmentFilter".equals(f.getFilterClassName())){
			fdt.filterClassName = "EAF";
	    }else if("org.oscarehr.appointment.search.filters.MultiUnitFilter".equals(f.getFilterClassName())){
	    	fdt.filterClassName = "MUF";
	    }else if("org.oscarehr.appointment.search.filters.OpenAccessFilter".equals(f.getFilterClassName())){
	    	fdt.filterClassName = "OAF";
	    }else if("org.oscarehr.appointment.search.filters.SufficientContiguousTimeFilter".equals(f.getFilterClassName())){
	    	fdt.filterClassName = "SCTF";
	    }
		
		fdt.params = f.getParams(); 
		return fdt;
	}
	public boolean isOn() {
		return on;
	}
	public void setOn(boolean on) {
		this.on = on;
	}
	
	
}
