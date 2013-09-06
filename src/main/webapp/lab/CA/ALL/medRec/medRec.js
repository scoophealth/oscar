/*
 * Medication object that will hold each medication on the page.
 */
function Medication(parent_element, id){
    this.id = id;
    if (parent_element != ""){
        this.id = id;
        var inputs = getAssArray($("#"+parent_element+" #med-summary_"+id+" :input"));   
        if(inputs["brandname"]) this.brandname = inputs["brandname"];
        else this.brandname = "";
        if(inputs['atc']) this.atc = inputs['atc'];
        else this.atc=''; 
        if(inputs['din']) this.din = inputs['din'];
        else this.din = '';
        if(inputs['ingredientname'])this.ingredientname = inputs["ingredientname"];
        else this.ingredientname = '';
        if(inputs['dose'])this.dose = inputs["dose"];
        else this.dose='';
        if(inputs['unit'])this.unit = inputs["unit"];
        else this.unit = '';
        if(inputs['route'])this.route = inputs["route"];
        else this.route = '';
        if(inputs['form']) this.form = inputs["form"];  
        else this.form = '';
        if(inputs['freq1'])this.freq1 = inputs["freq1"];
        else this.freq1 = '';
        if(inputs["prn"] == "true"){
            this.prn = true;
        }else{
            this.prn = false;
        }
        if(inputs["quantity"])this.quantity = inputs["quantity"];
        else this.quantity = "";
        if(inputs['repeats'])this.repeats = inputs["repeats"];
        else this.repeats='';
        if(inputs["min"])this.min = inputs["min"];
        else this.min="";
        if(inputs['max'])this.max = inputs["max"];
        else this.max="";
        if(inputs['duration']){
            this.duration = inputs["duration"];
            this.duration = this.duration.replace("'","");
        }
        else this.duration = '';
        if(inputs['duration-unit']) this.durationunit = inputs["duration-unit"]; 
        else this.durationunit = '';
        if(inputs["externalprovider"] == "true"){
            this.externalprovider = true;
        }else{
            this.externalprovider = false;
        }
        if(inputs["longterm"] == "true"){
            this.longterm = true;
        }else{
            this.longterm = false;
        }
        if(inputs['instructions'])this.instructions = inputs["instructions"];
        else this.instructions = '';
        if(inputs['associatedmed'])this.associatedmed = inputs["associatedmed"];
        else this.associatedmed = '';
        if(inputs['state']){
            this.state = inputs["state"];
        }
        else this.state='new';

        //set hte origin, or the inital state of hte med, need for updating the database.
        if(this.state=='current'){
            this.origin = 'current';
        }else if(this.state=='new'){
            this.origin = 'new';
        }else{
            this.origin = 'new';
        }
        if(inputs['drugid']){
            this.drugid = inputs["drugid"];
        }else {this.drugid='';}
        if(inputs['startdate']) this.startedate = inputs['startdate'];
        else{
            var today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth()+1;
            var yyyy = today.getFullYear();
            this.startdate=yyyy+'-'+mm+'-'+dd
        }
        if(inputs['writtendate']) this.startedate = inputs['writtendate'];
        else this.writtendate = '';
    }else{
        this.brandname = "";
        this.atc = "";
        this.din = "";
        this.ingredientname = "";
        this.dose = "";
        this.unit = '';
        this.route = '';
        this.form = '';  
        this.freq1 = '';
        this.prn = '';
        this.quantity = "";
        this.repeats = "";
        this.min = "";
        this.max = "";
        this.duration = ''; 
        this.durationunit = ''; 
        this.drugid = "";
        this.externalprovider = false;
        this.longterm = false;
        this.instructions = '';
        this.associatedmed = '';
        this.state = 'new';
        this.origin = "new";
        this.startdate = '';
        this.writtendate = '';
    }

    console.log("Medication object created: "+this)
}
/*
 * Returns a string representation of the medication for display.
 * See http://mscui.org/DesignGuide/QuickGuides/MedicationLine/
 * for specs on how to display.
 */
Medication.prototype.displayString = function(){
    var s = "";
    if(this.ingredientname != ""){
        s += "<b>"+this.ingredientname.toLowerCase()+"</b>";
    }
    if(this.brandname != ""){
        if(this.ingredientname != ""){
            s += " - ";
        }
        s += this.brandname.toUpperCase();
    }
    if(this.min != "" || this.max != ""){
        if((this.min != "" && this.max != "") && this.max != this.min){
            s += " - ";
            s += this.min+"-"+this.max;
        }
        if((this.min != "" && this.max != "") && this.max == this.min){
            s += " - ";
            s += this.min;
        }
        if(this.min != "" && this.max == ""){
            s += " - ";
            s += this.min;
        }
        if(this.min == "" && this.max != ""){
            s += " - ";
            s += this.max;
        }
    }
    if(this.form != ""){
        s += " - ";
        s += this.form;
    }
    if(this.dose != "" && this.unit != ""){
        s += " - ";
        s += "<span style='color:#428EC0; font-size:12px;'>DOSE</span> "
            s += "<b>"+this.dose+this.unit+"</b>";
    }
    if(this.route != ""){
        s += " - ";
        s += this.route;
    }
    if(this.freq1 != ""){
        s += " - ";
        s += this.freq1;
    }
    if(this.prn != ""){
        s += " - ";
        s += "PRN";
    }
    return s;
}

/*
* Displays a more descriptive version of the medication string, 
* including all of the input fields and their respective labels.
*/
Medication.prototype.displayStringVerbose = function(labels, bold){
    var s = "";
    if(this.ingredientname != ""){
        if(bold){
            s += "<b>"+this.ingredientname.toLowerCase()+"</b>";
        }else{
            s += this.ingredientname.toLowerCase();
        }
    }
    if(this.brandname != ""){
        if(this.ingredientname != ""){
            s += " - ";
        }
        s += this.brandname.toUpperCase();
    }
    if(this.min != "" || this.max != ""){
        if (labels) s += " <span style='color:#428EC0; font-size:12px;'>MIN/MAX</span> "
        if((this.min != "" && this.max != "") && this.min != this.max){
            s += " - ";
            s += this.min+"-"+this.max;
        }
        if((this.min != "" && this.max != "") && this.min == this.max){
            s += " - ";
            s += this.min;
        }
        if(this.min != "" && this.max == ""){
            s += " - ";
            s += this.min;
        }
        if(this.min == "" && this.max != ""){
            s += " - ";
            s += this.max;
        }
    }
    if(this.form != ""){
        s += " - ";
        if (labels) s += "<span style='color:#428EC0; font-size:12px;'>FORM</span> "
            s += this.form;
    }
    if(this.dose != "" && this.unit != ""){
        s += " - ";
        if (labels) s += "<span style='color:#428EC0; font-size:12px;'>DOSE</span> "
        if(bold){
            s += "<b>"+this.dose+this.unit+"</b>";
        }else{
            s += this.dose+this.unit;
        }
    }
    if(this.route != ""){
        s += " - ";
        if (labels) s += "<span style='color:#428EC0; font-size:12px;'>ROUTE</span> "
            s += this.route;
    }
    if(this.freq1 != ""){
        s += " - ";
        if (labels) s += "<span style='color:#428EC0; font-size:12px;'>FREQUENCY</span> "
            s += this.freq1;
    }
    if(this.prn != ""){
        s += " - ";
        if (labels) s += "PRN";
    }
    if(this.duration && this.durationunit){
        s += " - ";
        if (labels) s += "<span style='color:#428EC0; font-size:12px;'>DURATION</span> "
            s += this.duration+' '+this.durationunit;
    }
    if(this.instructions != ""){
        s += " - ";
        if (labels) s += "<span style='color:#428EC0; font-size:12px;'>INSTRUCTIONS</span> "
            s += this.instructions;
    }
    if(this.startdate != ''){
        s += " - ";
        if (labels) s += "<span style='color:#428EC0; font-size:12px;'>START</span> "
        s += this.startdate;

    }
    return s;
}

Medication.prototype.setState = function(state){
    this.state = state;
}

//-----------------------END OF MEDICATION CLASS ---------------------//

// //atcCode=B01AA03&atcCode=B01AA03&drugName_930546=COUMADIN%20TAB%2010MG&instructions_930546=&quantity_930546=0&repeats_930546=0&refillDuration_930546=0&refillQuantity_930546=0&dispenseInterval_930546=0&outsideProviderName_930546=&outsideProviderOhip_930546=&rxDate_930546=2013-08-28&lastRefillDate_930546=&writtenDate_930546=2013-08-28&pickupDate_930546=&pickupTime_930546=&comment_930546=&eTreatmentType_930546=--&rxStatus_930546=--&drugName_343798=TARO-WARFARIN%202MG&instructions_343798=&quantity_343798=0&repeats_343798=0&refillDuration_343798=0&refillQuantity_343798=0&dispenseInterval_343798=0&outsideProviderName_343798=&outsideProviderOhip_343798=&rxDate_343798=2013-08-28&lastRefillDate_343798=&writtenDate_343798=2013-08-28&pickupDate_343798=&pickupTime_343798=&comment_343798=&eTreatmentType_343798=--&rxStatus_343798=--&demographicNo=5&searchString=&search=Search

function MedicationOut(med){
    this.atc = med.atc;
    this.brandname=med.brandname;
    this.ingredientname=med.ingredientname;
    this.form=med.form;
    this.route=med.route;
    this.freq1=med.freq1;
    this.min = med.min;
    this.max = med.max;
    this.freq1 = med.freq1;
    this.duration = med.duration;
    this.durationunit = med.durationunit;
    this.dose = med.dose;
    this.din = med.din;
    this.instructions = med.min+'-'+med.max+" "+med.route+" "+med.freq1+" "+med.duration+" "+med.durationunit;
    this.quantity = med.quantity;
    this.writtendate = med.writtendate;
    this.startdate = med.startdate;
    this.comment = med.instructions;
    this.demographicId=pm.demographic_id;
    this.drugid = med.drugid;
}

//------------------------BEGIN PageManager CLASS --------------------//
/*
 * An object that will manage the medications on the page. 
 * @param element - the html element that the medication forms are stored.
 */
function PageManager(element){
    //#TODO: Implement opts array for table and modals.
    this.element = element;
    var medForms = $("#"+this.element).children();
    var meds = {}; 
    for(var i=0;  i<medForms.length; i++){
        var s = medForms[i].id.split("_")[1]; //get the id of the med in the form.
        meds[s] = new Medication(this.element, s); //create a new medication object.
    }  
    this.medications = meds;

    //init some values to null, these must be set seperately by calling
    //the respective set methods
    this.suggestedMedsTable = null;
    this.currentMedsTable = null;
    this.medicationModal = null;

    console.log("INFO: PageManager object created")
}

PageManager.prototype.signAndSave = function(){
   for(var med in this.medications){
       console.log("Making changese to: "+this.medications[med].brandname);
       console.log("state is: "+this.medications[med].state);
       if(this.medications[med].state == "current"){
           var new_url = "";
           if(this.medications[med].origin == "current"){
               console.log("Updating: "+this.medications[med].brandname);
               new_url= "/oscar_master/dms/medRecAction.do?method=updateMedication";
           }
           else if(this.medications[med].origin == "new"){
               console.log("Adding: "+this.medications[med].brandname);
               new_url= "/oscar_master/dms/medRecAction.do?method=addMedication";
           }
           var s = $.param(new MedicationOut(this.medications[med]));
           console.log("sending xhr to for med "+med+"to:"+new_url);
           $.ajax({
               url:new_url,
               type:"post",
               data:s,
               success: function(){console.log("INFO:Add/update XHR  was success.");},
               complete: function(){console.log("INFO:Add/update XHR was completed");}
           });
       }
       else if(this.medications[med].state=="discontinued"){
           console.log("D/C-ing: "+this.medications[med].brandname);
           new_url= "/oscar_master/dms/medRecAction.do?method=discontinueMedication";
           var s = $.param(new MedicationOut(this.medications[med]));
           console.log("sending xhr to for med "+med+"to:"+new_url);
           $.ajax({
               url:new_url,
               type:"post",
               data:s,
               success: function(){console.log("INFO:d/c XHR  was success.");},
               complete: function(){console.log("INFO:d/c XHR was completed");}
           });
       }
   }
}

/*
 * Sets the pointers to the medication modal on the page. 
 * @param modal - the id of the modal table on the page.
 */
PageManager.prototype.setMedicationModal = function(modal){
    this.medicationModal = document.getElementById(modal)
        console.log("INFO: Modal in PageManager set.");
}

/*
 * Sets the pointers to the respective tables in the class.
 * @param suggested - the id of the suggested medication table on the page.
 * @param current - the id of the current medication table on the page.
 */
PageManager.prototype.setTables = function(suggested, current){
    this.suggestedMedsTable = document.getElementById(suggested);
    this.currentMedsTable = document.getElementById(current);

    console.log("INFO: Tables in PageManager set.");
}

/*
* Sets the log table where the record of changes is displayed
* @param id - the id of the table element in the html.
*/
PageManager.prototype.setLogTable = function(id){
    this.logTable = document.getElementById(id);

    console.log("INFO: Log table was set");
}
/*
 *   Clears all of the tables on the page so that they can re-drawn according to the info 
 *    in the model forms
 */
PageManager.prototype.clearTables = function(){

    //delete all rows in the tables
    this.suggestedMedsTable.innerHTML="";
    this.currentMedsTable.innerHTML="";

    console.log("INFO: Tables cleared");
}

PageManager.prototype.addNewMedication = function(){
    var id = size(this.medications).toString();
    this.medications[id] = new Medication("", id);
    this.medications[id].brandname = this.medicationModal.querySelector("input#brandname").value;
    this.medications[id].ingredientname = this.medicationModal.querySelector("input#genericname").value;
    this.medications[id].dose = this.medicationModal.querySelector("input#dose").value;
    this.medications[id].dose = this.medicationModal.querySelector("input#dose").value;
    this.medications[id].unit = this.checkDropDownForValue("unit");
    this.medications[id].form = this.checkDropDownForValue("form");
    this.medications[id].prn = this.medicationModal.querySelector("input#prn").checked;
    this.medications[id].route = this.checkDropDownForValue("route");
    this.medications[id].freq1 = this.checkDropDownForValue("freq1");
    this.medications[id].min = this.medicationModal.querySelector("input#min").value;
    this.medications[id].max = this.medicationModal.querySelector("input#max").value;
    this.medications[id].repeats = this.medicationModal.querySelector("input#repeats").value;
    this.medications[id].quantity = this.medicationModal.querySelector("input#quantity").value;
    this.medications[id].duration = this.checkDropDownForValue("duration");
    this.medications[id].durationunit = this.checkDropDownForValue("duration-unit");
    this.medications[id].instructions = this.medicationModal.querySelector("textarea#instructions").value;
    this.medications[id].longterm = this.medicationModal.querySelector("input#longterm").checked;
    this.medications[id].externalprovider = this.medicationModal.querySelector("input#externalprovider").checked;
    this.medications[id].state = "current";
    this.medications[id].startdate = this.medicationModal.querySelector("input#start-date").value;
    this.medications[id].writtendate = this.medicationModal.querySelector("input#written-date").value;

    this.logAdd(this.medications[id]);
    this.refreshTables();
    console.log("INFO: Medication "+id+" was added to the list of current medications.");
}
/*
 *   Writes all the data in the PageManager.medications dict to the tables.
 *   NOTE:  The tables in page manager must be properly set by calling PageManager.setTables(current, suggested);
 */
PageManager.prototype.writeTables = function(){

    console.log("INFO: writeTables()");
    var working = jQuery.extend(true, {}, this.medications) //create a copy.

        //loop through all the medications in the PageManager's medciations.
        for(var key in working){
            console.log(key, working[key]);
            if(
                    working[key].state == "new" &&
                    working[key].associatedmed != undefined && 
                    working[working[key].associatedmed] != undefined &&
                    working[working[key].associatedmed].state == "current"
              ){
                //Write a new row in the new meds table
                var newRow = this.suggestedMedsTable.insertRow(-1);
                var c0 = newRow.insertCell(0);
                var c1 = newRow.insertCell(1);
                var c2 = newRow.insertCell(2);
                c0.innerHTML = addUpdateButton(key);
                c1.innerHTML = working[key].displayString();
                c2.innerHTML = addCancelIcon(key);

                //Write a new row in the current meds table
                var newRow = this.currentMedsTable.insertRow(-1);
                var c0 = newRow.insertCell(0);
                var c1 = newRow.insertCell(1);
                var c2 = newRow.insertCell(2);
                c0.innerHTML = "";
                c1.innerHTML = working[working[key].associatedmed].displayString();
                c2.innerHTML = "";

                //remove both the new and current med from the working list
                //as they are both displayed now.
                delete working[working[key].associatedmed];
                delete working[key];
            }
            else if(
                    working[key].state == "new" &&
                    working[key].associatedmed == ""  
                   ){
                //Write a new row in the new meds table
                var newRow = this.suggestedMedsTable.insertRow(-1);
                var c0 = newRow.insertCell(0);
                var c1 = newRow.insertCell(1);
                var c2 = newRow.insertCell(2);
                c0.innerHTML = addAddButton(key);
                c1.innerHTML = working[key].displayString();
                c2.innerHTML = addCancelIcon(key);

                //Write a new row in the current meds table
            var newRow = this.currentMedsTable.insertRow(-1);
            var c0 = newRow.insertCell(0);
            var c1 = newRow.insertCell(1);
            var c2 = newRow.insertCell(2);
            c0.innerHTML = "";
            c1.innerHTML = displayEmptyMed();
            c2.innerHTML = "";

            //delete medication from the working list
            delete working[key];
        }
        else if(
            working[key].state == "current" &&
            working[key].associatedmed == ""  
        ){
            //Write a new row in the new meds table
            var newRow = this.suggestedMedsTable.insertRow(-1);
            var c0 = newRow.insertCell(0);
            var c1 = newRow.insertCell(1);
            var c2 = newRow.insertCell(2);
            c0.innerHTML = "";
            c1.innerHTML = displayEmptyMed();
            c2.innerHTML = "";

            //Write a new row in the current meds table
            var newRow = this.currentMedsTable.insertRow(-1);
            var c0 = newRow.insertCell(0);
            var c1 = newRow.insertCell(1);
            var c2 = newRow.insertCell(2);
            c0.innerHTML = addEditButton(key);
            c1.innerHTML = working[key].displayString();
            c2.innerHTML = "";

            //delete medication from the working list
            delete working[key];
        }
        else if(
            working[key].state == "current" &&
            working[key].associatedmed != undefined && 
            working[working[key].associatedmed] != undefined &&
            working[working[key].associatedmed].state == "new"
        ){
            //Write a new row in the new meds table
            var newRow = this.suggestedMedsTable.insertRow(-1);
            var c0 = newRow.insertCell(0);
            var c1 = newRow.insertCell(1);
            var c2 = newRow.insertCell(2);
            c0.innerHTML = addUpdateButton(key);
            c1.innerHTML = working[working[key].associatedmed].displayString();
            c2.innerHTML = addCancelIcon(key);

            //Write a new row in the current meds table
            var newRow = this.currentMedsTable.insertRow(-1);
            var c0 = newRow.insertCell(0);
            var c1 = newRow.insertCell(1);
            var c2 = newRow.insertCell(2);
            c0.innerHTML = "";
            c1.innerHTML = working[key].displayString();
            c2.innerHTML = "";

            //delete medications from the working list
            delete working[working[key].associatedmed];
            delete working[key];
        }else if(
            working[key].state == "discontinued" &&
            !working[key].associatedmed
        ){
            //Write a new row in the new meds table
            var newRow = this.suggestedMedsTable.insertRow(-1);
            var c0 = newRow.insertCell(0);
            var c1 = newRow.insertCell(1);
            var c2 = newRow.insertCell(2);
            c0.innerHTML = "";
            c1.innerHTML = displayEmptyMed();
            c2.innerHTML = "";

            //Write a new row in the current meds table
            var newRow = this.currentMedsTable.insertRow(-1);
            var c0 = newRow.insertCell(0);
            var c1 = newRow.insertCell(1);
            var c2 = newRow.insertCell(2);
            c0.innerHTML = "";
            c1.innerHTML = displayEmptyMed();
            c2.innerHTML = "";
            
        }else if(
            working[key].state == "discontinued" &&
            working[key].associatedmed != undefined &&
            working[working[key].associatedmed].state == "current"
        ){
            //Write a new row in the new meds table
            var newRow = this.suggestedMedsTable.insertRow(-1);
            var c0 = newRow.insertCell(0);
            var c1 = newRow.insertCell(1);
            var c2 = newRow.insertCell(2);
            c0.innerHTML = "";
            c1.innerHTML = displayEmptyMed();
            c2.innerHTML = "";

            //Write a new row in the current meds table
            var newRow = this.currentMedsTable.insertRow(-1);
            var c0 = newRow.insertCell(0);
            var c1 = newRow.insertCell(1);
            var c2 = newRow.insertCell(2);
            c0.innerHTML = addEditButton(working[key].associatedmed);
            c1.innerHTML = working[working[key].associatedmed].displayString();
            c2.innerHTML = "";
            
        }else if(
            working[key].state == "discontinued" &&
            working[key].associatedmed != undefined &&
            working[working[key].associatedmed].state == "new"
        ){
            //Write a new row in the new meds table
            var newRow = this.suggestedMedsTable.insertRow(-1);
            var c0 = newRow.insertCell(0);
            var c1 = newRow.insertCell(1);
            var c2 = newRow.insertCell(2);
            c0.innerHTML = "";
            c1.innerHTML = working[working[key].associatedmed].displayString();
            c2.innerHTML = "";

            //Write a new row in the current meds table
            var newRow = this.currentMedsTable.insertRow(-1);
            var c0 = newRow.insertCell(0);
            var c1 = newRow.insertCell(1);
            var c2 = newRow.insertCell(2);
            c0.innerHTML = addEditButton(working[key].associatedmed);
            c1.innerHTML = working[working[key].associatedmed].displayString();
            c2.innerHTML = "";
            
        }


    }
    console.log("INFO: Tables written");
}

/*
* Manages the height of the table rows so that they all remain the same height
* even when text starts to wrap.
*/
PageManager.prototype.resizeTableRows = function(){
    var max_height = 0;
    for(var i=0; i < this.suggestedMedsTable.rows.length; i++){
        if(this.suggestedMedsTable.rows[i].offsetHeight > max_height){
            max_height = this.suggestedMedsTable.rows[i].offsetHeight;
        }
    }
    for(var i=0; i < this.currentMedsTable.rows.length; i++){
        if(this.currentMedsTable.rows[i].offsetHeight > max_height){
            max_height = this.currentMedsTable.rows[i].offsetHeight;
        }
    }

    for(var i=0; i < this.suggestedMedsTable.rows.length; i++){
        this.suggestedMedsTable.rows[i].style.height = max_height+"px";
    }
    for(var i=0; i < this.currentMedsTable.rows.length; i++){
        this.currentMedsTable.rows[i].style.height = max_height+"px";
    }
}

/*
* Clears the form fields in the medicationModal
*/
PageManager.prototype.clearModalFields = function(){
    this.medicationModal.querySelector("h4#modal-label span#med-modal-string").innerHTML = "";
    this.medicationModal.querySelector("input#brandname").value="";
    this.medicationModal.querySelector("input#genericname").value="";
    this.medicationModal.querySelector("input#dose").value="";
    this.modalResetDropdowns('form');
    this.modalResetDropdowns('unit');
    this.modalResetDropdowns('route');
    this.modalResetDropdowns('freq1');
    this.medicationModal.querySelector("input#prn").checked="";
    this.medicationModal.querySelector("input#min").value="1";
    this.medicationModal.querySelector("input#max").value="1";
    this.medicationModal.querySelector("input#quantity").value="";
    this.medicationModal.querySelector("input#repeats").value="";
    this.modalResetDropdowns('duration');
    this.modalResetDropdowns('duration-unit');
    this.medicationModal.querySelector("textarea#instructions").value="";
    this.medicationModal.querySelector("input#externalprovider").checked="";
    this.medicationModal.querySelector("input#longterm").checked="";
    this.medicationModal.querySelector("input#start-date").value="";
    this.medicationModal.querySelector("input#written-date").value="";

    //set the onclick for the save button to be adding a new medication by default, this will be reset to something else
    //if the action is not to add a new medication.
    this.medicationModal.querySelector("button#modal-save-button").setAttribute("onclick", "pm.addNewMedication();");

    //disable the discontinue button
    this.medicationModal.querySelector("button#modal-discontinue-button").classList.add("disabled");
    this.medicationModal.querySelector("button#modal-discontinue-button").setAttribute("onclick", "");
}

/*
* Computes the number of tablets/servings of the medication based on 
* the specified duration and the frequency.
* Also uses the min/max fields but will default to 1 for those if they are not 
* specified.  Changes the values in the medicationModal.
*/
PageManager.prototype.autoCalculateModalFields = function(){
    console.log("in autoCalculateModalFields()");
    if(
        (this.medicationModal.querySelector("select#duration-unit").value != "" ||
          this.medicationModal.querySelector("input#duration-unit-other").value != "") &&
        (this.medicationModal.querySelector("select#duration").value != "" ||
         this.medicationModal.querySelector("input#duration-other").value != "") &&
        (this.medicationModal.querySelector("select#freq1").value != "" ||
        this.medicationModal.querySelector("input#freq1-other").value != "")
    ){
        var dur_in_days = this.getDurationInDays();
        var freq_unit_in_days = this.getFreqUnitInDays();
        var freq_value = this.getFreqValue();
        var multiplyer = this.getMaxValue(); //represents min and max

        var computed = dur_in_days/freq_unit_in_days;
        console.log(computed);
        computed = computed*freq_value;
        computed = computed*multiplyer;

        console.log("computed medication quanitity to be: "+ computed);

        if(computed != 0 && !isNaN(computed)){
            this.medicationModal.querySelector("input#quantity").value = computed;
        }

    }else{
        //do nothing there is not enough information.
    }
}

/*
* Gets the the maximum value in the min and max fields and returns it as an integer.
* If the max is not specified then it will use the min field.
* If both the min and max fields are given it will use the max field
* If neither the min and max fields are given it will default to 1.
*/
PageManager.prototype.getMaxValue = function(){
    var max  = this.medicationModal.querySelector("input#max").value;
    var min  = this.medicationModal.querySelector("input#min").value;
    console.log("int getMaxValue() with min:"+min+" and max:"+max);
    if(max != ""){
        return parseInt(max);
    }else if(min != ""){
        return parseInt(min);
    }else{
        //return one for the default values
        return 1;
    }
}

/*
* Uses regexs to determine numiercal value of the frequency in terms of
* the number of days.
* For example "twice per day" would have a numerical value of 2 and 
* "every twelve hours" would have a value of 2 because there are 2x12 hours in a day.
* Returns 0 if it cannot match a value.
*/
PageManager.prototype.getFreqValue = function(){
    var value  = this.medicationModal.querySelector("select#freq1").value;
    if(value == ""){
        value = this.medicationModal.querySelector("input#freq1-other").value;
    }
    console.log("in getFreqValue() with: "+value);
    if(value.match("once") != null || value.match("one")){
        value = 1;
    }
    else if(value.match("twice") != null || value.match("two")){
        value = 2;
    }
    else if(value.match("three")){
        value = 3;
    }
    else if(value.match("four")){
        value = 4;
    }
    else if(value.match("4 hours")){
        value = 6;
    }
    else if(value.match("8 hours")){
        value = 3;
    }
    else if(value.match("12 hours")){
        value = 2;
    }
    else if(value.match("daily") != null || value.match("one")){
        value = 1;
    }
    else{
        value = 0;  
    }
    console.log("returning from getFreqValue() with: "+value);
    return value;
}

/*
* Returns the number of days in the specified duration.  
* uses both the units and number fields for duration to determine this value
* for example 3 months has 90 days (3*30 days/month)
*/
PageManager.prototype.getDurationInDays = function(){
    if(this.medicationModal.querySelector("select#duration").value != ""){
        var value = parseInt(this.medicationModal.querySelector("select#duration").value);
    }else{
        var value = parseInt(this.medicationModal.querySelector("input#duration-other").value);
    }
    if(this.medicationModal.querySelector("select#duration").value != ""){
        var unit = this.medicationModal.querySelector("select#duration-unit").value;
    }else if(this.medicationModal.querySelector("select#duration").value != ""){
        var unit = this.medicationModal.querySelector("input#duration-unit-other").value;
    }else{
        var unit = "days";
    }
    if(unit == "" || value == ""){
        return 0;
    }
    if(unit == "days"){
         unit = 1;
    }else if(unit == "months") {
        unit = 30;
    }else if(unit == "weeks"){
         unit = 7;
    }else if(unit == "years") {
        unit = 365;
    }else{
         unit = 0;
    }
    console.log("computed value for days in the duration: "+value*unit);
    return value*unit; 
}

/*
* Returns the number of days that are in the frequency unit.
* daily returns 1
* weekly returns 7
* hourly returns 1 (1 day)
* monthly returns 30
* yearly returns 365
*
* If no value is found returns 0
*/
PageManager.prototype.getFreqUnitInDays = function(){
    if(this.medicationModal.querySelector("select#freq1").value != ""){
        var value = this.medicationModal.querySelector("select#freq1").value;
    }else if(this.medicationModal.querySelector("input#freq1-other").value != ""){
        var value = this.medicationModal.querySelector("input#freq1-other").value;
    }else{
        var value = "daily";
    }
    console.log("in getFreqUnitInDays() with value="+value);

    if(value == ""){ return 0;}
    //check for matches and then assign the appropriate number of days
    //that the frequency corresponds too.
    if(value.match("hour") != null){
        value = 1;
    }else if(value.match("daily") != null){
        value = 1;
    }else if(value.match("day") != null){
        value = 1;
    }else if (value.match("weekly") != null) {
        value = 7;
    }else if (value.match("week") != null){
        value = 7;
    }else if (value.match("monthly") != null) {
        value = 30;
    }else if (value.match("month") != null) {
        value = 30;
    }else if (value.match("yearly") != null) {
        value = 365;
    }else if (value.match("yearly") != null) {
        value = 365;
    }else {
        value = 0;
    }
    console.log("returning from getFreqUnitInDays() with: "+value);
    return value;
}

/*
* Resets the dropdown menus for the input type identified by str
* @param str = the element to reset for.
*/
PageManager.prototype.modalResetDropdowns = function(str){
    //check to make sure it is not already in the right state
    if($('#medicationModal select#'+str+'').hasClass("hidden")){
        $('#medicationModal select#'+str).removeClass("hidden");
        $('#medicationModal input#'+str+'-other').addClass("hidden");
    }
    //clear the values of both
    this.medicationModal.querySelector("input#"+str+"-other").value="";
    this.medicationModal.querySelector("select#"+str).value="";
    //add a listener to the object
    this.medicationModal.querySelector("select#"+str).setAttribute("onchange", "pm.otherOptionListener('"+str+"');");
}
/*
* Updates the fields in the modal based on the what is the medication identified
* by the parameter id.
* @param id - the id of the medication to populate the modals fields with.
* @param action - the action to take after the modal is saved, gets passed toe the saveMedicationFields
*/
PageManager.prototype.updateModalFields = function(id, action){
    var med = this.medications[id];
    this.medicationModal.querySelector("h4#modal-label span#med-modal-string").innerHTML = med.displayString();
    this.medicationModal.querySelector("input#brandname").value=med.brandname;
    this.medicationModal.querySelector("input#genericname").value=med.ingredientname;
    this.medicationModal.querySelector("input#dose").value=med.dose;
    
    this.updateModalManageDropdowns('unit', med.unit);
    this.updateModalManageDropdowns('form', med.form);
    this.updateModalManageDropdowns('freq1', med.freq1);
    this.medicationModal.querySelector("input#prn").checked=med.prn;
    this.medicationModal.querySelector("input#min").value=med.min;
    this.medicationModal.querySelector("input#max").value=med.max;
    this.medicationModal.querySelector("input#quantity").value=med.quantity;
    this.medicationModal.querySelector("input#repeats").value=med.repeats;
    this.updateModalManageDropdowns('route', med.route);
    this.updateModalManageDropdowns('duration', med.duration);
    this.updateModalManageDropdowns('duration-unit', med.durationunit);
    
    this.medicationModal.querySelector("textarea#instructions").value=med.instructions;
    this.medicationModal.querySelector("input#externalprovider").checked=med.externalprovider;
    this.medicationModal.querySelector("input#longterm").checked=med.longterm;
    this.medicationModal.querySelector("input#start-date").value=med.startdate;
    this.medicationModal.querySelector("input#written-date").value=med.writtendate;
    console.log("INFO: writtendate"+ med.writtendate);
    console.log("INFO: startdate"+ med.startdate);
    if(med.startdate == undefined){
        med.startdate = new Date();
    }
    if(med.writtendate ==undefined){
        med.writtendate = new Date();
    }

    //set the onclick for the save button on the modal with this medication id.
    this.medicationModal.querySelector("button#modal-save-button").setAttribute("onclick", "pm.saveMedicationFields("+id+", \""+action+"\");");

    //only enable the discontinue button if the medication is being edited, not updated, or added.
    if(action =="edit"){
        this.medicationModal.querySelector("button#modal-discontinue-button").classList.remove("disabled");
        this.medicationModal.querySelector("button#modal-discontinue-button").setAttribute("onclick", "pm.discontinueMedication("+id+");");
        
    }else{
        this.medicationModal.querySelector("button#modal-discontinue-button").classList.add("disabled");
        this.medicationModal.querySelector("button#modal-discontinue-button").setAttribute("onclick", "");
    }
    this.autoCalculateModalFields();

    console.log("INFO: Modal updated for medication: "+id);
}

/*
* Hides/shows the select or text input for the element with the string str as the id
* based on whether the value in the Medication object is in the dropdown or not.
* @param str - the string of the element to manage
*/
PageManager.prototype.updateModalManageDropdowns = function(str, handle){
    if(optionInSelect(this.medicationModal.querySelector("select#"+str), handle)){
        this.medicationModal.querySelector("select#"+str).value=handle;
        this.medicationModal.querySelector("input#"+str+"-other").value="";
        if($('#medicationModal select#'+str).hasClass("hidden")){
            $('#medicationModal select#'+str).removeClass("hidden");
            $('#medicationModal input#'+str+'-other').addClass("hidden");
        }else{
            //do nothing, it is already showing
        }
    }else{
        this.medicationModal.querySelector("input#"+str+"-other").value=handle;
        this.medicationModal.querySelector("select#"+str).value='';
        if($('#medicationModal input#'+str+'-other').hasClass("hidden")){
            $('#medicationModal input#'+str+'-other').removeClass("hidden");
            $('#medicationModal select#'+str).addClass("hidden");
        }else{
            //do nothing, it is already showing
        }
    }
    //assign listeners
    this.medicationModal.querySelector("select#"+str).setAttribute("onchange", "pm.otherOptionListener('"+str+"');");
}

/*
* Changes the state of a medication to distcontinued if it was a current med
* @param id = the id of the med to be changed. 
*/
PageManager.prototype.discontinueMedication = function(id){
    if(this.medications[id].state == "new"){
        //do nothing
    }else{
        this.logDiscontinue(this.medications[id]);
    }
    this.medications[id].state = "discontinued";
    this.refreshTables();
}

/*
* Saves the data in the medication field to the medication identified by id.
* @param id - the id of the medicaiton
* @param action - the action that the modal is to do on the save, used to change the medication.state.
*/
PageManager.prototype.saveMedicationFields = function(id, action){
    var preedit = jQuery.extend(true, {}, this.medications[id]) //create a copy of the pre edited state.

    this.medications[id].brandname = this.medicationModal.querySelector("input#brandname").value;
    this.medications[id].ingredientname = this.medicationModal.querySelector("input#genericname").value;
    this.medications[id].dose = this.medicationModal.querySelector("input#dose").value;
    this.medications[id].unit = this.checkDropDownForValue("unit");
    this.medications[id].form = this.checkDropDownForValue("form");
    this.medications[id].route = this.checkDropDownForValue("route");
    this.medications[id].freq1 = this.checkDropDownForValue("freq1");
    this.medications[id].prn = this.medicationModal.querySelector("input#prn").checked;
    this.medications[id].min = this.medicationModal.querySelector("input#min").value;
    this.medications[id].max = this.medicationModal.querySelector("input#max").value;
    this.medications[id].repeats = this.medicationModal.querySelector("input#repeats").value;
    this.medications[id].quantity = this.medicationModal.querySelector("input#quantity").value;
    this.medications[id].duration = this.checkDropDownForValue("duration");
    this.medications[id].durationunit = this.checkDropDownForValue("duration-unit");
    this.medications[id].instructions = this.medicationModal.querySelector("textarea#instructions").value;
    this.medications[id].longterm = this.medicationModal.querySelector("input#longterm").checked;
    this.medications[id].externalprovider = this.medicationModal.querySelector("input#externalprovider").checked;
    this.medications[id].startdate = this.medicationModal.querySelector("input#start-date").value;
    this.medications[id].writtendate = this.medicationModal.querySelector("input#written-date").value;
    console.log("INFO: Medication "+id+" updated based on fields in modal");

    
    //handle actions for the medication, update, edit or add.
    //this will cause the medications to move from one table to the other.
    if(action == "update"){
        this.logUpdate(this.medications[id], this.medications[this.medications[id].associatedmed]);
        this.medications[id].state = "current";
        delete this.medications[this.medications[id].associatedmed]; //delete the reference to old medication
        this.medications[id].associatedmed = "";
    }else if(action == "add"){
        this.logAdd(this.medications[id]);
        this.medications[id].state = "current";
    }else if(action == "edit"){
        this.logEdit(preedit, this.medications[id]);
    }

    //refresh tables
    this.refreshTables();
}

PageManager.prototype.checkDropDownForValue = function(str){
    console.log("INFO: in checkDropDownForValue() with param :"+str+":");
    console.log("INFO:  "+this.medicationModal.querySelector("input#"+str+"-other").value);
    if(this.medicationModal.querySelector("input#"+str+"-other").value != ''){
        return this.medicationModal.querySelector("input#"+str+"-other").value;
    }else{
        return this.medicationModal.querySelector("select#"+str).value;
    }
}

/*
* Constructs a string that describes the update and sends it to change logger to be displayed on page.
* @param newmed - the medication object that the medication will be updated too.
* @param oldmed - the old medication that will be changed.
*/
PageManager.prototype.logUpdate = function(newmed, oldmed){
    //this.logMessage("<em>"+oldmed.displayStringVerbose()+"</em> will be updated to <em>"+newmed.displayStringVerbose()+"</em>.");
    this.logMessage("<div title='Medication was: "+oldmed.displayStringVerbose(false, false)+"'>Update <em>"+newmed.displayStringVerbose(true, true)+"</em>.</div>");
}

/*
* Constructs a string that describes that the med has been discontinued and sends it to the logger 
* @param med - the medication object of the medication that will be added.
*/
PageManager.prototype.logDiscontinue = function(med){
    this.logMessage("Discontinue <em>"+med.displayStringVerbose(true, true)+"</em>");
}
/*
* Constructs a string that describes the add and sends it to change logger to be displayed on page.
* @param newmed - the medication object of the medication that will be added.
*/
PageManager.prototype.logAdd = function(newmed){
    this.logMessage("Add <em>"+newmed.displayStringVerbose(true, true)+"</em>.");
}
/*
* Constructs a string that describes the add and sends it to change logger to be displayed on page.
* @param preedit - the medication object before it was edited.
* @param postedit - the medication object after it was edited.
*/
PageManager.prototype.logEdit = function(preedit, postedit){
    this.logMessage("<div title='Medication was: "+preedit.displayStringVerbose(false, false)+"'>Edit <em>"+postedit.displayStringVerbose(true, true)+"</em>.</div>");
}

/*
* Writes the log message to logging table by creating a new row.
* @param str - the string to be written to the table
*/ 
PageManager.prototype.logMessage = function(str){
    var newRow = this.logTable.insertRow(-1);
    var c0 = newRow.insertCell(0);
    c0.innerHTML = str;
}

/*
* Clears and then redraws the tables on the page based what 
* is in the page manager's medications
*/
PageManager.prototype.refreshTables = function(){
    this.clearTables();
    this.writeTables();
    this.resizeTableRows();
    console.log("INFO: Tables refreshed.");
}

/*
* The action to take for onchange of a dropdown menu in the medication modal
* @param str - the id of the element to preform the action on.
*/
PageManager.prototype.otherOptionListener = function(str){ 
    console.log("otherOptionListner with param :"+str+":");
    if($("#medicationModal select#"+str+"").val() == "other"){
        $('#medicationModal input#'+str+'-other').removeClass("hidden");
        $('#medicationModal select#'+str+'').addClass("hidden");
        console.log("INFO: Changed classes for "+str+" based on toggle")
    }else{
        //do nothing
    }
}


//------------------------END PageManager CLASS --------------------//


//------------------------START GLOBAL FUNCTIONS --------------------//
/*
* Returns an assoiative array of the inputs in a form by their id.
*/
function getAssArray(inputs){
    var values={};
    for (var i=0; i<inputs.length; i++){
        values[inputs[i].id] = inputs[i].value;
    }
    return values;
}

function size(obj) {
    var size = 0, key;
    for (key in obj) {
        if (obj.hasOwnProperty(key)){
        size++;
        }
    }
    return size;
}

/*
* Function for running tests on the page.
* DEPRECATED
*/
function __runTest(){
    var pm = new PageManager("medForms");
    pm.setTables("suggestedMeds", "currentMeds");
    pm.refreshTables();
}

function displayEmptyMed(){
    return "----------";
}

function addUpdateButton(id){
    return "<button data-toggle='modal' onclick='pm.updateModalFields("+id+", \"update\");' role='button' href='#medicationModal' class='btn btn-mini btn-warning'>Update</button>";
}

function addAddButton(id){
    return "<button data-toggle='modal' onclick='pm.updateModalFields("+id+", \"add\");' role='button' href='#medicationModal' class='btn btn-mini btn-success'>Add</button>";
}

function addEditButton(id){
    return "<button data-toggle='modal' onclick='pm.updateModalFields("+id+", \"edit\");' role='button'href='#medicationModal' class='btn btn-mini btn-primary'>Edit</button>";
}

function addCancelIcon(id){
    return "<a href='#' onclick='pm.discontinueMedication("+id+");'><i class='icon-remove-sign'></i></a>";
}

function loadContent(pm){
    pm.setTables("suggestedMeds", "currentMeds");
    pm.setLogTable("changes");
    pm.setMedicationModal("medicationModal");
    pm.refreshTables();
}

function initPageManager(){
    return new PageManager("medForms");
}

/*
* determines if an option (o_string) is in the select element (element)
*/
function optionInSelect(element, o_string){
    var exists = false;
    for(var i = 0, opts = element.options; i < opts.length; ++i){
       if( opts[i].value === o_string )
       {
          exists = true; 
          break;
       }
    }
       return exists;
}

/*
 * Send XHR to the drug ref service and get back info on the drug identified by id.
 */
function getDrugData(id, pm){
    var url = "/drugref/DrugrefService"
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange=function(){
        if (xmlhttp.readyState==4 && xmlhttp.status==200){
            xmlDoc=xmlhttp.responseXML;
            //get the ID of the medication that was picked
            console.log("Recieved response about drug: "+id);
            console.log(xmlDoc);
            var din = xmlDoc.getElementsByTagName("struct")[0].childNodes[1].childNodes[1].firstChild.nodeValue;
            console.log("DIN is: "+din);
            var atc = xmlDoc.getElementsByTagName("struct")[0].childNodes[4].childNodes[1].firstChild.nodeValue;
            console.log("ATC is: "+atc);
            var form = xmlDoc.getElementsByTagName("struct")[0].childNodes[5].childNodes[1].firstChild.nodeValue;
            console.log("FORM is: "+form);
            var ai = getDrugGenericNames(id, pm);
            console.log(pm);
            pm.updateModalManageDropdowns('form',form.toLowerCase());
        }
    }
    xmlhttp.open("POST",url,true);
    var xml = '<?xml version="1.0"?><methodCall><methodName>get_drug_2</methodName><params><param><value>'+id+'</value></param><param><value><boolean>true</boolean></value></param></params></methodCall>'
    console.log("Sending XHR with POST as: "+xml);
    xmlhttp.send(xml);
}

function getDrugGenericNames(id, pm){
    var url = "/drugref/DrugrefService"
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange=function(){
        if (xmlhttp.readyState==4 && xmlhttp.status==200){
            xmlDoc=xmlhttp.responseXML;
            //get the ID of the medication that was picked
            console.log("Recieved generic drug names  about drug: "+id);
            console.log(xmlDoc);
            var ai = xmlDoc.getElementsByTagName("struct")[0].childNodes[0].childNodes[1].firstChild.nodeValue;
            console.log("ACITVE INREDIENT(S) ARE/IS: "+ai);
            pm.medicationModal.querySelector("input#genericname").value=ai;
        }
    }
    xmlhttp.open("POST",url,true);
    var xml = '<?xml version="1.0"?><methodCall><methodName>get_generic_name</methodName><params><param><value>'+id+'</value></param></params></methodCall>';
    console.log("Sending XHR with POST as: "+xml);
    xmlhttp.send(xml);
}

/*
 * Un-used function to send an XHR.   
 */
function sendXHR(name){
    var url = "/drugref/DrugrefService"
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange=function(){
        if (xmlhttp.readyState==4 && xmlhttp.status==200){
            xmlDoc=xmlhttp.responseXML;
            //get the ID of the medication that was picked
            var id = xmlDoc.getElementsByTagName("params")[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[2].childNodes[1].childNodes[0].nodeValue;
        }
    }
    xmlhttp.open("POST",url,true);
    var xml = '<?xml version="1.0"?><methodCall><methodName>list_search_element</methodName><params><param><value>'+this.medicationModal.querySelector("input#brandname").value+'</value></param></params></methodCall>'
    console.log("Sending XHR with POST as: "+xml);
    xmlhttp.send(xml);
}
//------------------------END GLOBAL FUNCTIONS --------------------//
