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

package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.PMmodule.model.Program;

public interface ProgramDao {
	
	public boolean isBedProgram(Integer programId);
	
	public boolean isServiceProgram(Integer programId);
	
	public boolean isCommunityProgram(Integer programId);
	
	public Program getProgram(Integer programId);

	public String getProgramName(Integer programId);

	public List<Program> getAllPrograms();

	public List<Program> getAllActivePrograms();
	
	public List<Program> getProgramsByAgencyId(String agencyId);

	public Program[] getBedPrograms();

	public List getServicePrograms();

	public Program[] getCommunityPrograms();
	
	public void saveProgram(Program program);

	public void removeProgram(Integer programId);

	public List search(Program criteria);

	public void resetHoldingTank();

	public Program getHoldingTankProgram();

	public boolean programExists(Integer programId);
	
	public List<Program> getLinkedServicePrograms(Integer bedProgramId,Integer clientId);
	
}
