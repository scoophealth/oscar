<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
</head>
<body>
<center>Closing Window, Please Wait....</center>
<%@page import="oscar.eform.data.*"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<script type="text/javascript" language="javascript">
   window.close();
   if (!window.opener.closed) {
   window.opener.location.reload();
   window.opener.focus();
    }
</script>
</body>
</html>
