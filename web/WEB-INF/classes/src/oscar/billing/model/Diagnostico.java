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
/** Java class "Diagnostico.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.model;

import oscar.billing.cad.model.CadCid;


/**
 * <p>
 *
 * </p>
 */
public class Diagnostico {
    ///////////////////////////////////////
    // attributes
    private boolean isSave = false;

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
    public CadCid cadCid;

    /**
     * <p>
     *
     * </p>
     */
    public Appointment appointment;

    /**
     *
     */
    public Diagnostico() {
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
    public Appointment getAppointment() {
		if (appointment != null) {
			return appointment;
		} else {
			appointment = new Appointment();
			return appointment;
		}
    }

    /**
     * @return
     */
    public CadCid getCadCid() {
		if (cadCid != null) {
			return cadCid;
		} else {
			cadCid = new CadCid();
			return cadCid;
		}
    }

    /**
     * @return
     */
    public int getOrdem() {
        return ordem;
    }

    /**
     * @param appointment
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * @param cid
     */
    public void setCadCid(CadCid cid) {
        cadCid = cid;
    }

    /**
     * @param i
     */
    public void setOrdem(int i) {
        ordem = i;
    }

    /**
     * @return
     */
    public boolean isSave() {
        return isSave;
    }

    /**
     * @param b
     */
    public void setSave(boolean b) {
        isSave = b;
    }
}


// end Diagnostico
