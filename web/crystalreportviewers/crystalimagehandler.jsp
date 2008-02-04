<%@ page import="com.crystaldecisions.report.web.viewer.CrystalImageHandler" %><%! com.crystaldecisions.report.web.viewer.CrystalImageHandler imageHandler = null; %><%
   imageHandler = new CrystalImageHandler();
   try
   {
       imageHandler.handleImage(request, response, getServletConfig().getServletContext());
   }
   catch (Exception e)
   {
       e.printStackTrace();
   }
%>