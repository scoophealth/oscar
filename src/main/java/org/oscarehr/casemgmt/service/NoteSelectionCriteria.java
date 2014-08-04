/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.casemgmt.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds configuration parameters for the note search.
 */
public class NoteSelectionCriteria {

	public static final int DEFAULT_MAX_RESULTS = 20;

	private int maxResults = DEFAULT_MAX_RESULTS;
	private int firstResult;
	private int demographicId;
	private String userRole;
	private String userName;
	private String noteSort;
	private String programId;
	private List<String> roles = new ArrayList<String>();
	private List<String> providers = new ArrayList<String>();
	private List<String> issues = new ArrayList<String>();
	private boolean sliceFromEndOfList = true;  //historically this is the default

	/**
	 * Gets demographic id to obtains notes for.
	 * 
	 * @return
	 * 		Returns the notes.
	 */
	public int getDemographicId() {
		return demographicId;
	}

	/**
	 * Sets demographic id to obtains notes for.
	 * 
	 * @param demographicId
	 * 		Demographic ID to get notes for
	 */
	public void setDemographicId(int demographicId) {
		this.demographicId = demographicId;
	}

	/**
	 * Gets role of the user requesting the note.
	 * 
	 * @return
	 * 		Returns the current user role
	 */
	public String getUserRole() {
		return userRole;
	}

	/**
	 * Sets role of the user requesting the note.
	 * 
	 * @param userRole
	 * 		The current user role to set
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	/**
	 * Gets ID of the user requesting the note report 
	 * 
	 * @return
	 * 		Returns the current user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets ID of the user requesting the note report 
	 * 
	 * @param userName
	 * 		The current user name to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets note sort expected for the result set. Two magic values are <code>observation_date_desc, observation_date_asc</code>
	 * 
	 * @return
	 * 		Returns the sort order
	 */
	public String getNoteSort() {
		return noteSort;
	}

	/**
	 * Checks if the {@link #getNoteSort()} specifies a valid value.
	 * 
	 * @return
	 * 		Returns true if {@link #getNoteSort()} field should be used for sorting the results
	 * 		and false otherwise.
	 */
	public boolean isNoteSortSpecified() {
		return getNoteSort() != null && !getNoteSort().trim().isEmpty();
	}

	public void setNoteSort(String noteSort) {
		this.noteSort = noteSort;
	}

	/**
	 * Gets program ID for the filtering out notes
	 * 
	 * @return
	 * 		Returns the program id
	 */
	public String getProgramId() {
		return programId;
	}

	/**
	 * Sets program ID for the filtering out notes
	 * 
	 * @param programId
	 * 		Program ID to set
	 */
	public void setProgramId(String programId) {
		this.programId = programId;
	}

	/**
	 * Gets the roles filter to be used for filtering off matching notes.
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * Sets the roles filter to be used for filtering off matching notes.
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}


	/**
	 * Gets the providers filter to be used for filtering off matching notes.
	 */
	public List<String> getProviders() {
		return providers;
	}

	/**
	 * Sets the providers filter to be used for filtering off matching notes.
	 */
	public void setProviders(List<String> providers) {
		this.providers = providers;
	}

	/**
	 * Gets the issues filter to be used for filtering off matching notes. 
	 * 
	 */
	public List<String> getIssues() {
		return issues;
	}

	/**
	 * Sets the issues filter to be used for filtering off matching notes. 
	 * 
	 * @param issues
	 * 		Issues to set 
	 */
	public void setIssues(List<String> issues) {
		this.issues = issues;
	}

	/**
	 * Gets the maximum number of results to retrieve. 
	 * 
	 * @return
	 * 		Max results to get.
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * Sets the maximum number of results to retrieve. 
	 * 
	 * @param maxResults
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * Gets the position of the first result to retrieve.
	 *  
	 * @return
	 * 		The start position of the first result, numbered from 0
	 */
	public int getFirstResult() {
		return firstResult;
	}

	/**
	 * Set the position of the first result to retrieve.
	 *  
	 * @param firstResult
	 * 		The start position of the first result, numbered from 0
	 */
	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public boolean isSliceFromEndOfList() {
	    return sliceFromEndOfList;
    }

	public void setSliceFromEndOfList(boolean sliceFromEndOfList) {
	    this.sliceFromEndOfList = sliceFromEndOfList;
    }

	@Override
    public String toString() {
	    return "NoteSelectionCriteria [maxResults=" + maxResults + ", firstResult=" + firstResult + ", demographicId=" + demographicId + ", userRole=" + userRole + ", userName=" + userName + ", noteSort=" + noteSort + ", programId=" + programId + ", roles=" + roles + ", providers=" + providers + ", issues=" + issues + "]";
    }

}
