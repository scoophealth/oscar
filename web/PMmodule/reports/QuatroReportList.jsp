<%@ include file="/taglibs.jsp"%>
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
<table>

<%@ include file="/common/messages.jsp"%>
<div class="tabs" id="tabs">
    <table cellpadding="3" cellspacing="0" border="0">
        <tr>
            <th title="Facilities">Reports of "<c:out value="${quatroReportListForm.provider}"/>"</th>
        </tr>
    </table>
</div>
<display:table class="simple" cellspacing="2" cellpadding="3" id="report" name="quatroReportListForm.reports" export="false" pagesize="0" requestURI="/PMmodule/Reports/QuatroReportList">
    <display:setProperty name="paging.banner.placement" value="bottom" />
    <display:setProperty name="paging.banner.item_name" value="agency" />
    <display:setProperty name="paging.banner.items_name" value="facilities" />
    <display:setProperty name="basic.msg.empty_list" value="No reports found." />

    <display:column sortable="false" title="Title">
        <a href="<html:rewrite action="/PMmodule/Reports/QuatroReportRunner.do"/>?id=<c:out value="${report.reportNo}" />"> <c:out value="${report.title}"/> - <c:out value="${report.description}"/> </a>
    </display:column>

</display:table>
