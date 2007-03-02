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

import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;

public interface GenericIntakeDAO {

	/**
	 * Get most recent intake with given node for given client
	 * 
	 * @param node
	 *            node id
	 * @param clientId
	 *            client id
	 * @return Most recent intake
	 */
	public Intake getIntake(IntakeNode node, Integer clientId);

	/**
	 * Save a new intake
	 * 
	 * @param intake
	 *            given intake
	 * @return intake id
	 */
	public Integer saveIntake(Intake intake);

}
