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

<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.model.Measurements" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.dao.MeasurementsDao" %>

<%
	String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
	Measurements m = new Measurements();
	m.setComments("");
	m.setDataField("Yes");
	m.setDateEntered(new java.util.Date());
	m.setDateObserved(new java.util.Date());
	m.setDemographicNo(Integer.parseInt(request.getParameter("demographicNo")));
	m.setMeasuringInstruction("");
	m.setProviderNo(providerNo);
	m.setType("medr");
	
	MeasurementsDao dao = (MeasurementsDao)SpringUtils.getBean("measurementsDao");
	dao.addMeasurements(m);
%>
