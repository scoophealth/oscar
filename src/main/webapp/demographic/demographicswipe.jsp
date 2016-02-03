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

<%@page import="java.util.Date"%>
<%@page import="org.oscarehr.integration.mchcv.HCMagneticStripe" %>
<%@page import="org.oscarehr.integration.mchcv.HCValidationResult" %>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT DETAIL INFO</title>
<link rel="stylesheet" href="../web.css" />
<script LANGUAGE="JavaScript">
<!--


function Attach(lname, fname, hin, yob,mob,dob, vercode, sex, effyear, effmonth, effdate, endyear, endmonth, enddate) {
        	 if(confirm("You are about to replace the existing patient's information, are you sure?")) {
        	
        	self.close();  
        	 self.opener.document.updatedelete.last_name.value = lname;
        	 self.opener.document.updatedelete.first_name.value = fname;
        	 self.opener.document.updatedelete.hin.value = hin;
        	 self.opener.document.updatedelete.year_of_birth.value = yob;
        	 self.opener.document.updatedelete.month_of_birth.value = mob;
        	 self.opener.document.updatedelete.date_of_birth.value = dob;
        	 self.opener.document.updatedelete.ver.value = vercode;
        	 self.opener.document.updatedelete.sex.value = sex;
		 self.opener.document.updatedelete.eff_date_year.value = effyear;
		 self.opener.document.updatedelete.eff_date_month.value = effmonth;
		 self.opener.document.updatedelete.eff_date_date.value = effdate;
		 self.opener.document.updatedelete.hc_renew_date_year.value = endyear;
		 self.opener.document.updatedelete.hc_renew_date_month.value = endmonth;
		 self.opener.document.updatedelete.hc_renew_date_date.value = enddate;
        	 }
}
-->
</script>
</head>



<body topmargin="0" onLoad="setfocus();" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">PATIENT'S
		DETAIL RECORD</font></th>
	</tr>
</table>
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%"
	BGCOLOR="#C4D9E7">
    <%
   HCMagneticStripe hcMagneticStripe = (HCMagneticStripe) request.getAttribute("hcMagneticStripe");
   HCValidationResult validationResult = (HCValidationResult) request.getAttribute("validationResult");
   
   String responseCode = validationResult.getResponseCode();
   String responseDescription = validationResult.getResponseDescription();
   String responseAction = validationResult.getResponseAction();
   
   String firstName = validationResult.getFirstName();
   if (firstName == null) {
       firstName = hcMagneticStripe.getFirstName();
   }
   if(firstName != null) {firstName = firstName.toUpperCase();}
   
   String lastName = validationResult.getLastName();
   if (lastName == null) {
       lastName = hcMagneticStripe.getLastName();
   }
   if(lastName != null) {lastName = lastName.toUpperCase();}
           
   String birthDate = validationResult.getBirthDate();
   if (birthDate == null) {
       birthDate = hcMagneticStripe.getBirthDate();
   }
   
   String dobyear = birthDate.substring(0, 4);
   String dobmonth = birthDate.substring(4, 6);
   String dobdate = birthDate.substring(6, 8);
   
   String expiryDate = validationResult.getExpiryDate();
   if (expiryDate == null) {
       expiryDate = hcMagneticStripe.getExpiryDate();
   }

   String endyear = expiryDate.substring(0, 4);
   String endmonth = expiryDate.substring(4, 6);
   String enddate = expiryDate.substring(6, 8);
   
   String issueDate = validationResult.getIssueDate();
   if (issueDate == null) {
       issueDate = hcMagneticStripe.getIssueDate();
   }   
   String effyear = issueDate.substring(0, 4);
   String effmonth = issueDate.substring(4, 6);
   String effdate = issueDate.substring(6, 8);
   
   String gender = validationResult.getGender();
   if (gender == null) {
       gender = hcMagneticStripe.getSex();
   }
   %>
   
       	<tr>
                <td align="left"><font size="-1"><b>Validation result: </b></font></td>
		<td><font size="-1"><%=responseDescription%></font></td>
	</tr>   
	<tr>
                <td align="left"><font size="-1"><b>Response action: </b></font></td>
		<td><font size="-1">(<%=responseCode%>) <%=responseAction%></font></td>
	</tr>   

	<tr>
		<td align="right"><b>Last Name: </b></td>
		<td align="left"><input type="text" name="last_name"
			value="<%=lastName%>"></td>
		<td align="right"><b>First Name: </b></td>
		<td align="left"><input type="text" name="first_name"
			value="<%=firstName%>"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b>DOB</b><font size="-2">(yyyy-mm-dd)</font><b>:</b>
		</td>
		<td align="left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="text" name="year_of_birth"
					value="<%=dobyear%>" size="4" maxlength="4"></td>
				<td>-</td>
				<td><input type="text" name="month_of_birth"
					value="<%=dobmonth%>" size="2" maxlength="2"></td>
				<td>-</td>
				<td><input type="text" name="date_of_birth"
					value="<%=dobdate%>" size="2" maxlength="2"></td>
			</tr>
		</table>
		</td>
		<td align="right"><b> Sex:</b></td>
		<td align="left"><input type="text" name="sex" value="<%=gender%>">
		</td>
	</tr>
	<tr valign="top">
		<td align="right"><b>HIN: </b></td>
                <td align="left"><input type="text" name="hin" value="<%=hcMagneticStripe.getHealthNumber()%>"></td>
		<td align="right"><b>Ver.</b></td>
		<td align="left"><input type="text" name="ver"
			value="<%=hcMagneticStripe.getCardVersion().toUpperCase()%>"></td>
	</tr>
        <tr valign="top">
                <td align="right"><b>EFF Date:</b></td>
                <td align="left">
                <table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                                <td><input type="text" name="eff_date_year"
                                        value="<%=effyear%>" size="4" maxlength="4"></td>
                                <td>-</td>
                                <td><input type="text" name="eff_date_month"
                                        value="<%=effmonth%>" size="2" maxlength="2"></td>
                                <td>-</td>
                                <td><input type="text" name="eff_date_date"
                                        value="<%=effdate%>" size="2" maxlength="2"></td>
                        </tr>
                </table>
                </td>
                <td align="right"><b>Renew Date:</b></td>
                <td align="left">
                <table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                                <td><input type="text" name="end_date_year"
                                        value="<%=endyear%>" size="4" maxlength="4"></td>
                                <td>-</td>
                                <td><input type="text" name="end_date_month"
                                        value="<%=endmonth%>" size="2" maxlength="2"></td>
                                <td>-</td>
                                <td><input type="text" name="end_date_date"
                                        value="<%=enddate%>" size="2" maxlength="2"></td>
                        </tr>
                </table>
                </td>

        </tr>

</table>

<br>
<br>
<form><input type="button" name="Button1" value="Confirm"
	onclick="javascript:Attach('<%=lastName%>','<%=firstName%>','<%=hcMagneticStripe.getHealthNumber()%>','<%=dobyear%>'
            ,'<%=dobmonth%>','<%=dobdate%>', '<%=hcMagneticStripe.getCardVersion().toUpperCase()%>','<%=gender%>', '<%=effyear%>', '<%=effmonth%>', '<%=effdate%>'
            , '<%=endyear%>', '<%=endmonth%>', '<%=enddate%>');"><input
	type="button" name="Button" value="Cancel" onclick=self.close();>
</form>
</body>
</html>
