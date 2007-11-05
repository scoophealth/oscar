<%@ page import="org.oscarehr.PMmodule.model.ProgramQueue" %>
<%@ page import="java.net.URLEncoder" %>
<!--
/*
*
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for
* Centre for Research on Inner City Health, St. Michael's Hospital,
* Toronto, Ontario, Canada
*/
-->

<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>

<script>
    function do_admission() {
        var form = document.programManagerViewForm;
        form.method.value='admit';
        form.submit();

    }
    function do_rejection() {
        var form = document.programManagerViewForm;
        form.method.value='reject_from_queue';
        form.submit();

    }
    function refresh_queue() {
        var form = document.programManagerViewForm;
        form.method.value='view';
        form.submit();
    }

    function select_client(client_id,action,queue_id) {
        var form = document.programManagerViewForm;
        form.elements['clientId'].value=client_id;
        form.elements['queueId'].value=queue_id;
        if(action == 'admit') {
            form.method.value='select_client_for_admit';
        }
        if(action == 'reject') {
            if(!confirm('Are you sure you would like to reject admission for this client?')) {
                return;
            }
            form.method.value='select_client_for_reject';
			//form.method.value='reject_from_queue';
        }
        form.submit();

    }

    function popup(title, url) {
        window.open(url, title, 'width=800, height=800,resizable=yes, scrollbars=yes');
    }

    function cme_client(programId, clientId) {
        popup("caseManagement" + clientId, "../oscarEncounter/IncomingEncounter.do?case_program_id=" + programId + "&demographicNo=" + clientId + "&status=B");
    }
</script>
<html:hidden property="clientId" />
<html:hidden property="queueId" />
<div class="tabs" id="tabs">
    <table cellpadding="3" cellspacing="0" border="0">
        <tr>
            <th title="Programs">Queue</th>
        </tr>
    </table>
</div>
<!--  show current clients -->
<display:table class="simple" cellspacing="2" cellpadding="3" id="queue_entry" name="queue" export="false" pagesize="0" requestURI="/PMmodule/ProgramManagerView.do">
    <display:setProperty name="paging.banner.placement" value="bottom" />
    <display:setProperty name="basic.msg.empty_list" value="Queue is empty." />
    <display:column sortable="false">
        <input type="button" value="Admit" onclick="select_client('<c:out value="${queue_entry.clientId}"/>','admit','<c:out value="${queue_entry.id}"/>')" />
    </display:column>
    <display:column sortable="false">
        <input type="button" value="Reject" onclick="select_client('<c:out value="${queue_entry.clientId}"/>','reject','<c:out value="${queue_entry.id}"/>')" />
    </display:column>
    <!-- disabled by rwd because visibility of link and permissions in CME are a problem -->
    <%--<display:column sortable="false">--%>
        <!--<a href="javascript:void(0)" title="Case management" onclick="cme_client('<c:out value="${queue_entry.programId}"/>', '<c:out value="${queue_entry.clientId}"/>')">-->
            <!--Case Management Encounter-->
        <!--</a>-->
    <%--</display:column>--%>
    <display:column sortable="true" property="clientFormattedName" title="Client name"/>
    <display:column property="referralDate" sortable="true" title="Referral Date" />
    <display:column property="providerFormattedName" sortable="true" title="Referring Provider" />
    <caisi:isModuleLoad moduleName="pmm.refer.temporaryAdmission.enabled">
        <display:column property="temporaryAdmission" sortable="true" title="Temporary Admission" />
    </caisi:isModuleLoad>
    <display:column property="notes" sortable="true" title="Reason for referral" />
    <display:column property="presentProblems" sortable="true" title="Present problems"/>
</display:table>
<br />
<br />
<c:if test="${requestScope.do_admit != null}">
    <table width="100%" border="1" cellspacing="2" cellpadding="3">
        <c:if test="${requestScope.current_admission != null}">
            <tr>
                <td colspan="2"><b style="color:red">Warning:<br />
                    This client is currently admitted to a bed program (<c:out value="${current_program.name}" />).<br />
                    By completing this admission, you will be discharging them from this current program.</b></td>
            </tr>
            <tr class="b">
                <td width="20%">Discharge Notes:</td>
                <td><textarea cols="50" rows="7" name="admission.dischargeNotes"></textarea></td>
            </tr>
        </c:if>
        <tr class="b">
            <td width="20%">Admission Notes:</td>
            <td><textarea cols="50" rows="7" name="admission.admissionNotes"></textarea></td>
        </tr>
        <tr class="b">
            <td colspan="2"><input type="button" value="Process Admission" onclick="do_admission()" /> <input type="button" value="Cancel" onclick="refresh_queue()" /></td>
        </tr>
    </table>
</c:if>
<c:if test="${requestScope.do_reject != null}">
    <table width="100%" border="1" cellspacing="2" cellpadding="3">
        <tr>
            <td width="5%"><html:radio property="radioRejectionReason" value="1" /></td>
            <td>Client requires acute care</td>
        </tr>
        <tr>
            <td width="5%"><html:radio property="radioRejectionReason" value="2" /></td>
            <td>Client not interested</td>
        </tr>
        <tr>
            <td width="5%"><html:radio property="radioRejectionReason" value="3" /></td>
            <td>Client does not fit program criteria</td>
        </tr>
        <tr>
            <td width="5%"><html:radio property="radioRejectionReason" value="4" /></td>
            <td>Program does not have space available</td>
        </tr>
        <tr>
            <td width="5%"><html:radio property="radioRejectionReason" value="5" /></td>
            <td>Other</td>
        </tr>
        <tr class="b">
            <td width="20%">Rejection Note:</td>
            <td><textarea cols="50" rows="7" name="admission.admissionNotes"></textarea></td>
        </tr>
        <tr class="b">
            <td colspan="2"><input type="button" value="Process" onclick="do_rejection()" /> <input type="button" value="Cancel" onclick="refresh_queue()" /></td>
        </tr>
    </table>
</c:if>
