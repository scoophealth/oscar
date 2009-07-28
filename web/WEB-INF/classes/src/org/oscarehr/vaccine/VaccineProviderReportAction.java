package org.oscarehr.vaccine;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.er.ReceptionistReportAction;
import org.oscarehr.util.LoggedInInfo;

public class VaccineProviderReportAction extends BaseAction {
	private static Log log = LogFactory.getLog(ReceptionistReportAction.class);

	private ClientManager clientManager;
	private GenericIntakeManager genericIntakeManager;

	public void setGenericIntakeManager(GenericIntakeManager mgr) {
		this.genericIntakeManager = mgr;
	}
		
	protected void postMessage(HttpServletRequest request, String key, String val) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(key,val));
		saveMessages(request,messages);
	}
	
	protected void postMessage(HttpServletRequest request, String key) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(key));
		saveMessages(request,messages);
	}

	protected String getProviderNo(HttpServletRequest request) {
		return (String)request.getSession().getAttribute("user");
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return show_report(mapping,form,request,response);
	}
	
	/*
	 * Client Name
	 * DOB
	 * Health Card
	 * List of current Bed and Service programs (+contact info)
	 * list of current issues
	 * list of medications
	 * 
	 */
	public ActionForward show_report(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String clientId = request.getParameter("id");
			
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
        
		Demographic client = clientManager.getClientByDemographicNo(clientId);
		
		if(client == null) {
			postMessage(request,"client.missing");
			log.warn("client not found");
			return mapping.findForward("error");
		}
		
		String name = client.getFormattedName();
		String dob = client.getFormattedDob();
		String healthCard = client.getHin() + " " + client.getVer();
				
		
		request.setAttribute("demographicNo",clientId);
		request.setAttribute("client_name",name);
		request.setAttribute("client_dob",dob);
		request.setAttribute("client_healthCard",healthCard);
					
		//List allergies = this.caseManagementManager.getAllergies(clientId);
		//request.setAttribute("allergies",allergies);
		
		Intake quickIntake = genericIntakeManager.getMostRecentQuickIntake(Integer.parseInt(clientId), loggedInInfo.currentFacility.getId());
		Map<String,String> answerMap = quickIntake.getAnswerKeyValues();
		String allergies = answerMap.get("Allergies");
		request.setAttribute("allergies", allergies);
		request.setAttribute("intakeMap", answerMap);
		for (Entry<String, String> entry : quickIntake.getAnswerKeyValues().entrySet()) {
		   String key = entry.getKey();
		   String value = entry.getValue();
		   System.out.println(key + "=" + value);
		}

		return mapping.findForward("report");
	}

    public void setClientManager(ClientManager mgr) {
    	this.clientManager = mgr;
    }

}
