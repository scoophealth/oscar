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
angular.module("securityServices", [])
	.service("securityService", function ($http,$q,$log) {
		return {
		apiPath:'../ws/rs/',
		configHeaders: {headers: {"Content-Type": "application/json","Accept":"application/json"}},
		configHeadersWithCache: {headers: {"Content-Type": "application/json","Accept":"application/json"},cache: true},
		user:null,
        
		getUser: function() {
			return this.user;
		},
		
		setUser: function(u) {
			this.user = u;
		},
        
        hasRight: function (objectName, privilege, demographicNo) {
            var deferred = $q.defer();
            $http.get(this.apiPath+'persona/hasRight?objectName='+objectName+'&privilege='+privilege+'&demographicNo='+demographicNo,this.configHeadersWithCache).success(function(data){
            	console.log(data);
            	deferred.resolve(data.success);
            }).error(function(){
            	console.log("error fetching rights");
            	deferred.reject("An error occured while fetching access right");
            });
     
          return deferred.promise;
        },
        
        hasRights: function (listOfItems) {
        	var deferred = $q.defer();
            
            $http({
                url: this.apiPath+'persona/hasRights',
                method: "POST",
                data: JSON.stringify(listOfItems),
                headers: {'Content-Type': 'application/json'}
             }).success(function (data, status, headers, config) {
            	 deferred.resolve(data);
             }).error(function (data, status, headers, config) {
            	 deferred.reject("An error occured while fetching access rights");
             });

          return deferred.promise;
        },
        isAllowedAccessToPatientRecord: function (demographicNo) {
        	console.log('in isAllowedAccessToPatientRecord');
        	var deferred = $q.defer();
            
            $http({
                url: this.apiPath+'persona/isAllowedAccessToPatientRecord',
                method: "POST",
                data: JSON.stringify({"demographicNo":demographicNo}),   
                headers: {'Content-Type': 'application/json'}
             }).success(function (data, status, headers, config) {
            	 deferred.resolve(data);
             }).error(function (data, status, headers, config) {
            	 deferred.reject("An error occured while fetching access rights");
             });

          return deferred.promise;
        }
    };
});