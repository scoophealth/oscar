<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" /> 
 <%   
  if(session.getValue("user") == null)
    response.sendRedirect("../../logout.jsp");
  String user_no;
  user_no = (String) session.getAttribute("user");
           String docdownload = oscarVariables.getProperty("project_home") ;;
           session.setAttribute("homepath", docdownload);      

%>

<html:html>
<head>
<title><bean:message key="admin.admin.btnImportFormData"/></title>
<meta http-equiv="Content-Type" content="text/html">
</head>

<body bgcolor="#FFFFFF" text="#000000">
<p><font face="Arial, Helvetica, sans-serif" size="2"><b><bean:message key="admin.admin.btnImportFormData"/></b></font></p>
<html:form action="/form/xmlUpload.do" method="POST" enctype="multipart/form-data">
  <font face="Arial, Helvetica, sans-serif" size="2"> </font>
<html:errors/>
<table width="400" border="0">
    <tr> 
      <td width="181"><b><font face="Arial, Helvetica, sans-serif" size="2">Select Data (in zip format)</font></b></td>
      <td width="209"><font face="Arial, Helvetica, sans-serif" size="2"> 
        <input type="file" name="file1" value=""></font></td>
    </tr>
        <tr> 
      <td width="181"> 
            <input type="submit" name="Submit" value="<bean:message key="admin.admin.btnImportFormData"/>">
      </td>
      <td width="209">&nbsp;</td>
    </tr>
  </table>

  <p><font face="Arial, Helvetica, sans-serif" size="2"> </font></p>
  <p>&nbsp; </p>
</html:form>
</body>
</html:html>
