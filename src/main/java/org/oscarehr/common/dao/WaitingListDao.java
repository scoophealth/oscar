/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.WaitingList;
import org.springframework.stereotype.Repository;

@Repository
public class WaitingListDao extends AbstractDao<WaitingList> {

	public WaitingListDao() {
		super(WaitingList.class);
	}

	/**
	 * Finds all waiting lists for the specified demographic 
	 * 
	 * @param demographicNo
	 * 		Demographic to find the lists for
	 * @return
	 * 		Returns a list of all matching {@link org.oscarehr.common.model.WaitingListName}, {@link org.oscarehr.common.model.WaitingList} pairs.
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findByDemographic(Integer demographicNo) {
		Query query = entityManager.createQuery("FROM WaitingListName wn, WaitingList w WHERE wn.id = w.listId AND w.demographicNo = :demoNo AND w.isHistory != 'Y'");
		query.setParameter("demoNo", demographicNo);
		return query.getResultList();
	}

	/**
	 * Finds all waiting lists and demographics for the specified list id 
	 * 
	 * @param listId
	 * 		List ID to get gemographics for
	 * @return
	 * 		Returns a list of WaitingList, Demographic pairs.
	 */
	@SuppressWarnings("unchecked")
    public List<Object[]> findWaitingListsAndDemographics(Integer listId) {
		Query query = entityManager.createQuery("FROM WaitingList w, Demographic d WHERE w.demographicNo = d.DemographicNo AND  w.listId = :listId AND w.isHistory = 'N' ORDER BY w.position");
		query.setParameter("listId", listId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
    public List<WaitingList> findByWaitingListId(Integer listId) {
		Query query = entityManager.createQuery("FROM WaitingList w WHERE w.listId = :listId AND w.isHistory = 'N' ORDER BY w.onListSince");
		query.setParameter("listId", listId);
		return query.getResultList();
    }

	/**
	 * Finds appointments that take place after the demographic has been placed onto the waiting list. 
	 * 
	 * @param waitingList
	 * 		Waiting list entry
	 * @return
	 * 		Returns all available appointments for the specified waiting list entry.
	 */
	@SuppressWarnings("unchecked")
    public List<Appointment> findAppointmentFor(WaitingList waitingList) {
		// this is a translation attempt of this query
		// sql = "select a.demographic_no, a.appointment_date, wl.onListSince from appointment a, waitingList wl where a.appointment_date >= wl.onListSince AND a.demographic_no=wl.demographic_no AND a.demographic_no=" + oscar.Misc.getString(rs, "demographic_no") + "";  
		Query query = entityManager.createQuery("From Appointment a where a.appointmentDate >= :onListSince AND a.demographicNo = :demographicNo");
		query.setParameter("onListSince", waitingList.getOnListSince());
		query.setParameter("demographicNo", waitingList.getDemographicNo());
	    return query.getResultList();
    }

	/**
	 * Finds all waiting list entries for the specified waiting list and demographic IDs
	 * 
	 * @param waitingListId
	 * 		Waiting list id to be searched for
	 * @param demographicId
	 * 		Demographic id to be searched for
	 * @return
	 * 		Returns all matching waiting list entries.
	 */
	@SuppressWarnings("unchecked")
    public List<WaitingList> findByWaitingListIdAndDemographicId(Integer waitingListId, Integer demographicId) {
		Query query = createQuery("wl", "wl.demographicNo = :demoNo AND wl.listId = :listId");
		query.setParameter("demoNo", demographicId);
		query.setParameter("listId", waitingListId);
	    return query.getResultList();
    }

	/**
	 * Gets max position form the specified waiting list 
	 * 
	 * @param listId
	 * 		Waiting list to find max position for
	 * @return
	 * 		Returns the position for the specified list.
	 */
	public Integer getMaxPosition(Integer listId) {
		Query query = entityManager.createQuery("select max(w.position) from WaitingList w where w.listId = :listId AND w.isHistory = 'N'");
		query.setParameter("listId", listId);
		Long result = (Long) query.getSingleResult();
		if (result == null) {
			return 0;
		} 
		return result.intValue();
    }
	
	@SuppressWarnings("unchecked")
    public List<WaitingList> search_wlstatus(Integer demographicId) {
		Query query = entityManager.createQuery("select wl from WaitingList wl where wl.demographicNo = :demoNo AND wl.isHistory = 'N' order BY wl.onListSince desc");
		query.setParameter("demoNo", demographicId);

	    return query.getResultList();
    }
}
