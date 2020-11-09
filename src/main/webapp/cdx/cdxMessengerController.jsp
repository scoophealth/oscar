

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
    String patient = request.getParameter("patient");
    //String primaryrecipient = request.getParameter("precipients");
    String precipients[]=request.getParameterValues("precipients[]");
    String srecipients[]=request.getParameterValues("srecipients[]");
    String msgType = request.getParameter("msgtype");
    String documentType = request.getParameter("documenttype");
    String content = request.getParameter("content");

    String recipientsToStore="";
    String pSpecialists="";
    String sSpecialists="";
    if(precipients!=null && precipients.length>0){

        for (String rec:precipients){
            String splittedSpecialistsAndClinics[] = rec.split("@");
            recipientsToStore = recipientsToStore + splittedSpecialistsAndClinics[0] + ", ";
            pSpecialists=pSpecialists+rec+'#';
        }
        pSpecialists= pSpecialists.substring(0, pSpecialists.length() - 1);
        recipientsToStore = recipientsToStore.substring(0, recipientsToStore.length() - 2);
    }
    if(srecipients!=null && srecipients.length>0){

        for (String rec:srecipients){
            String splittedSpecialistsAndClinics[] = rec.split("@");
            recipientsToStore = recipientsToStore + ", " + splittedSpecialistsAndClinics[0];
            sSpecialists=sSpecialists+rec+'#';
        }
        sSpecialists= sSpecialists.substring(0, sSpecialists.length() - 1);
    }

    if(patient!=null ||patient.length()!=0 ) {
        cdxMessenger.setPatient(patient);
        cdxMessenger.setRecipients(recipientsToStore);
        cdxMessenger.setPrimaryRecipient(pSpecialists);
        cdxMessenger.setSecondaryRecipient(sSpecialists);
        cdxMessenger.setCategory(msgType);
        cdxMessenger.setContent(content);
        cdxMessenger.setDocumentType(documentType);
        cdxMessenger.setAuthor(session.getAttribute("userfirstname") + "," + session.getAttribute("userlastname"));
        cdxMessenger.setTimeStamp(new Timestamp(new Date().getTime()));
        cdxMessenger.setDeliveryStatus("Not Sent");
        cdxMessenger.setDraft("Y");
        try {
            cdxMessengerDao.persist(cdxMessenger);
        } catch (Exception ex) {
            MiscUtils.getLogger().error("Got exception saving messenger Information " + ex.getMessage());
        }

    }

 %>
<br>
<div class="container">
<div class="alert alert-success">
    <strong>Success!</strong> The document is successfully saved in the database !
</div>
</div>
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
</body>
</html>
