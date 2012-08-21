/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


/** Java class "Appointment.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;

import oscar.billing.cad.model.CadCid;
import oscar.billing.cad.model.CadProcedimentos;
import oscar.billing.fat.model.FatFormularioProcedimento;


/**
 * <p>
 *
 * </p>
 */
@Deprecated
public class Appointment {
    public static final String TODOS = "T";
    public static final String PENDENTE = "D";
    public static final String AGENDADO = "A";
    public static final String FATURADO = "F";
    public static final String PREENCHIDO = "P";

    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private long appointmentNo;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private Date appointmentDate;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private Date startTime;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private Date endTime;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String name;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String status;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String reason;
    private String billing;
    private String descBilling;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public Provider provider;

    /**
     * <p>
     *
     * </p>
     */
    public Demographic demographic;

    /**
     * <p>
     *
     * </p>
     */
    public List<ProcedimentoRealizado> procedimentoRealizado = new ArrayList<ProcedimentoRealizado>(); // of type ProcedimentoRealizado

    /**
     * <p>
     *
     * </p>
     */
    public List<Diagnostico> diagnostico = new ArrayList<Diagnostico>(); // of type Diagnostico

    /**
     *
     */
    public Appointment() {
    }

    ///////////////////////////////////////
    // operations

    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p>
     */
    public void clear() {
        appointmentNo = 0;
        appointmentDate = null;
        startTime = null;
        endTime = null;
        name = "";
        status = "";
        reason = "";

        if (provider != null) {
            provider.clear();
        }

        if (demographic != null) {
            //demographic.clear(); //this didn't do anything
        }

        procedimentoRealizado.clear();
        diagnostico.clear();
    }

    // end clear

    /**
     * @return
     */
    public Date getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * @return
     */
    public long getAppointmentNo() {
        return appointmentNo;
    }

    /**
     * @return
     */
    public Demographic getDemographic() {
        if (demographic != null) {
            return demographic;
        } else {
            demographic = new Demographic();

            return demographic;
        }
    }

    /**
     * @return
     */
    public List<Diagnostico> getDiagnostico() {
        return diagnostico;
    }

    /**
     * @return
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public List<ProcedimentoRealizado> getProcedimentoRealizado() {
        return procedimentoRealizado;
    }

    /**
     * @return
     */
    public Provider getProvider() {
        if (provider != null) {
            return provider;
        } else {
            provider = new Provider();

            return provider;
        }
    }

    /**
     * @return
     */
    public String getReason() {
        return reason;
    }

    /**
     * @return
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param date
     */
    public void setAppointmentDate(Date date) {
        appointmentDate = date;
    }

    /**
     * @param l
     */
    public void setAppointmentNo(long l) {
        appointmentNo = l;
    }

    /**
     * @param demographic
     */
    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    /**
     * @param collection
     */
    public void setDiagnostico(List<Diagnostico> collection) {
        diagnostico = collection;
    }

    /**
     * @param date
     */
    public void setEndTime(Date date) {
        endTime = date;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param collection
     */
    public void setProcedimentoRealizado(List<ProcedimentoRealizado> collection) {
        procedimentoRealizado = collection;
    }

    /**
     * @param provider
     */
    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    /**
     * @param string
     */
    public void setReason(String string) {
        reason = string;
    }

    /**
     * @param date
     */
    public void setStartTime(Date date) {
        startTime = date;
    }

    /**
     * @param string
     */
    public void setStatus(String string) {
        status = string;
    }

    public void addProcedimentos(List<FatFormularioProcedimento> procedimento) {
        for (int i = 0; i < procedimento.size(); i++) {
            FatFormularioProcedimento formProc = procedimento.get(i);
            ProcedimentoRealizado pr = new ProcedimentoRealizado();
            pr.setAppointment(this);
            pr.setCadProcedimentos(formProc.getCadProcedimentos());
            pr.setDtRealizacao(new Date());

            this.procedimentoRealizado.add(pr);
        }
    }

	public void addProcedimentos(CadProcedimentos procedimento) {
		ProcedimentoRealizado pr = new ProcedimentoRealizado();
		pr.setAppointment(this);
		pr.setCadProcedimentos(procedimento);
		pr.setDtRealizacao(new Date());
		this.procedimentoRealizado.add(pr);
	}

    public void removeProcedimentos(long id) {
        for (int i = 0; i < procedimentoRealizado.size(); i++) {
            ProcedimentoRealizado pr = procedimentoRealizado.get(i);
            MiscUtils.getLogger().debug("pr " +
                pr.getCadProcedimentos().getCoProcedimento());

            if (pr.getCadProcedimentos().getCoProcedimento() == id) {
                procedimentoRealizado.remove(i);
            }
        }
    }

    public void removeDiagnostico(String id) {
        for (int i = 0; i < diagnostico.size(); i++) {
            Diagnostico diag = diagnostico.get(i);

            if (diag.getCadCid().getCoCid().toUpperCase().trim().equals(id.toUpperCase()
                                                                              .trim())) {
                diagnostico.remove(i);
            }
        }
    }

    public void addDiagnostico(CadCid cid) {
        Diagnostico diag = new Diagnostico();
        diag.setAppointment(this);
        diag.setCadCid(cid);
        diag.setOrdem(diagnostico.size() + 1);

        this.diagnostico.add(diag);
    }

    /**
     * @return
     */
    public String getBilling() {
        return billing;
    }

    /**
     * @param string
     */
    public void setBilling(String string) {
        billing = string;
    }

    /**
     * @return
     */
    public String getDescBilling() {
        if (billing == null) {
            descBilling = "Pendente";
        } else {
            if (billing.equals(oscar.billing.model.Appointment.AGENDADO)) {
                descBilling = "Agendado";
            } else if (billing.equals(oscar.billing.model.Appointment.FATURADO)) {
                descBilling = "Faturado";
            } else if (billing.equals(oscar.billing.model.Appointment.PENDENTE)) {
                descBilling = "Pendente";
            } else if (billing.equals(
                        oscar.billing.model.Appointment.PREENCHIDO)) {
                descBilling = "Informado";
            }
        }

        return descBilling;
    }

    /**
     * @param string
     */
    public void setDescBilling(String string) {
        descBilling = string;
    }
}


// end Appointment
