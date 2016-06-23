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
oscarApp.controller('DashboardCtrl', function ($scope,providerService,ticklerService,messageService, inboxService, k2aService, $modal,noteService, securityService, personaService) {
	
	//header	
	$scope.displayDate= function() {return new Date();}
	
	$scope.me = null;

	$scope.busyLoadingData = false;
	
	personaService.getDashboardPreferences().then(function(data){
		$scope.prefs = data.dashboardPreferences;
	});
	
	securityService.hasRights({items:[{objectName:'_tickler',privilege:'w'},{objectName:'_tickler',privilege:'r'}]}).then(function(result){
		if(result.content != null && result.content.length == 2) {
			$scope.ticklerWriteAccess = result.content[0];
			$scope.ticklerReadAccess = result.content[1];
		}
	});
		
	$scope.openInbox = function() {
		 newwindow=window.open('../dms/inboxManage.do?method=prepareForIndexPage','inbox','height=700,width=1000');
		 if (window.focus) {
			 newwindow.focus();
		 }	
	}

	$scope.loadMoreK2aFeed = function() {
		$scope.updateFeed($scope.k2afeed.length,10);
	}

	$scope.authenticateK2A = function(id){
	    window.open('../apps/oauth1.jsp?id='+id,'appAuth','width=700,height=450');
	}

	$scope.agreeWithK2aPost = function(item) {
		if(item.agree) {
			k2aService.removeK2AComment(item.agreeId).then(function(response){
				item.agree = false;
				item.agreeCount--;
				item.agreeId = '';			
			},function(reason){
				alert(reason);
			});
		} else if(!(item.agree || item.disagree)) {
			if(typeof item.newComment === 'undefined') {
				item.newComment = {};		
			}
			item.newComment.agree = true;
			item.newComment.body = '';

			$scope.commentOnK2aPost(item);
		}
	}

	$scope.disagreeWithK2aPost = function(item) {
		if(item.disagree) {
			k2aService.removeK2AComment(item.agreeId).then(function(response){
				item.disagree = false;
				item.disagreeCount--;
				item.agreeId = '';			
			},function(reason){
				alert(reason);
			});
		} if(!(item.agree || item.disagree)) {		
			if(typeof item.newComment === 'undefined') {
				item.newComment = {};		
			}
			item.newComment.agree = false;
			item.newComment.body = '';

			$scope.commentOnK2aPost(item);
		}
	}

	$scope.commentOnK2aPost = function(item) {
		item.newComment.postId = item.id;
		k2aService.postK2AComment(item.newComment).then(function(response){
			item.newComment.body = '';
			item.newComment.agree = '';
			item.agreeId = response.agreeId;
			if(!(typeof response.post[0].agree === 'undefined')) {			
				if(response.post[0].agree) {
					item.agree = true;
					item.agreeId = response.post[0].agreeId;
					item.agreeCount++;
				} else {
					item.disagree = true;
					item.agreeId = response.post[0].agreeId;
					item.disagreeCount++;
				}
			} else {
				item.commentCount++;
				item.comments.unshift(response.post[0]);
			}
		},function(reason){
			alert(reason);
		});
	}
	
	$scope.updateTicklers = function() {
		//consider the option to have overdue only or not
		ticklerService.search({priority:'',status:'A',assignee:$scope.me.providerNo,overdueOnly:'property'},0,6).then(function(response){
			$scope.totalTicklers = response.total;
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
	}
	
	$scope.updateMessages = function() {
		messageService.getUnread(6).then(function(response){
			$scope.totalMessages = response.total;
			
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
		
	}
	
	$scope.updateReports = function() {
//TODO: changed to return 5 since that is all we are using at the moment
		inboxService.getDashboardItems(5).then(function(response){
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
			$scope.totalInbox = response.total;
		},function(reason){
			alert(reason);
		});
	}
	
	$scope.updateFeed = function(startPoint,numberOfRows) {
		if ($scope.busyLoadingData) return;
  		$scope.busyLoadingData = true;
		k2aService.getK2aFeed(startPoint,numberOfRows).then(function(response){
			if(response.post == null) {
				return;
			}
			
			if (response.post instanceof Array) {
				for(var i=0; i < response.post.length; i++) {
					if(!Array.isArray(response.post[i].comments)) {
						var arr = new Array();
						arr[0] = response.post[i].comments;
						response.post[i].comments = arr;						
					}					
				}
				if(typeof $scope.k2afeed === 'undefined') {
					$scope.k2afeed = response.post;
				} else {
					$scope.k2afeed = $scope.k2afeed.concat(response.post);
				}
				$scope.busyLoadingData = false;	
			} else {
				if(response.post.authenticatek2a) {
					$scope.authenticatek2a = response.post.description;
				} else {
					var arr = new Array();
					arr[0] = response.post;
					$scope.k2afeed = arr;
				}
			}	
		},function(reason){
			alert(reason);
			$scope.busyLoadingData = false;	
		});
	}
	
	$scope.updateDashboard = function() {
		$scope.updateTicklers();
		$scope.updateMessages();
		$scope.updateReports();
		$scope.updateFeed(0,10);
		
	}
	
	$scope.$watch(function() {
		  return securityService.getUser();
		}, function(newVal) {
			$scope.me = newVal;
			
			if(newVal != null) {
				$scope.updateDashboard();
			}
		}, true);
	
	
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
	
	$scope.openClassicMessenger = function() {
		if($scope.me != null) {
			window.open('../oscarMessenger/DisplayMessages.do?providerNo='+$scope.me.providerNo,'msgs','height=700,width=1024,scrollbars=1');
		}
	}	
	
	$scope.viewMessage = function(message) {
		window.open('../oscarMessenger/ViewMessage.do?messageID='+message.id+'&boxType=0','msg'+message.id,'height=700,width=1024,scrollbars=1');
	}

	$scope.viewTickler = function(tickler) {
        var modalInstance = $modal.open({
        	templateUrl: 'tickler/ticklerView.jsp',
            controller: 'TicklerViewController',
            backdrop: false,
            size: 'lg',
            resolve: {
                tickler: function () {
                	return tickler;
                },
                ticklerNote: function() {
                	return noteService.getTicklerNote(tickler.id);
                },
                ticklerWriteAccess: function() {
                	return  $scope.ticklerWriteAccess;
                },
                me: function() {
                	return $scope.me;
                }
            }
        });
        
        modalInstance.result.then(function(data){
        	//console.log('data from modalInstance '+data);
        	if(data != null && data == true) {
        		$scope.updateTicklers();
        	}
        },function(reason){
        	alert(reason);
        });
        
	}
	
	$scope.configureTicklers = function() {
        var modalInstance = $modal.open({
        	templateUrl: 'tickler/configureDashboard.jsp',
            controller: 'TicklerConfigureController',
            backdrop: false,
            size: 'lg',
            resolve: {
            	prefs: function() {
                	return personaService.getDashboardPreferences();
                }
            }
        });
        
        modalInstance.result.then(function(data){
        	if(data == true ) {
        		$scope.updateTicklers();
        		personaService.getDashboardPreferences().then(function(data){
    				$scope.prefs = data.dashboardPreferences;
    			});
        	}
        },function(reason){
        	alert(reason);
        });
        
	}
});


oscarApp.controller('TicklerConfigureController',function($scope,$modalInstance,personaService,prefs) {
	
	$scope.prefs = prefs.dashboardPreferences;
	
	   $scope.close = function () {
		   $modalInstance.close(false);		
	    }
	    $scope.save = function () {
	    	
	    	personaService.updateDashboardPreferences($scope.prefs).then(function(data){
    			$modalInstance.close(true);
    			
    			
    		}, function(reason){
    			$modalInstance.close(false);
    		});
	    	
	    }
});
