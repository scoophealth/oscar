


<!-- Page to be deleted-->

<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxMessenger" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxMessengerDao" %>
<%@ page import="org.oscarehr.integration.cdx.dao.NotificationDao" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>
<%


    int flag=0;
    CdxMessengerDao cdxMessengerDao= SpringUtils.getBean(CdxMessengerDao.class);
    CdxMessenger cdxMessenger = new CdxMessenger();
    String patient = request.getParameter("patientsearch");
    //String primaryrecipient = request.getParameter("precipients");
    String recipients[]=request.getParameterValues("precipients");
    String specialists="";
    if(recipients!=null && recipients.length>0){

        for (String rec:recipients){
            specialists=specialists+rec.split("@")[0]+",";
        }
        specialists= specialists.substring(0, specialists.length() - 1);
    }

    //String secondaryrecipient = request.getParameter("srecipients");
    String messagetype = request.getParameter("messagetype");
    String documenttype = request.getParameter("documenttype");
    String contentmessage = request.getParameter("contentmessage");
    if(patient!=null ||patient.length()!=0 ) {
        cdxMessenger.setPatient(patient);
        cdxMessenger.setRecipients(specialists);
        cdxMessenger.setCategory(messagetype);
        cdxMessenger.setContent(contentmessage);
        cdxMessenger.setDocumentType(documenttype);
        cdxMessenger.setAuthor(session.getAttribute("userfirstname") + "," + session.getAttribute("userlastname"));
        cdxMessenger.setTimeStamp(new Timestamp(new Date().getTime()));
        cdxMessenger.setDeliveryStatus("Unknown");
        try {
            cdxMessengerDao.persist(cdxMessenger);
            flag=1;
        } catch (Exception ex) {
            MiscUtils.getLogger().error("Got exception saving messenger Information " + ex.getMessage());
        }

    }
    if(flag==1)
    {
 %>
<br>
<div class="container">
<div class="alert alert-success">
    <strong>Success!</strong> The document is successfully saved in the database !
</div>
</div>
   <%

       // out.print("<br><div><center><strong><font size=\"2px\">*Success :The document is successfully saved in the database ! </font> </strong></center></div>");
    }


   %>
<html>
<head>
    <meta http-equiv="Refresh" content="5;url=../cdx/cdxMessenger.jsp">
    <script type="text/javascript" src="/oscar/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="/oscar/js/jquery-ui-1.10.2.custom.min.js"></script>
    <script type="text/javascript" src="/oscar/share/javascript/Oscar.js"></script>

    <link rel="stylesheet" href="/oscar/share/css/bootstrap.min.css">

    <script src="/oscar/share/javascript/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/oscar/share/yui/css/fonts-min.css">
    <link rel="stylesheet" type="text/css" href="/oscar/share/yui/css/autocomplete.css">
    <title>Success</title>
</head>
<body>
<div class="container">
<p> *Sending functionality is under implementation phase. </p>
</div>
</body>
</html>
