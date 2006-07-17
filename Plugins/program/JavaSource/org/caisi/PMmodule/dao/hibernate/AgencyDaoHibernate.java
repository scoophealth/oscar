package org.caisi.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.AgencyDao;
import org.caisi.PMmodule.model.Agency;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class AgencyDaoHibernate  extends HibernateDaoSupport 
implements AgencyDao 
{
	 private Log log = LogFactory.getLog(AgencyDaoHibernate.class);

//###############################################################################
	 
public Agency getAgency(String agencyId)
{
	if(agencyId == null  ||  agencyId.length() <= 0)
	{
		return null;
	}
	Agency agency = (Agency)getHibernateTemplate().get(Agency.class, Long.valueOf(agencyId));
		
	return agency;
}

public Agency getLocalAgency()
{
	List results = getHibernateTemplate().find("from Agency a where a.Local = true");
	if(results.size() > 0) {
		return (Agency)results.get(0);
	}
	return null;
}

public void saveAgency(Agency agency)
{
	//if(agency.isLocal()) {
		this.getHibernateTemplate().saveOrUpdate(agency);
	//} else {
	//	log.warn("Attempted to save non-local agency");
	//}
}

	public List getAgencies() {
		return this.getHibernateTemplate().find("from Agency");
	}
	
	public void deleteLocalAgency() {
		Agency agency = getLocalAgency();
		this.getHibernateTemplate().delete(agency);
	}
}
