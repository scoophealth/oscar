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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.oscarehr.decisionSupport.model.conditionValue.DSValue;
import org.oscarehr.decisionSupport.model.impl.drools.DSGuidelineDrools;
import org.oscarehr.util.MiscUtils;
/**
 *
 * @author apavel
 */
public class DSGuidelineFactory {
    private static Logger _log = MiscUtils.getLogger();
    public DSGuideline createGuidelineFromXml(String xml) throws DecisionSupportParseException {
       if (xml == null || xml.equals("")) throw new DecisionSupportParseException("Xml not set");
        SAXBuilder parser = new SAXBuilder();
        Document doc;
        try {
            doc = parser.build(new StringReader(xml));
        } catch (JDOMException jdome) {
                   throw new DecisionSupportParseException("Failed to read the xml string for parsing",jdome);
        } catch (IOException ioe) {
            throw new DecisionSupportParseException("Failed to read the xml string for parsing",ioe);
        }
        //<guideline evidence="" significance="" title="Plavix Drug DS">
        Element guidelineRoot = doc.getRootElement();

        DSGuideline dsGuideline = this.createBlankGuideline();

        String guidelineTitle = guidelineRoot.getAttributeValue("title");
        dsGuideline.setTitle(guidelineTitle);

        //Load parameters such as classes
        //<parameter identifier="a">
        //  <class>java.util.ArrayList</class>
        //</parameter>
        ArrayList<DSParameter> parameters = new ArrayList<DSParameter>();
        @SuppressWarnings("unchecked")
        List<Element> parameterTags = guidelineRoot.getChildren("parameter");
        for( Element parameterTag : parameterTags ) {
            String alias = parameterTag.getAttributeValue("identifier");
            if( alias == null ) {
                throw new DecisionSupportParseException(guidelineTitle, "Parameter identifier attribute is mandatory");
            }

            Element Eclass = parameterTag.getChild("class");
            String strClass = Eclass.getText();
            DSParameter dsParameter = new DSParameter();
            dsParameter.setStrAlias(alias);
            dsParameter.setStrClass(strClass);
            parameters.add(dsParameter);
        }

        dsGuideline.setParameters(parameters);

        //Load Conditions
        //<conditions>
        //  <condition type="dxcodes" any="icd9:4439,icd9:4438,icd10:E11,icd10:E12"/>
        //  <condition type="drug" not="atc:34234"/>
        ArrayList<DSCondition> conditions = new ArrayList<DSCondition>();
        @SuppressWarnings("unchecked")
        List<Element> conditionTags = guidelineRoot.getChild("conditions").getChildren("condition");
        for (Element conditionTag: conditionTags) {

            String conditionTypeStr = conditionTag.getAttributeValue("type");
            if (conditionTypeStr == null)
                throw new DecisionSupportParseException(guidelineTitle, "Condition 'type' attribute is mandatory");
            //try to resolve type
            DSDemographicAccess.Module conditionType;
            try {
                conditionType = DSDemographicAccess.Module.valueOf(conditionTypeStr);
            } catch (IllegalArgumentException iae) {
                String knownTypes = StringUtils.join(DSDemographicAccess.Module.values(), ",");
                throw new DecisionSupportParseException(guidelineTitle, "Cannot recognize condition type: '" + conditionTypeStr + "'.  Known types: " + knownTypes, iae);
            }

            String conditionDescStr = conditionTag.getAttributeValue("desc");


            Hashtable<String,String> paramHashtable = new Hashtable<String,String>();
            @SuppressWarnings("unchecked")
            List<Element> paramList = conditionTag.getChildren("param");
            if (paramList != null){
                for (Element param :paramList){
                    String key   = param.getAttributeValue("key");
                    String value = param.getAttributeValue("value");
                    paramHashtable.put(key, value);
                }
            }

            @SuppressWarnings("unchecked")
            List<Attribute> attributes = conditionTag.getAttributes();
            for (Attribute attribute: attributes) {
                if (attribute.getName().equalsIgnoreCase("type")) continue;
                if (attribute.getName().equalsIgnoreCase("desc")) continue;
                DSCondition.ListOperator operator;
                try {
                    operator = DSCondition.ListOperator.valueOf(attribute.getName().toLowerCase());
                } catch (IllegalArgumentException iae) {
                    throw new DecisionSupportParseException(guidelineTitle, "Unknown condition attribute'" + attribute.getName() + "'", iae);
                }
                DSCondition dsCondition = new DSCondition();
                dsCondition.setConditionType(conditionType);
                dsCondition.setDesc(conditionDescStr);
                dsCondition.setListOperator(operator); //i.e. any, all, not
                if (paramHashtable != null && !paramHashtable.isEmpty()){
                    _log.debug("THIS IS THE HASH STRING "+paramHashtable.toString());
                    dsCondition.setParam(paramHashtable);
                }

                dsCondition.setValues(DSValue.createDSValues(attribute.getValue())); //i.e. icd9:3020,icd9:3021,icd10:5022
                conditions.add(dsCondition);
            }
        }
        dsGuideline.setConditions(conditions);

        //CONSEQUENCES
        ArrayList<DSConsequence> dsConsequences = new ArrayList<DSConsequence>();
        @SuppressWarnings("unchecked")
        List<Element> consequenceElements = guidelineRoot.getChild("consequence").getChildren();
        for (Element consequenceElement: consequenceElements) {
            DSConsequence dsConsequence = new DSConsequence();

            String consequenceTypeStr = consequenceElement.getName();
            DSConsequence.ConsequenceType consequenceType = null;
            //try to resolve type
            try {
                consequenceType = DSConsequence.ConsequenceType.valueOf(consequenceTypeStr.toLowerCase());
            } catch (IllegalArgumentException iae) {
                String knownTypes = StringUtils.join(DSConsequence.ConsequenceType.values(), ",");
                throw new DecisionSupportParseException(guidelineTitle, "Unknown consequence: " + consequenceTypeStr + ". Allowed: " + knownTypes, iae);
            }
            dsConsequence.setConsequenceType(consequenceType);

            if (consequenceType == DSConsequence.ConsequenceType.warning) {
                String strengthStr = consequenceElement.getAttributeValue("strength");
                if( strengthStr == null ) {
                    strengthStr = "warning";
                }
                DSConsequence.ConsequenceStrength strength = null;
                //try to resolve strength type
                try {
                    strength = DSConsequence.ConsequenceStrength.valueOf(strengthStr.toLowerCase());
                    dsConsequence.setConsequenceStrength(strength);
                } catch (IllegalArgumentException iae) {
                    String knownStrengths = StringUtils.join(DSConsequence.ConsequenceStrength.values(), ",");
                    throw new DecisionSupportParseException(guidelineTitle, "Unknown strength: " + strengthStr + ". Allowed: " + knownStrengths, iae);
                }
            }
            dsConsequence.setText(consequenceElement.getText());
            dsConsequences.add(dsConsequence);
        }
        dsGuideline.setConsequences(dsConsequences);
        dsGuideline.setXml(xml);
        dsGuideline.setParsed(true);
        //populate consequence here
        return dsGuideline;
    }

    public DSGuideline createBlankGuideline() {
        DSGuidelineDrools newGuideline = new DSGuidelineDrools();
        //newGuideline.setEngine("drools");
        return newGuideline;
    }

    //i.e. valueString = icd9:4439,icd9:4438,icd10:E11,icd10:E12
}
