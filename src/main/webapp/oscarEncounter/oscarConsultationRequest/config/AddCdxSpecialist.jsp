<%--

    Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
    Department of Computer Science
    LeadLab
    University of Victoria
    Victoria, Canada

--%>

<%@ page import="ca.uvic.leadlab.obibconnector.facades.receive.ITelco" %>
<%@ page import="ca.uvic.leadlab.obibconnector.facades.registry.IProvider" %>
<%@ page import="org.oscarehr.integration.cdx.CDXSpecialist" %>
<%@ page import="java.util.List" %>
<%@ page import="org.oscarehr.common.dao.ProfessionalSpecialistDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ProfessionalSpecialist" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.consult" rights="w" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.consult");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">

    <%
        CDXSpecialist cdxSpecialist = new CDXSpecialist();
        List<IProvider> providers = cdxSpecialist.findAllTesting();
        ProfessionalSpecialistDao professionalSpecialistDao = SpringUtils.getBean(ProfessionalSpecialistDao.class);
    %>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <title><bean:message
                key="oscarEncounter.oscarConsultationRequest.config.EditSpecialists.title"/>
        </title>
        <html:base/>
    </head>
    <script>
        function BackToOscar() {
            window.close();
        }
    </script>
    <link rel="stylesheet" type="text/css" href="../../encounterStyles.css">
    <body class="BodyStyle">

    <html:errors/>
    <!--  -->
    <table class="MainTable" id="scrollNumber1">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">Consultation</td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td class="Header">Add CDX Service Provider</td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr style="vertical-align: top">
            <td class="MainTableLeftColumn">
                <%
                    oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar titlebar = new oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar(request);
                    out.print(titlebar.estBar(request));
                %>
            </td>
            <td class="MainTableRightColumn">
                <table style="border-collapse: collapse">

                    <!----Start new rows here-->
                    <tr>
                        <td>
                            <h5>Select the specialist you would like to add<br></h5>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <form action="../../oscarConsultationRequest/config/PersistCdxSpecialist.jsp">

                                <div class="ChooseRecipientsBox1">
                                    <table>
                                        <tr>
                                            <th>&nbsp;</th>
                                            <th><bean:message
                                                    key="oscarEncounter.oscarConsultationRequest.config.EditSpecialists.specialist"/>
                                            </th>
                                            <th><bean:message
                                                    key="oscarEncounter.oscarConsultationRequest.config.EditSpecialists.address"/>
                                            </th>
                                            <th><bean:message
                                                    key="oscarEncounter.oscarConsultationRequest.config.EditSpecialists.phone"/>
                                            </th>
                                        </tr>
                                        <tr>
                                            <td><!--<div class="ChooseRecipientsBox1">--> <%

								 String phone = "";
								 List<ProfessionalSpecialist> professionalSpecialists;
								 int first = 0;
								 for(int i=0;i < providers.size(); i++) {
								     String cdxSpecId = providers.get(i).getID();
								     professionalSpecialists = professionalSpecialistDao.findByCdxId(cdxSpecId);
								     if (professionalSpecialists == null || professionalSpecialists.isEmpty()) {
								     String fName = providers.get(i).getFirstName();
								     String lName = providers.get(i).getLastName();
								     String address = "";
								     if (providers.get(i).getClinicName() != null) {
								         address = providers.get(i).getClinicName();
								     } else if (providers.get(i).getStreetAddress() != null) {
								         address += providers.get(i).getStreetAddress();
								     } else if (providers.get(i).getCity() != null) {
								         address += providers.get(i).getCity();
								     } else if (providers.get(i).getProvince() != null) {
								         address += providers.get(i).getProvince();
								     } else if (providers.get(i).getPostalCode() != null) {
								         address += providers.get(i).getPostalCode();
								     }
								     List<ITelco> phones = providers.get(0).getPhones();
								     if(phones!=null && phones.size()>0) {
								         phone = phones.get(0).getAddress();
								     }
                              %>

                                        <tr>
                                            <td><label>
                                                <input type="radio" name="cdxSpecId"
                                                       value="<%=cdxSpecId%>" <% if (first == 0) {%> checked
                                            </label><% first++;
                                            } %>
                                            </td>
                                            <td>
                                                <%
                                                    out.print(lName + " " + fName);
                                                %>
                                            </td>
                                            <td><%=address %>
                                            </td>
                                            <td><%=phone%>
                                            </td>
                                        </tr>
                                        <% }
                                        } %>
                                    </table>
                                </div>
                                <input type="submit" value="Add">
                                <input type="button" value="Close" onclick="window.close()">
                            </form>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn"></td>
            <td class="MainTableBottomRowRightColumn"></td>
        </tr>
    </table>
    </body>
</html:html>
