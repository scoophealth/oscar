package org.oscarehr.PMmodule.web;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.common.Gender;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.CdsData;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CdsForm4 {

	private static AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");

	private CdsData cdsData=null;
	
	public static CdsForm4 makeNewCds(Integer clientId) {
		CdsForm4 cdsForm=new CdsForm4();
		cdsForm.cdsData=new CdsData();
		
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

	public CdsData getCdsData()
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
}
