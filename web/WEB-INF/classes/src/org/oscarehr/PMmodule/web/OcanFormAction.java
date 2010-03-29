package org.oscarehr.PMmodule.web;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.OcanClientFormDao;
import org.oscarehr.common.dao.OcanClientFormDataDao;
import org.oscarehr.common.dao.OcanStaffFormDao;
import org.oscarehr.common.dao.OcanStaffFormDataDao;
import org.oscarehr.common.model.OcanClientForm;
import org.oscarehr.common.model.OcanClientFormData;
import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.common.model.OcanStaffFormData;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class OcanFormAction {
	
	private static OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
	private static OcanStaffFormDataDao ocanStaffFormDataDao = (OcanStaffFormDataDao) SpringUtils.getBean("ocanStaffFormDataDao");

	private static OcanClientFormDao ocanClientFormDao = (OcanClientFormDao) SpringUtils.getBean("ocanClientFormDao");
	private static OcanClientFormDataDao ocanClientFormDataDao = (OcanClientFormDataDao) SpringUtils.getBean("ocanClientFormDataDao");

	
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
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		ocanStaffForm.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
		ocanStaffForm.setProviderName(loggedInInfo.loggedInProvider.getFormattedName());
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
	
	
	
	
	public static OcanClientForm createOcanClientForm(Integer clientId)
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		
		OcanClientForm ocanClientForm=new OcanClientForm();		
		ocanClientForm.setOcanFormVersion("1.2");		
		ocanClientForm.setClientId(clientId);
		ocanClientForm.setFacilityId(loggedInInfo.currentFacility.getId());
		ocanClientForm.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
		
		
		return(ocanClientForm);
	}
	
	public static void saveOcanClientForm(OcanClientForm ocanClientForm) {
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		ocanClientForm.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
		ocanClientForm.setProviderName(loggedInInfo.loggedInProvider.getFormattedName());
		
		ocanClientFormDao.persist(ocanClientForm);
	}
	
	public static void addOcanClientFormData(Integer ocanClientFormId, String question, String answer)
	{
		answer=StringUtils.trimToNull(answer);
		if (answer==null) return;
		
		OcanClientFormData ocanClientFormData=new OcanClientFormData();
		
		ocanClientFormData.setOcanClientFormId(ocanClientFormId);
		ocanClientFormData.setQuestion(question);
		ocanClientFormData.setAnswer(answer);
		
		ocanClientFormDataDao.persist(ocanClientFormData);
	}
}
