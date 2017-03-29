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

<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="oscar.oscarRx.data.RxProviderData"%>
<%@page import="oscar.oscarRx.data.RxProviderData.Provider"%>
<%@page import="oscar.oscarClinic.ClinicData"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.eyeform.web.ConsultationReportFormBean"%>

			<td colspan="1">Site : </td>
				<!-- adding filter for sites -->
				<%! 
				boolean bMultisites=org.oscarehr.common.IsPropertiesOn.isMultisitesEnable(); 
				ClinicData clinic = new ClinicData();
				
				RxProviderData rx = new RxProviderData();
				List<Provider> prList = rx.getAllProviders();
				
				EctConsultationFormRequestUtil consultUtil = new EctConsultationFormRequestUtil();
				
				ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
				List<Program> programList = programDao.getAllActivePrograms();
				
				%>
				<jsp:useBean id="consultationReportForm" type="org.apache.struts.validator.DynaValidatorForm" scope="request"></jsp:useBean>
	<%
	ConsultationReportFormBean formBean_ = null;
	if(consultationReportForm!=null && consultationReportForm.get("cr")!=null)
	{
		formBean_ = (ConsultationReportFormBean) consultationReportForm.get("cr");
	}
		%>
		
	<script>
	function loadAllProviders()
	{
		var providerSelect = document.getElementsByName("cr.providerNo")[0];
		
		<%if(formBean_!=null && formBean_.getProviderList()!=null){
		List<org.oscarehr.common.model.Provider> provList = formBean_.getProviderList();
		%>
		providerSelect.options.length=<%=provList.size()%>+1;
		providerSelect.options[0]=new Option("All","",true,false);
			<%for(int i=0;i<provList.size();i++){
				org.oscarehr.common.model.Provider provider_ = provList.get(i);
			%>
				var isSelected = false;
				providerSelect.options[<%=i%>+1]=new Option('<%=provider_.getFormattedName()%>','<%=provider_.getProviderNo()%>',isSelected,false);
			<%}%>
		<%}%>				
	}
	</script>
		
	<%
	
	//multi-site support
	//String selectedProviderNo = request.getParameter("letterheadName");
	String selectedProviderNo = "";
	if(formBean_!=null)
		selectedProviderNo = formBean_.getProviderNo();
	
	String appNo = request.getParameter("appNo");
	appNo = (appNo==null ? "" : appNo);

	String defaultSiteName = "";
	int defaultSiteIdx = 0;
	Integer defaultSiteId = null;
	List<Integer> siteIds = new ArrayList<Integer>();
	List<String> siteNames = new ArrayList<String>();
	List<String> siteColors = new ArrayList<String>();
	String siteNameJS = null;
	String siteAddressJS = null;
	String sitePhoneJS = null;
	String siteFaxJS = null;
	String siteProviderNoJS = null;
	String siteProviderNameJS = null;
	List<Site> sites = null;
	List<org.oscarehr.common.model.Provider> siteProviders = null;
	if (bMultisites) {
		SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
		ProviderDao providerDao = (ProviderDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("providerDao");
	    if (appNo != "" && defaultSiteName.equals("")) {
		    defaultSiteName = siteDao.getSiteNameByAppointmentNo(appNo);
	    }
		sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));
		if (sites != null) {
			siteNameJS = "var siteNameList = new Array();";
			siteAddressJS = "var siteAddressList = new Array();";
			sitePhoneJS = "var sitePhoneList = new Array();";
			siteFaxJS = "var siteFaxList = new Array();";
		        siteProviderNoJS = "var siteProviderNoList = new Array();";
		        siteProviderNameJS = "var siteProviderNameList = new Array();";
			int i = 0;
			for (Site s : sites) {
				   siteIds.add(s.getId());
		           siteNames.add(s.getName());
		           siteColors.add(s.getBgColor());
		           siteNameJS += "siteNameList["+i+"]='"+s.getName().replaceAll("'", "\\\\'")+"';";
		           siteAddressJS += "siteAddressList["+i+"]='"+s.getAddress().replaceAll("'", "\\\\'")+", "+s.getCity().replaceAll("'", "\\\\'")+", "+s.getProvince().replaceAll("'", "\\\\'")+"';";
		           sitePhoneJS += "sitePhoneList["+i+"]='"+s.getPhone().replaceAll("'", "\\\\'")+"';";
		           siteFaxJS += "siteFaxList["+i+"]='"+s.getFax().replaceAll("'", "\\\\'")+"';";
		           if(s.getName().equals(defaultSiteName) || sites.size()==1) {
		               defaultSiteIdx = i;
		               defaultSiteId = s.getId();
		           }
	                   siteProviders = providerDao.getProvidersBySiteLocation(s.getName());
		           int j = 0;
		           siteProviderNoJS += "var sno"+i+"=new Array();";
		           siteProviderNameJS += "var sname"+i+"=new Array();";
		           for(org.oscarehr.common.model.Provider siteProvider : siteProviders) {
		               siteProviderNoJS += "sno"+i+"["+j+"]='"+siteProvider.getProviderNo()+"';";
		               //siteProviderNameJS += "sname"+i+"["+j+"]='"+siteProvider.getFirstName().replaceAll("'", "\\\\'")+" "+siteProvider.getLastName().replaceAll("'", "\\\\'")+"';";
		               siteProviderNameJS += "sname"+i+"["+j+"]='"+siteProvider.getFormattedName().replaceAll("'", "\\\\'")+"';";
			       j++;
		           }
		           siteProviderNoJS += "siteProviderNoList["+i+"]=sno"+i+";";
		           siteProviderNameJS += "siteProviderNameList["+i+"]=sname"+i+";";
		           i++;
		 	}
                }
	}
	
	%>
	<%--
	<jsp:useBean id="consultationReportForm" type="org.apache.struts.validator.DynaValidatorForm" scope="request"></jsp:useBean>
	<%
	if(consultationReportForm!=null && consultationReportForm.get("cr")!=null)
	{
		ConsultationReportFormBean formBean_ = (ConsultationReportFormBean) consultationReportForm.get("cr");
		if(formBean_.getProviderList()!=null)
		{
			List<Provider> allProviderList = formBean_.getProviderList();
			for(int i=0;i<allProviderList.size();i++)
			{
				
			}
		}
	}
	%> --%>
			
	<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<script>
	var ctx = '<%=request.getContextPath()%>';
	var requestId = '<%=request.getParameter("requestId")%>';
	<%-- var demographicNo = '<%=demo%>';
	var demoNo = '<%=demo%>'; --%>
	var appointmentNo = '<%=appNo%>';
	<%=(siteNameJS!=null ? siteNameJS : "")%>;
	<%=(siteAddressJS!=null ? siteAddressJS : "")%>;
	<%=(sitePhoneJS!=null ? sitePhoneJS : "")%>;
	<%=(siteFaxJS!=null ? siteFaxJS : "")%>;
	<%=(siteProviderNoJS!=null ? siteProviderNoJS : "")%>;
	<%=(siteProviderNameJS!=null ? siteProviderNameJS : "")%>;
	var user = <%= request.getSession().getAttribute("user").toString() %>;
	
	 function setLetterhead() {
	        var isMultisites = <%= bMultisites %>;  	
	        var isDefaultSite = false;
	        if(isMultisites) {
	            /*for(n=0;n<siteNameList.length;n++) {
	                if(siteNameList[n] == '<%= defaultSiteName %>' || siteNameList.length==1) {
	                    switchSite(n);
	                    isDefaultSite = true;
	                    break;
	                }
	            }*/
	            var objSiteId = document.getElementById("siteId");
	            if(objSiteId.selectedValue!="-1")
            	{
	            	if(objSiteId.selectedIndex!=0)
	            		switchSite(objSiteId.selectedIndex, '<%=selectedProviderNo%>');
            	}
	            else
            	{
	            	//if(!isDefaultSite) switchSite(0);
            	}
	            
	        }
	    }
	 
	 function switchProvider(value) {
		    if(!<%= bMultisites %>) {
			if (value==-1) {
				document.getElementById("letterheadName").value = value;
				document.getElementById("letterheadAddress").value = "<%=(clinic.getClinicAddress() + "  " + clinic.getClinicCity() + "   " + clinic.getClinicProvince() + "  " + clinic.getClinicPostal()).trim() %>";
				document.getElementById("letterheadAddressSpan").innerHTML = "<%=(clinic.getClinicAddress() + "  " + clinic.getClinicCity() + "   " + clinic.getClinicProvince() + "  " + clinic.getClinicPostal()).trim() %>";
				document.getElementById("letterheadPhone").value = "<%=clinic.getClinicPhone().trim() %>";
				document.getElementById("letterheadPhoneSpan").innerHTML = "<%=clinic.getClinicPhone().trim() %>";
				document.getElementById("letterheadFax").value = "<%=clinic.getClinicFax().trim() %>";
				document.getElementById("letterheadFaxSpan").innerHTML = "<%=clinic.getClinicFax().trim() %>";
			} else {
				if (typeof providerData["prov_" + value] != "undefined")
					value = "prov_" + value;

				document.getElementById("letterheadName").value = value;
				document.getElementById("letterheadAddress").value = providerData[value]['address'];
				document.getElementById("letterheadAddressSpan").innerHTML = providerData[value]['address'].replace(" ", "&nbsp;");
				document.getElementById("letterheadPhone").value = providerData[value]['phone'];
				document.getElementById("letterheadPhoneSpan").innerHTML = providerData[value]['phone'];
				document.getElementById("letterheadFax").value = providerData[value]['fax'];
				document.getElementById("letterheadFaxSpan").innerHTML = providerData[value]['fax'];
			}
		    }
		}
	 
	 var allProviderOptions = null;
	 function switchSite(value, selectedProviderNo) {
		 if(!allProviderOptions || allProviderOptions==null)
		 {
			 allProviderOptions = document.getElementsByName("cr.providerNo")[0].innerHTML;
		 }
		 
		 if(value==0)
		 {
			 /*var providerSelect = document.getElementsByName("cr.providerNo")[0];
			 providerSelect.innerHTML = allProviderOptions;
			 
			 providerSelect.options[0].selected = true;*/
			 loadAllProviders();
		 }
		 else
		 {
			 value = value-1;
			 
			 if(selectedProviderNo==null || !selectedProviderNo || selectedProviderNo=="null")
				 selectedProviderNo = document.getElementsByName("cr.providerNo")[0].value;
			 var providerSelect = document.getElementsByName("cr.providerNo")[0];
				providerSelect.options.length=siteProviderNoList[value].length+1;
				providerSelect.options[0]=new Option("All","",true,false);
				for(i=0;i<siteProviderNoList[value].length;i++) {
				    var isSelected = false;		   
				    providerSelect.options[i+1]=new Option(siteProviderNameList[value][i],siteProviderNoList[value][i],isSelected,false);		
				    
				    <%-- if('<%= consultUtil.letterheadName %>' == providerSelect.options[i+1].value) providerSelect.options[i+1].selected = true;		    
				    //else --%> 
				    if(providerSelect.options[i+1].value == selectedProviderNo) providerSelect.options[i+1].selected = true;
				} 
		 }
		 
		 
		 	/*if(value==0)
		 		{
		 		var providerSelect = document.getElementById("letterheadName");
				providerSelect.options.length=1;
				providerSelect.options[0]=new Option("-- Select Provider --","-1",false,false);
				
				document.getElementById("letterheadAddress").value = "";
				document.getElementById("letterheadAddressSpan").innerHTML = "";
				document.getElementById("letterheadPhone").value = "";
				document.getElementById("letterheadPhoneSpan").innerHTML = "";
				document.getElementById("letterheadFax").value = "";
				document.getElementById("letterheadFaxSpan").innerHTML = "";
				
		 		return;
		 		}
		 	value = value-1;
		 	
			document.getElementById("letterheadAddress").value = siteAddressList[value];
			document.getElementById("letterheadAddressSpan").innerHTML = siteAddressList[value].replace(" ", "&nbsp;");
			document.getElementById("letterheadPhone").value = sitePhoneList[value];
			document.getElementById("letterheadPhoneSpan").innerHTML = sitePhoneList[value];
			document.getElementById("letterheadFax").value = siteFaxList[value];
			document.getElementById("letterheadFaxSpan").innerHTML = siteFaxList[value];
			
			var providerSelect = document.getElementById("letterheadName");
			providerSelect.options.length=siteProviderNoList[value].length+1;
			providerSelect.options[0]=new Option("-- Select Provider --","-1",true,false);
			for(i=0;i<siteProviderNoList[value].length;i++) {
			    var isSelected = false;		   
			    providerSelect.options[i+1]=new Option(siteProviderNameList[value][i],siteProviderNoList[value][i],isSelected,false);		
			    if('<%= consultUtil.letterheadName %>' == providerSelect.options[i+1].value) providerSelect.options[i+1].selected = true;		    
			    else if(providerSelect.options[i+1].value == selectedProviderNo) providerSelect.options[i+1].selected = true;
			}    */
	}
</script>
	<%
	if (bMultisites) { 

		String cmbBgColor = "";
		if(sites!=null && sites.size()>0)
		{
			cmbBgColor = sites.get(0).getBgColor();
		}
		%>
		<td>
		<select name="siteId" id="siteId" 
		style="background-color: <%=cmbBgColor %>" onchange='this.style.backgroundColor=this.options[this.selectedIndex].style.backgroundColor;switchSite(this.selectedIndex, "-1")'>
		<option value="-1">-- All --</option>
		<%
		String selectedSiteId = request.getParameter("siteId");
		if(selectedSiteId!=null)
			selectedSiteId = selectedSiteId.trim();
		else
			selectedSiteId = "";
		if(sites!=null && sites.size()>0)
		{
			for(int i=0;i<sites.size();i++){
				String siteId = sites.get(i).getSiteId().toString();
				String siteName = sites.get(i).getName();
                String bgColor = sites.get(i).getBgColor();
                
                String selectedStr = "";
                if(siteId!=null && siteId.equalsIgnoreCase(selectedSiteId))
                	selectedStr = "selected='selected'";
			%>
			<option <%=selectedStr %> value="<%=siteId%>" style="background-color: <%=bgColor%>"><%=siteName %></option>
			<%}
		}
		else
		{
			%>
			<option value="-1">No Site Found</option>
			<%
		}
		
		%>
		</select>
		</td>
		<%
	%>
	<%} %>
