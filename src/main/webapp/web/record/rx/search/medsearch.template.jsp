<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<style>
	.searchedHeader{
		color: black;
	}
	
	.favHeader{
		color: purple;
		
	}
</style>

<form >
	<div class="form-group">
		<input type="text" 
			   class="form-control search-query" 
			   placeholder="<bean:message key="global.search" bundle="ui"/>" 
			   id="medQuickSearch" 
			   autocomplete="off" 
			   value="" />
	</div>
</form>