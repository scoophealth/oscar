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

package org.oscarehr.decisionSupport.model;

import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.decisionSupport.model.conditionValue.DSValue;
import org.oscarehr.util.MiscUtils;

/**
 *
 * @author apavel
 */
public class DSCondition {
    private static final Logger _log = MiscUtils.getLogger();

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
    private String desc  = null;
    
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
     * @param listOperator
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
    
    
}
