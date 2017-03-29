<%@ page import="oscar.oscarClinic.ClinicData" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%--

    Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
    Department of Computer Science
    LeadLab
    University of Victoria
    Victoria, Canada

--%>

<script type="text/javascript" src="record/rx/rxModel.js"></script>

<div class="row"> <!-- MAIN CONTAINER -->
    <div class="col-sm-8" id="rx-wrapper">

        <div class="row"> <!-- CURRENT MEDS CONTAINER -->
            <div class="col-sm-12">

                <div class="row">
                    <div class="col-xs-9"><h3>Current Medications</h3></div>
                    <div class="col-xs-3">
                        <button class="btn btn-primary"
                                style="margin-top:20px;"
                                ng-click="openLegacyRx(demographicNo)">
                            Classic Rx</button>
                    </div>
                </div>


                <hr>

                <div id="no-current-meds-panel" style="display:none;">
                    <div class="well well-lg"
                         style="text-align:center">
                        <span class="well-text">
                            No Current Medications
                        </span>
                    </div>
                </div>

                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">

                    <!-- Repeat panels for each medication -->
                    <div ng-repeat="c in currentMedications" class="panel panel-default">

                        <div class="panel-heading row rx-toggle-row"
                             ng-class="selectedMedications.indexOf(c.id) !== -1 ? 'rx-med-selected' : ''"
                             role="tab" id="heading-{{c.id}}"
                             ng-click="toggleSelectedMed(c.id);">

                            <div class="col-xs-10">
                                <p style="margin-top:3%;">
                                    <b>{{ c.brandName || c.name }}</b>
                                    <span ng-if="c.takeMin && c.takeMax">- {{c.takeMin}}-{{c.takeMax}} </span>
                                    <span ng-if="c.frequency">- {{ c.frequency }} </span>
                                    <span ng-if="c.route"> - {{ c.route }} </span>
                                    <span ng-if="c.prn"> - PRN </span>
                                </p>

                                <p style="padding-left:10px;">
                                    <span ng-if="c.rxDate">Start Date: {{ c.rxDate | date:'yyyy-MM-dd'  }}</span>
                                    <span ng-if="c.endDate && !sameDateToDay(c.endDate, c.rxDate)">
                                        , End Date: <span ng-class="{'date-expiring' : willExpire(c, 7) ,'date-expired': expiredMedication(c)}" >{{ c.endDate | date:'yyyy-MM-dd'  }}</span>
                                    </span>
                                    <span ng-if="c.longTerm"> - long term </span>
                                    <span ng-if="c.externalProvider"> - {{c.externalProvider}}</span>
                                </p>

                            </div>

                            <div class="col-xs-2">
                                <div class="rx-chevron-wrapper">
                                    <a role="button" data-toggle="collapse" data-parent="#accordion"
                                       ng-click="toggleAccordian('collapse-'+c.id);" aria-expanded="true"
                                       aria-controls="collapse-{{c.id}}">
                                        <div class="rx-collapse-toggle">
                                            <icon class="rx-chevron glyphicon glyphicon-chevron-down"></icon>
                                        </div>
                                    </a>
                                </div>
                            </div>

                        </div>

                        <div id="collapse-{{c.id}}"
                             class="panel-collapse collapse in"
                             role="tabpanel"
                             aria-labelledby="heading-{{c.id}}">
                            <div class="panel-body" id="panel-body-{{c.id}}">

                                <div class="row">
                                    <div class="col-xs-12">
                                        <ul class="nav nav-tabs">
                                            <li id='panel-{{c.id}}-details-tab'
                                                ng-click="selectTab('details', c);"
                                                role="presentation"
                                                class="active">
                                                    <a href="">Details</a>
                                            </li>
                                            <li id='panel-{{c.id}}-history-tab'
                                                ng-click="selectTab('history', c);"
                                                role="presentation">
                                                    <a href="">History</a>
                                            </li>
                                            <li id='panel-{{c.id}}-discontinue-tab'
                                                ng-click="selectTab('discontinue', c);"
                                                role="presentation">
                                                    <a href="">Discontinue</a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>


                                <div id="details-panel-{{c.id}}" style="display:block;">

                                    <form class="form-horizontal">

                                        <h4>Instructions:</h4>
                                        <br>

                                        <div class="form-group">

                                            <label class="col-xs-3 control-label" for="medication-dose-min-{{c.id}}">Dose</label>
                                            <div class="col-xs-2">
                                                <input type="number" class="form-control"
                                                       id="medication-dose-min-{{c.id}}"
                                                       ng-model="c.takeMin"
                                                       disabled >
                                            </div>
                                            <div class="col-xs-2">
                                                <input type="number" class="form-control"
                                                       id="medication-dose-max={{c.id}}"
                                                       ng-model="c.takeMax"
                                                        disabled >
                                            </div>


                                            <label class="col-xs-1 control-label" for="medication-form-{{c.id}}">Form</label>
                                            <div class="col-xs-3">
                                                <input type="text" class="form-control"
                                                       id="medication-form-{{c.id}}"
                                                       ng-model="c.form" disabled>
                                            </div>

                                        </div>

                                        <div class="form-group">

                                            <label class="col-xs-3 control-label" for="medication-freq-{{c.id}}">Frequency</label>
                                            <div class="col-xs-4">
                                                <input type="text" class="form-control"
                                                       id="medication-freq-{{c.id}}"
                                                       ng-model="c.frequency" disabled>
                                            </div>


                                            <label class="col-xs-1 control-label" for="medication-route-{{c.id}}">Route</label>
                                            <div class="col-xs-3">
                                                <select class="form-control"
                                                        id="medication-route-{{c.id}}"
                                                        ng-model="c.route" disabled>
                                                    <option value="PO">oral</option>
                                                    <option value="SC">subcutaneous</option>
                                                    <option value="SL">sublingual</option>
                                                    <option value="PATCH">patch</option>
                                                    <option value="TOP">topical</option>
                                                    <option value="INH">inhale</option>
                                                    <option value="IM">intra-muscular</option>
                                                    <option value="SUPP">rectal</option>
                                                    <option value="O.D.">right eye</option>
                                                    <option value="O.S.">left eye</option>
                                                    <option value="O.U.">both eyes</option>
                                                </select>
                                            </div>

                                        </div>

                                        <div class="form-group">

                                            <div class="row">
                                                <div class="col-xs-3"></div>
                                                <div class="col-xs-9">
                                                    <label class="checkbox-inline">
                                                        <input type="checkbox"
                                                               id="medication-prn-{{c.id}}"
                                                               ng-model="c.prn" disabled> PRN
                                                    </label>
                                                    <label class="checkbox-inline">
                                                        <input type="checkbox"
                                                               id="medication-lt-{{c.id}}"
                                                               ng-model="c.longTerm" disabled> Longterm Medication
                                                    </label>

                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group">

                                            <div class="row">
                                                <label class="col-xs-3 control-label"
                                                       for="medication-additional-instructions-{{c.id}}">
                                                    Additional Instructions
                                                </label>
                                                <div class="col-xs-8">
                                            <textarea style='margin-left:5px;' class="form-control"
                                                      rows="2"
                                                      id="medication-additional-instructions-{{c.id}}"
                                                      ng-model="c.additionalInstructions"disabled></textarea>
                                                </div>
                                            </div>

                                        </div>

                                        <div class="form-group">
                                            <label class="col-xs-3 control-label"
                                                   for="medication-prescriber">Prescriber/Initiator</label>
                                            <div class="col-xs-6" style="display:block;" id="medication-prescriber-wrapper-{{c.id}}">

                                                <input type="text" class="form-control" ng-if="c.externalProvider" ng-model="c.externalProvider" disabled>
                                                <input type="text" class="form-control" ng-if="!c.externalProvider" ng-model="user.formattedName" disabled>
                                            </div>
                                        </div>

                                        <div class="form-group" id="medication-started-wrap-{{c.id}}">
                                            <label class="col-xs-3 control-label" for="medication-started-{{c.id}}">Date Started</label>
                                            <div class="col-xs-3">
                                                <input type="text" class="form-control"
                                                       id="medication-started-{{c.id}}"
                                                       placeholder="yyyy-MM-dd"
                                                       value="{{c.rxDate | date:'yyyy-MM-dd'}}"
                                                       disabled >
                                            </div>
                                            <label ng-if="!sameDateToDay(c.rxDate, c.endDate)" class="col-xs-1 control-label" for="medication-end-{{c.id}}">End</label>
                                            <div ng-if="!sameDateToDay(c.rxDate, c.endDate)" class="col-xs-3">
                                                <input type="text" class="form-control"
                                                       id="medication-end-{{c.id}}"
                                                       placeholder="yyyy-MM-dd"
                                                       value="{{c.endDate | date:'yyyy-MM-dd'}}"
                                                       disabled >
                                            </div>
                                        </div>

                                    </form>

                                </div> <!-- END Instructions -->

                                <div id="history-panel-{{c.id}}" style="display:none;">

                                    <h4>History <span style="font-size:12pt;font-style: italic; color:grey;">(as record in OSCAR)</span></h4>

                                    <div style="max-height:200px; overflow:scroll">

                                        <table class="table table-condensed table-hover">

                                            <thead>
                                                <th>Provider</th>
                                                <th>Start</th>
                                                <th>End/Archived</th>
                                                <th>Details</th>
                                            </thead>

                                            <tbody>

                                                <tr ng-repeat="h in c.history"
                                                    style="font-size:10pt;"
                                                    ng-class="{'rx-med-inactive': h.archived}"
                                                >
                                                    <td>
                                                        <span ng-if="h.externalProvider">{{h.externalProvider}}</span>
                                                        <span ng-if="!h.externalProvider">{{user.formattedName}}</span>
                                                    </td>
                                                    <td>{{h.rxDate | date : 'yyyy-MM-dd'}}</td>
                                                    <td>
                                                        <span ng-if="!h.archived && h.endDate && !sameDateToDay(h.endDate, h.rxDate)">
                                                            {{h.endDate | date : 'yyyy-MM-dd'}}
                                                        </span>
                                                        <span ng-if="!h.archived && (!h.endDate || sameDateToDay(h.endDate, h.rxDate))">
                                                            N/A
                                                        </span>
                                                        <span ng-if="h.archived">
                                                            {{h.archivedDate | date : 'yyyy-MM-dd' }}
                                                        </span>
                                                    </td>
                                                    <td>{{h.instructions}}</td>
                                                </tr>

                                            </tbody>

                                        </table>

                                    </div>
                                    <div class="" id="history-panel-error-{{c.id}}" style="display:none;">
                                        <div class="alert alert-danger">
                                            <p>Failed to load history for this medication.</p>
                                        </div>
                                    </div>

                                </div>

                                <div id="discontinue-panel-{{c.id}}" style="display:none;">

                                    <h4>Discontinue Medication:</h4>
                                    <br>
                                    <div class="row">
                                        <div class="col-xs-9">
                                            <form class="form-horizontal">
                                                <div class="form-group" id="discontinue-{{c.id}}-wrapper">
                                                    <label class="col-xs-2 control-label" for="discontinue-{{c.id}}">Reason</label>
                                                    <div class="col-xs-10">
                                                        <select class="form-control"
                                                                id="discontinue-{{c.id}}"
                                                                ng-init="c.archivedReason = null"
                                                                ng-model="c.archivedReason">
                                                            <option value="null">---</option>
                                                            <option value="adverseReaction">Adverse Reaction</option>
                                                            <option value="allergy">Allergy</option>
                                                            <option value="ineffectiveTreatment">Ineffective Treatment</option>
                                                            <option value="prescribingError">Prescribing Error</option>
                                                            <option value="noLongerNecessary">No Longer Necessary</option>
                                                            <option value="simplifyingTreatment">Simplifying Treatment</option>
                                                            <option value="patientRequest">Patient Request</option>
                                                            <option value="newScientificEvidence">New Scientific Evidence</option>
                                                            <option value="increasedRiskBenefitRatio">Increased Risk/Benefit Ratio</option>
                                                            <option value="discontinuedByAnotherPhysician">Discontinued By Another Physician</option>
                                                            <option value="cost">Cost</option>
                                                            <option value="drugInteraction">Drug Interaction</option>
                                                            <option value="deleted">Delete</option>
                                                            <option value="unknown">Unknown Reason</option>
                                                            <option value="other">Other Reason</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                        <div class="col-xs-3">
                                            <button class="btn btn-danger" ng-click="discontinueMedication(c.id)">Discontinue</button>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>

                    </div>
                    <br>
                </div>

            </div>
        </div> <!-- END CURRENT MEDS CONTAINER -->

        <div class="row">

            <div class="col-xs-12">

                <div class="row" id="add-medication-button-wrapper">
                    <div class="col-xs-12">
                        <button onclick="toggleAddMedication();"
                                class="btn btn-primary">
                            Add New Medication
                        </button>
                    </div>
                </div>

                <div class="row" id="add-medication-wrapper" style="display:none;">

                    <hr>

                    <div class="col-xs-12">

                        <form class="form-horizontal">
                            <div class="form-group" id="medication-search-wrapper">
                                <label class="col-xs-3 control-label"
                                       for="medication-search">Medication Search</label>
                                    <div class="input-group col-xs-6">
                                        <input type="text"
                                               class="form-control typeahead search-query"
                                               ng-model="medicationSearch"
                                               id="medication-search"
                                               placeholder=""
                                        >
                                        <div class="input-group-addon">
                                            <a href="" style='color:black;' ng-click="moreDrugChoices();">
                                                <icon class="glyphicon glyphicon-search"></icon>
                                            </a>
                                        </div>
                                    </div>
                                <div class="col-xs-2">
                                    <a href="" ng-click="customDrugHandler();">
                                        <button class="btn btn-default">Add Custom</button>
                                    </a>
                                </div>
                            </div>

                            <div class="row" id="drug-details-panel" style="display:none;">
                                <div class="col-xs-3"></div>
                                <div class="col-xs-8">
                                    <div class="panel panel-default">
                                        <div class="panel-body" id="medication-details-panel-body">
                                            <h5 ng-if="newMed.genericName !== '$$custom$$' && newMed.gn !== '$$custom$$'">
                                                Medication Details:
                                            </h5>
                                             <h5 ng-if="newMed.gn === '$$custom$$' || newMed.genericName === '$$custom$$'">
                                                <b style="color:red">Custom Drug:</b>
                                            </h5>
                                            <table class="table borderless table-condensed" id="instructions-guide-table">
                                                <tbody>
                                                    <tr><td>Name:</td><td>{{newMed.name}}</td></tr>
                                                    <tr ng-if="newMed.gn !== '$$custom$$'"><td>Generic Name:</td><td>{{newMed.gn}}</td></tr>
                                                    <tr ng-if="newMed.genericName !== '$$custom$$'"><td>Ingredient:</td><td>{{newMed.genericName}} {{newMed.strength}} {{newMed.strengthUnit}}</td></tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group has-feedback" id="instruction-wrapper">

                                <label class="col-xs-3 control-label" for="medication-instruction">Instructions</label>

                                <div class="col-xs-8 input-group">
                                    <input type="text"
                                           class="form-control"
                                           ng-model="newMed.instructions"
                                           ng-blur="blurInstructionsField(newMed.instructions);"
                                           id="medication-instruction"
                                           placeholder=""
                                           autocomplete="off">

                                    <div class="input-group-addon">
                                        <a href="" style='color:black;' ng-click="toggleInstructionsGuidePanel();">
                                            <icon class="glyphicon glyphicon-question-sign"></icon>
                                        </a>
                                    </div>
                                </div>
                            </div>

                            <div class="row" id="instructions-guide-panel" style="display:none;">
                                <div class="col-xs-3"></div>
                                <div class="col-xs-8">
                                    <div class="panel panel-default">
                                        <div class="panel-body" id="instructions-guide-panel-body">
                                            <h4>Instruction Guide:</h4>
                                            <hr>
                                            <table class="table borderless table-condensed" id="medication-details-table">
                                                <thead>
                                                <th>Method</th>  <th>Frequency</th> <th>Route</th>
                                                </thead>
                                                <tbody>
                                                <tr>  <td>take</td>              <td>OD, BID, TID, QID</td>      <td>PO</td> </tr>
                                                <tr>  <td>apply</td>            <td>Q1H, Q2H, Q1-2H</td>        <td>SL</td> </tr>
                                                <tr>  <td>rub well in</td>     <td>Q4H, Q4-6H, Q6H</td>        <td>IM</td> </tr>
                                                <tr>  <td></td>                <td>Q8H, Q12H</td>              <td>SC</td> </tr>
                                                <tr>  <td><b>Dose</b></td>              <td>QAM, QPM, QHS</td>          <td>SL</td> </tr>
                                                <tr>  <td>1</td>               <td>daily, twice daily</td>     <td>TOP</td> </tr>
                                                <tr>  <td>1-2</td>               <td>3x daily, 4x daily</td>          <td>PATCH</td> </tr>
                                                <tr>  <td>1/2</td>               <td>weekly</td>          <td>INH</td> </tr>
                                                <tr>  <td>1/4</td>                <td>Q1Week, Q2Week</td>          <td>SUPP</td> </tr>
                                                <tr>  <td>3-4</td>    <td>monthly</td>          <td>O.D., O.S., O.U.</td> </tr>
                                                <tr>  <td>5-6</td>               <td>Q1Month, Q3Month</td>          <td></td> </tr>
                                                </tbody>
                                            </table>
                                            <hr>
                                            <p>Use these "short hands" to quickly create medication instructions.</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">

                                <label class="col-xs-3 control-label"
                                       for="medication-strength">Strength</label>
                                <div class="col-xs-4">
                                    <input type="text"
                                           ng-model="newMed.strength"
                                           class="form-control"
                                           id="medication-strength"
                                           placeholder="">
                                </div>

                                <label class="col-xs-1 control-label"
                                       for="medication-strength-unit">Units</label>
                                <div class="col-xs-3">
                                    <select class="form-control"
                                            ng-model="newMed.strengthUnit"
                                            id="medication-strength-unit">
                                        <option value="MG">mg</option>
                                        <option value="ML">mL</option>
                                        <option value="G">g</option>
                                        <option value="UG">ug</option>
                                        <option value="UNIT">units</option>
                                        <option value="%">%</option>
                                    </select>
                                </div>

                            </div>

                            <div class="form-group">

                                <label class="col-xs-3 control-label" for="medication-dose-min">Dose</label>
                                <div class="col-xs-2">
                                    <input type="number" class="form-control"
                                           id="medication-dose-min"
                                           ng-model="newMed.takeMin">
                                </div>
                                <div class="col-xs-2">
                                    <input type="number" class="form-control"
                                           id="medication-dose-max"
                                            ng-model="newMed.takeMax">
                                </div>


                                <label class="col-xs-1 control-label" for="medication-form">Form</label>
                                <div class="col-xs-3">
                                    <input type="text" class="form-control"
                                           id="medication-form"
                                           ng-model="newMed.form">
                                </div>

                            </div>

                            <div class="form-group">

                                <label class="col-xs-3 control-label" for="medication-freq">Frequency</label>
                                <div class="col-xs-4">
                                    <input type="text" class="form-control"
                                           id="medication-freq"
                                           ng-model="newMed.frequency">
                                </div>

                                <label class="col-xs-1 control-label" for="medication-route">Route</label>
                                <div class="col-xs-3">
                                    <select class="form-control"
                                            id="medication-route"
                                            ng-model="newMed.route">
                                        <option value="PO">oral</option>
                                        <option value="SC">subcutaneous</option>
                                        <option value="SL">sublingual</option>
                                        <option value="PATCH">patch</option>
                                        <option value="TOP">topical</option>
                                        <option value="INH">inhale</option>
                                        <option value="IM">intra-muscular</option>
                                        <option value="SUPP">rectal</option>
                                        <option value="O.D.">right eye</option>
                                        <option value="O.S.">left eye</option>
                                        <option value="O.U.">both eyes</option>
                                    </select>
                                </div>

                            </div>

                            <div class="form-group">
                                <div class="row">
                                    <div class="col-xs-3"></div>
                                    <div class="col-xs-9">
                                        <label class="checkbox-inline">
                                            <input type="checkbox"
                                                   id="medication-prn"
                                                   ng-model="newMed.prn"> PRN
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox"
                                                   id="medication-lt"
                                                   ng-model="newMed.longTerm"> Longterm Medication
                                        </label>

                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="row">
                                    <div class="col-xs-3"></div>
                                    <div class="col-xs-9">
                                        <label class="checkbox-inline">
                                            <input type="checkbox"
                                                   id="medication-no-subs"
                                                   ng-model="newMed.noSubs"> Dispense brand name / no substitution
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">

                                <div class="row">
                                    <label class="col-xs-3 control-label"
                                           for="medication-additional-instructions">
                                        Additional Instructions
                                    </label>
                                    <div class="col-xs-8">
                                        <textarea style='margin-left:5px;' class="form-control"
                                                  rows="2"
                                                  id="medication-additional-instructions"
                                                  ng-model="newMed.additionalInstructions"></textarea>
                                    </div>

                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-xs-3 control-label"
                                       for="medication-prescriber">Prescriber/Initiator</label>
                                <div class="col-xs-6" style="display:block;" id="medication-prescriber-wrapper">
                                    <select class="form-control"
                                            id="medication-prescriber"
                                            ng-model="newMed.provider"
                                            ng-change="prescriberChange();">
                                        <option value="{{user.providerNo}}" selected="selected">
                                            <b>me ({{user.formattedName}} </b>
                                            <span ng-if="user.providerType">- {{user.providerType}})</span>
                                        </option>

                                        <option ng-repeat="p in providers" value="{{p.provideNo}}">
                                            {{p.formattedName}}
                                            <span ng-if="p.providerType">- {{p.providerType}}</span>
                                        </option>

                                        <option value="$$other$$">Other</option>
                                    </select>
                                </div>
                                <div id="medication-external-prescriber-wrapper"  style="display:none;">
                                    <div class="col-xs-5">
                                        <input type="text" class="form-control"
                                               id="medication-external-prescriber"
                                               ng-model="newMed.externalProvider" />
                                    </div>
                                    <div class="col-xs-1">
                                        <button class="btn btn-default" ng-click="showCurrentProvider();">
                                            <icon class="glyphicon glyphicon-remove"></icon>
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group" id="medication-started-wrap">
                                <label class="col-xs-3 control-label" for="medication-started">Date Started</label>
                                <div class="col-xs-4">
                                    <input type="date"
                                           class="form-control"
                                           id="medication-started"
                                           placeholder="YYYY-MM-DD"
                                           ng-model="newMed.rxDate">
                                </div>
                                <div class="col-xs-4">
                                    <button ng-click="setNewMedStartToday();" class="btn btn-default">
                                        <a href="">
                                            today
                                        </a>
                                    </button>
                                </div>
                            </div>

                            <!--

                            This is commented out b/c a there is no concept of *medication* stop, only a
                            prescription stop in OSCAR's Drug table.

                            <div class="form-group" id="medication-stop-wrap">
                                <label class="col-xs-3 control-label" for="medication-stop">Planned Stop</label>
                                <div class="col-xs-4">
                                    <input type="text" class="form-control"
                                           id="medication-stop"
                                           placeholder="YYYY-MM-DD"
                                            ng-model="newMed.endDate">
                                </div>
                                <div class="col-xs-4">
                                    <ul class="pagination reduced-pagination">
                                        <li class=""><a ng-click="setNewMedStopDate('3m');" href="">3m</a></li>
                                        <li class=""><a ng-click="setNewMedStopDate('6m');" href="">6m</a></li>
                                        <li class=""><a ng-click="setNewMedStopDate('12m');" href="">12m</a></li>
                                    </ul>
                                </div>
                            </div>

                            -->

                            <div class="row" id="medication-error-wrapper" style="display:none;">
                                <div class="col-xs-3"></div>
                                <div class="col-xs-9">
                                    <div class="panel panel-warning">
                                        <div class="panel-heading">Incomplete Medication</div>
                                        <div class="panel-body">
                                            <p>The new medication is incomplete, please ensure you have specified:</p>
                                            <ul>
                                                <li>Medication name</li>
                                                <li>Instructions</li>
                                                <li>Start Date</li>
                                            </ul>
                                            <p>Make adjustments and try again.</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row" id="medication-request-error-wrapper" style="display:none;">
                                <div class="col-xs-3"></div>
                                <div class="col-xs-9">
                                    <div class="panel panel-warning">
                                        <div class="panel-heading">Failed to Add Medication</div>
                                        <div class="panel-body">
                                            <p>
                                                OSCAR was unable to add this medication the patient's record.
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="row">
                                    <div class="col-xs-8"></div>
                                    <div class="col-xs-4">
                                        <button ng-click="addNewMed();"
                                                class="btn btn-success">
                                            Add Medication
                                        </button>
                                        <button ng-click="closeNewMed();"
                                                class="btn btn-danger">
                                            Cancel
                                        </button>
                                    </div>
                                    <div class="col-xs-2"></div>
                                </div>
                            </div>


                        </form>

                        <hr>

                    </div>
                </div>

            </div>

        </div>

        <!-- BEGIN PRESCRIBE SECTION -->

        <div class="row">
            <div class="col-sm-12">
                <h3>Prescribe</h3>
                <hr>

                <div class="" id="prescribe-none-selected-wrapper" style="display:block;">
                    <div class="well well-lg"
                         style="text-align:center">
                        <span class="well-text">
                            No Medications Selected
                        </span>
                    </div>
                </div>

                <div class="" id="prescribe-form-wrapper" style="display:none;">
                    <form class="form-horizontal">

                        <div class="form-group" id="prescribe-duration-wrapper">
                            <label class="col-xs-3 control-label"
                                   for="prescribe-duration">Duration/Amount</label>
                            <div class="col-xs-3">
                                <input type="number"
                                       class="form-control"
                                       id="prescribe-duration"
                                       placeholder=""
                                       ng-model="currentPrescription.duration">
                            </div>
                            <label class="col-xs-1 control-label"
                                   for="prescribe-duration-unit">Units</label>
                            <div class="col-xs-3">
                                <select class="form-control"
                                        id="prescribe-duration-unit"
                                        ng-model="currentPrescription.durationUnit">
                                    <option value="D">days</option>
                                    <option value="W">weeks</option>
                                    <option value="M">months</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-3 control-label" for="prescribe-repeats">Repeats</label>
                            <div class="col-xs-3">
                                <input type="number"
                                       class="form-control"
                                       id="prescribe-repeats"
                                       placeholder=""
                                       ng-init="0"
                                       ng-model="currentPrescription.repeats">
                            </div>
                            <!-- only enable the quantity field one medication is selected for prescribing. -->
                            <div id="prescribe-quantity-wrapper">
                                <label class="col-xs-1 control-label" for="prescribe-quantity">Qty</label>
                                <div class="col-xs-3">
                                    <input type="number"
                                           class="form-control"
                                           id="prescribe-quantity"
                                           placeholder=""
                                           ng-disabled='selectedMedications && selectedMedications.length !== 1'
                                           ng-model="currentPrescription.quantity">
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-3 control-label" for="prescribe-start">Start Date: </label>
                            <div class="col-xs-3">
                                <input type="text"
                                       class="form-control"
                                       id="prescribe-start"
                                       placeholder="yyyy-MM-dd"
                                       ng-model="currentPrescription.start"
                                >
                            </div>

                            <div class="col-xs-3">
                                <p style="margin-top:10px;">
                                    <b>Rx Ends: &nbsp; </b>
                                    <span id="prescribe-end">{{currentPrescription.end | date : 'yyyy-MM-dd'}}</span>
                                </p>
                            </div>
                        </div>
                    </form>

                    <div class="row">
                        <div class="col-xs-6"></div>
                        <div class="col-xs-6">
                            <button ng-click="addFavoriteHandler(selectedMedications, currentPrescription);"
                                    class="btn btn-default"
                                    ng-disabled='selectedMedications && selectedMedications.length !== 1'>
                                Add to Favorites
                            </button>
                            <button ng-click="addPrescription();" class="btn btn-primary">Add to Prescription</button>
                            <button ng-click="clearPrescription();" class="btn btn-danger">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- END PRESCRIBE SECTION -->

        <div class="row">
            <div class="col-sm-12">
                <h3>Ready to Prescribe</h3>
                <hr>

                <div ng-if='readyToPrescribe && readyToPrescribe.length > 0' class="" id="ready-to-prescribe-wrapper" style="display:block;">

                    <div class="row">
                        <ul class="list-group">
                            <li ng-repeat='m in readyToPrescribe' class="list-group-item">
                                <div class="row">
                                    <div class="col-xs-11">

                                        <!--
                                        <span style="font-weight: bold;">{{m.brandName}}</span>
                                        <span ng-if="m.frequency">- {{ m.frequency }} </span>
                                        <span ng-if="m.route"> - {{ m.route }} </span>
                                        <span ng-if="m.prn"> - PRN </span>
                                        <span ng-if="m.duration && m.durationUnit"> - {{m.duration}} {{m.durationUnit}}</span>
                                        <span ng-if="m.quantity"> - Qty: {{m.quantity}}</span>
                                        <span ng-if="m.repeats"> - {{m.repeats}} repeats</span>
                                        -->

                                        <span ng-if="m">{{m.getInstructionLine()}}</span>
                                        <br>
                                        <span style='padding-left:5px;' ng-if="m.additionalInstructions">{{m.additionalInstructions}}</span>
                                    </div>
                                    <div class="col-xs-1">
                                        <a href="" style='color:black;' ng-click="removeFromReadyToPrescribe($index)"><icon class="glyphicon glyphicon-remove-circle pull-right"></icon></a>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>

                    <div class="row">
                        <div class="col-xs-6"></div>
                        <div class="col-xs-6" style="text-align:right;">
                            <button ng-click='saveReadyToPrescribe()' class="btn btn-primary">Save</button>
                            <%
                                ClinicData clinicData = new ClinicData();
                                clinicData.refreshClinicData();
                                String clinic = "<div><p style=\"text-align:left\">"+
                                        StringEscapeUtils.escapeHtml(clinicData.getClinicName())+"<br>"+
                                        StringEscapeUtils.escapeHtml(clinicData.getClinicAddress())+"<br>"+
                                        StringEscapeUtils.escapeHtml(clinicData.getClinicCity())+", "+
                                        StringEscapeUtils.escapeHtml(clinicData.getClinicProvince())+"<br>"+
                                        StringEscapeUtils.escapeHtml(clinicData.getClinicPostal())+"</p></div>"+
                                        "<p style=\"text-align:right\">Tel:"+
                                        StringEscapeUtils.escapeHtml(clinicData.getClinicPhone())+"<br>"+
                                        "Fax:"+StringEscapeUtils.escapeHtml(clinicData.getClinicFax())+"</p></div>";
                            %>
                            <script type="text/javascript" >
                                var clinic = '<%=clinic%>';
                            </script>
                            <button ng-click="saveAndPrintReadyToPrescribe()" class="btn btn-primary">Save and Print</button>
                            <button class="btn btn-danger" ng-click="cancelReadyToPrescribe();">Cancel</button>
                        </div>
                    </div>

                </div>

                <div class="alert alert-danger" id="readyToPrescribeError" style="display:none;margin-top:25px;">
                    <p>An error occured! Unable to complete prescription.</p>
                </div>

                <div ng-if='readyToPrescribe && readyToPrescribe.length <= 0' class="" id="none-ready-to-prescribe-wrapper" style="display:block;">
                    <div class="well well-lg"
                         style="text-align:center">
                        <span class='well-text'>
                            No Medications Ready (please prescribe at least one medication)
                        </span>
                    </div>
                </div>
            </div>
        </div>

    </div>


    <div class="col-sm-3" id="right-side-wrapper" style="margin-top:10px;"> <!-- col-sm-3 wraps -->

        <!-- BEGIN Allergies-->
        <div class="row">
            <legend style="margin-bottom:0;">
                <a href="javascript:void(0)"
                   style="font-size:12px;color:#333;padding-top:10px"
                   class="pull-right"
                   ng-click="openAllergies(demographicNo)"
                >
                    <span class="glyphicon glyphicon-plus-sign" title="allergies"></span>
                </a>
                Allergies
            </legend>
            <ul style="padding-left:12px;">
                <li ng-repeat="item in summaryAllergyList">
                    <span class="pull-right">{{item.date}}</span>
                        <a ng-click="openAllergies(demographicNo)"
                           href="javascript:void(0)"
                           ng-class="item.indicatorClass"
                           popover="{{item.displayName}} {{item.warning}}"
                           popover-trigger="mouseenter">
                            {{item.displayName | limitTo: 132 }} {{item.displayName.length > 132 ? '...' : '' }}
                            <small ng-show="item.classification">({{item.classification}})</small>
                    </a>
                </li>
                <a href="javascript:void(0)"
                   class="text-muted add-summary"
                   ng-if="summaryAllergyList==null"
                   ng-click="openAllergies(demographicNo)">
                        <bean:message key="global.btnAdd"/>
                    Allergies
                </a>
            </ul>
            </ul>
        </div>

        <!-- END Allergies -->

        <!-- BEGIN Favorites list -->

        <div class='row'>
            <legend style="margin-bottom:0;">
                Favorites
            </legend>
            <ul style="padding-left:12px;">
                <li ng-repeat="f in favorites"><a ng-click='prescribeFavorite($index);' href="">{{f.name | limitTo : 120}} {{f.name.length > 120? '...' : '' }}</a></li>
            </ul>
        </div>

        <!-- END favorites list -->

        <!-- BEGIN Disease Registry -->
        <div class="row">
            <legend style="margin-bottom:0;">
                <a href="javascript:void(0)"
                   style="font-size:12px;color:#333;padding-top:10px"
                   class="pull-right"
                   ng-click="openDxResearch(demographicNo)"
                >
                    <span class="glyphicon glyphicon-plus-sign" title="Disease Registry"></span>
                </a>
                Disease Registry
            </legend>
            <ul style="padding-left:12px;">
                <li ng-repeat="item in summaryDxList">
                    <!--<span class="pull-right">{{item.date}}</span>-->
                    <a ng-click="openDxResearch(demographicNo)"
                       href="javascript:void(0)"
                       ng-class="item.indicatorClass"
                       popover="{{item.displayName}} {{item.warning}}"
                       popover-trigger="mouseenter">
                        {{item.displayName | limitTo: 132 }} {{item.displayName.length > 132 ? '...' : '' }}
                        <small ng-show="item.classification">({{item.classification}})</small>
                    </a>
                </li>
                <a href="javascript:void(0)"
                   class="text-muted add-summary"
                   ng-if="summaryDxList==null"
                   ng-click="openDxResearch(demographicNo)">
                    <bean:message key="global.btnAdd"/>
                    Disease Registry
                </a>
            </ul>
        </div>
        <!-- END Disease Registry -->


    </div>
</div>
<!-- END MAIN CONTAINER -->
<!-- MODAL: Custom Drugs -------- -->
<div>
<script type="text/ng-template" id="customDrug.html">
    <div class="modal-header">
        <h3>Custom Drug</h3>
    </div>

    <div class="modal-body">

        <div class="panel panel-danger" id=custom-drug-warning>
            <div class="panel-heading">Custom Drug Warning</div>
            <div class="panel-body">
                <p>This feature allows you to create a custom drug/medication.</p>

                <p><b>Only use this feature if absolutely required!</b></p>

                <p>The following features will be unavailable:</p>
                <ul>
                    <li>Known dosage strengths, forms, and routes.</li>
                    <li>Allergy information/decision support.</li>
                    <li>Drug-drug interaction information/decision support.</li>
                    <li>General drug information.</li>
                </ul>

                <p>Custom drugs and instructions have been found to cause data inconsistencies in health information systems.</p>
            </div>
        </div>

        <form class="form-horizontal">

            <div class="form-group">
                <label class="col-xs-3 control-label" for="custom-drug-name">Custom Name</label>
                <div class="col-xs-9">
                    <input type="text" class="form-control" ng-model="customDrug.name" id="custom-drug-name"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-xs-3 control-label" for="custom-drug-strength">Strength</label>
                <div class="col-xs-4">
                    <input type="number" class="form-control" ng-model="customDrug.strength" id="custom-drug-strength"/>
                </div>

                <label class="col-xs-1 control-label" for="custom-drug-units">Units</label>
                <div class="col-xs-4">
                    <input type="text" class="form-control" ng-model="customDrug.strengthUnit" id="custom-drug-units"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-xs-3 control-label" for="custom-drug-form">Form</label>
                <div class="col-xs-4">
                    <input type="text" class="form-control" ng-model="customDrug.form" id="custom-drug-form"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-xs-3 control-label" for="custom-drug-form">Note</label>
                <div class="col-xs-9">
                    <textarea rows="2" class="form-control" ng-model="customDrug.note" id="custom-drug-note"/>
                </div>
            </div>

        </form>
    </div>

    <div class="modal-footer">
        <div clas="row">
            <div class="col-xs-2">
                <button ng-click="modalAccept();" class="btn btn-success">Accept</button>
            </div>

            <div class="col-xs-8"></div>

            <div class="col-xs-2">
                <button ng-click="modalCancel();" class="btn btn-danger">Cancel</button>
            </div>

        </div>

    </div>
</script>
</div>
<!-- END MODAL: Custom Drugs ------ -->

<!-- MODAL: Favorites Drugs -------- -->
<div>
    <script type="text/ng-template" id="addFavorite.html">
        <div class="modal-header">
            <h3>Add Favorite</h3>
        </div>

        <div class="modal-body">

            <form class="form-horizontal">

                <div class="form-group">
                    <label class="col-xs-3 control-label" for="new-favorite-name">Favorite Name: </label>
                    <div class="col-xs-9">
                        <input type="text" class="form-control" ng-model="newFavorite.name" id="new-favorite-name"/>
                    </div>
                </div>

            </form>

            <div class="panel panel-default" id="">
                <div class='panel-body'>
                    <p> {{newFavorite.drug.getInstructionLine()}}</p>
                </div>
            </div>

            <div class="panel panel-danger" id="add-favorite-error" style="display:none;">
                <div class='panel-body'>
                    <p id="add-favorite-error-msg">Failed to add favorite!</p>
                </div>
            </div>

        </div>

        <div class="modal-footer">
            <div clas="row">
                <div class="col-xs-2">
                    <button ng-click="modalAccept();" class="btn btn-success">Add Favorite</button>
                </div>

                <div class="col-xs-8"></div>

                <div class="col-xs-2">
                    <button ng-click="modalCancel();" class="btn btn-danger">Cancel</button>
                </div>

            </div>

        </div>
    </script>
</div>
<!-- END MODAL: Add Favorite  ------ -->

<!-- MODAL: More Drug Suggestions -->
<div>

    <script type="text/ng-template" id="myModalContent.html">

        <div class="modal-header">
            <h3>More Suggestions</h3>
        </div>

        <div class="modal-body">

            <form class="form-horizontal">

                <div class="form-group">
                    <label class="col-xs-2 control-label" for="searchModalInput">Medication</label>
                    <div class="col-xs-9">
                        <input type="text" class="form-control" ng-model="extendedMedicationSearch"
                               id="searchModalInput" ng-change="updateSearchModalDrugs(extendedMedicationSearch);"/>
                    </div>
                </div>
            </form>

            <div class="row">
                <div class="col-xs-8">
                    <h4>Results:</h4>
                </div>
                <div class="col-xs-4">
                    <form class="">
                        <div style="margin-top:10px;">
                            <label class="checkbox-inline">
                                <input type="checkbox"
                                       ng-model='modalShowInactive'
                                       id="modal-show-inactive"> Show Inactive Drugs
                            </label>
                        </div>
                    </form>
                </div>
            </div>
            <hr>
            <div class="row">
                <div class="" style="margin-left:15px;">
                    <div class="col-xs-12" style="overflow:auto; height:300px;">

                        <table class="table table-hover">
                            <tbody>
                            <tr ng-repeat="m in lookUpResult"
                                ng-click="modalSelectMed($index);">
                                <td ng-if="m.active || modalShowInactive"
                                    ng-class="{'rx-med-inactive': !m.active, 'rx-med-selected':m.selected}">
                                    {{ m.name }}
                                    <span ng-if="!m.active"> (inactive) </span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>

        <div class="modal-footer">

            <div clas="row">
                <div class="col-xs-2">
                    <button ng-click="modalAccept();" class="btn btn-success">Accept</button>
                </div>

                <div class="col-xs-8"></div>

                <div class="col-xs-2">
                    <button ng-click="modalCancel();" class="btn btn-danger">Cancel</button>
                </div>


            </div>

        </div>

    </script>

</div>

<!-- END Drug Search MODAL -->

<script type="text/javascript">

    // TODO: Break this out into a seperate file...

    $('.collapse').collapse();

    function toggleAddMedication() {
        $("#add-medication-wrapper").toggle();
        $("#medication-search").focus();
        $("#add-medication-button-wrapper").toggle();
        var scope = angular.element($("#medication-search")).scope();
        scope.resetNewMedication();
    }

    function drugSearchOff(x){
        console.log(x);
        $('#medication-search').typeahead('destroy');
    }


    function setSearchValue(v){
        $("medication-search").typeahead('setQuery', v);
    }

    function drugSearchOn(x) {

        $('#medication-search').typeahead({
            // docs for typehead:
            //  https://github.com/twitter/typeahead.js/blob/master/doc/jquery_typeahead.md
            //  Version 9.3
            name: 'drugs',
            valueKey: 'name',
            limit: 8,

            remote: {
                url: '../ws/rs/rxlookup/search?string=%QUERY',
                cache: false,
                filter: function (parsedResponse) {
                    if (parsedResponse.success) {

                        if (parsedResponse['drugs'] instanceof Array) {
                            return parsedResponse['drugs'].filter(function (d) {
                                return d.active;
                            });
                        }
                    }
                    return [];
                }
            },

            template: function (obj) {
                // Template function for how items will appear
                // in the dropdown.
                // obj has structure: { active : boolean, category : number, id: number, name : string }
                var s = obj.name;
                var c = "";
                if (!obj.active) {
                    s += " <span class='rx-med-inactive-label'>(inactive)</span>";
                    c = "rx-med-inactive";
                }
                return "<p class='demo-quick-name " + c + "'>" + s + "</p>"

            },

            footer: "<a onclick='moreDrugChoices();'><div class='rx-search-footer'><p class='demo-quick-name rx-search-footer-text'>More Suggestions</p></div></a>"

        }).on('typeahead:selected', function (event, datum) {
            // datum is the item picked
            // event is the event generated by the selection.
            var scope = angular.element($("#medication-search")).scope();
            drugSearchOff();
            scope.selectNewMed(datum);
        });

        setSearchValue("");
    }

    function moreDrugChoices() {
        // get the scope and forward the request to have the scope open a modal.
        var scope = angular.element($("#medication-search")).scope();
        scope.moreDrugChoices();
    }

    drugSearchOn();
</script>
