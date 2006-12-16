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
