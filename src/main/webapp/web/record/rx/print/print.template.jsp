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
<div class="modal-header">
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Print PDF</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.print()">Print</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Print & Paste into EMR</button>
    <button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Create New Rx</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Close</button>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Edit Rx</button>
</div>
<div class="modal-body" id="modal-body">
				
	<div class="row">
		<div class="col-lg-6">
			<iframe id="preview" name="preview" src="{{$ctrl.scriptURL}}" border="0" align="middle" width="420px" height="1000px" frameborder="0"></iframe>	
		</div>
		<div class="col-lg-6">	
			Additional Notes to add to Rx
			<textarea class="form-control" rows="3"></textarea>
		</div>
	</div>

</div>
<div class="modal-footer">
 <%-- todo still need a way to pick the size of the drop box --%>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Print PDF</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.print()">Print</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Print & Paste into EMR</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Create New Rx</button>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Close</button>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Edit Rx</button>
</div>




