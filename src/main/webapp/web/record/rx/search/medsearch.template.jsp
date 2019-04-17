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
<style>
	.searchedHeader{
		color: black;
	}
	
	.favHeader{
		color: fuchsia;
		
	}
	.inactiveHeader{
		text-decoration: line-through;
	}
</style>

<script type="text/ng-template" id="customTemplate.html">
   <a ng-class="{'favourite': match.model.fav,'greyedOut': (!match.model.active && !match.model.fav) }" >
	  <span ng-if="match.model.fav"  class='glyphicon glyphicon-star'></span>
      <span ng-class="{'deletedItem': (!match.model.active && !match.model.fav)}" ng-bind-html="match.label | uibTypeaheadHighlight:query"></span>
   </a>
</script>

<form >
	<div class="input-group">
	
      <input type="text"
      		 ng-model="$ctrl.selected"
      		 typeahead-on-select="$ctrl.onSelect($item, $model, $label)"
			 placeholder="<bean:message key="oscarRx.placeholder.searchforMeds" bundle="ui"/>"
			 uib-typeahead="med as $ctrl.medTypeAheadLabel(med) for med in $ctrl.lookupMeds($viewValue)"
			 typeahead-loading="loadingLocations"
			 typeahead-template-url="customTemplate.html"
			 <%--  typeahead-no-results="noResults" --%> 
			 class="form-control">
			
      <div class="input-group-btn">
      	<button type="button" class="btn btn-default" ng-click="$ctrl.fullSearch()">?</button>
        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Action <span class="caret"></span></button>
        <ul class="dropdown-menu dropdown-menu-right">
          <li><a ng-click="$ctrl.customDrug()">Custom Drug</a></li>
          <li><a ng-click="$ctrl.customNote()">Note</a></li>
          <li><a ng-click="$ctrl.DrugOfChoice()">Drug of Choice</a></li>
          <li role="separator" class="divider"></li>
          <li class="dropdown-header">Favourites</li>
          <li  ng-repeat="fmed in $ctrl.favouriteMeds"><a ng-click="$ctrl.selectFav(fmed)">{{fmed.name}}</a></li>
        </ul>
      </div><!-- /btn-group -->
    </div><!-- /input-group -->
</form>