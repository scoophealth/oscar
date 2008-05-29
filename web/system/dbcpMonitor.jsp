<%-- 
    Document   : dbcpMonitor
    Created on : 29-May-2008, 9:05:02 AM
    Author     : apavel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.oscarehr.util.*"%>
<%@page import="org.apache.commons.dbcp.*"%>
<%@page import="oscar.oscarDB.*"%>
<%@page import="java.sql.*"%>

<%@page import="java.lang.management.*"%>
<%@page import="java.util.*"%>

<%
        BasicDataSource basicDataSource = (BasicDataSource) SpringUtils.getBean("dataSource");
        DBHandler dbHandler = new DBHandler(DBHandler.OSCAR_DATA);
        
        
        
        int numActive = basicDataSource.getNumActive();
        int numIdle = basicDataSource.getNumIdle();
        
        ResultSet rsProcessList = dbHandler.GetSQL("show processlist");
        
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
        <h3>----- DBCP Monitor -----</h2>
        <font color="red">Active Connections: <%=numActive%></font><br/>
        <font color="green">Idle Connections:  <%=numIdle%></font><br/><br/>
        
        <hr>
        <h3>----- Mysql Monitor -----</h3>
        <table border="1" style="border-collapse: collapse; border: 1px solid grey">
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
    <h3>----- Thread Monitor -----</h3>        
    
	Thread Format: <%=VMStat.getThreadFormat() %><br />
	Thread Info: <%=VMStat.getThreadInfo()%><br />
	<br /><br />
        <font color="blue">DbConnections in the Thread: <%=DbConnectionFilter.debugMap.size()%></font>
	<br /><br />
	<%
        int threadCount = 0;
        //make local copy of the map to prevent thread interruption
        HashMap<Thread, StackTraceElement[]> threadMap = new HashMap(DbConnectionFilter.debugMap);
        ArrayList<Map.Entry<Thread, StackTraceElement[]>> threadList = new ArrayList(Arrays.asList(threadMap.entrySet().toArray()));
        for (int i=0; i<threadList.size(); i++)
		{
                    threadCount++;
			%>
		<a href="javascript: hideDiv('stacktrace<%=threadCount%>');">Hide Stacktrace</a> | 
                <a href="javascript: showDiv('stacktrace<%=threadCount%>');">Show FULL Stacktrace</a><br/>
                <div id="stacktrace<%=threadCount%>" style="border: 1px solid grey; height: 70px; overflow: hidden;">
                    <%=threadList.get(i).getKey().getName()%> : <%=Arrays.toString(threadList.get(i).getValue()).replace(",", "<br/>") %>
				</div><br /><br />
			<%
		}
	%>

    </body>
</html>
