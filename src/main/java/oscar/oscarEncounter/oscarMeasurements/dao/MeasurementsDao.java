/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarEncounter.oscarMeasurements.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.oscarEncounter.oscarMeasurements.model.Measurements;

/**
 * @deprecated use MeasurementDao instead (2012-01-23)
 */
public class MeasurementsDao extends HibernateDaoSupport {

	public int displayCount = 10;

	public void addMeasurements(Measurements measurements) {
		getHibernateTemplate().merge(measurements);
	}

	public List<Measurements> getMeasurementsByDemo(Integer demoId) {
		String queryStr = "FROM Measurements m WHERE m.demographicNo = " + demoId + " ORDER BY m.id";

		@SuppressWarnings("unchecked")
		List<Measurements> rs = getHibernateTemplate().find(queryStr);

		return rs;
	}

	public HashMap<String, Measurements> getMeasurements(String demo, String[] types) {
		HashMap<String, Measurements> map = new HashMap<String, Measurements>();

		StringBuilder sb = new StringBuilder();
		for (String type : types) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append("'");
			sb.append(StringEscapeUtils.escapeSql(type));
			sb.append("'");
		}

		String queryStr = "From Measurements m WHERE m.demographicNo = " + demo + " AND type IN (" + sb.toString() + ") ORDER BY type,m.dateObserved";
		logger.debug(queryStr);

		List<Measurements> rs = getHibernateTemplate().find(queryStr);

		for (Measurements m : rs) {
			map.put(m.getType(), m);
		}
		return map;
	}

	public List<Measurements> getMeasurements(String demo) {

		String queryStr = "From Measurements m WHERE m.demographicNo = " + demo + " ORDER BY m.dateObserved DESC";
		logger.debug(queryStr);

		List<Measurements> rs = getHibernateTemplate().find(queryStr);

		return rs;
	}

	public List<Measurements> getMeasurements(String demo, Date startDate, Date endDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		String queryStr = "From Measurements m WHERE m.demographicNo = " + demo + " AND m.dateObserved >= '" + formatter.format(startDate) + "' AND m.dateObserved <= '" + formatter.format(endDate) + "' ORDER BY m.dateObserved DESC";
		logger.debug(queryStr);

		List<Measurements> rs = getHibernateTemplate().find(queryStr);

		return rs;
	}

	public List<Measurements> getMeasurementsByAppointment(int appointmentNo) {
		String queryStr = "From Measurements m WHERE m.appointmentNo = " + appointmentNo + " ORDER BY m.dateObserved DESC";

		List<Measurements> rs = getHibernateTemplate().find(queryStr);

		return rs;
	}

	public Measurements getLatestMeasurementByDemographicNoAndType(int demographicNo, String type) {
		String queryStr = "From Measurements m WHERE m.demographicNo = " + demographicNo + " and m.type=? ORDER BY m.dateObserved DESC";

		@SuppressWarnings("unchecked")
		List<Measurements> rs = getHibernateTemplate().find(queryStr, new Object[] { type });

		if (!rs.isEmpty()) {
			return rs.get(0);
		}
		return null;
	}

	public Set<Integer> getAppointmentNosByDemographicNoAndType(int demographicNo, String type, Date startDate, Date endDate) {
		Map<Integer, Boolean> results = new HashMap<Integer, Boolean>();

		String queryStr = "From Measurements m WHERE m.demographicNo = " + demographicNo + " and m.type=? and m.dateObserved>=? and m.dateObserved<=? ORDER BY m.dateObserved DESC";

		@SuppressWarnings("unchecked")
		List<Measurements> rs = getHibernateTemplate().find(queryStr, new Object[] { type, startDate, endDate });
		for (Measurements m : rs) {
			results.put(m.getAppointmentNo(), true);
		}

		return results.keySet();
	}

	public Measurements getLatestMeasurementByAppointment(int appointmentNo, String type) {
		String queryStr = "From Measurements m WHERE m.appointmentNo = " + appointmentNo + " and m.type=? ORDER BY m.dateObserved DESC";

		@SuppressWarnings("unchecked")
		List<Measurements> rs = getHibernateTemplate().find(queryStr, new Object[] { type });

		if (!rs.isEmpty()) {
			return rs.get(0);
		}
		return null;
	}

	public HashMap<String, Measurements> getMeasurementsPriorToDate(String demographicNo, Date d) {
		String queryStr = "From Measurements m WHERE m.demographicNo = " + demographicNo + " AND m.dateObserved <= ?";

    	List<Measurements> rs = getHibernateTemplate().find(queryStr, new Object[] { d } );
    	HashMap<String,Measurements> map = new HashMap<String,Measurements>();

    	for(Measurements m:rs) {
    		map.put(m.getType(), m);
    	}

    	return map;
	}

	public List<Date> getDatesForMeasurements(String demographicNo, String[] types) {
		StringBuilder sb = new StringBuilder();
    	for(String type:types) {
    		if(sb.length()>0) {
    			sb.append(",");
    		}
    		sb.append("'");
    		sb.append(StringEscapeUtils.escapeSql(type));
    		sb.append("'");
    	}

		String queryStr = "SELECT DISTINCT m.dateObserved FROM Measurements m WHERE m.demographicNo = '" +
			StringEscapeUtils.escapeSql(demographicNo) + "' AND m.type IN (" + sb.toString() + ") ORDER BY m.dateObserved DESC";

		List<Date> results = getHibernateTemplate().find(queryStr);

		return results;
	}
}
