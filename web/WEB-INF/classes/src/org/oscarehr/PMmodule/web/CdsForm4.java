package org.oscarehr.PMmodule.web;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.common.Gender;
import org.oscarehr.common.dao.CdsFormOptionDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.CdsClientData;
import org.oscarehr.common.model.CdsFormOption;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CdsForm4 {

	private static AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static CdsFormOptionDao cdsFormOptionDao = (CdsFormOptionDao) SpringUtils.getBean("cdsFormOptionDao");

	private static final int MAX_DISPLAY_NAME_LENGTH=60;
	
	private CdsClientData cdsData=null;
	
	public static CdsForm4 makeNewCds(Integer clientId) {
		CdsForm4 cdsForm=new CdsForm4();
		cdsForm.cdsData=new CdsClientData();
		
		Demographic demographic=demographicDao.getDemographicById(clientId);
		
		try {
	        cdsForm.cdsData.setClientGender(Gender.valueOf(demographic.getSex()));
        } catch (Exception e) {
	        // it's okay leave it as null, no known gender.
        }

        try {
	        cdsForm.cdsData.setClientAge(MiscUtils.calculateAge(demographic.getBirthDay()));
        } catch (Exception e) {
	        // it's okay leave it as null, no known gender.
        }
        
        return(cdsForm);
	}

	public CdsClientData getCdsData()
	{
		return(cdsData);
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
	public static String renderAsSelectOptions(List<CdsFormOption> options)
	{
		StringBuilder sb=new StringBuilder();

		for (CdsFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getCdsDataCategoryName());
			String lengthLimitedEscapedName=limitLengthAndEscape(option.getCdsDataCategoryName());

			sb.append("<option value=\""+StringEscapeUtils.escapeHtml(option.getCdsDataCategory())+"\" title=\""+htmlEscapedName+"\">"+lengthLimitedEscapedName+"</option>");
		}
		
		return(sb.toString());
	}

	/**
	 * This method is meant to return a bunch of html <input type="radio"> tags for each list element on a separate line (br delimited).
	 */
	public static String renderAsRadioOptions(String radioName, List<CdsFormOption> options)
	{
		StringBuilder sb=new StringBuilder();

		for (CdsFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getCdsDataCategoryName());
			String lengthLimitedEscapedName=limitLengthAndEscape(option.getCdsDataCategoryName());
				
			sb.append("<div title=\""+htmlEscapedName+"\"><input type=\"radio\" name=\""+radioName+"\" value=\""+StringEscapeUtils.escapeHtml(option.getCdsDataCategory())+"\" /> "+lengthLimitedEscapedName+"</div>");
		}
		
		return(sb.toString());
	}

	/**
	 * This method is meant to return a bunch of html <input type="checkbox"> tags for each list element on a separate line (br delimited).
	 */
	public static String renderAsCheckBoxOptions(String checkBoxName, List<CdsFormOption> options)
	{
		StringBuilder sb=new StringBuilder();

		for (CdsFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getCdsDataCategoryName());
			String lengthLimitedEscapedName=limitLengthAndEscape(option.getCdsDataCategoryName());
				
			sb.append("<div title=\""+htmlEscapedName+"\"><input type=\"checkBox\" name=\""+checkBoxName+"\" value=\""+StringEscapeUtils.escapeHtml(option.getCdsDataCategory())+"\" /> "+lengthLimitedEscapedName+"</div>");
		}
		
		return(sb.toString());
	}
	
	private static String limitLengthAndEscape(String s)
	{
		if (s.length()>MAX_DISPLAY_NAME_LENGTH) s=s.substring(0, MAX_DISPLAY_NAME_LENGTH-3)+"...";
		return(StringEscapeUtils.escapeHtml(s));
	}
}
