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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.util.LocaleUtils" %>
<%@page import="java.util.Locale" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
    <%authed=false; %>
    <%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
        if(!authed) {
                return;
        }
%>

<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.ws.rest.util.QuestimedUtil" %>


<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/library/bootstrap/3.0.0/css/bootstrap.min.css" />

<%
        Locale locale = request.getLocale();
        LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
        String demographicNo= request.getParameter("demographic_no");
         DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
        Demographic demo = demographicDao.getDemographic(demographicNo);
        String errorMsg="";
        String email=request.getParameter("email");
        if(email!=null) {
            errorMsg=QuestimedUtil.createAccount(loggedInInfo,demographicNo,email);
            if(errorMsg==null) {
                errorMsg = LocaleUtils.getMessage(locale, "questimed.connectionerror");
            }
            else if(errorMsg.isEmpty())
            {
                response.sendRedirect(request.getContextPath() + "/questimed/launch.jsp?demographic_no="+demographicNo);
            }
        } else {
            email=demo.getEmail();
        }
%>
<html:html locale="true">
    <head>
        <title>Questimed</title>
    </head>
    <body>
        <form method="post">
            <br>
            <h4><bean:message key="questimed.patient.registration" /></h4>
            <br>
            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
                <div class="row">
                    <div class="form-group">
                        <label class="col-sm-12 col-md-3 control-label"><bean:message key="questimed.patient" /></label>
                        <div class="col-sm-12 col-md-9" id='patientInfo'><%=demo.getHin()%><br>
                            <%=demo.getFirstName()%> <%=demo.getLastName()%><br>
                            <%=demo.getBirthDayAsString()%> <%=demo.getSex()%><br>
                        </div>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="form-group">
                        <label class="col-sm-12 col-md-3  control-label"><bean:message key="questimed.email" /></label>
                        <div class="col-sm-12 col-md-9 "><input type='text' class="form-control" name=email autofocus id='email' value="<%=email%>"></div>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="form-group">
                        <label class="col-sm-12 col-md-3 control-label"></label>
                        <div class="col-sm-12 col-md-9">
                            <bean:message key="questimed.Authorization" /><br><br>
                            <input type='submit' class="btn btn-primary" id='createAccountBtn'  value='<bean:message key="questimed.createAccount" />'>
                        </div>
                    </div>
                </div>
            </div>
            <br>
            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
                <br>
                <div class="row">
                    <div class="form-group">
                        <label class="col-sm-12 col-md-3 control-label"></label>
                        <div class="col-sm-12 col-md-9" id="msg">
                            <p class='text-danger'><%=errorMsg%></p>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html:html>
