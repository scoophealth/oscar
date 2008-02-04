<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>

<table width="100%">
<tr><td style="color: white;font-family:Tahoma,Verdana,Arial;font-size: 14px;padding-left:8px;padding-right:8px;padding-top:4px;padding-bottom:4px; font-weight: bold" background="../images/TitleBar2.png" align="center">
Bed Reports</td></tr>
<tr><td><html:link action="/PMmodule/Reports/QuatroReportRunner.do?id=81">Occupied Beds</html:link></td></tr>
<tr><td><html:link action="/PMmodule/Reports/QuatroReportRunner.do?id=83">Available Beds</html:link></td></tr>
<tr><td>Occupancy Reports</td></tr>
<tr><td><html:link action="/PMmodule/Reports/QuatroReportRunner.do?id=100">By Shelter</html:link></td></tr>
<tr><td>Demographic Statistics</td></tr>
<tr><td><html:link action="/PMmodule/Reports/QuatroReportRunner.do?id=129">Population Report</html:link></td></tr>
</table> 
