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
<security:oscarSec roleName="<%=roleName$%>" objectName="_eform" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_eform");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
//Lists forms available to add to patient
  String demographic_no = request.getParameter("demographic_no"); 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
  String country = request.getLocale().getCountry();
  String parentAjaxId = request.getParameter("parentAjaxId");
  String appointment = request.getParameter("appointment");
  
  LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
%>  

<%@ page import = "java.util.*, java.sql.*, oscar.eform.*"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO, org.oscarehr.common.model.UserProperty" %>
<%
String user = (String) session.getAttribute("user");
    
String orderByRequest = request.getParameter("orderby");
String orderBy = "";
if (orderByRequest == null) orderBy = EFormUtil.NAME;
else if (orderByRequest.equals("form_subject")) orderBy = EFormUtil.SUBJECT;
else if (orderByRequest.equals("form_date")) orderBy = EFormUtil.DATE;

String groupView = request.getParameter("group_view");
if (groupView == null) {
    UserPropertyDAO userPropDAO = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
    UserProperty usrProp = userPropDAO.getProp(user, UserProperty.EFORM_FAVOURITE_GROUP);
    if( usrProp != null ) {
        groupView = usrProp.getValue();
    }
    else {
        groupView = "";
    }
}
%>


<%@page import="org.oscarehr.util.LoggedInInfo"%><html:html locale="true">

<head>
<title>
<bean:message key="eform.myform.title"/>
</title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" href="../share/css/eformStyle.css">
<script type="text/javascript" language="JavaScript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" language="JavaScript">
function popupPage(varpage, windowname) {
    var page = "" + varpage;
    windowprops = "height=700,width=800,location=no,"
    + "scrollbars=yes,menubars=no,status=yes,toolbars=no,resizable=yes,top=10,left=200";
    var popup = window.open(page, windowname, windowprops);
    if (popup != null) {
       if (popup.opener == null) {
          popup.opener = self;
       }
       popup.focus();
    }
}

function updateAjax() {
    var parentAjaxId = "<%=parentAjaxId%>";    
    if( parentAjaxId != "null" ) {
        window.opener.document.forms['encForm'].elements['reloadDiv'].value = parentAjaxId;
        window.opener.updateNeeded = true;    
    }

}
</script>
</head>

<body onunload="updateAjax()" class="BodyStyle" vlink="#0000FF">
<!--  -->
    <table class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="175">
                <bean:message key="eform.myform.msgEForm"/>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            <bean:message key="eform.myform.msgFormLib"/>
                        </td>
                        <td>&nbsp;
							
                        </td>   
                        <td style="text-align:right">
                                <oscar:help keywords="eform" key="app.top1"/> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">
                
                    <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&displaymode=edit&dboperation=search_detail"><bean:message key="demographic.demographiceditdemographic.btnMasterFile" /></a>
               
                <br>
                <a href="efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>" class="current"> <bean:message key="eform.showmyform.btnAddEForm"/></a><br/>
                <a href="efmpatientformlist.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"><bean:message key="eform.calldeletedformdata.btnGoToForm"/></a><br/>
                <a href="efmpatientformlistdeleted.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"><bean:message key="eform.showmyform.btnDeleted"/></a>
                
                <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.eform" rights="w" reverse="<%=false%>" >
                <br/>
                <a href="#" onclick="javascript: return popup(600, 1200, '../administration/?show=Forms', 'manageeforms');" style="color: #835921;"><bean:message key="eform.showmyform.msgManageEFrm"/></a>
                </security:oscarSec>
<jsp:include page="efmviewgroups.jsp">
    <jsp:param name="url" value="../eform/efmformslistadd.jsp"/>
    <jsp:param name="groupView" value="<%=groupView%>"/>
</jsp:include>
     
            </td>
            <td class="MainTableRightColumn" style="vertical-align: top">

<table class="elements" style="margin-left: 0px; margin-right: 0px;" width="100%">
      <tr bgcolor=<%=deepColor%>>
      <th><a href="efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&group_view=<%=groupView%>&parentAjaxId=<%=parentAjaxId%>"><bean:message key="eform.showmyform.btnFormName"/></a></th>
      <th><a href="efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&group_view=<%=groupView%>&orderby=form_subject&parentAjaxId=<%=parentAjaxId%>"><bean:message key="eform.showmyform.btnSubject"/></a></th>
      <!--<th><a href="myform.jsp?demographic_no=<%=demographic_no%>&group_view=<%=groupView%>&orderby=file_name"><bean:message key="eform.myform.btnFile"/></a></th>-->
      <th><a href="efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&group_view=<%=groupView%>&orderby=form_date&parentAjaxId=<%=parentAjaxId%>"><bean:message key="eform.showmyform.formDate"/></a></th>
      <!--<th><a href="myform.jsp?demographic_no=<%=demographic_no%>&group_view=<%=groupView%>"><bean:message key="eform.showmyform.formTime"/></a></th> -->
      </tr>      
      
<%
  ArrayList<HashMap<String, ? extends Object>> eForms;
  if (groupView.equals("") || groupView.equals("default")) {
      eForms = EFormUtil.listEForms(LoggedInInfo.getLoggedInInfoFromSession(request), orderBy, EFormUtil.CURRENT, roleName$);
  } else {
      eForms = EFormUtil.listEForms(LoggedInInfo.getLoggedInInfoFromSession(request), orderBy, EFormUtil.CURRENT, groupView, roleName$);
  }
  if (eForms.size() > 0) {
      for (int i=0; i<eForms.size(); i++) {
    	  HashMap<String, ? extends Object> curForm = eForms.get(i);
%>
      <tr bgcolor="<%= ((i%2) == 1)?"#F2F2F2":"white"%>">
	    <td width="30%" style="padding-left: 7px">
	    <a HREF="#" ONCLICK ="javascript: popupPage('efmformadd_data.jsp?fid=<%=curForm.get("fid")%>&demographic_no=<%=demographic_no%>&appointment=<%=appointment%>','<%=curForm.get("fid") + "_" + demographic_no %>'); return true;"  TITLE='Add This eForm' OnMouseOver="window.status='Add This eForm' ; return true">
            <%=curForm.get("formName")%>
        </a></td>
                <td style="padding-left: 7px"><%=curForm.get("formSubject")%></td>
		<td nowrap align='center'><%=curForm.get("formDate")%></td>
	  </tr>
<%
     }  
 } else {
%>
<tr><td colspan="3" align="center"><bean:message key="eform.showmyform.msgNoData"/></td></tr>
<%}%>               
 
</table>
			</td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
</body>
</html:html>
