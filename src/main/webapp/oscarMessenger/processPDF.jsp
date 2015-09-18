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

<%@ page
	import="oscar.oscarMessenger.docxfer.send.*,oscar.oscarMessenger.docxfer.util.*, oscar.oscarEncounter.data.*, oscar.oscarEncounter.pageUtil.EctSessionBean "%>
<%@  page
	import=" java.util.*, org.w3c.dom.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_msg" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_msg");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="oscar.util.*"%>


<%
String demographic_no = request.getParameter("demographic_no");
String uri = request.getParameter("uri");
String pdfTitle = request.getParameter("pdfTitle");

%>




<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Generate Preview Page</title>
</head>
<script type="text/javascript">   
    function SetBottomURL(url) {
        f = parent.attFrame;
        
        if ( url != "") {
            loc = url;
        }
        else {
            loc = document.forms[0].url.value;
        }
        f.location = loc;          
    }
    function GetBottomSRC() {
        f = parent.attFrame;
        document.forms[0].srcText.value = f.document.body.innerHTML;       
    }
    


    </script>
<body>
<%-- <jsp:useBean id="beanInstanceName" scope="session" class="beanPackage.BeanClassName" /> --%>
<%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>


<html:form action="/oscarMessenger/ProcessDoc2PDF">

        Attaching <%=demographic_no%>
	<%=pdfTitle%>

	<textarea name="srcText" rows="5" cols="80"></textarea>
	<html:hidden property="isPreview" value="false" />
	<html:submit property="ok" />
	<html:hidden property="pdfTitle" value="<%=pdfTitle%>" />

</html:form>

<script>
            SetBottomURL('<%=uri%>' + "&demographic_no=" + '<%=demographic_no%> ' );
            setTimeout("GetBottomSRC()", 5000); 
            setTimeout("document.forms[0].submit()", 5000);     
            this.close();
            parent.window.focus();
            
        </script>

</body>
</html>
