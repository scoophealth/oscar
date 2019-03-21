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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_prevention" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_prevention");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html ng-app="preventionReport">
<head>
	<title><bean:message key="oscarprevention.index.oscarpreventiontitre" /></title>
	<link href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/angular.min.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/preventionReportServices.js"></script>	
	<script src="<%=request.getContextPath() %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>	
</head>

<body vlink="#0000FF" class="BodyStyle" >
	<div ng-controller="preventionReport">
		<div class="page-header">
			<h4><bean:message key="oscarprevention.index.oscarpreventiontitre" /> <small><a ng-click="manageReportShowHide()">Manage reports</a></small></h4>
		</div>
		<div class="container-fluid" style="margin-left:15px;margin-right:15px;" ng-if="showManageReport">
		<!-- manage report start -->
		<div class="row">
				<h3>Manage Report</h3>
				<div class="col-sm-12">
					<div class="form-group">
					    <label for="provider">Report Name</label>
						<input type="text" ng-model="newReport.reportName" class="form-control"/>
					</div>
				</div>
				<div class="col-sm-2">
				  <div class="form-group">
				    <label for="provider">Age Search</label>
				    <select class="form-control" ng-model="newReport.ageStyle">
					   <option value="0">---NO AGE SPECIFIED---</option>
                        <option value="1">younger than</option>
                        <option value="2">older than</option>
                        <option value="3">equal too</option>
                        <option value="4">ages between</option>
					</select>
				  </div>
			 	</div>
				<div class="col-sm-2">
				  <div class="form-group">
				    <label for="provider">Age <small>in years, add m for months</small></label>
				    <input type="text" class="form-control" id="lower" placeholder="age" ng-model="newReport.age1">
				    <input type="text" class="form-control" id="high" placeholder="age" ng-show="newReport.ageStyle == 4" ng-model="newReport.age2">
				  </div>
				</div>
				<div class="col-sm-2">
					<div class="form-group">
				    
				      <label for="provider">Age Calculated</label>
				      <div class="radio">
					  	<label>
					    		<input type="radio" ng-model="newReport.ageCalc" id="optionsRadios1" value="0" >
					    		When report is run
					  	</label>
					  </div>
					  <div class="radio">
					  	<label>
					    <input type="radio" ng-model="newReport.ageCalc" id="optionsRadios2" value="1">
					   		as of : <input type="date" class="form-control" id="lower" placeholder="2020-03-31" ng-model="newReport.ageAsOf">
					  	</label>
					  	
					</div>
				      
				    </div>
				  </div>
				  <div class="col-sm-2">	 
					<div class="form-group">
				    		<label for="provider">Roster Status</label>
				    		<div class="radio" ng-repeat="rs in rosterArray">
						  <label>
						    <input type="radio" ng-model="newReport.rosterStat" value="{{rs.name}}">
						    {{rs.name}}
						  </label>
						</div>
						as of : <input type="date" class="form-control" id="lower" ng-model="newReport.rosterAsOf" placeholder="2020-03-31">
				  	</div>
                  </div>
                  <div class="col-sm-2">                     
               		<div class="form-group">
				    		<label for="provider">Sex</label>
				    		<select class="form-control" ng-model="newReport.sex">
					   		<option value="0">---NO SEX SPECIFIED---</option>
                        		<option value="1">Female</option>
                        		<option value="2">Male</option>
						</select>
				  	</div>
				</div>
				<div class="col-sm-2">                     
               		<div class="form-group">
				    		<label for="provider">Prevention Follow</label>
				    		<select class="form-control" ng-model="newReport.measurementTrackingType">
					   		<option value="CIMF">CIMF - Child Imms</option>
                        		<option value="FLUF">FLUF - Flu</option>
                        		<option value="PAPF">PAPF - Pap</option>
                        		<option value="MAMF">MAMF - Mam</option>
                        		<option value="FOBF">FOBF - FOBT</option>
						</select>
				  	</div>
				  	<div class="checkbox">
					  <label>
					    <input type="checkbox" value="L1" ng-model="newReport.letter1">
					    Letter 1
					  </label>
					</div>
					<div class="checkbox">
					  <label>
					    <input type="checkbox" value="L2" ng-model="newReport.letter2">
					    Letter 2
					  </label>
					</div>
					<div class="checkbox">
					  <label>
					    <input type="checkbox" value="P1" ng-model="newReport.phone1">
					    Phone 1
					  </label>
					</div>
				</div>

                
                  
                  
                  
               <%--  private List<PreventionSearchConfigTo1> preventions; --%>
	
	    


                      
                
			</div> <!-- 2nd row -->
			<div class="row">
				<div class="col-sm-2">
					<label for="provider">Exclusion Codes</label>
					<div class="input-group">
				      <input type="text" class="form-control" placeholder="Billing Code" ng-model="exCodeToAdd">
				      <span class="input-group-btn">
				        <button class="btn btn-default" type="button" ng-click="addExclusionCode(exCodeToAdd)">Add</button>
				      </span>
				    </div>
					<ul>
						<li ng-repeat="exCode in newReport.exclusionCodes">{{exCode}} <a ng-click="deleteElement(newReport.exclusionCodes,$index)">-del-</a></li>
					</ul>
				</div>
				<div class="col-sm-2">
					<label for="provider">Tracking Codes</label>
					<div class="input-group">
				      <input type="text" class="form-control" placeholder="Billing Code" ng-model="trackingCodeToAdd">
				      <span class="input-group-btn">
				        <button class="btn btn-default" type="button" ng-click="addTrackingCode(trackingCodeToAdd)">Add</button>
				      </span>
				    </div>
					<ul>
						<li ng-repeat="code in newReport.trackingCodes">{{code}}<a ng-click="deleteElement(newReport.trackingCodes,$index)">-del-</a></li>
					</ul>
				</div>
				<div class="col-sm-3">
					<div class="form-group">
					    <label for="provider">Exclusion and Tracking Code Start Date</label>
						<input type="date" ng-model="newReport.billingCodeStart" class="form-control"/>
					</div>
				</div>
				<div class="col-sm-3">
					<div class="form-group">
					    <label for="provider">Exclusion and Tracking Code End Date</label>
						<input type="date" ng-model="newReport.billingCodeEnd" class="form-control"/>
					</div>
				</div>
				
				<div class="col-sm-8">
					<div class="form-group">
					    <label for="provider">Preventions <button ng-click="addPrevention(newPrevReport)" class="btn btn-xs btn-default">Add</button></label>
					    <div>
					    		<div class="col-sm-2">                     
			               		<div class="form-group">
							    		<label for="provider">Prevention Type</label>   <%-- private String name; --%>
							    		<select class="form-control" ng-model="newPrevReport.name">
								   		<option value="DTaP">DTaP</option>
<option value="DTaP-IPV-Hib-HB">DTaP-IPV-Hib-HB</option>
<option value="DTaP-IPV-HB">DTaP-IPV-HB</option>
<option value="DTaP-Hib">DTaP-Hib</option>
<option value="DT-IPV">DT-IPV</option>
<option value="Tdap-IPV">Tdap-IPV</option>
<option value="TdP-IPV-Hib">TdP-IPV-Hib</option>
<option value="DTaP-IPV-Hib">DTaP-IPV-Hib</option>
<option value="DTaP-HBV-IPV-Hib">DTaP-HBV-IPV-Hib</option>
<option value="DTaP-IPV">DTaP-IPV</option>
<option value="Rot">Rot</option>
<option value="Hib">Hib</option>
<option value="Pneumococcus">Pneumococcus</option>
<option value="Pneumovax">Pneumovax</option>
<option value="Pneu-C">Pneu-C</option>
<option value="M">M</option>
<option value="MR">MR</option>
<option value="MMR">MMR</option>
<option value="MMRV">MMRV</option>
<option value="Men-P-AC">Men-P-AC</option>
<option value="Men-P-ACWY">Men-P-ACWY</option>
<option value="Men-C-ACYW-135">Men-C-ACYW-135</option>
<option value="Men">Men</option>
<option value="Men-B">Men-B</option>
<option value="MenC-C">MenC-C</option>
<option value="rMenB">rMenB</option>
<option value="VZ">VZ</option>
<option value="HZV">HZV</option>
<option value="RZV">RZV</option>
<option value="HepB">HepB</option>
<option value="T">T</option>
<option value="Td-IPV">Td-IPV</option>
<option value="dTap">dTap</option>
<option value="Td">Td</option>
<option value="Flu">Flu</option>
<option value="HA-Typh-I">HA-Typh-I</option>
<option value="HBTmf">HBTmf</option>
<option value="HepA">HepA</option>
<option value="HepAB">HepAB</option>
<option value="Chol-Ecol-O">Chol-Ecol-O</option>
<option value="CholEcol">CholEcol</option>
<option value="CHOLERA">CHOLERA</option>
<option value="Rabies">Rabies</option>
<option value="Typhoid-I">Typhoid-I</option>
<option value="Typhoid">Typhoid</option>
<option value="Typh-O">Typh-O</option>
<option value="HPV">HPV</option>
<option value="Tuberculosis">Tuberculosis</option>
<option value="TdP">TdP</option>
<option value="IPV">IPV</option>
<option value="BCG">BCG</option>
<option value="YF">YF</option>
<option value="TBE">TBE</option>
<option value="JE">JE</option>
<option value="PAP">PAP</option>
<option value="MAM">MAM</option>
<option value="PSA">PSA</option>
<option value="FOBT">FOBT</option>
<option value="COLONOSCOPY">COLONOSCOPY</option>
<option value="BMD">BMD</option>
<option value="HIV">HIV</option>
<option value="HepB">HepB</option>
<option value="HepC">HepC</option>
<option value="VDRL">VDRL</option>
<option value="chlamydia">chlamydia</option>
<option value="ghonorrhea">ghonorrhea</option>
<option value="H1N1">H1N1</option>
<option value="Zostavax">Zostavax</option>
<option value="Smoking">Smoking</option>
<option value="PHV">PHV</option>
<option value="Annual">Annual</option>
<option value="Obesity">Obesity</option>
									</select>
							  	</div>
							</div>
							<div class="col-sm-2">                     
			               		<div class="form-group">
							    		<label for="provider">Number</label> <%-- private int howManyPreventions = 0; --%>
							    		<select class="form-control" ng-model="newPrevReport.howManyPreventions">
			                        		<option value="1">1</option>
			                        		<option value="2">2</option>
			                        		<option value="3">3</option>
			                        		<option value="4">4</option>
			                        		<option value="5">5</option>
			                        		<option value="6">6</option>
			                        		<option value="7">7</option>
			                        		<option value="8">8</option>
									</select>
							  	</div>
							</div>
							<div class="col-sm-2">                     
			               		<div class="form-group">
							    		<label for="provider">Date Calculation</label> <%-- private int dateCalcType = 0; --%>
							    		<select class="form-control" ng-model="newPrevReport.dateCalcType">
			                        		<option value="1">As of a Date</option>
			                        		<option value="2">By Age</option>
									</select>
							  	</div>
							</div>
							<div class="col-sm-6" ng-if="newPrevReport.dateCalcType == 1">   <%-- public final static int ASOFDATE = 1; --%>
								<div class="col-sm-5">
									<div class="form-group">
									    <label for="provider">As of Date</label>
										<input type="date" ng-model="newPrevReport.asOfDate" class="form-control"/>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group">
									    <label for="provider">Number of months before</label>
										<input type="text" ng-model="newPrevReport.cutoffTimefromAsOfDate" class="form-control"/>
									</div>
								</div>
							<%-- 
							private Date asOfDate;
							private int cutoffTimefromAsOfDate;
							private String cuttoffTimeType = "M";
							--%>
							</div>
							<div class="col-sm-6"  ng-if="newPrevReport.dateCalcType == 2"> <%-- public final static int BYAGE = 2;  --%>
								<div class="col-sm-12">
									<div class="form-group">
									    <label for="provider">By Age</label>
										<input type="text" ng-model="newPrevReport.byAge" class="form-control"/>
									</div>
								</div>	
								<%--
							private int byAge;
							private String byAgeTimeType = "M";
							 --%>
							</div>
							
					    </div>
					    
					</div>
				<br>
				<%-- ul>
					<li ng-repeat="preve in newReport.preventions">{{preventionSummary(preve)}} <a ng-click="deleteElement(newReport.preventions,$index)">-del-</a></li>
				</ul>	
				--%>
				</div>
				
			</div>
			<ul>
					<li ng-repeat="preve in newReport.preventions">{{preventionSummary(preve)}} <a ng-click="deleteElement(newReport.preventions,$index)">-del-</a></li>
				</ul>
			<button ng-click="saveReport(newReport)" class="btn btn-default">Save Report</button>
			<hr>
		</div>
		<!-- manage report end -->
		<fieldset>
		<form>
		
		<div class="container-fluid" style="margin-left:15px;margin-right:15px;">
		<div class="row" >
		  <div class="col-sm-3">
		  <div class="form-group">
		    <label for="report">Report</label>
		    <select class="form-control" ng-model="selectedReport">
			  <option ng-repeat="report in reports" value="{{report.id}}">{{report.label}}</option>
			</select> 			
			<a class="pull-right" ng-click="editReport(selectedReport)" ng-if="selectedReport != undefined">-edit-</a>&nbsp;
			<a class="pull-right" ng-click="dectivateReport(selectedReport)" ng-if="selectedReport != undefined">-deactivate-</a>
		  </div>
		  </div>
		  <div class="col-sm-3">
		  <div class="form-group">
		    <label for="provider">Provider</label>
		    <select class="form-control" ng-model="selectedProvider">
			  <option ng-repeat="provider in providers" value="{{provider.providerNo}}">{{provider.lastName}}, {{provider.firstName}} ({{provider.providerNo}})</option>
			</select>
		  </div>
		  </div>
		  </div>
		  
		  <div class="row" >
		  	<div class="col-sm-1">
		  		<button type="submit" class="btn btn-default" ng-click="runReport(selectedReport,selectedProvider);">Submit</button>
		  	</div>
		  	<div class="col-sm-7 well" ng-if="selectedReport"> 
		  		<pre>{{summarySearchConfig(reportData)}}</pre> 
		  	</div>
		  	 
		  </div>
		  </div>
		</form>
		</fieldset>
		<hr>
		
		<div class="container-fluid" style="margin-left:15px;margin-right:15px;">
		
		<table class="table table-bordered table-hover">
		    <tr>
		    		<th>Total Patients</th>
		    		<td>{{reportData.totalPatients}}</td>
		    		<th>Ineligible</th>
		    		<td>{{reportData.ineligiblePatients}}</td>
		    		<th>Up to date</th>
		    		<td>{{reportData.up2date}} = {{getPercentage(reportData.totalPatients,reportData.ineligiblePatients,reportData.up2date) | number:0}} % </td>
		    		
		    <tr>
  			<tr>
  				<th>Demographic</th>
  				<th>DOB</th>
  				<th>Age as of {{reportData.searchConfig.ageAsOf | date}}</th>
  				<th>Sex</th>
  				<th>Lastname</th>
  				<th>Firstname</th>
  				<th>HIN</th>
  				<th>Phone</th>
  				<th>Email</th>
  				<th>Address</th>
  				<th>Guardian</th>
  				<th>Next Appt.</th>
  				<th>Status</th>
  				<th>Bonus Stat</th>
  				<th>Since Last Procedure Date</th>
  				<th>Last Procedure Date</th>
  				<th>Last Contact Method</th>
  				<th>Next Contact Method</th>
  				<%-- th>Select Contact</th --%>
  				<th>Roster Physician</th>
  				<th>&nbsp;</th>
  				
  			</tr>
  			<tr ng-repeat="line in reportData.items" class="{{getRowColor(line)}}" >
  				<td><a ng-click="openDemo(line.demographicNo)">{{line.demographicNo}}</a></td>
  				<td>{{line.dob | date }}</td>
  				<td>{{line.age}}</td>
  				<td>{{line.sex}}</td>
  				<td>{{line.lastname}}</td>
  				<td>{{line.firstname}}</td>
  				<td>{{line.hin}}</td>
  				<td>{{line.phone}}</td>
  				<td>{{line.email}}</td>
  				<td>{{line.address}}</td>
  				<td>
  					<span ng-if="line.substituteDecisionMakerReq">
  					Name:{{line.sdName}}<br>
  					Phone: {{line.sdPhone}}<br>
  					Address: {{line.sdAddress}}<br>
					Email:{{line.sdEmail}}<br>
					</span>
  				</td>
  				<td>{{line.nextAppt}}</td>
  				<td title="{{line.rank}}">{{line.state}}</td>
  				<td>{{line.bonusStatus}}</td>
  				<td>{{line.numMonths}}</td>
  				<td>{{line.lastDate | date }}</td>
  				<td ng-if="reportData.active">{{line.lastFollupProcedure}} - {{line.lastFollowup | date}}</td>
  				<td bgcolor="grey" ng-if="!reportData.active">-----</td>
  				<td ng-if="reportData.active">
  					<a ng-if="line.nextSuggestedProcedure != '----'" ng-click="openLetterScreen(line.nextSuggestedProcedure,line.demographicNo)">{{line.nextSuggestedProcedure}}</a>
  					<span ng-if="line.nextSuggestedProcedure === '----'">{{line.nextSuggestedProcedure}}</span>
  				</td>
  				<td bgcolor="grey" ng-if="!reportData.active">-----</td>
  				<%-- td>DOB7</td --%>
  				<td>{{line.rosteringDoc}}</td>
  				<td>
  				
  				<a ng-if="reportData.searchConfig.trackingCodes.length > 0" ng-click="openBill(line.demographicNo,reportData.searchConfig.trackingCodes[0])">Track</a>
  				<a ng-if="reportData.searchConfig.exclusionCodes.length > 0" ng-click="openBill(line.demographicNo,reportData.searchConfig.exclusionCodes[0])">Exclude</a>
  				<%--
  					<div class="btn-group">
					  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					    Options <span class="caret"></span>
					  </button>
					  <ul class="dropdown-menu">
					    <li><a href="#">Action</a></li>
					    <li><a href="#">Another action</a></li>
					    <li><a href="#">Something else here</a></li>
					    <li role="separator" class="divider"></li>
					    <li><a href="#">Separated link</a></li>
					  </ul>
					</div>
					--%>
  				</td>
  			</tr>
  			
		</table>
		<div class="row" >
			 <div class="col-sm-3">
				Recall letters: 
				<a ng-if="letter1.length > 0 && reportData.active" ng-click='openLetterScreen("L1",letter1)'>Send Letter One</a>
				<a ng-if="letter2.length > 0 && reportData.active" ng-click='openLetterScreen("L1",letter2)'>Send Letter Two</a>
				</div>
		<%--  a ng-if="phone1.length > 0" ng-click='openLetterScreen("L1",phone1)'>Send Letter One</a> --%>
		
		</div>
		</div>
	</div>
	</div>
	<br>
	<br>
	<script>
		var app = angular.module("preventionReport", ['preventionReportServices']);
		
		app.controller("preventionReport", function($scope,preventionReportService,$filter,$http) {
			
			$scope.rosterArray = [{name:"RO"},{name:"NR"},{name:"TR"}];
			
			$scope.newReport = {};
			$scope.newReport.exclusionCodes = [];
			$scope.newReport.trackingCodes = [];
			$scope.newReport.preventions = [];
			
			$scope.reports = [];
			$scope.reportData = {};
			
			$scope.newPrevReport = {};
			
			$scope.letter1 = [];
			$scope.letter2 = [];
			$scope.phone1 = [];
			
			$scope.dectivateReport = function(selectedReport){
				console.log("selectedReport",selectedReport);
				
				preventionReportService.dectivateReport(selectedReport).then(function(data){
					getList();
				});
				
			}
			
			$scope.showManageReport = false;
			
			$scope.manageReportShowHide = function(){
				if($scope.showManageReport){
					$scope.showManageReport = false;
				}else{
					$scope.showManageReport = true;
				}
			}
				
			
			$scope.editReport = function(selectedReport){
				console.log("selectedReport",selectedReport);
				
				preventionReportService.getReport(selectedReport).then(function(data){
					console.log("data coming back",data);
					console.log("before get report ",$scope.newReport);
					$scope.newReport = data;
					var d = new Date();
					d.setTime(data.ageAsOf);
					$scope.newReport.ageAsOf = d;
					
					
					var d2 = new Date();
					d2.setTime(data.rosterAsOf);
					$scope.newReport.rosterAsOf = d2;	
					
					
					var d3 = new Date();
					d3.setTime(data.billingCodeStart);
					$scope.newReport.billingCodeStart = d3;
					
					var d4 = new Date();
					d4.setTime(data.billingCodeEnd);
					$scope.newReport.billingCodeEnd = d4;
					 
					console.log("after get report ",$scope.newReport);
					$scope.showManageReport = true;
					//$scope.$apply();
				});
			}
			
			$scope.addExclusionCode = function(exCodeToAdd){
				exCodeToAdd = exCodeToAdd.replace(/\s/g,'');
				if(exCodeToAdd === ""){
					return;
				}
				$scope.newReport.exclusionCodes.push(exCodeToAdd);
				$scope.exCodeToAdd = "";
			}
		      
			$scope.deleteElement = function(list,ind){
				list.splice(ind, 1);
			}
			
			$scope.addTrackingCode = function(exCodeToAdd){
				exCodeToAdd = exCodeToAdd.replace(/\s/g,'');
				if(exCodeToAdd === ""){
					return;
				}
				$scope.newReport.trackingCodes.push(exCodeToAdd);
				$scope.trackingCodeToAdd = "";
			}
			
			$scope.addPrevention = function(newPrevReport){
				console.log("newPrevReport.name="+newPrevReport.name+"newPrevReport.howManyPreventions="+newPrevReport.howManyPreventions+"newPrevReport.dateCalcType="+newPrevReport.dateCalcType);
				if(newPrevReport.name == undefined){
					alert("A prevention type must be selected.");
					return;
				}
				if(newPrevReport.howManyPreventions == undefined){
					alert("The number of preventions for this type must be selected");
					return;
				}
				if(newPrevReport.dateCalcType == undefined){
					alert("Date calculation must be select to add prevention configuration.");
					return;
				}else{
					newPrevReport.dateCalcType = parseInt(newPrevReport.dateCalcType);
				}
				if(newPrevReport.dateCalcType != undefined && newPrevReport.dateCalcType == 1){
					if(newPrevReport.asOfDate == undefined){
						alert("An as of Date must be selected");
						return
					}
					if(newPrevReport.cutoffTimefromAsOfDate == undefined){
						alert("Enter the number of months prior to the as of date this will be valid for.");
						return
					}
					
				}
				if(newPrevReport.dateCalcType != undefined && newPrevReport.dateCalcType == 2){
					if(newPrevReport.byAge == undefined){
						alert("Enter the age in months that the preventions need to be completed by.");
						return
					}
					
					
				}
				
				
				$scope.newReport.preventions.push(newPrevReport);
				$scope.newPrevReport = {};
			}
			
			$scope.saveReport = function(newReport){
				preventionReportService.saveNewReport(newReport).then(function(data){
    					console.log("data coming back",data);
    					getList();
				});
				console.log("newReport",newReport);
			}
			
			getList = function(){
				preventionReportService.getList().then(function(data){
					console.log("data coming back",data);
					$scope.reports = data;
					
				});
			
			}
			
			getList();
			
			getAllActiveProviders = function(){
				preventionReportService.getAllActiveProviders().then(function(data){
					console.log("data coming back",data);
					$scope.providers = data;
				});
			
			}
			getAllActiveProviders()
			
			$scope.runReport = function(selectedReport,selectedProvider){
				console.log("$scope.selectedReport",selectedReport);
				console.log("$scope.selectedProvider",selectedProvider);
				
				$scope.letter1 = [];
				$scope.letter2 = [];
				$scope.phone1 = [];

				
				preventionReportService.runReport(selectedReport,selectedProvider).then(function(data){
					console.log("data coming back",data);
					$scope.reportData = data;
					
					for (line in $scope.reportData.items) {
						console.log("lin --- e",line,$scope.reportData.items[line].nextSuggestedProcedure)
						if($scope.reportData.items[line].nextSuggestedProcedure === "L1"){
							$scope.letter1.push($scope.reportData.items[line].demographicNo);
						}else if($scope.reportData.items[line].nextSuggestedProcedure === "L2"){
							$scope.letter2.push($scope.reportData.items[line].demographicNo);
						}else if($scope.reportData.items[line].nextSuggestedProcedure === "P1"){
							$scope.phone1.push($scope.reportData.items[line].demographicNo);
						}
						
					}
					
					console.log("$scope.letter1 "+$scope.letter1.length+" $scope.letter2 "+$scope.letter2);
					
				
					
				});
			}
			
			$scope.getRowColor = function(line){
				console.log("line.rank === 4"+(line.rank === 4)+ "(line.rank === 3)"+(line.rank === 3)+"(line.rank === 2)"+(line.rank === 2))
				if(line.rank === 4){ //update to date
					return "success";
				}else if(line.rank === 3){ // Refused
					return "danger";
				}else if(line.rank === 2){  //overdue
					return "warning";
				}
				
				return "";
			}
			
			$scope.getPercentage = function(totalPatients,ineligiblePatients,up2date){
				if(totalPatients == 0) return 0;
				
				return up2date / (totalPatients - ineligiblePatients) * 100;
			}
			
			$scope.preventionSummary = function(newPrevReport){
				console.log("newPrevReport",newPrevReport);
				console.log("one "+(newPrevReport.dateCalcType === 1)+" two "+(newPrevReport.dateCalcType === 2));
				if(newPrevReport.dateCalcType === 1){
					var asDate = $filter('date')(newPrevReport.asOfDate);
					//console.log("dddfdfd");
					return	"Require "+newPrevReport.howManyPreventions+" "+ newPrevReport.name + " preventions before "+asDate+" up to "+newPrevReport.cutoffTimefromAsOfDate+ " months prior";
				}else if(newPrevReport.dateCalcType === 2){
					return	"Require "+newPrevReport.howManyPreventions+" "+ newPrevReport.name + " preventions by age "+newPrevReport.byAge+" months";
				}
				return "N/A";
			}
			
			$scope.openLetterScreen = function(followupType,demoarr){
				//<a target="_blank" href="../report
				if("P1" === followupType){
					var comment = prompt('Are you sure you want to added this to patients record \n\nAdd Comment Below ','');
				    if (comment != null){																							//
				       var params =  {};
				       params.id=0;
				    	   params.followupType=$scope.reportData.searchConfig.measurementTrackingType;
				    	   params.followupValue=followupType;
				    	   params.demos=demoarr;
				    	   params.message=comment;
				       $http({
				            url: "../oscarMeasurement/AddShortMeasurement.do",
				            method: "GET",
				            params: params
				         }).then(function (data, status, headers, config) {
				        	 	console.log("success ",data);
				        	 	$scope.runReport($scope.selectedReport,$scope.selectedProvider);
				         },function (data, status, headers, config) {
				        	 	console.log("fail ",data);
				         });
				       
				    }
				       
				}else{ 
					console.log("$scope.reportData.",$scope.reportData);
					var urlToOpen = "GenerateLetters.jsp?"+ $.param({demo: demoarr,message: "Reminder Letter",followupType: $scope.reportData.searchConfig.measurementTrackingType,followupValue:followupType});
					window.open(urlToOpen);
				}
				<%-- <%=queryStr%>&amp;message=Letter 1 Reminder Letter sent for :"+request.getAttribute("prevType"),"UTF-8")%>&amp;followupType=<%=followUpType%>&amp;followupValue=L1">Generate First Letter</a>*/ --%>
			}
			
			$scope.openBill = function(demographicNo,billingCode){
			
			    var d = new Date();
			       month = '' + (d.getMonth() + 1);
			       day = '' + d.getDate();
			       year = d.getFullYear();

			    if (month.length < 2) month = '0' + month;
			    if (day.length < 2) day = '0' + day;

			    var today =  [year, month, day].join('-');
			
				var urlToOpen = "../billing/CA/ON/billingOB.jsp?billRegion=ON&billForm=GP&hotclick=&appointment_no=0&demographic_name=&demographic_no="+demographicNo+"&providerview=&user_no=&apptProvider_no=none&appointment_date="+today+"&start_time=00:00:00&bNewForm=1&status=t&serviceUnit0=1&serviceCode0="+billingCode;
				console.log("urlToOpen",urlToOpen);
				
				window.open(urlToOpen);
			}		
			
			$scope.summarySearchConfig = function(report){
				if(!angular.isDefined(report.searchConfig)){
					console.log("report.searchConfig not defined");
					return "";
				}
				console.log("");
				var reportStr = "Report Name: "+report.searchConfig.reportName+"\n"
				
				//{"id":null,"reportName":"Pap 5",
				reportStr = reportStr+"Age: ";
					if(report.searchConfig.ageStyle === 1){
						reportStr = reportStr+"Younger than : "+report.searchConfig.age1;
					}else if(report.searchConfig.ageStyle === 2){
						reportStr = reportStr+"Older than : "+report.searchConfig.age1;
					}else if(report.searchConfig.ageStyle === 3){
						reportStr = reportStr+"Equal too : "+report.searchConfig.age1;
					}else if(report.searchConfig.ageStyle === 4){
						reportStr = reportStr+"Ages Between: "+report.searchConfig.age1+" and "+report.searchConfig.age2;
					}
				
				if(report.searchConfig.ageCalc == 0){
					reportStr = reportStr+" As of today "
				}else{
					reportStr = reportStr+" As of: "+$filter('date')(report.searchConfig.ageAsOf);
				}
				
				
				
				//	"ageStyle":2,"age1":"2","age2":null,"ageCalc":1,"ageAsOf":1554004800000,
				reportStr = reportStr+"\nRoster Status: "+report.searchConfig.rosterStat+" as of "+$filter('date')(report.searchConfig.rosterAsOf)+"\n";	
				//	"rosterStat":"RO","rosterAsOf":1554004800000,
				reportStr = reportStr+"Measurement Tracking:"+report.searchConfig.measurementTrackingType;
				//	"measurementTrackingType":"PAPF","letter1":true,"letter2":true,"phone1":true,"sex":1,
				reportStr = reportStr+"\nPreventions:\n";
//				"preventions":[{"name":"PAP","dateCalcType":1,"asOfDate":1554004800000,"cutoffTimefromAsOfDate":42,"cuttoffTimeType":"M","byAge":0,"byAgeTimeType":"M","howManyPreventions":1}],				
				for(prev in report.searchConfig.preventions){
					console.log("prev",prev);
					reportStr = reportStr + $scope.preventionSummary(report.searchConfig.preventions[prev]) + "\n";
				}
				
//				"exclusionCodes":["Q140A"],"trackingCodes":["Q011A"],"billingCodeStart":157766400000,"billingCodeEnd":1640995200000} 
				reportStr = reportStr+"Exclusion Codes:";
				exFirst = false;
				for(prev in report.searchConfig.exclusionCodes){
					console.log("prev",prev);
					if(exFirst){
						reportStr = reportStr + ",";
					}
					exFirst = true;
					reportStr = reportStr + report.searchConfig.exclusionCodes[prev] + "\n";
					
				}
				
				reportStr = reportStr+"Tracking Codes:";
				exFirst = false;
				for(prev in report.searchConfig.trackingCodes){
					console.log("prev",prev);
					if(exFirst){
						reportStr = reportStr + ",";
					}
					exFirst = true;
					reportStr = reportStr + report.searchConfig.trackingCodes[prev] + "\n";
					
				}
				
				reportStr=reportStr + "Billing Code Range: "+$filter('date')(report.searchConfig.billingCodeStart)+" to "+$filter('date')(report.searchConfig.billingCodeEnd)+"\n";
				
				
				
				return reportStr;
			}
			
			$scope.openDemo = function(demo){
				window.open("../demographic/demographiccontrol.jsp?demographic_no="+demo+"&displaymode=edit&dboperation=search_detail",'MasterDemographic'); 
				<%--','MasterDemographic')"><%=dis.demographicNo%></a>) --%>
			}
		});
	
	</script>
</html>

