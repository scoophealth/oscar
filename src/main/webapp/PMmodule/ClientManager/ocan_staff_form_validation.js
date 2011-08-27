$("document").ready(function() {
		
	$("input[name='immigration_issues'][value='4']").change(function(){
		if($("input[name='immigration_issues'][value='4']").attr('checked') == true) {
			$("input[name='immigration_issues'][value='3']").attr('checked',true); //It won't be saved as it's disabled, so have one hidden field to hold the value.
			$("#immigration_issues_hidden").val("3");			
			$("input[name='immigration_issues'][value='3']").attr('disabled','disabled');
		} else {
			$("input[name='immigration_issues'][value='3']").attr('checked',false); 
			$("#immigration_issues_hidden").val("");			
			$("input[name='immigration_issues'][value='3']").attr('disabled','');
		}
	});
	
	$("input[name='immigration_issues'][value='4']").each(function(){
		if($("input[name='immigration_issues'][value='4']").attr('checked') == true) {
			$("#immigration_issues_hidden").val("3");
				
			$("input[name='immigration_issues'][value='3']").attr('disabled','disabled');
			$("input[name='immigration_issues'][value='3']").attr('checked',true);		
		} else {
			$("#immigration_issues_hidden").val("");
			$("input[name='immigration_issues'][value='3']").attr('disabled','');
		}
	});
	
	$("input[name='immigration_issues'][value='None']").change(function(){
		if($("input[name='immigration_issues'][value='None']").attr('checked') == true) {
			$("input[name='immigration_issues'][value='1']").attr('checked',false);
			$("input[name='immigration_issues'][value='1']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='1']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='2']").attr('checked',false);
			$("input[name='immigration_issues'][value='2']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='2']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='3']").attr('checked',false);
			$("input[name='immigration_issues'][value='3']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='3']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='4']").attr('checked',false);
			$("input[name='immigration_issues'][value='4']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='4']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='6']").attr('checked',false);
			$("input[name='immigration_issues'][value='6']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='6']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='8']").attr('checked',false);
			$("input[name='immigration_issues'][value='8']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='8']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='EWIT']").attr('checked',false);
			$("input[name='immigration_issues'][value='EWIT']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='EWIT']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='OTH']").attr('checked',false);
			$("input[name='immigration_issues'][value='OTH']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='OTH']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='UNK']").attr('checked',false);
			$("input[name='immigration_issues'][value='UNK']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='UNK']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='CDA']").attr('checked',false);
			$("input[name='immigration_issues'][value='CDA']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='CDA']").attr('disabled','disabled');
			
			$("#immigration_issues_other").attr('disabled','disabled');
			$("#immigration_issues_other").val("");
		} else {
			
			$("input[name='immigration_issues'][value='1']").attr('readonly','');
			$("input[name='immigration_issues'][value='1']").attr('disabled','');
				
			$("input[name='immigration_issues'][value='2']").attr('readonly','');
			$("input[name='immigration_issues'][value='2']").attr('disabled','');
			
			$("input[name='immigration_issues'][value='3']").attr('readonly','');
			$("input[name='immigration_issues'][value='3']").attr('disabled','');

			$("input[name='immigration_issues'][value='4']").attr('readonly','');
			$("input[name='immigration_issues'][value='4']").attr('disabled','');

			$("input[name='immigration_issues'][value='6']").attr('readonly','');
			$("input[name='immigration_issues'][value='6']").attr('disabled','');

			$("input[name='immigration_issues'][value='8']").attr('readonly','');
			$("input[name='immigration_issues'][value='8']").attr('disabled','');

			$("input[name='immigration_issues'][value='EWIT']").attr('readonly','');
			$("input[name='immigration_issues'][value='EWIT']").attr('disabled','');

			$("input[name='immigration_issues'][value='OTH']").attr('readonly','');
			$("input[name='immigration_issues'][value='OTH']").attr('disabled','');

			$("input[name='immigration_issues'][value='UNK']").attr('readonly','');
			$("input[name='immigration_issues'][value='UNK']").attr('disabled','');

			$("input[name='immigration_issues'][value='CDA']").attr('readonly','');
			$("input[name='immigration_issues'][value='CDA']").attr('disabled','');
			
			$("#immigration_issues_other").attr('disabled','disabled');
			$("#immigration_issues_other").val("");
		}
	});
	
	$("input[name='immigration_issues'][value='None']").each(function(){
		if($("input[name='immigration_issues'][value='None']").attr('checked') == true) {
			$("input[name='immigration_issues'][value='1']").attr('checked',false);
			$("input[name='immigration_issues'][value='1']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='1']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='2']").attr('checked',false);
			$("input[name='immigration_issues'][value='2']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='2']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='3']").attr('checked',false);
			$("input[name='immigration_issues'][value='3']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='3']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='4']").attr('checked',false);
			$("input[name='immigration_issues'][value='4']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='4']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='6']").attr('checked',false);
			$("input[name='immigration_issues'][value='6']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='6']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='8']").attr('checked',false);
			$("input[name='immigration_issues'][value='8']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='8']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='EWIT']").attr('checked',false);
			$("input[name='immigration_issues'][value='EWIT']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='EWIT']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='OTH']").attr('checked',false);
			$("input[name='immigration_issues'][value='OTH']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='OTH']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='UNK']").attr('checked',false);
			$("input[name='immigration_issues'][value='UNK']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='UNK']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='CDA']").attr('checked',false);
			$("input[name='immigration_issues'][value='CDA']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='CDA']").attr('disabled','disabled');
			
			$("#immigration_issues_other").attr('disabled','disabled');
			$("#immigration_issues_other").val("");
		} 
	});
	
	
	$("input[name='immigration_issues'][value='UNK']").change(function(){
		if($("input[name='immigration_issues'][value='UNK']").attr('checked') == true) {
			$("input[name='immigration_issues'][value='1']").attr('checked',false);
			$("input[name='immigration_issues'][value='1']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='1']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='2']").attr('checked',false);
			$("input[name='immigration_issues'][value='2']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='2']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='3']").attr('checked',false);
			$("input[name='immigration_issues'][value='3']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='3']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='4']").attr('checked',false);
			$("input[name='immigration_issues'][value='4']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='4']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='6']").attr('checked',false);
			$("input[name='immigration_issues'][value='6']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='6']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='8']").attr('checked',false);
			$("input[name='immigration_issues'][value='8']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='8']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='EWIT']").attr('checked',false);
			$("input[name='immigration_issues'][value='EWIT']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='EWIT']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='OTH']").attr('checked',false);
			$("input[name='immigration_issues'][value='OTH']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='OTH']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='None']").attr('checked',false);
			$("input[name='immigration_issues'][value='None']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='None']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='CDA']").attr('checked',false);
			$("input[name='immigration_issues'][value='CDA']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='CDA']").attr('disabled','disabled');
			
			$("#immigration_issues_other").attr('disabled','disabled');
			$("#immigration_issues_other").val("");
		} else {
			
			$("input[name='immigration_issues'][value='1']").attr('readonly','');
			$("input[name='immigration_issues'][value='1']").attr('disabled','');
				
			$("input[name='immigration_issues'][value='2']").attr('readonly','');
			$("input[name='immigration_issues'][value='2']").attr('disabled','');
			
			$("input[name='immigration_issues'][value='3']").attr('readonly','');
			$("input[name='immigration_issues'][value='3']").attr('disabled','');

			$("input[name='immigration_issues'][value='4']").attr('readonly','');
			$("input[name='immigration_issues'][value='4']").attr('disabled','');

			$("input[name='immigration_issues'][value='6']").attr('readonly','');
			$("input[name='immigration_issues'][value='6']").attr('disabled','');

			$("input[name='immigration_issues'][value='8']").attr('readonly','');
			$("input[name='immigration_issues'][value='8']").attr('disabled','');

			$("input[name='immigration_issues'][value='EWIT']").attr('readonly','');
			$("input[name='immigration_issues'][value='EWIT']").attr('disabled','');

			$("input[name='immigration_issues'][value='OTH']").attr('readonly','');
			$("input[name='immigration_issues'][value='OTH']").attr('disabled','');

			$("input[name='immigration_issues'][value='None']").attr('readonly','');
			$("input[name='immigration_issues'][value='None']").attr('disabled','');

			$("input[name='immigration_issues'][value='CDA']").attr('readonly','');
			$("input[name='immigration_issues'][value='CDA']").attr('disabled','');
			
			$("#immigration_issues_other").attr('disabled','disabled');
			$("#immigration_issues_other").val("");
		}
	});
	
	$("input[name='immigration_issues'][value='UNK']").each(function(){
		if($("input[name='immigration_issues'][value='UNK']").attr('checked') == true) {
			$("input[name='immigration_issues'][value='1']").attr('checked',false);
			$("input[name='immigration_issues'][value='1']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='1']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='2']").attr('checked',false);
			$("input[name='immigration_issues'][value='2']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='2']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='3']").attr('checked',false);
			$("input[name='immigration_issues'][value='3']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='3']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='4']").attr('checked',false);
			$("input[name='immigration_issues'][value='4']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='4']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='6']").attr('checked',false);
			$("input[name='immigration_issues'][value='6']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='6']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='8']").attr('checked',false);
			$("input[name='immigration_issues'][value='8']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='8']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='EWIT']").attr('checked',false);
			$("input[name='immigration_issues'][value='EWIT']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='EWIT']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='OTH']").attr('checked',false);
			$("input[name='immigration_issues'][value='OTH']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='OTH']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='None']").attr('checked',false);
			$("input[name='immigration_issues'][value='None']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='None']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='CDA']").attr('checked',false);
			$("input[name='immigration_issues'][value='CDA']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='CDA']").attr('disabled','disabled');
			
			$("#immigration_issues_other").attr('disabled','disabled');
			$("#immigration_issues_other").val("");
		} 
	});
	
	$("input[name='immigration_issues'][value='CDA']").change(function(){
		if($("input[name='immigration_issues'][value='CDA']").attr('checked') == true) {
			$("input[name='immigration_issues'][value='1']").attr('checked',false);
			$("input[name='immigration_issues'][value='1']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='1']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='2']").attr('checked',false);
			$("input[name='immigration_issues'][value='2']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='2']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='3']").attr('checked',false);
			$("input[name='immigration_issues'][value='3']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='3']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='4']").attr('checked',false);
			$("input[name='immigration_issues'][value='4']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='4']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='6']").attr('checked',false);
			$("input[name='immigration_issues'][value='6']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='6']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='8']").attr('checked',false);
			$("input[name='immigration_issues'][value='8']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='8']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='EWIT']").attr('checked',false);
			$("input[name='immigration_issues'][value='EWIT']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='EWIT']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='OTH']").attr('checked',false);
			$("input[name='immigration_issues'][value='OTH']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='OTH']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='UNK']").attr('checked',false);
			$("input[name='immigration_issues'][value='UNK']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='UNK']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='None']").attr('checked',false);
			$("input[name='immigration_issues'][value='None']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='None']").attr('disabled','disabled');
			
			$("#immigration_issues_other").attr('disabled','disabled');
			$("#immigration_issues_other").val("");
		} else {
			
			$("input[name='immigration_issues'][value='1']").attr('readonly','');
			$("input[name='immigration_issues'][value='1']").attr('disabled','');
				
			$("input[name='immigration_issues'][value='2']").attr('readonly','');
			$("input[name='immigration_issues'][value='2']").attr('disabled','');
			
			$("input[name='immigration_issues'][value='3']").attr('readonly','');
			$("input[name='immigration_issues'][value='3']").attr('disabled','');

			$("input[name='immigration_issues'][value='4']").attr('readonly','');
			$("input[name='immigration_issues'][value='4']").attr('disabled','');

			$("input[name='immigration_issues'][value='6']").attr('readonly','');
			$("input[name='immigration_issues'][value='6']").attr('disabled','');

			$("input[name='immigration_issues'][value='8']").attr('readonly','');
			$("input[name='immigration_issues'][value='8']").attr('disabled','');

			$("input[name='immigration_issues'][value='EWIT']").attr('readonly','');
			$("input[name='immigration_issues'][value='EWIT']").attr('disabled','');

			$("input[name='immigration_issues'][value='OTH']").attr('readonly','');
			$("input[name='immigration_issues'][value='OTH']").attr('disabled','');

			$("input[name='immigration_issues'][value='UNK']").attr('readonly','');
			$("input[name='immigration_issues'][value='UNK']").attr('disabled','');

			$("input[name='immigration_issues'][value='None']").attr('readonly','');
			$("input[name='immigration_issues'][value='None']").attr('disabled','');
			
			$("#immigration_issues_other").attr('disabled','disabled');
			$("#immigration_issues_other").val("");
		}
	});
	
	$("input[name='immigration_issues'][value='CDA']").each(function(){
		if($("input[name='immigration_issues'][value='CDA']").attr('checked') == true) {
			$("input[name='immigration_issues'][value='1']").attr('checked',false);
			$("input[name='immigration_issues'][value='1']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='1']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='2']").attr('checked',false);
			$("input[name='immigration_issues'][value='2']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='2']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='3']").attr('checked',false);
			$("input[name='immigration_issues'][value='3']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='3']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='4']").attr('checked',false);
			$("input[name='immigration_issues'][value='4']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='4']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='6']").attr('checked',false);
			$("input[name='immigration_issues'][value='6']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='6']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='8']").attr('checked',false);
			$("input[name='immigration_issues'][value='8']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='8']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='EWIT']").attr('checked',false);
			$("input[name='immigration_issues'][value='EWIT']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='EWIT']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='OTH']").attr('checked',false);
			$("input[name='immigration_issues'][value='OTH']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='OTH']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='UNK']").attr('checked',false);
			$("input[name='immigration_issues'][value='UNK']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='UNK']").attr('disabled','disabled');
			
			$("input[name='immigration_issues'][value='None']").attr('checked',false);
			$("input[name='immigration_issues'][value='None']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='None']").attr('disabled','disabled');
			
			$("#immigration_issues_other").attr('disabled','disabled');
			$("#immigration_issues_other").val("");
		} 
	});
	
	
	
	$("input[name='immigration_issues'][value='OTH']").change(function() {
		if($("input[name='immigration_issues'][value='OTH']").attr('checked') == true) {
			$("#immigration_issues_other").attr('disabled','');
			$("#immigration_issues_other").val("");
		} else {
			$("#immigration_issues_other").attr('disabled','disabled');
			$("#immigration_issues_other").val("");
		}		
	});
	
	$("input[name='immigration_issues'][value='OTH']").each(function() {
		if($("input[name='immigration_issues'][value='OTH']").attr('checked') == true) {
			$("#immigration_issues_other").attr('disabled','');			
		} else {
			$("#immigration_issues_other").attr('disabled','disabled');
			$("#immigration_issues_other").val("");
		}		
	});
	
	$("input[name='discrimination'][value='UNK']").change(function(){
		if($("input[name='discrimination'][value='UNK']").attr('checked') == true) {
			$("input[name='discrimination'][value='21134002']").attr('checked',false);
			$("input[name='discrimination'][value='21134002']").attr('readonly','readonly');
			$("input[name='discrimination'][value='21134002']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='397731000']").attr('checked',false);
			$("input[name='discrimination'][value='397731000']").attr('readonly','readonly');
			$("input[name='discrimination'][value='397731000']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='365873007']").attr('checked',false);
			$("input[name='discrimination'][value='365873007']").attr('readonly','readonly');
			$("input[name='discrimination'][value='365873007']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='IMGR']").attr('checked',false);
			$("input[name='discrimination'][value='IMGR']").attr('readonly','readonly');
			$("input[name='discrimination'][value='IMGR']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='74732009']").attr('checked',false);
			$("input[name='discrimination'][value='74732009']").attr('readonly','readonly');
			$("input[name='discrimination'][value='74732009']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='415229000']").attr('checked',false);
			$("input[name='discrimination'][value='415229000']").attr('readonly','readonly');
			$("input[name='discrimination'][value='415229000']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='365577002']").attr('checked',false);
			$("input[name='discrimination'][value='365577002']").attr('readonly','readonly');
			$("input[name='discrimination'][value='365577002']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='365956009']").attr('checked',false);
			$("input[name='discrimination'][value='365956009']").attr('readonly','readonly');
			$("input[name='discrimination'][value='365956009']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='OTH']").attr('checked',false);
			$("input[name='discrimination'][value='OTH']").attr('readonly','readonly');
			$("input[name='discrimination'][value='OTH']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='CDA']").attr('checked',false);
			$("input[name='discrimination'][value='CDA']").attr('readonly','readonly');
			$("input[name='discrimination'][value='CDA']").attr('disabled','disabled');	
			
			$("#discrimination_other").attr('disabled','disabled');
			$("#discrimination_other").val("");
			
		} else {
			$("input[name='discrimination'][value='21134002']").attr('readonly','');
			$("input[name='discrimination'][value='21134002']").attr('disabled','');
				
			$("input[name='discrimination'][value='397731000']").attr('readonly','');
			$("input[name='discrimination'][value='397731000']").attr('disabled','');
			
			$("input[name='discrimination'][value='365873007']").attr('readonly','');
			$("input[name='discrimination'][value='365873007']").attr('disabled','');
			
			$("input[name='discrimination'][value='IMGR']").attr('readonly','');
			$("input[name='discrimination'][value='IMGR']").attr('disabled','');
			
			$("input[name='discrimination'][value='74732009']").attr('readonly','');
			$("input[name='discrimination'][value='74732009']").attr('disabled','');
			
			$("input[name='discrimination'][value='415229000']").attr('readonly','');
			$("input[name='discrimination'][value='415229000']").attr('disabled','');
			
			$("input[name='discrimination'][value='365577002']").attr('readonly','');
			$("input[name='discrimination'][value='365577002']").attr('disabled','');
			
			$("input[name='discrimination'][value='365956009']").attr('readonly','');
			$("input[name='discrimination'][value='365956009']").attr('disabled','');
			
			$("input[name='discrimination'][value='OTH']").attr('readonly','');
			$("input[name='discrimination'][value='OTH']").attr('disabled','');
			
			$("input[name='discrimination'][value='CDA']").attr('readonly','');
			$("input[name='discrimination'][value='CDA']").attr('disabled','');			
			
			$("#discrimination_other").attr('disabled','');
			$("#discrimination_other").val("");
		}	
	});
	
	$("input[name='discrimination'][value='UNK']").each(function(){
		if($("input[name='discrimination'][value='UNK']").attr('checked') == true) {
			$("input[name='discrimination'][value='21134002']").attr('checked',false);
			$("input[name='discrimination'][value='21134002']").attr('readonly','readonly');
			$("input[name='discrimination'][value='21134002']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='397731000']").attr('checked',false);
			$("input[name='discrimination'][value='397731000']").attr('readonly','readonly');
			$("input[name='discrimination'][value='397731000']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='365873007']").attr('checked',false);
			$("input[name='discrimination'][value='365873007']").attr('readonly','readonly');
			$("input[name='discrimination'][value='365873007']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='IMGR']").attr('checked',false);
			$("input[name='discrimination'][value='IMGR']").attr('readonly','readonly');
			$("input[name='discrimination'][value='IMGR']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='74732009']").attr('checked',false);
			$("input[name='discrimination'][value='74732009']").attr('readonly','readonly');
			$("input[name='discrimination'][value='74732009']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='415229000']").attr('checked',false);
			$("input[name='discrimination'][value='415229000']").attr('readonly','readonly');
			$("input[name='discrimination'][value='415229000']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='365577002']").attr('checked',false);
			$("input[name='discrimination'][value='365577002']").attr('readonly','readonly');
			$("input[name='discrimination'][value='365577002']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='365956009']").attr('checked',false);
			$("input[name='discrimination'][value='365956009']").attr('readonly','readonly');
			$("input[name='discrimination'][value='365956009']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='OTH']").attr('checked',false);
			$("input[name='discrimination'][value='OTH']").attr('readonly','readonly');
			$("input[name='discrimination'][value='OTH']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='CDA']").attr('checked',false);
			$("input[name='discrimination'][value='CDA']").attr('readonly','readonly');
			$("input[name='discrimination'][value='CDA']").attr('disabled','disabled');	
			
			$("#discrimination_other").attr('disabled','disabled');
			$("#discrimination_other").val("");
			
		} 
	});
	
	
	$("input[name='discrimination'][value='CDA']").change(function(){
		if($("input[name='discrimination'][value='CDA']").attr('checked') == true) {
			$("input[name='discrimination'][value='21134002']").attr('checked',false);
			$("input[name='discrimination'][value='21134002']").attr('readonly','readonly');
			$("input[name='discrimination'][value='21134002']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='397731000']").attr('checked',false);
			$("input[name='discrimination'][value='397731000']").attr('readonly','readonly');
			$("input[name='discrimination'][value='397731000']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='365873007']").attr('checked',false);
			$("input[name='discrimination'][value='365873007']").attr('readonly','readonly');
			$("input[name='discrimination'][value='365873007']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='IMGR']").attr('checked',false);
			$("input[name='discrimination'][value='IMGR']").attr('readonly','readonly');
			$("input[name='discrimination'][value='IMGR']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='74732009']").attr('checked',false);
			$("input[name='discrimination'][value='74732009']").attr('readonly','readonly');
			$("input[name='discrimination'][value='74732009']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='415229000']").attr('checked',false);
			$("input[name='discrimination'][value='415229000']").attr('readonly','readonly');
			$("input[name='discrimination'][value='415229000']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='365577002']").attr('checked',false);
			$("input[name='discrimination'][value='365577002']").attr('readonly','readonly');
			$("input[name='discrimination'][value='365577002']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='365956009']").attr('checked',false);
			$("input[name='discrimination'][value='365956009']").attr('readonly','readonly');
			$("input[name='discrimination'][value='365956009']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='OTH']").attr('checked',false);
			$("input[name='discrimination'][value='OTH']").attr('readonly','readonly');
			$("input[name='discrimination'][value='OTH']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='UNK']").attr('checked',false);
			$("input[name='discrimination'][value='UNK']").attr('readonly','readonly');
			$("input[name='discrimination'][value='UNK']").attr('disabled','disabled');			
			
			$("#discrimination_other").attr('disabled','disabled');
			$("#discrimination_other").val("");
			
		} else {
			$("input[name='discrimination'][value='21134002']").attr('readonly','');
			$("input[name='discrimination'][value='21134002']").attr('disabled','');
				
			$("input[name='discrimination'][value='397731000']").attr('readonly','');
			$("input[name='discrimination'][value='397731000']").attr('disabled','');
			
			$("input[name='discrimination'][value='365873007']").attr('readonly','');
			$("input[name='discrimination'][value='365873007']").attr('disabled','');
			
			$("input[name='discrimination'][value='IMGR']").attr('readonly','');
			$("input[name='discrimination'][value='IMGR']").attr('disabled','');
			
			$("input[name='discrimination'][value='74732009']").attr('readonly','');
			$("input[name='discrimination'][value='74732009']").attr('disabled','');
			
			$("input[name='discrimination'][value='415229000']").attr('readonly','');
			$("input[name='discrimination'][value='415229000']").attr('disabled','');
			
			$("input[name='discrimination'][value='365577002']").attr('readonly','');
			$("input[name='discrimination'][value='365577002']").attr('disabled','');
			
			$("input[name='discrimination'][value='365956009']").attr('readonly','');
			$("input[name='discrimination'][value='365956009']").attr('disabled','');
			
			$("input[name='discrimination'][value='OTH']").attr('readonly','');
			$("input[name='discrimination'][value='OTH']").attr('disabled','');
			
			$("input[name='discrimination'][value='UNK']").attr('readonly','');
			$("input[name='discrimination'][value='UNK']").attr('disabled','');			
			
			$("#discrimination_other").attr('disabled','');
			$("#discrimination_other").val("");
		}	
	});	
	
	$("input[name='discrimination'][value='CDA']").each(function(){
		if($("input[name='discrimination'][value='CDA']").attr('checked') == true) {
			$("input[name='discrimination'][value='21134002']").attr('checked',false);
			$("input[name='discrimination'][value='21134002']").attr('readonly','readonly');
			$("input[name='discrimination'][value='21134002']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='397731000']").attr('checked',false);
			$("input[name='discrimination'][value='397731000']").attr('readonly','readonly');
			$("input[name='discrimination'][value='397731000']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='365873007']").attr('checked',false);
			$("input[name='discrimination'][value='365873007']").attr('readonly','readonly');
			$("input[name='discrimination'][value='365873007']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='IMGR']").attr('checked',false);
			$("input[name='discrimination'][value='IMGR']").attr('readonly','readonly');
			$("input[name='discrimination'][value='IMGR']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='74732009']").attr('checked',false);
			$("input[name='discrimination'][value='74732009']").attr('readonly','readonly');
			$("input[name='discrimination'][value='74732009']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='415229000']").attr('checked',false);
			$("input[name='discrimination'][value='415229000']").attr('readonly','readonly');
			$("input[name='discrimination'][value='415229000']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='365577002']").attr('checked',false);
			$("input[name='discrimination'][value='365577002']").attr('readonly','readonly');
			$("input[name='discrimination'][value='365577002']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='365956009']").attr('checked',false);
			$("input[name='discrimination'][value='365956009']").attr('readonly','readonly');
			$("input[name='discrimination'][value='365956009']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='OTH']").attr('checked',false);
			$("input[name='discrimination'][value='OTH']").attr('readonly','readonly');
			$("input[name='discrimination'][value='OTH']").attr('disabled','disabled');
			
			$("input[name='discrimination'][value='UNK']").attr('checked',false);
			$("input[name='discrimination'][value='UNK']").attr('readonly','readonly');
			$("input[name='discrimination'][value='UNK']").attr('disabled','disabled');			
			
			$("#discrimination_other").attr('disabled','disabled');
			$("#discrimination_other").val("");
		} 
	});	
	
	
	$("input[name='discrimination'][value='OTH']").change(function() {
		if($("input[name='discrimination'][value='OTH']").attr('checked') == true) {
			$("#discrimination_other").attr('disabled','');
			$("#discrimination_other").val("");
		} else {
			$("#discrimination_other").attr('disabled','disabled');
			$("#discrimination_other").val("");
		}		
	});
	
	$("input[name='discrimination'][value='OTH']").each(function() {
		if($("input[name='discrimination'][value='OTH']").attr('checked') == true) {
			$("#discrimination_other").attr('disabled','');			
		} else {
			$("#discrimination_other").attr('disabled','disabled');
			$("#discrimination_other").val("");
		}		
	});
	
	$("input[name='legal_status'][value='UNK']").change(function(){
		if($("input[name='legal_status'][value='UNK']").attr('checked') == true) {
			$("input[name='legal_status'][value='013-01']").attr('checked',false);
			$("input[name='legal_status'][value='013-01']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-01']").attr('disabled','disabled');
	
			$("input[name='legal_status'][value='013-02']").attr('checked',false);
			$("input[name='legal_status'][value='013-02']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-02']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-03']").attr('checked',false);
			$("input[name='legal_status'][value='013-03']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-03']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-04']").attr('checked',false);
			$("input[name='legal_status'][value='013-04']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-04']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-05']").attr('checked',false);
			$("input[name='legal_status'][value='013-05']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-05']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-06']").attr('checked',false);
			$("input[name='legal_status'][value='013-06']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-06']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-07']").attr('checked',false);
			$("input[name='legal_status'][value='013-07']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-07']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-08']").attr('checked',false);
			$("input[name='legal_status'][value='013-08']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-08']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-09']").attr('checked',false);
			$("input[name='legal_status'][value='013-09']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-09']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-10']").attr('checked',false);
			$("input[name='legal_status'][value='013-10']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-10']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-11']").attr('checked',false);
			$("input[name='legal_status'][value='013-11']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-11']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-12']").attr('checked',false);
			$("input[name='legal_status'][value='013-12']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-12']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-13']").attr('checked',false);
			$("input[name='legal_status'][value='013-13']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-13']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-14']").attr('checked',false);
			$("input[name='legal_status'][value='013-14']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-14']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-15']").attr('checked',false);
			$("input[name='legal_status'][value='013-15']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-15']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-16']").attr('checked',false);
			$("input[name='legal_status'][value='013-16']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-16']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-17']").attr('checked',false);
			$("input[name='legal_status'][value='013-17']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-17']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-18']").attr('checked',false);
			$("input[name='legal_status'][value='013-18']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-18']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-19']").attr('checked',false);
			$("input[name='legal_status'][value='013-19']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-19']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-20']").attr('checked',false);
			$("input[name='legal_status'][value='013-20']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-20']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-21']").attr('checked',false);
			$("input[name='legal_status'][value='013-21']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-21']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='CDA']").attr('checked',false);
			$("input[name='legal_status'][value='CDA']").attr('readonly','readonly');
			$("input[name='legal_status'][value='CDA']").attr('disabled','disabled');			
			
		} else {
			$("input[name='legal_status'][value='013-01']").attr('readonly','');
			$("input[name='legal_status'][value='013-01']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-02']").attr('readonly','');
			$("input[name='legal_status'][value='013-02']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-03']").attr('readonly','');
			$("input[name='legal_status'][value='013-03']").attr('disabled','');			
		
			$("input[name='legal_status'][value='013-04']").attr('readonly','');
			$("input[name='legal_status'][value='013-04']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-05']").attr('readonly','');
			$("input[name='legal_status'][value='013-05']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-06']").attr('readonly','');
			$("input[name='legal_status'][value='013-06']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-07']").attr('readonly','');
			$("input[name='legal_status'][value='013-07']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-08']").attr('readonly','');
			$("input[name='legal_status'][value='013-08']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-09']").attr('readonly','');
			$("input[name='legal_status'][value='013-09']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-10']").attr('readonly','');
			$("input[name='legal_status'][value='013-10']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-11']").attr('readonly','');
			$("input[name='legal_status'][value='013-11']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-12']").attr('readonly','');
			$("input[name='legal_status'][value='013-12']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-13']").attr('readonly','');
			$("input[name='legal_status'][value='013-13']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-14']").attr('readonly','');
			$("input[name='legal_status'][value='013-14']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-15']").attr('readonly','');
			$("input[name='legal_status'][value='013-15']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-16']").attr('readonly','');
			$("input[name='legal_status'][value='013-16']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-17']").attr('readonly','');
			$("input[name='legal_status'][value='013-17']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-18']").attr('readonly','');
			$("input[name='legal_status'][value='013-18']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-19']").attr('readonly','');
			$("input[name='legal_status'][value='013-19']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-20']").attr('readonly','');
			$("input[name='legal_status'][value='013-20']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-21']").attr('readonly','');
			$("input[name='legal_status'][value='013-21']").attr('disabled','');			
			
			$("input[name='legal_status'][value='CDA']").attr('readonly','');
			$("input[name='legal_status'][value='CDA']").attr('disabled','');			
			
		}
	});
		
	$("input[name='legal_status'][value='UNK']").each(function(){	
		if($("input[name='legal_status'][value='UNK']").attr('checked') == true ) {				
			$("input[name='legal_status'][value='013-01']").attr('checked',false);
			$("input[name='legal_status'][value='013-01']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-01']").attr('disabled','disabled');
	
			$("input[name='legal_status'][value='013-02']").attr('checked',false);
			$("input[name='legal_status'][value='013-02']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-02']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-03']").attr('checked',false);
			$("input[name='legal_status'][value='013-03']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-03']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-04']").attr('checked',false);
			$("input[name='legal_status'][value='013-04']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-04']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-05']").attr('checked',false);
			$("input[name='legal_status'][value='013-05']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-05']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-06']").attr('checked',false);
			$("input[name='legal_status'][value='013-06']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-06']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-07']").attr('checked',false);
			$("input[name='legal_status'][value='013-07']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-07']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-08']").attr('checked',false);
			$("input[name='legal_status'][value='013-08']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-08']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-09']").attr('checked',false);
			$("input[name='legal_status'][value='013-09']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-09']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-10']").attr('checked',false);
			$("input[name='legal_status'][value='013-10']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-10']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-11']").attr('checked',false);
			$("input[name='legal_status'][value='013-11']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-11']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-12']").attr('checked',false);
			$("input[name='legal_status'][value='013-12']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-12']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-13']").attr('checked',false);
			$("input[name='legal_status'][value='013-13']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-13']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-14']").attr('checked',false);
			$("input[name='legal_status'][value='013-14']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-14']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-15']").attr('checked',false);
			$("input[name='legal_status'][value='013-15']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-15']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-16']").attr('checked',false);
			$("input[name='legal_status'][value='013-16']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-16']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-17']").attr('checked',false);
			$("input[name='legal_status'][value='013-17']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-17']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-18']").attr('checked',false);
			$("input[name='legal_status'][value='013-18']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-18']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-19']").attr('checked',false);
			$("input[name='legal_status'][value='013-19']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-19']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-20']").attr('checked',false);
			$("input[name='legal_status'][value='013-20']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-20']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-21']").attr('checked',false);
			$("input[name='legal_status'][value='013-21']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-21']").attr('disabled','disabled');			
						
		} 
	});
	
	$("input[name='legal_status'][value='CDA']").change(function(){
		if($("input[name='legal_status'][value='CDA']").attr('checked') == true) {
			$("input[name='legal_status'][value='013-01']").attr('checked',false);
			$("input[name='legal_status'][value='013-01']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-01']").attr('disabled','disabled');
	
			$("input[name='legal_status'][value='013-02']").attr('checked',false);
			$("input[name='legal_status'][value='013-02']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-02']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-03']").attr('checked',false);
			$("input[name='legal_status'][value='013-03']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-03']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-04']").attr('checked',false);
			$("input[name='legal_status'][value='013-04']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-04']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-05']").attr('checked',false);
			$("input[name='legal_status'][value='013-05']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-05']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-06']").attr('checked',false);
			$("input[name='legal_status'][value='013-06']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-06']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-07']").attr('checked',false);
			$("input[name='legal_status'][value='013-07']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-07']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-08']").attr('checked',false);
			$("input[name='legal_status'][value='013-08']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-08']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-09']").attr('checked',false);
			$("input[name='legal_status'][value='013-09']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-09']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-10']").attr('checked',false);
			$("input[name='legal_status'][value='013-10']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-10']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-11']").attr('checked',false);
			$("input[name='legal_status'][value='013-11']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-11']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-12']").attr('checked',false);
			$("input[name='legal_status'][value='013-12']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-12']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-13']").attr('checked',false);
			$("input[name='legal_status'][value='013-13']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-13']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-14']").attr('checked',false);
			$("input[name='legal_status'][value='013-14']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-14']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-15']").attr('checked',false);
			$("input[name='legal_status'][value='013-15']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-15']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-16']").attr('checked',false);
			$("input[name='legal_status'][value='013-16']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-16']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-17']").attr('checked',false);
			$("input[name='legal_status'][value='013-17']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-17']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-18']").attr('checked',false);
			$("input[name='legal_status'][value='013-18']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-18']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-19']").attr('checked',false);
			$("input[name='legal_status'][value='013-19']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-19']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-20']").attr('checked',false);
			$("input[name='legal_status'][value='013-20']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-20']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-21']").attr('checked',false);
			$("input[name='legal_status'][value='013-21']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-21']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='UNK']").attr('checked',false);
			$("input[name='legal_status'][value='UNK']").attr('readonly','readonly');
			$("input[name='legal_status'][value='UNK']").attr('disabled','disabled');			
			
		} else {
			$("input[name='legal_status'][value='013-01']").attr('readonly','');
			$("input[name='legal_status'][value='013-01']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-02']").attr('readonly','');
			$("input[name='legal_status'][value='013-02']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-03']").attr('readonly','');
			$("input[name='legal_status'][value='013-03']").attr('disabled','');			
		
			$("input[name='legal_status'][value='013-04']").attr('readonly','');
			$("input[name='legal_status'][value='013-04']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-05']").attr('readonly','');
			$("input[name='legal_status'][value='013-05']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-06']").attr('readonly','');
			$("input[name='legal_status'][value='013-06']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-07']").attr('readonly','');
			$("input[name='legal_status'][value='013-07']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-08']").attr('readonly','');
			$("input[name='legal_status'][value='013-08']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-09']").attr('readonly','');
			$("input[name='legal_status'][value='013-09']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-10']").attr('readonly','');
			$("input[name='legal_status'][value='013-10']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-11']").attr('readonly','');
			$("input[name='legal_status'][value='013-11']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-12']").attr('readonly','');
			$("input[name='legal_status'][value='013-12']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-13']").attr('readonly','');
			$("input[name='legal_status'][value='013-13']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-14']").attr('readonly','');
			$("input[name='legal_status'][value='013-14']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-15']").attr('readonly','');
			$("input[name='legal_status'][value='013-15']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-16']").attr('readonly','');
			$("input[name='legal_status'][value='013-16']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-17']").attr('readonly','');
			$("input[name='legal_status'][value='013-17']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-18']").attr('readonly','');
			$("input[name='legal_status'][value='013-18']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-19']").attr('readonly','');
			$("input[name='legal_status'][value='013-19']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-20']").attr('readonly','');
			$("input[name='legal_status'][value='013-20']").attr('disabled','');			
			
			$("input[name='legal_status'][value='013-21']").attr('readonly','');
			$("input[name='legal_status'][value='013-21']").attr('disabled','');			
			
			$("input[name='legal_status'][value='UNK']").attr('readonly','');
			$("input[name='legal_status'][value='UNK']").attr('disabled','');			
			
		}
	});
	
	$("input[name='legal_status'][value='CDA']").each(function(){
		if($("input[name='legal_status'][value='CDA']").attr('checked') == true) {
			$("input[name='legal_status'][value='013-01']").attr('checked',false);
			$("input[name='legal_status'][value='013-01']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-01']").attr('disabled','disabled');
	
			$("input[name='legal_status'][value='013-02']").attr('checked',false);
			$("input[name='legal_status'][value='013-02']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-02']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-03']").attr('checked',false);
			$("input[name='legal_status'][value='013-03']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-03']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-04']").attr('checked',false);
			$("input[name='legal_status'][value='013-04']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-04']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-05']").attr('checked',false);
			$("input[name='legal_status'][value='013-05']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-05']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-06']").attr('checked',false);
			$("input[name='legal_status'][value='013-06']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-06']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-07']").attr('checked',false);
			$("input[name='legal_status'][value='013-07']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-07']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-08']").attr('checked',false);
			$("input[name='legal_status'][value='013-08']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-08']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-09']").attr('checked',false);
			$("input[name='legal_status'][value='013-09']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-09']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-10']").attr('checked',false);
			$("input[name='legal_status'][value='013-10']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-10']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-11']").attr('checked',false);
			$("input[name='legal_status'][value='013-11']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-11']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-12']").attr('checked',false);
			$("input[name='legal_status'][value='013-12']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-12']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-13']").attr('checked',false);
			$("input[name='legal_status'][value='013-13']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-13']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-14']").attr('checked',false);
			$("input[name='legal_status'][value='013-14']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-14']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-15']").attr('checked',false);
			$("input[name='legal_status'][value='013-15']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-15']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-16']").attr('checked',false);
			$("input[name='legal_status'][value='013-16']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-16']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-17']").attr('checked',false);
			$("input[name='legal_status'][value='013-17']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-17']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-18']").attr('checked',false);
			$("input[name='legal_status'][value='013-18']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-18']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-19']").attr('checked',false);
			$("input[name='legal_status'][value='013-19']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-19']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-20']").attr('checked',false);
			$("input[name='legal_status'][value='013-20']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-20']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='013-21']").attr('checked',false);
			$("input[name='legal_status'][value='013-21']").attr('readonly','readonly');
			$("input[name='legal_status'][value='013-21']").attr('disabled','disabled');
			
			$("input[name='legal_status'][value='UNK']").attr('checked',false);
			$("input[name='legal_status'][value='UNK']").attr('readonly','readonly');
			$("input[name='legal_status'][value='UNK']").attr('disabled','disabled');			
			
		} 
	});
	
	
	
	$("#1_where_live").change(function() {
		//var version = $().jquery;
		//alert("jquery version="+version);
		if($("#1_where_live").val() == "OTH") {
			$("#1_where_live_other").attr("disabled","");
			$("#1_where_live_other").val("");
			
			$("#1_any_support").val("").attr("disabled","");
			$("#1_any_support").val("").attr("selected", "selected");			
			$("#1_any_support").val("");
		} else {		
			$("#1_where_live_other").attr("disabled","disabled");
			$("#1_where_live_other").val("");
			
			if($("#1_where_live").val() == "024-01" || $("#1_where_live").val() == "024-02"
				|| $("#1_where_live").val() == "024-05" || $("#1_where_live").val() == "024-06"
					|| $("#1_where_live").val() == "024-08"|| $("#1_where_live").val() == "024-09") {
				
				$("#1_any_support").val("24A-04"); //The value of disabled selected option cannot be changed.
				//save the value in a hidden text field.
				$("#1_any_support_hidden").val("24A-04");
				$("#1_any_support").val("24A-04").attr("selected", "selected");					
				$("#1_any_support").val("24A-04").attr("disabled","disabled");
			} else {
				$("#1_any_support").val("24A-04").attr("disabled","");
				$("#1_any_support").val("24A-04").attr("selected", "");
				$("#1_any_support").val("");				
				$("#1_any_support").val("").attr("selected", "selected");
				$("#1_any_support").val("").attr("disabled","");
			}
		}
	});
	
	$("#1_where_live").each(function() {
		if($("#1_where_live").val() == "OTH") {
			$("#1_where_live_other").attr("disabled","");			
		} else {		
			$("#1_where_live_other").attr("disabled","disabled");
			$("#1_where_live_other").val("");
			
			if($("#1_where_live").val() == "024-01" || $("#1_where_live").val() == "024-02"
				|| $("#1_where_live").val() == "024-05" || $("#1_where_live").val() == "024-06"
					|| $("#1_where_live").val() == "024-08"|| $("#1_where_live").val() == "024-09") {
				
				$("#1_any_support_hidden").val("24A-04");
				$("#1_any_support").val("24A-04").attr("selected", "selected");					
				$("#1_any_support").val("24A-04").attr("disabled","disabled");
			} 
		}
		
	});
	
	$("#5_education_program_status").change(function() {		
		if($("#5_education_program_status").val() == "OTH") {
			$("#5_education_program_status_other").attr("disabled","");
			$("#5_education_program_status_other").val("");			
		} else {		
			$("#5_education_program_status_other").attr("disabled","disabled");
			$("#5_education_program_status_other").val("");
		}
	});
		
	$("#5_education_program_status").each(function() {
		if($("#5_education_program_status").val() == "OTH") {
			$("#5_education_program_status_other").attr("disabled","");			
		} else {		
			$("#5_education_program_status_other").attr("disabled","disabled");
			$("#5_education_program_status_other").val("");
		}
	});
	
	$("input[name='5_barriersFindingWork'][value='OTH']").change(function(){
		if($("input[name='5_barriersFindingWork'][value='OTH']").attr('checked') == true) {
			$("#5_barriersFindingWork_Other").attr("disabled","");				
		} else {
			$("#5_barriersFindingWork_Other").attr("disabled","disabled");
			$("#5_barriersFindingWork_Other").val("");			
		}
	});
	
	$("input[name='5_barriersFindingWork'][value='OTH']").each(function(){
		if($("input[name='5_barriersFindingWork'][value='OTH']").attr('checked') == true) {
			$("#5_barriersFindingWork_Other").attr("disabled","");				
		} else {
			$("#5_barriersFindingWork_Other").attr("disabled","disabled");
			$("#5_barriersFindingWork_Other").val("");			
		}
	});
	
	$("input[name='5_barriersFindingWork'][value='CDA']").change(function(){
		if($("input[name='5_barriersFindingWork'][value='CDA']").attr('checked') == true) {
			
			$("input[name='5_barriersFindingWork'][value='ADD']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='ADD']").attr('disabled','disabled');
			
			$("input[name='5_barriersFindingWork'][value='CAB']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='CAB']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='CON']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='CON']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='CTM']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='CTM']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='DIS']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='DIS']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='FOC']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='FOC']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='FFT']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='FFT']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='LOR']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='LOR']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='LGC']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='LGC']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='LIT']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='LIT']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='MSE']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='MSE']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='PHY']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='PHY']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='PCO']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='PCO']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='STG']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='STG']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='SYM']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='SYM']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='TRN']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='TRN']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='OTH']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='OTH']").attr('disabled','disabled');
	
			$("#5_barriersFindingWork_Other").attr('disabled','disabled');
			$("#5_barriersFindingWork_Other").val("");
		} else {				
			$("input[name='5_barriersFindingWork'][value='ADD']").attr('disabled','');							
			$("input[name='5_barriersFindingWork'][value='CAB']").attr('disabled','');	
			$("input[name='5_barriersFindingWork'][value='CON']").attr('disabled','');
			$("input[name='5_barriersFindingWork'][value='CTM']").attr('disabled','');			
			$("input[name='5_barriersFindingWork'][value='DIS']").attr('disabled','');			
			$("input[name='5_barriersFindingWork'][value='FOC']").attr('disabled','');			
			$("input[name='5_barriersFindingWork'][value='FFT']").attr('disabled','');			
			$("input[name='5_barriersFindingWork'][value='LOR']").attr('disabled','');			
			$("input[name='5_barriersFindingWork'][value='LGC']").attr('disabled','');			
			$("input[name='5_barriersFindingWork'][value='LIT']").attr('disabled','');			
			$("input[name='5_barriersFindingWork'][value='MSE']").attr('disabled','');			
			$("input[name='5_barriersFindingWork'][value='PHY']").attr('disabled','');			
			$("input[name='5_barriersFindingWork'][value='PCO']").attr('disabled','');			
			$("input[name='5_barriersFindingWork'][value='STG']").attr('disabled','');			
			$("input[name='5_barriersFindingWork'][value='SYM']").attr('disabled','');			
			$("input[name='5_barriersFindingWork'][value='TRN']").attr('disabled','');
			$("input[name='5_barriersFindingWork'][value='OTH']").attr('disabled','');
	
			$("#5_barriersFindingWork_Other").attr('disabled','disabled');
			$("#5_barriersFindingWork_Other").val("");
		}
	});
	
	$("input[name='5_barriersFindingWork'][value='CDA']").each(function(){
		if($("input[name='5_barriersFindingWork'][value='CDA']").attr('checked') == true) {
			
			$("input[name='5_barriersFindingWork'][value='ADD']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='ADD']").attr('disabled','disabled');
			
			$("input[name='5_barriersFindingWork'][value='CAB']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='CAB']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='CON']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='CON']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='CTM']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='CTM']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='DIS']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='DIS']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='FOC']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='FOC']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='FFT']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='FFT']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='LOR']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='LOR']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='LGC']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='LGC']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='LIT']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='LIT']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='MSE']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='MSE']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='PHY']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='PHY']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='PCO']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='PCO']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='STG']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='STG']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='SYM']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='SYM']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='TRN']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='TRN']").attr('disabled','disabled');
	
			$("input[name='5_barriersFindingWork'][value='OTH']").attr('checked',false);			
			$("input[name='5_barriersFindingWork'][value='OTH']").attr('disabled','disabled');
	
			$("#5_barriersFindingWork_Other").attr('disabled','disabled');
			$("#5_barriersFindingWork_Other").val("");
		} 
	});
	
	//autism
	$("input[name='6_medical_conditions'][value='408856003']").change(function(){
		if($(this).attr("checked")) {
			$("#6_medical_conditions_autism").attr("disabled","");
		} else {
			$("#6_medical_conditions_autism").attr("disabled","disabled");
			$("#6_medical_conditions_autism").val("");
		}
		
	});

	if($("input[name='6_medical_conditions'][value='408856003']").attr("checked")) {	
		$("#6_medical_conditions_autism").attr("disabled","");
	} else {
		$("#6_medical_conditions_autism").attr("disabled","disabled");
		$("#6_medical_conditions_autism").val("");
	}

	//phys health - other
	//410515003
	$("input[name='6_medical_conditions'][value='OTH']").change(function(){
		if($(this).attr("checked")) {
			$("#6_medical_conditions_other").attr("disabled","");
		} else {
			$("#6_medical_conditions_other").attr("disabled","disabled");
			$("#6_medical_conditions_other").val("");
		}
		
	});
	
	if($("input[name='6_medical_conditions'][value='OTH']").attr("checked")) {	
		$("#6_medical_conditions_other").attr("disabled","");
	} else {
		$("#6_medical_conditions_other").attr("disabled","disabled");
		$("#6_medical_conditions_other").val("");
	}
	
	$("input[name='6_medical_conditions'][value='UNK']").change(function() {
		if($("input[name='6_medical_conditions'][value='UNK']").attr('checked') == true) {
			$("input[name='6_medical_conditions'][value='127294003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='127294003']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='3723001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='3723001']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='408856003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='408856003']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='386813002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='386813002']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='363346000']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='363346000']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='19943007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='19943007']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='191415002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='191415002']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='46635009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='46635009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='44054006']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='44054006']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='359939009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='359939009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='73211009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='73211009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='72366004']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='72366004']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='84757009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='84757009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='15188001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='15188001']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='301095005']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='301095005']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='40468003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='40468003']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='66071002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='66071002']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='50711007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='50711007']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='86406008']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='86406008']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='38341003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='38341003']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='13644009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='13644009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='228156007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='228156007']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='45007003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='45007003']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='414916001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='414916001']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='64859006']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='64859006']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='118185001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='118185001']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='128613002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='128613002']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='8098009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='8098009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='95320005']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='95320005']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='230690007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='230690007']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='14304000']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='14304000']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='397540003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='397540003']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='OTH']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='OTH']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='CDA']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='CDA']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='26929004']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='26929004']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='424460009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='424460009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='44186003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='44186003']").attr('disabled','disabled');
	
			$("#6_medical_conditions_other").attr("disabled","disabled");
			$("#6_medical_conditions_other").val("");
			
			$("#6_medical_conditions_autism").attr("disabled","disabled");
			$("#6_medical_conditions_autism").val("");
		} else {
			$("input[name='6_medical_conditions'][value='127294003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='127294003']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='3723001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='3723001']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='408856003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='408856003']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='386813002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='386813002']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='363346000']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='363346000']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='19943007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='19943007']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='191415002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='191415002']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='46635009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='46635009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='44054006']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='44054006']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='359939009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='359939009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='73211009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='73211009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='72366004']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='72366004']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='84757009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='84757009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='15188001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='15188001']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='301095005']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='301095005']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='40468003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='40468003']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='66071002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='66071002']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='50711007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='50711007']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='86406008']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='86406008']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='38341003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='38341003']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='13644009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='13644009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='228156007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='228156007']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='45007003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='45007003']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='414916001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='414916001']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='64859006']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='64859006']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='118185001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='118185001']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='128613002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='128613002']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='8098009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='8098009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='95320005']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='95320005']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='230690007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='230690007']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='14304000']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='14304000']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='397540003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='397540003']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='OTH']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='OTH']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='CDA']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='CDA']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='26929004']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='26929004']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='424460009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='424460009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='44186003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='44186003']").attr('disabled','');
	
			$("#6_medical_conditions_other").attr("disabled","disabled");
			$("#6_medical_conditions_other").val("");
			
			$("#6_medical_conditions_autism").attr("disabled","disabled");
			$("#6_medical_conditions_autism").val("");
		}
	});
	
	$("input[name='6_medical_conditions'][value='CDA']").change(function() {
		if($("input[name='6_medical_conditions'][value='CDA']").attr('checked') == true) {
			$("input[name='6_medical_conditions'][value='127294003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='127294003']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='3723001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='3723001']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='408856003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='408856003']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='386813002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='386813002']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='363346000']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='363346000']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='19943007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='19943007']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='191415002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='191415002']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='46635009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='46635009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='44054006']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='44054006']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='359939009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='359939009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='73211009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='73211009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='72366004']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='72366004']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='84757009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='84757009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='15188001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='15188001']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='301095005']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='301095005']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='40468003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='40468003']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='66071002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='66071002']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='50711007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='50711007']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='86406008']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='86406008']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='38341003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='38341003']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='13644009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='13644009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='228156007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='228156007']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='45007003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='45007003']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='414916001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='414916001']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='64859006']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='64859006']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='118185001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='118185001']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='128613002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='128613002']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='8098009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='8098009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='95320005']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='95320005']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='230690007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='230690007']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='14304000']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='14304000']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='397540003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='397540003']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='OTH']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='OTH']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='UNK']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='UNK']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='26929004']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='26929004']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='424460009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='424460009']").attr('disabled','disabled');
	
			$("input[name='6_medical_conditions'][value='44186003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='44186003']").attr('disabled','disabled');
	
			$("#6_medical_conditions_other").attr("disabled","disabled");
			$("#6_medical_conditions_other").val("");
			
			$("#6_medical_conditions_autism").attr("disabled","disabled");
			$("#6_medical_conditions_autism").val("");
		} else {
			$("input[name='6_medical_conditions'][value='127294003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='127294003']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='3723001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='3723001']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='408856003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='408856003']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='386813002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='386813002']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='363346000']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='363346000']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='19943007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='19943007']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='191415002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='191415002']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='46635009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='46635009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='44054006']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='44054006']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='359939009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='359939009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='73211009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='73211009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='72366004']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='72366004']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='84757009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='84757009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='15188001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='15188001']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='301095005']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='301095005']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='40468003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='40468003']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='66071002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='66071002']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='50711007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='50711007']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='86406008']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='86406008']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='38341003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='38341003']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='13644009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='13644009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='228156007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='228156007']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='45007003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='45007003']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='414916001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='414916001']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='64859006']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='64859006']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='118185001']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='118185001']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='128613002']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='128613002']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='8098009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='8098009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='95320005']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='95320005']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='230690007']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='230690007']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='14304000']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='14304000']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='397540003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='397540003']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='OTH']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='OTH']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='UNK']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='UNK']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='26929004']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='26929004']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='424460009']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='424460009']").attr('disabled','');
	
			$("input[name='6_medical_conditions'][value='44186003']").attr('checked',false);			
			$("input[name='6_medical_conditions'][value='44186003']").attr('disabled','');
	
			$("#6_medical_conditions_other").attr("disabled","disabled");
			$("#6_medical_conditions_other").val("");
			
			$("#6_medical_conditions_autism").attr("disabled","disabled");
			$("#6_medical_conditions_autism").val("");
		}
	});
	
	if($("input[name='6_medical_conditions'][value='CDA']").attr('checked') == true  ||
			$("input[name='6_medical_conditions'][value='UNK']").attr('checked') == true	) {
		
		$("input[name='6_medical_conditions'][value='127294003']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='127294003']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='3723001']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='3723001']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='408856003']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='408856003']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='386813002']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='386813002']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='363346000']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='363346000']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='19943007']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='19943007']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='191415002']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='191415002']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='46635009']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='46635009']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='44054006']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='44054006']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='359939009']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='359939009']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='73211009']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='73211009']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='72366004']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='72366004']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='84757009']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='84757009']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='15188001']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='15188001']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='301095005']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='301095005']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='40468003']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='40468003']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='66071002']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='66071002']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='50711007']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='50711007']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='86406008']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='86406008']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='38341003']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='38341003']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='13644009']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='13644009']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='228156007']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='228156007']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='45007003']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='45007003']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='414916001']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='414916001']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='64859006']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='64859006']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='118185001']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='118185001']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='128613002']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='128613002']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='8098009']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='8098009']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='95320005']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='95320005']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='230690007']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='230690007']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='14304000']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='14304000']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='397540003']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='397540003']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='OTH']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='OTH']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='26929004']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='26929004']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='424460009']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='424460009']").attr('disabled','disabled');

		$("input[name='6_medical_conditions'][value='44186003']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='44186003']").attr('disabled','disabled');

		$("#6_medical_conditions_other").attr("disabled","disabled");
		$("#6_medical_conditions_other").val("");
		
		$("#6_medical_conditions_autism").attr("disabled","disabled");
		$("#6_medical_conditions_autism").val("");
	}
	
	if($("input[name='6_medical_conditions'][value='CDA']").attr('checked') == true ) {
		$("input[name='6_medical_conditions'][value='UNK']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='UNK']").attr('disabled','disabled');		
	}
	if($("input[name='6_medical_conditions'][value='UNK']").attr('checked') == true	) {
		$("input[name='6_medical_conditions'][value='CDA']").attr('checked',false);			
		$("input[name='6_medical_conditions'][value='CDA']").attr('disabled','disabled');		
	}
	
	
	$("input[name='diagnostic_categories'][value='CDA']").change(function() {
		if($("input[name='diagnostic_categories'][value='CDA']").attr('checked') == true) {
			
			$("input[name='diagnostic_categories'][value='17226007']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='17226007']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='197480006']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='197480006']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='2776000']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='2776000']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='DCA']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='DCA']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='44376007']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='44376007']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='72366004']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='72366004']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='50705009']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='50705009']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='66347000']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='66347000']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='MDGMC']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='MDGMC']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='46206005']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='46206005']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='33449004']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='33449004']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='58214004']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='58214004']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='39898005']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='39898005']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='31297008']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='31297008']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='87858002']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='87858002']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='SRD']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='SRD']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='0DH']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='0DH']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='UNK']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='UNK']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='228156007']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='228156007']").attr('disabled','disabled');
	
		} else {
			$("input[name='diagnostic_categories'][value='17226007']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='17226007']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='197480006']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='197480006']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='2776000']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='2776000']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='DCA']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='DCA']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='44376007']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='44376007']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='72366004']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='72366004']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='50705009']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='50705009']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='66347000']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='66347000']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='MDGMC']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='MDGMC']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='46206005']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='46206005']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='33449004']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='33449004']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='58214004']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='58214004']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='39898005']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='39898005']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='31297008']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='31297008']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='87858002']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='87858002']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='SRD']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='SRD']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='0DH']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='0DH']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='UNK']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='UNK']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='228156007']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='228156007']").attr('disabled','');
	
		}
	});
	
	$("input[name='diagnostic_categories'][value='UNK']").change(function() {
		if($("input[name='diagnostic_categories'][value='UNK']").attr('checked') == true) {
			
			$("input[name='diagnostic_categories'][value='17226007']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='17226007']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='197480006']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='197480006']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='2776000']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='2776000']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='DCA']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='DCA']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='44376007']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='44376007']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='72366004']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='72366004']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='50705009']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='50705009']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='66347000']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='66347000']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='MDGMC']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='MDGMC']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='46206005']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='46206005']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='33449004']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='33449004']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='58214004']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='58214004']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='39898005']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='39898005']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='31297008']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='31297008']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='87858002']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='87858002']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='SRD']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='SRD']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='0DH']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='0DH']").attr('disabled','disabled');
	
			$("input[name='diagnostic_categories'][value='CDA']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='CDA']").attr('disabled','disabled');						
			$("input[name='diagnostic_categories'][value='CDA']").attr('disabled','disabled');

	
			$("input[name='diagnostic_categories'][value='228156007']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='228156007']").attr('disabled','disabled');
	
		} else {
			$("input[name='diagnostic_categories'][value='17226007']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='17226007']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='197480006']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='197480006']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='2776000']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='2776000']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='DCA']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='DCA']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='44376007']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='44376007']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='72366004']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='72366004']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='50705009']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='50705009']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='66347000']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='66347000']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='MDGMC']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='MDGMC']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='46206005']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='46206005']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='33449004']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='33449004']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='58214004']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='58214004']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='39898005']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='39898005']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='31297008']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='31297008']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='87858002']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='87858002']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='SRD']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='SRD']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='0DH']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='0DH']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='CDA']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='CDA']").attr('disabled','');
	
			$("input[name='diagnostic_categories'][value='228156007']").attr('checked',false);			
			$("input[name='diagnostic_categories'][value='228156007']").attr('disabled','');
	
		}
	});
	
	
	if($("input[name='diagnostic_categories'][value='UNK']").attr('checked') == true || 
			$("input[name='diagnostic_categories'][value='CDA']").attr('checked') == true	) {
		
		$("input[name='diagnostic_categories'][value='17226007']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='17226007']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='197480006']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='197480006']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='2776000']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='2776000']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='DCA']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='DCA']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='44376007']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='44376007']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='72366004']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='72366004']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='50705009']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='50705009']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='66347000']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='66347000']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='MDGMC']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='MDGMC']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='46206005']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='46206005']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='33449004']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='33449004']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='58214004']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='58214004']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='39898005']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='39898005']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='31297008']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='31297008']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='87858002']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='87858002']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='SRD']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='SRD']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='0DH']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='0DH']").attr('disabled','disabled');

		$("input[name='diagnostic_categories'][value='228156007']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='228156007']").attr('disabled','disabled');
	}
	
	if($("input[name='diagnostic_categories'][value='UNK']").attr('checked') == true ) {
		$("input[name='diagnostic_categories'][value='CDA']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='CDA']").attr('disabled','disabled');
	} 
	
	if($("input[name='diagnostic_categories'][value='CDA']").attr('checked') == true) {
		$("input[name='diagnostic_categories'][value='UNK']").attr('checked',false);			
		$("input[name='diagnostic_categories'][value='UNK']").attr('disabled','disabled');
	}
		
	
	
	
	$("#power_attorney_property").change(function() {
		if($("#power_attorney_property").val() != "TRUE") {
			$("#power_attorney_property_name").attr("disabled","disabled");
			$("#power_attorney_property_address").attr("disabled","disabled");
			$("#power_attorney_property_phone").attr("disabled","disabled");
			$("#power_attorney_property_phoneExt").attr("disabled","disabled");	
			
			$("#power_attorney_property_name").val("");
			$("#power_attorney_property_address").val("");
			$("#power_attorney_property_phone").val("");
			$("#power_attorney_property_phoneExt").val("");
			
		} else {
			$("#power_attorney_property_name").attr("disabled","");
			$("#power_attorney_property_address").attr("disabled","");
			$("#power_attorney_property_phone").attr("disabled","");
			$("#power_attorney_property_phoneExt").attr("disabled","");	
			
			$("#power_attorney_property_name").val("");
			$("#power_attorney_property_address").val("");
			$("#power_attorney_property_phone").val("");
			$("#power_attorney_property_phoneExt").val("");
		}
	});
	
	$("#power_attorney_property").each(function() {
		if($("#power_attorney_property").val() != "TRUE") {
			$("#power_attorney_property_name").attr("disabled","disabled");
			$("#power_attorney_property_address").attr("disabled","disabled");
			$("#power_attorney_property_phone").attr("disabled","disabled");
			$("#power_attorney_property_phoneExt").attr("disabled","disabled");	
			
			$("#power_attorney_property_name").val("");
			$("#power_attorney_property_address").val("");
			$("#power_attorney_property_phone").val("");
			$("#power_attorney_property_phoneExt").val("");
			
		} else {
			$("#power_attorney_property_name").attr("disabled","");
			$("#power_attorney_property_address").attr("disabled","");
			$("#power_attorney_property_phone").attr("disabled","");
			$("#power_attorney_property_phoneExt").attr("disabled","");	
			
		}
	});
	
	$("#power_attorney_personal_care").change(function() {
		if($("#power_attorney_personal_care").val() != "TRUE") {
			$("#power_attorney_personal_care_name").attr("disabled","disabled");
			$("#power_attorney_personal_care_address").attr("disabled","disabled");
			$("#power_attorney_personal_care_phone").attr("disabled","disabled");
			$("#power_attorney_personal_care_phoneExt").attr("disabled","disabled");	
			
			$("#power_attorney_personal_care_name").val("");
			$("#power_attorney_personal_care_address").val("");
			$("#power_attorney_personal_care_phone").val("");
			$("#power_attorney_personal_care_phoneExt").val("");
			
		} else {
			$("#power_attorney_personal_care_name").attr("disabled","");
			$("#power_attorney_personal_care_address").attr("disabled","");
			$("#power_attorney_personal_care_phone").attr("disabled","");
			$("#power_attorney_personal_care_phoneExt").attr("disabled","");	
			
			$("#power_attorney_personal_care_name").val("");
			$("#power_attorney_personal_care_address").val("");
			$("#power_attorney_personal_care_phone").val("");
			$("#power_attorney_personal_care_phoneExt").val("");
		}
	});
	
	$("#power_attorney_personal_care").each(function() {
		if($("#power_attorney_personal_care").val() != "TRUE") {
			$("#power_attorney_personal_care_name").attr("disabled","disabled");
			$("#power_attorney_personal_care_address").attr("disabled","disabled");
			$("#power_attorney_personal_care_phone").attr("disabled","disabled");
			$("#power_attorney_personal_care_phoneExt").attr("disabled","disabled");	
			
			$("#power_attorney_personal_care_name").val("");
			$("#power_attorney_personal_care_address").val("");
			$("#power_attorney_personal_care_phone").val("");
			$("#power_attorney_personal_care_phoneExt").val("");
			
		} else {
			$("#power_attorney_personal_care_name").attr("disabled","");
			$("#power_attorney_personal_care_address").attr("disabled","");
			$("#power_attorney_personal_care_phone").attr("disabled","");
			$("#power_attorney_personal_care_phoneExt").attr("disabled","");	
		}
	});
	
	$("#court_appointed_guardian").change(function() {
		if($("#court_appointed_guardian").val() != "TRUE") {
			$("#guardian_name").attr("disabled","disabled");
			$("#guardian_address").attr("disabled","disabled");
			$("#guardian_phone").attr("disabled","disabled");
			$("#guardian_phoneExt").attr("disabled","disabled");	
			
			$("#guardian_name").val("");
			$("#guardian_address").val("");
			$("#guardian_phone").val("");
			$("#guardian_phoneExt").val("");
			
		} else {
			$("#guardian_name").attr("disabled","");
			$("#guardian_address").attr("disabled","");
			$("#guardian_phone").attr("disabled","");
			$("#guardian_phoneExt").attr("disabled","");	
			
			$("#guardian_name").val("");
			$("#guardian_address").val("");
			$("#guardian_phone").val("");
			$("#guardian_phoneExt").val("");
		}
	});
	
	$("#court_appointed_guardian").each(function() {
		if($("#court_appointed_guardian").val() != "TRUE") {
			$("#guardian_name").attr("disabled","disabled");
			$("#guardian_address").attr("disabled","disabled");
			$("#guardian_phone").attr("disabled","disabled");
			$("#guardian_phoneExt").attr("disabled","disabled");	
			
			$("#guardian_name").val("");
			$("#guardian_address").val("");
			$("#guardian_phone").val("");
			$("#guardian_phoneExt").val("");
			
		} else {
			$("#guardian_name").attr("disabled","");
			$("#guardian_address").attr("disabled","");
			$("#guardian_phone").attr("disabled","");
			$("#guardian_phoneExt").attr("disabled","");	
		}
	});
	
	
	$("input[name='presenting_issues'][value='OTH']").change(function() {
		if($("input[name='presenting_issues'][value='OTH']").attr('checked') == true) {
			$("#presenting_issues_other").attr('disabled','');
			$("#presenting_issues_other").val("");
		} else {
			$("#presenting_issues_other").attr('disabled','disabled');
			$("#presenting_issues_other").val("");
		}		
	});
	
	$("input[name='presenting_issues'][value='OTH']").each(function() {
		if($("input[name='presenting_issues'][value='OTH']").attr('checked') == true) {
			$("#presenting_issues_other").attr('disabled','');			
		} else {
			$("#presenting_issues_other").attr('disabled','disabled');
			$("#presenting_issues_other").val("");
		}		
	});
	
	$("#assessment_status").each(function() {
		if($("#assessment_status").val() == 'Completed') {
			$("#assessment_status").attr('disabled','disabled');			
		}
	});
	
			
	$("#reasonForAssessment").change(function() {
		if($("#reasonForAssessment").val() == 'OTHR') {
			$("#reason_for_assessment_other").attr('disabled','');
			$("#reason_for_assessment_other").val("");
		} else {		
			$("#reason_for_assessment_other").attr('disabled','disabled');
			$("#reason_for_assessment_other").val("");
		}
		
		var demographicId1=$("#clientId").val();;
		var reasonForAssessment1 = $("#reasonForAssessment").val();
		var params={demographicId1:demographicId1,reasonForAssessment1:reasonForAssessment1};

		$("#reasonForAssessmentBlock")
		.load("ocan_check_assessment_type.jsp?", params, function(data){
			
			 $('#reasonForAssessmentBlock').hide();

			if(data.match("ia_false")){				
				alert("You can not create a new initial assessment for this client for now. It already exists in the system.");
				$("#reasonForAssessment").val('').attr("selected", "selected");
			} else if(data.match("ra_false")){				
				alert("You can not do reassessment for this client for now.");
				$("#reasonForAssessment").val('').attr("selected", "selected");
			} else if(data.match("ia_exists_false")){				
				alert("You must create an initial assessment first.");
				$("#reasonForAssessment").val('').attr("selected", "selected");
			} 
		});

	});
	
	$("#reasonForAssessment").each(function() {
		if($("#reasonForAssessment").val() == 'OTHR') {
			$("#reason_for_assessment_other").attr('disabled','');			
		} else {		
			$("#reason_for_assessment_other").attr('disabled','disabled');
			$("#reason_for_assessment_other").val("");
		}
		
	});
	
	$("#consumerSelfAxCompleted").change(function() {
		if($("#consumerSelfAxCompleted").val()=='TRUE' || $("#consumerSelfAxCompleted").val()=='') { 
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('disabled','disabled');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('disabled','disabled');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('disabled','disabled');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('disabled','disabled');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('disabled','disabled');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('disabled','disabled');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('disabled','disabled');
			
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked',false);
			
			$("#otherReason").attr('disabled','disabled');
			$("#otherReason").val("");
			
		} else {
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('disabled','');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('disabled','');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('disabled','');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('disabled','');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('disabled','');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('disabled','');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('disabled','');
			
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked',false);
			
			$("#otherReason").attr('disabled','disabled');
			$("#otherReason").val("");
		}
	});
	
	$("#consumerSelfAxCompleted").each(function() {
	if($("#consumerSelfAxCompleted").val()=='TRUE' || $("#consumerSelfAxCompleted").val()=='') { 
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('disabled','disabled');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('disabled','disabled');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('disabled','disabled');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('disabled','disabled');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('disabled','disabled');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('disabled','disabled');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('disabled','disabled');
		
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('checked',false);
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('checked',false);
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('checked',false);
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('checked',false);
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('checked',false);
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('checked',false);
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked',false);
		
		$("#otherReason").attr('disabled','disabled');
		$("#otherReason").val("");
		
	} else {
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('disabled','');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('disabled','');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('disabled','');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('disabled','');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('disabled','');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('disabled','');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('disabled','');
			
		if($("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked') == true) {
			$("#otherReason").attr('disabled','');
			
		}else{
			$("#otherReason").attr('disabled','disabled');
			$("#otherReason").val("");
		}
	}
	
	});
	
	$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").change(function() {
		if($("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked') == true) {
			$("#otherReason").attr('disabled','');
			$("#otherReason").val("");
		}else{
			$("#otherReason").attr('disabled','disabled');
			$("#otherReason").val("");
		}
	
	});
	
	$("#hospitalized_mental_illness").change(function() { 
		if($("#hospitalized_mental_illness").val()=='TRUE'){ 
			$("#hospitalized_mental_illness_admissions").attr('disabled','');
			$("#hospitalized_mental_illness_admissions").val("");
			$("#hospitalized_mental_illness_days").attr('disabled','');
			$("#hospitalized_mental_illness_days").val("");
		} else {
			$("#hospitalized_mental_illness_admissions").attr('disabled','disabled');
			$("#hospitalized_mental_illness_admissions").val("");	
			$("#hospitalized_mental_illness_days").attr('disabled','disabled');
			$("#hospitalized_mental_illness_days").val("");
		}
	});	
	
	$("#hospitalized_mental_illness").each(function() { 
		if($("#hospitalized_mental_illness").val()=='TRUE'){ 
			$("#hospitalized_mental_illness_admissions").attr('disabled','');
			$("#hospitalized_mental_illness_days").attr('disabled','');
		} else {
			$("#hospitalized_mental_illness_admissions").attr('disabled','disabled');
			$("#hospitalized_mental_illness_admissions").val("");
			$("#hospitalized_mental_illness_days").attr('disabled','disabled');
			$("#hospitalized_mental_illness_days").val("");
		}
	});
			
			
	$("#6_physical_health_concerns").change(function() { 
		if($("#6_physical_health_concerns").val()=='TRUE'){ 
			$("input[name='6_physical_health_details'][value='118254002']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='279084009']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='119415007']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='302293008']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='300479008']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='106076001']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='118952005']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='365092005']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='102957003']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='118230007']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='118235002']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='410515003']").attr('disabled','');
			
			$("input[name='6_physical_health_details'][value='118254002']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='279084009']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='119415007']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='302293008']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='300479008']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='106076001']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118952005']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='365092005']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='102957003']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118230007']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118235002']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='410515003']").attr('checked',false);
			
			$("#6_physical_health_details_other").attr('disabled','disabled');
			$("#6_physical_health_details_other").val("");
			
		} else {
			$("input[name='6_physical_health_details'][value='118254002']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='279084009']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='119415007']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='302293008']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='300479008']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='106076001']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='118952005']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='365092005']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='102957003']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='118230007']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='118235002']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='410515003']").attr('disabled','disabled');
			
			$("input[name='6_physical_health_details'][value='118254002']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='279084009']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='119415007']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='302293008']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='300479008']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='106076001']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118952005']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='365092005']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='102957003']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118230007']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118235002']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='410515003']").attr('checked',false);
			
			$("#6_physical_health_details_other").attr('disabled','disabled');
			$("#6_physical_health_details_other").val("");			
		}
	});
	
	
	$("#6_physical_health_concerns").each(function() { 
		if($("#6_physical_health_concerns").val()=='TRUE'){ 
			$("input[name='6_physical_health_details'][value='118254002']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='279084009']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='119415007']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='302293008']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='300479008']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='106076001']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='118952005']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='365092005']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='102957003']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='118230007']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='118235002']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='410515003']").attr('disabled','');
			
			if($("input[name='6_physical_health_details'][value='410515003']").attr('checked') == true) {
				$("#6_physical_health_details_other").attr('disabled','');
				
			}else{
				$("#6_physical_health_details_other").attr('disabled','disabled');
				$("#6_physical_health_details_other").val("");
			}						
		} else {
			$("input[name='6_physical_health_details'][value='118254002']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='279084009']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='119415007']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='302293008']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='300479008']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='106076001']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='118952005']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='365092005']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='102957003']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='118230007']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='118235002']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='410515003']").attr('disabled','disabled');
			
			$("input[name='6_physical_health_details'][value='118254002']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='279084009']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='119415007']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='302293008']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='300479008']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='106076001']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118952005']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='365092005']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='102957003']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118230007']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118235002']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='410515003']").attr('checked',false);
			
			$("#6_physical_health_details_other").attr('disabled','disabled');
			$("#6_physical_health_details_other").val("");				
		}
	});
	
	$("input[name='6_physical_health_details'][value='410515003']").change(function() {
		if($("input[name='6_physical_health_details'][value='410515003']").attr('checked') == true) {
			$("#6_physical_health_details_other").attr('disabled','');
			$("#6_physical_health_details_other").val("");
		}else{
			$("#6_physical_health_details_other").attr('disabled','disabled');
			$("#6_physical_health_details_other").val("");
		}
	
	});
	
	
	//$("#serviceUseRecord_orgLHIN")
});

$("document").ready(function() {
	
	$("#otherContact").each(function() {
		if($("#otherContact").val()!='TRUE' || $("#otherContact").val()=='') { 
			for(var x=1;x<=3;x++) { 
				$("#"+x+"_otherContactType").attr('disabled','disabled');
				$("#"+x+"_otherContactType").val("");
				$("#"+x+"_otherContactName").attr('disabled','disabled');
				$("#"+x+"_otherContactName").val("");
				$("#"+x+"_otherContactAddress1").attr('disabled','disabled');
				$("#"+x+"_otherContactAddress1").val("");
				$("#"+x+"_otherContactAddress2").attr('disabled','disabled');
				$("#"+x+"_otherContactAddress2").val("");
				$("#"+x+"_otherContactCity").attr('disabled','disabled');
				$("#"+x+"_otherContactCity").val("");
				$("#"+x+"_otherContactProvince").attr('disabled','disabled');
				$("#"+x+"_otherContactProvince").val("");
				$("#"+x+"_otherContactPostalCode").attr('disabled','disabled');
				$("#"+x+"_otherContactPostalCode").val("");
				$("#"+x+"_otherContactPhoneNumber").attr('disabled','disabled');
				$("#"+x+"_otherContactPhoneNumber").val("");
				$("#"+x+"_otherContactPhoneNumberExt").attr('disabled','disabled');
				$("#"+x+"_otherContactPhoneNumberExt").val("");
				$("#"+x+"_otherContactEmail").attr('disabled','disabled');
				$("#"+x+"_otherContactEmail").val("");
				$("#"+x+"_otherContactLastSeen").attr('disabled','disabled');
				$("#"+x+"_otherContactLastSeen").val("");
			}
					
		} else {
			for(var x=1;x<=3;x++) { 
				$("#"+x+"_otherContactType").attr('disabled','');
				$("#"+x+"_otherContactName").attr('disabled','');
				$("#"+x+"_otherContactAddress1").attr('disabled','');
				$("#"+x+"_otherContactAddress2").attr('disabled','');
				$("#"+x+"_otherContactCity").attr('disabled','');
				$("#"+x+"_otherContactProvince").attr('disabled','');
				$("#"+x+"_otherContactPostalCode").attr('disabled','');
				$("#"+x+"_otherContactPhoneNumber").attr('disabled','');
				$("#"+x+"_otherContactPhoneNumberExt").attr('disabled','');
				$("#"+x+"_otherContactEmail").attr('disabled','');
				$("#"+x+"_otherContactLastSeen").attr('disabled','');
			}
		}
		});
		
	
	$("#otherContact").change(function() {		
	if($("#otherContact").val()!='TRUE' || $("#otherContact").val()=='') { 
		for(var x=1;x<=3;x++) { 
			$("#"+x+"_otherContactType").attr('disabled','disabled');
			$("#"+x+"_otherContactType").val("");
			$("#"+x+"_otherContactName").attr('disabled','disabled');
			$("#"+x+"_otherContactName").val("");
			$("#"+x+"_otherContactAddress1").attr('disabled','disabled');
			$("#"+x+"_otherContactAddress1").val("");
			$("#"+x+"_otherContactAddress2").attr('disabled','disabled');
			$("#"+x+"_otherContactAddress2").val("");
			$("#"+x+"_otherContactCity").attr('disabled','disabled');
			$("#"+x+"_otherContactCity").val("");
			$("#"+x+"_otherContactProvince").attr('disabled','disabled');
			$("#"+x+"_otherContactProvince").val("");
			$("#"+x+"_otherContactPostalCode").attr('disabled','disabled');
			$("#"+x+"_otherContactPostalCode").val("");
			$("#"+x+"_otherContactPhoneNumber").attr('disabled','disabled');
			$("#"+x+"_otherContactPhoneNumber").val("");
			$("#"+x+"_otherContactPhoneNumberExt").attr('disabled','disabled');
			$("#"+x+"_otherContactPhoneNumberExt").val("");
			$("#"+x+"_otherContactEmail").attr('disabled','disabled');
			$("#"+x+"_otherContactEmail").val("");
			$("#"+x+"_otherContactLastSeen").attr('disabled','disabled');
			$("#"+x+"_otherContactLastSeen").val("");
		}
				
	} else {
		for(var x=1;x<=3;x++) { 
			$("#"+x+"_otherContactType").attr('disabled','');
			$("#"+x+"_otherContactName").attr('disabled','');
			$("#"+x+"_otherContactAddress1").attr('disabled','');
			$("#"+x+"_otherContactAddress2").attr('disabled','');
			$("#"+x+"_otherContactCity").attr('disabled','');
			$("#"+x+"_otherContactProvince").attr('disabled','');
			$("#"+x+"_otherContactPostalCode").attr('disabled','');
			$("#"+x+"_otherContactPhoneNumber").attr('disabled','');
			$("#"+x+"_otherContactPhoneNumberExt").attr('disabled','');
			$("#"+x+"_otherContactEmail").attr('disabled','');
			$("#"+x+"_otherContactLastSeen").attr('disabled','');
		}
	}
	});
	
	
	$("#otherAgency").each(function() {
		if($("#otherAgency").val()!='TRUE' || $("#otherAgency").val()=='') { 
			for(var x=1;x<=3;x++) { 
				$("#"+x+"_otherAgencyType").attr('disabled','disabled');
				$("#"+x+"_otherAgencyType").val("");
				$("#"+x+"_otherAgencyName").attr('disabled','disabled');
				$("#"+x+"_otherAgencyName").val("");
				$("#"+x+"_otherAgencyAddress1").attr('disabled','disabled');
				$("#"+x+"_otherAgencyAddress1").val("");
				$("#"+x+"_otherAgencyAddress2").attr('disabled','disabled');
				$("#"+x+"_otherAgencyAddress2").val("");
				$("#"+x+"_otherAgencyCity").attr('disabled','disabled');
				$("#"+x+"_otherAgencyCity").val("");
				$("#"+x+"_otherAgencyProvince").attr('disabled','disabled');
				$("#"+x+"_otherAgencyProvince").val("");
				$("#"+x+"_otherAgencyPostalCode").attr('disabled','disabled');
				$("#"+x+"_otherAgencyPostalCode").val("");
				$("#"+x+"_otherAgencyPhoneNumber").attr('disabled','disabled');
				$("#"+x+"_otherAgencyPhoneNumber").val("");
				$("#"+x+"_otherAgencyPhoneNumberExt").attr('disabled','disabled');
				$("#"+x+"_otherAgencyPhoneNumberExt").val("");
				$("#"+x+"_otherAgencyEmail").attr('disabled','disabled');
				$("#"+x+"_otherAgencyEmail").val("");
				$("#"+x+"_otherAgencyLastSeen").attr('disabled','disabled');
				$("#"+x+"_otherAgencyLastSeen").val("");
			}
					
		} else {
			for(var x=1;x<=3;x++) { 
				$("#"+x+"_otherAgencyType").attr('disabled','');
				$("#"+x+"_otherAgencyName").attr('disabled','');
				$("#"+x+"_otherAgencyAddress1").attr('disabled','');
				$("#"+x+"_otherAgencyAddress2").attr('disabled','');
				$("#"+x+"_otherAgencyCity").attr('disabled','');
				$("#"+x+"_otherAgencyProvince").attr('disabled','');
				$("#"+x+"_otherAgencyPostalCode").attr('disabled','');
				$("#"+x+"_otherAgencyPhoneNumber").attr('disabled','');
				$("#"+x+"_otherAgencyPhoneNumberExt").attr('disabled','');
				$("#"+x+"_otherAgencyEmail").attr('disabled','');
				$("#"+x+"_otherAgencyLastSeen").attr('disabled','');
			}
		}
		});
	
	
	
	$("#otherAgency").change(function() {		
		if($("#otherAgency").val()!='TRUE' || $("#otherAgency").val()=='') { 
			for(var x=1;x<=3;x++) { 
				$("#"+x+"_otherAgencyType").attr('disabled','disabled');
				$("#"+x+"_otherAgencyType").val("");
				$("#"+x+"_otherAgencyName").attr('disabled','disabled');
				$("#"+x+"_otherAgencyName").val("");
				$("#"+x+"_otherAgencyAddress1").attr('disabled','disabled');
				$("#"+x+"_otherAgencyAddress1").val("");
				$("#"+x+"_otherAgencyAddress2").attr('disabled','disabled');
				$("#"+x+"_otherAgencyAddress2").val("");
				$("#"+x+"_otherAgencyCity").attr('disabled','disabled');
				$("#"+x+"_otherAgencyCity").val("");
				$("#"+x+"_otherAgencyProvince").attr('disabled','disabled');
				$("#"+x+"_otherAgencyProvince").val("");
				$("#"+x+"_otherAgencyPostalCode").attr('disabled','disabled');
				$("#"+x+"_otherAgencyPostalCode").val("");
				$("#"+x+"_otherAgencyPhoneNumber").attr('disabled','disabled');
				$("#"+x+"_otherAgencyPhoneNumber").val("");
				$("#"+x+"_otherAgencyPhoneNumberExt").attr('disabled','disabled');
				$("#"+x+"_otherAgencyPhoneNumberExt").val("");
				$("#"+x+"_otherAgencyEmail").attr('disabled','disabled');
				$("#"+x+"_otherAgencyEmail").val("");
				$("#"+x+"_otherAgencyLastSeen").attr('disabled','disabled');
				$("#"+x+"_otherAgencyLastSeen").val("");
			}
					
		} else {
			for(var x=1;x<=3;x++) { 
				$("#"+x+"_otherAgencyType").attr('disabled','');
				$("#"+x+"_otherAgencyName").attr('disabled','');
				$("#"+x+"_otherAgencyAddress1").attr('disabled','');
				$("#"+x+"_otherAgencyAddress2").attr('disabled','');
				$("#"+x+"_otherAgencyCity").attr('disabled','');
				$("#"+x+"_otherAgencyProvince").attr('disabled','');
				$("#"+x+"_otherAgencyPostalCode").attr('disabled','');
				$("#"+x+"_otherAgencyPhoneNumber").attr('disabled','');
				$("#"+x+"_otherAgencyPhoneNumberExt").attr('disabled','');
				$("#"+x+"_otherAgencyEmail").attr('disabled','');
				$("#"+x+"_otherAgencyLastSeen").attr('disabled','');
			}
		}
		});	
	
	
	
	
	
});


function submitOcanForm() {
	var status = document.getElementById('assessment_status').value;
	if(status != 'Completed') {
		$('#ocan_staff_form').unbind('submit').submit();		
		return true;
	}
	if(!$("#ocan_staff_form").valid()) {
		alert('Validation failed. Please check all required fields highlighted');
		return false;
	}
	
	if(!validateStartAndCompletionDates()) {
		return false;
	}
	
	if($("#clientDOBType").val() == 'EST') {		
		if($("#date_of_birth").val().length==0) {			
			alert('Date of Birth (YYYY-MM-DD) - Please provide date of birth');
			$("#date_of_birth").focus();
			return false;
		}		
	}
	if($("#date_of_birth").val().length==0) {	
		if($("#clientDOBType").val() != 'UNK') {
			alert('Date of Birth - do not know the date of birth? please choose unknown');
			$("#clientDOBType").focus();
			return false;
		}
	}
	
	if($("#clientDOBType").val() == 'UNK') {		
		if($("#date_of_birth").val().length!=0) {			
			alert('Date of Birth (YYYY-MM-DD) - You should not provide date of birth and choose unknown at the same time');
			$("#date_of_birth").focus();
			return false;
		}		
	}
	
	
	var newCount = $("#center_count").val(); 
	var ocanLeadNumber = 0;
	for(var x=1;x<=newCount;x++) { 
		if($("#serviceUseRecord_exitDate"+x).val().length != 0) {		
			if($("#serviceUseRecord_exitDisposition"+x).val().length==0) {			
				alert('Exit Disposition - Please specify one');
				$("#serviceUseRecord_exitDisposition"+x).focus();
				return false;
			}		
		}
		
		if($("#serviceUseRecord_OCANLead"+x).val() == 'TRUE') { 
			ocanLeadNumber ++;
		}
	}
	if (ocanLeadNumber > 1) {
		alert('OCAN Lead can only have one answered Yes');
		$("#serviceUseRecord_OCANLead1").focus();
		return false;
	}	
		
	if($("#completedByOCANLead").val() == 'FALSE') { 
		if($("#reasonForAssessment").val() != 'REV' && $("#reasonForAssessment").val() != 'REK') {
			alert('Reason for OCAN -- This OCAN was not completed by OCAN lead. You can only choose Re-view or Rekey');
			$("#reasonForAssessment").focus();
			return false;
		}
		
	}	
	
	
	if($("#reasonForAssessment").val() == 'OTHR') {		
		if($("#reason_for_assessment_other").val().length==0) {			
			alert('Reason for assessment - Please specify other');
			$("#reason_for_assessment_other").focus();
			return false;
		}		
	}
	
		
	if($("#consumerSelfAxCompleted").val() == 'FALSE') {
		if($("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('checked') == false &&
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('checked') == false &&
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('checked') == false &&
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('checked') == false &&
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('checked') == false &&
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('checked') == false &&
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked') == false 
		) {
			alert('Reason for the Consumer Self-Assessment not completed -- please select one');
			$("#consumerSelfAxCompleted").focus();
			return false;
		}
	}
	
	if($("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked') == true) {
			if($("#otherReason").val().length==0) {
				alert('Other reason for the Consumer Self-Assessment not completed -- please provide other reason');
				$("#otherReason").focus();
				return false;
			}
	}	
		
	
	
	
	if($("input[name='immigration_issues'][value='8']").attr('checked') == true) {
		if($("#immigration_issues_other").val().length == 0) {
			alert('Immigration issues - please provider other');
			$("#immigration_issues_other").focus();
			return false;
		}
	}
	
	if($("#discrimination").val() == '410515003') {
		if($("#discrimination_other").val().length == 0) {
			alert('Discrimination - please provide additional information');
			$("#discrimination_other").focus();
			return false;
		}
	}
	
	
	if($("input[name='6_medical_conditions'][value='408856003']").attr('checked') == true) {
		if($("#6_medical_conditions_autism").val().length == 0) {
			alert('Medical Conditions - please provide autism information');
			$("#6_medical_conditions_autism").focus();
			return false;
		}
	}
	
	if($("input[name='6_medical_conditions'][value='410515003']").attr('checked') == true) {
		if($("#6_medical_conditions_other").val().length == 0) {
			alert('Medical Conditions - please provide other information');
			$("#6_medical_conditions_other").focus();
			return false;
		}
	}
	

	if($("input[name='6_physical_health_details'][value='410515003']").attr('checked') == true) {
		if($("#6_physical_health_details_other").val().length == 0) {
			alert('Physical Health Details - please provide other information');
			$("#6_physical_health_details_other").focus();
			return false;
		}
	}
	
	
	//medication_1_se_description
	var medicationFailed=false;
	$("input[value='410515003']").each(function(){	
		if(medicationFailed) {return;}
		if($(this).attr('name').indexOf('_se_description')!=-1) {
			if($(this).attr('checked') == true) {
				var cbName = $(this).attr('name');
				var otherName = cbName + '_other';
				if($("#"+otherName).val().length == 0) {
					alert('Medication - Side Effects - please provide other information');
					$("#"+otherName).focus();
					medicationFailed=true;
				}
			}			
		}		
	});	
	if(medicationFailed) {
		return false;
	}
	
	if($("input[name='symptom_checklist'][value='410515003']").attr('checked') == true) {
		if($("#symptom_checklist_other").val().length == 0) {
			alert('Symptom Checklist - please provide other information');
			$("#symptom_checklist_other").focus();
			return false;
		}
	}
	
	if($("input[name='risks'][value='410515003']").attr('checked') == true) {
		if($("#risks_other").val().length == 0) {
			alert('Risks - please provide other information');
			$("#risks_other").focus();
			return false;
		}
	}
	
	if($("input[name='addiction_type'][value='410515003']").attr('checked') == true) {
		if($("#addiction_type_other").val().length == 0) {
			alert('Risks - please provide other information');
			$("#addiction_type_other").focus();
			return false;
		}
	}	
	

		
	if($("#hospitalized_mental_illness").val()=='TRUE') {
		if($("#hospitalized_mental_illness_admissions").val().length == 0) {
			alert('Please input - Total Number of Admissions for Mental Health Reasons');
			$("#hospitalized_mental_illness_admissions").focus();
			return false;
		}
		if($("#hospitalized_mental_illness_days").val().length == 0) {
			alert('Please input - Total Number of Hospitalization Days for Mental Health Reasons');
			$("#hospitalized_mental_illness_days").focus();
			return false;
		}
	}	
	
	
	
	var ppCount=0;
	$("input[name='presenting_issues']").each(function(){	
		if($(this).attr('checked')==true) {
			ppCount++;
		}
	});	
	
	if(ppCount==0) {
		alert('You must choose atleast 1 presenting issue');
		$("input[name='presenting_issues']").focus();
		return false;
	}
	
	
	if($("#assessment_status").val() == 'Completed') {
		var r = confirm("Are you sure you have completed this assessment?");
		if(r == true) {
			return true;
		}
		else {
			return false;
		}
	}
		
	return true;
}


function validateStartAndCompletionDates()
{
	var start_date = $("#startDate").val();
	var completion_date = $("#completionDate").val();
	
	sd = start_date.split("-");
	cd = completion_date.split("-");
			
	var sdd = new Date(sd[0],sd[1]-1,sd[2]);
	var cdd = new Date(cd[0],cd[1]-1,cd[2]);

	
	if(cdd < sdd) {
		alert('Completion date must be equal or after start date');
		return false;
	}
	
	var millis = cdd.getTime()-sdd.getTime();
	var secs = millis/1000;
	var mins = secs/60;
	var hours = mins/60;
	var days = hours/24;
	
	// a warning message may be displayed when
	//Completion Date is greater than 30 days from Start Date but this should not
	//prevent you from completing and saving the OCAN.
	if(days > 30) {
		alert('Completion Date must be within 30 days of Start Date');
		
	}
	return true;
}

function checkForRequired(elementId) {
	var domain = elementId.substring(0,elementId.indexOf('_'));
	var domain_q1_val = $("#"+domain+"_1").val();
	if(domain_q1_val == '1' || domain_q1_val == '2') {
		return true;
	}
	return false;
}
