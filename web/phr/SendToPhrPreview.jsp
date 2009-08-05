<%-- 
    Document   : SendToPhrPreview
    Created on : 8-Jul-2009, 12:18:50 PM
    Author     : apavel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="oscar.oscarDemographic.data.DemographicData"%>
<%@ page import="java.util.Enumeration"%>

<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr"%>

<%
String demographicNo = request.getParameter("demographic_no");
if (demographicNo == null) demographicNo = request.getParameter("demographicNo");
DemographicData demographicData = new DemographicData();
String demographicName = demographicData.getDemographicFirstLastName(demographicNo);
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Preview</title>

        <style type="text/css">
            body {
                font-size: 12px;
            }
            .title {
                border-bottom: 1px solid black;
                font-size: 12px;
                font-weight: bold;
                width: 300px;
                margin-bottom: 10px;
            }
            table {
                border-collapse: collapse;
                padding: 1px;
            }
            .heading, th {
                color: gray;
                font-weight: normal;
                text-align: left;
                width: 60px;
            }
        </style>

        <script type="text/javascript" language="JavaScript" src="phr.js"></script>
        <script type="text/javascript" language="JavaScript">
            function send(obj) {
                if (obj.form.demographic_no.value == "") {
                    alert("Cannot find demographic number");
                    return false;
                }
                if (obj.form.subject.value == "") {
                    alert("Subject cannot be empty");
                    obj.form.subject.focus();
                    return false;
                }
                obj.disabled = true;
                obj.value = "Sending....";
                //phrActionPopup("google.ca");
                obj.form.method.value = 'send';
                obj.form.submit();
            }
            function onloadd() {
        <%--String labId = request.getParameter("labId");%>
        <phr:ifDocumentPreviouslySent documentOscarId="<%=labId%>" recipientDemographicNo="<%=demographicNo%>">
            alert("test");
        </phr:ifDocumentPreviouslySent>---%>
            }
        </script>

    </head>
    <body onload="onloadd()">
        <div class="title">Send to PHR - Preview</div>
        <form action="<%=request.getContextPath()%>/SendToPhr.do" method="POST">
            <input type="hidden" name="demographic_no" value="<%=demographicNo%>">
            <input type="hidden" name="method" value="send">
            <input type="Hidden" name="SendToPhrPreview" value="yes">
            <table>
                <tr><th>Send to:</th><td><%=demographicName%></td></tr>
                <tr><th>Subject:</th><td><input type="text" name="subject"> <span style="font-size: 10px;">(Subject of the message and document name)</span></td></tr>
                <tr><td colspan="2">
                        <span class="heading">Message:</span>
                        <br>
                        <textarea name="message" style="width: 500px; height: 100px;"></textarea>
                    </td>
                </tr>
                <tr><th>Attached</th>
                    <td><img style="width: 20px; height: 20px;" src="<%=request.getContextPath()%>/images/pdf-logo-small.jpg" alt="">
                        <%
                        String labId = request.getParameter("labId");
                        if (labId != null) {%>
                        <%}%>
                        <span style="padding-bottom: 10px; vertical-align: middle;"><input type="submit" onclick="this.form.method.value='documentPreview'" value="Document Preview"></span>
<%--document.getElementById('iframe').src='<%=request.getContextPath()%>/lab/CA/ALL/PrintPDF.do?segmentID=<%=labId%>'--%>
                        <%Enumeration<String> parameterNames = request.getParameterNames();
                        while (parameterNames.hasMoreElements()) {
                            String parameterName = parameterNames.nextElement();
                            if (parameterName.equals("method")) continue;
                            for (String parameterValue: request.getParameterValues(parameterName)) {%>
                            <input type="hidden" name="<%=parameterName%>" value="<%=parameterValue%>">
                        <%}}%>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="height: 40px; vertical-align: bottom;">
                        <div style="width: 200px; border-top: 1px solid #bdbdbd; padding-top: 3px;">
                            <input type="button" onclick="window.close()" value="Cancel">
                            <input type="button" onclick="send(this)" value="Send -->">
                        </div>
                    </td>
                </tr>
            </table>
        </form>
        <iframe name="iframe" id="iframe" style="border:0px; width: 0px; height: 0px;"/>
        
    </body>
</html>
