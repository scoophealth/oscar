/*

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

*/
angular.module("personaServices", [])
	.service("personaService", function ($http,$q,$log) {
		return {
		apiPath:'../ws/rs/persona',
		configHeaders: {headers: {"Content-Type": "application/json","Accept":"application/json"}},
		configHeadersWithCache: {headers: {"Content-Type": "application/json","Accept":"application/json"},cache: true},
	    
		getDashboardMenu: function() {
			var deferred = $q.defer();
			$http({
                url: this.apiPath+'/dashboardMenu',
                method: "GET",
                headers: this.configHeaders,
              }).success(function (data) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers) {
                	deferred.reject("An error occured while getting the dashboard menu from persona");
                });
           return deferred.promise;
		},
        getNavBar: function () {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/navbar',
                method: "GET",
                headers: this.configHeaders,
              }).success(function (data) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers) {
                	deferred.reject("An error occured while getting navbar from persona");
                });
           return deferred.promise;
        },
        getPatientLists: function () {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/patientLists',
                method: "GET",
                headers: this.configHeaders,
              }).success(function (data) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers) {
                	deferred.reject("An error occured while getting patient lists from persona");
                });
           return deferred.promise;
        },
        getPatientListConfig: function () {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/patientList/config',
                method: "GET",
                headers: this.configHeaders,
              }).success(function (data) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers) {
                	deferred.reject("An error occured while getting getPatientListConfig from persona");
                });
           return deferred.promise;
        },
        setPatientListConfig: function (patientListConfig) {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/patientList/config',
                method: "POST",
                data: patientListConfig,
                headers: this.configHeaders,
              }).success(function (data) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers) {
                	deferred.reject("An error occured while setting setPatientListConfig from persona");
                });
           return deferred.promise;
        },
        setCurrentProgram: function (programId) {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath + '/setDefaultProgramInDomain?programId='+programId,
                method: "GET",
                headers: this.configHeaders,
              }).success(function (data) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers) {
                	deferred.reject("An error occured while setting current");
                });
           return deferred.promise;
        },
        getDashboardPreferences: function () {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/preferences',
                method: "POST",
                data: {type:'dashboard'},
                headers: this.configHeaders,
              }).success(function (data) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers) {
                	deferred.reject("An error occured while getting preferences from persona");
                });
           return deferred.promise;
        },
        updateDashboardPreferences: function (prefs) {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/updatePreferences',
                method: "POST",
                data: JSON.stringify(prefs),
                headers: {'Content-Type': 'application/json'}
              }).success(function (data, status, headers, config) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers, config) {
                	deferred.reject("An error occured while updating preferences");
                });
           return deferred.promise;
        }
    };
});