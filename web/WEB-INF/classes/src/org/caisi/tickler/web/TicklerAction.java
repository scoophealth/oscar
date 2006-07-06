package org.caisi.tickler.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
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
import org.caisi.model.EChart;
import org.caisi.model.Provider;
import org.caisi.model.Tickler;
import org.caisi.service.ConsultationManager;
import org.caisi.service.DemographicManagerTickler;
import org.caisi.service.EChartManager;
import org.caisi.service.ProviderManagerTickler;
import org.caisi.service.TicklerManager;
import org.caisi.tickler.prepared.PreparedTickler;
import org.caisi.tickler.prepared.PreparedTicklerManager;

public class TicklerAction extends DispatchAction {
	private static Log log = LogFactory.getLog(TicklerAction.class);
	private TicklerManager ticklerMgr = null;
	private ProviderManagerTickler providerMgr = null;
	private PreparedTicklerManager preparedTicklerMgr = null;
	private ConsultationManager consultationMgr = null;
	private DemographicManagerTickler demographicMgr = null;
	private EChartManager chartMgr = null;
	
	public void setTicklerManager(TicklerManager ticklerManager) {
		this.ticklerMgr = ticklerManager;
	}
	
	public void setDemographicManager(DemographicManagerTickler demographicManager) {
		this.demographicMgr = demographicManager;
	}
	
	public void setProviderManager(ProviderManagerTickler providerMgr) {
		this.providerMgr = providerMgr;
	}
	
	public void setPreparedTicklerManager(PreparedTicklerManager preparedTicklerMgr) {
		this.preparedTicklerMgr = preparedTicklerMgr;
	}
	
	public void setConsultationManager(ConsultationManager consultationMgr) {
		this.consultationMgr = consultationMgr;
	}
	
	public void setChartManager(EChartManager eChartManager) {
		this.chartMgr = eChartManager;
	}
	
	String getProviderNo(HttpServletRequest request) {
		return (String)request.getSession().getAttribute("user");
	}
	/* default to 'list' */
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("unspecified");
		return filter(mapping,form,request,response);
	}
	
	public String getFrom(HttpServletRequest request) {
		String from = request.getParameter("from");
		if(from == null) {
			from = (String)request.getAttribute("from");
		}
		return from;
	}
	
	/* show all ticklers */
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("list");
		List ticklers = ticklerMgr.getTicklers();
		request.getSession().setAttribute("ticklers",ticklers);
		request.setAttribute("providers",providerMgr.getProviders());
		request.setAttribute("demographics",demographicMgr.getDemographics());
		request.setAttribute("customFilters",ticklerMgr.getCustomFilters(this.getProviderNo(request)));
		request.setAttribute("from",getFrom(request));
		return mapping.findForward("list");
	}
	
	/* show a tickler */
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("view");
		String tickler_id = request.getParameter("id");
		Tickler tickler = ticklerMgr.getTickler(tickler_id);
		request.setAttribute("tickler",tickler);
		request.setAttribute("providers",providerMgr.getProviders());
		request.setAttribute("from",getFrom(request));
		return mapping.findForward("view");
	}
	
	/* run a filter */
	/* show all ticklers */
	public ActionForward filter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("filter");
		DynaActionForm ticklerForm = (DynaActionForm)form;
		CustomFilter filter = (CustomFilter)ticklerForm.get("filter");
        List ticklers = ticklerMgr.getTicklers(filter);
        String filter_order=(String)request.getSession().getAttribute( "filter_order" );
        request.getSession().setAttribute("ticklers",ticklers);
		request.setAttribute("providers",providerMgr.getProviders());
		request.setAttribute("demographics",demographicMgr.getDemographics());
		request.setAttribute("customFilters",ticklerMgr.getCustomFilters(this.getProviderNo(request)));
		request.setAttribute("from",getFrom(request));
		request.getSession().setAttribute("filter_order",filter_order);
		return mapping.findForward("list");
	}
	
	/* run myfilter */
	/* show myticklers */
	public ActionForward my_tickler_filter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("my_tickler_filter");
		DynaActionForm ticklerForm = (DynaActionForm)form;
		CustomFilter filter = (CustomFilter)ticklerForm.get("filter");
		filter.setStart_date(null);
		filter.setEnd_date(new Date(System.currentTimeMillis()));
		filter.setProvider(null);
		filter.setStatus("A");
		filter.setPriority(null);
		filter.setClient(null);
		filter.setAssignee((String)request.getSession().getAttribute("user"));
		filter.setDemographic_webName(null);
        List ticklers = ticklerMgr.getTicklers(filter);
        request.getSession().setAttribute("ticklers",ticklers);
		request.setAttribute("providers",providerMgr.getProviders());
		request.setAttribute("demographics",demographicMgr.getDemographics());
		request.setAttribute("customFilters",ticklerMgr.getCustomFilters(this.getProviderNo(request)));
		request.setAttribute("from",getFrom(request));
		return mapping.findForward("list");
	}
	
	public ActionForward run_custom_filter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("run_custom_filter");
		DynaActionForm ticklerForm = (DynaActionForm)form;
        CustomFilter filter = (CustomFilter)ticklerForm.get("filter");
        String name = filter.getName();
        CustomFilter newFilter = ticklerMgr.getCustomFilter(name);
        if(newFilter == null) {
        	newFilter = new CustomFilter();
        }
        ticklerForm.set("filter",newFilter);
        return filter(mapping,form,request,response);
	}

	/*ningys-reassign a ticker */
	public ActionForward reassign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("reassign");

		String id = request.getParameter("id");
		String reassignee = request.getParameter("tickler.task_assigned_to");
		log.debug("reassign by" + id);

		ticklerMgr.reassign(id,getProviderNo(request),reassignee);
		return view(mapping,form,request,response);
	}
	
	
	/* delete a tickler */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("delete");
		String[] checks = request.getParameterValues("checkbox");

		for(int x=0;x<checks.length;x++) {
			ticklerMgr.deleteTickler(checks[x],getProviderNo(request));
		}
		return filter(mapping,form,request,response);
	}
	
	/* add a comment to a tickler */
	public ActionForward add_comment(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("add_comment");

		String id = request.getParameter("id");
		String message = request.getParameter("comment");
		log.debug("add_comment:" + id + "," + message);
		
		ticklerMgr.addComment(id,getProviderNo(request),message);

		return view(mapping,form,request,response);
	}
	
	
	
	
	
	
	/* complete a tickler */
	public ActionForward complete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("complete");
		String[] checks = request.getParameterValues("checkbox");

		for(int x=0;x<checks.length;x++) {
			ticklerMgr.completeTickler(checks[x],getProviderNo(request));
		}
		return filter(mapping,form,request,response);
	}
	
	/* edit a tickler */
	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("edit");
		request.setAttribute("providers",providerMgr.getProviders());
		request.setAttribute("from",getFrom(request));
		return mapping.findForward("edit");
	}
	
	/* save a tickler */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("save");
		Provider user = providerMgr.getProvider(getProviderNo(request));
		DynaActionForm ticklerForm = (DynaActionForm)form;
        Tickler tickler = (Tickler)ticklerForm.get("tickler");
        
        /* get service time */
        String service_hour = request.getParameter("tickler.service_hour");
        String service_minute = request.getParameter("tickler.service_minute");
        String service_ampm = request.getParameter("tickler.service_ampm");
        
        tickler.setServiceTime(service_hour + ":" + service_minute + " " + service_ampm);
        
        tickler.setUpdate_date(new java.util.Date());
        ticklerMgr.addTickler(tickler);

        String echart = request.getParameter("echart");
        if(echart != null && echart.equals("true")) {
        	Provider assignee = providerMgr.getProvider(tickler.getTask_assigned_to());
            
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        	SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yy : hh:mm a");
        	
    		/* get current chart */
    		EChart tempChart = chartMgr.getLatestChart(tickler.getDemographic_no());
    		String postedDate = formatter.format(tempChart.getTimeStamp());
    		
    		/* create new object */
    		EChart chart = new EChart();
    		BeanUtils.copyProperties(chart,tempChart);
    		String today = formatter.format(new Date());
    		
    		String e = chart.getEncounter();
    		StringBuffer buf = new StringBuffer(e);
    		buf.append("\n\n");
    		if(!today.equals(postedDate)) {
    			buf.append("__________________________________________________\n");
    			buf.append("[" + today + " .: ]");
    			buf.append("\n");
    		}
    		buf.append("Message from  [" + user.getFormattedName() + "] to [" + assignee.getFormattedName() + "] [assigned " + formatter2.format(tickler.getUpdate_date()) + "]\n");
    		buf.append("'" + tickler.getMessage() + "'");
    		chart.setEncounter(buf.toString());
    		chart.setId(null);
    		chartMgr.saveEncounter(chart);
        }
        
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("tickler.saved"));
        saveMessages(request,messages);
        
        ticklerForm.set("tickler",new Tickler());
		return filter(mapping,form,request,response);
	}
	
	/* get a list of prepared ticklers */
	public ActionForward prepared_tickler_list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("prepared_tickler_list");
		String path = this.getServlet().getServletContext().getRealPath("/");
		preparedTicklerMgr.setPath(path);
		request.setAttribute("preparedTicklers",preparedTicklerMgr.getTicklers());
		request.setAttribute("from",getFrom(request));
		return mapping.findForward("preparedTicklerList");
	}
	
	public ActionForward prepared_tickler_edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("prepared_tickler_edit");
		
		String name = request.getParameter("id");
		PreparedTickler pt = preparedTicklerMgr.getTickler(name);

		if(pt != null) {
			pt.setDependency("consultationManager",consultationMgr);
			pt.setDependency("ticklerManager",ticklerMgr);
			pt.setDependency("providerManager",providerMgr);
			ActionForward af =  pt.execute(mapping,form,request,response);
			if(af != null) {
				return af;
			}
		}

		return prepared_tickler_list(mapping,form,request,response);
	}
	
	/* complete a tickler */
	public ActionForward update_status(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("update_status");
		char status = request.getParameter("status").charAt(0);
		String provider = request.getParameter("provider");
		String id = request.getParameter("id");
		
		switch(status) {
		case 'A':
			ticklerMgr.activateTickler(id,getProviderNo(request));
			break;
		case 'C':
			ticklerMgr.completeTickler(id,getProviderNo(request));
			break;
		case 'D':
			ticklerMgr.deleteTickler(id,getProviderNo(request));
			break;
		}
		return this.view(mapping,form,request,response);
	}
}
