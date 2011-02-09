<%--<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->
<%@ include file="/casemgmt/taglibs.jsp" %>
<%
    oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
    if((bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean"))==null) {
        response.sendRedirect("error.jsp");
        return;
    }
    
    String demoNo = bean.getDemographicNo();
    int maxLen = 12;
    int shorted = 9;
    String ellipses = "...";
    
 %>
<div class="leftBox" id="allergyBox">
    <h3 style="margin-right: 3px; background-color:orange; float:right"><a>+</a></h3>
   <h3 style="background-color:orange;">
        <a href=# onClick="popupPage(700,960,'allergy','../oscarRx/showAllergy.do?demographicNo=<%=demoNo%>');return false;"><bean:message key="global.allergies"/></a>
    </h3>
    <ul>
        <%
          oscar.oscarRx.data.RxPatientData.Patient.Allergy[] allergies
            = new oscar.oscarRx.data.RxPatientData().getPatient(Integer.parseInt(demoNo)).getAllergies();

            String bgColour = "background-color: #f3f3f3";
            for (int j=0; j<allergies.length; j++){%>
                <li style="<%=j%2==0?bgColour:""%>">
                   <a title="<%= allergies[j].getAllergy().getDESCRIPTION() %>" >
                      <%=allergies[j].getAllergy().getShortDesc(13,8,"...")%>
                   </a>
                </li>
        <%}%>
    </ul>
</div>

<div class="leftBox" id="prescriptionBox">
    <h3 style="margin-right: 3px; background-color:#C3C3C3; float:right"><a>+</a></h3>
    <h3 style="background-color:#C3C3C3;">
        <a href=# onClick="popupPage(700,960,'Rx','../oscarRx/choosePatient.do?providerNo=<%=bean.providerNo%>&demographicNo=<%=demoNo%>');return false;"><bean:message key="global.prescriptions"/></a>
    </h3>
     <%
    oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
    oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = {};
    arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(demoNo));
    if (arr.length > 0){%>
        <ul>
            <%
             
             for (int i = 0; i < arr.length; i++){
                String rxD = arr[i].getRxDate().toString();
                //String rxP = arr[i].getRxDisplay();
                String rxP = arr[i].getFullOutLine().replaceAll(";"," ");
                rxP = rxP + "   " + arr[i].getEndDate();
                String styleColor = "";
                String bgColor = i % 2 == 0 ? "background-color:#f3f3f3" : "";
                if(arr[i].isCurrent() == true){  
                    styleColor="color:red;";                    
                }
            %>
                <li style="<%=styleColor%>">
                    <a title="<%=rxP%>"><%=rxD%>&nbsp;&nbsp;
                    <%=oscar.util.StringUtils.maxLenString(rxP, maxLen, shorted, ellipses)%></a>
                </li>
            <%}%>
        </ul>
    <%}else{out.write("&nbsp;");}%>
</div>
--%>
<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>


<script type="text/javascript">            
           
    <c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>    
                  
</script>

<div class="leftBox">
<h3>Right Menu</h3>
</div>
<div id="rightNavBar"></div>