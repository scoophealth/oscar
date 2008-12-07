package com.quatro.dao.security;

import java.util.ArrayList;
import java.util.List;

import com.quatro.model.ReportValue;
import com.quatro.model.security.*;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
public class SecSiteDao extends HibernateDaoSupport {

	  public void Save(SecSiteValue secSiteVal)
	  {
	      getHibernateTemplate().saveOrUpdate(secSiteVal);
	  }
	  
	  public SecSiteValue getSecSiteValue(String siteId)
	  {
		  SecSiteValue ssv = null; 
		  String sql = "from SecSiteValue where siteId=?";
		  List list = getHibernateTemplate().find(sql , new Object[] {siteId});
		  if (list.size() != 0) ssv = (SecSiteValue) list.get(0); 
		  return ssv;
	  }
}
