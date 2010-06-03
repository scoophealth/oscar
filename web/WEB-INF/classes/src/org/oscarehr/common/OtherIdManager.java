/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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

package org.oscarehr.common;

import org.oscarehr.common.dao.OtherIdDAO;
import org.oscarehr.common.model.OtherId;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author Ronnie Cheng
 */
public class OtherIdManager {
	final public Integer DEMOGRAPHIC = 1;
	final public Integer APPOINTMENT = 2;

	private OtherIdDAO otherIdDao = (OtherIdDAO) SpringUtils.getBean("otherIdDao");

    public String getDemoOtherId(Integer demographicNo, String otherKey) {
		String oid = "";
		OtherId oidObj = getOtherIdObj(this.DEMOGRAPHIC, demographicNo, otherKey);
		if (oidObj!=null) oid=oidObj.getOtherId();
		return oid;
    }

    public String getDemoOtherId(String demographicNo, String otherKey) {
		return getDemoOtherId(getNumeric(demographicNo), otherKey);
    }

    public String getApptOtherId(Integer appointmentNo, String otherKey) {
		String oid = "";
		OtherId oidObj = getOtherIdObj(this.APPOINTMENT, appointmentNo, otherKey);
		if (oidObj!=null) oid=oidObj.getOtherId();
		return oid;
    }

	public String getApptOtherId(String appointmentNo, String otherKey) {
		return getApptOtherId(getNumeric(appointmentNo), otherKey);
	}

	public void saveIdAppointment(Integer appointmentNo, String otherKey, String otherId) {
		OtherId oid = getOtherIdObj(this.APPOINTMENT, appointmentNo, otherKey);
		if (doNotSave(oid, otherId)) return;

		oid = new OtherId(this.APPOINTMENT, appointmentNo, otherKey, otherId);
		otherIdDao.save(oid);
	}

	public void saveIdAppointment(String appointmentNo, String otherKey, String otherId) {
		Integer apptNo = getNumeric(appointmentNo);
		if (apptNo!=null) saveIdAppointment(apptNo, otherKey, otherId);
	}

	public void saveIdDemographic(Integer demographicNo, String otherKey, String otherId) {
		OtherId oid = getOtherIdObj(this.DEMOGRAPHIC, demographicNo, otherKey);
		if (doNotSave(oid, otherId)) return;

		oid = new OtherId(this.DEMOGRAPHIC, demographicNo, otherKey, otherId);
		otherIdDao.save(oid);
	}

	public void saveIdDemographic(String demographicNo, String otherKey, String otherId) {
		Integer demoNo = getNumeric(demographicNo);
		if (demoNo!=null) saveIdDemographic(demoNo, otherKey, otherId);
	}

    private OtherId getOtherIdObj(Integer tableName, Integer tableId, String otherKey) {
		return otherIdDao.getOtherId(tableName, tableId, otherKey);
    }

	private void setDelete(OtherId otherId) {
		if (otherId!=null) {
			otherId.setDeleted(Boolean.TRUE);
			otherIdDao.save(otherId);
		}
	}

	private boolean doNotSave(OtherId oid, String otherId) {
		if (otherId==null) return true;
		if (oid==null) {
			if (otherId.trim().equals("")) return true;
		} else {
			if (oid.getOtherId().equals(otherId)) return true;
			else setDelete(oid);
		}
		return false;
	}

	private Integer getNumeric(String n) {
		String numeric="0123456789";
		for (int i=0; i<n.length(); i++) {
			if (!numeric.contains(n.subSequence(i, i+1))) return null;
		}
		return Integer.valueOf(n);
	}
}
