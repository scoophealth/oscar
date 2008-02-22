package com.quatro.web.report;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.quatro.model.*;
import com.quatro.service.QuatroReportManager;
import com.quatro.util.*;
import java .util.ArrayList;


public class QuatroReportTemplateAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		QuatroReportSaveTemplateForm myForm = (QuatroReportSaveTemplateForm)form;
		if((String)request.getParameter("Save")!=null)
		{
			btnSave_Click(myForm, request);
		}
		ActionForward forward = mapping.findForward("success");
		return forward;
	}

	public void btnSave_Click(QuatroReportSaveTemplateForm myForm, HttpServletRequest request)
	{
		ReportValue rptValue = (ReportValue)request.getSession().getAttribute(DataViews.REPORT);

		QuatroReportManager reportManager = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
        		getServlet().getServletContext()).getBean("quatroReportManager");

		ReportTempValue temp = rptValue.getReportTemp();
		
        if ("optOld".equals((String)request.getParameter("optSaveAs"))==false){
            temp.setTemplateNo(0);
            temp.setDesc(myForm.getTxtDescription());
        }else{
            temp.setDesc(myForm.getTxtTitle());
        }

        if (Utility.IsEmpty(temp.getDesc())) {
            myForm.setMsg("Please specify the Description");
//    		return;
        }

        temp.setPrivateTemplate(myForm.getChkPrivate()!=null);

		String userId = (String)request.getSession().getAttribute("user");

        ArrayList temps = (ArrayList)reportManager.GetReportTemplates(temp.getReportNo(), userId);
        for(int i=0;i<temps.size();i++){
          ReportTempValue rtv = (ReportTempValue)temps.get(i);	
          if (temp.getTemplateNo() != rtv.getTemplateNo()){
            if (rtv.getDesc().toLowerCase().equals(temp.getDesc().toLowerCase())){
            	myForm.setMsg("The Description is already been taken by another template.");
        		return;
            }
          }
        }

        try{
            reportManager.SaveReportTemplate(temp);
			myForm.setMsg(" The template saved successfully");
		}
		catch(Exception ex){
            ;
		}

	}
	
}
