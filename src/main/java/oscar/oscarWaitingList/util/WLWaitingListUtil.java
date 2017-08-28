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

package oscar.oscarWaitingList.util;

import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.WaitingListDao;
import org.oscarehr.common.model.WaitingList;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class WLWaitingListUtil {
	
	
	// Modified this method in Feb 2007 to ensure that all records cannot be deleted except hidden.
	static public synchronized void removeFromWaitingList(String waitingListID, String demographicNo) {
		MiscUtils.getLogger().debug("WLWaitingListUtil.removeFromWaitingList(): removing waiting list: " + waitingListID + " for patient " + demographicNo);

		WaitingListDao dao = SpringUtils.getBean(WaitingListDao.class);
		for (WaitingList wl : dao.findByWaitingListIdAndDemographicId(ConversionUtils.fromIntString(waitingListID), ConversionUtils.fromIntString(demographicNo))) {
			wl.setHistory(true);
			dao.merge(wl);
		}
		rePositionWaitingList(waitingListID);
	}

	static public synchronized void add2WaitingList(String waitingListID, String waitingListNote, String demographicNo, String onListSince) {
		MiscUtils.getLogger().debug("WLWaitingListUtil.add2WaitingList(): adding to waitingList: " + waitingListID + " for patient " + demographicNo);

		boolean emptyIds = waitingListID.equalsIgnoreCase("0") || demographicNo.equalsIgnoreCase("0");
		if (emptyIds) {
			MiscUtils.getLogger().debug("Ids are not proper - exiting");
			return;
		}

		WaitingListDao dao = SpringUtils.getBean(WaitingListDao.class);
		int maxPosition = dao.getMaxPosition(ConversionUtils.fromIntString(waitingListID));

		WaitingList list = new WaitingList();
		list.setListId(ConversionUtils.fromIntString(waitingListID));
		list.setDemographicNo(ConversionUtils.fromIntString(demographicNo));
		list.setNote(waitingListNote);
		if (onListSince == null || onListSince.length() <= 0) {
			list.setOnListSince(new Date());
		} else {
			list.setOnListSince(ConversionUtils.fromDateString(onListSince));
		}
		list.setPosition(maxPosition + 1);
		list.setHistory(false);
		dao.persist(list);

		// update the waiting list positions
		rePositionWaitingList(waitingListID);
	}

	/*
	 * This method adds the Waiting List note to the same position in the waitingList table but do not delete previous ones - later on DisplayWaitingList.jsp will display only the most current Waiting List Note record.
	 */
	static public synchronized void updateWaitingListRecord(String waitingListID, String waitingListNote, String demographicNo, String onListSince) {
		MiscUtils.getLogger().debug("WLWaitingListUtil.updateWaitingListRecord(): waitingListID: " + waitingListID + " for patient " + demographicNo);
		boolean isWatingIdSet = ! "0".equals(waitingListID) && ! "0".equals(demographicNo);
		if (!isWatingIdSet) return;

		WaitingListDao dao = SpringUtils.getBean(WaitingListDao.class);
		List<WaitingList> waitingLists = dao.findByWaitingListIdAndDemographicId(ConversionUtils.fromIntString(waitingListID), ConversionUtils.fromIntString(demographicNo));
		if (waitingLists.isEmpty()) return;

		long pos = 1;
		for (WaitingList wl : waitingLists) {
			pos = wl.getPosition();
		}

		// set all previous records 'is_history' fielf to 'N' --> to keep as record but never display
		for (WaitingList wl : waitingLists) {
			wl.setHistory(true);
			dao.merge(wl);
		}

		WaitingList wl = new WaitingList();
		wl.setListId(ConversionUtils.fromIntString(waitingListID));
		wl.setDemographicNo(ConversionUtils.fromIntString(demographicNo));
		wl.setNote(waitingListNote);
		wl.setPosition(pos);
		if (onListSince == null || onListSince.length() <= 0) {
			wl.setOnListSince(new Date());
		} else {
			wl.setOnListSince(ConversionUtils.fromDateString(onListSince));
		}
		wl.setHistory(false);

		dao.saveEntity(wl);

		// update the waiting list positions
		rePositionWaitingList(waitingListID);
	}

	/*
	 * This method adds the Waiting List note to the same position in the waitingList table but do not delete previous ones - later on DisplayWaitingList.jsp will display only the most current Waiting List Note record.
	 */
	static public synchronized void updateWaitingList(String id, String waitingListID, String waitingListNote, String demographicNo, String onListSince) {
		MiscUtils.getLogger().debug("WLWaitingListUtil.updateWaitingList(): waitingListID: " + waitingListID + " for patient " + demographicNo);
		boolean idsSet = !waitingListID.equalsIgnoreCase("0") && !demographicNo.equalsIgnoreCase("0");
		if (!idsSet) {
			MiscUtils.getLogger().debug("Ids are not set - exiting");
			return;
		}

		boolean wlIdsSet = (id != null && !id.equals(""));
		if (!wlIdsSet) {
			MiscUtils.getLogger().debug("Waiting list id is not set");
			return;
		}

		WaitingListDao dao = SpringUtils.getBean(WaitingListDao.class);
		WaitingList waitingListEntry = dao.find(ConversionUtils.fromIntString(id));
		if (waitingListEntry == null) {
			MiscUtils.getLogger().debug("Unable to fetch waiting list with id " + id);
			return;
		}

		waitingListEntry.setListId(ConversionUtils.fromIntString(waitingListID));
		waitingListEntry.setNote(waitingListNote);
		waitingListEntry.setOnListSince(ConversionUtils.fromDateString(onListSince));

		dao.merge(waitingListEntry);
	}

	public static void rePositionWaitingList(String waitingListID) {
		int i = 1;
		WaitingListDao dao = SpringUtils.getBean(WaitingListDao.class);
		for (WaitingList waitingList : dao.findByWaitingListId(ConversionUtils.fromIntString(waitingListID))) {
			waitingList.setPosition(i);
			dao.merge(waitingList);
			i++;
		}
	}
}
