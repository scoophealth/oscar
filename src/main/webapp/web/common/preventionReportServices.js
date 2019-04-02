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
angular.module("preventionReportServices", [])
	.service("preventionReportService", function ($http,$q,$log) {
		return {
		apiPath:'../ws/rs',
		configHeaders: {headers: {"Content-Type": "application/json","Accept":"application/json"}},
		configHeadersWithCache: {headers: {"Content-Type": "application/json","Accept":"application/json"},cache: true},
	      
		getList: function () {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/reporting/preventionReport/getList',
                method: "GET",
                headers: this.configHeaders,
              }).then(function(response){
            	  deferred.resolve(response.data);
                },function (data, status, headers) {
                	deferred.reject("An error occured while getting k2a content");
                });
           return deferred.promise;
        },
        getAllActiveProviders: function(){
    		var deferred = $q.defer();
        
        $http({
            url: this.apiPath+'/providerService/providers_json',
            method: "GET"
         }).then(function (data, status, headers, config) {
        	 	deferred.resolve(data.data.content);
         },function (data, status, headers, config) {
        	 	deferred.reject("An error occured while fetching provider teams");
         });

        return deferred.promise;
        },
        saveNewReport: function(newReport){
           	var deferred = $q.defer();
           	 $http.post(this.apiPath+'/reporting/preventionReport/saveNew',newReport,this.configHeaders).then(function(response){
               	console.log("returned from /K2AInit",response.data);
               	deferred.resolve(response.data);
               },function(){
               	console.log("error initializing k2a");
               	deferred.reject("An error occured while trying to initialize k2a");
               });
        
             return deferred.promise;
        },
        runReport: function(id,providerNo){
        		var pro = {};
        		pro.providerNo = providerNo;
           	var deferred = $q.defer();
           	 $http.post(this.apiPath+'/reporting/preventionReport/runReport/'+id,pro,this.configHeaders).then(function(response){
               	console.log("returned from /K2AInit",response.data);
               	deferred.resolve(response.data);
               },function(){
               	console.log("error initializing k2a");
               	deferred.reject("An error occured while trying to initialize k2a");
               });
        
             return deferred.promise;
        },
        getReport: function(id){
       	var deferred = $q.defer();
       	 $http.post(this.apiPath+'/reporting/preventionReport/getReport/'+id,this.configHeaders).then(function(response){
           	console.log("returned from /getReport",response.data);
           	deferred.resolve(response.data);
           },function(){
           	console.log("error initializing k2a");
           	deferred.reject("An error occured while trying to initialize k2a");
           });
    
         return deferred.promise;
        },
        dectivateReport: function(id){
           	var deferred = $q.defer();
           	 $http.post(this.apiPath+'/reporting/preventionReport/dectivateReport/'+id,this.configHeaders).then(function(response){
               	console.log("returned from /dectivateReport",response.data);
               	deferred.resolve(response.data);
               },function(){
               	console.log("error initializing k2a");
               	deferred.reject("An error occured while trying to initialize k2a");
               });
        
             return deferred.promise;
            }
        
    };
});
