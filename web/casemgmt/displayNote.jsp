<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%
    String noteStr = (String)request.getAttribute("noteStr");
    Boolean raw = (Boolean)request.getAttribute("raw");
    if( raw )
        out.print(noteStr);
    else
        out.print(noteStr.replaceAll("\n","<br>"));
%>