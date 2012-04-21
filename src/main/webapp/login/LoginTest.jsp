<%--     
    Author     : rjonasz
--%>
<%@page import="java.util.List, java.util.Map, java.util.Date, java.util.Calendar, java.util.GregorianCalendar, java.io.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        <%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
        <%
            List<Map<String,Object>> resultList = oscarSuperManager.find("providerDao", "search_signed_confidentiality",
            new Object[] {session.getAttribute("user")});
            Map map = resultList.get(0);
            Date signdate = (Date)map.get("signed_confidentiality");

            Calendar cal = GregorianCalendar.getInstance();
            cal.add(Calendar.YEAR, -1);
            Date cutoff = cal.getTime();

            if( signdate.after(cutoff) ) {
                String proceedURL = (String)request.getSession().getAttribute("proceedURL");
                proceedURL = request.getContextPath() + proceedURL;
                request.getSession().setAttribute("proceedURL", null);                
                response.sendRedirect(proceedURL);
            }

            String path = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "/OSCARloginText.txt";
            FileReader filereader = null;
            BufferedReader reader;
            StringBuffer content = new StringBuffer();
            String line;
            try {
                filereader = new FileReader(path);
                reader = new BufferedReader(filereader);

                while( (line = reader.readLine()) != null ) {
                    content.append(line + "<br>");
                }

                reader.close();

            }catch(FileNotFoundException e) {
                String proceedURL = (String)request.getSession().getAttribute("proceedURL");
                proceedURL = request.getContextPath() + proceedURL;
                request.getSession().setAttribute("proceedURL", null);                
                response.sendRedirect(proceedURL);
            }

          %>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
            <title><bean:message key="provider.login.title.confidentiality"/></title>
    </head>
    <body>
        <div style="text-align:center;">
            <h3><bean:message key="provider.login.title.confidentiality"/></h3>
        <div style="margin-left:auto; margin-right:auto; text-align:left; width:70%; padding:5px; border:2px groove black;"><%=content.toString()%></div>
        <br>
        <form method="post" action="<c:out value="${pageContext.request.contextPath}/login/recordLogin.do"/>">
            <input type="hidden" name="submit" id="submit">
            <input type="submit" onclick="document.forms[0].submit.value='accept';return true;" value="<bean:message key="provider.login.btn.agree"/>">
            <input type="submit" onclick="document.forms[0].submit.value='refuse';return true;" value="<bean:message key="provider.login.btn.refuse"/>">
        </form>
        </div>
    </body>
</html>
