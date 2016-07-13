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

/*
 * A model class used to represent a drug.
 *
 * Transfer objects from the Rx API (/ws/rx/) are parsed into Drug
 * instances of this Drug class. The UI features operate
 * on instances of this Drug class.
 *
 * Instances of the Drug class may be formatted as transfer objects
 * and sent to the Rx API.
 */

function Drug(demo, provider) {

    this.id = null;
    this.brandName = null;
    this.name = null;
    this.genericName = null;
    this.atc = null;
    this.regionalIdentifier = null;

    this.strength = null;
    this.strengthUnit = null;

    this.demographic = demo || null;
    this.provider = provider || null;
    this.externalProvider = null;

    this.instructions = null;
    this.takeMin = 0;
    this.takeMax = 0;
    this.route = null;
    this.frequency = null;
    this.duration = null;
    this.durationUnit = null;
    this.additionalInstructions = null;

    this.prn = false;
    this.form = null;
    this.method = null;
    this.repeats = null;
    this.quantity = null;

    this.endDate = null;
    this.rxDate = null;

    this.archived = false;
    this.archivedReason = null;
    this.archivedDate = null;

    this.longTerm = false;
    this.noSubs = false;

    // work-around for db model not supporting
    // both generic names and active ingredients.
    this.gn = null;

    // flag to indicate that this is a new medication
    // that has not yet been commited to the database.
    this.newMed = false;

    // an array of drug objects that represent
    // the history of this drug.
    this.history = null;
    
}

/**
 * Sets this Drug's fields to be copies of
 * the input drug, d.
 *
 * @param d {Drug} - drug to copy.
 */
Drug.prototype.copy = function(d){

    if(!d) return;

    this.id = d.id;
    this.brandName = d.brandName;
    this.genericName = d.genericName;
    this.name = d.name;
    this.atc = d.atc;
    this.regionalIdentifier = d.regionalIdentifier;

    this.strength = d.strength;
    this.strengthUnit = d.strengthUnit;

    this.demographic = d.demographic;
    this.provider = d.provider;
    this.externalProvider = d.externalProvider;

    this.instructions = d.instructions;
    this.takeMax = d.takeMax;
    this.takeMin = d.takeMin;
    this.route = d.route;
    this.frequency = d.frequency;
    this.duration = d.duration;
    this.durationUnit = d.durationUnit;
    this.additionalInstructions = d.additionalInstructions;

    this.prn = d.prn;
    this.form = d.form;
    this.method = d.method;
    this.repeats = d.repeats;
    this.quantity = d.quantity;

    this.endDate = d.endDate === null ? null : new Date(d.endDate);
    this.rxDate = d.rxDate === null ? null : new Date(d.rxDate);

    this.archived = d.archived;
    this.archivedDate = d.archivedDate === null ? null : new Date(d.archivedDate);
    this.archivedReason = d.archivedReason;

    this.gn = d.gn;

};

Drug.prototype.toDrugTransferObject = function (drugKey) {

    // default behaviour drugKey = true.
    if(drugKey === undefined){
        drugKey = true;
    }

    // start and end dates default to today.
    var tempEndDate = this.endDate ? (new Date(this.endDate)) : new Date();

    var tempStartDate = null;

    // We need to switch on the possible source of the date.
    // if it is already a date just assign it.
    if(this.rxDate instanceof Date){
        tempStartDate = new Date();
    }else if (typeof this.rxDate === "string"){

        // if it is a string then we need to make a date out of it.

        // we assume rxDate will be yyyy-MM-dd
        var l = this.rxDate.split("-");
        tempStartDate = new Date();
        tempStartDate.setDate(parseInt(l[2]));
        tempStartDate.setMonth(parseInt(l[1] - 1));
        tempStartDate.setFullYear(parseInt(l[0]));

        tempStartDate.setHours(0);
        tempStartDate.setMinutes(0);
        tempStartDate.setSeconds(0);

    }else{
        tempStartDate = new Date();
    }

    var generic = null;

    if (this.genericName && this.genericName !== "$$custom$$") {

        generic = this.genericName;

        if (this.strength && this.strengthUnit) {

            var s = " "+this.strength + " " + this.strengthUnit;

            // check to make sure the strength is not already included in the
            // generic name, removing this check will cause the
            // strength to continually be appended to the generic name
            // field the the DB.
            if(generic.indexOf(s) <= -1) {
                generic += s;
            }
        }

    }

    var tempInstructions = this.getInstructionLine();

    var obj = {
            drugId : this.id,
            brandName: this.name || this.brandName,
            genericName: generic,
            atc: this.atc,
            regionalIdentifier: this.regionalIdentifier,

            strength: this.strength,
            strengthUnit: this.strengthUnit,

            demographicNo: this.demographic,
            providerNo: this.provider,
            externalProvider: this.externalProvider,

            instructions: tempInstructions,
            takeMin: this.takeMin,
            takeMax: this.takeMax,
            route: this.route,
            form: this.form,
            frequency: this.frequency,
            method: this.method,
            duration: this.duration,
            durationUnit: this.durationUnit,
            prn: this.prn || false,
            repeats: this.repeats,
            additionalInstructions: this.additionalInstructions,

            endDate: tempEndDate.toISOString(),
            rxDate: tempStartDate.toISOString(),

            longTerm: this.longTerm || false,
            noSubstitutions: this.noSubs || false,

            archived: false,
            archivedReason: this.archivedReason,
            archivedDate: this.archivedDate
    };

    return drugKey ? {drug : obj} : obj ;

};

/**
 * Populates the drug based on information passed from a
 * DrugTo1 object from the /rx API.
 *
 * See: org.oscarehr.ws.rest.to.model.DrugTo1
 *
 * @param obj - object that represents the drug.
 */
Drug.prototype.fromDrugTransferObject = function (obj) {

    this.id = obj.drugId;

    this.brandName = obj.brandName;
    this.genericName = obj.genericName;
    this.atc = obj.atc;
    this.regionalIdentifier = obj.regionalIdentifier;
    this.provider = obj.providerNo;
    this.demographic = obj.demographicNo;

    this.takeMin = obj.takeMin;
    this.takeMax = obj.takeMax;
    this.frequency = obj.frequency;
    this.duration = obj.duration;
    this.durationUnit = obj.durationUnit;
    this.route = obj.route;
    this.form = obj.form;
    this.strength = obj.strength;
    this.strengthUnit = obj.strengthUnit;
    this.method = obj.method;
    this.repeats = obj.repeats;
    this.prn = obj.prn;
    this.instructions = obj.instructions;
    this.additionalInstructions = obj.additionalInstructions;

    this.archived = obj.archived;
    this.archivedReason = obj.archivedReason;

    if (obj.archivedDate) {
        this.archivedDate = new Date(obj.archivedDate);
    } else {
        this.archivedDate = null;
    }

    if (obj.rxDate) {
        this.rxDate = new Date(obj.rxDate);
    } else {
        this.rxDate = null;
    }

    if (obj.endDate) {
        this.endDate = new Date(obj.endDate);
    } else {
        this.endDate = null;
    }

    this.noSubs = obj.noSubstitutions;
    this.longTerm = obj.longTerm;
    this.externalProvider = obj.externalProvider;

    // not a new medication if we are unpacking from
    // a transfer object.
    this.newMed = false;
};

/**
 *
 * @param t the transfer object from the server that contains drug product details
 *  has structure like:
 *      { "regionalId":INT, "atc":STRING, "form":STRING, "name":STRING, "genericName":STRING,
 *          "components": [ {"name":STRING,"strength":DOUBLE,"unit":STRING}, ... ]
 *      }
 */
Drug.prototype.populateFromDrugSearchDetails = function (t) {

    this.brandName = t.name;
    this.gn = t.genericName;
    this.form = t.form;
    this.atc = t.atc;
    this.regionalIdentifier = t.regionalId;

    if (!(t.component instanceof Array)) {

        this.genericName = t.genericName;
        this.strength = t.components.strength;
        this.strengthUnit = t.components.unit;

    } else {


    }

};
/**
 * Updates the current drug's instructions to reflect
 * the instructions in the input object. Used for
 * keeping the drug object in sync with the text
 * typed in the instructions field.
 *
 * @param x an object which has instruction information.
 *  Has structure like:
 *      {
 *          duration: NUMBER,
 *          durationUnit: STRING,
 *          frequency : STRING,
 *          method : STRING
 *          prn : BOOLEAN
 *          takeMax : NUMBER
 *          takeMin : NUMBER
 *      }
 */
Drug.prototype.applyInstructions = function (x) {

    if (!x || !x.drug) return;

    if (x.drug.takeMin) this.takeMin = x.drug.takeMin;

    if (x.drug.takeMax) this.takeMax = x.drug.takeMax;

    if (x.drug.frequency) this.frequency = x.drug.frequency.toUpperCase();

    if (x.drug.method) this.method = x.drug.method;

    this.prn = x.drug.prn || this.prn;

    if (x.drug.route) this.route = x.drug.route.toUpperCase();

    // Call this to update the instruction field to reflect
    // the retrieved instructions.
    // this.updateInstructionField();

};

/**
 * Updates the Drug's instruction field
 * based on the values of the instruction component fields (takeMin, takeMax etc...)
 */
Drug.prototype.updateInstructionField = function () {

    var s = "";

    if (this.method) {
        s += this.method + " ";
    }

    if (this.takeMax && this.takeMin) {
        s += this.takeMin + "-" + this.takeMax + " "
    } else if (this.takeMax) {
        s += this.takeMax + " ";
    } else if (this.takeMin) {
        s += this.takeMin + " ";
    }

    if (this.frequency) {
        s += this.frequency + " ";
    }

    if (this.route) {
        s += this.route + " ";
    }

    if (this.prn) {
        s += "PRN ";
    }

    this.instructions = s;

};

/**
 * Creates the drug instruction line that can be
 * used for printing the drug or saving in the "special"
 * field of the data base.
 *
 * This should be used for the following:
 * - Printing the Rx to the prescription object.
 * - Displaying drug info in "ReadyToPrescribe" field.
 * - Sending to database's "special" field
 */
Drug.prototype.getInstructionLine = function(){

    var s = "";

    s += (this.name || this.brandName) +  " - ";

    if(this.method) s += this.method + " ";
    
    if(this.takeMin !== null && this.takeMax !== null){
        s += this.takeMin + "-"+this.takeMax + " ";
    }else if(this.takeMin !== null && this.takeMax === null){
        s += this.takeMin + " ";
    }else if(this.takeMin === null && this.takeMax !== null){
        s += this.takeMax + " ";
    }

    if(this.route) s+= this.route + " ";

    if(this.frequency) s+= this.frequency + " ";

    if(this.prn) s+= "PRN ";

    if(this.duration !== null && this.duration !== undefined && this.durationUnit){
        s += "- Duration:"+ this.duration +this.durationUnit+" ";
    }

    if(this.quantity !== null && this.quantity !== undefined){
        s += "- Qty:" + this.quantity+" ";
    }

    if(this.repeats !== null && this.repeats !== undefined){
        s += "- Repeats:" + this.repeats+" ";
    }

    return s;
    
};

Drug.prototype.resetInstructions = function () {

    // resets the instructions for the drug.

    this.takeMax = null;
    this.takeMin = null;
    this.prn = null;
    this.route = null;
    this.frequency = null;

};

/**
 * Determine if the current drug is valid.
 * The drug must have a name, instructions, and rxDate.
 *
 * @return {boolean} - true if the medication is valid, false otherwise.
 */
Drug.prototype.checkValid = function () {

    if (this.name === undefined
        || this.name === null
        || this.name === "") {

        return false;

    } else if (this.instructions === undefined
        || this.instructions === null
        || this.instructions === "") {

        return false;

    } else if (!this.rxDate) {

        return false;

    } else {

        return true;

    }

};


/**
 * Applies the data from the input favorite to this drug.
 * Overwrites any existing drug information.
 *
 * @param f {Favorite} the favorite to apply
 */
Drug.prototype.applyFavorite = function(f){

    this.copy(f.drug);

    this.rxDate = new Date();
    this.endDate = calculateEndDate(
        {
            start : this.rxDate,
            duration : this.duration,
            durationUnit : this.durationUnit,
            repeats : this.repeats
        }
    );

    this.quantity = calculateQuantity(
        this.frequency, this.duration, this.durationUnit, this.takeMax || this.takeMin
    );

};

/**
 * Applies information about the prescription to the drug object.
 * Overwrites existing prescription information if it exists.
 *
 * @param rx {object} has structure like:
 *  { duration : number, durationUnit : 'D'|'M'|'W',
 *      repeats : number, start : Date, end : Date, ... }
 */
Drug.prototype.applyPrescriptionInformation = function(rx){

    this.duration = rx.duration;
    this.durationUnit = rx.durationUnit;
    this.repeats = rx.repeats;
    this.rxDate = new Date(rx.start);
    this.endDate = new Date(rx.end);

    // local vars

    var freq = null;

    // if the quantity field is set then use that
    // otherwise auto-calculate the quanity.
    if(rx.quantity){

        this.quantity = rx.quantity;

    }else{
        // get the quantity
        this.quantity = calculateQuantity(this.frequency, this.duration, this.durationUnit, this.takeMax)

    }

};

// Lookup table for the frequency codes
// gives a numerator and denominator for
// each frequency code.
// - Numerator is always a number
// - Denominator is always a time unit
// Example: TID == 3 per Day
frequencyLookupTable = {
    'OD'    : {num: 1, den: 'D'},
    'QAM'   : {num: 1, den: 'D'},
    'QPM'   : {num: 1, den: 'D'},
    'QHS'   : {num: 1, den: 'D'},
    'BID'   : {num: 2, den: 'D'},
    'TID'   : {num: 3, den: 'D'},
    'QID'   : {num: 4, den: 'D'},
    'Q1H'   : {num: 24, den: 'D'},
    'Q2H'   : {num: 12, den: 'D'},
    'Q1-2H' : {num: 24, den: 'D'},
    'Q4H'   : {num: 1, den: 'D'},
    'Q4-6H' : {num: 6, den: 'D'},
    'Q6H'   : {num: 4, den: 'D'},
    'Q8H'   : {num: 3, den: 'D'},
    'Q12H'  : {num: 2, den: 'D'},
    'Q1WEEK': {num: 1, den: 'W'},
    'Q2WEEK': {num: 2, den: 'W'},
    'Q1MONTH': {num: 1, den: 'M'},
    'Q3MONTH': {num: 3, den: 'M'}
};

// Conversion table that contains ratios for
// translating between time units.
timeUnitConversionTable = {
    'D': {  'D': 1,       'W': 7,      'M': 28 },
    'W': {  'D': 1 / 7,   'W': 1,      'M': 4  },
    'M': {  'D': 1 / 28,  'W': 4 / 28, 'M': 1  }
};

/**
 * Determines quantity of 'pills' to dispense given the frequency
 * duration and number of pills per dose.
 *
 * @param freq {string} - a frequency code, number of doses per day.
 * @param dur {number} - the duration
 * @param durUnit {string} - unit of duration, qualifies the duration number, one of 'D'|'W'|'M'
 * @param numPerDose {number} - number of pills per dose, defaults to 1.
 *
 * @return {number|null} the calculated number of pills, null if there was an error.
 */
function calculateQuantity(freq, dur, durUnit, numPerDose){

    numPerDose = numPerDose || 1;

    // sanity checks.
    if(!freq || !dur || !durUnit) return null;

    // cast things to uppercase to handle lookups
    freq = freq.toUpperCase();
    durUnit = durUnit.toUpperCase();

    // sanity check
    if(!frequencyLookupTable[freq] || !timeUnitConversionTable[durUnit]) return null;

    // 0) get the numerator and denominator for the frequency code given
    //    freq now has structure { num : number, den : 'D'|'W'|'M' }
    freq = frequencyLookupTable[freq];

    // 1) convert duration units to units of frequency
    dur = dur * timeUnitConversionTable[freq.den][durUnit];
    durUnit = freq.den;

    // 2) multiply out the number of pills required.
    return Math.ceil(dur * freq.num) * numPerDose;

}

/**
 * Given a start date, duration, and repeats, works out the end date.
 *
 * @param obj {object} has structure like:
 *  { start : Date, duration : number, durationUnit : 'D'|'W'|'M', repeats : number ... }
 */
function calculateEndDate(obj){

    if(!obj.start || !obj.durationUnit){
        return null;
    }

    var d = new Date(obj.start);

    switch(obj.durationUnit){
        case 'D':
            d.setDate(d.getDate() + obj.duration * (obj.repeats <= 0 ? 1 : obj.repeats) );
            break;
        case 'W':
            d.setDate(d.getDate() + 7 * obj.duration * (obj.repeats <= 0 ? 1 : obj.repeats));
            break;
        case 'M':
            d.setMonth(d.getMonth() + obj.duration * (obj.repeats <= 0 ? 1 : obj.repeats));
            break;
        default:
            break;
    }

    return d;

}


function Favorite(drug, name){
    this.drug = drug || null;
    this.name = name || null;
}

/**
 * Unpacks the favorite transfer object and
 * sets the relevant fields of this Favorite object, including the Drug property.
 *
 * @param t {Object} the favorite transfer object ot unpack.
 * @param demo
 * @param provider
 */
Favorite.prototype.fromFavoriteTransferObject = function(t, demo, provider){

    this.drug = new Drug(demo, provider);

    // populate drug object here...

    this.drug.brandName = t.brandName;
    this.drug.genericName = t.genericName;
    this.drug.atc = t.atc;
    this.drug.regionalIdentifier = t.regionalIdentifier;
    this.drug.form = t.form;
    this.drug.frequency = t.frequency;
    this.drug.instructions = t.instructions;
    this.drug.method = t.method;
    this.drug.route = t.route;
    this.drug.prn = t.prn;
    this.drug.takeMin = t.takeMin;
    this.drug.takeMax = t.takeMax;
    this.drug.repeats = t.repeats;

    this.drug.duration = t.duration;
    this.drug.durationUnit = t.durationUnit;

    this.drug.provider = provider;
    this.drug.demographic = demo;

    this.name = t.favoriteName;

};

Favorite.prototype.toTransferObject = function(){

    return {
        favoriteName : this.name,
        favoriteId : null,
        brandName : this.drug.brandName || null,
        genericName : this.drug.genericName || null,
        atc : this.drug.atc || null,
        regionalIdentifier : this.drug.regionalIdentifier || null,
        form : this.drug.form || null,
        frequency : this.drug.frequency || null,
        method : this.drug.method || null,
        instructions : this.drug.instructions || null,
        route : this.drug.route || null,
        prn : this.drug.prn || false,
        takeMin : this.drug.takeMin || null,
        takeMax : this.drug.takeMax || null,
        repeats : this.drug.repeats || 0,
        quantity : this.drug.quantity || null,

        duration : this.drug.duration || null,
        durationUnit : this.drug.durationUnit || null

    };

};

Favorite.prototype.fromDrug = function(d, name){

    if(!this.drug) this.drug = new Drug();

    this.drug.brandName = d.brandName;
    this.drug.genericName = d.genericName;
    this.drug.atc = d.atc;
    this.drug.regionalIdentifier = d.regionalIdentifier;
    this.drug.form = d.form;
    this.drug.frequency = d.frequency;
    this.drug.instructions = d.instructions;
    this.drug.method = d.method;
    this.drug.route = d.route;
    this.drug.prn = d.prn;
    this.drug.takeMax = d.takeMax;
    this.drug.takeMin = d.takeMin;
    this.drug.quantity = d.quantity;

    this.drug.duration = d.duration;
    this.drug.durationUnit = d.durationUnit;
    this.drug.repeats = d.repeats;

    this.drug.provider = d.provider;

    this.name = name || this.drug.instructions;

};