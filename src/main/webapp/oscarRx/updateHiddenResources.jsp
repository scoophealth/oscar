<%@page import="java.util.*" %>
<%
       String hr = request.getParameter("hiddenResources");
       if(hr.length()==0){
                Hashtable ht=new Hashtable();
                session.setAttribute("hideResources",ht);
       }
%>
