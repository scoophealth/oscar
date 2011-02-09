<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="org.oscarehr.util.ProgressStatus"%><html>
	<head>
		<title>Progress Dialog</title>
	</head>

	<body style="font-size:12px">
		<%
			ProgressStatus progressStatus=null;
		
			// wait for process to start
			for (int i=0; i<20; i++)
			{
				progressStatus=(ProgressStatus)session.getAttribute("progressStatus");
				if (progressStatus!=null) break;
				Thread.sleep(1000);
			}
			
			if (progressStatus==null || progressStatus.completed)
			{
				session.removeAttribute("progressStatus");
				%>
					<script type="text/javascript">
						window.close();
					</script>					
				<%
			}
			else
			{
				%>
					<script type="text/javascript">
						self.focus();
						setTimeout('window.location.reload()', 2000);
					</script>
					
					<span style="font-weight:bold">Total </span><%=progressStatus.total%><br /><br />					
					<span style="font-weight:bold">Processed </span><%=progressStatus.processed%><br /><br />						
					<span style="font-weight:bold">Percent Complete </span><%=progressStatus.percentComplete%>%<br /><br />						
					<span style="font-weight:bold">Current Item </span><%=progressStatus.currentItem%>					
				<%
			}
		%>
	</body>
</html>