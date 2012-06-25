<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

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
