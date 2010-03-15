/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   
 *
 * MeasurementTemplateFlowSheetConfig.java
 *
 * Created on January 28, 2006, 10:45 PM
 *
 */

package oscar.oscarEncounter.oscarMeasurements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.RuleBase;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.InitializingBean;

import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementTypeBeanHandler;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementTypesBean;
import oscar.oscarEncounter.oscarMeasurements.data.ExportMeasurementType;
import oscar.oscarEncounter.oscarMeasurements.data.ImportMeasurementTypes;
import oscar.oscarEncounter.oscarMeasurements.util.DSCondition;
import oscar.oscarEncounter.oscarMeasurements.util.Recommendation;
import oscar.oscarEncounter.oscarMeasurements.util.RuleBaseCreator;
import org.oscarehr.common.model.FlowSheetCustomization;
import oscar.oscarEncounter.oscarMeasurements.util.TargetColour;

/**
 * @author jay
 */
public class MeasurementTemplateFlowSheetConfig implements InitializingBean {

    private static Log log = LogFactory.getLog(MeasurementTemplateFlowSheetConfig.class);

    private List<File> flowSheets;
    
    ArrayList<String> dxTriggers = new ArrayList<String>();
    Hashtable<String, ArrayList<String>> dxTrigHash = new Hashtable<String, ArrayList<String>>();
    Hashtable<String, String> flowsheetDisplayNames = new Hashtable<String, String>();
    ArrayList<String> universalFlowSheets = new ArrayList<String>();

    static MeasurementTemplateFlowSheetConfig measurementTemplateFlowSheetConfig;

    Hashtable<String, MeasurementFlowSheet> flowsheets = null;

    public void afterPropertiesSet() throws Exception {
        measurementTemplateFlowSheetConfig = this;
    }

    /**
     * Creates a new instance of MeasurementTemplateFlowSheetConfig
     */
    private MeasurementTemplateFlowSheetConfig() {
    }


    static public MeasurementTemplateFlowSheetConfig getInstance() {
        if (measurementTemplateFlowSheetConfig.flowsheets == null) {
            try {
                measurementTemplateFlowSheetConfig.loadFlowsheets();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return measurementTemplateFlowSheetConfig;
    }

    /**
     * Takes a list of Dx codes in, compares those dx codes to the dx triggers for each flowsheet and
     * then returns the appopriate flowsheet names in a ArrayList (Should this be an String array instead)?
     * Possible problems:
     * How to handle multiple coding systems?
     * How to query in an effiecent way
     * How to handle when codes have multiple flowsheets
     */
    public ArrayList<String> getFlowsheetsFromDxCodes(Vector coll) {
        ArrayList<String> alist = new ArrayList<String>();

        //should i search run thru the list of possible flowsheets?
        //or should i run thru the list of dx codes for the patient?
        log.debug("Triggers size " + dxTriggers.size());
        for (int i = 0; i < dxTriggers.size(); i++) {
            String dx = dxTriggers.get(i);
            log.debug("Checking dx " + dx);
            if (coll.contains(dx) && !alist.contains(dx)) {
                log.debug("coll contains " + dx);
                ArrayList<String> flowsheets = getFlowsheetForDxCode(dx);
                log.debug("Size of flowsheets for " + dx + " is " + flowsheets.size());
                for (int j = 0; j < flowsheets.size(); j++) {
                    String flowsheet = flowsheets.get(j);
                    if (!alist.contains(flowsheet)) {
                        log.debug("adding flowsheet " + flowsheet);
                        alist.add(flowsheet);
                    }
                }
            }
        }
        log.debug("alist size " + alist.size());
        return alist;
    }

    public ArrayList<String> getUniveralFlowsheets() {
        return universalFlowSheets;
    }

    public Hashtable<String, ArrayList<String>> getDxTrigHash() {
        return dxTrigHash;
    }

    public String getDisplayName(String name) {
        return flowsheetDisplayNames.get(name);
    }
    
    public Hashtable<String, String> getFlowsheetDisplayNames(){
        return flowsheetDisplayNames;
    }

    
    public String addFlowsheet(MeasurementFlowSheet m ){
        if( m.getName() == null || m.getName().equals("")){
            m.setName("M"+flowsheets.size());
        }
        flowsheets.put(m.getName(),m);
        flowsheetDisplayNames.put(m.getName(), m.getDisplayName());
        return m.getName();
    }

    void loadFlowsheets() throws FileNotFoundException {

        flowsheets = new Hashtable<String, MeasurementFlowSheet>();
        EctMeasurementTypeBeanHandler mType = new EctMeasurementTypeBeanHandler();
        //TODO: Will change this when there are more flowsheets
        log.debug("LOADING FLOWSSHEETS");
        for (File flowSheet : flowSheets) {
            InputStream is = new FileInputStream(flowSheet);
            MeasurementFlowSheet d = createflowsheet(mType, is);
            flowsheets.put(d.getName(), d);
            if (d.isUniversal())
                universalFlowSheets.add(d.getName());
            else {
                String[] dxTrig = d.getDxTriggers();
                addTriggers(dxTrig, d.getName());
            }

            flowsheetDisplayNames.put(d.getName(), d.getDisplayName());
        }
    }

    public ArrayList<String> getFlowsheetForDxCode(String code) {
        return dxTrigHash.get(code);
    }

    private void addTriggers(String[] dxTrig, String name) {
        if (dxTrig != null) {
            for (String aDxTrig : dxTrig) {
                if (!dxTriggers.contains(aDxTrig)) {
                    dxTriggers.add(aDxTrig);
                }
                if (dxTrigHash.containsKey(aDxTrig)) {
                    ArrayList<String> l = dxTrigHash.get(aDxTrig);
                    if (!l.contains(name)) {
                        l.add(name);
                    }
                } else {
                    ArrayList<String> l = new ArrayList<String>();
                    l.add(name);
                    dxTrigHash.put(aDxTrig, l);
                }
            }
        }
    }

    
    public MeasurementFlowSheet createflowsheet(InputStream is ){
         EctMeasurementTypeBeanHandler mType = new EctMeasurementTypeBeanHandler();
         MeasurementFlowSheet d = createflowsheet(mType,is);
         
            
            flowsheets.put(d.getName(), d);
            flowsheetDisplayNames.put(d.getName(), d.getDisplayName());
            return d;
    }
    
    private void processItems(List<Element> elements, List<Node> aLevels, Node parent, MeasurementFlowSheet mFlowSheet ) {
        for( Element e: elements) {
            Hashtable<String, String> h = new Hashtable<String, String>();
            List<Attribute> attr = e.getAttributes();
            for (Attribute att : attr) {
                h.put(att.getName(), att.getValue());
                //System.out.print("DDDDDDD "+att.getName()+" "+att.getValue() );
            }
            FlowSheetItem item = new FlowSheetItem(h);
            Node node = new Node();
            node.flowSheetItem = item;
            node.parent = parent;
            if( e.getName().equalsIgnoreCase("header") ) {
                List<Element>children = e.getChildren();
                node.children = new ArrayList<Node>();
                aLevels.add(node);
                processItems(children,node.children, node, mFlowSheet);
            }
            else if( e.getName().equalsIgnoreCase("item") ) {
                
                if (h.get("measurement_type") != null) {

                    log.debug("ADDING " + h.get("measurement_type"));
                    //d.addMeasurement("" + h.get("measurement_type"));
                    //d.addMeasurementInfo("" + h.get("measurement_type"), mType.getMeasurementType("" + h.get("measurement_type")));
                    //d.addMeasurementFlowSheetInfo("" + h.get("measurement_type"), h);


                    int ruleCount = 0;
                    Element rules  = e.getChild("rules");
                    if (rules !=null){
                       List<Element> recomends = rules.getChildren("recommendation");
                       List ds = new ArrayList();
                       for(Element reco: recomends){
                           ruleCount++;
                           //Hashtable recoHash =  getRecommendationHash( reco);
                            //ds.add(getRuleBaseElement("" + h.get("measurement_type")+ruleCount,"" + h.get("measurement_type"), recoHash));
                            ds.add(new Recommendation(reco,"" + h.get("measurement_type")+ruleCount,"" + h.get("measurement_type")));
                       }
                       System.out.println(""+ h.get("measurement_type")+ " adding ds  "+ds);
                       //d.addDSElement(""+ h.get("measurement_type"),ds);
                       item.setRecommendations(ds);
                    }
                    //<rules>
                    //  <recommendation between="3m-6m">Blood Glucose hasn't been reviewed in $NUMMONTHS months"</recommendation>
                    //  <warning gt="6m">Blood Glucose hasn't been reviewed in $NUMMONTHS months</warning>
                    //  <warning eq="-1">Blood Glucose hasn't been reviewed</warning>
                    //</rules>

                    /*
                     <ruleset>
                        <rule indicationColor="HIGH">
                            <condition>m.getDataAsDouble() &gt;= 7</condition>
                        </rule>
                        <rule indicationColor="HIGH">
                            <condition type="getDataAsDouble"  value="&lt;= 2" />
                            <condition type="getDataAsDouble"  value="&gt;= 0.07"/>
                        </rule>
                     </ruleset>
                     */
                    Element rulesets = e.getChild("ruleset");
                    List<TargetColour> rs = new ArrayList();
                    if (rulesets != null){
                        List<Element> rulez = rulesets.getChildren("rule");
                        if (rulez != null){
                            for(Element r: rulez){
                                rs.add(new TargetColour(r));
                                //r.getAttributeValue("indicatorColour");
                            }
                        }

                    }

                    log.debug(" meas "+h.get("measurement_type")+"  size "+rs.size());

                    if (rs.size() > 0){
                        item.setTargetColour(rs);
                    }

                }

                item = mFlowSheet.addListItem(item);
                node.flowSheetItem = item;
                aLevels.add(node);
                //for( Element element : )
            }
        }
       
    }

    public class Node {
        public Node parent;
        public List<Node> children;
        public FlowSheetItem flowSheetItem;
        public int numSibling = -1;

        public Node getFirstChild() {
            if( children != null && children.size() > 0 ) {
                numSibling = 0;
                return children.get(numSibling);
            }

            return null;
        }

        public Node getNextSibling() {

            if( parent != null) {
                ++parent.numSibling;
                if( parent.numSibling < parent.children.size() ) {
                    return parent.children.get(parent.numSibling);
                }
            }

            return null;
        }

        public boolean hasNextSibling() {
            if( parent == null ) {
                return false;
            }

            return (parent.numSibling < parent.children.size()-1);
        }
    }

    private MeasurementFlowSheet createflowsheet(final EctMeasurementTypeBeanHandler mType, InputStream is) {
        MeasurementFlowSheet d = new MeasurementFlowSheet();

        try {
            SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(is);
            Element root = doc.getRootElement();
            
            ///
            XMLOutputter outp = new XMLOutputter();
            //outp.output(root, System.out);
 
            
            ///
            //MAKE SURE ALL MEASUREMENTS HAVE BEEN INITIALIZED
            ImportMeasurementTypes importMeasurementTypes = new ImportMeasurementTypes();
            importMeasurementTypes.importMeasurements(root);

            List indi = root.getChildren("indicator"); // key="LOW" colour="blue">
            for (int i = 0; i < indi.size(); i++) {
                Element e = (Element) indi.get(i);
                d.AddIndicator(e.getAttributeValue("key"), e.getAttributeValue("colour"));
            }
            List<Element> elements = root.getChildren();
            List<Element> items = root.getChildren("item");
            List<Node> aItems = new ArrayList<Node>();
//            for (Element e : elements ) {
                processItems(elements, aItems, null, d);
                d.setItemHeirarchy(aItems);
//                List<Attribute> attr = e.getAttributes();

//                Hashtable<String, String> h = new Hashtable<String, String>();
//               for (Attribute att : attr) {
//                    h.put(att.getName(), att.getValue());
                    //System.out.print("DDDDDDD "+att.getName()+" "+att.getValue() );
//               }
//                FlowSheetItem item = new FlowSheetItem(h);
//                if (h.get("measurement_type") != null) {
//
//                    log.debug("ADDING " + h.get("measurement_type"));
//                    //d.addMeasurement("" + h.get("measurement_type"));
//                    //d.addMeasurementInfo("" + h.get("measurement_type"), mType.getMeasurementType("" + h.get("measurement_type")));
//                    //d.addMeasurementFlowSheetInfo("" + h.get("measurement_type"), h);
//
//
//                    int ruleCount = 0;
//                    Element rules  = e.getChild("rules");
//                    if (rules !=null){
//                       List<Element> recomends = rules.getChildren("recommendation");
//                       List ds = new ArrayList();
//                       for(Element reco: recomends){
//                           ruleCount++;
//                           //Hashtable recoHash =  getRecommendationHash( reco);
//                            //ds.add(getRuleBaseElement("" + h.get("measurement_type")+ruleCount,"" + h.get("measurement_type"), recoHash));
//                            ds.add(new Recommendation(reco,"" + h.get("measurement_type")+ruleCount,"" + h.get("measurement_type")));
//                       }
//                       System.out.println(""+ h.get("measurement_type")+ " adding ds  "+ds);
//                       //d.addDSElement(""+ h.get("measurement_type"),ds);
//                       item.setRecommendations(ds);
//                    }
//                    //<rules>
//                    //  <recommendation between="3m-6m">Blood Glucose hasn't been reviewed in $NUMMONTHS months"</recommendation>
//                    //  <warning gt="6m">Blood Glucose hasn't been reviewed in $NUMMONTHS months</warning>
//                    //  <warning eq="-1">Blood Glucose hasn't been reviewed</warning>
//                    //</rules>
//
//                    /*
//                     <ruleset>
//                        <rule indicationColor="HIGH">
//                            <condition>m.getDataAsDouble() &gt;= 7</condition>
//                        </rule>
//                        <rule indicationColor="HIGH">
//                            <condition type="getDataAsDouble"  value="&lt;= 2" />
//                            <condition type="getDataAsDouble"  value="&gt;= 0.07"/>
//                        </rule>
//                     </ruleset>
//                     */
//                    Element rulesets = e.getChild("ruleset");
//                    List<TargetColour> rs = new ArrayList();
//                    if (rulesets != null){
//                        List<Element> rulez = rulesets.getChildren("rule");
//                        if (rulez != null){
//                            for(Element r: rulez){
//                                rs.add(new TargetColour(r));
//                                //r.getAttributeValue("indicatorColour");
//                            }
//                        }
//
//                    }
//
//                    log.debug(" meas "+h.get("measurement_type")+"  size "+rs.size());
//
//                    if (rs.size() > 0){
//                        item.setTargetColour(rs);
//                    }
//
//                }// else {
                 //   d.addMeasurement("" + h.get("prevention_type"));
                 //   d.addMeasurementFlowSheetInfo("" + h.get("prevention_type"), h);
                //}
                //log.debug("ADDING ITEM "+item);
                //d.addListItem(item);
                
                
                
                //prevList.add(h);
                //prevHash.put(h.get("name"), h);
            //}
            if (root.getAttribute("name") != null) {
                d.setName(root.getAttribute("name").getValue());
            }
            if (root.getAttribute("display_name") != null) {
                d.setDisplayName(root.getAttribute("display_name").getValue());
            }
            
            if (root.getAttribute("top_HTML") != null) {
                d.setTopHTMLFileName(root.getAttribute("top_HTML").getValue());
            }           
            
            if (root.getAttribute("ds_rules") != null) {
                d.loadRuleBase(root.getAttribute("ds_rules").getValue());
            }
            if (root.getAttribute("dxcode_triggers") != null) {
                d.parseDxTriggers(root.getAttribute("dxcode_triggers").getValue());
            }

            if (root.getAttribute("warning_colour") != null) {
                d.setWarningColour(root.getAttribute("warning_colour").getValue());
            }
            if (root.getAttribute("recommendation_colour") != null) {
                d.setRecommendationColour(root.getAttribute("recommendation_colour").getValue());
            }
            if (root.getAttribute("is_universal") != null) {
                d.setUniversal("true".equals(root.getAttribute("is_universal").getValue()));
            }
            if (root.getAttribute("is_medical") != null) {
                d.setMedical("true".equals(root.getAttribute("is_medical").getValue()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        d.loadRuleBase();
        return d;
    }
    
    //<rules>    
    //  <recommendation between="3m-6m">Blood Glucose hasn't been reviewed in $NUMMONTHS months"</recommendation>
    //  <warning gt="6m">Blood Glucose hasn't been reviewed in $NUMMONTHS months</warning>
    //  <warning eq="-1">Blood Glucose hasn't been reviewed</warning>
    //</rules>
    
    private Hashtable<String,String> getRecommendationHash(Element recowarn){
        Hashtable h = new Hashtable();
        String toParse = recowarn.getAttributeValue("monthrange");
        h.put("monthrange", toParse);
        
        if( recowarn.getAttribute("strength") != null){
            h.put("strength",recowarn.getAttribute("strength") );
        }
        if ( recowarn.getText() == null){
            h.put("text",recowarn.getText());
        }
        return h;
    }
    
    private Element getRuleBaseElement(String ruleName,String measurement,Hashtable<String,String> recowarn){
        
        log.debug("LOADING RULES - getRuleBaseElement");
                    ArrayList list = new ArrayList();
        String toParse = recowarn.get("monthrange");
        String consequenceType = "Recommendation";
        if( recowarn.get("strength") != null){
            if (recowarn.get("strength").equals("warning")){
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
                    if ( recowarn.get("text") == null){
                         consequence ="m.add"+consequenceType+"(\""+measurement+"\",\""+measurement+"\" hasn't been reviewed in \"+m.getLastDateRecordedInMonths(\""+measurement+"\")+\" months\";";
                    }
                } 

            }else if (toParse.indexOf("&gt;") != -1 ||  toParse.indexOf(">") != -1 ){ // greater than style
                toParse = toParse.replaceFirst("&gt;","");
                toParse = toParse.replaceFirst(">","");
                
                int gt = Integer.parseInt(toParse);
                
                list.add(new DSCondition("getLastDateRecordedInMonths", measurement, ">", ""+gt));  
                if ( recowarn.get("text") == null){
                         consequence ="m.add"+consequenceType+"(\""+measurement+"\",\""+measurement+"\" hasn't been reviewed in \"+m.getLastDateRecordedInMonths(\""+measurement+"\")+\" months\";";
                }
            }else if (toParse.indexOf("&lt;") != -1  ||  toParse.indexOf("<") != -1 ){ // less than style
                toParse = toParse.replaceFirst("&lt;","");
                toParse = toParse.replaceFirst("<","");
                
                int lt = Integer.parseInt(toParse);           
                list.add(new DSCondition("getLastDateRecordedInMonths", measurement, "<=", ""+lt));
                if ( recowarn.get("text") == null){
                         consequence ="m.add"+consequenceType+"(\""+measurement+"\",\""+measurement+"\" hasn't been reviewed in \"+m.getLastDateRecordedInMonths(\""+measurement+"\")+\" months\";";
                    }
                
            }else if (!toParse.equals("")){ // less than style
                int eq = Integer.parseInt(toParse); 
                list.add(new DSCondition("getLastDateRecordedInMonths", measurement, "==", ""+eq));
                if ( recowarn.get("text") == null){
                         consequence ="m.add"+consequenceType+"(\""+measurement+"\",\""+measurement+"\" hasn'taaaaa been reviewed in \"+m.getLastDateRecordedInMonths(\""+measurement+"\")+\" months\";";
                    }
            }
        
        if ( recowarn.get("text") != null){
            String txt = recowarn.get("text");
            log.debug("TRY TO REPLACE $NUMMONTHS:"+txt.indexOf("$NUMMONTHS")+" WITH "+NUMMONTHS+  " "+txt);
            
            txt = txt.replaceAll("\\$NUMMONTHS", NUMMONTHS);
            log.debug("TEXT "+txt);
            consequence ="m.add"+consequenceType+"(\""+measurement+"\",\""+txt+"\");";
            //consequence ="System.out.println(\"HAPPY TO BE WORKING\");";
            
        }
        
        RuleBaseCreator rcb = new RuleBaseCreator();
        Element ruleElement = rcb.getRule(ruleName, "oscar.oscarEncounter.oscarMeasurements.MeasurementInfo", list,  consequence) ;
    
        
        return ruleElement;
    }
    
    
    
    public MeasurementFlowSheet getFlowSheet(String flowsheetName,List<FlowSheetCustomization> list) {
        log.debug("IN CUSTOMIZED FLOWSHEET ");
        if (list.size() > 0){
            log.debug("IN CUSTOMIZED FLOWSHEET "+list.size());
            try{
            MeasurementFlowSheet personalizedFlowsheet =  makeNewFlowsheet(getFlowSheet(flowsheetName) );
           
            for (FlowSheetCustomization cust:list){
                if (FlowSheetCustomization.ADD.equals(cust.getAction())){
                    log.debug(" CUST ADDING");
                    FlowSheetItem item =getItemFromString(cust.getPayload());
                    if (item.getTargetColour() != null && item.getTargetColour().size()>0){
                        RuleBase rb = personalizedFlowsheet.loadMeasuremntRuleBase(item.getTargetColour());
                        item.setRuleBase(rb);
                    }
                    personalizedFlowsheet.addAfter(cust.getMeasurement(), item);
                }else if(FlowSheetCustomization.UPDATE.equals(cust.getAction())){
                    log.debug(" CUST UPDATING");
                    FlowSheetItem item =getItemFromString(cust.getPayload());
                    if (item.getTargetColour() != null && item.getTargetColour().size()>0){
                        RuleBase rb = personalizedFlowsheet.loadMeasuremntRuleBase(item.getTargetColour());
                        item.setRuleBase(rb);
                    }
                    personalizedFlowsheet.updateMeasurementFlowSheetInfo(cust.getMeasurement(),item);
                     
                    
                }else if(FlowSheetCustomization.DELETE.equals(cust.getAction())){
                    personalizedFlowsheet.setToHidden(cust.getMeasurement());
                    log.debug(" CUST DELETE");
                }else{
                    log.debug("ERR"+cust);
                }
            }
            personalizedFlowsheet.loadRuleBase();
            return personalizedFlowsheet;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        log.debug("Returning normal flowsheet");
        return getFlowSheet(flowsheetName);
    }
    
    
//    public MeasurementFlowSheet getFlowSheet(String flowsheetName,String providerNo,String demographicNo) {
//        log.debug("DOME " +demographicNo);
//        if (demographicNo.equals("2")){
//            try{
//            MeasurementFlowSheet personalizedFlowsheet =  makeNewFlowsheet(getFlowSheet(flowsheetName) );
//            EctMeasurementTypeBeanHandler mType = new EctMeasurementTypeBeanHandler();
//            
//                 Hashtable h = new Hashtable();
//
//                    h.put("measurement_type","BP");
//                    h.put("display_name", "BLood Pressure");
//                    h.put("guideline", "");
//                    h.put("graphable", "NO");
//                    h.put("value_name", "BP");
//                    int cou =0;
//                    FlowSheetItem item = new FlowSheetItem(h);
//                 personalizedFlowsheet.addFlowSheetItem(cou, item);
//                
//            return personalizedFlowsheet;
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//            
//        return getFlowSheet(flowsheetName);
//    }
    
    
    public MeasurementFlowSheet getFlowSheet(String flowsheetName) {
        log.debug("GET FLOWSHEET "+flowsheetName+"  "+flowsheets.get(flowsheetName));
        return flowsheets.get(flowsheetName);
    }

    public List<File> getFlowSheets() {
        return flowSheets;
    }

    public void setFlowSheets(List<File> flowSheets) {
        log.debug("SETTING FLOWSHEETS");
        this.flowSheets = flowSheets;
    }
    
 
    
    //This could be used to create the custom on the file flowsheet
    public MeasurementFlowSheet makeNewFlowsheet(MeasurementFlowSheet mFlowsheet ) throws Exception{
            XMLOutputter outp = new XMLOutputter();
            Element va = getExportFlowsheet( mFlowsheet);
            
            ByteArrayOutputStream byteArrayout = new ByteArrayOutputStream();
            outp.output(va, byteArrayout);
            
            //System.out.println("FLOWSHEET EXPORT "+mFlowsheet.getDisplayName());
            //outp.output(va,System.out);
 
            InputStream is = new ByteArrayInputStream(byteArrayout.toByteArray());

            EctMeasurementTypeBeanHandler mType = new EctMeasurementTypeBeanHandler();
            MeasurementFlowSheet d = createflowsheet(mType,is);
            
            //System.out.println("NOW LOOKS LIKE");
            //outp.output(getExportFlowsheet( d),System.out);
            return d;
            
    }
    
    public FlowSheetItem getItemFromString(String s){
        log.debug("->>>"+s);
        FlowSheetItem item = null;
        try {
            SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(new StringReader(s));
            Element root = doc.getRootElement();
            
            List<Attribute> attr = root.getAttributes();
                Hashtable<String, String> h = new Hashtable<String, String>();
                for (Attribute att : attr) {
                    h.put(att.getName(), att.getValue());//System.out.print(att.getName()+" "+att.getValue() );
                }
                item = new FlowSheetItem(h);
                
                int ruleCount = 0;
                    Element rules  = root.getChild("rules");
                    
                    if (rules !=null){
                       List<Element> recomends = rules.getChildren("recommendation");
                       List ds = new ArrayList();
                       for(Element reco: recomends){
                           ruleCount++;
                           ds.add(new Recommendation(reco,"" + h.get("measurement_type")+ruleCount,"" + h.get("measurement_type")));
                       }
                       log.debug(""+ h.get("measurement_type")+ " adding ds  "+ds);
                       item.setRecommendations(ds);
                    }
                    
                    
                    Element rulesets = root.getChild("ruleset");
                    List<TargetColour> rs = new ArrayList();
                    if (rulesets != null){
                        List<Element> rulez = rulesets.getChildren("rule");
                        if (rulez != null){
                            for(Element r: rulez){
                                rs.add(new TargetColour(r));
                                //r.getAttributeValue("indicatorColour");
                            }
                        }
                        
                    }
                    
                    log.debug(" meas "+h.get("measurement_type")+"  size "+rs.size());
                    
                    if (rs.size() > 0){
                        item.setTargetColour(rs);
                       
                    }
                    
                    
                    
                    
        }catch(Exception e){
            e.printStackTrace();
        }
         return item;     
    }
    
    
    public Element getItemFromObject(FlowSheetItem fsi){
        Element item = new Element("item");
                    
                    Hashtable h2 = fsi.getAllFields();

                    addAttributeifValueNotNull(item, "prevention_type", fsi.getPreventionType());
                    addAttributeifValueNotNull(item, "measurement_type", fsi.getMeasurementType());
                    addAttributeifValueNotNull(item, "display_name", fsi.getDisplayName());
                    addAttributeifValueNotNull(item, "guideline", fsi.getGuideline());
                    addAttributeifValueNotNull(item, "graphable", (String) h2.get("graphable"));
                    addAttributeifValueNotNull(item, "ds_rules", (String) h2.get("ds_rules"));
                    addAttributeifValueNotNull(item, "value_name", fsi.getValueName());

                    if (h2.get("measurement_type") != null) {
                        log.debug("MEASUREMENT TYPE " + (String) h2.get("measurement_type"));

                        List<Recommendation> dsR = fsi.getRecommendations();
                        log.debug(h2.get("measurement_type") + " LIST DSR " + dsR);
                        if (dsR != null) {
                            Element rules = new Element("rules");
                            for (Recommendation e : dsR) {
                                log.debug("BEFORE ADDING ");
                                rules.addContent(e.getFlowsheetXML());
                                log.debug(rules);
                            }
                            item.addContent(rules);
                        }
                        
                        
                        List<TargetColour> targetColour =fsi.getTargetColour();
                        log.debug("TARGET COLOURS"+targetColour);
                        
                        if (targetColour != null){
                           Element ruleset = new Element("ruleset");
                           
                           for(TargetColour t : targetColour){
                               ruleset.addContent(t.getFlowsheetXML());
                           } 
                           item.addContent(ruleset);
                        }
                    }
                    return item;
    }
    
    
    
    
    public Element getExportFlowsheet(MeasurementFlowSheet mFlowsheet){
            EctMeasurementTypeBeanHandler mType = new EctMeasurementTypeBeanHandler();
            Element va = new Element("flowsheet");
        

            addAttributeifValueNotNull(va, "name", mFlowsheet.getName());
            addAttributeifValueNotNull(va, "display_name", mFlowsheet.getDisplayName());
            //addAttributeifValueNotNull(va,"ds_rules",mFlowsheet.get);
            addAttributeifValueNotNull(va, "warning_colour", mFlowsheet.getWarningColour());
            addAttributeifValueNotNull(va, "recommendation_colour", mFlowsheet.getRecommendationColour());
            addAttributeifValueNotNull(va, "top_HTML", mFlowsheet.getTopHTMLStream());        
            addAttributeifValueNotNull(va,"dxcode_triggers",mFlowsheet.getDxTriggersString());

            Hashtable indicatorHash = mFlowsheet.getIndicatorHashtable();
            Enumeration enu = indicatorHash.keys();
            while (enu.hasMoreElements()) {
                String key = (String) enu.nextElement();
                Element ind = new Element("indicator");
                addAttributeifValueNotNull(ind, "key", key);
                addAttributeifValueNotNull(ind, "colour", (String) indicatorHash.get(key));
                va.addContent(ind);
            }

            List<String> measurements = mFlowsheet.getMeasurementList();

            log.debug("SET HAS MEASUREMENTS" + measurements);
            int count = 0;
            if (measurements != null) {
                for (String mstring : measurements) {
                    //java.util.List ruleList = mFlowsheet.getRules(mstring);
                    
                    EctMeasurementTypesBean measurementTypesBean = mType.getMeasurementType(mstring);

                    Hashtable h2 = mFlowsheet.getMeasurementFlowSheetInfo(mstring);

                    Element item = new Element("item");
                    

                    addAttributeifValueNotNull(item, "prevention_type", (String) h2.get("prevention_type"));
                    addAttributeifValueNotNull(item, "measurement_type", (String) h2.get("measurement_type"));
                    addAttributeifValueNotNull(item, "display_name", (String) h2.get("display_name"));
                    addAttributeifValueNotNull(item, "guideline", (String) h2.get("guideline"));
                    addAttributeifValueNotNull(item, "graphable", (String) h2.get("graphable"));
                    addAttributeifValueNotNull(item, "value_name", (String) h2.get("value_name"));
                    addAttributeifValueNotNull(item, "ds_rules", (String) h2.get("ds_rules"));
                    if (h2.get("measurement_type") != null) {
                        log.debug("MEASUREMENT TYPE " + (String) h2.get("measurement_type"));

                        List<Recommendation> dsR = mFlowsheet.getDSElements((String) h2.get("measurement_type"));
                        log.debug(h2.get("measurement_type") + " LIST DSR " + dsR);
                        if (dsR != null) {
                            Element rules = new Element("rules");
                            for (Recommendation e : dsR) {
                                log.debug("BEFORE ADDING ");
                                rules.addContent(e.getFlowsheetXML());
                                log.debug(rules);
                            }
                            item.addContent(rules);
                        }
                        
                        FlowSheetItem fsi = mFlowsheet.getFlowSheetItem(mstring);  //TODO: MOVE THIS UP AND REPLACE THE CODE ABOVE
                        List<TargetColour> targetColour =fsi.getTargetColour();
                        log.debug("TARGET COLOURS"+targetColour);
                        
                        if (targetColour != null){
                           Element ruleset = new Element("ruleset");
                           
                           for(TargetColour t : targetColour){
                               ruleset.addContent(t.getFlowsheetXML());
                           } 
                           item.addContent(ruleset);
                        }
                        
                    }

                    va.addContent(item);
                    count++;
           

                    if (measurementTypesBean != null) {
                        ExportMeasurementType emt = new ExportMeasurementType();
                        Element export = emt.exportElement(measurementTypesBean);
                        va.addContent(export);
                    } else {
                        log.debug("--- not loaded --- " + mstring);
                    }
                }
            }
            return va;
            //XMLOutputter outp = new XMLOutputter();
            //outp.setFormat(Format.getPrettyFormat());
    }
    
       private void addAttributeifValueNotNull(Element element, String attr, String value) {
        if (value != null) {
            element.setAttribute(attr, value);
        }
    }
    
    
}

