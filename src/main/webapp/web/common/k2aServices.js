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
	      
        getK2aFeed: function (startPoint,numberOfRows) {
        	var deferred = $q.defer();
        	$http({
                url: this.apiPath+'/rssproxy/rss?key=k2a&startPoint=' + startPoint + '&numberOfRows=' + numberOfRows,
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
           	 $http.post(this.apiPath+'/app/K2AInit',clinicName,this.configHeaders).success(function(data){
               	console.log("returned from /K2AInit",data);
               	deferred.resolve(data);
               }).error(function(){
               	console.log("error initializing k2a");
               	deferred.reject("An error occured while trying to initialize k2a");
               });
        
             return deferred.promise;
        },
	postK2AComment: function(post) {
		var deferred = $q.defer();
		var commentItem = {post};
		$http.post(this.apiPath+'/app/comment',commentItem).success(function(data){
		console.log("return from /comment",data);
		deferred.resolve(data);
		}).error(function(){
		  console.log("error posting comment to k2a");
		  deferred.reject("An error occured while trying to post a comment to k2a");
		});
	      return deferred.promise;
	},
	removeK2AComment: function(commentId) {
		var deferred = $q.defer();
		$http.delete(this.apiPath+'/app/comment/' + commentId).success(function(data){
		console.log("return from /comment/" + commentId,data);
		deferred.resolve(data);
		}).error(function(){
		  console.log("error removing comment from k2a");
		  deferred.reject("An error occured while trying to remove a comment from k2a");
		});
	      return deferred.promise;
	},
	preventionRulesList: function() {
		var deferred = $q.defer();
		$http.get(this.apiPath+'/resources/preventionRulesList').success(function(data){
		console.log("return from /preventionRulesList",data);
		deferred.resolve(data);
		}).error(function(){
		  console.log("error getting preventionRulesList");
		  deferred.reject("An error occured while trying to remove a comment from k2a");
		});
	      return deferred.promise;
	},loadPreventionRuleById: function(id) {
		var deferred = $q.defer();
		
		$http.post(this.apiPath+'/resources/loadPreventionRulesById/'+id.id,id,this.configHeaders).success(function(data){
		console.log("return from /loadPreventionRulesById",data);
		deferred.resolve(data);
		}).error(function(){
		  console.log("error loading loadPreventionRulesById from k2a");
		  deferred.reject("An error occured while trying to loadPreventionRulesById");
		});
	      return deferred.promise;
	},
	getCurrentPreventionRulesVersion: function() {
		var deferred = $q.defer();
		$http.get(this.apiPath+'/resources/currentPreventionRulesVersion').success(function(data){
		console.log("return from /getCurrentPreventionRulesVersion",data);
		deferred.resolve(data);
		}).error(function(){
		  console.log("error getCurrentPreventionRulesVersion");
		  deferred.reject("An error occured while trying to getCurrentPreventionRulesVersion");
		});
	      return deferred.promise;
	},
	
	luCodesList: function() {
		var deferred = $q.defer();
		$http.get(this.apiPath+'/resources/luCodesList').success(function(data){
		console.log("return from /luCodesList",data);
		deferred.resolve(data);
		}).error(function(){
		  console.log("error getting luCodesList");
		  deferred.reject("An error occured while trying to remove a comment from k2a");
		});
	      return deferred.promise;
	},
	loadLuCodesById: function(id) {
		var deferred = $q.defer();
		
		$http.post(this.apiPath+'/resources/loadLuCodesById/'+id.id,id,this.configHeaders).success(function(data){
		console.log("return from /loadLuCodesById",data);
		deferred.resolve(data);
		}).error(function(){
		  console.log("error loading loadLuCodesById from k2a");
		  deferred.reject("An error occured while trying to loadLuCodesById");
		});
	      return deferred.promise;
	},
	currentLuCodesVersion: function() {
		var deferred = $q.defer();
		$http.get(this.apiPath+'/resources/currentLuCodesVersion').success(function(data){
		console.log("return from /currentLuCodesVersion",data);
		deferred.resolve(data);
		}).error(function(){
		  console.log("error currentLuCodesVersion");
		  deferred.reject("An error occured while trying to currentLuCodesVersion");
		});
	      return deferred.promise;
	},
	getNotifications: function() {
		var deferred = $q.defer();
		$http.get(this.apiPath+'/resources/notifications').success(function(data){
		deferred.resolve(data);
		}).error(function(){
		  deferred.reject("An error occured while trying to getCurrentPreventionRulesVersion");
		});
	      return deferred.promise;
	},
	getMoreNotification: function(id) {
		var deferred = $q.defer();
		
		$http.post(this.apiPath+'/resources/notifications/readmore',id,this.configHeaders).success(function(data){
		deferred.resolve(data);
		}).error(function(){
		  deferred.reject("An error occured while trying to /resources/notifications/readmore");
		});
	      return deferred.promise;
	},
	ackNotification: function(id) {
		var deferred = $q.defer();
		$http.post(this.apiPath+'/resources/notifications/ack',id,this.configHeaders).success(function(data){
		deferred.resolve(data);
		}).error(function(){
		  deferred.reject("An error occured while trying to /resources/notifications/ack");
		});
	      return deferred.promise;
	}
	
    };
});
