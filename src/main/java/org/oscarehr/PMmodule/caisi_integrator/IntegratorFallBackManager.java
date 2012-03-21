package org.oscarehr.PMmodule.caisi_integrator;

/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. 
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
 */

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.common.dao.RemoteIntegratedDataCopyDao;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.RemoteIntegratedDataCopy;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class IntegratorFallBackManager {
	static RemoteIntegratedDataCopyDao  remoteIntegratedDataCopyDao = SpringUtils.getBean(RemoteIntegratedDataCopyDao.class); 
	static DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	
	/*
	 * First call the Integrator, if there is a problem fall back to the local store.  
	 * If the integrator returns something it stores it in the local store, only for patients with primaryEMR set in demographicExt
	 * Issues
	 *  - Not sure how to tell who has access to the data
	 *  - When the note is display it looses who wrote it because the web service to find out provider information is down.
	 */
	public static List<CachedDemographicNote> getLinkedNotes(Integer demographicNo)  {
		
		List<CachedDemographicNote> linkedNotes = null;
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		boolean integratorLocalStore = OscarProperties.getInstance().getBooleanProperty("INTEGRATOR.LOCAL.STORE","yes");
		try{
			linkedNotes= CaisiIntegratorManager.getLinkedNotes(demographicNo);
			
			DemographicExt demographicExt = demographicDao.getLatestDemographicExt(demographicNo, "primaryEMR");
			if (integratorLocalStore && demographicExt != null && demographicExt.getValue().equals("1")){
				try{
					MiscUtils.getLogger().debug("Saving remote copy for "+demographicNo);
					CachedDemographicNote[] array = linkedNotes.toArray(new CachedDemographicNote[linkedNotes.size()]);
					
					remoteIntegratedDataCopyDao.save(demographicNo,array,loggedInInfo.loggedInProvider.getProviderNo(),loggedInInfo.currentFacility.getId());
	            } catch (Exception eSaveLocal) {
	            	MiscUtils.getLogger().error("Could not save local copy of xml ",eSaveLocal);
				}
			}

		}catch(Exception e2){
			MiscUtils.getLogger().error("Loading local remote notes for : "+demographicNo+" from local store "+linkedNotes,e2);
			if (integratorLocalStore && linkedNotes == null || linkedNotes.size() == 0){ 
				//TODO: add more security to this. Right now if anyone in the facility has viewed the remote notes they will be able to see them.
				//It would be easy to have each provider that viewed the record store a copy of it but im worried on storing that much data.
				RemoteIntegratedDataCopy remoteIntegratedDataCopy = remoteIntegratedDataCopyDao.findByDemoType(loggedInInfo.currentFacility.getId(),demographicNo,CachedDemographicNote[].class.getName());
				if (remoteIntegratedDataCopy != null){
					if (linkedNotes == null){
						linkedNotes = new ArrayList<CachedDemographicNote>();
					}
					try{
						CachedDemographicNote[] array = remoteIntegratedDataCopyDao.getObjectFrom( CachedDemographicNote[].class, remoteIntegratedDataCopy);
						for(CachedDemographicNote cdn:array){
							linkedNotes.add(cdn);
						}
					}catch(Exception e){
						MiscUtils.getLogger().error("Error Loading Notes for : "+demographicNo+" from local store ",e);
					}
				}
			}
		}
		return linkedNotes;
    }

}
