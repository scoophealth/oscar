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

import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oscar.dms.ConvertToEdoc;
import oscar.dms.EDoc;
import oscar.eform.EFormUtil;
import oscar.eform.data.EForm;
import oscar.log.LogAction;

@Service
public class EformDataManager {

	@Autowired
	SecurityInfoManager securityInfoManager;
	
	@Autowired
	EFormDataDao eFormDataDao;
	
	@Autowired
	DocumentManager documentManager;
	
	public EformDataManager() {
		// Default
	}
	
	public Integer saveEformData( LoggedInInfo loggedInInfo, EForm eform ) {
		Integer formid = null;
		
		if ( ! securityInfoManager.hasPrivilege(loggedInInfo, "_eform", SecurityInfoManager.UPDATE, null)) {
			throw new RuntimeException("missing required security object (_eform)");
		}

		EFormData eFormData = EFormUtil.toEFormData( eform );
		eFormDataDao.persist( eFormData );
		formid = eFormData.getId();
		
		if( formid != null ) {
			LogAction.addLogSynchronous(loggedInInfo, "EformDataManager.saveEformData", "Saved EformDataID=" + formid);
		} else {
			LogAction.addLogSynchronous(loggedInInfo, "EformDataManager.saveEformData", "Failed to save eform EformDataID=" + formid);
		}
		
		return formid;
	}
	
	/**
	 * Saves an form as PDF EDoc. 
	 * Returns the Eform id that was saved.
	 */
	public Integer saveEformDataAsEDoc( LoggedInInfo loggedInInfo, String fdid ) {

		// Integer formid = saveEformData( loggedInInfo, eform );
		Integer documentId = null;
		Integer formid = null;
		
		if( fdid != null ) {
			formid = Integer.parseInt( fdid );
			EFormData eformData = eFormDataDao.find( formid );			
			EDoc edoc = ConvertToEdoc.from( eformData );
			documentManager.moveDocument( loggedInInfo, edoc.getDocument(), ConvertToEdoc.getFilePath(), null );
			documentId = documentManager.saveDocument( loggedInInfo, edoc );
		}
		
		if( documentId != null ) {
			LogAction.addLogSynchronous(loggedInInfo, "EformDataManager.saveEformDataAsEDoc", "Document ID saved: " + documentId );
		} else {
			LogAction.addLogSynchronous(loggedInInfo, "EformDataManager.saveEformDataAsEDoc", "Document conversion for Eform id: " + formid + " failed.");
		}
		
		return documentId;
	}

}
