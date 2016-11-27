/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.FunctionalCentreAdmissionDao;
import org.oscarehr.common.dao.OcanClientFormDao;
import org.oscarehr.common.dao.OcanConnexOptionDao;
import org.oscarehr.common.dao.OcanFormOptionDao;
import org.oscarehr.common.dao.OcanStaffFormDao;
import org.oscarehr.common.dao.OcanStaffFormDataDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.OcanClientForm;
import org.oscarehr.common.model.OcanConnexOption;
import org.oscarehr.common.model.OcanFormOption;
import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.common.model.OcanStaffFormData;
import org.oscarehr.util.SpringUtils;

public class OcanForm {
	
	public static final int PRE_POPULATION_LEVEL_ALL 			= 3;
	public static final int PRE_POPULATION_LEVEL_DEMOGRAPHIC 	= 2;
	public static final int PRE_POPULATION_LEVEL_NONE 			= 1;
	
	private static AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static DemographicExtDao demographicExtDao = (DemographicExtDao) SpringUtils.getBean("demographicExtDao");
	private static OcanFormOptionDao ocanFormOptionDao = (OcanFormOptionDao) SpringUtils.getBean("ocanFormOptionDao");
	private static OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
	private static OcanStaffFormDataDao ocanStaffFormDataDao = (OcanStaffFormDataDao) SpringUtils.getBean("ocanStaffFormDataDao");	
	private static OcanClientFormDao ocanClientFormDao = (OcanClientFormDao) SpringUtils.getBean("ocanClientFormDao");
	private static OcanConnexOptionDao ocanConnexOptionDao = (OcanConnexOptionDao) SpringUtils.getBean("ocanConnexOptionDao");
    private static FunctionalCentreAdmissionDao functionalCentreAdmissionDao = (FunctionalCentreAdmissionDao) SpringUtils.getBean("functionalCentreAdmissionDao");
		
	public static Demographic getDemographic(String demographicId)
	{
		return demographicDao.getDemographic(demographicId);
	}
	
	public static OcanStaffForm getOcanStaffForm(Integer ocanStaffFormId) {
		OcanStaffForm ocanStaffForm = null;
		ocanStaffForm = ocanStaffFormDao.findOcanStaffFormById(ocanStaffFormId);
		return ocanStaffForm;
	}
	
		
	public static OcanStaffForm getOcanStaffForm(Integer facilityId,Integer clientId, int prepopulationLevel,String ocanType)
	{
		OcanStaffForm ocanStaffForm=null;
		
		if(prepopulationLevel != OcanForm.PRE_POPULATION_LEVEL_NONE) {
			ocanStaffForm=ocanStaffFormDao.findLatestByFacilityClient(facilityId, clientId, ocanType);
		}

		if (ocanStaffForm==null || "Completed".equals(ocanStaffForm.getAssessmentStatus()))			
		{
			ocanStaffForm=new OcanStaffForm();
			ocanStaffForm.setAddressLine2("");
			
			if(prepopulationLevel != OcanForm.PRE_POPULATION_LEVEL_NONE) {
				ocanStaffForm.setAssessmentId(ocanStaffForm.getId());
				
				Demographic demographic=demographicDao.getDemographicById(clientId);		
				ocanStaffForm.setLastName(demographic.getLastName());
				ocanStaffForm.setFirstName(demographic.getFirstName());	
				ocanStaffForm.setAddressLine1(demographic.getAddress());
				ocanStaffForm.setCity(demographic.getCity());
				ocanStaffForm.setProvince(demographic.getProvince());
				ocanStaffForm.setPostalCode(demographic.getPostal());
				ocanStaffForm.setPhoneNumber(demographic.getPhone());
				ocanStaffForm.setEmail(demographic.getEmail());
				ocanStaffForm.setHcNumber(demographic.getHin());
				ocanStaffForm.setHcVersion(demographic.getVer());
				ocanStaffForm.setDateOfBirth(demographic.getFormattedDob());
				ocanStaffForm.setClientDateOfBirth(demographic.getFormattedDob());
				ocanStaffForm.setGender(convertGender(demographic.getSex()));
				
			}				             
		}
		
		return(ocanStaffForm);
	}
	
	public static OcanStaffForm getCbiForm(Integer ocanStaffFormId, Integer clientId, int prepopulationLevel,String ocanType, Integer programId) {
		//The demographic data should be populated each time when create or edit cbi form
		OcanStaffForm ocanStaffForm = null;
		if(prepopulationLevel == OcanForm.PRE_POPULATION_LEVEL_DEMOGRAPHIC) {
			ocanStaffForm = ocanStaffFormDao.findOcanStaffFormById(ocanStaffFormId);
			Demographic demographic=demographicDao.getDemographicById(clientId);		
			ocanStaffForm.setLastName(demographic.getLastName());
			ocanStaffForm.setFirstName(demographic.getFirstName());	
			ocanStaffForm.setAddressLine1(demographic.getAddress()==null?"":demographic.getAddress());
			ocanStaffForm.setCity(demographic.getCity()==null?"":demographic.getCity());
			ocanStaffForm.setProvince(demographic.getProvince()==null?"":demographic.getProvince());
			ocanStaffForm.setPostalCode(demographic.getPostal()==null?"":demographic.getPostal());
			ocanStaffForm.setPhoneNumber(demographic.getPhone()==null?"":demographic.getPhone());
			ocanStaffForm.setEmail(demographic.getEmail()==null?"":demographic.getEmail());
			ocanStaffForm.setHcNumber(demographic.getHin()==null?"":demographic.getHin());
			ocanStaffForm.setHcVersion(demographic.getVer()==null?"":demographic.getVer());
			ocanStaffForm.setDateOfBirth(demographic.getFormattedDob());
			ocanStaffForm.setClientDateOfBirth(demographic.getFormattedDob()==null?"":demographic.getFormattedDob());
			ocanStaffForm.setGender(convertGender(demographic.getSex()==null?"":demographic.getSex()));
			
			Calendar rightNow = Calendar.getInstance();
			int year = rightNow.get(Calendar.YEAR);
			int month = rightNow.get(Calendar.MONTH)+1;
			int date = rightNow.get(Calendar.DATE);
			if(demographic.getFormattedDob()!=null) {
				String[] split_dob=demographic.getFormattedDob().split("-");
				int year_dob=Integer.parseInt(split_dob[0]);
				int month_dob = Integer.parseInt(split_dob[1]);
				int date_dob = Integer.parseInt(split_dob[2]);
				int age = year - year_dob;			
				if(month < month_dob) {
					age--;
				} else if(month==month_dob){
					if(date < date_dob)
						age--;
				}
				ocanStaffForm.setEstimatedAge(String.valueOf(age));
			}
		} else {
			ocanStaffForm = getOcanStaffForm(ocanStaffFormId);
		}
		
		return ocanStaffForm;
	}
	
	public static OcanStaffForm getCbiInitForm(Integer clientId, int prepopulationLevel,String ocanType, Integer programId)
	{		
		OcanStaffForm ocanStaffForm=new OcanStaffForm();
		ocanStaffForm.setAddressLine2("");
		ocanStaffForm.setOcanType(ocanType);
		ocanStaffForm.setAssessmentId(ocanStaffForm.getId());
		/*
		//get admission info
		List<Admission> admissions = admissionDao.getAdmissionsByProgramAndClient(clientId, programId);
		if(admissions.size()>0) {
			Admission ad = admissions.get(0);
			ocanStaffForm.setAdmissionId(ad.getId().intValue());
			ocanStaffForm.setServiceInitDate(ad.getAdmissionDate());
			ocanStaffForm.setAdmissionDate(ad.getAdmissionDate());
			ocanStaffForm.setDischargeDate(ad.getDischargeDate());
			
			//Find referral date
			ClientReferralDAO clientReferralDao = (ClientReferralDAO) SpringUtils.getBean("clientReferralDAO");
			List<ClientReferral> referrals = clientReferralDao.getActiveReferralsByClientAndProgram(Long.valueOf(clientId.longValue()), Long.valueOf(programId.longValue()));
			if(referrals.size() > 0 ) {
				ClientReferral ref = referrals.get(0);
				if(ref.getReferralDate()!=null)
					ocanStaffForm.setReferralDate(ref.getReferralDate());
				else
					ocanStaffForm.setReferralDate(ad.getAdmissionDate());
			} else {
				ocanStaffForm.setReferralDate(ad.getAdmissionDate());
			}
			
		}
		*/
		
		if(prepopulationLevel == OcanForm.PRE_POPULATION_LEVEL_DEMOGRAPHIC) {				
				Demographic demographic=demographicDao.getDemographicById(clientId);		
				ocanStaffForm.setLastName(demographic.getLastName());
				ocanStaffForm.setFirstName(demographic.getFirstName());	
				ocanStaffForm.setAddressLine1(demographic.getAddress()==null?"":demographic.getAddress());
				ocanStaffForm.setCity(demographic.getCity()==null?"":demographic.getCity());
				ocanStaffForm.setProvince(demographic.getProvince()==null?"":demographic.getProvince());
				ocanStaffForm.setPostalCode(demographic.getPostal()==null?"":demographic.getPostal());
				ocanStaffForm.setPhoneNumber(demographic.getPhone()==null?"":demographic.getPhone());
				ocanStaffForm.setEmail(demographic.getEmail()==null?"":demographic.getEmail());
				ocanStaffForm.setHcNumber(demographic.getHin()==null?"":demographic.getHin());
				ocanStaffForm.setHcVersion(demographic.getVer()==null?"":demographic.getVer());
				ocanStaffForm.setDateOfBirth(demographic.getFormattedDob());
				ocanStaffForm.setClientDateOfBirth(demographic.getFormattedDob()==null?"":demographic.getFormattedDob());
				ocanStaffForm.setGender(convertGender(demographic.getSex()==null?"":demographic.getSex()));
				
				Calendar rightNow = Calendar.getInstance();
				int year = rightNow.get(Calendar.YEAR);
				int month = rightNow.get(Calendar.MONTH)+1;
				int date = rightNow.get(Calendar.DATE);
				if(demographic.getFormattedDob()!=null) {
					String[] split_dob=demographic.getFormattedDob().split("-");
					int year_dob=Integer.parseInt(split_dob[0]);
					int month_dob = Integer.parseInt(split_dob[1]);
					int date_dob = Integer.parseInt(split_dob[2]);
					int age = year - year_dob;			
					if(month < month_dob) {
						age--;
					} else if(month==month_dob){
						if(date < date_dob)
							age--;
					}
					ocanStaffForm.setEstimatedAge(String.valueOf(age));
				}
		}
		
		return(ocanStaffForm);
	}
	
	/**
	 * input is ""/M/F/T/O
	 * 
	 * returns M/F/OTH/UNK/CDA
	 */
	public static String convertGender(String oscarGender) {
		if(oscarGender == null || oscarGender.equals("")) {
			return "UNK";
		}
		if(oscarGender.equals("T") || oscarGender.equals("O")) {
			return "OTH";
		}
		if(oscarGender.equals("M")) {
			return "M";
		}
		if(oscarGender.equals("F")) {
			return "F";
		}
		return "UNK";
	}
	
	public static List<OcanFormOption> getOcanFormOptions(String category)
	{
		List<OcanFormOption> results=ocanFormOptionDao.findByVersionAndCategory("1.2", category);
		return(results);
	}
	
	public static List<OcanConnexOption> getOcanConnexOrgOptions(String LHIN_code)
	{
		List<OcanConnexOption> results=ocanConnexOptionDao.findByLHINCode(LHIN_code);
		return(results);
	}
	
	public static List<OcanConnexOption> getOcanConnexProgramOptions(String LHIN_code, String orgName)
	{
		List<OcanConnexOption> results=ocanConnexOptionDao.findByLHINCodeOrgName(LHIN_code, orgName);
		return(results);
	}
	
	public static String getOcanConnexOrgNumber(String LHIN_code, String optionId)
	{
		//List<OcanConnexOption> results=ocanConnexOptionDao.findByLHINCodeOrgName(LHIN_code, orgName);
		//return(results.get(0).getOrgNumber());
		OcanConnexOption result = ocanConnexOptionDao.findByID(Integer.valueOf(optionId));
		return result.getOrgNumber();
	}
	
	public static String getOcanConnexProgramName(String LHIN_code, String optionId)
	{
		OcanConnexOption result = ocanConnexOptionDao.findByID(Integer.valueOf(optionId));
		return result.getProgramName();
	}
	
	public static String getOcanConnexProgramNumber(String LHIN_code, String optionId)
	{
		//List<OcanConnexOption> results=ocanConnexOptionDao.findByLHINCodeOrgNameProgramName(LHIN_code, orgName, programName);
		//return(results.get(0).getProgramNumber());
		OcanConnexOption result = ocanConnexOptionDao.findByID(Integer.valueOf(optionId));
		return result.getProgramNumber();
	}
	
	public static List<OcanStaffFormData> getStaffAnswers(Integer ocanStaffFormId, String question, int prepopulationLevel)
	{
		if(prepopulationLevel != OcanForm.PRE_POPULATION_LEVEL_ALL) {
			return(new ArrayList<OcanStaffFormData>()); 
		}
		if (ocanStaffFormId==null) return(new ArrayList<OcanStaffFormData>()); 
			
		return(ocanStaffFormDataDao.findByQuestion(ocanStaffFormId, question));
	}
	
	private static List<OcanStaffFormData> getClientAnswers(Integer ocanClientFormId, String question, int prepopulationLevel)
	{
		if(prepopulationLevel != OcanForm.PRE_POPULATION_LEVEL_ALL) {
			return(new ArrayList<OcanStaffFormData>()); 
		}
		
		if (ocanClientFormId==null) return(new ArrayList<OcanStaffFormData>()); 
			
		//return(ocanClientFormDataDao.findByQuestion(ocanClientFormId, question));
		return(ocanStaffFormDataDao.findByQuestion(ocanClientFormId, question));
	}
	
	public static String renderAsDate(Integer ocanStaffFormId, String question, boolean required, int prepopulationLevel)
	{
		return renderAsDate(ocanStaffFormId,question,required, prepopulationLevel, false);
	}
	
	/**
	 * This method is meant to return a bunch of html <option> tags for each list element.
	 */
	public static String renderAsDate(Integer ocanStaffFormId, String question, boolean required, int prepopulationLevel, boolean clientForm)
	{
		String value="", className="";
		if(!clientForm) {
			List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);
			if(existingAnswers.size()>0) {value = existingAnswers.get(0).getAnswer();}
		} else {
			List<OcanStaffFormData> existingAnswers=getClientAnswers(ocanStaffFormId, question, prepopulationLevel);
			if(existingAnswers.size()>0) {value = existingAnswers.get(0).getAnswer();}
		}
		if(required) {className="{validate: {required:true}}";}
		return "<input type=\"text\" value=\"" + value + "\" id=\""+question+"\" name=\""+question+"\" onfocus=\"this.blur()\" readonly=\"readonly\" class=\""+className+"\"/> <img title=\"Calendar\" id=\"cal_"+question+"\" src=\"../../images/cal.gif\" alt=\"Calendar\" border=\"0\"><script type=\"text/javascript\">Calendar.setup({inputField:'"+question+"',ifFormat :'%Y-%m-%d',button :'cal_"+question+"',align :'cr',singleClick :true,firstDay :1});</script><img src=\"../../images/icon_clear.gif\" border=\"0\"/ onclick=\"clearDate('"+question+"');\">";
	}
	
	public static String renderAsDate(Integer ocanStaffFormId, String question, boolean required, String defaultValue , int prepopulationLevel)
	{
		return renderAsDate(ocanStaffFormId, question, required, defaultValue,prepopulationLevel,false);
	}
	
	public static String renderAsDate(Integer ocanStaffFormId, String question, boolean required, String defaultValue, int prepopulationLevel, boolean clientForm)
	{
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);
		String value="", className="";
		if(existingAnswers.size()>0) {value = existingAnswers.get(0).getAnswer();}
		if(value.equals("")) {value =defaultValue;}
		if(required) {className="{validate: {required:true}}";}
		return "<input type=\"text\" value=\"" + value + "\" id=\""+question+"\" name=\""+question+"\" onfocus=\"this.blur()\" readonly=\"readonly\" class=\""+className+"\"/> <img title=\"Calendar\" id=\"cal_"+question+"\" src=\"../../images/cal.gif\" alt=\"Calendar\" border=\"0\"><script type=\"text/javascript\">Calendar.setup({inputField:'"+question+"',ifFormat :'%Y-%m-%d',button :'cal_"+question+"',align :'cr',singleClick :true,firstDay :1});</script><img src=\"../../images/icon_clear.gif\" border=\"0\"/ onclick=\"clearDate('"+question+"');\">";
	}	
	
	public static List<Admission> getAdmissions(Integer facilityId, Integer clientId) {
		return(admissionDao.getAdmissionsByFacility(clientId, facilityId));
	}

	public static String renderAsEstimatedAge(Integer ocanStaffFormId, String question, boolean required, String dob, int prepopulationLevel)
	{
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);
		String value="", className="";
		if(existingAnswers.size()>0) {value = existingAnswers.get(0).getAnswer();}
		if(value.equals("")) {			
			Calendar rightNow = Calendar.getInstance();
			int year = rightNow.get(Calendar.YEAR);
			int month = rightNow.get(Calendar.MONTH)+1;
			int date = rightNow.get(Calendar.DATE);
			String[] split_dob=dob.split("-");
			int year_dob=Integer.parseInt(split_dob[0]);
			int month_dob = Integer.parseInt(split_dob[1]);
			int date_dob = Integer.parseInt(split_dob[2]);
			int age = year - year_dob;			
			if(month < month_dob) {
				age--;
			} else if(month==month_dob){
				if(date < date_dob)
					age--;
			}
			value = String.valueOf(age);
		}
		if(required) {className="{validate: {required:true}}";}
		return "<input type=\"text\" value=\"" + value + "\" id=\""+question+"\" name=\""+question+"\" onfocus=\"this.blur()\" readonly=\"readonly\" class=\""+className+"\"/> ";
	}	
	
	public static String getEscapedAdmissionSelectionDisplay(Admission admission)
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append(admission.getProgramName());
		sb.append(" ( ");
		sb.append(DateFormatUtils.ISO_DATE_FORMAT.format(admission.getAdmissionDate()));
		sb.append(" - ");
		if (admission.getDischargeDate()==null) sb.append("current");
		else sb.append(DateFormatUtils.ISO_DATE_FORMAT.format(admission.getDischargeDate()));
		sb.append(" )");
		
		return(StringEscapeUtils.escapeHtml(sb.toString()));
	}
	
	
	public static String renderAsSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, int prepopulationLevel)
	{
		return renderAsSelectOptions(ocanStaffFormId,question, options, prepopulationLevel, false);
	}
	/**
	 * This method is meant to return a bunch of html <option> tags for each list element.
	 */
	public static String renderAsSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, int prepopulationLevel, boolean clientForm)
	{
		List<OcanStaffFormData> existingStaffAnswers=null;
		List<OcanStaffFormData> existingClientAnswers=null;
		if(!clientForm)
			existingStaffAnswers = getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);
		else
			existingClientAnswers = getClientAnswers(ocanStaffFormId, question, prepopulationLevel);

		StringBuilder sb=new StringBuilder();

		sb.append("<option value=\"\">Select an answer</option>");
		for (OcanFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getOcanDataCategoryName());
			//String lengthLimitedEscapedName=limitLengthAndEscape(option.getOcanDataCategoryName());
			String selected=null;
			if(!clientForm)
				selected=(OcanStaffFormData.containsAnswer(existingStaffAnswers, option.getOcanDataCategoryValue())?"selected=\"selected\"":"");
			else
				selected=(OcanStaffFormData.containsAnswer(existingClientAnswers, option.getOcanDataCategoryValue())?"selected=\"selected\"":"");
			
			sb.append("<option "+selected+" value=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"\" title=\""+htmlEscapedName+"\">"+htmlEscapedName+"</option>");
		}
		
		return(sb.toString());
	}
	
	
	public static String renderAsConnexOrgNameSelectOptions(Integer ocanStaffFormId, String question, List<OcanConnexOption> options, int prepopulationLevel)
	{
		return renderAsConnexOrgNameSelectOptions(ocanStaffFormId,question, options, prepopulationLevel, false);
	}
	public static String renderAsConnexOrgNameSelectOptions(Integer ocanStaffFormId, String question, List<OcanConnexOption> options, int prepopulationLevel, boolean clientForm)
	{	//save connex option Id in orgName to make it identified.
		List<OcanStaffFormData> existingStaffAnswers=null;		
		existingStaffAnswers = getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);
		String actualOrgName = null;
		if(existingStaffAnswers!=null && existingStaffAnswers.size()>0) {
			actualOrgName = existingStaffAnswers.get(0).getAnswer();
			
		}
		StringBuilder sb=new StringBuilder();

		sb.append("<option value=\"\">Select an answer</option>");
		for (OcanConnexOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getOrgName());
			
			String selected=null;
			
			//selected=(OcanStaffFormData.containsAnswer(existingStaffAnswers, option.getOrgName())?"selected=\"selected\"":"");
			//sb.append("<option "+selected+" value=\""+StringEscapeUtils.escapeHtml(option.getOrgName())+"\" title=\""+htmlEscapedName+"\">"+htmlEscapedName+"</option>");
			selected = String.valueOf(option.getId()).equalsIgnoreCase(actualOrgName)?"selected=\"selected\"":"";
			sb.append("<option "+selected+" value=\""+StringEscapeUtils.escapeHtml(String.valueOf(option.getId()))+"\" title=\""+htmlEscapedName+"\">"+htmlEscapedName+"</option>");
		}
		
		return(sb.toString());
	}
	
	
	
	public static String renderAsConnexProgramNameSelectOptions(Integer ocanStaffFormId, String question, List<OcanConnexOption> options, int prepopulationLevel)
	{
		return renderAsConnexProgramNameSelectOptions(ocanStaffFormId,question, options, prepopulationLevel, false);
	}
	public static String renderAsConnexProgramNameSelectOptions(Integer ocanStaffFormId, String question, List<OcanConnexOption> options, int prepopulationLevel, boolean clientForm)
	{
		List<OcanStaffFormData> existingStaffAnswers=null;
		List<OcanStaffFormData> existingClientAnswers=null;
		if(!clientForm)
			existingStaffAnswers = getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);
		else
			existingClientAnswers = getClientAnswers(ocanStaffFormId, question, prepopulationLevel);

		StringBuilder sb=new StringBuilder();

		sb.append("<option value=\"\">Select an answer</option>");
		for (OcanConnexOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getProgramName());
			
			String selected=null;
			if(!clientForm)
				selected=(OcanStaffFormData.containsAnswer(existingStaffAnswers, option.getProgramName())?"selected=\"selected\"":"");
			else
				selected=(OcanStaffFormData.containsAnswer(existingClientAnswers, option.getProgramName())?"selected=\"selected\"":"");
			
			sb.append("<option "+selected+" value=\""+StringEscapeUtils.escapeHtml(option.getProgramName())+"\" title=\""+htmlEscapedName+"\">"+htmlEscapedName+"</option>");
		}
		
		return(sb.toString());
	}
	
	public static String renderAsNumberOfReferralsSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options,  int prepopulationLevel)	
	{
		return renderAsNumbersSelectOptions(ocanStaffFormId, question, options, prepopulationLevel);
	}
	
	public static String renderAsMonthsSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options,  int prepopulationLevel)	
	{
		return renderAsNumbersSelectOptions(ocanStaffFormId, question, options, prepopulationLevel);
	}
	
	public static String renderAsYearsSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options,  int prepopulationLevel)	
	{
		return renderAsNumbersSelectOptions(ocanStaffFormId, question, options, prepopulationLevel);
	}
	
	public static String renderAsNumberOfCentresSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options,  int prepopulationLevel)	
	{
		return renderAsNumbersSelectOptions(ocanStaffFormId, question, options, prepopulationLevel);
	}
	
	public static String renderAsNumbersSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options,  int prepopulationLevel) {
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question ,prepopulationLevel);				
		Map<Integer , String> optionsMap = new TreeMap<Integer , String>();
		for (OcanFormOption option : options)
		{
			optionsMap.put(Integer.parseInt(option.getOcanDataCategoryValue()), option.getOcanDataCategoryName());
		}
		
		StringBuilder sb=new StringBuilder();

		for ( Integer key : optionsMap.keySet() ) 
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(optionsMap.get(key));			
			String selected=(OcanStaffFormData.containsAnswer(existingAnswers, optionsMap.get(key))?"selected=\"selected\"":"");

			sb.append("<option "+selected+" value=\""+StringEscapeUtils.escapeHtml(optionsMap.get(key))+"\" title=\""+htmlEscapedName+"\">"+htmlEscapedName+"</option>");
		
		}
		
		return(sb.toString());
	}
	
	
	public static String renderAsProvinceSelectOptions(OcanStaffForm ocanStaffForm)
	{
		String province = ocanStaffForm.getProvince();
		if(province==null) province = "ON";
		List<OcanFormOption> options = getOcanFormOptions("Province List");
		
		StringBuilder sb=new StringBuilder();

		for (OcanFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getOcanDataCategoryName());
			//String lengthLimitedEscapedName=limitLengthAndEscape(option.getOcanDataCategoryName());
			String selected=province.equals(option.getOcanDataCategoryValue())?"selected=\"selected\"":"";

			sb.append("<option "+selected+" value=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"\" title=\""+htmlEscapedName+"\">"+htmlEscapedName+"</option>");
		}
		
		return(sb.toString());
	}
	
	public static String renderAsAssessmentStatusSelectOptions(OcanStaffForm ocanStaffForm)
	{
		String assessmentStatus = ocanStaffForm.getAssessmentStatus();
		if(assessmentStatus==null) assessmentStatus = "In Progress";
		List<OcanFormOption> options = getOcanFormOptions("OCAN Assessment Status");
		
		StringBuilder sb=new StringBuilder();

		for (OcanFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getOcanDataCategoryName());
			//String lengthLimitedEscapedName=limitLengthAndEscape(option.getOcanDataCategoryName());
			String selected=assessmentStatus.equals(option.getOcanDataCategoryValue())?"selected=\"selected\"":"";

			sb.append("<option "+selected+" value=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"\" title=\""+htmlEscapedName+"\">"+htmlEscapedName+"</option>");
		}
		
		return(sb.toString());
	}
	
	
	public static String renderAsDomainSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, String[] valuesToInclude, int prepopulationLevel)
	{
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question ,prepopulationLevel);

		StringBuilder sb=new StringBuilder();

		for (OcanFormOption option : options)
		{
			if(valuesToInclude !=null && valuesToInclude.length>0) {
				boolean include=false;
				for(String inclValue:valuesToInclude) {
					if(option.getOcanDataCategoryValue().equals(inclValue)) {
						include=true;
						break;
					}
				}
				if(!include) {
					continue;
				}
			}
			
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getOcanDataCategoryName());
			//String lengthLimitedEscapedName=limitLengthAndEscape(option.getOcanDataCategoryName());
			String selected=(OcanStaffFormData.containsAnswer(existingAnswers, option.getOcanDataCategoryValue())?"selected=\"selected\"":"");

			sb.append("<option "+selected+" value=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"\" title=\""+htmlEscapedName+"\">"+htmlEscapedName+"</option>");
		}
		
		return(sb.toString());
	}
	
	public static String renderAsSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, String defaultValue, int prepopulationLevel)
	{
		return renderAsSelectOptions(ocanStaffFormId, question, options, defaultValue, prepopulationLevel, false);
	}
	/**
	 * This method is meant to return a bunch of html <option> tags for each list element.
	 */
	public static String renderAsSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, String defaultValue, int prepopulationLevel, boolean readonly)
	{
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);
		boolean useDefaultValue=false;
		if(existingAnswers.size()==0) {
			useDefaultValue=true;
		}
		StringBuilder sb=new StringBuilder();
		sb.append("<option value=\"\">Select an answer</option>");
		for (OcanFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getOcanDataCategoryName());
			//String lengthLimitedEscapedName=limitLengthAndEscape(option.getOcanDataCategoryName());
			String selected="";
			if(!useDefaultValue)
				selected=(OcanStaffFormData.containsAnswer(existingAnswers, option.getOcanDataCategoryValue())?"selected=\"selected\"":"");
			else {
				if(option.getOcanDataCategoryValue().equals(defaultValue)) {
					selected="selected=\"selected\"";
				}
			}

			sb.append("<option "+selected+" value=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"\" title=\""+htmlEscapedName+"\">"+htmlEscapedName+"</option>");
		}
		
		return(sb.toString());
	}	
	
	
	public static String renderAsTextArea(Integer ocanStaffFormId, String question, int rows, int cols, int prepopulationLevel)
	{
		return renderAsTextArea(ocanStaffFormId, question, rows, cols, prepopulationLevel, false);
	}
	
	public static String renderAsTextArea(Integer ocanStaffFormId, String question, int rows, int cols, int prepopulationLevel, boolean clientForm)
	{
		List<OcanStaffFormData> existingAnswers= null;
		List<OcanStaffFormData> existingClientAnswers=null;

		StringBuilder sb=new StringBuilder();

		sb.append("<textarea maxlength=\"512\" name=\""+question+"\" id=\""+question+"\" rows=\"" + rows + "\" cols=\"" + cols + "\">");

		if(!clientForm) {
			existingAnswers=getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);
			if(existingAnswers.size()>0) {
				sb.append(existingAnswers.get(0).getAnswer());
			}	
		} else { 
			existingClientAnswers=getClientAnswers(ocanStaffFormId, question, prepopulationLevel);
			if(existingClientAnswers.size()>0) {
				sb.append(existingClientAnswers.get(0).getAnswer());
			}	
		}
		
		
		sb.append("</textarea>");
		return(sb.toString());
	}
	
	public static String renderAsSoATextArea(Integer ocanStaffFormId, String question, int rows, int cols, int prepopulationLevel)
	{
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question,prepopulationLevel);

		StringBuilder sb=new StringBuilder();

		sb.append("<textarea maxlength=\"512\" name=\""+question+"\" id=\""+question+"\" rows=\"" + rows + "\" cols=\"" + cols + "\" readonly=\"readonly\" onfocus=\"this.blur()\">");
		if(existingAnswers.size()>0) {
			sb.append(existingAnswers.get(0).getAnswer());
		}
		sb.append("</textarea>");
		return(sb.toString());
	}
	
	public static String renderAsOrgProgramNumberTextField(Integer ocanStaffFormId, String question, String answer, int size, int prepopulationLevel)
	{
		return renderAsOrgProgramNumberTextField(ocanStaffFormId, question, answer, size, prepopulationLevel, false);
	}
	
	public static String renderAsOrgProgramNumberTextField(Integer ocanStaffFormId, String question, String answer, int size, int prepopulationLevel, boolean clientForm)
	{
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);

		String value = "";
		if(existingAnswers.size()>0) {
			value = existingAnswers.get(0).getAnswer();
		}
		StringBuilder sb=new StringBuilder();

		sb.append("<input type=\"text\" name=\""+question+"\" id=\""+question+"\" readonly size=\"" + size + "\" value=\""+answer+"\"/>");
		
		return(sb.toString());
	}
	
	public static String renderAsTextField(Integer ocanStaffFormId, String question, int size, int prepopulationLevel, String styleClass)
	{
		return renderAsTextField(ocanStaffFormId, question, size, prepopulationLevel, false, styleClass);
	}
	
	public static String renderAsTextField(Integer ocanStaffFormId, String question, int size, int prepopulationLevel)
	{
		return renderAsTextField(ocanStaffFormId, question, size, prepopulationLevel, false, null);
	}
	
	public static String renderAsTextField(Integer ocanStaffFormId, String question, int size, int prepopulationLevel, boolean clientForm, String styleClass)
	{
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);

		String value = "";
		if(existingAnswers.size()>0) {
			value = existingAnswers.get(0).getAnswer();
		}
		StringBuilder sb=new StringBuilder();

		sb.append("<input type=\"text\" name=\""+question+"\" "+(styleClass!=null?"class=\""+styleClass+"\" ":"")+"id=\""+question+"\" size=\"" + size + "\" maxlength=\"" + size +"\" value=\""+value+"\"/>");
		
		return(sb.toString());
	}
	
	public static String renderAsTextField(Integer ocanStaffFormId, String question, String defaultValue, int size, int prepopulationLevel)
	{
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);

		String value = "";
		if(existingAnswers.size()>0) {
			value = existingAnswers.get(0).getAnswer();
		} else {
			value = defaultValue;
		}
		StringBuilder sb=new StringBuilder();

		sb.append("<input type=\"text\" name=\""+question+"\" id=\""+question+"\" size=\"" + size + "\" maxlength=\"" + size +"\" value=\""+value+"\"/>");
		
		return(sb.toString());
	}
	
	public static String renderAsTextFieldReadOnly(Integer ocanStaffFormId, String question, int size, int prepopulationLevel, boolean readonly, String defaultValue)
	{
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);		
		
		String value = "";
		if(existingAnswers.size()>0) {
			value = existingAnswers.get(0).getAnswer();
		}
		if(value==null || "".equals(value)) {
			//fill with default data from demographic, e.g. name, DOB, gender, HIN, hin version, phone, address
			value=defaultValue;
		}
		
		StringBuilder sb=new StringBuilder();
		if(readonly) 
			sb.append("<input type=\"text\" name=\""+question+"\" id=\""+question+ "\" readonly=\"readonly\" " +   "\" size=\"" + size + "\" maxlength=\"" + size +"\" value=\""+value+"\"/>");
		else 
			sb.append("<input type=\"text\" name=\""+question+"\" id=\""+question+  "\" size=\"" + size + "\" maxlength=\"" + size +"\" value=\""+value+"\"/>");
		
		return(sb.toString());
	}
	
	public static String renderAsCheckBoxOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, int prepopulationLevel)
	{
		return renderAsCheckBoxOptions(ocanStaffFormId, question,options,prepopulationLevel, false);
	}
	
	public static String renderAsCheckBoxOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, int prepopulationLevel, boolean clientForm)
	{
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);
 
		StringBuilder sb=new StringBuilder();

		for (OcanFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getOcanDataCategoryName());
			//String lengthLimitedEscapedName=limitLengthAndEscape(option.getOcanDataCategoryName());
			String checked=(OcanStaffFormData.containsAnswer(existingAnswers, option.getOcanDataCategoryValue())?"checked=\"checked\"":"");
				
			sb.append("<div title=\""+htmlEscapedName+"\"><input type=\"checkBox\" "+checked+" name=\""+question+"\" value=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"\" /> "+htmlEscapedName+"</div>");
		}
		
		return(sb.toString());
	}
	
	public static String renderLegalStatusOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, int prepopulationLevel, boolean clientForm)
	{
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);
 
		StringBuilder sb=new StringBuilder();

		Map<String,OcanFormOption> optionMap = new HashMap<String,OcanFormOption>();
		for (OcanFormOption option : options)
		{
			optionMap.put(option.getOcanDataCategoryValue(), option);
		}
		
		
		sb.append("<h4>Pre-Charge</h4>");
		renderSingleCheckbox(optionMap.get("013-01"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-02"),sb,question,existingAnswers);
		sb.append("<h4>Pre-Trial</h4>");
		renderSingleCheckbox(optionMap.get("013-03"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-04"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-05"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-06"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-07"),sb,question,existingAnswers);
		sb.append("<h4>Custody Status</h4>");
		renderSingleCheckbox(optionMap.get("013-17"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-18"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-19"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-20"),sb,question,existingAnswers);
		sb.append("<h4>Outcomes</h4>");
		renderSingleCheckbox(optionMap.get("013-08"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-09"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-10"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-11"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-12"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-13"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-14"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-15"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("013-16"),sb,question,existingAnswers);
		sb.append("<h4>Other</h4>");
		renderSingleCheckbox(optionMap.get("013-21"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("UNK"),sb,question,existingAnswers);
		renderSingleCheckbox(optionMap.get("CDA"),sb,question,existingAnswers);
		
		
		return(sb.toString());
	}
	
	public static void renderSingleCheckbox(OcanFormOption option,StringBuilder sb, String question, List<OcanStaffFormData> existingAnswers) {
		String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getOcanDataCategoryName());			
		String checked=(OcanStaffFormData.containsAnswer(existingAnswers, option.getOcanDataCategoryValue())?"checked=\"checked\"":"");
			
		sb.append("<div title=\""+htmlEscapedName+"\"><input type=\"checkBox\" "+checked+" name=\""+question+"\" value=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"\" /> "+htmlEscapedName+"</div>");
	}
	
	public static String renderAsHiddenField(Integer ocanStaffFormId, String question, int prepopulationLevel)
	{
		return renderAsHiddenField(ocanStaffFormId, question, prepopulationLevel, false);
	}
	
	public static String renderAsHiddenField(Integer ocanStaffFormId, String question, int prepopulationLevel, boolean clientForm)
	{
		List<OcanStaffFormData> existingAnswers=getStaffAnswers(ocanStaffFormId, question, prepopulationLevel);

		String value = "";
		if(existingAnswers.size()>0) {
			value = existingAnswers.get(0).getAnswer();
		}
		StringBuilder sb=new StringBuilder();

		sb.append("<input type=\"hidden\" name=\""+question+"\" id=\""+question+"\" value=\""+value+"\"/>");
		
		return(sb.toString());
	}
	
	
	
	
	///client form//////
	
	public static OcanClientForm getOcanClientForm(Integer facilityId,Integer clientId, int prepopulationLevel)
	{
		OcanClientForm ocanClientForm=null;
		
		if(prepopulationLevel != OcanForm.PRE_POPULATION_LEVEL_NONE) {
			ocanClientForm = ocanClientFormDao.findLatestByFacilityClient(facilityId, clientId);
		}

		if (ocanClientForm==null)
		{
			ocanClientForm=new OcanClientForm();

			if(prepopulationLevel != OcanForm.PRE_POPULATION_LEVEL_NONE) {
				Demographic demographic=demographicDao.getDemographicById(clientId);
				ocanClientForm.setLastName(demographic.getLastName());
				ocanClientForm.setFirstName(demographic.getFirstName());				
				ocanClientForm.setDateOfBirth(demographic.getFormattedDob());
			}
		}
		
		return(ocanClientForm);
	}
	
	/*
	public static String renderAsDrugUseCheckBoxOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, int prepopulationLevel)
	{
		return renderAsDrugUseCheckBoxOptions(ocanStaffFormId, question,options,prepopulationLevel, false);
	}
	
	public static String renderAsDrugUseCheckBoxOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, int prepopulationLevel, boolean clientForm)
	{
		 
		StringBuilder sb=new StringBuilder();
		sb.append("<table width=\"100%\">");
		sb.append("<tr><td></td><td>Past 6 Months</td><td>Ever</td></tr>");
		for (OcanFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getOcanDataCategoryName());
			String value = option.getOcanDataCategoryValue(); //drug id
			
			List<OcanStaffFormData> freqMnthAnswer = getStaffAnswers(ocanStaffFormId, value+"_freq_6months", prepopulationLevel);
			List<OcanStaffFormData> freqEverAnswer = getStaffAnswers(ocanStaffFormId, value+"_freq_ever", prepopulationLevel);
			
			String checked2=((freqMnthAnswer.size()>0)?"checked=\"checked\"":"");
			String checked3=((freqEverAnswer.size()>0)?"checked=\"checked\"":"");
				
			sb.append("<tr><td>"+htmlEscapedName+"</td><td><input drugfreq=\"true\" type=\"checkBox\" "+checked2+" name=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"_freq_6months\" value=\"true\" /></td><td><input drugfreq=\"true\" type=\"checkBox\" "+checked3+" name=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"_freq_ever\" value=\true\" /></td></tr>");
		}
		
		sb.append(renderAsDrugInjectionCheckBoxOptions(ocanStaffFormId,question,options,prepopulationLevel,clientForm));
		sb.append("</table>");
		return(sb.toString());
	}

	
	public static String renderAsDrugInjectionCheckBoxOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, int prepopulationLevel, boolean clientForm)
	{
		 
		StringBuilder sb=new StringBuilder();
		
	
		List<OcanStaffFormData> freqMnthAnswer = getStaffAnswers(ocanStaffFormId, "drug_injection_freq_6months", prepopulationLevel);
		List<OcanStaffFormData> freqEverAnswer = getStaffAnswers(ocanStaffFormId, "drug_injection_freq_ever", prepopulationLevel);
		
		String checked2=((freqMnthAnswer.size()>0)?"checked=\"checked\"":"");
		String checked3=((freqEverAnswer.size()>0)?"checked=\"checked\"":"");
			
		sb.append("<tr><td>Has the substance been injected?</td><td><input druginjection=\"true\" type=\"checkBox\" "+checked2+" name=\"drug_injection_freq_6months\" value=\"true\" /></td><td><input druginjection=\"true\" type=\"checkBox\" "+checked3+" name=\"drug_injection_freq_ever\" value=\true\" /></td></tr>");
	
		
		return(sb.toString());
	}
	*/
	
	public static String renderAsDrugUseCheckBoxOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, int prepopulationLevel)
	{
		return renderAsDrugUseCheckBoxOptions(ocanStaffFormId, question,options,prepopulationLevel, false);
	}
	
	public static String renderAsDrugUseCheckBoxOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, int prepopulationLevel, boolean clientForm)
	{
		 
		StringBuilder sb=new StringBuilder();
		sb.append("<table width=\"100%\">");
		sb.append("<tr><td></td><td>Past 6 Months</td><td>Ever</td></tr>");
		for (OcanFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getOcanDataCategoryName());
			String value = option.getOcanDataCategoryValue(); //drug id
			
			List<OcanStaffFormData> freqMnthAnswer = getStaffAnswers(ocanStaffFormId, value+"_freq_6months", prepopulationLevel);
			List<OcanStaffFormData> freqEverAnswer = getStaffAnswers(ocanStaffFormId, value+"_freq_ever", prepopulationLevel);
			
			String checked2=((freqMnthAnswer.size()>0)?"checked=\"checked\"":"");
			String checked3=((freqEverAnswer.size()>0)?"checked=\"checked\"":"");
			/*
			List<OcanStaffFormData> freqAnswer = getStaffAnswers(ocanStaffFormId, value+"_DrugUseFreq", prepopulationLevel);
			Iterator it = freqAnswer.iterator();
			while(it.hasNext()) {
				OcanStaffFormData data = (OcanStaffFormData)it.next();
				checked2=((data.getAnswer().equals("5"))?"checked":"");
				checked3=((data.getAnswer().equals("6"))?"checked":"");
			}	
			*/
			sb.append("<tr><td>"+htmlEscapedName+"</td><td><input type=\"checkbox\" "+checked2+" id=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"_freq_6months\" name=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"_freq_6months\" value=\"5\" /></td><td><input type=\"checkbox\" "+checked3+" id=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"_freq_ever\" name=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"_freq_ever\" value=\"6\" /></td></tr>");
		}
		
		sb.append(renderAsDrugInjectionCheckBoxOptions(ocanStaffFormId,question,options,prepopulationLevel,clientForm));
		sb.append("</table>");
		return(sb.toString());
	}

	
	public static String renderAsDrugInjectionCheckBoxOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, int prepopulationLevel, boolean clientForm)
	{
		 
		StringBuilder sb=new StringBuilder();
		/*
		String checked2 = null;
		String checked3 = null;
		
		List<OcanStaffFormData> freqAnswer = getStaffAnswers(ocanStaffFormId, "drug_injection_freq", prepopulationLevel);
		Iterator it = freqAnswer.iterator();
		while(it.hasNext()) {
			OcanStaffFormData data = (OcanStaffFormData) it.next();
			checked2=((data.getAnswer().equals("5"))?"checked":"");
			checked3=((data.getAnswer().equals("6"))?"checked":"");
		}
		*/
		List<OcanStaffFormData> freqMnthAnswer = getStaffAnswers(ocanStaffFormId, "drug_injection_freq_6months", prepopulationLevel);
		List<OcanStaffFormData> freqEverAnswer = getStaffAnswers(ocanStaffFormId, "drug_injection_freq_ever", prepopulationLevel);
		
		String checked2=((freqMnthAnswer.size()>0)?"checked=\"checked\"":"");
		String checked3=((freqEverAnswer.size()>0)?"checked=\"checked\"":"");
		
		sb.append("<tr><td>Has the substance been injected?</td><td><input type=\"checkbox\" "+checked2+" id=\"drug_injection_freq_6months\" name=\"drug_injection_freq_6months\" value=\"5\" /></td><td><input type=\"checkbox\" "+checked3+" id=\"drug_injection_freq_ever\" name=\"drug_injection_freq_ever\" value=\"6\" /></td></tr>");
	
		
		return(sb.toString());
	}

	public static Date getAssessmentCompletionDate(Date completionDate, Date clientCompletionDate) {
		if(clientCompletionDate==null || completionDate.after(clientCompletionDate)) {
			return completionDate;
		} else {
			return clientCompletionDate;
		}
	}
	
	public static Date getAssessmentStartDate(Date startDate, Date clientStartDate) {
		if(clientStartDate==null || startDate.before(clientStartDate)) {
			return startDate;
		} else {
			return clientStartDate;
		}
	}	
	
	public static boolean afterCurrentDateAddMonth(Date date1, int month) {
		Calendar cal = Calendar.getInstance(); 
		cal.add(Calendar.MONTH,month);
		if(cal.getTime().after(date1)) {
			return true;
		} else {
			return false;
		}
			
	}
	
	public static boolean canCreateInitialAssessment(Integer facilityId,Integer clientId) {
		
		boolean result = false;
		
		OcanStaffForm ocanStaffForm = ocanStaffFormDao.findLatestCompletedInitialOcan(facilityId,clientId);	
		if(ocanStaffForm!=null) {						
			OcanStaffForm ocanStaffForm1 = ocanStaffFormDao.findLatestCompletedDischargedAssessment(facilityId, clientId);
			if(ocanStaffForm1!=null) {
				Date completionDate = getAssessmentCompletionDate(ocanStaffForm1.getCompletionDate(),ocanStaffForm1.getClientCompletionDate());
				result = afterCurrentDateAddMonth(completionDate, -3);				
			}
		} else {
			result = true;
		}
		
		return result;
	}

	public static boolean haveInitialAssessment(Integer facilityId,Integer clientId) {
		
		boolean result = false;
		
		OcanStaffForm ocanStaffForm = ocanStaffFormDao.findLatestCompletedInitialOcan(facilityId,clientId);	
		if(ocanStaffForm!=null) {						
			result = true;
		} 
		
		return result;
	}

	
	 public static boolean haveReassessment(Integer facilityId,Integer clientId) {

         boolean result = false;

         OcanStaffForm ocanStaffForm = ocanStaffFormDao.findLatestCompletedReassessment(facilityId,clientId);
         if(ocanStaffForm!=null) {
                 result = true;
         }

         return result;
	 }


	 
	public static String getOcanWarningMessage(Integer facilityId) {
		boolean appendFailureMessage = true;	
		StringBuilder messages = new StringBuilder();
		int doReassessment = 0;
		
	
		List<Integer> demographicList = ocanStaffFormDao.findClientsWithOcan(facilityId);
		for(Integer clientId: demographicList) {
			if(isItTimeToDoReassessment(facilityId, clientId)){
				if(appendFailureMessage) {
					messages.append("You need to do OCAN reassessment for the clients whose IDs are: ");
					appendFailureMessage = false;
				}
				messages.append(clientId+" , ");
				doReassessment ++;
			}		
		}
		if(doReassessment>0) {
			messages.delete(messages.length()-3, messages.length());
			return messages.toString();
		} else {
			return null;
		}
	}
	
	public static boolean isItTimeToDoReassessment(Integer facilityId,Integer clientId) {
		
		boolean result = false;
		
		Object[] ocanStaffForm = ocanStaffFormDao.findLatestCompletedReassessment_startDates(facilityId,clientId);
		Object[] ocanStaffForm1 = ocanStaffFormDao.findLatestCompletedInitialOcan_startDates(facilityId,clientId);	
		Object[] ocanStaffForm2 = ocanStaffFormDao.findLatestCompletedFormStartDates(facilityId, clientId);
		
		OcanStaffFormData formData;	
		
		if(ocanStaffForm2!=null){
			//If exit date exists, it means the client was discharged from the functional centre. Don't have to do OCAN again.
			formData = ocanStaffFormDataDao.findLatestByQuestion((Integer)ocanStaffForm2[2], "serviceUseRecord_exitDate1");
			if(formData!=null) {
				if(formData.getAnswer()!=null)
					return false;
			}	
		}		
		
		Date startDate = null;
		if(ocanStaffForm!=null) {
			formData = ocanStaffFormDataDao.findLatestByQuestion((Integer)ocanStaffForm[2], "serviceUseRecord_exitDate1");
			if(formData!=null) {
				if(formData.getAnswer()!=null)
					return false;
			}
			startDate = OcanForm.getAssessmentStartDate((Date)ocanStaffForm[0],(Date)ocanStaffForm[1]);
		} else if(ocanStaffForm1!=null) {	
			formData = ocanStaffFormDataDao.findLatestByQuestion((Integer)ocanStaffForm1[2], "serviceUseRecord_exitDate1");
			if(formData!=null) {
				if(formData.getAnswer()!=null)
					return false;
			}
			startDate = OcanForm.getAssessmentStartDate((Date)ocanStaffForm1[0],(Date)ocanStaffForm1[1]);			
		} else {
			return result;
		}
		
		return OcanForm.afterCurrentDateAddMonth(startDate, -6);		
			
	}
	
	public static OcanStaffForm getLastCompletedOcanForm(Integer facilityId,Integer clientId)
	{
		OcanStaffForm ocanStaffForm = ocanStaffFormDao.getLastCompletedOcanForm(facilityId, clientId);
		
		return ocanStaffForm;
	}		
	
	
	 public static OcanStaffForm getLastCompletedOcanFormByOcanType(Integer facilityId,Integer clientId, String ocanType)
     {
         OcanStaffForm ocanStaffForm = ocanStaffFormDao.getLastCompletedOcanFormByOcanType(facilityId, clientId, ocanType);

         return ocanStaffForm;
     }



	public static List<OcanStaffFormData> getOcanFormDataByFormId(Integer ocanStaffFormId) {
		return ocanStaffFormDataDao.findByForm(ocanStaffFormId);		
	}
	
	public static List<Admission> getServiceAndBedProgramAdmissions(Integer facilityId,Integer clientId) {
		return (admissionDao.getServiceAndBedProgramAdmissions(clientId, facilityId));
	}
	
	public static OcanStaffForm findLatestCbiFormsByFacilityAdmissionId(Integer facilityId,Integer admissionId, Boolean signed) {
		return (ocanStaffFormDao.findLatestCbiFormsByFacilityAdmissionId(facilityId, admissionId, signed));
	}
	
}
