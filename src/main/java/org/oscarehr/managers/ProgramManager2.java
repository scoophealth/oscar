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

package org.oscarehr.managers;

import java.util.List;

import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.common.dao.ProviderDefaultProgramDao;
import org.oscarehr.common.model.ProviderDefaultProgram;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class ProgramManager2 {
	
	@Autowired
	private ProgramDao programDao;

	@Autowired
	private ProgramProviderDAO programProviderDAO;
	
	@Autowired
	private ProviderDefaultProgramDao providerDefaultProgramDao;
	

	public Program getProgram(LoggedInInfo loggedInInfo, Integer programId) {
		Program result = programDao.getProgram(programId);

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "ProgramManager2.getPrograms" , "id:"+result.getId());

		return (result);
	}


	public List<Program> getAllPrograms(LoggedInInfo loggedInInfo) {
		List<Program> results = programDao.findAll();

		//--- log action ---
		if (results.size()>0) {
			String resultIds=Program.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo, "ProgramManager2.getAllPrograms", "ids returned=" + resultIds);
		}

		return (results);
	}

	public List<ProgramProvider> getAllProgramProviders(LoggedInInfo loggedInInfo) {
		List<ProgramProvider> results = programProviderDAO.getAllProgramProviders();

		//--- log action ---
		if (results.size()>0) {
			String resultIds=ProgramProvider.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo, "ProgramManager2.getAllProgramProviders", "ids returned=" + resultIds);
		}

		return (results);
	}
	
	public List<ProgramProvider> getProgramDomain(LoggedInInfo loggedInInfo, String providerNo) {
		List<ProgramProvider> results = programProviderDAO.getProgramProvidersByProvider(providerNo);
		
		//--- log action ---
		if (results.size()>0) {
			String resultIds=ProgramProvider.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo, "ProgramManager2.getProgramDomain", "ids returned=" + resultIds);
		}
		
		return (results);
	}
	
	public ProgramProvider getCurrentProgramInDomain(LoggedInInfo loggedInInfo, String providerNo) {
		ProgramProvider result = null;
		int defProgramId = 0;
        List<ProviderDefaultProgram> rs = providerDefaultProgramDao.getProgramByProviderNo(providerNo);
        if(!rs.isEmpty()) {
    	   defProgramId = rs.get(0).getProgramId();
    	   if(defProgramId >0) {
    		   result =  programProviderDAO.getProgramProvider(providerNo, Long.valueOf(defProgramId));
    	   }
        }
        
        if(result == null) {
        	List<ProgramProvider> ppList = programProviderDAO.getProgramProviderByProviderNo(providerNo);
        	if(!ppList.isEmpty()) {
        		result = ppList.get(0);
        	}
        }
        
        if(result !=null) {
        	LogAction.addLogSynchronous(loggedInInfo, "ProgramManager2.getCurrentProgramInDomain", "id returned=" + result.getId());
        }
        
        return (result);
	}
	
	public void setCurrentProgramInDomain(String providerNo, Integer programId) {

		if(programProviderDAO.getProgramProvider(providerNo, programId.longValue()) != null) {
			providerDefaultProgramDao.setDefaultProgram(providerNo, programId);
		} else {
			throw new RuntimeException("Program not in user's domain");
		}
	}
}
