<%--  
/*
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
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
--%>

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.OscarProperties, oscar.oscarClinic.ClinicData, java.util.*" %>

<html:html locale="true">

<%
    oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil reqFrm;
    reqFrm = new oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil ();
    reqFrm.estRequestFromId((String)request.getAttribute("reqId"));

    if (reqFrm.specPhone == null || reqFrm.specPhone.equals("null")){
        reqFrm.specPhone = new String();
    }
    
    if (reqFrm.specFax == null || reqFrm.specFax.equals("null")){
        reqFrm.specFax = new String();
    }
    if (reqFrm.specAddr == null || reqFrm.specAddr.equals("null")){
        reqFrm.specAddr = new String();
    }

    OscarProperties props = OscarProperties.getInstance();

    ClinicData clinic = new ClinicData();
    
    String strPhones = clinic.getClinicDelimPhone();
    if (strPhones == null) { strPhones = ""; }
    String strFaxes  = clinic.getClinicDelimFax();
    if (strFaxes == null) { strFaxes = ""; }
    Vector vecPhones = new Vector();
    Vector vecFaxes  = new Vector();
    StringTokenizer st = new StringTokenizer(strPhones,"|");
    while (st.hasMoreTokens()) {
         vecPhones.add(st.nextToken());
    }
 
    st = new StringTokenizer(strFaxes,"|");
    while (st.hasMoreTokens()) {
         vecFaxes.add(st.nextToken());
    }
%>
<head>
<html:base/>
<style type="text/css" media="print">
.header {
    display:none;
}
.header INPUT {
    display:none;
}

.header A {
    display:none;
}

</style>

<style type="text/css">

.Header{
    background-color:#BBBBBB;
    padding-top:5px;
    padding-bottom:5px;
    width: 450pt;
    font-size:12pt;
}

.Header INPUT{
    width: 100px;
}

.Header A{
    font-size: 12pt;
}

table.patientInfo{
border: 1pt solid #888888;
}

table.leftPatient{
border-left: 1pt solid #AAAAAA;
}

table.printTable{
width: 450pt;
border: 1pt solid #888888;
font-size: 10pt;
font-family: arial, verdana, tahoma, helvetica, sans serif;
}
td.subTitles{
font-size:12pt;
font-family: arial, verdana, tahoma, helvetica, sans serif;
}

td.fillLine{
border-bottom: 1pt solid #444444;
font-size:10pt;
font-family: arial, verdana, tahoma, helvetica, sans serif;
}
pre.text{
font-size:10pt;
font-family: arial, verdana, tahoma, helvetica, sans serif;
}
td.title4{
font-size:10pt;
font-family: arial, verdana, tahoma, helvetica, sans serif;


}
td.address{
font-size:8pt;
font-family: arial, verdana, tahoma, helvetica, sans serif;

}

</style>

<script type="text/javascript">
var flag = 1;
function PrintWindow()
{
       window.print();
}

function CloseWindow()
{
       window.close();
}

function flipFaxFooter(){

      if (flag == 1 ){
      document.getElementById("faxFooter").innerHTML="<hr><bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgFaxFooterMessage"/>";
      flag = 0;
      }else{
      document.getElementById("faxFooter").innerHTML="";
      flag = 1;
      }
}

function phoneNumSelect() {
    document.getElementById("clinicPhone").innerHTML="Tel: "+document.getElementById("sendersPhone").value;
}

function faxNumSelect() {
    document.getElementById("clinicFax").innerHTML="Fax: "+document.getElementById("sendersFax").value;
}

</script>
<title>
<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.title"/>
</title>
</head>
<body>
<table class="header" >
    <tr>
	<td align="center">
            <input type=button value="<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgFaxFooter"/>" onclick="javascript :flipFaxFooter();"/>
        </td>
        
	<td align="center">
            <input type=button value="<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPrint"/>" onclick="javascript: PrintWindow();"/>
        </td>

        <td align="center">
            <input type=button value="<bean:message key="global.btnClose"/>" onclick="javascript: CloseWindow();"/>
        </td>
        <td align="center">
            P
            <select name="sendersPhone" id="sendersPhone" onChange="phoneNumSelect()">
            <%  for (int i =0; i < vecPhones.size();i++){
                 String te = (String) vecPhones.elementAt(i);
            %>
            <option value="<%=te%>"><%=te%></option>
            <%  }%>
            </select>
        </td>
        <td align="center">
            F
            <select name="sendersFax" id="sendersFax" onChange="faxNumSelect()">
            <%  for (int i =0; i < vecFaxes.size();i++){
                 String te = (String) vecFaxes.elementAt(i);
            %>
            <option value="<%=te%>"><%=te%></option>
            <%  }%>
            </select>
        </td>
    </tr>
</table>
<table class="printTable" name="headerTable">
<!--header-->
<tr>
    <td>      
       <table name="innerTable" border="0">
            <tr>
                <td rowspan=3>
                    &nbsp;&nbsp;  <%-- blank column for spacing --%>
                </td>
                <td rowspan=3>                    
                    <%=props.getProperty("faxLogo", "").equals("")?"":"<img src=\""+props.getProperty("faxLogo", "")+"\">"%>
                </td>
                <td rowspan=3>
                    &nbsp;&nbsp;  <%-- blank column for spacing --%>
                </td>

                <td colspan="2" class="title4">
                    <b><%=clinic.getClinicName()%></b>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="address">
                <%=clinic.getClinicAddress()%>, <%=clinic.getClinicCity()%>, <%=clinic.getClinicProvince()%>  <%=clinic.getClinicPostal()%>
                </td>
            </tr>
            <tr>
                <td class="address" id="clinicPhone">
                    Tel: <%=vecPhones.size()>=1?vecPhones.elementAt(0):clinic.getClinicPhone()%>
                </td>
                <td class="address" id="clinicFax">
                    Fax: <%=vecFaxes.size()>=1?vecFaxes.elementAt(0):clinic.getClinicFax()%>
                </td>
            </tr>
        </table>      
    </td>
</tr>
<tr>
    <td align="center">
       <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgConsReq"/>
       <br>
       <font size="-1">
           <b>
               <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPleaseReplyPart1"/>
               <%=reqFrm.getClinicName()%>
               <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPleaseReplyPart2"/>
           </b>
       </font>
    </td>
</tr>

<tr>
    <td>
        <table border=0 align="center" width="100%" cellspacing="0" class="patientInfo">
            <tr>
                <td valign="top" align="left">
                    <table border=0  >
                        <tr>
                            <td class="subTitles">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgDate"/>:
                            </td>
                            <td class="fillLine">
                                <%=reqFrm.referalDate%>
                            </td>
                        </tr>
                        <tr>
                            <td class="subTitles">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgStatus"/>:
                            </td>
                            <td class="fillLine">
			    <% if (reqFrm.urgency.equals("1")) { %>
			    <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgUrgent"/>
			    <%  }else if(reqFrm.urgency.equals("2")){ %>
			    <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgNUrgent"/>
			    <%    }else if (reqFrm.urgency.equals("3")){ %>
			    <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgReturn"/>
			    <% } %>
                            </td>
                        </tr>
                        <tr>
                            <td class="subTitles">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgService"/>:
                            </td>
                            <td class="fillLine">
                                <%=reqFrm.getServiceName(reqFrm.service) %>
                            </td>
                        </tr>
                        <tr>
                            <td class="subTitles">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgConsultant"/>:
                            </td>
                            <td class="fillLine">
                                <%=reqFrm.getSpecailistsName(reqFrm.specialist) %>
                            </td>
                        </tr>

                        <tr>
                            <td class="subTitles">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPhone"/>:
                            </td>
                            <td class="fillLine">
                                <%=reqFrm.specPhone%>
                            </td>
                        </tr>
                        <tr>
                            <td class="subTitles">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgFax"/>:
                            </td>
                            <td class="fillLine">
                                <%=reqFrm.specFax%>
                            </td>
                        </tr>
                        <tr>
                            <td class="subTitles" >
                                <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAddr"/>:
                            </td>

                            <td class="fillLine" >
                                <%=reqFrm.specAddr%>
                            </td>
                        </tr>



                    </table>
                </td>
                <td valign="top">
                    <table border=0 class="leftPatient">
                        <tr>
                            <td class="subTitles">
                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPat"/>:
                            </td>
                            <td class="fillLine">
                            <%=reqFrm.patientName %>
                            </td>
                        </tr>
                        <tr>
                            <td class="subTitles">
                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAddr"/>:
                            </td>
                            <td class="fillLine">
                                <%=reqFrm.patientAddress %>
                            </td>
                        </tr>
                        <tr>
                            <td class="subTitles">
                               <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPhone"/>
                            </td>
                            <td class="fillLine">
                                <%=reqFrm.patientPhone %>
                            </td>
                        </tr>
                        <tr>
                            <td class="subTitles">
                               <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgBirth"/>:
                            </td>
                            <td class="fillLine">
                                <%=reqFrm.patientDOB %>  (d/m/y)
                            </td>
                        </tr>
                        <tr>
                            <td class="subTitles">
                               <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgCard"/>
                            </td>
                            <td class="fillLine">
                             <%=reqFrm.patientHealthNum %>&nbsp;<%=reqFrm.patientHealthCardVersionCode%>&nbsp;<%=reqFrm.patientHealthCardType%>
                            </td>
                        </tr>
                        <tr>
                            <td class="subTitles">
                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgappDate"/>:
                            </td>
                            <td class="fillLine">
                            <%if (Integer.parseInt(reqFrm.status) > 2 ){%>
                                <%=reqFrm.appointmentDay %>/<%=reqFrm.appointmentMonth %>/<%=reqFrm.appointmentYear %>  (d/m/y)
                            <%}else{%>
				&nbsp;
			    <%}%>
			    </td>
                        </tr>
                        <tr>
                            <td class="subTitles">
                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgTime"/>:
                            </td>

                            <td class="fillLine">
                            <%if (Integer.parseInt(reqFrm.status) > 2 ){%>
                                <%=reqFrm.appointmentHour %>:<%=reqFrm.appointmentMinute %> <%=reqFrm.appointmentPm %>
                            <%}else{%>
				&nbsp;
		            <%}%>
                            </td>
                        </tr>
                        <tr>
                            <td class="subTitles">
                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgChart"/>
                            </td>
                            <td class="fillLine">
                                <%=reqFrm.patientChartNo%>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </td>
</tr>


<tr>
    <td class="subTitles">
        <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgReason"/>:
    </td>
</tr>
<tr>
    <td class="fillLine">
        <%=reqFrm.reasonForConsultation %>
       &nbsp;<br>
    </td>
</tr>

<tr>
    <td class="subTitles">
        <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgClinicalInfom"/>:
    </td>
</tr>
<tr>
    <td class="fillLine">
        <%=divy(reqFrm.clinicalInformation) %>
       &nbsp;<br>
    </td>
</tr>


<tr>
    <td class="subTitles">
        <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgSigProb"/>:
    </td>
</tr>
<tr>
    <td class="fillLine">

        <%=divy(reqFrm.concurrentProblems) %>

       &nbsp;<br>
    </td>
</tr>



<tr>
    <td class="subTitles">
        <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgCurrMed"/>
    </td>
</tr>
<tr>
    <td class="fillLine">
        <%=divy(reqFrm.currentMedications) %>

       &nbsp;<br>
    </td>
</tr>

<tr>
    <td class="subTitles">
        <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAllergies"/>
    </td>
</tr>
<tr>
    <td class="fillLine">
        <%=divy(reqFrm.allergies) %>
       &nbsp;<br>
    </td>
</tr>
<tr>
    <td class="subTitles">
    <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAssociated"/> : <%=reqFrm.getProviderName(reqFrm.providerNo) %>   
       &nbsp;<br>
    </td>
</tr>
<tr>
    <td class="subTitles">
    <%-- Dr. Hunter wants the form to say "Physician" instead of "Family Physician".  This is a quick and dirty hack to make it work.  This
     should really be rewritten more elegantly at some later point in time. --%>    
       <% if (props.getProperty("clinic_no", "").startsWith("1022")) { %>
       Physician
       <% } else { %>
       <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgFamilyDoc"/>
       <% } %>
        : <%=reqFrm.getFamilyDoctor() %>
       &nbsp;<br>
    </td>
</tr>
<tr>
    <td id="faxFooter">
    </td>
</tr>
</table>
</body>
</html:html>
<%!
public String divy (String str){
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(str);
    int j =0;
    int i = 0 ;
    System.out.println("str "+str);
    while (i < stringBuffer.length() ){
        if (stringBuffer.charAt(i) == '\n'){

        stringBuffer.insert(i,"<BR>");


        System.out.println("i = "+stringBuffer.charAt(i)+" i-1 = "+stringBuffer.charAt(i-1)+" i+1 "+stringBuffer.charAt(i+4));
        i = i + 4;
        }


    i++;
    }


return stringBuffer.toString();

}

%>
