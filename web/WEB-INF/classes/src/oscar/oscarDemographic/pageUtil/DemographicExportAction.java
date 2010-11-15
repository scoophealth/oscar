/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of DemographicExportAction
 *
 *
 * DemographicExportAction.java
 *
 * Created on June 29, 2005, 11:49 AM
 */

package oscar.oscarDemographic.pageUtil;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarDemographic.data.DemographicExt;
import oscar.oscarLab.ca.on.CommonLabTestValues;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarPrevention.PreventionDisplayConfig;
import oscar.oscarReport.data.DemographicSets;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Jay Gallagher
 */
public class DemographicExportAction extends Action {

   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
      String setName = request.getParameter("patientSet");      

      DemographicSets dsets = new DemographicSets();
      ArrayList list = dsets.getDemographicSet(setName);
      ArrayList list2 = dsets.getDemographicSet(setName);
              
      Document doc = new Document();
      Element e = new Element("DemographicRecords");
         
      doc.addContent(e);
                  
      DemographicData d = new DemographicData();
      
      ArrayList inject = new ArrayList();
      
      PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance();//new PreventionDisplayConfig();         
      ArrayList prevList  = pdc.getPreventions();

      for (int k =0 ; k < prevList.size(); k++){
             Hashtable a = (Hashtable) prevList.get(k);   

             if (a != null && a.get("layout") != null &&  a.get("layout").equals("injection")){
                inject.add((String) a.get("name"));

             }	     	
      }
      
      pdc = null;
      prevList = null;
         
      oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
      oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = null;
      
      CommonLabTestValues comLab = new CommonLabTestValues();
      
      PreventionData pd = new PreventionData();
      
      DemographicExt ext = new DemographicExt();
      
      for(int i = 0 ; i < list.size(); i++){

         String demoNo = (String) list.get(i);         
         DemographicData.Demographic demographic = d.getDemographic(demoNo);
         
         Element demographicData = new Element("DemographicData");
         e.addContent(demographicData);
         
         Element demographicInfo = new Element("DemographicInfo");
         demographicData.addContent(demographicInfo);
         
         
         addElement("lastName",demographic.getLastName(),demographicInfo);
         addElement("firstName",demographic.getFirstName(),demographicInfo);
         addElement("sex",demographic.getSex(),demographicInfo);
         addElement("birthDate",demographic.getDob("-"),demographicInfo);                                                     
         addElement("versionCode",demographic.getVersionCode(),demographicInfo);
         addElement("hin",demographic.getJustHIN(),demographicInfo);
         
         addElement("address",demographic.getAddress(),demographicInfo);
         addElement("city",demographic.getCity(),demographicInfo);
         addElement("province",demographic.getProvince(),demographicInfo);
         addElement("postalCode",demographic.getPostal(),demographicInfo);
         
         Hashtable demoExt = ext.getAllValuesForDemo(demoNo);
                                                                           
         Element telephone= new Element("telephone");
         telephone.setAttribute("type","Home");
         telephone.setAttribute("number",demographic.getPhone());
         addAttrIfNotNull("extension",(String) demoExt.get("hPhoneExt"), telephone);

         demographicInfo.addContent(telephone);
         
         telephone= new Element("telephone");
         telephone.setAttribute("type","Work");
         telephone.setAttribute("number",demographic.getPhone2());
         addAttrIfNotNull("extension",(String) demoExt.get("wPhoneExt"), telephone);
                  
         demographicInfo.addContent(telephone);

         demoExt = null;
         
         // IMUNIZATIONS
         ArrayList prevList2 = pd.getPreventionData(demoNo);                           
         for (int k =0 ; k < prevList2.size(); k++){
             Hashtable a = (Hashtable) prevList2.get(k);  

             if (a != null && inject.contains((String) a.get("type")) ){
                Element imm = new Element("Immunizations");
                addAttrIfNotNull("type",(String) a.get("type"), imm);
                addAttrIfNotNull("date",(String) a.get("prevention_date"), imm);
                addAttrIfNotNull("completed",convert10toboolean((String) a.get("refused")), imm);               
                Hashtable extraData = pd.getPreventionKeyValues((String) a.get("id"));                
                addAttrIfNotNull("notes",(String)extraData.get("comments"), imm);                
                extraData = null;
                demographicData.addContent(imm);                                                                            
             }                                                       
             a = null;
         }
         prevList2 = null;
         
                  
         arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(demoNo));
         for (int p = 0; p < arr.length; p++){
            Element pres = new Element("Prescriptions");
            addAttrIfNotNull("startDate",arr[p].getRxDate().toString(), pres);
            addAttrIfNotNull("writtenDate",arr[p].getRxCreatedDate().toString() , pres);
            addAttrIfNotNull("drug",arr[p].getDrugName(), pres);
            addAttrIfNotNull("dosage",arr[p].getDosageDisplay(), pres);     ///IM NOT SURE IF THIS IS CORRECT
            addAttrIfNotNull("frequency",arr[p].getFreqDisplay(), pres);
            addAttrIfNotNull("repeats",""+arr[p].getRepeat(), pres);
            addAttrIfNotNull("quantity",arr[p].getQuantity(), pres);            
            addAttrIfNotNull("notes",arr[p].getSpecial(), pres);                               
            demographicData.addContent(pres);                                                                                        
         }
         arr = null;
                  
         ArrayList labs = comLab.findValuesForDemographic(demoNo);         
         
         for (int l = 0 ; l < labs.size(); l++){
            Hashtable h = (Hashtable) labs.get(l);
            
                        
            Element lab = new Element("LabResults");
            addAttrIfNotNull("testDate",(String) h.get("collDate"), lab);
            addAttrIfNotNull("labTestDescription",(String) h.get("testName"), lab);
            addAttrIfNotNull("result",(String) h.get("result"), lab);
            addAttrIfNotNull("unit",(String) h.get("unit"), lab);
            addAttrIfNotNull("range",(String) h.get("range"), lab);
            addAttrIfNotNull("abnormal",convertLabBoolean((String) h.get("abn")), lab);
            demographicData.addContent(lab);   
            h = null;
         }
         labs = null;
      }

      XMLOutputter outp = new XMLOutputter();
      outp.setFormat(Format.getPrettyFormat());
            
      try{

         response.setContentType("application/octet-stream");
         response.setHeader("Content-Disposition", "attachment; filename=\"export-"+setName+"-"+UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")+".xml\"" );
         
         
      outp.output(doc, response.getOutputStream());
      }catch(Exception e2){MiscUtils.getLogger().error("Error", e2);}

      return null;
      //return mapping.findForward("success");
   }  
   
   String convert10toboolean(String s){
      String ret = "true";
      if ( s!= null && s.trim().equals("1") ){
        ret = "false"; 
      }
            
      return ret;
   }
   
   String convertLabBoolean(String s){
      String ret = "false";
      if ( s!= null && ( s.trim().equals("A") || s.trim().equals("HI")  || s.trim().equals("LO") ) ){
        ret = "true"; 
      }
      return ret;
   }
   
   void addAttrIfNotNull(String attrName,String attrVal, Element addToMe){
      if (attrName != null && attrVal != null){
         addToMe.setAttribute(attrName,attrVal);      
      }
                
   }
   
   void addElement(String elementName,String elementTextValue, Element addToThis){   
      Element e = new Element(elementName);
      e.setText(elementTextValue);
      addToThis.addContent(e);         
   }
      
   public DemographicExportAction() {
   }
   
   public String currentMem(){        
       long total = Runtime.getRuntime().totalMemory();
       long free  = Runtime.getRuntime().freeMemory();
       long Used = total -  free;
       return "Total "+total+" Free "+free+" USED "+Used;
    }
}
