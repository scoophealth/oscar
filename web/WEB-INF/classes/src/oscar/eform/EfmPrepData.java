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
  private String today;
  private String label ;
  private String Problem_List ;
  private String Medication ;
  private String Family_Social_History ;
  private String Alert ;
  
  private String socialFamilyHistory  ;
  private String otherMedications   ;
  private String medicalHistory  ;
  private String ongoingConcerns ;
  private String reminders      ;

  private int demographic_no ;
  private int fid ;
  private String form_name ;
  String [] meta ;
  String [] value ;
  String [][] tagSymbol = new String[][] { {"input", ""}, {"textarea",">"}, {"checkbox",""}, {"form",""}   };

  public EfmPrepData(int demographic_no1, int fid1, String form_name1) { 
    demographic_no = demographic_no1 ;
    form_name = form_name1 ;
    fid = fid1 ;
    setPatientName();
    setToday();
    setLabel();
    setEChartAcc();
    createDemoAcc();
    
    meta = new String[] {"oscarDB=patient_name","action=","oscarDB=today","oscarDB=label","oscarDB=Problem_List","oscarDB=Medication","oscarDB=Family_Social_History","oscarDB=Alert","oscarDB=Social_Family_History","oscarDB=Other_Medications_History","oscarDB=Medical_History","oscarDB=OngoingConcerns","oscarDB=Reminders" };
    try {
      value = new String[] {patient_name," \"savemyform.jsp?demographic_no="+demographic_no+"&fid="+fid+"&form_name="+URLEncoder.encode(form_name,"UTF-8")+"\"", today, label, Problem_List, Medication, Family_Social_History,Alert,socialFamilyHistory ,otherMedications ,medicalHistory ,ongoingConcerns ,reminders };
    } catch(Exception ex) {
      System.err.println(" : " + ex.getMessage());
    }
  }

  public EfmPrepData(HttpServletRequest req, String strHTML) { 
    String temp = null;
    ArrayList metaTemp = new ArrayList() ;
    ArrayList valueTemp = new ArrayList() ;
    String[] tagElm = new String[] { "type = text", "textarea","type = checkbox" };
    int i = 0;

	  for (Enumeration e = req.getParameterNames() ; e.hasMoreElements() ;) {
		  temp=e.nextElement().toString();
      String elmTag = isRightParam(strHTML, temp, tagElm) ;
		  if(elmTag!=null ) {
        metaTemp.add(i, temp) ;
        if(elmTag.indexOf("textarea") < 0 && elmTag.indexOf("text") >= 0) valueTemp.add(i, " value=\""+req.getParameter(temp)+"\"" ) ;
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


  public void setPatientName() {  
    patient_name = " value=\"" + (new EfmDataOpt()).getPatientName(demographic_no) + "\"";
  }
  public void setToday() {  
    today = " value=\"" + UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd" ) + "\"";
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