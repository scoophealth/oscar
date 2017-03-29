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

<%@ include file="/taglibs.jsp"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_con");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.oscarehr.eyeform.model.EyeformConsultationReport"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="org.oscarehr.common.dao.SiteDao" %>
<%@page import="org.oscarehr.common.model.Site" %>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.Clinic" %>
<html:html>

    <head>
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
        width: 595pt;
        font-size:16pt;
		font-weight:bold;
        }

        .Header INPUT{
        width: 100px;
		font-weight:bold;
        }

        .Header A{
        font-size: 12pt;
		font-weight:bold;
        }

        table.patientInfo{
        border: 1pt solid #888888;
        }

        table.leftPatient{
        border-left: 1pt solid #AAAAAA;
        }

        table.printTable{
        width: 595pt;
        border: 1pt solid #888888;
        font-size: 12pt;
		
        font-family: courier;
        }

        td.subTitles{
        font-size:12pt;
		font-weight:bold;
        font-family: courier;
        }

        td.fillLine{
        border-bottom: 1pt solid #444444;
        font-size:10pt;
		
        font-family: courier;
        }
        td.subTitlesh{
        font-size:12pt;
		font-weight:bold;
        font-family: courier;
        }

		td {
			font-size:10pt;
			font-family:courier;
		}
		
        td.fillLineh{
        border-bottom: 1pt solid #444444;
        font-size:10pt;
		
        font-family: courier;
        }

        pre.text{
        font-size:12pt;
		font-weight:bold;
        font-family: courier;
        }

        td.title4{
        font-size:14pt;
		font-weight:bold;
        font-family: courier;
        }

        td.address{
        font-size:10pt;
		
        font-family:courier;
        }
		
		td.title{
        font-size:12pt;
		font-weight:bold;
		}

		td.letterContent{
		font-size:10pt;
        font-family: courier;
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
            document.getElementById("faxFooter").innerHTML="<hr>This information is direct in confidence solely to the person named above and may not otherwise be distributed, copied or disclosed. Therefore, this information should be considered strictly confidential.  If you have received this telecopy in error, please notify us immediately by telephone. Thank you for your assistance.";
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

    }
    function changeAddress(){
    	var cid=document.inputForm.elements['satelliteId'].value;
    	document.getElementById("sclinicName").innerHTML=clinicName[cid];
    	document.getElementById("sclinicAddress").innerHTML=clinicAddress[cid];
    	document.getElementById("sclinicCity").innerHTML=clinicCity[cid];
    	document.getElementById("sclinicProvince").innerHTML=clinicProvince[cid];
    	document.getElementById("sclinicPostal").innerHTML=clinicPostal[cid];
    	document.getElementById("sclinicPhone").innerHTML=clinicPhone[cid];
    	document.getElementById("sclinicFax").innerHTML=clinicFax[cid];
    }
	var clinicName=new Array();
	var clinicAddress=new Array();
	var clinicCity=new Array();
	var clinicProvince=new Array();
	var clinicPostal=new Array();
	var clinicPhone=new Array();
	var clinicFax=new Array();

    <c:forEach items="${requestScope.clinicArr}" var="cli" varStatus="status">

    clinicName['<c:out value="${cli.clinicId}"/>']='<c:out value="${cli.clinicName}"/>'
    clinicAddress['<c:out value="${cli.clinicId}"/>']='<c:out value="${cli.clinicAddress}"/>'
    clinicCity['<c:out value="${cli.clinicId}"/>']='<c:out value="${cli.clinicCity}"/>'
    clinicProvince['<c:out value="${cli.clinicId}"/>']='<c:out value="${cli.clinicProvince}"/>'
    clinicPostal['<c:out value="${cli.clinicId}"/>']='<c:out value="${cli.clinicPostal}"/>'
    clinicPhone['<c:out value="${cli.clinicId}"/>']='<c:out value="${cli.clinicPhone}"/>'
    clinicFax['<c:out value="${cli.clinicId}"/>']='<c:out value="${cli.clinicFax}"/>'

    </c:forEach>
    </script>
    <title>
    ConsultationFormPrint
    </title>
    </head>
    <body onload="changeAddress()">

    <%EyeformConsultationReport cp=(EyeformConsultationReport)request.getAttribute("cp");
    OscarProperties props = OscarProperties.getInstance();
    %>

        <html:form action="/eyeform/Eyeform">
            <html:hidden property="cp.id"/>
            <html:hidden property="cp.demographicNo"/>

        <table class="header" >
            <tr>
            <td align="center">

                <input type=button value="Fax Footer" onclick="javascript :flipFaxFooter();"/>
            </td>

            <td align="center">
                <input type=button value="Print" onclick="javascript: PrintWindow();"/>
            </td>
            <!--  td align="center">
                 <input type="submit" value="Print Attached" />
            </td -->
            <td align="center">
                <input type=button value="Close" onclick="javascript: CloseWindow();"/>
            </td>

            <!-- TODO: sateliteFlag -->


            </tr>
        </table>


        <table class="printTable" name="headerTable" style="table-layout:fixed" width="595">
            <!--header-->
            <tr>
                <td>
                    <table name="innerTable" border="0" >
                        <tr>
                            <td rowspan=3>
                                &nbsp;&nbsp;
                            </td>

                            <td rowspan=3>
                    		 <%=props.getProperty("faxLogo", "").equals("")?"":"<img src=\""+props.getProperty("faxLogo", "")+"\">"%>

                            </td>
                            <td rowspan=3>
                                &nbsp;&nbsp;
                            </td>


                            <td colspan="2" class="title4" id="clinicName">

                            <b>
                           		<c:out value="${mdstring}"/>
                         <!--       <c:out value="${internalDrName}"/>
                                <c:out value="${specialty}"/>                     
                           -->   	
                           		<c:out value="${appointmentDoctor}"/>
                                <c:out value="${specialty_apptDoc}"/> 
                            </b>
                                <br>
                                <b><span id="sclinicName"><c:out value="${clinic.clinicName}"/></span></b>
                            </td>

                        </tr>
                        <tr>
                            <td colspan="2" class="address" id="clinicAddress">
                <span id="sclinicAddress"><c:out value="${clinic.clinicAddress}"/></span>,
                <span id="sclinicCity"><c:out value="${clinic.clinicCity}"/></span>,
                <span id="sclinicProvince"><c:out value="${clinic.clinicProvince}"/></span>,
                <span id="sclinicPostal"><c:out value="${clinic.clinicPostal}"/></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="address" id="clinicPhone">
                                Tel: <span id="sclinicPhone"><c:out value="${clinic.clinicPhone}"/></span>
                            </td>

                            <td class="address" id="clinicFax">
                                Fax: <span id="sclinicFax"><c:out value="${clinic.clinicFax}"/></span>
                                <% 
                                boolean isMultiSites = props.getBooleanProperty("multisites", "on");
                                if (!isMultiSites) {
                                	out.print("URL: " + props.getProperty("clinicurl", ""));
                                } else {
                                	Clinic tmpCli = (Clinic)request.getAttribute("clinic");
                                	if (tmpCli != null){
                                		SiteDao siteDao = (SiteDao)SpringUtils.getBean(SiteDao.class);
                                        Site site = siteDao.findByName(tmpCli.getClinicName());
                                        out.print("URL: " + site.getSiteUrl()==null?"":site.getSiteUrl());
                                	} else {
                                		out.print("URL: " + props.getProperty("clinicurl", ""));
                                	}
                                }
                                %>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr><td>&nbsp;
            	</td>
            </tr>
            <tr>
                <td align="center" class="title">					
                    <b>Consultation Report</b>
                    <br>

                </td>
            </tr>
            <tr>

                <td >
                    <table border=0 align="center" width="100%" cellspacing="0" class="patientInfo">
                        <tr>
                            <td valign="top" align="left" width="50%">
                                <table border=0  >

                                    <tr>
                                        <td class="subTitlesh">
                                            Date:
                                        </td>

                                        <td class="fillLineh">

                                	<c:out value="${date}"/>

                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitlesh">
                                            To:
                                        </td>
                                        <td class="fillLineh">

                                            <c:out value="${refer.lastName}"/>,
			    							<c:out value="${refer.firstName}"/>,
                                        </td>

                                    </tr>
                                    <tr>
                                        <td class="subTitlesh">
                                            Address:
                                        </td>
                                        <td class="fillLineh">
                                	<p><c:out value="${refer.streetAddress}"/></p>
                                        </td>
                                    </tr>
                                    <tr>

                                        <td class="subTitlesh">
                                            Phone:
                                        </td>
                                        <td class="fillLineh">
                                	<c:out value="${refer.phoneNumber}"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitlesh">
                                            fax:
                                        </td>

                                        <td class="fillLineh">
                                			<c:out value="${refer.faxNumber}"/>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="subTitlesh">
                                            cc:
                                        </td>
                                        <td class="fillLineh">
                                <%=cp.getCc() %>
                                        </td>

                                    </tr>
                                    
                                </table>

                            </td>
                            <td valign="top">
                                <table border=0 class="leftPatient">
                                    <tr>
                                        <td class="subTitlesh">
                                            Patient:
                                        </td>
                                        <td class="fillLineh">
                            <c:out value="${demographic.lastName}"/>,
                            <c:out value="${demographic.firstName}"/>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td class="subTitlesh">
                                            Address:
                                        </td>
                                        <td class="fillLineh">
                                <c:out value="${demographic.address}"/><br>
                                <c:out value="${demographic.city}"/>,
                                <c:out value="${demographic.province}"/>,
                                <c:out value="${demographic.postal}"/>
                                        </td>
                                    </tr>
                                    <tr>

                                        <td class="subTitlesh">
                                            Phone:
                                        </td>
                                        <td class="fillLineh">
                                <c:out value="${demographic.phone}"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitlesh">
                                            DOB:
                                        </td>

                                        <td class="fillLineh">
                                <c:out value="${demographic.yearOfBirth}"/>/<c:out value="${demographic.monthOfBirth}"/>/<c:out value="${demographic.dateOfBirth}"/>  (y/m/d)
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="subTitlesh">
                                            OHIP #:
                                        </td>
                                        <td class="fillLineh">
                             <c:out value="${demographic.hin}"/>&nbsp;<c:out value="${demographic.ver}"/>&nbsp;

                                        </td>
                                    </tr>
                                    
                                    <tr>

                                        <td class="subTitlesh">

                                        </td>
                                        <td class="fillLineh">

                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                    </table>
                </td>
            </tr>
            <tr><td>&nbsp;
            	</td>
            </tr>
            <tr>
                <td class="subTitles">
                    Dear Dr.<c:out value="${refer.lastName}"/>:
                </td>
            </tr>
            <tr>

                <td class="letterContent">
                   <nested:equal property="cp.greeting" value="1">
                    I had the pleasure of seeing <c:out value="${demographic.age}"/> year old
                    <c:out value="${demographic.lastName}"/>,<c:out value="${demographic.firstName}"/>
                    <c:if test="${not empty appointDate}">on <c:out value="${appointDate}"/></c:if> on your kind referral.<br>
                    </nested:equal>
                    <c:if test="${cp.greeting == 2}">
                    This is a report on my most recent assessment of <c:out value="${demographic.age}"/> year old
                    <c:out value="${demographic.lastName}"/>,<c:out value="${demographic.firstName}"/>, whom I saw
                    <c:if test="${not empty appointDate}">on <c:out value="${appointDate}"/></c:if>.<br>
                    </c:if>
                </td>
            </tr>
            <c:if test="${not empty cp.clinicalInfo}">
            <tr>
                <td class="subTitles">
                    <b>Clinical Information:</b>
                </td>
            </tr>

            <tr>
                <td class="fillLine">
       <%=cp.getClinicalInfo()%>
                </td>
            </tr>
            </c:if>
            <c:if test="${not empty cp.allergies}">
            <tr>
                <td class="subTitles">

                    <b>Allergies and Medications:</b>

                </td>

            </tr>
            <tr>
                <td class="fillLine">

                   <%=cp.getAllergies()%>
                </td>
            </tr>
            </c:if>


            <c:if test="${not empty cp.examination}">
            <tr>
                <td class="subTitles">
                    <b>Examination:</b>
                </td>
            </tr>
            <tr>
                <td class="fillLine" style="table-layout:fixed;word-break:break-word;">
                    <%=cp.getExamination()%>

                </td>

            </tr>
            </c:if>
             <c:if test="${not empty cp.impression}">
            <tr>
                <td class="subTitles">
                    <b>Impression/Plan:</b>
                </td>
            </tr>
            <tr>
                <td class="fillLine">

                    <%=cp.getImpression()%>
                </td>

            </tr>
            </c:if>

            <tr>

                <td class="letterContent">
                    Thank you for allowing me to participate in the care of this patient.
                </td>
            </tr>
            <tr>
            <td class="letterContent">
            Best regards,
            </td>
            </tr>
            <tr>
            <td>
            <br>
            <br>
            </td>
            </tr>
            <c:if test="${providerflag eq 'false'}">
            <tr>
            <td class="letterContent">

                Associated with: <c:out value="cp.provider"/>
            </td>
            </tr>
            </c:if>
            <tr>
            <td class="letterContent">

                <c:out value="${appointmentDoctor}"/>
            </td>
            </tr>
            <tr>
                <td id="faxFooter">
                </td>
            </tr>
        </table>
	</html:form>
    </body>
</html:html>
