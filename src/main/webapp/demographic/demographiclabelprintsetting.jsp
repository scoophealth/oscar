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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*" errorPage="../appointment/errorpage.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>

<%@page import="org.oscarehr.common.model.Demographic" %>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.common.model.ProviderData"%>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao"%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%
	if(session.getValue("user") == null)  response.sendRedirect("../logout.jsp");
	String curProvider_no = (String) session.getAttribute("user");

	java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); 

	DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
 	ProviderDataDao providerDao = SpringUtils.getBean(ProviderDataDao.class);
%>



<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="demographic.demographiclabelprintsetting.title" /></title>
<script src="../share/javascript/prototype.js" language="javascript" type="text/javascript"></script>

<script type="text/javascript">

function onNewPatient() {
  document.labelprint.label1no.value="1";
  document.labelprint.label1checkbox.checked=true;
  document.labelprint.label2checkbox.checked=true;
  document.labelprint.label3checkbox.checked=true;
  document.labelprint.label2no.value="6";
  document.labelprint.label3no.value="0";
}
function checkTotal() {
  var total = 0+ document.labelprint.label1no.value + document.labelprint.label2no.value + document.labelprint.label3no.value + document.labelprint.label4no.value + document.labelprint.label5no.value;
  if(total>7) return false;
  return true;
}

<%-- RJ added code to copy text to clipboard in firefox 07/06/2006 --%>
function ClipBoard1(spanId) {

	var browser = navigator.userAgent.toLowerCase();

	if( browser.indexOf('msie') > -1 )
	{			
		document.getElementById("text1").innerText = document.getElementById(spanId).innerText;
		//alert("clip");
		Copied = document.getElementById("text1").createTextRange();
		//alert("clip");
		Copied.execCommand("RemoveFormat");
		Copied.execCommand("Copy");
	}
	else if( browser.indexOf('safari') > -1 )
	{
		alert("Copy to clipboard is not supported in Safari");
	}
	else if( browser.indexOf('firefox') > -1 )
	{

		//need privelege to access clipboard
		//We'll catch exception if security prevents access and tell user how to correct the problem
		try
		{
			netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
		}
		catch(ex)
		{
			alert("Your browser has restricted access to clipboard\n" + 
			       "Please type \"about:config\" in location bar\n" + 
			       "and search for \"signed.applets.codebase_principal_support\"\n" +
			       "then set value to true.  You will then be able to copy to clipboard");
			return;
		}

		var strText = document.getElementById(spanId).innerHTML;
		
		//we want to keep line format so replace <br> with \r\n
		strText = strText.replace(/\t/g, "");
		strText = strText.replace(/<br>/g,"\r\n");
		
		//get rid of html tags and &nbsp;
		strText = strText.stripTags();
		strText = strText.replace(/&nbsp;/g," ");

		//object to hold copy of string 
		var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
		str.data = strText;

		//transfer object holds string. xfer obj is placed on clipboard
		var trans = Components.classes["@mozilla.org/widget/transferable;1"].createInstance(Components.interfaces.nsITransferable);
		trans.addDataFlavor("text/unicode");
		trans.setTransferData("text/unicode",str,strText.length * 2); 

		//xfer object to clipboard
		var clipid = Components.interfaces.nsIClipboard;
		var clip = Components.classes["@mozilla.org/widget/clipboard;1"].getService(clipid);
		clip.setData(trans,null,clipid.kGlobalClipboard);

	}
}
function ClipBoard2() {
	document.getElementById("text1").innerText = document.getElementById("copytext").innerText;
	//alert("cl ip");
	Copied = document.getElementById("text1").createTextRange();
	//alert("clip");
	Copied.execCommand("RemoveFormat");
	Copied.execCommand("Copy");
}
function ClipBoard3() {
	document.getElementById("text1").innerText = document.getElementById("copytext").innerText;
	//alert("cl ip");
	Copied = document.getElementById("text1").createTextRange();
	//alert("clip");
	Copied.execCommand("RemoveFormat");
	Copied.execCommand("Copy");
}
function ClipBoard4() {
	document.getElementById("text1").innerText = document.getElementById("copytext").innerText;
	//alert("cl ip");
	Copied = document.getElementById("text1").createTextRange();
	//alert("clip");
	Copied.execCommand("RemoveFormat");
	Copied.execCommand("Copy");
}

</script>
</head>
<body bgcolor="white" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message key="demographic.demographiclabelprintsetting.msgMainLabel" /></font></th>
	</tr>
</table>

<%
	GregorianCalendar now=new GregorianCalendar();  int curYear = now.get(Calendar.YEAR);  int curMonth = (now.get(Calendar.MONTH)+1);  int curDay = now.get(Calendar.DAY_OF_MONTH);
	int age=0, dob_year=0, dob_month=0, dob_date=0;
	String first_name="",last_name="",chart_no="",address="",city="",province="",postal="",phone="",phone2="",dob="",sex="",hin="";
	String refDoc = "";
	String providername = "";
	String demoNo = request.getParameter("demographic_no");
	
	Demographic demo = demographicDao.getDemographic(demoNo);
	if(demo==null) { 
%>
		<bean:message key="demographic.demographiclabelprintsetting.msgFailed" />
<%
	}
	else {
		ProviderData provider = providerDao.findByProviderNo(demo.getProviderNo());
		if(provider != null) {
			providername = provider.getLastName() + "," + provider.getFirstName();
		}
		
		first_name = Misc.JSEscape(demo.getFirstName());
		last_name = Misc.JSEscape(demo.getLastName());
		sex = demo.getSex();
		dob_year = Integer.parseInt(demo.getYearOfBirth());
		dob_month = Integer.parseInt(demo.getMonthOfBirth());
		dob_date = Integer.parseInt(demo.getDateOfBirth());
		if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);
		dob=dob_year + "/" + demo.getMonthOfBirth() + "/" + demo.getDateOfBirth();
		
		if (demo.getChartNo()!=null) chart_no = demo.getChartNo();
		if (demo.getAddress()!=null) address = Misc.JSEscape(demo.getAddress());
		if (demo.getCity()!=null) city = demo.getCity();
		if (demo.getProvince()!=null) province = demo.getProvince();
		if (demo.getPostal()!=null) postal = demo.getPostal();
		if (demo.getPhone()!=null) phone = demo.getPhone();
		if (demo.getPhone2()!=null) phone2 = demo.getPhone2();
		if (demo.getHin()!=null) hin = "HN "+demo.getHcType()+" "+demo.getHin()+" "+demo.getVer();
		if (demo.getFamilyDoctor()!=null) refDoc = SxmlMisc.getXmlContent(demo.getFamilyDoctor(),"rd");
	}
	phone2 = (phone2==null || phone2.equals(""))?"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;":(phone2+"&nbsp;") ;
%>

<form method="post" name="labelprint" action="demographicprintdemographic.jsp">
<table border="0" cellpadding="0" cellspacing="3" width="100%">
	<tr bgcolor="gold" align="center">
		<td><bean:message key="demographic.demographiclabelprintsetting.msgLabel" /></td>
		<td><bean:message key="demographic.demographiclabelprintsetting.msgNumeberOfLabel" />
		<td><bean:message key="demographic.demographiclabelprintsetting.msgLocation" /> 
			<input type="hidden" name="address" value="<%=address%>"> 
			<input type="hidden" name="chart_no" value="<%=chart_no%>"> 
			<input type="hidden" name="city" value="<%=city%>"> 
			<input type="hidden" name="dob" value="<%=dob%>"> 
			<input type="hidden" name="first_name" value="<%=first_name%>"> 
			<input type="hidden" name="hin" value="<%=hin%>"> 
			<input type="hidden" name="last_name" value="<%=last_name%>"> 
			<input type="hidden" name="phone" value="<%=phone%>"> 
			<input type="hidden" name="phone2" value="<%=phone2%>"> 
			<input type="hidden" name="postal" value="<%=postal%>"> 
			<input type="hidden" name="providername" value="<%=providername%>">
			<input type="hidden" name="province" value="<%=province%>"> 
			<input type="hidden" name="sex" value="<%=sex%>"> 
			<input type="hidden" name="age" value="<%=age%>">
		</td>
	</tr>
	<tr>
		<td align="center">
		<table width="90%" border="1" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
			<tr>
				<td><font face="Courier New, Courier, mono" size="2">
				<span id="copytext1"> <b><%=last_name%>,&nbsp;<%=first_name%></b><br>
				&nbsp;&nbsp;&nbsp;&nbsp;<%=hin%><br>
				&nbsp;&nbsp;&nbsp;&nbsp;<%=dob%>&nbsp;<%=sex%><br>
				<br>
				<b><%=last_name%>,&nbsp;<%=first_name%></b><br>
				&nbsp;&nbsp;&nbsp;&nbsp;<%=hin%><br>
				&nbsp;&nbsp;&nbsp;&nbsp;<%=dob%>&nbsp;<%=sex%><br>
				</span></font></td>
			</tr>
		</table>
		</td>
		<td align="center" bgcolor="#CCCCCC"><a href="#" onClick="onNewPatient()">
			<bean:message key="demographic.demographiclabelprintsetting.btnNewPatientLabel" /></a><br>
			<input type="button" onClick="ClipBoard1('copytext1');" value="Copy to Clipboard" /> 
			<input type="checkbox" name="label1checkbox" value="checked"> 
			<input type="text" name="label1no" size="2" maxlength="2" value="<%= oscarVariables.getProperty("label.1no","1") %>" />
		</td>
		<td bgcolor="#999999" rowspan="5" valign="middle" align="right">
			<p><bean:message key="demographic.demographiclabelprintsetting.formLeft" />: 
			<input type="text" name="left" size="3" maxlength="3" value="<%= oscarVariables.getProperty("label.left","200") %>" /> 
			<bean:message key="demographic.demographiclabelprintsetting.msgPx" /></p>
			<p><bean:message key="demographic.demographiclabelprintsetting.formTop" />: 
			<input type="text" name="top" size="3" maxlength="3" value="<%= oscarVariables.getProperty("label.top","0")%>" /> 
			<bean:message key="demographic.demographiclabelprintsetting.msgPx" /></p>
			<p><bean:message key="demographic.demographiclabelprintsetting.formHeight" />: 
			<input type="text" name="height" size="3" maxlength="3" value="<%= oscarVariables.getProperty("label.height","145")%>" /> 
			<bean:message key="demographic.demographiclabelprintsetting.msgPx" /></p>
			<p><bean:message key="demographic.demographiclabelprintsetting.formGap" />: 
			<input type="text" name="gap" size="3" maxlength="3" value="<%= oscarVariables.getProperty("label.gap","0")%>" /> 
			<bean:message key="demographic.demographiclabelprintsetting.msgPx" /></p>
		</td>
	</tr>
	<tr>
		<td align="center">
		<table width="90%" border="1" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
			<tr>
				<td><font face="Courier New, Courier, mono" size="2">
				<span id="copytext2"> <b><%=last_name%>,&nbsp;<%=first_name%>&nbsp;<%=chart_no%></b><br><%=address%><br><%=city%>,&nbsp;<%=province%>,&nbsp;<%=postal%><br>
				<bean:message key="demographic.demographiclabelprintsetting.msgHome" />:&nbsp;<%=phone%><br><%=dob%>&nbsp;<%=sex%><br><%=hin%><br>
				<bean:message key="demographic.demographiclabelprintsetting.msgBus" />:<%=phone2%>&nbsp;
				<bean:message key="demographic.demographiclabelprintsetting.msgDr" />&nbsp;<%=providername%><br>
				</span></font></td>
			</tr>
		</table>
		</td>
		<td align="center" bgcolor="#CCCCCC">
		<input type="button" onClick="ClipBoard1('copytext2');" value="Copy to Clipboard" /> 
		<input type="checkbox" name="label2checkbox" value="checked" checked>
		<input type="text" name="label2no" size="2" maxlength="2" value="<%= oscarVariables.getProperty("label.2no","1") %>"></td>
	</tr>
	<tr>
		<td align="center">
		<table width="90%" border="1" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
			<tr>
				<td><font face="Courier New, Courier, mono" size="2">
				<span id="copytext3"> <%=last_name%>,&nbsp;<%=first_name%><br><%=address%><br><%=city%>,&nbsp;<%=province%>,&nbsp;<%=postal%><br>
				</span></font></td>
			</tr>
		</table>
		</td>
		<td align="center" bgcolor="#CCCCCC">
		<input type="button" onClick="ClipBoard1('copytext3');" value="Copy to Clipboard" /> 
		<input type="checkbox" name="label3checkbox" value="checked"> 
		<input type="text" name="label3no" size="2" maxlength="2" value="<%= oscarVariables.getProperty("label.3no","1") %>"></td>
	</tr>
	<tr>
		<td align="center">
		<table width="90%" border="1" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
			<tr>
				<td><font face="Courier New, Courier, mono" size="2">
				<span id="copytext4"> <%=first_name%>&nbsp;<%=last_name%><br><%=address%><br><%=city%>,&nbsp;<%=province%>,&nbsp;<%=postal%><br>
				</span></font></td>
			</tr>
		</table>
		</td>
		<td align="center" bgcolor="#CCCCCC">
		<textarea id="text1" STYLE="display: none;"> </textarea> 
		<input type="button" onClick="ClipBoard1('copytext4');" value="Copy to Clipboard" /> 
		<input type="checkbox" name="label4checkbox" value="checked"> 
		<input type="text" name="label4no" size="2" maxlength="2" value="<%= oscarVariables.getProperty("label.4no","1") %>"></td>
	</tr>
	<tr>
		<td align="center">
		<table width="90%" border="1" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
			<tr>
				<td><font face="Courier New, Courier, mono" size="2">
				<span id="copytext5"> <%=chart_no%> &nbsp;&nbsp;<%=last_name%>, <%=first_name%><br><%=address%>, <%=city%>, <%=province%>, <%=postal%>
				<br><%=dob%> &nbsp;&nbsp;&nbsp;<%=age%> <%=sex%> &nbsp;<%=hin%><br><%=phone%>&nbsp;&nbsp;&nbsp;<%=phone2%><br><%=refDoc%>
				</span></font></td>
			</tr>
		</table>
		</td>
		<td align="center" bgcolor="#CCCCCC"><textarea id="text1" style="display: none;"></textarea> 
		<input type="button" onClick="ClipBoard1('copytext5');" value="Copy to Clipboard" /> 
		<input type="checkbox" name="label5checkbox" value="checked"> 
		<input type="text" name="label5no" size="2" maxlength="2" value="<%= oscarVariables.getProperty("label.5no","1") %>"></td>
	</tr>
	<tr bgcolor="#486ebd">
		<td align="center" colspan="3"><input type="submit" name="Submit" value="<bean:message key='demographic.demographiclabelprintsetting.btnPrintPreviewPrint'/>">
		<input type="button" name="button" value="<bean:message key='global.btnBack'/>" onClick="javascript:history.go(-1);return false;"></td>
	</tr>
</table>
</form>

</body>
</html:html>
