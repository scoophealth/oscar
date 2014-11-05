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
package org.oscarehr.renal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarmcmaster.ckd.CKDConfig;
import org.oscarmcmaster.ckd.CkdConfigDocument;
import org.oscarmcmaster.ckd.DxCodes.Code;

public class CkdScreener {
	
	private Logger logger = MiscUtils.getLogger();

	private DxresearchDAO dxResearchDao = (DxresearchDAO)SpringUtils.getBean("DxresearchDAO");
	private MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	private CaseManagementNoteDAO caseManagementNoteDao = (CaseManagementNoteDAO)SpringUtils.getBean("caseManagementNoteDAO");
	private IssueDAO issueDao = (IssueDAO) SpringUtils.getBean("IssueDAO");
	private DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	private BillingONCHeader1Dao billingDao = SpringUtils.getBean(BillingONCHeader1Dao.class);
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private DrugDao drugDao = SpringUtils.getBean(DrugDao.class);
	
	private CKDConfig config = null;
	private Map<String,Boolean> excludeMap = new HashMap<String,Boolean>();
	
	List<Issue> issues = new ArrayList<Issue>();
	
	public static List<String> getInterestingATCs() {
		List<String> atcList = new ArrayList<String>();
		atcList.add("C09CA08");
		atcList.add("C09DB04");
		atcList.add("C09CA01");
		atcList.add("C09DA08");
		atcList.add("C09DA07");
		atcList.add("C09DA02");
		atcList.add("C09DA06");
		atcList.add("C09DA03");
		atcList.add("C09DA04");
		atcList.add("C09CA07");
		atcList.add("C09CA02");
		atcList.add("C09CA06");
		atcList.add("C09CA04");
		atcList.add("C09CA03");
		atcList.add("C09DA01");
		atcList.add("C09BA05");
		atcList.add("C09AA06");
		atcList.add("C09AA08");
		atcList.add("C09AA07");
		atcList.add("C09AA02");
		atcList.add("C09BA03");
		atcList.add("C09BA02");
		atcList.add("C09BA04");
		atcList.add("C09BB10");
		atcList.add("C09BB05");
		atcList.add("C09BA08");
		atcList.add("C09BA06");
		atcList.add("C09AA10");
		atcList.add("C09AA03");
		atcList.add("C09AA04");
		atcList.add("C09AA09");
		atcList.add("C09AA05");
		atcList.add("C09AA01");
		atcList.add("C09XA02");
		atcList.add("C09XA52");
		return atcList;
	}
	public CkdScreener() {
		try {
			CkdConfigDocument doc = CkdConfigDocument.Factory.parse(this.getClass().getResourceAsStream("/ckd.xml"));
			config = doc.getCkdConfig();
		}catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
			throw new RuntimeException("Unable to load CKD configuration file.");
		}
		
		for(String issueStr:config.getHx().getIssues().getIssueArray()) {
			issues.add(issueDao.findIssueByCode(issueStr));
		}
		
		if(config.getExcludes() != null && config.getExcludes().getExcludeArray() != null) {
			for(String exclude:config.getExcludes().getExcludeArray()) {
				excludeMap.put(exclude, true);
			}
		}
	}
	
	public void screenPopulation(LoggedInInfo loggedInInfo) {
		logger.debug("beginning screening");
		//TODO: only ones which havn't been screened, or don't have have active dx for screening
		List<Integer> demographicNos = demographicDao.getActiveDemographicIds();
		for(Integer demographicNo:demographicNos) {
			List<String> reasons = new ArrayList<String>();
			MyBoolean first = new MyBoolean();
			first.setValue(false);
			boolean isMatch = screenDemographic(demographicNo,reasons, first);
			logger.debug("demographicNo " + demographicNo + " isMatch=" + isMatch);
			
	        boolean notify=false;
	        
	        
	        if(!notify) {
	        	//there's no active ones, but let's look at the latest one
	        	List<Dxresearch> drs = dxResearchDao.find(demographicNo, "OscarCode", "CKDSCREEN");
	        	if(drs.size()>0) {
	        		Dxresearch dr = drs.get(0);
	        		Calendar aYearAgo = Calendar.getInstance();
	        		aYearAgo.add(Calendar.MONTH, -12);
	        		if(dr.getUpdateDate().before(aYearAgo.getTime())) {
	        			notify=true;
	        			//reopen it
	        			dr.setStatus('A');
	        			dr.setUpdateDate(new Date());
	        			dxResearchDao.merge(dr);
	        		}
	        	}
	        }
	        
	        if(first.getValue().booleanValue()==true) {
	        	notify=true;
	        }
			
			if(isMatch && notify) {
				//notification stuff
				boolean generateNotification=true;

				if(generateNotification) {
					logger.debug("generating notification");
					CkdNotificationManager notificationMgr = new CkdNotificationManager();
					notificationMgr.doNotify(loggedInInfo,demographicNo,reasons);
				}
			}
			
		}		
		
	}
	
	public boolean screenDemographic(int demographicNo, List<String> reasons, MyBoolean first) {
		logger.debug("checking demographic " + demographicNo);
		
		Demographic demographic = demographicDao.getDemographicById(demographicNo);
		if(demographic != null && demographic.getProviderNo() != null && demographic.getProviderNo().length()>0) {
			if(excludeMap.get(demographic.getProviderNo()) != null) {
				logger.debug("skipping " + demographicNo + " - provider has opted out");
				return false;
			}
		}
		
		if(hasActiveDxCode(demographicNo,"icd9","585"))  {
			logger.debug("skipping " + demographicNo + " - already dx'ed with Chronic Renal Failure");
			return false;
		}
		
		//Dx Codes
		boolean positiveDxMatch=false;
		for(Code code:config.getDxCodes().getCodeArray()) {
			String type = code.getType().toString();
			String value = code.getStringValue();
			
			boolean tmp = hasActiveDxCode(demographicNo,type,value);
			boolean tmp2 = hasBilledDxCode(demographicNo,type, value);
			if(tmp || tmp2) {
				reasons.add("Patient diagnosted with (" + type + ":" + value + ") " + code.getName());
				positiveDxMatch=true;
			}
			MiscUtils.getLogger().debug(type +":"+value + " returned "+  (tmp||tmp2) + " for " + demographicNo);
		}
		logger.debug("positiveDxMatch:"+positiveDxMatch);
		
		
		boolean positiveDrugMatch=checkMedication(demographicNo);
		if(positiveDrugMatch) {
			reasons.add("Patient currently taking medications associated with Hypertension");
		}
		
		logger.debug("positiveDrugMatch:"+positiveDrugMatch);
		
		
		//Blood Pressure - 50% of at least 2 readings are above 140/90
		int sys = Integer.parseInt(config.getBp().getSystolic());
		int dia = Integer.parseInt(config.getBp().getDiastolic());
		
		boolean positiveBP = this.checkBP(demographicNo);
		logger.debug("BP match:"+positiveBP);
		if(positiveBP)
			reasons.add(">50% of blood pressure readings over " + sys +"/"+dia);
		
		//CPP History - Matches text (regular expressions)
		boolean positiveCppMatch = this.checkCpp(demographicNo);
		logger.debug("CPP match:"+positiveCppMatch);
		if(positiveCppMatch)
			reasons.add("Family Hx");
		
		boolean positiveAboriginalMatch=false;
		DemographicExt ab = demographicExtDao.getLatestDemographicExt(demographicNo, "aboriginal");
		if(ab != null && ab.getValue().equals("Yes")) {
			positiveAboriginalMatch=true;
		}
		logger.debug("Aboriginal match:"+positiveAboriginalMatch);
		if(positiveAboriginalMatch)
			reasons.add("Aboriginal descent");
		
		boolean labs = checkLabs(demographicNo);
		logger.debug("labs match:"+labs);
		if(labs)
			reasons.add("Overdue EGFR Labs (>12 months)");
		
		boolean isMatch=false;
		
		if((positiveDxMatch || positiveBP || positiveCppMatch || positiveAboriginalMatch || positiveDrugMatch ) && labs) {
			isMatch=true;
		}
		
		logger.debug("patient match:"+isMatch);
		
		
		if(!isMatch)
			return false;
		
		//has patient been matched before?
		List<Dxresearch> dxs = dxResearchDao.find(demographicNo, "OscarCode", "CKDSCREEN");
		if(dxs.isEmpty()) {	
			//tag patient w/ screening
			if(first != null)
				first.setValue(true);
			Dxresearch dr = new Dxresearch();
			dr.setCodingSystem("OscarCode");
			dr.setDxresearchCode("CKDSCREEN");
			dr.setDemographicNo(demographicNo);
			dr.setStartDate(new java.util.Date());
			dr.setUpdateDate(new java.util.Date());
			dr.setStatus('A');
			dxResearchDao.persist(dr);
		} 
		return true;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean hasActiveDxCode(Integer demographicNo, String codingSystem, String code) {
		return dxResearchDao.activeEntryExists(demographicNo, codingSystem, code);
	}
	
	private boolean hasBilledDxCode(Integer demographicNo, String codingSystem, String code) {
		return billingDao.getBillingItemByDxCode(demographicNo, code).size()>0;
	}
	
	protected boolean checkBP(int demographicNo) {
		int sys = Integer.parseInt(config.getBp().getSystolic());
		int dia = Integer.parseInt(config.getBp().getDiastolic());
		
		List<Measurement> bps = measurementDao.findByType(demographicNo, "BP");
		
		//need at least 2 measurements
		if(bps.size()<2) {
			return false;
		}
		
		boolean positiveBP=false;
		int count=0;
		for(Measurement bp:bps) {
			String  val = bp.getDataField();
			if(val != null && val.indexOf("/") != -1) {
				int systolic = Integer.parseInt(val.split("/")[0]);
				int diastolic =Integer.parseInt(val.split("/")[1]);
				if(systolic > sys && diastolic > dia) {
					count++;
				}
			}
		}
		
		if(count >= Math.ceil((bps.size()/2.0))) {
			positiveBP=true;
		}
				
		return positiveBP;
	}
	
	protected boolean checkCpp(int demographicNo) {
		boolean result=false;

		for(Issue issue:issues) {
			List<CaseManagementNote> notes = caseManagementNoteDao.getCPPNotes(String.valueOf(demographicNo), issue.getId(), null);
			for(CaseManagementNote note:notes) {
				
				StringBuilder exp = new StringBuilder();
				for(int x=0;x<config.getHx().getSearchtext().getTextArray().length;x++) {
					if(exp.length()>0)
						exp.append("|");
					exp.append("(?i)" + config.getHx().getSearchtext().getTextArray(x));
					
				}
				
				if(note.getNote().matches(exp.toString())) {
					MiscUtils.getLogger().debug("match! - " + note.getNote());
					result=true;
					
				}
			}
		}
		
		return result;
	
	}
	
	protected boolean checkMedication(int demographicNo) {
		for(Drug d:drugDao.findByDemographicId(demographicNo, true)) {
			if(!d.isDeleted()) {
			//if(d.isLongTerm() || (!d.isExpired() && !d.isArchived() && !d.isDeleted() && !d.isDiscontinued())) {
				//does it match one of the ATCs?
				if(getInterestingATCs().contains(d.getAtc())) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected boolean checkLabs(int demographicNo) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		
		boolean labs=false;
		//labs over a year
		List<Measurement> measures = measurementDao.findByType(demographicNo, "EGFR");
		if(measures == null || measures.size() == 0 || 
				(measures != null && measures.get(0) != null && measures.get(0).getDateObserved()!= null && measures.get(0).getDateObserved().before(cal.getTime()))) {
			MiscUtils.getLogger().debug("Missing EGFR lab");
			//we  don't have
			labs=true;
		}
		/*
		measures = measurementDao.findByType(demographicNo, "ACR");
		if(measures == null || measures.size() == 0 || measures.get(0).getDateObserved().before(cal.getTime())) {
			MiscUtils.getLogger().info("Missing ACR lab");
			//we  don't have
			labs=true;
		}
		*/
		return labs;
	}
	
	public class MyBoolean {  
		   private Boolean value;  
		   
		   public MyBoolean() {  
		   }  
		   public MyBoolean(Boolean b) {  
		      value = b;  
		   }  
		   public MyBoolean(String b) {  
		      value = new Boolean(b);  
		   }  
		   public MyBoolean(boolean b) {  
		      value = new Boolean(b);  
		   }  
		   public Boolean getValue() {  
		      return this.value;  
		   }  
		   public void setValue(Boolean b) {  
		      this.value = b;  
		   }  
		   public void setValue(MyBoolean b) {  
		      this.value = b.getValue();  
		   }  
		}  
	
}
