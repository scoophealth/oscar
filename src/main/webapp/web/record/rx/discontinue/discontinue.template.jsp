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
<div class="modal-header">
	
	<h2>Discontinue: {{$ctrl.drug.instructions}}</h2>
</div>
<div class="modal-body" id="modal-body">
				
	<div class="row">
	
	
		 <form>
			  <div class="form-group">
			    <label for="exampleInputEmail1"><bean:message key="oscarRx.discontinuedReason.msgReason"/></label>
			   <select name="disReason" id="disReason" ng-model="$ctrl.discon.reason">
		            <option value="adverseReaction"><bean:message key="oscarRx.discontinuedReason.AdverseReaction"/></option>
		            <option value="allergy"><bean:message key="oscarRx.discontinuedReason.Allergy"/></option>
		            <option value="cost"><bean:message key="oscarRx.discontinuedReason.Cost"/></option>
		            <option value="discontinuedByAnotherPhysician"><bean:message key="oscarRx.discontinuedReason.DiscontinuedByAnotherPhysician"/></option>
		            <option value="doseChange"><bean:message key="oscarRx.discontinuedReason.DoseChange"/></option>
		            <option value="drugInteraction"><bean:message key="oscarRx.discontinuedReason.DrugInteraction"/></option>
		            <option value="increasedRiskBenefitRatio"><bean:message key="oscarRx.discontinuedReason.IncreasedRiskBenefitRatio"/></option>
		            <option value="ineffectiveTreatment"><bean:message key="oscarRx.discontinuedReason.IneffectiveTreatment"/></option>
		            <option value="newScientificEvidence"><bean:message key="oscarRx.discontinuedReason.NewScientificEvidence"/></option>
		            <option value="noLongerNecessary"><bean:message key="oscarRx.discontinuedReason.NoLongerNecessary"/></option>
					<option value="enteredInError"><bean:message key="oscarRx.discontinuedReason.EnteredInError"/></option>
		            <option value="patientRequest"><bean:message key="oscarRx.discontinuedReason.PatientRequest"/></option>
		            <option value="prescribingError"><bean:message key="oscarRx.discontinuedReason.PrescribingError"/></option>
		            <option value="simplifyingTreatment"><bean:message key="oscarRx.discontinuedReason.SimplifyingTreatment"/></option>
		            <option value="unknown"><bean:message key="oscarRx.discontinuedReason.Unknown"/></option>
		
					<option value="other"><bean:message key="oscarRx.discontinuedReason.Other"/></option>
		        </select>
			  </div>
			  <div class="form-group">
			    <label for="exampleInputPassword1"><bean:message key="oscarRx.discontinuedReason.msgComment"/></label>
			   <textarea ng-model="$ctrl.discon.comment" class="form-control" rows="3"></textarea>
			  </div>
			  
			</form>
		
	</div>

</div>
<div class="modal-footer">
 <%-- todo still need a way to pick the size of the drop box --%>
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Discontinue</button>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Close</button>
</div>




