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

import org.apache.log4j.Category;

import org.apache.struts.action.*;

import oscar.billing.cad.dao.CadProcedimentoDAO;

import oscar.util.OscarAction;
import oscar.util.PagerDef;

import java.util.List;

import javax.servlet.http.*;


public class ProcedimentoAction extends OscarAction {
    static Category cat = Category.getInstance(ProcedimentoAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
        ActionForward myforward = null;
        String myaction = mapping.getParameter();
        cat.debug(" [ProcedimentoAction] My action = " + myaction);

        if (isCancelled(request)) {
            cat.info(" [FormBillingAction] " + mapping.getAttribute() +
                " - acao foi cancelada");

            return mapping.findForward("cancel");
        }

        if ("".equalsIgnoreCase(myaction)) {
            myforward = mapping.findForward("failure");
        } else if ("INIT".equalsIgnoreCase(myaction)) {
            myforward = performInit(mapping, form, request, response);
        } else if ("FIND_PROC".equalsIgnoreCase(myaction)) {
            myforward = performFindProcedimento(mapping, form, request, response);
        } else {
            myforward = mapping.findForward("failure");
        }

        return myforward;
    }

    private ActionForward performInit(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        cat.info(" [ProcedimentoAction] INIT");

        ProcedimentoForm form = (ProcedimentoForm) actionForm;

        try {
            form.clear();
            cat.info(" [ProcedimentoAction] Limpou form");
        } catch (Exception e) {
            generalError(request, e, "error.general");
            cat.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

    private ActionForward performFindProcedimento(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        cat.info(" [ProcedimentoAction] FIND_PROC");

        ProcedimentoForm form = (ProcedimentoForm) actionForm;

        try {
			CadProcedimentoDAO procDAO = new CadProcedimentoDAO(getPropertiesDb(
			request));

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

            cat.info(" [ProcedimentoAction] setou procedimentos");
        } catch (Exception e) {
            generalError(request, e, "error.general");
            cat.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

}
