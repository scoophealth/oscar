package org.oscarehr.common.web;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;


import org.oscarehr.common.dao.FlowSheetDrugDAO;
import org.oscarehr.common.model.FlowSheetDrug;
import org.oscarehr.common.model.FlowSheetDx;

public class FlowSheetDrugAction extends DispatchAction {

    private static final Log log2 = LogFactory.getLog(FlowSheetDrugAction.class);
    private FlowSheetDrugDAO flowSheetDrugDAO;

    public void setFlowSheetDrugDAO(FlowSheetDrugDAO flowSheetDrugDAO) {
        this.flowSheetDrugDAO = flowSheetDrugDAO;
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log2.debug("AnnotationAction-unspec");
        //return setup(mapping, form, request, response);
        return null;
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String flowsheet = request.getParameter("flowsheet");
        String demographicNo = request.getParameter("demographic");
        String atcCode = request.getParameter("atcCode");

        FlowSheetDrug cust = new FlowSheetDrug();
        cust.setFlowsheet(flowsheet);
        cust.setAtcCode(atcCode);
        cust.setProviderNo((String) request.getSession().getAttribute("user"));
        cust.setDemographicNo(Integer.parseInt(demographicNo));
        cust.setCreateDate(new Date());

        log2.debug("SAVE " + cust);

        flowSheetDrugDAO.save(cust);
        
        ActionForward ff = mapping.findForward("success");
        //ff.setRedirect(true);
        ActionForward af = new ActionForward(ff.getPath()+"?demographic_no="+demographicNo+"&template="+flowsheet, true);
        



        //request.setAttribute("demographic", demographicNo);
        //request.setAttribute("flowsheet", flowsheet);
        return af;
    }
    
    
     public ActionForward dxSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String flowsheet = request.getParameter("flowsheet");
        String demographicNo = request.getParameter("demographic");
        String dxCode = request.getParameter("dxCode");
        String dxType = request.getParameter("dxCodeType");

        FlowSheetDx cust = new FlowSheetDx();
        cust.setFlowsheet(flowsheet);
        cust.setDxCode(dxCode);
        cust.setDxCodeType(dxType);
        cust.setProviderNo((String) request.getSession().getAttribute("user"));
        cust.setDemographicNo(Integer.parseInt(demographicNo));
        cust.setCreateDate(new Date());

        log2.debug("SAVE " + cust);

        flowSheetDrugDAO.save(cust);
        
        ActionForward ff = mapping.findForward("success");
        //ff.setRedirect(true);
        ActionForward af = new ActionForward(ff.getPath()+"?demographic_no="+demographicNo+"&template="+flowsheet, true);
        



        //request.setAttribute("demographic", demographicNo);
        //request.setAttribute("flowsheet", flowsheet);
        return af;
    }
    
    
    
}