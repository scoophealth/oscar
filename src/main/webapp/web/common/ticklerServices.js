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
angular.module("ticklerServices", [])
	.service("ticklerService", function ($http,$q,$log) {
		return {
		apiPath:'../ws/rs/tickler',
		configHeaders: {headers: {"Content-Type": "application/json","Accept":"application/json"}},
		configHeadersWithCache: {headers: {"Content-Type": "application/json","Accept":"application/json"},cache: true},
	      
        setCompleted: function (ticklerIds) {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/complete',
                method: "POST",
                data: JSON.stringify({"ticklers":ticklerIds}),
                headers: {'Content-Type': 'application/json'}
              }).success(function (data, status, headers, config) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers, config) {
                	deferred.reject("An error occured while setting ticklers to completed status");
                });
           return deferred.promise;
        },
        setDeleted: function (ticklerIds) {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/delete',
                method: "POST",
                data: JSON.stringify({"ticklers":ticklerIds}),
                headers: {'Content-Type': 'application/json'}
              }).success(function (data, status, headers, config) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers, config) {
                	deferred.reject("An error occured while setting ticklers to deleted status");
                });
           return deferred.promise;
        },
        search: function (filter, startIndex,limit) {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/search?startIndex='+startIndex+'&limit='+limit,
                method: "POST",
                data: JSON.stringify(filter),
                headers: {'Content-Type': 'application/json'}
              }).success(function (data, status, headers, config) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers, config) {
                	deferred.reject("An error occured while searching ticklers");
                });
           return deferred.promise;
        },
        update: function (tickler) {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/update',
                method: "POST",
                data: JSON.stringify(tickler),
                headers: {'Content-Type': 'application/json'}
              }).success(function (data, status, headers, config) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers, config) {
                	deferred.reject("An error occured while updating tickler");
                });
           return deferred.promise;
        },getTextSuggestions: function () {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/textSuggestions',
                method: "GET"
              }).success(function (data, status, headers, config) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers, config) {
                	deferred.reject("An error occured while getting tickler text suggestions");
                });
           return deferred.promise;
        },
        add: function (tickler) {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/add',
                method: "POST",
                data: JSON.stringify(tickler),
                headers: {'Content-Type': 'application/json'}
              }).success(function (data, status, headers, config) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers, config) {
                	deferred.reject("An error occured while saving tickler");
                });
           return deferred.promise;
        },
        getTicklerOverdueCount: function (demographicNo) {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/'+demographicNo+'/count/overdue',
                method: "GET",
                headers: this.configHeaders,
              }).success(function (data) {
            	  deferred.resolve(data);
                }).error(function (data, status, headers) {
                	deferred.reject("An error occured while getting tickler overdue count");
                });
           return deferred.promise;
        }
    };
});