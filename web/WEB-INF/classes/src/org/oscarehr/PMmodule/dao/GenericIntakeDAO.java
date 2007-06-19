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
package org.oscarehr.PMmodule.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.common.model.ReportStatistic;

public interface GenericIntakeDAO {

	/**
	 * Get most recent intake for given node with given client
	 * 
	 * @param node
	 *            node
	 * @param clientId
	 *            client id
	 * @param programId
	 *            program id
	 * @return Most recent intake
	 */
	public Intake getLatestIntake(IntakeNode node, Integer clientId, Integer programId);

	/**
	 * Get intakes for given node with given client
	 * 
	 * @param node
	 *            node
	 * @param clientId
	 *            client id
	 * @param programId
	 *            program id
	 * @return Intakes
	 */
	public List<Intake> getIntakes(IntakeNode node, Integer clientId, Integer programId);

	/**
	 * Save an intake
	 * 
	 * @param intake
	 *            intake
	 * @return saved intake id
	 */
	public Integer saveIntake(Intake intake);

	/**
	 * Get most recent intake ids for given node
	 * 
	 * @param nodeId
	 *            node
	 * @param startDate
	 *            start date
	 * @param endDate
	 *            end date
	 * @return Most recent intakes
	 */
	public SortedSet<Integer> getLatestIntakeIds(Integer nodeId, Date startDate, Date endDate);

	/**
	 * Get map of (id, element) tuple to element count for given intake and answer ids
	 * 
	 * @param answerIds
	 *            answer ids
	 * @param intakeIds
	 *            intake ids
	 * 
	 * @return Map of (id, element) tuple to element count
	 */
	public SortedMap<Integer, SortedMap<String, ReportStatistic>> getReportStatistics(Set<Integer> answerIds, Set<Integer> intakeIds);

}