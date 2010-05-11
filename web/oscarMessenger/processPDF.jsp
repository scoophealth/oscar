<%@page contentType="text/html"%>
<%@page pageEncoding="ISO-8859-1"%> 
<%@ page language="java"%>
<%@ page
	import="oscar.oscarMessenger.docxfer.send.*,oscar.oscarMessenger.docxfer.util.*, oscar.oscarEncounter.data.*, oscar.oscarEncounter.pageUtil.EctSessionBean "%>
<%@  page
	import=" java.util.*, org.w3c.dom.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.util.*"%>


<%
String demographic_no = (String) request.getParameter("demographic_no");
String uri = (String) request.getParameter("uri");
String pdfTitle = (String) request.getParameter("pdfTitle");

%>

<%@ include file="../admin/dbconnection.jsp"%>



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

