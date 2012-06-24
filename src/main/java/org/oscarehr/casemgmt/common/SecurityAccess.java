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

package org.oscarehr.casemgmt.common;

public class SecurityAccess extends BasicTag
{

	private String accessName;
	private String accessType;
	private String providerNo;
	private String demoNo;
	private String programId;
	private String reverse;
	
	
	public void setAccessName(String accessName)
	{
		this.accessName = accessName;
	}
	public void setProviderNo(String providerNo)
	{
		this.providerNo = providerNo;
	}
	
	public void setDemoNo(String demoNo)
	{
		this.demoNo = demoNo;
	}
	public void setAccessType(String accessType)
	{
		this.accessType = accessType;
	}
	
	public String getProgramId()
	{
		return programId;
	}
	public void setProgramId(String programId)
	{
		this.programId = programId;
	}
	
	public void setReverse(String reverse) {
		this.reverse = reverse;			
	}
	
	public int doStartTag()
	{
		if ("".equalsIgnoreCase(programId)||"null".equalsIgnoreCase(programId)) programId="0";
		boolean hasAccess=getCaseManagementManager().hasAccessRight(accessName,accessType,providerNo,demoNo,programId);
		if(reverse != null && reverse.equals("true")) { hasAccess = !hasAccess;}
		if (hasAccess) return EVAL_BODY_INCLUDE;
		else return SKIP_BODY;
	}
}
