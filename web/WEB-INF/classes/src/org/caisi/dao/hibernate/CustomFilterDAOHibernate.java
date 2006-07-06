package org.caisi.dao.hibernate;

import java.util.List;

import org.caisi.dao.CustomFilterDAO;
import org.caisi.model.CustomFilter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation for the corresponding DAO interface 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class CustomFilterDAOHibernate extends HibernateDaoSupport implements
		CustomFilterDAO {

	public CustomFilter getCustomFilter(String name) {
		List results = getHibernateTemplate().find("from CustomFilter c where c.name = ?",new Object[]{name});
		if(results.size()>0) {
			return(CustomFilter)results.get(0);
		} else {
			return null;
		}
	}

	public void saveCustomFilter(CustomFilter filter) {
		getHibernateTemplate().saveOrUpdate(filter);
	}
	
	public List getCustomFilters() {
		return getHibernateTemplate().find("from CustomFilter");
	}
	
	public List getCustomFilters(String provider_no) {
		return getHibernateTemplate().find("from CustomFilter cf where cf.provider_no = ?",new Object[] {provider_no});
	}

	public void deleteCustomFilter(String name) {
		CustomFilter filter = getCustomFilter(name);
		getHibernateTemplate().delete(filter);
	}
}
