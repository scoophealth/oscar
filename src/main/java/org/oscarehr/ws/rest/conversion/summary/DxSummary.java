/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.ws.rest.conversion.summary;

import java.util.List;

import org.oscarehr.common.dao.AbstractCodeSystemDao;
import org.oscarehr.common.model.AbstractCodeSystemModel;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.managers.DxManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.model.SummaryItemTo1;
import org.oscarehr.ws.rest.to.model.SummaryTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oscar.util.StringUtils;

@Component
public class DxSummary implements Summary{

	@Autowired
	DxManager dxManager;

	private static final String ELLIPSES = "...";
	private static final int MAX_LEN_TITLE = 48;
	private static final int CROP_LEN_TITLE = 45;

	public SummaryTo1 getSummary(LoggedInInfo loggedInInfo,Integer demographicNo,String summaryCode){
		
		SummaryTo1 summary = new SummaryTo1("diseases",0,SummaryTo1.DISEASES);
		
		List<SummaryItemTo1> list = summary.getSummaryItem();
		int count = 0;
	   
	    fillDx( loggedInInfo, list,demographicNo,count);
	   
		return summary;
	}
	
	private void fillDx(LoggedInInfo loggedInInfo,List<SummaryItemTo1> list,Integer demographicNo,int count){

		List<Dxresearch> diseaseList = dxManager.getDiseases(loggedInInfo,  demographicNo);
        String description = null;
        Character status;
        String CodeSys;

        for( Dxresearch disease :diseaseList ) {
            status = disease.getStatus();
            if( status != null && status.compareTo('D') == 0 ) {
                continue;
            }
            CodeSys = disease.getCodingSystem();

            String daoName = AbstractCodeSystemDao.getDaoName(AbstractCodeSystemDao.codingSystem.valueOf(CodeSys));
            @SuppressWarnings("unchecked")
            AbstractCodeSystemDao<AbstractCodeSystemModel<?>> csDao = (AbstractCodeSystemDao<AbstractCodeSystemModel<?>>) SpringUtils.getBean(daoName);
            String code = disease.getDxresearchCode();
            if (code != null && !code.isEmpty()) {
                AbstractCodeSystemModel<?> codingSystemEntity = csDao.findByCode(code);
                description = codingSystemEntity.getDescription();
            }
            String tmp = code + " " + description;
            String strTitle = StringUtils.maxLenString(tmp, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
            
            SummaryItemTo1 summaryItem = new SummaryItemTo1(disease.getId(),strTitle,"action","diseases");
            summaryItem.setDate(disease.getStartDate());
            summaryItem.setAction("../oscarResearch/oscarDxResearch/setupDxResearch.do?quickList=&demographicNo="+demographicNo);
            list.add(summaryItem);
            count++;
        }	
	}
}
