
<%/*

    spellcheck-entry.jsp - This is a temporary page that the user doesn't
     see. The purpose of this page is to obtain the values of the fields
     that spell checking is desired on and submit those values to the 
     spellcheck-results.jsp page.
 
    Copyright (C) 2005 Balanced Insight, Inc.

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/%>

<%@ page import="java.util.Enumeration"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" href="spellcheck.css" type="text/css" />
</head>
<body>

<form name="entry" method="post" action="spellcheck-results.jsp">

<%
    for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) 
    {
        String param = (String)e.nextElement();
        if( param.startsWith("element_") )
        {
            String value = request.getParameter( param );
            %> <input type="hidden" name="<%=param%>_name"
	value="<%=value%>" /> <input type="hidden" name="<%=param%>_value" /> <script>
            <!--
                document.entry.<%=param%>_value.value = opener.document.<%=value%>.value;
            //-->
            </script> <%
        }
    }
%>
</form>

<script>
<!--
    document.entry.submit();
// -->
</script>

</body>
</html>

