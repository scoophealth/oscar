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
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
 
<c:set var="ctx" value="${ pageContext.servletContext.contextPath }" />
<c:set var="url" value="${ ctx }/demographic/demographiccontrol.jsp?demographic_no=${ param.demographicNo }&displaymode=edit&dboperation=search_detail&appointment=" />

<table id="${ not empty param.tableId ? param.tableId : 'topLink' }" >
<tr>
	<td id="topLinkLeftColumn"> 
		<h1><c:out value="${ param.title }" /></h1> 	
	</td>
		
		<td id="topLinkCenterColumn" >

		<c:if test="${ not empty param.patientName }" >
	        <a href="#" onClick="popupPage(700,1000,'${ param.title }','${ url }'); return false;" 
	        	title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>" > 
	        	<c:out value="${ param.patientName }" />
	        </a> 
        </c:if>
        
        <c:if test="${ not empty param.sex }" >
	        <span class="label">
	        	sex
	        </span>	
	        <span>
	        	${ param.sex }
	        </span>   
        </c:if>
        
         <c:if test="${ not empty param.age }" >
	        <span class="label">
	        	age
	        </span>	
	        <span>
	        	${ param.age }
	        </span>	
        </c:if>
        
        <c:if test="${ not empty param.phone }" >
	        <span class="label">
	        	home
	        </span>	
	        <span>
	        	${ param.phone }
	        </span>
        </c:if>
        
        <c:if test="${ not empty param.mrp }" >
		<security:oscarSec roleName="${ security }" objectName="_newCasemgmt.doctorName" rights="r">
	    	<span class="label">	
	    		  <bean:message key="oscarEncounter.Index.msgMRP"/>  			   
		    </span>
		    <span>
		     	<c:out value="${ param.mrp }" />
		    </span>
		</security:oscarSec>
		</c:if>	
		</td>

	<td id="topLinkRightColumn">
	 		<span class="HelpAboutLogout" style="color:white;">
                 <oscar:help keywords="2.2.4" key="app.top1" style="color:white;" />
                 <a style="color:white;" href="${ ctx }/oscarEncounter/About.jsp" target="_new">About</a>
             </span>
	</td>
</tr>
</table>