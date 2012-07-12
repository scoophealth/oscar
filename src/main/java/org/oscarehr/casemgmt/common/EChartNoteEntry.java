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
package org.oscarehr.casemgmt.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EChartNoteEntry {

	private Object id;
	private Date date;
	private String providerNo;
	private int programId;
	private String role;
	private String type;
	

	private List<Integer> issueIds = new ArrayList<Integer>();

	public Object getId() {
    	return id;
    }

	public void setId(Object id) {
    	this.id = id;
    }

	public Date getDate() {
    	return date;
    }

	public void setDate(Date date) {
    	this.date = date;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public String getRole() {
    	return role;
    }

	public void setRole(String role) {
    	this.role = role;
    }

	public List<Integer> getIssueIds() {
    	return issueIds;
    }

	public void setIssueIds(List<Integer> issueIds) {
    	this.issueIds = issueIds;
    }

	public int getProgramId() {
    	return programId;
    }

	public void setProgramId(int programId) {
    	this.programId = programId;
    }
	

	public String getType() {
    	return type;
    }

	public void setType(String type) {
    	this.type = type;
    }

	public static Comparator<EChartNoteEntry> getDateComparator() {
		return new Comparator<EChartNoteEntry>() {
			public int compare(EChartNoteEntry note1, EChartNoteEntry note2) {
				if (note1 == null || note2 == null) {
					return 0;
				}

				return note1.getDate().compareTo(note2.getDate());
			}
		};

	}
	
	public static Comparator<EChartNoteEntry> getDateComparatorDesc() {
		return new Comparator<EChartNoteEntry>() {
			public int compare(EChartNoteEntry note1, EChartNoteEntry note2) {
				if (note1 == null || note2 == null) {
					return 0;
				}

				return note2.getDate().compareTo(note1.getDate());
			}
		};

	}
	
	public String toString() {
		return "NoteEntry:" + getType() + ":" + getId() + ":" + getDate();
	}
}
