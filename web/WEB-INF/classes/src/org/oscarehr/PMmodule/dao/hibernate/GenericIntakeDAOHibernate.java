/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.oscarehr.PMmodule.dao.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.GenericIntakeDAO;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.common.model.ReportStatistic;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of GenericIntakeDAO interface
 */
public class GenericIntakeDAOHibernate extends HibernateDaoSupport implements GenericIntakeDAO {

	private static final Log LOG = LogFactory.getLog(GenericIntakeDAOHibernate.class);

	/**
	 * @see org.oscarehr.PMmodule.dao.GenericIntakeDAO#getLatestIntake(IntakeNode, java.lang.Integer, Integer)
	 */
	public Intake getLatestIntake(IntakeNode node, Integer clientId, Integer programId) {
		if (node == null || clientId == null) {
			throw new IllegalArgumentException("Parameters node and clientId must be non-null");
		}

		List<Intake> intakes = getIntakes(node, clientId, programId);
		Intake intake = !intakes.isEmpty() ? intakes.get(0) : null;
		LOG.info("get latest intake: " + intake);

		return intake;
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.GenericIntakeDAO#getIntakes(org.oscarehr.PMmodule.model.IntakeNode, java.lang.Integer, Integer)
	 */
	public List<Intake> getIntakes(IntakeNode node, Integer clientId, Integer programId) {
		if (node == null || clientId == null) {
			throw new IllegalArgumentException("Parameters node and clientId must be non-null");
		}

		List<?> results = getHibernateTemplate().find("from Intake i where i.node = ? and i.clientId = ? order by i.createdOn desc", new Object[] { node, clientId });
		List<Intake> intakes = convertToIntakes(results, programId);

		LOG.info("get intakes: " + intakes.size());

		return intakes;
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.GenericIntakeDAO#saveIntake(org.oscarehr.PMmodule.model.Intake)
	 */
	public Integer saveIntake(Intake intake) {
		Integer intakeId = (Integer) getHibernateTemplate().save(intake);
		getHibernateTemplate().flush();
		LOG.info("saved intake: " + intake);

		return intakeId;
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.GenericIntakeDAO#getLatestIntakeIds(Integer, java.util.Date, java.util.Date)
	 */
	public SortedSet<Integer> getLatestIntakeIds(Integer nodeId, Date startDate, Date endDate) {
		if (nodeId == null || startDate == null || endDate == null) {
			throw new IllegalArgumentException("Parameters node, startDate and endDate must be non-null");
		}
		
		//endDate is "YYYY-MM-DD 00:00:00", it has to be "YYYY-MM-DD 23:59:59"
		Calendar c = new GregorianCalendar();
		c.setTime(endDate);
		c.add(Calendar.DAY_OF_MONTH, 1);
		endDate = c.getTime();		
		
		// wrong, only got the oldest first record: List<?> results = getHibernateTemplate().find("select i.id, max(i.createdOn) from Intake i where i.node.id = ? and i.createdOn between ? and ? group by i.clientId", new Object[] { nodeId, startDate, endDate });
		List<?> results = getHibernateTemplate().find("select i.id, max(i.createdOn) from Intake i where i.node.id = ? and i.createdOn between ? and ? and i.createdOn = (select max(ii.createdOn) from Intake ii where ii.clientId = i.clientId) group by i.clientId", new Object[] { nodeId, startDate, endDate });
		SortedSet<Integer> intakeIds = convertToIntegers(results);

		LOG.info("get latest intake ids: " + intakeIds.size());

		return intakeIds;
	}
	
	/**
	 * @see org.oscarehr.PMmodule.dao.GenericIntakeDAO#getReportStatistics(java.util.List, java.util.List)
	 */
	public SortedMap<Integer, SortedMap<String, ReportStatistic>> getReportStatistics(Set<Integer> answerIds, Set<Integer> intakeIds) {
		if (intakeIds == null || answerIds == null) {
			throw new IllegalArgumentException("Parameters intakeIds and answerIds must be non-null");
		}
		
		SortedMap<Integer, SortedMap<String, ReportStatistic>> reportStatistics = new TreeMap<Integer, SortedMap<String, ReportStatistic>>();
		
		if (!intakeIds.isEmpty() && !answerIds.isEmpty()) {
			List<?> results = getHibernateTemplate().find("select ia.node.id, ia.value, count(ia.value) from IntakeAnswer ia where ia.node.id in (" + convertToString(answerIds) + ") and ia.intake.id in (" + convertToString(intakeIds) + ") group by ia.node.id, ia.value");
			convertToReportStatistics(results, intakeIds.size(), reportStatistics);
		}
		
		LOG.info("get reportStatistics: " + reportStatistics.size());
		
		return reportStatistics;
	}
	
	// Private
	
	private List<Intake> convertToIntakes(List<?> results, Integer programId) {
		List<Intake> intakes = new ArrayList<Intake>();

		if (results != null) {
			for (Object o : results) {
				Intake intake = (Intake) o;
				
				Demographic client = (Demographic) getHibernateTemplate().get(Demographic.class, intake.getClientId());
				intake.setClient(client);
				
				Provider staff = (Provider) getHibernateTemplate().get(Provider.class, intake.getStaffId());
				intake.setStaff(staff);
				
				intake.setProgramId(programId);

				intakes.add(intake);
			}
		}

		return intakes;
	}

	private SortedSet<Integer> convertToIntegers(List<?> results) {
		SortedSet<Integer> intakeIds = new TreeSet<Integer>();

		if (results != null) {
			for (Object o : results) {
				Object[] tuple = (Object[]) o;
				Integer intakeId = (Integer) tuple[0];
				
				intakeIds.add(intakeId);
			}
		}

		return intakeIds;
	}

	private String convertToString(Set<Integer> ids) {
		StringBuilder builder = new StringBuilder();
		
		for (Iterator<Integer> i = ids.iterator(); i.hasNext();) {
			builder.append(i.next());
			
			if (i.hasNext()) {
				builder.append(",");
			}
		}
		
		return builder.toString();
	}

	private void convertToReportStatistics(List<?> results, int size, SortedMap<Integer, SortedMap<String, ReportStatistic>> reportStatistics) {
		for (Object o : results) {
			Object[] tuple = (Object[]) o;
			
			Integer nodeId = (Integer) tuple[0];
			String value = (String) tuple[1];
			Integer count = (Integer) tuple[2];
			
			if (!reportStatistics.containsKey(nodeId)) {
				reportStatistics.put(nodeId, new TreeMap<String, ReportStatistic>());
			}
			
			reportStatistics.get(nodeId).put(value, new ReportStatistic(count, size));
		}
	}
	
}