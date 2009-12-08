/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * UserPropertyDAO.java
 *
 * Created on December 19, 2007, 4:29 PM
 *
 *
 *
 */

package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Site;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 
 * @author Victor Weng
 */
public class SiteDao extends HibernateDaoSupport {

	/** Creates a new instance of UserPropertyDAO */
	public SiteDao() {
	}

	public void save(Site s) {
		boolean isUpdate = s.getSiteId() != null && s.getSiteId() > 0;
		if (isUpdate) {
			Site old = (Site) getHibernateTemplate().get(Site.class,
					s.getSiteId());
			if (!old.getName().equals(s.getName())) {
				// site name changed, need to update all references as it serves as PK
				// so we need to update the tables that references to the site
				Session sess = getSession();
				try {
					sess.createSQLQuery(
									"update rschedule set avail_hour = replace(avail_hour, :oldname, :newname) ")
							.setParameter("oldname", ">"+old.getName()+"<")
							.setParameter("newname", ">"+s.getName()+"<").executeUpdate();
					sess.createSQLQuery(
									"update scheduledate set reason = :newname where reason = :oldname ")
							.setParameter("oldname", old.getName())
							.setParameter("newname", s.getName()).executeUpdate();
					sess.createSQLQuery(
									"update appointment set location = :newname where location = :oldname ")
							.setParameter("oldname", old.getName())
							.setParameter("newname", s.getName()).executeUpdate();
					sess.createSQLQuery(
									"update billing_on_cheader1 set clinic = :newname where clinic = :oldname ")
							.setParameter("oldname", old.getName())
							.setParameter("newname", s.getName()).executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						sess.close();
					} catch (HibernateException e) {
						e.printStackTrace();
					}
				}
						
			}
		}

		getHibernateTemplate().merge(s);

	}

	public List<Site> getAllSites() {
		List rs = getHibernateTemplate().find("from Site s order by s.name");
		return rs;
	}

	public List<Site> getAllActiveSites() {
		List rs = getHibernateTemplate().find(
				"from Site s where s.status=1 order by s.name");
		return rs;
	}

	public List<Site> getActiveSitesByProviderNo(String provider_no) {
		Provider p = (Provider) getHibernateTemplate().get(Provider.class,
				provider_no);

		List<Site> rs = new ArrayList(p.getSites());
		Iterator<Site> it = rs.iterator();
		while (it.hasNext()) {
			Site site = (Site) it.next();
			// remove inactive sites
			if (site.getStatus() == 0)
				it.remove();
		}

		Collections.sort(rs, new Comparator<Site>() {
			public int compare(Site o1, Site o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		return rs;
	}

	public Site getById(Integer id) {
		return (Site) getHibernateTemplate().get(Site.class, id);
	}
	
	public Site getByLocation(String location) {
		List<Site> rs = getHibernateTemplate().find(
				"from Site s where s.name=?", location);
		if (rs.size()>0)
			return rs.get(0);
		else
			return null;
	}

}
