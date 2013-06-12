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
package org.oscarehr.integration.nclass.clientRegistry.model;

import java.util.ArrayList;
import java.util.List;

public class Candidate {
	
	private String first;
	
	private String last;

	private List<Identifier> ids = new ArrayList<Identifier>();

	public List<Identifier> getIds() {
		return ids;
	}

	public void setIds(List<Identifier> ids) {
		this.ids = ids;
	}

	public boolean addId(String id, String source) {
		return addId(new Identifier(id, source));
	}
	
	public boolean addId(Identifier id) {
		if (!ids.contains(id)) {
			return ids.add(id);
		}
		return false;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

}
