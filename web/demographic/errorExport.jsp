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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Export Error</title>
    </head>
    <body>

    <h1>Error Exporting Demographic Files!</h1>
    
    <p>Please check the following:</p>
    1) "TMP_DIR: &lt;&lt;export-directory&gt;&gt;" is present in "oscar.properties" file.<br>
    2) &lt;&lt;export-directory&gt;&gt; is presence in the system, e.g. /usr/local/export/<br>
    3) Full read/write permissions is set for &lt;&lt;export-directory&gt;&gt;
    
    
    </body>
</html>
