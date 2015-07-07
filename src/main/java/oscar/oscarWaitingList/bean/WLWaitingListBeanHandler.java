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

package oscar.oscarWaitingList.bean;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.dao.WaitingListDao;
import org.oscarehr.common.dao.WaitingListNameDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.WaitingList;
import org.oscarehr.common.model.WaitingListName;
import org.oscarehr.util.SpringUtils;

import oscar.oscarWaitingList.util.WLWaitingListUtil;
import oscar.util.ConversionUtils;

public class WLWaitingListBeanHandler {

	List<WLPatientWaitingListBean> waitingListArrayList = new ArrayList<WLPatientWaitingListBean>();
	String waitingListName = "";

	public WLWaitingListBeanHandler(String waitingListID) {
		init(waitingListID);
	}

	public boolean init(String waitingListID) {
		WaitingListDao dao = SpringUtils.getBean(WaitingListDao.class);
		List<Object[]> waitingListsAndDemographics = dao.findWaitingListsAndDemographics(ConversionUtils.fromIntString(waitingListID));

		String onListSinceDateOnly = "";
		for (Object[] i : waitingListsAndDemographics) {
			WaitingList waitingList = (WaitingList) i[0];
			Demographic demographic = (Demographic) i[1];

			onListSinceDateOnly = ConversionUtils.toDateString(waitingList.getOnListSince());

			WLPatientWaitingListBean wLBean = new WLPatientWaitingListBean(String.valueOf(waitingList.getDemographicNo()), // oscar.Misc.getString(rs, "demographic_no"),
			        String.valueOf(waitingList.getListId()), // oscar.Misc.getString(rs, "listID"),
			        String.valueOf(waitingList.getPosition()), // oscar.Misc.getString(rs, "position"),
			        demographic.getFullName(),//  oscar.Misc.getString(rs, "patientName"), 
			        demographic.getPhone(), //  oscar.Misc.getString(rs, "phone"),
			        waitingList.getNote(), // o oscar.Misc.getString(rs, "note"),
			        onListSinceDateOnly);
			waitingListArrayList.add(wLBean);
		}

		if (waitingListID != null && waitingListID.length() > 0) {
			WaitingListNameDao nameDao = SpringUtils.getBean(WaitingListNameDao.class);
			WaitingListName name = nameDao.find(Integer.parseInt(waitingListID));
			if (name != null) {
				waitingListName = name.getName();
			}
		}
		return true;
	}

	static public void updateWaitingList(String waitingListID) {
		WaitingListDao dao = SpringUtils.getBean(WaitingListDao.class);

		Integer waitingListId = ConversionUtils.fromIntString(waitingListID);
		List<WaitingList> waitingList = dao.findByWaitingListId(waitingListId);

		boolean needUpdate = false;
		// go thru all the patient on the list
		for (WaitingList i : waitingList) {
			int demographicNo = i.getDemographicNo();

			// check if the patient has an appointment already
			List<Appointment> appointments = dao.findAppointmentFor(i);
			if (!appointments.isEmpty()) {
				//delete patient from the waitingList

				WLWaitingListUtil.removeFromWaitingList(waitingListID, String.valueOf(demographicNo));
				needUpdate = true;
			}
		}

		if (!needUpdate) {
			return;
		}

		//update the list
		for (int i = 1; i < waitingList.size(); i++) {
			WaitingList waitingListEntry = waitingList.get(i);
			waitingListEntry.setPosition(i);
			dao.saveEntity(waitingListEntry);
		}
	}

	public List<WLPatientWaitingListBean> getWaitingList() {
		return waitingListArrayList;
	}

	public String getWaitingListName() {
		return waitingListName;
	}
}
