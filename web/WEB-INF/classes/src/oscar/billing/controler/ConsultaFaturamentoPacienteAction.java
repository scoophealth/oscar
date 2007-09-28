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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.billing.controler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.billing.dao.AppointmentDAO;
import oscar.billing.model.Demographic;
import oscar.util.OscarAction;


public class ConsultaFaturamentoPacienteAction extends OscarAction {
    private static Logger logger = LogManager.getLogger(ConsultaFaturamentoPacienteAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
        ActionForward myforward = null;
        String myaction = mapping.getParameter();
        logger.debug(" [ConsultaFaturamentoPacienteAction] My action = " + myaction);

        if (isCancelled(request)) {
            logger.info(" [ConsultaFaturamentoPacienteAction] " +
                mapping.getAttribute() + " - acao foi cancelada");

            return mapping.findForward("cancel");
        }

        if ("".equalsIgnoreCase(myaction)) {
            myforward = mapping.findForward("failure");
        } else if ("INIT".equalsIgnoreCase(myaction)) {
            myforward = performInit(mapping, form, request, response);
        } else {
            myforward = mapping.findForward("failure");
        }

        return myforward;
    }

    private ActionForward performInit(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        logger.info(" [ConsultaFaturamentoPacienteAction] INIT");

        ConsultaFaturamentoPacienteForm form = (ConsultaFaturamentoPacienteForm) actionForm;

        try {
            form.clear();
            logger.info(" [ConsultaFaturamentoPacienteAction] Limpou form");
            
            String id = request.getParameter("demographic_no");
			logger.info(" [ConsultaFaturamentoPacienteAction] id " + id);
            Demographic demographic = new Demographic();
            demographic.setDemographicNo(Long.parseLong(id));

            AppointmentDAO appDAO = new AppointmentDAO(getPropertiesDb(
                        request));

            //Obter lista de faturamentos
            request.setAttribute("BILLING", appDAO.listFatPatiente(demographic));
            logger.info(
                " [ConsultaFaturamentoPacienteAction] setou billings no form");
        } catch (Exception e) {
            generalError(request, e, "error.general");
            logger.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

}
