
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

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.oscarReport.pageUtil.*"%>
<%@ page import="java.util.*, java.sql.*, java.text.*, java.net.*;"%>
<%
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy   

    GregorianCalendar now=new GregorianCalendar(); 
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);
    String today = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DATE) ;
    //String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
%>

<html:html locale="true">

<head>
<title>
<bean:write name="title"/>
</title>
<html:base/>
<style type="text/css">
   td.nameBox {
      border-bottom: 1pt solid #888888;
      font-family: tahoma, helvetica; ;
      font-size: 12pt;
   }
   td.sideLine {
      border-right: 1pt solid #888888;
   }
   td.fieldBox {
      font-family: tahoma, helvetica;
   }
   th.subTitles{
      font-family: tahoma, helvetica ;
      font-size:10pt;
   }
</style>
</head>
<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="window.focus();">
<html:errors/>
<table  class="MainTable" id="scrollNumber1" name="encounterTable">
    <tr class="MainTableTopRow">
        <td class="MainTableTopRowLeftColumn">
            <bean:message key="oscarReport.CDMReport.msgReport"/>
        </td>
        <td class="MainTableTopRowRightColumn">
            <table class="TopStatusBar" >             
                <tr>
                    <td ><bean:message key="oscarReport.CDMReport.msgTitle"/>: <bean:write name="CDMGroup"/>  </td>
                    <td></td>
                    <td style="text-align:right">
                            <a href="javascript:popupStart(300,400,'Help.jsp')"  ><bean:message key="global.help" /></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                    </td>
                </tr>              
            </table>
        </td>
    </tr>
    <tr>
        <td class="MainTableLeftColumn">
         &nbsp;
        </td>
        <td class="MainTableRightColumn">
            <table border=0 cellspacing=4 width=900>                
                <tr>
                    <td>
                        <table>
                            <tr>
                                <td>                              
                                    <tr>
                                        <th align="left" class="subTitles" width="600">
                                            <bean:write name="title"/>
                                        </th>
                                    </tr>                                   
                                    <logic:present name="messages">
                                    <logic:iterate id="msg" name="messages">
                                    <tr>
                                        <td><bean:write name="msg"/></td>
                                    </tr>   
                                    </logic:iterate>
                                    </logic:present>                                                                                   
                                </td>
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
                             
                                   
                                
