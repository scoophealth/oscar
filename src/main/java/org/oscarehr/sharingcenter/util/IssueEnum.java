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

package org.oscarehr.sharingcenter.util;

import java.util.ArrayList;
import java.util.List;

public enum IssueEnum {

	OngoingConcerns(68), MedicalHistory(67), FamilyMedicalHistory(70), SocialHistory(66);

	private long id;

	private IssueEnum(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	/**
	 * @author Nityan Khanna
	 * Gets all the issue values for easy iteration and further use.
	 * @return Returns the issue enum values as a list.
	 */
	public static List<IssueEnum> getAllIssueValues() {
		
		List<IssueEnum> items = new ArrayList<IssueEnum>();
		
		items.add(OngoingConcerns);
		items.add(MedicalHistory);
		items.add(SocialHistory);
		items.add(FamilyMedicalHistory);
		
		return items;
	}
}
