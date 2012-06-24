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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.BedCheckTime;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BedCheckTimeDAO extends HibernateDaoSupport {

    private static final Logger log=MiscUtils.getLogger();

    public boolean bedCheckTimeExists(Integer programId, Date time) {
        List bedCheckTimes = getHibernateTemplate().find("from BedCheckTime where programId = ? and time = ?", new Object[] {programId, time});
        log.debug("bedCheckTimeExists: " + (bedCheckTimes.size() > 0));

        return bedCheckTimes.size() > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.oscarehr.PMmodule.dao.BedCheckTimeDAO#getBedCheckTime(java.lang.Integer)
     */
    public BedCheckTime getBedCheckTime(Integer id) {
        BedCheckTime bedCheckTime = getHibernateTemplate().get(BedCheckTime.class, id);
        log.debug("getBedCheckTime: id " + id);

        return bedCheckTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.oscarehr.PMmodule.dao.BedCheckTimeDAO#getBedCheckTimes(java.lang.Integer)
     */
    @SuppressWarnings("unchecked")
    public BedCheckTime[] getBedCheckTimes(Integer programId) {
        String query = getBedCheckTimesQuery(programId);
        Object[] values = getBedCheckTimesValues(programId);
        List bedCheckTimes = getBedCheckTimes(query, values);
        log.debug("getBedCheckTimes: size " + bedCheckTimes.size());

        return (BedCheckTime[])bedCheckTimes.toArray(new BedCheckTime[bedCheckTimes.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.oscarehr.PMmodule.dao.BedCheckTimeDAO#saveBedCheckTime(org.oscarehr.PMmodule.model.BedCheckTime)
     */
    public void saveBedCheckTime(BedCheckTime bedCheckTime) {
        getHibernateTemplate().saveOrUpdate(bedCheckTime);
        getHibernateTemplate().flush();
        getHibernateTemplate().refresh(bedCheckTime);

        log.debug("saveBedCheckTime: id " + bedCheckTime.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.oscarehr.PMmodule.dao.BedCheckTimeDAO#deleteBedCheckTime(org.oscarehr.PMmodule.model.BedCheckTime)
     */
    public void deleteBedCheckTime(BedCheckTime bedCheckTime) {
        getHibernateTemplate().delete(bedCheckTime);
        getHibernateTemplate().flush();

        log.debug("deleteBedCheckTime: " + bedCheckTime);
    }

    String getBedCheckTimesQuery(Integer programId) {
        StringBuilder queryBuilder = new StringBuilder("from BedCheckTime bct");

        if (programId != null) {
            queryBuilder.append(" where ");
        }

        if (programId != null) {
            queryBuilder.append("bct.programId = ?");
        }

        queryBuilder.append(" order by bct.time asc");

        return queryBuilder.toString();
    }

    Object[] getBedCheckTimesValues(Integer programId) {
        List<Object> values = new ArrayList<Object>();

        if (programId != null) {
            values.add(programId);
        }

        return values.toArray(new Object[values.size()]);
    }

    List getBedCheckTimes(String query, Object[] values) {
        return (values.length > 0)?getHibernateTemplate().find(query, values):getHibernateTemplate().find(query);
    }

}
