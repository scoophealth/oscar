<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%

  String   curUser_no = (String)session.getAttribute("user");
  String[] ROLE       = new String[]{"doctor", "resident", "nurse", "social worker", "other"};
%>
<%@ page errorPage="../errorpage.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="com.quatro.model.security.Secuserrole"%>
<%@ page import="com.quatro.dao.security.SecuserroleDao"%>
<%
	SecuserroleDao secuserroleDao = (SecuserroleDao)SpringUtils.getBean("secuserroleDao");
%>
<%
  DBPreparedHandler dbObj = new DBPreparedHandler();

  // update the role list
  if (request.getParameter("buttonUpdate") != null && request.getParameter("buttonUpdate").length() > 0) {
    String number = request.getParameter("providerId");
    String name   = request.getParameter("name" + number);

    List<Secuserrole> surs = secuserroleDao.findByProviderNo(number);
    for(Secuserrole sur:surs) {
    	secuserroleDao.updateRoleName(sur.getId(), name);
    }
  }

  // save the role list
  if (request.getParameter("submit") != null && request.getParameter("submit").equals("Add Role(s)")) {
    Properties prop  = new Properties();

    List<Secuserrole> surs=secuserroleDao.findAll();
    for(Secuserrole sur:surs) {
    	prop.setProperty(sur.getProviderNo(), "");
    }

    for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
      String temp = e.nextElement().toString();

      if (!temp.startsWith("type") || prop.containsKey(temp.substring(4, temp.length()))) {
        continue;
      }

      Secuserrole sur = new Secuserrole();
      sur.setProviderNo(temp.substring(4, temp.length()));
      sur.setRoleName(request.getParameter(temp));
      secuserroleDao.save(sur);

    }
  }
%>
<%@page import="oscar.oscarDB.DBPreparedHandler"%>

<%@page import="oscar.Misc"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PROVIDER</title>
<link rel="stylesheet" href="../css/receptionistapptstyle.css">
<script language="JavaScript">

                <!--
function setfocus() {
	this.focus();
	//  document.titlesearch.keyword.select();
}
function submit(form) {
	form.submit();
}

//-->

      </script>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER" width="90%"><font face="Helvetica"
			color="#FFFFFF"> PROVIDER LIST </font></th>
		<td nowrap><font size="-1" color="#FFFFFF"> FP-doctor;
		MFP-resident; NP-nurse; SW-social worker; OT-other </font></td>
	</tr>
</table>
<%
      String     color       = "#ccCCFF";
      Properties prop        = new Properties();
      Properties oldRoleProp = new Properties();
      Vector     vec         = new Vector();
      Vector     oldRoleList = new Vector();
      String     query       = "select u.*, p.first_name, p.last_name from secUserRole u, provider p ";

      query += "where u.provider_no=p.provider_no  order by p.first_name, p.last_name";

      ResultSet rs = dbObj.queryResults(query);

      while (rs.next()) {
        oldRoleProp.setProperty(Misc.getString(rs,"provider_no"), Misc.getString(rs,"role_name"));
        oldRoleList.add(Misc.getString(rs,"first_name"));
        oldRoleList.add(Misc.getString(rs,"last_name"));
        oldRoleList.add(Misc.getString(rs,"role_name"));
        oldRoleList.add(Misc.getString(rs,"provider_no"));
      }

      query = "select * from provider order by first_name, last_name";
      rs = dbObj.queryResults(query);

      while (rs.next()) {
        if (Misc.getString(rs,"last_name").length() < 1 || oldRoleProp.containsKey((Misc.getString(rs,"provider_no")))) {
          continue;
        }

        prop = new Properties();

        prop.setProperty("provider_no", Misc.getString(rs,"provider_no"));
        prop.setProperty("first_name", Misc.getString(rs,"first_name"));
        prop.setProperty("last_name", Misc.getString(rs,"last_name"));
        prop.setProperty("provider_type", Misc.getString(rs,"provider_type"));
        prop.setProperty("specialty", Misc.getString(rs,"specialty"));
        prop.setProperty("ohip_no", Misc.getString(rs,"ohip_no"));
        vec.add(prop);
      }
%>
<form name="myform" action="reportonbilledvisitprovider.jsp"
	method="POST">
<table width="100%" border="0" bgcolor="ivory" cellspacing="1"
	cellpadding="1">
	<tr bgcolor="mediumaquamarine">
		<th colspan="7" align="left">New Provider-Role List</th>
		<td align="right"><input type="submit" name="submit"
			value="Add Role(s)"></td>
	</tr>
	<tr bgcolor="silver">
		<th width="30%" nowrap>ID</th>
		<th width="30%" nowrap><b>First Name</b></th>
		<th width="30%" nowrap><b>Last Name</b></th>
		<!--th width="10%" nowrap>
            <b>provider type</b>
          </th>
          <th width="10%" nowrap>
            <b>specialty</b>
          </th>
          <th width="10%" nowrap>
            <b>ohip_no</b>
          </th-->
		<th width="5%" nowrap>FP <br>
		doctor</th>
		<th width="5%" nowrap>RFP <br>
		resident</th>
		<th width="5%" nowrap>NP <br>
		nurse</th>
		<th width="5%" nowrap>SW <br>
		social worker</th>
		<th width="5%" nowrap>OT</th>
	</tr>
	<%
          for (int i = 0; i < vec.size(); i++) {
            boolean bDoc         = false;
            boolean bRes         = false;
            boolean bNp          = false;
            boolean bSw          = false;
            boolean bOt          = false;
            String  providerType = ((Properties)vec.get(i)).getProperty("provider_type", "");

            if (((Properties)vec.get(i)).getProperty("ohip_no", "").length() > 3) {
              bDoc = true;
            } else if (providerType.matches(".*[rR][eE][sS][iI][dD][eE][nN][tT].*")) {
              bRes = true;
            } else if (providerType.matches(".*[nN][pP].*|.*[nN][uU][rR][sS][eE].*")) {
              bNp = true;
            } else {
              bOt = true;
            }
%>
	<tr bgcolor="<%=i%2==0?"white":color%>">
		<td><%= ((Properties)vec.get(i)).getProperty("provider_no", "") %>
		</td>
		<td><%= ((Properties)vec.get(i)).getProperty("first_name", "") %>
		</td>
		<td><%= ((Properties)vec.get(i)).getProperty("last_name", "") %>
		</td>
		<!--td>
              <%= ((Properties)vec.get(i)).getProperty("provider_type", "") %>
            </td>
            <td>
              <%= ((Properties)vec.get(i)).getProperty("specialty", "") %>
            </td>
            <td>
              <%= ((Properties)vec.get(i)).getProperty("ohip_no", "") %>
            </td-->
		<td align="center" <%=bDoc?"bgcolor=\"silver\"":""%> title="Doctor">
		<input type="radio"
			name="type<%=((Properties)vec.get(i)).getProperty("provider_no", "")%>"
			value="<%=ROLE[0]%>" <%=bDoc?"checked":""%>></td>
		<td align="center" <%=bRes?"bgcolor=\"silver\"":""%> title="Resident">
		<input type="radio"
			name="type<%=((Properties)vec.get(i)).getProperty("provider_no", "")%>"
			value="<%=ROLE[1]%>" <%=bRes?"checked":""%>></td>
		<td align="center" <%=bNp?"bgcolor=\"silver\"":""%> title="Nurse">
		<input type="radio"
			name="type<%=((Properties)vec.get(i)).getProperty("provider_no", "")%>"
			value="<%=ROLE[2]%>" <%=bNp?"checked":""%>></td>
		<td align="center" <%=bSw?"bgcolor=\"silver\"":""%>
			title="Social Worker"><input type="radio"
			name="type<%=((Properties)vec.get(i)).getProperty("provider_no", "")%>"
			value="<%=ROLE[3]%>" <%=bSw?"checked":""%>></td>
		<td align="center" <%=bOt?"bgcolor=\"silver\"":""%> title="Other">
		<input type="radio"
			name="type<%=((Properties)vec.get(i)).getProperty("provider_no", "")%>"
			value="<%=ROLE[4]%>" <%=bOt?"checked":""%>></td>
	</tr>
	<%
          }
          if (vec.size() > 0) {
%>
	<tr bgcolor="A9A9A9">
		<td colspan="8" align="right"><input type="submit" name="submit"
			value="Add Role(s)"></td>
	</tr>
	<%
          }
%>
</table>
</form>
<hr>
<table width="100%" border="0" bgcolor="ivory" cellspacing="1"
	cellpadding="1">
	<tr bgcolor="mediumaquamarine">
		<th colspan="5" align="left">Confirmed Provider-Role List</th>
	</tr>
	<tr bgcolor="silver">
		<th width="10%" nowrap>ID</th>
		<th width="30%" nowrap><b>First Name</b></th>
		<th width="30%" nowrap><b>Last Name</b></th>
		<th nowrap>Role</th>
		<th nowrap>Action</th>
		<%
          int k = 0;

          for (int i = 0; i < oldRoleList.size(); i += 4) {
            k++;
%>

	<tr bgcolor="<%=k%2==0?"white":color%>">
		<form name="mySecform<%=i%>" action="reportonbilledvisitprovider.jsp"
			method="POST">
		<td><%= oldRoleList.get(i+3) %></td>
		<td><%= oldRoleList.get(i) %></td>
		<td><%= oldRoleList.get(i + 1) %></td>
		<td align="center"><select
			name="<%="name" + oldRoleList.get(i + 3)%>">
			<%
                    for (int j = 0; j < ROLE.length; j++) {
%>
			<option value="<%=ROLE[j]%>"
				<%= ROLE[j].equals(oldRoleList.get(i + 2))?"selected":"" %>>
			<%= ROLE[j] %></option>
			<%
                    }
%>
		</select></td>
		<td align="center"><input type="hidden" name="providerId"
			value="<%= oldRoleList.get(i + 3) %>"> <input type="submit"
			name="buttonUpdate" value="Update"></td>
		</form>
	</tr>
	<%
          }
%>
</table>
</body>
</html>
