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
package org.oscarehr.decisionSupport.prevention;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarEncounter.oscarMeasurements.util.DSCondition;
import oscar.oscarEncounter.oscarMeasurements.util.RuleBaseCreator;

public class DSPreventionDrools {
	private static final Logger logger=MiscUtils.getLogger();

    public static final Namespace namespace = Namespace.getNamespace("http://drools.org/rules");
    public static final Namespace javaNamespace = Namespace.getNamespace("java", "http://drools.org/semantics/java");
    public static final String preventionObjectClassPath =  "oscar.oscarPrevention.Prevention";

	public static RuleBase createRuleBase(byte[] ruleSet) throws Exception{
	    logger.debug(preventionObjectClassPath);
	    RuleBaseCreator rbc = new RuleBaseCreator();
	    ResourceBundle oscarResource = ResourceBundle.getBundle("oscarResources"); 

	    SAXBuilder parser = new SAXBuilder();
        Document doc = parser.build(new ByteArrayInputStream(ruleSet));        
        Element root = doc.getRootElement();
        int count = 0;
        List<Element> elementList = new ArrayList<Element>();
        
        List<Element> recommendations = root.getChildren("recommendations");
        logger.debug("recommendations size :"+recommendations.size());
        
        for(Element recommendationsElements: recommendations){
        	String preventionType = recommendationsElements.getAttributeValue("for");
        	List<Element> recommendation = recommendationsElements.getChildren("recommendation");
        	for(Element recommendationElement:recommendation){
        		String ruleNumber = preventionType+"-"+count++;
        		String message = recommendationElement.getAttributeValue("message");
        		String reminder = recommendationElement.getAttributeValue("reminder");
        		List<DSCondition> dsConditions = getConditions(recommendationElement,preventionType);
        		StringBuilder consequence = new StringBuilder();
        		if(OscarProperties.getInstance().getBooleanProperty("DEBUG.PREVENTION", "yes")){
        			consequence.append("m.log(\""+ruleNumber+"\"); ");
        		}
        		if(message != null){
        			message = oscarResource.getString(message);
        			message= replaceKeys(message,preventionType);
        			consequence.append("m.addWarning(\""+preventionType+"\",\""+message+"\"); ");
        		}
        		if(reminder != null){
        			reminder = oscarResource.getString(reminder);
        			reminder= replaceKeys(reminder,preventionType);
        			consequence.append("m.addReminder(\""+reminder+"\"); ");
        		}
        		elementList.add(rbc.getRule(ruleNumber, DSPreventionDrools.preventionObjectClassPath, dsConditions, consequence.toString()));
        	}
        }
        return rbc.getRuleBase("preventions", elementList);
	    
    }
	
	private static String replaceKeys(String txt,String preventionType){
		String NUMMONTHS = "\"+m.getHowManyMonthsSinceLast(\""+preventionType+"\")+\"";
        txt = txt.replaceAll("\\$NUMMONTHS", NUMMONTHS);
        txt = txt.replaceAll("\\$PREVENTION_TYPE", preventionType);
        return txt;
	}
	
	
	private static List<DSCondition> getConditions(Element recommendationElement,String preventionType){
		List<DSCondition> dsConditions = new ArrayList<DSCondition>();
    	
		List<Element> conditions = recommendationElement.getChildren("condition");
    		
		for(Element condition:conditions){
			String type = condition.getAttributeValue("type");	
			if("age".equals(type)) { //<condition type="age" value="2m-72m" />  //int getAgeInMonths() //int getAgeInYears()
				processAgeElement(dsConditions,condition);
			}else if("numberOfPreventions".equals(type)){ //<condition type="numberOfPreventions" param="DTaP-IPV" value="0" />
				processGenericNumberValues("getNumberOfPreventionType",dsConditions, condition,preventionType);
			}else if("numberOfMonthsSinceLast".equals(type)){
				processGenericNumberValues("getHowManyMonthsSinceLast",dsConditions, condition,preventionType);
			}else if("numberOfDaysSinceLast".equals(type)){ 
				processGenericNumberValues("getHowManyDaysSinceLast",dsConditions, condition,preventionType);
			}else if("numberOfAgeInMonthsSinceLastPreventionTypeGiven".equals(type)){
				processGenericNumberValues("getAgeInMonthsLastPreventionTypeGiven",dsConditions, condition,preventionType);
			}else if("isNextDateSet".equals(type)){
				processSimpleBooleanValue("isNextDateSet",dsConditions, condition,preventionType);
			}else if("!isNextDateSet".equals(type)){
				processFalseBooleanValue("isNextDateSet",dsConditions, condition,preventionType);
			}else if("isPassedNextDate".equals(type)){
				processSimpleBooleanValue("isPassedNextDate",dsConditions, condition,preventionType);
			}else if("!isPassedNextDate".equals(type)){
				processFalseBooleanValue("isPassedNextDate",dsConditions, condition,preventionType);
			}else if("isPreventionNever".equals(type)){
				processSimpleBooleanValue("isPreventionNever",dsConditions, condition,preventionType);
			}else if("!isPreventionNever".equals(type)){
				processFalseBooleanValue("isPreventionNever",dsConditions, condition,preventionType);
			}else if("isInelligible".equals(type)){
				processSimpleBooleanValue("isInelligible",dsConditions, condition,preventionType);
			}else if("!isInelligible".equals(type)){
				processFalseBooleanValue("isInelligible",dsConditions, condition,preventionType);
			}else if("isMale".equals(type)){
				processSimpleBooleanValue("isMale",dsConditions, condition,null);
			}else if("isFemale".equals(type)){
				processSimpleBooleanValue("isFemale",dsConditions, condition,null);
			}else if("todayIsInDateRange".equals(type)){  //boolean isTodayinDateRange(String startDate,String endDate)
				processTodayIsInDateRange(dsConditions, condition);
			}else if("!todayIsInDateRange".equals(type)){
				processTodayIsNotInDateRange(dsConditions, condition);
			}else if("lastPreventionIsWithinRange".equals(type)){ //boolean isLastPreventionWithinRange(String preventionType, String startDate, String endDate)
				processLastPreventionIsWithinRange(dsConditions, condition);
			}else if("!lastPreventionIsWithinRange".equals(type)){
				processLastPreventionIsNotWithinRange(dsConditions, condition);
			}
			else{
				logger.error("Not processing type "+type);
	             //throw new IllegalArgumentException("Invalid Type: " + type);
			}
		}
		return dsConditions;
	}
	
	private static void processLastPreventionIsWithinRange(List<DSCondition> dsConditions, Element condition) {
		String param = condition.getAttributeValue("param");
		String value = condition.getAttributeValue("value");
		
		String[] values = value.split(",");
		param = param+"\",\""+values[0]+"\",\""+values[1];
		param.replaceAll(";", "");
		dsConditions.add(new DSCondition("isLastPreventionWithinRange", param, "", ""));
	    
    }
	
	private static void processLastPreventionIsNotWithinRange(List<DSCondition> dsConditions, Element condition) {
		String param = condition.getAttributeValue("param");
		String value = condition.getAttributeValue("value");
		
		String[] values = value.split(",");
		param = param+"\",\""+values[0]+"\",\""+values[1];
		param.replaceAll(";", "");
		dsConditions.add(new DSCondition("isLastPreventionWithinRange", param, "==", "false"));
    }
	
	private static void processTodayIsInDateRange(List<DSCondition> dsConditions, Element condition) {
		String param = condition.getAttributeValue("value");
		String[] params = param.split(",");
		param = params[0]+"\",\""+params[1];
		param.replaceAll(";", "");
		dsConditions.add(new DSCondition("isTodayinDateRange", param, "", ""));
    }
	
	private static void processTodayIsNotInDateRange(List<DSCondition> dsConditions, Element condition) {
		String param = condition.getAttributeValue("value");
		String[] params = param.split("-");
		param = "\""+params[0]+"\",\""+params[1]+"\"";
		param.replaceAll(";", "");
		dsConditions.add(new DSCondition("isTodayinDateRange", param, "==", "false"));
    }
	
	
	private static void processSimpleBooleanValue(String method, List<DSCondition> dsConditions, Element condition,String paramDefaultIfNull) {
		String param = condition.getAttributeValue("param");
		if(param == null && paramDefaultIfNull != null){
			param = paramDefaultIfNull;
		}
		dsConditions.add(new DSCondition(method, param, "", ""));
	}
	
	private static void processFalseBooleanValue(String method, List<DSCondition> dsConditions, Element condition,String paramDefaultIfNull) {
		String param = condition.getAttributeValue("param");
		if(param == null && paramDefaultIfNull != null){
			param = paramDefaultIfNull;
		}
		dsConditions.add(new DSCondition(method, param, "==", "false"));
	}
	
	private static void processGenericNumberValues(String method, List<DSCondition> dsConditions, Element condition,String paramDefaultIfNull) {
		String param = condition.getAttributeValue("param");
		if(param == null && paramDefaultIfNull != null){
			param = paramDefaultIfNull;
		}
		String toParse = condition.getAttributeValue("value");
		if (toParse.indexOf("-") != -1 && toParse.indexOf("-") != 0 ){ //between style
            String[] betweenVals = toParse.split("-");
            if (betweenVals.length == 2 ){
            	dsConditions.add(new DSCondition(method, param, ">=", betweenVals[0]));
            	dsConditions.add(new DSCondition(method, param, "<=", betweenVals[1]));
            }

        }else if (toParse.indexOf("&gt;") != -1 ||  toParse.indexOf(">") != -1 ){ // greater than style
            toParse = toParse.replaceFirst("&gt;","");
            toParse = toParse.replaceFirst(">","");
            int gt = Integer.parseInt(toParse);
            dsConditions.add(new DSCondition(method, param, ">=", ""+gt));

        }else if (toParse.indexOf("&lt;") != -1  ||  toParse.indexOf("<") != -1 ){ // less than style
            toParse = toParse.replaceFirst("&lt;","");
            toParse = toParse.replaceFirst("<","");

            int lt = Integer.parseInt(toParse);
            dsConditions.add(new DSCondition(method, param, "<=", ""+lt));
        }else if (toParse.indexOf("!=") != -1 ){ // less than style
            toParse = toParse.replaceFirst("!=","");
            int eq = Integer.parseInt(toParse);
            dsConditions.add(new DSCondition(method, param, "!=", ""+eq));
        }else if (!toParse.equals("")){ // less than style
            int eq = Integer.parseInt(toParse);
            dsConditions.add(new DSCondition(method, param, "==", ""+eq));
        }
		
	}

	/**
	 * condition tag with type age 
	 * value can be 2m-72m,2m-72,2-72, &gt;4, &gt;4m, &gt;=4m
	 * 
	 * @param condition Xml element to parse format: <condition type="age" value="2m-72m" />
	 */
	private static void processAgeElement(List<DSCondition> dsConditions, Element condition) {
		String valueToParse = condition.getAttributeValue("value");
		logger.debug("process Age Element :"+valueToParse);
		if (valueToParse.indexOf("-") != -1 && valueToParse.indexOf("-") != 0 ){ //between style
            String[] betweenVals = valueToParse.split("-");
            if (betweenVals.length == 2 ){
            	if(betweenVals[0].indexOf("m") != -1){
            		dsConditions.add(new DSCondition("getAgeInMonths","",">=",betweenVals[0].replaceAll("m", "")));
            	}else{ // assume years 
            		dsConditions.add(new DSCondition("getAgeInYears","",">=",betweenVals[0].replaceAll("y", "")));
            	}
            	if(betweenVals[1].indexOf("m") != -1){
            		dsConditions.add(new DSCondition("getAgeInMonths","","<=",betweenVals[1].replaceAll("m", "")));
            	}else{ // assume years 
            		dsConditions.add(new DSCondition("getAgeInYears","","<=",betweenVals[1].replaceAll("y", "")));
            	}
            }
		}else if (valueToParse.indexOf("&gt;") != -1 ||  valueToParse.indexOf(">") != -1 ){ // greater than style
			valueToParse = valueToParse.replaceFirst("&gt;","");
			valueToParse = valueToParse.replaceFirst(">","");
			
			if(valueToParse.indexOf("m") != -1){
        		dsConditions.add(new DSCondition("getAgeInMonths","",">=",valueToParse.replaceAll("m", "")));
        	}else{ // assume years 
        		dsConditions.add(new DSCondition("getAgeInYears","",">=",valueToParse.replaceAll("y", "")));
        	}
        }else if (valueToParse.indexOf("&lt;") != -1  ||  valueToParse.indexOf("<") != -1 ){ // less than style
        	valueToParse = valueToParse.replaceFirst("&lt;","");
        	valueToParse = valueToParse.replaceFirst("<","");

        	if(valueToParse.indexOf("m") != -1){
        		dsConditions.add(new DSCondition("getAgeInMonths","","<=",valueToParse.replaceAll("m", "")));
        	}else{ // assume years 
        		dsConditions.add(new DSCondition("getAgeInYears","","<=",valueToParse.replaceAll("y", "")));
        	}
        	
        }else if (valueToParse.indexOf("!=") != -1 ){ // less than style
        	valueToParse = valueToParse.replaceFirst("!=","");
            if(valueToParse.indexOf("m") != -1){
        		dsConditions.add(new DSCondition("getAgeInMonths","","!=",valueToParse.replaceAll("m", "")));
        	}else{ // assume years 
        		dsConditions.add(new DSCondition("getAgeInYears","","!=",valueToParse.replaceAll("y", "")));
        	}
            
        }else if (!valueToParse.equals("")){ // less than style
            int eq = Integer.parseInt(valueToParse);
            if(valueToParse.indexOf("m") != -1){
        		dsConditions.add(new DSCondition("getAgeInMonths","","==",valueToParse.replaceAll("m", "")));
        	}else{ // assume years 
        		dsConditions.add(new DSCondition("getAgeInYears","","==",valueToParse.replaceAll("y", "")));
        	}
        }
		

		/*
		 * String toParse = value;
            if (toParse.indexOf("-") != -1 && toParse.indexOf("-") != 0 ){ //between style
                String[] betweenVals = toParse.split("-");
                if (betweenVals.length == 2 ){
                    list.add(new DSCondition("getAge", "", ">=", betweenVals[0]));
                    list.add(new DSCondition("getAge", "", "<=", betweenVals[1]));
                }
            }else if (toParse.indexOf("&gt;") != -1 ||  toParse.indexOf(">") != -1 ){ // greater than style
                toParse = toParse.replaceFirst("&gt;","");
                toParse = toParse.replaceFirst(">","");
                int gt = Integer.parseInt(toParse);
                list.add(new DSCondition("getAge", "", ">", ""+gt));

            }else if (toParse.indexOf("&lt;") != -1  ||  toParse.indexOf("<") != -1 ){ // less than style
                toParse = toParse.replaceFirst("&lt;","");
                toParse = toParse.replaceFirst("<","");

                int lt = Integer.parseInt(toParse);
                list.add(new DSCondition("getAge", "", "<=", ""+lt));
                
            }else if (toParse.indexOf("!=") != -1 ){ // less than style
                toParse = toParse.replaceFirst("!=","");
                int eq = Integer.parseInt(toParse);
                list.add(new DSCondition("getAge", "", "!=", ""+eq));
                
            }else if (!toParse.equals("")){ // less than style
                int eq = Integer.parseInt(toParse);
                list.add(new DSCondition("getAge", "", "==", ""+eq));
            }
	    */
    }
}
