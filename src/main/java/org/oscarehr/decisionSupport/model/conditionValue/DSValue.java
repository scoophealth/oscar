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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.decisionSupport.model.DecisionSupportException;
import org.oscarehr.util.MiscUtils;

/**
 *
 * @author apavel
 */
public abstract class DSValue {
    private static final Logger _log = MiscUtils.getLogger();

    private String valueType;
    private String valueUnit;
    private String value;


    public DSValue() {
    }

    /**
     * @return the valueType
     */
    public String getValueType() {
        return valueType;
    }

    /**
     * @param valueType the valueType to set
     */
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (this.getValueType() != null) result.append(this.getValueType() + ":");
        result.append(this.getValue());
        if (this.getValueUnit() != null) result.append(" " + this.getValueUnit());
        return result.toString();
    }

    public static List<DSValue> createDSValues(String values) {
        String[] dsValuesStr = new String[0];
        boolean doHyphenSearch = true;
        Pattern stringQuotePattern = Pattern.compile("'.+?'");
        if (stringQuotePattern.matcher(values).find()) {  //if has a pair of quotes with something in it - treat as a list of strings
            Pattern stringSeparatorPattern = Pattern.compile("'[\\s]*,");
            String[] separatedValues = stringSeparatorPattern.split(values); // [ ',' ] is absolutely illegal in a quoted string
            ArrayList<String> dsValueStrArray = new ArrayList<String>();
            for (String separatedValue: separatedValues) {
                if (!separatedValue.trim().endsWith("'")) {
                    separatedValue = separatedValue.trim() + "'";
                }
                dsValueStrArray.add(separatedValue);
                _log.debug("Separated Value: " + separatedValue);
            }

            dsValuesStr = dsValueStrArray.toArray(dsValuesStr);
            doHyphenSearch = false;
        } else {
            dsValuesStr = StringUtils.split(values, ",");
        }
        ArrayList<DSValue> dsValues = new ArrayList<DSValue>();
        for (String dsValueStr: dsValuesStr) {
            int hyphenIndex = -1;
            if (doHyphenSearch)
                hyphenIndex = dsValueStr.indexOf("-");
            //if specified range, i.e. "age: 1 y - 5 y"
            if (hyphenIndex != -1) {
                int colonIndex = dsValueStr.indexOf(":");
                String type = "";
                if (colonIndex != -1)
                    type = dsValueStr.substring(0, colonIndex+1);
                String value1 = dsValueStr.substring(colonIndex+1, hyphenIndex);
                String value2 = dsValueStr.substring(hyphenIndex+1);
                String dsValueStr1 = type + ">=" + value1;
                String dsValueStr2 = type + "<=" + value2;
                dsValues.add(DSValue.createDSValue(dsValueStr1));
                dsValues.add(DSValue.createDSValue(dsValueStr2));
            } else {
                dsValues.add(DSValue.createDSValue(dsValueStr));
            }
        }
        return dsValues;
    }

    // i.e. "age: <23m"
    public static DSValue createDSValue(String typeOperatorValueUnit) {
        boolean processStatement = true;
        Pattern stringQuotePattern = Pattern.compile("'.+?'");
        if (stringQuotePattern.matcher(typeOperatorValueUnit).find()) {
            typeOperatorValueUnit = typeOperatorValueUnit.replaceAll("'", "");
            processStatement = false;
        }
        DSValue returnDsValue;
        int typeSeparatorIndex = indexOfNotQuoted(typeOperatorValueUnit, ":");
        String valueStr;
        String typeStr = null;
        String operator = null;
        String unit = null;


        //take out the type i.e. 'atc:'
        if (typeSeparatorIndex == -1) {
            valueStr = typeOperatorValueUnit.trim();
        } else {
            typeStr = typeOperatorValueUnit.substring(0, typeSeparatorIndex).trim();
            valueStr = typeOperatorValueUnit.substring(typeSeparatorIndex+1).trim();
        }

        Matcher operatorMatcher = Pattern.compile("[<>=-]+").matcher(valueStr);

        //find operator
        if (processStatement && operatorMatcher.find()) {
            operator = operatorMatcher.group().trim();
            valueStr = operatorMatcher.replaceFirst("").trim();

            Matcher unitMatcher = Pattern.compile("([^\\s]+$)").matcher(valueStr);
            //must be trimmed
            if (valueStr.indexOf(" ") != -1) {
                unitMatcher.find();
                unit = unitMatcher.group().trim();
                valueStr = unitMatcher.replaceFirst("").trim();
            }

            DSValueStatement dsValue = new DSValueStatement();
            dsValue.setValueType(typeStr);
            dsValue.setValue(valueStr);
            dsValue.setOperator(operator);
            dsValue.setValueUnit(unit);
            returnDsValue = dsValue;
        } else {
            DSValueString dsValue = new DSValueString();
            dsValue.setValueType(typeStr);
            dsValue.setValue(valueStr);
            returnDsValue = dsValue;
        }
        _log.debug("DSValue type: "+returnDsValue.getValueType()+ " operator: " + returnDsValue.getValue()+" unit: " + returnDsValue.getValueUnit()+" object type: " + returnDsValue.getClass().getName());
        return returnDsValue;
    }


    //WARNING: CANNOT SEARCH FOR SINGLE, DOUBLE, TRIPPLE, ETC.. QUOTES
    //i.e. cannot search:  '  ''  ''' '''' etc   (don't know why you'd want to anyways)
    private static int indexOfNotQuoted(String str, String query) {
        if (str.contains("'")) {
            Pattern stringQuotePattern = Pattern.compile("'.+?'");
            Pattern allCharacters = Pattern.compile(".");
            String[] quotedStrings = stringQuotePattern.split(str);
            for (String quotedString: quotedStrings) {
                String blankedString = allCharacters.matcher(quotedString).replaceAll("'");
                str = str.replace(quotedString, blankedString);
            }
        }
        return str.indexOf(query);
    }

    public abstract boolean testValue(String value) throws DecisionSupportException;

    public String getValueUnit() {
        return valueUnit;
    }

    public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
    }

}
