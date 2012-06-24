<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<html>

<head>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/newCaseManagementView.js"></script>

</head>


<body>
<b>Following fields had bad input and could not be updated:</b>
<br>
<pre><%=request.getAttribute("testOutput") %></pre>
</body>

</html>
