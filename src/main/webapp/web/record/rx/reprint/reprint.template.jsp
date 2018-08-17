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
	
	<h2>Reprint: </h2>
</div>
<div class="modal-body" id="modal-body">
				
	<div class="row" ng-repeat="rx in $ctrl.rxList | orderBy: '-datePrescribed'" >
	
	<div  class="col-md-11">
		<h3>{{rx.datePrescribed}}  Printed: {{rx.reprintCount+1}}  <button ng-click="$ctrl.reprint(rx)" class="btn btn-primary pull-right">Print</button></h3>
		<div class="well"><pre>{{rx.textView}}</pre></div>
	</div>
	
		 
		
	</div>

</div>
<div class="modal-footer">
 <%-- todo still need a way to pick the size of the drop box --%>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Close</button>
</div>




