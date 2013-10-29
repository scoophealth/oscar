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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.FormLabReq07Dao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementMapDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.FormLabReq07;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementMap;
import org.oscarehr.util.SpringUtils;

public class ReportHelper {

	static DxresearchDAO dxDao = (DxresearchDAO)SpringUtils.getBean("DxresearchDAO");
	static DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
	static MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	static DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	static MeasurementMapDao measurementMapDao = (MeasurementMapDao)SpringUtils.getBean("measurementMapDao");
	static FormLabReq07Dao labReq07Dao = SpringUtils.getBean(FormLabReq07Dao.class);
	static DrugDao drugDao = SpringUtils.getBean(DrugDao.class);
	
	static CkdScreener screener = new CkdScreener();
	
	
	
	public static boolean patientScreenedInLastYear(int demographicNo) {
		//acr
		List<String> idents = new ArrayList<String>();
		List<MeasurementMap> maps = measurementMapDao.getMapsByLoinc("9318-7");
		for(MeasurementMap map:maps) {
			idents.add(map.getIdentCode());
		}
		//eGFR
		maps = measurementMapDao.getMapsByLoinc("33914-3");
		for(MeasurementMap map:maps) {
			idents.add(map.getIdentCode());
		}
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -1);
		List<Measurement> ms =  measurementDao.findByType(demographicNo, idents,c.getTime());
		
		if(ms.size()==0)
			return false;
		
		//bp
		List<Measurement> bps = measurementDao.findByType(demographicNo, "BP",c.getTime());
		
		if(ms.size()>0 && bps.size()>0)
			return true;
		
		return false;
	}
	
	public static boolean patientScreened(int demographicNo) {
		//acr
		List<String> idents = new ArrayList<String>();
		List<MeasurementMap> maps = measurementMapDao.getMapsByLoinc("9318-7");
		for(MeasurementMap map:maps) {
			idents.add(map.getIdentCode());
		}
		//eGFR
		maps = measurementMapDao.getMapsByLoinc("33914-3");
		for(MeasurementMap map:maps) {
			idents.add(map.getIdentCode());
		}
		
		List<Measurement> ms =  measurementDao.findByType(demographicNo,idents);
		
		if(ms.size()==0)
			return false;
		
		//bp
		List<Measurement> bps = measurementDao.findByType(demographicNo, "BP");
		
		if(ms.size()>0 && bps.size()>0)
			return true;
		
		return false;
	}

	public static void getTotals(ReportDataContainer r) {
		int diabeticCount = 0;
		int hypertensiveCount=0;
		int bpCount=0;
		int famCount=0;
		int aboriginalCount=0;
		
		List<Dxresearch> diabetics = dxDao.findCurrentByCodeTypeAndCode("icd9","250");
		for(Dxresearch d:diabetics) {
			Integer demographicNo = d.getDemographicNo();
			Demographic demo = demographicDao.getDemographic(String.valueOf(demographicNo));
			if(demo.isActive()) {
				diabeticCount++;
			}
		}
		r.setTotalDiabetic(diabeticCount);
		
		List<Dxresearch> hypertensives = dxDao.findCurrentByCodeTypeAndCode("icd9","401");
		for(Dxresearch d:hypertensives) {
			Integer demographicNo = d.getDemographicNo();
			Demographic demo = demographicDao.getDemographic(String.valueOf(demographicNo));
			if(demo.isActive()) {
				hypertensiveCount++;
			}
		}
		r.setTotalHypertensive(hypertensiveCount);
		
		List<Measurement> bps = measurementDao.findByType("BP");
		for(Measurement m:bps) {
			if(m.getDataField().indexOf("/") == -1) {
				continue;
			}
			String[] bp = m.getDataField().split("/");
			try {
				if(Integer.parseInt(bp[0]) > 140 || Integer.parseInt(bp[1]) > 90) {
					Demographic demo = demographicDao.getDemographic(String.valueOf(m.getDemographicId()));
					if( demo == null || !demo.isActive()) {
						continue;
					}
					boolean diab = dxDao.entryExists(demo.getDemographicNo(), "icd9", "250");
					boolean hyp = dxDao.entryExists(demo.getDemographicNo(), "icd9", "401");
					if(!diab && !hyp) {
						bpCount++;
					}
				
				}
			}
			catch( NumberFormatException e ) {
				//ignore
			}
		}
		r.setTotalBp(bpCount);
		
	
		List<Integer> demos = demographicDao.getActiveDemographicIds();
		for(Integer d:demos) {
			boolean result = screener.checkCpp(d);
			if(result){
				famCount++;
			}
		}
		r.setTotalFamHx(famCount);
		
		List<Integer> demoIds = demographicExtDao.findDemographicIdsByKeyVal("aboriginal","Yes");
		aboriginalCount = demoIds.size();
		r.setTotalAboriginals(aboriginalCount);
		
		int ageCount = demographicDao.getActiveDemographicIdsOlderThan(55).size();
		r.setTotalAge(ageCount);
	}
	
	public static void getTotalsScreenedLastYear(ReportDataContainer r) {
		int diabeticCount = 0;
		int hypertensiveCount=0;
		int bpCount=0;
		int famCount=0;
		int aboriginalCount=0;
		int ageCount=0;
		
		List<Dxresearch> diabetics = dxDao.findCurrentByCodeTypeAndCode("icd9","250");
		for(Dxresearch d:diabetics) {
			Integer demographicNo = d.getDemographicNo();
			Demographic demo = demographicDao.getDemographic(String.valueOf(demographicNo));
			if(demo != null && demo.isActive() && patientScreenedInLastYear(demo.getDemographicNo())) {
				diabeticCount++;
			}
		}
		r.setDiabeticScreened1yr(diabeticCount);
		
		List<Dxresearch> hypertensives = dxDao.findCurrentByCodeTypeAndCode("icd9","250");
		for(Dxresearch d:hypertensives) {
			Integer demographicNo = d.getDemographicNo();
			Demographic demo = demographicDao.getDemographic(String.valueOf(demographicNo));
			if(demo != null && demo.isActive() && patientScreenedInLastYear(demo.getDemographicNo())) {
				hypertensiveCount++;
			}
		}
		r.setHypertensiveScreened1yr(hypertensiveCount);
		
		
		List<Measurement> bps = measurementDao.findByType("BP");
		Map<Integer,Boolean> bpPatients = new HashMap<Integer,Boolean>();
		for(Measurement m:bps) {
			if(m.getDataField().indexOf("/") == -1) {
				continue;
			}
			String[] bp = m.getDataField().split("/");
			
			try {
				if(Integer.parseInt(bp[0]) > 140 || Integer.parseInt(bp[1]) > 90) {
					bpPatients.put(m.getDemographicId(), true);
				}
			}
			catch( NumberFormatException e ) {
				//ignore
			}
		}
		for(Integer demographicNo:bpPatients.keySet()) {
			Demographic d = demographicDao.getDemographicById(demographicNo);
			if(d != null && d.isActive() && patientScreenedInLastYear(demographicNo)) {
				bpCount++;
			}
		}
		r.setBpScreened1yr(bpCount);
		
		
		List<Integer> demos = demographicDao.getActiveDemographicIds();
		for(Integer d:demos) {
			if(patientScreenedInLastYear(d)) {
				boolean result = screener.checkCpp(d);
				
				if(result){
					famCount++;
				}
			}
		}
		r.setFamHxScreened1yr(famCount);
		
		List<Integer> demoIds = demographicExtDao.findDemographicIdsByKeyVal("aboriginal","Yes");
		Map<Integer,Boolean> map = new HashMap<Integer,Boolean>();
		for(Integer demoId:demoIds) {
			if(patientScreenedInLastYear(demoId)) {
				map.put(demoId, true);
			}
		}
		r.setAboriginalScreened1yr(map.keySet().size());
		
		List<Integer> ages = demographicDao.getActiveDemographicIdsOlderThan(55);
		for(Integer demoId:ages) {
			if(patientScreenedInLastYear(demoId))
				ageCount++;
		}
		r.setAgeScreened1yr(ageCount);
	}
	
	
	public static void getTotalsScreened(ReportDataContainer r) {
		int diabeticCount = 0;
		int hypertensiveCount=0;
		int bpCount=0;
		int famCount=0;
		int aboriginalCount=0;
		int ageCount=0;
		
		List<Dxresearch> diabetics = dxDao.findCurrentByCodeTypeAndCode("icd9","250");
		for(Dxresearch d:diabetics) {
			Integer demographicNo = d.getDemographicNo();
			Demographic demo = demographicDao.getDemographic(String.valueOf(demographicNo));
			if(demo.isActive() && patientScreened(demo.getDemographicNo())) {
				diabeticCount++;
			}
		}
		r.setDiabeticScreened(diabeticCount);
		
		List<Dxresearch> hypertensives = dxDao.findCurrentByCodeTypeAndCode("icd9","250");
		for(Dxresearch d:hypertensives) {
			Integer demographicNo = d.getDemographicNo();
			Demographic demo = demographicDao.getDemographic(String.valueOf(demographicNo));
			if(demo.isActive() && patientScreened(demo.getDemographicNo())) {
				hypertensiveCount++;
			}
		}
		r.setHypertensiveScreened(hypertensiveCount);
		
		List<Measurement> bps = measurementDao.findByType("BP");
		Map<Integer,Boolean> bpPatients = new HashMap<Integer,Boolean>();
		for(Measurement m:bps) {
			if(m.getDataField().indexOf("/") == -1) {
				continue;
			}
			String[] bp = m.getDataField().split("/");
			
			try {
				if(Integer.parseInt(bp[0]) > 140 || Integer.parseInt(bp[1]) > 90) {
					bpPatients.put(m.getDemographicId(), true);
				}
			}
			catch( NumberFormatException e ) {
				//ignore
			}
		}
		for(Integer demographicNo:bpPatients.keySet()) {
			Demographic d = demographicDao.getDemographicById(demographicNo);
			if(d != null && d.isActive() && patientScreened(demographicNo)) {
				bpCount++;
			}
		}
		r.setBpScreened(bpCount);
		
		
		List<Integer> demos = demographicDao.getActiveDemographicIds();
		for(Integer d:demos) {
			if(patientScreened(d)) {
				boolean result=screener.checkCpp(d);
				if(result){
					famCount++;
				}
			}
		}
		r.setFamHxScreened(famCount);
		
		List<Integer> demoIds = demographicExtDao.findDemographicIdsByKeyVal("aboriginal","Yes");
		Map<Integer,Boolean> map = new HashMap<Integer,Boolean>();
		for(Integer demoId:demoIds) {
			if(patientScreenedInLastYear(demoId)) {
				map.put(demoId, true);
			}
		}
		r.setAboriginalScreened(map.keySet().size());
		
		
		List<Integer> ages = demographicDao.getActiveDemographicIdsOlderThan(55);
		for(Integer demoId:ages) {
			if(patientScreened(demoId))
				ageCount++;
		}
		r.setAgeScreened(ageCount);
	}
	
	public static void calculateScreenPercs(ReportDataContainer r) {
		if(r.getTotalDiabetic()>0)
			r.setDiabeticScreenedPerc(((double)r.getDiabeticScreened()/(double)r.getTotalDiabetic())*100);
		if(r.getTotalHypertensive()>0)
			r.setHypertensiveScreenedPerc(((double)r.getHypertensiveScreened()/(double)r.getTotalHypertensive())*100);
		if(r.getTotalBp()>0)
			r.setBpScreenedPerc(((double)r.getBpScreened()/(double)r.getTotalBp())*100);
		if(r.getTotalAboriginals()>0)
			r.setAboriginalScreenedPerc(((double)r.getAboriginalScreened()/(double)r.getTotalAboriginals())*100);
		if(r.getTotalFamHx()>0)
			r.setFamHxScreenedPerc(((double)r.getFamHxScreened()/(double)r.getTotalFamHx())*100);
		if(r.getTotalAge()>0)
			r.setAgeScreenedPerc(((double)r.getAgeScreened()/(double)r.getTotalAge())*100);
		
		if(r.getTotalDiabetic()>0)
			r.setDiabeticScreenedPerc1yr(((double)r.getDiabeticScreened1yr()/(double)r.getTotalDiabetic())*100);
		if(r.getTotalHypertensive()>0)
			r.setHypertensiveScreenedPerc1yr(((double)r.getHypertensiveScreened1yr()/(double)r.getTotalHypertensive())*100);
		if(r.getTotalBp()>0)
			r.setBpScreenedPerc1yr(((double)r.getBpScreened1yr()/(double)r.getTotalBp())*100);
		if(r.getTotalAboriginals()>0)
			r.setAboriginalScreenedPerc1yr(((double)r.getAboriginalScreened1yr()/(double)r.getTotalAboriginals())*100);
		if(r.getTotalFamHx()>0)
			r.setFamHxScreenedPerc1yr(((double)r.getFamHxScreened1yr()/(double)r.getTotalFamHx())*100);
		if(r.getTotalAge()>0)
			r.setAgeScreenedPerc1yr(((double)r.getAgeScreened1yr()/(double)r.getTotalAge())*100);
		
	}
	
	public static void getCKDStages(ReportDataContainer r) {
		//CKD Stages
		List<String> idents = new ArrayList<String>();
		List<MeasurementMap> maps = measurementMapDao.getMapsByLoinc("33914-3");
		for(MeasurementMap map:maps) {
			idents.add(map.getIdentCode());
		}
		
		Double val;
		List<Integer> demoIds = measurementDao.findDemographicIdsByType(idents);
		for(Integer demoId:demoIds) {
			List<Measurement> ms = measurementDao.findByType(demoId, idents);
			if(ms.size()>0) {
				Measurement m = ms.get(0);
				try {
					val = Double.valueOf(m.getDataField());
				}
				catch( NumberFormatException e ) {
					continue;
				}
				
				if(val >= 90) {
					r.setCkdStage1(r.getCkdStage1()+1);
				}
				if(val >=60 && val <= 89) {
					r.setCkdStage2(r.getCkdStage2()+1);
				}
				if(val >=30 && val <= 59) {
					r.setCkdStage3(r.getCkdStage3()+1);
				}
				if(val >=15 && val <=29) {
					r.setCkdStage4(r.getCkdStage4()+1);
				}
				if(val < 15) {
					r.setCkdStage5(r.getCkdStage5()+1);
				}
			}
		}
	}
	
	//"select distinct(tc_atc_number) from cd_therapeutic_class where tc_ahfs = 'ANGIOTENSIN-CONVERTING ENZYME INHIBITORS' or tc_ahfs = 'ANGIOTENSIN II RECEPTOR ANTAGONISTS' or tc_ahfs='RENIN INHIBITORS'"

	
	public static ReportDataContainer getPreImplementationReportData() {
 
		ReportDataContainer r = new ReportDataContainer();
		getTotals(r);
		getTotalsScreenedLastYear(r);
		getTotalsScreened(r);
		calculateScreenPercs(r);
		getCKDStages(r);

		//TODO: forms10
		List<FormLabReq07> forms07 = labReq07Dao.findCreatinine();
		r.setEgfrTestsOrdered(forms07.size());
		
		forms07 = labReq07Dao.findAcr();
		r.setAcrTestsOrdered(forms07.size());
		
		
		if(r.getEgfrTestsOrdered()>0)
			r.setEgfrTestsRatio(((double)r.getEgfrTestsReceived()/(double)r.getEgfrTestsOrdered())*100);
		
		if(r.getAcrTestsOrdered()>0)
			r.setAcrTestsRatio(((double)r.getAcrTestsReceived()/(double)r.getAcrTestsOrdered())*100);
		
		//PCR tests?
		
		r.setTotalPatients(demographicDao.getActiveDemographicIds().size());
		
		
		//////////////////////////
		
		
		//generate list of demographic ids where have a drug which is interesting to us (ACE-I, ARB, Renin Inhibitor)
		List<Drug> drugs = drugDao.findByAtc(CkdScreener.getInterestingATCs());
		List<Integer> demographicIds = new ArrayList<Integer>();
		for(Drug drug:drugs) {
			if(!demographicIds.contains(drug.getDemographicId())) {
				demographicIds.add(drug.getDemographicId());
			}
		}
		
		for(Integer demographicNo:demographicIds) {
			//diabetic
			if(dxDao.findByDemographicNoResearchCodeAndCodingSystem(demographicNo, "250", "icd9").size()>0) {
				r.setDiabetesAndDrugs(r.getDiabetesAndDrugs()+1);
			}
			
			//ckd
			
			//bp issue
			List<Measurement> dbps = measurementDao.findByType(demographicNo, "BP");
			for(Measurement dbp:dbps) {
				if(dbp.getDataField().indexOf("/")!=-1) {
					String[] parts = dbp.getDataField().split("/");
					try {
						if(Integer.parseInt(parts[0]) >= 140 || Integer.parseInt(parts[1]) >= 90) {
							r.setBpAndDrugs(r.getBpAndDrugs()+1);
						}
					}
					catch( NumberFormatException e ) {
						//ignore
					}
				}
			}
			
		}
		
		
		for(Dxresearch dx: dxDao.findActive("icd9", "250")) {
			int dn = dx.getDemographicNo();
			//get last BP
			boolean metTarget=false;
			for(Measurement m:measurementDao.findByType(dn, "BP")) {
				String bp = m.getDataField();
				if( bp.indexOf("/") != -1 ) {
					String[] parts = bp.split("/");
					try {
						if(Integer.parseInt(parts[0]) <= 130 && Integer.parseInt(parts[1]) <= 80) {
							metTarget=true;
							break;
						}
					}
					catch( NumberFormatException e ) {
						//Ignore
					}
				}
				
			}
			
			if(metTarget) {
				r.setDiabeticAndBpTarget(r.getDiabeticAndBpTarget()+1);
			}
		}
		
		//TODO:do it for CKD too
		
		
		/*
		
		//hba1c < 7% ,fasting gluc 4-7 
		idents = new ArrayList<String>();
		for(MeasurementMap mp:measurementMapDao.getMapsByLoinc("4548-4")) {
			idents.add(mp.getIdentCode());
		}
		List<Measurement> mps = measurementDao.findByType(idents);
		*/
		
		
		//Avg.
		
		return r;
	}
	
}
