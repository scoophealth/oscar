<%@page contentType="text/html"%>
<%@page pageEncoding="ISO-8859-1"%> 
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.util.*"%>
<% 

    String srcText = (String) request.getParameter("srcText");
    
// Doc2PDF.parseJSP2PDF( request, response, "http://localhost:8084/oscar_mcmaster/index.jsp" ); 

    Doc2PDF.parseString2PDF( request, response, "<HTML>" + srcText + "</HTML>" );

%>
