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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page contentType="text/xml"%>
<%@ page import="java.util.*, java.sql.*, javax.xml.parsers.*, org.w3c.dom.*, oscar.util.*,java.io.*,org.xml.sax.InputSource" errorPage="../../appointment/errorpage.jsp"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.common.model.Demographic"%>
<%@ page import="org.oscarehr.common.dao.DemographicDao"%>
<%@ page import="org.oscarehr.common.model.EChart"%>
<%@ page import="org.oscarehr.common.dao.EChartDao"%>
<%
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
    EChartDao eChartDao = SpringUtils.getBean(EChartDao.class);
%>

<jsp:useBean id="studyMapping" class="java.util.Properties" scope="page" />

<%--database command part  --%>
<%@ include file="../../admin/dbconnection.jsp"%>

<%
    String demoNo = request.getParameter("demographic_no");
    Properties demo = new Properties();
    Properties form = new Properties();
    Properties echart = new Properties();
    Properties allergy = new Properties();
    Properties drug = new Properties();

	//read the mapping file
    try {
      studyMapping.load(new FileInputStream("../webapps/"+ oscarVariables.getProperty("project_home") +"/form/study/formdiabete2studymapping.txt")); //change to speciallll name
    } catch(Exception e) {
    	MiscUtils.getLogger().error("*** No Mapping File ***", e);
    	}

	//take data from demographic
    Demographic d = demographicDao.getDemographic(demoNo);
    if (d != null) {
        demo.setProperty("demographic.first_name", d.getFirstName());
        demo.setProperty("demographic.last_name", d.getLastName());
        demo.setProperty("demographic.sex", d.getSex());
        demo.setProperty("demographic.phone", d.getPhone());
        demo.setProperty("demographic.hin", d.getHin());

        demo.setProperty("demographic.postal", d.getPostal()!=null?d.getPostal().replaceAll(" ", ""):"");
        demo.setProperty("demographic.dob",d.getYearOfBirth() +"-"+ (d.getMonthOfBirth().length()<2?("0"+d.getMonthOfBirth()):d.getDateOfBirth()) +"-"+ (d.getDateOfBirth().length()<2?("0"+d.getDateOfBirth()):d.getDateOfBirth()) );
	}

	//xml part
    Document doc = UtilXML.newDocument();

	UtilXML.addNode(doc, "contact");

	Node contact1 = doc.getFirstChild();
	UtilXML.addNode(contact1, "contact_firstname", demo.getProperty(studyMapping.getProperty("contact.contact_firstname")));
	UtilXML.addNode(contact1, "contact_lastname", demo.getProperty(studyMapping.getProperty("contact.contact_lastname")));
	UtilXML.addNode(contact1, "contact_sex", demo.getProperty(studyMapping.getProperty("contact.contact_sex")));
	UtilXML.addNode(contact1, "contact_dob", demo.getProperty(studyMapping.getProperty("contact.contact_dob")));

	UtilXML.addNode(contact1, "conphone");
	Node conphone = contact1.getLastChild();
	UtilXML.addNode(conphone, "conphone_phonenumber", demo.getProperty(studyMapping.getProperty("contact.conphone.conphone_phonenumber")));

	UtilXML.addNode(contact1, "conidentity");
	Node conidentity = contact1.getLastChild();
	UtilXML.addNode(conidentity, "conidentity_addrpostal", demo.getProperty(studyMapping.getProperty("contact.conidentity.conidentity_addrpostal")));

	UtilXML.addNode(contact1, "conEHRpatient");
	Node conEHRpatient = contact1.getLastChild();
	UtilXML.addNode(conEHRpatient, "conEHRpatient_EHR_id", studyMapping.getProperty("contact.conEHRpatient.conEHRpatient_EHR_id"));

	out.clear();
    out.flush();
    out.println(UtilXML.toXML(doc, "contact1_2.dtd"));
%>
