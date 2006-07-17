package org.caisi.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.ProviderDao;
import org.caisi.PMmodule.model.Provider;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProviderDaoHibernate  extends HibernateDaoSupport 
implements ProviderDao 
{
	 private String dbTable = " Provider  p ";

	 private Log log = LogFactory.getLog(ProviderDaoHibernate.class);

//###############################################################################
	 
public Provider getProvider(String providerNo)
{
	if(providerNo == null  ||  providerNo.length() <= 0)
	{
		return null;
	}
	Provider provider = (Provider)getHibernateTemplate().get(Provider.class, providerNo);
		
	return provider;
}

//###############################################################################
public String getProviderName(String providerNo)
{
	Provider provider = getProvider(providerNo);
	String providerName = "";
	
	if(provider != null  &&  provider.getFirstName() != null)
	{
		providerName = provider.getFirstName() + " ";
	}
	
	if(provider != null  &&  provider.getLastName() != null)
	{
		providerName += provider.getLastName();
	}
		
	return  providerName;
}
//###############################################################################
public List getProviders()
{
	List rs = getHibernateTemplate().find(" FROM  " + dbTable);
	
	return rs;
}

//###############################################################################
public List getProvidersInfo()
{
	String queryStr = " SELECT  p.ProviderNo, p.LastName, p.FirstName  FROM " + dbTable;
	List rs = getHibernateTemplate().find(queryStr);
	
	return rs;
}

//#################################################################################	

public boolean addProvider(Provider provider)
{
	  
    if(provider == null)
    {
  	  return false;
    }
      	
    try
    {
    	getHibernateTemplate().save(provider);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;
}

//#################################################################################	

public boolean updateProvider(Provider provider)
{
	  
    if(provider == null)
    {
  	  return false;
    }
      	
    try
    {
    	getHibernateTemplate().update(provider);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;
    
}


//################################################################################

public boolean removeProvider(String providerNo)
{
    Object provider = getHibernateTemplate().load(Provider.class, providerNo);
    
    try
    {
    	getHibernateTemplate().delete(provider);
    }
    catch(Exception ex)
    {
    	return false;
    }

    return true;
}

//	###############################################################################

	public List search(String name) {
		Criteria c = this.getSession().createCriteria(Provider.class);
		c.add(Restrictions.or(Expression.like("FirstName",name+"%"),Expression.like("LastName",name+"%")));
		c.addOrder(Order.asc("ProviderNo"));
		return c.list();
	}
}
