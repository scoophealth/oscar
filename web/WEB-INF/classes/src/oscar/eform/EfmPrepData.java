/*
 * 
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
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.eform ; 

//import java.util.*; 
import java.net.*; 
import java.util.*; 
import oscar.util.*; 
import javax.servlet.http.HttpServletRequest;

public class EfmPrepData { 
  private String patient_name;
  private String provider_name;
  private String today;
  private String label ;
  private String address;
  private String addressLine;
  private String doctor;
  private String DOB;
  private String Email;
  private String HIN;
  private String phone;
  private String phone2;
  private String clinic_name;
  private String clinic_phone;
  private String clinic_fax;
  private String clinic_label;
  private String clinic_addressLine;
  private String clinic_addressLineFull;
  private String clinic_address;
  private String NameAddress;  
  private String Problem_List ;
  private String Medication ;
  private String Family_Social_History ;
  private String Alert ;
  private String age;
  private String sex;
  private String FHNcontact;
  private String lastPapDate;
  private String lastMamDate;
  private String lastImmDate;
  private String lastFluDate;
  private String guardian_name;
  private String guardian_NameAddress;
  
  private String socialFamilyHistory  ;
  private String otherMedications   ;
  private String medicalHistory  ;
  private String ongoingConcerns ;
  private String reminders      ;

  private int provider_no;
  private int demographic_no ;
  private int fid ;
  private String form_name ;
  String [] meta ;
  String [] value ;
  String [][] tagSymbol = new String[][] { {"input", ""}, {"textarea",">"}, {"checkbox",""}, {"form",""}, {"hidden", ""} };

  public EfmPrepData(int provider_no1, int demographic_no1, int fid1, String form_name1) { 
    provider_no = provider_no1;
    demographic_no = demographic_no1 ;
    form_name = form_name1 ;
    fid = fid1 ;
    setPatientName();
    setToday();
    setLabel();
    setAddress();
    setAddressLine();
    setDoctor();
    setDOB();
    setNameAddress();
    setEmail();
    setHIN();
    setPhone();
    setPhone2();
    setAge();
    setSex();
    setFHNcontact();
    setLastPapDate();
    setLastMamDate();
    setLastImmDate();
    setLastFluDate();
    setGuardianName();
    setGuardianNameAddress();
    setClinicName();
    setClinicPhone();
    setClinicFax();
    setClinicLabel();
    setClinicAddressLine();
    setClinicAddressLineFull();
    setClinicAddress();
    setEChartAcc();
    setProviderName();
    createDemoAcc();
    
    meta = new String[] {"oscarDB=patient_name","action=","oscarDB=today","oscarDB=label","oscarDB=address","oscarDB=addressLine","oscarDB=doctor","oscarDB=DOB","oscarDB=NameAddress","oscarDB=Email","oscarDB=HIN","oscarDB=phone","oscarDB=phone2","oscarDB=clinic_name","oscarDB=clinic_phone","oscarDB=clinic_fax","oscarDB=clinic_label","oscarDB=clinic_addressLine","oscarDB=clinic_addressLineFull","oscarDB=clinic_address","oscarDB=Problem_List","oscarDB=Medication","oscarDB=Family_Social_History","oscarDB=Alert","oscarDB=Social_Family_History","oscarDB=Other_Medications_History","oscarDB=Medical_History","oscarDB=OngoingConcerns","oscarDB=Reminders", "oscarDB=age", "oscarDB=sex", "oscarDB=FHNcontact", "oscarDB=LastPapDate", "oscarDB=LastMamDate", "oscarDB=LastImmDate", "oscarDB=LastFluDate", "oscarDB=guardian_name", "oscarDB=guardian_NameAddress", "oscarDB=provider_name"};
    try {
      value = new String[] {patient_name," \"savemyform.jsp?demographic_no="+demographic_no+"&fid="+fid+"&form_name="+URLEncoder.encode(form_name,"UTF-8")+"\"", today, label, address, addressLine, doctor, DOB, NameAddress, Email, HIN, phone, phone2, clinic_name, clinic_phone, clinic_fax, clinic_label, clinic_addressLine, clinic_addressLineFull, clinic_address, Problem_List, Medication, Family_Social_History,Alert,socialFamilyHistory ,otherMedications ,medicalHistory ,ongoingConcerns ,reminders, age, sex, FHNcontact, lastPapDate, lastMamDate, lastImmDate, lastFluDate, guardian_name, guardian_NameAddress, provider_name };
    } catch(Exception ex) {
      System.err.println(" : " + ex.getMessage());
    }
  }

  public EfmPrepData(HttpServletRequest req, String strHTML) { 
    String temp = null;
    ArrayList metaTemp = new ArrayList() ;
    ArrayList valueTemp = new ArrayList() ;
    String[] tagElm = new String[] { "type = text", "textarea","type = checkbox", "type = hidden" };
    int i = 0;

	  for (Enumeration e = req.getParameterNames() ; e.hasMoreElements() ;) {
		  temp=e.nextElement().toString();
      String elmTag = isRightParam(strHTML, temp, tagElm) ;
		  if(elmTag!=null ) {
        metaTemp.add(i, temp) ;
        if(elmTag.indexOf("textarea") < 0 && elmTag.indexOf("text") >= 0) valueTemp.add(i, " value=\""+req.getParameter(temp)+"\"" ) ;
        else if (elmTag.indexOf("textarea") < 0 && elmTag.indexOf("hidden") >= 0) valueTemp.add(i, " value=\""+req.getParameter(temp)+"\"" );
        else if(elmTag.indexOf("checkbox") >= 0 ) valueTemp.add(i, " checked" ) ; 
   
        else valueTemp.add(i, req.getParameter(temp) ) ;
        i++;
      }
    }
    meta = new String[metaTemp.size()] ;
    value = new String[metaTemp.size()] ;
    for (int j=0; j<metaTemp.size(); j++ ) {
      meta[j] = (String) metaTemp.get(j) ;
      value[j] = (String) valueTemp.get(j) ;
    }
  }

  //tagElm = {"input", "textarea", "checkbox", ...} , param = name_...
  private String isRightParam(String strHTML, String param, String[] tagElm) {
    String bParam = null;
    int fromIndex = 0 ;
    int posIndex = 0;
    int lPos = 0; // < pos
    int mPos = 0; // > pos

    for(; ;) { //check all the appearance of param
      posIndex = strHTML.toLowerCase().indexOf(param.toLowerCase(), fromIndex) ;
      if ( posIndex  > 0 ) {
        mPos = strHTML.indexOf('>', posIndex) ;
        lPos = strHTML.indexOf('<', posIndex) ;
 
        if (mPos<lPos || (lPos<0 && mPos>0) ) { //in tag
          //change the pos and check the next appearance of the exact param
          if (!(strHTML.charAt(posIndex+param.length())=='"' || strHTML.charAt(posIndex+param.length())==' ' || strHTML.charAt(posIndex+param.length())=='\'' || strHTML.charAt(posIndex+param.length())=='>')) {
            fromIndex = posIndex + param.length(); 
            continue; //not the exact param, just alike
          }

          for(int i = 0; i < tagElm.length; i++) {
            StringTokenizer strToken = new StringTokenizer(tagElm[i]," ");
            String tempToken=null;
            
            while(strToken.hasMoreTokens()){
       	      tempToken = strToken.nextToken();
              if(strHTML.substring(strHTML.substring(0,posIndex).lastIndexOf('<'), posIndex).toLowerCase().indexOf(tempToken.toLowerCase() ) < 0) {
                bParam = null ;
                break ;
              } else { bParam = tagElm[i] ; 
              }
            } 
            
            if(bParam!=null) break;  //find the right param
            //if(strHTML.substring(strHTML.substring(0,posIndex).lastIndexOf('<'), posIndex).toLowerCase().indexOf(tagElm[i].toLowerCase() ) >= 0) bParam = true ;
          }
          break;
        }
        fromIndex = posIndex + param.length(); //change the pos and check the next appearance of the param[]
      } else break; //no param in the text
    }
   
    return bParam;
  }

  public void setAddress() {  
    address = (new EfmDataOpt()).getAddress(demographic_no);
  }
  public void setAddressLine() {  
    addressLine = " value=\"" + (new EfmDataOpt()).getAddressLine(demographic_no) + "\"";
  }
  public void setDoctor() {  
    doctor = " value=\"" + (new EfmDataOpt()).getDoctor(demographic_no) + "\"";
  }
  public void setDOB() {  
    DOB = " value=\"" + (new EfmDataOpt()).getDOB(demographic_no) + " (d/m/y)\"";
  }
  public void setNameAddress() {  
    NameAddress = (new EfmDataOpt()).getNameAddress(demographic_no);
  }
  public void setEmail() {
    Email = " value=\"" + (new EfmDataOpt()).getEmail(demographic_no) + "\"";
   }
  public void setHIN() {
    HIN = " value=\"" + (new EfmDataOpt()).getHIN(demographic_no) + "\"";
  }
  public void setPhone() {
    phone = " value=\"" + (new EfmDataOpt()).getPhone(demographic_no) + "\"";
  }
  public void setPhone2() {
    phone2 = " value=\"" + (new EfmDataOpt()).getPhone2(demographic_no) + "\"";
  }
  public void setClinicName() {
    clinic_name = " value=\"" + (new EfmDataOpt()).getClinicName() + "\"";
  }
  public void setClinicPhone() {
    clinic_phone = " value=\"" + (new EfmDataOpt()).getClinicPhone() + "\"";
  }
  public void setClinicFax() {
    clinic_fax = " value=\"" + (new EfmDataOpt()).getClinicFax() + "\"";
  }
  public void setClinicLabel() {  
    clinic_label = (new EfmDataOpt()).getClinicLabel();
  }
  public void setClinicAddressLine() {
    clinic_addressLine = " value=\"" + (new EfmDataOpt()).getClinicAddressLine() + "\"";
  }
  public void setClinicAddressLineFull() {
    clinic_addressLineFull = " value=\"" + (new EfmDataOpt()).getClinicAddressLineFull() + "\"";
  }
  public void setClinicAddress() {
    clinic_address = (new EfmDataOpt()).getClinicAddress();
  }
  public void setPatientName() {  
    patient_name = " value=\"" + (new EfmDataOpt()).getPatientName(demographic_no) + "\"";
  }
  public void setToday() {  
    today = " value=\"" + UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd" ) + "\"";
  }
  public void setAge() {  
    age = " value=\"" + (new EfmDataOpt()).getAge(demographic_no) + "\"";
    //System.out.println("age: " + age);
  }
  public void setSex() {  
    sex = " value=\"" + (new EfmDataOpt()).getSex(demographic_no) + "\"";
  }
  public void setFHNcontact() {
    FHNcontact = " value=\"" + (new EfmDataOpt()).getFHNcontact(demographic_no) + "\"";
  }
  public void setLastPapDate() {
    lastPapDate = " value=\"" + (new EfmDataOpt()).getLastPapDate(demographic_no) + "\"";
  }
  public void setLastMamDate() {
    lastMamDate = " value=\"" + (new EfmDataOpt()).getLastMamDate(demographic_no) + "\"";
  }
  public void setLastImmDate() {
    lastImmDate = " value=\"" + (new EfmDataOpt()).getLastImmDate(demographic_no) + "\"";
  }
  public void setLastFluDate() {
    lastFluDate = " value=\"" + (new EfmDataOpt()).getLastFluDate(demographic_no) + "\"";
  }
  public void setGuardianName() {
    guardian_name = " value=\"" + (new EfmDataOpt()).getGuardianName(demographic_no) + "\"";
  }
  public void setGuardianNameAddress() {
    guardian_NameAddress = (new EfmDataOpt()).getGuardianNameAddress(demographic_no);
  }
  public void setLabel() {  
    label = (new EfmDataOpt()).getLabel(demographic_no);
  }
  public void setEChartAcc() {  
    String[] temp = (new EfmDataOpt()).getEChartAcc(demographic_no, new String[]{"socialHistory","familyHistory" ,"medicalHistory" ,"ongoingConcerns" ,"reminders"});
    socialFamilyHistory = temp[0]==null?"":temp[0];
    otherMedications = temp[1]==null?"":temp[1] ;
    medicalHistory = temp[2]==null?"":temp[2];
    ongoingConcerns = temp[3]==null?"":temp[3];
    reminders = temp[4]==null?"":temp[4]    ;
            System.out.println("xxxxxxxxxxxmeta is :"+socialFamilyHistory+"  |value is :"+ otherMedications );

  }
  public void setProviderName(){      
      provider_name = " value=\"" + (new EfmDataOpt()).getProviderName(provider_no) + "\"";
  }

  public String[] getMeta() {  
    return meta;
  }
  public String[] getValue() {  
    return value;
  }
  public String[][] getTagSymbol() {  
    return tagSymbol;
  }
  public void createDemoAcc() { 
    Problem_List = getDemoAcc("xml_Problem_List");
    Medication = getDemoAcc("xml_Medication");
    Family_Social_History = getDemoAcc("xml_Family_Social_History");
    Alert = getDemoAcc("xml_Alert");
    Problem_List = Problem_List == null?"":Problem_List ;
    Medication =  Medication == null?"":Medication ;
    Family_Social_History =  Family_Social_History == null?"":Family_Social_History ;
    Alert =  Alert == null?"":Alert ;
  }
  public String getDemoAcc(String tag) { 
    return (new EfmDataOpt()).getXML(demographic_no, tag);
  }
}