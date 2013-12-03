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
    <title>Bootstrap 101 Tooltip Template</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet" media="screen">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="<%=request.getContextPath() %>/library/bootstrap/3.0.0/assets/js/html5shiv.js"></script>
      <script src="<%=request.getContextPath() %>/library/bootstrap/3.0.0/assets/js/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    



     
    <div class="container">

      <div class="starter-template">
        <h1>Hello, tool! <small><a href="index.jsp">view example list</a></small></h1>
    	<button data-original-title="Tooltip on left" type="button" class="btn btn-default" data-toggle="tooltip" data-placement="left" title="">Tooltip on left</button>
        <button data-original-title="Tooltip on top" type="button" class="btn btn-default" data-toggle="tooltip" data-placement="top" title="">Tooltip on top</button>
        <button data-original-title="Tooltip on bottom" type="button" class="btn btn-default" data-toggle="tooltip" data-placement="bottom" title="">Tooltip on bottom</button>
        <button data-original-title="Tooltip on right" type="button" class="btn btn-default" data-toggle="tooltip" data-placement="right" title="">Tooltip on right</button>
      </div>

    </div><!-- /.container --> 

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<%=request.getContextPath() %>/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>

	<script>
	$('.btn-default').tooltip();
	</script>


  </body>
</html>

