<%
  if(session.getAttribute("user") == null || !( ((String) session.getAttribute("userprofession")).equalsIgnoreCase("doctor") ))
    response.sendRedirect("../../logout.jsp");

    // forward to the page 'form'+study_name+'.jsp'
    if(true) {
        //out.clear();
        pageContext.forward(request.getParameter("study_link") + "?demographic_no=" + request.getParameter("demographic_no") + "&study_no=" + request.getParameter("study_no") ); //forward request&response to the target page "&formId=" + request.getParameter("formId") + 
        //return;
    }
%>