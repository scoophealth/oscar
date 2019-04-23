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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<div class="page-header" style="margin-top: 0px; margin-bottom: 0px;">
		<h1 class="patientHeaderName" style="margin-top: 0px;" ng-cloak>
			<b>{{demographic.lastName}}, {{demographic.firstName}}</b>  <span ng-show="demographic.alias">({{demographic.alias}})</span> 
			
			<small class="patientHeaderExt pull-right"> 
				<i><bean:message key="demographic.patient.context.born"/>: </i>
				<b>{{demographic.dobYear}}-{{demographic.dobMonth}}-{{demographic.dobDay}}</b> (<b>{{demographic.age | age}}</b>) &nbsp;&nbsp; <i><bean:message key="demographic.patient.context.sex"/>:</i> <b>{{demographic.sex}}</b>
				<i> &nbsp;&nbsp; <bean:message key="Appointment.msgTelephone"/>:</i> <b>{{demographic.phone}}</b> 
				<!-- <span class="glyphicon glyphicon-new-window"></span>-->
			</small>
		</h1>
</div>

			
	
			<!-- -->
<div class="row">
	<div class="include-record-peice" ui-view></div>
</div>
    
