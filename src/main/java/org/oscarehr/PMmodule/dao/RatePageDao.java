/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.PMmodule.model.RatePage;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class RatePageDao extends HibernateDaoSupport {

    public void rate(String pageName, int score) {
        List rs = getScoreByName(pageName);

        if (rs.size() > 1) {
            throw new RuntimeException("More than 1 records found.");
        }

        if (rs.size() == 0) {
            RatePage rp = new RatePage();
            rp.setPageName(pageName);
            rp.setScore(new Integer(score));
            rp.setVisitors(new Integer(1));
            getHibernateTemplate().save(rp);
        }
        else if (rs.size() == 1) {
            RatePage rp = (RatePage)rs.get(0);
            rp.setScore(new Integer(rp.getScore().intValue() + score));
            rp.setVisitors(new Integer(rp.getVisitors().intValue() + 1));
            getHibernateTemplate().update(rp);
        }
    }

    public List getScoreByName(String pageName) {
        List rs = getHibernateTemplate().find("FROM RatePage r WHERE r.pageName=?", pageName);
        return rs;
    }

    public List getAllRecord() {
        List rs = getHibernateTemplate().find("FROM RatePage r");
        return rs;
    }

}
