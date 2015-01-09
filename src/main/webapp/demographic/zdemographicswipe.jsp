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

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath()%>/js/global.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/js/jquery.js"></script>
        <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
        <title>PATIENT DETAIL INFO</title>
        <link rel="stylesheet" href="../web.css" />
        <script language="JavaScript">
            <!--
            
            function verifyInput() {
            	var input = document.forms[0].magneticStripe.value;
            	var tracks = input.split(";");
            	track1 = tracks[0];
            	
            	if( track1.length == 78 || track1.length == 79 ) {
            		return true;
            	}
            	
            	alert("I didn't get that.  Try scanning again");
            	document.forms[0].magneticStripe.value = "";
            	return false;
            }
            
            function setfocus() {
            	document.forms[0].magneticStripe.focus();
            }
            //-->
        </script>
    </head>
    <body background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0" onload="setfocus()">

        <table border="0" cellspacing="0" cellpadding="0" width="100%">
            <tr bgcolor="#486ebd">
                <th align=CENTER NOWRAP>
                    <font face="Helvetica" color="#FFFFFF">PATIENT'S DETAIL RECORD</font>
                </th>
            </tr>
        </table>

        <html:form action="/demographic/ValidateSwipeCard" onsubmit="return verifyInput();">

            <div class="container">

                <p class="row">
                    <p class="span">
                        Swipe card
                    </p>
                    <p class="span">
                        <html:text property="magneticStripe" size="79"/>
                    </p>
                </p>

                <p class="row">
                    <p class="span2">
                        <html:submit value="Validate" />
                    </p>
                </p>
            </div>

        </html:form>        
        <br>
        <br>
        <form>
            <input type="button" name="Button" value="Cancel" onclick=self.close();>
        </form>
    </body>
</html>