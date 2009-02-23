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
 * Ontario, Canada   Creates a new instance of ImportDemographicDataAction
 *
 *
 * ImportDemographicDataAction.java
 *
 * Created on June 29, 2005, 9:26 AM
 */

package oscar.oscarDemographic.pageUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarDemographic.data.DemographicExt;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Jay Gallagher
 */
public class ImportDemographicDataAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
       
        //TODO: put most of this in a class, validate the file being uploaded
       
        ImportDemographicDataForm frm = (ImportDemographicDataForm) form; 
        FormFile importFile = frm.getImportFile();
        ArrayList warnings = new ArrayList();
        String filename = "";
        String proNo = (String) request.getSession().getAttribute("user");
        
        try {  
         InputStream is = importFile.getInputStream();
         filename = importFile.getFileName();
         DemographicData dd = new DemographicData();
                  
         //<DemographicRecords>
         //  <DemographicData>
	 //    <DemographicInfo>
           
         SAXBuilder parser = new SAXBuilder();
         Document doc = parser.build(is);  
         
         Element demographicRecord = doc.getRootElement();         
         
         List demographicData = demographicRecord.getChildren("DemographicData");
         
         DemographicExt dExt = new DemographicExt();
         //System.out.println("how many elements: "+demographicData.size());
         for (int i = 0; i < demographicData.size(); i++){
            
            Element e = (Element) demographicData.get(i);
            Element demoInfo = e.getChild("DemographicInfo");            
            String lastName = getInternalString(demoInfo.getChild("lastName"));       //<lastName>CHINE</lastName> *
            String firstName = getInternalString(demoInfo.getChild("firstName"));     //<firstName>DEBORAH</firstName> *
            String sex = getInternalString(demoInfo.getChild("sex"));                 //<sex>F</sex> *
            String birthDate = getInternalString(demoInfo.getChild("birthDate"));     //<birthDate>1953-08-16</birthDate> *
            String versionCode = getInternalString(demoInfo.getChild("versionCode")); //<versionCode>LP</versionCode> *
            String hin = getInternalString(demoInfo.getChild("hin"));                 //<hin>1808171431</hin> *
            String address = getInternalString(demoInfo.getChild("address"));         //<address>24 King road E</address> *
            String city = getInternalString(demoInfo.getChild("city"));               //<city>Hamilton</city> *
            String province = getInternalString(demoInfo.getChild("province"));       //<province>ON</province> *
            String postalCode = getInternalString(demoInfo.getChild("postalCode"));   //<postalCode>L9N2Z4</postalCode> *
            
            String homePhone = "";
            String homeExt   = "";
            String workPhone = "";
            String workExt   = "";
            
            //System.out.println("lastName "+lastName +" firstName "+firstName+" sex "+sex+" birthDate "+birthDate+" versionCode "+versionCode+" hin "+hin);
            //System.out.println("address "+address+" city "+city+" province "+province+" postalCode "+postalCode);
            
            List telephones  = demoInfo.getChildren("telephone");
            for (int j = 0; j < telephones.size(); j++){
               Element tele = (Element) telephones.get(j);
               String telType = tele.getAttributeValue("type");
               
               if (telType != null && telType.equalsIgnoreCase("home")){
                  homePhone = tele.getAttributeValue("number");
                  homeExt   = tele.getAttributeValue("extension");
               } else if (telType != null && telType.equalsIgnoreCase("work")){
                  workPhone = tele.getAttributeValue("number");
                  workExt   = tele.getAttributeValue("extension");
               }
               //<telephone type="Home" number="905-456-6789" extension="123" />
               //<telephone type="Work" number="905-444-4444" extension="654" />                              
            }
            
            telephones = null;
            
            
            DemographicData.DemographicAddResult demoRes = null;
            //"1953-08-16"
            
            Date bDate = UtilDateUtilities.StringToDate(birthDate,"yyyy-MM-dd");
            String year_of_birth = UtilDateUtilities.DateToString(bDate,"yyyy");
            String month_of_birth = UtilDateUtilities.DateToString(bDate,"MM");
            String date_of_birth = UtilDateUtilities.DateToString(bDate,"dd");
            
            demoRes = dd.addDemographic(""/*title*/, lastName, firstName, address, city, province, postalCode,
			    homePhone, workPhone, year_of_birth, month_of_birth, date_of_birth, hin, versionCode,
			    ""/*roster_status*/, ""/*patient_status*/, "" /*date_joined*/, "" /*chart_no*/,
			    ""/*official_lang*/, ""/*spoken_lang*/, ""/*provider_no*/, sex, ""/*end_date*/,
			    ""/*eff_date*/, ""/*pcn_indicator*/, ""/*hc_type*/, ""/*hc_renew_date*/,""/*family_doctor*/,
			    ""/*email*/, ""/*pin*/, ""/*alias*/, ""/*previousAddress*/, ""/*children*/,
			    ""/*sourceOfIncome*/, ""/*citizenship*/, ""/*sin*/);                              
            

            
            if (!"".equals(homeExt)){
               dExt.addKey(proNo,demoRes.getId(),"hPhoneExt",homeExt,"");
            }
            
            if (!"".equals(workExt)){
               dExt.addKey(proNo,demoRes.getId(),"wPhoneExt",workExt,"");        
            }
            
            
       
       
           
            
            bDate = null;
            year_of_birth = null;
            month_of_birth = null;
            date_of_birth = null;
            
            e = null;
            demoInfo = null;
            lastName = null;
            firstName = null;
            sex = null;
            birthDate = null;
            versionCode = null;
            hin = null;
            address = null;
            city = null;
            province = null;
            postalCode = null;
            
            homePhone = null;
            homeExt   = null;
            workPhone = null;
            workExt   = null;
            
            //System.out.println("demo res size "+ demoRes.getWarningsCollection().size() );
            warnings.addAll(demoRes.getWarningsCollection());
            demoRes = null;
         }
                  
        }catch(Exception e) {
            warnings.add("Error processing file: "+filename);
            e.printStackTrace();
        }        
        request.setAttribute("warnings",warnings);
        
        System.out.println("warnings size "+warnings.size());
        for( int i = 0; i < warnings.size(); i++ ){
           String str = (String) warnings.get(i);
           System.out.println(str);
        }
        
        
        return mapping.findForward("success");
    }           
    
    String getInternalString(Element e){
       String ret = "";
       if (e !=null){
          ret = e.getTextTrim();
       }
       return ret;
    }
   
   
   public ImportDemographicDataAction() {
   }
   
}





