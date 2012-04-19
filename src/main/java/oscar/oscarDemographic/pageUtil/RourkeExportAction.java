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


package oscar.oscarDemographic.pageUtil;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.DataExportDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.DataExport;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.form.dao.Rourke2009DAO;
import oscar.form.model.FormRourke2009;
import oscar.oscarReport.data.DemographicSets;
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;
import cdsrourke.PatientDocument;
import cdsrourke.PatientDocument.Patient;

public class RourkeExportAction extends DispatchAction {
	private ClinicDAO clinicDAO;
	private DataExportDao dataExportDAO;
	private DemographicDao demographicDao;
	private Rourke2009DAO frmRourke2009DAO;

	private Logger log = MiscUtils.getLogger();

	public Rourke2009DAO getFrmRourke2009DAO() {
	    return frmRourke2009DAO;
    }

	public void setFrmRourke2009DAO(Rourke2009DAO frmRourke2009DAO) {
	    this.frmRourke2009DAO = frmRourke2009DAO;
    }

	public void setDemographicDao(DemographicDao demographicDao) {
	    this.demographicDao = demographicDao;
    }

	public DemographicDao getDemographicDao() {
	    return demographicDao;
    }

	public DataExportDao getDataExportDAO() {
    	return dataExportDAO;
    }

	public void setDataExportDAO(DataExportDao dataExportDAO) {
    	this.dataExportDAO = dataExportDAO;
    }

	public ClinicDAO getClinicDAO() {
    	return clinicDAO;
    }

	public void setClinicDAO(ClinicDAO clinicDAO) {
    	this.clinicDAO = clinicDAO;
    }

	public ActionForward getFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		OscarProperties properties = OscarProperties.getInstance();
		String zipName = request.getParameter("zipFile");
		String dir = properties.getProperty("DOCUMENT_DIR");
		Util.downloadFile(zipName, dir, response);
		return null;

	}

	@SuppressWarnings("rawtypes")
    @Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		OscarProperties properties = OscarProperties.getInstance();
		Clinic clinic = clinicDAO.getClinic();
		List<DataExport> dataExportList = dataExportDAO.findAllByType(DataExportDao.ROURKE);

		DynaValidatorForm frm = (DynaValidatorForm)form;
		String patientSet = frm.getString("patientSet");

		if( patientSet == null || "".equals(patientSet) || patientSet.equals("-1")) {
			frm.set("orgName", clinic.getClinicName());
			frm.set("vendorId", properties.getProperty("vendorId", ""));
			frm.set("vendorBusinessName", properties.getProperty("vendorBusinessName", ""));
			frm.set("vendorCommonName", properties.getProperty("vendorCommonName", ""));
			frm.set("vendorSoftware", properties.getProperty("softwareName", ""));
			frm.set("vendorSoftwareCommonName", properties.getProperty("softwareCommonName", ""));
			frm.set("vendorSoftwareVer", properties.getProperty("version", ""));
			frm.set("installDate", properties.getProperty("buildDateTime", ""));

			if( dataExportList.size() > 0 ) {
				DataExport dataExport = dataExportList.get(dataExportList.size()-1);
				frm.set("contactLName",dataExport.getContactLName());
				frm.set("contactFName", dataExport.getContactFName());
				frm.set("contactPhone", dataExport.getContactPhone());
				frm.set("contactEmail", dataExport.getContactEmail());
				frm.set("contactUserName", dataExport.getUser());
			}
			request.setAttribute("dataExportList", dataExportList);
			return mapping.findForward("display");
		}


		//Create export files
	    String tmpDir = properties.getProperty("TMP_DIR");
	    if (!Util.checkDir(tmpDir)) {
	    	tmpDir = System.getProperty("java.io.tmpdir");
	        MiscUtils.getLogger().error("Error! Cannot write to TMP_DIR - Check oscar.properties or dir permissions. Using " + tmpDir);
	    }
	    tmpDir = Util.fixDirName(tmpDir);


	    //grab patient list from set
	    DemographicSets demoSets = new DemographicSets();
		List<String> patientList = demoSets.getDemographicSet(frm.getString("patientSet"));

		log.info("Exporting Rourke 2009 " + patientList.size() + " patients");

		//make all xml files, zip them and save to document directory
		String filename = this.make(patientList, tmpDir);

		//we got this far so save entry to db
		DataExport dataExport = new DataExport();
		dataExport.setContactLName(frm.getString("contactLName"));
		dataExport.setContactFName(frm.getString("contactFName"));
		dataExport.setContactPhone(frm.getString("contactPhone"));
		dataExport.setContactEmail(frm.getString("contactEmail"));
		dataExport.setUser(frm.getString("contactUserName"));
		dataExport.setType(frm.getString("extractType"));
		Timestamp runDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		dataExport.setDaterun(runDate);
		dataExport.setFile(filename);
		dataExportDAO.persist(dataExport);

		dataExportList.add(dataExport);
		request.setAttribute("dataExportList", dataExportList);
		log.info("Export Rourke 2009 completed");

		return mapping.findForward("display");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    private String make(List patientList, String tmpDir) throws Exception {
		 String demoNo;
		 Demographic demo;

		 PatientDocument patientDocument = PatientDocument.Factory.newInstance();
		 Patient patient;

		 for( int idx = 0; idx < patientList.size(); ++idx ) {
			 demoNo = null;
			 Object obj = patientList.get(idx);
			 if (obj instanceof String) {
				demoNo = (String)obj;
			 } else {
				ArrayList<String> l2 = (ArrayList<String>)obj;
				demoNo = l2.get(0);
			 }

			 demo = getDemographicDao().getDemographic(demoNo);
			 if( demo == null ) {
				 continue;
			 }

			 List<FormRourke2009> listRourkes = frmRourke2009DAO.findAllDistinctForms(demo.getDemographicNo());
			 Iterator<FormRourke2009> iterator = listRourkes.iterator();
			 FormRourke2009 frmRourke2009;

			 while( iterator.hasNext() ) {
				 frmRourke2009 = iterator.next();
				 patient = patientDocument.addNewPatient();
				 this.buildPatientDemographic(demo, patient);
				 this.buildRourkeForm(patient, frmRourke2009);
			 }
		 }

		 return this.makeFiles(patientDocument, tmpDir);
	}

	private void buildRourkeForm(Patient patient, FormRourke2009 frmRourke2009) {

		if( frmRourke2009.getP1_birthRemarksr1() == 1) {
			patient.setBREM(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_birthRemarksr2() == 1 ) {
			patient.setBREM(new BigInteger("1"));
		}
		else if( frmRourke2009.getP1_birthRemarksr3() == 1 ) {
			patient.setBREM(new BigInteger("2"));
		}

		if( frmRourke2009.getC_APGAR1min() != null && frmRourke2009.getC_APGAR1min() > -1 ) {
			patient.setAPGAR1(frmRourke2009.getC_APGAR1min());
		}

		if( frmRourke2009.getC_APGAR5min() != null && frmRourke2009.getC_APGAR5min() > -1 ) {
			patient.setAPGAR5(frmRourke2009.getC_APGAR5min());
		}

		//Risk factors
		if( frmRourke2009.getP12ndhandsmoke() == 1 ) {
			patient.setRFAC(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1Alcohol() == 1 ) {
			patient.setRFAC(new BigInteger("1"));
		}
		else if( frmRourke2009.getP1Drugs() == 1 ) {
			patient.setRFAC(new BigInteger("2"));
		}

		//risk factors text was combined with fam history in older rourke hence the misnamed function
		patient.setFAMH(frmRourke2009.getC_riskFactors());

		this.buildVisitdates(patient, frmRourke2009);
		this.buildMeasurements(patient, frmRourke2009);
		this.buildParentalConcerns(patient, frmRourke2009);
		this.buildBreastFeeding(patient, frmRourke2009);
		this.buildCanadaFoodGuide(patient, frmRourke2009);
		this.buildFormulaFeeding(patient, frmRourke2009);
		this.buildMilk(patient, frmRourke2009);
		this.buildStoolUrine(patient, frmRourke2009);
		this.buildBottleInBed(patient, frmRourke2009);
		this.buildCup(patient, frmRourke2009);
		this.buildNutrition(patient, frmRourke2009);
		this.buildEducationAdvice(patient, frmRourke2009);
		this.buildDevelopment(patient, frmRourke2009);
		this.buildPhysicalExam(patient, frmRourke2009);
		this.buildProblemsPlans(patient, frmRourke2009);
		this.buildImmunization(patient, frmRourke2009);
	}

	private void buildImmunization(Patient patient, FormRourke2009 frmRourke2009 ) {
		if( frmRourke2009.getP1_hepatitisVaccine1wOk() == 1 ) {
			patient.setHEPBV1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_hepatitisVaccine1wNo() == 1 ) {
			patient.setHEPBV1W(new BigInteger("1"));
		}

		if( frmRourke2009.getP1_hepatitisVaccine1mOk() == 1 ) {
			patient.setHEPBV1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_hepatitisVaccine1mNo() == 1 ) {
			patient.setHEPBV1M(new BigInteger("1"));
		}

		if( frmRourke2009.getP2_hepatitisVaccine6mOk() == 1 ) {
			patient.setHEPBV6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_hepatitisVaccine6mNo() == 1 ) {
			patient.setHEPBV6M(new BigInteger("1"));
		}

	}

	private void buildProblemsPlans(Patient patient, FormRourke2009 frmRourke2009 ) {

		patient.setPROBPLAN1W(StringUtils.noNull(frmRourke2009.getP1Problems1w()));
		patient.setPROBPLAN2W(StringUtils.noNull(frmRourke2009.getP1Problems2w()));
		patient.setPROBPLAN1M(StringUtils.noNull(frmRourke2009.getP1Problems1m()));

		patient.setPROBPLAN2M(StringUtils.noNull(frmRourke2009.getP2Problems2m()));
		patient.setPROBPLAN4M(StringUtils.noNull(frmRourke2009.getP2Problems4m()));
		patient.setPROBPLAN6M(StringUtils.noNull(frmRourke2009.getP2Problems6m()));

		patient.setPROBPLAN9M(StringUtils.noNull(frmRourke2009.getP3Problems9m()));
		patient.setPROBPLAN12M(StringUtils.noNull(frmRourke2009.getP3Problems12m()));
		patient.setPROBPLAN15M(StringUtils.noNull(frmRourke2009.getP3Problems15m()));

		patient.setPROBPLAN18M(StringUtils.noNull(frmRourke2009.getP4Problems18m()));
		patient.setPROBPLAN2T3Y(StringUtils.noNull(frmRourke2009.getP4Problems24m()));
		patient.setPROBPLAN4T5Y(StringUtils.noNull(frmRourke2009.getP4Problems48m()));


		if( frmRourke2009.getP1_pkuThyroid1wOk() == 1 ) {
			patient.setPKUT1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_pkuThyroid1wOkConcerns() == 1 ) {
			patient.setPKUT1W(new BigInteger("1"));
		}
		else {
			patient.setPKUT1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_hemoScreen1wOk() == 1 ) {
			patient.setHMGP1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_hemoScreen1wOkConcerns() == 1 ) {
			patient.setHMGP1W(new BigInteger("1"));
		}
		else {
			patient.setHMGP1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_tb6mOk() == 1 ) {
			patient.setRFTB6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_tb6mOkConcerns() == 1 ) {
			patient.setRFTB6M(new BigInteger("1"));
		}
		else {
			patient.setRFTB6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_antiHB9mOk() == 1 ) {
			patient.setAHBG9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_antiHB9mOkConcerns() == 1 ) {
			patient.setAHBG9M(new BigInteger("1"));
		}
		else {
			patient.setAHBG9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_hemoglobin9mOk() == 1 ) {
			patient.setHMGR9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_hemoglobin9mOkConcerns() == 1 ) {
			patient.setHMGR9M(new BigInteger("1"));
		}
		else {
			patient.setHMGR9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_hemoglobin12mOk() == 1 ) {
			patient.setHMGR12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_hemoglobin12mOkConcerns() == 1 ) {
			patient.setHMGR12M(new BigInteger("1"));
		}
		else {
			patient.setHMGR12M(new BigInteger("2"));
		}
	}

	private void buildPhysicalExam(Patient patient, FormRourke2009 frmRourke2009) {
		if( frmRourke2009.getP1_fontanelles1wOk() == 1 ) {
			patient.setFONT1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_fontanelles1wNo() == 1 ) {
			patient.setFONT1W(new BigInteger("1"));
		}
		else {
			patient.setFONT1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_fontanelles2wOk() == 1 ) {
			patient.setFONT2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_fontanelles2wNo() == 1 ) {
			patient.setFONT2W(new BigInteger("1"));
		}
		else {
			patient.setFONT2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_fontanelles1mOk() == 1 ) {
			patient.setFONT1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_fontanelles1mNo() == 1 ) {
			patient.setFONT1M(new BigInteger("1"));
		}
		else {
			patient.setFONT1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_fontanelles4mOk() == 1 ) {
			patient.setFONT4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_fontanelles4mOkConcerns() == 1 ) {
			patient.setFONT4M(new BigInteger("1"));
		}
		else {
			patient.setFONT4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_fontanelles6mOk() == 1 ) {
			patient.setFONT6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_fontanelles6mOkConcerns() == 1 ) {
			patient.setFONT6M(new BigInteger("1"));
		}
		else {
			patient.setFONT6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_fontanelles9mOk() == 1 ) {
			patient.setFONT9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_fontanelles9mOkConcerns() == 1 ) {
			patient.setFONT9M(new BigInteger("1"));
		}
		else {
			patient.setFONT9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_fontanelles12mOk() == 1 ) {
			patient.setFONT12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_fontanelles12mOkConcerns() == 1 ) {
			patient.setFONT12M(new BigInteger("1"));
		}
		else {
			patient.setFONT12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_fontanelles15mOk() == 1 ) {
			patient.setFONT15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_fontanelles15mOkConcerns() == 1 ) {
			patient.setFONT15M(new BigInteger("1"));
		}
		else {
			patient.setFONT15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_fontanellesClosedOk() == 1 ) {
			patient.setFONT18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_fontanellesClosedOkConcerns() == 1 ) {
			patient.setFONT18M(new BigInteger("1"));
		}
		else {
			patient.setFONT18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_skin1wOk() == 1 ) {
			patient.setSKIN1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_skin1wNo() == 1 ) {
			patient.setSKIN1W(new BigInteger("1"));
		}
		else {
			patient.setSKIN1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_skin2wOk() == 1 ) {
			patient.setSKIN2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_skin2wNo() == 1 ) {
			patient.setSKIN2W(new BigInteger("1"));
		}
		else {
			patient.setSKIN2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_eyes1wOk() == 1 ) {
			patient.setEYE1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_eyes1wNo() == 1 ) {
			patient.setEYE1W(new BigInteger("1"));
		}
		else {
			patient.setEYE1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_eyes2wOk() == 1 ) {
			patient.setEYE2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_eyes2wNo() == 1 ) {
			patient.setEYE2W(new BigInteger("1"));
		}
		else {
			patient.setEYE2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_eyes1mOk() == 1 ) {
			patient.setEYE1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_eyes1mNo() == 1 ) {
			patient.setEYE1M(new BigInteger("1"));
		}
		else {
			patient.setEYE1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_eyes2mOk() == 1 ) {
			patient.setEYE2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_eyes2mOkConcerns() == 1 ) {
			patient.setEYE1M(new BigInteger("1"));
		}
		else {
			patient.setEYE2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_eyes4mOk() == 1 ) {
			patient.setEYE4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_eyes4mOkConcerns() == 1 ) {
			patient.setEYE4M(new BigInteger("1"));
		}
		else {
			patient.setEYE4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_eyes6mOk() == 1 ) {
			patient.setEYE6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_eyes6mOkConcerns() == 1 ) {
			patient.setEYE6M(new BigInteger("1"));
		}
		else {
			patient.setEYE6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_eyes9mOk() == 1 ) {
			patient.setEYE9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_eyes9mOkConcerns() == 1 ) {
			patient.setEYE9M(new BigInteger("1"));
		}
		else {
			patient.setEYE9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_eyes12mOk() == 1 ) {
			patient.setEYE12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_eyes12mOkConcerns() == 1 ) {
			patient.setEYE12M(new BigInteger("1"));
		}
		else {
			patient.setEYE12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_eyes15mOk() == 1 ) {
			patient.setEYE15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_eyes15mOkConcerns() == 1 ) {
			patient.setEYE15M(new BigInteger("1"));
		}
		else {
			patient.setEYE15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_eyes18mOk() == 1 ) {
			patient.setEYE18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_eyes18mOkConcerns() == 1 ) {
			patient.setEYE18M(new BigInteger("1"));
		}
		else {
			patient.setEYE18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_eyes24mOk() == 1 ) {
			patient.setEYE2T3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_eyes24mOkConcerns() == 1 ) {
			patient.setEYE2T3Y(new BigInteger("1"));
		}
		else {
			patient.setEYE2T3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_eyes48mOk() == 1 ) {
			patient.setEYE4T5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_eyes48mOkConcerns() == 1 ) {
			patient.setEYE4T5Y(new BigInteger("1"));
		}
		else {
			patient.setEYE4T5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_ears1wOk() == 1 ) {
			patient.setEAR1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_ears1wNo() == 1 ) {
			patient.setEAR1W(new BigInteger("1"));
		}
		else {
			patient.setEAR1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_ears2wOk() == 1 ) {
			patient.setEAR2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_ears2wNo() == 1 ) {
			patient.setEAR2W(new BigInteger("1"));
		}
		else {
			patient.setEAR2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_heartLungs1wOk() == 1 ) {
			patient.setHRLU1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_heartLungs1wNo() == 1 ) {
			patient.setHRLU1W(new BigInteger("1"));
		}
		else {
			patient.setHRLU1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_heartLungs2wOk() == 1 ) {
			patient.setHRLU2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_heartLungs2wNo() == 1 ) {
			patient.setHRLU2W(new BigInteger("1"));
		}
		else {
			patient.setHRLU2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_umbilicus1wOk() == 1 ) {
			patient.setUMB1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_umbilicus1wNo() == 1 ) {
			patient.setUMB1W(new BigInteger("1"));
		}
		else {
			patient.setUMB1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_umbilicus2wOk() == 1 ) {
			patient.setUMB2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_umbilicus2wNo() == 1 ) {
			patient.setUMB2W(new BigInteger("1"));
		}
		else {
			patient.setUMB2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_femoralPulses1wOk() == 1 ) {
			patient.setFEMP1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_femoralPulses1wNo() == 1 ) {
			patient.setFEMP1W(new BigInteger("1"));
		}
		else {
			patient.setFEMP1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_femoralPulses2wOk() == 1 ) {
			patient.setFEMP2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_femoralPulses2wNo() == 1 ) {
			patient.setFEMP2W(new BigInteger("1"));
		}
		else {
			patient.setFEMP2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_hips1wOk() == 1 ) {
			patient.setHIPS1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_hips1wNo() == 1 ) {
			patient.setHIPS1W(new BigInteger("1"));
		}
		else {
			patient.setHIPS1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_hips2wOk() == 1 ) {
			patient.setHIPS2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_hips2wNo() == 1 ) {
			patient.setHIPS2W(new BigInteger("1"));
		}
		else {
			patient.setHIPS2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_hips1mOk() == 1 ) {
			patient.setHIPS1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_hips1mNo() == 1 ) {
			patient.setHIPS1M(new BigInteger("1"));
		}
		else {
			patient.setHIPS1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_hips2mOk() == 1 ) {
			patient.setHIPS2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_hips2mOkConcerns() == 1 ) {
			patient.setHIPS2M(new BigInteger("1"));
		}
		else {
			patient.setHIPS2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_hips4mOk() == 1 ) {
			patient.setHIPS4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_hips4mOkConcerns() == 1 ) {
			patient.setHIPS4M(new BigInteger("1"));
		}
		else {
			patient.setHIPS4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_hips6mOk() == 1 ) {
			patient.setHIPS6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_hips6mOkConcerns() == 1 ) {
			patient.setHIPS6M(new BigInteger("1"));
		}
		else {
			patient.setHIPS6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_hips9mOk() == 1 ) {
			patient.setHIPS9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_hips9mOkConcerns() == 1 ) {
			patient.setHIPS9M(new BigInteger("1"));
		}
		else {
			patient.setHIPS9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_hips12mOk() == 1 ) {
			patient.setHIPS12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_hips12mOkConcerns() == 1 ) {
			patient.setHIPS12M(new BigInteger("1"));
		}
		else {
			patient.setHIPS12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_hips15mOk() == 1 ) {
			patient.setHIPS15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_hips15mOkConcerns() == 1 ) {
			patient.setHIPS15M(new BigInteger("1"));
		}
		else {
			patient.setHIPS15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_muscleTone1wOk() == 1 ) {
			patient.setMUTO1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_muscleTone1wNo() == 1 ) {
			patient.setMUTO1W(new BigInteger("1"));
		}
		else {
			patient.setMUTO1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_muscleTone2wOk() == 1 ) {
			patient.setMUTO2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_muscleTone2wNo() == 1 ) {
			patient.setMUTO2W(new BigInteger("1"));
		}
		else {
			patient.setMUTO2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_muscleTone1mOk() == 1 ) {
			patient.setMUTO1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_muscleTone1mNo() == 1 ) {
			patient.setMUTO1M(new BigInteger("1"));
		}
		else {
			patient.setMUTO1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_muscleTone2mOk() == 1 ) {
			patient.setMUTO2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_muscleTone2mOkConcerns() == 1 ) {
			patient.setMUTO2M(new BigInteger("1"));
		}
		else {
			patient.setMUTO2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_muscleTone4mOk() == 1 ) {
			patient.setMUTO4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_muscleTone4mOkConcerns() == 1 ) {
			patient.setMUTO4M(new BigInteger("1"));
		}
		else {
			patient.setMUTO4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_muscleTone6mOk() == 1 ) {
			patient.setMUTO6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_muscleTone6mOkConcerns() == 1 ) {
			patient.setMUTO6M(new BigInteger("1"));
		}
		else {
			patient.setMUTO6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_testicles1wOk() == 1 ) {
			patient.setTEST1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_testicles1wNo() == 1 ) {
			patient.setTEST1W(new BigInteger("1"));
		}
		else {
			patient.setTEST1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_testicles2wOk() == 1 ) {
			patient.setTEST2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_testicles2wNo() == 1 ) {
			patient.setTEST2W(new BigInteger("1"));
		}
		else {
			patient.setTEST2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_maleUrinary1wOk() == 1 ) {
			patient.setMUFC1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_maleUrinary1wNo() == 1 ) {
			patient.setMUFC1W(new BigInteger("1"));
		}
		else {
			patient.setMUFC1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_maleUrinary2wOk() == 1 ) {
			patient.setMUFC2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_maleUrinary2wNo() == 1 ) {
			patient.setMUFC2W(new BigInteger("1"));
		}
		else {
			patient.setMUFC2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_corneal1mOk() == 1 ) {
			patient.setCORN1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_corneal1mNo() == 1 ) {
			patient.setCORN1M(new BigInteger("1"));
		}
		else {
			patient.setCORN1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_corneal2mOk() == 1 ) {
			patient.setCORN2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_corneal2mOkConcerns() == 1 ) {
			patient.setCORN2M(new BigInteger("1"));
		}
		else {
			patient.setCORN2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_corneal4mOk() == 1 ) {
			patient.setCORN4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_corneal4mOkConcerns() == 1 ) {
			patient.setCORN4M(new BigInteger("1"));
		}
		else {
			patient.setCORN4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_corneal6mOk() == 1 ) {
			patient.setCORN6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_corneal6mOkConcerns() == 1 ) {
			patient.setCORN6M(new BigInteger("1"));
		}
		else {
			patient.setCORN6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_corneal9mOk() == 1 ) {
			patient.setCORN9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_corneal9mOkConcerns() == 1 ) {
			patient.setCORN9M(new BigInteger("1"));
		}
		else {
			patient.setCORN9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_corneal12mOk() == 1 ) {
			patient.setCORN12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_corneal12mOkConcerns() == 1 ) {
			patient.setCORN12M(new BigInteger("1"));
		}
		else {
			patient.setCORN12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_corneal15mOk() == 1 ) {
			patient.setCORN15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_corneal15mOkConcerns() == 1 ) {
			patient.setCORN15M(new BigInteger("1"));
		}
		else {
			patient.setCORN15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_corneal18mOk() == 1 ) {
			patient.setCORN18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_corneal18mOkConcerns() == 1 ) {
			patient.setCORN18M(new BigInteger("1"));
		}
		else {
			patient.setCORN18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_corneal24mOk() == 1 ) {
			patient.setCORN2T3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_corneal24mOkConcerns() == 1 ) {
			patient.setCORN2T3Y(new BigInteger("1"));
		}
		else {
			patient.setCORN2T3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_corneal48mOk() == 1 ) {
			patient.setCORN4T5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_corneal48mOkConcerns() == 1 ) {
			patient.setCORN4T5Y(new BigInteger("1"));
		}
		else {
			patient.setCORN4T5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_hearing1mOk() == 1 ) {
			patient.setHEAR1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_heart1mNo() == 1 ) {
			patient.setHEAR1M(new BigInteger("1"));
		}
		else {
			patient.setHEAR1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_hearing2mOk() == 1 ) {
			patient.setHEAR2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_hearing2mOkConcerns() == 1 ) {
			patient.setHEAR2M(new BigInteger("1"));
		}
		else {
			patient.setHEAR2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_hearing4mOk() == 1 ) {
			patient.setHEAR4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_hearing4mOkConcerns() == 1 ) {
			patient.setHEAR4M(new BigInteger("1"));
		}
		else {
			patient.setHEAR4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_hearing6mOk() == 1 ) {
			patient.setHEAR6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_hearing6mOkConcerns() == 1 ) {
			patient.setHEAR6M(new BigInteger("1"));
		}
		else {
			patient.setHEAR6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_hearing9mOk() == 1 ) {
			patient.setHEAR9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_hearing9mOkConcerns() == 1 ) {
			patient.setHEAR9M(new BigInteger("1"));
		}
		else {
			patient.setHEAR9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_hearing12mOk() == 1 ) {
			patient.setHEAR12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_hearing12mOkConcerns() == 1 ) {
			patient.setHEAR12M(new BigInteger("1"));
		}
		else {
			patient.setHEAR12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_hearing15mOk() == 1 ) {
			patient.setHEAR15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_hearing15mOkConcerns() == 1 ) {
			patient.setHEAR15M(new BigInteger("1"));
		}
		else {
			patient.setHEAR15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_hearing18mOk() == 1 ) {
			patient.setHEAR18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_hearing18mOkConcerns() == 1 ) {
			patient.setHEAR18M(new BigInteger("1"));
		}
		else {
			patient.setHEAR18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_hearing24mOk() == 1 ) {
			patient.setHEAR2T3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_hearing24mOkConcerns() == 1 ) {
			patient.setHEAR2T3Y(new BigInteger("1"));
		}
		else {
			patient.setHEAR2T3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_hearing48mOk() == 1 ) {
			patient.setHEAR4T5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_hearing48mOkConcerns() == 1 ) {
			patient.setHEAR4T5Y(new BigInteger("1"));
		}
		else {
			patient.setHEAR4T5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_heart1mOk() == 1 ) {
			patient.setHEART1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_heart1mNo() == 1 ) {
			patient.setHEART1M(new BigInteger("1"));
		}
		else {
			patient.setHEART1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_heart2mOk() == 1 ) {
			patient.setHEART2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_heart2mOkConcerns() == 1 ) {
			patient.setHEART2M(new BigInteger("1"));
		}
		else {
			patient.setHEART2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_tonsil12mOk() == 1 ) {
			patient.setTONT12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_tonsil12mOkConcerns() == 1 ) {
			patient.setTONT12M(new BigInteger("1"));
		}
		else {
			patient.setTONT12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_tonsil15mOk() == 1 ) {
			patient.setTONT15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_tonsil15mOkConcerns() == 1 ) {
			patient.setTONT15M(new BigInteger("1"));
		}
		else {
			patient.setTONT15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_tonsil18mOk() == 1 ) {
			patient.setTONT18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_tonsil18mOkConcerns() == 1 ) {
			patient.setTONT18M(new BigInteger("1"));
		}
		else {
			patient.setTONT18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_tonsil24mOk() == 1 ) {
			patient.setTONT2T3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_tonsil24mOkConcerns() == 1 ) {
			patient.setTONT2T3Y(new BigInteger("1"));
		}
		else {
			patient.setTONT2T3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_tonsil48mOk() == 1 ) {
			patient.setTONT4T5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_tonsil48mOkConcerns() == 1 ) {
			patient.setTONT4T5Y(new BigInteger("1"));
		}
		else {
			patient.setTONT4T5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_bloodpressure24mOk() == 1 ) {
			patient.setBP2T3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_bloodpressure24mOkConcerns() == 1 ) {
			patient.setBP2T3Y(new BigInteger("1"));
		}
		else {
			patient.setBP2T3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_bloodpressure48mOk() == 1 ) {
			patient.setBP4T5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_bloodpressure48mOkConcerns() == 1 ) {
			patient.setBP4T5Y(new BigInteger("1"));
		}
		else {
			patient.setBP4T5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_pkuThyroid1wOk() == 1 ) {
			patient.setPKUT1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_pkuThyroid1wOkConcerns() == 1 ) {
			patient.setPKUT1W(new BigInteger("1"));
		}
		else {
			patient.setPKUT1W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_skin1mOk() == 1 ) {
			patient.setSKIN1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_skin1mNo() == 1 ) {
			patient.setSKIN1M(new BigInteger("1"));
		}
		else {
			patient.setSKIN1M(new BigInteger("2"));
		}
	}

	private void buildDevelopment(Patient patient, FormRourke2009 frmRourke2009 ) {
		if( frmRourke2009.getP1_focusGaze1mOk() == 1 ) {
			patient.setFCGZ1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_focusGaze1mNo() == 1 ) {
			patient.setFCGZ1M(new BigInteger("1"));
		}
		else {
			patient.setFCGZ1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_startles1mOk() == 1 ) {
			patient.setSLSN1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_startles1mNo() == 1 ) {
			patient.setSLSN1M(new BigInteger("1"));
		}
		else {
			patient.setSLSN1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_sucks2wOk() == 1 ) {
			patient.setSUCW2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_sucks2wNo() == 1 ) {
			patient.setSUCW2W(new BigInteger("1"));
		}
		else {
			patient.setSUCW2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_sucks1mOk() == 1 ) {
			patient.setSUCW1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_sucks1mNo() == 1 ) {
			patient.setSUCW1M(new BigInteger("1"));
		}
		else {
			patient.setSUCW1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_noParentsConcerns2wOk() == 1 ) {
			patient.setNPC2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_noParentsConcerns2wNo() == 1 ) {
			patient.setNPC2W(new BigInteger("1"));
		}
		else {
			patient.setNPC2W(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_noParentsConcerns1mOk() == 1 ) {
			patient.setNPC1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_noParentsConcerns1mNo() == 1 ) {
			patient.setNPC1M(new BigInteger("1"));
		}
		else {
			patient.setNPC1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_noParentsConcerns2mOk() == 1 ) {
			patient.setNPC2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_noParentsConcerns2mOkConcerns() == 1 ) {
			patient.setNPC2M(new BigInteger("1"));
		}
		else {
			patient.setNPC2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_noParentsConcerns4mOk() == 1 ) {
			patient.setNPC4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_noParentsConcerns4mOkConcerns() == 1 ) {
			patient.setNPC4M(new BigInteger("1"));
		}
		else {
			patient.setNPC4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_noParentsConcerns6mOk() == 1 ) {
			patient.setNPC6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_noParentsConcerns6mOkConcerns() == 1 ) {
			patient.setNPC6M(new BigInteger("1"));
		}
		else {
			patient.setNPC6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_noParentsConcerns9mOk() == 1 ) {
			patient.setNPC9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_noParentsConcerns9mOkConcerns() == 1 ) {
			patient.setNPC9M(new BigInteger("1"));
		}
		else {
			patient.setNPC9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_noParentsConcerns12mOk() == 1 ) {
			patient.setNPC12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_noParentsConcerns12mOkConcerns() == 1 ) {
			patient.setNPC12M(new BigInteger("1"));
		}
		else {
			patient.setNPC12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_noParentsConcerns15mOk() == 1 ) {
			patient.setNPC15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_noParentsConcerns15mOkConcerns() == 1 ) {
			patient.setNPC15M(new BigInteger("1"));
		}
		else {
			patient.setNPC15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_noParentsConcerns18mOk() == 1 ) {
			patient.setNPC18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_noParentsConcerns18mOkConcerns() == 1 ) {
			patient.setNPC18M(new BigInteger("1"));
		}
		else {
			patient.setNPC18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_noParentsConcerns24mOk() == 1 ) {
			patient.setNPC2Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_noParentsConcerns24mOkConcerns() == 1 ) {
			patient.setNPC2Y(new BigInteger("1"));
		}
		else {
			patient.setNPC2Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_noParentsConcerns36mOk() == 1 ) {
			patient.setNPC3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_noParentsConcerns36mOkConcerns() == 1 ) {
			patient.setNPC3Y(new BigInteger("1"));
		}
		else {
			patient.setNPC3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_noParentsConcerns48mOk() == 1 ) {
			patient.setNPC4Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_noParentsConcerns48mOkConcerns() == 1 ) {
			patient.setNPC4Y(new BigInteger("1"));
		}
		else {
			patient.setNPC4Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_noParentsConcerns60mOk() == 1 ) {
			patient.setNPC5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_noParentsConcerns60mOkConcerns() == 1 ) {
			patient.setNPC5Y(new BigInteger("1"));
		}
		else {
			patient.setNPC5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_eyes2mOk() == 1 ) {
			patient.setFMVE2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_eyes2mOkConcerns() == 1 ) {
			patient.setFMVE2M(new BigInteger("1"));
		}
		else {
			patient.setFMVE2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_coosOk() == 1 ) {
			patient.setVASC2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_coosOkConcerns() == 1 ) {
			patient.setVASC2M(new BigInteger("1"));
		}
		else {
			patient.setVASC2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_headUpTummyOk() == 1 ) {
			patient.setHHU2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_headUpTummyOkConcerns() == 1 ) {
			patient.setHHU2M(new BigInteger("1"));
		}
		else {
			patient.setHHU2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_cuddledOk() == 1 ) {
			patient.setETC2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_cuddledOkConcerns() == 1 ) {
			patient.setETC2M(new BigInteger("1"));
		}
		else {
			patient.setETC2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_smilesOk() == 1 ) {
			patient.setSMRES2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_smilesOkConcerns() == 1 ) {
			patient.setSMRES2M(new BigInteger("1"));
		}
		else {
			patient.setSMRES2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_respondsOk() == 1 ) {
			patient.setTHTS4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_respondsOkConcerns() == 1 ) {
			patient.setTHTS4M(new BigInteger("1"));
		}
		else {
			patient.setTHTS4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_laughsOk() == 1 ) {
			patient.setLSAP4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_laughsOkConcerns() == 1 ) {
			patient.setLSAP4M(new BigInteger("1"));
		}
		else {
			patient.setLSAP4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_headSteadyOk() == 1 ) {
			patient.setHSTD4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_headSteadyOkConcerns() == 1 ) {
			patient.setHSTD4M(new BigInteger("1"));
		}
		else {
			patient.setHSTD4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_holdsObjOk() == 1 ) {
			patient.setGRRE4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_holdsObjOkConcerns() == 1 ) {
			patient.setGRRE4M(new BigInteger("1"));
		}
		else {
			patient.setGRRE4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_movingObjOk() == 1 ) {
			patient.setFMVO6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_movingObjOkConcerns() == 1 ) {
			patient.setFMVO6M(new BigInteger("1"));
		}
		else {
			patient.setFMVO6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_turnsHeadOk() == 1 ) {
			patient.setLDNS6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_turnsHeadOkConcerns() == 1 ) {
			patient.setLDNS6M(new BigInteger("1"));
		}
		else {
			patient.setLDNS6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_makesSoundOk() == 1 ) {
			patient.setBBLS6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_makesSoundOkConcerns() == 1 ) {
			patient.setBBLS6M(new BigInteger("1"));
		}
		else {
			patient.setBBLS6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_soundsOk() == 1 ) {
			patient.setBBLS9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_soundsOkConcerns() == 1 ) {
			patient.setBBLS9M(new BigInteger("1"));
		}
		else {
			patient.setBBLS9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_rollsOk() == 1 ) {
			patient.setROLL6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_rollsOkConcerns() == 1 ) {
			patient.setROLL6M(new BigInteger("1"));
		}
		else {
			patient.setROLL6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_sitsOk() == 1 ) {
			patient.setSITWS6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_sitsOkConcerns() == 1 ) {
			patient.setSITWS6M(new BigInteger("1"));
		}
		else {
			patient.setSITWS6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_reachesGraspsOk() == 1 ) {
			patient.setHTTM6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_reachesGraspsOkConcerns() == 1 ) {
			patient.setHTTM6M(new BigInteger("1"));
		}
		else {
			patient.setHTTM6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_hiddenToyOk() == 1 ) {
			patient.setLKHT9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_hiddenToyOkConcerns() == 1 ) {
			patient.setLKHT9M(new BigInteger("1"));
		}
		else {
			patient.setLKHT9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_makeSoundsOk() == 1 ) {
			patient.setSNAT9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_makeSoundsOkConcerns() == 1 ) {
			patient.setSNAT9M(new BigInteger("1"));
		}
		else {
			patient.setSNAT9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_sitsOk() == 1 ) {
			patient.setSIWS9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_sitsOkConcerns() == 1 ) {
			patient.setSIWS9M(new BigInteger("1"));
		}
		else {
			patient.setSIWS9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_standsOk() == 1 ) {
			patient.setSTWS9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_standsOkConcerns() == 1 ) {
			patient.setSTWS9M(new BigInteger("1"));
		}
		else {
			patient.setSTWS9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_thumbOk() == 1 ) {
			patient.setOPTI9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_thumbOkConcerns() == 1 ) {
			patient.setOPTI9M(new BigInteger("1"));
		}
		else {
			patient.setOPTI9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_attention9mOk() == 1 ) {
			patient.setRPUH9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_attention9mOkConcerns() == 1 ) {
			patient.setRPUH9M(new BigInteger("1"));
		}
		else {
			patient.setRPUH9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_respondsOk() == 1 ) {
			patient.setRSON12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_respondsOkConcerns() == 1 ) {
			patient.setRSON12M(new BigInteger("1"));
		}
		else {
			patient.setRSON12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_simpleRequestsOk() == 1 ) {
			patient.setUSRQ12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_simpleRequestsOkConcerns() == 1 ) {
			patient.setUSRQ12M(new BigInteger("1"));
		}
		else {
			patient.setUSRQ12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_says3wordsOk() == 1 ) {
			patient.setCHAT12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_says3wordsOkConcerns() == 1 ) {
			patient.setCHAT12M(new BigInteger("1"));
		}
		else {
			patient.setCHAT12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_shufflesOk() == 1 ) {
			patient.setCRBU12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_shufflesOkConcerns() == 1 ) {
			patient.setCRBU12M(new BigInteger("1"));
		}
		else {
			patient.setCRBU12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_pull2standOk() == 1 ) {
			patient.setPUSW12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_pull2standOkConcerns() == 1 ) {
			patient.setPUSW12M(new BigInteger("1"));
		}
		else {
			patient.setPUSW12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_showDistressOk() == 1 ) {
			patient.setSHOEM12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_showDistressOkConcerns() == 1 ) {
			patient.setSHOEM12M(new BigInteger("1"));
		}
		else {
			patient.setSHOEM12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_says5wordsOk() == 1 ) {
			patient.setTMW15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_says5wordsOkConcerns() == 1 ) {
			patient.setTMW15M(new BigInteger("1"));
		}
		else {
			patient.setTMW15M(new BigInteger("2"));
		}

		//TRIES TO GET SOMETHING BY MAKING SOUNDS WHILE REACHING OR POINTING Not on Rourke
		patient.setGSMS15M(new BigInteger("2"));


		if( frmRourke2009.getP3_fingerFoodsOk() == 1 ) {
			patient.setEFNG15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_fingerFoodsOkConcerns() == 1 ) {
			patient.setEFNG15M(new BigInteger("1"));
		}
		else {
			patient.setEFNG15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_crawlsStairsOk() == 1 ) {
			patient.setCRST15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_crawlsStairsOkConcerns() == 1 ) {
			patient.setCRST15M(new BigInteger("1"));
		}
		else {
			patient.setCRST15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_squatsOk() == 1 ) {
			patient.setSQTY15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_squatsOkConcerns() == 1 ) {
			patient.setSQTY15M(new BigInteger("1"));
		}
		else {
			patient.setSQTY15M(new BigInteger("2"));
		}

		//REMOVES SOCKS AND TRIES TO UNTIE SHOE not on rourke at 15m; set to default
		patient.setRESOK15M(new BigInteger("2"));

		//stacks 2 blocks not on rourke; set it to default
		patient.setSTAC2B15M(new BigInteger("2"));

		//LOOKS AT YOU TO SEE HOW TO REACT not on rourke; set to default
		patient.setLORX15M(new BigInteger("2"));

		if( frmRourke2009.getP4_manageableOk() == 1 ) {
			patient.setMGCB18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_manageableOkConcerns() == 1 ) {
			patient.setMGCB18M(new BigInteger("1"));
		}
		else {
			patient.setMGCB18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_soothabilityOk() == 1 ) {
			patient.setEASO18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_soothabilityOkConcerns() == 1 ) {
			patient.setEASO18M(new BigInteger("1"));
		}
		else {
			patient.setEASO18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_comfortOk() == 1 ) {
			patient.setCOCO18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_comfortOkConcerns() == 1 ) {
			patient.setCOCO18M(new BigInteger("1"));
		}
		else {
			patient.setCOCO18M(new BigInteger("2"));
		}

		//Points to 3 body parts Not on Rourke set to default
		patient.setP3BP18M(new BigInteger("2"));

		if( frmRourke2009.getP4_getAttnOk() == 1 ) {
			patient.setGEAX18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_getAttnOkConcerns() == 1 ) {
			patient.setGEAX18M(new BigInteger("1"));
		}
		else {
			patient.setGEAX18M(new BigInteger("2"));
		}

		//PRETEND PLAY WITH TOYS AND FIGURES Not on Rourke
		patient.setPRPL18M(new BigInteger("2"));

		if( frmRourke2009.getP4_recsNameOk() == 1 ) {
			patient.setTWNC18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_recsNameOkConcerns() == 1 ) {
			patient.setTWNC18M(new BigInteger("1"));
		}
		else {
			patient.setTWNC18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_initSpeechOk() == 1 ) {
			patient.setIMI18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_initSpeechOkConcerns() == 1 ) {
			patient.setIMI18M(new BigInteger("1"));
		}
		else {
			patient.setIMI18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_4consonantsOk() == 1 ) {
			patient.setTCON18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_4consonantsOkConcerns() == 1 ) {
			patient.setTCON18M(new BigInteger("1"));
		}
		else {
			patient.setTCON18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_walksbackOk() == 1 ) {
			patient.setWABA18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_walksbackOkConcerns() == 1 ) {
			patient.setWABA18M(new BigInteger("1"));
		}
		else {
			patient.setWABA18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_feedsSelfOk() == 1 ) {
			patient.setFSWS18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_feedsSelfOkConcerns() == 1 ) {
			patient.setFSWS18M(new BigInteger("1"));
		}
		else {
			patient.setFSWS18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_removesHatOk() == 1 ) {
			patient.setRHSWH18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_removesHatOkConcerns() == 1 ) {
			patient.setRHSWH18M(new BigInteger("1"));
		}
		else {
			patient.setRHSWH18M(new BigInteger("2"));
		}

		//AT LEAST 1 NEW WORD/WEEK not on rourke; set to default
		patient.setONWW2Y(new BigInteger("2"));

		if( frmRourke2009.getP4_2wSentenceOk() == 1 ) {
			patient.setTWS2Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_2wSentenceOkConcerns() == 1 ) {
			patient.setTWS2Y(new BigInteger("1"));
		}
		else {
			patient.setTWS2Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_runsOk() == 1 ) {
			patient.setTRR2Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_runsOkConcerns() == 1 ) {
			patient.setTRR2Y(new BigInteger("1"));
		}
		else {
			patient.setTRR2Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_smallContainerOk() == 1 ) {
			patient.setPOSC2Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_smallContainerOkConcerns() == 1 ) {
			patient.setPOSC2Y(new BigInteger("1"));
		}
		else {
			patient.setPOSC2Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_smallContainerOk() == 1 ) {
			patient.setPOSC2Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_smallContainerOkConcerns() == 1 ) {
			patient.setPOSC2Y(new BigInteger("1"));
		}
		else {
			patient.setPOSC2Y(new BigInteger("2"));
		}

		//COPIES ADULT'S ACTIONS not on rourke; set to default
		patient.setCPAA2Y(new BigInteger("2"));

		if( frmRourke2009.getP4_newSkillsOk() == 1 ) {
			patient.setDVNS2Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_newSkillsOkConcerns() == 1 ) {
			patient.setDVNS2Y(new BigInteger("1"));
		}
		else {
			patient.setDVNS2Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_one2stepdirectionsOk() == 1 ) {
			patient.setU2SD3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_one2stepdirectionsOkConcerns() == 1 ) {
			patient.setU2SD3Y(new BigInteger("1"));
		}
		else {
			patient.setU2SD3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_twistslidsOk() == 1 ) {
			patient.setTWLJ3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_twistslidsOkConcerns() == 1 ) {
			patient.setTWLJ3Y(new BigInteger("1"));
		}
		else {
			patient.setTWLJ3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_turnsPagesOk() == 1 ) {
			patient.setTUPG3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_turnsPagesOkConcerns() == 1 ) {
			patient.setTUPG3Y(new BigInteger("1"));
		}
		else {
			patient.setTUPG3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_sharesSometimeOk() == 1 ) {
			patient.setSHARS3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_sharesSometimeOkConcerns() == 1 ) {
			patient.setSHARS3Y(new BigInteger("1"));
		}
		else {
			patient.setSHARS3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_listenMusikOk() == 1 ) {
			patient.setLIMUS3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_listenMusikOkConcerns() == 1 ) {
			patient.setLIMUS3Y(new BigInteger("1"));
		}
		else {
			patient.setLIMUS3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_3directionsOk() == 1 ) {
			patient.setU3SD4Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_3directionsOkConcerns() == 1 ) {
			patient.setU3SD4Y(new BigInteger("1"));
		}
		else {
			patient.setU3SD4Y(new BigInteger("2"));
		}

		//ASKS A LOT OF QUESTIONS Duplicate with ALQ4Y
		if( frmRourke2009.getP4_asksQuestionsOk() == 1 ) {
			patient.setASKQ4Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_asksQuestionsOkConcerns() == 1 ) {
			patient.setASKQ4Y(new BigInteger("1"));
		}
		else {
			patient.setASKQ4Y(new BigInteger("2"));
		}

		//STANDS ON 1 FOOT FOR 1 - 3 SEC Not on Rourke
		patient.setSF1T3S4Y(new BigInteger("2"));

		//DRAWS A PERSON WITH AT LEAST 3 BODY PARTS not on rourke; set to default
		patient.setDP3P4Y(new BigInteger("2"));

		//TOILET TRAINED DURING THE DAY not on rourke; set to default
		patient.setTOILD4Y(new BigInteger("2"));

		if( frmRourke2009.getP4_tries2comfortOk() == 1 ) {
			patient.setCOMUS4Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_tries2comfortOkConcerns() == 1 ) {
			patient.setCOMUS4Y(new BigInteger("1"));
		}
		else {
			patient.setCOMUS4Y(new BigInteger("2"));
		}

		//COUNTS TO 10 AND KNOWS COMMON COLOURS AND SHAPES Not on Rourke
		patient.setCOTEN5Y(new BigInteger("2"));

		if( frmRourke2009.getP4_speaksClearlyOk() == 1 ) {
			patient.setSPCLS5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_speaksClearlyOkConcerns() == 1 ) {
			patient.setSPCLS5Y(new BigInteger("1"));
		}
		else {
			patient.setSPCLS5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_throwsCatchesOk() == 1 ) {
			patient.setTCBAL5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_throwsCatchesOkConcerns() == 1 ) {
			patient.setTCBAL5Y(new BigInteger("1"));
		}
		else {
			patient.setTCBAL5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_hops1footOk() == 1 ) {
			patient.setHOPS5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_hops1footOkConcerns() == 1 ) {
			patient.setHOPS5Y(new BigInteger("1"));
		}
		else {
			patient.setHOPS5Y(new BigInteger("2"));
		}

		//SHARES WILLINGLY not on rourke for 5 years; set to default
		patient.setSHARW5Y(new BigInteger("2"));

		//WORKS ALONE AT AN ACTIVITY FOR 20 - 30 MIN not on rourke for 5 years; set to default
		patient.setWALON5Y(new BigInteger("2"));

		if( frmRourke2009.getP4_separatesOk() == 1 ) {
			patient.setSEPEA5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_separatesOkConcerns() == 1 ) {
			patient.setSEPEA5Y(new BigInteger("1"));
		}
		else {
			patient.setSEPEA5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_calms1mOk() == 1 ) {
			patient.setCWC1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_calms1mNo() == 1 ) {
			patient.setCWC1M(new BigInteger("1"));
		}
		else {
			patient.setCWC1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_coosOk() == 1 ) {
			patient.setCOOS2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_coosOkConcerns() == 1 ) {
			patient.setCOOS2M(new BigInteger("1"));
		}
		else {
			patient.setCOOS2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_headUpTummyOk() == 1 ) {
			patient.setLHLT2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_headUpTummyOkConcerns() == 1 ) {
			patient.setLHLT2M(new BigInteger("1"));
		}
		else {
			patient.setLHLT2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_cuddledOk() == 1 ) {
			patient.setCBC2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_cuddledOkConcerns() == 1 ) {
			patient.setCBC2M(new BigInteger("1"));
		}
		else {
			patient.setCBC2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_2sucksOk() == 1 ) {
			patient.setS2S2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_2sucksOkConcerns() == 1 ) {
			patient.setS2S2M(new BigInteger("1"));
		}
		else {
			patient.setS2S2M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_movingObjOk() == 1 ) {
			patient.setFMT4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_movingObjOkConcerns() == 1 ) {
			patient.setFMT4M(new BigInteger("1"));
		}
		else {
			patient.setFMT4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_respondsOk() == 1 ) {
			patient.setRTPE4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_respondsOkConcerns() == 1 ) {
			patient.setRTPE4M(new BigInteger("1"));
		}
		else {
			patient.setRTPE4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_holdsObjOk() == 1 ) {
			patient.setHOB4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_holdsObjOkConcerns() == 1 ) {
			patient.setHOB4M(new BigInteger("1"));
		}
		else {
			patient.setHOB4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_laughsOk() == 1 ) {
			patient.setLSR4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_laughsOkConcerns() == 1 ) {
			patient.setLSR4M(new BigInteger("1"));
		}
		else {
			patient.setLSR4M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_turnsHeadOk() == 1 ) {
			patient.setTHTS6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_turnsHeadOkConcerns() == 1 ) {
			patient.setTHTS6M(new BigInteger("1"));
		}
		else {
			patient.setTHTS6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_makesSoundOk() == 1 ) {
			patient.setMSWT6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_makesSoundOkConcerns() == 1 ) {
			patient.setMSWT6M(new BigInteger("1"));
		}
		else {
			patient.setMSWT6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_vocalizesOk() == 1 ) {
			patient.setVPD6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_vocalizesOkConcerns() == 1 ) {
			patient.setVPD6M(new BigInteger("1"));
		}
		else {
			patient.setVPD6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_rollsOk() == 1 ) {
			patient.setRBS6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_rollsOkConcerns() == 1 ) {
			patient.setRBS6M(new BigInteger("1"));
		}
		else {
			patient.setRBS6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_reachesGraspsOk() == 1 ) {
			patient.setRGO6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_reachesGraspsOkConcerns() == 1 ) {
			patient.setRGO6M(new BigInteger("1"));
		}
		else {
			patient.setRGO6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_responds2peopleOk() == 1 ) {
			patient.setRDTDP9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_responds2peopleOkConcerns() == 1 ) {
			patient.setRDTDP9M(new BigInteger("1"));
		}
		else {
			patient.setRDTDP9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_playGamesOk() == 1 ) {
			patient.setPSG9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_playGamesOkConcerns() == 1 ) {
			patient.setPSG9M(new BigInteger("1"));
		}
		else {
			patient.setPSG9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_attention9mOk() == 1 ) {
			patient.setCSA9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_attention9mOkConcerns() == 1 ) {
			patient.setCSA9M(new BigInteger("1"));
		}
		else {
			patient.setCSA9M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_consonantOk() == 1 ) {
			patient.setM1CVC12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_consonantOkConcerns() == 1 ) {
			patient.setM1CVC12M(new BigInteger("1"));
		}
		else {
			patient.setM1CVC12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_showDistressOk() == 1 ) {
			patient.setSDWS12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_showDistressOkConcerns() == 1 ) {
			patient.setSDWS12M(new BigInteger("1"));
		}
		else {
			patient.setSDWS12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_followGazeOk() == 1 ) {
			patient.setFGJR12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_followGazeOkConcerns() == 1 ) {
			patient.setFGJR12M(new BigInteger("1"));
		}
		else {
			patient.setFGJR12M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_says5wordsOk() == 1 ) {
			patient.setS5W15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_says5wordsOkConcerns() == 1 ) {
			patient.setS5W15M(new BigInteger("1"));
		}
		else {
			patient.setS5W15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_showsFearStrangersOk() == 1 ) {
			patient.setSFSP15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_showsFearStrangersOkConcerns() == 1 ) {
			patient.setSFSP15M(new BigInteger("1"));
		}
		else {
			patient.setSFSP15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_otherChildrenOk() == 1 ) {
			patient.setIIOC18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_otherChildrenOkConcerns() == 1 ) {
			patient.setIIOC18M(new BigInteger("1"));
		}
		else {
			patient.setIIOC18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_pointsOk() == 1 ) {
			patient.setPSDBP18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_pointsOkConcerns() == 1 ) {
			patient.setPSDBP18M(new BigInteger("1"));
		}
		else {
			patient.setPSDBP18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_points2wantOk() == 1 ) {
			patient.setPWW18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_points2wantOkConcerns() == 1 ) {
			patient.setPWW18M(new BigInteger("1"));
		}
		else {
			patient.setPWW18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_looks4toyOk() == 1 ) {
			patient.setLFTWA18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_looks4toyOkConcerns() == 1 ) {
			patient.setLFTWA18M(new BigInteger("1"));
		}
		else {
			patient.setLFTWA18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_says20wordsOk() == 1 ) {
			patient.setS20W18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_says20wordsOkConcerns() == 1 ) {
			patient.setS20W18M(new BigInteger("1"));
		}
		else {
			patient.setS20W18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_4consonantsOk() == 1 ) {
			patient.setP4C18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_4consonantsOkConcerns() == 1 ) {
			patient.setP4C18M(new BigInteger("1"));
		}
		else {
			patient.setP4C18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_walksbackOk() == 1 ) {
			patient.setWALK18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_walksbackOkConcerns() == 1 ) {
			patient.setWALK18M(new BigInteger("1"));
		}
		else {
			patient.setWALK18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_one2stepdirectionsOk() == 1 ) {
			patient.setU2SD2Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_one2stepdirectionsOkConcerns() == 1 ) {
			patient.setU2SD2Y(new BigInteger("1"));
		}
		else {
			patient.setU2SD2Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_walksbackwardOk() == 1 ) {
			patient.setWABA2Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_walksbackwardOkConcerns() == 1 ) {
			patient.setWABA2Y(new BigInteger("1"));
		}
		else {
			patient.setWABA2Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_pretendsPlayOk() == 1 ) {
			patient.setPRPL2Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_pretendsPlayOkConcerns() == 1 ) {
			patient.setPRPL2Y(new BigInteger("1"));
		}
		else {
			patient.setPRPL2Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_2directionsOk() == 1 ) {
			patient.setU3SD3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_2directionsOkConcerns() == 1 ) {
			patient.setU3SD3Y(new BigInteger("1"));
		}
		else {
			patient.setU3SD3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_5ormoreWordsOk() == 1 ) {
			patient.setS5WS3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_5ormoreWordsOkConcerns() == 1 ) {
			patient.setS5WS3Y(new BigInteger("1"));
		}
		else {
			patient.setS5WS3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_walksUpStairsOk() == 1 ) {
			patient.setWUS3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_walksUpStairsOkConcerns() == 1 ) {
			patient.setWUS3Y(new BigInteger("1"));
		}
		else {
			patient.setWUS3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_playMakeBelieveOk() == 1 ) {
			patient.setPMBG3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_playMakeBelieveOkConcerns() == 1 ) {
			patient.setPMBG3Y(new BigInteger("1"));
		}
		else {
			patient.setPMBG3Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_asksQuestionsOk() == 1 ) {
			patient.setALQ4Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_asksQuestionsOkConcerns() == 1 ) {
			patient.setALQ4Y(new BigInteger("1"));
		}
		else {
			patient.setALQ4Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_upDownStairsOk() == 1 ) {
			patient.setWSAF4Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_upDownStairsOkConcerns() == 1 ) {
			patient.setWSAF4Y(new BigInteger("1"));
		}
		else {
			patient.setWSAF4Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_undoesZippersOk() == 1 ) {
			patient.setBUTZIP4Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_undoesZippersOkConcerns() == 1 ) {
			patient.setBUTZIP4Y(new BigInteger("1"));
		}
		else {
			patient.setBUTZIP4Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_countsOutloudOk() == 1 ) {
			patient.setCOUNT5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_countsOutloudOkConcerns() == 1 ) {
			patient.setCOUNT5Y(new BigInteger("1"));
		}
		else {
			patient.setCOUNT5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_dressesUndressesOk() == 1 ) {
			patient.setDRESS5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_dressesUndressesOkConcerns() == 1 ) {
			patient.setDRESS5Y(new BigInteger("1"));
		}
		else {
			patient.setDRESS5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_obeysAdultOk() == 1 ) {
			patient.setCOOP5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_obeysAdultOkConcerns() == 1 ) {
			patient.setCOOP5Y(new BigInteger("1"));
		}
		else {
			patient.setCOOP5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_retellsStoryOk() == 1 ) {
			patient.setRETELL5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_retellsStoryOkConcerns() == 1 ) {
			patient.setRETELL5Y(new BigInteger("1"));
		}
		else {
			patient.setRETELL5Y(new BigInteger("2"));
		}

	}

	private void buildEducationAdvice(Patient patient, FormRourke2009 frmRourke2009) {
		this.buildInjuryPrevention(patient, frmRourke2009);
		this.buildOtherIssues(patient, frmRourke2009);
	}

	private void buildOtherIssues(Patient patient, FormRourke2009 frmRourke2009) {
		if( frmRourke2009.getP1_sleepCryOk() == 1 ) {
			patient.setSCRY1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_sleepCryOkConcerns() == 1 ) {
			patient.setSCRY1W1M(new BigInteger("1"));
		}
		else {
			patient.setSCRY1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_sleepCryOk() == 1 ) {
			patient.setSCRY1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_sleepCryOkConcerns() == 1 ) {
			patient.setSCRY1W1M(new BigInteger("1"));
		}
		else {
			patient.setSCRY1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_sleepCryOk() == 1 ) {
			patient.setSCNW2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_sleepCryOkConcerns() == 1 ) {
			patient.setSCNW2M6M(new BigInteger("1"));
		}
		else {
			patient.setSCNW2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_sleepCryOk() == 1 ) {
			patient.setSCNW9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_sleepCryOkConcerns() == 1 ) {
			patient.setSCNW9M15M(new BigInteger("1"));
		}
		else {
			patient.setSCNW9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_highRisk18mOk() == 1 ) {
			patient.setHRKC18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_highRisk18mOkConcerns() == 1 ) {
			patient.setHRKC18M(new BigInteger("1"));
		}
		else {
			patient.setHRKC18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_highRisk24mOk() == 1 ) {
			patient.setHRKC2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_highRisk24mOkConcerns() == 1 ) {
			patient.setHRKC2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setHRKC2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_highRisk24mOk() == 1 ) {
			patient.setHRKC2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_highRisk24mOkConcerns() == 1 ) {
			patient.setHRKC2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setHRKC2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_soothabilityOk() == 1 ) {
			patient.setSORE1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_soothabilityOkConcerns() == 1 ) {
			patient.setSORE1W1M(new BigInteger("1"));
		}
		else {
			patient.setSORE1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_soothabilityOk() == 1 ) {
			patient.setSORE2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_soothabilityOkConcerns() == 1 ) {
			patient.setSORE2M6M(new BigInteger("1"));
		}
		else {
			patient.setSORE2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_soothabilityOk() == 1 ) {
			patient.setSORE9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_soothabilityOkConcerns() == 1 ) {
			patient.setSORE9M15M(new BigInteger("1"));
		}
		else {
			patient.setSORE9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_discipline18mOk() == 1 ) {
			patient.setDISP18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_discipline18mOkConcerns() == 1 ) {
			patient.setDISP18M(new BigInteger("1"));
		}
		else {
			patient.setDISP18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_discipline24mOk() == 1 ) {
			patient.setDISP2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_discipline24mOkConcerns() == 1 ) {
			patient.setDISP2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setDISP2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_homeVisitOk() == 1 ) {
			patient.setHMNV1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_homeVisitOkConcerns() == 1 ) {
			patient.setHMNV1W1M(new BigInteger("1"));
		}
		else {
			patient.setHMNV1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_homeVisitOk() == 1 ) {
			patient.setHMNV2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_homeVisitOkConcerns() == 1 ) {
			patient.setHMNV2M6M(new BigInteger("1"));
		}
		else {
			patient.setHMNV2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_homeVisitOk() == 1 ) {
			patient.setHMNV9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_homeVisitOkConcerns() == 1 ) {
			patient.setHMNV9M15M(new BigInteger("1"));
		}
		else {
			patient.setHMNV9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_bondingOk() == 1 ) {
			patient.setPABO1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_bondingOkConcerns() == 1 ) {
			patient.setPABO1W1M(new BigInteger("1"));
		}
		else {
			patient.setPABO1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_bondingOk() == 1 ) {
			patient.setPABO2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_bondingOkConcerns() == 1 ) {
			patient.setPABO2M6M(new BigInteger("1"));
		}
		else {
			patient.setPABO2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_parentingOk() == 1 ) {
			patient.setPABO9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_parentingOkConcerns() == 1 ) {
			patient.setPABO9M15M(new BigInteger("1"));
		}
		else {
			patient.setPABO9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_parentChild18mOk() == 1 ) {
			patient.setPCI18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_parentChild18mOkConcerns() == 1 ) {
			patient.setPCI18M(new BigInteger("1"));
		}
		else {
			patient.setPCI18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_parentChild24mOk() == 1 ) {
			patient.setPCI2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_parentChild24mOkConcerns() == 1 ) {
			patient.setPCI2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setPCI2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_pFatigueOk() == 1 ) {
			patient.setPFPD1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_pFatigueOkConcerns() == 1 ) {
			patient.setPFPD1W1M(new BigInteger("1"));
		}
		else {
			patient.setPFPD1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_pFatigueOk() == 1 ) {
			patient.setPFPD2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_pFatigueOkConcerns() == 1 ) {
			patient.setPFPD2M6M(new BigInteger("1"));
		}
		else {
			patient.setPFPD2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_pFatigueOk() == 1 ) {
			patient.setPFPD9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_pFatigueOkConcerns() == 1 ) {
			patient.setPFPD9M15M(new BigInteger("1"));
		}
		else {
			patient.setPFPD9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_pFatigue18mOk() == 1 ) {
			patient.setPFSD18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_pFatigue18mOkConcerns() == 1 ) {
			patient.setPFSD18M(new BigInteger("1"));
		}
		else {
			patient.setPFSD18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_famConflictOk() == 1 ) {
			patient.setFCS1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_famConflictOkConcerns() == 1 ) {
			patient.setFCS1W1M(new BigInteger("1"));
		}
		else {
			patient.setFCS1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_famConflictOk() == 1 ) {
			patient.setFCS2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_famConflictOkConcerns() == 1 ) {
			patient.setFCS2M6M(new BigInteger("1"));
		}
		else {
			patient.setFCS2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_famConflictOk() == 1 ) {
			patient.setFCS9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_famConflictOkConcerns() == 1 ) {
			patient.setFCS9M15M(new BigInteger("1"));
		}
		else {
			patient.setFCS9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_famConflictOk() == 1 ) {
			patient.setFCS2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_famConflictOkConcerns() == 1 ) {
			patient.setFCS2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setFCS2Y5Y(new BigInteger("2"));
		}

		//abuse potential not on rourke that I can see so set it to default
		patient.setABPO1W1M(new BigInteger("2"));

		if( frmRourke2009.getP2_teethingOk() == 1 ) {
			patient.setTEETH2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_teethingOkConcerns() == 1 ) {
			patient.setTEETH2M6M(new BigInteger("1"));
		}
		else {
			patient.setTEETH2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_teethingOk() == 1 ) {
			patient.setTEETH8M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_teethingOkConcerns() == 1 ) {
			patient.setTEETH8M15M(new BigInteger("1"));
		}
		else {
			patient.setTEETH8M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_dentalCareOk() == 1 ) {
			patient.setDENT18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_dentalCareOkConcerns() == 1 ) {
			patient.setDENT18M(new BigInteger("1"));
		}
		else {
			patient.setDENT18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_dentalCleaningOk() == 1 ) {
			patient.setDENT2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_dentalCleaningOkConcerns() == 1 ) {
			patient.setDENT2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setDENT2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_siblingsOk() == 1 ) {
			patient.setSIBL1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_siblingsOkConcerns() == 1 ) {
			patient.setSIBL1W1M(new BigInteger("1"));
		}
		else {
			patient.setSIBL1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_siblingsOk() == 1 ) {
			patient.setSIBL2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_siblingsOkConcerns() == 1 ) {
			patient.setSIBL2M6M(new BigInteger("1"));
		}
		else {
			patient.setSIBL2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_siblingsOk() == 1 ) {
			patient.setSIBL9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_siblingsOkConcerns() == 1 ) {
			patient.setSIBL9M15M(new BigInteger("1"));
		}
		else {
			patient.setSIBL9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_siblingsOk() == 1 ) {
			patient.setSIBL2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_siblingsOkConcerns() == 1 ) {
			patient.setSIBL2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setSIBL2Y5Y(new BigInteger("2"));
		}
		/*START HERE */
		if( frmRourke2009.getP1_2ndSmokeOk() == 1 ) {
			patient.setSHS1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_2ndSmokeOkConcerns() == 1 ) {
			patient.setSHS1W1M(new BigInteger("1"));
		}
		else {
			patient.setSHS1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_2ndSmokeOk() == 1 ) {
			patient.setSHS2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_2ndSmokeOkConcerns() == 1 ) {
			patient.setSHS2M6M(new BigInteger("1"));
		}
		else {
			patient.setSHS2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_2ndSmokeOk() == 1 ) {
			patient.setSHS9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_2ndSmokeOkConcerns() == 1 ) {
			patient.setSHS9M15M(new BigInteger("1"));
		}
		else {
			patient.setSHS9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_2ndSmokeOk() == 1 ) {
			patient.setSHS2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_2ndSmokeOkConcerns() == 1 ) {
			patient.setSHS2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setSHS2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_altMedOk() == 1 ) {
			patient.setCOAL1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_altMedOkConcerns() == 1 ) {
			patient.setCOAL1W1M(new BigInteger("1"));
		}
		else {
			patient.setCOAL1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_altMedOk() == 1 ) {
			patient.setCOAL2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_altMedOkConcerns() == 1 ) {
			patient.setCOAL2M6M(new BigInteger("1"));
		}
		else {
			patient.setCOAL2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_altMedOk() == 1 ) {
			patient.setCOAL9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_altMedOkConcerns() == 1 ) {
			patient.setCOAL9M15M(new BigInteger("1"));
		}
		else {
			patient.setCOAL9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_altMedOk() == 1 ) {
			patient.setCOAL2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_altMedOkConcerns() == 1 ) {
			patient.setCOAL2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setCOAL2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_pacifierOk() == 1 ) {
			patient.setPACU1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_pacifierOkConcerns() == 1 ) {
			patient.setPACU1W1M(new BigInteger("1"));
		}
		else {
			patient.setPACU1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_pacifierOk() == 1 ) {
			patient.setPACU2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_pacifierOkConcerns() == 1 ) {
			patient.setPACU2M6M(new BigInteger("1"));
		}
		else {
			patient.setPACU2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_pacifierOk() == 1 ) {
			patient.setPACU9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_pacifierOkConcerns() == 1 ) {
			patient.setPACU9M15M(new BigInteger("1"));
		}
		else {
			patient.setPACU9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_feverOk() == 1 ) {
			patient.setFETH1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_feverOkConcerns() == 1 ) {
			patient.setFETH1W1M(new BigInteger("1"));
		}
		else {
			patient.setFETH1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_feverOk() == 1 ) {
			patient.setFETH2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_feverOkConcerns() == 1 ) {
			patient.setFETH2M6M(new BigInteger("1"));
		}
		else {
			patient.setFETH2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_feverOk() == 1 ) {
			patient.setFETH9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_feverOkConcerns() == 1 ) {
			patient.setFETH9M15M(new BigInteger("1"));
		}
		else {
			patient.setFETH9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_dayCareOk() == 1 ) {
			patient.setDAYSC2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_dayCareOkConcerns() == 1 ) {
			patient.setDAYSC2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setDAYSC2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_tmpControlOk() == 1 ) {
			patient.setTEOD1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_tmpControlOkConcerns() == 1 ) {
			patient.setTEOD1W1M(new BigInteger("1"));
		}
		else {
			patient.setTEOD1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_tmpControlOk() == 1 ) {
			patient.setTEOD2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_tmpControlOkConcerns() == 1 ) {
			patient.setTEOD2M6M(new BigInteger("1"));
		}
		else {
			patient.setTEOD2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_checkSerumOk() == 1 ) {
			patient.setSEPB9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_checkSerumOkConcerns() == 1 ) {
			patient.setSEPB9M15M(new BigInteger("1"));
		}
		else {
			patient.setSEPB9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_checkSerumOk() == 1 ) {
			patient.setSEPB2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_checkSerumOkConcerns() == 1 ) {
			patient.setSEPB2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setSEPB2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_sunExposureOk() == 1 ) {
			patient.setSESSIN1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_sunExposureOkConcerns() == 1 ) {
			patient.setSESSIN1W1M(new BigInteger("1"));
		}
		else {
			patient.setSESSIN1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_sunExposureOk() == 1 ) {
			patient.setSESSIN2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_sunExposureOkConcerns() == 1 ) {
			patient.setSESSIN2M6M(new BigInteger("1"));
		}
		else {
			patient.setSESSIN2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_sunExposureOk() == 1 ) {
			patient.setSESSIN9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_sunExposureOkConcerns() == 1 ) {
			patient.setSESSIN9M15M(new BigInteger("1"));
		}
		else {
			patient.setSESSIN9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_sunExposureOk() == 1 ) {
			patient.setSESSIN2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_sunExposureOkConcerns() == 1 ) {
			patient.setSESSIN2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setSESSIN2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_pesticidesOk() == 1 ) {
			patient.setPESEX2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_pesticidesOkConcerns() == 1 ) {
			patient.setPESEX2M6M(new BigInteger("1"));
		}
		else {
			patient.setPESEX2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_pesticidesOk() == 1 ) {
			patient.setPESEX9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_pesticidesOkConcerns() == 1 ) {
			patient.setPESEX9M15M(new BigInteger("1"));
		}
		else {
			patient.setPESEX9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_pesticidesOk() == 1 ) {
			patient.setPESEX2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_pesticidesOkConcerns() == 1 ) {
			patient.setPESEX2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setPESEX2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_childCareOk() == 1 ) {
			patient.setCCRW2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_childCareOkConcerns() == 1 ) {
			patient.setCCRW2M6M(new BigInteger("1"));
		}
		else {
			patient.setCCRW2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_childCareOk() == 1 ) {
			patient.setCCRW9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_childCareOkConcerns() == 1 ) {
			patient.setCCRW9M15M(new BigInteger("1"));
		}
		else {
			patient.setCCRW9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_socializing18mOk() == 1 ) {
			patient.setSOCL18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_socializing18mOkConcerns() == 1 ) {
			patient.setSOCL18M(new BigInteger("1"));
		}
		else {
			patient.setSOCL18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_socializing24mOk() == 1 ) {
			patient.setSOCL2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_socializing24mOkConcerns() == 1 ) {
			patient.setSOCL2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setSOCL2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_activeOk() == 1 ) {
			patient.setAHMU9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_activeOkConcerns() == 1 ) {
			patient.setAHMU9M15M(new BigInteger("1"));
		}
		else {
			patient.setAHMU9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_activeOk() == 1 ) {
			patient.setAHMU2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_activeOkConcerns() == 1 ) {
			patient.setAHMU2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setAHMU2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_readingOk() == 1 ) {
			patient.setREAD2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_readingOkConcerns() == 1 ) {
			patient.setREAD2M6M(new BigInteger("1"));
		}
		else {
			patient.setREAD2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_readingOk() == 1 ) {
			patient.setREAD9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_readingOkConcerns() == 1 ) {
			patient.setREAD9M15M(new BigInteger("1"));
		}
		else {
			patient.setREAD9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_readingOk() == 1 ) {
			patient.setREAD2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_readingOkConcerns() == 1 ) {
			patient.setREAD2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setREAD2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_footwearOk() == 1 ) {
			patient.setFOOT9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_footwearOkConcerns() == 1 ) {
			patient.setFOOT9M15M(new BigInteger("1"));
		}
		else {
			patient.setFOOT9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_toiletLearning18mOk() == 1 ) {
			patient.setTOIL18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_toiletLearning18mOkConcerns() == 1 ) {
			patient.setTOIL18M(new BigInteger("1"));
		}
		else {
			patient.setTOIL18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_toiletLearning24mOk() == 1 ) {
			patient.setTOIL2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_toiletLearning24mOkConcerns() == 1 ) {
			patient.setTOIL2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setTOIL2Y5Y(new BigInteger("2"));
		}
	}

	private void buildInjuryPrevention(Patient patient, FormRourke2009 frmRourke2009) {
		if( frmRourke2009.getP1_carSeatOk() == 1 ) {
			patient.setCARI1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_carSeatOkConcerns() == 1 ) {
			patient.setCARI1W1M(new BigInteger("1"));
		}
		else {
			patient.setCARI1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_sleepPosOk() == 1 ) {
			patient.setSBE1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_sleepPosOkConcerns() == 1 ) {
			patient.setSBE1W1M(new BigInteger("1"));
		}
		else {
			patient.setSBE1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_cribSafetyOk() == 1 ) {
			patient.setCS1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_cribSafetyOkConcerns() == 1 ) {
			patient.setCS1W1M(new BigInteger("1"));
		}
		else {
			patient.setCS1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_firearmSafetyOk() == 1 ) {
			patient.setFAS1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_firearmSafetyOkConcerns() == 1 ) {
			patient.setFAS1W1M(new BigInteger("1"));
		}
		else {
			patient.setFAS1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_smokeSafetyOk() == 1 ) {
			patient.setCOD1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_smokeSafetyOkConcerns() == 1 ) {
			patient.setCOD1W1M(new BigInteger("1"));
		}
		else {
			patient.setCOD1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_hotWaterOk() == 1 ) {
			patient.setH201W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_hotWaterOkConcerns() == 1 ) {
			patient.setH201W1M(new BigInteger("1"));
		}
		else {
			patient.setH201W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_safeToysOk() == 1 ) {
			patient.setCST1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_safeToysOkConcerns() == 1 ) {
			patient.setCST1W1M(new BigInteger("1"));
		}
		else {
			patient.setCST1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_carSeatOk() == 1 ) {
			patient.setCARI2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_carSeatOkConcerns() == 1 ) {
			patient.setCARI2M6M(new BigInteger("1"));
		}
		else {
			patient.setCARI2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_poisonsOk() == 1 ) {
			patient.setPOIS2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_poisonsOkConcerns() == 1 ) {
			patient.setPOIS2M6M(new BigInteger("1"));
		}
		else {
			patient.setPOIS2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_sleepPosOk() == 1 ) {
			patient.setSBECS2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_sleepPosOkConcerns() == 1 ) {
			patient.setSBECS2M6M(new BigInteger("1"));
		}
		else {
			patient.setSBECS2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_firearmSafetyOk() == 1 ) {
			patient.setFAS2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_firearmSafetyOkConcerns() == 1 ) {
			patient.setFAS2M6M(new BigInteger("1"));
		}
		else {
			patient.setFAS2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_smokeSafetyOk() == 1 ) {
			patient.setCOS2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_smokeSafetyOkConcerns() == 1 ) {
			patient.setCOS2M6M(new BigInteger("1"));
		}
		else {
			patient.setCOS2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_hotWaterOk() == 1 ) {
			patient.setH2O2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_hotWaterOkConcerns() == 1 ) {
			patient.setH2O2M6M(new BigInteger("1"));
		}
		else {
			patient.setH2O2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_safeToysOk() == 1 ) {
			patient.setCST2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_safeToysOkConcerns() == 1 ) {
			patient.setCST2M6M(new BigInteger("1"));
		}
		else {
			patient.setCST2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_electricOk() == 1 ) {
			patient.setELE2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_electricOkConcerns() == 1 ) {
			patient.setELE2M6M(new BigInteger("1"));
		}
		else {
			patient.setELE2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_fallsOk() == 1 ) {
			patient.setFALL2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_fallsOkConcerns() == 1 ) {
			patient.setFALL2M6M(new BigInteger("1"));
		}
		else {
			patient.setFALL2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_carSeatOk() == 1 ) {
			patient.setCAR19M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_carSeatOkConcerns() == 1 ) {
			patient.setCAR19M15M(new BigInteger("1"));
		}
		else {
			patient.setCAR19M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_poisonsOk() == 1 ) {
			patient.setPOIS9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_poisonsOkConcerns() == 1 ) {
			patient.setPOIS9M15M(new BigInteger("1"));
		}
		else {
			patient.setPOIS9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_firearmSafetyOk() == 1 ) {
			patient.setFAS9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_firearmSafetyOkConcerns() == 1 ) {
			patient.setFAS9M15M(new BigInteger("1"));
		}
		else {
			patient.setFAS9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_smokeSafetyOk() == 1 ) {
			patient.setCOS9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_smokeSafetyOkConcerns() == 1 ) {
			patient.setCOS9M15M(new BigInteger("1"));
		}
		else {
			patient.setCOS9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_hotWaterOk() == 1 ) {
			patient.setH2O9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_hotWaterOkConcerns() == 1 ) {
			patient.setH2O9M15M(new BigInteger("1"));
		}
		else {
			patient.setH2O9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_safeToysOk() == 1 ) {
			patient.setCST9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_safeToysOkConcerns() == 1 ) {
			patient.setCST9M15M(new BigInteger("1"));
		}
		else {
			patient.setCST9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_electricOk() == 1 ) {
			patient.setELE9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_electricOkConcerns() == 1 ) {
			patient.setELE9M15M(new BigInteger("1"));
		}
		else {
			patient.setELE9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_fallsOk() == 1 ) {
			patient.setFALL9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_fallsOkConcerns() == 1 ) {
			patient.setFALL9M15M(new BigInteger("1"));
		}
		else {
			patient.setFALL9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_carSeat18mOk() == 1 ) {
			patient.setCARC18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_carSeat18mOkConcerns() == 1 ) {
			patient.setCARC18M(new BigInteger("1"));
		}
		else {
			patient.setCARC18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_bathSafetyOk() == 1 ) {
			patient.setBATH18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_bathSafetyOkConcerns() == 1 ) {
			patient.setBATH18M(new BigInteger("1"));
		}
		else {
			patient.setBATH18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_safeToysOk() == 1 ) {
			patient.setCST18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_safeToysOkConcerns() == 1 ) {
			patient.setCST18M(new BigInteger("1"));
		}
		else {
			patient.setCST18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_carSeat24mOk() == 1 ) {
			patient.setCARCB2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_carSeat24mOkConcerns() == 1 ) {
			patient.setCARCB2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setCARCB2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_bikeHelmetsOk() == 1 ) {
			patient.setBIKE2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_bikeHelmetsOkConcerns() == 1 ) {
			patient.setBIKE2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setBIKE2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_matchesOk() == 1 ) {
			patient.setMATC2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_matchesOkConcerns() == 1 ) {
			patient.setMATC2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setMATC2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_firearmSafetyOk() == 1 ) {
			patient.setFAS2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_firearmSafetyOkConcerns() == 1 ) {
			patient.setFAS2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setFAS2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_smokeSafetyOk() == 1 ) {
			patient.setCOD2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_smokeSafetyOkConcerns() == 1 ) {
			patient.setCOD2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setCOD2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_waterSafetyOk() == 1 ) {
			patient.setH2OS(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_waterSafetyOkConcerns() == 1 ) {
			patient.setH2OS(new BigInteger("1"));
		}
		else {
			patient.setH2OS(new BigInteger("2"));
		}

		if( frmRourke2009.getP1_noCoughMedOk() == 1 ) {
			patient.setNOCC1W1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_noCoughMedOkConcerns() == 1 ) {
			patient.setNOCC1W1M(new BigInteger("1"));
		}
		else {
			patient.setNOCC1W1M(new BigInteger("2"));
		}

		if( frmRourke2009.getP2_noCoughMedOk() == 1 ) {
			patient.setNOCC2M6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_noCoughMedOkConcerns() == 1 ) {
			patient.setNOCC2M6M(new BigInteger("1"));
		}
		else {
			patient.setNOCC2M6M(new BigInteger("2"));
		}

		if( frmRourke2009.getP3_coughMedOk() == 1 ) {
			patient.setNOCC9M15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_coughMedOkConcerns() == 1 ) {
			patient.setNOCC9M15M(new BigInteger("1"));
		}
		else {
			patient.setNOCC9M15M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_noCough24mOk() == 1 ) {
			patient.setNOCC2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_noCough24mOkConcerns() == 1 ) {
			patient.setNOCC2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setNOCC2Y5Y(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_weanPacifier18mOk() == 1 ) {
			patient.setWFPAC18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_weanPacifier18mOkConcerns() == 1 ) {
			patient.setWFPAC18M(new BigInteger("1"));
		}
		else {
			patient.setWFPAC18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_encourageReading18mOk() == 1 ) {
			patient.setREAD18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_encourageReading18mOkConcerns() == 1 ) {
			patient.setREAD18M(new BigInteger("1"));
		}
		else {
			patient.setREAD18M(new BigInteger("2"));
		}

		if( frmRourke2009.getP4_noPacifier24mOk() == 1 ) {
			patient.setNOPAC2Y5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_noPacifier24mOkConcerns() == 1 ) {
			patient.setNOPAC2Y5Y(new BigInteger("1"));
		}
		else {
			patient.setNOPAC2Y5Y(new BigInteger("2"));
		}

	}

	private void buildNutrition(Patient patient, FormRourke2009 frmRourke2009) {
		if( frmRourke2009.getP4_lowerfatdiet24mOk() == 1 ) {
			patient.setGRF2T3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_lowerfatdiet24mOkConcerns() == 1 ) {
			patient.setGRF2T3Y(new BigInteger("1"));
		}
		else {
			patient.setGRF2T3Y(new BigInteger("3"));
		}

		if( frmRourke2009.getP2_liquids6mOk() == 1 ) {
			patient.setNSL6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_liquids6mOkConcerns() == 1 ) {
			patient.setNSL6M(new BigInteger("1"));
		}
		else {
			patient.setNSL6M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_liquids9mOk() == 1 ) {
			patient.setNSL9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_liquids9mOkConcerns() == 1 ) {
			patient.setNSL9M(new BigInteger("1"));
		}
		else {
			patient.setNSL9M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_appetite12mOk() == 1 ) {
			patient.setRA12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_appetite12mOkConcerns() == 1 ) {
			patient.setRA12M(new BigInteger("1"));
		}
		else {
			patient.setRA12M(new BigInteger("3"));
		}

		if( frmRourke2009.getP2_choking6mOk() == 1 ) {
			patient.setSF6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_choking6mOkConcerns() == 1 ) {
			patient.setSF6M(new BigInteger("1"));
		}
		else {
			patient.setSF6M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_choking9mOk() == 1 ) {
			patient.setSF9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_choking9mOkConcerns() == 1 ) {
			patient.setSF9M(new BigInteger("1"));
		}
		else {
			patient.setSF9M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_choking12mOk() == 1 ) {
			patient.setSF12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_choking12mOkConcerns() == 1 ) {
			patient.setSF12M(new BigInteger("1"));
		}
		else {
			patient.setSF12M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_choking15mOk() == 1 ) {
			patient.setSF15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_choking15mOkConcerns() == 1 ) {
			patient.setSF15M(new BigInteger("1"));
		}
		else {
			patient.setSF15M(new BigInteger("3"));
		}

		if( frmRourke2009.getP2_iron6mOk() == 1 ) {
			patient.setIRN6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_iron6mOkConcerns() == 1 ) {
			patient.setIRN6M(new BigInteger("1"));
		}
		else {
			patient.setIRN6M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_cereal9mOk() == 1 ) {
			patient.setCMV9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_cereal9mOkConcerns() == 1 ) {
			patient.setCMV9M(new BigInteger("1"));
		}
		else {
			patient.setCMV9M(new BigInteger("3"));
		}

		if( frmRourke2009.getP2_vegFruit6mOk() == 1 ) {
			patient.setFNV6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_vegFruit6mOkConcerns() == 1 ) {
			patient.setFNV6M(new BigInteger("1"));
		}
		else {
			patient.setFNV6M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_introCowMilk9mOk() == 1 ) {
			patient.setCOW9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_introCowMilk9mOkConcerns() == 1 ) {
			patient.setCOW9M(new BigInteger("1"));
		}
		else {
			patient.setCOW9M(new BigInteger("3"));
		}

		if( frmRourke2009.getP2_egg6mOk() == 1 ) {
			patient.setNENH6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_egg6mOkConcerns() == 1 ) {
			patient.setNENH6M(new BigInteger("1"));
		}
		else {
			patient.setNENH6M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_egg9mOk() == 1 ) {
			patient.setNENH9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_egg9mOkConcerns() == 1 ) {
			patient.setNENH9M(new BigInteger("1"));
		}
		else {
			patient.setNENH9M(new BigInteger("3"));
		}

	}

	private void buildCup(Patient patient, FormRourke2009 frmRourke2009) {
		if( frmRourke2009.getP3_cup12mOk() == 1 ) {
			patient.setCUP12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_cup12mOkConcerns() == 1 ) {
			patient.setCUP12M(new BigInteger("1"));
		}
		else {
			patient.setCUP12M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_cup15mOk() == 1 ) {
			patient.setCUP15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_cup15mOkConcerns() == 1 ) {
			patient.setCUP15M(new BigInteger("1"));
		}
		else {
			patient.setCUP15M(new BigInteger("3"));
		}
	}

	private void buildBottleInBed(Patient patient, FormRourke2009 frmRourke2009) {
		if( frmRourke2009.getP2_bottle6mOk() == 1 ) {
			patient.setNBIB6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_bottle6mOkConcerns() == 1 ) {
			patient.setNBIB6M(new BigInteger("1"));
		}
		else {
			patient.setNBIB6M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_bottle9mOk() == 1 ) {
			patient.setNBIB9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_bottle9mOkConcerns() == 1 ) {
			patient.setNBIB9M(new BigInteger("1"));
		}
		else {
			patient.setNBIB9M(new BigInteger("3"));
		}

		if( frmRourke2009.getP4_bottle18mOk() == 1 ) {
			patient.setNOBO18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_bottle18mOkConcerns() == 1 ) {
			patient.setNOBO18M(new BigInteger("1"));
		}
		else {
			patient.setNOBO18M(new BigInteger("3"));
		}
	}

	private void buildStoolUrine(Patient patient, FormRourke2009 frmRourke2009) {
		if( frmRourke2009.getP1_stoolUrine1wOk() == 1 ) {
			patient.setSU1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_stoolUrine1wOkConcerns() == 1 ) {
			patient.setSU1W(new BigInteger("1"));
		}
		else {
			patient.setSU1W(new BigInteger("3"));
		}

		if( frmRourke2009.getP1_stoolUrine2wOk() == 1 ) {
			patient.setSU2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_stoolUrine2wOkConcerns() == 1 ) {
			patient.setSU2W(new BigInteger("1"));
		}
		else {
			patient.setSU2W(new BigInteger("3"));
		}

		if( frmRourke2009.getP1_stoolUrine1mOk() == 1 ) {
			patient.setSU1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_stoolUrine1mOkConcerns() == 1 ) {
			patient.setSU1M(new BigInteger("1"));
		}
		else {
			patient.setSU1M(new BigInteger("3"));
		}

	}

	private void buildMilk(Patient patient, FormRourke2009 frmRourke2009 ) {
		if( frmRourke2009.getP3_homoMilk12mOk() == 1 ) {
			patient.setHH12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_homoMilk12mOkConcerns() == 1 ) {
			patient.setHH12M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP3_homoMilk12mNo() == 1 ) {
			patient.setHH12M(new BigInteger("2"));
		}
		else {
			patient.setHH12M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_homoMilk15mOk() == 1 ) {
			patient.setHH15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_homoMilk15mOkConcerns() == 1 ) {
			patient.setHH15M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP3_homoMilk15mNo() == 1 ) {
			patient.setHH15M(new BigInteger("2"));
		}
		else {
			patient.setHH15M(new BigInteger("3"));
		}

		if( frmRourke2009.getP4_homoMilk18mOk() == 1 ) {
			patient.setHH18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_homoMilk18mOkConcerns() == 1 ) {
			patient.setHH18M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP4_homoMilk18mNo() == 1 ) {
			patient.setHH18M(new BigInteger("2"));
		}
		else {
			patient.setHH18M(new BigInteger("3"));
		}

		if( frmRourke2009.getP4_homo2percent24mOk() == 1 ) {
			patient.setHM2P2T3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_homo2percent24mOkConcerns() == 1 ) {
			patient.setHM2P2T3Y(new BigInteger("1"));
		}
		else if( frmRourke2009.getP4_homo2percent24mNo() == 1 ) {
			patient.setHM2P2T3Y(new BigInteger("2"));
		}
		else {
			patient.setHM2P2T3Y(new BigInteger("3"));
		}

		if( frmRourke2009.getP4_2pMilk48mOk() == 1 ) {
			patient.setM2P4T5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_2pMilk48mOkConcerns() == 1 ) {
			patient.setM2P4T5Y(new BigInteger("1"));
		}
		else if( frmRourke2009.getP4_2pMilk48mNo() == 1 ) {
			patient.setM2P4T5Y(new BigInteger("2"));
		}
		else {
			patient.setM2P4T5Y(new BigInteger("3"));
		}
	}

	private void buildFormulaFeeding(Patient patient, FormRourke2009 frmRourke2009 ) {
		if( frmRourke2009.getP1_formulaFeeding1wOk() == 1 ) {
			patient.setFF1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_formulaFeeding1wOkConcerns() == 1 ) {
			patient.setFF1W(new BigInteger("1"));
		}
		else if( frmRourke2009.getP1_formulaFeeding1wNo() == 1 ) {
			patient.setFF1W(new BigInteger("2"));
		}
		else {
			patient.setFF1W(new BigInteger("3"));
		}

		if( frmRourke2009.getP1_formulaFeeding2wOk() == 1 ) {
			patient.setFF2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_formulaFeeding2wOkConcerns() == 1 ) {
			patient.setFF2W(new BigInteger("1"));
		}
		else if( frmRourke2009.getP1_formulaFeeding2wNo() == 1 ) {
			patient.setFF2W(new BigInteger("2"));
		}
		else {
			patient.setFF2W(new BigInteger("3"));
		}

		if( frmRourke2009.getP1_formulaFeeding1mOk() == 1 ) {
			patient.setFF1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_formulaFeeding1mOkConcerns() == 1 ) {
			patient.setFF1M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP1_formulaFeeding1mNo() == 1 ) {
			patient.setFF1M(new BigInteger("2"));
		}
		else {
			patient.setFF1M(new BigInteger("3"));
		}

		if( frmRourke2009.getP2_formulaFeeding2mOk() == 1 ) {
			patient.setFF2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_formulaFeeding2mOkConcerns() == 1 ) {
			patient.setFF2M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP2_formulaFeeding2mNo() == 1 ) {
			patient.setFF2M(new BigInteger("2"));
		}
		else {
			patient.setFF2M(new BigInteger("3"));
		}

		if( frmRourke2009.getP2_formulaFeeding4mOk() == 1 ) {
			patient.setFF4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_formulaFeeding4mOkConcerns() == 1 ) {
			patient.setFF4M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP2_formulaFeeding4mNo() == 1 ) {
			patient.setFF4M(new BigInteger("2"));
		}
		else {
			patient.setFF4M(new BigInteger("3"));
		}

		if( frmRourke2009.getP2_formulaFeeding6mOk() == 1 ) {
			patient.setFF6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_formulaFeeding6mOkConcerns() == 1 ) {
			patient.setFF6M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP2_formulaFeeding6mNo() == 1 ) {
			patient.setFF6M(new BigInteger("2"));
		}
		else {
			patient.setFF6M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_formulaFeeding9mOk() == 1 ) {
			patient.setFF9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_formulaFeeding9mOkConcerns() == 1 ) {
			patient.setFF9M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP3_formulaFeeding9mNo() == 1 ) {
			patient.setFF9M(new BigInteger("2"));
		}
		else {
			patient.setFF9M(new BigInteger("3"));
		}

	}

	private void buildCanadaFoodGuide(Patient patient, FormRourke2009 frmRourke2009 ) {
		if( frmRourke2009.getP4_foodguide24mOk() == 1 ) {
			patient.setCANF2T3Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_foodguide24mNo() == 1 ) {
			patient.setCANF2T3Y(new BigInteger("2"));
		}
		else {
			patient.setCANF2T3Y(new BigInteger("3"));
		}

		if( frmRourke2009.getP4_foodguide48mOk() == 1 ) {
			patient.setCANF4T5Y(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_foodguide48mNo() == 1 ) {
			patient.setCANF4T5Y(new BigInteger("2"));
		}
		else {
			patient.setCANF4T5Y(new BigInteger("3"));
		}

	}

	private void buildBreastFeeding(Patient patient, FormRourke2009 frmRourke2009) {
		if( frmRourke2009.getP1_breastFeeding1wOk() == 1 ) {
			patient.setBFVD1W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_breastFeeding1wOkConcerns() == 1 ) {
			patient.setBFVD1W(new BigInteger("1"));
		}
		else if( frmRourke2009.getP1_breastFeeding1wNo() == 1 ) {
			patient.setBFVD1W(new BigInteger("2"));
		}
		else {
			patient.setBFVD1W(new BigInteger("3"));
		}

		if( frmRourke2009.getP1_breastFeeding2wOk() == 1 ) {
			patient.setBFVD2W(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_breastFeeding2wOkConcerns() == 1 ) {
			patient.setBFVD2W(new BigInteger("1"));
		}
		else if( frmRourke2009.getP1_breastFeeding2wNo() == 1 ) {
			patient.setBFVD2W(new BigInteger("2"));
		}
		else {
			patient.setBFVD2W(new BigInteger("3"));
		}

		if( frmRourke2009.getP1_breastFeeding1mOk() == 1 ) {
			patient.setBFVD1M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP1_breastFeeding1mOkConcerns() == 1 ) {
			patient.setBFVD1M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP1_breastFeeding1mNo() == 1 ) {
			patient.setBFVD1M(new BigInteger("2"));
		}
		else {
			patient.setBFVD1M(new BigInteger("3"));
		}

		if( frmRourke2009.getP2_breastFeeding2mOk() == 1 ) {
			patient.setBFVD2M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_breastFeeding2mOkConcerns() == 1 ) {
			patient.setBFVD2M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP2_breastFeeding2mNo() == 1 ) {
			patient.setBFVD2M(new BigInteger("2"));
		}
		else {
			patient.setBFVD2M(new BigInteger("3"));
		}

		if( frmRourke2009.getP2_breastFeeding4mOk() == 1 ) {
			patient.setBFVD4M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_breastFeeding4mOkConcerns() == 1 ) {
			patient.setBFVD4M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP2_breastFeeding4mNo() == 1 ) {
			patient.setBFVD4M(new BigInteger("2"));
		}
		else {
			patient.setBFVD4M(new BigInteger("3"));
		}

		if( frmRourke2009.getP2_breastFeeding6mOk() == 1 ) {
			patient.setBFVD6M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP2_breastFeeding6mOkConcerns() == 1 ) {
			patient.setBFVD6M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP2_breastFeeding6mNo() == 1 ) {
			patient.setBFVD6M(new BigInteger("2"));
		}
		else {
			patient.setBFVD6M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_breastFeeding9mOk() == 1 ) {
			patient.setBFVD9M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_breastFeeding9mOkConcerns() == 1 ) {
			patient.setBFVD9M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP3_breastFeeding9mNo() == 1 ) {
			patient.setBFVD9M(new BigInteger("2"));
		}
		else {
			patient.setBFVD9M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_breastFeeding12mOk() == 1 ) {
			patient.setBFVD12M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_breastFeeding12mOkConcerns() == 1 ) {
			patient.setBFVD12M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP3_breastFeeding12mNo() == 1 ) {
			patient.setBFVD12M(new BigInteger("2"));
		}
		else {
			patient.setBFVD12M(new BigInteger("3"));
		}

		if( frmRourke2009.getP3_breastFeeding15mOk() == 1 ) {
			patient.setBFVD15M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP3_breastFeeding15mOkConcerns() == 1 ) {
			patient.setBFVD15M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP3_breastFeeding15mNo() == 1 ) {
			patient.setBFVD15M(new BigInteger("2"));
		}
		else {
			patient.setBFVD15M(new BigInteger("3"));
		}

		if( frmRourke2009.getP4_breastFeeding18mOk() == 1 ) {
			patient.setBFVD18M(new BigInteger("0"));
		}
		else if( frmRourke2009.getP4_breastFeeding18mOkConcerns() == 1 ) {
			patient.setBFVD18M(new BigInteger("1"));
		}
		else if( frmRourke2009.getP4_breastFeeding18mNo() == 1 ) {
			patient.setBFVD18M(new BigInteger("2"));
		}
		else {
			patient.setBFVD18M(new BigInteger("3"));
		}
	}

	private void buildParentalConcerns(Patient patient, FormRourke2009 frmRourke2009) {
		patient.setPCON1W(frmRourke2009.getP1_pConcern1w());
		patient.setPCON2W(frmRourke2009.getP1_pConcern2w());
		patient.setPCON1M(frmRourke2009.getP1_pConcern1m());

		patient.setPCON2M(frmRourke2009.getP2_pConcern2m());
		patient.setPCON4M(frmRourke2009.getP2_pConcern4m());
		patient.setPCON6M(frmRourke2009.getP2_pConcern6m());

		patient.setPCON9M(frmRourke2009.getP3_pConcern9m());
		patient.setPCON12M(frmRourke2009.getP3_pConcern12m());
		patient.setPCON15M(frmRourke2009.getP3_pConcern15m());

		patient.setPCON18M(frmRourke2009.getP4_pConcern18m());
		patient.setPCON2T3Y(frmRourke2009.getP4_pConcern24m());
		patient.setPCON4T5Y(frmRourke2009.getP4_pConcern48m());
	}

	private void buildMeasurements(Patient patient, FormRourke2009 frmRourke2009) {
		String measure;
		//Birth Length
		if( StringUtils.isNumeric(frmRourke2009.getCLength()) ) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getCLength())));
			patient.setLENB(new BigInteger(measure));
		}

		//Birth head circ.
		if( StringUtils.isNumeric(frmRourke2009.getC_headCirc())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getC_headCirc())));
			patient.setHDCRB(new BigInteger(measure));
		}

		//Birth weight
		if( StringUtils.isNumeric(frmRourke2009.getC_birthWeight())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getC_birthWeight())));
			patient.setWTB(new BigInteger(measure));
		}

		//Discharge weight
		if( StringUtils.isNumeric(frmRourke2009.getC_dischargeWeight())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getC_dischargeWeight())));
			patient.setWTD(new BigInteger(measure));
		}

		//1 week measurements
		if( StringUtils.isNumeric(frmRourke2009.getP1Ht1w())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP1Ht1w())));
			patient.setHT1W(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP1Wt1w())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP1Wt1w())));
			patient.setWT1W(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP1Hc1w())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP1Hc1w())));
			patient.setHDCR1W(new BigInteger(measure));
		}

		//2 week measurements
		if( StringUtils.isNumeric(frmRourke2009.getP1Ht2w())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP1Ht2w())));
			patient.setHT2W(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP1Wt2w())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP1Wt2w())));
			patient.setWT2W(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP1Hc2w())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP1Hc2w())));
			patient.setHDCR2W(new BigInteger(measure));
		}

		//1 month measurements
		if( StringUtils.isNumeric(frmRourke2009.getP1Ht1m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP1Ht1m())));
			patient.setHT1M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP1Wt1m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP1Wt1m())));
			patient.setWT1M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP1Hc1m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP1Hc1m())));
			patient.setHDCR1M(new BigInteger(measure));
		}

		//2 month measurements
		if( StringUtils.isNumeric(frmRourke2009.getP2Ht2m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP2Ht2m())));
			patient.setHT2M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP2Wt2m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP2Wt2m())));
			patient.setWT2M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP2Hc2m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP2Hc2m())));
			patient.setHDCR2M(new BigInteger(measure));
		}

		//4 month measurements
		if( StringUtils.isNumeric(frmRourke2009.getP2Ht4m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP2Ht4m())));
			patient.setHT4M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP2Wt4m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP2Wt4m())));
			patient.setWT4M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP2Hc4m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP2Hc4m())));
			patient.setHDCR4M(new BigInteger(measure));
		}

		//6 month measurements
		if( StringUtils.isNumeric(frmRourke2009.getP2Ht6m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP2Ht6m())));
			patient.setHT6M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP2Wt6m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP2Wt6m())));
			patient.setWT6M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP2Hc6m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP2Hc6m())));
			patient.setHDCR6M(new BigInteger(measure));
		}

		//9 month measurements
		if( StringUtils.isNumeric(frmRourke2009.getP3Ht9m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP3Ht9m())));
			patient.setHT9M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP3Wt9m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP3Wt9m())));
			patient.setWT9M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP3Hc9m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP3Hc9m())));
			patient.setHDCR9M(new BigInteger(measure));
		}

		//12 month measurements
		if( StringUtils.isNumeric(frmRourke2009.getP3Ht12m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP3Ht12m())));
			patient.setHT12M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP3Wt12m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP3Wt12m())));
			patient.setWT12M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP3Hc12m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP3Hc12m())));
			patient.setHDCR12M(new BigInteger(measure));
		}

		//15 month measurements
		if( StringUtils.isNumeric(frmRourke2009.getP3Ht15m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP3Ht15m())));
			patient.setHT15M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP3Wt15m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP3Wt15m())));
			patient.setWT15M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP3Hc15m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP3Hc15m())));
			patient.setHDCR15M(new BigInteger(measure));
		}

		//18 month measurements
		if( StringUtils.isNumeric(frmRourke2009.getP4Ht18m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP4Ht18m())));
			patient.setHT18M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP4Wt18m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP4Wt18m())));
			patient.setWT18M(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP4Hc18m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP4Hc18m())));
			patient.setHDCR18M(new BigInteger(measure));
		}

		//24 month measurements
		if( StringUtils.isNumeric(frmRourke2009.getP4Ht24m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP4Ht24m())));
			patient.setHT2T3Y(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP4Wt24m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP4Wt24m())));
			patient.setWT2T3Y(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP4Hc24m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP4Hc24m())));
			patient.setHDCR2T3Y(new BigInteger(measure));
		}

		//48 month measurements
		if( StringUtils.isNumeric(frmRourke2009.getP4Ht48m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP4Ht48m())));
			patient.setHT4T5Y(new BigInteger(measure));
		}

		if( StringUtils.isNumeric(frmRourke2009.getP4Wt48m())) {
			measure = String.valueOf(Math.round(Float.parseFloat(frmRourke2009.getP4Wt48m())));
			patient.setWT4T5Y(new BigInteger(measure));
		}


	}

	private void buildVisitdates(Patient patient, FormRourke2009 frmRourke2009) {
		Calendar calendar = Calendar.getInstance();
		Date date = frmRourke2009.getP1Date1w();

		if( date != null ) {
			calendar.setTime(date);
			patient.setVD1W(calendar);
		}

		date = frmRourke2009.getP1Date2w();
		if( date != null ) {
			calendar.setTime(date);
			patient.setVD2W(calendar);
		}

		date = frmRourke2009.getP1Date1m();
		if( date != null ) {
			calendar.setTime(date);
			patient.setVD1M(calendar);
		}

		date = frmRourke2009.getP2Date2m();
		if( date != null ) {
			calendar.setTime(date);
			patient.setVD2M(calendar);
		}

		date = frmRourke2009.getP2Date4m();
		if( date != null ) {
			calendar.setTime(date);
			patient.setVD4M(calendar);
		}

		date = frmRourke2009.getP2Date6m();
		if( date != null ) {
			calendar.setTime(date);
			patient.setVD6M(calendar);
		}

		date = frmRourke2009.getP3Date9m();
		if( date != null ) {
			calendar.setTime(date);
			patient.setVD9M(calendar);
		}

		date = frmRourke2009.getP3Date12m();
		if( date != null ) {
			calendar.setTime(date);
			patient.setVD12M(calendar);
		}

		date = frmRourke2009.getP3Date15m();
		if( date != null ) {
			calendar.setTime(date);
			patient.setVD15M(calendar);
		}

		date = frmRourke2009.getP4Date18m();
		if( date != null ) {
			calendar.setTime(date);
			patient.setVD18M(calendar);
		}

		date = frmRourke2009.getP4Date24m();
		if( date != null ) {
			calendar.setTime(date);
			patient.setVD2T3Y(calendar);
		}

		date = frmRourke2009.getP4Date48m();
		if( date != null ) {
			calendar.setTime(date);
			patient.setVD4T5Y(calendar);
		}

	}

	private void buildPatientDemographic(Demographic demo, Patient patient) {

		String healthCard = demo.getHin();
		if( healthCard == null ) {
			healthCard = "";
		}
        patient.setHCNUM(healthCard);

        String patientName = demo.getFormattedName();
        patient.setNAME(patientName);


        Calendar calendarDob = demo.getBirthDay();
        if( calendarDob != null ) {
        	patient.setDOB(demo.getYearOfBirth() + "-" + demo.getMonthOfBirth());
        }

        String sex = demo.getSex();

        if( "F".equalsIgnoreCase(sex) ) {
        	patient.setSEX(new BigInteger("1"));
        }
        else if( "M".equalsIgnoreCase(sex) ) {
        	patient.setSEX(new BigInteger("0"));
        }

        String postalCode = demo.getPostal();
        if (StringUtils.filled(postalCode) && postalCode.length() >= 6 ) {
            postalCode = StringUtils.noNull(postalCode).replace(" ","");
            patient.setFSA(postalCode.substring(0, 3));
        }
	}



/*
	private void buildProcedure2(Demographic demo, PatientRecord patientRecord) {
            OscarProperties properties = OscarProperties.getInstance();
            Calendar cal = Calendar.getInstance();
            Date procedureDate;
            boolean hasIssue;
            String code;
            List<CaseManagementNote> notesList = getCaseManagementNoteDAO().getNotesByDemographic(demo.getDemographicNo().toString());
            for( CaseManagementNote caseManagementNote: notesList) {

                Procedure procedure = patientRecord.addNewProcedure();
                hasIssue = false;
                Set<CaseManagementIssue> noteIssueList = caseManagementNote.getIssues();
                if( noteIssueList != null && noteIssueList.size() > 0 ) {
                    Iterator<CaseManagementIssue> i = noteIssueList.iterator();
                    CaseManagementIssue cIssue;
                    hasIssue = true;
                    while ( i.hasNext() ) {
                        cIssue = i.next();
                        if (cIssue.getIssue().getType().equals("system")) continue;

                        StandardCoding procedureCode = procedure.addNewProcedureCode();
                        procedureCode.setStandardCodingSystem(properties.getProperty("dxResearch_coding_sys","icd9"));
                        procedureCode.setStandardCode(cIssue.getIssue().getCode());
                        procedureCode.setStandardCodeDescription(cIssue.getIssue().getDescription());
                        break;
                    }
                }

                //if( !hasIssue ) {
                    String note = caseManagementNote.getNote();
                    if (note!=null && note.length()>250)
                        procedure.setProcedureInterventionDescription(caseManagementNote.getNote().substring(0, 249));//Description only allow 250 characters
                //}

                procedureDate = caseManagementNote.getObservation_date();
                cal.setTime(procedureDate);
                DateFullOrPartial dateFullOrPartial = procedure.addNewProcedureDate();
                dateFullOrPartial.setFullDate(cal);
            }
	}
 *
 */

	private String makeFiles(PatientDocument patientDocument, String tmpDir) throws Exception {
    	XmlOptions options = new XmlOptions();
    	options.put( XmlOptions.SAVE_PRETTY_PRINT );
    	options.put( XmlOptions.SAVE_PRETTY_PRINT_INDENT, 3 );
    	options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES );

    	options.setSaveOuter();

    	String fileName = "Rourke2009Export.xml";
    	File xmlFile = new File(tmpDir, fileName);
    	try {
    		patientDocument.save(xmlFile, options);
    	}
    	catch(IOException e) {
    		MiscUtils.getLogger().error("Cannot write .xml file(s) to export directory " + tmpDir + ".\nPlease check directory permissions.", e);
    	}

    	ArrayList<File>files = new ArrayList<File>();
    	files.add(xmlFile);
    	//Zip export files
        String zipName = "rourke2009_export-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".zip";
        if (!Util.zipFiles(files, zipName, tmpDir)) {
        	MiscUtils.getLogger().error("Error! Failed zipping export files");
        }

        //copy zip to document directory
        File zipFile = new File(tmpDir,zipName);
        OscarProperties properties = OscarProperties.getInstance();
        File destDir = new File(properties.getProperty("DOCUMENT_DIR"));
        org.apache.commons.io.FileUtils.copyFileToDirectory(zipFile, destDir);

        //Remove zip & export files from temp dir
        Util.cleanFile(zipName, tmpDir);
        Util.cleanFiles(files);

        return zipName;
    }
}
