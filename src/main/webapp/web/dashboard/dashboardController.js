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
oscarApp.controller('DashboardCtrl', function ($scope,$http,providerService,ticklerService,messageService, inboxService, k2aService) {
	
	//header	
	$scope.displayDate= function() {return new Date();}
	
	providerService.getMe().then(function(data){
		$scope.userFirstName = data.firstName;
		
		ticklerService.search({priority:'',status:'A',assignee:data.providerNo},0,6).then(function(response){
			if(response.tickler == null) {
				return;
			}
				
			if (response.tickler instanceof Array) {
				$scope.ticklers = response.tickler;
			} else {
				var arr = new Array();
				arr[0] = response.tickler;
				$scope.ticklers = arr;
			}	
		},function(reason){
			alert(reason);
		});
		
		messageService.getUnread(6).then(function(response){
			if(response.message == null) {
				return;
			}
			
			if (response.message instanceof Array) {
				$scope.messages = response.message;
			} else {
				var arr = new Array();
				arr[0] = response.message;
				$scope.messages = arr;
			}			
		},function(reason){
			alert(reason);
		});
		
		inboxService.getDashboardItems(20).then(function(response){
			if(response.inbox == null) {
				return;
			}
			if (response.inbox instanceof Array) {
				$scope.inbox = response.inbox;
			} else {
				var arr = new Array();
				arr[0] = response.inbox;
				$scope.inbox = arr;
			}			
		},function(reason){
			alert(reason);
		});
		
		k2aService.getK2aFeed().then(function(response){
			if(response.item == null) {
				return;
			}
			
			if (response.item instanceof Array) {
				$scope.k2afeed = response.item;
			} else {
				var arr = new Array();
				arr[0] = response.item;
				$scope.k2afeed = arr;
			}			
		},function(reason){
			alert(reason);
		});
		
	},function(reason){
		alert(reason);
	});

	
	
	$scope.isTicklerExpiredOrHighPriority = function(tickler) {
		var ticklerDate = Date.parse(tickler.serviceDate);
		var now = new Date();
		var result = false;
		if(ticklerDate < now) {
			result=true;
		}
		if(tickler.priority == 'High') {
			result=true;
		}
			
		return result;
	}
	
	$scope.isTicklerHighPriority = function(tickler) {
		var ticklerDate = Date.parse(tickler.serviceDate);
		var now = new Date();
		var result = false;
		
		if(tickler.priority == 'High') {
			result=true;
		}
			
		return result;
	}
	
});
