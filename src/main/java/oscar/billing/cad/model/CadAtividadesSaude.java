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
/** Java class "CadAtividadesSaude.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.cad.model;

import oscar.billing.fat.model.FatBpa;
import oscar.billing.model.Provider;



/**
 * <p>
 *
 * </p>
 */
public class CadAtividadesSaude {
    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private long coAtividade;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dsAtividade;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public FatBpa fatBpa;

    /**
     * <p>
     *
     * </p>
     */
    public Provider provider;

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
    public long getCoAtividade() {
        return coAtividade;
    }

    /**
     * @return
     */
    public String getDsAtividade() {
        return dsAtividade;
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
    public Provider getProvider() {
        return provider;
    }

    /**
     * @param l
     */
    public void setCoAtividade(long l) {
        coAtividade = l;
    }

    /**
     * @param string
     */
    public void setDsAtividade(String string) {
        dsAtividade = string;
    }

    /**
     * @param bpa
     */
    public void setFatBpa(FatBpa bpa) {
        fatBpa = bpa;
    }

    /**
     * @param provider
     */
    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
 // end CadAtividadesSaude
