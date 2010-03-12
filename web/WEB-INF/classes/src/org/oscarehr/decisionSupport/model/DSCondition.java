/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.model;

import java.util.Hashtable;
import org.oscarehr.decisionSupport.model.conditionValue.DSValue;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author apavel
 */
public class DSCondition {
    private static final Log _log = LogFactory.getLog(DSCondition.class);

    /**
     * @return the param
     */
    public Hashtable getParam() {
        return param;
    }

    /**
     * @param param the param to set
     */
    public void setParam(Hashtable param) {
        this.param = param;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        _log.debug("getting label in "+this.hashCode());
        return label;
    }

    /**
     * Label is used to match the asserted value in the drools engine
     * @param label the label to set
     */
    public void setLabel(String label) {
        _log.debug("Setting label in "+this.hashCode());
        this.label = label;
    }
    
    public enum ListOperator {any, all, not, notany, notall}

    protected DSDemographicAccess.Module module;
    protected ListOperator listOperator;
    protected List<DSValue> values;
    private Hashtable param = null;
    private String label = null;

    
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
