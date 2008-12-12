<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<html>
<head>
<title>PATIENT DETAIL INFO</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />
<script LANGUAGE="JavaScript">
<!--


function Attach(lname, fname, hin, yob,mob,dob, vercode, sex) {
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
        	 }
}
-->
</script>
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	topmargin="0" onLoad="setfocus()" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">PATIENT'S
		DETAIL RECORD</font></th>
	</tr>
</table>
<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%"
	BGCOLOR="#C4D9E7">
	<% String card = request.getParameter("card_no");
   String hin = card.substring(8,card.indexOf("^"));  
   String lastname = card.substring(card.indexOf("^")+1, card.indexOf("/")).toUpperCase();
   String subcard = card.substring(card.indexOf("/")+1);
   String firstname = subcard.substring(0,subcard.indexOf("^")).toUpperCase();
   String dobyear = subcard.substring(subcard.indexOf("^")+9,subcard.indexOf("^")+13);
   String dobmonth = subcard.substring(subcard.indexOf("^")+13, subcard.indexOf("^")+15);
   String dobdate = subcard.substring(subcard.indexOf("^")+15, subcard.indexOf("^")+17);
 String vercode = subcard.substring(subcard.indexOf("^")+17, subcard.indexOf("^")+19);
   vercode = vercode.toUpperCase();
   
   
   String sex = subcard.substring(subcard.indexOf("^")+8,subcard.indexOf("^")+9);
    if (sex.compareTo("2") == 0) {
   sex="F";
   } else{
   sex="M";
   }
   %>

	<tr>
		<td align="right"><b>Last Name: </b></td>
		<td align="left"><input type="text" name="last_name"
			value="<%=lastname%>"></td>
		<td align="right"><b>First Name: </b></td>
		<td align="left"><input type="text" name="first_name"
			value="<%=firstname%>"></td>
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
		<td align="left"><input type="text" name="sex" value="<%=sex%>">
		</td>
	</tr>
	<tr valign="top">
		<td align="right"><b>HIN: </b></td>
		<td align="left"><input type="text" name="hin" value="<%=hin%>">
		</td>
		<td align="right"><b>Ver.</b></td>
		<td align="left"><input type="text" name="ver"
			value="<%=vercode%>"></td>
	</tr>
</table>

<br>
<br>
<form><input type="button" name="Button1" value="Confirm"
	onclick="javascript:Attach('<%=lastname%>','<%=firstname%>','<%=hin%>','<%=dobyear%>','<%=dobmonth%>','<%=dobdate%>', '<%=vercode%>','<%=sex%>')"><input
	type="button" name="Button" value="Cancel" onclick=self.close();>
</form>
</body>
</html>

