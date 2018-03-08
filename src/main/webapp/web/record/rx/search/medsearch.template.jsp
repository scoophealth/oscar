<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<style>
	.searchedHeader{
		color: black;
	}
	
	.favHeader{
		color: purple;
		
	}
	.inactiveHeader{
		text-decoration: line-through;
	}
</style>

<form >
	<div class="input-group">
      <input type="text" 
			   class="form-control search-query" 
			   placeholder="<bean:message key="global.search" bundle="ui"/>" 
			   id="medQuickSearch" 
			   autocomplete="off" 
			   value="" />
      <div class="input-group-btn">
      	<button type="button" class="btn btn-default">?</button>
      	<%-- button type="button" class="btn btn-default">Custom/Note</button>  --%>
        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Action <span class="caret"></span></button>
        <ul class="dropdown-menu dropdown-menu-right">
          <li><a href="#">Custom Drug</a></li>
          <li><a href="#">Note</a></li>
          <li><a href="#">Drug of Choice</a></li>
          <li role="separator" class="divider"></li>
          <li class="dropdown-header">Favourites</li>
          <li  ng-repeat="fmed in $ctrl.favouriteMeds"><a href="#">{{fmed.name}}</a></li>
        </ul>
      </div><!-- /btn-group -->
    </div><!-- /input-group -->
</form>