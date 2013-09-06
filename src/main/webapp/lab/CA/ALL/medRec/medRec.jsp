<!--<%--

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

--%>-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.oscarehr.medextract.*"%>
<%@page import="org.oscarehr.medextract.MedicationExtractor.DrugPair"%>
<%@ page import="java.util.ArrayList" %>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@ page import="oscar.oscarRx.data.RxPrescriptionData" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
    oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
    MedicationExtractor extractor = (MedicationExtractor)session.getAttribute("medExtract");
    String demographicID = request.getParameter("demoid");

    //atcCode=B01AA03&atcCode=B01AA03&drugName_930546=COUMADIN%20TAB%2010MG&instructions_930546=&quantity_930546=0&repeats_930546=0&refillDuration_930546=0&refillQuantity_930546=0&dispenseInterval_930546=0&outsideProviderName_930546=&outsideProviderOhip_930546=&rxDate_930546=2013-08-28&lastRefillDate_930546=&writtenDate_930546=2013-08-28&pickupDate_930546=&pickupTime_930546=&comment_930546=&eTreatmentType_930546=--&rxStatus_930546=--&drugName_343798=TARO-WARFARIN%202MG&instructions_343798=&quantity_343798=0&repeats_343798=0&refillDuration_343798=0&refillQuantity_343798=0&dispenseInterval_343798=0&outsideProviderName_343798=&outsideProviderOhip_343798=&rxDate_343798=2013-08-28&lastRefillDate_343798=&writtenDate_343798=2013-08-28&pickupDate_343798=&pickupTime_343798=&comment_343798=&eTreatmentType_343798=--&rxStatus_343798=--&demographicNo=5&searchString=&search=Search

%>

<html>
    <head>
        <title>MedRec</title>
        <link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.css">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script type="text/javascript" src="bootstrap/js/jquery-1.8.3.js"></script>
        <script src="http://yui.yahooapis.com/3.11.0/build/yui/yui-min.js"></script>
        <script type="text/javascript" src="bootstrap/js/bootstrap.js"></script>
        <script type="text/javascript" src="medRec.js"></script>
        <link rel="stylesheet" type="text/css" href="/oscar_master/share/yui/css/fonts-min.css" >
        <link rel="stylesheet" type="text/css" href="autocomplete_custom.css" >
        <script type="text/javascript" src="/oscar_master/share/yui/js/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="/oscar_master/share/yui/js/connection-min.js"></script>
        <script type="text/javascript" src="/oscar_master/share/yui/js/animation-min.js"></script>
        <script type="text/javascript" src="/oscar_master/share/yui/js/datasource-min.js"></script>
        <script type="text/javascript" src="/oscar_master/share/yui/js/autocomplete-min.js"></script>
        <style type='text/css'>
            .well-bordered {
                border: 1px solid black;    
            }
            .btn-wrapper{
                width:50px;
            }
            .hidden {
                display:none !important;
            }
            #myAutoComplete { 
                width:15em; /* set width here or else widget will expand to fit its container */ 
                padding-bottom:2em; 
            }
        </style>
    </head>
    <body style='background-color:#e5e5e5;'>
        </div>

        <div class="navbar">
            <div class="navbar-inner">
                <a class="brand" href="#">MedRec</a>
                <ul class="nav">
                    <li><a href="#">HART, Gordon</a></li>
                </ul>
                <ul class='pull-right nav'>
                    <li><button class='btn btn-primary' data-toggle='modal' href='#medicationModal'onclick='pm.clearModalFields();'>Add New Medication</button></li>
                </ul>
          </div>
        </div>
        <div style=''class="container visible-phone visible-tablet visible-desktop">
            <div class='row'>
                <div class='span6'>
                    <div class='well well-bordered'>
                        <h5>Suggested Medications</h5>
                        <table class='table table-striped' id='suggestedMeds'>
                            <tbody>
                            </tbody>
                    </table>
                    </div>
                </div>
                <div class='span6'>
                    <div class='well well-bordered'>
                        <h5>Current Medications in OSCAR</h5>
                        <table class='table table-striped' id='currentMeds'>
                            <tbody>
                            </tbody>
                    </table>
                    </div>
                </div>
            </div>
            <div class='row'>
                <div class='span12' style=''>
                    <div class='well well-bordered'>
                        <h5>Your Changes</h5>
                        <table class='table table-striped' id='changes'>
                            <tbody>
                            </tbody>
                    </table>
                    <a href='#' class='btn btn-primary' onclick="pm.signAndSave()">Sign and Save</a>
                    <a href='#' class='btn btn-danger'>Cancel</a>
                    </div>
                </div>
            </div>
        </div>
        <div id="medicationModal" class="modal fade hide">
          <div class="modal-header">
            <h4 id="modal-label"><span id='med-modal-string'></span></h4>
          </div>
          <div class="modal-body">
            <form class='form-horizontal'>
                    <div class='control-group'>
                        <label class='control-label' for="query">Product:</label>
                        <div id="controls myAutoComplete"> 
                            <input type="text" class='input-xlarge' style='margin-left:20px;'id="brandname" value=""/>
                            <div id="myContainer"></div> 
                        </div> 
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="genericname">Ingredient(s):</label>
                        <div class="controls">
                            <input type="text" class='input-xlarge' name="genericname" id="genericname" value=""/>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="form">Form:</label>
                        <div class="controls">
                            <select class='input-medium' id='form'>
                                <option value=''>form<option>
                                <option value='tablet'>tablet</option>
                                <option value='capsule'>capsule</option>
                                <option value='liquid'>liquid</option>
                                <option value='suspension'>suspension</option>
                                <option value='spray'>spray</option>
                                <option value='drops'>drops</option>
                                <option value='other'>other</option>
                            </select>
                            <input type="text" class='input-medium hidden' name="" id="form-other" value=""/>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="route">Route:</label>
                        <div class="controls">
                            <select class='input-medium' id='route'>
                                <option value=''>route<option>
                                <option value='oral'>oral</option>
                                <option value='subcutaneous'>subcutaneous</option>
                                <option value='inhale'>inhale</option>
                                <option value='patch'>patch</option>
                                <option value='transdermal'>transdermal</option>
                                <option value='sublingual'>sublingual</option>
                                <option value='intravitreal'>intravitreal</option>
                                <option value='topical'>topical</option>
                                <option value='left eye'>left eye</option>
                                <option value='right eye'>right eye</option>
                                <option value='both eyes'>both eye</option>
                                <option value='other'>other</option>
                            </select>
                            <input type="text" class='input-medium hidden' name="" id="route-other" value=""/>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="freq1">Frequency:</label>
                        <div class="controls">
                            <select class='input-medium' id="freq1" onblur="pm.autoCalculateModalFields();">
                                <option value=''>frequency<option>
                                <option value='daily'>daily</option>
                                <option value='twice daily'>twice daily</option>
                                <option value='three times daily'>three times daily</option>
                                <option value='four times daily'>four times daily</option>
                                <option value='every 4 hours'>every 4 hours</option>
                                <option value='every 8 hours'>every 8 hours</option>
                                <option value='every 12 hours'>every 12 hours</option>
                                <option value='once weekly'>once weekly</option>
                                <option value='twice weekly'>twice weekly</option>
                                <option value='three times weekly'>three times weekly</option>
                                <option value='four times weekly'>four times weekly</option>
                                <option value='once monthly'>once monthly</option>
                                <option value='twice monthly'>twice monthly</option>
                                <option value='once'>once</option>
                                <option value='as needed'>as needed</option>
                                <option value='other'>other</option>
                            </select>
                            <input type="text" class='input-medium hidden' name="" id="freq1-other" value=""onblur="pm.autoCalculateModalFields();"/>
                            <span style='margin-left:10px;'>PRN: </span>
                            <input type="checkbox" class='' name="" id="prn" value=""/>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="min">Min/Max:</label>
                        <div class="controls">
                            <input type="number" class='input-mini' name="" id="min" placeholder='min'value=""onblur="pm.autoCalculateModalFields();"/>
                            <input type="number" class='input-mini' name="" id="max" placeholder='max'value=""onblur="pm.autoCalculateModalFields();"/>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="duration">Duration:</label>
                        <div class="controls">
                            <select class='input-mini' id="duration"onblur="pm.autoCalculateModalFields();">
                                <option value=''>no.</option>
                                <option value='1'>1</option>
                                <option value='2'>2</option>
                                <option value='3'>3</option>
                                <option value='4'>4</option>
                                <option value='5'>5</option>
                                <option value='6'>6</option>
                                <option value='7'>7</option>
                                <option value='8'>8</option>
                                <option value='9'>9</option>
                                <option value='10'>10</option>
                                <option value='12'>12</option>
                                <option value='24'>24</option>
                                <option value='other'>other</option>
                            </select>
                            <input type="text" class='input-mini hidden' name="" id="duration-other" value=""onblur="pm.autoCalculateModalFields();"/>
                            <select class='input-medium' id="duration-unit"onblur="pm.autoCalculateModalFields();">
                                <option value=''>units</option>
                                <option value='days'>day(s)</option>
                                <option value='weeks'>week(s)</option>
                                <option value='months'>month(s)</option>
                                <option value='years'>year(s)</option>
                                <option value='other'>other</option>
                            </select>
                            <input type="text" class='input-medium hidden' name="" id="duration-unit-other" value=""onblur="pm.autoCalculateModalFields();"/>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="quantity">Quantity:</label>
                        <div class="controls">
                            <input type="number" class='input-mini' name="" id="quantity" value=""/>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="repeats">Repeats:</label>
                        <div class="controls">
                            <input type="number" class='input-mini' name="" id="repeats" value=""/>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="dose">Dose:</label>
                        <div class="controls">
                            <input disabled type="number" class='input-mini' name="dose" id="dose" maxlength="6" size="6"value=""/>
                            <select class='input-mini' id='unit' disabled>
                                <option value="">units</option>
                                <option value="mg">mg</option>
                                <option value='mcg'>mcg</option>
                                <option value='mL'>mL</option>
                                <option value='mEq'>mEq</option>
                                <option value='drops'>drops</option>
                                <option value='other'>other</option>
                            </select>
                            <input type="text" class='input-mini hidden' name="" id="unit-other" value=""/>
                        </div>
                    </div>
                    <div class='control-group'>
                        <div class='controls'>
                          <label class="checkbox">
                            Long Term Medication <input type="checkbox" name="longterm" id="longterm" value=''>
                          </label>
                          <label class="checkbox">
                            External Provider <input type="checkbox" name="externalprovider" id="externalprovider" value=''>
                          </label>
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="start-date">Start Date:</label>
                        <div class="controls">
                           <input type='text' id='start-date' class='input-medium' placeholder='yyyy-mm-dd'/> 
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="written-date">Written Date:</label>
                        <div class="controls">
                           <input type='text' id='written-date' class='input-medium' placeholder='yyyy-mm-dd'/> 
                        </div>
                    </div>
                    <div class='control-group'>
                        <label class='control-label' for="instructions">Other Instructions:</label>
                        <div class="controls">
                           <textarea id='instructions' rows="3"></textarea> 
                        </div>
                    </div>
            </form>
          </div>
          <div class="modal-footer">
            <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
            <button id="modal-discontinue-button" data-dismiss="modal" class="btn btn-danger">Discontinue Medication</button>
            <button id="modal-save-button" data-dismiss="modal" class="btn btn-success">Submit changes to OSCAR</button>
          </div>
        </div>

        <! -- Hidden Medication information Forms -->
        <!--
            <form id='med-summary_7'>
                <input type='hidden' id='brandname' value="Amoxil">
                <input type='hidden' id='ingredientname' value="Amoxicillin">
                <input type='hidden' id='dose' value="100">
                <input type='hidden' id='unit' value="mg">
                <input type='hidden' id='route' value="oral">
                <input type='hidden' id='form' value="tablet">
                <input type='hidden' id='freq1' value="twice daily">
                <input type='hidden' id='prn' value="">
                <input type='hidden' class="non-descript" id='externalprovider' value="true">
                <input type='hidden' id='duration-unit' value="">
                <input type='hidden' class="non-descript"id='longterm' value="">
                <input type='hidden' class="non-descript"id='instructions' value="Take with meals">
                <input type="hidden" id='associatedmed' value=""> <!-- Notes if this value is null then the associated med becomes ------------
                <input type='hidden' class="non-descript"id='state' value="current"> <!-- States what kind of medication it is.  Either, "new" or "current"
            </form>-->
     <div id='medForms' class="hidden-desktop">
    <%
        int count = 0;
        ArrayList<DrugPair> pairs = extractor.getPairs();
        for(int i=0; i<pairs.size(); i++){
            org.oscarehr.medextract.MedicationExtractor.DrugPair p = pairs.get(i);
            if(p.getCurrentDrug() != null && p.getNewDrug() != null){
               //the case where we have a linked medication.
                out.println("<form id='med-summary_"+Integer.toString(count)+"'>");
                count ++;
                out.println("<input type='hidden' id='state' value='current'>");
                if(p.getCurrentDrug().getBrandName() != null){
                    out.println("<input type='hidden' id='brandname' value='"+p.getCurrentDrug().getBrandName()+"'>");
                }
                if(p.getCurrentDrug().getAtc() != null){
                    out.println("<input type='hidden' id='atc' value='"+p.getCurrentDrug().getAtc()+"'>");
                }
                if(p.getCurrentDrug().getRegionalIdentifier() != null){
                    out.println("<input type='hidden' id='din' value='"+p.getCurrentDrug().getRegionalIdentifier()+"'>");
                }
                if(p.getCurrentDrug().getGenericName() != null){
                    out.println("<input type='hidden' id='ingredientname' value='"+p.getCurrentDrug().getGenericName()+"'>");
                }
                if(p.getCurrentDrug().getDosage() != null){
                    out.println("<input type='hidden' id='dose' value='"+p.getCurrentDrug().getDosage()+"'>");
                }
                if(p.getCurrentDrug().getRoute() != null){
                    out.println("<input type='hidden' id='route' value='"+p.getCurrentDrug().getRoute()+"'>");
                }
                if(p.getCurrentDrug().getDrugForm() != null){
                    out.println("<input type='hidden' id='form' value='"+p.getCurrentDrug().getDrugForm()+"'>");
                }
                if(p.getCurrentDrug().getFreqCode() != null){
                    out.println("<input type='hidden' id='freq1' value='"+p.getCurrentDrug().getFreqCode()+"'>");
                }
                if(p.getCurrentDrug().getOutsideProviderName() != null){
                    out.println("<input type='hidden' id='externalprovider' value='true'>");
                }else{
                    out.println("<input type='hidden' id='externalprovider' value='false'>");
                }
                if(p.getCurrentDrug().getLongTerm()){
                    out.println("<input type='hidden' id='longterm' value='true'>");
                }else{
                    out.println("<input type='hidden' id='longterm' value='false'>");
                }
                if(p.getCurrentDrug().getDuration() != null){
                    out.println("<input type='hidden' id='duration' value='"+p.getCurrentDrug().getDuration()+"'>");
                }
                if(p.getCurrentDrug().getDurUnit() != null){
                    out.println("<input type='hidden' id='duration-unit' value='"+p.getCurrentDrug().getDurUnit()+"'>");
                }
                if(p.getCurrentDrug().getSpecialInstruction() != null){
                    out.println("<input type='hidden' id='instructions' value='"+p.getCurrentDrug().getSpecialInstruction()+"'>");
                }
                if(p.getCurrentDrug().getTakeMax() != 0){
                    out.println("<input type='hidden' id='max' value='"+p.getCurrentDrug().getTakeMax()+"'>");
                }
                if(p.getCurrentDrug().getTakeMin() != 0){
                    out.println("<input type='hidden' id='min' value='"+p.getCurrentDrug().getTakeMin()+"'>");
                }
                if(p.getCurrentDrug().getWrittenDate() != null){
                    out.println("<input type='hidden' id='writtendate' value='"+p.getCurrentDrug().getWrittenDate().toString()+"'>");
                    out.println("<input type='hidden' id='startdate' value='"+p.getCurrentDrug().getWrittenDate().toString()+"'>");
                }
                out.println("<input type='hidden' id='associatedmed' value='"+Integer.toString(count)+"'>");
                if(p.getCurrentDrug().getId() != null){
                    out.println("<input type='hidden' id='drugid' value='"+p.getCurrentDrug().getId()+"'>");
                }
                out.println("</form>");

                out.println("<form id='med-summary_"+Integer.toString(count)+"'>");
                count ++;
                out.println("<input type='hidden' id='state' value='new'>");
                if(p.getNewDrug().get("name") != null){
                    out.println("<input type='hidden' id='brandname' value='"+p.getNewDrug().get("name")+"'>");
                }
                if(p.getNewDrug().get("din") != null){
                    out.println("<input type='hidden' id='din' value='"+p.getNewDrug().get("din")+"'>");
                }
                if(p.getNewDrug().get("ingredient") != null){
                    out.println("<input type='hidden' id='ingredientname' value='"+p.getNewDrug().get("ingredient")+"'>");
                }
                if(p.getNewDrug().get("dose") != null){
                    out.println("<input type='hidden' id='dose' value='"+p.getNewDrug().get("dose")+"'>");
                }
                if(p.getNewDrug().get("route") != null){
                    out.println("<input type='hidden' id='route' value='"+p.getNewDrug().get("route")+"'>");
                }
                if(p.getNewDrug().get("form") != null){
                    out.println("<input type='hidden' id='form' value='"+p.getNewDrug().get("form")+"'>");
                }
                if(p.getNewDrug().get("freq") != null){
                    out.println("<input type='hidden' id='freq1' value='"+p.getNewDrug().get("freq")+"'>");
                }
                out.println("<input type='hidden' id='externalprovider' value='false'>");
                out.println("<input type='hidden' id='longterm' value='false'>");
                if(p.getNewDrug().get("duration") != null){
                    out.println("<input type='hidden' id='duration' value='"+p.getNewDrug().get("duration")+"'>");
                }
                /*if(p.getNewDrug().getDurUnit() != null){
                    out.println("<input type='hidden' id='duration-unit' value='"+p.getNewDrug().getDurUnit()+"'>");
                }*/
                /*if(p.getNewDrug().getSpecialInstruction() != null){
                    out.println("<input type='hidden' id='"+p.getNewDrug().getSpecialInstruction()+"' value='true'>");
                }*/
                out.println("<input type='hidden' id='associatedmed' value='"+Integer.toString(count-1)+"'>");
                out.println("</form>");
                i++;
            }else if(p.getCurrentDrug() != null){
                out.println("<form id='med-summary_"+Integer.toString(count)+"'>");
                count ++;
                out.println("<input type='hidden' id='state' value='current'>");
                if(p.getCurrentDrug().getBrandName() != null){
                    out.println("<input type='hidden' id='brandname' value='"+p.getCurrentDrug().getBrandName()+"'>");
                }
                if(p.getCurrentDrug().getAtc() != null){
                    out.println("<input type='hidden' id='atc' value='"+p.getCurrentDrug().getAtc()+"'>");
                }
                if(p.getCurrentDrug().getRegionalIdentifier() != null){
                    out.println("<input type='hidden' id='din' value='"+p.getCurrentDrug().getRegionalIdentifier()+"'>");
                }
                if(p.getCurrentDrug().getGenericName() != null){
                    out.println("<input type='hidden' id='ingredientname' value='"+p.getCurrentDrug().getGenericName()+"'>");
                }
                if(p.getCurrentDrug().getDosage() != null){
                    out.println("<input type='hidden' id='dose' value='"+p.getCurrentDrug().getDosage()+"'>");
                }
                if(p.getCurrentDrug().getRoute() != null){
                    out.println("<input type='hidden' id='route' value='"+p.getCurrentDrug().getRoute()+"'>");
                }
                if(p.getCurrentDrug().getDrugForm() != null){
                    out.println("<input type='hidden' id='form' value='"+p.getCurrentDrug().getDrugForm()+"'>");
                }
                if(p.getCurrentDrug().getFreqCode() != null){
                    out.println("<input type='hidden' id='freq1' value='"+p.getCurrentDrug().getFreqCode()+"'>");
                }
                if(p.getCurrentDrug().getOutsideProviderName() != null){
                    out.println("<input type='hidden' id='externalprovider' value='true'>");
                }else{
                    out.println("<input type='hidden' id='externalprovider' value='false'>");
                }
                if(p.getCurrentDrug().getWrittenDate() != null){
                    out.println("<input type='hidden' id='writtendate' value='"+p.getCurrentDrug().getWrittenDate().toString()+"'>");
                    out.println("<input type='hidden' id='startdate' value='"+p.getCurrentDrug().getWrittenDate().toString()+"'>");
                }
                if(p.getCurrentDrug().getLongTerm()){
                    out.println("<input type='hidden' id='longterm' value='true'>");
                }else{
                    out.println("<input type='hidden' id='longterm' value='false'>");
                }
                if(p.getCurrentDrug().getDuration() != null){
                    out.println("<input type='hidden' id='duration' value='"+p.getCurrentDrug().getDuration()+"'>");
                }
                if(p.getCurrentDrug().getDurUnit() != null){
                    out.println("<input type='hidden' id='duration-unit' value='"+p.getCurrentDrug().getDurUnit()+"'>");
                }
                if(p.getCurrentDrug().getSpecialInstruction() != null){
                    out.println("<input type='hidden' id='"+p.getCurrentDrug().getSpecialInstruction()+"' value='true'>");
                }
                if(p.getCurrentDrug().getTakeMax() != 0){
                    out.println("<input type='hidden' id='max' value='"+p.getCurrentDrug().getTakeMax()+"'>");
                }
                if(p.getCurrentDrug().getTakeMin() != 0){
                    out.println("<input type='hidden' id='min' value='"+p.getCurrentDrug().getTakeMin()+"'>");
                }
                if(p.getCurrentDrug().getId() != null){
                    out.println("<input type='hidden' id='drugid' value='"+p.getCurrentDrug().getId()+"'>");
                }
                //out.println("<input type='hidden' id='associatedmed' value='"+Integer.toString(i+1)+"'>");
                out.println("</form>");
            }else if(p.getNewDrug() != null){
                out.println("<form id='med-summary_"+Integer.toString(count)+"'>");
                count++;
                out.println("<input type='hidden' id='state' value='new'>");
                if(p.getNewDrug().get("name") != null){
                    out.println("<input type='hidden' id='brandname' value='"+p.getNewDrug().get("name")+"'>");
                }
                if(p.getNewDrug().get("atc") != null){
                    out.println("<input type='hidden' id='atc' value='"+p.getNewDrug().get("atc")+"'>");
                }
                if(p.getNewDrug().get("din") != null){
                    out.println("<input type='hidden' id='din' value='"+p.getNewDrug().get("din")+"'>");
                }
                if(p.getNewDrug().get("ingredient") != null){
                    out.println("<input type='hidden' id='ingredientname' value='"+p.getNewDrug().get("ingredient")+"'>");
                }
                if(p.getNewDrug().get("dose") != null){
                    out.println("<input type='hidden' id='dose' value='"+p.getNewDrug().get("dose")+"'>");
                }
                if(p.getNewDrug().get("route") != null){
                    out.println("<input type='hidden' id='route' value='"+p.getNewDrug().get("route")+"'>");
                }
                if(p.getNewDrug().get("form") != null){
                    out.println("<input type='hidden' id='form' value='"+p.getNewDrug().get("form")+"'>");
                }
                if(p.getNewDrug().get("freq") != null){
                    out.println("<input type='hidden' id='freq1' value='"+p.getNewDrug().get("freq")+"'>");
                }
                out.println("<input type='hidden' id='externalprovider' value='false'>");
                out.println("<input type='hidden' id='longterm' value='false'>");
                if(p.getNewDrug().get("duration") != null){
                    out.println("<input type='hidden' id='duration' value='"+p.getNewDrug().get("duration")+"'>");
                }
                /*if(p.getNewDrug().getDurUnit() != null){
                    out.println("<input type='hidden' id='duration-unit' value='"+p.getNewDrug().getDurUnit()+"'>");
                }*/
                /*if(p.getNewDrug().getSpecialInstruction() != null){
                    out.println("<input type='hidden' id='"+p.getNewDrug().getSpecialInstruction()+"' value='true'>");
                }*/
                //out.println("<input type='hidden' id='associatedmed' value='"+Integer.toString(i+1)+"'>");
                out.println("</form>");
            }else{
                //neither....
            }
        }
    %>
    </div> <!-- END MED FORMS -->
    </body>
    <script type="text/javascript">
        var pm = initPageManager();
        loadContent(pm);
        $(window).resize(function(){
            pm.resizeTableRows();
        });
        pm.demographic_id = <%out.print(demographicID);%>
    </script>
    <script type="text/javascript">
    //http://developer.yahoo.com/yui/examples/autocomplete/ac_basic_xhr.html see this link for details on implementation.
    YAHOO.example.BasicRemote = function(){
        var url = "/oscar_master" + "/oscarRx/searchDrug.do?method=jsonSearch";
        var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ingoreStaleResponse'});
        oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;// Set the responseType
        // Define the schema of the delimited results
        oDS.responseSchema = {
            resultsList : "results",
            fields : ["name", "id","isInactive"]
        };
        // Enable caching
        oDS.maxCacheEntries=20;
        oDS.connXhrMode ="cancelStaleRequests";
        // Instantiate AutoComplete
        var oAC = new YAHOO.widget.AutoComplete("brandname", "myContainer", oDS);
        oAC.useShadow = true;
        oAC.autoHighlight = true;
        oAC.resultTypeList = false;
        oAC.queryMatchSubset = true;
        oAC.minQueryLength = 1;
        oAC.maxResultsDisplayed = 15;
        pm.oac = oAC;
       }();
    </script>        
</html>
