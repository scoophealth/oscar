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
/** Java class "FatFormularios.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.fat.model;

import java.util.Collection;
import java.util.TreeSet;


/**
 * <p>
 *
 * </p>
 */
public class FatFormularios {
    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private int coFormulario;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dsFormulario;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String stAtivo;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public Collection<FatFormularioProcedimento> fatFormularioProcedimento = new TreeSet<FatFormularioProcedimento>(); // of type FatFormularioProcedimento

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
        coFormulario = 0;
        dsFormulario = "";
        fatFormularioProcedimento.clear();
    }
     // end clear        

    /**
     * @return
     */
    public int getCoFormulario() {
        return coFormulario;
    }

    /**
     * @return
     */
    public String getDsFormulario() {
        return dsFormulario;
    }

    /**
     * @return
     */
    public Collection<FatFormularioProcedimento> getFatFormularioProcedimento() {
        return fatFormularioProcedimento;
    }

    /**
     * @return
     */
    public String getStAtivo() {
        return stAtivo;
    }

    /**
     * @param i
     */
    public void setCoFormulario(int i) {
        coFormulario = i;
    }

    /**
     * @param string
     */
    public void setDsFormulario(String string) {
        dsFormulario = string;
    }

    /**
     * @param collection
     */
    public void setFatFormularioProcedimento(Collection<FatFormularioProcedimento> collection) {
        fatFormularioProcedimento = collection;
    }

    /**
     * @param string
     */
    public void setStAtivo(String string) {
        stAtivo = string;
    }
}
 // end FatFormularios
