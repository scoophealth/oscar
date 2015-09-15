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

<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.common.model.Allergy"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.RemoteDrugAllergyHelper"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="java.util.*,net.sf.json.*,java.lang.reflect.*,java.io.*,org.apache.xmlrpc.*,oscar.oscarRx.util.*,oscar.oscarRx.data.*"  %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_allergy" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_allergy");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
String atcCode =  request.getParameter("atcCode");
String id = request.getParameter("id");

String disabled = oscar.OscarProperties.getInstance().getProperty("rx3.disable_allergy_warnings","false");
if(disabled.equals("false")) {

oscar.oscarRx.pageUtil.RxSessionBean rxSessionBean = (oscar.oscarRx.pageUtil.RxSessionBean) session.getAttribute("RxSessionBean");
Allergy[] allergies = RxPatientData.getPatient(loggedInInfo, rxSessionBean.getDemographicNo()).getAllergies(loggedInInfo);

if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
	try {
		ArrayList<Allergy> remoteAllergies=RemoteDrugAllergyHelper.getRemoteAllergiesAsAllergyItems(loggedInInfo,rxSessionBean.getDemographicNo());

		// now merge the 2 lists
		for (Allergy alleryTemp : allergies) remoteAllergies.add(alleryTemp);
		allergies=remoteAllergies.toArray(new Allergy[0]);
	} catch (Exception e) {
		MiscUtils.getLogger().error("error getting remote allergies", e);
	}
}

Allergy[] allergyWarnings = null;
   RxDrugData drugData = new RxDrugData();
   allergyWarnings = drugData.getAllergyWarnings(atcCode, allergies);


   // Hashtable d = new Hashtable();
    Hashtable d2=new Hashtable();

  //  d.put("id",id);
    d2.put("id",id);
    for(Allergy allg:allergyWarnings){
   	 	String temp=StringUtils.trimToEmpty(allg.getDescription());
        d2.put("DESCRIPTION", temp);

        temp=StringUtils.trimToEmpty(allg.getReaction());
        d2.put("reaction", temp);
    }

   try{
    response.setContentType("text/x-json");
    JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d2 );
    jsonArray.write(out);
    }
   catch(Exception e){
	   MiscUtils.getLogger().error("Error", e);
    }
}
%>
