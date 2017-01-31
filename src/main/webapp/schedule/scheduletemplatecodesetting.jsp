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
  if (session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*"
	errorPage="../appointment/errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<jsp:useBean id="dataBean" class="java.util.Properties" scope="page" />
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ScheduleTemplateCode" %>
<%@ page import="org.oscarehr.common.dao.ScheduleTemplateCodeDao" %>
<%
	ScheduleTemplateCodeDao scheduleTemplateCodeDao = SpringUtils.getBean(ScheduleTemplateCodeDao.class);
%>
<%
  int rowsAffected = 0;
  if (request.getParameter("dboperation") != null)
  {
	  if (request.getParameter("dboperation").compareTo(" Save ")==0 )
	  {
	    ScheduleTemplateCode code = scheduleTemplateCodeDao.getByCode(request.getParameter("code").toCharArray()[0]);
	    if(code != null) {
	    	scheduleTemplateCodeDao.remove(code.getId());
	    }

	    code = new ScheduleTemplateCode();
	    code.setCode(request.getParameter("code").toCharArray()[0]);
	    code.setDescription(request.getParameter("description"));
	    code.setDuration(request.getParameter("duration"));
	    code.setColor(request.getParameter("color"));
		code.setConfirm(request.getParameter("confirm"));
		code.setBookinglimit(Integer.parseInt(request.getParameter("bookinglimit")));
		scheduleTemplateCodeDao.persist(code);

	  }
	  if (request.getParameter("dboperation").equals("Delete") )
	  {
		  ScheduleTemplateCode code = scheduleTemplateCodeDao.getByCode(request.getParameter("code").toCharArray()[0]);
		    if(code != null) {
		    	scheduleTemplateCodeDao.remove(code.getId());
		    }
	  }
  }
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="schedule.scheduletemplatecodesetting.title" /></title>
<link rel="stylesheet" href="../web.css" />
<script language="JavaScript">
<!--

function validateNum() {
    var node = document.getElementById("bookinglimit");

    if( isNaN(node.value) ) {
        alert("<bean:message key="schedule.scheduletemplatecodesetting.msgCheckInput"/>");
        node.focus();
        return false;
    }

    return true;
}

function setfocus() {
  this.focus();
  document.addtemplatecode.code.focus();
  document.addtemplatecode.code.select();
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
function checkInput() {
	if(document.schedule.holiday_name.value == "") {
	  alert("<bean:message key="schedule.scheduletemplatecodesetting.msgCheckInput"/>");
	  return false;
	} else {
	  return true;
	}
}

//-->
</script>
<script type="text/javascript" src="../share/javascript/picker.js"></script>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" width="100%">
	<tr>
		<td width="50" bgcolor="#009966">&nbsp;</td>
		<td align="center">
		<table width="100%" border="0" cellspacing="0" cellpadding="5">
			<form name="deletetemplatecode" method="post"
				action="scheduletemplatecodesetting.jsp">
			<tr bgcolor="#CCFFCC">
				<td>
				<p align="right"><bean:message
					key="schedule.scheduletemplatecodesetting.formTemplateCode" />:</p>
				</td>
				<td><select name="code">
					<%
					List<ScheduleTemplateCode> stcs = scheduleTemplateCodeDao.findAll();
					Collections.sort(stcs,ScheduleTemplateCode.CodeComparator);
	
					for (ScheduleTemplateCode stc:stcs)
	{
%>
					<option value="<%=stc.getCode()%>"><%=stc.getCode()+" |"+stc.getDescription()%></option>
					<%
	}
%>
				</select> <input type="hidden" name="dboperation" value=" Edit ">
				<td><input type="submit"
					value='<bean:message key="schedule.scheduletemplatecodesetting.btnEdit"/>'></td>
			</tr>
			</form>
		</table>
		<table border="0" cellpadding="0" CELLSPACING="0" WIDTH="95%">
			<tr>
				<td width="50%" align="center">&nbsp;</td>
			</TR>
		</table>
		<table width="95%" border="1" cellspacing="0" cellpadding="2"
			bgcolor="silver">
			<form name="addtemplatecode" method="post"
				action="scheduletemplatecodesetting.jsp">
			<%
	boolean bEdit = request.getParameter("dboperation")!=null&&request.getParameter("dboperation").equals(" Edit ");
	if (bEdit)
	{
		ScheduleTemplateCode stc = scheduleTemplateCodeDao.findByCode(request.getParameter("code"));
     	if(stc != null) {
		
			dataBean.setProperty("code", String.valueOf(stc.getCode()) );
			dataBean.setProperty("description", stc.getDescription() );
			dataBean.setProperty("duration", stc.getDuration()==null?"":stc.getDuration() );
			dataBean.setProperty("color", stc.getColor()==null?"":stc.getColor() );
			dataBean.setProperty("confirm", stc.getConfirm()==null?"No":stc.getConfirm() );
                        dataBean.setProperty("bookinglimit", String.valueOf(stc.getBookinglimit()));
		}
	}
%>

			<tr bgcolor="#FOFOFO" align="center">
				<td colspan=2><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"
					color="red"><bean:message
					key="schedule.scheduletemplatecodesetting.msgApptTemplateCode" /></font></td>
			</tr>
			<tr bgcolor='ivory'>
				<td><font color="red"><bean:message
					key="schedule.scheduletemplatecodesetting.formCode" />:</font></td>
				<td><input type="text" name="code" size="1" maxlength="1"
					<%=bEdit?("value='"+dataBean.getProperty("code")+"'"):"value=''"%>></td>
			</tr>
			<tr bgcolor='ivory'>
				<td><font color="red"><bean:message
					key="schedule.scheduletemplatecodesetting.formDescription" />:</font></td>
				<td><input type="text" name="description" size="25"
					<%=bEdit?("value='"+dataBean.getProperty("description")+"'"):"value=''"%>></td>
			</tr>
			<tr bgcolor='ivory'>
				<td><font color="red"><bean:message
					key="schedule.scheduletemplatecodesetting.formDuration" />:</font></td>
				<td><input type="text" name="duration" size="3" maxlength="3"
					<%=bEdit?("value='"+dataBean.getProperty("duration")+"'"):"value=''"%>>
				mins.</td>
			</tr>
			<tr bgcolor='ivory'>
				<td><font color="red"><bean:message
					key="schedule.scheduletemplatecodesetting.formColor" />:</font></td>
				<td><input type="text" name="color" id="color" size="10" maxlength="10"
					<%=bEdit?("value='"+dataBean.getProperty("color")+"'"):"value=''"%>> <a href="javascript:TCP.popup(document.forms['addtemplatecode'].elements['color']);"><img width="15" height="13" border="0" src="../images/sel.gif"></a>
				<bean:message
					key="schedule.scheduletemplatecodesetting.msgColorExample" /></td>
			</tr>
                        <tr bgcolor='ivory'>
				<td><font color="red"><bean:message
					key="schedule.scheduletemplatecodesetting.formBookingLimit" />:</font></td>
				<td><input type="text" id="bookinglimit" name="bookinglimit" size="10"
					<%=bEdit?("value='"+dataBean.getProperty("bookinglimit")+"'"):"value='1'"%>></td>
			</tr>
			<tr bgcolor='ivory'>
				<td><font color="red">Limit Type:</font></td>
				<td>
				<input type="radio" name="confirm" value="No"
					<%=(bEdit? (dataBean.getProperty("confirm").startsWith("N")? "checked" : "") : "checked")%>>Off
				<input type="radio" name="confirm" value="Yes"
					<%=((bEdit && dataBean.getProperty("confirm").equals("Yes"))? "checked" : "")%>>Warning
				<!-- <input type="radio" name="confirm" value="Str"
					<%=(bEdit? (dataBean.getProperty("confirm").startsWith("Str")? "checked" : "") : "checked")%>>Strict
				not implimented --> <br>
				<input type="radio" name="confirm" value="Day"
					<%=(bEdit? (dataBean.getProperty("confirm").equals("Day")? "checked" : "") : "checked")%>>Same Day
				<input type="radio" name="confirm" value="Wk"
					<%=(bEdit? (dataBean.getProperty("confirm").equals("Wk")? "checked" : "") : "checked")%>>Same Week					
			   <input type="radio" name="confirm" value="Onc"
					<%=(bEdit? (dataBean.getProperty("confirm").equals("Onc")? "checked" : "") : "checked")%>>On-Call Urgent

				</td>
			</tr>
		</table>
		<table width="95%" border="0" cellspacing="0" cellpadding="2"
			bgcolor="silver">
			<tr bgcolor="#FOFOFO">
				<input type="hidden" name="dboperation" value="" />
				<td><input type="button"
					onclick="document.forms['addtemplatecode'].dboperation.value='Delete'; document.forms['addtemplatecode'].submit();"
					value='<bean:message key="schedule.scheduletemplatecodesetting.btnDelete"/>'></td>
				<td align="right"><input type="button"
					onclick="if( validateNum() ) { document.forms['addtemplatecode'].dboperation.value=' Save '; document.forms['addtemplatecode'].submit();}"
					value='<bean:message key="schedule.scheduletemplatecodesetting.btnSave"/>'>
				<input type="button" name="Button"
					value='<bean:message key="global.btnExit"/>'
					onClick="window.close()"></td>
			</tr>
			</form>
		</table>
		<p align='left'>&nbsp; <bean:message
			key="schedule.scheduletemplatecodesetting.msgCode" /><br>
		&nbsp; <bean:message
			key="schedule.scheduletemplatecodesetting.msgDescription" /><br>
		&nbsp; <bean:message
			key="schedule.scheduletemplatecodesetting.msgDuration" /><br>
		&nbsp; <bean:message
			key="schedule.scheduletemplatecodesetting.msgColor" /><br>
                &nbsp; <bean:message
			key="schedule.scheduletemplatecodesetting.msgBookingLimit" /><br>
		
                    </p>
		</td>
	</tr>
</table>
</form>
</body>
</html:html>
