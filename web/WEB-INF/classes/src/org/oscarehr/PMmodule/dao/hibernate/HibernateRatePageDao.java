package org.oscarehr.PMmodule.dao.hibernate;

import java.util.List;

import org.oscarehr.PMmodule.dao.RatePageDao;
import org.oscarehr.PMmodule.model.RatePage;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateRatePageDao extends HibernateDaoSupport implements
		RatePageDao 
{

	public void rate(String pageName, int score) 
	{
		List rs = getScoreByName(pageName);

		if (rs.size() > 1) 
		{
			throw new RuntimeException("More than 1 records found.");
		}

		if (rs.size() == 0) 
		{
			RatePage rp = new RatePage();
			rp.setPageName(pageName);
			rp.setScore(new Integer(score));
			rp.setVisitors(new Integer(1));
			getHibernateTemplate().save(rp);
		} 
		else if (rs.size() == 1) 
		{
			RatePage rp = (RatePage) rs.get(0);
			rp.setScore(new Integer(rp.getScore().intValue()+score));
			rp.setVisitors(new Integer(rp.getVisitors().intValue()+1));
			getHibernateTemplate().update(rp);
		}
	}

	public List getScoreByName(String pageName) 
	{
		List rs = getHibernateTemplate().find(
				"FROM RatePage r WHERE r.pageName=?", pageName);
		return rs;
	}
	
	public List getAllRecord() {
		List rs = getHibernateTemplate().find(
				"FROM RatePage r");
		return rs;
	}

}
