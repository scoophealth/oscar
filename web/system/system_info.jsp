<%--
/*
* Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. 
* 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. 
* 
* You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.  
* 
* This software was written for 
* CAISI, 
* Toronto, Ontario, Canada 
*/
--%>

<%@page import="org.oscarehr.util.*"%>
<%@page import="java.lang.management.*"%>
<%@page import="java.util.*"%>

<html>
	<head>
		<title>system info</title>
	</head>
	<body>
	<h1>MemInfo</h1>
	<%=VMStat.getMemoryFormat() %><br />
	<%
		List<MemoryPoolMXBean> memoryPools=ManagementFactory.getMemoryPoolMXBeans();
		
		for (MemoryPoolMXBean memoryPool : memoryPools)
		{
			String memInfo=VMStat.getMemoryInfo(memoryPool);
			%>
				<%=memInfo%><br />
			<%
		}
		
	%>
	<br /><br />
	<h1>GCInfo</h1>
	<%=VMStat.getGCFormat() %><br />
	<%
		List<GarbageCollectorMXBean> garbageCollectorMXBeans=ManagementFactory.getGarbageCollectorMXBeans();
		
		for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans)
		{
			String gcInfo=VMStat.getGCInfo(garbageCollectorMXBean);
			%>
				<%=gcInfo%><br />
			<%
		}
	%>
	<br /><br />
	<h1>ThreadInfo</h1>
	<%=VMStat.getThreadFormat() %><br />
	<%=VMStat.getThreadInfo()%><br />
	<br /><br />
	<h1>DbConnections</h1>
	<%
		for (Map.Entry<Thread, StackTraceElement[]> entry : DbConnectionFilter.debugMap.entrySet())
		{
			%>
				<%=entry.getKey().getName()%> : <%=Arrays.toString(entry.getValue()) %>
				<br /><br />
			<%
		}
	%>
	</body>
</html>