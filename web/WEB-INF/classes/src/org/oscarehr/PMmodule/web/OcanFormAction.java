package org.oscarehr.PMmodule.web;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.OcanStaffFormDao;
import org.oscarehr.common.dao.OcanStaffFormDataDao;
import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.common.model.OcanStaffFormData;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class OcanFormAction {
	
	private static OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
	private static OcanStaffFormDataDao ocanStaffFormDataDao = (OcanStaffFormDataDao) SpringUtils.getBean("ocanStaffFormDataDao");

	public static OcanStaffForm createOcanStaffForm(Integer admissionId, Integer clientId, boolean signed)
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		
		OcanStaffForm ocanStaffForm=new OcanStaffForm();
		ocanStaffForm.setAdmissionId(admissionId);
		ocanStaffForm.setOcanFormVersion("1.2");		
		ocanStaffForm.setClientId(clientId);
		ocanStaffForm.setFacilityId(loggedInInfo.currentFacility.getId());
		ocanStaffForm.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
		ocanStaffForm.setSigned(signed);
		
		
		return(ocanStaffForm);
	}
	
	public static void saveOcanStaffForm(OcanStaffForm ocanStaffForm) {
		ocanStaffFormDao.persist(ocanStaffForm);
	}
	
	public static void addOcanStaffFormData(Integer ocanStaffFormId, String question, String answer)
	{
		answer=StringUtils.trimToNull(answer);
		if (answer==null) return;
		
		OcanStaffFormData ocanStaffFormData=new OcanStaffFormData();
		
		ocanStaffFormData.setOcanStaffFormId(ocanStaffFormId);
		ocanStaffFormData.setQuestion(question);
		ocanStaffFormData.setAnswer(answer);
		
		ocanStaffFormDataDao.persist(ocanStaffFormData);
	}
}
