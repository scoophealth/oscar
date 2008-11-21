/*
 * 
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */

package org.caisi.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.caisi.dao.CustomFilterDAO;
import org.caisi.dao.TicklerDAO;
import org.caisi.model.CustomFilter;
import org.caisi.model.Role;
import org.caisi.model.Tickler;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.casemgmt.dao.RoleProgramAccessDAO;
import org.oscarehr.casemgmt.service.CaseManagementManager;

import oscar.OscarProperties;

/**
 */
public class TicklerManager {

    private TicklerDAO ticklerDAO = null;
    private CustomFilterDAO customFilterDAO = null;
    private RoleProgramAccessDAO roleProgramAccessDAO;
    
    private ProgramManager programManager = null;
    private CaseManagementManager caseManagementManager = null;
    
    public void setProgramManager(ProgramManager programManager) {
        this.programManager = programManager;
    }

    public void setCaseManagementManager(CaseManagementManager caseManagementManager) {
        this.caseManagementManager = caseManagementManager;
    }
    
    public void setTicklerDAO(TicklerDAO ticklerDAO) {
        this.ticklerDAO = ticklerDAO;
    }

    public void setCustomFilterDAO(CustomFilterDAO customFilterDAO) {
        this.customFilterDAO = customFilterDAO;
    }
    
    public void setRoleProgramAccessDAO(RoleProgramAccessDAO roleProgramAccessDAO) {
        this.roleProgramAccessDAO = roleProgramAccessDAO;
    }
    
    public void addTickler(Tickler tickler) {
        ticklerDAO.saveTickler(tickler);
    }

    public List<Tickler> getTicklers() {
        return ticklerDAO.getTicklers();
    }

   
    public List<Tickler> getTicklers(CustomFilter filter, Integer currentFacilityId,String providerNo,String programId) {
        List<Tickler> results = ticklerDAO.getTicklers(filter);     
           
        //String programNo = filter.getProgramId();
        
        
        if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {        	
        	//filter based on facility
        	results = ticklerFacilityFiltering(currentFacilityId, results);
        	
        	//filter based on caisi role access
            results = filterTicklersByAccess(results,providerNo,programId);
        }        
        
        return(results);
    }
    
    private List<Tickler> ticklerFacilityFiltering(Integer currentFacilityId, List<Tickler> ticklers) {
        ArrayList<Tickler> results = new ArrayList<Tickler>();

        for (Tickler tickler : ticklers) {
            Integer programId = tickler.getProgram_id();
            
            if (programManager.hasAccessBasedOnFacility(currentFacilityId, programId)) {            	
            	results.add(tickler);
            }        
        }

        return results;
    }
    private List<Tickler> filterTicklersByAccess(List<Tickler> ticklers, String providerNo, String programNo) {
    	List<Tickler> filteredTicklers = new ArrayList<Tickler>();

    	if (ticklers.isEmpty()) {
    		return ticklers;
    	}    	
    	
	    String programId = "";
	    //iterate through the tickler list
	    for (Iterator iter = ticklers.iterator(); iter.hasNext();) {
	        Tickler t = (Tickler)iter.next();
	        boolean add = false;	        
	        List ppList = new ArrayList();
	        
	        //If there is no selected program, show all ticklers in domain
	        //if(programNo==null || "".equals(programNo) || "null".equals(programNo))
	        	programId = String.valueOf(t.getProgram_id());
	        
	        //If the ticklers are not in any problem (old ticklers), show them.
	        //They will not be filtered by the role access.
	        if(programId==null || "".equals(programId) || "null".equals(programId)) {
	        	filteredTicklers.add(t);
	        	continue;
	        }
	        
	        ppList = roleProgramAccessDAO.getProgramProviderByProviderProgramID(providerNo, new Long(programId));
	        if (ppList == null || ppList.isEmpty()) {
	        	continue;
	        }
	        ProgramProvider pp = (ProgramProvider)ppList.get(0);
		    Role role = pp.getRole();		   
		    
		    //Load up access list from program
		    List programAccessList = roleProgramAccessDAO.getAccessListByProgramID(new Long(programId));
		    Map programAccessMap = caseManagementManager.convertProgramAccessListToMap(programAccessList);
			        
	        
	        //Get the tickler assigned provider's role         
	        String assignedProviderId = t.getTask_assigned_to();
	        Integer ticklerProgramId = t.getProgram_id();
	        List ppList2 = new ArrayList();
	        if(ticklerProgramId!=null) {
	        	ppList2 = this.roleProgramAccessDAO.getProgramProviderByProviderProgramID(assignedProviderId, new Long(ticklerProgramId));
	        	if (ppList2 == null || ppList2.isEmpty()) {
	        		//add = true; //????ture or false????
	        		//filteredTicklers.add(t);
	        		continue;
	        	}	
	        } else {
	        	//add = true;
	        	//filteredTicklers.add(t);
	        	continue;
	        }
	        
	        ProgramProvider pp2 = (ProgramProvider)ppList2.get(0);
	        String ticklerRole = pp2.getRole().getName();
	        
	        ProgramAccess pa = null;        
	
	        //read
	        pa = (ProgramAccess)programAccessMap.get("read ticklers assigned to a " + ticklerRole );
	        if (pa != null) {
	            if (pa.isAllRoles() || caseManagementManager.isRoleIncludedInAccess(pa, role)) {                
	                add = true;
	            }
	        }
	        else {
	            if (ticklerRole.equals(role.getName())) {                               
	                add = true;
	            }
	        }
	        pa = null;
	        
	        //if this provider wrote the tickler, they should see it..doesn't matter
	        //about the role based access
	        if(!add) {
	        	if(t.getProvider().getProviderNo().equals(providerNo)) {
	        		add=true;
	        	}
	        
	        }
	
	        //apply defaults
	        if (!add) {
	            if (ticklerRole.equals(role.getName())) {                
	                add = true;
	            }
	        }
	
	        //did it pass the test?
	        if (add) {
	        	filteredTicklers.add(t);
	        }
	    }
	    return filteredTicklers;
	}
   
    
    public int getActiveTicklerCount(String providerNo) {
        return ticklerDAO.getActiveTicklerCount(providerNo);
    }

    public int getNumTicklers(CustomFilter filter) {
        return ticklerDAO.getNumTicklers(filter);
    }

    public Tickler getTickler(String tickler_no) {
        Long id = Long.valueOf(tickler_no);
        return ticklerDAO.getTickler(id);
    }

    public void addComment(String tickler_no, String provider, String message) {
        Long id = Long.valueOf(tickler_no);
        ticklerDAO.addComment(id, provider, message);
    }

    public void reassign(String tickler_no, String provider, String task_assigned_to) {
        Long id = Long.valueOf(tickler_no);
        ticklerDAO.reassign(id, provider, task_assigned_to);
    }

    public void deleteTickler(String tickler_no, String provider) {
        ticklerDAO.deleteTickler(Long.valueOf(tickler_no), provider);
    }

    public void completeTickler(String tickler_no, String provider) {
        ticklerDAO.completeTickler(Long.valueOf(tickler_no), provider);
    }

    public void activateTickler(String tickler_no, String provider) {
        ticklerDAO.activateTickler(Long.valueOf(tickler_no), provider);
    }

    public List getCustomFilters() {
        return customFilterDAO.getCustomFilters();
    }

    public List getCustomFilters(String provider_no) {
        return customFilterDAO.getCustomFilters(provider_no);
    }

    public List getCustomFilterWithShortCut(String providerNo) {
        return customFilterDAO.getCustomFilterWithShortCut(providerNo);
    }

    public CustomFilter getCustomFilter(String name) {
        return customFilterDAO.getCustomFilter(name);
    }

    public CustomFilter getCustomFilter(String name, String providerNo) {
        return customFilterDAO.getCustomFilter(name, providerNo);
    }

    public CustomFilter getCustomFilterById(Integer id) {
        return customFilterDAO.getCustomFilterById(id);
    }

    public void saveCustomFilter(CustomFilter filter) {
        customFilterDAO.saveCustomFilter(filter);
    }

    public void deleteCustomFilter(String name) {
        customFilterDAO.deleteCustomFilter(name);
    }

    public void deleteCustomFilterById(Integer id) {
        customFilterDAO.deleteCustomFilterById(id);
    }
}
