<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page  import="java.sql.*, java.util.*, oscar.SxmlMisc" errorPage="errorpage.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />

<%
  java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
%>
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
<meta http-equiv="Cache-Control" content="no-cache" />
<title><bean:message key="admin.providerupdateprovider.title"/></title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
<!--
function setfocus() {
  document.updatearecord.last_name.focus();
  document.updatearecord.last_name.select();
}
//-->
</script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()"  topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="#486ebd"><th><font face="Helvetica" color="#FFFFFF"><bean:message key="admin.providerupdateprovider.description"/></font></th>
  </tr>
</table>

<form method="post" action="admincontrol.jsp" name="updatearecord" >
<%
  ResultSet rs = apptMainBean.queryResults(request.getParameter("keyword"), request.getParameter("dboperation"));
  if(rs==null) {
    out.println("failed");
  } else {
    while (rs.next()) {
%>

<table cellspacing="0" cellpadding="2" width="100%" border="0" datasrc='#xml_list'>

  <tr> 
    <td width="50%" align="right"><bean:message key="admin.provider.formProviderNo"/>: </td>
    <td><% String provider_no = rs.getString("provider_no"); %><%= provider_no %>
          <input type="hidden"  name="provider_no" value="<%= provider_no %>">
          <input type="hidden"  name="dboperation" value="provider_update_record"></td>
  </tr>
  <tr> 
    <td><div align="right"><bean:message key="admin.provider.formLastName"/>: </div></td>
    <td><input type="text"  index="3" name="last_name" value="<%= rs.getString("last_name") %>"></td>
  </tr>
  <tr>
    <td><div align="right"><bean:message key="admin.provider.formFirstName"/>: </div></td>
    <td><input type="text"  index="4" name="first_name" value="<%= rs.getString("first_name") %>"></td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formType"/>: </td>
    <td>
          <% if (vLocale.getCountry().equals("BR")) { %>  
          <select name="provider_type">
            <option value="receptionist"<% if (rs.getString("provider_type").equals("receptionist")) { %>SELECTED<%}%>><bean:message key="admin.provider.formType.optionReceptionist"/></option>
            <option value="doctor"<% if (rs.getString("provider_type").equals("doctor")) { %>SELECTED<%}%>><bean:message key="admin.provider.formType.optionDoctor"/></option>
            <option value="doctor"><bean:message key="admin.provider.formType.optionNurse"/></option>
            <option value="doctor"><bean:message key="admin.provider.formType.optionResident"/></option>
            <option value="admin"<% if (rs.getString("provider_type").equals("admin")) { %>SELECTED<%}%>><bean:message key="admin.provider.formType.optionAdmin"/></option>
            <option value="admin_billing"<% if (rs.getString("provider_type").equals("admin_billing")) { %>SELECTED<%}%>><bean:message key="admin.provider.formType.optionAdminBilling"/></option>
            <option value="billing"<% if (rs.getString("provider_type").equals("billing")) { %>SELECTED<%}%>><bean:message key="admin.provider.formType.optionBilling"/></option>
          </select>
		  <% } else { %>
            <input type="text" name="provider_type" value="<%= rs.getString("provider_type") %>">
          <% } %>
     </td>
  </tr> 
  <tr> 
    <td align="right"><bean:message key="admin.provider.formSpecialty"/>: </td>
    <td><input type="text" name="specialty" value="<%= rs.getString("specialty") %>"></td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formTeam"/>: </td>
    <td><input type="text" name="team" value="<%= rs.getString("team") %>"></td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formSex"/>: </td>
    <td><input type="text" name="sex" value="<%= rs.getString("sex") %>"></td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formDOB"/>: </td>
    <td><input type="text" name="dob" value="<%= rs.getString("dob") %>"></td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formAddress"/>: </td>
    <td><input type="text" name="address" value="<%= rs.getString("address") %>" size="40"></td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formHomePhone"/>: </td>
    <td><input type="text" name="phone" value="<%= rs.getString("phone") %>"><bean:message key="admin.provider.formWorkPhone"/>: 
    <input type="text" name="workphone" value="<%= rs.getString("work_phone") == null?"":(rs.getString("work_phone")) %>"></td></tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formPager"/>: </td>
    <td>
          <input type="text" name="xml_p_pager" value="<%= SxmlMisc.getXmlContent(rs.getString("comments"),"xml_p_pager") %>" datafld='xml_p_pager'>
        </td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formCell"/>: </td>
    <td>
          <input type="text" name="xml_p_cell" value="<%= SxmlMisc.getXmlContent(rs.getString("comments"),"xml_p_cell") %>" datafld='xml_p_cell'>
        </td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formOtherPhone"/>: </td> 
    <td>
          <input type="text" name="xml_p_phone2" value="<%= SxmlMisc.getXmlContent(rs.getString("comments"),"xml_p_phone2") %>" datafld='xml_p_phone2'>
        </td> 
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formFax"/>: </td>
    <td>
          <input type="text" name="xml_p_fax" value="<%= SxmlMisc.getXmlContent(rs.getString("comments"),"xml_p_fax") %>" datafld='xml_p_fax'>
        </td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formOhipNo"/>: </td>
    <td><input type="text" name="ohip_no" value="<%= rs.getString("ohip_no") %>"></td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formRmaNo"/>: </td>
    <td><input type="text" name="rma_no" value="<%= rs.getString("rma_no") %>"></td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formBillingNo"/>: </td>
    <td><input type="text" name="billing_no" value="<%= rs.getString("billing_no") %>"></td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formHsoNo"/>: </td>
    <td><input type="text" name="hso_no" value="<%= rs.getString("hso_no") %>"></td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formStatus"/>: </td>
    <td><input type="text" name="status" value="<%= rs.getString("status") %>"></td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formSpecialtyCode"/>: </td>
    <td><input type="text" name="xml_p_specialty_code" value="<%= SxmlMisc.getXmlContent(rs.getString("comments"),"xml_p_specialty_code") %>" datafld='xml_p_specialty_code'></td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formBillingGroupNo"/>: </td>
    <td><input type="text" name="xml_p_billinggroup_no" value="<%= SxmlMisc.getXmlContent(rs.getString("comments"),"xml_p_billinggroup_no") %>" datafld='xml_p_billinggroup_no'></td>
  </tr>
  <% if (vLocale.getCountry().equals("BR")) { %>  
  <tr>
    <td align="right"><bean:message key="admin.provider.formProviderActivity"/>: </td>
    <td>
      <input type="text" name="provider_activity" value="<%= rs.getString("provider_activity") %>" size="5" maxlength="3">
    </td>
  </tr>
  <% } else { %>
     <input type="hidden" name="provider_activity" value="">
  <% }  %>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formSlpUsername"/>: </td>
    <td>
          <input type="text" name="xml_p_slpusername" value="<%= SxmlMisc.getXmlContent(rs.getString("comments"),"xml_p_slpusername") %>" datafld='xml_p_slpusername'>
        </td>
  </tr>
  <tr> 
    <td align="right"><bean:message key="admin.provider.formSlpPassword"/>: </td>
    <td>
          <input type="text" name="xml_p_slppassword" value="<%= SxmlMisc.getXmlContent(rs.getString("comments"),"xml_p_slppassword") %>" datafld='xml_p_slppassword'>
        </td>
  </tr>
  <tr> 
      <td colspan="2">
          <div align="center"> 
	     <input type="hidden" name="displaymode" value="Provider_Update_Record">
             <input type="submit" name="subbutton" value="<bean:message key="admin.providerupdateprovider.btnSubmit"/>">
          </div>
      </td>
  </tr>
  </table>
<%
  }}
  apptMainBean.closePstmtConn();
%>
  </form>
  
  <p></p>
<%@ include file="footerhtm.jsp" %>
</center>
</body>
</html:html>
