<%
  if(session.getValue("user") == null || !( ((String) session.getValue("userprofession")).equalsIgnoreCase("doctor") ))
    response.sendRedirect("../logout.jsp");

    // forward to the page 'form_link'
    if(true) {
        out.clear();
        pageContext.forward(request.getParameter("form_link") + "?demographic_no=" + request.getParameter("demographic_no") ); //+ "&study_no=" + request.getParameter("study_no") ); //forward request&response to the target page "&formId=" + request.getParameter("formId") + 
        return;
    }
%>