package org.oscarehr.PMmodule.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.FormsDAO;
import org.oscarehr.PMmodule.model.FormInfo;
import org.oscarehr.PMmodule.model.Provider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class FormsDAOHibernate extends HibernateDaoSupport implements FormsDAO {

	private Log log = LogFactory.getLog(FormsDAOHibernate.class);

	public void saveForm(Object o) {
		this.getHibernateTemplate().save(o);
		
		if(log.isDebugEnabled()) {
			log.debug("saveForm:" + o);
		}
	}

	public Object getCurrentForm(String clientId, Class clazz) {
		Object result = null;
		
		if(clientId == null || clazz == null) {
			throw new IllegalArgumentException();
		}
		
		String className = clazz.getName();
		if(className.indexOf(".") != -1) {
			className = className.substring(className.lastIndexOf(".")+1);
		}
		List results = this.getHibernateTemplate().find("from " + className + " f where f.DemographicNo=" + clientId);
		if(results.size()>0) {
			result =  results.get(0);
		}
		
		if(log.isDebugEnabled()) {
			log.debug("getCurrentForm: clientId=" + clientId + ",class=" + clazz + ",found=" + (result!=null));
		}
		
		return result;
	}

	public List getFormInfo(String clientId,Class clazz) {
		if(clientId == null || clazz == null) {
			throw new IllegalArgumentException();
		}
		
		List formInfos = new ArrayList();
		String className = clazz.getName();
		if(className.indexOf(".") != -1) {
			className = className.substring(className.lastIndexOf(".")+1);
		}
		List results = this.getHibernateTemplate().find("select f.id,f.ProviderNo,f.FormEdited from " + className + " f where f.DemographicNo=? order by f.FormEdited DESC",Long.valueOf(clientId));
		for(Iterator iter=results.iterator();iter.hasNext();) {
			FormInfo fi = new FormInfo();
			Object[] values = (Object[])iter.next();
			Long id = (Long)values[0];
			Long providerNo = (Long)values[1];
			Date dateEdited = (Date)values[2];
			Provider provider = (Provider)this.getHibernateTemplate().get(Provider.class,String.valueOf(providerNo));
			fi.setFormId(id);
			fi.setProviderNo(providerNo);
			fi.setFormDate(dateEdited);
			fi.setProviderName(provider.getFormattedName());
			formInfos.add(fi);
		}
		
		if(log.isDebugEnabled()) {
			log.debug("getFormInfo: clientId=" + clientId + ",class=" + clazz + ",# of results=" + formInfos.size());
		}
		
		return formInfos;
	}
}
