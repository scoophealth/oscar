/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.caisi.tickler.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.caisi.service.DemographicManagerTickler;
import org.caisi.tickler.prepared.PreparedTickler;
import org.caisi.tickler.prepared.PreparedTicklerManager;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.dao.EChartDao;
import org.oscarehr.common.model.CustomFilter;
import org.oscarehr.common.model.EChart;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class TicklerAction extends DispatchAction {
    private static Logger log = MiscUtils.getLogger();
    private TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
    private ProviderManager providerMgr = null;
    private PreparedTicklerManager preparedTicklerMgr = null;
    private DemographicManagerTickler demographicMgr = null;
    private EChartDao echartDao = null;
    private ProgramManager programMgr = null;



    public void setDemographicManager(DemographicManagerTickler demographicManager) {
        this.demographicMgr = demographicManager;
    }

    public void setProviderManager(ProviderManager providerMgr) {
        this.providerMgr = providerMgr;
    }

    public void setPreparedTicklerManager(PreparedTicklerManager preparedTicklerMgr) {
        this.preparedTicklerMgr = preparedTicklerMgr;
    }

    public void setProgramManager(ProgramManager programMgr) {
		this.programMgr = programMgr;
	}

	String getProviderNo(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("user");
    }

    /* default to 'list' */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("unspecified");
        return filter(mapping, form, request, response);
    }

    public String getFrom(HttpServletRequest request) {
        String from = request.getParameter("from");
        if (from == null) {
            from = (String) request.getAttribute("from");
        }
        return from;
    }


    /* show a tickler */
    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
        log.debug("view");
        String tickler_id = request.getParameter("id");
        Tickler tickler = ticklerManager.getTickler(tickler_id);
        request.setAttribute("tickler", tickler);
        
        
        // only active providers listed in the program stuff can be assigned
        if (tickler.getProgramId()!=null) 
        	request.setAttribute("providers", providerMgr.getActiveProviders(null, tickler.getProgramId().toString()));
        else 
        	request.setAttribute("providers", providerMgr.getActiveProviders());
        
        request.setAttribute("from", getFrom(request));

        return mapping.findForward("view");
    }

    /* run a filter */
    /* show all ticklers */
    public ActionForward filter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("filter");
        DynaActionForm ticklerForm = (DynaActionForm) form;
        CustomFilter filter = (CustomFilter) ticklerForm.get("filter");

        //view tickler from CME
        String filter_clientId = filter.getDemographicNo();
        String filter_clientName = filter.getDemographic_webName();
        if (filter_clientId != null && !"".equals(filter_clientId)) {
        	if (filter_clientName == null || "".equals(filter_clientName)) {
        		filter.setDemographic_webName(demographicMgr.getDemographic(filter_clientId).getFormattedName());
        	}
        } else {
        	filter_clientName = "";
        	filter.setDemographic_webName("");
        }

        String providerId = (String)request.getSession().getAttribute("user");
        String programId = "";

        List<Program> programs=programMgr.getProgramDomainInCurrentFacilityForCurrentProvider(true);
        request.setAttribute("programs", programs);


        List<Tickler> ticklers = ticklerManager.getTicklers(filter,providerId, programId);

        List<CustomFilter> cf = ticklerManager.getCustomFilters(this.getProviderNo(request));
        // make my tickler filter
        boolean myticklerexisted = false;
        for (int i = 0; i < cf.size(); i++) {
            if ((cf.get(i).getName()).equals("*Myticklers*")) {
                myticklerexisted = true;
            }
        }
        if (!myticklerexisted) {

            CustomFilter myfilter = new CustomFilter();
            myfilter.setName("*Myticklers*");
            myfilter.setStartDateWeb("");
            // myfilter.setEnd_date(new Date(System.currentTimeMillis()));
            myfilter.setEndDateWeb("");
            myfilter.setProviderNo(this.getProviderNo(request));
            myfilter.setStatus("A");
            myfilter.setPriority("");
            myfilter.setClient("");
            myfilter.setAssignee((String) request.getSession().getAttribute("user"));
            myfilter.setDemographic_webName("");
            myfilter.setDemographicNo("");
            myfilter.setProgramId("");
            ticklerManager.saveCustomFilter(myfilter);
        }

        String filter_order = (String) request.getSession().getAttribute("filter_order");
        request.getSession().setAttribute("ticklers", ticklers);
        request.setAttribute("providers", providerMgr.getProviders());
        if( OscarProperties.getInstance().getBooleanProperty("clientdropbox","on") ) {
            request.setAttribute("demographics", demographicMgr.getDemographics());
        }

		request.setAttribute("customFilters", ticklerManager.getCustomFilters(this.getProviderNo(request)));
        request.setAttribute("from", getFrom(request));
        request.getSession().setAttribute("filter_order", filter_order);
        return mapping.findForward("list");
    }

    /* run myfilter */
    /* show myticklers */
    public ActionForward my_tickler_filter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("my_tickler_filter");
        DynaActionForm ticklerForm = (DynaActionForm) form;
        CustomFilter filter = (CustomFilter) ticklerForm.get("filter");
        filter.setStartDate(null);
        filter.setEndDate(new Date(System.currentTimeMillis()));
        filter.setProvider(null);
        filter.setStatus("A");
        filter.setPriority(null);
        filter.setClient(null);
        filter.setAssignee((String) request.getSession().getAttribute("user"));
        filter.setDemographic_webName(null);
        filter.setProgramId(null);
        String providerId = (String)request.getSession().getAttribute("user");
        String programId = "";
        List<Tickler> ticklers = ticklerManager.getTicklers(filter,providerId,programId);
        request.getSession().setAttribute("ticklers", ticklers);
        request.setAttribute("providers", providerMgr.getProviders());
        if( OscarProperties.getInstance().getBooleanProperty("clientdropbox","on") ) {
            request.setAttribute("demographics", demographicMgr.getDemographics());
        }

		request.setAttribute("programs", programMgr.getProgramDomainInCurrentFacilityForCurrentProvider(true));

		request.setAttribute("customFilters", ticklerManager.getCustomFilters(this.getProviderNo(request)));
        request.setAttribute("from", getFrom(request));
        return mapping.findForward("list");
    }

    public ActionForward run_custom_filter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("run_custom_filter");
        DynaActionForm ticklerForm = (DynaActionForm) form;
        CustomFilter filter = (CustomFilter) ticklerForm.get("filter");
        String name = filter.getName();
        // CustomFilter newFilter = ticklerMgr.getCustomFilter(name);
        CustomFilter newFilter = ticklerManager.getCustomFilter(name, this.getProviderNo(request));

        /*
         * String filterId = Long.toString(filter.getId()); CustomFilter newFilter = ticklerMgr.getCustomFilterById(Integer.valueOf(filterId));
         */
        if (newFilter == null) {
            newFilter = new CustomFilter();
        }
        ticklerForm.set("filter", newFilter);
        return filter(mapping, form, request, response);
    }

    /* ningys-reassign a ticker */
    public ActionForward reassign(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("reassign");

        String id = request.getParameter("id");
        String reassignee = request.getParameter("tickler.taskAssignedTo");
        log.debug("reassign by" + id);

        ticklerManager.reassign(Integer.parseInt(id), getProviderNo(request), reassignee);

        DynaActionForm ticklerForm = (DynaActionForm) form;
        ticklerForm.set("tickler", new Tickler());

        return view(mapping, form, request, response);
    }

    /* delete a tickler */
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("delete");
        String[] checks = request.getParameterValues("checkbox");

        for (int x = 0; x < checks.length; x++) {
        	ticklerManager.deleteTickler(Integer.parseInt(checks[x]), getProviderNo(request));
        }
        return filter(mapping, form, request, response);
    }

    /* add a comment to a tickler */
    public ActionForward add_comment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("add_comment");

        String id = request.getParameter("id");
        String message = request.getParameter("comment");
        log.debug("add_comment:" + id + "," + message);

        ticklerManager.addComment(Integer.parseInt(id), getProviderNo(request), message);

        return view(mapping, form, request, response);
    }

    /* complete a tickler */
    public ActionForward complete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
        log.debug("complete");
        String[] checks = request.getParameterValues("checkbox");

        for (int x = 0; x < checks.length; x++) {
        	ticklerManager.completeTickler(Integer.parseInt(checks[x]), getProviderNo(request));
        }
        return filter(mapping, form, request, response);
    }

    /* edit a tickler */
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("edit");
        String programId = (String) request.getSession().getAttribute(SessionConstants.CURRENT_PROGRAM_ID);
        if(programId == null) {
        	programId = String.valueOf(programMgr.getProgramIdByProgramName("OSCAR"));
        }
        request.setAttribute("providers", providerMgr.getActiveProviders(null, programId));
        request.setAttribute("program_name", programMgr.getProgramName(programId));
        request.setAttribute("from", getFrom(request));
        return mapping.findForward("edit");
    }

    /* save a tickler */
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("save");
        Provider user = providerMgr.getProvider(getProviderNo(request));
        DynaActionForm ticklerForm = (DynaActionForm) form;
        Tickler tickler = (Tickler) ticklerForm.get("tickler");

        // set the program which the tickler was written in if there is a program.
        String programIdStr = (String) request.getSession().getAttribute(SessionConstants.CURRENT_PROGRAM_ID);
        if (programIdStr != null) tickler.setProgramId(Integer.valueOf(programIdStr));

        /* get service time */
        String service_hour = request.getParameter("tickler.service_hour");
        String service_minute = request.getParameter("tickler.service_minute");
        String service_ampm = request.getParameter("tickler.service_ampm");
        tickler.setServiceTime(service_hour + ":" + service_minute + " " + service_ampm);

        tickler.setUpdateDate(new java.util.Date());

        ticklerManager.addTickler(tickler);

        String echart = request.getParameter("echart");
        if (echart != null && echart.equals("true")) {
            Provider assignee = providerMgr.getProvider(tickler.getTaskAssignedTo());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yy : hh:mm a");

            /* get current chart */
            EChart tempChart = echartDao.getLatestChart(tickler.getDemographicNo());
            String postedDate = "";
            if (tempChart != null) {
                postedDate = formatter.format(tempChart.getTimestamp());
            }

            /* create new object */
            EChart chart = new EChart();
            if (tempChart != null) {
                BeanUtils.copyProperties(chart, tempChart);
            }
            else {
                String curUser_no = (String) request.getSession().getAttribute("user");
                chart.setProviderNo(curUser_no);
            }
            String today = formatter.format(new Date());

            String e = chart.getEncounter();
            StringBuilder buf;
            if (e != null) {
                buf = new StringBuilder(e);
            }
            else {
                buf = new StringBuilder();
            }
            buf.append("\n\n");
            if (!today.equals(postedDate)) {
                buf.append("__________________________________________________\n");
                buf.append("[" + today + " .: ]");
                buf.append("\n");
            }
            buf.append("Message from  [" + user.getFormattedName() + "] to [" + assignee.getFormattedName() + "] [assigned " + formatter2.format(tickler.getUpdateDate()) + "]\n");
            buf.append("'" + tickler.getMessage() + "'");
            chart.setEncounter(buf.toString());
            chart.setId(null);
            echartDao.persist(chart);
        }

        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("tickler.saved"));
        saveMessages(request, messages);

		CustomFilter filter = new CustomFilter();
        filter.setDemographicNo(tickler.getDemographicNo().toString());
        filter.setDemographic_webName(tickler.getDemographic_webName());
        filter.setEndDate(null);
		ticklerForm.set("filter", filter);
		ticklerForm.set("tickler", new Tickler());
      //  return filter(mapping, form, request, response);
        ActionForward af = new ActionForward();
        af.setRedirect(true);
        af.setPath("/Tickler.do?tickler.demographic_webName="+tickler.getDemographic_webName()+"&tickler.demographicNo="+tickler.getDemographicNo());
        return af;

    }

    /* get a list of prepared ticklers */
    public ActionForward prepared_tickler_list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("prepared_tickler_list");
        String path = this.getServlet().getServletContext().getRealPath("/");
        preparedTicklerMgr.setPath(path);
        request.setAttribute("preparedTicklers", preparedTicklerMgr.getTicklers());
        request.setAttribute("from", getFrom(request));
        return mapping.findForward("preparedTicklerList");
    }

    public ActionForward prepared_tickler_edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("prepared_tickler_edit");

        String name = request.getParameter("id");
        PreparedTickler pt = preparedTicklerMgr.getTickler(name);

        if (pt != null) {
            pt.setDependency("ticklerManager", ticklerManager);
            pt.setDependency("providerManager", providerMgr);
            ActionForward af = pt.execute(mapping, form, request, response);
            if (af != null) {
                return af;
            }
        }

        return prepared_tickler_list(mapping, form, request, response);
    }

    /* complete a tickler */
    public ActionForward update_status(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
        log.debug("update_status");
        char status = request.getParameter("status").charAt(0);

        String id = request.getParameter("id");

        switch (status) {
            case 'A':
                ticklerManager.activateTickler(Integer.parseInt(id), getProviderNo(request));
                break;
            case 'C':
            	ticklerManager.completeTickler(Integer.parseInt(id), getProviderNo(request));
                break;
            case 'D':
            	ticklerManager.deleteTickler(Integer.parseInt(id), getProviderNo(request));
                break;
        }
        return this.view(mapping, form, request, response);
    }

    public boolean isModuleLoaded(HttpServletRequest request, String moduleName) {


        OscarProperties proper = OscarProperties.getInstance();

        if (proper.getProperty(moduleName, "").equalsIgnoreCase("yes") || proper.getProperty(moduleName, "").equalsIgnoreCase("true") || proper.getProperty(moduleName, "").equalsIgnoreCase("on")) {
            return true;
        }

        return false;
    }
}
