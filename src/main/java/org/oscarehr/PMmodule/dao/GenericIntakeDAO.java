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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ReportStatistic;
import org.oscarehr.util.AccumulatorMap;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.util.SqlUtils;

/**
 * Hibernate implementation of GenericIntakeDAO interface
 */
public class GenericIntakeDAO extends HibernateDaoSupport {

	private static final Logger LOG = MiscUtils.getLogger();

	@SuppressWarnings("unchecked")
	public List<Object[]> getOcanIntakesAfterDate(Calendar after) {
		return getHibernateTemplate().find("from Intake i, IntakeNode n, IntakeNodeLabel l where " +
			"i.createdOn > ? and i.node.id = n.id and n.label.id = l.id and " +
			"(l.label like '%OCAN Staff Assessment%' or l.label like '%OCAN Client Self Assessment%') order by i.createdOn desc",
			new Object[] { after });
	}

	public Intake getLatestIntakeByFacility(IntakeNode node, Integer clientId, Integer programId, Integer facilityId) {
		if (node == null || clientId == null) {
			throw new IllegalArgumentException("Parameters node and clientId must be non-null");
		}

		List<Intake> intakes = getIntakesByFacility(node, clientId, programId, facilityId);
		Intake intake = !intakes.isEmpty() ? intakes.get(0) : null;
		LOG.info("get latest intake: " + intake);

		return intake;
	}

	public Intake getLatestIntake(IntakeNode node, Integer clientId, Integer programId, Integer facilityId) {
		if (node == null || clientId == null) {
			throw new IllegalArgumentException("Parameters node and clientId must be non-null");
		}

		List<Intake> intakes = getIntakes(node, clientId, programId, facilityId);
		Intake intake = !intakes.isEmpty() ? intakes.get(0) : null;
		LOG.info("get latest intake: " + intake);

		return intake;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getIntakeClientsByFacilityId(Integer facilityId) {
		if(facilityId == null) {
			throw new IllegalArgumentException("Parameter facilityId must be non-null");
		}

		List<Integer> clientIds = getHibernateTemplate().find("select distinct i.clientId from Intake i where i.facilityId = ? order by i.clientId",
				new Object[] { facilityId });

		return clientIds;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getIntakeFacilityIds() {
		List<Integer> facilityIds = getHibernateTemplate().find("select distinct i.facilityId from Intake i ");

		return facilityIds;
	}

	public List<Intake> getIntakes(IntakeNode node, Integer clientId, Integer programId, Integer facilityId) {
		if (node == null || clientId == null) {
			throw new IllegalArgumentException("Parameters node and clientId must be non-null");
		}

		List<?> results = getHibernateTemplate().find("from Intake i where i.node = ? and i.clientId = ? and (i.facilityId=? or i.facilityId is null) order by i.createdOn desc",
				new Object[] { node, clientId, facilityId });
		List<Intake> intakes = convertToIntakes(results, programId);

		LOG.info("get intakes: " + intakes.size());

		return intakes;
	}

	/*
	 * 1. get nodes.
	 * foreach node, get intakes for that node
	 */
	public List<Intake> getIntakesByType(Integer formType, Integer clientId, Integer programId, Integer facilityId) {
		List<Intake> intake_results = new ArrayList<Intake>();
		if (formType == null || clientId == null) {
			throw new IllegalArgumentException("Parameters node and clientId must be non-null");
		}

		List<IntakeNode> nodes = this.getIntakeNodesByType(formType);

		for(IntakeNode node:nodes) {
			List<?> results = getHibernateTemplate().find("from Intake i where i.node = ? and i.clientId = ? and (i.facilityId=? or i.facilityId is null) order by i.createdOn desc",
					new Object[] { node, clientId, facilityId });
			List<Intake> intakes = convertToIntakes(results, programId);
			intake_results.addAll(intakes);
		}


		return intake_results;
	}

	public List<Intake> getIntakesByFacility(IntakeNode node, Integer clientId, Integer programId, Integer facilityId) {
		if (node == null || clientId == null) {
			throw new IllegalArgumentException("Parameters node and clientId must be non-null");
		}

		List<?> results = getHibernateTemplate().find("from Intake i where i.node = ? and i.clientId = ? and i.facilityId =? order by i.createdOn desc",
				new Object[] { node, clientId, facilityId });
		List<Intake> intakes = convertToIntakes(results, programId);

		LOG.info("get intakes: " + intakes.size());

		return intakes;
	}

	public List<Intake> getRegIntakes(List<IntakeNode> nodes, Integer clientId, Integer programId, Integer facilityId) {
		if (nodes.isEmpty() || clientId == null) {
			throw new IllegalArgumentException("Parameters nodes and clientId must be non-null");
		}

		List<?> results = getHibernateTemplate().find("from Intake i where i.node = ? and i.clientId = ? and (i.facilityId=? or i.facilityId is null) order by i.createdOn desc",
				new Object[] { nodes.get(0), clientId, facilityId });
		List<Intake> intakes = convertToIntakes(results, programId);

		for (int i=1; i<nodes.size(); i++) {
		    results = getHibernateTemplate().find("from Intake i where i.node = ? and i.clientId = ? and (i.facilityId=? or i.facilityId is null) order by i.createdOn desc",
				new Object[] { nodes.get(i), clientId, facilityId });
		    intakes.addAll(convertToIntakes(results, programId));
		}

		LOG.info("get intakes: " + intakes.size());
		return intakes;
	}

	public Intake getIntakeById(IntakeNode node, Integer intakeId, Integer programId, Integer facilityId) {
		if (node == null || intakeId == null) {
			throw new IllegalArgumentException("Parameters node and intakeId must be non-null");
		}

		List<?> results = getHibernateTemplate().find("from Intake i where i.node = ? and i.id = ? and (i.facilityId=? or i.facilityId is null) order by i.createdOn desc",
				new Object[] { node, intakeId, facilityId });
		List<Intake> intakes = convertToIntakes(results, programId);
		LOG.info("get intakes: " + intakes.size());
		Intake intake = !intakes.isEmpty() ? intakes.get(0) : null;

		return intake;
	}

	public Integer getIntakeNodeIdByIntakeId(Integer intakeId) {
		if (intakeId == null) {
			throw new IllegalArgumentException("Parameters intakeId must be non-null");
		}

		List<?> results = getHibernateTemplate().find("from Intake i where i.id = ? order by i.createdOn desc",
				new Object[] { intakeId });
		List<Intake> intakes = convertToIntakes(results, null);
		LOG.info("get intakes: " + intakes.size());
		Integer intakeNodeId = !intakes.isEmpty() ? intakes.get(0).getNode().getId() : null;

		return intakeNodeId;
	}

	public List<Integer> getIntakeNodesIdByClientId(Integer clientId) {
		return getIntakeNodesIdByClientId(clientId,null);
	}

	public List<Integer> getIntakeNodesIdByClientId(Integer clientId, Integer formType) {
		if (clientId == null) {
			throw new IllegalArgumentException("Parameters intakeId must be non-null");
		}

		List<?> results = getHibernateTemplate().find("from Intake i where i.clientId = ? order by i.createdOn desc",
				new Object[] { clientId });
		List<Intake> intakes = convertToIntakes(results, null);
		LOG.info("get intakes: " + intakes.size());

		List<Integer> intakeNodeIds = new ArrayList<Integer>();
		for (Intake i : intakes) {
			if(formType != null && i.getNode().getFormType() == formType) {
				intakeNodeIds.add(i.getNode().getId());
			}
		}
		for (int i=1; i<intakeNodeIds.size(); i++) {
		    if (intakeNodeIds.get(i).equals(intakeNodeIds.get(i-1))) {
			intakeNodeIds.remove(i);
			i--;
		    }
		}
		return intakeNodeIds;
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

	public void saveUpdateIntake(Intake intake) {
		getHibernateTemplate().saveOrUpdate(intake);
		getHibernateTemplate().flush();
		LOG.info("saved intake: " + intake);

		//return intakeId;
	}

	/**
	 * @throws SQLException
	 */
	public List<Integer> getIntakeIds2(Integer nodeId, Date startDate, Date endDate) throws SQLException {
		if (nodeId == null || startDate == null || endDate == null) {
			throw new IllegalArgumentException("Parameters nodeId, startDate and endDate must be non-null");
		}

		Connection c = DbConnectionFilter.getThreadLocalDbConnection();
		ArrayList<Integer> results = new ArrayList<Integer>();
		try {
			PreparedStatement ps = c.prepareStatement("select intake_id from intake where intake_node_id=? and creation_date>? and creation_date<?");
			ps.setInt(1, nodeId);
			ps.setTimestamp(2, new Timestamp(startDate.getTime()));
			ps.setTimestamp(3, new Timestamp(endDate.getTime()));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				results.add(rs.getInt(1));
			}
		}
		finally {
			c.close();
		}

		return results;
	}

	/**
	 * @see org.oscarehr.PMmodule.dao.GenericIntakeDAO#getLatestIntakeIds(Integer, java.util.Date, java.util.Date)
	 */
	public SortedSet<Integer> getLatestIntakeIds(Integer nodeId, Date startDate, Date endDate) {
		if (nodeId == null || startDate == null || endDate == null) {
			throw new IllegalArgumentException("Parameters node, startDate and endDate must be non-null");
		}

		// endDate is "YYYY-MM-DD 00:00:00", it has to be "YYYY-MM-DD 23:59:59"
		Calendar endCal = new GregorianCalendar();
		endCal.setTime(endDate);
		endCal.add(Calendar.DAY_OF_YEAR, 1);

		Calendar startCal = new GregorianCalendar();
		startCal.setTime(startDate);

		// wrong, only got the oldest first record: List<?> results = getHibernateTemplate().find("select i.id, max(i.createdOn) from Intake i where i.node.id =
		// ? and i.createdOn between ? and ? group by i.clientId", new Object[] { nodeId, startDate, endDate });
		//List<?> results = getHibernateTemplate()
		//		.find(
		//				"select i.id, max(i.createdOn) from Intake i where i.node.id = ? and i.createdOn between ? and ? and i.createdOn = (select max(ii.createdOn) from Intake ii where ii.clientId = i.clientId) group by i.clientId",
		//				new Object[] { nodeId, startCal.getTime(), endCal.getTime() });

		List<?> results = getHibernateTemplate()
		.find(
				"select i.id, i.createdOn from Intake i where i.node.id = ? and i.createdOn between ? and ? and i.createdOn = (select max(ii.createdOn) from Intake ii where ii.clientId = i.clientId group by ii.clientId)",
				new Object[] { nodeId, startCal, endCal });


		SortedSet<Integer> intakeIds = convertToIntegers(results);

		LOG.info("get latest intake ids: " + intakeIds.size());

		return intakeIds;
	}

	public SortedMap<Integer, SortedMap<String, ReportStatistic>> getReportStatistics(Hashtable<Integer,Integer> answerIds, Set<Integer> intakeIds) {
		if (intakeIds == null || answerIds == null) {
			throw new IllegalArgumentException("Parameters intakeIds, answerIds must be non-null");
		}

		SortedMap<Integer, SortedMap<String, ReportStatistic>> reportStatistics = new TreeMap<Integer, SortedMap<String, ReportStatistic>>();

		if (!intakeIds.isEmpty() && !answerIds.isEmpty()) {
			List<?> results = getHibernateTemplate().find(
					"select ia.node.id, ia.value, count(ia.value) from IntakeAnswer ia where ia.node.id in (" + convertToString(answerIds.keySet())
							+ ") and ia.intake.id in (" + convertToString(intakeIds) + ") group by ia.node.id, ia.value");
			convertToReportStatistics(results, intakeIds.size(), reportStatistics, answerIds);
		}
		LOG.info("get reportStatistics: " + reportStatistics.size());

		return reportStatistics;
	}

	/**
	 * This method will populate the totalIntakeCount and
	 */
	public GenericIntakeReportStatistics getReportStatistics2(List<Integer> intakeIds, Set<Integer> answerIds) throws SQLException {

		// this is a brute for simple algorithm, if the data set gets large we can group these into
		// smaller groups like doing 10 results at a time or something, we can't do all results
		// at the same time because the in clause doesn't have a dynamic length prepared statement
		// parameter and we can't put ?,?,? ... for each item because there's a maximum query string
		// length so we should limit it to at most blocks of 100's if we do that.

		GenericIntakeReportStatistics genericIntakeReportStatistics=new GenericIntakeReportStatistics();
		genericIntakeReportStatistics.totalIntakeCount = intakeIds.size();
		Object[] answerIdArray=answerIds.toArray();

		for (Integer intakeId : intakeIds) {
			Connection c = DbConnectionFilter.getThreadLocalDbConnection();
			try {
				PreparedStatement ps = c.prepareStatement("select intake_node_id,val from intake_answer where intake_id=? and intake_answer_id in "+SqlUtils.constructInClauseForStatements(answerIdArray));
				ps.setInt(1, intakeId);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					genericIntakeReportStatistics.addResult(rs.getInt("intake_node_id"), oscar.Misc.getString(rs,"val"));
				}
				else {
					LOG.error("Error, an intake has no answers? intakeId=" + intakeId);
				}
			}
			finally {
				c.close();
			}
		}

		return(genericIntakeReportStatistics);
	}

	public static class GenericIntakeReportStatistics {
		public int totalIntakeCount = 0;
		/**
		 * This is a map of <intake_node_id, <intake_answer.val, count>>
		 */
		public HashMap<Integer, AccumulatorMap<String>> intakeNodeResults = new HashMap<Integer, AccumulatorMap<String>>();

		public void addResult(int intakeNodeId, String answer) {
			AccumulatorMap<String> accumulatorMap = intakeNodeResults.get(intakeNodeId);

			if (accumulatorMap == null) {
				accumulatorMap = new AccumulatorMap<String>();
				intakeNodeResults.put(intakeNodeId, accumulatorMap);
			}

			accumulatorMap.increment(answer);
		}
	}

	// Private

	private List<Intake> convertToIntakes(List<?> results, Integer programId) {
		List<Intake> intakes = new ArrayList<Intake>();

		if (results != null) {
			for (Object o : results) {
				Intake intake = (Intake) o;

				Demographic client = getHibernateTemplate().get(Demographic.class, intake.getClientId());
				intake.setClient(client);

				Provider staff = getHibernateTemplate().get(Provider.class, intake.getStaffId());
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

	private void convertToReportStatistics(List<?> results, int size, SortedMap<Integer, SortedMap<String, ReportStatistic>> reportStatistics, Hashtable<Integer, Integer> resultHash) {
		for (Object o : results) {
			Object[] tuple = (Object[]) o;

			Integer nodeId = (Integer) tuple[0];
			String value = (String) tuple[1];
			Integer count = Integer.valueOf(tuple[2].toString());

			if (!reportStatistics.containsKey(nodeId)) {
			    reportStatistics.put(nodeId, new TreeMap<String, ReportStatistic>());
			}

			SortedMap<String, ReportStatistic> rpStNodeId = reportStatistics.get(nodeId);
			if (rpStNodeId.containsKey(value)) {
			    count += rpStNodeId.get(value).getCount();
			    rpStNodeId.remove(value);
			}
			rpStNodeId.put(value, new ReportStatistic(count, size));
		}
	}



	public List<IntakeNode> getIntakeNodesByType(Integer formType) {
		return this.getHibernateTemplate().find("From IntakeNode n where n.formType = ? and n.publish_by is not null", new Object[] {formType});
	}
}
