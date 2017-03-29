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
    <h4>Demographic Record History</h4>
</div>  
<div class="modal-body">
	<div class="row" ng-show="showErrors === true">
		<div class="col-xs-12">
			<ul>
				<li class="text-danger" ng-repeat="error in errors">{{error}}</li>
			</ul>
		</div>
	</div>

	<div class="row">
		<div class="col-xs-12">
				<table class="table">
					<thead>
						<tr>
							<th>Date Modified</th>
							<th>Provider</th>
						</tr>
					</thead>
					
					<tbody>
						<tr ng-repeat="item in historyList.metadata">
							<td>{{item.lastUpdateDate | date: 'yyyy-MM-dd'}}</td>
							<td>{{item.lastUpdateUserName}}</td>
						</tr>
					</tbody>
				</table>
		</div>
	</div>
</div>
<div class="modal-footer">
    <button class="btn" ng-click="close()"><bean:message key="global.close" bundle="ui"/></button>
</div>





