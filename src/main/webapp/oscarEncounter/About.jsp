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

<%@page import="oscar.OscarProperties" %>


<html:html locale="true">
<head>
<title>About OSCAR | Open Source Clinical Application Resource</title>

<style type="text/css">
p.build_info 
{
padding-left: 36px; 
}

p 
{
color: #666666;
font-family: verdana;
font-size: 10px;
}

p.close 
{
color: #666666;
font-family: verdana;
font-size: 16px;
}

A:link {color: #666666;}
A:visited {color: #666666;}
A:active {color: #666666;}
A:hover {color: black;}

</style>

</head>

<body bgcolor="#B7B18D">

<table width="600" cellspacing="0" cellpadding="0" align="center">

<!--instead of using css for the border I am using an image so the look is seamless between the table and about_oscar.jpg image-->
<td background="../images/about_layout/table_body_bkg.jpg">

<img src="../images/about_layout/about_oscar.jpg" border="0">

<!--START about oscar body table-->
		<table width="560"  align="center" cellspacing="0" cellpadding="0">
			<td>
			
			<p class="build_info">build date: <%= OscarProperties.getBuildDate() %><br />
			   build tag: <%=OscarProperties.getBuildTag()%>	
			</p>
			
			<table width="85%" align="center">
				<td>
				<p><u>About Us</u></p>
				
				<p>OSCAR through its product suite and partnerships offers a unique model for connecting care and creating community. The OSCAR CMS has been collaboratively developed based on the collective work, ideas and support of numerous health care providers and developers across the nation. Together these individuals form the OSCAR community known as the OSCAR Canada Users Society (OCUS). The enthusiasm of the OSCAR Community to improve patient care has brought together dedicated individuals from across the country who work towards continuously developing the not only the software tools to enable this, but also a strong community to support it.</p>
				
				<br />
				
				<p><u>Terms</u></p>
				
				<p>
				Copyright (c) 2001-2015. Department of Family
				Medicine, McMaster University. All Rights Reserved. This software is
				published under the GPL GNU General Public License. This program is
				free software; you can redistribute it and/or modify it under the
				terms of the GNU General Public License as published by the Free
				Software Foundation; either version 2 of the License, or (at your
				option) any later version. This program is distributed in the hope
				that it will be useful, but WITHOUT ANY WARRANTY; without even the
				implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
				PURPOSE. See the GNU General Public License for more details. You
				should have received a copy of the GNU General Public License along
				with this program; if not, write to the Free Software Foundation,
				Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. <br>
				<br>
				This software was written for the<br>
				Department of Family Medicine<br>
				McMaster University<br>
				Hamilton<br>
				Ontario, Canada <br>
				<br>
				</p>
				
				<p class="close" align="right"><a href="javascript: self.close()"> Close| </a></p>
				
				</td>
			</table>	
			
			<img  src="../images/about_layout/table_bottom_green.jpg" border="0" align="ABSBOTTOM">
			
			</td>
		</table>
<!--END about oscar body table-->

</td>
</tr>

</table>

</body>

</html:html>
