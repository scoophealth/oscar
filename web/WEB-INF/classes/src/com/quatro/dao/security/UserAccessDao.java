package com.quatro.dao.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
public class UserAccessDao extends HibernateDaoSupport {
	  public List GetUserAccessList(String providerNo)
	  {
          ArrayList paramList = new ArrayList();
		  String sSQL="from UserAccessValue s where s.providerNo= ? order by s.functionCd, s.privilege desc, s.orgCd";		
	      paramList.add(providerNo);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);
	      return getHibernateTemplate().find(sSQL ,params);
	  }
}
