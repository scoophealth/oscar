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
package org.oscarehr.study.types;

import java.util.List;

import org.caisi.dao.ProviderDAO;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.dao.StudyDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.study.Study;
import org.oscarehr.study.decisionSupport.StudyGuidelines;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class MyMedsStudy implements Study {
	
	private String providerNo;
	private int studyId;
	private String demographicNo;
	private List<Object> atcCodes;
	
    public MyMedsStudy(List<Object>args ) {
		this.demographicNo = (String)args.get(0);
		args.remove(0);
		this.providerNo = (String)args.get(0);
		args.remove(0);
		this.atcCodes = args;
	}

	@Override
	public String printInitcode() {
		
		boolean studyIsActive = isActive();
		StringBuilder javascript = new StringBuilder();		
		
		javascript.append("function updateSaveAllDrugsPrint() {");
		
		if( studyIsActive ) {
						
			javascript.append("var atcCodeArr = document.getElementsByName('atcCode');");
			javascript.append("var atcCodes = '';");			
			javascript.append("for(var idx = 0; idx < atcCodeArr.length; ++idx) {");			
			javascript.append("atcCodes += atcCodeArr[idx].value;");
			javascript.append("if( idx <= atcCodeArr.length - 2 ) {");
			javascript.append("atcCodes += ',';");
			javascript.append("}}");
			
			javascript.append("var data = 'method=RunStudy&studyId=");
			javascript.append(studyId);
			javascript.append("&providerNo=");
			javascript.append(this.providerNo);
			javascript.append("&demographicNo=");
			javascript.append(this.demographicNo);
			javascript.append("&args=' + atcCodes;");

			javascript.append("new Ajax.Request('../study/ManageStudy.do', {method:'post',parameters:data,onSuccess:function onSuccess(transport) {");
			javascript.append("var json=transport.responseText.evalJSON();");
			javascript.append("if( json.isEligible ) {");			
			javascript.append("var windowprops = \"height=350,width=650,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes\";");
            javascript.append("var popup=window.open('../study/myMedsPrompt.jsp?studyId=");
            javascript.append(this.studyId);
            javascript.append("&providerNo=" + this.providerNo + "&demographicNo=");
            javascript.append(this.demographicNo);
            javascript.append("&func=Print");
            javascript.append("', 'myMeds', windowprops);");
            javascript.append("}else {");
            javascript.append("updateSaveAllDrugsPrintContinue();");
            javascript.append("}}});");            
            
		}
		else {
			javascript.append("updateSaveAllDrugsPrintContinue();");
		}
				
		javascript.append("}");
		
		javascript.append("function updateSaveAllDrugs() {");
		
		if( studyIsActive ) {
			
			javascript.append("var atcCodeArr = document.getElementsByName('atcCode');");
			javascript.append("var atcCodes = '';");			
			javascript.append("for(var idx = 0; idx < atcCodeArr.length; ++idx) {");			
			javascript.append("atcCodes += atcCodeArr[idx].value;");
			javascript.append("if( idx <= atcCodeArr.length - 2 ) {");
			javascript.append("atcCodes += ',';");
			javascript.append("}}");
			
			javascript.append("var data = 'method=RunStudy&studyId=");
			javascript.append(studyId);
			javascript.append("&providerNo=");
			javascript.append(this.providerNo);
			javascript.append("&demographicNo=");
			javascript.append(this.demographicNo);
			javascript.append("&args=' + atcCodes;");

			javascript.append("new Ajax.Request('../study/ManageStudy.do', {method:'post',parameters:data,onSuccess:function onSuccess(transport) {");
			javascript.append("var json=transport.responseText.evalJSON();");
			javascript.append("if( json.isEligible ) {");			
			javascript.append("var windowprops = \"height=350,width=650,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes\";");
            javascript.append("var popup=window.open('../study/myMedsPrompt.jsp?studyId=");
            javascript.append(this.studyId);
            javascript.append("&providerNo=" + this.providerNo + "&demographicNo=");
            javascript.append(this.demographicNo);
            javascript.append("&func=Save");
            javascript.append("', 'myMeds', windowprops);");
            javascript.append("}else {");
            javascript.append("updateSaveAllDrugsContinue();");
            javascript.append("}}});");            
		}
		else {
			javascript.append("updateSaveAllDrugsContinue();");
		}
				
		javascript.append("}");
		
		return javascript.toString();
	}

	@Override
	public boolean run() {
		StudyGuidelines studyGuidelines = StudyGuidelines.getInstance(Study.MYMEDS);
		List<DSConsequence> listDSConsequence = studyGuidelines.evaluateAndGetConsequences(getLoggedInInfo(), demographicNo, providerNo, atcCodes);
		
		return (listDSConsequence.size() == 1);
		
	}
	
	private LoggedInInfo getLoggedInInfo() {
		String providerNo = OscarProperties.getInstance().getProperty("mymeds_job_run_as_provider");
		if(providerNo == null) {
			return null;
		}
		
		ProviderDAO providerDao = SpringUtils.getBean(ProviderDao.class);
		Provider provider = providerDao.getProvider(providerNo);
		
		if(provider == null) {
			return null;
		}
		
		SecurityDao securityDao = SpringUtils.getBean(SecurityDao.class);
		List<Security> securityList = securityDao.findByProviderNo(providerNo);
		
		if(securityList.isEmpty()) {
			return null;
		}
		
		LoggedInInfo x = new LoggedInInfo();
		x.setLoggedInProvider(provider);
		x.setLoggedInSecurity(securityList.get(0));
		return x;
	}
	
	public boolean isActive() {
		StudyDao studyDao = SpringUtils.getBean(StudyDao.class);
		org.oscarehr.common.model.Study study = studyDao.findByName(Study.MYMEDS);
		
		if( study == null ) {
			return false;
		}
		else {
			studyId = study.getId();
			return (study.getCurrent1() == 1);
		}
	}

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public int getStudyId() {
    	return studyId;
    }

	public void setStudyId(int studyId) {
    	this.studyId = studyId;
    }

}
