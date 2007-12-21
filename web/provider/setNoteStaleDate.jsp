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

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ include file="/casemgmt/taglibs.jsp" %>

<%
if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  String curUser_no;
  curUser_no = (String) session.getAttribute("user");

  boolean bFirstLoad = request.getAttribute("status") == null;    

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<html:html>
    <head>
        <html:base/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="provider.setNoteStaleDate.title"/></title>
    
        <link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
          <!-- calendar stylesheet -->
        <link rel="stylesheet" type="text/css" media="all" href="<c:out value="${ctx}"/>/share/calendar/calendar.css" title="win2k-cold-1">
        
        <script src="<c:out value="${ctx}"/>/share/javascript/prototype.js" type="text/javascript"></script>
        <script src="<c:out value="${ctx}"/>/share/javascript/scriptaculous.js" type="text/javascript"></script> 

          <!-- main calendar program -->
        <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/calendar.js"></script>

          <!-- language for the calendar -->
        <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

          <!-- the following script defines the Calendar.setup helper function, which makes
               adding a calendar a matter of 1 or 2 lines of code. -->
        <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/calendar-setup.js"></script>
        <script type="text/javascript">            
            function setup() {
                Calendar.setup({ inputField : "staleDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "staleDate_cal", singleClick : true, step : 1 });
            }
            
            function validate() {
                var date = document.getElementById("staleDate");
                if( date.value == "" ) {
                    alert("Please select a date before saving");
                    return false;
                }
                
                return true;
            }
        </script>

    </head>
        
    <body class="BodyStyle" vlink="#0000FF" <%=bFirstLoad?"onload='setup()'":""%> >

    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <bean:message key="provider.setNoteStaleDate.msgPrefs"/>
            </td>
            <td style="color:white" class="MainTableTopRowRightColumn">
                <bean:message key="provider.setNoteStaleDate.msgProviderStaleDate"/>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">
               &nbsp;
            </td>
            <td class="MainTableRightColumn">                       
            <%
               if( request.getAttribute("status") == null )
               {
                
            %>
            <html:form action = "/setProviderStaleDate.do" >
               <input type="hidden" name="method" value="save"> 
               <html:hidden property="dateProperty.name"/>
               <html:hidden property="dateProperty.provider_no"/>
               <html:hidden property="dateProperty.id"/>
               <bean:message key="provider.setNoteStaleDate.msgEdit"/>
               <img src="<c:out value="${ctx}/images/cal.gif" />" id="staleDate_cal" alt="calendar"><html:text property="dateProperty.value" styleId="staleDate" ondblclick="this.value='';" readonly="true" size="10" />
               <input type="submit" onclick="return validate();" value="<bean:message key="provider.setNoteStaleDate.btnSubmit"/>" />                           
            </html:form> 
            <%
               }
               else {
            %>                            
                <bean:message key="provider.setNoteStaleDate.msgSuccess"/> <br>
                
            <%
               }
            %>
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
