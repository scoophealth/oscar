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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ page errorPage="../errorpage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="oscar.util.*" %>
<%@ page import="oscar.login.*" %>
<%@ page import="oscar.log.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%
if(session.getAttribute("user") == null )
	response.sendRedirect("../logout.jsp");
String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
String curUser_no = (String)session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="r" reverse="<%=true%>" >
<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%
//Caisi roles; Declared in function section below
roles.put("doctor","1");
roles.put("locum","1");
roles.put("nurse","2");


String ip = request.getRemoteAddr();

String msg = "";
DBHelp dbObj = new DBHelp();


//get caisi programid for oscar
String caisiQuery = "select program_id from program where name = 'OSCAR'";
ResultSet result = dbObj.searchDBRecord(caisiQuery);
if( result.next() )
    caisiProgram = result.getString(1);

// get role from database
Vector vecRoleName = new Vector();
String	sql   = "select * from secRole order by role_name";
ResultSet rs = dbObj.searchDBRecord(sql);
while (rs.next()) {
	vecRoleName.add(rs.getString("role_name"));
}

// update the role list
if (request.getParameter("buttonUpdate") != null && request.getParameter("buttonUpdate").length() > 0) {
    String number = request.getParameter("providerId");
    String name   = request.getParameter("name" + number);
    String roleName   = request.getParameter("roleName");

	sql = "insert into recyclebin (provider_no,updatedatetime,table_name,keyword,table_content) values(";
	sql += "'" + curUser_no + "',";
	sql += "'" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "',";
	sql += "'" + "secUserRole" + "',";
	sql += "'" + number +"|"+ roleName + "',";
	sql += "'" + "<provider_no>" + number + "</provider_no>" + "<role_name>" + roleName + "</role_name>" + "')";
	dbObj.updateDBRecord(sql, curUser_no);

    //System.out.println(number + "  " + name);
    sql = "update secUserRole set role_name='" + name + "' where provider_no='" + number + "' and role_name='" + roleName + "'";
    if(dbObj.updateDBRecord(sql, curUser_no)){
    	msg = "Role " + name + " is updated. (" + number + ")";
	    LogAction.addLog(curUser_no, LogConst.UPDATE, LogConst.CON_ROLE, number +"|"+ roleName, ip);
            updateCaisiPriv(dbObj, roleName, name, number, curUser_no);
    } else {
    	msg = "Role " + name + " is <font color='red'>NOT</font> updated!!! (" + number + ")";
    }
        
}

// add the role list
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Add")) {
    String number = request.getParameter("providerId");
    String name   = request.getParameter("name" + number);
    sql = "insert into secUserRole(provider_no, role_name) values('" + number + "', '" + StringEscapeUtils.escapeSql(name) + "')";
    if(dbObj.updateDBRecord(sql, curUser_no)){
    	msg = "Role " + name + " is added. (" + number + ")";
	    LogAction.addLog(curUser_no, LogConst.ADD, LogConst.CON_ROLE, number +"|"+ name, ip);
            addCaisiPriv(dbObj, name, number, curUser_no);
    } else {
    	msg = "Role " + name + " is <font color='red'>NOT</font> added!!! (" + number + ")";
    }
}

// delete the role list
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Delete")) {
    String number = request.getParameter("providerId");
    String name   = request.getParameter("name" + number);
    String roleName   = request.getParameter("roleName");
    sql = "delete from secUserRole where role_name='" + StringEscapeUtils.escapeSql(name) + "' and provider_no='" + number + "' and role_name='" + roleName + "'";
    if(dbObj.updateDBRecord(sql, curUser_no)){
    	msg = "Role " + name + " is deleted. (" + number + ")";
    	sql = "insert into recyclebin (provider_no,updatedatetime,table_name,keyword,table_content) values(";
    	sql += "'" + curUser_no + "',";
    	sql += "'" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "',";
    	sql += "'" + "secUserRole" + "',";
    	sql += "'" + number +"|"+ roleName + "',";
    	sql += "'" + "<provider_no>" + number + "</provider_no>" + "<role_name>" + roleName + "</role_name>" + "')";
		dbObj.updateDBRecord(sql, curUser_no);
	    LogAction.addLog(curUser_no, LogConst.DELETE, LogConst.CON_ROLE, number +"|"+ roleName, ip);
            delCaisiPriv(dbObj, roleName, name, number, curUser_no);
    } else {
    	msg = "Role " + name + " is <font color='red'>NOT</font> deleted!!! (" + number + ")";
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
        public void updateCaisiPriv(DBHelp dbObj, String roleName, String name, String provNo, String curUser_no) throws java.sql.SQLException {
            String sql;
            ResultSet rs;            
            if( name.equals("doctor") || name.equals("nurse") || name.equals("locum") ) {
                if( roleName.equals("doctor") || roleName.equals("nurse") || roleName.equals("locum") )
                    sql = "UPDATE program_provider SET role_id = " + roles.get(name) + " WHERE provider_no ='" + provNo + "'";                
                else {
                    sql = "SELECT role_id FROM program_provider WHERE provider_no = '" + provNo + "'";
                    rs = dbObj.searchDBRecord(sql);
                    if( rs.next() )
                        sql = "UPDATE program_provider SET role_id = " + roles.get(name) + " WHERE provider_no ='" + provNo + "'";
                    else
                        sql = "INSERT INTO program_provider (program_id,provider_no,role_id) Values('" + caisiProgram + "'," + provNo + "," + roles.get(name) + ")";
                }

                dbObj.updateDBRecord(sql,curUser_no);
            }
            else {
                if( roleName.equals("doctor") || roleName.equals("nurse") || roleName.equals("locum") ) {
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
        public void addCaisiPriv(DBHelp dbObj, String name, String provNo, String curUser_no) throws java.sql.SQLException {
            if( name.equals("doctor") || name.equals("nurse") || name.equals("locum") ) {
                String sql = "SELECT role_id FROM program_provider WHERE provider_no = '" + provNo + "'";
                ResultSet rs = dbObj.searchDBRecord(sql);
                
                if( rs.next() )
                    sql = "UPDATE program_provider SET role_id = " + roles.get(name) + " WHERE provider_no ='" + provNo + "'";
                else
                    sql = "INSERT INTO program_provider (program_id,provider_no,role_id) Values('" + caisiProgram + "'," + provNo + "," + roles.get(name) + ")";
                               
                dbObj.updateDBRecord(sql,curUser_no);
            }
        }
        
        //delete privelege from caisi casemanagement if needed
        public void delCaisiPriv(DBHelp dbObj, String roleName, String name, String provNo, String curUser_no) throws java.sql.SQLException {
            if( (name.equals("doctor") || name.equals("nurse") || name.equals("locum")) && name.equals(roleName) ) {
                String sql = "SELECT role_name FROM secUserRole WHERE provider_no = '" + provNo + "' AND (role_name = 'doctor' OR role_name = 'nurse' OR role_name = 'locum')";
                ResultSet rs = dbObj.searchDBRecord(sql);
                if(!rs.next()) {
                    sql = "DELETE FROM program_provider WHERE provider_no = '" + provNo + "'";
                    dbObj.updateDBRecord(sql,curUser_no);
                }
            }
        }
%>
  <html>
    <head>
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
String     color       = "#ccCCFF";
Properties prop        = null;
Vector     vec         = new Vector();

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
String query = "select u.*, p.provider_no, p.first_name, p.last_name from provider p LEFT JOIN secUserRole u ON ";
query += " p.provider_no=u.provider_no where p.last_name like '" + lastName + "' and p.first_name like '" + firstName + "' and p.status='1' order by p.first_name, p.last_name, u.role_name";
System.out.println(query);
rs = dbObj.searchDBRecord(query);
while (rs.next()) {
	prop = new Properties();
	prop.setProperty("provider_no", rs.getString("p.provider_no"));
	prop.setProperty("first_name", rs.getString("p.first_name"));
	prop.setProperty("last_name", rs.getString("p.last_name"));
	prop.setProperty("role_name", rs.getString("u.role_name")!=null?rs.getString("u.role_name"):"");
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
		String tempNo = null;
		String bgColor = color;
        for (int i = 0; i < vec.size(); i++) {
          	String providerNo = ((Properties)vec.get(i)).getProperty("provider_no", "");
          	if(!providerNo.equals(tempNo)) {
          		tempNo = providerNo;
          		bgColor = bgColor.equals("#EEEEFF")?color:"#EEEEFF";
          	}
%>
      <form name="myform" action="providerRole.jsp" method="POST">
            <tr bgcolor="<%=bgColor%>">
              <td>
                <%= providerNo %>
              </td>
              <td>
                <%= ((Properties)vec.get(i)).getProperty("first_name", "") %>
              </td>
              <td>
                <%= ((Properties)vec.get(i)).getProperty("last_name", "") %>
              </td>
              <td align="center">
                  <select name="<%="name" + providerNo%>">
						<option value="-" >-</option>
<%
                    for (int j = 0; j < vecRoleName.size(); j++) {
%>
                      <option value="<%=vecRoleName.get(j)%>" <%= vecRoleName.get(j).equals(((Properties)vec.get(i)).getProperty("role_name", ""))?"selected":"" %>>
                      <%= vecRoleName.get(j) %>
                      </option>
<%
                    }
%>
                  </select>
            </td>
            <td align="center">
              <input type="hidden" name="keyword" value="<%=keyword%>" />
              <input type="hidden" name="providerId" value="<%= ((Properties)vec.get(i)).getProperty("provider_no", "")%>">
              <input type="hidden" name="roleName" value="<%= ((Properties)vec.get(i)).getProperty("role_name", "")%>">
              <input type="submit" name="submit" value="Add">
              <input type="submit" name="buttonUpdate" value="Update">
              -
              <input type="submit" name="submit" value="Delete">
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
