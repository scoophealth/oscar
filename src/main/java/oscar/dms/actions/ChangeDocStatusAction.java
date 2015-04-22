/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package oscar.dms.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.dms.EDocUtil;
import oscar.dms.data.ChangeDocStatusForm;

public class ChangeDocStatusAction extends DispatchAction {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		
		ChangeDocStatusForm fm = (ChangeDocStatusForm) form;
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_edoc", "w", null)) {
			throw new SecurityException("missing required security object (_edoc)");
		}
	
		if ((fm.getDocTypeD()!="")&&(fm.getStatusD()!="")) {
				EDocUtil.changeDocTypeStatusSQL(fm.getDocTypeD(),"Demographic",fm.getStatusD());
				
		} 
		
		if ((fm.getDocTypeP()!="")&&(fm.getStatusP()!="")){
				EDocUtil.changeDocTypeStatusSQL(fm.getDocTypeP(),"Provider",fm.getStatusP());
				
		}
		
		return(mapping.findForward("success"));
		
	}
	
	
}
