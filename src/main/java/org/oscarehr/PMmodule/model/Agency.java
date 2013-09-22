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

package org.oscarehr.PMmodule.model;

import java.io.Serializable;
import java.util.Map;

/**
 * This is the object class that relates to the agency table. Any customizations belong here.
 */
public class Agency implements Serializable {

	private static Agency localAgency;
	private static Map<?, ?> agencyMap;

	private int hashCode = Integer.MIN_VALUE;// primary key

	private Long id;// fields
	private Integer intakeQuick;
	private String intakeQuickState="HSC";
	private Integer intakeIndepth;
	private String intakeIndepthState="HSC";

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

	// constructors
	public Agency() {
		// do nothing
	}

	/**
	 * Constructor for primary key
	 */
	public Agency(Long id) {
		this.setId(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Agency(Long id, Integer intakeQuick, String intakeQuickState, String intakeIndepthState) {

		this.setId(id);
		this.setIntakeQuick(intakeQuick);
		this.setIntakeQuickState(intakeQuickState);
		this.setIntakeIndepthState(intakeIndepthState);
	}

	public boolean areHousingProgramsVisible(String intakeType) {
		boolean visible = false;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			visible = getIntakeQuickState().contains("H");
		}
		else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			visible = getIntakeIndepthState().contains("H");
		}

		return visible;
	}

	public boolean areCommunityProgramsVisible(String intakeType) {
		boolean visible = false;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			visible = getIntakeQuickState().contains("C");
		}
		else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			visible = getIntakeIndepthState().contains("C");
		}

		return visible;
	}
	
	public boolean areServiceProgramsVisible(String intakeType) {
		boolean visible = false;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			visible = getIntakeQuickState().contains("S");
		}
		else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			visible = getIntakeIndepthState().contains("S");
		}

		return visible;
	}

	public boolean areExternalProgramsVisible(String intakeType) {
		boolean visible = false;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			//"S" should be changed to be "E" in the future if still use this external program function.
			visible = getIntakeQuickState().contains("S"); 			
		}
		else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			//"S" should be changed to be "E" in the future if still use this external program function.
			visible = getIntakeIndepthState().contains("S");
		}

		return visible;
	}
	
	/**
	 * Return the unique identifier of this class generator-class="native" column="id"
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(Long id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: intake_quick
	 */
	public Integer getIntakeQuick() {
		return intakeQuick;
	}

	/**
	 * Set the value related to the column: intake_quick
	 * 
	 * @param intakeQuick
	 *            the intake_quick value
	 */
	public void setIntakeQuick(Integer intakeQuick) {
		this.intakeQuick = intakeQuick;
	}

	/**
	 * Return the value associated with the column: intake_quick_state
	 */
	public String getIntakeQuickState() {
		return intakeQuickState;
	}

	/**
	 * Set the value related to the column: intake_quick_state
	 * 
	 * @param intakeQuickState
	 *            the intake_quick_state value
	 */
	public void setIntakeQuickState(String intakeQuickState) {
		this.intakeQuickState = intakeQuickState;
	}

	/**
	 * Return the value associated with the column: intake_indepth
	 */
	public Integer getIntakeIndepth() {
		return intakeIndepth;
	}

	/**
	 * Set the value related to the column: intake_indepth
	 * 
	 * @param intakeIndepth
	 *            the intake_indepth value
	 */
	public void setIntakeIndepth(Integer intakeIndepth) {
		this.intakeIndepth = intakeIndepth;
	}

	/**
	 * Return the value associated with the column: intake_indepth_state
	 */
	public String getIntakeIndepthState() {
		return intakeIndepthState;
	}

	/**
	 * Set the value related to the column: intake_indepth_state
	 * 
	 * @param intakeIndepthState
	 *            the intake_indepth_state value
	 */
	public void setIntakeIndepthState(String intakeIndepthState) {
		this.intakeIndepthState = intakeIndepthState;
	}


	public boolean equals(Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof Agency)) return false;
		else {
			Agency agency = (Agency) obj;
			if (null == this.getId() || null == agency.getId()) return false;
			else return (this.getId().equals(agency.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}
}
