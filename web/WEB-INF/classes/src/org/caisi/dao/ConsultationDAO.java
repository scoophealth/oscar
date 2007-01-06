/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.caisi.dao;

import java.util.List;

import org.caisi.model.Consultation;

/**
 * Data Access Object for Consultation Requests
 * 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface ConsultationDAO extends DAO {
	
	/**
	 * Get all consultations for a patient
	 * @param demographic_no Demographic Id
	 * @return A List of Consultation objects
	 */
	public List getConsultations(String demographic_no);
	
	/**
	 * Get a set of consultations for a patient, filtered by status
	 * For set of statuses, see OSCAR table.
	 * 
	 * @param demographic_no Demographic Id
	 * @param status the status
	 * @return A list of Consultations objects
	 */
	public List getConsultationsByStatus(String demographic_no, String status);
	
	/**
	 * Get a Consultation using it's primary identifier
	 * @param requestId The id
	 * @return The Consultation
	 */
	public Consultation getConsultation(Long requestId);
}
