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

<div class="modal-header">
    <h4><bean:message key="patientsearch.integrator.title" bundle="ui"/>s ({{total}} <bean:message key="patientsearch.integrator.found" bundle="ui"/>)</h4>
</div>  
<div class="modal-body" >
	<table class="table">
		<thead>
			<tr>
				<th><bean:message key="patientsearch.integrator.header.name" bundle="ui"/></th>
				<th><bean:message key="patientsearch.integrator.header.gender" bundle="ui"/></th>
				<th><bean:message key="patientsearch.integrator.header.dob" bundle="ui"/></th>
				<th></th>
			</tr>
		</thead>
		<tr ng-repeat="d in results | offset:startIndex | limitTo:pageSize">
			<td>{{d.lastName}}, {{d.firstName}}</td>
			<td>{{d.sex}}</td>
			<td>{{d.dob| date: 'yyyy-MM-dd'}}</td>
			<td><button class="btn btn-primary" ng-click="doImport(d)"><bean:message key="patientsearch.integrator.import" bundle="ui"/></button>
</td>
		</tr>
		<tfoot ng-show="total > pageSize">
			<tr>
				<td colspan="4">						
					<button class="btn" ng-click="prevPage()" ng-disabled="startIndex==0"><bean:message key="patientsearch.integrator.prev" bundle="ui"/></button>
					<button class="btn" ng-click="nextPage()" ng-disabled="startIndex+pageSize > total"><bean:message key="patientsearch.integrator.next" bundle="ui"/></button>
				</td>
			</tr>
		</tfoot>
	</table>
	
<!-- 
<pre>{{results}}</pre>
-->
</div>
<div class="modal-footer">
    <button class="btn" ng-click="close()"><bean:message key="global.close" bundle="ui"/></button>
</div>

