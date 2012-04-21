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
<body>
	
	<%
		if(request.getAttribute("result")!=null) {
			%><span style="color:red"><%=request.getAttribute("result")%></span><%
		}
		if(request.getAttribute("errors")!=null) {
			%><span style="color:red"><%=request.getAttribute("errors")%></span><%
		}
	%>
	<form action="<%=request.getContextPath() %>/olis/UploadSimulationData.do" method="POST" enctype="multipart/form-data" name="simulate_form">
<table>	
		<tr>
			<td colspan="2"><b>Simulation File:</b><input type="file" name="simulateFile"/></td>
		</tr>
		<tr>
			<td colspan="2"><b>Simulate Error:</b><input type="checkbox" name="simulateError"/></td>
		</tr>
		<tr>
			<td> <input type="submit" value="Upload Simulation Data"/></td>
		</tr>
		</table>
	</form>	
	
</body>
</html>
