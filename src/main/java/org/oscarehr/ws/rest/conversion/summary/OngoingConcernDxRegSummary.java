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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.DiagnosisTo1;
import org.oscarehr.ws.rest.to.model.SummaryItemTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class OngoingConcernDxRegSummary extends IssueNoteSummary implements Summary  {
	private static final Logger logger = MiscUtils.getLogger();
	
	@Autowired
    @Qualifier("DxresearchDAO")
    protected DxresearchDAO dxresearchDao;
	
	@Autowired
	private ProviderDao providerDao;
	
	protected void getSummaryListForIssuedNotes(LoggedInInfo loggedInInfo,Integer demographicNo, List<SummaryItemTo1> list, String[] issueCodes){
		
		List<Dxresearch> dxList =  dxresearchDao.findNonDeletedByDemographicNo(demographicNo);
		List<Integer> usedDx = new ArrayList<Integer>();
		List<Issue> issueList = new ArrayList<Issue>();
		for (int j = 0; j < issueCodes.length; ++j) {
			issueList.addAll(caseManagementMgr.getIssueInfoByCode(loggedInInfo.getLoggedInProviderNo(), issueCodes[j]));
		}
		String[] issueIds = getIssueIds(issueList);
		
		Collection<CaseManagementNote> notes = caseManagementMgr.getActiveNotes(""+demographicNo, issueIds);
		
		String cppExts = "";
		int count = 0;
		for(CaseManagementNote note:notes){
			String classification = null;
			Set<CaseManagementIssue> issueSet = note.getIssues();
			StringBuilder issueBuilder = new StringBuilder();
			for (CaseManagementIssue s : issueSet) {
				issueBuilder.append(s.getIssue().getCode());
				for(Dxresearch dx:dxList){
					if(s.getIssue().getType().equals(dx.getCodingSystem()) && s.getIssue().getCode().equals(dx.getDxresearchCode())){
						usedDx.add(dx.getId());
						logger.debug("Added dx  id "+dx.getId());
						classification = "Dx: "+dxresearchDao.getDescription(dx.getCodingSystem(),dx.getDxresearchCode());
					}
				}
				
				
			}
			String issueString = issueBuilder.toString();
			
			if( preferenceManager.isCppItem(issueString) && preferenceManager.isCustomSummaryEnabled(loggedInInfo) ){
				cppExts = preferenceManager.getCppExtsItem(loggedInInfo, caseManagementMgr.getExtByNote(note.getId()), issueString);
			}
			logger.debug("IssueString "+issueString+" ccpExts "+cppExts);
			SummaryItemTo1 summaryItem = new SummaryItemTo1(count, note.getNote() + cppExts,"action","notes"+issueString);
			summaryItem.setDate(note.getObservation_date());
			summaryItem.setEditor(note.getProviderName());
			summaryItem.setNoteId(note.getId());
			if(classification != null){
				summaryItem.setClassification(classification);
			}
			
			list.add(summaryItem);
			count++;
		}
		
		for(Dxresearch dx:dxList){
			logger.debug("checking for dx id"+dx.getId()+" = "+(!usedDx.contains(dx.getId())));
			if(!usedDx.contains(dx.getId())){
				String dxDesc = dxresearchDao.getDescription(dx.getCodingSystem(),dx.getDxresearchCode());
				if(dxDesc != null){
					SummaryItemTo1 summaryItem = new SummaryItemTo1(count, "Dx: "+dxDesc,"add","dx_reg");
					summaryItem.setDate(dx.getStartDate());
					summaryItem.setEditor(getProviderName(dx.getProviderNo()));
					summaryItem.setNoteId(null);
					summaryItem.setClassification("DX-REG");
					DiagnosisTo1 diagnosisTo1 = new DiagnosisTo1();
					diagnosisTo1.setCode(dx.getDxresearchCode());
					diagnosisTo1.setCodingSystem(dx.getCodingSystem());
					diagnosisTo1.setDescription(dxDesc);
					summaryItem.setExtra(diagnosisTo1);
					count++;
					list.add(summaryItem);
				}
			}
		}
		
		Collections.sort(list, Collections.reverseOrder(new Comparator<SummaryItemTo1>() {
			  public int compare(SummaryItemTo1 o1, SummaryItemTo1 o2) {
			      return o1.getDate().compareTo(o2.getDate());
			  }
		}));
		
		for(int i = 0; i < list.size(); i++){
			list.get(i).setId(i);
		}
		
	}
	
	
	private String getProviderName(String providerNo){
		String providerName = "";
		Provider provider = providerDao.getProvider(providerNo);

		if (provider != null) {
			if (provider.getLastName() != null) {
				providerName = provider.getLastName() + ", ";
			}
	
			if (provider.getFirstName() != null) {
				providerName += provider.getFirstName();
			}
		}
		return providerName;
	}
	
}
