/* printControl - Changes print behaviour to receive a server side generated PDF 
 *                instead of using the browsers print functionality.
 */

if (typeof jQuery == "undefined") { alert("The printControl library requires jQuery. Please ensure that it is loaded first"); }
var printControl = {
	initialize: function () {
		var print = jQuery("input[name='PrintButton']");
		var printSave = jQuery("input[name='PrintSaveButton']");
		if (printSave.size() == 0) { printSave = jQuery("input[name='PrintSubmitButton']"); }
		
		if (print.size() != 0) {
			print.attr("onclick", "").unbind("click");
			print.click(function(){submitPrintButton(false);})
		}
		
		if (printSave.size() != 0) {
			//if (print) { print.remove(); }
			printSave.attr("onclick", "").unbind("click");
			printSave.attr("value", "Submit & Print");
			printSave.click(function(){submitPrintButton(true);});
		}
		if (print.size() != 0 && printSave.size() != 0) {
			print.insertAfter(printSave);
		}
	}
};

function submitPrintButton(save) {
	
	// Setting this form to print.
	var printHolder = jQuery('#printHolder');
	if (printHolder == null || printHolder.size() == 0) {
		jQuery("form").append("<input id='printHolder' type='hidden' name='print' value='true' >");
	}	
	printHolder = jQuery('#printHolder');
	printHolder.val("true");
	
	var saveHolder = jQuery("#saveHolder");
	if (saveHolder == null || saveHolder.size() == 0) {
		jQuery("form").append("<input id='saveHolder' type='hidden' name='skipSave' value='"+!save+"' >");
	}
	saveHolder = jQuery("#saveHolder");
	saveHolder.val(!save);
	needToConfirm=false;
	
	if (document.getElementById('Letter') != null) {
		document.getElementById('Letter').value=editControlContents('edit');		
	}	
	
	if (!save) { jQuery("form").attr("target", "_blank"); }
	jQuery("form").submit();	
	if (save) { setTimeout("window.close()", 3000); }
	printHolder.val("false");
	saveHolder.val("false");
	
}



jQuery(document).ready(function(){printControl.initialize();});