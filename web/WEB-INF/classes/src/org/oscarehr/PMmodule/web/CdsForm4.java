package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.common.dao.CdsClientFormDao;
import org.oscarehr.common.dao.CdsClientFormDataDao;
import org.oscarehr.common.dao.CdsFormOptionDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.CdsClientForm;
import org.oscarehr.common.model.CdsClientFormData;
import org.oscarehr.common.model.CdsFormOption;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CdsForm4 {

	private static AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static CdsFormOptionDao cdsFormOptionDao = (CdsFormOptionDao) SpringUtils.getBean("cdsFormOptionDao");
	private static CdsClientFormDao cdsClientFormDao = (CdsClientFormDao) SpringUtils.getBean("cdsClientFormDao");
	private static CdsClientFormDataDao cdsClientFormDataDao = (CdsClientFormDataDao) SpringUtils.getBean("cdsClientFormDataDao");

	private static final int MAX_DISPLAY_NAME_LENGTH=60;

	public static CdsClientForm getCdsClientForm(Integer clientId)
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		
		CdsClientForm cdsClientForm=cdsClientFormDao.findLatestByFacilityClient(loggedInInfo.currentFacility.getId(), clientId);

		if (cdsClientForm==null)
		{
			cdsClientForm=new CdsClientForm();
			
			Demographic demographic=demographicDao.getDemographicById(clientId);
			
	        try {
	        	cdsClientForm.setClientAge(MiscUtils.calculateAge(demographic.getBirthDay()));
	        } catch (Exception e) {
		        // it's okay leave it as null, no known age.
	        }	        
		}
		
		return(cdsClientForm);
	}
	
	public static List<Admission> getAdmissions(Integer clientId) {
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		
		return(admissionDao.getAdmissionsByFacility(clientId, loggedInInfo.currentFacility.getId()));
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
	
	public static List<CdsFormOption> getCdsFormOptions(String category)
	{
		List<CdsFormOption> results=cdsFormOptionDao.findByVersionAndCategory("4", category);
		return(results);
	}
	
	/**
	 * This method is meant to return a bunch of html <option> tags for each list element.
	 */
	public static String renderAsSelectOptions(Integer cdsClientFormId, String question, List<CdsFormOption> options)
	{
		List<CdsClientFormData> existingAnswers=getAnswers(cdsClientFormId, question);

		StringBuilder sb=new StringBuilder();

		for (CdsFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getCdsDataCategoryName());
			String lengthLimitedEscapedName=limitLengthAndEscape(option.getCdsDataCategoryName());
			String selected=(CdsClientFormData.containsAnswer(existingAnswers, option.getCdsDataCategory())?"selected=\"selected\"":"");

			sb.append("<option "+selected+" value=\""+StringEscapeUtils.escapeHtml(option.getCdsDataCategory())+"\" title=\""+htmlEscapedName+"\">"+lengthLimitedEscapedName+"</option>");
		}
		
		return(sb.toString());
	}

	/**
	 * This method is meant to return a bunch of html <option> tags for each list element.
	 */
	public static String renderNumbersAsSelectOptions(Integer cdsClientFormId, String question, int maxNumber)
	{
		List<CdsClientFormData> existingAnswers=getAnswers(cdsClientFormId, question);

		StringBuilder sb=new StringBuilder();

		for (int i=-1; i<maxNumber; i++)
		{
			String value=String.valueOf(i);
			String label=value;
			
			if (i==-1) 
			{
				value="";
				label="Unknown / refused to answer";
			}
			
			if (i==0) 
			{
				label="none";
			}
			
			String selected=(CdsClientFormData.containsAnswer(existingAnswers, value)?"selected=\"selected\"":"");
			
			sb.append("<option "+selected+" value=\""+value+"\" title=\""+label+"\">"+label+"</option>");
		}
		
		return(sb.toString());
	}

	/**
	 * This method is meant to return a bunch of html <input type="radio"> tags for each list element on a separate line (br delimited).
	 */
	public static String renderAsRadioOptions(Integer cdsClientFormId, String question, List<CdsFormOption> options)
	{
		List<CdsClientFormData> existingAnswers=getAnswers(cdsClientFormId, question);
		
		StringBuilder sb=new StringBuilder();

		for (CdsFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getCdsDataCategoryName());
			String lengthLimitedEscapedName=limitLengthAndEscape(option.getCdsDataCategoryName());
			String selected=(CdsClientFormData.containsAnswer(existingAnswers, option.getCdsDataCategory())?"checked=\"checked\"":"");
				
			sb.append("<div title=\""+htmlEscapedName+"\"><input type=\"radio\" "+selected+" name=\""+question+"\" value=\""+StringEscapeUtils.escapeHtml(option.getCdsDataCategory())+"\" /> "+lengthLimitedEscapedName+"</div>");
		}
		
		return(sb.toString());
	}

	/**
	 * This method is meant to return a bunch of html <input type="checkbox"> tags for each list element on a separate line (br delimited).
	 */
	public static String renderAsCheckBoxOptions(Integer cdsClientFormId, String question, List<CdsFormOption> options)
	{
		List<CdsClientFormData> existingAnswers=getAnswers(cdsClientFormId, question);
 
		StringBuilder sb=new StringBuilder();

		for (CdsFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getCdsDataCategoryName());
			String lengthLimitedEscapedName=limitLengthAndEscape(option.getCdsDataCategoryName());
			String checked=(CdsClientFormData.containsAnswer(existingAnswers, option.getCdsDataCategory())?"checked=\"checked\"":"");
				
			sb.append("<div title=\""+htmlEscapedName+"\"><input type=\"checkBox\" "+checked+" name=\""+question+"\" value=\""+StringEscapeUtils.escapeHtml(option.getCdsDataCategory())+"\" /> "+lengthLimitedEscapedName+"</div>");
		}
		
		return(sb.toString());
	}
	
	public static String limitLengthAndEscape(String s)
	{
		if (s.length()>MAX_DISPLAY_NAME_LENGTH) s=s.substring(0, MAX_DISPLAY_NAME_LENGTH-3)+"...";
		return(StringEscapeUtils.escapeHtml(s));
	}
	
	private static List<CdsClientFormData> getAnswers(Integer cdsClientFormId, String question)
	{
		if (cdsClientFormId==null) return(new ArrayList<CdsClientFormData>()); 
			
		return(cdsClientFormDataDao.findByQuestion(cdsClientFormId, question));
	}
}
