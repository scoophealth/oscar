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

<!-- make div layout more fluid see medical history as an example -->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
	<div class="modal-content">

		<div class="modal-header"  > 
		<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="cancelPrint()"><span aria-hidden="true" >&times;</span></button>
			<h4 class="modal-title"><bean:message key="oscarEncounter.Index.PrintDialog" /></h4>
		</div>

		<div class="modal-body">			
	        	<div class="row">
		        	<div class="alert alert-danger" ng-show="page.selectedWarning">
	  					<strong><bean:message key="global.warning" /></strong> <bean:message key="oscarEncounter.nothingToPrint.msg" />
					</div>
	        	
	        		<div class="col-xs-6">
	        			<div class="radio">
						  <label>
						    <input type="radio" ng-model="pageOptions.printType" id="printopSelected" value="selected">
						    <bean:message key="oscarEncounter.Index.PrintSelect" />
						  </label>
						</div>
						<div class="radio">
						  <label>
						    <input type="radio" ng-model="pageOptions.printType" id="printopAll" value="all">
						    <bean:message key="oscarEncounter.Index.PrintAll" />
						  </label>
						</div>
	        			<div class="radio">
						  <label>
						    <input type="radio" ng-model="pageOptions.printType" id="printopDates" value="dates">
						    <bean:message key="oscarEncounter.Index.PrintDates" />&nbsp;
						    <a ng-click="printToday()" ><bean:message key="oscarEncounter.Index.PrintToday" /></a><br>
						  </label>
						
						<div class="form-group">
    							<label for="exampleInputEmail1"><bean:message key="oscarEncounter.startdate.title" /></label>
  							    <div class="input-group">
									<input type="text" class="form-control" placeholder="<bean:message key="oscarEncounter.startdate.title" />"  
									ng-model="pageOptions.dates.start" 
									datepicker-popup="yyyy-MM-dd" 
									datepicker-append-to-body="false" 
									is-open="startDatePrintPicker" 
									ng-click="startDatePrintPicker = true" 
									placeholder="YYYY-MM-DD"
									/>
									<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
								</div>
						</div>
						<div class="form-group">
    							<label for="exampleInputEmail1"><bean:message key="global.enddate" /></label>
  							    
							    <div class="input-group">
									<input type="text" class="form-control" placeholder="<bean:message key="global.enddate" />"  
									ng-model="pageOptions.dates.end" 
									datepicker-popup="yyyy-MM-dd" 
									datepicker-append-to-body="false" 
									is-open="endDatePrintPicker" 
									ng-click="endDatePrintPicker = true" 
									placeholder="YYYY-MM-DD"
									/>
									<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
								</div>
					 	</div>
						</div>
					
	        			
					
	        		</div>
	        		<div class="col-xs-6">
		        		<div class="checkbox">
						  <label>
						    <input type="checkbox" value="true" ng-model="pageOptions.cpp" alt="<bean:message key="oscarEncounter.togglePrintCPP.title"/>" id="imgPrintCPP"  >
						    <bean:message key="oscarEncounter.cpp.title" />
						  </label>
						</div>
						<div class="checkbox">
						  <label>
						    <input type="checkbox" value="true" ng-model="pageOptions.rx" alt="<bean:message key="oscarEncounter.togglePrintRx.title"/>" id="imgPrintRx"  >
						    <bean:message key="oscarEncounter.Rx.title" />
						  </label>
						</div>
						<div class="checkbox">
						  <label>
						    <input type="checkbox" value="true" ng-model="pageOptions.labs" alt="<bean:message key="oscarEncounter.togglePrintLabs.title"/>" id="imgPrintLabs"  >
						    <bean:message key="oscarEncounter.Labs.title" />
						  </label>
						</div>
	        		</div>
	        	</div>
	        	
		</div><!-- modal-body -->		
		<div class="modal-footer">
			<input type="button" class="btn" ng-click="print();" value="<bean:message key="global.btnPrint"/>" >		
			<input type="button" class="btn" ng-click="sendToPhr();" value="<bean:message key="global.btnSendToPHR"/>">
			<input type="button" class="btn" ng-click="cancelPrint()" value="<bean:message key="global.btnCancel"/>"> 
			<input type="button" class="btn" ng-click="clearPrint()" value="<bean:message key="global.clear"/>">
		</div>
	</div>
