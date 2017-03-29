/*

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

 */

oscarApp.controller('RxCtrl', function ($scope, $http, $resource, $location,
                                         $modal, demographicService, securityService,
                                         summaryService, $stateParams, rxService, demo, user, providerService) {
    console.log("in rx Ctrl ", $stateParams);

    // Other providers, array of provider objects from providerService.
    $scope.providers = [];

    // Current Medications
    $scope.currentMedications = [];
    
    // for comparing against current date.
    $scope.today = new Date(); 

    // Medications the user has selected
    // to prescribe, this array contains the their drug id's.
    // Should be a subset of the drug id's of the $scope.currentMedications
    // array.
    $scope.selectedMedications = [];

    // Medications the user has entered prescription
    // info for and just require them to verify.
    $scope.readyToPrescribe = [];

    // Contains data from the current prescription (not drug) input elements
    $scope.currentPrescription = {};

    // List of allergies from the summaryService
    $scope.gotAllergyData = 0;
    $scope.summaryAllergyList = fillAllergyItems();

    // List of diseases from the summaryService
    $scope.gotDiseasesData = 0;
    $scope.summaryDxList = fillDxItems();

    // Providers favorite meds
    $scope.favorites = [];
    
    // Add Favorites
    $scope.newFavorite = null;

    $scope.user = user;
    $scope.demo = demo;

    $scope.formatDate = function(d){
        return d.date();
    };

    $scope.updateCurrentMedications = function(){

        if(!demo.demographicNo) {
            console.log(demo.demographicNo + " is invalid demographicNo");
            return; 
        }

        rxService.getMedications(demo.demographicNo, "current").then(function(objs){
            
            // objs has structure: 
            //  [ drug, drug, ... ] or drug

            $scope.currentMedications = [];
            $scope.selectedMedications = [];

            $scope.clearPrescription();
            
            var temp;

            if(objs instanceof Array && objs.length >= 1){

                for(var i = 0; i < objs.length; i++){

                    temp = new Drug();
                    temp.fromDrugTransferObject(objs[i]);
                    $scope.currentMedications.push(temp);

                }
                document.getElementById('no-current-meds-panel').style.display= 'none';

            }else if(objs instanceof Array && objs.length === 0){

                document.getElementById('no-current-meds-panel').style.display= 'block';
                toggleAddMedication(); //show new medication form by default.

            }else if (objs instanceof Object){

                temp = new Drug();
                temp.fromDrugTransferObject(objs);
                $scope.currentMedications.push(temp);
                document.getElementById('no-current-meds-panel').style.display= 'none';

            }else{
                // no drugs, show panel
                $('#no-current-meds-panel').show();
                document.getElementById('no-current-meds-panel').style.display= 'block';
                toggleAddMedication(); //show new medication form by default.

            }

            console.log($scope.currentMedications);
            $("body").scrollTop(0);

        });

    };

    /**
     * Tracks which medications have been selected from
     * the current med list for prescribing.
     *
     * @param id {number} - the drug id of the medication that was just clicked.
     */
    $scope.toggleSelectedMed = function(id){
        var i = $scope.selectedMedications.indexOf(id);

        if( i === -1){
            $scope.selectedMedications.push(id);
        } else{
            $scope.selectedMedications.splice(i, 1);
        }

        if($scope.selectedMedications.length > 0){
            $scope.showPrescribeForm();
        }else{
            $scope.hidePrescribeForm();
        }
    };
    
    $scope.clearPrescribeFieldErrors = function(){
        $("#prescribe-duration").removeClass('field-has-error');
        $("#prescribe-duration-unit").removeClass('field-has-error');
        $("#prescribe-repeats").removeClass('field-has-error');
        $("#prescribe-quantity").removeClass('field-has-error');
        $("#prescribe-start").removeClass('field-has-error');
    };

    $scope.showPrescribeForm = function(){
        $scope.clearPrescribeFieldErrors(); 
        $("#prescribe-none-selected-wrapper").hide();
        $("#prescribe-form-wrapper").show();
    };

    $scope.hidePrescribeForm = function(){
        $scope.clearPrescribeFieldErrors();
        $("#prescribe-none-selected-wrapper").show();
        $("#prescribe-form-wrapper").hide();
    };

    /**
     *
     * @param tabName {string}
     * @param d {Drug}
     */
    $scope.selectTab = function(tabName, d){

        if(!tabName || !d) return null;

        $scope.resetTabs(d.id);

        switch(tabName){
            case 'details':
                $("#panel-"+d.id+'-details-tab').addClass('active');
                $('#details-panel-'+d.id).show();
                break;
            case 'history':
                $("#panel-"+d.id+'-history-tab').addClass('active');
                $('#history-panel-'+d.id).show();
                $scope.loadHistory(d, false);
                break;
            case 'discontinue':
                $("#panel-"+d.id+'-discontinue-tab').addClass('active');
                $('#discontinue-panel-'+d.id).show();
                break;
            default:
                console.log("unknown tab name: "+ tabName);
                break;
        }

    };

    /**
     * Populates the history field of the drug if it
     * is not already populated.
     *
     * @param drug {Drug}
     * @param reload {boolean}
     */
    $scope.loadHistory = function(drug, reload){

        if(!drug) return;

        $("#history-panel-error-"+drug.id).hide();

        // do not reload if they have not requested a
        // reload and the history is already populated.
        if(drug.history && !reload) return;

        rxService.history(drug, demo.demographicNo, function(history){
            
            if(!history){
                // error case
                $("#history-panel-error-"+drug.id).show();

            }else{
                drug.history = history;    
            }
            
        });

    };

    /**
     * Removes the active class from all tabs for the
     * medication identified by id. Hides all of the
     * inner panels.
     *
     * @param id {number}
     */
    $scope.resetTabs = function(id){

        $("#panel-"+id+'-details-tab').removeClass('active');
        $("#panel-"+id+'-history-tab').removeClass('active');
        $("#panel-"+id+'-discontinue-tab').removeClass('active');

        $('#details-panel-'+id).hide();
        $('#history-panel-'+id).hide();
        $('#discontinue-panel-'+id).hide();

    };

    $scope.addPrescription = function(){

        // 1) Check that instructions are
        //    valid and complete.

        if(!$scope.prescriptionInfoValid($scope.currentPrescription)){
            return;
        }

        // 2) Clone selected drug objects into
        //    the readyToPrescribe array.
        //    Apply prescription information
        //    from currentPrescription to the
        //    cloned drug objects.

        var tmpDrug = null;


        console.log($scope.selectedMedications);

        for(var i = 0; i <  $scope.selectedMedications.length; i++){

            for(var m = 0; m < $scope.currentMedications.length; m++){

                if($scope.selectedMedications[i] === $scope.currentMedications[m].id){
                    tmpDrug = new Drug();
                    tmpDrug.copy($scope.currentMedications[m]);
                    tmpDrug.applyPrescriptionInformation($scope.currentPrescription);
                    $scope.readyToPrescribe.push(tmpDrug);
                }
            }
        }

        // 3) Clear the current prescription information
        $scope.clearPrescription();

        $scope.hideSaveErrorMessage();

    };

    $scope.clearPrescription = function(){
        $scope.resetCurrentPrescription();
        $scope.selectedMedications = [];
        $scope.hidePrescribeForm();
    };

    /**
     * Boolean function that determines whether
     * the information entered for a prescription
     * is complete and valid.
     *
     * Also toggles error highlighting on offending fields.
     *
     * @param info {object} - prescription info, has structure like:
     *  { duration : number, durationUnit : 'D'|'M'|'W', repeats : number
     *      start : Date, end: Date }
     *
     * @return {boolean}
     */
    $scope.prescriptionInfoValid = function(info){


        if(!info || !(info instanceof Object)) return false;

        var result = true;

        if(!info.duration){
            result = result && false;
            $("#prescribe-duration").addClass('field-has-error');
        }

        if(!info.durationUnit){
            result = result && false;
            $("#prescribe-duration-unit").addClass('field-has-error');
        }

        if(info.quantity !== null && info.quantity <= 0 ){
            result = result && false;
            $("#prescribe-quantity").addClass('field-has-error');
        }

        if(!info.repeats && info.repeats !== 0){
            result = result && false;
            $("#prescribe-repeats").addClass('field-has-error');
        }

        if(!info.start){
            result = result && false;
            $("#prescribe-start").addClass('field-has-error');
        }

        if(!info.end){
            result = result && false;
        }

        return result;

    };

    $scope.resetCurrentPrescription = function(){

        var d = new Date();

        // Contains data from the current prescription (not drug) input elements
        $scope.currentPrescription = {
            duration : null,
            durationUnit : 'D',
            repeats : 0,
            quantity : null,
            start : $scope.formatDate(d),
            end : null
        };

        console.log($scope.currentPrescription);
    };

    /**
     * Update the currentPrescription quantity field.
     * We can only do this if one medication has been selected
     * for prescribing.
     */
    $scope.updatePrescriptionQuantity = function(){

        // Check the number of medications selected for prescribing.
        if($scope.selectedMedications && $scope.selectedMedications.length === 1){

            for(var i = 0; i < $scope.currentMedications.length; i++){

                // find the current drug's frequency
                if($scope.currentMedications[i].id === $scope.selectedMedications[0]){

                    $scope.currentPrescription.quantity = calculateQuantity(
                        $scope.currentMedications[i].frequency,
                        $scope.currentPrescription.duration,
                        $scope.currentPrescription.durationUnit,
                        $scope.currentMedications[i].takeMax
                    );

                }

            }

        }

    };

    /**
     * removes the prescription at the designated index
     * from the list of prescriptions that are ready
     * to prescribe.
     *
     * @param i {number} - index of element to remove.
     */
    $scope.removeFromReadyToPrescribe = function(i){
        $scope.readyToPrescribe.splice(i,1);

        if($scope.readyToPrescribe.length <= 0){
            $scope.hideSaveErrorMessage();
        }
    };

    $scope.showSaveErrorMessage = function(){
        $("#readyToPrescribeError").show();
    };

    $scope.hideSaveErrorMessage = function(){
        $("#readyToPrescribeError").hide();
    };

    /**
     * Saves the drugs that are currently in the readyToPrescribe list
     * by making a call to the API rx/prescribe/ route.
     */
    $scope.saveReadyToPrescribe = function(){

        rxService.prescribe(
            demo.demographicNo,
            $scope.readyToPrescribe,
            function(x){

                if(x){
                    // request success
                    $scope.readyToPrescribe = [];
                    $scope.updateCurrentMedications();
                    $scope.hideSaveErrorMessage();
                    $('body').scrollTop(0);
                }else{
                    // request failed
                    $scope.showSaveErrorMessage();
                }

            }
        );

    };


    function noNull(s) {
        if (s==null) s = "";
        if (s instanceof String) s=s.trim();
        return s;
    }

    function alignLeft(s) {
        return "<p style=\"text-align:left\">" + s + "</p>"
    }

    function alignRight(s) {
        return "<p style=\"text-align:right\">" + s + "</p>"
    }

    function isPresent(s) {
        return s ? " - " + s : "";
    }

    function formatPrescriptionPrintout(meds, user) {
        var clinicName = clinic;
        var prescriptionDate = $scope.formatDate(new Date());

        return "<html>" +
            "<title>Prescription</title>" +
            "<style>" +
            "body{width:800px;font-family:arial,verdana,tahoma,helvetica,sans serif}" +
            "label{font-weight:bold}" +
            "em{font-size:small}.large{font-size:large}.center{text-align:center}" +
            ".signed-by,.signer{float:left;}" +
            ".signer{margin:20px 10px; border-top:1px solid #000; width:200px; text-align:center;}" +
            "</style>" +
            "<style media='print'>button{display:none}.noprint{display:none}</style>" +
            "<body>" +
            "<button name='Print' onclick='window.print()'>Print</button>" +
            "<button name='Done' onclick='window.close()'>Done</button>" +
            "<div class='center'>" +
            "<img src=\"record/rx/img/rx.gif\" alt=\"OscarRx\" style=\"width:87px;height:86px;\">" +
            "<label class='large'>" + alignLeft(user.fullName) + clinicName +
            alignRight("<strong>" + prescriptionDate + "</strong>") + "<br></label></div><hr>" +
            "<div><pre><strong>" + demo.firstName + " " + demo.lastName + "</strong><br>" +
            demo.address.address + ", " + demo.address.city + " " + demo.address.province + " " + demo.address.postal + "<br>" +
            "Home Tel:" + demo.phone + "<br>" +
            "Work Tel:" + demo.alternativePhone + "<br>" +
            "Health Ins#:" + demo.hin + "<br>" +
            "</pre>" +
            "<hr>" +
            "<pre>" + meds +
            "</pre></div>" +
            "<hr>" +
            "<div><br>" +
            "<label><span class = \"signed-by\">Signature: </span></label>" +
            "<span class =\"signer\">" + user.fullName + "</span>" +
            "</div>" +
            "<br><br><br>" +
            "<div class='center'><br><br>" +
            "<em>Created by: OSCAR The open-source EMR www.oscarcanada.org</em></div>" +
            "</body></html>";
    }

    /**
     * Saves the drugs that are in the readyToPrescribe list.
     * Provides a method of printing the resulting prescription.
     */
    $scope.saveAndPrintReadyToPrescribe = function(){

        var printWin = window.open("", "saveAndPrintReadyToPrescribeWin", "width=830,height=900,scrollbars=yes,location=no");
        printWin.document.open();

        var pMeds = "", pLine = "", pAdditional = "", m = null;
        for (var i = 0; i < $scope.readyToPrescribe.length; i++) {

            m = $scope.readyToPrescribe[i];
            pLine = m.getInstructionLine();
            pAdditional = m.additionalInstructions ? "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + m.additionalInstructions : "";

            pMeds += "<p>"+pLine+pAdditional+"<p>";

        }

        var printout = formatPrescriptionPrintout(pMeds, user);

        printWin.document.write(printout);
        printWin.document.close();
        $scope.saveReadyToPrescribe();

    };

    $scope.cancelReadyToPrescribe = function(){

        $scope.readyToPrescribe = [];
        $scope.hideSaveErrorMessage();

        $('body').scrollTop(0);

    };

    // ------- watches for prescription field --------

    // watches fire every time the associated field changes.

    $scope.$watch("currentPrescription.start", function(x){
        $scope.currentPrescription.end = calculateEndDate($scope.currentPrescription);
    });

    $scope.$watch("currentPrescription.repeats", function(x){
        $scope.currentPrescription.end = calculateEndDate($scope.currentPrescription);
    });

    $scope.$watch("currentPrescription.duration", function(x){
        $scope.currentPrescription.end = calculateEndDate($scope.currentPrescription);
        $scope.updatePrescriptionQuantity();
    });

    $scope.$watch("currentPrescription.durationUnit", function(x){
        $scope.currentPrescription.end = calculateEndDate($scope.currentPrescription);
        $scope.updatePrescriptionQuantity();
    });

    // ------- end watches for prescription field ----

    // Lookup Medication variables
    $scope.lookUpResult = [];
    $scope.modalShowInactive = false;
    $scope.selectedMed = null;

    // New Medication
    $scope.newMed = null;

    /**
     * Discontinues the selected medication.
     *
     * Makes a call to the rxService to do the discontinue.
     *
     * @param id {number} the drug id of the medication to discontinue.
     */
    $scope.discontinueMedication = function(id){

        var reason = $("#discontinue-"+id).val();

        if(reason === 'null'){

            // if they haven't selected a reason
            // for d/c the drug then highlight the
            // field.

            $("#discontinue-"+id+"-wrapper").addClass("has-error");

        }else {
            rxService.discontinueMedication(demo.demographicNo,
                id, reason).then(function (x) {
                $scope.updateCurrentMedications();
            });
        }

    };

    /**
     * Show an error to indicate that the current
     * medication is inconsistent/not complete. 
     */
    $scope.showInconsistentError = function(){

        $("#medication-error-wrapper").show();

        $("#medication-search-wrapper").addClass("has-error");
        $("#medication-started-wrap").addClass("has-error");
        $("#instruction-wrapper").addClass("has-error");

    };

    $scope.hideInconsistentError = function(){

        $("#medication-error-wrapper").hide();

        $("#medication-search-wrapper").removeClass("has-error");
        $("#medication-started-wrap").removeClass("has-error");
        $("#instruction-wrapper").removeClass("has-error");
    };

    /**
     * Attempts to add the medication
     * currently stored in $scope.newMed.
     * 
     * Checks if the medication is valid,
     * then makes a request via the rxService.
     */
    $scope.addNewMed = function(){

        // 1. Check medication valid.
        //  - make noise if it isn't...

        if(!$scope.newMed.checkValid()){

            // show errors
            $scope.showInconsistentError();
            return;
        }
        
        // 2. Call rxService with medication
        //  - rxService will translate to backend
        //      API object and make request.
        
        rxService.addMedication(this.newMed, function(result){

            // 3. Handle response:
            //  - errors for failed requests
            //  - update med list if successful

            // if result is true then the drug was added successfully
            if(result){

                $scope.closeNewMed();
                $scope.updateCurrentMedications();

            }else{

                // show an error to indicate the add medication
                // request failed.
                $scope.showAddMedicationFailedResponse(); 
            }
            
        }); 

    };

    /**
     * Shows a message to say that the medication could not be 
     * added by the API. 
     */
    $scope.showAddMedicationFailedResponse = function(){
        $("#medication-request-error-wrapper").show();
    };

    /**
     * Hide the add medication error message.
     */
    $scope.hideAddMedicationFailedResponse = function(){
        $("#medication-request-error-wrapper").show();
    };

    $scope.closeNewMed = function(){
        $scope.resetNewMedication();
        toggleAddMedication();
    };

    $scope.resetNewMedication = function () {

        document.getElementById("drug-details-panel").style.display = "none";
        $scope.medicationSearch = "";
        $scope.extendedMedicationSearch = "";
        $scope.newMed = new Drug(demo.demographicNo, user.providerNo);

        $scope.newMed.newMed = true; //set flag so we

        // Reset UI features

        $scope.hideInconsistentError(); 

        drugSearchOff();
        drugSearchOn();
    };

    $scope.setNewMedStartToday = function () {
        $scope.newMed.rxDate = makeDate(new Date());
    };

    $scope.setNewMedStopDate = function(delta){

        var d = new Date();

        switch(delta){
            case '1w':
                d.setDate(d.getDate() + 7);
                break;
            case '2w':
                d.setDate(d.getDate() + 14);
                break;
            case '1m':
                d.setDate(d.getDate() + 28);
                break;
            case '2m':
                d.setDate(d.getDate() + 56);
                break;
            case '6m':
                d.setDate(d.getDate() + 168);
                break;
            case '12m':
                d.setDate(d.getDate() + 336);
                break;
            default:
                break;
        }
        $scope.newMed.endDate = makeDate(d);

    };

    // ----- Watches for fields in add medication --------

    $scope.$watch("newMed.rxDate", function (val) {
        dateWatcher(val, "medication-started-wrap");
    });

    $scope.$watch("newMed.endDate", function (val) {
        dateWatcher(val, "medication-stop-wrap");
    });

    $scope.syncInstructionAndControlFields = function(v){
        if($scope.newMed) {
            $scope.newMed.updateInstructionField();
            $("#medication-instruction").addClass('field-notice-warning');

            setTimeout(function(){
                $("#medication-instruction").removeClass('field-notice-warning');
            }, 1000);
        }
    };

    $scope.$watch("newMed.instructions", function(val){
        if($scope.newMed && (!val || val.length === 0)){
            $scope.newMed.resetInstructions();
        }
    });

    $scope.$watch("newMed.takeMax", function(val) {

        $scope.syncInstructionAndControlFields(val);

    });

    $scope.$watch("newMed.takeMin", function(val) {
        $scope.syncInstructionAndControlFields(val);
    });

    $scope.$watch("newMed.frequency", function(val) {
        $scope.syncInstructionAndControlFields(val);
    });

    $scope.$watch("newMed.route", function(val) {
        $scope.syncInstructionAndControlFields(val);
    });

    $scope.$watch("newMed.prn", function(val) {
        $scope.syncInstructionAndControlFields(val);
    });

    // --------------------------------------------------

    $scope.selectNewMed = function (m) {
        console.log("selectNewMed(): " + JSON.stringify(m));
        rxService.getMedicationDetails(m.id).then(function (d) {
    
            $scope.newMed = new Drug(demo.demographicNo, user.providerNo);
            $scope.newMed.name = m.name;
            $scope.newMed.newMed = true;
            $scope.newMed.populateFromDrugSearchDetails(d.drugs);

            showDrugDetailsPanel();
            updateStrengthUnits(d.drugs);
        });
    };

    $scope.doDrugLookUp = function (s, cb) {
        rxService.lookup(s).then(function (d) {
            cb(d);
        });
    };

    // ---------- Custom Drug Modal -----------------

    $scope.customDrugHandler = function(){

        $scope.modalInstance = $modal.open({
            animation: true,
            templateUrl: 'customDrug.html',
            scope: $scope
        });
        
        $scope.modalInstance.opened.then(function(x){

            // Reset
            $scope.customDrug = {name : null, form : null, strength : null, strengthUnit : null, note : null};

        });

        $scope.modalInstance.result.then(function(d){

            $scope.resetNewMedication();

            drugSearchOff();

            var name = d.name;

            if(d.strength){
                $scope.newMed.strength = d.strength;
                name += " "+d.strength;
            }

            if(d.strengthUnit){
                $scope.newMed.strengthUnit = d.strengthUnit.toUpperCase();
                name += " "+d.strengthUnit.toUpperCase();
            }

            if(d.form){
                $scope.newMed.form = d.form.toUpperCase();
                name += " "+d.form.toUpperCase();
            }

            if(d.note){
                $scope.newMed.additionalInstructions = d.note;
            }

            $scope.newMed.name = name;

            $scope.newMed.genericName = "$$custom$$";
            $scope.newMed.gn = "$$custom$$";

            $scope.medicationSearch = $scope.newMed.name;

            showDrugDetailsPanel();
            
        },function(x){

            
        });

        $scope.modalAccept = function (x) {
            $scope.modalInstance.close($scope.customDrug);
        };

        $scope.modalCancel = function () {
            $scope.modalInstance.dismiss("Custom drug modal cancel");
        };
        
    };

    // ---------- END Custom Drug Modal --------------

    // ---------- Add Favorite Modal -----------------

    $scope.addFavoriteHandler = function(){

        // We can only open the modal if there
        // is exactly one item fixed. 
        if($scope.selectedMedications.length !== 1) {
            return;
        }

        $scope.modalInstance = $modal.open({
            animation: true,
            templateUrl: 'addFavorite.html',
            scope: $scope
        });

        $scope.modalInstance.opened.then(function(x){

            var tmpDrug = new Drug();

            for(var i = 0; i < $scope.currentMedications.length; i++){
                if($scope.currentMedications[i].id === $scope.selectedMedications[0]){
                    tmpDrug.copy($scope.currentMedications[i]);
                    tmpDrug.applyPrescriptionInformation($scope.currentPrescription);
                    break;
                }
            }
            
            $scope.newFavorite = new Favorite(tmpDrug, null);

            console.log($scope.newFavorite);

            $("#add-favorite-error").hide();
        });

        $scope.modalInstance.result.then(function(x){

            // when (close) accepted....
            console.log("Add Favorite Modal Accept");
            $scope.newFavorite = null;
            $("#add-favorite-error").hide();

        },function(x){

            // when (close) cancelled/dismissed...
            console.log("Add Favorite Modal Cancel");
            $scope.newFavorite = null;
            $("#add-favorite-error").hide();

        });

        $scope.modalAccept = function (x) {

            if(!$scope.newFavorite.name){
                $scope.modalInstance.showAddFavoriteError("Please choose a name for the favorite.");
            }else {

                rxService.addFavorite($scope.newFavorite, function (x) {

                    if (x.success) {
                        $scope.modalInstance.close();
                        $scope.updateFavorites();
                    } else {
                        $scope.modalInstance.showAddFavoriteError();
                    }
                });
            }
        };

        $scope.modalCancel = function () {
            $scope.modalInstance.dismiss("add favorite modal cancel");
        };
        
        $scope.modalInstance.showAddFavoriteError = function(s){

            if(s) {
                $("#add-favorite-error-msg").text(s);
            } else{
                $("#add-favorite-error-msg").text("Failed to add favorite!");
            }
            $("#add-favorite-error").show();
        }

    };

    // ---------- END Add Favorite Modal --------------

    // ---------- Functions for Lookup Modal ---------
    $scope.moreDrugChoices = function () {

        $scope.selectedMed = null;

        $scope.modalInstance = $modal.open({
            animation: true,
            templateUrl: 'myModalContent.html',
            scope: $scope
        });

        $scope.modalInstance.opened.then(function (a) {

            $scope.extendedMedicationSearch = $scope.medicationSearch;

            $scope.doDrugLookUp($scope.extendedMedicationSearch, function (d) {
                $scope.lookUpResult = d.drugs;
            });

            $scope.updateSearchModalDrugs = function (x) {
                $scope.doDrugLookUp(x, function (d) {
                    $scope.lookUpResult = d.drugs;
                });
            };

            $scope.modalSelectMed = function (index) {
                if ($scope.selectedMed !== null) {
                    $scope.selectedMed.selected = false;
                }
                $scope.selectedMed = $scope.lookUpResult[index];
                $scope.selectedMed.selected = true;
            }
        });

        $scope.modalInstance.result.then(function (item) {
            console.log("Modal closed :" + JSON.stringify(item));
            $scope.medicationSearch = item.name;
            $scope.selectNewMed(item);
        }, function (x) {
            console.log("Modal dismissed:" + x);
        });

        $scope.modalAccept = function () {
            drugSearchOff();
            $scope.modalInstance.close($scope.selectedMed);
        };

        $scope.modalCancel = function () {
            $scope.modalInstance.dismiss("cancel");
        };
    };

    // --------- END Functions for Lookup Modal ----------


    // --------- Functions for parsing drug instructions ---------

    $scope.blurInstructionsField = function(s){
        $scope.parseNewMedInstructions(s);
        $scope.toggleInstructionsGuidePanel(false);
    };

    $scope.parseNewMedInstructions = function (s) {

        if (s && s.length > 5) {
            rxService.parseInstructions(s).then(function (x) {
                $scope.newMed.applyInstructions(x);
                $scope.newMed.updateInstructionField();
            });
        }
    };

    // --------- END drug parsing functions
    
    $scope.getRxUrl = function (demographicNo) {
        url = "../oscarRx/choosePatient.do?demographicNo=" + demographicNo;
        return url;
    };

    $scope.openRx = function (demographicNo) {
        console.log(" in openRx ", demographicNo);
        win = "Rx" + demographicNo;
        var url = "../oscarRx/choosePatient.do?demographicNo=" + demographicNo;
        window.open(url, win, "scrollbars=yes, location=no, width=900, height=600", "");
    };

    $scope.gotoState = function (item) {
        /*        if (item.type == 'rx') {
         win = "Rx" + $stateParams.demographicNo;
         } else {
         win = "win_item.type_";
         }*/
        win = "Rx" + $stateParams.demographicNo;
        console.log(" item.action= ", item.action);
        window.open(item.action, win, "scrollbars=yes, location=no, width=900, height=600", "");
        return false;
    };

    /**
     * Changes the visibiliy of the instructions panel.
     *
     * @param x {boolean} - if true shows panel, if false hides panel, undefined toggles panel.
     */
    $scope.toggleInstructionsGuidePanel = function(x){
        if(x === true){
            $("#instructions-guide-panel").show();
        }else if(x === false){
            $("#instructions-guide-panel").hide();
        }else{
            $("#instructions-guide-panel").toggle();
        }
    };

    $scope.loadProviders = function(){

        providerService.searchProviders("").then(function(d){
            console.log("providerService response: ");
            console.log(d);
        })

    };

    $scope.prescriberChange = function(){

        // If they have selected the "other" option
        // we change the field to an input field instead of
        // dropdown.
        if($scope.newMed && $scope.newMed.provider === "$$other$$"){

            $scope.newMed.provider = user.providerNo; // set provider to the current user, they made the med.
            $scope.newMed.externalProvider = null;

            $scope.showExternalProvider();

        }
    };

    $scope.toggleAccordian = function(id) {
        $("#" + id).collapse('toggle');
    };

    $scope.showExternalProvider = function(){

        $("#medication-external-prescriber-wrapper").show();
        $("#medication-prescriber-wrapper").hide();

    };

    $scope.showCurrentProvider = function(){

        $("#medication-external-prescriber-wrapper").hide();
        $("#medication-prescriber-wrapper").show();

        $scope.newMed.externalProvier = null;

    };

    /**
     * Determines if two dates are on the same day.
     *
     * @param d1 {Date}
     * @param d2 {Date}
     * @returns {boolean}
     */
    $scope.sameDateToDay = function(d1, d2){
        if(d1.getFullYear() !== d2.getFullYear()){
            return false;
        }else if(d1.getMonth() !== d2.getMonth()){
            return false;
        }else if(d1.getDate() !== d2.getDate()){
            return false;
        }else{
            return true;
        }
    };

    $scope.openAllergies = function(demoNo){
        console.log(" in openAllergies", demoNo);
        win = "Allergy"+demoNo;
        var url = "../oscarRx/showAllergy.do?demographicNo=" + demoNo;
        window.open(url,win,"scrollbars=yes, location=no, width=900, height=600","");
        return false;
    };

    $scope.openDxResearch = function(demoNo) {
        console.log(" in openDxResearch", demoNo);
        win = "Disease Registry" + demoNo;
        var url = "../oscarResearch/oscarDxResearch/setupDxResearch.do?quickList=&demographicNo=" + demoNo;
        window.open(url, win, "scrollbars=yes, location=no, width=900, height=600", "");
        return false;
    };

    $scope.openLegacyRx = function(demoNo){

        win = "Legacy Rx " + demoNo;
        var url = "../oscarRx/choosePatient.do?demographicNo=" + demoNo;
        window.open(url, win, "scrollbars=yes, location=no, width=900, height=600", "");

    };

    /**
     * Determines if a medication is expired by checking its
     * endDate field.
     *
     * @param m {Drug} the drug to check.
     * @returns {boolean} true if expired, false otherwise.
     */
    $scope.expiredMedication = function(m){

        if(!m || !m.endDate) return false;

        return m.endDate < $scope.today;

    };

    /**
     * Determines if a drug will expire in a number of days.
     *
     * @param m {Drug} - the drug to check.
     * @param days {number} - the number of days until expiry.
     * @returns {boolean} - true if will expire within days, false otherwise.
     */
    $scope.willExpire = function(m,days) {

        if (!m || !m.endDate || !days) return false;

        // we change to number of ms
        var ms = days * 86400000;

        // date subtraction returns difference in ms
        return (m.endDate > $scope.today) && (m.endDate - $scope.today < ms);
    };

    // -------- Scope functions for managing favorites ------

    /**
     * Makes a request to the API for a new list of favorites.
     */
    $scope.updateFavorites = function(){

        rxService.favorites(demo.demographicNo, user.providerNo , function(x){
            console.log(x);
            $scope.favorites = x;
        });

    };

    /**
     * Adds a new drug to the prescribe list
     * based on a favorite choice.
     *
     * @param index {number} the index in the $scope.favorite list of the drug
     *  to prescribe.
     *
     *  @return {boolean} true if successfully added, false otherwise
     */
    $scope.prescribeFavorite = function(index){

        // sanity check.
        if(isNaN(index) || index < 0) return false;

        var d = new Drug();
        d.applyFavorite($scope.favorites[index]);
        $scope.readyToPrescribe.push(d);

        console.log($scope.readyToPrescribe);

        return true;

    };

    /*$scope.addFavorite = function(){

        if($scope.selectedMedications.length !== 1) return null;

        var tmpDrug = new Drug();

        for(var i = 0; i < $scope.currentMedications.length; i++){

            if($scope.currentMedications[i].id === $scope.selectedMedications[0]){
                tmpDrug.copy($scope.currentMedications[i]);
                tmpDrug.applyPrescriptionInformation($scope.currentPrescription);

                rxService.addFavorite(tmpDrug, null, function(){
                    $scope.updateFavorites();
                });
                break;
            }

        }

    };*/

    $scope.formatDate = function(d){

        if(!d) return "";

        var s = d.getFullYear();
        s += "-"+ (d.getMonth()+1 >= 10 ? (d.getMonth()+1) : "0"+(d.getMonth()+1));
        s += "-"+ (d.getDate() >= 10 ? (d.getDate()) : "0"+(d.getDate()));

        return s;
    };

    // -------- END favorites -------------------------------

    // ------- END OF SCOPE FUNCTIONS -----------


    // ------- HELPER FUNCTIONS ---------
    function showDrugDetailsPanel() {
        if ($scope.newMed.genericName && $scope.newMed.gn) {
            document.getElementById("drug-details-panel").style.display = "block";

        }
    }

    function makeDate(d){
        var s = d.getFullYear() + "-";
        s += ((d.getMonth() + 1) <= 9 ? "0" + (d.getMonth() + 1) : (d.getMonth() + 1)) + "-";
        s += (d.getDate() <= 9 ? "0" : "") + (d.getDate());
        return s;
    }

    /**
     * Helper function for watching date fields.
     * Changes input field to error state if date is invalid.
     * @param val
     * @param id
     */
    function dateWatcher(val, id){
        var v = checkValidDate(val);
        if (!v) {
            // rxDate in newMed is invalid, show an error.
            if(val){
                document.getElementById(id).classList.add("has-error");
            }
        }else{
            document.getElementById(id).classList.remove("has-error");
        }
    }

    /**
     * Determines if a date string has form: YYYY-mm-dd
     * @param d the string to be checked.
     * @returns {boolean} true if the string is valid, false otherwise.
     */
    function checkValidDate(d) {

        if (typeof d !== 'string') return false;


        var r = new RegExp("^(\\d{4})-(\\d{2})-(\\d{2})$", "i");
        var m = d.match(r);
        if(!m) return false;

        var x = new Date(d);
        if(isNaN(x.getTime())) return false;
        else return true;
    }

    function updateStrengthUnits(drug) {

        var unit = drug.components.unit;

        if (unit) {

            var s = document.getElementById("medication-strength-unit");

            for (var i = 0; i < s.options.length; i++) {
                if (s.options[i].value === unit) {
                    return unit;
                }
            }

            // if we get to here the unit was not already found.
            var opt = document.createElement("option");
            opt.text = unit;
            opt.value = unit.toUpperCase();

            return unit;

        } else {

            return null;

        }

    }

    function fillAllergyItems() {
        console.log(" entering fillAllergyItems ");
        if ($scope.gotAllergyData === 0 && isListEmpty($scope.summaryAllergyList)) {
            $scope.gotAllergyData = 1;
            summaryService.getFullSummary($stateParams.demographicNo, "allergies").then(function (data) {
                console.log(" fullSummary returned ", data);
                console.log(" allergy is defined ", angular.isDefined(data.summaryItem));
                if (angular.isDefined(data.summaryItem)) {
                    console.log("summaryItem defined");
                    if (data.summaryItem instanceof Array) {
                        $scope.summaryAllergyList = data.summaryItem;
                    } else {
                        $scope.summaryAllergyList = [data.summaryItem];
                    }
                    console.log("summaryAllergyList ", $scope.summaryAllergyList);
                }
                return $scope.summaryAllergyList;
            }, function (errorMessage) {
                console.log("fillItems" + errorMessage);
            });
        }
    }

    function fillDxItems() {
        console.log(" entering fillDxItems ");
        if ($scope.gotDiseasesData === 0 && isListEmpty($scope.summaryDxList)) {
            $scope.gotDiseasesData = 1;
            summaryService.getFullSummary($stateParams.demographicNo, "diseases").then(function (data) {
                console.log(" fullSummary returned ", data);
                console.log(" diseases is defined ", angular.isDefined(data.summaryItem));
                if (angular.isDefined(data.summaryItem)) {
                    console.log("summaryItem defined");
                    if (data.summaryItem instanceof Array) {
                        $scope.summaryDxList = data.summaryItem;
                    } else {
                        $scope.summaryDxList = [data.summaryItem];
                    }
                    console.log("summaryDxList ", $scope.summaryDxList);
                }
                return $scope.summaryDxList;
            }, function (errorMessage) {
                console.log("fillItems" + errorMessage);
            });
        }
    }

    // ------- END HELPER FUNCTIONS ----------

    function isListEmpty(object) {
        for (var key in object) {
            if (object.hasOwnProperty(key)) {
                return false;
            }
        }
        return true;
    }

    // ------- FUNCTIONS TO CALL ON LOAD ---------------

    $scope.resetCurrentPrescription();
    $scope.updateCurrentMedications();
    $scope.updateFavorites();


    $('body').scrollTop(0);
});
