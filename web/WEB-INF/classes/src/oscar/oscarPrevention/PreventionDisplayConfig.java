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
