<!DOCTYPE html>
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
    <title>Bootstrap 101 Template</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet" media="screen">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="<%=request.getContextPath() %>/library/bootstrap/3.0.0/assets/js/html5shiv.js"></script>
      <script src="<%=request.getContextPath() %>/library/bootstrap/3.0.0/assets/js/respond.min.js"></script>
    <![endif]-->

   <style>
	.label{ cursor: pointer; cursor: hand;}
   </style>

  </head>
  <body>


    <div class="container">

      <div class="starter-template">
	<h1>Hello, world!</h1>

	<table class="table table-striped table-hover">
	<thead>
          <tr>
            <th></th>
	    <th></th>
	  </tr>
	</thead>

	<tbody>
	<tr><td><a href="basic-example.jsp">basics</a></td> <td><small><span class="label label-info" onclick='window.location="view-source:basic-example.jsp"'>view source</span></small> This is just a simple sample of what you will need to start using twitter bootstrap in oscar. </td></tr>
	<tr><td><a href="tooltip.jsp">tooltip</a>  </td> <td><small><span class="label label-info" onclick='window.location="view-source:tooltip.jsp"'>view source</span></small> </td></tr>
	<tr><td><a href="popover.jsp">popover</a></td> <td><small><span class="label label-info" onclick='window.location="view-source:popover.jsp"'>view source</span></small> </td></tr>
	<tr><td><a href="modal.jsp">modal</a></td> <td><small><span class="label label-info" onclick='window.location="view-source:modal.jsp"'>view source</span></small> </td></tr>
	<tr><td><a href="icons.jsp">icons</a></td> <td><small><span class="label label-info" onclick='window.location="view-source:icons.jsp"'>view source</span></small> </td></tr>
	<tr><td><a href="alert.jsp">alerts</a></td> <td><small><span class="label label-info" onclick='window.location="view-source:alert.jsp"'>view source</span></small> </td></tr>
	<tr><td><a href="tables.jsp">tables</a></td> <td><small><span class="label label-info" onclick='window.location="view-source:tables.jsp"'>view source</span></small> </td></tr>
	<tr><td><a href="data-tables.jsp">data tables</a></td> <td><small><span class="label label-info" onclick='window.location="view-source:data-tables.jsp"'>view source</span></small> </td></tr>
	</tbody>
	</table>

      </div>

    </div><!-- /.container -->	


    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<%=request.getContextPath() %>/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
  </body>
</html>

