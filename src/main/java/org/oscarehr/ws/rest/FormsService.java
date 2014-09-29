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
package org.oscarehr.ws.rest;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.FormsManager;
import org.oscarehr.ws.rest.to.model.FormListTo1;
import org.oscarehr.ws.rest.to.model.FormTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Service for interacting with forms (eforms and sql table forms in oscar) 
 */
@Path("/forms")
@Component("formsService")
public class FormsService extends AbstractServiceImpl {
	
	
	@Autowired
	private DemographicManager demographicManager;
	
	@Autowired
	private FormsManager formsManager;
	
	@GET
	@Path("/{demographicNo}/all")
	@Produces("application/json")
	public FormListTo1 getFormsForHeading(@PathParam("demographicNo") Integer demographicNo ,@QueryParam("heading") String heading){
		FormListTo1 formListTo1 = new FormListTo1();
		if(heading.equals("Completed")){
			List<EFormData> completedEforms = formsManager.findByDemographicId(getLoggedInInfo(),demographicNo);
			Collections.sort(completedEforms, Collections.reverseOrder(EFormData.FORM_DATE_COMPARATOR));
			
			for(EFormData eformData: completedEforms){	
				int id = eformData.getId();
				int formId = eformData.getFormId();
				String name = eformData.getFormName();
				String subject = eformData.getSubject();
				String status = eformData.getSubject();
				Date date = eformData.getFormDate();
				Boolean showLatestFormOnly = eformData.isShowLatestFormOnly();
				formListTo1.add(FormTo1.create(id, demographicNo, formId, FormsManager.EFORM, name, subject, status, date, showLatestFormOnly));
			}
			
		}else{  // Only two options right now.  Need to change this anyways
			List<EForm> eforms =  formsManager.findByStatus(getLoggedInInfo(),true, null);  //This will have to change to accommodate forms too.
			Collections.sort(eforms,EForm.FORM_NAME_COMPARATOR);
			for(EForm eform :eforms){
				int formId = eform.getId();
				String name = eform.getFormName();
				String subject = eform.getSubject();
				String status = null;
				Date date = null;
				Boolean showLatestFormOnly = eform.isShowLatestFormOnly();
				formListTo1.add(FormTo1.create(null, demographicNo, formId, FormsManager.EFORM, name, subject, status, date, showLatestFormOnly));
			}
		}
		return formListTo1;
	}

}
