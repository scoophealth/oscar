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
  String user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Billingreferral" %>
<%@page import="org.oscarehr.common.dao.BillingreferralDao" %>
<%
	BillingreferralDao billingReferralDao = (BillingreferralDao)SpringUtils.getBean("BillingreferralDAO");
%>

<% String search = "",search2 = "";
 search = request.getParameter("search");
 if (search == null || search.compareTo("") == 0){
 search = "search_referral_code";
 }


   String codeName= "",codeName1 = "", codeName2 = "";
   String xcodeName= "",xcodeName1 = "",xcodeName2 = "";
   codeName = request.getParameter("name");
   codeName1= request.getParameter("name1");
   codeName2 = request.getParameter("name2");
   xcodeName = request.getParameter("name");
   xcodeName1= request.getParameter("name1");
   xcodeName2 = request.getParameter("name2");

   String formName = request.getParameter("formName");
   String formElement = request.getParameter("formElement");
   if ( formName == null || formElement == null){
      formName = "";
      formElement = "";
   }

   String desc = "", desc1 = "", desc2 = "";
    String fdesc = "", fdesc1 = "", fdesc2 = "";



 if (codeName == null || codeName.compareTo("") == 0){
 codeName = " ";
 desc = " ";
 }
	 else{
		codeName = codeName + "%";
		if (codeName.indexOf(",") != -1)
		{
			desc = codeName.substring(0,codeName.indexOf(",")) + "%";
			fdesc =codeName.substring(codeName.indexOf(",")+1, codeName.length()-1).trim() + "%";
		}
		else{
		desc =  codeName + "%";
		fdesc = "%";

		}
         }
  if (codeName1 == null || codeName1.compareTo("") == 0){
  codeName1 = " ";
  desc1 = " ";
  }
  else{
 codeName1 = codeName1 + "%";
		if (codeName1.indexOf(",") != -1)
		{
			desc1 =  codeName1.substring(0,codeName1.indexOf(",")) + "%";
			fdesc1 = codeName1.substring(codeName1.indexOf(",")+1, codeName1.length()-1).trim() + "%" ;
		}
		else{
		desc1 =  codeName1 + "%";
		fdesc1 = "%";
	}

	}
 if (codeName2 == null || codeName2.compareTo("") == 0){
 codeName2 = " ";
 desc2 = " ";
 }
 else{
codeName2 = codeName2 + "%";


		if (codeName2.indexOf(",") != -1)
		{
			desc2 =  codeName2.substring(0,codeName2.indexOf(",")) + "%";
			fdesc2 = codeName2.substring(codeName2.indexOf(",")+1);
		}
		else{
		desc2 =  codeName2 + "%";
		fdesc2 = "%";
		}
		}

 String[] param =new String[9];
 param[0] = codeName;
 param[1] = codeName1;
 param[2] = codeName2;
 param[3] = desc;
 param[4] = fdesc;
 param[5] = desc1;
 param[6] = fdesc1;
 param[7] = desc2;
 param[8] = fdesc2;

%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Diagnostic Code Search</title>
<script LANGUAGE="JavaScript">
<!--
function CodeAttach(File0) {
      self.close();
      self.opener.document.BillingCreateBillingForm.xml_refer1.value = File0;
      self.opener.document.BillingCreateBillingForm.xml_refer2.value ='';
      self.opener.document.BillingCreateBillingForm.xml_refer3.value ='';
}
-->
</script>

</head>

<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0"
	rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP bgcolor="#CCCCFF"><font face="Helvetica"
			color="#000000">Referral Doctor</font><font
			face="Arial, Helvetica, sans-serif" color="#FF0000">(Maximum 3
		selections)</font></th>
	</tr>
</table>
<form name="servicecode" id="servicecode" method="post"
	action="billingReferCodeUpdate.jsp"><input type="hidden"
	name="formName" value="<%=formName%>" /> <input type="hidden"
	name="formElement" value="<%=formElement%>" />
<table width="600" border="1">
	<tr bgcolor="#CCCCFF">
		<td width="12%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Code</font></b></td>
		<td width="22%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Name</font></b></td>
		<td width="22%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Specialty</font></b></td>
		<td width="22%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">City</font></b></td>
		<td width="22%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Phone</font></b></td>
	</tr>

	<%
    String color="";
 int Count = 0;
 int intCount = 0;
 String numCode="";
   String textCode="";
   String searchType="";
// Retrieving Provider

String Dcode="", DcodeDesc="", DcodeCity="", DcodeSpecialty="", DcodePhone="";

  List<Billingreferral> billingReferrals = billingReferralDao.searchReferralCode(param[0], param[1], param[2], param[3], param[4], param[5], param[6], param[7], param[8]);
  for(Billingreferral billingReferral:billingReferrals) {


 intCount = intCount + 1;
 Dcode = billingReferral.getReferralNo();
  DcodeDesc = billingReferral.getLastName()+","+billingReferral.getFirstName();
  DcodeCity = billingReferral.getCity();
  DcodeSpecialty = billingReferral.getSpecialty();
  DcodePhone =billingReferral.getPhone();
 if (Count == 0){
 Count = 1;
 color = "#FFFFFF";
 } else {
 Count = 0;
 color="#EEEEFF";
 }
 %>

	<tr bgcolor="<%=color%>">
		<td width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2">
		<% if (Dcode.compareTo(xcodeName)==0 || Dcode.compareTo(xcodeName1)==0 || Dcode.compareTo(xcodeName2)==0){ %><input
			type="checkbox" name="code_<%=Dcode%>" checked>
		<%}else{%><input type="checkbox" name="code_<%=Dcode%>">
		<%}%><%=Dcode%></font></td>
		<td width="22%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=DcodeDesc%></font></td>
		<td width="22%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=DcodeSpecialty%></font></td>
		<td width="22%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=DcodeCity%></font></td>
		<td width="22%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=DcodePhone%></font></td>
	</tr>
	<%
  }
  %>

	<%  if (intCount == 0 ) { %>
	<tr bgcolor="<%=color%>">
		<td colspan="5"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=desc%>No match found. <%=fdesc%>
		<%// =i%>
		</font></td>

	</tr>
	<%  }%>

	<% if (intCount == 1) { %>
	<script LANGUAGE="JavaScript">
<!--
 CodeAttach('<%=Dcode%>');
-->

</script>
	<% } %>
</table>
<input type="submit" name="update" value="Confirm"><input
	type="button" name="cancel" value="Cancel"
	onclick="javascript:window.close()"></form>
</body>
</html>
