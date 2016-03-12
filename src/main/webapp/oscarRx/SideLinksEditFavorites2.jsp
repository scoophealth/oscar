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
<%@page import="org.oscarehr.common.dao.DxresearchDAO"%>
<%@page import="org.oscarehr.common.model.Dxresearch"%>
<%@page import="oscar.oscarRx.data.RxPatientData"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.managers.CodingSystemManager" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager" %>
<%@page import="org.oscarehr.casemgmt.model.Issue"%>
<%@page import="org.oscarehr.casemgmt.model.CaseManagementNote"%>
<%@page import="java.util.List" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
        oscar.oscarRx.pageUtil.RxSessionBean bean2 = (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");

        org.oscarehr.common.model.Allergy[] allergies = RxPatientData.getPatient(LoggedInInfo.getLoggedInInfoFromSession(request),bean2.getDemographicNo()).getActiveAllergies();
        String alle = "";
        if (allergies.length > 0 ){ alle = "Red"; }
        %>

<div class="PropSheetMenu">

<security:oscarSec roleName="<%=roleName$%>" objectName="_allergy" rights="r" reverse="<%=false%>">

<p class="PropSheetLevel1CurrentItem<%=alle%>">
    <bean:message key="oscarRx.sideLinks.msgAllergies"/>
    <a href="javascript:void(0);" name="cmdAllergies"   onclick="javascript:window.location.href='ShowAllergies2.jsp?demographicNo=<%=request.getParameter("demographicNo")%>';" style="width: 200px" >+</a>
</p>
<p class="PropSheetMenuItemLevel1">
<%

        for (int j=0; j<allergies.length; j++){%>

<p class="PropSheetMenuItemLevel1"><a
	title="<%= allergies[j].getDescription() %> - <%= allergies[j].getReaction() %>">
<%=allergies[j].getShortDesc(13,8,"...")%> </a></p>
<%}%>
</p>

</security:oscarSec>

<security:oscarSec roleName="<%=roleName$%>" objectName="_rxresearch" rights="r" reverse="<%=false%>">

<p class="PropSheetLevel1CurrentItem">
	<bean:message key="oscarRx.sideLinks.msgDiseases"/>
</p>
<%	
DxresearchDAO dxreasearchDao = SpringUtils.getBean(DxresearchDAO.class);
CodingSystemManager codingSystemManager = SpringUtils.getBean(CodingSystemManager.class);

for (Dxresearch dx:dxreasearchDao.getByDemographicNo(bean2.getDemographicNo())){
	String codeDescr = null;

	try{
		if(!dx.getDxresearchCode().equals("CKDSCREEN")){
			codeDescr = codingSystemManager.getCodeDescription(dx.getCodingSystem(),dx.getDxresearchCode());
		}
	}
	catch (Exception e){
	    out.println("Please report error to support: " + e.getMessage());
	}

	if(codeDescr != null) {
%>
<p class="PropSheetMenuItemLevel1"><%=StringEscapeUtils.escapeHtml(codeDescr)%></p>
<%
	} 
}
%>
</p>

</security:oscarSec>

<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" reverse="<%=false%>">

<p class="PropSheetLevel1CurrentItem">
	<bean:message key="oscarRx.sideLinks.msgMedHistory"/>
</p>
<%	
	CaseManagementManager cmgmtMgr1 = SpringUtils.getBean(CaseManagementManager.class);
	List<Issue> issues1 = cmgmtMgr1.getIssueInfoByCode(bean2.getProviderNo(), "MedHistory");
	String[] issueIds1 = new String[] {String.valueOf(issues1.get(0).getId())};
	List<CaseManagementNote> notes1 = cmgmtMgr1.getNotes(bean2.getDemographicNo()+"", issueIds1);
	for(CaseManagementNote note:notes1) {
		 if (!note.isLocked() && !note.isArchived()) {
			 
%>
<p class="PropSheetMenuItemLevel1"><%=StringEscapeUtils.escapeHtml(note.getNote()) %></p>
<%
	} }
%>
</p>

</security:oscarSec>

<p class="PropSheetLevel1CurrentItem"><bean:message key="oscarRx.sideLinks.msgFavorites"/>
<a href="EditFavorites2.jsp">edit</a>
<a href="CopyFavorites2.jsp">copy</a>  <%-- <bean:message key="oscarRx.sideLinks.msgCopyFavorites"/> --%>
</p>
<p class="PropSheetMenuItemLevel1">
<%
        oscar.oscarRx.data.RxPrescriptionData.Favorite[] favorites
            = new oscar.oscarRx.data.RxPrescriptionData().getFavorites(bean2.getProviderNo());

        for (int j=0; j<favorites.length; j++){%>

<p class="PropSheetMenuItemLevel1"><a
        href="javascript:void(0);" onclick="useFav2('<%= favorites[j].getFavoriteId() %>');"
	title="<%= favorites[j].getFavoriteName() %>"> <%if(favorites[j].getFavoriteName().length()>13){%>
<%= favorites[j].getFavoriteName().substring(0, 10) + "..." %> <%}else{%>
<%= favorites[j].getFavoriteName() %> <%}%> </a></p>
<%}%>
</p>
</div>
