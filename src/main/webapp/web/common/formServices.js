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
angular.module("formServices", [])
	.service("formService", function ($http,$q,$log) {
		return {
		apiPath:'../ws/rs/forms',
		configHeaders: {headers: {"Content-Type": "application/json","Accept":"application/json"}},
		configHeadersWithCache: {headers: {"Content-Type": "application/json","Accept":"application/json"},cache: true},
        getAllFormsByHeading: function(demographicNo,heading){
        	var deferred = $q.defer();
        	 $http.get(this.apiPath+'/'+demographicNo+'/all?heading='+heading,this.configHeaders).success(function(data){
            	console.log(data);
            	deferred.resolve(data);
            }).error(function(){
            	console.log("error fetching forms");
            	deferred.reject("An error occured while fetching items");
            });
     
          return deferred.promise;
        },
        getAllEncounterForms: function(){
        	var deferred = $q.defer();
        	 $http.get(this.apiPath+'/allEncounterForms',this.configHeaders).success(function(data){
            	deferred.resolve(data.content);
            }).error(function(){
            	console.log("error fetching encounter");
            	deferred.reject("An error occured while fetching encounter forms");
            });
     
          return deferred.promise;
        },
        getSelectedEncounterForms: function(){
        	var deferred = $q.defer();
        	 $http.get(this.apiPath+'/selectedEncounterForms',this.configHeaders).success(function(data){
            	deferred.resolve(data.content);
            }).error(function(){
            	console.log("error fetching selected encounter");
            	deferred.reject("An error occured while fetching selected encounter forms");
            });
     
          return deferred.promise;
        },
        getCompletedEncounterForms: function(demographicNo){
        	var deferred = $q.defer();
        	 $http.get(this.apiPath+'/'+demographicNo+'/completedEncounterForms',this.configHeaders).success(function(data){
            	deferred.resolve(data);
            }).error(function(){
            	console.log("error fetching completed encounter forms");
            	deferred.reject("An error occured while fetching completed encounter forms");
            });
     
          return deferred.promise;
        },        
        getAllEForms: function(){
        	var deferred = $q.defer();
        	 $http.get(this.apiPath+'/allEForms',this.configHeaders).success(function(data){
            	deferred.resolve(data.content);
            }).error(function(){
            	console.log("error fetching eforms");
            	deferred.reject("An error occured while fetching eforms");
            });
     
          return deferred.promise;
        },
        getGroupNames: function(){
        	var deferred = $q.defer();
        	 $http.get(this.apiPath+'/groupNames',this.configHeaders).success(function(data){
        		 //console.log('got group names !' + data.toSource());
            	deferred.resolve(data.content);
            }).error(function(){
            	console.log("error fetching eforms");
            	deferred.reject("An error occured while fetching group names");
            });
     
          return deferred.promise;
        },
        getFormGroups: function(){
        	var deferred = $q.defer();
        	 $http.get(this.apiPath+'/getFormGroups',this.configHeaders).success(function(data){
        		 console.log(data);
            	deferred.resolve(data);
            }).error(function(){
            	console.log("error fetching eforms");
            	deferred.reject("An error occured while fetching group names");
            });
     
          return deferred.promise;
        },
        getFavouriteFormGroup: function(){
        	var deferred = $q.defer();
        	 $http.get(this.apiPath+'/getFavouriteFormGroup',this.configHeaders).success(function(data){
        		 console.log(data);
            	deferred.resolve(data);
            }).error(function(){
            	console.log("error fetching eforms");
            	deferred.reject("An error occured while fetching group names");
            });
     
          return deferred.promise;
        },
        getFormOptions: function(demographicNo){
        	var deferred = $q.defer();
        	 $http.get(this.apiPath+'/'+demographicNo+'/formOptions',this.configHeaders).success(function(data){
        		 //console.log('got group names !' + data.toSource());
            	deferred.resolve(data);
            }).error(function(){
            	console.log("error fetching eforms");
            	deferred.reject("An error occured while fetching group names");
            });
     
          return deferred.promise;
        }
    };
});