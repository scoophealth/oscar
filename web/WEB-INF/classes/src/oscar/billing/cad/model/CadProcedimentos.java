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
/** Java class "CadProcedimentos.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.cad.model;

import java.util.Date;

import oscar.billing.fat.model.FatBpa;
import oscar.billing.fat.model.FatFormularioProcedimento;
import oscar.billing.model.ProcedimentoRealizado;


/**
 * <p>
 *
 * </p>
 */
public class CadProcedimentos {
	public static final String AMBULATORIAL = "A";
	public static final String ATIVO = "A";
	public static final String INATIVO = "I";
	
    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private long coProcedimento;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dsProcedimento;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dsReduzida;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String stProcedimento;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private Date dtIniciovalidade;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private double vlProcedimento;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public ProcedimentoRealizado procedimentoRealizado;

    /**
     * <p>
     *
     * </p>
     */
    public FatFormularioProcedimento fatFormularioProcedimento;

    /**
     * <p>
     *
     * </p>
     */
    public FatBpa fatBpa;

	/**
	 * 
	 */
	public CadProcedimentos() {

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
        // your code here
    }
     // end clear        

    /**
     * @return
     */
    public long getCoProcedimento() {
        return coProcedimento;
    }

    /**
     * @return
     */
    public String getDsProcedimento() {
        return dsProcedimento;
    }

    /**
     * @return
     */
    public String getDsReduzida() {
        return dsReduzida;
    }

    /**
     * @return
     */
    public Date getDtIniciovalidade() {
        return dtIniciovalidade;
    }

    /**
     * @return
     */
    public FatBpa getFatBpa() {
        return fatBpa;
    }

    /**
     * @return
     */
    public FatFormularioProcedimento getFatFormularioProcedimento() {
		if (fatFormularioProcedimento != null) {
			return fatFormularioProcedimento;
		} else {
			fatFormularioProcedimento = new FatFormularioProcedimento();
			return fatFormularioProcedimento;
		}        
    }

    /**
     * @return
     */
    public ProcedimentoRealizado getProcedimentoRealizado() {
		if (procedimentoRealizado != null) {
			return procedimentoRealizado;
		} else {
			procedimentoRealizado = new ProcedimentoRealizado();
			return procedimentoRealizado;
		}        
    }

    /**
     * @return
     */
    public String getStProcedimento() {
        return stProcedimento;
    }

    /**
     * @return
     */
    public double getVlProcedimento() {
        return vlProcedimento;
    }

    /**
     * @param l
     */
    public void setCoProcedimento(long l) {
        coProcedimento = l;
    }

    /**
     * @param string
     */
    public void setDsProcedimento(String string) {
        dsProcedimento = string;
    }

    /**
     * @param string
     */
    public void setDsReduzida(String string) {
        dsReduzida = string;
    }

    /**
     * @param date
     */
    public void setDtIniciovalidade(Date date) {
        dtIniciovalidade = date;
    }

    /**
     * @param bpa
     */
    public void setFatBpa(FatBpa bpa) {
        fatBpa = bpa;
    }

    /**
     * @param procedimento
     */
    public void setFatFormularioProcedimento(
        FatFormularioProcedimento procedimento) {
        fatFormularioProcedimento = procedimento;
    }

    /**
     * @param realizado
     */
    public void setProcedimentoRealizado(ProcedimentoRealizado realizado) {
        procedimentoRealizado = realizado;
    }

    /**
     * @param string
     */
    public void setStProcedimento(String string) {
        stProcedimento = string;
    }

    /**
     * @param d
     */
    public void setVlProcedimento(double d) {
        vlProcedimento = d;
    }
}
 // end CadProcedimentos
