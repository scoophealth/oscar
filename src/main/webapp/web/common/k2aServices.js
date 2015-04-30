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
angular.module("k2aServices", [])
	.service("k2aService", function ($http,$q,$log) {
		return {
		apiPath:'../ws/rs',
		configHeaders: {headers: {"Content-Type": "application/json","Accept":"application/json"}},
		configHeadersWithCache: {headers: {"Content-Type": "application/json","Accept":"application/json"},cache: true},
	      
        getK2aFeed: function () {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/rssproxy/rss?key=k2a',
                method: "GET",
                headers: this.configHeaders,
              }).success(function (data) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers) {
                	deferred.reject("An error occured while getting k2a content");
                });
           return deferred.promise;
        },
        isK2AInit: function () {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/app/K2AActive',
                method: "GET",
                headers: this.configHeaders,
              }).success(function (data) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers) {
                	deferred.reject("An error occured while getting k2a content");
                });
           return deferred.promise;
        },
        initK2A: function(clinicName){
           	var deferred = $q.defer();
           	 $http.post(this.apiPath+'/app/K2AInit',clinicName).success(function(data){
               	console.log("returned from /K2AInit",data);
               	deferred.resolve(data);
               }).error(function(){
               	console.log("error initializing k2a");
               	deferred.reject("An error occured while trying to initialize k2a");
               });
        
             return deferred.promise;
         }
    };
});