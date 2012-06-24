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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.BedDemographicHistorical;
import org.oscarehr.PMmodule.model.BedDemographicPK;
import org.oscarehr.PMmodule.model.BedDemographicStatus;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of BedDemographicDao
 */
public class BedDemographicDao extends HibernateDaoSupport {

    private static final Logger log=MiscUtils.getLogger();

    public boolean bedDemographicStatusExists(Integer bedDemographicStatusId) {
        boolean exists = (((Long)getHibernateTemplate().iterate("select count(*) from BedDemographicStatus bds where bds.id = " + bedDemographicStatusId).next()) == 1);
        log.debug("bedDemographicStatusExists: " + exists);

        return exists;
    }

    /**
     * @see org.oscarehr.PMmodule.dao.BedDemographicDao#demographicExists(java.lang.Integer)
     */
    public boolean demographicExists(Integer bedId) {
        boolean exists = (((Long)getHibernateTemplate().iterate("select count(*) from BedDemographic bd where bd.id.bedId = " + bedId).next()) == 1);
        log.debug("clientExists: " + exists);

        return exists;
    }

    /**
     * @see org.oscarehr.PMmodule.dao.BedDemographicDao#bedExists(java.lang.Integer)
     */
    public boolean bedExists(Integer demographicNo) {
        boolean exists = (((Long)getHibernateTemplate().iterate("select count(*) from BedDemographic bd where bd.id.demographicNo = " + demographicNo).next()) == 1);
        log.debug("bedExists: " + exists);

        return exists;
    }

    /**
     * @see org.oscarehr.PMmodule.dao.BedDemographicDao#getBedDemographicByBed(java.lang.Integer)
     */
    public BedDemographic getBedDemographicByBed(Integer bedId) {
        List bedDemographics = getHibernateTemplate().find("from BedDemographic bd where bd.id.bedId = ?", bedId);

        if (bedDemographics.size() > 1) {
            throw new IllegalStateException("Bed is assigned to more than one client");
        }

        BedDemographic bedDemographic = (BedDemographic)((bedDemographics.size() == 1)?bedDemographics.get(0):null);

        log.debug("getBedDemographicByBed: " + bedId);

        return bedDemographic;
    }

    /**
     * @see org.oscarehr.PMmodule.dao.BedDemographicDao#getBedDemographicByDemographic(java.lang.Integer)
     */
    public BedDemographic getBedDemographicByDemographic(Integer demographicNo) {
        List bedDemographics = getHibernateTemplate().find("from BedDemographic bd where bd.id.demographicNo = ?", demographicNo);

        if (bedDemographics.size() > 1) {
            throw new IllegalStateException("Client is assigned to more than one bed");
        }

        BedDemographic bedDemographic = (BedDemographic)((bedDemographics.size() == 1)?bedDemographics.get(0):null);

        log.debug("getBedDemographicByDemographic: " + demographicNo);

        return bedDemographic;
    }

    public int getBedsCountByRoom(Integer roomId) {
		Long count = (Long)getHibernateTemplate().find("select count(*) from BedDemographic bd where bd.id.roomId = ?", roomId).get(0);
        if(count != null){
        	log.debug("getRoomDemographicByRoom: roomOccupancy = " + count.intValue());
        }
        return count.intValue();
    }
    
    /**
     * @see org.oscarehr.PMmodule.dao.BedDemographicDao#getBedDemographicStatus(java.lang.Integer)
     */
    public BedDemographicStatus getBedDemographicStatus(Integer bedDemographicStatusId) {
        BedDemographicStatus bedDemographicStatus = getHibernateTemplate().get(BedDemographicStatus.class, bedDemographicStatusId);
        log.debug("getBedDemographicStatus: id: " + (bedDemographicStatus != null?bedDemographicStatus.getId():null));

        return bedDemographicStatus;
    }

    /**
     * @see org.oscarehr.PMmodule.dao.BedDemographicDao#getBedDemographicStatuses()
     */
    @SuppressWarnings("unchecked")
    public BedDemographicStatus[] getBedDemographicStatuses() {
        List bedDemographicStatuses = getHibernateTemplate().find("from BedDemographicStatus");
        log.debug("getBedDemographicStatuses: size: " + bedDemographicStatuses.size());

        return (BedDemographicStatus[])bedDemographicStatuses.toArray(new BedDemographicStatus[bedDemographicStatuses.size()]);
    }

    /**
     * @see org.oscarehr.PMmodule.dao.BedDemographicDao#getBedDemographicHistoricals(java.util.Date)
     */
    @SuppressWarnings("unchecked")
    public BedDemographicHistorical[] getBedDemographicHistoricals(Date since) {
        List bedDemographicHistoricals = getHibernateTemplate().find("from BedDemographicHistorical bdh where bdh.usageEnd >= ?", DateTimeFormatUtils.getDateFromDate(since));
        log.debug("getBedDemographicHistoricals: size: " + bedDemographicHistoricals.size());

        return (BedDemographicHistorical[])bedDemographicHistoricals.toArray(new BedDemographicHistorical[bedDemographicHistoricals.size()]);
    }

    /**
     * @see org.oscarehr.PMmodule.dao.BedDemographicDao#saveBedDemographic(org.oscarehr.PMmodule.model.BedDemographic)
     */
    public void saveBedDemographic(BedDemographic bedDemographic) {
        updateHistory(bedDemographic);

        bedDemographic = getHibernateTemplate().merge(bedDemographic);
        getHibernateTemplate().flush();

        log.debug("saveBedDemographic: " + bedDemographic);
    }

    public void deleteBedDemographic(BedDemographic bedDemographic) {
        // save historical
        if (!DateUtils.isSameDay(bedDemographic.getReservationStart(), Calendar.getInstance().getTime())) {
            BedDemographicHistorical historical = BedDemographicHistorical.create(bedDemographic);
            getHibernateTemplate().saveOrUpdate(historical);
        }

        // delete
        getHibernateTemplate().delete(bedDemographic);
        getHibernateTemplate().flush();
    }

    boolean bedDemographicExists(BedDemographicPK id) {
        boolean exists = (((Long)getHibernateTemplate().iterate("select count(*) from BedDemographic bd where bd.id.bedId = " + id.getBedId() + " and bd.id.demographicNo = " + id.getDemographicNo()).next()) == 1);
        log.debug("bedDemographicExists: " + exists);

        return exists;
    }

    void updateHistory(BedDemographic bedDemographic) {
        BedDemographic existing = null;

        BedDemographicPK id = bedDemographic.getId();

        if (!bedDemographicExists(id)) {
            Integer demographicNo = id.getDemographicNo();
            Integer bedId = id.getBedId();

            if (bedExists(demographicNo)) {
                existing = getBedDemographicByDemographic(demographicNo);
            }
            else if (demographicExists(bedId)) {
                existing = getBedDemographicByBed(bedId);
            }
        }

        if (existing != null) {
            deleteBedDemographic(existing);
        }
    }

}
