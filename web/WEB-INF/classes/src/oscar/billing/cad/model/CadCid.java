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
/** Java class "CadCid.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.cad.model;

import oscar.billing.model.Diagnostico;


/**
 * <p>
 *
 * </p>
 */
public class CadCid {
    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String coCid;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dsCid;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dsReduzidocid;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public Diagnostico diagnostico;

	/**
	 * 
	 */
	public CadCid() {
		this.diagnostico = new Diagnostico();

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
    public String getCoCid() {
        return coCid;
    }

    /**
     * @return
     */
    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    /**
     * @return
     */
    public String getDsCid() {
        return dsCid;
    }

    /**
     * @return
     */
    public String getDsReduzidocid() {
        return dsReduzidocid;
    }

    /**
     * @param l
     */
    public void setCoCid(String l) {
        coCid = l;
    }

    /**
     * @param diagnostico
     */
    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    /**
     * @param string
     */
    public void setDsCid(String string) {
        dsCid = string;
    }

    /**
     * @param string
     */
    public void setDsReduzidocid(String string) {
        dsReduzidocid = string;
    }
}


// end CadCid
