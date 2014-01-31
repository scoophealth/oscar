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

import org.jdom.Element;

/**
 * Stores Conditions for target Colours
 *
 <ruleset>
  <rule indicationColor="HIGH">
     <condition type="doubleValue"  value="&gt;=2.0"/>
     <condition type="isfemale"/>
  </rule>
  <rule consequence="m.setIndicationColor(\"HIGH\");">
  	 <condition type="doubleValue"  value="&gt;=2.0"/>
     <condition type="isMale"/>
  </rule>
</ruleset>

 * @author jaygallagher
 */
public class RecommendationCondition {

    private String type = null;
    private String value = null;
    private String param = null;




//    public String toString(){
//         return "monthrange "+monthrange+" strength "+strength+" text "+text+" ruleName "+ruleName+" measurement "+measurement;
//    }

    public RecommendationCondition(){

    }

    public RecommendationCondition(Element recowarn){
        type  = recowarn.getAttributeValue("type");
        value = recowarn.getAttributeValue("value") ;
        param = recowarn.getAttributeValue("param");

    }

    public void getRuleBaseElement(ArrayList<DSCondition> list,String measurement){

        //m.getLastDateRecordedInMonths("NOSK") &gt;= 3</java:condition>
        //<java:condition>m.getLastDateRecordedInMonths("NOSK") &lt; 6</java:condition>
        //<java:condition>m.getLastValueAsInt("NO
        if (param == null || param.trim().equals("")){
            param = measurement;
        }
        if ("monthrange".equals(type)){
            String toParse = value;
            if (toParse.indexOf("-") != -1 && toParse.indexOf("-") != 0 ){ //between style
                String[] betweenVals = toParse.split("-");
                if (betweenVals.length == 2 ){
                    list.add(new DSCondition("getLastDateRecordedInMonths", param, ">=", betweenVals[0]));
                    list.add(new DSCondition("getLastDateRecordedInMonths", param, "<=", betweenVals[1]));
                }

            }else if (toParse.indexOf("&gt;") != -1 ||  toParse.indexOf(">") != -1 ){ // greater than style
                toParse = toParse.replaceFirst("&gt;","");
                toParse = toParse.replaceFirst(">","");
                int gt = Integer.parseInt(toParse.trim());
                list.add(new DSCondition("getLastDateRecordedInMonths", param, ">", ""+gt));

            }else if (toParse.indexOf("&lt;") != -1  ||  toParse.indexOf("<") != -1 ){ // less than style
                toParse = toParse.replaceFirst("&lt;","");
                toParse = toParse.replaceFirst("<","");

                int lt = Integer.parseInt(toParse);
                list.add(new DSCondition("getLastDateRecordedInMonths", param, "<=", ""+lt));
            }else if (!toParse.equals("")){ // less than style
                int eq = Integer.parseInt(toParse.trim());
                list.add(new DSCondition("getLastDateRecordedInMonths", param, "==", ""+eq));
            }
        }else if ("lastValueAsInt".equals(type)){
            String toParse = value;
            if (toParse.indexOf("-") != -1 && toParse.indexOf("-") != 0 ){ //between style
                String[] betweenVals = toParse.split("-");
                if (betweenVals.length == 2 ){
                    list.add(new DSCondition("getLastValueAsInt", param, ">=", betweenVals[0]));
                    list.add(new DSCondition("getLastValueAsInt", param, "<=", betweenVals[1]));
                }

            }else if (toParse.indexOf("&gt;") != -1 ||  toParse.indexOf(">") != -1 ){ // greater than style
                toParse = toParse.replaceFirst("&gt;","");
                toParse = toParse.replaceFirst(">","");
                int gt = Integer.parseInt(toParse);
                list.add(new DSCondition("getLastValueAsInt", param, ">", ""+gt));

            }else if (toParse.indexOf("&lt;") != -1  ||  toParse.indexOf("<") != -1 ){ // less than style
                toParse = toParse.replaceFirst("&lt;","");
                toParse = toParse.replaceFirst("<","");

                int lt = Integer.parseInt(toParse);
                list.add(new DSCondition("getLastValueAsInt", param, "<=", ""+lt));
            }else if (toParse.indexOf("!=") != -1 ){ // less than style
                toParse = toParse.replaceFirst("!=","");
                int eq = Integer.parseInt(toParse);
                list.add(new DSCondition("getLastValueAsInt", param, "!=", ""+eq));
            }else if (!toParse.equals("")){ // less than style
                int eq = Integer.parseInt(toParse);
                list.add(new DSCondition("getLastValueAsInt", param, "==", ""+eq));
            }
        }else if ("patientAge".equals(type)){
            String toParse = value;
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
        }else if ("isMale".equals(type)){
        	list.add(new DSCondition("getGender", "M", "==", "true"));
        }else if ("isFemale".equals(type)){
        	list.add(new DSCondition("getGender", "F", "==", "true"));
	    }else if ("isDataEqualToYes".equals(type)){
	    	int v = 0;
	    	if(value.equals("Yes")){ v=1;}
	        list.add(new DSCondition("isDataEqualToYes",param,"==",""+v)); //change "Yes" to ""+value
	    }

        /*if ("getDataAsDouble".equals(type)){
            String toParse = value;
            ///---
            if (toParse.indexOf("-") != -1 && toParse.indexOf("-") != 0 ){ //between style
                String[] betweenVals = toParse.split("-");
                if (betweenVals.length == 2 ){
                    //int lower = Integer.parseInt(betweenVals[0]);
                    //int upper = Integer.parseInt(betweenVals[1]);
                    list.add(new DSCondition("getDataAsDouble", "", ">=", betweenVals[0]));
                    list.add(new DSCondition("getDataAsDouble", "", "<=", betweenVals[1]));
                }
//TODO: how to handle = sign in greater than or equal too.
            }else if (toParse.indexOf("&gt;") != -1 ||  toParse.indexOf(">") != -1 ){ // greater than style
                toParse = toParse.replaceFirst("&gt;","");
                toParse = toParse.replaceFirst(">","");
                double gt = Double.parseDouble(toParse.trim());
                list.add(new DSCondition("getDataAsDouble", "", ">", ""+gt));
            }else if (toParse.indexOf("&lt;") != -1  ||  toParse.indexOf("<") != -1 ){ // less than style
                toParse = toParse.replaceFirst("&lt;","");
                toParse = toParse.replaceFirst("<","");
                double lt = Double.parseDouble(toParse.trim());
                list.add(new DSCondition("getDataAsDouble", "", "<=", ""+lt));
            }else if (!toParse.equals("")){ // less than style
                double eq = Double.parseDouble(toParse.trim());
                list.add(new DSCondition("getDataAsDouble", "", "==", ""+eq));
            }

            ///---

        }else if ("isMale".equals(type)){
            if (value == null ){
                list.add(new DSCondition("isMale", "", "==", "true"));
            }
            //        list.add(new DSCondition("getLastDateRecordedInMonths", measurement, "<=", betweenVals[1]));

        }else if ("isFemale".equals(type)){
            if (value == null ){
                list.add(new DSCondition("isFemale", "", "==", "true"));
            }
        }else if ("getNumberFromSplit".equals(type)){

           // /  >130/
           //   >130/>80
           //  140-120/>80
          //   >130


            String toParse = value;
            param = null;

            String[] bps = toParse.split("/");

            log.debug("Len " + bps.length+" -- "+bps[0]);
            if (bps.length>1){
                log.debug("Len " + bps.length+" -- "+bps[1]);
            }

            if (bps.length <3){
                for(int i =0; i < bps.length; i++){
                    toParse = bps[i];
                    if (toParse.indexOf("-") != -1 && toParse.indexOf("-") != 0 ){ //between style
                        String[] betweenVals = toParse.split("-");
                        if (betweenVals.length == 2 ){
                            //int lower = Integer.parseInt(betweenVals[0]);
                            //int upper = Integer.parseInt(betweenVals[1]);
                            list.add(new DSCondition("getNumberFromSplit(\"/\","+i+")", param, ">=", betweenVals[0]));
                            list.add(new DSCondition("getNumberFromSplit(\"/\","+i+")", param, "<=", betweenVals[1]));
                        }

                    }else if (toParse.indexOf("&gt;") != -1 ||  toParse.indexOf(">") != -1 ){ // greater than style
                        toParse = toParse.replaceFirst("&gt;","");
                        toParse = toParse.replaceFirst(">","");
                        int gt = Integer.parseInt(toParse);
                        list.add(new DSCondition("getNumberFromSplit(\"/\","+i+")", param, ">", ""+gt));
                    }else if (toParse.indexOf("&lt;") != -1  ||  toParse.indexOf("<") != -1 ){ // less than style
                        toParse = toParse.replaceFirst("&lt;","");
                        toParse = toParse.replaceFirst("<","");
                        int lt = Integer.parseInt(toParse);
                        list.add(new DSCondition("getNumberFromSplit(\"/\","+i+")", param, "<=", ""+lt));
                    }else if (!toParse.equals("")){ // less than style
                        int eq = Integer.parseInt(toParse);
                        list.add(new DSCondition("getNumberFromSplit(\"/\","+i+")", param, "==", ""+eq));
                    }

                }
            }


        }else if ("isDataEqualTo".equals(type)){
            list.add(new DSCondition("isDataEqualTo",value,"",""));
        }
         */

    }

    public Element getFlowsheetXML(){
        Element e = new Element("condition");
            e.setAttribute("type",type);
         if (param != null){
            e.setAttribute("param",param) ;
         }
         if (value != null){
            e.setAttribute("value",value);
         }

         return e;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public static void main(String[] args) {
        //String filename = "/Users/jaygallagher/Desktop/PATIENT.D
        ArrayList<DSCondition> list = new ArrayList<DSCondition>();
        RecommendationCondition tc = new RecommendationCondition();
        tc.setType("monthrange");





        tc.setValue("4-6");
        tc.getRuleBaseElement(list,"test");


        tc.setValue(">6");
        tc.getRuleBaseElement(list,"test");

        tc.setType("lastValueAsInt");
        tc.setValue("3-4");
        tc.getRuleBaseElement(list,"test");


        tc.setValue("0");
        tc.getRuleBaseElement(list,"test");


    }

}
