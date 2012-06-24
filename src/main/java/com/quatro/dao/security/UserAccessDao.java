/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */
package com.quatro.dao.security;

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
