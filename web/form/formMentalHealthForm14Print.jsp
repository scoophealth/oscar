<!-- 
/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
 -->
  
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*, org.oscarehr.util.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.springframework.context.*,org.springframework.web.context.support.*" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
	String formClass = "MentalHealthForm14";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));

	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);
%>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>

<head>
    <title>Mental Health Form14</title>
</head>


<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">

<table width="100%" class="header">
	<tr width="100%">
		<td align="left"><input type="button" value="Exit"
			onclick="window.close();" /> <input type="button" value="Print"
			onclick="window.print()" /></td>
	</tr>
</table>


<table width="100%" height="63" border="0">
  <tr>
    <th width="131" height="59" scope="col"><p>Ministry<br />
      Of<br />
      Health </p></th>
    <th width="243" scope="col">Form14<br />
      Mental Health Act</th>
    <th width="322" scope="col">Application by Physician
      for<br />
      Psychiatric Assessment</th>
  </tr>
</table>
<hr />
<p>&nbsp;</p>
<table width="100%" border="0">
  <tr>
    <td height="47"><p>I, (print full name of person) 
        <u>
          <%=props.getProperty("name","")%>
        </u>
    </p></td>
  </tr>
  <tr>
    <td>of (address) <u>
          <%=props.getProperty("address","")%>
        </u>
    </td>
  </tr>
  <tr>
    <td>hereby consent to the disclosure or transmittal to or the examination by (print name) <u>
          <%=props.getProperty("physicianName","")%>
        </u>
    </td>
  </tr>
  <tr>
    <td>of the clinical record compiled in (name of psychiatric facility) <u>
          <%=props.getProperty("nameOfFacility","")%>
        </u>
    </td>
  </tr>
  <tr>
    <td>in respect of (name of patient) <u>
          <%=props.getProperty("clientName","")%>
        </u>
      
    (date of birth, where available) <u>
        <%=props.getProperty("clientDOB","")%>
      </u>
    </td>
  </tr>
  <tr>
    <td>(witness) <u>
         <%=props.getProperty("witness","")%>
        </u>
    </td>
  </tr>
  <tr>
    <td>(signature) <u>
          <%=props.getProperty("signature","")%>
        </u>
    </td>
  </tr>
  <tr>
    <td>(if other than the patient, state relationship to the patient) <u>
          <%=props.getProperty("relationship","")%>
        </u>
    </td>
  </tr>
  <tr>
    <td>Date (day / month / year) <u>
          <%=props.getProperty("signatureDate","")%>
        </u>
    </td>
  </tr>
</table>
<p>&nbsp;</p>

</body>
</html:html>

