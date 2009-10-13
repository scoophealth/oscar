<!--
/*
 *
 * Copyright (c) 2005. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 */
-->

<%-- Updated by Eugene Petruhin on 09 dec 2008 while fixing #2392669 --%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ page errorPage="../errorpage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="oscar.util.*" %>
<%@ page import="oscar.login.*" %>
<%@ page import="oscar.log.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.springframework.util.StringUtils" %>
<%
if(session.getAttribute("user") == null )
	response.sendRedirect("../logout.jsp");
String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
String curUser_no = (String)session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin,_admin.torontoRfq" rights="r" reverse="<%=true%>" >
<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%
//check to see if new case management is request
ArrayList<String> users = (ArrayList<String>)session.getServletContext().getAttribute("CaseMgmtUsers");
boolean newCaseManagement = false;

if( users != null && users.size() > 0 )
    newCaseManagement = true; 


//Caisi roles; Declared in function section below
roles.put("doctor","1");
roles.put("locum","1");
roles.put("nurse","2");


String ip = request.getRemoteAddr();

String msg = "";
DBHelp dbObj = new DBHelp();


//get caisi programid for oscar
if( newCaseManagement ) {
    String caisiQuery = "select id from program where name = 'OSCAR'";
    ResultSet result = dbObj.searchDBRecord(caisiQuery);
    if( result.next() )
        caisiProgram = result.getString(1);
}

// get role from database
Vector vecRoleName = new Vector();
String	sql   = "select * from secRole order by role_name";
ResultSet rs = dbObj.searchDBRecord(sql);
while (rs.next()) {
	vecRoleName.add(dbObj.getString(rs,"role_name"));
}

// update the role
if (request.getParameter("buttonUpdate") != null && request.getParameter("buttonUpdate").length() > 0) {
    String number = request.getParameter("providerId");
    String roleId = request.getParameter("roleId");
    String roleOld = request.getParameter("roleOld");
    String roleNew = request.getParameter("roleNew");

    sql = "update secUserRole set role_name='" + roleNew + "' where id='" + roleId + "'";
    if(!"-".equals(roleNew) && dbObj.updateDBRecord(sql, curUser_no)){
    	msg = "Role " + roleNew + " is updated. (" + number + ")";

    	sql = "insert into recyclebin (provider_no,updatedatetime,table_name,keyword,table_content) values(";
    	sql += "'" + curUser_no + "',";
    	sql += "'" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "',";
    	sql += "'" + "secUserRole" + "',";
    	sql += "'" + number +"|"+ roleOld + "',";
    	sql += "'" + "<provider_no>" + number + "</provider_no>" + "<role_name>" + roleOld + "</role_name>"  + "<role_id>" + roleId + "</role_id>" + "')";
    	dbObj.updateDBRecord(sql, curUser_no);

    	LogAction.addLog(curUser_no, LogConst.UPDATE, LogConst.CON_ROLE, number +"|"+ roleOld +">"+ roleNew, ip);
            
            if( newCaseManagement )
                updateCaisiPriv(dbObj, roleOld, roleNew, number, curUser_no);
    } else {
    	msg = "Role " + roleNew + " is <font color='red'>NOT</font> updated!!! (" + number + ")";
    }
}

// add the role
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Add")) {
    String number = request.getParameter("providerId");
    String roleNew = request.getParameter("roleNew");

    sql = "insert into secUserRole(provider_no, role_name, activeyn) values('" + number + "', '" + StringEscapeUtils.escapeSql(roleNew) + "',1)";
    if(!"-".equals(roleNew) && dbObj.updateDBRecord(sql, curUser_no)){
    	msg = "Role " + roleNew + " is added. (" + number + ")";

    	LogAction.addLog(curUser_no, LogConst.ADD, LogConst.CON_ROLE, number +"|"+ roleNew, ip);
            
            if( newCaseManagement )
                addCaisiPriv(dbObj, roleNew, number, curUser_no);
    } else {
    	msg = "Role " + roleNew + " is <font color='red'>NOT</font> added!!! (" + number + ")";
    }
}

// delete the role
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Delete")) {
    String number = request.getParameter("providerId");
    String roleId = request.getParameter("roleId");
    String roleOld = request.getParameter("roleOld");
    String roleNew = request.getParameter("roleNew");

    sql = "delete from secUserRole where id='" + roleId + "'";
    if(dbObj.updateDBRecord(sql, curUser_no)){
    	msg = "Role " + roleOld + " is deleted. (" + number + ")";

    	sql = "insert into recyclebin (provider_no,updatedatetime,table_name,keyword,table_content) values(";
    	sql += "'" + curUser_no + "',";
    	sql += "'" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "',";
    	sql += "'" + "secUserRole" + "',";
    	sql += "'" + number +"|"+ roleOld + "',";
    	sql += "'" + "<provider_no>" + number + "</provider_no>" + "<role_name>" + roleOld + "</role_name>" + "')";
		dbObj.updateDBRecord(sql, curUser_no);

		LogAction.addLog(curUser_no, LogConst.DELETE, LogConst.CON_ROLE, number +"|"+ roleOld, ip);
            
            if( newCaseManagement )
                delCaisiPriv(dbObj, roleOld, roleNew, number, curUser_no);
    } else {
    	msg = "Role " + roleOld + " is <font color='red'>NOT</font> deleted!!! (" + number + ")";
    }
}

String keyword = request.getParameter("keyword")!=null?request.getParameter("keyword"):"";
%>

<%!
        //Caisi role mapping; see top of file for init values
        private java.util.HashMap<String,String> roles = new HashMap<String,String>();  
        //caisi program id for oscar
        private String caisiProgram;// = "10015";

        //update caisi case management provider table so access can be granted and denied
        public void updateCaisiPriv(DBHelp dbObj, String roleOld, String roleNew, String provNo, String curUser_no) throws java.sql.SQLException {
            String sql;
            ResultSet rs;
            if( roleNew.equals("doctor") || roleNew.equals("nurse") || roleNew.equals("locum") ) {
                if( roleOld.equals("doctor") || roleOld.equals("nurse") || roleOld.equals("locum") )
                    sql = "UPDATE program_provider SET role_id = " + roles.get(roleNew) + " WHERE provider_no ='" + provNo + "'";                
                else {
                    sql = "SELECT role_id FROM program_provider WHERE provider_no = '" + provNo + "'";
                    rs = dbObj.searchDBRecord(sql);
                    if( rs.next() )
                        sql = "UPDATE program_provider SET role_id = " + roles.get(roleNew) + " WHERE provider_no ='" + provNo + "'";
                    else
                        sql = "INSERT INTO program_provider (program_id,provider_no,role_id) Values('" + caisiProgram + "','" + provNo + "'," + roles.get(roleNew) + ")";
                }

                dbObj.updateDBRecord(sql,curUser_no);
            }
            else {
                if( roleOld.equals("doctor") || roleOld.equals("nurse") || roleOld.equals("locum") ) {
                    sql = "SELECT role_name FROM secUserRole WHERE provider_no = '" + provNo + "' AND (role_name = 'doctor' OR role_name = 'nurse' OR role_name = 'locum')";
                    rs = dbObj.searchDBRecord(sql);
                    if(!rs.next()) {
                        sql = "DELETE FROM program_provider WHERE provider_no = '" + provNo + "'";
                        dbObj.updateDBRecord(sql,curUser_no);
                    }
                }
            }
        }
        
        //add privelege to caisi casemanagement if needed
        public void addCaisiPriv(DBHelp dbObj, String roleNew, String provNo, String curUser_no) throws java.sql.SQLException {
            if( roleNew.equals("doctor") || roleNew.equals("nurse") || roleNew.equals("locum") ) {
                String sql = "SELECT role_id FROM program_provider WHERE provider_no = '" + provNo + "'";
                ResultSet rs = dbObj.searchDBRecord(sql);
                
                if( rs.next() )
                    sql = "UPDATE program_provider SET role_id = " + roles.get(roleNew) + " WHERE provider_no ='" + provNo + "'";
                else
                    sql = "INSERT INTO program_provider (program_id,provider_no,role_id) Values('" + caisiProgram + "','" + provNo + "'," + roles.get(roleNew) + ")";
                               
                dbObj.updateDBRecord(sql,curUser_no);
            }
        }
        
        //delete privilege from caisi casemanagement if needed
        //set privilege to highest still assigned
        public void delCaisiPriv(DBHelp dbObj, String roleOld, String roleNew, String provNo, String curUser_no) throws java.sql.SQLException {
            if( (roleNew.equals("doctor") || roleNew.equals("nurse") || roleNew.equals("locum")) && roleNew.equals(roleOld) ) {
                String sql = "SELECT role_name FROM secUserRole WHERE provider_no = '" + provNo + "' AND (role_name = 'doctor' OR role_name = 'nurse' OR role_name = 'locum')";
                ResultSet rs = dbObj.searchDBRecord(sql);
                if(!rs.next()) {
                    sql = "DELETE FROM program_provider WHERE provider_no = '" + provNo + "'";
                    dbObj.updateDBRecord(sql,curUser_no);
                }
                else {
                    sql = "SELECT cr.role_name AS name from secRole cr, program_provider pp WHERE pp.provider_no = '" + provNo + "' AND cr.role_no = pp.role_id";
                    ResultSet rs1 = dbObj.searchDBRecord(sql);
                    rs1.next();
                    String caisiRole = rs1.getString("name");                    
                    String highRole = null;
                    do {
                    	String curRole = rs.getString("role_name");
                        if( curRole.equals("doctor") || curRole.equals("locum") ) {
                            highRole = curRole;
                            break;
                        }
                        else if( curRole.equals("nurse") )
                            highRole = curRole;
                        
                    }while( rs.next());
                    
                    if( !caisiRole.equals(highRole) ) {
                        sql = "UPDATE program_provider SET role_id = " + roles.get(highRole) + " WHERE provider_no = '" + provNo + "'";
                        dbObj.updateDBRecord(sql, curUser_no);
                    }
                    rs1.close();
                }
                rs.close();
            }
        }
%>
  <html>
    <head>
      <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
      <title>
        PROVIDER
      </title>
      <link rel="stylesheet" href="../receptionist/receptionistapptstyle.css">
      <script language="JavaScript">
<!--
function setfocus() {
	this.focus();
	document.forms[0].keyword.select();
}
function submit(form) {
	form.submit();
}
//-->
      </script>
    </head>
    <body bgproperties="fixed" bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
      <form name="myform" action="providerRole.jsp" method="POST">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr bgcolor="#486ebd">
          <th align="CENTER" width="90%">
            <font face="Helvetica" color="#FFFFFF">
            <% if(msg.length()>1) {%>
			<%=msg%>
			<% } %>
            </font>
          </th>
          <td nowrap>
            <font size="-1" color="#FFFFFF">
              Name:
              <input type="text" name="keyword" size="15" value="<%=keyword%>" />
              <input type="submit" name="search" value="Search">
            </font>
          </td>
        </tr>
      </table>
      </form>
<%
String lastName = "";
String firstName = "";
String[] temp = keyword.split("\\,");
if(temp.length>1) {
	lastName = temp[0] + "%";
	firstName = temp[1] + "%";
} else {
	lastName = keyword + "%";
	firstName = "%";
}
String query = "select u.id, u.role_name, p.provider_no, p.first_name, p.last_name from provider p LEFT JOIN secUserRole u ON ";
query += " p.provider_no=u.provider_no where p.last_name like '" + lastName + "' and p.first_name like '" + firstName + "' and p.status='1' order by p.first_name, p.last_name, u.role_name";
//System.out.println(query);
rs = dbObj.searchDBRecord(query);
Vector<Properties> vec = new Vector<Properties>();
while (rs.next()) {
	Properties prop = new Properties();
	prop.setProperty("provider_no", dbObj.getString(rs,"provider_no")==null?"":dbObj.getString(rs,"provider_no"));
	prop.setProperty("first_name", dbObj.getString(rs,"first_name"));
	prop.setProperty("last_name", dbObj.getString(rs,"last_name"));
	prop.setProperty("role_id", dbObj.getString(rs,"id")!=null?dbObj.getString(rs,"id"):"");
	prop.setProperty("role_name", dbObj.getString(rs,"role_name")!=null?dbObj.getString(rs,"role_name"):"");
	vec.add(prop);
}
%>
        <table width="100%" border="0" bgcolor="ivory" cellspacing="1" cellpadding="1">
          <tr bgcolor="mediumaquamarine">
            <th colspan="5" align="left">
              Provider-Role List
            </th>
          </tr>
          <tr bgcolor="silver">
            <th width="10%" nowrap>
              ID
            </th>
            <th width="20%" nowrap>
              <b>First Name</b>
            </th>
            <th width="20%" nowrap>
              <b>Last Name</b>
            </th>
            <th width="20%" nowrap>
              Role
            </th>
            <th nowrap>
              Action
            </th>
          </tr>
<%
        String[] colors = { "#ccCCFF", "#EEEEFF" };
        for (int i = 0; i < vec.size(); i++) {
          	Properties item = (Properties) vec.get(i);
          	String providerNo = item.getProperty("provider_no", "");
%>
      <form name="myform<%= providerNo %>" action="providerRole.jsp" method="POST">
            <tr bgcolor="<%=colors[i%2]%>">
              <td>
                <%= providerNo %>
              </td>
              <td>
                <%= item.getProperty("first_name", "") %>
              </td>
              <td>
                <%= item.getProperty("last_name", "") %>
              </td>
              <td align="center">
                  <select name="roleNew">
                      <option value="-" >-</option>
<%
                    for (int j = 0; j < vecRoleName.size(); j++) {
%>
                      <option value="<%=vecRoleName.get(j)%>" <%= vecRoleName.get(j).equals(item.getProperty("role_name", ""))?"selected":"" %>>
                      <%= vecRoleName.get(j) %>
                      </option>
<%
                    }
%>
                  </select>
            </td>
            <td align="center">
              <input type="hidden" name="keyword" value="<%=keyword%>" />
              <input type="hidden" name="providerId" value="<%=providerNo%>">
              <input type="hidden" name="roleId" value="<%= item.getProperty("role_id", "")%>">
              <input type="hidden" name="roleOld" value="<%= item.getProperty("role_name", "")%>">
              <input type="submit" name="submit" value="Add">
              -
              <input type="submit" name="buttonUpdate" value="Update" <%= StringUtils.hasText(item.getProperty("role_id"))?"":"disabled"%>>
              -
              <input type="submit" name="submit" value="Delete" <%= StringUtils.hasText(item.getProperty("role_id"))?"":"disabled"%>>
            </td>
            </tr>
      </form>
<%
          }
%>
        </table>
      <hr>
      </body>
    </html>
