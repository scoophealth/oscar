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
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.PMmodule.model.VacancyTemplate"%>
<%@page import="org.oscarehr.PMmodule.model.Criteria"%>
<%@page import="org.oscarehr.PMmodule.model.CriteriaType"%>
<%@page import="org.oscarehr.PMmodule.model.CriteriaTypeOption"%>
<%@page import="org.oscarehr.PMmodule.service.VacancyTemplateManager"%>
<%@page import="org.oscarehr.PMmodule.dao.CriteriaTypeDao"%>
<%@page import="org.oscarehr.PMmodule.dao.CriteriaTypeOptionDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%
	CriteriaTypeDao criteriaTypeDAO = SpringUtils.getBean(CriteriaTypeDao.class);
	CriteriaTypeOptionDao criteriaTypeOptionDAO = SpringUtils.getBean(CriteriaTypeOptionDao.class);
	String min="", max="", nameMax="", nameMin="";	
	
	String optionValueSelected = request.getParameter("optionValueSelected");
	String typeSelected = request.getParameter("typeSelected"); //Age Category	
	if(!StringUtils.isBlank(optionValueSelected)) {		
		CriteriaTypeOption cto = criteriaTypeOptionDAO.getByValue(optionValueSelected);
		if(cto!=null) {
			min = String.valueOf(cto.getRangeStartValue());
			max = String.valueOf(cto.getRangeEndValue());
		}
	}
	nameMax=typeSelected.toLowerCase().replaceAll(" ","_").concat("Maximum");
	nameMin=typeSelected.toLowerCase().replaceAll(" ","_").concat("Minimum");
%>

<div id="block_vacancyType_<%=typeSelected.toLowerCase().replaceAll(" ","_")%>">
	<table>
	<tr class="b">
	<td class="beright"><%=typeSelected%> Range Minimum:</td>
	<td><input type="text" size="50" maxlength="50" value="<%=min %>" name="<%=nameMin%>">
	</td>
	</tr>
	<tr class="b">
	<td class="beright"><%=typeSelected%> Range Maximum:</td>
	<td><input type="text" size="50" maxlength="50" value="<%=max %>" name="<%=nameMax%>">
	</td>
	</tr>
	</table>			 
	
</div>
