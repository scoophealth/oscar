/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mymeds.casemanagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author jackson
 */
public class CaseMgmtAction extends DispatchAction {
    Logger logger = Logger.getLogger(CaseMgmtAction.class);
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

            return mapping.findForward("casemgmt");
       }
}
