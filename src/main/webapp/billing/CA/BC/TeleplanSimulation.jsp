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
if(session.getAttribute("user") == null) response.sendRedirect("../../../logout.jsp");
String user_no = (String) session.getAttribute("user");
%>

<%@ page
	import="java.util.*, java.sql.*, oscar.util.*,oscar.oscarProvider.data.ProviderData,oscar.oscarBilling.ca.bc.data.*,oscar.entities.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el"
	prefix="html-el"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%
GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
now.add(Calendar.DAY_OF_YEAR, -1);
pageContext.setAttribute("yesterday",now.getTime());


String[] yearArray = new String[5];
String thisyear = (request.getParameter("year")==null || request.getParameter("year").compareTo("")==0) ? String.valueOf(curYear) : request.getParameter("year");

String yearColor = "";
yearArray[0] = String.valueOf(curYear);
yearArray[1] = String.valueOf(curYear-1);
yearArray[2] = String.valueOf(curYear-2);
yearArray[3] = String.valueOf(curYear-3);
yearArray[4] = String.valueOf(curYear-4);

if (thisyear.compareTo(yearArray[0])==0) yearColor="#B1D3EF";
if (thisyear.compareTo(yearArray[1])==0) yearColor="#BBBBBB";
if (thisyear.compareTo(yearArray[2])==0) yearColor="#CCCCCC";
if (thisyear.compareTo(yearArray[3])==0) yearColor="#DDDDDD";
if (thisyear.compareTo(yearArray[4])==0) yearColor="#EEEEEE";

BillActivityDAO billActDAO = new BillActivityDAO();
List billList = billActDAO.getBillactivityByYear(Integer.parseInt(thisyear));
pageContext.setAttribute("billActivityList",billList);

%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Report</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">



var checkSubmitFlg = false;
function checkSubmit() {
	if (checkSubmitFlg == true) {
		return false;
	}
	checkSubmitFlg = true;
	document.forms[0].Submit.disabled = true;
	return true;
}

function findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function showHideLayers() { //v3.0
    var i,p,v,obj,args=showHideLayers.arguments;
    for (i=0; i<(args.length-2); i+=3){
    if ((obj=findObj(args[i]))!=null) {
        v=args[i+2];
        if (obj.style) {
            obj=obj.style;
            v=(v=='show')?'visible':(v='hide')?'hidden':v;
        }
        obj.visibility=v;
    }
  }
}
//-->
                                           </script>
</head>

<body bgcolor="#FFFFFF" text="#000000">

<table border="0" cellspacing="0" cellpadding="0" width="100%"
	onLoad="setfocus()" rightmargin="0" topmargin="0" leftmargin="0">
	<tr bgcolor="#486ebd">
		<td align="LEFT"><input type='button' name='print' value='Print'
			onClick='window.print()'></td>
		<th align="CENTER" style="color: #FFFFFF">Simulate Teleplan
		Report - <%=thisyear%></th>
		<td align="RIGHT"><input type='button' name='close' value='Close'
			onClick='window.close()'></td>
	</tr>
	<tr>
		<td colspan="2"><c:if test="${!empty error}">
			<c:out value="${error}" />
		</c:if></td>
	</tr>
</table>

<table width="100%" border="0" bgcolor="#E6F0F7">
	<html:form action="/billing/CA/BC/SimulateTeleplanFile.do"
		onsubmit="return checkSubmit();">
		<tr>
			<td width="220">&nbsp;</td>
			<td width="220">Select provider</td>
			<td width="254"><select name="provider">
				<option value="all">All Providers</option>
				<%ProviderData pd = new ProviderData();
                    List list = pd.getProviderListWithInsuranceNo();
                    for (int i=0;i < list.size(); i++){
                    String provNo = (String) list.get(i);
                    ProviderData provider = new ProviderData(provNo);%>
				<option value="<%=provider.getOhip_no()%>"><%=provider.getLast_name()%>,<%=provider.getFirst_name()%></option>
				<%}%>
			</select></td>
			<td width="181">&nbsp;</td>
			<td width="254">&nbsp;</td>
			<td width="277"><input type="submit" name="Submit"
				value="Create Report"></td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
	</html:form>
</table>

</body>
</html>
