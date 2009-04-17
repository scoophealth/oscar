<%@ page import="oscar.*, org.oscarehr.util.*"%>

<logic:equal name="infirmaryView_isOscar" value="false">
<%	
	session.setAttribute("case_program_id", session.getAttribute(SessionConstants.CURRENT_PROGRAM_ID));
	java.util.Date todayDate=new java.util.Date();
	todayDate.setHours(23);
	todayDate.setMinutes(59);
	todayDate.setSeconds(59);
	
	if (((String)session.getAttribute(SessionConstants.CURRENT_PROGRAM_ID)).equalsIgnoreCase("0"))
	{%>
	<p><b>No Assigned Program.</b></p>
	<%}else
	if (session.getAttribute("infirmaryView_date")!=null && todayDate.before((java.util.Date) session.getAttribute("infirmaryView_date")))
	{ %>
	<p><b>Future clients list is unavailable.</b></p>
	<%}
	else{
	if (((java.util.List) session.getAttribute("infirmaryView_demographicBeans")).size()==0)
	{ %>
	<p><b>no client in this program.</b></p>
	<%if(session.getAttribute("archiveView")==null || session.getAttribute("archiveView")=="false") { %>
	<b>You are in Case Management View! </b>
	&nbsp; <a href="<html:rewrite action="/ArchiveView.do"/>">Click here for Archive View</a>
	<%} else {%>
	<b>You are in the archive view ! </b>&nbsp; <a
		href="<html:rewrite action="/ArchiveView.do"/>?method=cmm">Back to
	Case Management View</a>
	<%} %>
	<%}else{
	int k=0;

	%>
	<table border="1" cellpadding="0"
		bgcolor="<%=userAvail?"#486ebd":"silver"%>" cellspacing="0"
		width="100%">
		<tr>
			<td>
			<%if(session.getAttribute("archiveView")==null || session.getAttribute("archiveView")=="false") { %>
			<b>You are in Case Management View! </b> &nbsp; <a href="<html:rewrite action="/ArchiveView.do"/>">Click here for Archive View</a>
			<%} else {%> <b>You are in Archive View ! </b>&nbsp; <a
				href="<html:rewrite action="/ArchiveView.do"/>?method=cmm">Back
			to Case Management View</a> <%} %>
			</td>
		</tr>
		<tr>
			<td width='1' title='null'><font color='white'></font></td>
		</tr>
	</table>

	<table border="1" cellpadding="0"
		bgcolor="<%=userAvail?"#486ebd":"silver"%>" cellspacing="0"
		width="100%">
		<logic:iterate id="de" name="infirmaryView_demographicBeans"
			type="org.apache.struts.util.LabelValueBean">
			<tr>
				<td width='1' title='null'><font color='white'></font></td>
				<% k++;
java.util.Date apptime=new java.util.Date();
int demographic_no = (new Integer(de.getValue())).intValue();
String demographic_name=de.getLabel();

%><c:if test="${empty sessionScope.archiveView or sessionScope.archiveView != true}"><%
paramTickler[0]=String.valueOf(demographic_no);
paramTickler[1]=MyDateFormat.getSysDate(strDate); //year+"-"+month+"-"+day;//e.g."2001-02-02";
List<Map> ticklerResultList = oscarSuperManager.find("providerDao", "search_tickler", paramTickler);
tickler_no = "";
tickler_note="";
for (Map tickler : ticklerResultList) {
    tickler_no = String.valueOf(tickler.get("tickler_no"));
    tickler_note = tickler.get("message")==null?tickler_note:tickler_note + "\n" + tickler.get("message");
}
%></c:if><%

ver = "";
roster = "";
study_no = new StringBuffer("");
study_link = new StringBuffer("");
studyDescription = new StringBuffer("");
String reason ="";
String notes = "";
String status = "T";
bFirstTimeRs=true;
as.setApptStatus(status);

String rsAppointNO="0";
if ((k/2)*2==k){ %>
				<td bgcolor='#FDFEC7' rowspan="1" nowrap>
				<%}else{ %>
				
				<td bgcolor='#FFBBFF' rowspan="1" nowrap>
				<%}%> <img src="../images/todo.gif" border="0" height="10"
					title="appointment"> <% if(demographic_no==0) {   
    if (tickler_no.compareTo("") != 0) { %> <caisi:isModuleLoad
					moduleName="ticklerplus" reverse="true">
					<a href="#"
						onClick="popupPage(700,1000, '../tickler/ticklerDemoMain.jsp?demoview=0');return false;"
						title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>">
					<font color="red">!</font></a>
				</caisi:isModuleLoad> <caisi:isModuleLoad moduleName="ticklerplus">
					<a href="#"
						onClick="popupPage(700,1000, '../ticklerPlus/index.jsp?demoview=0');return false;"
						title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>">
					<font color="red">!</font></a>
				</caisi:isModuleLoad> <%  } %> <b>.<%=de.getLabel()%></b> <%}else {
    if (tickler_no.compareTo("") != 0) { %> <caisi:isModuleLoad
					moduleName="ticklerplus" reverse="true">
					<a href="#"
						onClick="popupPage(700,1000, '../tickler/ticklerDemoMain.jsp?demoview=<%=demographic_no%>');return false;"
						title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>">
					<font color="red">!</font></a>
				</caisi:isModuleLoad> <caisi:isModuleLoad moduleName="ticklerplus">
					<a href="#"
						onClick="popupPage(700,1000, '../Tickler.do?method=filter&filter.client=<%=demographic_no %>');return false;"
						title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>">
					<font color="red">!</font></a>
				</caisi:isModuleLoad> <%  } %> <!-- doctor code block --> <% if(bShowDocLink) { %> <!-- security:oscarSec roleName="<%--=roleName$--%>" objectName="_appointment.doctorLink" rights="r" -->
				<% if (study_no.toString().compareTo("") != 0) {%> <a href="#"
					onClick="popupPage(700,1000, '../form/study/forwardstudyname.jsp?study_link=<%=study_link.toString()%>&demographic_no=<%=demographic_no%>&study_no=<%=study_no%>');return false;"
					title="<bean:message key="provider.appointmentProviderAdminDay.study"/>: <%=UtilMisc.htmlEscape(studyDescription.toString())%>"><%="<font color='"+studyColor+"'>"+studySymbol+"</font>"%></a>
				<%} %> <% if (ver.toString().compareTo("##") == 0){%><a href="#"
					title="<bean:message key="provider.appointmentProviderAdminDay.versionMsg"/> <%=UtilMisc.htmlEscape(ver)%>">
				<font color="red">*</font>
				<%}%> <% if (roster.equalsIgnoreCase("FS")){%> <a href="#"
					title="<bean:message key="provider.appointmentProviderAdminDay.rosterMsg"/> <%=UtilMisc.htmlEscape(roster)%>"><font
					color="red">$</font>
				<%}%> <!-- /security:oscarSec --> <% } %> <!-- doctor code block --> <a
					href="#"
					onclick="location.href='../PMmodule/ClientManager.do?id=<%=demographic_no%>'">
				<%=de.getLabel()%></a> <!-- doctor code block --> <% if(bShowEncounterLink) {
	String  eURL = "../oscarEncounter/IncomingEncounter.do?providerNo="+curUser_no+"&appointmentNo="+rsAppointNO+"&demographicNo="+demographic_no+"&curProviderNo="+curProvider_no[nProvider]+"&reason="+URLEncoder.encode(reason)+"&encType="+URLEncoder.encode("face to face encounter with client","UTF-8")+"&userName="+URLEncoder.encode( userfirstname+" "+userlastname)+"&curDate="+curYear+"-"+curMonth+"-"+curDay+"&appointmentDate="+year+"-"+month+"-"+day+"&startTime="+apptime.getHours()+":"+apptime.getMinutes()+"&status="+status;%>
				<!-- open CME in current window
        <a href="#" onclick="location.href='../oscarSurveillance/CheckSurveillance.do?demographicNo=<%=demographic_no%>&proceed=<%=URLEncoder.encode(eURL)%>'" title="<bean:message key="global.encounter"/>">  
        --> <a href=#
					onClick="popupPage(710, 1024,'../oscarSurveillance/CheckSurveillance.do?demographicNo=<%=demographic_no%>&proceed=<%=URLEncoder.encode(eURL)%>');return false;"
					title="<bean:message key="global.encounter"/>"> |<bean:message
					key="provider.appointmentProviderAdminDay.btnE" /></a> <% } %> <!-- billing code block -->
				<security:oscarSec roleName="<%=roleName$%>" objectName="_billing"
					rights="r">

					<%= bShortcutForm?"<a href=# onClick='popupPage2( \"../form/forwardshortcutname.jsp?formname="+formName+"&demographic_no="+demographic_no+"\")' title='form'>|"+formNameShort+"</a>" : ""%>
					<%= bShortcutForm2?"<a href=# onClick='popupPage2( \"../form/forwardshortcutname.jsp?formname="+formName2+"&demographic_no="+demographic_no+"\")' title='form'>|"+formName2Short+"</a>" : ""%>
					<% if(status.indexOf('B')==-1) {
	//java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.action.Action.LOCALE_KEY);
	if (vLocale.getCountry().equals("BR")) { %>
					<!-- get rid of billing link from case management view because of caisi bug 925 -->
					<!-- <a href=# onClick='popupPage(700,1000, "../oscar/billing/procedimentoRealizado/init.do?appId=<%=rsAppointNO%>");return false;' title="Faturamento">|FAT|</a> -->
					<%}else {%>
					<!-- <a href=# onClick='popupPage(700,1000, "../billing.do?billRegion=<%=URLEncoder.encode(prov)%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=rsAppointNO%>&demographic_name=<%=URLEncoder.encode(de.getLabel())%>&status=<%=status%>&demographic_no=<%=demographic_no%>&providerview=<%=curProvider_no[nProvider]%>&user_no=<%=curUser_no%>&apptProvider_no=<%=curProvider_no[nProvider]%>&appointment_date=<%=year+"-"+month+"-"+day%>&start_time=<%=apptime.getHours()+":"+apptime.getMinutes()%>&bNewForm=1");return false;' title="<bean:message key="global.billing"/>">|<bean:message key="provider.appointmentProviderAdminDay.btnB"/></a>  -->
					<%}
    } %>
				</security:oscarSec> <!-- billing code block --> <security:oscarSec
					roleName="<%=roleName$%>" objectName="_masterLink" rights="r">
					<% if (vLocale.getCountry().equals("BR")) {%>
					<a href=#
						onClick="popupPage2('../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail_ptbr');return false;"
						title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>"><bean:message
						key="provider.appointmentProviderAdminDay.btnM" /></a>
					<%}else{%>
					<a href=#
						onClick="popupPage2('../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail');return false;"
						title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>">|<bean:message
						key="provider.appointmentProviderAdminDay.btnM" /></a>
					<%}%>
				</security:oscarSec> <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
					<% if (!vLocale.getCountry().equals("BR")) { %>
					<!-- doctor code block -->
					<security:oscarSec roleName="<%=roleName$%>"
						objectName="_appointment.doctorLink" rights="r">
						<a href=#
							onClick="popupOscarRx(700,960,'../oscarRx/choosePatient.do?providerNo=<%=curUser_no%>&demographicNo=<%=demographic_no%>')">|<bean:message
							key="global.rx" /></a>
						<oscar:oscarPropertiesCheck property="SHOW_APPT_REASON"
							value="yes">| <b><%=reason%></b>
						</oscar:oscarPropertiesCheck>
					</security:oscarSec>
					<% } %>
					<!-- doctor code block -->
				</caisi:isModuleLoad> <%}%> <% if(isBirthday(monthDay,demBday)){%> | <img
					src="../images/cake.gif" height="20" /> <%}%>
				
				<td width='1'></td>
			</tr>
		</logic:iterate>
	</table>
	<%
	}
}%>
</logic:equal>
