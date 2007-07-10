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
<%@ page import="org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean" %>
<%@ include file="/casemgmt/taglibs.jsp" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

<p id="sumary<nested:write name="caseManagementEntryForm" property="caseNote.id" />">
    
    <nested:equal name="newNote" value="false">    
        <nested:write name="caseManagementEntryForm" property="caseNote.update_date" format="dd-MMM-yyyy H:mm" />
        <nested:equal name="caseManagementEntryForm" property="caseNote.signed" value="true"> 
            signed by 
            <nested:write name="caseManagementEntryForm" property="caseNote.signing_provider_no"/>
        </nested:equal>
        <nested:notEqual name="caseManagementEntryForm" property="caseNote.signed" value="true"> 
            saved by 
            <nested:write name="caseManagementEntryForm" property="caseNote.provider.formattedName" />
        </nested:notEqual>
        <sup>rev <nested:write name="caseManagementEntryForm" property="caseNote.revision" /></sup>
    </nested:equal>
    
    <nested:size id="numIssues" name="caseManagementEntryForm" property="caseNote.issues" />
    <nested:greaterThan name="numIssues" value="0">
        <br>Assigned Issues
        <ul style="list-style: circle outside none; margin-top:0px;">
            <nested:iterate id="noteIssue" property="caseNote.issues" name="caseManagementEntryForm">
                <li><c:out value="${noteIssue.issue.description}"/></li>
            </nested:iterate>
        </ul>
    </nested:greaterThan>
</p>
<div id="noteIssues" style="font-size:8px; display:none;">
    <b>Reference Issues</b>
    <table id="setIssueList" style="font-size:8px;">
        <nested:iterate indexId="ind" id="issueCheckList" property="issueCheckList" name="caseManagementEntryForm" type="org.oscarehr.casemgmt.web.CheckBoxBean">
            <tr>
                <td style="font-size:8px; background-color: <%= (ind.intValue()%2==0)?"#EEEEFF":"white" %>;">
                    <%String submitString = "this.form.method.value='issueChange';";
                    submitString = submitString + "this.form.lineId.value=" + "'"
                    + ind.intValue() + "'; return ajaxUpdateIssues('issueChange', $('noteIssues').up().id);";
                    String id = "noteIssue" + ind;
                    org.oscarehr.casemgmt.web.CheckBoxBean cbb = (org.oscarehr.casemgmt.web.CheckBoxBean)pageContext.getAttribute("issueCheckList");
                    boolean writeAccess = cbb.getIssue().isWriteAccess();
                    boolean disabled = !writeAccess;
                    %>
                    
                    <nested:checkbox styleId="<%=id%>" indexed="true" name="issueCheckList" property="checked" disabled="<%=disabled%>"></nested:checkbox>
                    
                    <nested:write name="issueCheckList" property="issue.issue.description" />
                </td>
                <%-- <ul style="list-style: none inside url(<c:out value="${ctx}/oscarMessenger/img/plus.gif"/>);">
                    <li onclick="toggleChild(this, event)">Type --%>
                <td style="font-size:8px">
                    <ul style="list-style: none inside none;">
                        <li><nested:radio indexed="true" name="issueCheckList" property="issue.acute" value="true" onchange="<%=submitString%>">acute</nested:radio></li>
                        <li><nested:radio indexed="true" name="issueCheckList" property="issue.acute" value="false" onchange="<%=submitString%>">chronic</nested:radio></li>
                    </ul>
                </td>
                <%--</li>
                </ul>
                <ul style="list-style: none inside url(<c:out value="${ctx}/oscarMessenger/img/plus.gif"/>);">
                    <li onclick="toggleChild(this, event)">Importance --%>
                <td style="font-size:8px">
                    <ul style="list-style: none inside none;">
                        <li><nested:radio indexed="true" name="issueCheckList" property="issue.resolved" value="true" onchange="<%=submitString%>">resolved</nested:radio></li>
                        <li><nested:radio indexed="true" name="issueCheckList" property="issue.resolved" value="false" onchange="<%=submitString%>">unresolved</nested:radio></li>
                    </ul>
                </td>
                <%--    </li>
                </ul>
                <ul style="list-style: none inside url(<c:out value="${ctx}/oscarMessenger/img/plus.gif"/>);">
                    <li onclick="toggleChild(this, event)">Misc --%>
                <td style="font-size:8px">
                    <ul style="list-style: none inside none;">
                        <li><nested:text indexed="true" name="issueCheckList" property="issue.type"  size="10" disabled="<%=disabled%>" /></li>
                        <nested:equal name="issueCheckList" property="used" value="false">
                            <%submitString = "document.forms['caseManagementEntryForm'].deleteId.value=" + "'"
                            + ind.intValue() + "';return ajaxUpdateIssues('issueDelete', $('noteIssues').up().id);";
                            %>
                            <li><a href="#" onclick="<%=submitString%>">Delete</a></li>
                        </nested:equal>
                        
                        <!--  change diagnosis button -->
                        <%submitString = "return changeDiagnosis('" + ind.intValue() + "');";
                        %>					
                        <li><a href="#" onclick="<%=submitString%>">Change</a></li>
                    </ul>
                </td>
                <%--</li>
                </ul> --%>
            </tr>
            
        </nested:iterate>
    </table>
</div>	
        
<script type="text/javascript">        
   if( $("toggleIssue") != null )
        $("toggleIssue").disabled = false;
    
   if( showIssue )
        $("noteIssues").show();
   
        
</script>
        