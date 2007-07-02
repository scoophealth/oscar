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
<%@ include file="/taglibs.jsp" %>
<html:html locale="true">
    <head>
        <title>Admission Details</title>

        <style type="text/css">
            @import "<html:rewrite page="/css/tigris.css" />";
            @import "<html:rewrite page="/css/displaytag.css" />";
            @import "<html:rewrite page="/jsCalendar/skins/aqua/theme.css" />";
        </style>

    <body>
    <html:form action="/PMmodule/ClientManager.do">

        <html:hidden property="admission.id"/>

        <table width="100%" border="1" cellspacing="2" cellpadding="3">
            <tr class="b">
                <td width="20%">Client name:</td>
                <td><bean:write name="clientManagerForm" property="client.formattedName"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Program name:</td>
                <td><bean:write name="clientManagerForm" property="admission.programName"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Team name:</td>
                <td><bean:write name="clientManagerForm" property="admission.teamName"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Program type:</td>
                <td><bean:write name="clientManagerForm" property="admission.programType"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Agency:</td>
                <td><bean:write name="clientManagerForm" property="agency.name"/></td>
                <td><c:out 
            </tr>
            <tr class="b">
                <td width="20%">Client status:</td>
                <td><bean:write name="clientManagerForm" property="admission.clientStatus"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Admission status:</td>
                <td><bean:write name="clientManagerForm" property="admission.admissionStatus"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Admission notes:</td>
                <td><bean:write name="clientManagerForm" property="admission.admissionNotes"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Admission date:</td>
                <td><bean:write name="clientManagerForm" property="admission.admissionDate"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Temporary admission?</td>
                <td><bean:write name="clientManagerForm" property="admission.temporaryAdmission"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Discharge date:</td>
                <td><bean:write name="clientManagerForm" property="admission.dischargeDate"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Discharge reason:</td>
                <td><bean:write name="clientManagerForm" property="admission.radioDischargeReason"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Discharge notes:</td>
                <td><bean:write name="clientManagerForm" property="admission.dischargeNotes"/></td>
            </tr>

        </table>

        <!--
	public static String REF = "Admission";
	public static String PROP_CLIENT = "client";
	public static String PROP_PROGRAM_NAME = "programName";
	public static String PROP_TEMP_ADMIT_DISCHARGE = "TempAdmitDischarge";
	public static String PROP_AGENCY_ID = "AgencyId";
	public static String PROP_PROGRAM_ID = "ProgramId";
	public static String PROP_PROGRAM_TYPE = "programType";
	public static String PROP_TEMP_ADMISSION = "TempAdmission";
	public static String PROP_CLIENT_STATUS = "clientStatus";
	public static String PROP_TEAM_NAME = "teamName";
	public static String PROP_ADMISSION_NOTES = "AdmissionNotes";
	public static String PROP_CLIENT_ID = "ClientId";
	public static String PROP_RADIO_DISCHARGE_REASON = "RadioDischargeReason";
	public static String PROP_TEAM = "team";
	public static String PROP_PROVIDER_NO = "ProviderNo";
	public static String PROP_DISCHARGE_DATE = "DischargeDate";
	public static String PROP_TEAM_ID = "TeamId";
	public static String PROP_CLIENT_STATUS_ID = "ClientStatusId";
	public static String PROP_ADMISSION_STATUS = "AdmissionStatus";
	public static String PROP_ADMISSION_DATE = "AdmissionDate";
	public static String PROP_DISCHARGE_NOTES = "DischargeNotes";
	public static String PROP_ID = "Id";
	public static String PROP_TEMPORARY_ADMISSION = "TemporaryAdmission";        -->


    </html:form>
    </body>
</html:html>
