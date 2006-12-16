package org.oscarehr.PMmodule.dao;

import java.util.List;

public interface FormsDAO {

	public void saveForm(Object o);
	public Object getCurrentForm(String clientId, Class clazz);
	public List getFormInfo(String clientId,Class clazz);
}
