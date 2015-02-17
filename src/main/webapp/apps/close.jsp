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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html>
<head>
<title><bean:message key="global.btnClose"/></title>

<link href="../library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
</head>
<body>
<br>
	<div class="container">
		<div class="row">
		  	<div class="col-md-12">
		  	
		  		<div class="panel panel-default">
				  <div class="panel-heading">
				    <h3 class="panel-title"><bean:message key="global.resources"/></h3>
				  </div>
				  <div class="panel-body">
					<%=session.getAttribute("oauthMessage") %>
					<%session.removeAttribute("oauthMessage"); %>
					<a class="pull-right" onclick="window.close()"><bean:message key="global.btnClose"/></a>
				  </div>
				</div>
		  	
		
		
			</div>
		</div>
	</div>
</body>
</html>