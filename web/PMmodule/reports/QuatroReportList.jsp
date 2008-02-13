<%@ include file="/taglibs.jsp"%>
<%@ include file="/common/messages.jsp"%>
 <table cellpadding="3" cellspacing="0" border="0" width="100%">
     <tr>
         <td style="color: white;font-family:Tahoma,Verdana,Arial;font-size: 14px;padding-left:8px;padding-right:8px;padding-top:4px;padding-bottom:4px; font-weight: bold" background="../images/TitleBar2.png" align="center">
	Reports</td>
     </tr>
 </table>
<logic:iterate id="reportGroup" property="reportGroups" name="quatroReportListForm">
	<ul><bean:write name="reportGroup" property="reportGroupDesc"/>
		<ul>
		<logic:iterate id="report" property="reports" name="reportGroup">
				<li>
			        <a href="<html:rewrite action="/QuatroReport/ReportRunner.do"/>?id=<c:out value="${report.reportNo}" />"> <c:out value="${report.title}"/> - <c:out value="${report.description}"/> </a>
		        </li>
		</logic:iterate>
		</ul>
	</ul>
</logic:iterate>
