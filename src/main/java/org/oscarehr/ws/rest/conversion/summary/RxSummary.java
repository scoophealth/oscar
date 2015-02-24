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

import java.util.List;

import org.oscarehr.common.model.Drug;
import org.oscarehr.managers.PrescriptionManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.SummaryItemTo1;
import org.oscarehr.ws.rest.to.model.SummaryTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oscar.util.StringUtils;




@Component
public class RxSummary implements Summary{

	@Autowired
	PrescriptionManager rxManager;
	
	protected static final String ELLIPSES = "...";
	protected static final int MAX_LEN_TITLE = 48;
	protected static final int CROP_LEN_TITLE = 45;
	protected static final int MAX_LEN_KEY = 12;
	protected static final int CROP_LEN_KEY = 9;
	
	public SummaryTo1 getSummary(LoggedInInfo loggedInInfo,Integer demographicNo,String summaryCode){
		
		SummaryTo1 summary = new SummaryTo1("Meds",0,SummaryTo1.MEDICATIONS_CODE);
		
		List<SummaryItemTo1> list = summary.getSummaryItem();
		int count = 0;
	   
	    fillRx( loggedInInfo, list,demographicNo,count);
	   
		return summary;
	}
	
	private void fillRx(LoggedInInfo loggedInInfo,List<SummaryItemTo1> list,Integer demographicNo,int count){

		List<Drug> drugList = rxManager.getUniqueDrugsByPatient( loggedInInfo,  demographicNo);
		
		long now = System.currentTimeMillis();
        long month = 1000L * 60L * 60L * 24L * 30L;
        for( Drug drug :drugList ) {
            if( drug.isArchived() )
                continue;
            if(drug.isHideFromDrugProfile()) {
            	continue;
            }

            String styleColor = "";
            if (drug.isCurrent() && (drug.getEndDate().getTime() - now <= month)) {
                styleColor="style=\"color:orange;font-weight:bold;\"";
            }else if (drug.isCurrent() )  {
                styleColor="style=\"color:blue;\"";
            }else if (drug.isLongTerm() )  {
                styleColor="style=\"color:grey;\"";
            }else
                continue;
            
            String tmp = "";
            if (drug.getFullOutLine()!=null) tmp=drug.getFullOutLine().replaceAll(";", " ");
            String strTitle = StringUtils.maxLenString(tmp, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
            
            SummaryItemTo1 summaryItem = new SummaryItemTo1(drug.getId(),strTitle,"action","rx");
            summaryItem.setDate(drug.getRxDate());
            summaryItem.setAction("../oscarRx/choosePatient.do?demographicNo="+demographicNo);  // for now, open the Rx module if the user clicks on any meds.
             
            list.add(summaryItem);
            count++;
        }	
	}
}
