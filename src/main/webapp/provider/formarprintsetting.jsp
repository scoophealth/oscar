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


<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PRINT SETTING</title>
<script language="JavaScript">
<!--		
function setfocus() {
  this.focus();
  document.printsetting.x.focus();
  document.printsetting.x.select();  
}
function onSetting() {
  opener.serviceform.oox.value=document.printsetting.x.value;
  opener.serviceform.ooy.value=document.printsetting.y.value;
  window.close();
}
//-->
</SCRIPT>
</head>
<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0"
	bgcolor="gold">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER><font face="Arial, Helvetica, sans-serif"
			color="#FFFFFF"> PRINT SETTING</font></th>
</table>

<center>
<form name="printsetting" method="post" action="">
<p>X : <input type="text" name="x" size="10" value="0"> <br>
Y : <input type="text" name="y" size="10" value="0"></p>
<p><input type="button" name="Submit" value=" Exit "
	onClick="onSetting()"></p>
</form>
</center>

</BODY>
</html>
