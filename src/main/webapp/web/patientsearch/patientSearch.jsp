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
input.checkStyle{
   background: #888 !important;
   border: none !important;
   color: #000 !important;
}
</style>

<div ng-show="demographicReadAccess" >
 			
 		
<h2>Patient Search</h2>
<div class="row">
<div class="col-xs-6">
<form role="form" class="form-inline">  
      <div class="form-group">
        <select ng-model="search.type" ng-change="clearButMaintainSearchType()" style="width:auto;" class="form-control selectWidth" ng-init="search.type='Name'">
          <option value="Name">Name</option>
          <option value="Phone">Phone</option>
          <option value="DOB">Date of Birth</option>
          <option value="Address">Address</option>
          <option value="HIN">HIN</option>
          <option value="ChartNo">Chart No</option>
        </select>
            <div class="btn-group">
    <a class="btn dropdown-toggle" data-toggle="dropdown">
    
    <span class="caret"></span>
    </a>
    <ul class="dropdown-menu">
    <li>
    	<input ng-model="search.active" type="checkbox" class="checkStyle" ng-init="search.active=true"/>&nbsp;Show Active Only
    	
    </li>
    <li>
    	<input ng-model="search.integrator" ng-init="search.integrator=false" type="checkbox" class="checkStyle"/>&nbsp;Include Integrator	
    </li>
    <li>
    	<input ng-model="search.outofdomain" ng-init="search.outofdomain=true" type="checkbox" class="checkStyle"/>&nbsp;Out of Domain	
    </li>
    
    </ul>
    </div>
    
        <input ng-model="search.term" type="text" class="form-control" style="width:auto" placeholder="Search Term" ng-init="search.term=''"/>
        <button class="btn btn-primary" ng-click="doSearch()">Search</button>
        <button class="btn" ng-click="doClear()">Clear</button>  
        
      </div>
</form>
</div>
<div class="col-xs-6">
        <button class="btn btn-warning" ng-show="integratorResults != null && integratorResults.total > 0" ng-click="showIntegratorResults()"><span class="glyphicon glyphicon-exclamation-sign"></span>You have remote matches!</button>        
</div>
</div>  
                  
<div style="height:10px"></div>


<table ng-table="tableParams" show-filter="false" class="table">
  <tbody>
    <tr ng-repeat="patient in $data" ng-mouseover="patient.$selected=true" ng-mouseout="patient.$selected=false"
    	 ng-class="{'active': patient.$selected}"  ng-click="loadRecord(patient.demographicNo)">
 		
		<td data-title="'ID'" sortable="'DemographicNo'">
              {{patient.demographicNo}}
        </td>
        <td data-title="'Patient Name'" sortable="'Name'">
              {{patient.lastName}}, {{patient.firstName}}
        </td>
        <td data-title="'Chart #'" sortable="'ChartNo'">
              {{patient.chartNo}}
        </td>
        <td data-title="'Gender'" class="text-center" sortable="'Sex'">
              {{patient.sex}}
        </td>
        <td data-title="'DOB'" class="text-center" sortable="'DOB'">
              {{patient.dob| date: 'yyyy-MM-dd'}}
        </td>
        <td data-title="'Doctor'" sortable="'ProviderName'">
              {{patient.providerName}}
        </td>
        <td data-title="'Roster Status'" class="text-center" sortable="'RS'">
              {{patient.rosterStatus}}
        </td>
        <td data-title="'Patient Status'" class="text-center" sortable="'PS'">
              {{patient.patientStatus}}
        </td>	
        <td data-title="'Phone'" sortable="'Phone'">
              {{patient.phone}}
        </td>
    </tr>
  </tbody>

</table> 

 <!-- 
<pre>{{search}}</pre>

<pre>{{lastResponse}}</pre>
-->

</div>


<div ng-show="demographicReadAccess != null && !demographicReadAccess" >
 	<h3 class="text-danger"><span class="glyphicon glyphicon-warning-sign"></span>You don't have access to search for patients</h3>
</div>