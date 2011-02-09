/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.decisionSupport.service.DSService;
import org.oscarehr.util.MiscUtils;

/**
 *
 * @author apavel
 */
public class TestActionW extends Action {
     private static Logger log = MiscUtils.getLogger();
     private DSService dsService;
    
    /**
     * Creates a new instance of PHRLoginAction
     */
    public TestActionW() {
    }
    
     
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String demographic_no = request.getParameter("demographic_no");
        if (demographic_no == null) demographic_no = "1";
        response.getWriter().println(dsService.evaluateAndGetConsequences(demographic_no, (String) request.getSession().getAttribute("user")));
        return null;
    }

    public void setDsService(DSService dsService) {
        this.dsService = dsService;
    }
}