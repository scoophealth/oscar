<%--  
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
--%>

<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="oscar.oscarEncounter.data.*,java.net.*,oscar.oscarPrevention.*,java.util.*"%>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />

<html:html>

<head>
<title> Cumulative Patient Profile</title>
<link rel="stylesheet" type="text/css" media="print" href="print.css"/>
<link rel="stylesheet" type="text/css" href="encounterPrintStyles.css"/>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<%
    response.setHeader("Cache-Control","no-cache");
    //The oscarEncounter session manager, if the session bean is not in the context it looks for a session cookie with the appropriate name and value, if the required cookie is not available
    //it dumps you out to an erros page.

  oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
  if((bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean"))==null) {
    response.sendRedirect("error.jsp");
    return;
  }
  
 
    oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
    oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = {};
    arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(bean.demographicNo));

    oscar.oscarRx.data.RxPatientData.Patient.Allergy[] allergies
    = new oscar.oscarRx.data.RxPatientData().getPatient(Integer.parseInt(bean.demographicNo)).getAllergies();
    
    PreventionData pd = new PreventionData();
    ArrayList prevList2 = pd.getPreventionData(bean.demographicNo);
    
    ArrayList inject = new ArrayList();
      
      PreventionDisplayConfig pdc = new PreventionDisplayConfig();         
      ArrayList prevList  = pdc.getPreventions();
      System.out.println("size"+prevList.size());
      for (int k =0 ; k < prevList.size(); k++){
             Hashtable a = (Hashtable) prevList.get(k);   
             System.out.println("layout ="+a.get("layout")+"<");
             if (a != null && a.get("layout") != null &&  a.get("layout").equals("injection")){
                inject.add((String) a.get("name"));
                System.out.println("added "+a.get("name")+"<");
             }	     	
      }
%>            

<style type="text/css">
pre {
font-size:7.5pt;
margin-top:2pt;

}

ul{
    padding-left:5pt;
}
li{
    list-style-type: none;
    font-size:8pt;
    padding-right: 15px;
}

a.hideShow{
    font-size:xx-small;    
}
</style>

<script type="text/javascript" language="Javascript">
    function onPrint() {
        window.print();
        return true;
    }
    function onClose() {
        window.close();
        return true;
    }
</script>
</head>


<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="window.focus()">
<html:errors/>
<div style="width:7in">

    <div class="Header" >    
        <input type="button" value="<bean:message key="global.btnPrint"/>" onclick="javascript:return onPrint();" style="margin-left:2px;" />    
        <input type="button" value="<bean:message key="global.btnClose"/>" onclick="javascript:return onClose();"/>    
    </div>

    <div style="width:100%;">
        <div style="width:100%;" >            
            <span style="float:left; font-weight:bold; margin-top:4pt;"><%=bean.patientLastName %>, <%=bean.patientFirstName%> <%=bean.patientSex%> <%=bean.patientAge%></span>                
            <span style="float:right; font-weight:bold; margin-top:4pt;"><bean:message key="oscarEncounter.encounterPrint.msgDr"/>. <%=providerBean.getProperty(bean.familyDoctorNo)%></span>            
        </div>

        <div style="width:100%;clear:left;"/>
            
                <div style="float:left; margin-left:3pt;" id="socialFam">
                    <div class="RowTop"><bean:message key="oscarEncounter.Index.socialFamHist"/>: 
                    <a class="hideShow" onclick="javascript:showHideItem('socialFam')" href="javascript: function myFunction() {return false; }" >hide</a>                                          
                    </div>
                    <div class="TableWithBorder" style="max-height:100%;">
                        <pre name='shTextarea' ><%=bean.socialHistory%>&nbsp;</pre>
                    </div>
                </div>

                <div style=" float:left; margin-left:3pt;" id="otherMed">
                    <div class="RowTop"><bean:message key="oscarEncounter.Index.otherMed"/>:
                    <a class="hideShow" onclick="javascript:showHideItem('otherMed')" href="javascript: function myFunction() {return false; }" >hide</a>                                          
                    </div>
                    <div class="TableWithBorder">
                        <pre name='fhTextarea' ><%=bean.familyHistory%>&nbsp;</pre>
                    </div>
                </div>

                <div style="  float:left; margin-left:3pt;" id="medHist">
                    <div class="RowTop"><bean:message key="oscarEncounter.Index.medHist"/>:
                    <a class="hideShow" onclick="javascript:showHideItem('medHist')" href="javascript: function myFunction() {return false; }" >hide</a>                                          
                    </div>
                    <div class="TableWithBorder">
                        <pre name='mhTextarea' ><%=bean.medicalHistory%>&nbsp;</pre>
                    </div>
                </div>
            
        <!--/div>

        <div style="width:100%;padding-left:3px;clear:left;"/-->
            
                <div style="float:left;margin-left:3pt;" id="ongoingCon">
                    <div class="RowTop"><bean:message key="oscarEncounter.encounterPrint.msgOngCon"/>:
                    <a class="hideShow" onclick="javascript:showHideItem('ongoingCon')" href="javascript: function myFunction() {return false; }" >hide</a>                                          
                    </div>
                    <div class="TableWithBorder">
                        <pre name='ocTextarea' ><%=bean.ongoingConcerns%>&nbsp;</pre>
                    </div>
                </div>
                <div style="float:left;margin-left:15pt;" id="reminder">
                    <div class="RowTop"><bean:message key="oscarEncounter.encounterPrint.msgReminders"/>:
                    <a class="hideShow" onclick="javascript:showHideItem('reminder')" href="javascript: function myFunction() {return false; }" >hide</a>                                          
                    </div>
                    <div class="TableWithBorder">
                        <pre name='reTextarea' ><%=bean.reminders%>&nbsp;</pre>
                    </div>
                </div>                        
            
        </div>
        
        <div style="clear:left;">       
            <div class="RowTop">Medications
            <a class="hideShow" onclick="javascript:showHideItem('presBox')" href="javascript: function myFunction() {return false; }" >hide</a>                                          
            </div>            
            <div class="presBox" id="presBox">            
                <ul>
                    <%for (int i = 0; i < arr.length; i++){
                        String rxD = arr[i].getRxDate().toString();                        
                        String rxP = arr[i].getFullOutLine().replaceAll(";"," ");
                        rxP = rxP + "   " + arr[i].getEndDate();
                        String styleColor = "";
                        if(arr[i].isCurrent() == true){  styleColor="color:red;";  }
                    %>
                        <li  style="border-bottom: 1pt solid #888888; font-size:8pt;margin-top:1px; <%=styleColor%>" id="pres<%=i%>">
                            <a class="hideShow" onclick="javascript:showHideItem('pres<%=i%>')" href="javascript: function myFunction() {return false; }" >hide</a>                                          
                            <%=rxD%>&nbsp;
                            <%=rxP%>
                        </li>
                    <%}%>
                </ul>            
            </div>
        </div>
        <div style="clear:left;">       
            <div class="RowTop">Allergies
            <a class="hideShow" onclick="javascript:showHideItem('allergyBox')" href="javascript: function myFunction() {return false; }" >hide</a>                                          
            </div>
            <div class="presBox" id="allergyBox">
                <ul>
                    <%for (int j=0; j<allergies.length; j++){%>              
                    <li id="allergy<%=j%>">                    
                               <a class="hideShow" onclick="javascript:showHideItem('allergy<%=j%>')" href="javascript: function myFunction() {return false; }" >hide</a>                                          
                    <b><%= allergies[j].getAllergy().getDESCRIPTION() %></b>
                    <!--%= allergies[j].getAllergy().getTypeDesc() %-->   
                    &nbsp;Severity: <%= allergies[j].getAllergy().getSeverityOfReactionDesc() %>                       
                    Onset: <%= allergies[j].getAllergy().getOnSetOfReactionDesc() %>   
                    Reaction: <%= allergies[j].getAllergy().getReaction() %>   
                                                                                               
                                        
                                                                           
                                        
                    </li>                                        
                    <%}%>                                    
                </ul>
            </div>
        </div>
        
        <div style="clear:left;" id="imms">       
        <div class="RowTop">Immunizations
        <a class="hideShow" onclick="javascript:showHideItem('imms')" href="javascript: function myFunction() {return false; }" >hide</a>                                          
        </div>        
            <ul>
            <%for (int i = 0; i < inject.size(); i++ ){                 
                 ArrayList list = pd.getPreventionData((String) inject.get(i),bean.demographicNo);                  
                 if ( list.size() > 0 ){%>
                 <li id="imm<%=i%>"> 
                            <a class="hideShow" onclick="javascript:showHideItem('imm<%=i%>')" href="javascript: function myFunction() {return false; }" >hide</a>                                          
                 <b><%=(String) inject.get(i)%></b>
                 
                    <%for (int k =0 ; k < list.size(); k++){
                        Hashtable a = (Hashtable) list.get(k);              
                            if (a != null && inject.contains((String) a.get("type")) ){%>                            
                                 (<%=completeRefused(a.get("refused"))%>: <%=(String) a.get("prevention_date")%>)
                                
                          <%}                      
                       }
                    %>
                                        
                 </li>
                 <%}%>
            <%}%>
              </ul>        
        </div>

    </div> <!--printing div-->
                    
    <div style="clear:left;">&nbsp;</div>
<div class="Header" style="width:100%;">    
    <input type="button" value="<bean:message key="global.btnPrint"/>" onclick="javascript:return onPrint();" style="margin-left:2px;"/>
    <input type="button" value="<bean:message key="global.btnClose"/>" onclick="javascript:return onClose();"/>    
</div>
</div>
</body>
</html:html>

<%!
    String completeRefused(Object s){
        String ret = "Competed";
        String st = "";
        if ( s instanceof String){
             st = (String) s;
        }        
        if ( ret != null && st.equalsIgnoreCase("1")){
            ret = "Refused";
        }
        return ret;
    }
%>