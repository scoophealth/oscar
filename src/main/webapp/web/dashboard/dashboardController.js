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
oscarApp.controller('DashboardCtrl', function ($scope,$http) {
	console.log("Dashboard Ctrl");
	
	
	//header
	$scope.displayDate= new Date();
	$http({
	    url: '../ws/rs/providerService/provider/me.json',
	    dataType: 'json',
	    method: 'GET',
	    headers: {
	        "Content-Type": "application/json"
	    }

	}).success(function(response){
		$scope.userFirstName = response.firstName;
	}).error(function(error){
	    $scope.error = error;
	});	
	
	
	//ticklers
	$http({
	    url: '../ws/rs/tickler/mine?limit=6',
	    dataType: 'json',
	    method: 'GET',
	    headers: {
	        "Content-Type": "application/json"
	    }

	}).success(function(response){
		if (response.tickler instanceof Array) {
			$scope.ticklers = response.tickler;
		} else {
			var arr = new Array();
			arr[0] = response.tickler;
			$scope.ticklers = arr;
		}
		
	}).error(function(error){
	    $scope.error = error;
	});	
	
	
	//oscar messages
	$http({
	    url: '../ws/rs/messaging/unread?limit=6',
	    dataType: 'json',
	    method: 'GET',
	    headers: {
	        "Content-Type": "application/json"
	    }

	}).success(function(response){
		if (response.message instanceof Array) {
			$scope.messages = response.message;
		} else {
			var arr = new Array();
			arr[0] = response.message;
			$scope.messages = arr;
		}
	}).error(function(error){
	    $scope.error = error;
	});	
	
	
	//inbox
	$http({
	    url: '../ws/rs/inbox/mine?limit=20',
	    dataType: 'json',
	    method: 'GET',
	    headers: {
	        "Content-Type": "application/json"
	    }

	}).success(function(response){
		if (response.inbox instanceof Array) {
			$scope.inbox = response.inbox;
		} else {
			var arr = new Array();
			arr[0] = response.inbox;
			$scope.inbox = arr;
		}

	}).error(function(error){
	    $scope.error = error;
	});	
	
	//k2a
	$http({
	    url: '../ws/rs/rssproxy/rss?key=k2a',
	    dataType: 'json',
	    method: 'GET',
	    headers: {
	        "Content-Type": "application/json"
	    }

	}).success(function(response){
		if (response.item instanceof Array) {
			$scope.k2afeed = response.item;
		} else {
			var arr = new Array();
			arr[0] = response.item;
			$scope.k2afeed = arr;
		}

	}).error(function(error){
	    $scope.error = error;
	});	
	
	
});
