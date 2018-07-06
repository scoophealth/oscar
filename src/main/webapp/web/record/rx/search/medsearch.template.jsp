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