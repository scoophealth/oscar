/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.dao.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
public class UserAccessDao extends HibernateDaoSupport {
	  public List GetUserAccessList(String providerNo, Integer shelterId)
	  {
		  String sSQL="";
		  if (shelterId != null && shelterId.intValue()>0) {
			  String s = "'%S" + shelterId.toString() + ",%'";
			  sSQL = "from UserAccessValue s where s.providerNo= ? " +
		  		" and s.orgCdcsv like " + s + " order by s.functionCd, s.privilege desc, s.orgCd";
		  }
		  else
		  {
			  sSQL = "from UserAccessValue s where s.providerNo= ? " +
		  		" order by s.functionCd, s.privilege desc, s.orgCd";
		  }
	      return getHibernateTemplate().find(sSQL ,providerNo);
	  }
	  
	  public List GetUserOrgAccessList(String providerNo, Integer shelterId)
	  {
		  String sSQL="";
		  if (shelterId != null && shelterId.intValue() > 0) {
			  sSQL="select distinct o.codecsv from UserAccessValue s, LstOrgcd o " +
			  		"where s.providerNo= ? and s.privilege>='r' and s.orgCd=o.code " +
			  		" and o.codecsv like '%S" + shelterId.toString() + ",%'" +
			  		" order by o.codecsv";	
			  return getHibernateTemplate().find(sSQL ,providerNo);
		  }
		  else
		  {
			  sSQL="select distinct o.codecsv from UserAccessValue s, LstOrgcd o where s.providerNo= ? and s.privilege>='r' and s.orgCd=o.code order by o.codecsv";			  
			  return getHibernateTemplate().find(sSQL ,providerNo);
		  }
	  }
}
