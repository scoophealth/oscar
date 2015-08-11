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
angular.module("uxServices", [])
	.service("summaryService", function ($http,$q,$log) {
		return {
		apiPath:'../ws/rs/recordUX',
        getSummaryHeaders: function (demographicNo,key) {
        	//
            console.log("Debug: calling left");
            var deferred = $q.defer();
            $http.get(this.apiPath+'/'+demographicNo+'/summary/'+key).success(function(data){
              console.log(data);
              deferred.resolve(data);
          }).error(function(){
        	  console.log("error fetching items");
            deferred.reject("An error occured while fetching items");
          });
     
          return deferred.promise;
            
        },
        getFullSummary: function(demographicNo,summaryCode){
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'/'+demographicNo+'/fullSummary/'+summaryCode).success(function(data){
                deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while fetching items");
            });
       
            return deferred.promise;
        	
        },
        getFamilyHistory: function(demographicNo){
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'/'+demographicNo+'/getFamilyHistory').success(function(data){
                deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while fetching items");
            });
            return deferred.promise;
        },
        getMedicalHistory: function(demographicNo){
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'/'+demographicNo+'/getMedicalHistory').success(function(data){
                deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while fetching items");
            });
            return deferred.promise;
        },
    	getOngoingConcerns: function(demographicNo){
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'/'+demographicNo+'/getOngoingConcerns').success(function(data){
                deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while fetching items");
            });
            return deferred.promise;
    	},
    	getOtherMeds: function(demographicNo){
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'/'+demographicNo+'/getOtherMeds').success(function(data){
                deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while fetching items");
            });
            return deferred.promise;
    	},
    	getReminders: function(demographicNo){
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'/'+demographicNo+'/getReminders').success(function(data){
                deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while fetching items");
            });
            return deferred.promise;
    	},
    	getRiskFactors: function(demographicNo){
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'/'+demographicNo+'/getRiskFactors').success(function(data){
                deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while fetching items");
            });
            return deferred.promise;
    	}
    };
}).service("uxService", function($http,$q,$log){
   return {
	 apiPath:'../ws/rs/recordUX',
     menu: function (demographicNo) {
     	//
         console.log("Debug: calling left");
         var deferred = $q.defer();
         $http.get(this.apiPath+'/'+demographicNo+'/recordMenu').success(function(data){
           console.log(data);
           deferred.resolve(data);
       }).error(function(){
     	  console.log("error fetching items");
         deferred.reject("An error occured while fetching items");
       });
  
       return deferred.promise;
         
     },
 	searchTemplates: function(search,startIndex,itemsToReturn){
    	var deferred = $q.defer();
    	$http.post(this.apiPath+'/searchTemplates?startIndex='+startIndex + "&itemsToReturn="+itemsToReturn,search).success(function(data){
    		deferred.resolve(data);
        }).error(function(){
      	  console.log("error fetching items");
          deferred.reject("An error occured while fetching items");
        });
   
        return deferred.promise;
    },
 	getTemplate: function(name){
    	var deferred = $q.defer();
    	$http.post(this.apiPath+'/template',name).success(function(data){
    		deferred.resolve(data);
        }).error(function(){
      	  console.log("error fetching items");
          deferred.reject("An error occured while fetching items");
        });
   
        return deferred.promise;
    }
     
   };
});
