/**
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
 *  Ontario, Canada   Creates a new instance of PreventionDisplayConfig    
 *
 * PreventionDisplayConfig.java
 *
 * Created on May 25, 2005, 9:06 AM
 */

package oscar.oscarPrevention;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import oscar.OscarProperties;
import oscar.oscarDemographic.data.DemographicData;

/**
 *
 * @author Jay Gallagher
 */
public class PreventionDisplayConfig {
    private static Log log = LogFactory.getLog(PreventionDisplayConfig.class);
    static PreventionDisplayConfig preventionDisplayConfig = new PreventionDisplayConfig();
   
    Hashtable prevHash = null;
    ArrayList prevList = null;

    Hashtable configHash = null;
    ArrayList configList = null;

    private PreventionDisplayConfig() {}
    
    static public PreventionDisplayConfig getInstance(){
       if (preventionDisplayConfig.prevList == null) {
         preventionDisplayConfig.loadPreventions();
       }
       return preventionDisplayConfig;
    }

    public ArrayList getPreventions() {
        if (prevList == null) {
            loadPreventions();
        }
        return prevList;
    }

    public Hashtable getPrevention(String s) {
        if (prevHash == null) {
            loadPreventions();
        }
        log.debug("getting " + s);
        return (Hashtable) prevHash.get(s);
    }

    private void addPrevention(Hashtable prev, String name){
       prevList.add(prev);
       prevHash.put(name, prev);
    }
    
    
    public void loadPreventions() {
      prevList = new ArrayList();
      prevHash = new Hashtable(); 
      log.debug("STARTING2");
      
      try{
         InputStream is = this.getClass().getClassLoader().getResourceAsStream("oscar/oscarPrevention/PreventionItems.xml");               
         
         if (OscarProperties.getInstance().getProperty("PREVENTION_ITEMS") != null){
            String filename = OscarProperties.getInstance().getProperty("PREVENTION_ITEMS");
            is = new FileInputStream(filename) ;                                                
         }
                  
         SAXBuilder parser = new SAXBuilder();
         Document doc = parser.build(is);        
         Element root = doc.getRootElement();                  
         List items = root.getChildren("item");                  
         for (int i = 0; i < items.size(); i++){           
            Element e = (Element) items.get(i);
            List attr = e.getAttributes();
            Hashtable h = new Hashtable();
            String name = "";
            for (int j = 0; j < attr.size(); j++){           
               Attribute att = (Attribute) attr.get(j);
               h.put(att.getName(),att.getValue() );
               //System.out.print(att.getName()+" "+att.getValue() );               
            }            
            prevList.add(h);            
            prevHash.put(h.get("name"), h);            
         }                
      }catch(Exception e ){
         e.printStackTrace();
      }
   }
    
//    public void loadPreventionsOLD() {
//        prevList = new ArrayList();
//        prevHash = new Hashtable();
//
//        Hashtable a = new Hashtable();
//        a.put("desc", "DTaP=diphtheria, tetanus and acellular pertussis for children under 18 years of age; IPV=inactivated poliovirus");
//        a.put("name", "DTaP-IPV");
//        a.put("link", "http://www.phac-aspc.gc.ca/naci-ccni/is-si/recimmsche-icy_e.html#DTaPIPV");
//        a.put("layout", "injection");
//        prevList.add(a);
//        prevHash.put("DTaP-IPV", a);
//
//        Hashtable a1 = new Hashtable();
//        a1.put("desc", "Hib=haemophilus influenzae type B for children under 5 years of age");
//        a1.put("name", "Hib");
//        a1.put("link", "http://www.phac-aspc.gc.ca/naci-ccni/is-si/recimmsche-icy_e.html#Hib");
//        a1.put("layout", "injection");
//        prevList.add(a1);
//        prevHash.put("Hib", a1);
//
//        Hashtable a2 = new Hashtable();
//        a2.put("desc", "Pneu-C=pneumococcal 7-valent conjugate");
//        a2.put("name", "Pneu-C");
//        a2.put("link", "http://www.phac-aspc.gc.ca/naci-ccni/is-si/recimmsche-icy_e.html#PneuC");
//        a2.put("layout", "injection");
//        prevList.add(a2);
//        prevHash.put("Pneu-C", a2);
//
//        Hashtable a3 = new Hashtable();
//        a3.put("desc", "MMR=measles, mumps and rubella");
//        a3.put("name", "MMR");
//        a3.put("link", "http://www.phac-aspc.gc.ca/naci-ccni/is-si/recimmsche-icy_e.html#MMR");
//        a3.put("layout", "injection");
//        prevList.add(a3);
//        prevHash.put("MMR", a3);
//
//        Hashtable a4 = new Hashtable();
//        a4.put("desc", "MenC-C=meningocococcal C conjugate");
//        a4.put("name", "MenC-C");
//        a4.put("link", "http://www.phac-aspc.gc.ca/naci-ccni/is-si/recimmsche-icy_e.html#MenC");
//        a4.put("layout", "injection");
//        prevList.add(a4);
//        prevHash.put("MenC-C", a4);
//
//        Hashtable a5 = new Hashtable();
//        a5.put("desc", "VZ=varicella zoster");
//        a5.put("name", "VZ");
//        a5.put("link", "http://www.phac-aspc.gc.ca/naci-ccni/is-si/recimmsche-icy_e.html#Var");
//        a5.put("layout", "injection");
//        prevList.add(a5);
//        prevHash.put("VZ", a5);
//
//        Hashtable a6 = new Hashtable();
//        a6.put("desc", "Hep B=Hepatitis B");
//        a6.put("name", "HepB");
//        a6.put("link", "http://www.phac-aspc.gc.ca/naci-ccni/is-si/recimmsche-icy_e.html#HepB");
//        a6.put("layout", "injection");
//        prevList.add(a6);
//        prevHash.put("HepB", a6);
//
//        Hashtable a7 = new Hashtable();
//        a7.put("desc", "dTap=diphtheria, tatanus and acellular pertussis adult/adolescent formulation");
//        a7.put("name", "dTap");
//        a7.put("link", "http://www.phac-aspc.gc.ca/naci-ccni/is-si/recimmsche-icy_e.html#dTap");
//        a7.put("layout", "injection");
//        prevList.add(a7);
//        prevHash.put("dTap", a7);
//
//        Hashtable a8 = new Hashtable();
//        a8.put("desc", "Td=tetanus and diphtheria adult type formulation");
//        a8.put("name", "Td");
//        a8.put("link", "http://www.phac-aspc.gc.ca/naci-ccni/is-si/recimmsche-icy_e.html#dTap");
//        a8.put("layout", "injection");
//        prevList.add(a8);
//        prevHash.put("Td", a8);
//
//        Hashtable a9 = new Hashtable();
//        a9.put("desc", "Flu=influenza vaccine");
//        a9.put("name", "Flu");
//        a9.put("link", "http://www.phac-aspc.gc.ca/naci-ccni/is-si/recimmsche-icy_e.html#Flu");
//        a9.put("layout", "injection");
//        prevList.add(a9);
//        prevHash.put("Flu", a9);
//
//        Hashtable a10 = new Hashtable();
//        a10.put("desc", "PAP=");
//        a10.put("name", "PAP");
//        a10.put("link", "");
//        a10.put("layout", "PAPMAM");
//        prevList.add(a10);
//        prevHash.put("PAP", a10);
//
//        Hashtable a11 = new Hashtable();
//        a11.put("desc", "Mammorgram");
//        a11.put("name", "MAM");
//        a11.put("link", "");
//        a11.put("layout", "PAPMAM");
//        prevList.add(a11);
//        prevHash.put("MAM", a11);
//
//        Hashtable a14 = new Hashtable();
//        a14.put("desc", "PSA");
//        a14.put("name", "PSA");
//        a14.put("link", "");
//        a14.put("layout", "PAPMAM");
//        prevList.add(a14);
//        prevHash.put("PSA", a14);
//
//        Hashtable a12 = new Hashtable();
//        a12.put("desc", "Other Layout A");
//        a12.put("name", "OtherA");
//        a12.put("link", "");
//        a12.put("layout", "injection");
//        prevList.add(a12);
//        prevHash.put("OtherA", a12);
//        
//        Hashtable a13 = new Hashtable();
//        a13.put("desc", "Other Layout B");
//        a13.put("name", "OtherB");
//        a13.put("link", "");
//        a13.put("layout", "PAPMAM");
//        prevList.add(a13);
//        prevHash.put("OtherB", a13);
//        
//        
//        Hashtable a15 = new Hashtable();
//        a15.put("desc", "Hep A=Hepatitis A");
//        a15.put("name", "HepA");
//        a15.put("link", "");
//        a15.put("layout", "injection");
//        prevList.add(a15);
//        prevHash.put("HepA", a15);
//        
//        
//        Hashtable a16 = new Hashtable();
//        a16.put("desc", "Hep AB=Hepatitis AB");
//        a16.put("name", "HepAB");
//        a16.put("link", "");
//        a16.put("layout", "injection");
//        prevList.add(a16);
//        prevHash.put("HepAB", a16);
//        
//        Hashtable a17 = new Hashtable();
//        a17.put("desc", "Rabies=Rabies");
//        a17.put("name", "Rabies");
//        a17.put("link", "");
//        a17.put("layout", "injection");
//        prevList.add(a17);
//        prevHash.put("Rabies", a17);
//        
//        Hashtable a18 = new Hashtable();
//        a18.put("desc", "TB=Tuberculosis");
//        a18.put("name", "Tuberculosis");
//        a18.put("link", "");
//        a18.put("layout", "injection");
//        prevList.add(a18);
//        prevHash.put("TB", a18);
//        
//        
//    }

    public ArrayList getConfigurationSets() {
        log.debug("returning config sets");
        if (configList == null) {
            loadConfigurationSets();
        }
        log.debug("returning config sets" + configList);
        return configList;
    }

   
    public void loadConfigurationSets() {
       getPreventions();
       configHash = new Hashtable();
       configList = new ArrayList();
       try{
         InputStream is = this.getClass().getClassLoader().getResourceAsStream("oscar/oscarPrevention/PreventionConfigSets.xml");               
         if (OscarProperties.getInstance().getProperty("PREVENTION_CONFIG_SETS") != null){
            String filename = OscarProperties.getInstance().getProperty("PREVENTION_CONFIG_SETS");
            is = new FileInputStream(filename) ;                                                
         }
         SAXBuilder parser = new SAXBuilder();
         Document doc = parser.build(is);        
         Element root = doc.getRootElement();                  
         List items = root.getChildren("set");                  
         for (int i = 0; i < items.size(); i++){           
            Element e = (Element) items.get(i);
            List attr = e.getAttributes();
            Hashtable h = new Hashtable();
            String name = "";
            for (int j = 0; j < attr.size(); j++){           
               Attribute att = (Attribute) attr.get(j);
               if(att.getName().equals("prevList")){
                  h.put(att.getName(),att.getValue().split(","));
               }else{
                  h.put(att.getName(),att.getValue() );
               }
               
               System.out.print(att.getName()+" "+att.getValue() );               
            }                        
            configList.add(h);        
            configHash.put(h.get("title"), h);            
         }                
      }catch(Exception e ){
         e.printStackTrace();
      }
       
    }
    
//    public void loadConfigurationSets2() {
//        log.debug("Loading config sets");
//        getPreventions();
//        configHash = new Hashtable();
//        configList = new ArrayList();
//
//        Hashtable a1 = new Hashtable();
//        a1.put("title", "Childhood Immunization");
//        a1.put("effective", "(Ontario February 2005)");
//        a1.put("minAge", "0");
//        a1.put("maxAge", "19");
//        a1.put("prevList", new String[] { "DTaP-IPV", "Hib", "Pneu-C", "MMR", "MenC-C", "VZ", "HepB", "dTap", "Td",
//                "Flu" });
//
//        configList.add(a1);
//        configHash.put("Childhood Immunization", a1); // maybe change this to
//                                                        // a seperate id
//
//        Hashtable a2 = new Hashtable();
//        a2.put("title", "Women's Preventive Care");
//        a2.put("effective", "");
//        a2.put("minAge", "20");
//        a2.put("maxAge", "65");
//        a2.put("sex", "F");
//        a2.put("prevList", new String[] { "PAP", "MAM", "Flu", "Td" });
//
//        configList.add(a2);
//        configHash.put("Women's Preventitive Care", a2); // maybe change this
//                                                            // to a seperate id
//        Hashtable a5 = new Hashtable();
//        a5.put("title", "Men's Preventive Care");
//        a5.put("effective", "");
//        a5.put("minAge", "20");
//        a5.put("maxAge", "65");
//        a5.put("sex", "M");
//        a5.put("prevList", new String[] { "Td", "Flu", "PSA" });
//
//        configList.add(a5);
//        configHash.put("Men's Preventitive Care", a5); // maybe change this
//
//        Hashtable a3 = new Hashtable();
//        a3.put("title", "Over 65 Preventive Care");
//        a3.put("effective", "");
//        a3.put("minAge", "65");
//        a3.put("prevList", new String[] { "Flu", "Td" });
//
//        configList.add(a3);
//        configHash.put("Over 65 Preventitive Care", a3); // maybe change this
//                                                            // to a seperate id
//
//        Hashtable a4 = new Hashtable();
//        a4.put("title", "Other");
//        a4.put("effective", "");
//        a4.put("minAge", "0");
//        a4.put("prevList", new String[] { "OtherA", "OtherB" });
//
//        configList.add(a4);
//        configHash.put("Other", a4); 
//    }

    public String getDisplay(Hashtable setHash, String Demographic_no) {
        String display = "style=\"display:none;\"";
        DemographicData dData = new DemographicData();
        log.debug("demoage " + Demographic_no);
        DemographicData.Demographic demograph = dData.getDemographic(Demographic_no);
        try {
            String minAgeStr = (String) setHash.get("minAge");
            String maxAgeStr = (String) setHash.get("maxAge");
            String sex = (String) setHash.get("sex");
            int demoAge = demograph.getAgeInYears();
            String demoSex = demograph.getSex();
            boolean inAgeGroup = true;
            log.debug("min age " + minAgeStr + " max age " + maxAgeStr + " sex " + sex + " demoAge " + demoAge
                    + " demoSex " + demoSex);
            if (minAgeStr != null && maxAgeStr != null) { // between ages
                log.debug("HERE1");
                int minAge = Integer.parseInt(minAgeStr);
                int maxAge = Integer.parseInt(maxAgeStr);
                if (minAge <= demoAge && maxAge >= demoAge) {
                    display = "";
                } else {
                    inAgeGroup = false;
                }
            } else if (minAgeStr != null) { // older than
                log.debug("HERE2");
                int minAge = Integer.parseInt(minAgeStr);
                if (minAge <= demoAge) {
                    display = "";
                } else {
                    inAgeGroup = false;
                }
            } else if (maxAgeStr != null) { // younger than
                log.debug("HERE3");
                int maxAge = Integer.parseInt(maxAgeStr);
                if (maxAge >= demoAge) {
                    display = "";
                } else {
                    inAgeGroup = false;
                }
            }// else? neither defined should the default be to display it or
                // not?

            if (sex != null && inAgeGroup) {
                log.debug("HERE4");
                if (sex.equals(demoSex)) {
                    display = "";
                } else {
                    display = "style=\"display:none;\"";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return display;
    }

    public void showAllElements(Hashtable h){
       Enumeration e = h.keys();//elements();
       while(e.hasMoreElements()){
          
          log.debug(e.nextElement());
       }
    }
    
    public boolean display(Hashtable setHash, String Demographic_no,int numberOfPrevs) {
        boolean display = false;
        DemographicData dData = new DemographicData();
        log.debug("demoage " + Demographic_no);
        DemographicData.Demographic demograph = dData.getDemographic(Demographic_no);
        try {
            String minAgeStr = (String) setHash.get("minAge");
            String maxAgeStr = (String) setHash.get("maxAge");
            String sex = (String) setHash.get("sex");
            String minNumPrevs = (String) setHash.get("showIfMinRecordNum");
            int demoAge = demograph.getAgeInYears();
            String demoSex = demograph.getSex();
            boolean inAgeGroup = true;
            //log.debug("min age " + minAgeStr + " max age " + maxAgeStr + " sex " + sex + " demoAge " + demoAge
            //        + " demoSex " + demoSex);
            
            if (minNumPrevs != null){
               int minNum = Integer.parseInt(minNumPrevs);
               if (numberOfPrevs >= minNum){
                  display = true;
               }
            }
            
            if(!display){
            
               if (minAgeStr != null && maxAgeStr != null) { // between ages
                   //log.debug("HERE1");
                   int minAge = Integer.parseInt(minAgeStr);
                   int maxAge = Integer.parseInt(maxAgeStr);
                   if (minAge <= demoAge && maxAge >= demoAge) {
                       display = true;
                   } else {
                       inAgeGroup = false;
                   }
               } else if (minAgeStr != null) { // older than
                   //log.debug("HERE2");
                   int minAge = Integer.parseInt(minAgeStr);
                   if (minAge <= demoAge) {
                       display = true;
                   } else {
                       inAgeGroup = false;
                   }
               } else if (maxAgeStr != null) { // younger than
                   //log.debug("HERE3");
                   int maxAge = Integer.parseInt(maxAgeStr);
                   if (maxAge >= demoAge) {
                       display = true;
                   } else {
                       inAgeGroup = false;
                   }
               }// else? neither defined should the default be to display it or
                   // not?
            
               if (sex != null && inAgeGroup) {
                   //log.debug("HERE4");
                   if (sex.equals(demoSex)) {
                       display = true;
                   } else {
                       display = false;
                   }
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return display;
    }
    
    /*
     * <div class="immSet"> <div> <h2>Childhood Immunization <span
     * style="font-size:smaller">(Ontario February 2005)</span></h2> </div>
     * <div style="border: thin solid grey;"> <% for (int i = 0; i <
     * prevList.size() ; i++) { Hashtable h = (Hashtable) prevList.get(i); %>
     * <div style="postion:relative; margin-top:5px;float:left;clear:left;">
     * <div style="position:relative; float:left; border: thin solid blue
     * ;min-width:100px;"> <span title="<%=h.get("desc")%>"
     * style="font-weight:bold;"><%=h.get("name")%></span> <a href="<%=h.get("link")%>">ref</a>
     * <div> &nbsp;<a href="javascript: function myFunction() {return false; }"
     * onClick="javascript:popup(465,635,'AddPreventionData.jsp?prevention=<%=
     * response.encodeURL( (String) h.get("name")) %>&demographic_no=<%=demographic_no%>','addPreventionData')">.add</a>
     * </div> </div> <%ArrayList alist =
     * pd.getPreventionData((String)h.get("name"), demographic_no);
     * log.debug("alist "+alist.size()); for (int k = 0; k <
     * alist.size(); k++){ Hashtable hdata = (Hashtable) alist.get(k); %> <div
     * style="float:left; border: thin solid red ; margin-left:3px;"
     * onClick="javascript:popup(465,635,'AddPreventionData.jsp?id=<%=hdata.get("id")%>&demographic_no=<%=demographic_no%>','addPreventionData')" >
     * <div>Age: <%=hdata.get("age")%></div> <div><%=refused(hdata.get("refused"))%>:
     * <%=hdata.get("prevention_date")%></div> </div> <%}%> </div>
     * 
     * 
     * <%}%>
     * 
     * 
     * 
     * </div> </div><!--immSet-->
     * 
     */
    
    

}
