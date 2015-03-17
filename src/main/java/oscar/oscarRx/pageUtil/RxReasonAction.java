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


package oscar.oscarRx.pageUtil;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.oscarehr.common.dao.DrugReasonDao;
import org.oscarehr.common.dao.Icd9Dao;
import org.oscarehr.common.model.DrugReason;
import org.oscarehr.common.model.Icd9;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;


public final class RxReasonAction extends DispatchAction {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);


	/*
	 * Needed for a new Drug Reason
	 *
	private Integer drugId = null;
	private String codingSystem = null;    // (icd9,icd10,etc...) OR protocol
	private String code = null;   // (250 (for icd9) or could be the protocol identifier )
	private String comments = null;
	private Boolean primaryReasonFlag;
	private String providerNo = null;
	private Integer demographicNo = null;
	 */
    public ActionForward addDrugReason(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {

		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_rx", "r", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}
		
    		MessageResources mResources = MessageResources.getMessageResources( "oscarResources" );
    		DrugReasonDao drugReasonDao     = (DrugReasonDao) SpringUtils.getBean("drugReasonDao");
    		Icd9Dao icd9Dao = (Icd9Dao)  SpringUtils.getBean("Icd9DAO");

            String codingSystem = request.getParameter("codingSystem");
            String primaryReasonFlagStr = request.getParameter("primaryReasonFlag");
            String comments = request.getParameter("comments");
            String code = request.getParameter("code");

            String drugIdStr = request.getParameter("drugId");
            String demographicNo = request.getParameter("demographicNo");
            String providerNo = (String) request.getSession().getAttribute("user");

            request.setAttribute("drugId",Integer.parseInt(drugIdStr));
    		request.setAttribute("demoNo",Integer.parseInt(demographicNo));

    		if(code != null && code.trim().equals("")){
				request.setAttribute("message", mResources.getMessage("SelectReason.error.codeEmpty"));
    			return (mapping.findForward("success"));
    		}

    		List<Icd9> list = icd9Dao.getIcd9Code(code);
            if (list.size() == 0){
				request.setAttribute("message", mResources.getMessage("SelectReason.error.codeValid"));
            	return (mapping.findForward("success"));
            }

            if(drugReasonDao.hasReason(Integer.parseInt(drugIdStr),codingSystem, code, true)){
            	request.setAttribute("message", mResources.getMessage("SelectReason.error.duplicateCode"));
            	return (mapping.findForward("success"));
            }

            MiscUtils.getLogger().debug("addDrugReasonCalled codingSystem "+codingSystem+ " code "+code+ " drugIdStr "+drugIdStr);



            boolean primaryReasonFlag = true;
            if(!"true".equals(primaryReasonFlagStr)){
            	primaryReasonFlag = false;
            }

            DrugReason dr = new DrugReason();

            dr.setDrugId(Integer.parseInt(drugIdStr));
            dr.setProviderNo(providerNo);
            dr.setDemographicNo(Integer.parseInt(demographicNo));

            dr.setCodingSystem(codingSystem);
            dr.setCode(code);
            dr.setComments(comments);
            dr.setPrimaryReasonFlag(primaryReasonFlag);
            dr.setArchivedFlag(false);
            dr.setDateCoded(new Date());

            drugReasonDao.addNewDrugReason(dr);

            String ip = request.getRemoteAddr();
            LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DRUGREASON, ""+dr.getId() , ip,demographicNo,dr.getAuditString());

            return (mapping.findForward("close"));
    }


    public ActionForward archiveReason(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {

		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_rx", "r", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}
		
    	MessageResources mResources = MessageResources.getMessageResources( "ApplicationResources" );
		DrugReasonDao drugReasonDao     = (DrugReasonDao) SpringUtils.getBean("drugReasonDao");
		String reasonId = request.getParameter("reasonId");
		String archiveReason = request.getParameter("archiveReason");

		DrugReason drugReason = drugReasonDao.find(Integer.parseInt(reasonId));

		drugReason.setArchivedFlag(true);
		drugReason.setArchivedReason(archiveReason);

		drugReasonDao.merge(drugReason);

		request.setAttribute("drugId",drugReason.getDrugId());
		request.setAttribute("demoNo",drugReason.getDemographicNo());

		String ip = request.getRemoteAddr();
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ARCHIVE, LogConst.CON_DRUGREASON, ""+drugReason.getId() , ip,""+drugReason.getDemographicNo(), drugReason.getAuditString());

		request.setAttribute("message", mResources.getMessage("SelectReason.msg.archived"));
		return (mapping.findForward("success"));
    }
}



/*
 public ActionForward unspecified(ActionMapping mapping,
			 ActionForm form,
			 HttpServletRequest request,
			 HttpServletResponse response)
    throws IOException, ServletException {

    	 MiscUtils.getLogger().debug("addDrugReasonCalled unspecified");



    	return null;
    }
*/
