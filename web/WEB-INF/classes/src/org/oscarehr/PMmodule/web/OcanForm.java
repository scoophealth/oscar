package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.OcanFormOptionDao;
import org.oscarehr.common.dao.OcanStaffFormDao;
import org.oscarehr.common.dao.OcanStaffFormDataDao;
import org.oscarehr.common.model.CdsClientFormData;
import org.oscarehr.common.model.CdsFormOption;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.OcanFormOption;
import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.common.model.OcanStaffFormData;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class OcanForm {
	
	private static AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static OcanFormOptionDao ocanFormOptionDao = (OcanFormOptionDao) SpringUtils.getBean("ocanFormOptionDao");
	private static OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
	private static OcanStaffFormDataDao ocanStaffFormDataDao = (OcanStaffFormDataDao) SpringUtils.getBean("ocanStaffFormDataDao");
	
	public static Demographic getDemographic(String demographicId)
	{
		return demographicDao.getDemographic(demographicId);
	}
	
	public static OcanStaffForm getOcanStaffForm(Integer clientId)
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		
		OcanStaffForm ocanStaffForm=ocanStaffFormDao.findLatestByFacilityClient(loggedInInfo.currentFacility.getId(), clientId);

		if (ocanStaffForm==null)
		{
			ocanStaffForm=new OcanStaffForm();
			ocanStaffForm.setAddressLine2("");
			
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
			
	        try {
	        	ocanStaffForm.setClientAge(MiscUtils.calculateAge(demographic.getBirthDay()));
	        } catch (Exception e) {
		        // it's okay leave it as null, no known age.
	        }	        
		}
		
		return(ocanStaffForm);
	}
	
	public static List<OcanFormOption> getOcanFormOptions(String category)
	{
		List<OcanFormOption> results=ocanFormOptionDao.findByVersionAndCategory("1.2", category);
		return(results);
	}
	
	private static List<OcanStaffFormData> getAnswers(Integer ocanStaffFormId, String question)
	{
		if (ocanStaffFormId==null) return(new ArrayList<OcanStaffFormData>()); 
			
		return(ocanStaffFormDataDao.findByQuestion(ocanStaffFormId, question));
	}
	
	/**
	 * This method is meant to return a bunch of html <option> tags for each list element.
	 */
	public static String renderAsDate(Integer ocanStaffFormId, String question, boolean required)
	{
		List<OcanStaffFormData> existingAnswers=getAnswers(ocanStaffFormId, question);
		String value="", className="";
		if(existingAnswers.size()>0) {value = existingAnswers.get(0).getAnswer();}
		if(required) {className="{validate: {required:true}}";}
		return "<input type=\"text\" value=\"" + value + "\" id=\""+question+"\" name=\""+question+"\" onfocus=\"this.blur()\" readonly=\"readonly\" class=\""+className+"\"/> <img title=\"Calendar\" id=\"cal_"+question+"\" src=\"../../images/cal.gif\" alt=\"Calendar\" border=\"0\"><script type=\"text/javascript\">Calendar.setup({inputField:'"+question+"',ifFormat :'%Y-%m-%d',button :'cal_"+question+"',align :'cr',singleClick :true,firstDay :1});</script>";
	}
	
	public static String renderAsDate(Integer ocanStaffFormId, String question, boolean required, String defaultValue)
	{
		List<OcanStaffFormData> existingAnswers=getAnswers(ocanStaffFormId, question);
		String value="", className="";
		if(existingAnswers.size()>0) {value = existingAnswers.get(0).getAnswer();}
		if(value.equals("")) {value =defaultValue;}
		if(required) {className="{validate: {required:true}}";}
		return "<input type=\"text\" value=\"" + value + "\" id=\""+question+"\" name=\""+question+"\" onfocus=\"this.blur()\" readonly=\"readonly\" class=\""+className+"\"/> <img title=\"Calendar\" id=\"cal_"+question+"\" src=\"../../images/cal.gif\" alt=\"Calendar\" border=\"0\"><script type=\"text/javascript\">Calendar.setup({inputField:'"+question+"',ifFormat :'%Y-%m-%d',button :'cal_"+question+"',align :'cr',singleClick :true,firstDay :1});</script>";
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
	
	/**
	 * This method is meant to return a bunch of html <option> tags for each list element.
	 */
	public static String renderAsSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options)
	{
		List<OcanStaffFormData> existingAnswers=getAnswers(ocanStaffFormId, question);

		StringBuilder sb=new StringBuilder();

		for (OcanFormOption option : options)
		{
			String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getOcanDataCategoryName());
			//String lengthLimitedEscapedName=limitLengthAndEscape(option.getOcanDataCategoryName());
			String selected=(OcanStaffFormData.containsAnswer(existingAnswers, option.getOcanDataCategoryValue())?"selected=\"selected\"":"");

			sb.append("<option "+selected+" value=\""+StringEscapeUtils.escapeHtml(option.getOcanDataCategoryValue())+"\" title=\""+htmlEscapedName+"\">"+htmlEscapedName+"</option>");
		}
		
		return(sb.toString());
	}
	
	public static String renderAsDomainSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, String[] valuesToInclude)
	{
		List<OcanStaffFormData> existingAnswers=getAnswers(ocanStaffFormId, question);

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
	
	/**
	 * This method is meant to return a bunch of html <option> tags for each list element.
	 */
	public static String renderAsSelectOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options, String defaultValue)
	{
		List<OcanStaffFormData> existingAnswers=getAnswers(ocanStaffFormId, question);
		boolean useDefaultValue=false;
		if(existingAnswers.size()==0) {
			useDefaultValue=true;
		}
		StringBuilder sb=new StringBuilder();

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
	
	public static String renderAsTextArea(Integer ocanStaffFormId, String question, int rows, int cols)
	{
		List<OcanStaffFormData> existingAnswers=getAnswers(ocanStaffFormId, question);

		StringBuilder sb=new StringBuilder();

		sb.append("<textarea name=\""+question+"\" id=\""+question+"\" rows=\"" + rows + "\" cols=\"" + cols + "\">");
		if(existingAnswers.size()>0) {
			sb.append(existingAnswers.get(0).getAnswer());
		}
		sb.append("</textarea>");
		return(sb.toString());
	}
	
	public static String renderAsSoATextArea(Integer ocanStaffFormId, String question, int rows, int cols)
	{
		List<OcanStaffFormData> existingAnswers=getAnswers(ocanStaffFormId, question);

		StringBuilder sb=new StringBuilder();

		sb.append("<textarea name=\""+question+"\" id=\""+question+"\" rows=\"" + rows + "\" cols=\"" + cols + "\" readonly=\"readonly\" onfocus=\"this.blur()\">");
		if(existingAnswers.size()>0) {
			sb.append(existingAnswers.get(0).getAnswer());
		}
		sb.append("</textarea>");
		return(sb.toString());
	}
	
	public static String renderAsTextField(Integer ocanStaffFormId, String question, int size)
	{
		List<OcanStaffFormData> existingAnswers=getAnswers(ocanStaffFormId, question);

		String value = "";
		if(existingAnswers.size()>0) {
			value = existingAnswers.get(0).getAnswer();
		}
		StringBuilder sb=new StringBuilder();

		sb.append("<input type=\"text\" name=\""+question+"\" id=\""+question+"\" size=\"" + size + "\" value=\""+value+"\"/>");
		
		return(sb.toString());
	}
	
	public static String renderAsCheckBoxOptions(Integer ocanStaffFormId, String question, List<OcanFormOption> options)
	{
		List<OcanStaffFormData> existingAnswers=getAnswers(ocanStaffFormId, question);
 
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
	
	public static String renderAsHiddenField(Integer ocanStaffFormId, String question)
	{
		List<OcanStaffFormData> existingAnswers=getAnswers(ocanStaffFormId, question);

		String value = "";
		if(existingAnswers.size()>0) {
			value = existingAnswers.get(0).getAnswer();
		}
		StringBuilder sb=new StringBuilder();

		sb.append("<input type=\"hidden\" name=\""+question+"\" id=\""+question+"\" value=\""+value+"\"/>");
		
		return(sb.toString());
	}
}
