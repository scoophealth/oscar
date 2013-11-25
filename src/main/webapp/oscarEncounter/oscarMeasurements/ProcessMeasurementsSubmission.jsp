<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.lang.*,oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.Measurements.msgProcessMeasurementsSubmission" /></title>
<html:base />
</head>

<script language="javascript">
function write2Parent(text){  
    var changed = <bean:write name="parentChanged"/>;    
    
    if( !changed ) {
      //we support pasting into orig encounter and new casemanagement
      if( opener.document.forms["caseManagementEntryForm"] != undefined ) {        
        opener.pasteToEncounterNote(text);
      }
      else if( opener.document.encForm != undefined ) {
        opener.document.encForm.enTextarea.focus();
        opener.document.encForm.enTextarea.value = opener.document.encForm.enTextarea.value + "\n" + text;
        opener.setTimeout("document.encForm.enTextarea.scrollTop=document.encForm.enTextarea.scrollHeight", 0);  // setTimeout is needed to allow browser to realize that text field has been updated         
      }
      
    }
    
    self.window.close();    
 }
</script>

<body topmargin="0" leftmargin="0" vlink="#0000FF">
<html:errors />
<table>
	<tr>
		<td>Processing...</td>
		<script>
            write2Parent("<bean:write name="textOnEncounter"/>");
        </script>
	</tr>
</table>


<%
//clear so values don't repeat after added to note
session.setAttribute("textOnEncounter", null);
%>

</body>
</html:html>
