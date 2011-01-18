package org.oscarehr.eyeform.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.ConsultationRequestExtDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.util.SpringUtils;

public class EyeformAction extends DispatchAction {

	   public ActionForward getConReqCC(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		   String requestId = request.getParameter("requestId");
		   ConsultationRequestExtDao dao = (ConsultationRequestExtDao)SpringUtils.getBean("consultationRequestExtDao");
		   String cc = "";
		   if(requestId != null) {
			   try {
				   int reqId = Integer.parseInt(requestId);
				   String value = dao.getConsultationRequestExtsByKey(reqId,"cc");
				   if(value!=null)
					   cc = value;
			   } catch(NumberFormatException e){}
		   }
		   request.setAttribute("requestCc", cc);		   
		   ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao)SpringUtils.getBean("professionalSpecialistDao");
		   List<ProfessionalSpecialist> psList = professionalSpecialistDao.findAll();
		   request.setAttribute("professionalSpecialists",psList);
	       return mapping.findForward("conreqcc");
	    }
}
