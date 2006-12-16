package org.oscarehr.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Provider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProviderDaoHibernate extends HibernateDaoSupport implements ProviderDao {
	private Log log = LogFactory.getLog(ProviderDaoHibernate.class);
	
	public boolean providerExists(String providerNo) {
		boolean exists = (((Integer) getHibernateTemplate().iterate("select count(*) from Provider p where p.ProviderNo = " + providerNo).next()) == 1);
		log.debug("providerExists: " + exists);

		return exists;
	}

	public Provider getProvider(String providerNo) {
		if (providerNo == null || providerNo.length() <= 0) {
			throw new IllegalArgumentException();
		}

		Provider provider = (Provider) getHibernateTemplate().get(Provider.class, providerNo);

		if (log.isDebugEnabled()) {
			log.debug("getProvider: providerNo=" + providerNo + ",found=" + (provider != null));
		}

		return provider;
	}

	public String getProviderName(String providerNo) {
		if (providerNo == null || providerNo.length() <= 0) {
			throw new IllegalArgumentException();
		}

		Provider provider = getProvider(providerNo);
		String providerName = "";

		if (provider != null && provider.getFirstName() != null) {
			providerName = provider.getFirstName() + " ";
		}

		if (provider != null && provider.getLastName() != null) {
			providerName += provider.getLastName();
		}

		if (log.isDebugEnabled()) {
			log.debug("getProviderName: providerNo=" + providerNo + ",result=" + providerName);
		}

		return providerName;
	}

	public List getProviders() {
		List rs = getHibernateTemplate().find("FROM  Provider p ORDER BY p.LastName");

		if (log.isDebugEnabled()) {
			log.debug("getProviders: # of results=" + rs.size());
		}
		return rs;
	}

	public List search(String name) {
		Criteria c = this.getSession().createCriteria(Provider.class);
		c.add(Restrictions.or(Expression.like("FirstName", name + "%"), Expression.like("LastName", name + "%")));
		c.addOrder(Order.asc("ProviderNo"));

		List results = c.list();

		if (log.isDebugEnabled()) {
			log.debug("search: # of results=" + results.size());
		}
		return results;
	}

	public List getProvidersByType(String type) {
		List results = this.getHibernateTemplate().find("from Provider p where p.ProviderType = ?", type);

		if (log.isDebugEnabled()) {
			log.debug("getProvidersByType: type=" + type + ",# of results=" + results.size());
		}

		return results;
	}
}
