<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
</head>
<body>
<center>Closing Window, Please Wait....</center>
<%@page import="oscar.eform.data.*"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<script type="text/javascript" language="javascript">
   
   if (!window.opener.closed) {
        var parentAjaxId = "<%=request.getParameter("parentAjaxId")%>";
        if( window.opener. writeToEncounterNote ) {
            window.opener.reloadNav(parentAjaxId);
            //window.opener.document.forms['encForm'].elements['reloadDiv'].value = parentAjaxId;
            //window.opener.updateNeeded = true;
        } else {
            if (window.opener.location.href.indexOf("efmpatientformlist.jsp")>=0)
                window.opener.location.reload();
        }
        window.opener.focus();
        window.close();
    }
</script>
</body>
</html>
