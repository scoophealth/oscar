package org.oscarehr.PMmodule.web;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.CdsClientFormDao;
import org.oscarehr.common.dao.CdsClientFormDataDao;
import org.oscarehr.common.model.CdsClientForm;
import org.oscarehr.common.model.CdsClientFormData;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class CdsForm4Action {

	private static CdsClientFormDao cdsClientFormDao = (CdsClientFormDao) SpringUtils.getBean("cdsClientFormDao");
	private static CdsClientFormDataDao cdsClientFormDataDao = (CdsClientFormDataDao) SpringUtils.getBean("cdsClientFormDataDao");

	public static CdsClientForm createCdsClientForm(Integer admissionId, Integer clientAge, Integer clientId, boolean signed)
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		
		CdsClientForm cdsClientForm=new CdsClientForm();
		cdsClientForm.setAdmissionId(admissionId);
		cdsClientForm.setCdsFormVersion("4");
		cdsClientForm.setClientAge(clientAge);
		cdsClientForm.setClientId(clientId);
		cdsClientForm.setFacilityId(loggedInInfo.currentFacility.getId());
		cdsClientForm.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
		cdsClientForm.setSigned(signed);
		cdsClientFormDao.persist(cdsClientForm);
		
		return(cdsClientForm);
	}
	
	public static void addCdsClientFormData(Integer cdsClientFormId, String question, String answer)
	{
		answer=StringUtils.trimToNull(answer);
		if (answer==null) return;
		
		CdsClientFormData cdsClientFormData=new CdsClientFormData();
		
		cdsClientFormData.setCdsClientFormId(cdsClientFormId);
		cdsClientFormData.setQuestion(question);
		cdsClientFormData.setAnswer(answer);
		
		cdsClientFormDataDao.persist(cdsClientFormData);
	}
}
