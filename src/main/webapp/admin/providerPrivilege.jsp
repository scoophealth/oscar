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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="*" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.userAdmin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@ page errorPage="../errorpage.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="oscar.util.*"%>
<%@ page import="oscar.login.*"%>
<%@ page import="oscar.log.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.springframework.dao.DataIntegrityViolationException"%>
<%@ page import="java.sql.BatchUpdateException"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.common.model.SecRole"%>
<%@ page import="org.oscarehr.common.dao.SecRoleDao"%>
<%@ page import="org.oscarehr.common.model.SecPrivilege"%>
<%@ page import="org.oscarehr.common.dao.SecPrivilegeDao"%>
<%@ page import="org.oscarehr.common.model.SecObjectName"%>
<%@ page import="org.oscarehr.common.dao.SecObjectNameDao"%>
<%@ page import="org.oscarehr.common.model.ProviderData"%>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao"%>
<%@ page import="org.oscarehr.common.model.SecObjPrivilege"%>
<%@ page import="org.oscarehr.common.model.SecObjPrivilegePrimaryKey"%>
<%@ page import="org.oscarehr.common.dao.SecObjPrivilegeDao"%>
<%@ page import="org.oscarehr.common.model.RecycleBin"%>
<%@ page import="org.oscarehr.common.dao.RecycleBinDao"%>
<%
	SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);
	SecPrivilegeDao secPrivilegeDao = SpringUtils.getBean(SecPrivilegeDao.class);
	SecObjectNameDao secObjectNameDao = (SecObjectNameDao)SpringUtils.getBean("secObjectNameDaoJpa");
	ProviderDataDao providerDataDao = SpringUtils.getBean(ProviderDataDao.class);
	SecObjPrivilegeDao secObjPrivilegeDao = SpringUtils.getBean(SecObjPrivilegeDao.class);
	RecycleBinDao recycleBinDao = SpringUtils.getBean(RecycleBinDao.class);
	String curUser_no = (String)session.getAttribute("user");
	String ip = request.getRemoteAddr();
%>

<%
String msg = "";
String sql = null;
ResultSet rs = null;

// get role from database
Vector vecRoleName = new Vector();

List<SecRole> secRoles = secRoleDao.findAllOrderByRole();
for(SecRole secRole:secRoles) {
	vecRoleName.add(secRole.getName());
}



// get rights from database
Vector vecRightsName = new Vector();
Vector vecRightsDesc = new Vector();
List<SecPrivilege> secPrivileges = secPrivilegeDao.findAll();
for(SecPrivilege sp:secPrivileges) {
	vecRightsName.add(sp.getPrivilege());
	vecRightsDesc.add(sp.getDescription());
}

// get objId from database
Vector vecObjectId = new Vector();
List<SecObjectName> objectNames = secObjectNameDao.findAll();
for(SecObjectName objectName:objectNames) {
	vecObjectId.add(objectName.getId());
}

List<String> distinctObjectNames = secObjectNameDao.findDistinctObjectNames();
for(String objectName:distinctObjectNames) {
	if(!vecObjectId.contains(objectName))
		vecObjectId.add(objectName);
}

// get provider name from database
Vector vecProviderName = new Vector();
Vector vecProviderNo = new Vector();
List<ProviderData> providers = providerDataDao.findAllOrderByLastName();
for(ProviderData provider:providers) {
	vecProviderNo.add(provider.getId());
	vecProviderName.add(provider.getLastName() + "," + provider.getFirstName());
}

// add the role list
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Add")) {
    String roleUserGroup   = request.getParameter("roleUserGroup");
    if(roleUserGroup.equals("")) roleUserGroup   = request.getParameter("roleUserGroup1");

    Vector vecObjRowNo = new Vector();
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
         String paraName = (String)e.nextElement();
         if(paraName.startsWith("object$")) {
         	vecObjRowNo.add(paraName.substring("object$".length()));
         }
    }

	for(int i=0; i<vecObjRowNo.size(); i++) {
	    String objectName = (String) vecObjRowNo.get(i);
	    if(objectName.equals("Name1") && request.getParameter("object$Name1").trim().equals("") ) continue;

	    String privilege = "";
		for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	         String paraName = (String)e.nextElement();
	         String prefix = "privilege$" + objectName + "$";
	         if(paraName.startsWith(prefix)) {
	         	privilege += paraName.substring( prefix.length() );
	         }
	    }
		String prefix = "priority$" + objectName;
	    String priority = request.getParameter(prefix);
	    if(objectName.equals("Name1") )  objectName = request.getParameter("object$Name1").trim();

	    SecObjPrivilege sop = new SecObjPrivilege();
	    sop.setId(new SecObjPrivilegePrimaryKey());
		sop.getId().setRoleUserGroup(roleUserGroup);
		sop.getId().setObjectName(objectName.trim());
		sop.setPrivilege(privilege);
		sop.setPriority(Integer.parseInt(priority));
		sop.setProviderNo(curUser_no);
		String secExceptionMsg = new String();
		try {
			secObjPrivilegeDao.persist(sop);
		} 
		catch(DataIntegrityViolationException divEx) {
			secExceptionMsg = divEx.getMostSpecificCause().getLocalizedMessage();
		}
		if(secExceptionMsg.length() > 0)
			msg += secExceptionMsg;
		else
			msg += "Role/Obj/Rights " + roleUserGroup + "/" + objectName + "/" + privilege + " is added. ";
	    LogAction.addLog(curUser_no, LogConst.ADD, LogConst.CON_PRIVILEGE, roleUserGroup +"|"+ objectName +"|"+privilege, ip);
	}
}

// update the role list
if (request.getParameter("buttonUpdate") != null && request.getParameter("buttonUpdate").length() > 0) {
    String roleUserGroup   = request.getParameter("roleUserGroup");
    String objectName   = request.getParameter("objectName");

    String privilege = request.getParameter("privilege");
    String priority   = request.getParameter("priority");
    String provider_no   = request.getParameter("provider_no");

    SecObjPrivilege sop = secObjPrivilegeDao.find(new SecObjPrivilegePrimaryKey(roleUserGroup,objectName));
    if(sop != null) {
    	privilege = sop.getPrivilege();
    	priority = String.valueOf(sop.getPriority());
    	provider_no = sop.getProviderNo();
    }

    RecycleBin recycleBin = new RecycleBin();
    recycleBin.setProviderNo(curUser_no);
    recycleBin.setUpdateDateTime(new java.util.Date());
    recycleBin.setTableName("secObjPrivilege");
    recycleBin.setKeyword(roleUserGroup +"|"+ objectName );

	String xml = "<roleUserGroup>" + roleUserGroup + "</roleUserGroup>" + "<objectName>" + objectName + "</objectName>";
	xml += "<privilege>" + privilege + "</privilege>" + "<priority>" + priority + "</priority>";
	xml += "<provider_no>" + provider_no + "</provider_no>";

	recycleBin.setTableContent(xml);
	recycleBinDao.persist(recycleBin);


    //String privilege = request.getParameter("privilege");
    privilege = "";
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
         String paraName = (String)e.nextElement();
         if(paraName.startsWith("privilege")) {
         	privilege += paraName.substring("privilege".length());
         }
    }
    priority   = request.getParameter("priority");
    provider_no   = curUser_no;

    sop = secObjPrivilegeDao.find(new SecObjPrivilegePrimaryKey(roleUserGroup,objectName));
    if(sop != null) {
    	sop.setProviderNo(provider_no);
    	sop.setPrivilege(privilege);
    	sop.setPriority(Integer.parseInt(priority));
    	secObjPrivilegeDao.merge(sop);
    	msg = "Role/Obj/Rights " + roleUserGroup + "/" + objectName + "/" + privilege + " is updated. ";
	    LogAction.addLog(curUser_no, LogConst.UPDATE, LogConst.CON_PRIVILEGE, roleUserGroup +"|"+ objectName, ip);
    } else {
    	msg = "Role/Obj/Rights " + roleUserGroup + "/" + objectName + "/" + privilege + " is <font color='red'>NOT</font> updated!!! ";
    }

}


// delete the role list
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Delete")) {
    String roleUserGroup   = request.getParameter("roleUserGroup");
    String objectName   = request.getParameter("objectName");

    String privilege = request.getParameter("privilege");
    String priority   = request.getParameter("priority");
    String provider_no   = request.getParameter("provider_no");

    SecObjPrivilege sop = secObjPrivilegeDao.find(new SecObjPrivilegePrimaryKey(roleUserGroup,objectName));
    if(sop != null) {
    	privilege = sop.getPrivilege();
    	priority = String.valueOf(sop.getPriority());
    	provider_no = sop.getProviderNo();
    	secObjPrivilegeDao.remove(sop.getId());
    	msg = "Role/Obj/Rights " + roleUserGroup + "/" + objectName + "/" + privilege + " is deleted. ";

    	RecycleBin recycleBin = new RecycleBin();
    	recycleBin.setProviderNo(curUser_no);
    	recycleBin.setUpdateDateTime(new java.util.Date());
    	recycleBin.setTableName("secObjPrivilege");
    	recycleBin.setKeyword(roleUserGroup +"|"+ objectName);
    	String xml = "<roleUserGroup>" + roleUserGroup + "</roleUserGroup>" + "<objectName>" + objectName + "</objectName>";
    	xml += "<privilege>" + privilege + "</privilege>" + "<priority>" + priority + "</priority>";
    	xml += "<provider_no>" + provider_no + "</provider_no>";
    	recycleBin.setTableContent(xml);
    	recycleBinDao.persist(recycleBin);
    	LogAction.addLog(curUser_no, LogConst.DELETE, LogConst.CON_PRIVILEGE, roleUserGroup +"|"+ objectName, ip);
    } else {
    	msg = "Role/Obj/Rights " + roleUserGroup + "/" + objectName + "/" + privilege + " is <font color='red'>NOT</font> deleted!!! ";
    }
}

String keyword = request.getParameter("keyword")!=null?request.getParameter("keyword"):"";

%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PROVIDER</title>
<script language="JavaScript" type="text/javascript">
<!--
function setfocus() {
	this.focus();
	document.forms[0].keyword.select();
}
function submit(form) {
	form.submit();
}

newBrowser = (document.getElementById) ? 1 : 0;
function onChangeSelect(){
	if (newBrowser){
		//me.style.backgroundColor='red';
		if(document.myform2.roleUserGroup.selectedIndex == 0) {
			document.myform2.roleUserGroup1.style.backgroundColor = 'white';
			document.myform2.roleUserGroup1.style.color = 'black';
			//document.myform2.roleUserGroup1.style.visibility = 'hidden';
		} else {
			document.myform2.roleUserGroup1.style.backgroundColor = 'silver';
			document.myform2.roleUserGroup1.style.color = 'silver';
		}
		if(document.myform2.objectName.selectedIndex == 0) {
			document.myform2.objectName1.style.backgroundColor = 'white';
			document.myform2.objectName1.style.color = 'black';
		} else {
			document.myform2.objectName1.style.backgroundColor = 'silver';
			document.myform2.objectName1.style.color = 'silver';
		}
	}
}
// -->
      </script>
</head>
<body bgproperties="fixed" bgcolor="ivory" onLoad="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">
<form name="myform" action="providerPrivilege.jsp" method="POST">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER" width="90%"><font face="Helvetica"
			color="#FFFFFF"> <% if(msg.length()>1) {%> <%=msg%> <% } %> </font></th>
		<td nowrap><font size="-1" color="#FFFFFF"> Object
		Name/Role Name: <input type="text" name="keyword" size="15"
			value="<%=keyword%>" /> <input type="submit" name="search"
			value="Search"> </font></td>
	</tr>
</table>
</form>
<%
String     color       = "#ccCCFF";
Properties prop        = null;
Vector<Properties>     vec         = new Vector<Properties>();

List<SecObjPrivilege> sops = null;
if("".equals(keyword)||vecRoleName.contains(keyword)) {
	sops = secObjPrivilegeDao.findByRoleUserGroup(keyword+"%");
} else {
	sops = secObjPrivilegeDao.findByObjectName(keyword+"%");
}
for(SecObjPrivilege sop:sops) {
	prop = new Properties();
	prop.setProperty("roleUserGroup",sop.getId().getRoleUserGroup());
	prop.setProperty("objectName",sop.getId().getObjectName());
	prop.setProperty("privilege", sop.getPrivilege());
	prop.setProperty("priority", String.valueOf(sop.getPriority()));
	vec.add(prop);
}

%>
<table width="100%" border="0" bgcolor="ivory" cellspacing="1" cellpadding="1">
	<tr bgcolor="mediumaquamarine">
		<th colspan="5" align="left">Role/Privilege List</th>
	</tr>
	<tr bgcolor="silver">
		<th width="8%" nowrap>Role</th>
		<th width="35%" nowrap>Object ID</th>
		<th width="40%" nowrap>Privilege</th>
		<th width="5%" nowrap>Priority</th>
		<th nowrap>Action</th>
	</tr>
	<%
		String tempNo = null;
		String bgColor = color;
        for (int i = 0; i < vec.size(); i++) {
       		bgColor = bgColor.equals("#EEEEFF")?color:"#EEEEFF";
       		String roleUser = (vec.get(i)).getProperty("roleUserGroup", "");
       		String roleUserName = vecProviderNo.contains(roleUser)? "<font size='-1'>"+(String)vecProviderName.get(vecProviderNo.indexOf(roleUser))+"</font>": roleUser;
       		String obj = (vec.get(i)).getProperty("objectName", "");
%>
	<form name="myformrow<%=i%>" action="providerPrivilege.jsp"
		method="POST">
	<tr bgcolor="<%=bgColor%>">
		<td><%= roleUserName %></td>
		<td><%= obj %></td>
		<td align="left">
		<%
			String priv = (vec.get(i)).getProperty("privilege", "");
			boolean bSet = true;
            for (int j = 0; j < vecRightsName.size(); j++) {
            	if(bSet&&((String)vecRightsName.get(j)).startsWith("o")) {
            		out.print("</br>");
            		bSet = false;
            	}
%> <input type="checkbox" name="privilege<%=vecRightsName.get(j)%>"
			<%=priv.indexOf(((String)vecRightsName.get(j)))>=0?"checked":""%> />
		<font size="-1"><%=((String)vecRightsDesc.get(j)).replaceAll("Only","O")%></font>
		<%			}%> <!--input type="text" name="privilege" value="<%--= priv--%>" /-->
		</td>
		<td><select name="priority">
			<option value="">-</option>
			<%			for (int j = 10; j >=0; j--) {%>
			<option value="<%=j%>"
				<%= (""+j).equals((vec.get(i)).getProperty("priority", ""))?"selected":"" %>><%= j %></option>
			<%			} %>
		</select></td>
		<td align="center">
		<%			if(!roleUser.equals("admin") && !obj.equals("_admin")) { %> <input
			type="hidden" name="keyword" value="<%=keyword%>" /> <input
			type="hidden" name="objectName" value="<%=obj %>" /> <input
			type="hidden" name="roleUserGroup" value="<%=roleUser %>" /> <input
			type="submit" name="buttonUpdate" value="Update"> <input
			type="submit" name="submit" value="Delete"> <%			} %>
		</td>
	</tr>
	</form>
	<%		} %>
</table>
<hr>

<table width="100%" border="0" bgcolor="ivory" cellspacing="1"
	cellpadding="1">
	<tr bgcolor="mediumaquamarine">
		<th colspan="4" align="left">Add Role/Privilege</th>
	</tr>
	<tr>
		<th width="20%">Role</th>
		<th width="30%">Object ID</th>
		<th width="40%">Privilege</th>
		<th>Priority</th>
	</tr>
	<form name="myform2" action="providerPrivilege.jsp" method="POST">
	<%		for (int i = 0; i <= vecObjectId.size(); i++) {
			if( i!=vecObjectId.size() && ((String)vecObjectId.get(i)).indexOf("$")>=0 ) { continue; }
%>

	<tr bgcolor="<%=bgColor%>">
		<td>
		<%			if(i==0) { %> <select name="roleUserGroup"
			onChange="onChangeSelect()">
			<option value="">-</option>
			<%					for (int j = 0; j < vecRoleName.size(); j++) {%>
			<option value="<%=vecRoleName.get(j)%>"><%= vecRoleName.get(j) %>
			</option>
			<%                  }%>
		</select> or <select name="roleUserGroup1">
			<option value="">-</option>
			<%					for (int j = 0; j < vecProviderNo.size(); j++) {%>
			<option value="<%=vecProviderNo.get(j)%>"><%= vecProviderName.get(j) %>
			</option>
			<%                  }%>
			<option value="_principal">_principal</option>
		</select> <%			}%>
		</td>
		<%       		bgColor = bgColor.equals("#EEEEFF")?color:"#EEEEFF"; %>
		<td>
		<%
			String objName = "";
			if(i==vecObjectId.size()) {
				objName = "Name1";
%> <input type="text" name="object$<%=objName%>" value="" size=35 /> <%	} else {

				objName = (String)vecObjectId.get(i);
%> <input type="checkbox" name="object$<%=objName%>" /> <%= vecObjectId.get(i) %>
		<%	if(objName.startsWith("_queue.")){
                    String d=null;
                    SecObjectName son = secObjectNameDao.find(objName);
                    if(son!=null) {
                    	d=son.getDescription();
                    }

                    if(d==null || d.equalsIgnoreCase("null")||d.trim().length()==0){
                            d="";
                        }
                    else{
                            d="("+d+")";
                        }
    %>

                                <%=d%>
                            <%}		}%>
		</td>
		<%       		bgColor = bgColor.equals("#EEEEFF")?color:"#EEEEFF"; %>
		<td>
		<%
					boolean bSet = true;
                    for (int j = 0; j < vecRightsName.size(); j++) {
                    	if(bSet&&((String)vecRightsName.get(j)).startsWith("o")) {
                    		out.print("</br>");
                    		bSet = false;
                    	}
%> <font size="-1"><input type="checkbox"
			name="privilege$<%=objName%>$<%=vecRightsName.get(j)%>" /> <%=vecRightsDesc.get(j)%></font>
		<%                  }%>
		</td>
		<%       		bgColor = bgColor.equals("#EEEEFF")?color:"#EEEEFF"; %>
		<td><select name="priority$<%=objName%>">
			<option value="">-</option>
			<%                    for (int j = 10; j >=0; j--) { %>
			<option value="<%=j%>" <%= (""+j).equals("0")?"selected":"" %>>
			<%= j %></option>
			<%                  }%>
		</select></td>
	</tr>
	<%                  }%>
	<%       		bgColor = bgColor.equals("#EEEEFF")?color:"#EEEEFF"; %>
	<tr bgcolor="<%=bgColor%>">
		<td align="center" colspan="4"><input type="hidden"
			name="keyword" value="<%=keyword%>" /> <input type="submit"
			name="submit" value="Add"></td>
	</tr>
	</form>
</table>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script>
$( document ).ready(function() {	
    parent.parent.resizeIframe($('html').height());	
	
});
</script>
</body>
</html>
