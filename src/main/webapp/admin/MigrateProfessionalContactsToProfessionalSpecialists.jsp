<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page contentType="text/html"%>
<%@ include file="/casemgmt/taglibs.jsp"%>
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


<%
String curUser_no = (String) session.getAttribute("user");
%>

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">

<script type="text/JavaScript">
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
  var popup=window.open(page, "groupno", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}
</script>
<html:base />

<title>Migration Tool</title>


</head>


<body>
        <table class="MainTable" id="scrollNumber1" name="encounterTable" style="margin: 0px;">
            <tr class="topbar">
                <td class="MainTableTopRowLeftColumn" width="60px">Migrate</td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>Migrate Professional Contacts to Professional Specialists</td>
                            <td style="text-align: right;"  > 
                                    <a href="javascript: popupStart(300, 400, 'About.jsp')">About</a> |
                                    <a href="javascript: popupStart(300, 400, 'License.jsp')">License</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
         </table>
         <br/>
         
         <%if(request.getParameter("action") == null) { %>
        <table  cellspacing="0" style="margin: 0px;" >
            <tr><td>
            	<ul>
           			<li>This utility will move all the professional contacts to the professional specialists table, and update
            any references to them.</li>
            <li> Res phone, Cell phone, System Id, and Note will all go into the annotation field.</li>
            <li> You will need to assign the professional specialist records to services manually</li>
            <li> You should make sure NEW_CONTACTS_UI_EXTERNAL_CONTACT=false in the properties after migration complete</li>
            	</ul>
            </td></tr>
        </table>
        
        <br/>
        
        <form action="MigrateProfessionalContactsToProfessionalSpecialists.jsp">
        	<input type="submit" value="Perform Migration"/>
        	<input type="hidden" name="action" value="migrate"/>
        </form>
        
        <% } else { 
        	boolean result = org.oscarehr.admin.web.MigrateProfessionalContactsHelper.doMigration();
        	if(result) {
        		%><h4>Migration was successful</h4><%
        	} else {
        		%><h4>Migration failed</h4><%
        	}
        } %>
         
</body>

</html:html>
