/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.casemgmt.model;

import java.io.Serializable;

/**
 * This is the object class that relates to the providerext table.
 * Any customizations belong here.
 */
public class Providerext implements Serializable {
    private String _providerNo;
    private String _signature;

    // constructors
    public Providerext () {
        initialize();
    }


    /*[CONSTRUCTOR MARKER END]*/
    protected void initialize () {}

    /**
     * Return the value associated with the column: provider_no
     */
    public String getProviderNo () {
        return _providerNo;
    }

    /**
     * Set the value related to the column: provider_no
     * @param _providerNo the provider_no value
     */
    public void setProviderNo (String _providerNo) {
        this._providerNo = _providerNo;
    }

    /**
     * Return the value associated with the column: signature
     */
    public String getSignature () {
        return _signature;
    }

    /**
     * Set the value related to the column: signature
     * @param _signature the signature value
     */
    public void setSignature (String _signature) {
        this._signature = _signature;
    }

    public String toString () {
        return super.toString();
    }
}
