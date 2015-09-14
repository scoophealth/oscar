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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ page errorPage="../errorpage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="oscar.util.*,oscar.*" %>
<%@ page import="oscar.login.*" %>
<%@ page import="oscar.log.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.springframework.util.StringUtils" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.PMmodule.model.Program" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProgramDao" %>
<%@ page import="org.oscarehr.PMmodule.model.ProgramProvider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProgramProviderDAO" %>
<%@ page import="org.oscarehr.common.model.SecRole" %>
<%@ page import="org.oscarehr.common.dao.SecRoleDao" %>
<%@ page import="com.quatro.model.security.Secuserrole" %>
<%@ page import="com.quatro.dao.security.SecuserroleDao" %>
<%@ page import="org.oscarehr.common.model.RecycleBin" %>
<%@ page import="org.oscarehr.common.dao.RecycleBinDao" %>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao" %>

<%
	ProgramDao programDao = SpringUtils.getBean(ProgramDao.class);
	SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);
	ProviderDataDao providerDao = SpringUtils.getBean(ProviderDataDao.class);
	
	SecuserroleDao secUserRoleDao = (SecuserroleDao)SpringUtils.getBean("secuserroleDao");
	RecycleBinDao recycleBinDao = SpringUtils.getBean(RecycleBinDao.class);
	ProgramProviderDAO programProviderDao = (ProgramProviderDAO) SpringUtils.getBean("programProviderDAO");

	
	String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	String curUser_no = (String)session.getAttribute("user");

	boolean isSiteAccessPrivacy=false;
	boolean authed=true;
%>

<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.userAdmin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false"><%isSiteAccessPrivacy=true; %></security:oscarSec>

<%
//check to see if new case management is request
ArrayList<String> users = (ArrayList<String>)session.getServletContext().getAttribute("CaseMgmtUsers");
boolean newCaseManagement = false;

if(!org.oscarehr.common.IsPropertiesOn.isCaisiEnable()) {
	//This should only temporarily apply to oscar, not caisi.
	//You cannot assign provider to one program "OSCAR" here if you have caisi enabled.
	//If there is no program called "OSCAR", it will only assign empty program to the provider which is not acceptable.
	if(( users != null && users.size() > 0 ) || OscarProperties.getInstance().getProperty("CASEMANAGEMENT", "").equalsIgnoreCase("all"))
    	newCaseManagement = true;
}

String ip = request.getRemoteAddr();
String msg = "";
String caisiProgram = null;

//get caisi programid for oscar
if( newCaseManagement ) {
	Program p = programDao.getProgramByName("OSCAR");
	if(p != null) {
		caisiProgram = String.valueOf(p.getId());
	}
}

// get role from database
Vector vecRoleName = new Vector();
String	sql;
String adminRoleName = "";

String omit="";
if (isSiteAccessPrivacy) {
	omit = OscarProperties.getInstance().getProperty("multioffice.admin.role.name", "");
}

List<SecRole> secRoles = secRoleDao.findAllOrderByRole();
for(SecRole secRole:secRoles) {
	if(!secRole.getName().equals(omit)) {
		vecRoleName.add(secRole.getName());
	}
}

//set the primary role
if (request.getParameter("buttonSetPrimaryRole") != null && request.getParameter("buttonSetPrimaryRole").length() > 0) {
      String providerNo = request.getParameter("primaryRoleProvider");
      String roleName = request.getParameter("primaryRoleRole");
      SecRole secRole = secRoleDao.findByName(roleName);
      Long roleId = secRole.getId().longValue();
      ProgramProvider pp = programProviderDao.getProgramProvider(providerNo, Long.valueOf(caisiProgram));
      if(pp != null) {
              pp.setRoleId(roleId);
              programProviderDao.saveProgramProvider(pp);
      } else {
              pp = new ProgramProvider();
              pp.setProgramId(Long.valueOf(caisiProgram));
              pp.setProviderNo(providerNo);
              pp.setRoleId(roleId);
              programProviderDao.saveProgramProvider(pp);
      }
}


// update the role
if (request.getParameter("buttonUpdate") != null && request.getParameter("buttonUpdate").length() > 0) {
    String number = request.getParameter("providerId");
    String roleId = request.getParameter("roleId");
    String roleOld = request.getParameter("roleOld");
    String roleNew = request.getParameter("roleNew");

    if(!"-".equals(roleNew)) {
		Secuserrole secUserRole = secUserRoleDao.findById(Integer.parseInt(roleId));
		if(secUserRole != null) {
			secUserRole.setRoleName(roleNew);
			secUserRoleDao.updateRoleName(Integer.parseInt(roleId),roleNew);
			msg = "Role " + roleNew + " is updated. (" + number + ")";

			RecycleBin recycleBin = new RecycleBin();
			recycleBin.setProviderNo(curUser_no);
			recycleBin.setUpdateDateTime(new java.util.Date());
			recycleBin.setTableName("secUserRole");
			recycleBin.setKeyword(number +"|"+ roleOld);
			recycleBin.setTableContent("<provider_no>" + number + "</provider_no>" + "<role_name>" + roleOld + "</role_name>"  + "<role_id>" + roleId + "</role_id>");
			recycleBinDao.persist(recycleBin);

			LogAction.addLog(curUser_no, LogConst.UPDATE, LogConst.CON_ROLE, number +"|"+ roleOld +">"+ roleNew, ip);

			if( newCaseManagement ) {
                ProgramProvider programProvider = programProviderDao.getProgramProvider(number, Long.valueOf(caisiProgram));
                if(programProvider == null) {
                	programProvider = new ProgramProvider();
                }
                
                programProvider.setProgramId( Long.valueOf(caisiProgram));
                programProvider.setProviderNo(number);
                programProvider.setRoleId(Long.valueOf(secRoleDao.findByName(roleNew).getId()));
                programProviderDao.saveProgramProvider(programProvider);
			}

		} else {
			msg = "Role " + roleNew + " is <font color='red'>NOT</font> updated!!! (" + number + ")";
		}
    }

}

// add the role
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Add")) {
    String number = request.getParameter("providerId");
    String roleNew = request.getParameter("roleNew");

    if(!"-".equals(roleNew)) {
	    Secuserrole secUserRole = new Secuserrole();
	    secUserRole.setProviderNo(number);
	    secUserRole.setRoleName(roleNew);
	    secUserRole.setActiveyn(1);
	    secUserRoleDao.save(secUserRole);
	    msg = "Role " + roleNew + " is added. (" + number + ")";
	    LogAction.addLog(curUser_no, LogConst.ADD, LogConst.CON_ROLE, number +"|"+ roleNew, ip);
	    if( newCaseManagement ) {
            ProgramProvider programProvider = programProviderDao.getProgramProvider(number, Long.valueOf(caisiProgram));
            if(programProvider == null) {
            	programProvider = new ProgramProvider();
            }
            programProvider.setProgramId( Long.valueOf(caisiProgram));
            programProvider.setProviderNo(number);
            programProvider.setRoleId(Long.valueOf(secRoleDao.findByName(roleNew).getId()));
            programProviderDao.saveProgramProvider(programProvider);
	    }
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

    Secuserrole secUserRole = secUserRoleDao.findById(Integer.parseInt(roleId));
    if(secUserRole != null) {
    	secUserRoleDao.deleteById(secUserRole.getId());
    	msg = "Role " + roleOld + " is deleted. (" + number + ")";

    	RecycleBin recycleBin = new RecycleBin();
		recycleBin.setProviderNo(curUser_no);
		recycleBin.setUpdateDateTime(new java.util.Date());
		recycleBin.setTableName("secUserRole");
		recycleBin.setKeyword(number +"|"+ roleOld);
		recycleBin.setTableContent("<provider_no>" + number + "</provider_no>" + "<role_name>" + roleOld + "</role_name>");
		recycleBinDao.persist(recycleBin);

		LogAction.addLog(curUser_no, LogConst.DELETE, LogConst.CON_ROLE, number +"|"+ roleOld, ip);

        if( newCaseManagement ) {
            ProgramProvider programProvider = programProviderDao.getProgramProvider(number, Long.valueOf(caisiProgram));
            if(programProvider != null) {
            	programProviderDao.deleteProgramProvider(programProvider.getId());
            }
        }
    } else {
    	msg = "Role " + roleOld + " is <font color='red'>NOT</font> deleted!!! (" + number + ")";
    }

}

String keyword = request.getParameter("keyword")!=null?request.getParameter("keyword"):"";
%>
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

List<Object[]> providerList = null;
providerList = providerDao.findProviderSecUserRoles(lastName, firstName);

Vector<Properties> vec = new Vector<Properties>();
for (Object[] providerSecUser : providerList) {
	
	String id = String.valueOf(providerSecUser[0]);
	String role_name = String.valueOf(providerSecUser[1]);
	String provider_no = String.valueOf(providerSecUser[2]);
	String first_name = String.valueOf(providerSecUser[3]);
	String last_name = String.valueOf(providerSecUser[4]);

	Properties prop = new Properties();
	prop.setProperty("provider_no", provider_no=="null"?"":provider_no);
	prop.setProperty("first_name", first_name);
	prop.setProperty("last_name", last_name);
	prop.setProperty("role_id", id!="null"?id:"");
	prop.setProperty("role_name", role_name!="null"?role_name:"");
	vec.add(prop);
}

List<Boolean> primaries = new ArrayList<Boolean>();

//when caisi is off, we need to show which role is the one in the program_provider table for each provider.
if(newCaseManagement) {
	for(Properties prop:vec) {
	      boolean res = false;
	      String providerNo = prop.getProperty("provider_no");
	      String secUserRoleId = prop.getProperty("role_id");
	      String roleName = prop.getProperty("role_name");
	      if(!roleName.equals("")) {
	              SecRole secRole = secRoleDao.findByName(roleName);
	              if(secRole != null) {
	                      ProgramProvider pp = programProviderDao.getProgramProvider(providerNo, Long.valueOf(caisiProgram), secRole.getId().longValue());
	                      res = (pp != null);
	              }
	      } else {
	              res = false;
	      }
	      primaries.add(res);
	}
}


%>
  <html>
    <head>
      <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
      <title>
        PROVIDER
      </title>
      <link rel="stylesheet" href="../css/receptionistapptstyle.css">
       <script src="../js/jquery-1.7.1.min.js"></script>
      
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
      
        <script>
        var items = new Array();
        <%
                for(Properties prop:vec) {
                        %>
                                item={providerNo:"<%=prop.get("provider_no")%>",role_id:"<%=prop.get("role_id")%>",roleName:"<%=prop.get("role_name")%>"};
                                items.push(item);
                        <%
                }
        %>
        </script>
        <script>
        $(document).ready(function(){
                $("#primaryRoleProvider").val("");
        });

        function primaryRoleChooseProvider() {
            $("#primaryRoleRole").find('option').remove();
            var provider = $("#primaryRoleProvider").val();
            for(var i=0;i<items.length;i++) {
                    if(items[i].providerNo == provider && items[i].role_id != "") {
                            $("#primaryRoleRole").append('<option value="'+items[i].roleName+'">'+items[i].roleName+'</option>');
                    }
            }
    }

    function setPrimaryRole() {
            var providerNo = $("#primaryRoleProvider").val();
            var roleName = $("#primaryRoleRole").val();
            if(providerNo != '' && roleName != '') {
                    return true;
            } else {
                    alert('Please enter in a provider and a corresponding role');
                    return false;
            }
    }
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

        <table width="100%" border="0" bgcolor="ivory" cellspacing="1" cellpadding="1">
          <tr bgcolor="mediumaquamarine">
          <% if( newCaseManagement ) { %>
            <th colspan="6" align="left">
          <%} else { %>
           <th colspan="5" align="left">
          <%} %>
          </tr>
          <tr bgcolor="silver">
            <th width="10%" nowrap>ID</th>
            <th width="20%" nowrap><b>First Name</b></th>
            <th width="20%" nowrap><b>Last Name</b></th>
			<% if( newCaseManagement ) { %>
            <th width="10%" nowrap>
              Role
            </th>
           <th width="10%" nowrap>
              Primary Role
            </th>
			<% } else {%>
           <th width="20%" nowrap>
              Role
            </th>
			<%} %>
            
            <th nowrap>Action</th>
          </tr>
<%
        String[] colors = { "#ccCCFF", "#EEEEFF" };
        for (int i = 0; i < vec.size(); i++) {
          	Properties item = vec.get(i);
          	String providerNo = item.getProperty("provider_no", "");
%>
      <form name="myform<%= providerNo %>" action="providerRole.jsp" method="POST">
            <tr bgcolor="<%=colors[i%2]%>">
              <td><%= providerNo %></td>
              <td><%= item.getProperty("first_name", "") %></td>
              <td><%= item.getProperty("last_name", "") %></td>
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
			<% if( newCaseManagement ) { %>
            <td align="center">
             <%=(primaries.get(i)!=null && (primaries.get(i)).booleanValue()==true)?"Yes":"" %>
            </td>
			<% } %>
            
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

      <% if( newCaseManagement ) { %>

       <form name="myform" action="providerRole.jsp" method="POST">
      <table>
      <tr>
        <td colspan="2">Set primary role</td>
      </tr>
      <tr>
        <td>Provider:</td>
        <td>
                <select id="primaryRoleProvider" name="primaryRoleProvider" onChange="primaryRoleChooseProvider()">
                        <option value="">Select Below</option>
                        <%
                                List<String> temp1 = new ArrayList<String>();
                                for(Properties prop:vec) {
                                        String providerNo = prop.getProperty("provider_no");
                                        if(!temp1.contains(providerNo)) {
                                                %>
                                                        <option value="<%=providerNo%>"><%=prop.getProperty("last_name") + "," + prop.getProperty("first_name") %></option>
                                                <%
                                                temp1.add(providerNo);
                                        }
                                }
                        %>
                </select>
        </td>
        </tr>
      </tr>
      <tr>
        <td>Role:</td>
        <td>
                <select id="primaryRoleRole" name="primaryRoleRole">
                </select>
        </td>
      </tr>
      <tr>
        <td colspan="2">
                <input type="submit" name="buttonSetPrimaryRole" value="Set Primary Role" onClick="return setPrimaryRole();"/>
        </td>
      </tr>
      </table>
       </form>
       <% } %>
      
      
      </body>
    </html>
