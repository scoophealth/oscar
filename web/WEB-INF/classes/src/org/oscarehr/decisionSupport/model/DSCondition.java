/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.model;

import org.oscarehr.decisionSupport.model.conditionValue.DSValue;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author apavel
 */
public class DSCondition {
    
    public enum ListOperator {any, all, not, notany, notall}

    protected DSDemographicAccess.Module module;
    protected ListOperator listOperator;
    protected List<DSValue> values;

    
    /**
     * @return the conditionType
     */
    public DSDemographicAccess.Module getConditionType() {
        return module;
    }

    /**
     * @param conditionType the conditionType to set
     */
    public void setConditionType(DSDemographicAccess.Module conditionType) {
        this.module = conditionType;
    }
    /**
     * @return the type
     */
    public ListOperator getListOperator() {
        return listOperator;
    }

    /**
     * @param type the type to set
     */
    public void setListOperator(ListOperator listOperator) {
        this.listOperator = listOperator;
    }

    public void setListOperatorStr(String listOperatorStr) throws IllegalArgumentException {
        try {
            this.listOperator = ListOperator.valueOf(listOperatorStr);
        } catch (IllegalArgumentException iae) {
            String allowedListOperators = StringUtils.join(ListOperator.values(), ",");
            throw new IllegalArgumentException("Cannot recognize list operator '" + listOperatorStr + "'.  Allowed list operators: " + allowedListOperators, iae);
        }
    }

    /**
     * @return the values
     */
    public List<DSValue> getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(List<DSValue> values) {
        this.values = values;
    }
}
