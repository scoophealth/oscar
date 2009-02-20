/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.oscarEncounter.oscarMeasurements.util;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;


/**
 * Flowsheet recommendation class
 * @author jaygallagher
 */
public class Recommendation {
    private static final Log log = LogFactory.getLog(Recommendation.class);
    
    private String monthrange = null;
    private String strength = null;
    private String text = null;
    
    private String ruleName = null;
    private String measurement =  null;
    
    
    public String toString(){
         return "monthrange "+monthrange+" strength "+strength+" text "+text+" ruleName "+ruleName+" measurement "+measurement;
    }
    
    public Recommendation(){
        
    }
    
    public Recommendation(Element recowarn){
        monthrange = recowarn.getAttributeValue("monthrange");
        strength = recowarn.getAttributeValue("strength") ;
        text = recowarn.getText();
        
    }
    
    public Recommendation(String measurement,String monthrange, String strength,String text){
        this.measurement = measurement;
        this.monthrange = monthrange;
        this.strength = strength;
        this.text = text;
    }
    
    public Recommendation(Element recowarn,String ruleName, String measurement){
        monthrange = recowarn.getAttributeValue("monthrange");
        strength = recowarn.getAttributeValue("strength") ;
        text = recowarn.getText();
        
        this.ruleName = ruleName;
        this.measurement = measurement;
        
    }
    
    public Element getRuleBaseElement(){
        return getRuleBaseElement(ruleName,measurement);
    }
    
    public Element getRuleBaseElement(String ruleName,String measurement){
        
        log.debug("LOADING RULES - getRuleBaseElement"+measurement);
        ArrayList list = new ArrayList();
        String toParse = monthrange;
        String consequenceType = "Recommendation";
        if( strength != null){
            if ("warning".equals(strength)){
                consequenceType = "Warning";
            }
        }
        String consequence = "";
        
        String NUMMONTHS = "\"+m.getLastDateRecordedInMonths(\""+measurement+"\")+\"";
        
                
            if (toParse.indexOf("-") != -1 && toParse.indexOf("-") != 0 ){ //between style
                String[] betweenVals = toParse.split("-");
                if (betweenVals.length == 2 ){
                    //int lower = Integer.parseInt(betweenVals[0]);
                    //int upper = Integer.parseInt(betweenVals[1]);
                    list.add(new DSCondition("getLastDateRecordedInMonths", measurement, ">=", betweenVals[0]));
                    list.add(new DSCondition("getLastDateRecordedInMonths", measurement, "<=", betweenVals[1]));
                    if (text == null){
                         consequence ="m.add"+consequenceType+"(\""+measurement+"\",\""+measurement+"\" hasn't been reviewed in \"+m.getLastDateRecordedInMonths(\""+measurement+"\")+\" months\";";
                    }
                } 

            }else if (toParse.indexOf("&gt;") != -1 ||  toParse.indexOf(">") != -1 ){ // greater than style
                toParse = toParse.replaceFirst("&gt;","");
                toParse = toParse.replaceFirst(">","");
                
                int gt = Integer.parseInt(toParse);
                
                list.add(new DSCondition("getLastDateRecordedInMonths", measurement, ">", ""+gt));  
                if ( text == null){
                         consequence ="m.add"+consequenceType+"(\""+measurement+"\",\""+measurement+"\" hasn't been reviewed in \"+m.getLastDateRecordedInMonths(\""+measurement+"\")+\" months\";";
                }
            }else if (toParse.indexOf("&lt;") != -1  ||  toParse.indexOf("<") != -1 ){ // less than style
                toParse = toParse.replaceFirst("&lt;","");
                toParse = toParse.replaceFirst("<","");
                
                int lt = Integer.parseInt(toParse);           
                list.add(new DSCondition("getLastDateRecordedInMonths", measurement, "<=", ""+lt));
                if ( text == null){
                         consequence ="m.add"+consequenceType+"(\""+measurement+"\",\""+measurement+"\" hasn't been reviewed in \"+m.getLastDateRecordedInMonths(\""+measurement+"\")+\" months\";";
                    }
                
            }else if (!toParse.equals("")){ // less than style
                int eq = Integer.parseInt(toParse); 
                list.add(new DSCondition("getLastDateRecordedInMonths", measurement, "==", ""+eq));
                if ( text == null){
                         consequence ="m.add"+consequenceType+"(\""+measurement+"\",\""+measurement+"\" hasn'taaaaa been reviewed in \"+m.getLastDateRecordedInMonths(\""+measurement+"\")+\" months\";";
                    }
            }
        
        if ( text != null){
            String txt = text;
            log.debug("TRY TO REPLACE $NUMMONTHS:"+txt.indexOf("$NUMMONTHS")+" WITH "+NUMMONTHS+  " "+txt);
            
            txt = txt.replaceAll("\\$NUMMONTHS", NUMMONTHS);
            log.debug("TEXT "+txt);
            consequence ="m.add"+consequenceType+"(\""+measurement+"\",\""+txt+"\");";
        }
        
        RuleBaseCreator rcb = new RuleBaseCreator();
        Element ruleElement = rcb.getRule(ruleName, "oscar.oscarEncounter.oscarMeasurements.MeasurementInfo", list,  consequence) ;
    
        
        return ruleElement;
    }

    public String getMonthrange() {
        return monthrange;
    }

    public void setMonthrange(String monthrange) {
        this.monthrange = monthrange;
    }

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
            e.setAttribute("monthrange",monthrange);
         if (strength != null){
            e.setAttribute("strength",strength) ;
         }
         if (text != null){
            e.setText(text);
         }
       
         return e;
    }

}
