<%@ include file="/taglibs.jsp"%>
<html:form action="/QuatroReport/ReportList.do">
<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Reports">Reports</th>
		</tr>
	</table>
</div>

<table cellpadding="3" cellspacing="0" border="0" width="100%" class="toolgroup">
<tr><td align="left">
<img src="../images/Delete16.gif"/>
<a href="javascript:quatroReportListForm.Delete.click();">Delete Template(s)</a>&nbsp;|&nbsp;
<html:submit property="Delete" style="width:1px;height:1px;">Delete Template(s)</html:submit>
</td></tr>
</table>
<table width="100%">
<tr><td>
<%StringBuilder str= new StringBuilder(); %>
<logic:iterate id="reportGroup" property="reportGroups" name="quatroReportListForm" indexId="rIndex1">
<ul><bean:write name="reportGroup" property="reportGroupDesc"/>
  <ul>
	<logic:iterate id="report" property="reports" name="reportGroup" indexId="rIndex2">
	  <li>
        <a href="<html:rewrite action="/QuatroReport/ReportRunner.do"/>?id=<c:out value="${report.reportNo}" />"> <c:out value="${report.title}"/> - <c:out value="${report.description}"/> </a>
      </li>
   	  <logic:iterate id="template" property="childList" name="report" indexId="rIndex3">
        <ul>
          <input type="checkbox" name="p<%=String.valueOf(rIndex1)%>_<%=String.valueOf(rIndex2)%>_<%=String.valueOf(rIndex3)%>" value="<c:out value="${template.templateNo}" />">
          <%
             str.append("," + String.valueOf(rIndex1) + "_" + String.valueOf(rIndex2) + "_" + String.valueOf(rIndex3)); 
          %>
          <a href="<html:rewrite action="/QuatroReport/ReportRunner.do"/>?id=<c:out value="${report.reportNo}" />&templateNo=<c:out value="${template.templateNo}" />"> <c:out value="${template.desc}"/> </a>
        </ul>
	  </logic:iterate>
	</logic:iterate>
  </ul>
</ul>
</logic:iterate>
<input type="hidden" name="chkDel" value="<%=str.toString()%>" />
</td></tr>
 </table>
</html:form>