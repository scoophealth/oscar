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
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
--%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@page import="java.util.*,org.apache.struts.util.LabelValueBean,org.oscarehr.common.model.Demographic" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%String providerNo=(String) session.getAttribute("user");
    if(providerNo==null){
        response.sendRedirect("../logout.jsp");
    }
 List<Demographic> ds=(List<Demographic>)request.getAttribute("demographics");
 HashMap<String,String> ps=(HashMap<String,String>)request.getAttribute("providerNames");


%>
<html>
    <head>
        <script type="text/javascript" src="<c:out value="${ctx}/phr/phr.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/screen.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/scriptaculous.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/effects.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/controls.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>
        <script type="text/javascript">
            function backToOscar(providerNo){
                var url="<c:out value="${ctx}"/>" + "/mymeds/providerLogin.do";
                var data = "method=setMyMedsPreference&providerNo="+providerNo+"&preference=no";
                new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
                        window.location.href="../logout.jsp";
                }});
                //ajax to change user preference.
            }
        </script>
                <style type="text/css">
                    #dobname{
                        word-wrap: break-word;
                    }
                    .colname{
                        width:14%
                    }
                </style>
    </head>
    <body>
        <table><tr>
                <th colspan="2" >MyMeds</th>
                <td>
                    <!--a href="javascript:void(0);" onclick="backToOscar(<%=providerNo%>);">Log in OSCAR</a>
                    <a href="../logout.jsp" >Logout</a-->
                <td>
            </tr>
            <tr><td>
                    <select>
                        <option>Name</option>
                        <option>Phone</option>
                        <option>DOB(yyyy/mm/dd)</option>
                        <option>Address</option>
                        <option>Health Ins.No.</option>
                        <option>Chart No.</option>
                    </select>
                    <input type="text" name="searchDemo"><input type="submit" value="Search"/><br>
                </td>
                <td>
                    
                <td>
            </tr>
        </table>
        <table>
            <tr>
                <td>
                    <table>
                        <tr><td align="center" colspan="7">Patients</td></tr>
                        <tr><td class="colname" align="center">HIN</td><td  class="colname" align="center">Name</td>
                            <td  class="colname" align="center">Gender</td><td  class="colname" id="dobname" align="center">DOB (yyyy/mm/dd)</td>
                            <td  class="colname" align="center">Doctor</td><td  class="colname" align="center">Pat Status</td>
                            <td  class="colname" align="center">Phone</td><!--td align="center">Most Recently Viewed</td--></tr>

                                <%for(int i=0;i<ds.size();i++){
                                Demographic demo= ds.get(i);
                                String hin = demo.getHin();
                                String dob= demo.getYearOfBirth()+"/"+demo.getMonthOfBirth()+"/"+demo.getDateOfBirth();
                                String demographic_name=demo.getDisplayName();
                                String gender=demo.getSex();
                                String mrp=demo.getProviderNo();
                                String pName=ps.get(mrp);
                                String pStatus=demo.getPatientStatus();
                                String tel=demo.getPhone();
                                %>
                                <tr><td align="center"><%=hin%></td><td align="left" ><a href="javascript:void(0);" onclick=""><%=demographic_name%></a></td><td align="center" ><%=gender%></td><td align="center" ><%=dob%></td><td align="left" ><%=pName%></td><td align="center" ><%=pStatus%></td><td align="center" ><%=tel%></td></tr>
                        <%}%>
                    </table>
                </td>
                <td>
                    <table>
                        <%for(int i=0;i<5;i++){%>
                        <tr><td><a href="javascript:void(0);">most recently viewed <%=i+1%></a></td></tr>
                        <%}%>
                    </table>
                </td>
            <tr>
        </table>
        
        
       
    </body>
</html>

