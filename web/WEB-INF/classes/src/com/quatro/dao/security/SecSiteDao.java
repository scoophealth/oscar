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
