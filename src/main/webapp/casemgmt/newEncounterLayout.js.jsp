<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@page contentType="text/javascript"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"	scope="request" />

Messenger.options = {
		extraClasses: 'messenger-fixed messenger-on-top messenger-on-left',
		theme: 'future'
};

// global message
var msg;

/*
 * Handles eAAPs notification dismissal
 * 
 * @param notificationId ID of the notification to be dismissed
 * 
 */
function dismissHandler(notificationId) {
	jQuery.ajax({
		type : "POST",
		url : "<c:out value="${ctx}"/>/notification/create.do",
		data : {id: notificationId, type: "eaaps"},
		/*
		 * success : function(response) { msg.update({ message: "Notification
		 * will not be displayed again.", type: "success", actions: false }); },
		 */
		error: function(response) {
			msg.update({
				message: "We are sorry, there was an error saving your preference",
				type: "error",
				actions: false
			})
		}
	});
	return msg.hide();
}

/*
 * Handles messenger display
 * 
 * @param url URL of the notification popup
 * @param notificationId Id of the notification
 * @param message Additional message information
 */
function showMessenger(url, notificationId, message) {
	var messageContent = "";
	// in case URL is specified - make sure it's clickable
	if (url) {
		messageContent = "<a style='color: white' href='" + url + "' target='_blank'>" + message + "</a>";
	} else { // otherwise - just show the message provided
		messageContent = message;
	}

	msg = Messenger().post({
		id: url,
		singleton: true,
		type: 'info',
		message: messageContent,
		actions: {
			snooze: {
				label: 'Snooze',
				action: function() {
					return msg.update({
						message: "This message will display next time this eChart is open",
						type: "success",
						actions: false
					});
				}
			},
			dismiss: {
				label: 'Dismiss',
				action: function() {
					dismissHandler(notificationId);
					return msg;
				}
			}
		}
	});
}

/*
 * Main entry point into the messenger functionality. This function is called from the eAAP eChart
 * window.
 * 
 * @param url URL of the notification popup
 * @param notificationId Id of the notification 
 * @param message additional message information to be displayed in the popup
 */
function displayEaapsWindow(url, notificationId, message) {
	// make sure we give a chance for eChart to load
	setTimeout(
		function() {
			showMessenger(url, notificationId, message);	
		}, 500
	);
}