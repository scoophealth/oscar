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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="java.text.*, java.util.*, oscar.oscarBilling.ca.bc.data.*,oscar.oscarBilling.ca.bc.pageUtil.*,oscar.*,oscar.entities.*"%>
<style type="text/css">
      ul.dxlist {
        margin-right:10px;
        margin-top:0px;
        //margin: 0;
        padding: 0;
        list-style:none;
        font-size: 13px;
        float:left;
      }

      ul.dxlist li {
        margin: 0;
        padding: 0;
        line-height: 1.3em;
      }
      
      ul.dxlist li a {
        text-decoration: none;
        font-weight: bold;
      }

  </style>

  <%
  String demo = request.getParameter("demographicNo");
  DxReference dxRef = new DxReference();
  List<DxReference.DxCode> pastDxList = dxRef.getLatestDxCodes(demo);
  int col = 0;
  if (pastDxList != null){%>
  <div style="margin-top: 2px;padding-left:6px;">
          <%  
          for (DxReference.DxCode dxc : pastDxList){
              if (col ==0  ){    
              %>
          <ul class="dxlist" >
              <%}else if ( (col % 5 ) ==0){%>
          </ul>
          <ul class="dxlist" >
             <%}%>  
             <li>
                 <a href="javascript: function myFunction() {return false; }" onClick="quickPickDiagnostic('<%=dxc.getDx()%>');return false;" title="<%=dxc.getDesc()%>">
                    <%=dxc.getDx()%> <%=dxc.getNumMonthSinceDate()%>M
                 </a>
             
             </li>
          <%  
          if (col >= 19){ break;}  // 24 gives 5 columns of 5  #  19 gives 4 columns of 5
          col++;
          }%>
          </ul>
  </div>
  <%}%>
