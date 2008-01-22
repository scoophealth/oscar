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
 * McMaster Unviersity test2
 * Hamilton 
 * Ontario, Canada 
 */
-->
<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  String demographic_no = request.getParameter("demographic_no"); 
%>  

<%@page  import="oscar.oscarDemographic.data.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<html:html locale="true">

<head><!--I18n-->
<title>
Import Demographic Information 
</title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>

<SCRIPT LANGUAGE="JavaScript">
function displayAndDisable(){
    forms.Submit.disabled = true;
    showHideItem('waitingMessage');
    return true;
}
</SCRIPT>


<style type="text/css">
	table.outline{
	   margin-top:50px;
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	table.grid{
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	td.gridTitles{
		border-bottom: 2pt solid #888888;
		font-weight: bold;
		text-align: center;
	}
        td.gridTitlesWOBottom{
                font-weight: bold;
                text-align: center;
        }
	td.middleGrid{
	   border-left: 1pt solid #888888;	   
	   border-right: 1pt solid #888888;
           text-align: center;
	}	
</style>
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="175" >
      <bean:message key="demographic.demographiceditdemographic.msgPatientDetailRecord"/>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
					Import <!--i18n-->
                        </td>
                        <td  >&nbsp;
							
                        </td>
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  ><bean:message key="global.help" /></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">
            &nbsp;
            </td>
            <td valign="top" class="MainTableRightColumn">
<!--            
            <html:form action="/form/importUpload.do" method="POST" enctype="multipart/form-data" onsubmit="displayAndDisable()">
                        <input type="file" name="importFile" value="">                    
                        <input type="submit" name="Submit" value="Import">
            </html:form>
//-->	    
            <html:form action="/form/importUpload2.do" method="POST" enctype="multipart/form-data" onsubmit="displayAndDisable()">
                        <input type="file" name="importFile" value="">
			<input type="submit" name="Submit" value="Import (CMS spec 2.0)">
            </html:form>

	    <div id="waitingMessage" style="display: none;">
            <h2>This make take several minutes</h2>
            </div>
            
            <% ArrayList list = (ArrayList)  request.getAttribute("warnings");
	       String importLog = (String) request.getAttribute("importlog");
               if ( list != null ) {
	          if (list.size()>0) {
            %>
            <div>
                <h2>Warnings</h2>
                <ul>
		    <% for (int i = 0; i < list.size(); i++){
			  String warn = (String) list.get(i); %>
		       <li><%=warn%></li>
		    <%}%>
                </ul>
		<%}%>
		<% if (importLog!=null) { %>
		Demographic Imported! 
		<html:form action="/form/importLogDownload.do" method="POST">
		    <input type="hidden" name="importlog" value="<%=importLog%>">
		    <input type="submit" name="Submit" value="Download Import Event Log">
		</html:form>
		<% } %>
            </div>            
            <% } %>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
            &nbsp;
            </td>
            <td class="MainTableBottomRowRightColumn" valign="top">
            &nbsp;
            </td>
        </tr>
    </table>
</body>
</html:html>
