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


package oscar.oscarEncounter.oscarMeasurements.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.oscarehr.util.MiscUtils;


/**
 * Flowsheet recommendation class
 * @author jaygallagher
 */
public class Recommendation {
    private static final Logger log=MiscUtils.getLogger();

    private String strength = null;
    private String text = null;
    private List<RecommendationCondition> recommendationCondition = new ArrayList<RecommendationCondition>();

    private String ruleName = null;
    private String measurement =  null;


    public String toString(){
         return " strength "+strength+" text "+text+" ruleName "+ruleName+" measurement "+measurement;
    }

    public Recommendation(){

    }

    public Recommendation(Element recowarn){
        //monthrange = recowarn.getAttributeValue("monthrange");
        strength = recowarn.getAttributeValue("strength") ;
        text = recowarn.getAttributeValue("message");
        List<Element> cond = recowarn.getChildren("condition");
        for(Element ele:cond){
            recommendationCondition.add(new RecommendationCondition(ele));
        }

    }

    public Recommendation(String measurement,String monthrange, String strength,String text){
        this.measurement = measurement;
        RecommendationCondition rec = new RecommendationCondition();
        rec.setType("monthrange");
        rec.setValue(monthrange);
        recommendationCondition.add(rec);
        this.strength = strength;
        this.text = text;
    }

    public Recommendation(Element recowarn,String ruleName, String measurement){
        //monthrange = recowarn.getAttributeValue("monthrange");
        strength = recowarn.getAttributeValue("strength") ;
        text = recowarn.getAttributeValue("message");

        this.ruleName = ruleName;
        this.measurement = measurement;

        List<Element> cond = recowarn.getChildren("condition");
        for(Element ele:cond){
            recommendationCondition.add(new RecommendationCondition(ele));
        }

    }

    public Element getRuleBaseElement(){
        return getRuleBaseElement(ruleName,measurement);
    }

    public Element getRuleBaseElement(String ruleName,String measurement){
    	
        log.debug("LOADING RULES - getRuleBaseElement"+measurement);
        ArrayList<DSCondition> list = new ArrayList<DSCondition>();
        
        for(RecommendationCondition cond: getRecommendationCondition()){
            cond.getRuleBaseElement(list,measurement);
        }
        
        String consequence = "";
        if( strength != null){
        	if ("hidden".equals(strength)){
        		consequence = "m.addHidden(\""+measurement+"\",true);";
        	}else{
                String consequenceType = "Recommendation";
	            if ("warning".equals(strength)){
	                consequenceType = "Warning";
	            }
	            if (text == null || text.trim().equals("")){
	                consequence ="m.add"+consequenceType+"(\""+measurement+"\",\""+measurement+"  \"+m.getLastDateRecordedInMonthsMsg(\""+measurement+"\")+\" \");";
	            }else if( text != null){
	               String txt = text;
	               String NUMMONTHS = "\"+m.getLastDateRecordedInMonths(\""+measurement+"\")+\"";
	               log.debug("TRY TO REPLACE $NUMMONTHS:"+txt.indexOf("$NUMMONTHS")+" WITH "+NUMMONTHS+  " "+txt);

	               txt = txt.replaceAll("\\$NUMMONTHS", NUMMONTHS);
	               log.debug("TEXT "+txt);
	               consequence ="m.add"+consequenceType+"(\""+measurement+"\",\""+txt+"\");";
	           }
        	}
        }

        RuleBaseCreator rcb = new RuleBaseCreator();
        Element ruleElement = rcb.getRule(ruleName, "oscar.oscarEncounter.oscarMeasurements.MeasurementInfo", list,  consequence) ;


        return ruleElement;
    }

//    public String getMonthrange() {
//        return monthrange;
//    }
//
//    public void setMonthrange(String monthrange) {
//        this.monthrange = monthrange;
//    }

	public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Element getFlowsheetXML(){
        Element e = new Element("recommendation");
        //    e.setAttribute("monthrange",monthrange);
         if (strength != null){
            e.setAttribute("strength",strength) ;
         }
         if (text != null){
             e.setAttribute("message",text) ;
         }

         log.debug("Number of Conditions "+getRecommendationCondition().size());
         for(RecommendationCondition cond : getRecommendationCondition() ){
             e.addContent(cond.getFlowsheetXML());//a cond.getFlowsheetXML();
         }

         return e;
    }

    public List<RecommendationCondition> getRecommendationCondition() {
        return recommendationCondition;
    }

    public void setRecommendationCondition(List<RecommendationCondition> recommendationCondition) {
        this.recommendationCondition = recommendationCondition;
    }

}