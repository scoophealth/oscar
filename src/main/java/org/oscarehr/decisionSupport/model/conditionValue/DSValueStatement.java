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

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.decisionSupport.model.DecisionSupportException;

/**
 *
 * @author apavel
 */
public class DSValueStatement extends DSValue {
    private enum Operator {
        lessThan("<"),
        lessThanEqualTo("<="),
        greaterThan(">"),
        greaterThanEqualTo(">="),
        equal("="),
        between("-");

        String symbol;

        private Operator(String symbol) {this.symbol = symbol; }
        private static Operator getOperator(String symbol) throws IllegalArgumentException {
            for (Operator currentOperator: Operator.values()) {
                if (currentOperator.getSymbol().equals(symbol)) return currentOperator;
            }
            throw new IllegalArgumentException("Cannot get enum Operator from '" + symbol + "'");
        }
        private static String[] getOperatorSymbols() {
            ArrayList<String> operatorSymbols = new ArrayList<String>();
            for (Operator currentOperator: Operator.values()) {
                operatorSymbols.add(currentOperator.getSymbol());
            }
            return (String[]) operatorSymbols.toArray();
        }
        private String getSymbol() { return symbol; }
    }

    private Operator operator;

    public boolean testValue(String testValue) throws DecisionSupportException {
        try {
            int testValueInt = Integer.parseInt(testValue);
            int valueInt = Integer.parseInt(this.getValue());
            switch (operator) {
                case lessThan: return testValueInt < valueInt;
                case lessThanEqualTo: return testValueInt <= valueInt;
                case greaterThan: return testValueInt > valueInt;
                case greaterThanEqualTo: return testValueInt >= valueInt;
                case equal: return testValueInt == valueInt;
            }

         //   if (this.operator == Operator.lessThan) {
         //       return testValueInt < valueInt;
         //   } else
        } catch (NumberFormatException nfe) {
            throw new DecisionSupportException("cannot parse value '" + testValue + "' or '" + this.getValue() + "' to int to evaluate a compare statement)", nfe);
        }
        throw new DecisionSupportException("One of the operators is not defined");
    }

    public String getOperator() {
        return operator.getSymbol();
    }

    public void setOperator(String operator) throws IllegalArgumentException {
        try {
            this.operator = Operator.getOperator(operator);
        } catch (IllegalArgumentException iae) {
            String allowedOperators = StringUtils.join(Operator.getOperatorSymbols(), ",");
            throw new IllegalArgumentException("Operator '" + operator + "' not supported.  Supported operators; " + allowedOperators, iae);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (this.getValueType() != null) result.append(this.getValueType() + ":");
        if (this.getOperator() != null) result.append(this.getOperator());
        result.append(this.getValue());
        if (this.getValueUnit() != null) result.append(" " + this.getValueUnit());
        return result.toString();
    }


}
