<!--  
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
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<form name="servicetypeform" method="post">
<table width="75%" border="0" height="285">
  <tr>
    <td width="74%" valign="top">
        <table width="100%" border="0">
          <tr> 
          <td colspan="2" class="white">Adding New Form</td>
        </tr>
        <tr> 
          <td width="26%" class="white">Service Type ID:</td>
          <td width="74%" class="white">
              <input type="text" name="typeid" maxlength="3">
            </td>
        </tr>
        <tr> 
          <td width="26%" class="white">Service Type Name:</td>
          <td width="74%" class="white">
              <input type="text" name="type" value="Service Type Name">
            </td>
        </tr>
        <tr> 
          <td width="26%" class="white">Group1 Name:</td>
          <td width="74%" class="white">
              <input type="text" name="group1" value="Group 1 Description">
            </td>
        </tr>
        <tr> 
          <td width="26%" class="white">Group 2 Name:</td>
          <td width="74%" class="white">
              <input type="text" name="group2" value="Group 2 Description">
            </td>
        </tr>
        <tr> 
          <td width="26%" class="white">Group 3 Name:</td>
          <td width="74%" class="white">
              <input type="text" name="group3" value="Group 3 Description">
            </td>
        </tr>
        <tr> 
          <td width="26%" class="white">
              <input type="button" name="addForm" value="Add Form" onClick="valid(this.form)">
            </td>
          <td width="74%" class="white">&nbsp;</td>
        </tr>
      </table>
    </td>
    <td width="26%" bgcolor="#336699" valign="top"> 
      <table width="100%" border="0">
        <tr> 
          <td colspan="2" valign="top" class="black">Delete Existing Type</td>
        </tr>

  <% 

 ResultSet rs=null ;
  ResultSet rs2=null ;

    
  rs = apptMainBean.queryResults("%", "search_ctlbillservice");
int rCount = 0;
  boolean bodd=false;

  if(rs==null) {
    out.println("failed!!!"); 
  } else {
  %>
  <% 
    while (rs.next()) {
%><tr> 
          <td width="23%" valign="top" class="black"> <a href=# onClick='onUnbilled("dbManageBillingform_delete.jsp?servicetype=<%=rs.getString("servicetype")%>");return false;' title="Delete Billing Form"><%=rs.getString("servicetype")%></a></td>
          <td width="77%" class="black"><%=rs.getString("servicetype_name")%></td>
        </tr>
<%
}}%>
       
      </table>
    </td>
  </tr>
</table>
</form>
