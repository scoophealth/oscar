package org.oscarehr.PMmodule.service;

import java.util.List;

public interface FormsManager {

	public void saveForm(Object o);
	public Object getCurrentForm(String clientId, Class clazz);
	public List getFormInfo(String clientId,Class clazz);
}
