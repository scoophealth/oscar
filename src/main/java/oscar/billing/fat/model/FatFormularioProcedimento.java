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
/** Java class "FatFormularioProcedimento.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.fat.model;

import oscar.billing.cad.model.CadProcedimentos;


/**
 * <p>
 *
 * </p>
 */
public class FatFormularioProcedimento {
    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private int ordem;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public FatFormularios fatFormularios;

    /**
     * <p>
     *
     * </p>
     */
    public CadProcedimentos cadProcedimentos;

	/**
	 * 
	 */
	public FatFormularioProcedimento() {
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
    public CadProcedimentos getCadProcedimentos() {
		if (cadProcedimentos != null) {
			return cadProcedimentos;
		} else {
			this.cadProcedimentos = new CadProcedimentos();
			return cadProcedimentos;
		}        
    }

    /**
     * @return
     */
    public FatFormularios getFatFormularios() {
		if (fatFormularios != null) {
			return fatFormularios;
		} else {
			fatFormularios = new FatFormularios(); 
			return fatFormularios;
		}        
    }

    /**
     * @return
     */
    public int getOrdem() {
        return ordem;
    }

    /**
     * @param procedimentos
     */
    public void setCadProcedimentos(CadProcedimentos procedimentos) {
        cadProcedimentos = procedimentos;
    }

    /**
     * @param formularios
     */
    public void setFatFormularios(FatFormularios formularios) {
        fatFormularios = formularios;
    }

    /**
     * @param i
     */
    public void setOrdem(int i) {
        ordem = i;
    }
}
 // end FatFormularioProcedimento
