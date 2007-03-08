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
package org.oscarehr.PMmodule.service;

import java.util.List;

import org.oscarehr.PMmodule.model.Intake;

public interface GenericIntakeManager {
	
	public Intake copyQuickIntake(Integer clientId, String staffId);
	
	public Intake copyIndepthIntake(Integer clientId, String staffId);
	
	public Intake copyProgramIntake(Integer clientId, Integer programId, String staffId);
	
	public Intake createQuickIntake(String providerNo);

	public Intake createIndepthIntake(String providerNo);

	public Intake createProgramIntake(Integer programId, String providerNo);
	
	public Intake getMostRecentQuickIntake(Integer clientId);

	public Intake getMostRecentIndepthIntake(Integer clientId);

	public Intake getMostRecentProgramIntake(Integer clientId, Integer programId);

	public List<Intake> getQuickIntakes(Integer clientId);

	public List<Intake> getIndepthIntakes(Integer clientId);

	public List<Intake> getProgramIntakes(Integer clientId);

	public Integer saveIntake(Intake intake);

}
