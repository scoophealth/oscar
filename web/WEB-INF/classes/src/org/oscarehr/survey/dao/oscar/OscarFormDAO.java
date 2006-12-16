package org.oscarehr.survey.dao.oscar;

import java.util.List;

import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormInstance;

public interface OscarFormDAO {
	public void saveOscarForm(OscarForm form);
	public void updateStatus(Long formId, Short status);
	public OscarForm getOscarForm(Long formId);
	
	
	public void saveOscarFormInstance(OscarFormInstance instance);
	public void saveOscarFormData(OscarFormData data);
	public OscarFormInstance getOscarFormInstance(Long formId, Long clientId);
	public List getOscarForms(Long formId, Long clientId);
	public List getOscarFormsByClientId(Long clientId);
	
}
