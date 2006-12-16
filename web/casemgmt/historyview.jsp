<%@ include file="/casemgmt/taglibs.jsp" %>

<html>
<head>
<title>Note History</title>
 	<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>	
	<link rel="stylesheet" href="<c:out value="${ctx}"/>/css/casemgmt.css" type="text/css">

</head>
<body bgcolor="#eeeeff">
<nested:form action="/CaseManagementEntry">
<br>
<b>Archived Note Update History</b>
<br>
<br>
Client name: 
<I>
<logic:notEmpty name="demoName" scope="request">
<c:out value="${requestScope.demoName}" />
</logic:notEmpty>
<logic:empty name="demoName" scope="request">
<c:out value="${param.demoName}" />
</logic:empty>
</I>
<br>
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Age: 
<I>
<logic:notEmpty name="demoName" scope="request">
<c:out value="${requestScope.demoAge}" />
</logic:notEmpty>
<logic:empty name="demoName" scope="request">
<c:out value="${param.demoAge}" />
</logic:empty>
</I>
<br>
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; DOB: 
<I>
<logic:notEmpty name="demoName" scope="request">
<c:out value="${requestScope.demoDOB}" />
</logic:notEmpty>
<logic:empty name="demoName" scope="request">
<c:out value="${param.demoDOB}" />
</logic:empty>
</I>
<br><br>


<input type="button" value=" Close This Page " onclick="self.close()">
<br>
<table width="400" border="0">
		<tr>
			<td class="fieldValue" >
				<textarea name="caseNote_history" cols="107" rows="29" wrap="soft"><nested:write property="caseNote_history" /></textarea>
			</td>
		</tr>
<br>

</nested:form>

</body>
</html>
