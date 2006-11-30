package org.caisi.tickler.web;

import java.util.HashSet;
import java.util.List;
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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.caisi.model.CustomFilter;
import org.caisi.model.Demographic;
import org.caisi.model.Provider;
import org.caisi.service.DemographicManagerTickler;
import org.caisi.service.ProviderManagerTickler;
import org.caisi.service.TicklerManager;

public class CustomFilterAction extends DispatchAction {
	
	private static Log log = LogFactory.getLog(CustomFilterAction.class);
	private TicklerManager ticklerMgr = null;
	private ProviderManagerTickler providerMgr = null;
	private DemographicManagerTickler demographicMgr = null;
	
	public void setTicklerManager(TicklerManager ticklerManager) {
		this.ticklerMgr = ticklerManager;
	}
	
	public void setDemographicManager(DemographicManagerTickler demographicManager) {
		this.demographicMgr = demographicManager;
	}
	
	public void setProviderManager(ProviderManagerTickler providerMgr) {
		this.providerMgr = providerMgr;
	}
	
	String getProviderNo(HttpServletRequest request) {
		return (String)request.getSession().getAttribute("user");
	}
	
	/* default to 'list' */
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("unspecified");
		return list(mapping,form,request,response);
	}
	
	//TODO: need to forward to TicklerAction
	public ActionForward run(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("run");

		return list(mapping,form,request,response);
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("list");
	
		request.setAttribute("custom_filters",ticklerMgr.getCustomFilters(this.getProviderNo(request)));
		return mapping.findForward("customFilterList");
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("edit");
		
		String name = request.getParameter("name");
		if(name != null && !name.equals("")) {
			CustomFilter filter = ticklerMgr.getCustomFilter(name);
			/* get the demographic */
			String demo_no=filter.getDemographic_no();
			if(!(demo_no.equals(""))&&demo_no!=null)
			{
			Demographic demographic = demographicMgr.getDemographic(demo_no);
			if(demographic != null) {
				filter.setDemographic_webName(demographic.getFormattedName());
			}}
			else
				filter.setDemographic_webName("");
				
			DynaActionForm filterForm = (DynaActionForm)form;
			filterForm.set("filter",filter);
			request.setAttribute("customFilterForm",filterForm);
			request.setAttribute("custom_filter",filter);
			request.setAttribute("me_no",(String)request.getSession().getAttribute("user"));
			request.setAttribute("me",providerMgr.getProvider((String)request.getSession().getAttribute("user")).getFormattedName());
		}
		
		request.setAttribute("providers",providerMgr.getProviders());
		request.setAttribute("priorityList",CustomFilter.priorityList);
		request.setAttribute("statusList",CustomFilter.statusList);
		
		return mapping.findForward("customFilterForm");
	}
	
	/* save a custom filter */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("save");
		
        DynaActionForm filterForm = (DynaActionForm)form;
        CustomFilter filter = (CustomFilter)filterForm.get("filter");
        
        String[] providers = request.getParameterValues("provider");
        if(providers != null) {
	        Set sProviders = new HashSet();
	        for(int x=0;x<providers.length;x++) {
	        	sProviders.add(new Provider(providers[x]));
	        }
	        filter.setProviders(sProviders);
        }
        
        String[] assignees = request.getParameterValues("assignee");
        if(assignees != null) {
	        Set sAssignees = new HashSet();
	        for(int x=0;x<assignees.length;x++) {
	        	sAssignees.add(new Provider(assignees[x]));
	        }
	        filter.setAssignees(sAssignees);
        }
        filter.setProvider_no(this.getProviderNo(request));
        ticklerMgr.saveCustomFilter(filter);
        
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("filter.saved"));
        saveMessages(request,messages);
        
        
		return list(mapping,form,request,response);
	}
	
	/* delete a filter */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("delete");
		String[] checks = request.getParameterValues("checkbox");

		for(int x=0;x<checks.length;x++) {
			ticklerMgr.deleteCustomFilter(checks[x]);
		}
		return list(mapping,form,request,response);
	}
	
}
