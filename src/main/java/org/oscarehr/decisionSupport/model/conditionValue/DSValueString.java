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
