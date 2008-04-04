<%-- 
// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
--%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ include file="/casemgmt/taglibs.jsp" %>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.PMmodule.model.Provider" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
 <nested:size id="num" name="Notes"/>
<div style="width:10%; float:right; text-align:center;"><h3 style="padding:0px; background-color:<c:out value="${param.hc}"/>" ><a href="#" title='Add Item' onclick="return showEdit(event,'',0,'','','','<%=request.getAttribute("addUrl")%>0', '<c:out value="${param.cmd}"/>','<%=request.getAttribute("identUrl")%>');">+</a></h3></div>
<div style="clear:left; float:left; width:90%;"><h3 style="width:100%; background-color:<c:out value="${param.hc}"/>"><a href="#" onclick="$('showEditNote').style.display='none';return false;"><c:out value="${param.title}"/></a></h3></div>
<div style="clear:both; height:63px; overflow:auto;">
    <ul style="margin-left:5px;">
        <nested:iterate indexId="noteIdx" id="note" name="Notes" type="org.oscarehr.casemgmt.model.CaseManagementNote">
            <% if( noteIdx % 2 == 0 ) { %>
            <li class="cpp" style="clear:both; whitespace:nowrap; background-color:#F3F3F3;">
            <%}else {%>
            <li class="cpp" style="clear:both; whitespace:nowrap;">                 
                <%}
                List<Provider> listEditors = note.getEditors();
                StringBuffer editors = new StringBuffer();
                for( Provider p: listEditors) {
                editors.append(p.getFormattedName() + ";");                     
                }             
                
                String htmlNoteTxt = note.getNote();
                htmlNoteTxt = htmlNoteTxt.replaceAll("\n", "<br>");
                
                String noteTxt = note.getNote();
                noteTxt = noteTxt.replaceAll("\"","");
                noteTxt = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(noteTxt);             
                %>
                <span id="spanListNote<nested:write name="note" property="id"/>" >
                    <a class="topLinks" onmouseover="this.className='topLinkhover'"  onmouseout="this.className='topLinks'" title="Rev:<nested:write name="note" property="revision"/> - Last update:<nested:write name="note" property="update_date" format="dd-MMM-yyyy"/>" id="listNote<nested:write name="note" property="id"/>" href="#" onclick="showEdit(event, '<nested:write name="note" property="id"/>','<%=editors.toString()%>','<nested:write name="note" property="observation_date" format="dd-MMM-yyyy"/>','<nested:write name="note" property="revision"/>','<%=noteTxt%>', '<%=request.getAttribute("addUrl")%><nested:write name="note" property="id"/>', '<c:out value="${param.cmd}"/>','<%=request.getAttribute("identUrl")%>');return false;"><%=htmlNoteTxt%></a>
                </span>            
            </li>
        </nested:iterate>
    </ul>
</div>
 <input type="hidden" id="<c:out value="${param.cmd}"/>num" value="<nested:write name="num"/>">
 <input type="hidden" id="<c:out value="${param.cmd}"/>threshold" value="0">