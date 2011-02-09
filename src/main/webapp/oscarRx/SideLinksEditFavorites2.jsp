<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%  
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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

        oscar.oscarRx.pageUtil.RxSessionBean bean2 = (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
        
        oscar.oscarRx.data.RxPatientData.Patient.Allergy[] allergies = new oscar.oscarRx.data.RxPatientData().getPatient(bean2.getDemographicNo()).getAllergies();
        String alle = "";
        if (allergies.length > 0 ){ alle = "Red"; }
        %>
<td width="10%" height="100%" valign="top">
<div class="PropSheetMenu">
<p class="PropSheetLevel1CurrentItem<%=alle%>">
    <bean:message key="oscarRx.sideLinks.msgAllergies"/>
    <a href="javascript:void(0);" name="cmdAllergies"   onclick="javascript:window.location.href='ShowAllergies2.jsp';" style="width: 200px" >+</a>
</p>
<p class="PropSheetMenuItemLevel1">
<%
                
        for (int j=0; j<allergies.length; j++){%>

<p class="PropSheetMenuItemLevel1"><a
	title="<%= allergies[j].getAllergy().getDESCRIPTION() %> - <%= allergies[j].getAllergy().getReaction() %>">
<%=allergies[j].getAllergy().getShortDesc(13,8,"...")%> </a></p>
<%}%>
</p>
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
</td>
