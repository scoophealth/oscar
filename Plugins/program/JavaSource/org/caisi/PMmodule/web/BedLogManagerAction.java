package org.caisi.PMmodule.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.caisi.PMmodule.model.Admission;
import org.caisi.PMmodule.model.BedLog;
import org.caisi.PMmodule.model.BedLogSheet;
import org.caisi.PMmodule.model.Demographic;
import org.caisi.PMmodule.model.Provider;
import org.caisi.PMmodule.service.AdmissionManager;
import org.caisi.PMmodule.service.BedLogManager;
import org.caisi.PMmodule.service.ClientManager;
import org.caisi.PMmodule.service.LogManager;
import org.caisi.PMmodule.service.ProgramManager;

public class BedLogManagerAction extends DispatchAction {
	private static Log log = LogFactory.getLog(BedLogManagerAction.class);
	private  final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	private ProgramManager programManager;
	private ClientManager clientManager;
	private BedLogManager bedLogManager;
	private AdmissionManager admissionManager;
	private LogManager logManager;
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}

	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}
	
	public void setBedLogManager(BedLogManager mgr) {
		this.bedLogManager = mgr;
	}
	
	public void setClientManager(ClientManager mgr) {
		this.clientManager = mgr;
	}
		
	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}
	
	public String getProviderNo(HttpServletRequest request) {
		Provider p =  (Provider)request.getSession().getAttribute("provider");
		return p.getProviderNo();
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm bedLogForm = (DynaActionForm)form;
		BedLogSheet sheet = (BedLogSheet)bedLogForm.get("sheet");
		
		String programId = request.getParameter("programId");
		if(programId == null || programId.equals("")) {
			//TODO:some error
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("bedlog.no_program"));
			saveMessages(request,messages);			

			return mapping.findForward("list");
		}
		sheet.setProgramId(Long.valueOf(programId));
		return list(mapping,form,request,response);
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm bedLogForm = (DynaActionForm)form;
		BedLogSheet sheet = (BedLogSheet)bedLogForm.get("sheet");
		String programId = String.valueOf(sheet.getProgramId());
		
		request.setAttribute("program",programManager.getProgram(programId));
		request.setAttribute("bedlog_config",programManager.getBedLogByProgramId(Long.valueOf(programId).longValue()));
		
		BedLogSheet currentSheet = bedLogManager.getLastSheet(programId);
		if(currentSheet != null) {
			log.info("using sheet " + currentSheet.getId() + " from " + currentSheet.getDateCreated());
			bedLogForm.set("sheet",currentSheet);
			request.setAttribute("sheet",currentSheet);
			request.setAttribute("bedlogs",bedLogManager.getBedLogsBySheet(currentSheet.getId()));
			
			List admissions = admissionManager.getCurrentAdmissionsByProgramId(programId);
			List clients = new ArrayList();
			for(int x=0;x<admissions.size();x++) {
				Admission admission = (Admission)admissions.get(x);
				Demographic client = clientManager.getClientByDemographicNo(String.valueOf(admission.getClientId()));
				clients.add(client);
			}
			request.setAttribute("clients",clients);
		}		
		return mapping.findForward("list");
	}
	
	public ActionForward new_sheet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm bedLogForm = (DynaActionForm)form;
		BedLogSheet sheet = (BedLogSheet)bedLogForm.get("sheet");
		
		if(sheet.getId().longValue() > 0) {
			sheet.setId(null);
		}
		sheet.setDateCreated(new Date());
		
		bedLogManager.saveBedLogSheet(sheet);
		
		log.info("Bed Log Sheet Id = " + sheet.getId());
		
		return list(mapping,form,request,response);
	}
	
	public ActionForward save_entry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm bedLogForm = (DynaActionForm)form;
		BedLogSheet sheet = (BedLogSheet)bedLogForm.get("sheet");
		BedLog bedlog = (BedLog)bedLogForm.get("bedlog");

		bedlog.setDateCreated(new Date());
		bedlog.setSheetId(sheet.getId());
		bedlog.setProviderNo(getProviderNo(request));
		
		List foundMatches = bedLogManager.searchBedLogs(bedlog);
		
		if(foundMatches.size() > 0) {
			BedLog update = (BedLog)foundMatches.get(0);
			update.setStatus(bedlog.getStatus());
			bedLogManager.saveBedLog(update);
		} else {
			bedLogManager.saveBedLog(bedlog);
		}
		
		bedLogForm.set("bedlog",new BedLog());
		return list(mapping,form,request,response);
	}
	
	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm bedLogForm = (DynaActionForm)form;
		BedLogSheet sheet = (BedLogSheet)bedLogForm.get("sheet");	
		String programId = null;
		if(sheet.getProgramId() == null || sheet.getProgramId().longValue() == 0) {
			programId= request.getParameter("programId");
		} else {
			programId=String.valueOf(sheet.getProgramId());
		}
		sheet.setProgramId(Long.valueOf(programId));
		request.setAttribute("sheets",bedLogManager.getBedLogSheetsByProgram(programId));
		
		if(sheet.getId() != null && sheet.getId().longValue() > 0) {
			request.setAttribute("sheet",bedLogManager.getSheet(String.valueOf(sheet.getId())));
			request.setAttribute("program",programManager.getProgram(programId));
			request.setAttribute("bedlog_config",programManager.getBedLogByProgramId(Long.valueOf(programId).longValue()));
			List bedlogs = bedLogManager.getBedLogsBySheet(sheet.getId());
			List clients = new ArrayList();
			Map clientMap = new HashMap();
			for(Iterator iter=bedlogs.iterator();iter.hasNext();) {
				BedLog bl = (BedLog)iter.next();
				Demographic client = (clientManager.getClientByDemographicNo(String.valueOf(bl.getDemographicNo())));
				clientMap.put(client.getDemographicNo(),client);
			}
			request.setAttribute("clients",clientMap.values());
			request.setAttribute("bedlogs",bedlogs);
			
		}
		return mapping.findForward("view");
	}
}
