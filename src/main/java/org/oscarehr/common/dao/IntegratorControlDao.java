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


package org.oscarehr.common.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.IntegratorControl;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;
@Repository
public class IntegratorControlDao extends AbstractDao<IntegratorControl> {

	public static final String REMOVE_DEMO_ID_CTRL = "RemoveDemographicIdentity";
    public static final String UPDATE_INTERVAL_CTRL = "UpdateInterval";
    public static final String INTERVAL_HR = "h";
    private static final Logger log=MiscUtils.getLogger();

    public IntegratorControlDao() {
		super(IntegratorControl.class);
	}

	public List<IntegratorControl> getAllByFacilityId(Integer facilityId) {
        String queryStr = "FROM IntegratorControl c WHERE c.facilityId = "+facilityId+" ORDER BY c.control";

        @SuppressWarnings("unchecked")
        List<IntegratorControl> rs = entityManager.createQuery(queryStr).getResultList();

        return rs;
    }

    public boolean readRemoveDemographicIdentity(Integer facilityId) {
        boolean rid = false;
        IntegratorControl ic_rid = readRemoveDemographicIdentityCtrl(facilityId);
        if (ic_rid!=null) rid = ic_rid.getExecute();
        return rid;
    }

    private IntegratorControl readRemoveDemographicIdentityCtrl(Integer facilityId) {
        IntegratorControl ic_rid = null;

        List<IntegratorControl> lic = getAllByFacilityId(facilityId);
        for (IntegratorControl ic : lic) {
            String ctrl = ic.getControl();
            if (ctrl!=null & ctrl.equals(REMOVE_DEMO_ID_CTRL)) {
                ic_rid = ic;
                break;
            }
        }
        return ic_rid;
    }

    public void saveRemoveDemographicIdentity(Integer facilityId, boolean removeDemoId) {
        IntegratorControl ic_rid = readRemoveDemographicIdentityCtrl(facilityId);
        if (ic_rid==null) { //create new control
            ic_rid = new IntegratorControl();
            ic_rid.setFacilityId(facilityId);
            ic_rid.setControl(REMOVE_DEMO_ID_CTRL);
        }
        ic_rid.setExecute(removeDemoId);
        save(ic_rid);
    }

    public Integer readUpdateInterval(Integer facilityId) {
        Integer upi = 0;
        IntegratorControl ic_upi = readUpdateIntervalCtrl(facilityId);
        if (ic_upi!=null && ic_upi.getExecute()) {
            upi = getIntervalTime(ic_upi.getControl());
        }
        return upi;
    }

    public IntegratorControl readUpdateIntervalCtrl(Integer facilityId) {
        IntegratorControl ic_upi = null;

        List<IntegratorControl> lic = getAllByFacilityId(facilityId);
        for (IntegratorControl ic : lic) {
            String ctrl = ic.getControl();
            if (ctrl!=null & ctrl.startsWith(UPDATE_INTERVAL_CTRL)) {
                ic_upi = ic;
                break;
            }
        }
        return ic_upi;
    }

    public void saveUpdateInterval(Integer facilityId, Integer updateInterval) {
        if (updateInterval==null) updateInterval=0;

        IntegratorControl ic_rid = readUpdateIntervalCtrl(facilityId);
        if (ic_rid==null) { //create new control
            ic_rid = new IntegratorControl();
            ic_rid.setFacilityId(facilityId);
        }
        ic_rid.setControl(UPDATE_INTERVAL_CTRL+"="+updateInterval+INTERVAL_HR);
        ic_rid.setExecute(updateInterval>0);
        save(ic_rid);
    }

    public void save(IntegratorControl ic) {
        if (ic == null) {
            throw new IllegalArgumentException();
        }
        if(ic.getId() == null || ic.getId().intValue() == 0) {
        	persist(ic);
        } else {
        	merge(ic);
        }

        if (log.isDebugEnabled()) {
            log.debug("saveIntegratorControl: " + ic.getId());
        }
    }

    private Integer getIntervalTime(String sIntervalTime) {
        Integer ret = 0;
        int begin = UPDATE_INTERVAL_CTRL.length()+1;
        if (sIntervalTime!=null && sIntervalTime.length()>begin) {
            int end = sIntervalTime.indexOf(INTERVAL_HR, begin);
            if (end>begin) {
                String sTime = sIntervalTime.substring(begin, end);
                ret = Integer.parseInt(sTime);
            }
        }
        return ret;
    }
}
