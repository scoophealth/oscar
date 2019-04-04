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
angular.module("surveillanceServices", [])
	.service("surveillanceService", function ($http,$q,$log) {
		return {
		apiPath:'../ws/rs',
		configHeaders: {headers: {"Content-Type": "application/json","Accept":"application/json"}},
		configHeadersWithCache: {headers: {"Content-Type": "application/json","Accept":"application/json"},cache: true},
	      
	allLoadedSurveillanceConfigs: function(id) {
		var deferred = $q.defer();
		$http.get(this.apiPath+'/surveillance/allLoadedSurveillanceConfigs',this.configHeaders).then(function(data){
		deferred.resolve(data.data);
		},function(){
		  deferred.reject("An error occured while trying to /resources/allLoadedSurveillanceConfigs");
		});
	      return deferred.promise;
	},
	getSurvey: function(id){
		var deferred = $q.defer();
		$http.post(this.apiPath+'/surveillance/getSurvey/'+id,this.configHeaders).then(function(data){
			deferred.resolve(data.data);
		},function(){
		  deferred.reject("An error occured while trying to /resources/getSurvey/"+id);
		});
	    return deferred.promise;
	},
	updateSurvey: function(id,survey){
		var deferred = $q.defer();
		$http.post(this.apiPath+'/surveillance/updateSurvey/'+id,survey,this.configHeaders).then(function(data){
			deferred.resolve(data.data);
		},function(){
		  deferred.reject("An error occured while trying to /resources/updateSurvey/"+id);
		});
	    return deferred.promise;
	},
	surveillanceConfigList: function() {
		var deferred = $q.defer();
		$http.get(this.apiPath+'/surveillance/surveillanceConfigList').then(function(data){
		console.log("return from /surveillanceConfigList",data);
		deferred.resolve(data.data);
		},function(){
		  console.log("error getting surveillanceConfigList");
		  deferred.reject("An error occured while trying to remove a comment from k2a");
		});
	      return deferred.promise;
	},
	addSurveyFromK2A: function(survey){
		var deferred = $q.defer();
		$http.post(this.apiPath+'/surveillance/addSurveyFromK2A/'+survey.id,survey,this.configHeaders).then(function(data){
			deferred.resolve(data.data);
		},function(){
		  deferred.reject("An error occured while trying to /resources/addSurveyFromK2A/"+id);
		});
	    return deferred.promise;	
    },
    updateSurveyFromK2A: function(survey){
		var deferred = $q.defer();
		$http.post(this.apiPath+'/surveillance/updateSurveyFromK2A/'+survey.id,survey,this.configHeaders).then(function(data){
			deferred.resolve(data.data);
		},function(){
		  deferred.reject("An error occured while trying to /resources/updateSurveyFromK2A/"+id);
		});
	    return deferred.promise;	
    },
    enableResource: function(id){
		var deferred = $q.defer();
		$http.post(this.apiPath+'/surveillance/enableResource/'+id,this.configHeaders).then(function(data){
			deferred.resolve(data.data);
		},function(){
		  deferred.reject("An error occured while trying to /resources/enableResource/"+id);
		});
	    return deferred.promise;	
    },
    disableResource: function(id){
		var deferred = $q.defer();
		$http.post(this.apiPath+'/surveillance/disableResource/'+id,this.configHeaders).then(function(data){
			deferred.resolve(data.data);
		},function(){
		  deferred.reject("An error occured while trying to /resources/disableResource/"+id);
		});
	    return deferred.promise;	
    },
    generateExport: function(id){
		var deferred = $q.defer();
		$http.post(this.apiPath+'/surveillance/generateExport/'+id,this.configHeaders).then(function(data){
			deferred.resolve(data.data);
		},function(){
		  deferred.reject("An error occured while trying to /resources/disableResource/"+id);
		});
	    return deferred.promise;	
    },
    getExportList: function(id){
		var deferred = $q.defer();
		$http.get(this.apiPath+'/surveillance/getExportFiles/'+id,this.configHeaders).then(function(data){
			deferred.resolve(data.data);
		},function(){
		  deferred.reject("An error occured while trying to /resources/getExportList/"+id);
		});
	    return deferred.promise;	
    },
    setAsSent: function(id){
		var deferred = $q.defer();
		$http.post(this.apiPath+'/surveillance/setExportAsSent/'+id,this.configHeaders).then(function(data){
			deferred.resolve(data.data);
		},function(){
		  deferred.reject("An error occured while trying to /resources/setExportAsSent/"+id);
		});
	    return deferred.promise;	
    },
    createJob: function(id,obj){
        var deferred = $q.defer();
        $http.post(this.apiPath+'/surveillance/createJob/'+id,obj,this.configHeaders).then(function(data){
                deferred.resolve(data.data);
        },function(){
          deferred.reject("An error occured while trying to /resources/setExportAsSent/"+id);
        });
        return deferred.promise;    
    }
    
    
    
    };
});
