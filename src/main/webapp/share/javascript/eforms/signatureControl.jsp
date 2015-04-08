<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ page import="oscar.OscarProperties" contentType="text/javascript"%>

<%
OscarProperties props = OscarProperties.getInstance();
if (props.isEFormSignatureEnabled()) {
%>

if (typeof jQuery == "undefined") {
	alert("The signatureControl library requires jQuery. Please ensure that it is loaded first");
}

var signaturePadIFrameStart = "Signature:<br/><iframe style=\"width:500px; height:132px;\"id=\"signatureFrame\" src=\"";
var signaturePadIFrameEnd = "\" ></iframe>";
var signatureControl = {
	initialize : function(options) {
		var element = this;
		// Generating an eform configuration.
		 if (typeof options == "undefined" || options == null || options.eform) {
			options = typeof options == "undefined" ? {} : options;
			element.sigHTML = "../signature_pad/tabletSignature.jsp?inWindow=true&saveToDB=true&demographicNo=";
			element.signatureDisplay = "#signatureDisplay";
			element.signatureInput = "#signatureInput";
			element.signatureValue = "#signatureValue"
			element.demographicNo = 0; //TODO: Keep on trucking
			element.imgAttributes = "";
			
			var placeholder = jQuery(element.signatureInput);
			if (placeholder == null || placeholder.size() == 0) { 
				if (jQuery(".DoNotPrint").size() > 0) { 
					placeholder = jQuery("<div id='signatureInput'>&nbsp;</div>");
					jQuery(".DoNotPrint").append(placeholder);				
				}
				else {
					alert("Missing placeholder please ensure a div with the id signatureInput or a div with class DoNotPrint exists on the page ."); 
					return;
				}
			}
			
			if (options.left != null) { jQuery(element.signatureDisplay).css("position", "absolute"); jQuery(element.signatureDisplay).css("left", options.left);  }
			if (options.right != null) { jQuery(element.signatureDisplay).css("position", "absolute"); jQuery(element.signatureDisplay).css("right", options.right);  }
			if (options.top != null) { jQuery(element.signatureDisplay).css("position", "absolute"); jQuery(element.signatureDisplay).css("top", options.top);  }
			if (options.bottom != null) { jQuery(element.signatureDisplay).css("position", "absolute"); jQuery(element.signatureDisplay).css("bottom", options.bottom);  }
			if (options.width != null) {
				element.imgAttributes += options.width != null ? " width='"+options.width+"' ": "";
			}				
			if (options.height != null) { 
				element.imgAttributes += options.height != null ? " height='"+options.height+"' ": "";
			}			
			
			if (jQuery(element.signatureValue).size() > 0 && "" != jQuery(element.signatureValue).val()) {
				jQuery(element.signatureDisplay).html("<img " + element.imgAttributes + " src='" + jQuery(element.signatureValue).val() + "' alt='signature' />");
			}
			
			element.refreshImage =  
				function(e) {
					if (jQuery(element.signatureDisplay).size() > 0) {
						jQuery(element.signatureDisplay).html("<img " + element.imgAttributes + " src='" + e.storedImageUrl + "&r=" + Math.floor(Math.random() * 1001) + "' alt='signature' />");
					}
					if (jQuery(element.signatureValue).size() > 0) {
						jQuery(element.signatureValue).val(e.storedImageUrl + "&r=" + Math.floor(Math.random() * 1001));
					}
				};
			
		}
		// Configuring signature pad with user provided settings.
		else {
			if (options.sigHTML == null) {
				alert("Error, missing url for tabletSignature.jsp");
				return;
			}
			if (options.demographicNo == null) {
				alert("Error, missing demographic number.");
				return;
			}
			element.sigHTML = options.sigHTML;
			element.signatureDisplay = options.signatureDisplay;
			element.signatureInput = options.signatureInput;
			element.demographicNo = options.demographicNo;
			element.isSignatureDirty = false;
			element.isSignatureSaved = false;
			element.refreshImage = options.refreshImage == null 
			  ? function(e) {
					if (jQuery(element.signatureDisplay).size() > 0) {
						jQuery(element.signatureDisplay).attr("src", e.storedImageUrl + "&r=" + Math.floor(Math.random() * 1001));
					}
				} : options.refreshImage;
		}
		
		// Inserting a hook into the window that the signature pad will
		// use to inform us when the signature's state has changed.
		window.signatureHandler = options.signatureHandler == null 
			? function(e) {
				  element.isSignatureDirty = e.isDirty;
				  element.isSignatureSaved = e.isSave;
				  if (e.isSave) {
					  element.refreshImage(e);
				  }
			  }
			  : options.signatureHandler;
		if ($(element.signatureInput).size() > 0) {
			$(element.signatureInput).html(signaturePadIFrameStart + element.sigHTML + signaturePadIFrameEnd);
		}
		return element;
	}
};
<% } %>
