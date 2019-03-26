<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
    <%authed=false; %>
    <%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
    if(!authed) {
        return;
    }
%>

<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="org.oscarehr.phr.util.MyOscarUtils,org.oscarehr.myoscar.utils.MyOscarLoggedInInfo,org.oscarehr.util.WebUtils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="oscar.dms.*,java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%@ page import="oscar.OscarProperties,oscar.log.*"%>
<%@ page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="oscar.util.ConversionUtils" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils,oscar.oscarLab.ca.all.*,oscar.oscarMDS.data.*,oscar.oscarLab.ca.all.util.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.oscarehr.common.dao.*,org.oscarehr.common.model.*,org.oscarehr.util.SpringUtils"%><%

    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

    String demoName=request.getParameter("demoName");
    String documentNo = request.getParameter("segmentID");
    Integer docNo = Integer.parseInt(documentNo);
    DocumentDao docDao = SpringUtils.getBean(DocumentDao.class);

    Document doc = docDao.findActiveByDocumentNo(docNo).get(0);

    String docXml = doc.getDocxml();

%>
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>CDX Document</title>

    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <script src="../js/jquery.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>


</head>
<body>

<div class="container-fluid">

    <div class="row">
        <div class="col-md-12">
            <div class="row">
                <div class="col-md-6">
                    <h3 >
                        [Titel of document]
                    </h3>
                    <table class="table table-condensed">
                        <tbody>
                        <tr >
                            <td class="active col-md-1">Document type:</td>
                            <td class="col-md-3">templatename/specifictype</td>
                        </tr>
                        <tr>
                            <td class="active">Author, Date:</td>
                            <td>name/time</td>
                        </tr>
                        <tr>
                            <td class="active">Status:</td>
                            <td>Status</td>
                        </tr>
                        <tr>
                            <td class="active">Custodian:</td>
                            <td>Custodian</td>
                        </tr>
                        <tr>
                            <td class="active">Device, Time:</td>
                            <td>Device/time</td>
                        </tr>
                        <tr>
                            <td class="active">Parent document:</td>
                            <td>N/A</td>
                        </tr>
                        <tr>
                            <td class="active">Procedure, Date:</td>
                            <td>procedure name, date</td>
                        </tr>
                        <tr>
                            <td class="active">Performer:</td>
                            <td>procedure performer</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="col-md-6">
                    <h3 >
                        Patient: Name, Name
                    </h3>

                    <table class="table table-condensed">
                        <tbody>
                        <tr >
                            <td class="active col-md-1">Primary Recipient:</td>
                            <td class="col-md-3">name, name</td>
                        </tr>
                        <tr>
                            <td class="active">Secondary Recipients:</td>
                            <td>name, name, name</td>
                        </tr>
                        <tr>
                            <td class="active">Ordering Provider:</td>
                            <td>name name</td>
                        </tr>
                        <tr>
                            <td class="active">Family Provider:</td>
                            <td>Custodian</td>
                        </tr>
                        <tr>
                            <td class="active">Participating Providers:</td>
                            <td>name, name</td>
                        </tr>
                        <tr>
                            <td class="active">Admission, Discharge:</td>
                            <td>Time, Time</td>
                        </tr>
                        <tr>
                            <td class="active">Disposition:</td>
                            <td>dispo</td>
                        </tr>
                        <tr>
                            <td class="active">Attachments:</td>
                            <td>attach</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="jumbotron">
                <p>
                    This is a template for a simple marketing or informational website. It includes a large callout called the hero unit and three supporting pieces of content. Use it as a starting point to create something more unique.
                </p>

            </div>

        </div>
    </div>
</div>


</body>
</html>