<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_measurement" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_measurement");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="oscar.oscarEncounter.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.bean.*"%>
<%@ page import="java.util.Vector"%>
<%
    java.text.SimpleDateFormat sf=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>

<%@page import="java.text.SimpleDateFormat"%>
<html:html locale="true">

<head>
<title>
<bean:message key="oscarEncounter.Index.oldMeasurements"/>
</title>
<html:base/>

</head>


<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<script type="text/javascript" language=javascript>
    function popupPage(vheight,vwidth,varpage) { //open a new popup window
      var page = "" + varpage;
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
      var popup=window.open(page, "<bean:message key="oscarEncounter.Index.popupPageWindow"/>", windowprops);
      if (popup != null) {
        if (popup.opener == null) {
          popup.opener = self;
          alert("<bean:message key="oscarEncounter.Index.popupPageAlert"/>");
        }
      }
    }
</script>
<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="window.focus();">
<html:errors/>
<html:form action="/oscarEncounter/oscarMeasurements/DeleteData">
<table>
    <tr>
        <td>
            <table>
                <tr>
                    <th colspan='3'><bean:message key="oscarEncounter.oscarMeasurements.oldmesurementindex"/></th>
                </tr>               
                <tr>                                                       
                    <th align="left" class="Header" style="color:white" width="20">
                        <bean:message key="oscarEncounter.oscarMeasurements.displayHistory.headingType"/>
                    </th>
                    <th align="left" class="Header" style="color:white">
                        Type Description
                    </th>
                    <logic:iterate id="date" name="measurementsDates" type="java.util.Date" indexId = "count" >
                    <th align="left" class="Header" style="width:50px;color:white">
						<%=sf.format(date)%>
                    </th> 
                    </logic:iterate>                                                                       
                    <th align="left" class="Header" style="color:white" width="50">

                    </th>
                 </tr>
                
                <c:forEach items="${sessionScope.measurementsTypes}" var="pair" varStatus="rowCounter">
                <c:choose>
          		<c:when test="${rowCounter.count % 2 == 0}">
            		<c:set var="rowStyle" value="background-color:white"/>
          		</c:when>
         	 	<c:otherwise>
            		<c:set var="rowStyle" value=""/>
          		</c:otherwise>
       			 </c:choose>
                            
                <tr class="data" style='<c:out value="${rowStyle}"/>'>                            
                    <td><c:out value="${pair.key}"/></td>
                    <td><c:out value="${pair.value}"/></td>
                    
                    <c:forEach items="${sessionScope.measurementsDates }" var="date">
                    	<c:set var="map" value="${sessionScope.measurementsData[date]}"/>
                    	<c:set var="cell" value="${map[pair.key]}"/>
                    	<c:if test="${cell!=null }">
                    	<c:set var="unit" value="${cell.measuringInstrc}"/>
                    	<c:set var="comment" value="${cell.comments}"/>
                    	<%
                    	String ucomments=(String)pageContext.getAttribute("comment");
                    	String ucStyle="";
                    	if (ucomments!=null && ucomments.toLowerCase().indexOf("abnormal")>=0){
                    		ucStyle="style='background-color: red; color: white;'";
                    	}
                    	
                    	%>
                    	<td title='<c:out value="${unit}" /> <c:out value="${comment}" />' <%=ucStyle%>>
                    	<c:out value="${cell.dataField}" />
                    	</td>
                    	</c:if>
                    	<c:if test="${cell==null }">
                    	<td></td>
                    	</c:if>
                    </c:forEach>
                                                                                                        
                    <td ><a href="#" name='<bean:message key="oscarEncounter.Index.oldMeasurements"/>' onClick="popupPage(300,800,'SetupDisplayHistory.do?type=<c:out value="${pair.key}"/>'); return false;">more...</a></td>
                </tr>                        
                </c:forEach>
                                   
            </table>
            <table>
                <tr>
                    <td><input type="button" name="Button" value="<bean:message key="global.btnPrint"/>" onClick="window.print()"></td>
                    <td><input type="button" name="Button" value="<bean:message key="global.btnClose"/>" onClick="window.close()"></td>                    
                    <logic:present name="type">
                        <input type="hidden" name="type" value="<bean:write name="type" />"/>
                    </logic:present>
                </tr>
            </table>
        </td>   
    </tr>
</table>
</html:form>
</body>
</html:html>
