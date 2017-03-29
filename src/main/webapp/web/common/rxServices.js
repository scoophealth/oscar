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

angular.module("rxServices", [])
    .service("rxService", function ($http, $q, $log) {
        return {
            apiPath: '../ws/rs/rx',
            getMedications: function (demographicNo, status) {
                console.log("Debug: calling getMedications, demo="+demographicNo);
                var deferred = $q.defer();
                var queryPath = this.apiPath + "/drugs";
                if (status !== "") {
                    queryPath = queryPath + '/' + status
                }
                queryPath = queryPath + '?demographicNo=' + demographicNo;
                $http.get(queryPath).success(function (data) {
                    deferred.resolve(data.drug);
                }).error(function () {
                    console.log("error fetching items");
                    deferred.reject("An error occurred while fetching items");
                });

                return deferred.promise;
            },

            /**
             * Transforms the UI model object to an object
             * compatible with the API.
             *
             * @param obj {Drug} to send to the API.
             * @param cb {Function} a function to call when the request is complete,
             *  has signature function(boolean), boolean param is true if the request
             *  was successful false otherwise.
             *
             */
            addMedication: function(obj, cb){

                // obj needs to be transformed into a transfer object
                // than can be handled by the API.

                var d = obj.toDrugTransferObject();

                this._addMedicationRequest(obj.demographic, d).then(function(resp){
                    return cb(resp.success);
                });

            },

            /**
             * "private" worker function that actually makes the request to the
             * API to add the medication.
             *
             * @param demographicNo - the patient id for the medication.
             * @param med {Drug} - the drug object to add.
             * @returns {deferred.promise|{then, catch, finally}}
             * @private
             */
            _addMedicationRequest: function (demographicNo, med) {
                var deferred = $q.defer();
                var queryPath = this.apiPath + "/new";
                queryPath += "?demographicNo=" + demographicNo;

                $http.post(queryPath, med).success(function (data) {
                    deferred.resolve(data);
                }).error(function () {
                    console.log("Error, could not add Medication.");
                    deferred.reject("An error occurred while attempting to add a medication.");
                });

                return deferred.promise;

            },

            updateMedication: function (demo, med) {

                var deferred = $q.defer();
                var queryPath = this.apiPath + "/update";
                queryPath += "?demographicNo=" + demo;

                $http.post(queryPath, med).success(function (data) {
                    deferred.resolve(data);
                    console.log(data);
                }).error(function () {
                    console.log("Error, could not update Medication.");
                    deferred.reject("An error occurred while attempting to update a medication.");
                });

                return deferred.promise;

            },

            discontinueMedication : function(demo, medId, reason){
                reason = reason || "unknown";

                var deferred = $q.defer();
                var queryPath = this.apiPath + "/discontinue";
                queryPath += "?demographicNo=" + demo;
                queryPath += "&drugId=" + encodeURIComponent(medId);
                queryPath += "&reason=" + encodeURIComponent(reason);

                $http.post(queryPath).success(function (data) {
                    deferred.resolve(data);
                }).error(function () {
                    console.log("Error, could not discontinue Medication.");
                    deferred.reject("An error occurred while attempting to update a medication.");
                });

                return deferred.promise;
            },

            /**
             *
             * @param demoNo {number}
             * @param drugs {Array<Drug>}
             */
            prescribe : function(demoNo, drugs, cb){

                var toSend = [];

                // transform to transfer objects for the API.
                for(var m in drugs){
                    toSend.push(drugs[m].toDrugTransferObject(false));
                }

                var obj = {drug : toSend};

                this._prescribe(demoNo, obj).then(function(resp){
                    return cb(resp.success);
                });
            },

            /**
             *
             * @param demo
             * @param meds
             * @returns {deferred.promise|{then, catch, finally}}
             * @private
             */
            _prescribe : function(demo, meds){

                var deferred = $q.defer();
                var queryPath = this.apiPath + "/prescribe";
                queryPath += "?demographicNo=" + demo;

                $http.post(queryPath, meds).success(function (data) {
                    deferred.resolve(data);
                    console.log(data);
                }).error(function () {
                    deferred.reject("An error occurred while attempting to prescribe");
                });

                return deferred.promise;

            },
            lookup : function(s){

                var deferred = $q.defer();
                var queryPath = this.apiPath + "lookup/search?string="+s;

                $http.get(queryPath).success(function (data) {
                    deferred.resolve(data);
                    console.log(data);
                }).error(function () {
                    deferred.reject("An error occurred while attempting to search for medication");
                });

                return deferred.promise;

            },

            getMedicationDetails : function(s){

                var deferred = $q.defer();
                var queryPath = this.apiPath + "lookup/details?id="+s;

                $http.get(queryPath).success(function (data) {
                    deferred.resolve(data);
                }).error(function () {
                    deferred.reject("An error occurred while attempting to get medication details");
                });

                return deferred.promise;

            },

            parseInstructions  : function (i) {

                var deferred = $q.defer();
                var queryPath = this.apiPath + "lookup/parse?input="+i;

                $http.post(queryPath).success(function (data) {
                    deferred.resolve(data);
                }).error(function () {
                    deferred.reject("An error occurred while attempting to parse instructions");
                });

                return deferred.promise;

            },

            history : function(drug, demo, cb){

                if(!drug){
                    cb(null);
                }

                this._history(drug.id, demo).then(function(resp){

                    if(resp && resp.drug){

                        var drugList = [];
                        var tempDrug = null;

                        if(resp.drug instanceof Array){
                            // several drugs returned in history
                            for(var i = 0; i < resp.drug.length; i++){
                                tempDrug = new Drug();
                                tempDrug.fromDrugTransferObject(resp.drug[i]);
                                drugList.push(tempDrug);
                            }
                        }else{
                            // only one drug
                            tempDrug = new Drug();
                            tempDrug.fromDrugTransferObject(resp.drug);
                            drugList.push(tempDrug);
                        }

                        cb(drugList);

                    }else{
                        console.log("error getting history for drug: "+ drug.id +" failed!");
                        cb(null);
                    }

                });

            },

            _history : function(id, demo){

                var deferred = $q.defer();
                var queryPath = this.apiPath + "/history";
                queryPath += "?demographicNo=" + demo;
                queryPath += "&id=" + id;

                $http.get(queryPath).success(function (data) {
                    deferred.resolve(data);
                }).error(function () {
                    deferred.reject("An error occurred while attempting to get history");
                });

                return deferred.promise;

            },

            addFavorite : function(fav, cb){

                var obj = {favorite : fav.toTransferObject()};

                this._addFavorite(obj).then(
                    function(resp){
                        console.log(resp);
                        cb(resp);
                    }
                );

            },

            /**
             * Makes a call to API to add a new favorite drug.
             *
             * @param fav the favorite object to add.
             * @returns {deferred.promise|{then, catch, finally}}
             */
            _addFavorite : function(fav){
                var deferred = $q.defer();
                var queryPath = this.apiPath + "/favorites";

                console.log(fav);

                $http.post(queryPath, fav).success(function (data) {
                    deferred.resolve(data);
                    console.log(data);
                }).error(function () {
                    deferred.reject("An error occurred while attempting to add favorite");
                });

                return deferred.promise;
            },

            _favorites : function(){

                var deferred = $q.defer();
                var queryPath = this.apiPath + "/favorites";

                $http.get(queryPath).success(function (data) {
                    deferred.resolve(data);
                }).error(function () {
                    deferred.reject("An error occurred while attempting to get favorites");
                });

                return deferred.promise;
            },

            favorites : function(demo, provider, cb){

                this._favorites().then(function(resp){

                    /*
                     * @property {object} resp
                     * @property {boolean} resp.success
                     * @property {object|array} resp.drugs
                     */

                    console.log("favorites:");
                    console.log(resp);


                    if(resp && resp.success && resp.drugs){

                        var favs = [];
                        var tmp = null;


                        if(resp.drugs instanceof Array){

                            for(var i = 0; i < resp.drugs.length; i++){

                                tmp = new Favorite();
                                tmp.fromFavoriteTransferObject(resp.drugs[i], demo, provider);
                                favs.push(tmp);

                            }

                        }else{
                            tmp = new Favorite();
                            tmp.fromFavoriteTransferObject(resp.drugs, demo, provider);
                            favs.push(tmp);
                        }

                        cb(favs);

                    }else{
                        cb([]);
                    }
                });
            }
        };
    });
