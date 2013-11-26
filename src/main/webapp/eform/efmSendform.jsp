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

<%@page import="org.springframework.web.context.support.WebApplicationContextUtils,org.oscarehr.util.OntarioMD,java.util.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.oscarehr.common.dao.*,org.oscarehr.common.model.*"%>

<%

    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    UserPropertyDAO userPropertyDAO = (UserPropertyDAO) ctx.getBean("UserPropertyDAO");

    UserProperty prop = userPropertyDAO.getProp((String) session.getAttribute("user"),  UserProperty.MYDRUGREF_ID);
    String mydrugrefid = prop == null ? null : prop.getValue();
    if (mydrugrefid == null){mydrugrefid = "";}


%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>myDrugref login details</title>

    </head>
    <body>

<%@ include file="efmTopNav.jspf"%>

         <div id="importDiv" class="well" >

<h3>Send to eForm Emporium</h3>
            <center>
            <form action="<%= request.getContextPath() %>/eform/manageEForm.do" method="POST" id="eformSendForm" >
            <table style="text-align: center; border-collapse: collapse; border: 0px;">        
                <tr>
                    <td class="fieldLabel">Username:</td>
                    <td>
                        <input name="username" type="text" value="<%=mydrugrefid%>"/>
                        <input type="hidden" name="method" value="exportEFormSend"/>
                        <input type="hidden" name="fid"    value="<%=request.getParameter("fid")%>"/>
                    </td>
                </tr>
                <tr>
                    <td class="fieldLabel">Password:</td>
                    <td><input name="password" type="password"/></td>
                </tr>
                 <tr>
                    <td class="fieldLabel">Category:</td>
                    <td>
                        <select name="category">
                            <option value="BC Specific">BC Specific</option>
                            <option value="Calculators">Calculators</option>
                            <option value="Cardiovascular">Cardiovascular</option>
                            <option value="Communication With Others">Communication With Others</option>
                            <option value="Geriatrics">Geriatrics</option>
                            <option value="History Taking">History Taking</option>
                            <option value="Imaging">Imaging</option>
                            <option value="Lab">Lab</option>
                            <option value="Musculoskeletal">Musculoskeletal</option>
                            <option value="Ob/Gyn">Ob/Gyn</option>
                            <option value="Graphical eForms">Graphical eForms</option>
                            <option value="Ontario Specific">Ontario Specific</option>
                            <option value="Chronic Disease Action Plans">Chronic Disease Action Plans</option>
                        </select>
                    </td>
                </tr>

                <tr><td colspan="2" style="text-align: left;"><input type="submit" name="subm" class="btn btn-primary" value="Send" onclick="this.value = 'Sending...'; this.disabled = true;"></td></tr>
                <tr><td>&nbsp;</td></tr>
            </table>
            </form>
            </center>
        </div>

        <form action="<%= request.getContextPath() %>/eform/manageEForm.do" method="POST" >
            <input type="hidden" name="method" value="exportEFormSend"/>
        </form>

<%@ include file="efmFooter.jspf"%>

<script>
registerFormSubmit('eformSendForm', 'dynamic-content');
</script>
    </body>
</html>
