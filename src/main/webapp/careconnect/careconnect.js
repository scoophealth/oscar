/*
 * This is the code provided in
 * "CareConnect ‘POST’ Integration Technical Specification"
 * Version 2.01, February 2019
 */
(function(eHealth) {
	eHealth.postwith = {
		// DateTimeFormat = "yyyyMMdd";
		// domain is an optional parameter that will put CareConnect into
		// ‘PORTLET’ mode
		theFollowingPatientInfo : function(uri, phn, firstName, lastName,
				dateOfBirth, gender, org, domain) {
			var postToCareConnect = function(params) {
				var form = document.createElement("form");
				form.setAttribute("method", "post");
				form.setAttribute("action", uri);
				form.setAttribute("target", "_blank");
				for ( var key in params) {
					if (params.hasOwnProperty(key)) {
						var hiddenField = document.createElement("input");
						hiddenField.setAttribute("type", "hidden");
						hiddenField.setAttribute("name", key);
						hiddenField.setAttribute("value", params[key]);
						form.appendChild(hiddenField);
					}
				}
				document.body.appendChild(form);
				form.submit();
			};
			this.init = function() {
				var integrationInfo = new Object({
					"phn" : phn,
					fn : firstName,
					ln : lastName,
					dob : dateOfBirth,
					g : gender,
					o : org,
					d : domain
				});
				postToCareConnect(integrationInfo);
			};
			this.init(uri, phn, firstName, lastName, dateOfBirth, gender, org,
					domain);
		}
	};
})(window.eHealth = window.eHealth || {});
