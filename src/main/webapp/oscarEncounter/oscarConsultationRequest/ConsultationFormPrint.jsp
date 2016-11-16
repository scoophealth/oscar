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
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_con");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%! boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable(); %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp" %>
<!-- add for special encounter -->
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<!-- end -->
<%@ page import="oscar.OscarProperties, oscar.oscarClinic.ClinicData, java.util.*" %>

<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.ConsultationRequestExt"%>
<%@page import="org.oscarehr.common.dao.ConsultationRequestExtDao"%>

<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.Provider"%>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%><html:html locale="true">

<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);

    oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil reqFrm;
    reqFrm = new oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil ();
    reqFrm.estRequestFromId(LoggedInInfo.getLoggedInInfoFromSession(request), (String)request.getAttribute("reqId"));

	String selectedSite = reqFrm.siteName;

    reqFrm.specPhone = request.getParameter("phone");

    if (reqFrm.specPhone == null || reqFrm.specPhone.equals("null")){
        reqFrm.specPhone = new String();
    }

    reqFrm.specFax = request.getParameter("fax");
    if (reqFrm.specFax == null || reqFrm.specFax.equals("null")){
        reqFrm.specFax = new String();
    }

    reqFrm.specAddr = request.getParameter("address");
    if (reqFrm.specAddr == null || reqFrm.specAddr.equals("null")){
        reqFrm.specAddr = new String();
    }

    OscarProperties props = OscarProperties.getInstance();
    ClinicData clinic = new ClinicData();
    String strPhones = clinic.getClinicDelimPhone();

    if (strPhones == null) { strPhones = ""; }
    String strFaxes  = clinic.getClinicDelimFax();
    if (strFaxes == null) { strFaxes = ""; }
    Vector vecPhones = new Vector();
    Vector vecFaxes  = new Vector();
    StringTokenizer st = new StringTokenizer(strPhones,"|");

    while (st.hasMoreTokens()) {
         vecPhones.add(st.nextToken());
    }

    st = new StringTokenizer(strFaxes,"|");
    while (st.hasMoreTokens()) {
         vecFaxes.add(st.nextToken());
    }

    // for satellite clinics
    Vector vecAddressName = null;
    Vector vecAddress = null;
    Vector vecAddressPhone = null;
    Vector vecAddressFax = null;
    Vector vecAddressBillingNo = null;
    String defaultAddrName = null;
    if (bMultisites) {
     	vecAddressName = new Vector();
        vecAddress = new Vector();
        vecAddressPhone = new Vector();
        vecAddressFax = new Vector();
        vecAddressBillingNo = new Vector();

    		SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
      		List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));
 			Site defaultSite = sites.get(0);
      		for (Site s:sites) {
                vecAddressName.add(s.getName());
                vecAddress.add(s.getAddress() + ", " + s.getCity() + ", " + s.getProvince() + "  " + s.getPostal());
                vecAddressPhone.add(s.getPhone());
                vecAddressFax.add(s.getFax());
                if (selectedSite.equals(s.getName())) {
                	defaultSite = s;
            	}
     		}
            // default address
	        if (defaultSite!=null) {
	            clinic.setClinic_address(defaultSite.getAddress());
	            clinic.setClinic_city(defaultSite.getCity());
	            clinic.setClinic_province(defaultSite.getProvince());
	            clinic.setClinic_postal(defaultSite.getPostal());
	            clinic.setClinic_phone(defaultSite.getPhone());
	            clinic.setClinic_fax(defaultSite.getFax());
	            clinic.setClinic_name(defaultSite.getName());
	   			defaultAddrName=defaultSite.getName();
	        }
    } else {
	    if(props.getProperty("clinicSatelliteName") != null) {
	        vecAddressName = new Vector();
	        vecAddress = new Vector();
	        vecAddressPhone = new Vector();
	        vecAddressFax = new Vector();
	        vecAddressBillingNo = new Vector();
	        String[] temp0 = props.getProperty("clinicSatelliteName", "").split("\\|");
	        String[] temp1 = props.getProperty("clinicSatelliteAddress", "").split("\\|");
	        String[] temp2 = props.getProperty("clinicSatelliteCity", "").split("\\|");
	        String[] temp3 = props.getProperty("clinicSatelliteProvince", "").split("\\|");
	        String[] temp4 = props.getProperty("clinicSatellitePostal", "").split("\\|");
	        String[] temp5 = props.getProperty("clinicSatellitePhone", "").split("\\|");
	        String[] temp6 = props.getProperty("clinicSatelliteFax", "").split("\\|");
	        String[] temp7 = props.getProperty("clinicDocBillingNoList", "").split("\\|");
	        for(int i=0; i<temp0.length; i++) {
	            vecAddressName.add(temp0[i]);
	            vecAddress.add(temp1[i] + ", " + temp2[i] + ", " + temp3[i] + "  " + temp4[i]);
	            vecAddressPhone.add(temp5[i]);
	            vecAddressFax.add(temp6[i]);
	        }
	        for(int i=0; i<temp7.length; i++) {
	            vecAddressBillingNo.add(temp7[i]);
	        }
	        // default address
	        //clinic.setClinic_name();
	        clinic.setClinic_address(temp1[0]);
	        clinic.setClinic_city(temp2[0]);
	        clinic.setClinic_province(temp3[0]);
	        clinic.setClinic_postal(temp4[0]);
	        clinic.setClinic_phone(temp5[0]);
	        clinic.setClinic_fax(temp6[0]);
	    } else {
	    	//is letterhead different?
	    	if(!reqFrm.letterheadName.equals(clinic.getClinicName())) {
	    		Provider p = providerDao.getProvider(reqFrm.letterheadName);
	    		if(p != null) {
		    		//why, yes it is
		    		vecAddressName = new Vector();
			        vecAddress = new Vector();
			        vecAddressPhone = new Vector();
			        vecAddressFax = new Vector();
			        vecAddressBillingNo = new Vector();
			        
			        vecAddressName.add(p.getFormattedName());
			        vecAddress.add(reqFrm.letterheadAddress);
			        vecAddressPhone.add(reqFrm.letterheadPhone);
			        vecAddressFax.add(reqFrm.letterheadFax);
	    		}
		        
	    	}
	    }

    }
    ConsultationRequestExtDao consultationRequestExtDao = (ConsultationRequestExtDao)SpringUtils.getBean("consultationRequestExtDao");
    List<ConsultationRequestExt> exts =consultationRequestExtDao.getConsultationRequestExts(Integer.parseInt((String)request.getAttribute("reqId")));
    
%>
    <head>
    <html:base/>
    <style type="text/css" media="print">
        .header {
        display:none;
        }

        .header INPUT {
        display:none;
        }

        .header A {
        display:none;
        }
    </style>

    <style type="text/css">
        .Header{
        background-color:#BBBBBB;
        padding-top:5px;
        padding-bottom:5px;
        width: 450pt;
        font-size:12pt;
        }

        .Header INPUT{
        width: 100px;
        }

        .Header A{
        font-size: 12pt;
        }

        table.patientInfo{
        border: 1pt solid #888888;
        }

        table.leftPatient{
        border-left: 1pt solid #AAAAAA;
        }

        table.printTable{
        width: 450pt;
        border: 1pt solid #888888;
        font-size: 10pt;
        font-family: arial, verdana, tahoma, helvetica, sans serif;
        }

        td.subTitles{
        font-size:12pt;
        font-family: arial, verdana, tahoma, helvetica, sans serif;
        }

        td.fillLine{
        border-bottom: 1pt solid #444444;
        font-size:10pt;
        font-family: arial, verdana, tahoma, helvetica, sans serif;
        }

        pre.text{
        font-size:10pt;
        font-family: arial, verdana, tahoma, helvetica, sans serif;
        }

        td.title4{
        font-size:12pt;
        font-family: arial, verdana, tahoma, helvetica, sans serif;
        }

        td.address{
        font-size:10pt;
        font-family: arial, verdana, tahoma, helvetica, sans serif;
        }

    </style>

    <script type="text/javascript">

    var flag = 1;
    function PrintWindow(){
    window.print();
    }

    function CloseWindow(){
    window.close();
    }

    function flipFaxFooter(){
        if (flag == 1 ){
            document.getElementById("faxFooter").innerHTML="<hr><bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgFaxFooterMessage"/>";
            flag = 0;
        }else{
            document.getElementById("faxFooter").innerHTML="";
            flag = 1;
        }
    }

    function phoneNumSelect() {
        document.getElementById("clinicPhone").innerHTML="Tel: "+document.getElementById("sendersPhone").value;
    }

    function faxNumSelect() {
        document.getElementById("clinicFax").innerHTML="Fax: "+document.getElementById("sendersFax").value;
    }

    function addressSelect() {
    	<% if(vecAddressName != null) {
    	    for(int i=0; i<vecAddressName.size(); i++) {%>
    	if(document.getElementById("addressSel").value=="<%=i%>") {
        	document.getElementById("clinicName").innerHTML="<%=vecAddressName.get(i)%>";
        	document.getElementById("clinicAddress").innerHTML="<%=vecAddress.get(i)%>";
        	document.getElementById("clinicPhone").innerHTML="Tel: "+"<%=vecAddressPhone.get(i)%>";
        	document.getElementById("clinicFax").innerHTML="Fax: "+"<%=vecAddressFax.get(i)%>";
        }
		<% } }%>
    }

    </script>
    <title>
    <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.title"/>
    </title>
    </head>
    <body onLoad="addressSelect();">
        <form  method="get "action="attachmentReport.jsp">
            <input type="hidden" name="reqId" value="<%=request.getAttribute("reqId")%>"/>
            <input type="hidden" name="demographicNo" value="<%=request.getParameter("demographicNo")%>"/>
            <input type="hidden" name="providerNo" value="<%=reqFrm.providerNo%>"/>
        <table class="header" >
            <tr>
            <td align="center">
                <oscarProp:oscarPropertiesCheck property="EMAIL_REFERRAL" value="yes">
                <a href="mailto:<%=reqFrm.getSpecailistsEmail(reqFrm.specialist)%>?subject=Consultation%20Request&body=<%=formatEmail(reqFrm)%>">mail</a>
                </oscarProp:oscarPropertiesCheck>
                <input type=button value="<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgFaxFooter"/>" onclick="javascript :flipFaxFooter();"/>
            </td>
            <td align="center">
                <input type=button value="<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPrint"/>" onclick="javascript: PrintWindow();"/>
            </td>
            <td align="center">
                <input type="submit" style="width:130px" value="<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPrintAttached"/>" />
            </td>
            <td align="center">
                <input type=button value="<bean:message key="global.btnClose"/>" onclick="javascript: CloseWindow();"/>
            </td>
		<% if(vecPhones.size() > 0) { %>
            <td align="center">
                P
                <select name="sendersPhone" id="sendersPhone" onChange="phoneNumSelect()">
            <%  for (int i =0; i < vecPhones.size();i++){
                 String te = (String) vecPhones.elementAt(i);
            %>
                   <option value="<%=te%>"><%=te%></option>
            <%  }%>
                </select>
            </td>
		<% } %>
		<% if(vecFaxes.size() > 0) { %>
            <td align="center">
                F
                <select name="sendersFax" id="sendersFax" onChange="faxNumSelect()">
            <%  for (int i =0; i < vecFaxes.size();i++){
                 String te = (String) vecFaxes.elementAt(i);
            %>
                    <option value="<%=te%>"><%=te%></option>
            <%  }%>
                </select>
            </td>
		<% } %>
		<% if(vecAddress != null) { %>
			<% if(vecAddress.size()>1) { %>
            <td align="center">
                Address
                <select name="addressSel" id="addressSel" onChange="addressSelect()" <%=(bMultisites && selectedSite != null ? " disabled " : " ") %>>>
            <%  for (int i =0; i < vecAddressName.size();i++){
                 String te = (String) vecAddressName.get(i);
            %>
                    <option value="<%=i%>" <%=te.equals(defaultAddrName)?"selected":"" %>><%=te%></option>
            <%  }%>
                </select>
            </td>
            <% } else { %>
            	<input type="hidden" name="addressSel" id="addressSel" value="0"/>
		<% } }%>
            </tr>
        </table>
        </form>
        <table class="printTable" name="headerTable">
            <!--header-->
            <tr>
                <td>
                    <table name="innerTable" border="0" <%=vecAddressBillingNo != null? "width='100%'": ""%>>
                        <tr>
                            <td rowspan=3>
                                &nbsp;&nbsp;  <%-- blank column for spacing --%>
                            </td>
                            <td rowspan=3>
                    <%=props.getProperty("faxLogo", "").equals("")?"":"<img src=\""+props.getProperty("faxLogo", "")+"\">"%>
                            </td>
                            <td rowspan=3>
                                &nbsp;&nbsp;  <%-- blank column for spacing --%>
                            </td>
                            <td colspan="2" class="title4" id="clinicName">
                            	<c:if test="${empty infirmaryView_programAddress}">                            	
	                                <b><%=clinic.getClinicName()%></b>
                                </c:if>
                            </td>	
<% if(vecAddressBillingNo != null) {%>
                            <td rowspan=3 align="right">
		                    <table name="innerTable1" border="0" cellspacing="0">
                            <% for(int i=0; i<vecAddressBillingNo.size(); i=i+3) { %>
		                        <tr>
                                <td class="address"><%=i<vecAddressBillingNo.size()? ("<input type='checkbox' name='c'/>" + vecAddressBillingNo.get(i)) : ""%></td>
                                <td class="address"><%=(i+1)<vecAddressBillingNo.size()? ("<input type='checkbox' name='c'/>")+ vecAddressBillingNo.get(i+1): ""%></td>
                                <td class="address"><%=(i+2)<vecAddressBillingNo.size()? ("<input type='checkbox' name='c'/>")+ vecAddressBillingNo.get(i+2): ""%></td>
                                </tr>
							<% } %>
							</table>
                            </td>
<% } %>
                        </tr>
                        <c:choose>
                        <c:when test="${empty infirmaryView_programAddress}">
                        <tr>
                            <td colspan="2" class="address" id="clinicAddress">
                <%=clinic.getClinicAddress()%>, <%=clinic.getClinicCity()%>, <%=clinic.getClinicProvince()%>  <%=clinic.getClinicPostal()%>
                            </td>
                        </tr>
                        <tr>
                            <td class="address" id="clinicPhone">
                                Tel: <%=vecPhones.size()>=1?vecPhones.elementAt(0):clinic.getClinicPhone()%>
                            </td>
                            <td class="address" id="clinicFax">
                                Fax: <%=vecFaxes.size()>=1?vecFaxes.elementAt(0):clinic.getClinicFax()%>
                            </td>
                        </tr>
                        </c:when>
                        <c:otherwise>
                        <tr>
                            <td colspan="2" class="address" id="clinicAddress">
               					<c:out value="${infirmaryView_programAddress}" escapeXml="false"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="address" id="clinicPhone">
                                Tel: <c:out value="${infirmaryView_programTel}"/>
                            </td>
                            <td class="address" id="clinicFax">
                                Fax: <c:out value="${infirmaryView_programFax}"/>
                            </td>
                        </tr>
                        </c:otherwise>
                        </c:choose>
                    </table>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgConsReq"/>
               	</td>
            </tr>
            <tr>
            	<td align="center"> 
            		<strong>        	
            		<%= reqFrm.getAppointmentInstructionsLabel() %>
            		</strong>
            	</td>
            </tr>
            <tr>
                <td>
                    <table border=0 align="center" width="100%" cellspacing="0" class="patientInfo">
                        <tr>
                            <td valign="top" align="left">
                                <table border=0  >
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgDate"/>:
                                        </td>
                                        <td class="fillLine">
			<%
                                	if(reqFrm.pwb.equals("1")){
                                %>
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.pwb"/>
                                <%}else{
                                %>
                                <%=reqFrm.referalDate%>
                                <%}%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgStatus"/>:
                                        </td>
                                        <td class="fillLine">
			    <% if (reqFrm.urgency.equals("1")) { %>
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgUrgent"/>
			    <%  }else if(reqFrm.urgency.equals("2")){ %>
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgNUrgent"/>
			    <%    }else if (reqFrm.urgency.equals("3")){ %>
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgReturn"/>
			    <% } %>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgService"/>:
                                        </td>
                                        <td class="fillLine">
                                <%=reqFrm.getServiceName(reqFrm.service) %>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgConsultant"/>:
                                        </td>
                                        <td class="fillLine">
                                <%=reqFrm.getSpecailistsName(reqFrm.specialist) %>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPhone"/>:
                                        </td>
                                        <td class="fillLine">
                                <%=reqFrm.specPhone%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgFax"/>:
                                        </td>
                                        <td class="fillLine">
                                <%=reqFrm.specFax%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles" >
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAddr"/>:
                                        </td>
                                        <td class="fillLine" >
                                <%=reqFrm.specAddr%>
                                        </td>
                                    </tr>
<plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
<special:SpecialEncounterTag moduleName="eyeform">
<%
String requestId=(String)request.getAttribute("reqId");
String aburl="/EyeForm.do?method=showCC";
if (requestId!=null) aburl+="&requestId="+requestId; %>
<plugin:include componentName="specialencounterComp" absoluteUrl="<%=aburl %>"></plugin:include>
</special:SpecialEncounterTag>
</plugin:hideWhenCompExists>
<%
for(ConsultationRequestExt ext:exts) {
	if(ext.getKey().equals("cc")) {
%>
<tr>
	<td class="subTitles" >cc:</td>
	<td class="fillLine" ><%=ext.getValue()%></td>
</tr>
<%
	}
}
%>
                                </table>
                            </td>
                            <td valign="top">
                                <table border=0 class="leftPatient">
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPat"/>:
                                        </td>
                                        <td class="fillLine">
                            <%=reqFrm.patientName %>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAddr"/>:
                                        </td>
                                        <td class="fillLine">
                                <%=reqFrm.patientAddress %>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPhone"/>
                                        </td>
                                        <td class="fillLine">
                                <%=reqFrm.patientPhone %>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgWPhone"/>
                                        </td>
                                        <td class="fillLine">
                                <%=reqFrm.patientWPhone %>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgBirth"/>:
                                        </td>
                                        <td class="fillLine">
                                <%=reqFrm.patientDOB %>  (y/m/d)
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSex"/>:
                                        </td>
                                        <td class="fillLine">
                                <%=reqFrm.patientSex %>
                                        </td>
                                    </tr>                                         
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgCard"/>
                                        </td>
                                        <td class="fillLine">
                             (<%=reqFrm.patientHealthCardType%>)&nbsp;<%=reqFrm.patientHealthNum %>&nbsp;<%=reqFrm.patientHealthCardVersionCode%>&nbsp;
                                        </td>
                                    </tr>                                                                           
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgappDate"/>:
                                        </td>
                                        <td class="fillLine">
                            <%if (Integer.parseInt(reqFrm.status) > 2 ){%>
                             <%=reqFrm.appointmentDate %>  (y/m/d)
                            <%}else{%>
                                            &nbsp;
                			    <%}%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgTime"/>:
                                        </td>
                                        <td class="fillLine">
                            <%if (Integer.parseInt(reqFrm.status) > 2 ){%>
                                <%=reqFrm.appointmentHour %>:<%=reqFrm.appointmentMinute %> <%=reqFrm.appointmentPm %>
                            <%}else{%>
                                &nbsp;
		                      <%}%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitles">
                                            <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgChart"/>
                                        </td>
                                        <td class="fillLine">
                                <%=reqFrm.patientChartNo == null || "null".equalsIgnoreCase(reqFrm.patientChartNo) ? "" : reqFrm.patientChartNo%>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td class="subTitles">
                    <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgReason"/>:
                </td>
            </tr>
            <tr>
                <td class="fillLine">
                    <%=wrap(reqFrm.reasonForConsultation,80) %>
                    &nbsp;<br>
                </td>
            </tr>
            <% if(getlen(reqFrm.clinicalInformation) > 0) {%>
            <tr>
                <td class="subTitles">
                    <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgClinicalInfom"/>:
                </td>
            </tr>
            <tr>
                <td class="fillLine">
                    	<%=divy(wrap(reqFrm.clinicalInformation,80)).replaceAll("\\s", "&nbsp;")%>
                </td>
            </tr>
            <%}%>
            <% if(getlen(reqFrm.concurrentProblems) > 0) {%>
            <tr>
                <td class="subTitles">
	            <% if(props.getProperty("significantConcurrentProblemsTitle", "").length() > 1) {
	                out.print(props.getProperty("significantConcurrentProblemsTitle", ""));
	             } else { %>
                    <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgSigProb"/>:
				<% } %>
                </td>
            </tr>
            <tr>
                <td class="fillLine">
                    <%=divy(wrap(reqFrm.concurrentProblems,80)) %>
                    &nbsp;<br>
                </td>
            </tr>
            <%}%>
            <plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
<special:SpecialEncounterTag moduleName="eyeform">
<script type="text/javascript" src="js/prototype.js">
</script>
<%
String requestId=(String)request.getAttribute("reqId");
String aburl="/EyeForm.do?method=showSpecial";
if (requestId!=null) aburl+="&requestId="+requestId; %>
<plugin:include componentName="specialencounterComp" absoluteUrl="<%=aburl %>"></plugin:include>
</special:SpecialEncounterTag>
</plugin:hideWhenCompExists>
<%
for(ConsultationRequestExt ext:exts) {
	if(ext.getKey().equals("specialProblem")) {
		if(ext.getValue().length() > 0) {
%>
            <tr>
                <td class="subTitles">
		            Ocular Examination
                </td>
            </tr>
            <tr>
                <td class="fillLine">
                    <%=divy(wrap(ext.getValue(),80)) %>
                    &nbsp;<br>
                </td>
            </tr>

<%
		}
	}
}
%>




            <% if(getlen(reqFrm.currentMedications) > 0) {%>
            <tr>
                <td class="subTitles">
		            <% if(props.getProperty("currentMedicationsTitle", "").length() > 1) {
		                out.print(props.getProperty("currentMedicationsTitle", ""));
		             } else { %>
                    <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgCurrMed"/>
					<% } %>
                </td>
            </tr>
            <tr>
                <td class="fillLine">
                    <%=divy(wrap(reqFrm.currentMedications,80)) %>
                    &nbsp;<br>
                </td>
            </tr>
            <%}%>
            <% if(getlen(reqFrm.allergies) > 0) {%>
            <tr>
                <td class="subTitles">
                    <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAllergies"/>
                </td>
            </tr>
            <tr>
                <td class="fillLine">
                    <%=divy(wrap(reqFrm.allergies,80)) %>
                    &nbsp;<br>
                </td>
            </tr>
            <%}%>
            
	    <tr>		
		<td class="subTitles">		
		<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAssociated"/> : <%=reqFrm.getFamilyDoctor() %>		
		&nbsp;<br>		
		</td>		
	    </tr>           

            <tr>
            <td class="subTitles">
                <%-- A more permanent, but I will not say elegant, implemenation of this "Physician indicator by a new property and other language support. --%>
	   <% if (props.getProperty("CONSULT_PHYSICIAN_IS_REFERRING", "").startsWith("true") ) 
          { 
          	%><bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgReferringIsPhysician"/>
       <% } else if(props.getProperty("isSpecialist", "").startsWith("true")) { %>
                Doctor
	   <% } else { %>
                <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgFamilyDoc"/>
       <% } %>
                : <%=reqFrm.getProviderName(reqFrm.providerNo) %>
                        &nbsp;<br>
                    </td>
                </tr>

                <tr>
                    <td id="faxFooter">

                    </td>
                </tr>
                <tr>
                <td align="center">
                    <% if (props.getProperty("FORMS_PROMOTEXT") != null){
                        %></br><%= props.getProperty("FORMS_PROMOTEXT") %>
                    <%}%>
                </td>
            </tr>
        </table>
    </body>
</html:html>
<%!
public String divy (String str){
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(str);
    int j =0;
    int i = 0 ;
    while (i < stringBuffer.length() ){
        if (stringBuffer.charAt(i) == '\n'){
        stringBuffer.insert(i,"<BR>");
        i = i + 4;
        }
    i++;
    }
return stringBuffer.toString();
}

public String wrap(String in,int len) {
	if(in==null)
		in="";
	//in=in.trim();
	if(in.length()<len) {
		if(in.length()>1 && !in.startsWith("  ")) {
			in=in.trim();
		}
		return in;
	}
	if(in.substring(0, len).contains("\n")) {
		String x = in.substring(0, in.indexOf("\n"));
		if(x.length()>1 && !x.startsWith("  ")) {
			x=x.trim();
		}
		return x + "\n" + wrap(in.substring(in.indexOf("\n") + 1), len);
	}
	int place=Math.max(Math.max(in.lastIndexOf(" ",len),in.lastIndexOf("\t",len)),in.lastIndexOf("-",len));
	if( place <= 0 ) {
		place = len;
	}
	return in.substring(0,place).trim()+"\n"+wrap(in.substring(place),len);
}

public int getlen (String str){
	if (str == null)
            return 0;
	return str.length();
}

public String formatEmail(oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil reqFrm){
   String Urgency = "";
   if (reqFrm.urgency.equals("1")) {
      Urgency = "Urgent";
   }else if(reqFrm.urgency.equals("2")){
      Urgency = "Non-Urgent";
   }else if (reqFrm.urgency.equals("3")){
      Urgency = "Return";
   }

   String s = "";
   s +="<b>Date:</b>"+reqFrm.referalDate+"<br/>";
   s +="<b>Status:</b>"+Urgency+"<br/>";
   s +="<b>Service:</b>"+reqFrm.getServiceName(reqFrm.service)+"<br/>";

   s +="<b>Specailist:</b>"+reqFrm.getSpecailistsName(reqFrm.specialist) +" <b>Phone:</b>"+reqFrm.specPhone+" <b>Fax:</b> "+reqFrm.specFax+"<br/>";
   s +="<b>Address:</b> "+reqFrm.specAddr+"<br/><br/><br/>";

   s +="<b>Patient Info</b><br/>";
   s +="<b>Name:</b>"+reqFrm.patientName+"<br/>";
   s +="<b>DOB:</b>"+reqFrm.patientDOB+"<br/>";
   s +="<b>HIN:</b>"+reqFrm.patientHealthNum+" "+reqFrm.patientHealthCardVersionCode+" <b>HIN Type:</b>"+reqFrm.patientHealthCardType+"<br/>";
   s +="<b>Phone:</b>"+reqFrm.patientPhone+"<br/>";
   s +="<b>Phone:</b>"+reqFrm.patientWPhone+"<br/>";
   s +="<b>Address:</b>"+reqFrm.patientAddress+"<br/><br/><br/>";


   s += "<b>Reason For Consultation</b> <br/> ";
   s += divy(reqFrm.reasonForConsultation);
   s += "<br/><br/>";
   s += "<b>Pertinent Clinical Information</b><br/> ";
   s += divy(reqFrm.clinicalInformation);
   s += "<br/><br/>";
   s += "<b>Significant Concurrent Problems</b><br/> ";
   s += divy(reqFrm.concurrentProblems);
   s += "<br/><br/>";
   s += "<b>Current Medications</b><br/> ";
   s += divy(reqFrm.currentMedications);
   s += "<br/><br/>";
   s += "<b>Allergies</b><br/> ";
   s += divy(reqFrm.allergies);
   s += "<br/><br/>";
   s += "<b>Associated with:</b> "+reqFrm.getProviderName(reqFrm.providerNo)+"<br/> ";
   s += "<b>Family Doctor:</b> "+reqFrm.getFamilyDoctor() ;
   s = s.replaceAll("\"","&quot;");
   return s;
}
%>
