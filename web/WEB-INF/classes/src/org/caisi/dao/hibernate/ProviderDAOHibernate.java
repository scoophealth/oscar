package org.caisi.dao.hibernate;

import java.util.List;

import org.caisi.dao.ProviderDAO;
import org.oscarehr.PMmodule.model.Provider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation for the corresponding DAO interface 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class ProviderDAOHibernate extends HibernateDaoSupport implements
		ProviderDAO {

	public List getProviders() {
		return getHibernateTemplate().find("from Provider p order by p.lastName");
	}

	public Provider getProvider(String provider_no) {
		return (Provider)getHibernateTemplate().get(Provider.class,provider_no);
	}
	
	public Provider getProviderByName(String lastName,String firstName) {
		return (Provider)getHibernateTemplate().find("from Provider p where p.first_name = ? and p.last_name = ?", new Object[] {firstName,lastName}).get(0);
	}
	
}
