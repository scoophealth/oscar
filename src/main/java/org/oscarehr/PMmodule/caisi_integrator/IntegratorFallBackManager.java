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
package org.oscarehr.PMmodule.caisi_integrator;



import java.util.ArrayList;
import java.util.List;

import org.oscarehr.caisi_integrator.ws.CachedDemographicForm;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.common.dao.RemoteIntegratedDataCopyDao;
import org.oscarehr.common.model.RemoteIntegratedDataCopy;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class IntegratorFallBackManager {
	static RemoteIntegratedDataCopyDao  remoteIntegratedDataCopyDao = SpringUtils.getBean(RemoteIntegratedDataCopyDao.class);

	public static List<CachedDemographicNote> getLinkedNotes(Integer demographicNo)  {
		if(!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE","yes")) return null;
		
		List<CachedDemographicNote> linkedNotes = null;
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(),demographicNo,CachedDemographicNote[].class.getName());
		
		if (remoteIntegratedDataCopy == null){
			return linkedNotes;
		}
			
		try{
			CachedDemographicNote[] array = remoteIntegratedDataCopyDao.getObjectFrom( CachedDemographicNote[].class, remoteIntegratedDataCopy);
			linkedNotes = new ArrayList<CachedDemographicNote>();
			for(CachedDemographicNote cdn:array){
				linkedNotes.add(cdn);
			}
		}catch(Exception e){
			MiscUtils.getLogger().error("Error Loading Notes for : "+demographicNo+" from local store ",e);
		}
		return linkedNotes;
    }
	
	
	public static List<CachedDemographicForm> getRemoteForms(Integer demographicNo,String table){
		if(!OscarProperties.getInstance().getBooleanProperty("INTEGRATOR_LOCAL_STORE","yes")) return null;
		
		List<CachedDemographicForm> linkedForms = null;
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(),demographicNo,CachedDemographicForm[].class.getName()+"+"+table);
		
		if (remoteIntegratedDataCopy == null){
			return linkedForms;
		}
		
		try{
			CachedDemographicForm[] array = remoteIntegratedDataCopyDao.getObjectFrom( CachedDemographicForm[].class, remoteIntegratedDataCopy);
			linkedForms = new ArrayList<CachedDemographicForm>();
			for(CachedDemographicForm cdn:array){
				linkedForms.add(cdn);
			}
		}catch(Exception e){
			MiscUtils.getLogger().error("Error Loading Notes for : "+demographicNo+" from local store ",e);
		}
		return linkedForms;
	}
	
}
