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


package oscar.oscarEncounter.oscarMeasurements.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.oscarEncounter.oscarMeasurements.model.MeasurementsExt;

public class MeasurementsExtDao extends HibernateDaoSupport {

	public void addMeasurementsExt(MeasurementsExt measurementsExt) {
		getHibernateTemplate().merge(measurementsExt);
	}

	public List<MeasurementsExt> getMeasurementsExtByMeasurementId(Integer measurementId) {
		String queryStr = "FROM MeasurementsExt m WHERE m.measurementId = "+measurementId+" ORDER BY m.id";
		
		@SuppressWarnings("unchecked")
		List<MeasurementsExt> rs = getHibernateTemplate().find(queryStr);

		return rs;
	}

	public MeasurementsExt getMeasurementsExtByMeasurementIdAndKeyVal(Integer measurementId, String keyVal) {
		String queryStr = "FROM MeasurementsExt m WHERE m.measurementId = "+measurementId+" AND m.keyVal='"+keyVal+"'";

		@SuppressWarnings("unchecked")
		List<MeasurementsExt> rs = getHibernateTemplate().find(queryStr);

		if(rs.isEmpty()) {
			return null;
		}

		return rs.get(0);
	}

	public Integer getMeasurementIdByKeyValue(String key, String value) {
		String queryStr = "FROM MeasurementsExt m WHERE m.keyVal='"+key+"' AND m.val='"+value+"'";
		
		@SuppressWarnings("unchecked")
		List<MeasurementsExt> rs = getHibernateTemplate().find(queryStr);
		
		if (rs.size()>0) return rs.get(0).getMeasurementId();
		return null;
	}
}
