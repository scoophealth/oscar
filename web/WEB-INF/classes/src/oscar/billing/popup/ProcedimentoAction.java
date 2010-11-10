/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.billing.popup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.billing.cad.dao.CadProcedimentoDAO;
import oscar.util.OscarAction;
import oscar.util.PagerDef;

public class ProcedimentoAction extends OscarAction {
    private static Logger logger = MiscUtils.getLogger();

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ActionForward myforward = null;
        String myaction = mapping.getParameter();
        logger.debug(" [ProcedimentoAction] My action = " + myaction);

        if (isCancelled(request)) {
            logger.info(" [FormBillingAction] " + mapping.getAttribute() + " - acao foi cancelada");

            return mapping.findForward("cancel");
        }

        if ("".equalsIgnoreCase(myaction)) {
            myforward = mapping.findForward("failure");
        }
        else if ("INIT".equalsIgnoreCase(myaction)) {
            myforward = performInit(mapping, form, request, response);
        }
        else if ("FIND_PROC".equalsIgnoreCase(myaction)) {
            myforward = performFindProcedimento(mapping, form, request, response);
        }
        else {
            myforward = mapping.findForward("failure");
        }

        return myforward;
    }

    private ActionForward performInit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        logger.info(" [ProcedimentoAction] INIT");

        ProcedimentoForm form = (ProcedimentoForm)actionForm;

        try {
            form.clear();
            logger.info(" [ProcedimentoAction] Limpou form");
        }
        catch (Exception e) {
            generalError(request, e, "error.general");
            logger.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

    private ActionForward performFindProcedimento(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        logger.info(" [ProcedimentoAction] FIND_PROC");

        ProcedimentoForm form = (ProcedimentoForm)actionForm;

        try {
            CadProcedimentoDAO procDAO = new CadProcedimentoDAO();

            if (request.getParameter("coProc") != null) {
                form.setCodigoProc(request.getParameter("coProc"));
            }

            if (request.getParameter("dsProc") != null) {
                form.setDescProc(request.getParameter("dsProc"));
            }

            List procs = procDAO.list(form.getCodigoProc(), form.getDescProc());

            PagerDef pagerDef = pagination(mapping, request, procs);

            request.setAttribute("offset", new Integer(pagerDef.offset));
            request.setAttribute("pagerHeader", pagerDef.pagerHeader);
            request.setAttribute("length", new Integer(pagerDef.length));

            form.setProcedimentos(procs);

            logger.info(" [ProcedimentoAction] setou procedimentos");
        }
        catch (Exception e) {
            generalError(request, e, "error.general");
            logger.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

}
