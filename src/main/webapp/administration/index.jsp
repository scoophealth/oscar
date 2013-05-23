<!doctype html>
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
<jsp:include page="/provider/providerheader.jspf" />

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%
String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<head>
<title><bean:message key="admin.admin.page.title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<style>
body{background-color: #f3f3f3;}

.accordion-heading{background-color:#fff;border-bottom:thin solid #C6C6C6;}

#side{
border-right:thin solid #C6C6C6;
padding-right:10px;
}

.icon-chevron-right{float:right;}
</style>
</head>

<body>



    <div class="container-fluid" style="margin-top:70px;">
    <div class="row-fluid">
    <div class="span2" id="side">
   
    <!--Sidebar content-->
    
  
		<div class="accordion" id="accordion2">
		
			<div class="accordion-group nav nav-tabs">
			
			<div class="accordion-heading well-small" style="margin-bottom:0px;">
			<i class="icon-cog"></i> <bean:message key="admin.admin.page.title" />
			</div>

<%
int fileIndex=0;
if(request.getParameter("index")!=null){
	fileIndex=Integer.parseInt(request.getParameter("index"));
}
%>

<!-- #USER MANAGEMENT -->
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.userAdmin,_admin.torontoRfq,_admin.provider"
	rights="r" reverse="<%=false%>">			
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">
				<bean:message key="admin.admin.UserManagement" />
				<i class="icon-chevron-right"></i> 
				</a>
				
				</div>
				
				<div id="collapseOne" class="accordion-body collapse <%=fileIndex==1?"in":""%>">
				<div class="accordion-inner">
				<a href="?index=1">Create Provider</a>
				</div>
				</div>
</security:oscarSec>
<!-- #USER MANAGEMENT END -->

<!-- #BILLING -->
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.invoices,_admin,_admin.billing" rights="r" reverse="<%=false%>">
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
				<bean:message key="admin.admin.billing" />
				<i class="icon-chevron-right"></i>
				</a>
				</div>
				<div id="collapseTwo" class="accordion-body collapse">
				<div class="accordion-inner">
				Anim pariatur cliche...
				</div>
				</div>
</security:oscarSec>
<!-- #BILLING END-->

<!-- #LABS/INBOX -->
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin," rights="r" reverse="<%=false%>">
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseThree">
				<bean:message key="admin.admin.LabsInbox" />
				<i class="icon-chevron-right"></i>
				</a>
				</div>
				
				<div id="collapseThree" class="accordion-body collapse">
				<div class="accordion-inner">
				Anim pariatur cliche...
				</div>
				</div>
</security:oscarSec>
<!-- #LABS/INBOX END -->	

<!--  #FORMS/EFORMS -->	
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.eform" rights="r" reverse="<%=false%>">
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseFour">
				<bean:message key="admin.admin.FormsEforms" />
				<i class="icon-chevron-right"></i>
				</a>
				</div>
				<div id="collapseFour" class="accordion-body collapse">
				<div class="accordion-inner">
				Anim pariatur cliche...
				</div>
				</div>
</security:oscarSec>
<!--  #FORMS/EFORMS END-->	

<!-- #REPORTS-->
<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.reporting" rights="r" reverse="<%=false%>">
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseFive">
				<bean:message key="admin.admin.oscarReport" />
				<i class="icon-chevron-right"></i>
				</a>
				</div>
				
				<div id="collapseFive" class="accordion-body collapse">
				<div class="accordion-inner">
				Anim pariatur cliche...
				</div>
				</div>
	</security:oscarSec>
</caisi:isModuleLoad>
<!-- #REPORTS END -->	

<!-- #ECHART -->
<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.encounter" rights="r" reverse="<%=false%>">
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseSix">
				<bean:message key="admin.admin.eChart" />
				<i class="icon-chevron-right"></i>
				</a>
				</div>
				<div id="collapseSix" class="accordion-body collapse">
				<div class="accordion-inner">
				Anim pariatur cliche...
				</div>
				</div>
	</security:oscarSec>
</caisi:isModuleLoad>
<!-- #ECHART END-->	


<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<!-- #Schedule Management -->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.schedule" rights="r" reverse="<%=false%>">
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseSeven">
				<bean:message key="admin.admin.ScheduleManagement" />
				<i class="icon-chevron-right"></i>
				</a>
				</div>
				
				<div id="collapseSeven" class="accordion-body collapse">
				<div class="accordion-inner">
				Anim pariatur cliche...
				</div>
				</div>
	</security:oscarSec>
<!-- #Schedule Management END-->

<!-- #SYSTEM Management-->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=false%>">
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseEight">
				<bean:message key="admin.admin.SystemManagement" />
				<i class="icon-chevron-right"></i>
				</a>
				</div>
				<div id="collapseEight" class="accordion-body collapse">
				<div class="accordion-inner">
				Anim pariatur cliche...
				</div>
				</div>
	</security:oscarSec>
<!-- #SYSTEM Management END-->

<!-- #SYSTEM REPORTS-->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=false%>">
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseNine">
				<bean:message key="admin.admin.SystemReports" />
				<i class="icon-chevron-right"></i>
				</a>
				</div>
				
				<div id="collapseNine" class="accordion-body collapse">
				<div class="accordion-inner">
				Anim pariatur cliche...
				</div>
				</div>
	</security:oscarSec>
<!-- #SYSTEM REPORTS END-->


<!-- #INTEGRATION-->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=false%>">
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTen">
				<bean:message key="admin.admin.Integration" />
				<i class="icon-chevron-right"></i>
				</a>
				</div>
				<div id="collapseTen" class="accordion-body collapse">
				<div class="accordion-inner">
				Anim pariatur cliche...
				</div>
				</div>
	</security:oscarSec>
<!-- #INTEGRATION END -->

<!-- #STATUS-->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=false%>">
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseEleven">
				<bean:message key="admin.admin.Status" />
				<i class="icon-chevron-right"></i>
				</a>
				</div>
				
				<div id="collapseEleven" class="accordion-body collapse">
				<div class="accordion-inner">
				Anim pariatur cliche...
				</div>
				</div>
	</security:oscarSec>
<!-- #STATUS END -->
	
<!-- #Data Management -->
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.backup" rights="r" reverse="<%=false%>">
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwelve">
				<bean:message key="admin.admin.DataManagement" />
				<i class="icon-chevron-right"></i>
				</a>
				</div>
				<div id="collapseTwelve" class="accordion-body collapse">
				<div class="accordion-inner">
				Anim pariatur cliche...
				</div>
				</div>
	</security:oscarSec>
<!-- #Data Management END-->



<oscar:oscarPropertiesCheck property="OSCAR_LEARNING" value="yes">
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=false%>">
				<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseThirteen">
				<bean:message key="admin.admin.learning" />
				<i class="icon-chevron-right"></i>
				</a>
				</div>
				<div id="collapseThirteen" class="accordion-body collapse">
				<div class="accordion-inner">
				Anim pariatur cliche...
				</div>
				</div>
	</security:oscarSec>
</oscar:oscarPropertiesCheck>


</caisi:isModuleLoad>
		</div> <!-- ACCORDION GROUP END -->	
		</div> <!-- ACCORDION END -->
	
    </div>
    <div class="span10">
    <!--Body content-->
 
<%   
	String fileName;
	String title; //probably change this to jquery using the nav menu as source
    switch(fileIndex) {
    	case 0:
    		fileName="adminSplash.jspf";
    		title="Administrative Panel";
    		break;
    	case 1:
    		fileName="UserManagement";
    		title="User Management";
    		break;    		
        case 2:
            fileName="Billing";
            title="Billing";
            break;
        case 3:
        	fileName="Labs_Inbox";
        	title="Labs/Inbox";
            break;
        case 4:
        	fileName="Forms_eForms";
        	title="Forms/eForms";
            break;
        case 5:
        	fileName="Reports";
        	title="Reports";
            break;
        case 6:
        	fileName="eChart";
        	title="eChart";
            break;
        case 7:
        	fileName="ScheduleManagement";
        	title="Schedule Management";
            break;
        case 8:
        	fileName="SystemManagement";
        	title="System Management";
            break;
        case 9:
        	fileName="SystemReports";
        	title="Sytem Reports";
            break;
        case 10:
        	fileName="Integration";
        	title="Integration";
            break;
        case 11:
        	fileName="Status";
        	title="Status";
            break;
        case 12:
        	fileName="DataManagement";
        	title="DataManagement";
            break; 
        default:
        	fileName="adminSplash.jspf";
        	title="Panel";
    }
        
%>  
    
 	<h4 style="margin:0px;"><i class="icon-cog"></i> <%=title%> </h4>
    <div class="span12 well" style="background-color:#fff;margin:0px;">
    
    <!-- ****DYNAMIC CONTENT**** -->

	<jsp:include page="<%=fileName%>" />
  
    <!-- ****DYNAMIC CONTENT END**** -->
    
    </div><!-- span12 end -->
    
    </div>
    </div>
    </div>

</body>
</html>