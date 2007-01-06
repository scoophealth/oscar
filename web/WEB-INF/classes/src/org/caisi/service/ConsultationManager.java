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


package org.caisi.service;

import java.util.List;

import org.caisi.model.Consultation;
/**
 * Manager Interface for Consultations
 * Implementing class will provide the business logic
 *
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface ConsultationManager {
	/**
	 * Get all consultations for a patient
	 * @param demographic_no Demographic Id
	 * @return List of Consultation objects
	 */
	public List getConsultations(String demographic_no);
	
	/**
	 * Get all consultations for a patient filtered by status
	 * @param demographic_no
	 * @param status
	 * @return
	 */
	public List getConsultationsByStatus(String demographic_no, String status);
	
	/**
	 * Get a single consultation using it's primary Id
	 * @param requestId
	 * @return
	 */
	public Consultation getConsultation(String requestId);
}
