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
angular.module("consultServices", [])
	.service("consultService", function ($http,$q,$log) {
		return {
		apiPath:'../ws/rs/consults/',
		configHeaders: {headers: {"Content-Type": "application/json","Accept":"application/json"}},
		configHeadersWithCache: {headers: {"Content-Type": "application/json","Accept":"application/json"},cache: true},

		searchRequests: function(search){
        	var deferred = $q.defer();
        	$http.post(this.apiPath+'searchRequests',search).success(function(data){
        		deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while searching consult requests");
            });
       
            return deferred.promise;
        },
        
		getRequest: function(requestId, demographicId){
			if (requestId=="new") requestId = 0;
			
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'getRequest', {params: {requestId:requestId, demographicId:demographicId}}).success(function(data){
        		deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while getting consult request (requestId="+requestId+")");
            });
       
            return deferred.promise;
        },
        
        getRequestAttachments: function(requestId, demographicId, attached){
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'getRequestAttachments?requestId='+requestId+'&demographicId='+demographicId+'&attached='+attached).success(function(data){
        		deferred.resolve(data);
        	}).error(function(){
            	  console.log("error fetching items");
                  deferred.reject("An error occured while getting consult attachments (requestId="+requestId+")");
        	});
        	
        	return deferred.promise;
        },
        
        saveRequest: function(request){
        	var deferred = $q.defer();
        	var requestTo1 = {consultationRequestTo1:request};
        	$http.post(this.apiPath+'saveRequest', requestTo1).success(function(data){
            	console.log(data);
                deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while fetching consult request after save");
            });
       
            return deferred.promise;
        },
        
        eSendRequest: function(requestId){
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'eSendRequest?requestId='+requestId).success(function(data){
        		deferred.resolve(data);
        	}).error(function(){
            	  console.log("error fetching items");
                  deferred.reject("An error occured while e-sending consult request (requestId="+requestId+")");
        	});
        	
        	return deferred.promise;
        },

		searchResponses: function(search){
        	var deferred = $q.defer();
        	$http.post(this.apiPath+'searchResponses',search).success(function(data){
        		deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while searching consult responses");
            });
       
            return deferred.promise;
        },
        
		getResponse: function(responseId, demographicNo){
			if (responseId=="new") responseId = 0;
			
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'getResponse', {params: {responseId:responseId, demographicNo:demographicNo}}).success(function(data){
        		deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while getting consult response (responseId="+responseId+")");
            });
       
            return deferred.promise;
        },
        
        getResponseAttachments: function(responseId, demographicNo, attached){
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'getResponseAttachments?responseId='+responseId+'&demographicNo='+demographicNo+'&attached='+attached).success(function(data){
        		deferred.resolve(data);
        	}).error(function(){
            	  console.log("error fetching items");
                  deferred.reject("An error occured while getting consult response attachments (responseId="+responseId+")");
        	});
        	
        	return deferred.promise;
        },
        
        saveResponse: function(response){
        	var deferred = $q.defer();
        	var responseTo1 = {consultationResponseTo1:response};
        	$http.post(this.apiPath+'saveResponse', responseTo1).success(function(data){
            	console.log(data);
                deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while fetching consult response after save");
            });
       
            return deferred.promise;
        },
        
        getReferralPathwaysByService: function(serviceName){
        	var deferred = $q.defer();
        	$http.get(this.apiPath+'getReferralPathwaysByService?serviceName='+serviceName).success(function(data){
            	console.log(data);
                deferred.resolve(data);
            }).error(function(){
          	  console.log("error fetching items");
              deferred.reject("An error occured while fetching referral pathways");
            });
       
            return deferred.promise;
        }
    };
});
