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


package oscar.oscarEncounter.oscarMeasurements;

import java.util.List;
import java.util.Map;

import org.drools.RuleBase;

import oscar.oscarEncounter.oscarMeasurements.util.Recommendation;
import oscar.oscarEncounter.oscarMeasurements.util.TargetColour;

/**
 * Class for markers on a flowsheet
 * @author jaygallagher
 */
public class FlowSheetItem {

    Map<String,String> allFields = null;
    private String measurementType = null;
    private String preventionType = null;
    private String displayName = null;
    private String guideline = null;
    private boolean graphable = false;
    private String valueName = null;
    private String possibleAnswer = null;
    private List<Recommendation> recommendations = null;
    private List<TargetColour> targetColour = null;
    private RuleBase ruleBase = null;
    private String dsRulesFileName = null;
    private boolean hide = false;

    @Override
    public String toString(){
        return " MEASUREMENT TYPE :"+measurementType +" PREV TYPE :"+preventionType+" dsRulesFileName :"+dsRulesFileName+" ruleBASE :"+ruleBase;
    }

   /*
    <item
        measurement_type="EDGI"
        display_name="Autonomic Neuropathy"
        guideline="Erectile Dysfunction, gastrointestinal disturbance"
        graphable="no"
        value_name="Present"
        ds_rules="diab-C-yes-is-high.drl"/>
    <item
        measurement_type="DMME"
        display_name="Diabetes Education"
        guideline="Assess and discuss self-management challenges"
        graphable="no"
        value_name="Discussed"
        possible_answer="Yes"/>

    <item
        prevention_type="Flu"
        display_name="Flu Vaccine"
        guideline="Annually"
        graphable="no"/>
    */

    public FlowSheetItem(){

    }

    public FlowSheetItem(Map<String,String> hashtable){
        allFields = hashtable;

        measurementType = allFields.get("measurement_type");
        preventionType = allFields.get("prevention_type");
        displayName = allFields.get("display_name");
        guideline = allFields.get("guideline");
        String graph = allFields.get("graphable");
        if (graph != null && graph.equals("yes")){
           graphable = true;
        }
        dsRulesFileName = allFields.get("ds_rules");

        valueName = allFields.get("value_name");
        possibleAnswer = allFields.get("possible_answer");

    }

    public String getMeasurementType() {
        return measurementType;
    }

    public String getPreventionType() {
        return preventionType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGuideline() {
        return guideline;
    }

    public boolean isGraphable() {
        return graphable;
    }

    public String getValueName() {
        return valueName;
    }

    public String getPossibleAnswer() {
        return possibleAnswer;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public String getItemName(){
        if (measurementType !=null){
            return measurementType;
        }
        return preventionType;
    }

    public Map<String,String> getAllFields(){
        return allFields;
    }

    public RuleBase getRuleBase() {
        return ruleBase;
    }

    public void setRuleBase(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public List<TargetColour> getTargetColour() {
        return targetColour;
    }

    public void setTargetColour(List<TargetColour> targetColour) {
        this.targetColour =targetColour;
    }


}
