<%@ page import="ca.uvic.leadlab.obibconnector.facades.exceptions.OBIBException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <meta http-equiv="Refresh" content="15;url=../cdx/cdxMessenger.jsp">
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

<br>

<!--Displays success and error message while sending documents.-->

<div class="container">
    <%
        boolean success=false;
        if(request.getAttribute("success") !=null) {
           success = (Boolean) request.getAttribute("success");
        }


        if (success==true){
    %>
    <div class="alert alert-success">
        <strong>Success!</strong> The document is successfully sent.
    </div>
    <%
        }
        else{
            if(request.getAttribute("error") !=null){
                OBIBException e=(OBIBException) request.getAttribute("error");
                String additionalText = e.getObibMessage();
                if (additionalText == null || additionalText.isEmpty()) {
                    additionalText = e.getMessage();
                }
    %>
    <div class="alert alert-danger">
        <strong>Error!</strong> <%=additionalText%>.
    </div>
    <%
        }
            else {
    %>

    <div class="alert alert-danger">
        <strong>Error!</strong> Sending document failed, please try sending again.
    </div>
    <%
        }
        }
    %>
</div>
</body>
</html>
