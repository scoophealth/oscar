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
<div class="modal-body">
	<form>
		<div class="form-group">
		    <label for="numAppt2Show"><bean:message key="patientList.config.numAppt2Show" bundle="ui"/></label>
	    	<input type="number" class="form-control" id="numAppt2Show" ng-model="patientListConfig.numberOfApptstoShow"  />
	  	</div>

	 	<br/>
	  	
	  	<div class="checkbox">
	    	<label>
	    		<input type="checkbox" ng-model="patientListConfig.showReason"> <b><bean:message key="patientList.config.showReason" bundle="ui"/></b>
		    </label>
	  	</div>
		  
	</form>  
</div>
<div class="modal-footer">  
	<button type="button" class="btn btn-default" ng-click="cancel()"><bean:message key="global.close" bundle="ui"/></button>
    <button type="button" class="btn btn-primary"  ng-click="saveConfiguration()"><bean:message key="global.save" bundle="ui"/></button>
</div>
    