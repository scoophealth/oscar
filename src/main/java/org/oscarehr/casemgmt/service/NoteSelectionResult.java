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

import org.oscarehr.casemgmt.web.NoteDisplay;

/**
 * Holds results of a note search. 
 *
 */
public class NoteSelectionResult {

	private boolean moreNotes;
	private List<NoteDisplay> notes = new ArrayList<NoteDisplay>();

	/**
	 * Gets flag indicating that there are more notes that actually
	 * being provided in the {@link #getNotes()} list. This is usually set when
	 * notes are limited in the search criteria.
	 * 
	 * @return
	 * 		Returns true if there are more notes available than returned and false otherwise
	 */
	public boolean isMoreNotes() {
		return moreNotes;
	}

	/**
	 * Sets flag indicating that there are more notes that actually
	 * being provided in the {@link #getNotes()} list. This is usually set when
	 * notes are limited in the search criteria.
	 * 
	 * @param moreNotes
	 * 		Flag that should be set to true if there are more notes available 
	 * than returned and false otherwise		
	 */
	public void setMoreNotes(boolean moreNotes) {
		this.moreNotes = moreNotes;
	}

	/**
	 * Gets the display notes to be rendered to the user
	 * 
	 * @return
	 * 		Returns the notes
	 */
	public List<NoteDisplay> getNotes() {
		return notes;
	}

	/**
	 * Sets the display notes to be rendered to the user
	 * 
	 * @param notes
	 * 		Notes to be set
	 */
	public void setNotes(List<NoteDisplay> notes) {
		this.notes = notes;
	}

	@Override
    public String toString() {
	    return "NoteSelectionResult [moreNotes=" + moreNotes + ", notes=" + notes + "]";
    }

}
