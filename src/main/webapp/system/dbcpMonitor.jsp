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

<%--
    Document   : dbcpMonitor
    Created on : 29-May-2008, 9:05:02 AM
    Author     : apavel

This script can be used to trace improper connection use and db connection pool
leaks.
Used to monitor:
1. Status of the DBCP (apache.commons connection pool)
2. Status of mysql db connections
3. Current db connections in the thread (with stack traces back to the relevant
classes)
Note: If a connection is opened independently of the spring dataSource,
then it will not show up in section #3. Will have to use sections #1 and #2 to
detect those and search the source.

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.oscarehr.util.*"%>
<%@page import="org.apache.commons.dbcp.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="oscar.oscarDB.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.lang.management.*"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Map.Entry"%>

<%
	BasicDataSource basicDataSource = (BasicDataSource) SpringUtils.getBean("dataSource");
	
	int numActive = basicDataSource.getNumActive();
	int numIdle = basicDataSource.getNumIdle();
	
	ResultSet rsProcessList = DBHandler.GetSQL("show processlist");

	String logDebugMapToError=request.getParameter("logDebugMapToError");
	if (logDebugMapToError!=null) TrackingBasicDataSource.logDebugMapToError();
%>


<html>
    <head>
        <title>JSP Page</title>
        <style type="text/css">
            body {
                font-size: 12px;
            }
        </style>
        <script type="text/javascript" language="JavaScript" src="../share/javascript/prototype.js"></script>
        <script type="text/javascript" language="JavaScript">
            function hideDiv(iden) {
                $(iden).style.height='70px';
            }
            function showDiv(iden) {
                $(iden).style.height='';
            }
        </script>
    </head>
    <body>
        <h1>DB Monitor</h2>
        <h3>----- DB Connection Pool Monitor -----</h2>
        <font color="red">Active Connections (borrowed from pool): <%=numActive%></font><br/>
        <font color="green">Idle Connections (currently in the pool):  <%=numIdle%></font><br/><br/>

        <hr>
        <h3>----- Mysql Monitor -----</h3>
        <table border="1" style="border-collapse: collapse; border: 1px solid grey; font-size: 12px;">
            <tr>
                <th>ConnID</th>
                <th>User</th>
                <th>Host</th>
                <th>Database</th>
                <th>Status</th>
                <th>LastActive (s)</th>
                <th>State</th>
                <th>Query</th>
            </tr>
            <%int count = 0;
            while (rsProcessList.next()) {
            count++;%>
            <tr>
                <%for (int i=1; i<=rsProcessList.getMetaData().getColumnCount(); i++) {%>
                <td><%=rsProcessList.getString(i)%></td>
                <%}%>
            </tr>
            <%}%>
        </table>
        <br/>
        <font color="blue">Total Mysql Connections: <%=count%></font><br/>
        <br/>
        <hr>

    <h3>----- JVM Memory Monitor -----</h3>
        Free Memory: <%=String.valueOf(Runtime.getRuntime().freeMemory()/1000000)%> MB<br/>
        Used Memory: <%=String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000)%> MB<br/>
        Total Memory: <%=String.valueOf(Runtime.getRuntime().totalMemory()/1000000)%> MB<br/>
        Max Memory: <%=String.valueOf(Runtime.getRuntime().maxMemory()/1000000)%> MB  (Maximum memory JVM will attempt to use.)

                <br />
        <br />

        <table style="border-collapse:collapse;border:solid black 1px; text-align:right">
        	<tr style="background-color:#ccccff">
        		<td style="border:solid black 1px">Pool</td>
        		<td style="border:solid black 1px">Used</td>
        		<td style="border:solid black 1px">Max</td>
        	</tr>
	        <%
		        List<MemoryPoolMXBean> memoryPools=ManagementFactory.getMemoryPoolMXBeans();

				for (MemoryPoolMXBean memoryPool : memoryPools)
				{
					%>
						<tr>
							<td style="border:solid black 1px"><%=memoryPool.getName()%></td>
							<td style="border:solid black 1px"><%=memoryPool.getUsage().getUsed()%></td>
							<td style="border:solid black 1px"><%=memoryPool.getUsage().getMax()%></td>
						</tr>
					<%
				}
	        %>
        </table>

    <h3>----- GC Monitor -----</h3>

        <table style="border-collapse:collapse;border:solid black 1px; text-align:right">
        	<tr style="background-color:#ccccff">
        		<td style="border:solid black 1px">Pool</td>
        		<td style="border:solid black 1px">count</td>
        		<td style="border:solid black 1px">total time (ms)</td>
        	</tr>
	        <%
				List<GarbageCollectorMXBean> garbageCollectorMXBeans=ManagementFactory.getGarbageCollectorMXBeans();

				for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans)
				{
					%>
						<tr>
							<td style="border:solid black 1px"><%=garbageCollectorMXBean.getName()%></td>
							<td style="border:solid black 1px"><%=garbageCollectorMXBean.getCollectionCount()%></td>
							<td style="border:solid black 1px"><%=garbageCollectorMXBean.getCollectionTime()%></td>
						</tr>
					<%
				}
	        %>
        </table>

    <h3>----- Thread Monitor -----</h3>

	Thread Format: <%=VmStat.getThreadFormat() %><br />
	Thread Info: <%=VmStat.getThreadInfo()%>

	<br /><br />
	<%
		Map<Thread,StackTraceElement[]> threadMap=Thread.getAllStackTraces();
		int i=0;
		for (Map.Entry<Thread, StackTraceElement[]> entry : threadMap.entrySet())
		{
			%>
				<a href="javascript: hideDiv('threadStacktrace<%=i%>');">Hide Stacktrace</a> |
				<a href="javascript: showDiv('threadStacktrace<%=i%>');">Show FULL Stacktrace</a><br/>
				<div id="threadStacktrace<%=i%>" style="border: 1px solid grey; height: 70px; overflow: hidden;">
				<%
					Thread t=entry.getKey();
					String key=StringEscapeUtils.escapeHtml(t.hashCode()+":"+t.toString()+":"+t.isDaemon()+":"+t.getName());
					String value=StringEscapeUtils.escapeHtml(Arrays.toString(entry.getValue())).replace(",", "<br />");
				%>
				<%=key%> : <%=value%>
				</div><br /><br />
			<%
			i++;
		}
	%>
	
	

    <h3>----- DataSource Connection Monitor -----</h3>

	<br />
        <font color="blue">DataSource connections : <%=TrackingBasicDataSource.debugMap.size()%></font> &nbsp;&nbsp; <a href="?logDebugMapToError=true">write map to stderr</a>
	<br /><br />
	<%
        //make local copy of the map to prevent thread interruption
        HashMap<Connection, StackTraceElement[]> connectionMap = new HashMap<Connection, StackTraceElement[]>(TrackingBasicDataSource.debugMap);
        i=0;
        for (Map.Entry<Connection, StackTraceElement[]> entry : connectionMap.entrySet())
		{
			%>
				<a href="javascript: hideDiv('connectionStacktrace<%=i%>');">Hide Stacktrace</a> |
                <a href="javascript: showDiv('connectionStacktrace<%=i%>');">Show FULL Stacktrace</a><br/>
                <div id="connectionStacktrace<%=i%>" style="border: 1px solid grey; height: 70px; overflow: hidden;">
                	<%
                		String key=StringEscapeUtils.escapeHtml(entry.getKey().hashCode()+":"+entry.getKey().toString());
                		String value=StringEscapeUtils.escapeHtml(Arrays.toString(entry.getValue())).replace(",", "<br />");
                	%>
                    <%=key%> : <%=value%>
				</div><br /><br />
			<%
			i++;
		}
	%>

    <h3>----- Hibernate Open Session / Stack Trace Monitor (open,connected,dirty)-----</h3>

	<%
	    //make local copy of the map to prevent thread interruption
		HashMap<org.hibernate.classic.Session, StackTraceElement[]> sessionMap = new HashMap<org.hibernate.classic.Session, StackTraceElement[]>(SpringHibernateLocalSessionFactoryBean.debugMap);
        Iterator<org.hibernate.classic.Session> it=sessionMap.keySet().iterator();
		while (it.hasNext())
        {
			if (!it.next().isOpen()) it.remove();
        }
	%>
	<br />
        <font color="blue">Open Sessions : <%=sessionMap.size()%></font>
	<br /><br />
	<%
        i=0;
        for (Map.Entry<org.hibernate.classic.Session, StackTraceElement[]> entry : sessionMap.entrySet())
		{
			%>
				<a href="javascript: hideDiv('sessionStacktrace<%=i%>');">Hide Stacktrace</a> |
                <a href="javascript: showDiv('sessionStacktrace<%=i%>');">Show FULL Stacktrace</a><br/>
                <div id="sessionStacktrace<%=i%>" style="border: 1px solid grey; height: 70px; overflow: hidden;">
                	<%
                		org.hibernate.classic.Session hs=entry.getKey();
                	
                		Integer hashCode=null;
                		Boolean isOpen=null;
                		Boolean isConnected=null;
                		Boolean isDirty=null;
                		
                		try
                		{
                			hashCode=hs.hashCode();
                			isOpen=hs.isOpen();
                			isConnected=hs.isConnected();
                			isDirty=hs.isDirty();
                		}
                		catch (Exception e)
                		{
                			// it's okay during debugging, the session state is unstable
                		}
                		
                		String key=StringEscapeUtils.escapeHtml(hashCode+"("+isOpen+","+isConnected+","+isDirty+")");
                		String value=StringEscapeUtils.escapeHtml(Arrays.toString(entry.getValue())).replace(",", "<br />");
                	%>
                    <%=key%> : <%=value%>
				</div><br /><br />
			<%
			i++;
		}
	%>

    </body>
</html>
