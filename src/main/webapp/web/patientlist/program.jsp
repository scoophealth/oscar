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
<div ng-controller="PatientListProgramCtrl">

<a ng-repeat="admission in admissions | filter:query" ng-click="goToRecord(admission.demographic)" class="list-group-item hand-hover" class="default">	
	<h5 class="list-group-item-heading">{{admission.demographic.lastName}},{{admission.demographic.firstName}}</h5>
	<p class="list-group-item-text"><bean:message key="patientList.program.since" bundle="ui"/>: {{admission.admissionDate | date: 'yyyy-MM-dd'}}  </p>
</a>

<a ng-if="admissions.length === 0" class="list-group-item hand-hover default">	
	<h5 class="list-group-item-heading"><bean:message key="patientList.program.empty" bundle="ui"/></h5>
</a>

</div>
