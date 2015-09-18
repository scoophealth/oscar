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
angular.module("billingServices", [])
	.service("billingService", function ($http,$q,$log) {
		return {
		apiPath:'../ws/rs/',
		configHeaders: {headers: {"Content-Type": "application/json","Accept":"application/json"}},
		configHeadersWithCache: {headers: {"Content-Type": "application/json","Accept":"application/json"},cache: true},
		getUniqueServiceTypes: function () {
			var deferred = $q.defer();
			$http.get(this.apiPath+'billing/uniqueServiceTypes',this.configHeadersWithCache).success(function(data){
				console.log(data);
				deferred.resolve(data.content);
			}).error(function(){
				console.log("error fetching billing service types");
				deferred.reject("An error occured while fetching billing service types");
			});
			return deferred.promise;
		},
		
		getBillingRegion: function () {
			var deferred = $q.defer();
			$http.get(this.apiPath+'billing/billingRegion',this.configHeadersWithCache).success(function (data) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject("An error occured while setting bilingRegion");
			});
			return deferred.promise;
		},
		
		getDefaultView: function () {
			var deferred = $q.defer();
			$http.get(this.apiPath+'billing/defaultView',this.configHeadersWithCache).success(function (data) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject("An error occured while setting defaultView");
			});
			return deferred.promise;
		},
	};
});
