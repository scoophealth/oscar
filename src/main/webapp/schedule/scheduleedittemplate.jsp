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

<%

%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*"
	errorPage="../appointment/errorpage.jsp"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="oscar.OscarProperties"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<jsp:useBean id="myTempBean" class="oscar.ScheduleTemplateBean"	scope="page" />
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ScheduleTemplate" %>
<%@ page import="org.oscarehr.common.model.ScheduleTemplatePrimaryKey" %>
<%@ page import="org.oscarehr.common.dao.ScheduleTemplateDao" %>
<%@ page import="org.oscarehr.common.model.ScheduleTemplateCode" %>
<%@ page import="org.oscarehr.common.dao.ScheduleTemplateCodeDao" %>
<%
	ScheduleTemplateDao scheduleTemplateDao = SpringUtils.getBean(ScheduleTemplateDao.class);
	ScheduleTemplateCodeDao scheduleTemplateCodeDao = SpringUtils.getBean(ScheduleTemplateCodeDao.class);
%>
<% //save or delete the settings
  int rowsAffected = 0;
  OscarProperties props = OscarProperties.getInstance();
  int STEP = request.getParameter("step")!=null&&!request.getParameter("step").equals("")?Integer.parseInt(request.getParameter("step")):(props.getProperty("template_time", "").length()>0?Integer.parseInt(props.getProperty("template_time", "")):15);
  if(request.getParameter("dboperation")!=null && (request.getParameter("dboperation").compareTo(" Save ")==0 || request.getParameter("dboperation").equals("Delete") ) ) {
    String pre = request.getParameter("providerid").equals("Public")&&!request.getParameter("name").startsWith("P:")?"P:":"" ;

    scheduleTemplateDao.remove(new ScheduleTemplatePrimaryKey(request.getParameter("providerid"),request.getParameter("name")));

    if(request.getParameter("dboperation")!=null && request.getParameter("dboperation").equals(" Save ") ) {
    	ScheduleTemplate scheduleTemplate = new ScheduleTemplate();
    	scheduleTemplate.setId(new ScheduleTemplatePrimaryKey());
    	scheduleTemplate.getId().setName(pre + request.getParameter("name"));
    	scheduleTemplate.getId().setProviderNo(request.getParameter("providerid"));
    	scheduleTemplate.setSummary(request.getParameter("summary"));
    	scheduleTemplate.setTimecode(SxmlMisc.createDataString(request,"timecode","_", 300));
    	scheduleTemplateDao.persist(scheduleTemplate);
    }
  }

%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="schedule.scheduleedittemplate.title" /></title>
<!--link rel="stylesheet" href="../web.css" /-->

<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.addtemplatecode.name.focus();
  document.addtemplatecode.name.select();
}
function go() {
  var s = document.reportform.startDate.value.replace('/', '-');
  s = s.replace('/', '-');
  var e = document.reportform.endDate.value.replace('/', '-');
  e = e.replace('/', '-');
  var u = 'reportedblist.jsp?startDate=' + s + '&endDate=' + e;
	popupPage(600,750,u);
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
function checkInput() {
	if(document.schedule.holiday_name.value == "") {
	  alert('<bean:message key="schedule.scheduleedittemplate.msgCheckInput"/>');
	  return false;
	} else {
	  return true;
	}
}
function changeGroup(s) {
	var newGroupNo = s.options[s.selectedIndex].value;
	newGroupNo = s.options[s.selectedIndex].value;
	self.location.href = "scheduleedittemplate.jsp?providerid=<%=request.getParameter("providerid")%>&providername=<%=java.net.URLEncoder.encode(StringEscapeUtils.escapeJavaScript(request.getParameter("providername")),"UTF-8")%>&step=" + newGroupNo;

}
//-->
</script>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" width="100%">
	<tr>
		<td width="50" bgcolor="#009966">&nbsp;</td>
		<td align="center">

		<form name="addtemplatecode1" method="post"
			action="scheduleedittemplate.jsp">
		<table width="100%" border="0" cellspacing="0" cellpadding="5">
			<input type="hidden" name="dboperation" value="">
			<input type="hidden" name="step" value="">
			<tr bgcolor="#CCFFCC">
				<td nowrap>
				<p><bean:message
					key="schedule.scheduleedittemplate.formProvider" />: <%=request.getParameter("providername")%></p>
				</td>
				<td align='right'><select name="name">
					<%
   boolean bEdit=request.getParameter("dboperation")!=null&&request.getParameter("dboperation").equals(" Edit ")?true:false;
  
   if(bEdit) {
	   for(ScheduleTemplate st:scheduleTemplateDao.findByProviderNoAndName(request.getParameter("providerid"), request.getParameter("name"))) {
     	 myTempBean.setScheduleTemplateBean(st.getId().getProviderNo(),st.getId().getName(),st.getSummary(),st.getTimecode() );
     }
   }
   
   for(ScheduleTemplate st:scheduleTemplateDao.findByProviderNo(request.getParameter("providerid"))) {
   
	%>
					<option value="<%=st.getId().getName()%>"><%=st.getId().getName()+" |"+st.getSummary()%></option>
					<%
     }
	%>
				</select> <input type="hidden" name="providerid"
					value="<%=request.getParameter("providerid")%>"> <input
					type="hidden" name="providername"
					value="<%=request.getParameter("providername")%>">
				<td align='right'><input type="button"
					value='<bean:message key="schedule.scheduleedittemplate.btnEdit"/>'
					onclick="document.forms['addtemplatecode1'].dboperation.value=' Edit '; document.forms['addtemplatecode1'].submit();"></td>
			</tr>
		</table>
		</form>

		<form name="addtemplatecode2" method="post"
			action="scheduleedittemplate.jsp">
		<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="95%">
			<tr>
				<td width="50%" align="right">&nbsp; <select name="step1"
					onChange="changeGroup(this)">
					<% for(int i=5; i<35; i+=5) {
      			if(i==25) continue;%>
					<option value="<%=i%>" <%=STEP==i? "selected":""%>><%=i%></option>
					<% }	%>
				</select> <input type="hidden" name="providerid"
					value="<%=request.getParameter("providerid")%>"> <input
					type="hidden" name="providername"
					value="<%=request.getParameter("providername")%>"> <input
					type="button" value='Go'
					onclick="document.forms['addtemplatecode1'].step.value=document.forms[1].step1.options[document.forms[1].step1.selectedIndex].value; document.forms['addtemplatecode1'].submit();"></td>
				</td>
			</tr>
		</table>
		</form>
		<form name="addtemplatecode" method="post"
			action="scheduleedittemplate.jsp">
		<table width="95%" border="1" cellspacing="0" cellpadding="2"
			bgcolor="silver">
			<tr bgcolor="#FOFOFO" align="center">
				<td colspan=3><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"
					color="red"><bean:message
					key="schedule.scheduleedittemplate.msgMainLabel" /></font></td>
			</tr>
			<tr bgcolor='ivory'>
				<td nowrap><bean:message
					key="schedule.scheduleedittemplate.formTemplateName" />:</td>
				<td><input type="text" name="name" size="30" maxlength="20"
					<%=bEdit?("value='"+myTempBean.getName()+"'"):"value=''"%>>
				<font size='-2'><bean:message
					key="schedule.scheduleedittemplate.msgLessTwentyChars" /></font></td>
				<td></td>
			</tr>
			<tr bgcolor='ivory'>
				<td><bean:message
					key="schedule.scheduleedittemplate.formSummary" />:</td>
				<td><input type="text" name="summary" size="30" maxlength="30"
					<%=bEdit?("value='"+myTempBean.getSummary()+"'"):"value=''"%>></td>
				<td nowrap><a href=#
					title="	<%
					
					List<ScheduleTemplateCode> stcs = scheduleTemplateCodeDao.findAll();
					Collections.sort(stcs,ScheduleTemplateCode.CodeComparator);
					
   for (ScheduleTemplateCode stc:stcs) {   %>
 <%=String.valueOf(stc.getCode())+" - "+stc.getDescription()%>  <%}	%>
             "><bean:message
					key="schedule.scheduleedittemplate.formTemplateCode" /></a></td>
			</tr>
			<tr bgcolor='ivory'>
				<td colspan='3' align='center'>
				<table>
					<%
             int cols=4, rows=6, step=bEdit?myTempBean.getStep():STEP;

             int icols=60/step, n=0;
             for(int i=0; i<rows; i++) {
             %>
					<tr>
						<% for(int j=0; j<cols; j++) { %>
						<td bgcolor='silver'><%=(n<10?"0":"")+n+":00"%></td>
						<%   for(int k=0; k<icols; k++) { %>
						<td><input type="text"
							name="timecode<%=i*(cols*icols)+j*icols+k%>" size="1"
							maxlength="1"
							<%=bEdit?("value='"+myTempBean.getTimecodeCharAt(i*(cols*icols)+j*icols+k)+"'"):"value=''"%>></td>
						<%   }
                n++;
                }%>
					</tr>
					<%} %>
				</table>
				</td>
			</tr>
		</table>


		<table width="100%" border="0" cellspacing="0" cellpadding="2"
			bgcolor="silver">
			<tr bgcolor="#FOFOFO">
				<td><input type="button"
					value='<bean:message key="schedule.scheduleedittemplate.btnDelete"/>'
					onclick="document.forms['addtemplatecode'].dboperation.value='Delete'; document.forms['addtemplatecode'].submit();"></td>
				<td align="right"><input type="hidden" name="providerid"
					value="<%=request.getParameter("providerid")%>"> <input
					type="hidden" name="providername"
					value="<%=request.getParameter("providername")%>"> <input
					type="hidden" name="dboperation" value=""> <input
					type="button"
					value='<bean:message key="schedule.scheduleedittemplate.btnSave"/>'
					onclick="document.forms['addtemplatecode'].dboperation.value=' Save '; document.forms['addtemplatecode'].submit();">
				<input type="button" name="Button"
					value='<bean:message key="global.btnExit"/>'
					onclick="window.close()"></td>
			</tr>
		</table>
		</form>

		</td>
	</tr>
</table>

</body>
</html:html>
