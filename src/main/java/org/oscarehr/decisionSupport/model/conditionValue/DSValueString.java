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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.model.conditionValue;

import org.oscarehr.decisionSupport.model.DecisionSupportException;

/**
 *
 * @author apavel
 */
public class DSValueString extends DSValue {
    public DSValueString() {

    }

    //not always called, sometimes assessed manually by the DSDemographicAccess object
    public boolean testValue(String value) throws DecisionSupportException {
        return this.getValue().equals(value);
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (this.getValueType() != null) result.append(this.getValueType() + ":");
        if (this.getValueUnit() == null) {  //shouldn't be a unit anyways, otherwise must be a number, so should do statement (i.e. =5 kg)
            result.append("'" + this.getValue() + "'");
        } else {
            result.append(this.getValue());
            result.append(" " + this.getValueUnit());
        }
        return result.toString();
    }
    
}
