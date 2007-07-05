<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>
<%
String errorMsg = (String) request.getAttribute("error_msg");
boolean errors = false;
String cssPrefix = "objectGreen";
if (errorMsg != null) errors = true;
if (errors) {
    cssPrefix = "objectRed";
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="../phr/phr.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PHR Call</title>
        <script type="text/javascript" language="JavaScript">
            function startWindowTimeout() {
                var sec=1000;
                setTimeout("closewindow()",sec);
            }

            function closewindow() {
                window.opener=self;
                window.close();
            }
        </script>
    </head>
    <body<%if (!errors) {%> onload="startWindowTimeout()"<%}%>>
        <div class="<%=cssPrefix%>">
            <div class="<%=cssPrefix%>Header">Personal Health Record Action</div>
            <%if (errors) {%>
               <%=errorMsg%>
            <%} else {%>
               SUCCESS<br/>
               <br/>
               Closing window...
            <%}%>
        </div>
        
    </body>
</html>
