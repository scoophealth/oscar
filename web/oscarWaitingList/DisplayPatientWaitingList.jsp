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
 * Date         Implemented By  Company                 Comments
 * 29-09-2004   Ivy Chan        iConcept Technologies   initial version
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->
 <%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ page language="java" %>
<%@ page import="java.util.*,oscar.oscarReport.pageUtil.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
<html:html locale="true">
<head>
<title>
Waiting List
</title>

</head>

<body class="BodyStyle" vlink="#0000FF" >
<!--  -->    
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                Waiting List
            </td>
            <td class="MainTableTopRowRightColumn" width="400">
                <table class="TopStatusBar" >                 
                    <tr>
                        <td ><logic:present name="demoInfo"><bean:write name="demoInfo"/> years</logic:present></td>                                                
                    </tr>                  
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">   
                <a href="javascript: window.back()"> Back</a>
            </td>
            <td class="MainTableRightColumn">
               <table border=0 cellspacing=4 width=700>
                <tr>
                    <td>
                        <table>                            
                            <tr>
                                <td>               
                                    <tr>
                                        <td align="left" class="Header" width="100">
                                            Waiting List
                                        </td>                                        
                                        <td align="left" class="Header" width="50">
                                            Position
                                        </td>
                                        <td align="left" class="Header" width="100">
                                            Note
                                        </td>
                                        <td align="left" class="Header" width="100">
                                            On the Waiting List Since
                                        </td>
                                     </tr>
                                    <logic:iterate id="waitingListBean" name="patientWaitingList" property="patientWaitingListVector">
                                    <tr class="data">
                                        <td width="100"><bean:write name="waitingListBean" property="waitingList" /></td>
                                        <td width="50"><bean:write name="waitingListBean" property="position" /></td>
                                        <td width="100"><bean:write name="waitingListBean" property="note" /></td>
                                        <td width="100"><bean:write name="waitingListBean" property="onListSince" /></td>                                        
                                    </tr>                        
                                    </logic:iterate>                                    
                                </td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td><input type="button" name="Button" value="<bean:message key="global.btnClose"/>" onClick="window.close()"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">

            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
</body>
</html:html>