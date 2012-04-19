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


package oscar.oscarDemographic.data;

import java.util.ArrayList;

public class DemographicAddResult {
	ArrayList<String> warnings = null;
	boolean added = false;
	String id = null;

	public void addWarning(String str) {

		if (warnings == null) {
			warnings = new ArrayList<String>();
		}
		warnings.add(str);
	}

	public String[] getWarnings() {
		String[] s = {};
		if (warnings != null) {
			s = warnings.toArray(s);
		}
		return s;
	}

	public ArrayList<String> getWarningsCollection() {
		if (warnings == null) {
			warnings = new ArrayList<String>();
		}
		return warnings;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean wasAdded() {
		return added;
	}

	public void setAdded(boolean b) {
		added = b;
	}
}
