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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
    <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/dragiframe.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body onload="addHandle(document.getElementsByTagName('body').item(0), window);">


<a onmouseover="this.style.cursor='pointer';" onMouseDown="parent.mb.hide();" ><img src="<c:out value="${ctx}/images/close.png"/>" border="0" TITLE="Close" style="position: absolute; top: 0.5em; right: 0.5em; "></a> 
<br />
<br />
        
<table class="mhTable" >
<tr><td align="center" colspan="3">Instructions Field Reference</td></tr>
<tr>
<td valign="top">
<b>Method</b><br />
Take	<br />
Apply	<br />
Rub well in	<br />
<br />	
<b>Route	</b><br />
PO	<br />
SL	<br />
IM	<br />
Subcut	<br />
PATCH	<br />
TOP	<br />
INH	<br />
SUPP	<br />
right eye	<br />
left eye	<br />
both eyes	<br />

</td>

<td valign="top">
<b>Frequency	</b><br />
BID	<br />
TID	<br />
QID	<br />
Q1H	<br />
Q2H	<br />
Q1-2H	<br />
Q3-4H	<br />
Q4H	<br />
Q4-6H	<br />
Q6H	<br />
Q8H	<br />
Q12H	<br />
QAM	<br />
QPM	<br />
QHS	<br />
daily	<br />
once daily	<br />
twice daily	<br />
3x day	<br />
4x day	<br />
3x daily	<br />
4x daily	<br />
weekly	<br />
Q1Week	<br />
Q2Week	<br />
Q1Month	<br />
Q3Month	<br />
monthly	<br />


</td>

<td valign="top">
<b>Number	</b><br />
1/4	<br />
1/2	<br />
1	<br />
1-2	<br />
1-3	<br />
2	<br />
2-3	<br />
3	<br />
3-4	<br />
4	<br />
5	<br />
6	<br />
7	<br />
8	<br />
9	<br />

	<br />
<b>Duration	</b><br />
d	<br />
w	<br />
m	<br />
mo	<br />
day	<br />
week	<br />
month	<br />
days	<br />
weeks	<br />
months	<br />

</td>
</tr>
</table>

    
        

    
</body>
</html>
