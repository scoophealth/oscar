/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.web.report;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.quatro.service.QuatroReportManager;

public class QuatroReportListAction extends DispatchAction {
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return reportlist(mapping,form,request,response);
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		QuatroReportListForm myForm = (QuatroReportListForm) form;
		String chkNo= myForm.getChkDel();
		if(chkNo==null) return reportlist(mapping,form,request,response);
		
		QuatroReportManager reportManager = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
        		getServlet().getServletContext()).getBean("quatroReportManager");
		
		chkNo = chkNo.substring(1);
		StringBuilder str = new StringBuilder();
		String[] sArray = chkNo.split(","); 
		for(int i=0;i<sArray.length;i++){
		  String param = (String)request.getParameter("p" + sArray[i]);
		  if(param!=null) str.append("," + param);
		}

		if(str.toString()!=null){
		  String templateNo= str.toString();
		  if("".equals(templateNo)==false){
			  templateNo = templateNo.substring(1);
			  reportManager.DeleteReportTemplates(templateNo);
		  }
		}  
		return reportlist(mapping,form,request,response);
	}
	
	public ActionForward reportlist(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String providerNo = (String)request.getSession().getAttribute("user");
		QuatroReportManager reportManager = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
        		getServlet().getServletContext()).getBean("quatroReportManager");
		List reports = reportManager.GetReportGroupList(providerNo);
		QuatroReportListForm qform = (QuatroReportListForm) form;
		qform.setReportGroups(reports);
		qform.setProvider(providerNo);
		return mapping.findForward("reportlist");
	}
}
