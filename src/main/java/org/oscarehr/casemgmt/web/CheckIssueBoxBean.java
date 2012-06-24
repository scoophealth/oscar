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

package org.oscarehr.casemgmt.web;

import java.io.Serializable;

import org.oscarehr.casemgmt.model.Issue;


public class CheckIssueBoxBean implements Serializable
{
	private boolean isChecked = false;
	private Issue issue = new Issue();
	private boolean community = false;
	private String communityString = Boolean.toString(community);
	
	public boolean isChecked()
	{
		return isChecked;
	}
	public void setChecked(boolean isChecked)
	{
		this.isChecked = isChecked;
	}
	public Issue getIssue()
	{
		return issue;
	}
	public void setIssue(Issue issue)
	{
		this.issue = issue;
	}
	public boolean getCommunity() {
		return community;
	}
	public void setCommunity(boolean isCommunity) {
		this.community = isCommunity;
		this.communityString = Boolean.toString(isCommunity);
	}
	public String getCommunityString() {
		return communityString;
	}
	

}
