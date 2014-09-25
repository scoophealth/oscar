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
<style>
	.field_error {color:red}
</style>
	
<div class="modal-header">
    <h4>Add QuickLink</h4>
</div>  
<div class="modal-body">
  <span class="field_error" ng-show="ql.$error.required">All fields required!</span>
  
   	<form name="ql" novalidate>
   		<div class="form-group">
		  <label>Name:</label>
		  <input ng-model="qll.name" placeholder="Name" class="form-control" type="text" ng-init="qll.name=''" required>
		</div>
   		<div class="form-group">
		  <label>URL:</label>
		  <input ng-model="qll.url" placeholder="http://..." class="form-control" type="text" ng-init="qll.url=''" required>
		</div>
		
	</form>
	
	
</div>
<div class="modal-footer">
    <button class="btn"  ng-click="addQuickLink(ql)">Add</button>
    <button class="btn" ng-click="close()">Cancel</button>
</div>







