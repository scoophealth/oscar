<%@ page import="oscar.form.data.*" errorPage="../errorpage.jsp" %>

<%
  if(session.getValue("user") == null || !( ((String) session.getValue("userprofession")).equalsIgnoreCase("doctor") ))
    response.sendRedirect("../logout.jsp");

    // forward to the page 'form_link'
    if(true) {
        out.clear();
		//forward to the current specified form, e.g. ../form/formar.jsp?demographic_no=
		String[] formPath = (new FrmData()).getShortcutFormValue(request.getParameter("demographic_no"), request.getParameter("formname"));
		//System.out.println(formPath[0]);
        pageContext.forward(formPath[0] + request.getParameter("demographic_no")  + "&formId=" + formPath[1]); 
        return;
    }
%>