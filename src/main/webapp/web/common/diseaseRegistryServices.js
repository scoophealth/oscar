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
angular.module("diseaseRegistryServices", [])
	.service("diseaseRegistryServices", function ($http,$q,$log) {
		return {
		apiPath:'../ws/rs/dxRegisty/',
		configHeaders: {headers: {"Content-Type": "application/json","Accept":"application/json"}},
		configHeadersWithCache: {headers: {"Content-Type": "application/json","Accept":"application/json"},cache: true},
		
        getQuickLists: function () {
            var deferred = $q.defer();
            $http.get(this.apiPath+'quickLists/',this.configHeadersWithCache).success(function(data){
            	console.log(data);
            	deferred.resolve(data);
            }).error(function(){
            	console.log("error fetching demographic");
            	deferred.reject("An error occured while fetching items");
            });
     
          return deferred.promise;
            
        },
        
        addToDxRegistry: function (demgraphicNo,disease) {
        	var deferred = $q.defer();
        	var issueToSend = {};
        	issueToSend.id = disease.id;
        	issueToSend.type = disease.type;
        	issueToSend.code = disease.code;
        	issueToSend.description = disease.description;
        	
            $http.post(this.apiPath+demgraphicNo+'/add',issueToSend).success(function(data){
            	console.log(data);
            	deferred.resolve(data);
          }).error(function(){
        	  console.log("error fetching items");
        	  deferred.reject("An error occured while fetching items");
          });
     
          return deferred.promise;
        },
        
        findLikeIssue: function(diagnosis){
        	var deferred = $q.defer();
        	$http.post(this.apiPath+'findLikeIssue',diagnosis).success(function(data){
            	console.log(data);
                deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while fetching items");
            });
       
            return deferred.promise;
        	
        }
    };
});