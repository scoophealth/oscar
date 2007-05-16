/*
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
package org.oscarehr.PMmodule.model;

import java.util.Map;

import org.oscarehr.PMmodule.model.base.BaseAgency;

/**
 * This is the object class that relates to the agency table. Any customizations belong here.
 */
public class Agency extends BaseAgency {

	private static final long serialVersionUID = 1L;

	private static Agency localAgency;
	private static Map<?, ?> agencyMap;

	public static Agency getLocalAgency() {
		return localAgency;
	}

	public static Map<?, ?> getAgencyMap() {
		return agencyMap;
	}

	public static void setLocalAgency(Agency agency) {
		localAgency = agency;
	}

	public static void setAgencyMap(Map<?, ?> map) {
		agencyMap = map;
	}

	public static String getAgencyName(Long agencyId) {
		if (agencyMap != null) {
			Agency agency = (Agency) agencyMap.get(agencyId);

			if (agency != null) {
				return agency.getName();
			}
		}

		return "Unknown (" + agencyId + ")";
	}

	/* [CONSTRUCTOR MARKER BEGIN] */

	public Agency() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Agency(java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Agency(java.lang.Long id, java.lang.Integer intakeQuick, java.lang.String intakeQuickState, java.lang.String intakeIndepthState, java.lang.String name, boolean local, boolean integratorEnabled) {
		super(id, intakeQuick, intakeQuickState, intakeIndepthState, name, local, integratorEnabled);
	}

	/* [CONSTRUCTOR MARKER END] */

	public boolean areHousingProgramsVisible(String intakeType) {
		boolean visible = false;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			visible = getIntakeQuickState().contains("H");
		} else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			visible = getIntakeIndepthState().contains("H");
		}

		return visible;
	}

	public boolean areServiceProgramsVisible(String intakeType) {
		boolean visible = false;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			visible = getIntakeQuickState().contains("S");
		} else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			visible = getIntakeIndepthState().contains("S");
		}

		return visible;
	}

}