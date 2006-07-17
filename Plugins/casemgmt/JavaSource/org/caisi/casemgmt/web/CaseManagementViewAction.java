package org.caisi.casemgmt.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.caisi.casemgmt.model.CaseManagementCPP;
import org.caisi.casemgmt.model.CaseManagementIssue;
import org.caisi.casemgmt.model.CaseManagementNote;
import org.caisi.casemgmt.service.CaseManagementManager;
import org.caisi.casemgmt.web.formbeans.CaseManagementViewFormBean;
import org.caisi.model.CustomFilter;
import org.caisi.service.TicklerManager;

public class CaseManagementViewAction extends DispatchAction {

	private static Log log = LogFactory.getLog(CaseManagementViewAction.class);
	
	private CaseManagementManager caseManagementMgr;
	private TicklerManager ticklerManager;
	
	public void setCaseManagementManager(CaseManagementManager caseManagementMgr) {
		this.caseManagementMgr = caseManagementMgr;
	}
	
	public void setTicklerManager(TicklerManager mgr) {
		this.ticklerManager = mgr;
	}
	
	public String getDemographicNo(HttpServletRequest request) {
		String demono= request.getParameter("demographicNo");
		if (demono==null || "".equals(demono)) demono=(String)request.getSession().getAttribute("casemgmt_DemoNo");
		else request.getSession().setAttribute("casemgmt_DemoNo", demono);
		return demono;
	}
	
	public String getDemoName(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoName(demoNo);
	}
	
	public String getDemoAge(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoAge(demoNo);
	}
	
	public String getDemoDOB(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoDOB(demoNo);
	}
	
	
	
	public String getProviderNo(HttpServletRequest request){
		String providerNo=request.getParameter("providerNo");
		if (providerNo==null) providerNo=(String) request.getSession().getAttribute("user");
		return providerNo;
	}
	
	public String getProviderName(HttpServletRequest request){
		String providerNo=getProviderNo(request);
		if (providerNo==null) return "";
		return caseManagementMgr.getProviderName(providerNo);
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("unspecified");
		return view(mapping,form,request,response);
	}
	
	/*save CPP for patient*/
	public ActionForward patientCPPSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean)form;
		CaseManagementCPP cpp=caseForm.getCpp();
		String providerNo = getProviderNo(request);
		caseManagementMgr.saveCPP(cpp,providerNo);
		/* prepare the message */
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"cpp.saved"));
		saveMessages(request, messages);
		return mapping.findForward("page.casemgmt.view");
	}
	
	public ActionForward setViewType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("setViewType");
		CaseManagementViewFormBean cform=(CaseManagementViewFormBean)form;
		request.getSession().setAttribute("NoteViewType",cform.getNote_view());
		return view(mapping,form,request,response);
	}
	public ActionForward setPrescriptViewType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("setPrescriptViewType");
		CaseManagementViewFormBean cform=(CaseManagementViewFormBean)form;
		request.getSession().setAttribute("show_all_presciption",cform.getPrescipt_view());
		return view(mapping,form,request,response);
	}
	
	public ActionForward setHideActiveIssues(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("setHideActiveIssue");
		CaseManagementViewFormBean cform=(CaseManagementViewFormBean)form;
		request.getSession().setAttribute("hideActiveIssue",cform.getHideActiveIssue());
		return view(mapping,form,request,response);
	}
	
	/* show case management view */
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean)form;
		String tab = request.getParameter("tab");
		log.debug("view");
		HttpSession se=request.getSession();
		//System.out.println("test for plugin------------");
		
		String providerNo = getProviderNo(request);
		String demoNo=getDemographicNo(request);
		String demoName = getDemoName(demoNo);
		String demoAge = getDemoAge(demoNo);
		String demoDOB = getDemoDOB(demoNo);
		
		request.setAttribute("casemgmt_demoName",demoName);
		request.setAttribute("casemgmt_demoAge",demoAge);
		request.setAttribute("casemgmt_demoDOB",demoDOB);
		
			
		/* get the provider access right for this client*/
		List accessRight=caseManagementMgr.getAccessRight(providerNo,getDemographicNo(request),(String)se.getAttribute("case_program_id"));
		//se.setAttribute("AccessRight",accessRight);
				
		/* get all issues for this patient */
		String hideActiveIssue="true";
		if (se.getAttribute("hideActiveIssue")!=null) hideActiveIssue=(String)se.getAttribute("hideActiveIssue");
		caseForm.setHideActiveIssue(hideActiveIssue);
		List issues; 
		if (!"true".equalsIgnoreCase(hideActiveIssue)) issues=caseManagementMgr.getIssues(providerNo,this.getDemographicNo(request),accessRight);
		else issues=caseManagementMgr.getActiveIssues(providerNo,this.getDemographicNo(request),accessRight);
		
		//nys, show all notes , no matter this notes related resolved or not
		List all_issues;
		all_issues=caseManagementMgr.getIssues(providerNo,this.getDemographicNo(request),accessRight);
		
		request.setAttribute("Issues",issues);
		
		/* get notes for this patient - possibly filtered by issue */
		List notes = null;
		String[] checked_issues = request.getParameterValues("check_issue");
		if(checked_issues != null) {
			for(int x=0;x<checked_issues.length;x++) {
				System.out.println("check issue id = "+ checked_issues[x]);
			}
			request.setAttribute("checked_issues",checked_issues);
			notes = caseManagementMgr.getNotes(this.getDemographicNo(request),checked_issues);
		} else {
			String[] checkedAllIssues=new String[all_issues.size()];
			for(int i=0;i<all_issues.size();i++){
				checkedAllIssues[i]=((CaseManagementIssue)all_issues.get(i)).getIssue().getId().toString();
			}
			notes = caseManagementMgr.getNotes(this.getDemographicNo(request),checkedAllIssues);
		}

		
		/*order by date*/
		List rtnotes=notesOrderByDate(notes);
		request.setAttribute("Notes",rtnotes);
		
		/* get CPP */
		
		CaseManagementCPP cpp = this.caseManagementMgr.getCPP(this.getDemographicNo(request));
		if(cpp == null) {
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(this.getDemographicNo(request));
		}
		
		//set the Note view type
		String noteview="summary";
		if (se.getAttribute("NoteViewType")!=null) noteview=(String)se.getAttribute("NoteViewType");
		caseForm.setNote_view(noteview);
		

		/*//add all issues name  for this demographic into ongoing concern
		String ongoing="";
		Iterator itr = issues.iterator();
		while (itr.hasNext()){
			CaseManagementIssue iss=(CaseManagementIssue)itr.next();
			String tempstr=iss.getIssue().getDescription();
			log.info("tempstr="+tempstr);
			if (ongoing.equals("")) ongoing=tempstr;
			else if (!ongoing.equals("") && tempstr!=null && !tempstr.equals(""))
				ongoing=ongoing+", "+tempstr;
		}
		log.info("ongoing concern="+ongoing);
		cpp.setOngoingConcerns(ongoing);
		*/
		request.setAttribute("cpp",cpp);
		se.setAttribute("casemgmt_cpp",cpp);
		caseForm.setCpp(cpp);
		
		
		/* get allergies */
		List allergies = this.caseManagementMgr.getAllergies(this.getDemographicNo(request));
		request.setAttribute("Allergies",allergies);
		
		/* get prescriptions */
		List prescriptions=null;
		if ("all".equalsIgnoreCase((String)request.getSession().getAttribute("show_all_presciption")))
			prescriptions = this.caseManagementMgr.getPrescriptions(this.getDemographicNo(request),true);
		else prescriptions = this.caseManagementMgr.getPrescriptions(this.getDemographicNo(request),false);
		request.setAttribute("Prescriptions",prescriptions);
		
		String prescripview="current";
		if (se.getAttribute("show_all_presciption")!=null) prescripview=(String)se.getAttribute("show_all_presciption");
		caseForm.setPrescipt_view(prescripview);
		
		/*set form value for e-chart*/
		
		Locale vLocale=(Locale) se.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		caseForm.setVlCountry(vLocale.getCountry());
		caseForm.setDemographicNo(getDemographicNo(request));
		
		se.setAttribute("casemgmt_DemoNo",getDemographicNo(request));
		//System.out.println("DemograNo="+getDemographicNo(request));
		caseForm.setRootCompURL((String)se.getAttribute("casemgmt_oscar_baseurl"));
		se.setAttribute("casemgmt_VlCountry",vLocale.getCountry());
		
		/*prepare new form list for patient*/
		se.setAttribute("casemgmt_newFormBeans",this.caseManagementMgr.getEncounterFormBeans());
		/*prepare messenger list*/
		se.setAttribute("casemgmt_msgBeans", this.caseManagementMgr.getMsgBeans(new Integer(getDemographicNo(request))));
		
		/* tickler */
		if(tab != null && tab.equalsIgnoreCase("Ticklers")) {
			CustomFilter cf = new CustomFilter();
			cf.setDemographic_no(this.getDemographicNo(request));
			cf.setStatus("A");
			request.setAttribute("ticklers",ticklerManager.getTicklers(cf));
		}
		
		return mapping.findForward("page.casemgmt.view");
	}
	
	public List notesOrderByDate(List notes)
	{
		List rtNotes=new ArrayList();
		int noteSize=notes.size();
		for (int i=0; i<noteSize; i++){
			Iterator itr=notes.iterator();
			CaseManagementNote inote=(CaseManagementNote) itr.next();
			while (itr.hasNext()){
				CaseManagementNote tempNote=(CaseManagementNote) itr.next();
				if( tempNote.getUpdate_date().before(inote.getUpdate_date())){
					inote=tempNote;
				}
			}
			rtNotes.add(inote);
			notes.remove(inote);
		}
		return rtNotes;
	}
}
