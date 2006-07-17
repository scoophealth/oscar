package org.caisi.casemgmt.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.caisi.casemgmt.model.CaseManagementCPP;
import org.caisi.casemgmt.model.CaseManagementIssue;
import org.caisi.casemgmt.model.CaseManagementNote;
import org.caisi.casemgmt.model.Issue;
import org.caisi.casemgmt.service.CaseManagementManager;
import org.caisi.casemgmt.web.formbeans.CaseManagementEntryFormBean;
import org.caisi.model.Provider;





import oscar.oscarEncounter.pageUtil.EctSessionBean;

public class CaseManagementEntryAction extends DispatchAction
{

	private static Log log = LogFactory.getLog(CaseManagementEntryAction.class);

	private String relateIssueString = "Issues related to this note:";

	private CaseManagementManager caseManagementMgr;
	
	public void setCaseManagementManager(CaseManagementManager caseManagementMgr)
	{
		this.caseManagementMgr = caseManagementMgr;
	}

	public String getDemographicNo(HttpServletRequest request) {
		String demono = request.getParameter("demographicNo");
		if (demono == null || "".equals(demono))
			demono = (String) request.getSession().getAttribute(
					"casemgmt_DemoNo");
		else
			request.getSession().setAttribute("casemgmt_DemoNo", demono);
		return demono;
	}

	public String getDemoName(String demoNo) {
		if (demoNo == null)
			return "";
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
	
	
	

	public String getProviderNo(HttpServletRequest request) {
		String providerNo = request.getParameter("providerNo");
		if (providerNo == null)
			providerNo = (String) request.getSession().getAttribute("user");
		return providerNo;
	}
		public String getProviderName(HttpServletRequest request) {
		String providerNo = getProviderNo(request);
		if (providerNo == null)
			return "";
		return caseManagementMgr.getProviderName(providerNo);
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		log.debug("unspecified");
		return edit(mapping, form, request, response);
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		log.debug("edit");

		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		request.setAttribute("change_flag", "false");

		request.setAttribute("from", "casemgmt");

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));
		
		
		/* process the request from other module */
		if (!"casemgmt".equalsIgnoreCase(request.getParameter("from"))) {

			// no demographic number, no page
			if (request.getParameter("demographicNo") == null
					|| "".equals(request.getParameter("demographicNo"))) {
				return mapping.findForward("NoDemoERR");
			}
			request.setAttribute("from", "");
		}
		CaseManagementCPP cpp = caseManagementMgr.getCPP(demono);
		if (cpp == null) {
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(demono);
		}
		request.getSession().setAttribute("note_cpp", cpp);

		/* prepare url for billing */
		if (request.getParameter("from") != null)
			request.setAttribute("from", request.getParameter("from"));

		String url = "";
		if ("casemgmt".equals(request.getAttribute("from"))) {
			String ss = (String) request.getSession().getAttribute(
					"casemgmt_VlCountry");
			Properties oscarVariables = (Properties) request.getSession()
					.getAttribute("oscarVariables");
			String province = ((String) oscarVariables.getProperty(
					"billregion", "")).trim().toUpperCase();
			EctSessionBean bean = (EctSessionBean) request.getSession()
					.getAttribute("casemgmt_bean");

			if (bean.appointmentNo == null)
				bean.appointmentNo = "0";
			String bsurl = (String) request.getSession().getAttribute(
					"casemgmt_oscar_baseurl");
			Date today = new Date();
			String Hour = Integer.toString(today.getHours());
			String Min = Integer.toString(today.getMinutes());
			if ("BR".equals(ss))
				url = bsurl
						+ "/oscar/billing/procedimentoRealizado/init.do?appId="
						+ bean.appointmentNo;
			else
				url = bsurl
						+ "/billing.do?billRegion="
						+ java.net.URLEncoder.encode(province)
						+ "&billForm="
						+ java.net.URLEncoder.encode(oscarVariables
								.getProperty("default_view"))
						+ "&hotclick="
						+ java.net.URLEncoder.encode("")
						+ "&appointment_no="
						+ bean.appointmentNo
						+ "&appointment_date="
						+ bean.appointmentDate
						+ "&start_time="
						+ Hour
						+ ":"
						+ Min
						+ "&demographic_name="
						+ java.net.URLEncoder.encode(bean.patientLastName + ","
								+ bean.patientFirstName) + "&demographic_no="
						+ bean.demographicNo + "&providerview="
						+ bean.curProviderNo + "&user_no=" + bean.providerNo
						+ "&apptProvider_no=" + bean.curProviderNo
						+ "&bNewForm=1&status=t";
			
			request.getSession().setAttribute("billing_url",url);
		}

		/* get current providerNo */
		String providerNo = getProviderNo(request);

		/* remove the remembered echart string */
		request.getSession().setAttribute("lastSavedNoteString", null);

		/*get access right*/
		List accessRight=caseManagementMgr.getAccessRight(providerNo,demono,(String)request.getSession().getAttribute("case_program_id"));
		/* get all issues for this patient */
		List issues = caseManagementMgr.getIssues(providerNo, demono,accessRight);
		cform.setDemoNo(demono);
		CaseManagementNote note = null;

		if (request.getParameter("noteId") == null
				|| request.getParameter("note_edit") != null
				&& request.getParameter("note_edit").equals("new"))
		{
			request.getSession().setAttribute("newNote","true");
			request.getSession().setAttribute("issueStatusChanged","false");
			note = new CaseManagementNote();
			// note.setNote("test");
			note.setProvider_no(providerNo);
			Provider prov = new Provider();
			prov.setProvider_no(providerNo);
			note.setProvider(prov);
			note.setDemographic_no(demono);
		} else
		{
			request.getSession().setAttribute("newNote","false");
			String noteid = (String) request.getParameter("noteId");
			note = caseManagementMgr.getNote(noteid);

		}

		cform.setCaseNote(note);
		/* set issue checked list */
		CheckBoxBean[] checkedList = new CheckBoxBean[issues.size()];
		for (int i = 0; i < issues.size(); i++)
		{
			checkedList[i] = new CheckBoxBean();
			CaseManagementIssue iss = (CaseManagementIssue) issues.get(i);
			checkedList[i].setIssue(iss);
			checkedList[i].setUsed(caseManagementMgr.haveIssue(iss.getId(),
					demono));

		}

		Iterator itr = note.getIssues().iterator();
		while (itr.hasNext())
		{
			int id = ((CaseManagementIssue) itr.next()).getId().intValue();
			SetChecked(checkedList, id);
		}

		cform.setIssueCheckList(checkedList);

		/* set new issue list */
		List aInfo = caseManagementMgr.getAllIssueInfo();
		List issueInfo = new ArrayList();
		itr = aInfo.iterator();
		while (itr.hasNext())
		{
			Issue iss = (Issue) itr.next();
			if (!inCaseIssue(iss, issues))
			{
				LabelValueBean ll = new LabelValueBean();
				ll.setValue(iss.getId().toString());
				ll.setLabel(iss.getDescription());
				issueInfo.add(ll);
			}
		}
		cform.setNewIssueList(issueInfo);
		if (!note.isSigned())
			cform.setSign("off");
		else
			cform.setSign("on");
		if (!note.isIncludeissue())
			cform.setIncludeIssue("off");
		else
			cform.setIncludeIssue("on");

		return mapping.findForward("view");
	}

	boolean inCaseIssue(Issue iss, List issues)
	{
		Iterator itr = issues.iterator();
		while (itr.hasNext())
		{
			CaseManagementIssue cIss = (CaseManagementIssue) itr.next();
			if (iss.getId().longValue() == cIss.getIssue_id())
				return true;
		}
		return false;
	}

	public void SetChecked(CheckBoxBean[] checkedlist, int id)
	{
		for (int i = 0; i < checkedlist.length; i++)
		{
			if (checkedlist[i].getIssue().getId().intValue() == id)
			{
				checkedlist[i].setChecked("on");
				break;
			}
		}
	}

	public boolean inCheckList(Long id, int[] list)
	{
		boolean ret = false;
		for (int i = 0; i < list.length; i++)
		{
			if (list[i] == id.intValue())
				ret = true;
		}
		return ret;
	}

	public void noteSave(CaseManagementEntryFormBean cform,
			HttpServletRequest request) throws Exception
	{

		String providerNo = getProviderNo(request);
		String userName = getProviderName(request);

		CaseManagementCPP cpp = (CaseManagementCPP) request.getSession()
				.getAttribute("note_cpp");

		String lastSavedNoteString = (String) request.getSession()
				.getAttribute("lastSavedNoteString");

		/* get the checked issue save into note */
		List issuelist = new ArrayList();
		CheckBoxBean[] checkedlist = (CheckBoxBean[]) cform.getIssueCheckList();
		CaseManagementNote note = (CaseManagementNote) cform.getCaseNote();
		String sign = (String) request.getParameter("sign");
		String includeIssue = (String) request.getParameter("includeIssue");
		if (sign == null || !sign.equals("on"))
		{
			note.setSigning_provider_no("");
			note.setSigned(false);
			cform.setSign("off");
		} else
		{
			note.setSigning_provider_no(userName);
			note.setSigned(true);
			
		}
		
		//nys , added new field to database, casemgmt_note
			
		//get agency is not implemented yet
		
		org.springframework.web.context.WebApplicationContext ctx=
			org.springframework.web.context.support.WebApplicationContextUtils.
				getWebApplicationContext(getServlet().getServletContext());

		org.caisi.PMmodule.service.ProgramManager programManager= 
			(org.caisi.PMmodule.service.ProgramManager)ctx.getBean("programManagerRemote");
		
		org.caisi.PMmodule.service.AdmissionManager admissionManager= 
			(org.caisi.PMmodule.service.AdmissionManager)ctx.getBean("admissionManagerRemote");
		
		
		
		
		//agency manager not implemented yet. 
		note.setAgency_no("not_ready");

		log.debug("saving note: Provider_no=" + note.getProvider_no());
		log.debug("saving note: Program_no=" + note.getProgram_no());
		
		String role=null;
		String team=null;

		try {
			role = String.valueOf((programManager.getProgramProvider(note.getProvider_no(),note.getProgram_no())).getRole().getId());
		}catch(Throwable e) {
			log.error(e);
			role = "0";
		}
	       	note.setReporter_caisi_role(role);	

		try {
			team = String.valueOf((admissionManager.getAdmission(note.getProgram_no(),note.getDemographic_no())).getTeamId());
		}catch(Throwable e) {
			log.error(e);
			team = "0";
		}
		note.setReporter_program_team(team);
	
		
		Set issueset = new HashSet();
		Set noteSet = new HashSet();
		String ongoing = "";
		for (int i = 0; i < checkedlist.length; i++)
		{
			if (!checkedlist[i].getIssue().isResolved())
				ongoing = ongoing
						+ checkedlist[i].getIssue().getIssue().getDescription()
						+ "\n";
			String ischecked = request.getParameter("issueCheckList[" + i
					+ "].checked");
			if (ischecked != null && ischecked.equalsIgnoreCase("on"))
			{
				checkedlist[i].setChecked("on");
				CaseManagementIssue iss = checkedlist[i].getIssue();
				iss.setNotes(noteSet);
				issueset.add(checkedlist[i].getIssue());
			} else
			{
				checkedlist[i].setChecked("off");
			}
			issuelist.add(checkedlist[i].getIssue());
		}

		note.setIssues(issueset);

		/* remove signature and the related issues from note */
		String noteString = note.getNote();
		noteString = removeSignature(noteString);
		noteString = removeCurrentIssue(noteString);
		note.setNote(noteString);

		/* add issues into notes */
		if (includeIssue == null || !includeIssue.equals("on"))
		{
			/* set includeissue in note */
			note.setIncludeissue(false);
			cform.setIncludeIssue("off");
		} else
		{
			note.setIncludeissue(true);
			/* add the related issues to note */

			String issueString = "";
			issueString = createIssueString(issueset);
			// insert the string before signiture

			int index = noteString.indexOf("\n[[");
			if (index >= 0)
			{
				String begString = noteString.substring(0, index);
				String endString = noteString.substring(index + 1);
				note.setNote(begString + issueString + endString);
			} else
			{
				note.setNote(noteString + issueString);
			}
		}
		/* save all issue changes for demographic */
		caseManagementMgr.saveAndUpdateCaseIssues(issuelist);
		cpp.setOngoingConcerns(ongoing);
		
		/*get access right*/
		List accessRight=caseManagementMgr.getAccessRight(providerNo,getDemographicNo(request),(String)request.getSession().getAttribute("case_program_id"));
		String roleName=caseManagementMgr.getRoleName(providerNo,(String)request.getSession().getAttribute("case_program_id"));
		/*
		 * if provider is a doctor or nurse,get all major and resolved medical
		 * issue for demograhhic and append them to CPP medical history
		 */
		setCPPMedicalHistory(cpp, providerNo,accessRight);

		caseManagementMgr.saveCPP(cpp, providerNo);
		request.getSession().setAttribute("note_cpp", cpp);
		/* save note including add signature */
		String savedStr = caseManagementMgr.saveNote(cpp, note, providerNo,
				userName, lastSavedNoteString, roleName);
		/* remember the str written into echart */
		request.getSession().setAttribute("lastSavedNoteString", savedStr);
	}

	/* remove related issue list from note */
	public String removeCurrentIssue(String noteString)
	{

		noteString = noteString.replaceAll("\r\n", "\n");
		noteString = noteString.replaceAll("\r", "\n");
		String rt = noteString;
		int index = noteString.indexOf("\n[" + relateIssueString);
		if (index >= 0)
		{
			String begString = noteString.substring(0, index);
			String endString = noteString.substring(index);
			endString = endString.substring(endString.indexOf("]\n") + 2);
			rt = begString + endString;
		}
		return rt;
	}

	/* remove signiature string */
	public String removeSignature(String note)
	{

		note = note.replaceAll("\r\n", "\n");
		note = note.replaceAll("\r", "\n");
		String rt = note;
		String subStr = "\n[[";
		int indexb = note.lastIndexOf(subStr);
		if (indexb >= 0)
		{
			String subNote = note.substring(indexb);
			int indexe = subNote.indexOf("]]\n");
			if (indexe < 0)
				return rt;
			String begNote = note.substring(0, indexb);
			String endNote = subNote.substring(indexe + 3);
			// String midNote = subNote.substring(subStr.length());
			// String[] sp = midNote.split(" ");
			// midNote = "[" + sp[0] + " " + sp[1] + "]";
			rt = begNote + endNote;
		}
		return rt;
	}

	/* create related issue string */
	public String createIssueString(Set issuelist)
	{
		if (issuelist.isEmpty())
			return "";
		String rt = "\n[" + relateIssueString;
		Iterator itr = issuelist.iterator();
		while (itr.hasNext())
		{
			CaseManagementIssue iss = (CaseManagementIssue) itr.next();
			rt = rt + "\n" + iss.getIssue().getDescription() + "\t\t";
			if (iss.isCertain())
				rt = rt + "certain" + "  ";
			else
				rt = rt + "uncertain" + "  ";
			if (iss.isAcute())
				rt = rt + "acute" + "  ";
			else
				rt = rt + "chronic" + "  ";
			if (iss.isMajor())
				rt = rt + "major" + "  ";
			else
				rt = rt + "not major" + "  ";
			if (iss.isResolved())
				rt = rt + "resolved";
			else
				rt = rt + "unresolved";
		}
		return rt + "]\n";
	}

	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		log.debug("save");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		request.setAttribute("change_flag","false");
		
		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));
		

		request.setAttribute("from", request.getParameter("from"));	
		noteSave(cform, request);
		/* prepare the message */
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"note.saved"));
		saveMessages(request, messages);

		// this.caseManagementMgr.saveNote();
		return mapping.findForward("view");
	}

	public CaseManagementIssue newIssueToCIssue(
			CaseManagementEntryFormBean cform, Issue iss)
	{
		CaseManagementIssue cIssue = new CaseManagementIssue();
		// cIssue.setActive(true);
		cIssue.setAcute(false);
		cIssue.setCertain(false);
		cIssue.setDemographic_no((String) cform.getDemoNo());

		cIssue.setIssue_id(iss.getId().longValue());

		cIssue.setIssue(iss);
		cIssue.setMajor(false);
		// cIssue.setMedical_diagnosis(true);
		cIssue.setNotes(new HashSet());
		cIssue.setResolved(false);
		String issueType = iss.getRole();
		cIssue.setType(issueType);
		cIssue.setUpdate_date(new Date());

		// add it to database
		List uList = new ArrayList();
		uList.add(cIssue);
		caseManagementMgr.saveAndUpdateCaseIssues(uList);
		// add new issues to ongoing concern
		caseManagementMgr.addNewIssueToConcern((String) cform.getDemoNo(), iss
				.getDescription());
		return cIssue;
	}

	public ActionForward saveAndExit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		log.debug("saveandexit");
		request.setAttribute("change_flag","false");
		
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"note.saved"));
		saveMessages(request, messages);

		noteSave(cform, request);
		cform.setMethod("view");
		return mapping.findForward("windowClose");
	}

	public ActionForward exit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		log.debug("back to casemanagement");
		request.setAttribute("change_flag","false");
		return mapping.findForward("list");
	}

	public ActionForward addNewIssue(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		log.debug("addNewIssue");
		request.setAttribute("change_flag","true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));
		
		
		request.setAttribute("from", request.getParameter("from"));
		//noteSave(cform, request);
		cform.setShowList("false");
		cform.setSearString("");
		return mapping.findForward("IssueSearch");
	}

	public ActionForward issueSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		log.debug("issueSearch");
		request.setAttribute("change_flag","true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;

		request.setAttribute("from", request.getParameter("from"));
		cform.setShowList("true");
		
		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));
		

		// get current providerNo
		String providerNo = getProviderNo(request);


		/*get access right*/
		List accessRight=caseManagementMgr.getAccessRight(providerNo,demono,(String)request.getSession().getAttribute("case_program_id"));
		
		// get the issue list have search string
		String search = (String) cform.getSearString();
		ArrayList allIssueList = (ArrayList) caseManagementMgr
				.getIssueInfoBySearch(providerNo, search, accessRight);

		
		// get the issues the demographic already has
		List caseIssues = caseManagementMgr.getIssues(providerNo, demono,accessRight);

		// remove the issues demographic already has
		ArrayList al = new ArrayList();
		boolean has = false;
		Iterator itr = allIssueList.iterator();
		while (itr.hasNext())
		{
			Issue iss = (Issue) itr.next();
			for (int i = 0; i < caseIssues.size(); i++)
			{
				if (iss.getId().longValue() == ((CaseManagementIssue) caseIssues
						.get(i)).getIssue_id())
				{
					has = true;
					break;
				}
			}
			if (!has)
				al.add(iss);
			has = false;
		}

		// prepare the new issue list
		CheckIssueBoxBean[] issueList = new CheckIssueBoxBean[al.size()];
		for (int i = 0; i < al.size(); i++)
		{
			issueList[i] = new CheckIssueBoxBean();
			issueList[i].setIssue((Issue) al.get(i));

		}
		cform.setNewIssueCheckList(issueList);

		return mapping.findForward("IssueSearch");
	}

	public ActionForward issueAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		log.debug("issueAdd");
		request.setAttribute("change_flag","true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		request.setAttribute("from", request.getParameter("from"));
				

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge",getDemoAge(demono));
		request.setAttribute("demoDOB",getDemoDOB(demono));
		

		// add checked new issues to client's issue list
		// client's old issues
		CheckBoxBean[] oldList = (CheckBoxBean[]) cform.getIssueCheckList();
		// client's new issues
		CheckIssueBoxBean[] issueList = (CheckIssueBoxBean[]) cform
				.getNewIssueCheckList();
		int k = 0;
		for (int i = 0; i < issueList.length; i++)
		{
			if (issueList[i].isChecked())
				k++;
		}
		CheckBoxBean[] caseIssueList = new CheckBoxBean[oldList.length + k];
		for (int i = 0; i < oldList.length; i++)
		{
			caseIssueList[i] = new CheckBoxBean();
			caseIssueList[i].setChecked(oldList[i].getChecked());
			caseIssueList[i].setUsed(oldList[i].isUsed());
			caseIssueList[i].setIssue(oldList[i].getIssue());
		}
		k = 0;
		for (int i = 0; i < issueList.length; i++)
		{
			if (issueList[i].isChecked())
			{
				caseIssueList[oldList.length + k] = new CheckBoxBean();
				caseIssueList[oldList.length + k].setIssue(newIssueToCIssue(
						cform, issueList[i].getIssue()));
				k++;
			}
		}

		cform.setIssueCheckList(caseIssueList);

		return mapping.findForward("view");
	}

	public ActionForward issueDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		log.debug("issueDelete");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		//noteSave(cform, request);
		request.setAttribute("change_flag","true");
		request.setAttribute("from", request.getParameter("from"));

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));
		
		
		
		CheckBoxBean[] oldList = (CheckBoxBean[]) cform.getIssueCheckList();
		
		String inds = (String) cform.getDeleteId();
		Integer ind = new Integer(inds);

		// delete the right issue
		CheckBoxBean[] caseIssueList = new CheckBoxBean[oldList.length - 1];
		int k = 0;

		if (ind.intValue() >= oldList.length)
		{
			log.error("issueDelete index error");
			return mapping.findForward("view");
		}
		for (int i = 0; i < oldList.length; i++)
		{

			if (i != ind.intValue())
			{
				caseIssueList[k] = new CheckBoxBean();
				caseIssueList[k].setChecked(oldList[i].getChecked());
				caseIssueList[k].setUsed(oldList[i].isUsed());
				caseIssueList[k].setIssue(oldList[i].getIssue());
				k++;
			}
			if (i == ind.intValue())
			{
				// delete from caseissue table
				CaseManagementIssue iss = oldList[i].getIssue();
				caseManagementMgr.deleteIssueById(iss);
			}
		}
		cform.setIssueCheckList(caseIssueList);
		// reset current concern in CPP
		updateIssueToConcern(cform);
		return mapping.findForward("view");
	}

	public ActionForward issueChange(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		log.debug("issueChange");
		request.setAttribute("from", request.getParameter("from"));
		request.setAttribute("change_flag", "true");
		request.getSession().setAttribute("issueStatusChanged", "true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));
		

		//noteSave(cform, request);
		CheckBoxBean[] oldList = (CheckBoxBean[]) cform.getIssueCheckList();
		String providerNo = getProviderNo(request);
		CaseManagementCPP cpp = (CaseManagementCPP) request.getSession()
				.getAttribute("note_cpp");

		String inds = (String) cform.getLineId();

		Integer ind = new Integer(inds);
		List iss = new ArrayList();
		oldList[ind.intValue()].getIssue().setUpdate_date(new Date());
		iss.add(oldList[ind.intValue()].getIssue());
		caseManagementMgr.saveAndUpdateCaseIssues(iss);
		// reset current concern in CPP
		updateIssueToConcern(cform);

		/*get access right*/
		List accessRight=caseManagementMgr.getAccessRight(providerNo,demono,(String)request.getSession().getAttribute("case_program_id"));
		
		/* add medical history to CPP */
		setCPPMedicalHistory(cpp, providerNo,accessRight);
		caseManagementMgr.saveCPP(cpp, providerNo);
		return mapping.findForward("view");
	}

	public void updateIssueToConcern(ActionForm form)
	{
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		CheckBoxBean[] oldList = (CheckBoxBean[]) cform.getIssueCheckList();
		List caseiss = new ArrayList();
		for (int i = 0; i < oldList.length; i++)
		{
			if (!oldList[i].getIssue().isResolved())
				caseiss.add(oldList[i].getIssue());
		}
		String demoNo = (String) cform.getDemoNo();
		caseManagementMgr.updateCurrentIssueToCPP(demoNo, caseiss);
	}

	public void setCPPMedicalHistory(CaseManagementCPP cpp, String providerNo,List accessRight)
	{
		if (caseManagementMgr.greaterEqualLevel(3, providerNo))
		{
			String mHis = cpp.getMedicalHistory();
			mHis = mHis.replaceAll("\r\n", "\n");
			mHis = mHis.replaceAll("\r", "\n");
			List allIssues = caseManagementMgr.getIssues(providerNo, cpp
					.getDemographic_no(),accessRight);
			Iterator itr = allIssues.iterator();
			while (itr.hasNext())
			{
				CaseManagementIssue cis = (CaseManagementIssue) itr.next();
				String issustring = cis.getIssue().getDescription();
				if (cis.isMajor() && cis.isResolved())
				{

					if (mHis.indexOf(issustring) < 0)
						mHis = mHis + issustring + ";\n";
				} else
				{

					if (mHis.indexOf(issustring) >= 0)
						mHis = mHis.replaceAll(issustring + ";\n", "");
				}

			}
			mHis = mHis.replaceAll("\r\n", "\n");
			mHis = mHis.replaceAll("\r", "\n");
			cpp.setMedicalHistory(mHis);
		}
	}



	
}
