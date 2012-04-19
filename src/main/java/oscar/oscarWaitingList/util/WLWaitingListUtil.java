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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class WLWaitingListUtil {
	// Modified this method in Feb 2007 to ensure that all records cannot be deleted except hidden.
	static public synchronized void removeFromWaitingList(String waitingListID, String demographicNo) {
		MiscUtils.getLogger().debug("WLWaitingListUtil.removeFromWaitingList(): removing waiting list: " + waitingListID + " for patient " + demographicNo);

		try {

			String sql;
			sql = " update  waitingList set is_history = 'Y' " + " where demographic_no = " + demographicNo + " and   listID = " + waitingListID;

			DBHandler.RunSQL(sql);
			// update the waiting list positions
			rePositionWaitingList(waitingListID);

		} catch (SQLException e) {
			MiscUtils.getLogger().debug("WLWaitingListUtil.removeFromWaitingList():" + e.getMessage());
		} finally {
			try {
			} catch (Exception ex2) {
				MiscUtils.getLogger().debug("WLWaitingListUtil.rePositionWaitingList(1):" + ex2.getMessage());
			}
		}

	}

	static public synchronized void add2WaitingList(String waitingListID, String waitingListNote, String demographicNo, String onListSince) {

		ResultSet rs = null;
		MiscUtils.getLogger().debug("WLWaitingListUtil.add2WaitingList(): insert into waitingList: " + waitingListID + " for patient " + demographicNo);
		if (!waitingListID.equalsIgnoreCase("0") && !demographicNo.equalsIgnoreCase("0")) {
			try {
				waitingListNote = org.apache.commons.lang.StringEscapeUtils.escapeSql(waitingListNote);

				String sql = " select max(position) as position from waitingList where listID=" + waitingListID + "  AND is_history = 'N' ";
				rs = DBHandler.GetSQL(sql);
				String nxPos = "1";
				if (rs.next()) {
					nxPos = Integer.toString(rs.getInt("position") + 1);
				}
				MiscUtils.getLogger().debug("WLWaitingListUtil.add2WaitingList(): position = " + nxPos);

				if (onListSince == null || onListSince.length() <= 0) {
					sql = " insert into waitingList " + " (listID, demographic_no, note, position, onListSince, is_history) " + " values(" + waitingListID + "," + demographicNo + ",'" + waitingListNote + "'," + nxPos + ", now() , 'N')";
				} else {
					sql = " insert into waitingList " + " (listID, demographic_no, note, position, onListSince, is_history) " + " values(" + waitingListID + "," + demographicNo + ",'" + waitingListNote + "'," + nxPos + ",'" + onListSince + "', 'N')";
				}
				MiscUtils.getLogger().debug("WLWaitingListUtil.add2WaitingList(): insert sql = " + sql);

				DBHandler.RunSQL(sql);

				// update the waiting list positions
				rePositionWaitingList(waitingListID);

			} catch (SQLException e) {
				MiscUtils.getLogger().debug("WLWaitingListUtil.add2WaitingList():" + e.getMessage());
			} finally {
				try {
					rs.close();
				} catch (Exception ex2) {
					MiscUtils.getLogger().debug("WLWaitingListUtil.rePositionWaitingList(1):" + ex2.getMessage());
				}
			}
		}
	}

	/*
	 * This method adds the Waiting List note to the same position in the waitingList table but do not delete previous ones - later on DisplayWaitingList.jsp will display only the most current Waiting List Note record.
	 */
	static public synchronized void updateWaitingListRecord(String waitingListID, String waitingListNote, String demographicNo, String onListSince) {

		MiscUtils.getLogger().debug("WLWaitingListUtil.updateWaitingListRecord(): waitingListID: " + waitingListID + " for patient " + demographicNo);

		ResultSet rs = null;
		if (!waitingListID.equalsIgnoreCase("0") && !demographicNo.equalsIgnoreCase("0")) {
			try {

				int pos = 1;
				String sql = " SELECT * FROM waitingList " + " where demographic_no = " + demographicNo + " and   listID = " + waitingListID + " AND is_history = 'N' ";
				rs = DBHandler.GetSQL(sql);
				if (rs == null) {
					MiscUtils.getLogger().debug("WLWaitingListUtil.updateWaitingListRecord(): result set == null");
					return;
				}
				while (rs.next()) {
					pos = rs.getInt("position");
				}

				waitingListNote = org.apache.commons.lang.StringEscapeUtils.escapeSql(waitingListNote);

				// set all previous records 'is_history' fielf to 'N' --> to keep as record but never display
				sql = " update  waitingList set is_history = 'Y' " + " where demographic_no = " + demographicNo + " and   listID = " + waitingListID;

				DBHandler.RunSQL(sql);
				MiscUtils.getLogger().debug("WLWaitingListUtil.updateWaitingListRecord(): update sql = " + sql);

				sql = " insert into waitingList " + " (listID, demographic_no, note, position, onListSince, is_history) " + " values(" + waitingListID + "," + demographicNo + ",'" + waitingListNote + "'," + pos + ", '" + onListSince + "', 'N')";

				MiscUtils.getLogger().debug("WLWaitingListUtil.updateWaitingListRecord(): insert sql = " + sql);
				DBHandler.RunSQL(sql);

				// update the waiting list positions
				rePositionWaitingList(waitingListID);

			}

			catch (SQLException e) {
				MiscUtils.getLogger().debug("WLWaitingListUtil.updateWaitingListRecord():" + e.getMessage());
			} finally {
				try {
					rs.close();
				} catch (Exception ex2) {
					MiscUtils.getLogger().debug("WLWaitingListUtil.updateWaitingListRecord(1):" + ex2.getMessage());
				}
			}
		}
	}

	/*
	 * This method adds the Waiting List note to the same position in the waitingList table but do not delete previous ones - later on DisplayWaitingList.jsp will display only the most current Waiting List Note record.
	 */
	static public synchronized void updateWaitingList(String id, String waitingListID, String waitingListNote, String demographicNo, String onListSince) {

		MiscUtils.getLogger().debug("WLWaitingListUtil.updateWaitingList(): waitingListID: " + waitingListID + " for patient " + demographicNo);

		ResultSet rs = null;
		if (!waitingListID.equalsIgnoreCase("0") && !demographicNo.equalsIgnoreCase("0")) {
			try {

				String sql = "";

				waitingListNote = org.apache.commons.lang.StringEscapeUtils.escapeSql(waitingListNote);

				if (id != null && !id.equals("")) {

					sql = " update  waitingList set listID = " + waitingListID + ", " + " note = '" + waitingListNote + "', " + " onListSince = '" + onListSince + "' " + " where id=" + id;
					MiscUtils.getLogger().debug("WLWaitingListUtil.updateWaitingList(): update sql = " + sql);

					DBHandler.RunSQL(sql);

				}
			}

			catch (SQLException e) {
				MiscUtils.getLogger().debug("WLWaitingListUtil.updateWaitingList():" + e.getMessage());
			} finally {
				try {
					rs.close();
				} catch (Exception ex2) {
					MiscUtils.getLogger().debug("WLWaitingListUtil.updateWaitingList(1):" + ex2.getMessage());
				}
			}
		}
	}

	public static void rePositionWaitingList(String waitingListID) {

		int i = 1;
		String sql = "";
		ResultSet rs = null;
		try {
			sql = " SELECT * FROM waitingList WHERE listID=" + waitingListID + " AND is_history='N' ORDER BY onListSince";
			rs = DBHandler.GetSQL(sql);

			while (rs.next()) {

				sql = " UPDATE waitingList SET position=" + i + " WHERE listID=" + waitingListID + " AND demographic_no=" + oscar.Misc.getString(rs, "demographic_no") + " AND is_history = 'N' ";
				MiscUtils.getLogger().debug("WLWaitingListUtil.rePositionWaitingList(2): " + sql);
				DBHandler.RunSQL(sql);
				i++;
			}

		} catch (SQLException sqlex) {
			MiscUtils.getLogger().debug("WLWaitingListUtil.rePositionWaitingList(2):" + sqlex.getMessage());
		} catch (Exception ex) {
			MiscUtils.getLogger().debug("WLWaitingListUtil.rePositionWaitingList(2):" + ex.getMessage());
		} finally {
			try {
				rs.close();
			} catch (Exception ex2) {
				MiscUtils.getLogger().debug("WLWaitingListUtil.rePositionWaitingList():" + ex2.getMessage());
			}
		}
	}
}
