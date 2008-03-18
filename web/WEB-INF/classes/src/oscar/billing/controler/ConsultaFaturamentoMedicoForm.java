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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import oscar.billing.model.Appointment;
import oscar.billing.model.Provider;


public class ConsultaFaturamentoMedicoForm extends ActionForm {
    private String dispatch;
    private int strutsAction;
    private String strutsButton = "";
    private String tipoPesquisa;
    private String dataInicial;
    private String dataFinal;
    private Provider medico;
    private List consultas;
    private String descTitle;

    public ConsultaFaturamentoMedicoForm() {
        this.medico = new Provider();
        this.consultas = new ArrayList();
        this.tipoPesquisa = Appointment.PENDENTE;
    }

    public int getStrutsAction() {
        return strutsAction;
    }

    public void setStrutsAction(int strutsAction) {
        this.strutsAction = strutsAction;
    }

    public void setStrutsButton(String strutsButton) {
        this.strutsButton = strutsButton;
    }

    public String getStrutsButton() {
        return strutsButton;
    }

    public ActionErrors validate(ActionMapping mapping,
        javax.servlet.http.HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);

        return errors;
    }

    public void clear() {
        tipoPesquisa = Appointment.PENDENTE;
        dataInicial = "";
        dataFinal = "";
        medico.clear();
        consultas.clear();
        dispatch = "";
    }

    /**
     * Returns the dispatch.
     * @return String
     */
    public String getDispatch() {
        return dispatch;
    }

    /**
     * Sets the dispatch.
     * @param dispatch The dispatch to set
     */
    public void setDispatch(String dispatch) {
        this.dispatch = dispatch;
    }

    /**
     * @see org.apache.struts.action.ActionForm#reset(ActionMapping, HttpServletRequest)
     */
    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);
    }

    /**
     * @return
     */
    public List getConsultas() {
        return consultas;
    }

    /**
     * @return
     */
    public String getDataFinal() {
        return dataFinal;
    }

    /**
     * @return
     */
    public String getDataInicial() {
        return dataInicial;
    }

    /**
     * @return
     */
    public Provider getMedico() {
        return medico;
    }

    /**
     * @return
     */
    public String getTipoPesquisa() {
        return tipoPesquisa;
    }

    /**
     * @param list
     */
    public void setConsultas(List list) {
        consultas = list;
    }

    /**
     * @param string
     */
    public void setDataFinal(String string) {
        dataFinal = string;
    }

    /**
     * @param string
     */
    public void setDataInicial(String string) {
        dataInicial = string;
    }

    /**
     * @param provider
     */
    public void setMedico(Provider provider) {
        medico = provider;
    }

    /**
     * @param string
     */
    public void setTipoPesquisa(String string) {
        tipoPesquisa = string;
    }

    /**
     * @return
     */
    public String getDescTitle() {
        if (tipoPesquisa.equals(oscar.billing.model.Appointment.AGENDADO)) {
			descTitle = "Agendados";
        } else if (tipoPesquisa.equals(oscar.billing.model.Appointment.FATURADO)) {
			descTitle = "Faturados";
        } else if (tipoPesquisa.equals(oscar.billing.model.Appointment.PENDENTE)) {
			descTitle = "Pendentes";
        }
        
        return  descTitle;
    }

    /**
     * @param string
     */
    public void setDescTitle(String string) {
        descTitle = string;
    }
}
