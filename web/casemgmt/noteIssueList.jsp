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
<nested:empty name="caseManagementEntryForm" property="caseNote.id">
    <div id="sumary0">
</nested:empty>
<nested:notEmpty name="caseManagementEntryForm" property="caseNote.id">
    <div id="sumary<nested:write name="caseManagementEntryForm" property="caseNote.id" />">
</nested:notEmpty>
    
    <div id="observation"><i>Observation Date:</i>&nbsp;<img src="<c:out value="${ctx}/images/cal.gif" />" id="observationDate_cal" alt="calendar">&nbsp;<input type="text" id="observationDate" name="observation_date" ondblclick="this.value='';" style="font-style:italic;border:none; width:140px;" readonly value="<nested:write name="caseManagementEntryForm" property="caseNote.observation_date" format="dd-MMM-yyyy H:mm" />"></div>
    
    <nested:equal name="newNote" value="false">
        Created:&nbsp;<nested:write name="caseManagementEntryForm" property="caseNote.create_date" format="dd-MMM-yyyy H:mm" /><br>
        <nested:equal name="caseManagementEntryForm" property="caseNote.signed" value="true"> 
            Signed by 
            <nested:write name="caseManagementEntryForm" property="caseNote.signing_provider_no"/>
        </nested:equal>
        <nested:notEqual name="caseManagementEntryForm" property="caseNote.signed" value="true"> 
            Saved by 
            <nested:write name="caseManagementEntryForm" property="caseNote.provider.formattedName" />
        </nested:notEqual>        
        <nested:write name="caseManagementEntryForm" property="caseNote.update_date" format="dd-MMM-yyyy H:mm" />
        <sup>rev<a href="#" onclick="return showHistory('<nested:write name="caseManagementEntryForm" property="caseNote.id" />', event);"><nested:write name="caseManagementEntryForm" property="caseNote.revision" /></a></sup>
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
</div>
                            <div id="noteIssues" style="font-size:8px; display:none;">
                                <b>Reference Issues</b>
                                <table id="setIssueList" style="font-size:8px;">
                                <nested:iterate indexId="ind" id="issueCheckList" property="issueCheckList" name="caseManagementEntryForm" type="org.oscarehr.casemgmt.web.CheckBoxBean">
                                <%
                                String winame = "window" + issueCheckList.getIssue().getIssue().getDescription();
                                winame = winame.replaceAll("\\s|\\/", "_");
                                if( ind % 2 == 0 ) {%>
                                <tr>
                                <% } %>
                                <td style="width:50%; font-size:8px; background-color: <%= (ind.intValue()%2==0)?"#EEEEFF":"white" %>;">
                                <%String submitString = "this.form.method.value='issueChange';";
                                submitString = submitString + "this.form.lineId.value=" + "'"
                                + ind.intValue() + "'; return ajaxUpdateIssues('issueChange', $('noteIssues').up().id);";
                                String id = "noteIssue" + ind;
                                org.oscarehr.casemgmt.web.CheckBoxBean cbb = (org.oscarehr.casemgmt.web.CheckBoxBean)pageContext.getAttribute("issueCheckList");
                                boolean writeAccess = cbb.getIssue().isWriteAccess();
                                boolean disabled = !writeAccess;
                                %>
                                
                                <nested:checkbox styleId="<%=id%>" indexed="true" name="issueCheckList" property="checked" disabled="<%=disabled%>"></nested:checkbox>
                                
                                <a href="#" onclick="return displayIssue('<%=winame%>');"><nested:write name="issueCheckList" property="issue.issue.description" /></a>
                                
                                <nested:equal name="issueCheckList" property="used" value="false">
                                    <%String submitDelete = "removeIssue('" + winame + "');document.forms['caseManagementEntryForm'].deleteId.value=" + "'"
                                    + ind.intValue() + "';return ajaxUpdateIssues('issueDelete', $('noteIssues').up().id);";
                                    %>
                                    &nbsp;<a href="#" onclick="<%=submitDelete%>">Delete</a>&nbsp;
                                </nested:equal>
                                
                                <!--  change diagnosis button -->
                                <%String submitChange = "return changeDiagnosis('" + ind.intValue() + "');";
                                %>					
                                &nbsp;<a href="#" onclick="<%=submitChange%>">Change</a>
                                <div id="<%=winame%>" style="margin-left:20px; display:none;">
                                    
                                        <div>
                                            <div style="width:50%;float:left;display:inline;"><nested:radio indexed="true" name="issueCheckList" property="issue.acute" value="true" onchange="<%=submitString%>">acute</nested:radio></div>
                                            <div style="width:50%;float:left;display:inline;clear:right;"><nested:radio indexed="true" name="issueCheckList" property="issue.acute" value="false" onchange="<%=submitString%>">chronic</nested:radio></div>
                                            <div style="width:50%;float:left;display:inline;"><nested:radio indexed="true" name="issueCheckList" property="issue.certain" disabled="<%=disabled%>" value="true" onchange="<%=submitString%>">certain</nested:radio></div>
                                            <div style="width:50%;float:left;display:inline;clear:right;"><nested:radio indexed="true" name="issueCheckList" property="issue.certain" disabled="<%=disabled%>" value="false" onchange="<%=submitString%>">uncertain</nested:radio></div>
                                            <div style="width:50%;float:left;display:inline;"><nested:radio indexed="true" name="issueCheckList" property="issue.major" disabled="<%=disabled%>" value="true" onchange="<%=submitString%>">major</nested:radio></div>
                                            <div style="width:50%;float:left;display:inline;clear:right;"><nested:radio indexed="true" name="issueCheckList" property="issue.major" disabled="<%=disabled%>" value="false" onchange="<%=submitString%>">not major</nested:radio></div>
                                            <div style="width:50%;float:left;display:inline;"><nested:radio indexed="true" name="issueCheckList" property="issue.resolved" value="true" onchange="<%=submitString%>">resolved</nested:radio></div>
                                            <div style="width:50%;float:left;display:inline;clear:right;"><nested:radio indexed="true" name="issueCheckList" property="issue.resolved" value="false" onchange="<%=submitString%>">unresolved</nested:radio></div>
                                            <div style="text-align:center;"><nested:text indexed="true" name="issueCheckList" property="issue.type"  size="10" disabled="<%=disabled%>" /></div>
                                        </div>                                    
                                </div>
                    </td>
           <% if( ind % 2 != 0 ) { %>
                </tr>
           <% } %>
            
        </nested:iterate>
    </table>
</div>	
        
<script type="text/javascript">         
   if( $("toggleIssue") != null )
        $("toggleIssue").disabled = false;
    
   if( showIssue ) {
        $("noteIssues").show();
        for( var idx = 0; idx < expandedIssues.length; ++idx )            
            displayIssue(expandedIssues[idx]);                   
   }
        
   function displayIssue(id) {
        //if issue has been changed/deleted remove it from array and return
        if( $(id) == null ) {
            removeIssue(id);
            return false;
        }
        var idx;
        var parent = $(id).parentNode;
        $(id).toggle();
        if( $(id).style.display != "none" ) {            
            parent.style.backgroundColor = "#dde3eb";
            parent.style.border = "1px solid #464f5a";
            
            if( (idx = expandedIssues.indexOf(id)) == -1 )
                expandedIssues.push(id);
        }
        else {
            parent.style.backgroundColor = "";
            parent.style.border = ""; 
            
            removeIssue(id);
        }
        return false;
   }
   
   function removeIssue(id) {
        var idx;
        
        if( (idx = expandedIssues.indexOf(id)) > -1 )
            expandedIssues.splice(idx,1);
   }      
   //store observation date so we know if user changes it
   origObservationDate = $("observationDate").value;
   
   //check to see if we need to update div containers to most recent note id
   //this happens only when we're called thru ajaxsave
   <nested:notEmpty name="ajaxsave">
    var origId = <nested:write name="origNoteId" />;
    var newId = <nested:write name="ajaxsave" />;
    var oldDiv;
    var newDiv;
    var prequel = ["h","n","sig"];
    
    for( var idx = 0; idx < prequel.length; ++idx ) {
        oldDiv = prequel[idx] + origId;
        newDiv = prequel[idx] + newId;
        $(oldDiv).id = newDiv;
    }    
    </nested:notEmpty>
   
    //create calendar
    Calendar.setup({ inputField : "observationDate", ifFormat : "%d-%b-%Y %H:%M ", showsTime :true, button : "observationDate_cal", singleClick : true, step : 1 });    
</script>
        