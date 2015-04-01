
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>



<%-- Updated by Eugene Petruhin on 11 dec 2008 while fixing #2356548 & #2393547 --%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<% long loadPage = System.currentTimeMillis(); %>
<%@ include file="/casemgmt/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet" %>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig" %>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.util.MeasurementHelper" %>
<%@ page import="oscar.oscarResearch.oscarDxResearch.bean.dxResearchBeanHandler" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Vector" %>

<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>
<%
    String province = ((String ) oscarVariables.getProperty("billregion","")).trim().toUpperCase();
    oscar.oscarEncounter.pageUtil.EctSessionBean bean=null;
    if ("true".equalsIgnoreCase((String)session.getAttribute("casemgmt_bean_flag"))){
        oscar.oscarEncounter.pageUtil.EctSessionBean bean1=(oscar.oscarEncounter.pageUtil.EctSessionBean)session.getAttribute("EctSessionBean");
        bean=new oscar.oscarEncounter.pageUtil.EctSessionBean();
        bean.providerNo=bean1.providerNo;
        bean.demographicNo=bean1.demographicNo;
        bean.appointmentNo=bean1.appointmentNo;
        bean.patientLastName=bean1.patientLastName;
        bean.patientFirstName=bean1.patientFirstName;
        bean.curProviderNo=bean1.curProviderNo;
        bean.appointmentDate=bean1.appointmentDate;
        bean.status=bean1.status;
        bean.userName=bean1.userName;
        bean.yearOfBirth=bean1.yearOfBirth;
        bean.monthOfBirth=bean1.monthOfBirth;
        bean.dateOfBirth=bean1.dateOfBirth;
        bean.measurementGroupNames=(java.util.ArrayList) bean1.measurementGroupNames.clone();
        session.setAttribute("casemgmt_bean", bean);
        session.setAttribute("casemgmt_bean_flag","false");
    }
//bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)session.getAttribute("EctSessionBean");
//session.setAttribute("casemgmt_bean", bean);
bean=(oscar.oscarEncounter.pageUtil.EctSessionBean) session.getAttribute("casemgmt_bean");
if (bean==null) bean=new oscar.oscarEncounter.pageUtil.EctSessionBean();
if (bean.appointmentNo==null) bean.appointmentNo="0";
String bsurl=(String)session.getAttribute("casemgmt_oscar_baseurl");
String backurl=bsurl+"/oscarEncounter/IncomingEncounter.do?";
//get programId
    String pgId=(String)session.getAttribute("case_program_id");
    if (pgId==null) pgId="";
%>

<script type="text/javascript" language=javascript>
    function popupPage(varpage) {
        var page = "" + varpage;
        windowprops = "height=600,width=700,location=no,"
                + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
        window.open(page, "", windowprops);
    }

    function onUnbilled(url) {
        if(confirm("You are about to delete the previous billing, are you sure?")) {
            popupPage(url);
        }
    }
    function selectBox(name) {
        to = name.options[name.selectedIndex].value;
        name.selectedIndex =0;
        if(to!="null")
            popupPage(to);
    }

    function popUpMsg(vheight,vwidth,msgPosition) {


        var page = "<%=session.getAttribute("casemgmt_oscar_baseurl")%>"+"/oscarMessenger/ViewMessageByPosition.do?from=encounter&orderBy=!date&demographic_no=<%=bean.demographicNo%>&messagePosition="+msgPosition;
        windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
        var popup=window.open(page, "", windowprops);
        if (popup != null) {
            if (popup.opener == null) {
                popup.opener = self;
            }
        }
        popup.focus();
    }
    function popUpMeasurements(vheight,vwidth,name,varpage) { //open a new popup window
        if(varpage!= 'null'){
            name.options[0].selected = true;
            var page = "<%=session.getAttribute("casemgmt_oscar_baseurl")%>"+"/oscarEncounter/oscarMeasurements/SetupMeasurements.do?groupName=" + varpage;
            windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
            var popup=window.open(page, "", windowprops);
            if (popup != null) {
                if (popup.opener == null) {
                    popup.opener = self;
                    alert("hi this is a null for self!");
                }
            }
        }
    }
    function popupSearchPage(vheight,vwidth,varpage) { //open a new popup window
        var page = "" + varpage;
        windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
        var popup=window.open(page, "", windowprop);
    }
    function grabEnter(event){
        if(window.event && window.event.keyCode == 13){
            popupSearchPage(600,800,document.forms['ksearch'].channel.options[document.forms['ksearch'].channel.selectedIndex].value+urlencode(document.forms['ksearch'].keyword.value));
            return false;
        }else if (event && event.which == 13){
            popupSearchPage(600,800,document.forms['ksearch'].channel.options[document.forms['ksearch'].channel.selectedIndex].value+urlencode(document.forms['ksearch'].keyword.value));
            return false;
        }
    }
    function urlencode(str) {
        var ns = (navigator.appName=="Netscape") ? 1 : 0;
        if (ns) { return escape(str); }
        var ms = "%25#23 20+2B?3F<3C>3E{7B}7D[5B]5D|7C^5E~7E`60";
        var msi = 0;
        var i,c,rs,ts ;
        while (msi < ms.length) {
            c = ms.charAt(msi);
            rs = ms.substring(++msi, msi +2);
            msi += 2;
            i = 0;
            while (true)	{
                i = str.indexOf(c, i);
                if (i == -1) break;
                ts = str.substring(0, i);
                str = ts + "%" + rs + str.substring(++i, str.length);
            }
        }
        return str;
    }
</script>

<style type="text/css">
    .ControlSelect {
        font-family: Verdana, Tahoma, Arial, sans-serif;
        font-size:80%;
        width:100%;
    }
</style>
<div id="noprint" name="noprint">
<table style="font:bold small-caps 10pt/12pt">
<form name="navigationForm" method="get">
<% java.util.Date today=new java.util.Date();
    String curYear=Integer.toString(today.getYear());
    String curMonth=Integer.toString(today.getMonth());
    String curDay=Integer.toString(today.getDay());
    String Hour=Integer.toString(today.getHours());
    String Min=Integer.toString(today.getMinutes());
    String  eURL ="/oscarEncounter/IncomingEncounter.do?casetoEncounter=true&providerNo="+bean.providerNo+"&appointmentNo="+bean.appointmentNo+"&demographicNo="+bean.demographicNo+"&curProviderNo="+bean.providerNo+"&reason="+java.net.URLEncoder.encode(" ")+"&userName="+java.net.URLEncoder.encode(bean.patientFirstName+" "+bean.patientLastName)+"&curDate="+curYear+"-"+curMonth+"-"+curDay+"&appointmentDate="+curYear+"-"+curMonth+"-"+curDay+"&startTime="+Hour+":"+Min+"&status=t";%>
<caisirole:SecurityAccess accessName="medical encounter" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">
    <!--
    <a href='<%=bsurl%>/oscarSurveillance/CheckSurveillance.do?demographicNo=<%=bean.demographicNo%>&proceed=<%=java.net.URLEncoder.encode(eURL)%>'>Oscar Encounter</a>
    -->
</caisirole:SecurityAccess>
<!-- tr><td><a href="</td></tr -->

<tr style="background-color:#BBBBBB;"><td>Clinical Modules</td></tr>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<!-- master -->
<caisirole:SecurityAccess accessName="master file" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">
    
        <tr><td><a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/demographic/demographiccontrol.jsp?demographic_no=<%=bean.demographicNo%>&displaymode=edit&dboperation=search_detail');return false;">Master</a></td></tr>

</caisirole:SecurityAccess>

<caisirole:SecurityAccess accessName="billing" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">
    <!-- billing -->

        <% if(bean.status.indexOf('B')==-1) { %>
        <tr><td><a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/billing.do?billRegion=<%=java.net.URLEncoder.encode(province)%>&billForm=<%=java.net.URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=<%=java.net.URLEncoder.encode("")%>&appointment_no=<%=bean.appointmentNo%>&appointment_date=<%=bean.appointmentDate%>&start_time=<%=Hour+":"+Min%>&demographic_name=<%=java.net.URLEncoder.encode(bean.patientLastName+","+bean.patientFirstName)%>&demographic_no=<%=bean.demographicNo%>&providerview=<%=bean.curProviderNo%>&user_no=<%=bean.providerNo%>&apptProvider_no=<%=bean.curProviderNo%>&bNewForm=1&status=t');return false;">Billing</a></td></tr>
        <%}else{ %>
        <tr><td><a href="javascript:void(0)" onClick="onUnbilled('<%=bsurl%>/billing/CA/<%=province%>/billingDeleteWithoutNo.jsp?appointment_no=<%=bean.appointmentNo%>');return false;">Billing</a></td></tr>
        <%} %>
    
</caisirole:SecurityAccess>

<caisirole:SecurityAccess accessName="prescription Write" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">

    <!-- prescription -->
   
        <tr><td><a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarRx/choosePatient.do?providerNo=<%=bean.providerNo%>&demographicNo=<%=bean.demographicNo%>');return false;">Prescriptions</a></td></tr>
 
</caisirole:SecurityAccess>

<!-- allergies -->
<!-- tr><td><a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarRx/ShowAllergies.jsp?providerNo=<%=bean.providerNo%>&demographicNo=<%=bean.demographicNo%>');return false;">Allergies</a></td></tr -->


<!-- Consultations -->
<tr><td><a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarEncounter/oscarConsultationRequest/DisplayDemographicConsultationRequests.jsp?de=<%=bean.demographicNo%>');return false;">Consultations</a></td></tr>

<caisirole:SecurityAccess accessName="immunization" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">
    <!-- IMMUNIZATION -->
    <oscar:oscarPropertiesCheck property="IMMUNIZATION" value="yes" defaultVal="true">
        <% if (oscar.oscarEncounter.immunization.data.EctImmImmunizationData.hasImmunizations(bean.demographicNo)) { %>
        <tr><td><a style="color:red" href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarEncounter/immunization/initSchedule.do');return false;">Immunizations</a></td></tr>
        <% } else {%>
        <tr><td><a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarEncounter/immunization/initSchedule.do');return false;">Immunizations</a></td></tr>
        <% } %>
    </oscar:oscarPropertiesCheck>
</caisirole:SecurityAccess>

<!-- Prevention -->
<caisirole:SecurityAccess accessName="prevention" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">
    <oscar:oscarPropertiesCheck property="PREVENTION" value="yes">
        <tr><td><a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarPrevention/index.jsp?demographic_no=<%=bean.demographicNo%>');return false;">
            <oscar:preventionWarnings demographicNo="<%=bean.demographicNo%>">prevention</oscar:preventionWarnings></a></td></tr>
    </oscar:oscarPropertiesCheck>
</caisirole:SecurityAccess>

<!-- oscarcomm -->
<caisirole:SecurityAccess accessName="oscarcomm" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">

    <%  if (oscar.OscarProperties.getInstance().getProperty("oscarcomm","").equals("on")) { %>
    <tr><td><a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarEncounter/RemoteAttachments.jsp');return false;">OscarComm</a></td></tr>
    <% } %>
</caisirole:SecurityAccess>

<!-- Disease Registry -->
<caisirole:SecurityAccess accessName="disease registry" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">

    <tr><td><a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarResearch/oscarDxResearch/setupDxResearch.do?demographicNo=<%=bean.demographicNo%>&providerNo=<%=bean.providerNo%>&quickList=');return false;">Disease Registry</a></td></tr>
</caisirole:SecurityAccess>

</caisi:isModuleLoad>

<!-- add tickler -->
<caisirole:SecurityAccess accessName="Write Ticklers" accessType="Action" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">
    <tr><td><a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/Tickler.do?method=edit&tickler.demographic_webName=<%=StringEscapeUtils.escapeJavaScript(bean.getPatientLastName() +"," + bean.getPatientFirstName())%>&tickler.demographicNo=<%=bean.demographicNo%>');return false;">Add Tickler</a></td></tr>
</caisirole:SecurityAccess>

<caisirole:SecurityAccess accessName="medical form" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">
    <tr style="background-color:#BBBBBB;"><td>Forms</td></tr>
    <!-- current forms -->
    <tr><td>

        <select name="selectCurrentForms" class="ControlSelect" onChange="javascript:selectBox(this)" onMouseOver="javascript:window.status='View any of <%=bean.patientLastName+","+bean.patientFirstName%>\'s current forms.';return true;">
            <option value="null" selected>-current forms-</option>
            <nested:iterate id="cf" name="casemgmt_newFormBeans" type="org.oscarehr.common.model.EncounterForm">
                <%
                    String table = cf.getFormTable();
                    if(!table.equalsIgnoreCase("")){
                        oscar.oscarEncounter.data.EctFormData.PatientForm[] pforms = oscar.oscarEncounter.data.EctFormData.getPatientForms(bean.demographicNo, table);
                        if(pforms.length>0) {
                            oscar.oscarEncounter.data.EctFormData.PatientForm pfrm = pforms[0];
                            String value=session.getAttribute("casemgmt_oscar_baseurl")+"/form/forwardshortcutname.jsp?formname="
                                    +cf.getFormName()+"&demographic_no="+bean.demographicNo;
                            String label=cf.getFormName()+"&nbsp;Cr:"+pfrm.getCreated()+"&nbsp;Ed:"+pfrm.getEdited();
                %>
                <option value="<%= value %>"><%=label%></option>
                <%
                        }
                    }
                %></nested:iterate>

        </select>
    </td></tr>

    <!-- add new form -->
    <tr><td>
        <select name="selectNewForms" class="ControlSelect" onChange="javascript:selectBox(this)" onMouseOver="javascript:window.status='View <%=bean.patientLastName+","+bean.patientFirstName%>\'s new forms list.';return true;">
            <option value="null" selected>-add new form-</option>
            <nested:iterate id="cf" name="casemgmt_newFormBeans" type="org.oscarehr.common.model.EncounterForm">
                <% if (cf.isHidden()) {
                    String value = session.getAttribute("casemgmt_oscar_baseurl")+"/appointment/"
                            +cf.getFormValue()+bean.demographicNo+"&formId=0&provNo="+bean.providerNo;
                    String label = cf.getFormName();
                %>
                <option value="<%= value %>"><%= label %></option>
                <%} %>
            </nested:iterate>
        </select>
    </td></tr>

    <!-- old forms -->
    <tr><td>
        <a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarEncounter/formlist.jsp?demographic_no=<%=bean.demographicNo%>'); return false;" >-old forms-</a>
    </td></tr>
</caisirole:SecurityAccess>
<script>
    function openSurvey(ctl) {
        var formId = ctl.options[ctl.selectedIndex].value;
        if(formId == 0) {
            return;
        }
        var id = document.getElementById('formInstanceId').value;
        var url = '<html:rewrite action="/PMmodule/Forms/SurveyExecute"/>?method=survey&type=provider&formId=' + formId + '&formInstanceId=' + id + '&clientId=' + <%=bean.demographicNo%>;
        ctl.selectedIndex=0;

        popupPage(url)

    }
</script>
<tr style="background-color:#BBBBBB;"><td>User Created Forms</td></tr>
<tr><td><input type="hidden" id="formInstanceId" value="0" /></td></tr>
<tr>
    <td>
        <select property="view.formId" onchange="openSurvey(this);">
            <option value="0">&nbsp;</option>
            <c:forEach var="survey" items="${survey_list}">
                <option value="<c:out value="${survey.formId}"/>"><c:out value="${survey.description}"/></option>
            </c:forEach>
        </select>
    </td>
</tr>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<tr style="background-color:#BBBBBB;"><td>oscarMessenger</td></tr>
<!-- select message -->
<tr><td>
    <select name="msgSelect" class="ControlSelect" onchange="javascript:popUpMsg(600,900,this.options[this.selectedIndex].value)">
        <option value="null" selected>-Select Message-</option>
        <nested:iterate id="cmb" name="casemgmt_msgBeans" type="org.apache.struts.util.LabelValueBean">
            <option value="<%= cmb.getLabel() %>"><%= cmb.getValue() %></option>
        </nested:iterate>
    </select>
</td></tr>

<!-- add a new message -->
<tr><td>
    <a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarMessenger/SendDemoMessage.do?orderby=date&boxType=3&demographic_no=<%=bean.demographicNo%>&providerNo=<%=bean.providerNo%>&userName=<%=bean.userName%>'); return false;" >New Messages</a>
</td></tr>

<!-- all message -->
<tr><td>
    <a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarMessenger/DisplayDemographicMessages.do?orderby=date&boxType=3&demographic_no=<%=bean.demographicNo%>&providerNo=<%=bean.providerNo%>&userName=<%=bean.userName%>'); return false;" >-All Messages-</a>
</td></tr>
</caisi:isModuleLoad>
<%
	dxResearchBeanHandler dxRes;
	ArrayList<String> flowsheets;
	Vector dxCodes;
%>
<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<caisirole:SecurityAccess accessName="measurements" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">
    <tr style="background-color:#BBBBBB;"><td>Case Management Flowsheets</td></tr>

    <!-- caisi flow sheet -->
    <tr><td>
        <%
            dxRes = new dxResearchBeanHandler(bean.demographicNo);
            dxCodes = dxRes.getActiveCodeListWithCodingSystem();
            flowsheets = MeasurementTemplateFlowSheetConfig.getInstance().getUniveralFlowsheets();
            for (String flowsheet : flowsheets) {
                MeasurementFlowSheet measurementFlowSheet = MeasurementTemplateFlowSheetConfig.getInstance().getFlowSheet(flowsheet);
                if (MeasurementHelper.flowSheetRequiresWork(bean.demographicNo, measurementFlowSheet)) {
                %>* <% }         
        %>
        <a href="javascript:void(0)"
           onClick="popupPage('<%=bsurl%>/oscarEncounter/oscarMeasurements/TemplateFlowSheet.jsp?demographic_no=<%=bean.demographicNo%>&template=<%=flowsheet%>','flowsheet')"><%=MeasurementTemplateFlowSheetConfig.getInstance().getDisplayName(flowsheet)%>
        </a><br/>
        <%}%>        
    </td></tr>
</caisirole:SecurityAccess>

<caisirole:SecurityAccess accessName="measurements" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">
    <tr style="background-color:#BBBBBB;"><td>Measurements</td></tr>
    <!-- measurement -->
    <tr><td>
        <%
            dxRes = new dxResearchBeanHandler(bean.demographicNo);
            dxCodes = dxRes.getActiveCodeListWithCodingSystem();
            flowsheets = MeasurementTemplateFlowSheetConfig.getInstance().getFlowsheetsFromDxCodes(dxCodes);
            for (String flowsheet : flowsheets) {
        %>
        <a href="javascript:void(0)"
           onClick="popupPage('<%=bsurl%>/oscarEncounter/oscarMeasurements/TemplateFlowSheet.jsp?demographic_no=<%=bean.demographicNo%>&template=<%=flowsheet%>','flowsheet')"><%=MeasurementTemplateFlowSheetConfig.getInstance().getDisplayName(flowsheet)%>
        </a>
        <%}%>

        <select name="measurementGroupSelect" class="ControlSelect" onchange="javascript:popUpMeasurements(500,1000,this,this.options[this.selectedIndex].value)">
            <option value="null" selected>-select group-</option>
            <%
                for(int j=0; j<bean.measurementGroupNames.size(); j++) {
                    String tmp = (String)bean.measurementGroupNames.get(j);
            %>
            <option value="<%=tmp%>"><%=tmp %></option>
            <%}%>
        </select>
    </td></tr>

    <!-- old measurements -->
    <tr><td>
        <a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarEncounter/oscarMeasurements/SetupHistoryIndex.do'); return false;" >-Old Measurements--</a>
    </td></tr>
</caisirole:SecurityAccess>
</caisi:isModuleLoad>

<tr style="background-color:#BBBBBB;"><td>Clinical Resources</td></tr>

<%
    String pAge = Integer.toString(oscar.util.UtilDateUtilities.calcAge(bean.yearOfBirth,bean.monthOfBirth,bean.dateOfBirth));
    oscar.oscarLab.ca.on.CommonLabResultData comLab = new oscar.oscarLab.ca.on.CommonLabResultData();
    java.util.ArrayList labs = comLab.populateLabResultsData(LoggedInInfo.getLoggedInInfoFromSession(request), "",bean.demographicNo, "", "","","U");
    session.setAttribute("casemgmt_labsbeans",labs);
%>
<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<tr><td>
    <a href="javascript:void(0)" ONCLICK ="popupPage('http://resource.oscarmcmaster.org/oscarResource/');return false;">resource</a><br>
</td></tr>

</caisi:isModuleLoad>
<tr><td>
    <a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/dms/documentReport.jsp?function=demographic&doctype=lab&functionid=<%=bean.demographicNo%>&curUser=<%=bean.curProviderNo%>');return false;">documents</a><br>
</td></tr>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<caisirole:SecurityAccess accessName="eform" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">
    <!-- eform -->
    <tr><td>
        <a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/eform/efmpatientformlist.jsp?demographic_no=<%=bean.demographicNo%>');return false;">E-Forms</a><br>
    </td></tr>
</caisirole:SecurityAccess>
</caisi:isModuleLoad>

<caisirole:SecurityAccess accessName="read ticklers" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">
    <tr><td>
        <a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/Tickler.do?method=filter&filter.demographic_webName=<%=StringEscapeUtils.escapeJavaScript(bean.getPatientLastName() +"," + bean.getPatientFirstName())%>&filter.demographic_no=<%=bean.demographicNo%>');return false;">View Tickler</a><br>
    </td></tr>
</caisirole:SecurityAccess>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<tr><td>
    <a href="javascript:void(0)" onClick="popupPage('<%=bsurl%>/oscarEncounter/calculators.jsp?sex=<%=bean.patientSex%>&age=<%=pAge%>'); return false;" >calculators</a><br>
</td></tr>

<caisirole:SecurityAccess accessName="lab" accessType="access" providerNo="<%=bean.providerNo%>" demoNo="<%=bean.demographicNo%>" programId="<%=pgId%>">

    <!-- lab result -->
    <tr><td>
        <select name="selectCurrentForms" class="ControlSelect" onChange="javascript:selectBox(this)" onMouseOver="javascript:window.status='View <%=bean.patientFirstName+" "+bean.patientLastName%>\'s lab results'; return true;">
            <option value="null" selected>-lab results-</option>
            <nested:iterate id="labrst" name="casemgmt_labsbeans" type="oscar.oscarLab.ca.on.LabResultData">
                <%

                    String lablable=labrst.dateTime+""+labrst.discipline;
                    String mdvalue=bsurl+"/oscarMDS/SegmentDisplay.jsp?providerNo="+bean.providerNo+"&segmentID="+labrst.segmentID+"&status="+labrst.reportStatus;
                    String cmvalue=bsurl+"/lab/CA/ON/CMLDisplay.jsp?providerNo="+bean.providerNo+"&segmentID="+labrst.segmentID;
                    String otvalue=bsurl+"/lab/CA/BC/labDisplay.jsp?segmentID="+labrst.segmentID+"&providerNo="+bean.providerNo;
                    if (labrst.isMDS()){
                %>
                <option value="<%=mdvalue%>"><%=lablable%></option>
                <% }else {if ( labrst.isCML()){
                %>
                <option value="<%=cmvalue%>" ><%=lablable%></option>
                <% }else {%>
                <option value="<%=otvalue%>" ><%=lablable%></option>
                <% }}%>
            </nested:iterate>
        </select>
        <%session.removeAttribute("casemgmt_labsbeans"); %>
    </td></tr>
</caisirole:SecurityAccess>
</caisi:isModuleLoad>

</form>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<form name="ksearch" method="get">
    <tr style="background-color:#BBBBBB;"><td>Internet Resources</td></tr>
    <tr><td>
        search for ...
        <br>
        <input type="text" name="keyword" value=""  onkeypress="return grabEnter(event)"/>
        <br>
        using ...
        <br>
        <select name="channel" class="ControlSelect">
            <option value="http://resource.oscarmcmaster.org/oscarResource/OSCAR_search/OSCAR_search_results?title=">OSCAR search</option>
            <option value="http://www.google.com/search?q=">Google</option>
            <option value="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?SUBMIT=y&CDM=Search&DB=PubMed&term=">Pubmed</option>
            <option value="http://search.nlm.nih.gov/medlineplus/query?DISAMBIGUATION=true&FUNCTION=search&SERVER2=server2&SERVER1=server1&PARAMETER=">Medline Plus</option>
            <option value="http://www.bnf.org/bnf/bnf/current/noframes/search.htm?n=50&searchButton=Search&q=">BNF.org</option>
        </select>
        <input type="button" name="button" value="Go" onClick="popupSearchPage(600,800,forms['ksearch'].channel.options[forms['ksearch'].channel.selectedIndex].value+urlencode(forms['ksearch'].keyword.value) ); return false;">
    </td></tr>
</form>
</caisi:isModuleLoad>
</table>
</div>
