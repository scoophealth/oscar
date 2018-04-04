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
   <a>
      <span ng-bind-html="match.label | uibTypeaheadHighlight:query"></span>
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
      	<button type="button" class="btn btn-default">?</button>
        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Action <span class="caret"></span></button>
        <ul class="dropdown-menu dropdown-menu-right">
          <li><a href="#">Custom Drug</a></li>
          <li><a href="#">Note</a></li>
          <li><a href="#">Drug of Choice</a></li>
          <li role="separator" class="divider"></li>
          <li class="dropdown-header">Favourites</li>
          <li  ng-repeat="fmed in $ctrl.favouriteMeds"><a ng-click="$ctrl.selectFav(fmed)">{{fmed.name}}</a></li>
        </ul>
      </div><!-- /btn-group -->
    </div><!-- /input-group -->
</form>