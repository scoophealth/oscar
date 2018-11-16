package org.oscarehr.ws.rest.to.model;

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
