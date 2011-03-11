function currentProAdd(val,ob) {
	var str='';
	switch(val) {
		case 'cHis':
			str='<%=(String)request.getAttribute("currentHistory") %>';
			break;
		case 'pHis':
			str='<%=(String)request.getAttribute("pastOcularHistory") %>';
			break;
		case 'oMeds':
			str='<%=(String)request.getAttribute("ocularMedication") %>';
			break;
		case 'dTest':
			str='<%=(String)request.getAttribute("diagnosticNotes") %>';
			break;
		case 'oProc':
			str='<%=(String)request.getAttribute("ocularProc") %>';
			break;
		case 'specs':
			str='<%=(String)request.getAttribute("specs") %>';
			break;
		case 'impress':
			str='<%=(String)request.getAttribute("impression") %>';
			break;
		case 'followup':
			str='<%=(String)request.getAttribute("followup") %>';
			break;
		case 'probooking':
			str='<%=(String)request.getAttribute("probooking") %>';
			break;
		case 'testbooking':
			str='<%=(String)request.getAttribute("testbooking") %>';
			break;
			
	}
	jQuery("#"+ob).val(jQuery("#"+ob).val() + str);
}
