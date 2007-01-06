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

import org.caisi.dao.DemographicDAO;
import org.oscarehr.PMmodule.model.Demographic;

/**
 * Manager Interface for Demographics
 * Implementing class will provide the business logic
 *
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public interface DemographicManagerTickler {
	
	public void setDemographicDAO(DemographicDAO demographicDAO);
	/**
	 * Get a demographic using it's primary Id
	 * @param demographic_no Demographic Id
	 * @return A Demographic object
	 */
	public Demographic getDemographic(String demographic_no);
	
	/**
	 * Get a list of all demographic objects in the database
	 * @return List of Demographic objects
	 */
	public List getDemographics();
	
	
	public List getProgramIdByDemoNo(String demoNo);
	public List getDemoProgram(String demoNo);
	
}
