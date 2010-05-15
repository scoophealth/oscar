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

<%@ page language="java"%>
<%@ page import="oscar.form.*"%>
<%@page
	import="java.util.*,java.io.*,oscar.oscarBilling.ca.bc.MSP.*,oscar.oscarBilling.ca.bc.administration.*"%>
<%@ include file="../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="../billing/CA/BC/dbBilling.jsp"%>
<%
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    oscar.oscarBilling.ca.bc.pageUtil.WCBForm form = new oscar.oscarBilling.ca.bc.pageUtil.WCBForm();

    form.setWCBForms(apptMainBean.queryResults(formId, "select_wcb_from_ID"));
    request.setAttribute("WCBForm",form);

    pageContext.forward("../billing/CA/BC/formwcb.jsp?readonly=true");

%>
