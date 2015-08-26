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

<div ng-controller="PatientListDemographicSetCtrl">
<form class="form-horizontal">
<select ng-model="filter.name" class="form-control" ng-change="changeMoreTab(currentmoretab.id,filter)" ng-init="filter.name=''">
	<option value=""><bean:message key="patientList.demographicSets.select" bundle="ui"/></option>
	<option ng-repeat="s in sets" value="{{s}}">{{s}}</option> 
</select>
</form>

<a ng-repeat="patient in patients | filter:query" ng-click="goToRecord(patient)" class="list-group-item default hand-hover">	
	<h5 class="list-group-item-heading">{{patient.name}}</h5>
</a>
</div>

