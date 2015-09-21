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
<%
/*
*	We can keep this one around a bit so that if anyone ever needs to migrate data from the old table (formONAREnhanced), they can.
*	
*	NOTES:
*		
*		1) this copies the ID from the old row, and uses it as the ID for the new row. No new ID is generated. I would run this before
*		you add data to the formONAREnhancedRecord table, or it will not allow you to copy.
*		
*		2) Run this *one* time only. Running it again will only cause duplicate key exceptions. 
*		
*		3) Every care has been taken care to handle the expected data reasonably. Nothing beats spot checking after to make sure success.
*/		
%>
<%@page import="oscar.oscarDB.*" %>
<%@page import="java.sql.*" %>
<%@page import="java.util.*" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="org.hibernate.type.NullableType" %>
<%@page import="org.oscarehr.util.DbConnectionFilter" %>
<%@page import="org.oscarehr.util.MiscUtils" %>
<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@page import="oscar.log.LogAction" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<html>
<head>
<title>formONAREnhanced data migration script</title>
</head>
<body>

<h2>formONAREnhanced data migration program</h2>


<%

	String action = request.getParameter("action");
%>				


<ul>
<li>This will copy the data from the formONAREnhanced table into the new tables your OSCAR is currently running.
<li>You only need to run this once. </li>
</ul>
</h4>

<%if(action == null || !"run".equals(action)) { %>
<form action="migrateToSplitOnArEnhancedForm.jsp">
	<input type="hidden" name="action" value="run"/>
	<input type="submit" value="Run Report"/>
</form>
<%} else {
%><table><%
	MiscUtils.getLogger().info("Running the migrateToSplitOnArEnhancedForm migration script");
		LogAction.addLogSynchronous(LoggedInInfo.getLoggedInInfoFromSession(request),"migrateToSplitOnArEnhancedForm","");
		
		
		MiscUtils.getLogger().info("Retrieving metadata");
		
		List<String> namesA = getColumnNames("formONAREnhancedRecord");
		List<String> namesB = getColumnNames("formONAREnhancedRecordExt1");
		List<String> namesC = getColumnNames("formONAREnhancedRecordExt2"); 
    
				
		MiscUtils.getLogger().info("Running query for existing data");
		ResultSet rs = null;

		try {
			rs = DBHandler.GetSQL("select * from formONAREnhanced");
			
			while(rs.next()) {
				//do the first insert
				try {
					addRecord(rs,"formONAREnhancedRecord", namesA);
					addRecord(rs,"formONAREnhancedRecordExt1", namesB);
					addRecord(rs,"formONAREnhancedRecordExt2", namesC);
					MiscUtils.getLogger().info("Migrated formONAREnhanced record " + rs.getInt("ID") + " for demographic " + rs.getInt("demographic_no"));
					
					%>
						<tr><td>form with ID <%=rs.getInt("ID") %> was converted</td></tr>
					<%
				} catch(Exception e) {
					MiscUtils.getLogger().error("ERROR MIGRATING A RECORD. VERIFY MANUALLY",e);
				}
			}

			
			
		}finally {
			if(rs != null) {
				rs.close();
			}
		}
%>
	<h3>Done running migration. The forms should now work as expected.</h3>
</table> <%
	}
 %>
</body>
</html>

<%!

	/*
	* 1) Generate the PreparedStatement
	* 2) set the params
	* 3) execute
	*/
	void addRecord(ResultSet rs,String table, List<String> namesA) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "+ table+ " (");
		for(String name:namesA) {
			sb.append(name.split("\\|")[0] + ",");
		}
		sb.deleteCharAt(sb.length()-1);
		
		sb.append(") VALUES (");
		//int,tinyint,varchar,date,timestamp,text,char
		for(String name:namesA) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(");");
		
		MiscUtils.getLogger().debug(sb.toString());
		PreparedStatement preparedStmt = null;
		try {
			preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(sb.toString());	
			for(int x=0;x<namesA.size();x++) {
				String name = namesA.get(x);
				String theName = name.split("\\|")[0];
				String type = name.split("\\|")[1];
				
				if(type.equals("VARCHAR") || type.equals("CHAR")) {
					String value = rs.getString(theName);
					if(value == null) {
						preparedStmt.setNull(x+1, getType(type));
					} else {
						preparedStmt.setString(x+1,value);
					}
				}
				else if(type.equals("INT") || type.equals("TINYINT")) {
					preparedStmt.setInt(x+1, rs.getInt(theName));
				} else if(type.equals("DATE")) {
					java.sql.Date val = rs.getDate(theName);
					preparedStmt.setDate(x+1, val);
				} else if(type.equals("TIMESTAMP")) {
					Timestamp val = rs.getTimestamp(theName);
					preparedStmt.setTimestamp(x+1, val);
				} else {
					MiscUtils.getLogger().error("Missing type handler for this column " + name, new Exception());
				}
				
			}
			
			preparedStmt.executeUpdate();	
		} finally {
			if(preparedStmt != null) {
				preparedStmt.close();
			}
		}
	}



	List<String> getColumnNames(String table) throws SQLException  {
		List<String> result = new ArrayList<String>();

		ResultSet rs2 =null;
		try {
			rs2 = DBHandler.GetSQL("select * from " + table + " limit 1");
			ResultSetMetaData md = rs2.getMetaData();
			
			for (int i = 1; i <= md.getColumnCount(); i++) {
		    	String name = md.getColumnName(i);
		    	String type = md.getColumnTypeName(i);
		    	result.add(name + "|" + type);
			}
		} finally {
			if(rs2 != null)
				rs2.close();
		}
		
		return result;
	}

	int getType(String type) {
		if(type.equals("VARCHAR")) {
			return Types.VARCHAR;
		}
		if(type.equals("CHAR")) {
			return Types.CHAR;
		}
		return -1;
	}
%>
