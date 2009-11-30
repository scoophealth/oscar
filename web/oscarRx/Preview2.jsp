<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="oscar.oscarProvider.data.*, oscar.log.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils, java.util.Enumeration"%>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="oscar.*,java.lang.*,java.util.Date"%>
<% response.setHeader("Cache-Control","no-cache");%>

<!--
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
-->


<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="RxPreview.title"/></title>
<html:base />

<logic:notPresent name="RxSessionBean" scope="session">
	<logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
	<bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean"
		name="RxSessionBean" scope="session" />
	<logic:equal name="bean" property="valid" value="false">
		<logic:redirect href="error.html" />
	</logic:equal>
</logic:present>

<link rel="stylesheet" type="text/css" href="styles.css">
<style type="text/css" media="print">
                   .noprint{
                       display:none;
                   }
                   .justforprint{
                       float:left;
                   }
               </style>
<script type="text/javascript" language="Javascript">

    function onPrint2(method) {
     
            document.getElementById("preview2Form").action = "../form/createcustomedpdf?__title=Rx&__method=" + method;
            document.getElementById("preview2Form").target="_blank";
            document.getElementById("preview2Form").submit();
       return true;
    }

     function printIframe(){
   window.focus();
   window.print();
}


function printPaste2Parent(){

   try{
      //text = ""****doctor oscardoc********************************************************************************";
      //text = text.substring(0, 82) + "\n";
      text = "";
      //var previewForm = $('preview2Form');

      if (document.all){
         text += document.getElementById('rx_no_newlines').value
      } else {
         text += document.getElementById('rx_no_newlines').value + "\n";
      }
      //text += "**********************************************************************************\n";
      
      //we support pasting into orig encounter and new casemanagement
      if( window.parent.opener.document.forms["caseManagementEntryForm"] != undefined ) {
        window.parent.opener.pasteToEncounterNote(text);
      }
      else if( window.parent.opener.document.encForm != undefined )
        window.parent.opener.document.encForm.enTextarea.value = window.parent.opener.document.encForm.enTextarea.value + text;

   }catch (e){
      alert ("ERROR: could not paste to EMR"+e);
   }
   printIframe();
}


function addNotes(){


    var url = "AddRxComment.jsp";
    var ran_number=Math.round(Math.random()*1000000);
    var comment = encodeURIComponent(document.getElementById('additionalNotes').value);
    var params = "scriptNo=60&comment="+comment+"&rand="+ran_number;  //]
    //new Ajax.Request(url, {method: 'post',parameters:params});
    document.getElementById('additNotes').innerHTML =  document.getElementById('additionalNotes').value;
    document.getElementsByName('additNotes')[0].value=  document.getElementById('additionalNotes').value;
}




</script>

</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF">

<%
//System.out.println("==========================IN Preview2.jsp=======================");

Date rxDate = oscar.oscarRx.util.RxUtil.Today();
//String rePrint = request.getParameter("rePrint");
//String rePrint=(String)request.getAttribute("rePrint");
String rePrint=(String)request.getSession().getAttribute("rePrint");
//System.out.println("rePrint="+rePrint);
//Enumeration en=request.getSession().getAttributeNames();
  //      while(en.hasMoreElements())
    //       System.out.println("session attr :"+en.nextElement());
oscar.oscarRx.pageUtil.RxSessionBean bean;
oscar.oscarRx.data.RxProviderData.Provider provider;
String signingProvider;
if( rePrint != null && rePrint.equalsIgnoreCase("true") ) {   
    bean = (oscar.oscarRx.pageUtil.RxSessionBean)session.getAttribute("tmpBeanRX");
   // System.out.println("stash size "+bean);
    signingProvider = bean.getStashItem(0).getProviderNo();
   // System.out.println("in if, signingProvider="+signingProvider);
    rxDate = bean.getStashItem(0).getRxDate();
 //   System.out.println("RX DATE " + rxDate);
    provider = new oscar.oscarRx.data.RxProviderData().getProvider(signingProvider);
  //  System.out.println("in if, provider no="+provider.getProviderNo());
    session.setAttribute("tmpBeanRX", null);
    String ip = request.getRemoteAddr();
    //System.out.println("in if, ip="+ip);
    //LogAction.addLog((String) session.getAttribute("user"), LogConst.UPDATE, LogConst.CON_PRESCRIPTION, String.valueOf(bean.getDemographicNo()), ip);
}
else {
    bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
    
    //set Date to latest in stash
    Date tmp;
  //  System.out.println("bean.getStashSize()="+bean.getStashSize());
  //  System.out.println("bean.getStashSize()="+bean.getStashSize());
 try{
     System.out.println(bean.getStashItem(0).getAtcCode());
 } catch(Exception e){e.printStackTrace();}
    for( int idx = 0; idx < bean.getStashSize(); ++idx ) {
        tmp = bean.getStashItem(idx).getRxDate();
        //System.out.println("in else, tmp="+tmp);
        if( tmp.after(rxDate) ) { //this is wrong
            rxDate = tmp;
        }
    }
    rePrint = "";    
    signingProvider = bean.getProviderNo();
    //System.out.println("in else , signingProvider="+signingProvider);
    provider = new oscar.oscarRx.data.RxProviderData().getProvider(bean.getProviderNo());
    //System.out.println("in else, provider no="+provider.getProviderNo());
}


oscar.oscarRx.data.RxPatientData.Patient patient = new oscar.oscarRx.data.RxPatientData().getPatient(bean.getDemographicNo());

oscar.oscarRx.data.RxPrescriptionData.Prescription rx = null;
int i;
ProSignatureData sig = new ProSignatureData();
boolean hasSig = sig.hasSignature(signingProvider);
String doctorName = "";
if (hasSig){
   doctorName = sig.getSignature(signingProvider);
   //System.out.println("in if doctorName="+doctorName);
}else{
   doctorName = (provider.getFirstName() + ' ' + provider.getSurname());
   //System.out.println("in else doctorName="+doctorName);
}



doctorName = doctorName.replaceAll("\\d{6}","");
doctorName = doctorName.replaceAll("\\-","");
//System.out.println("doctorName="+doctorName);
OscarProperties props = OscarProperties.getInstance();

String pracNo = provider.getPractitionerNo();
//System.out.println("pracNo="+pracNo);
String strUser = (String)session.getAttribute("user");
//System.out.println("strUser="+strUser);
ProviderData user = new ProviderData(strUser);
//System.out.println("user="+user);

//System.out.println("==========================done first java part Preview2.jsp=======================");
%>
<html:form action="/form/formname" styleId="preview2Form">

	<table id="pwTable" width="400px" height="500px" cellspacing=0 cellpadding=10
		border=2>
		<tr>
			<td valign=top height="100px"><input type="image"
				src="img/rx.gif" border="0" value="submit" alt="[Submit]"
				name="submit" title="Print in a half letter size paper"
				onclick="<%=rePrint.equalsIgnoreCase("true") ? "javascript:return onPrint2('rePrint');" : "javascript:return onPrint2('print');" %>"
                                >
			<!--input type="hidden" name="printPageSize" value="PageSize.A6" /-->
                        <% 	String clinicTitle = provider.getClinicName().replaceAll("\\(\\d{6}\\)","") + "<br>" ;
			 	clinicTitle += provider.getClinicAddress() + "<br>" ;
			 	clinicTitle += provider.getClinicCity() + "   " + provider.getClinicPostal()  ;
			%> <input type="hidden" name="doctorName"
				value="<%= StringEscapeUtils.escapeHtml(doctorName) %>" /> <c:choose>
				<c:when test="${empty infirmaryView_programAddress}">
					<input type="hidden" name="clinicName"
						value="<%= StringEscapeUtils.escapeHtml(clinicTitle.replaceAll("(<br>)","\\\n")) %>" />
					<input type="hidden" name="clinicPhone"
						value="<%= StringEscapeUtils.escapeHtml(provider.getClinicPhone()) %>" />
					<input type="hidden" name="clinicFax"
						value="<%= StringEscapeUtils.escapeHtml(provider.getClinicFax()) %>" />
				</c:when>
				<c:otherwise>
					<input type="hidden" name="clinicName"
						value="<c:out value="${infirmaryView_programAddress}"/>" />
					<input type="hidden" name="clinicPhone"
						value="<c:out value="${infirmaryView_programTel}"/>" />
					<input type="hidden" name="clinicFax"
						value="<c:out value="${infirmaryView_programFax}"/>" />
				</c:otherwise>
			</c:choose> <input type="hidden" name="patientName"
				value="<%= StringEscapeUtils.escapeHtml(patient.getFirstName())+ " " +StringEscapeUtils.escapeHtml(patient.getSurname()) %>" />
			<input type="hidden" name="patientAddress"
				value="<%= StringEscapeUtils.escapeHtml(patient.getAddress()) %>" />
			<input type="hidden" name="patientCityPostal"
				value="<%= StringEscapeUtils.escapeHtml(patient.getCity())+ " " + StringEscapeUtils.escapeHtml(patient.getPostal())%>" />
			<input type="hidden" name="patientPhone"
				value="<bean:message key="RxPreview.msgTel"/><%=StringEscapeUtils.escapeHtml(patient.getPhone()) %>" />

			<input type="hidden" name="rxDate"
				value="<%= StringEscapeUtils.escapeHtml(oscar.oscarRx.util.RxUtil.DateToString(rxDate, "MMMM d, yyyy")) %>" />
			<input type="hidden" name="sigDoctorName"
				value="<%= StringEscapeUtils.escapeHtml(doctorName) %>" /> <!--img src="img/rx.gif" border="0"-->
			</td>
			<td valign=top height="100px" id="clinicAddress"><b><%=doctorName %></b><br>
			<c:choose>
				<c:when test="${empty infirmaryView_programAddress}">
					<%= provider.getClinicName().replaceAll("\\(\\d{6}\\)","") %><br>
					<%= provider.getClinicAddress() %><br>
					<%= provider.getClinicCity() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        		    <%= provider.getClinicPostal() %><br>
		            <bean:message key="RxPreview.msgTel"/>: <%= provider.getClinicPhone() %><br>
        		    <bean:message key="RxPreview.msgFax"/>: <%= provider.getClinicFax() %><br>
				</c:when>
				<c:otherwise>
					<c:out value="${infirmaryView_programAddress}" escapeXml="false" />
					<br />
        			<bean:message key="RxPreview.msgTel"/>: <c:out value="${infirmaryView_programTel}" />
					<br />
        			<bean:message key="RxPreview.msgFax"/>: <c:out value="${infirmaryView_programFax}" />
				</c:otherwise>
			</c:choose></td>
		</tr>
		<tr>
			<td colspan=2 valign=top height="75px">
			<table width=100% cellspacing=0 cellpadding=0>
				<tr>
					<td align=left valign=top><br>
					<%= patient.getFirstName() %> <%= patient.getSurname() %><br>
					<%= patient.getAddress() %><br>
					<%= patient.getCity() %> <%= patient.getPostal() %><br>
					<%= patient.getPhone() %><br>
					<b> <% if(!props.getProperty("showRxHin", "").equals("false")) { %>
					<bean:message key="oscar.oscarRx.hin" /><%= patient.getHin() %> <% } %>
					</b></td>
					<td align=right valign=top><b> <%= oscar.oscarRx.util.RxUtil.DateToString(rxDate, "MMMM d, yyyy",request.getLocale()) %>
					</b></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td colspan=2 valign=top height="275px">
			<table height=100% width=100%>
				<tr valign=top>
					<td colspan=2 height=225px>
					<%
                        String strRx = "";
                        StringBuffer strRxNoNewLines = new StringBuffer();
                        for(i=0;i<bean.getStashSize();i++)
                        {
                        rx = bean.getStashItem(i);
						String fullOutLine=rx.getFullOutLine().replaceAll(";","<br />");
						
						if (fullOutLine==null || fullOutLine.length()<=6)
						{
							Logger.getLogger("preview_jsp").error("drug full outline was null");
							fullOutLine="<span style=\"color:red;font-size:16;font-weight:bold\">An error occurred, please write a new prescription.</span><br />"+fullOutLine;
						}
                        %>
                        <%=fullOutLine%>
					<hr>
					<%
                        strRx += rx.getFullOutLine() + ";;";
                        strRxNoNewLines.append(rx.getFullOutLine().replaceAll(";"," ")+ "\n");
                        }
                        %> <input type="hidden" name="rx"
						value="<%= StringEscapeUtils.escapeHtml(strRx.replaceAll(";","\\\n")) %>" />
					<input type="hidden" name="rx_no_newlines" id="rx_no_newlines"
						value="<%= strRxNoNewLines.toString() %>" />
                                        </td>
				</tr>
                                
                                <tr valign="bottom">
					<td colspan="2" id="additNotes"></td>
                                        <input type="hidden" name="additNotes" value="">
                                </tr>
                                
		
				<% if ( oscar.OscarProperties.getInstance().getProperty("RX_FOOTER") != null ){ out.write(oscar.OscarProperties.getInstance().getProperty("RX_FOOTER")); }%>


				<tr valign=bottom>
					<td height=25px width=25%><bean:message key="RxPreview.msgSignature"/>:</td>
					<td height=25px width=75%
						style="border-width: 0; border-bottom-width: 1; border-style: solid;">
					&nbsp;</td>
				</tr>
				<tr valign=bottom>
					<td height=25px></td>
					<td height=25px>&nbsp; <%= doctorName%> <% if ( pracNo == null || pracNo.equals("") ) { %>
					<% } else { %> Pract. No. <%= pracNo%> <% } %>
					</td>



				</tr>
				<% if( rePrint.equalsIgnoreCase("true") && rx != null ) { %>
				<tr valign=bottom style="font-size: 6px;">
                                    <td height=25px colspan="2"><bean:message key="RxPreview.msgReprintBy"/> <%=user.getProviderName(strUser)%><span style="float: left;">
					<bean:message key="RxPreview.msgOrigPrinted"/>:&nbsp;<%=rx.getPrintDate()%></span> <span
						style="float: right;"><bean:message key="RxPreview.msgTimesPrinted"/>:&nbsp;<%=String.valueOf(rx.getNumPrints())%></span>
					</td>                                        
					<input type="hidden" name="origPrintDate"
						value="<%=rx.getPrintDate()%>">
					<input type="hidden" name="numPrints"
						value="<%=String.valueOf(rx.getNumPrints())%>">
				</tr>

				<%
                   }
                 if (oscar.OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") != null){%>
				<tr valign=bottom align="center" style="font-size: 9px">
					<td height=25px colspan="2"></br>
					<%= oscar.OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") %>
					</td>
				</tr>
				<%}%>
			</table>
			</td>
		</tr>                
	</table>
        <table border="0">
            <tr>
                <th>Size of Print PDF :</th>
                <th><select name="printPageSize">
                        <option value="PageSize.A4">A4 page</option>
                        <option value="PageSize.A6">A6 page</option>                        
                    </select>
                </th>
            </tr>
        </table> 
        
</html:form>
<div align="center" class="noprint">
<button onclick="<%=rePrint.equalsIgnoreCase("true") ? "javascript:return onPrint2('rePrint');" : "javascript:return onPrint2('print');" %>">Print PDF</button>
<button onclick="window.print();" >Print</button>
<button onclick="printPaste2Parent();">Print and paste</button>
<button onclick="parent.window.close()">Back to Oscar</button>
<br>
<textarea id="additionalNotes" style="width: 200px" onchange="javascript:addNotes();" ></textarea>
<input type="button" value="Add to Rx" onclick="javascript:addNotes();"/>



</div>
</body>
</html:html>
