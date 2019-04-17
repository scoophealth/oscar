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
angular.module("phrServices", [])
	.service("phrService", function ($http,$q,$log) {
		return {
		apiPath:'../ws/rs',
		configHeaders: {headers: {"Content-Type": "application/json","Accept":"application/json"}},
		configHeadersWithCache: {headers: {"Content-Type": "application/json","Accept":"application/json"},cache: true},
	      
        getK2aFeed: function (startPoint,numberOfRows) {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/rssproxy/rss?key=k2a&startPoint=' + startPoint + '&numberOfRows=' + numberOfRows,
                method: "GET",
                headers: this.configHeaders,
              }).then(function(response){
            	  deferred.resolve(response.data);
                },function (data, status, headers) {
                	deferred.reject("An error occured while getting phr content");
                });
           return deferred.promise;
        },
        isPHRInit: function () {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/app/PHRActive',
                method: "GET",
                headers: this.configHeaders,
              }).then(function(response){
            	  deferred.resolve(response.data);
                },function (data, status, headers) {
                	deferred.reject("An error occured while getting phr content");
                });
           return deferred.promise;
        },
        isPHRConsentCheck: function () {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/app/PHRActiveAndConsentConfigured',
                method: "GET",
                headers: this.configHeaders,
              }).then(function(response){
            	  deferred.resolve(response.data);
                },function (data, status, headers) {
                	deferred.reject("An error occured while getting phr content");
                });
           return deferred.promise;
        },
        initPHR: function(clinicName){
           	var deferred = $q.defer();
           	 $http.post(this.apiPath+'/app/PHRInit',clinicName,this.configHeaders).then(function(response){
               	console.log("returned from /PHRInit",response.data);
               	deferred.resolve(response.data);
               },function(data, status, headers){
               	console.log("error initializing phr",data, status, headers);
               	deferred.reject("An error occured while trying to fetching data from  PHR");
               });
        
             return deferred.promise;
        },
        createPHRuser: function(provider){
           	var deferred = $q.defer();
           	 $http.post(this.apiPath+'/app/PHRCreateUser',provider,this.configHeaders).then(function(response){
               	console.log("returned from /PHRCreateUser",response.data);
               	deferred.resolve(response.data);
               },function(data, status, headers){
               	console.log("error initializing phr",data, status, headers);
               	deferred.reject("An error occured while trying to fetching data from  PHR");
               });
        
             return deferred.promise;
        },
        linkPHRUser: function(provider){
           	var deferred = $q.defer();
           	 $http.post(this.apiPath+'/app/PHRLinkUser',provider,this.configHeaders).then(function(response){
               	console.log("returned from /PHRInit",response.data);
               	deferred.resolve(response.data);
               },function(data, status, headers){
               	console.log("error initializing phr",data, status, headers);
               	deferred.reject("An error occured while trying to fetching data from  PHR");
               });
        
             return deferred.promise;
        },
        phrAbilities: function(){
           	var deferred = $q.defer();
           	 $http.post(this.apiPath+'/app/PHRAbilities',this.configHeaders).then(function(response){
               	console.log("returned from /PHRAbilities",response.data);
               	deferred.resolve(response);
               },function(data, status, headers){
               	console.log("error initializing phr",data, status, headers);
               	deferred.reject("An error occured while trying to initialize k2a");
               });
        
             return deferred.promise;
        },
        phrSetupAudit: function(){
           	var deferred = $q.defer();
           	 $http.post(this.apiPath+'/app/PHRAuditSetup',this.configHeaders).then(function(response){
               	console.log("returned from /PHRAuditSetup",response.data);
               	deferred.resolve(response);
               },function(data, status, headers){
               	console.log("error initializing phr",data, status, headers);
               	deferred.reject("An error occured while trying to initialize k2a");
               });
        
             return deferred.promise;
        }
	
    };
});
