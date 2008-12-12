<%@page
	import="java.text.*, java.util.*, oscar.oscarBilling.ca.bc.data.*,oscar.oscarBilling.ca.bc.pageUtil.*,oscar.*,oscar.entities.*"%>
<%--
/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
--%>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<%
  String demo = request.getParameter("demographicNo");
  DxReference dxRef = new DxReference();
  List<DxReference.DxCode> pastDxList = dxRef.getLatestDxCodes(demo);
  int col = 0;
  if (pastDxList != null){%>
<div style="margin-top: 2px; padding-left: 6px;">
<%  
          for (DxReference.DxCode dxc : pastDxList){
              if (col ==0  ){    
              %>
<ul class="dxlist">
	<%}else if ( (col % 5 ) ==0){%>
</ul>
<ul class="dxlist">
	<%}%>
	<li><a href="javascript: function myFunction() {return false; }"
		onClick="quickPickDiagnostic('<%=dxc.getDx()%>');return false;"> <%=dxc.getDx()%>
	<%=dxc.getNumMonthSinceDate()%>M </a></li>
	<%  
          if (col >= 19){ break;}  // 24 gives 5 columns of 5  #  19 gives 4 columns of 5
          col++;
          }%>
</ul>
</div>
<%}%>