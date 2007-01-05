/*
* 
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

package org.oscarehr.casemgmt.service.impl;

import org.caisi.event.OscarCaisiContext;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.service.CaseManagementManager;

import ca.macedo.events.client.CaisiComponentHelperFactory;

public class CMMHelper implements CaisiComponentHelperFactory{
	CaseManagementManager manager=null;
	
	public Object newHelper(OscarCaisiContext ctx){
		Helper p=new Helper();
		p.ctx=ctx;
		return p;
	}
	public String getBeanName(){
		return "cmm";
	}
	
	/**
	 * Exposed as "cmm" in scripting
	 */
	public class Helper{
		private OscarCaisiContext ctx=null;
		
		public CaseManagementCPP getCPP(){
			return manager.getCPP(ctx.getClientID());
		}
		// Add more here
	}

	public CaseManagementManager getManager() {
		return manager;
	}
	public void setManager(CaseManagementManager manager) {
		this.manager = manager;
	}
}
