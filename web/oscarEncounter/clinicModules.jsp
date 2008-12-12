<%@page contentType="text/html"%>
<%-- <%@page pageEncoding="UTF-8"%> --%>

<%@page import="java.net.URLEncoder"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%
  long startTime = System.currentTimeMillis();
  oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
  if((bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean"))==null) {
      response.sendError(response.SC_FORBIDDEN);    
      //response.sendRedirect("error.jsp");
      return;
  }
  java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);  
  oscar.util.UtilDateUtilities dateConvert = new oscar.util.UtilDateUtilities();
  String pAge = Integer.toString(dateConvert.calcAge(bean.yearOfBirth,bean.monthOfBirth,bean.dateOfBirth));
  String winName = "";
%>

<h3>&nbsp;<bean:message key="oscarEncounter.Index.clinicalModules" /></h3>
<ul id="ModuleList">
	<%-- <li>               
                        <%
                            winName = "Master" + bean.demographicNo;
                            if (vLocale.getCountry().equals("BR")) {                            
                        %>
                            <a class="links" onmouseover="this.className='linkhover'"  onmouseout="this.className='links'" href="javascript: function myFunction() {return false; }" onClick="popupPage(700,1000,'<%=winName%>','../demographic/demographiccontrol.jsp?demographic_no=<%=bean.demographicNo%>&displaymode=edit&dboperation=search_detail_ptbr','master')"
                            title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>"><bean:message key="global.master"/></a>
                        <%}else{%>
                            <a class="links" onmouseover="this.className='linkhover'"  onmouseout="this.className='links'" href="javascript: function myFunction() {return false; }" onClick="popupPage(700,1000,'<%=winName%>','../demographic/demographiccontrol.jsp?demographic_no=<%=bean.demographicNo%>&displaymode=edit&dboperation=search_detail','master')"
                            title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>"><bean:message key="global.master"/></a>
                        <%}%>
                    </li>
                     <li>
                        <%
                           winName = "Consultations" + bean.demographicNo;
                        %>
                        <a class="links" onmouseover="this.className='linkhover'"  onmouseout="this.className='links'" href=# onClick="popupPage(700,960,'<%=winName%>','<rewrite:reWrite jspPage="oscarConsultationRequest/DisplayDemographicConsultationRequests.jsp"/>?de=<%=bean.demographicNo%>');return false;"><bean:message key="global.consultations"/></a>
                    </li>
                    --%>
	<li><oscar:oscarPropertiesCheck property="PREVENTION" value="yes">
		<%
                               winName = "Prevention" + bean.demographicNo;
                            %>
		<a class="links" onmouseover="this.className='linkhover'"
			onmouseout="this.className='links'"
			href="javascript:popupPage(700,960,'<%=winName%>','../oscarPrevention/index.jsp?demographic_no=<%=bean.demographicNo%>')">
		<oscar:preventionWarnings demographicNo="<%=bean.demographicNo%>">prevention</oscar:preventionWarnings></a>
	</oscar:oscarPropertiesCheck></li>
	<%-- <li>
                        <%
                           winName = "Disease" + bean.demographicNo;
                        %>
                        <a class="links" onmouseover="this.className='linkhover'"  onmouseout="this.className='links'" href=# onClick="popupPage(580,900,'<%=winName%>','../oscarResearch/oscarDxResearch/setupDxResearch.do?demographicNo=<%=bean.demographicNo%>&providerNo=<%=bean.providerNo%>&quickList=');return false;"><bean:message key="global.disease"/></a>
                    </li>
                    <li>
                        <%
                           winName = "AddTickler" + bean.demographicNo;
                        %>
                        <a class="links" onmouseover="this.className='linkhover'"  onmouseout="this.className='links'" href=# onClick="popupPage(580,800,'<%=winName%>','../appointment/appointmentcontrol.jsp?keyword=<%=URLEncoder.encode(bean.patientLastName+","+bean.patientFirstName)%>&displaymode=<%=URLEncoder.encode("Search ")%>&search_mode=search_name&originalpage=<%=URLEncoder.encode("../tickler/ticklerAdd.jsp")%>&orderby=last_name&appointment_date=2000-01-01&limit1=0&limit2=5&status=t&start_time=10:45&end_time=10:59&duration=15&dboperation=add_apptrecord&type=&demographic_no=<%=bean.demographicNo%>');return false;"><bean:message key="oscarEncounter.Index.addTickler"/></a>                        
                        <%
                           winName = "ViewTickler" + bean.demographicNo;
                        %>                        
                        <a class="links" onmouseover="this.className='linkhover'"  onmouseout="this.className='links'" href="#" onClick="popupPage(700,1000,'<%=winName%>','../tickler/ticklerDemoMain.jsp?demoview=<%=bean.demographicNo%>');return false;"><bean:message key="global.viewTickler"/></a><br>
                        
                    </li> --%>
	<li>
	<%
                           winName = "Calculator" + bean.demographicNo;
                        %> <a class="links"
		onmouseover="this.className='linkhover'"
		onmouseout="this.className='links'"
		href="javascript: function myFunction() {return false; }"
		onClick="popupPage(150,200,'<%=winName%>','calculators.jsp?sex=<%=bean.patientSex%>&age=<%=pAge%>'); return false;"><bean:message
		key="oscarEncounter.Index.calculators" /></a></li>

</ul>
<input type="hidden" id="modCount" value="7">
<% System.out.println("clinicModules.jsp load time: " + (System.currentTimeMillis()-startTime) + "ms"); %>
