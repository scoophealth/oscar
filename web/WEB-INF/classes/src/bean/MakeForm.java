// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package bean; 
import java.util.*; 


public class MakeForm { 

private String content ; 

public MakeForm() { 
} 
 
 
public String addContent(int demographic_no, String str,int fid) { 

   String temp = new String();
   Utility util = new Utility();
   DBOperator Dbo = new DBOperator();
   
   String Today = util.getToday();
   String Label = Dbo.getLabel(demographic_no);
   String Problem_List = (Dbo.getXML(demographic_no,"<xml_Problem_List>" )!=null)?(Dbo.getXML(demographic_no,"<xml_Problem_List>" )):"";
   String Medication = (Dbo.getXML(demographic_no,"<xml_Medication>" )!=null)?(Dbo.getXML(demographic_no,"<xml_Medication>" )):"";
   String Family_Social_History = (Dbo.getXML(demographic_no,"<xml_Family_Social_History>" )!=null)?(Dbo.getXML(demographic_no,"<xml_Family_Social_History>" )):"";
   String Alert = (Dbo.getXML(demographic_no,"<xml_Alert>" )!=null)?(Dbo.getXML(demographic_no,"<xml_Alert>" )):"";

 
/*
public String RemoveComment(String Content){
String makeContent=new String();
StringTokenizer strToken = new StringTokenizer(Content,"\n");
String tempToken=null;

while(strToken.hasMoreTokens()){
	tempToken=strToken.nextToken();
	if(tempToken.indexOf(":")!=0)
	  makeContent=makeContent+tempToken+"\n";
	  makeContent=makeContent.substring(4);
	  makeContent=makeContent.substring(0,(makeContent.length()-1));
	}
return makeContent;
}
*/
 StringTokenizer strToken = new StringTokenizer(str,"\n");
 String tempToken=null;
 while(strToken.hasMoreTokens()){
       	tempToken = strToken.nextToken();
               if (tempToken.indexOf("action=")>0){
                   temp += tempToken.substring(0,tempToken.indexOf("action=")+7)+ "\"..\\e_form\\SaveMyForm.jsp?demographic_no="+demographic_no+"&fid="+fid+"\">"+"\n";
               }else if (tempToken.indexOf("oscarDB=Today")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Today")+13)+ "  value="+Today + tempToken.substring(tempToken.indexOf("oscarDB=Today")+13)+ "\n";
               }else if (tempToken.indexOf("oscarDB=Label")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Label")+14)+ Label + tempToken.substring(tempToken.indexOf("oscarDB=Label")+14)+ "\n";
               }else if (tempToken.indexOf("oscarDB=Problem_List")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Problem_List")+21)+ Problem_List + tempToken.substring(tempToken.indexOf("oscarDB=Problem_List")+21) + "\n";
               }else if (tempToken.indexOf("oscarDB=Medication")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Medication")+19)+ Medication + tempToken.substring(tempToken.indexOf("oscarDB=Medication")+19) + "\n";
 
               }else if (tempToken.indexOf("oscarDB=Family_Social_History")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Family_Social_History")+30)+ Family_Social_History + tempToken.substring(tempToken.indexOf("oscarDB=Family_Social_History")+30) + "\n";
               }else if (tempToken.indexOf("oscarDB=Alert")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Alert")+14)+ Alert + tempToken.substring(tempToken.indexOf("oscarDB=Alert")+14) + "\n";
 
               }else{
                     temp += tempToken;
               }
 }

/*
              if (str.indexOf("action=")>0){
//******************* action must change depend on the form
                   temp += str.substring(0,str.indexOf("action=")+7)+ "\"..\\WriteConsultataion_request.jsp?demographic_no=<%=demographic_no%>\">"+ "\n";
               }else if (str.indexOf("oscarDB=Today")>0){
                    temp += str.substring(0,str.indexOf("oscarDB=Today")+13)+ "  value="+getToday() + str.substring(str.indexOf("oscarDB=Today")+13,str.length())+ "\n";
               }else if (str.indexOf("oscarDB=Label")>0){
//                    temp += str.substring(0,str.indexOf("oscarDB=Label")+14)+ getLabel(10000025) + str.substring(str.indexOf("oscarDB=Label")+14,str.length())+ "\n";
                    temp += str.substring(0,str.indexOf("oscarDB=Label")+14)+ "<%=beanTextFileOperator.getLabel(demographic_no)%>" + str.substring(str.indexOf("oscarDB=Label")+14,str.length())+ "\n";
//                   temp += str.substring(0,str.indexOf("name=")+5)+ "Label" + str.substring(str.indexOf("name=")+9,str.indexOf("oscarDB=Label")+14)+ getLabel(10000025) + str.substring(str.indexOf("oscarDB=Label")+14,str.length())+ "\n";
               }else if (str.indexOf("oscarDB=Problem_List")>0) {
                    temp += str.substring(0,str.indexOf("oscarDB=Problem_List")+21)+ "<%if (RS.next()){\n out.print (beanFunctionGenerator.getXMLout(RS.getString(\"content\"),\"<xml_Problem_List>\",\"</xml_Problem_List>\"));\n RS.beforeFirst(); \n}\n%>" + str.substring(str.indexOf("oscarDB=Problem_List")+21,str.length()) + "\n";
//                   temp += str.substring(0,str.indexOf("name=")+5)+ "Problem_List" + str.substring(str.indexOf("name=")+9,str.indexOf("oscarDB=Problem_List")+21)+ "<%=beanFunctionGenerator.getXMLout(RS.getString(\"content\"),\"<xml_Problem_List>\",\"</xml_Problem_List>\")%>" + str.substring(str.indexOf("oscarDB=Problem_List")+21,str.length()) + "\n";
               }else if (str.indexOf("oscarDB=Medication")>0) {
                    temp += str.substring(0,str.indexOf("oscarDB=Medication")+19)+ "<% if (RS.next()){\n out.print (beanFunctionGenerator.getXMLout(RS.getString(\"content\"),\"<xml_Medication>\",\"</xml_Medication>\"));\n RS.beforeFirst(); \n}\n%>" + str.substring(str.indexOf("oscarDB=Medication")+19,str.length()) +"\n";
//                   temp += str.substring(0,str.indexOf("name=")+5)+ "Medication" + str.substring(str.indexOf("name=")+9,str.indexOf("oscarDB=Medication")+19)+ "<%=beanFunctionGenerator.getXMLout(RS.getString(\"content\"),\"<xml_Medication>\",\"</xml_Medication>\")%>" + str.substring(str.indexOf("oscarDB=Medication")+19,str.length()) + "\n";
               }else if (str.indexOf("oscarDB=Family_Social_History")>0) {
                    temp += str.substring(0,str.indexOf("oscarDB=Family_Social_History")+30)+ "<%=beanFunctionGenerator.getXMLout(RS.getString(\"content\"),\"<xml_Family_Social_History>\",\"</xml_Family_Social_History>\")%>" + str.substring(str.indexOf("oscarDB=Family_Social_History")+31,str.length()) +"\n";
//                   temp += str.substring(0,str.indexOf("name=")+5)+ "Family_Social_History" + str.substring(str.indexOf("name=")+9,str.indexOf("oscarDB=Medication")+19)+ "<%=beanFunctionGenerator.getXMLout(RS.getString(\"content\"),\"<xml_Medication>\",\"</xml_Medication>\")%>" + str.substring(str.indexOf("oscarDB=Medication")+19,str.length()) + "\n";
               }else if (str.indexOf("oscarDB=Alert")>0) {
                   temp += str.substring(0,str.indexOf("oscarDB=Alert")+14)+ "<%=beanFunctionGenerator.getXMLout(RS.getString(\"content\"),\"<xml_Alert>\",\"</xml_Alert>\")%>" + str.substring(str.indexOf("oscarDB=Alert")+15,str.length()) +"\n";
               }else{
                   temp += (str + System.getProperty("line.separator")); 
               }
 */                  
      return temp; 

}

//public String mixNewForm(String str,String label,String date,String currentproblems,String currentmedications,String familysocialhistory,String alert,String name_01) {
 public String mixNewForm(String str,String label,String date,String currentproblems,String currentmedications,String familysocialhistory,String alert,String subject,String name_01,String name_02,String name_03,String name_04,String name_05,String name_06,String name_07,String name_08,String name_09,String name_10,String name_11,String name_12,String name_13,String name_14,String name_15,String name_16,String name_17,String name_18,String name_19,String name_20) {
//public String mixNewForm(String str, String label,String date,String service,String consultant,String reason,String clinicalinformation,String currentproblems,String currentmedications) { 
 String temp = new String();
 StringTokenizer strToken = new StringTokenizer(str,"\n");
 String tempToken=null;
 while(strToken.hasMoreTokens()){
       	tempToken = strToken.nextToken();
               if (tempToken.indexOf("oscarDB=Today")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Today")+13)+ "  value=\""+date+"\"" + tempToken.substring(tempToken.indexOf("oscarDB=Today")+13)+ "\n";
               }else if (tempToken.indexOf("oscarDB=Label")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Label")+14)+ label + tempToken.substring(tempToken.indexOf("oscarDB=Label")+14)+ "\n";
               }else if (tempToken.indexOf("oscarDB=Problem_List")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Problem_List")+21)+ currentproblems + tempToken.substring(tempToken.indexOf("oscarDB=Problem_List")+21) + "\n";
               }else if (tempToken.indexOf("oscarDB=Medication")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Medication")+19)+ currentmedications + tempToken.substring(tempToken.indexOf("oscarDB=Medication")+19) + "\n";
               }else if (tempToken.indexOf("oscarDB=Family_Social_History")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Family_Social_History")+30)+ familysocialhistory + tempToken.substring(tempToken.indexOf("oscarDB=Family_Social_History")+30) + "\n";
               }else if (tempToken.indexOf("oscarDB=Alert")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Alert")+14)+ alert + tempToken.substring(tempToken.indexOf("oscarDB=Alert")+14) + "\n";
               }else if (tempToken.indexOf("name=subject")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("name=subject")+12)+ "  value=\""+subject+"\" " + tempToken.substring(tempToken.indexOf("name=subject")+12)+ "\n";


               }else if (tempToken.indexOf("name=name_01>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_01>")+13)+ name_01 + tempToken.substring(tempToken.indexOf("name=name_01>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_01>")+12)+ " value=\""+name_01+"\"" + tempToken.substring(tempToken.indexOf("name=name_01>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_02>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_02>")+13)+ name_02 + tempToken.substring(tempToken.indexOf("name=name_02>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_02>")+12)+ " value=\""+name_02+"\"" + tempToken.substring(tempToken.indexOf("name=name_02>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_03>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_03>")+13)+ name_03 + tempToken.substring(tempToken.indexOf("name=name_03>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_03>")+12)+ " value=\""+name_03+"\"" + tempToken.substring(tempToken.indexOf("name=name_03>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_04>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_04>")+13)+ name_04 + tempToken.substring(tempToken.indexOf("name=name_04>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_04>")+12)+ " value=\""+name_04+"\"" + tempToken.substring(tempToken.indexOf("name=name_04>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_05>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_05>")+13)+ name_05 + tempToken.substring(tempToken.indexOf("name=name_05>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_05>")+12)+ " value=\""+name_05+"\"" + tempToken.substring(tempToken.indexOf("name=name_05>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_06>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_06>")+13)+ name_06 + tempToken.substring(tempToken.indexOf("name=name_06>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_06>")+12)+ " value=\""+name_06+"\"" + tempToken.substring(tempToken.indexOf("name=name_06>")+12) + "\n";
                            }
                }else if (tempToken.indexOf("name=name_07>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_07>")+13)+ name_07 + tempToken.substring(tempToken.indexOf("name=name_07>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_07>")+12)+ " value=\""+name_07+"\"" + tempToken.substring(tempToken.indexOf("name=name_07>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_08>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_08>")+13)+ name_08 + tempToken.substring(tempToken.indexOf("name=name_08>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_08>")+12)+ " value=\""+name_08+"\"" + tempToken.substring(tempToken.indexOf("name=name_08>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_09>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_09>")+13)+ name_09 + tempToken.substring(tempToken.indexOf("name=name_09>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_09>")+12)+ " value=\""+name_09+"\"" + tempToken.substring(tempToken.indexOf("name=name_09>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_10>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_10>")+13)+ name_10 + tempToken.substring(tempToken.indexOf("name=name_10>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_10>")+12)+ " value=\""+name_10+"\"" + tempToken.substring(tempToken.indexOf("name=name_10>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_11>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_11>")+13)+ name_11 + tempToken.substring(tempToken.indexOf("name=name_11>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_11>")+12)+ " value=\""+name_11+"\"" + tempToken.substring(tempToken.indexOf("name=name_11>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_12>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_12>")+13)+ name_12 + tempToken.substring(tempToken.indexOf("name=name_12>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_12>")+12)+ " value=\""+name_12+"\"" + tempToken.substring(tempToken.indexOf("name=name_12>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_13>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_13>")+13)+ name_13 + tempToken.substring(tempToken.indexOf("name=name_13>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_13>")+12)+ " value=\""+name_13+"\"" + tempToken.substring(tempToken.indexOf("name=name_13>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_14>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_14>")+13)+ name_14 + tempToken.substring(tempToken.indexOf("name=name_14>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_14>")+12)+ " value=\""+name_14+"\"" + tempToken.substring(tempToken.indexOf("name=name_14>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_15>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_15>")+13)+ name_15 + tempToken.substring(tempToken.indexOf("name=name_15>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_15>")+12)+ " value=\""+name_15+"\"" + tempToken.substring(tempToken.indexOf("name=name_15>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_16>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_16>")+13)+ name_16 + tempToken.substring(tempToken.indexOf("name=name_16>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_16>")+12)+ " value=\""+name_16+"\"" + tempToken.substring(tempToken.indexOf("name=name_16>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_17>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_17>")+13)+ name_17 + tempToken.substring(tempToken.indexOf("name=name_17>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_17>")+12)+ " value=\""+name_17+"\"" + tempToken.substring(tempToken.indexOf("name=name_17>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_18>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_18>")+13)+ name_18 + tempToken.substring(tempToken.indexOf("name=name_18>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_18>")+12)+ " value=\""+name_18+"\"" + tempToken.substring(tempToken.indexOf("name=name_18>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_19>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_19>")+13)+ name_19 + tempToken.substring(tempToken.indexOf("name=name_19>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_19>")+12)+ " value=\""+name_19+"\"" + tempToken.substring(tempToken.indexOf("name=name_19>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_20>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_20>")+13)+ name_20 + tempToken.substring(tempToken.indexOf("name=name_20>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_20>")+12)+ " value=\""+name_20+"\"" + tempToken.substring(tempToken.indexOf("name=name_20>")+12) + "\n";
                            }
             }else{
                     temp += tempToken;
               }
 }
              
      return temp; 
}

 public String EditNewForm(String str,String label,String date,String currentproblems,String currentmedications,String familysocialhistory,String alert,String subject,String name_01,String name_02,String name_03,String name_04,String name_05,String name_06,String name_07,String name_08,String name_09,String name_10,String name_11,String name_12,String name_13,String name_14,String name_15,String name_16,String name_17,String name_18,String name_19,String name_20) {
 String temp = new String();
 StringTokenizer strToken = new StringTokenizer(str,"\n");
 String tempToken=null;
 while(strToken.hasMoreTokens()){
       	tempToken = strToken.nextToken();
               if (tempToken.indexOf("oscarDB=Today")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Today")+13)+ "  value=\""+date+"\"" + tempToken.substring(tempToken.indexOf("oscarDB=Today")+13)+ "\n";
               }else if (tempToken.indexOf("oscarDB=Label")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Label")+14)+ label + tempToken.substring(tempToken.indexOf("oscarDB=Label")+14)+ "\n";
               }else if (tempToken.indexOf("oscarDB=Problem_List")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Problem_List")+21)+ currentproblems + tempToken.substring(tempToken.indexOf("oscarDB=Problem_List")+21) + "\n";
               }else if (tempToken.indexOf("oscarDB=Medication")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Medication")+19)+ currentmedications + tempToken.substring(tempToken.indexOf("oscarDB=Medication")+19) + "\n";
               }else if (tempToken.indexOf("oscarDB=Family_Social_History")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Family_Social_History")+30)+ familysocialhistory + tempToken.substring(tempToken.indexOf("oscarDB=Family_Social_History")+30) + "\n";
               }else if (tempToken.indexOf("oscarDB=Alert")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("oscarDB=Alert")+14)+ alert + tempToken.substring(tempToken.indexOf("oscarDB=Alert")+14) + "\n";
               }else if (tempToken.indexOf("name=subject")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("name=subject")+12)+ "  value=\""+subject+"\" " + tempToken.substring(tempToken.indexOf("name=subject")+12)+ "\n";


               }else if (tempToken.indexOf("name=name_01>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_01>")+13)+ name_01 + tempToken.substring(tempToken.indexOf("name=name_01>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_01>")+12)+ " value=\""+name_01+"\"" + tempToken.substring(tempToken.indexOf("name=name_01>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_02>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_02>")+13)+ name_02 + tempToken.substring(tempToken.indexOf("name=name_02>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_02>")+12)+ " value=\""+name_02+"\"" + tempToken.substring(tempToken.indexOf("name=name_02>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_03>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_03>")+13)+ name_03 + tempToken.substring(tempToken.indexOf("name=name_03>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_03>")+12)+ " value=\""+name_03+"\"" + tempToken.substring(tempToken.indexOf("name=name_03>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_04>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_04>")+13)+ name_04 + tempToken.substring(tempToken.indexOf("name=name_04>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_04>")+12)+ " value=\""+name_04+"\"" + tempToken.substring(tempToken.indexOf("name=name_04>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_05>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_05>")+13)+ name_05 + tempToken.substring(tempToken.indexOf("name=name_05>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_05>")+12)+ " value=\""+name_05+"\"" + tempToken.substring(tempToken.indexOf("name=name_05>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_06>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_06>")+13)+ name_06 + tempToken.substring(tempToken.indexOf("name=name_06>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_06>")+12)+ " value=\""+name_06+"\"" + tempToken.substring(tempToken.indexOf("name=name_06>")+12) + "\n";
                            }
                }else if (tempToken.indexOf("name=name_07>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_07>")+13)+ name_07 + tempToken.substring(tempToken.indexOf("name=name_07>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_07>")+12)+ " value=\""+name_07+"\"" + tempToken.substring(tempToken.indexOf("name=name_07>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_08>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_08>")+13)+ name_08 + tempToken.substring(tempToken.indexOf("name=name_08>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_08>")+12)+ " value=\""+name_08+"\"" + tempToken.substring(tempToken.indexOf("name=name_08>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_09>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_09>")+13)+ name_09 + tempToken.substring(tempToken.indexOf("name=name_09>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_09>")+12)+ " value=\""+name_09+"\"" + tempToken.substring(tempToken.indexOf("name=name_09>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_10>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_10>")+13)+ name_10 + tempToken.substring(tempToken.indexOf("name=name_10>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_10>")+12)+ " value=\""+name_10+"\"" + tempToken.substring(tempToken.indexOf("name=name_10>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_11>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_11>")+13)+ name_11 + tempToken.substring(tempToken.indexOf("name=name_11>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_11>")+12)+ " value=\""+name_11+"\"" + tempToken.substring(tempToken.indexOf("name=name_11>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_12>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_12>")+13)+ name_12 + tempToken.substring(tempToken.indexOf("name=name_12>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_12>")+12)+ " value=\""+name_12+"\"" + tempToken.substring(tempToken.indexOf("name=name_12>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_13>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_13>")+13)+ name_13 + tempToken.substring(tempToken.indexOf("name=name_13>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_13>")+12)+ " value=\""+name_13+"\"" + tempToken.substring(tempToken.indexOf("name=name_13>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_14>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_14>")+13)+ name_14 + tempToken.substring(tempToken.indexOf("name=name_14>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_14>")+12)+ " value=\""+name_14+"\"" + tempToken.substring(tempToken.indexOf("name=name_14>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_15>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_15>")+13)+ name_15 + tempToken.substring(tempToken.indexOf("name=name_15>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_15>")+12)+ " value=\""+name_15+"\"" + tempToken.substring(tempToken.indexOf("name=name_15>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_16>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_16>")+13)+ name_16 + tempToken.substring(tempToken.indexOf("name=name_16>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_16>")+12)+ " value=\""+name_16+"\"" + tempToken.substring(tempToken.indexOf("name=name_16>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_17>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_17>")+13)+ name_17 + tempToken.substring(tempToken.indexOf("name=name_17>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_17>")+12)+ " value=\""+name_17+"\"" + tempToken.substring(tempToken.indexOf("name=name_17>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_18>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_18>")+13)+ name_18 + tempToken.substring(tempToken.indexOf("name=name_18>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_18>")+12)+ " value=\""+name_18+"\"" + tempToken.substring(tempToken.indexOf("name=name_18>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_19>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_19>")+13)+ name_19 + tempToken.substring(tempToken.indexOf("name=name_19>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_19>")+12)+ " value=\""+name_19+"\"" + tempToken.substring(tempToken.indexOf("name=name_19>")+12) + "\n";
                            }
               }else if (tempToken.indexOf("name=name_20>")>0){
                           if(tempToken.indexOf("<textarea")>0){
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_20>")+13)+ name_20 + tempToken.substring(tempToken.indexOf("name=name_20>")+13) + "\n";
                            }else{
                             temp += tempToken.substring(0,tempToken.indexOf("name=name_20>")+12)+ " value=\""+name_20+"\"" + tempToken.substring(tempToken.indexOf("name=name_20>")+12) + "\n";
                            }
             }else{
                     temp += tempToken;
               }
 }

              
      return temp; 

}

public String makePrintTableHead(String Content) { 
 String makeContent = new String();
 String tempString = Content;
/*
    while(tempString.indexOf("<textarea")>=0){  
        String head = ;
        makeContent = new Utility().changeString(makeContent,head,"<table><tr><td>")
        tempString = makeContent;  
    }
*/
    return tempString; 
}
public String makePrintTableTail(String Content) { 
        String makeContent = new Utility().changeString(Content,"</textarea>","</td></tr></table>");
    return makeContent; 
}
/*
public String changeString(String Content,String oldString,String newString){
String makeContent=new String();
String tempString = Content;
	while(tempString.indexOf(oldString)>=0){   
	  makeContent = tempString.substring(0,tempString.indexOf(oldString))+ newString + tempString.substring(tempString.indexOf(oldString)+1);
          tempString = makeContent; 
        }

return tempString;
*/
public String makePrintButton(String str) { 
 String temp = new String();
 StringTokenizer strToken = new StringTokenizer(str,"\n");
 String tempToken=null;
 while(strToken.hasMoreTokens()){
       	tempToken = strToken.nextToken();
               if (tempToken.indexOf("type=\"submit\"")>0){
                    temp +=  "<br><input type=button value=Print onclick=\"javascript:window.print()\">"+ "\n";
               }else{
                     temp += tempToken;
               }
 }
      return temp; 
}

public String makeSubmitHead(int fdid,String form_name,int demographic_no,int status,String form_date,String form_time,String form_provider,String str) { 
 String temp = new String();
 StringTokenizer strToken = new StringTokenizer(str,"\n");
 String tempToken=null;
 while(strToken.hasMoreTokens()){
       	tempToken = strToken.nextToken();
               if (tempToken.indexOf("action=")>0){
                    temp += tempToken.substring(0,tempToken.indexOf("action=")+7)+ "\"EditMyForm.jsp?fdid="+fdid+"&form_name="+form_name+"&demographic_no="+demographic_no+"&status="+status+"&form_date="+form_date+"&form_time="+form_time+"&form_provider="+form_provider+"\"  "+tempToken.substring(tempToken.indexOf("action=")+7)+ "\n";
               }else{
                     temp += tempToken;
               }
 }
      return temp; 
} 
} 
