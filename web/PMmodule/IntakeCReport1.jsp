<!DOCTYPE html PUBLIC "-//Tigris//DTD XHTML 1.0 Transitional//EN"  "tigris_transitional.dtd">

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/caisi_tag" prefix="caisi" %>

<% response.setHeader("Cache-Control","no-cache");%>
<% java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%
	String hasMsg = "";
	if(request.getAttribute("hasMsg") != null)
	{
		hasMsg = (String)request.getAttribute("hasMsg");
	}
%>

<%
	String[][] dataList = new String[335][15];
	if(request.getAttribute("dataList") != null)
	{
		dataList = (String[][])request.getAttribute("dataList");
	}
	
	java.util.Date[] dateList = (java.util.Date[])request.getAttribute("dateList");
%>

<html:html locale="true">
<head>
  <title>Street Health Mental Health Report</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/css/intakeC.css" />">
  <script language="JavaScript" src="<html:rewrite page="/js/ClientSearch.js" />"  ></script>
  <html:base />
</head>

<body>
<script language="javascript">
function download() {
	location.href="./do_download.jsp";
}
</script>
<table height="15" align="center">
  <tr>
    <td class="style76" align="center">
      <input type="button" name="backToClientSearch" value="Back" onclick="javascript:history.back();">
	  <input type="button" name="downLoadCSVFile" value="DownLoad" onclick="javascript:download();">
	</td>
  </tr>
</table>
<table border="1" cellpadding="1" cellspacing="1"  width="95%" align="center">
<%
	for (int i = 0; i <  dataList.length; i++) {
%>
   <tr>
<%
		for (int j = 0; j < dataList[i].length; j++) {
			if(i == 0 && dataList[i][j].startsWith("Cohort")) {
				int idx = Integer.parseInt(dataList[i][j].substring(7));
				java.util.Date tmpDate1 = dateList[idx];
				java.util.Date tmpDate2 = dateList[idx+1];
				String showDate1 = null;
				String showDate2 = null;
				try {
					showDate1 = sdf.format(tmpDate1);
					showDate2 = sdf.format(tmpDate2);
				}catch(Exception e) {}
%>
			<td class="style76" align="center"><%=showDate1%><br/><%=showDate2%><br/><%=dataList[i][j]%></td>		
<%				
			} else {
%>
			<td class="style76" align="center"><%=dataList[i][j]%></td>
<%
			}
		}
%>
   </tr>
<%
	}
%>
</table>
	
</body>
</html:html>
