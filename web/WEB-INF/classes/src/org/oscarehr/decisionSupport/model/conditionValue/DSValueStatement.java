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
            ArrayList<String> operatorSymbols = new ArrayList();
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
