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
package org.oscarehr.ws.rest.conversion.summary;


import java.util.Collections;
import java.util.List;

import org.oscarehr.common.model.EFormData;
import org.oscarehr.managers.FormsManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.SummaryItemTo1;
import org.oscarehr.ws.rest.to.model.SummaryTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;





@Component
public class FormsSummary implements Summary{
	//private static Logger logger = MiscUtils.getLogger();
	
	@Autowired
	private FormsManager formsManager;// = SpringUtils.getBean(FormsManager.class);
    	
	public SummaryTo1 getSummary(LoggedInInfo loggedInInfo,Integer demographicNo,String summaryCode){
		
		SummaryTo1 summary = new SummaryTo1("Assessments",0,SummaryTo1.ASSESSMENTS_CODE);
		
		List<SummaryItemTo1> list = summary.getSummaryItem();
		int count = 0;
		
	    fillEforms( loggedInInfo, list,demographicNo,count);

		return summary;
	}
	
	
	
	private  void fillEforms(LoggedInInfo loggedInInfo,List<SummaryItemTo1> list,Integer demographicNo,int count){

		List<EFormData> completedEforms = formsManager.findByDemographicId(loggedInInfo,demographicNo);
		Collections.sort(completedEforms, Collections.reverseOrder(EFormData.FORM_DATE_COMPARATOR));
			
		for(EFormData eformData: completedEforms){	
			int id = eformData.getId();
			
			//list.add(new SummaryItemTo1(id, eformData.getFormName(),"#/record/"+demographicNo+"/forms/eform/id/"+id));
			list.add(new SummaryItemTo1(id, eformData.getFormName(),"record.forms.existing","eform"));
			count++;
			/*int formId = eformData.getFormId();
			String name = eformData.getFormName();
			String subject = eformData.getSubject();
			String status = eformData.getSubject();
			Date date = eformData.getFormDate();
			Boolean showLatestFormOnly = eformData.isShowLatestFormOnly();
			formListTo1.add(FormTo1.create(id, demographicNo, formId, FormsManager.EFORM, name, subject, status, date, showLatestFormOnly));
			*/
		}
		
		
		

	}
	
}
