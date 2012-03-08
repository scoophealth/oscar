var X       = 10;
var pBSmall = 30;
var small   = 60;
var normal  = 166;
var medium  = 272;
var large   = 378;
var full    = 649;

var rowOne = ['cpp_currentHis','cpp_familyHis','cpp_diagnostics'];
var rowTwo = ['cpp_pastOcularHis','cpp_medicalHis','cpp_patientLog'];
var rowThree = ['cpp_ocularMeds','cpp_otherMeds','cpp_misc'];

function rowOneX1(){
	for(var x=0;x<rowOne.length;x++) {
		jQuery("textarea[name='"+rowOne[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowOne[x]+"']").css("height",X);
	}
}

function rowOneSmall1(){
	for(var x=0;x<rowOne.length;x++) {
		jQuery("textarea[name='"+rowOne[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowOne[x]+"']").css("height",small);
	}
}

function rowOneNormal1(){
	for(var x=0;x<rowOne.length;x++) {
		jQuery("textarea[name='"+rowOne[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowOne[x]+"']").css("height",normal);
	}
}

function rowOneLarge1(){
	for(var x=0;x<rowOne.length;x++) {
		jQuery("textarea[name='"+rowOne[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowOne[x]+"']").css("height",large);
	}
}

function rowOneFull1(){
	for(var x=0;x<rowOne.length;x++) {
		jQuery("textarea[name='"+rowOne[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowOne[x]+"']").css("height",full);
	}
}

function rowTwoX1(){
	for(var x=0;x<rowTwo.length;x++) {
		jQuery("textarea[name='"+rowTwo[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowTwo[x]+"']").css("height",X);
	}
}

function rowTwoSmall1(){
	for(var x=0;x<rowTwo.length;x++) {
		jQuery("textarea[name='"+rowTwo[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowTwo[x]+"']").css("height",small);
	}
}

function rowTwoNormal1(){
	for(var x=0;x<rowTwo.length;x++) {
		jQuery("textarea[name='"+rowTwo[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowTwo[x]+"']").css("height",normal);
	}
}

function rowTwoLarge1(){
	for(var x=0;x<rowTwo.length;x++) {
		jQuery("textarea[name='"+rowTwo[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowTwo[x]+"']").css("height",large);
	}
}

function rowTwoFull1(){
	for(var x=0;x<rowTwo.length;x++) {
		jQuery("textarea[name='"+rowTwo[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowTwo[x]+"']").css("height",full);
	}
}



function rowThreeX1(){
	for(var x=0;x<rowThree.length;x++) {
		jQuery("textarea[name='"+rowThree[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowThree[x]+"']").css("height",X);
	}
}

function rowThreeSmall1(){
	for(var x=0;x<rowThree.length;x++) {
		jQuery("textarea[name='"+rowThree[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowThree[x]+"']").css("height",small);
	}
}

function rowThreeNormal1(){
	for(var x=0;x<rowThree.length;x++) {
		jQuery("textarea[name='"+rowThree[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowThree[x]+"']").css("height",normal);
	}
}

function rowThreeLarge1(){
	for(var x=0;x<rowThree.length;x++) {
		jQuery("textarea[name='"+rowThree[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowThree[x]+"']").css("height",large);
	}
}

function rowThreeFull1(){
	for(var x=0;x<rowThree.length;x++) {
		jQuery("textarea[name='"+rowThree[x]+"']").css("overflow","auto");
		jQuery("textarea[name='"+rowThree[x]+"']").css("height",full);
	}
}

function reset1() {
	rowOneNormal1();
	rowTwoNormal1();
	rowThreeNormal1();
}


//STANDARD GLOBAL FROM EYEFORM
function sendConRequestTickler(ctx,demographicNo){
		var req=ctx+"/eyeform/Eyeform.do?method=specialReqTickler&demographicNo="+demographicNo;
        if (document.EctConsultationFormRequestForm.ackdoc.checked==true || document.EctConsultationFormRequestForm.ackfront.checked==true){
        	if (document.EctConsultationFormRequestForm.ackdoc.checked==true)
        		req+='&docFlag=true';
        	if (document.EctConsultationFormRequestForm.ackfront.checked==true){
        		req+='&frontFlag=true&providerNo=';
        		req+=document.EctConsultationFormRequestForm.providerl.value;
        	}
        	//alert(req);
        	jQuery.ajax({url:req,dataType:'script'});
        }else {
        	alert("Please check the checkbox before click this button!");
        }
}

function sendConReportTickler(ctx,demographicNo){
	var req=ctx+"/eyeform/Eyeform.do?method=specialRepTickler&demographicNo="+demographicNo;
	if (document.eyeForm.ack.checked==true){
		req+='&docFlag=true';
		//alert(req);
		jQuery.ajax({url:req,dataType:'script'});
	}else{
		alert("Please check the checkbox before click this button!");
	}
}



function addExam(ctx,listId,whereTo,appointmentNo) {
	//POST the list box, and get back the text to add.
	var data = jQuery("#"+listId).serialize();
	jQuery.ajax({type:'POST',url:ctx+'/eyeform/Eyeform.do?method=getMeasurementText&name='+listId+'&appointmentNo='+appointmentNo,data:data,success: function(data){
		whereTo.value = whereTo.value + data + "\n";
	}});
}

function addSection(fromList,toList) {
	//alert(fromList);
	//alert(toList);
	for(var x=0;x<fromList.options.length;x++) {
		if(fromList.options[x].selected) {
			selectHeaderFromSection(fromList.options[x].value,toList);
		}
	}

}

function selectHeaderFromSection(section,toList) {
	if(section == 'VISION ASSESSMENT') {
		selectOption(toList,'Auto-refraction');
		selectOption(toList,'Keratometry');
		selectOption(toList,'Distance vision (sc)');
		selectOption(toList,'Distance vision (cc)');
		selectOption(toList,'Distance vision (ph)');
		selectOption(toList,'Near vision (sc)');
		selectOption(toList,'Near vision (cc)');
	}
	if(section == 'MANIFEST VISION') {
		selectOption(toList,'Manifest distance');
		selectOption(toList,'Manifest near');
		selectOption(toList,'Cycloplegic refraction');
		//selectOption(toList,'Best corrected distance vision');

	}
	if(section == 'INTRAOCULAR PRESSURE') {
		selectOption(toList,'NCT');
		selectOption(toList,'Applanation');
		selectOption(toList,'Central corneal thickness');
	}
	if(section == 'OTHER EXAM') {
		selectOption(toList,'Colour vision');
		selectOption(toList,'Pupil');
		selectOption(toList,'Amsler grid');
		selectOption(toList,'Potential acuity meter');
		selectOption(toList,'Confrontation fields');
	}
	if(section == 'EOM/STEREO') {
		selectOption(toList,'EOM');
	}
	if(section == 'ANTERIOR SEGMENT') {
		selectOption(toList,'Cornea');
		selectOption(toList,'Conjunctiva/Sclera');
		selectOption(toList,'Anterior chamber');
		selectOption(toList,'Angle');
		selectOption(toList,'Iris');
		selectOption(toList,'Lens');
	}
	if(section == 'POSTERIOR SEGMENT') {
		selectOption(toList,'Optic disc');
		selectOption(toList,'C/D ratio');
		selectOption(toList,'Macula');
		selectOption(toList,'Retina');
		selectOption(toList,'Vitreous');
	}
	if(section == 'EXTERNAL/ORBIT') {
		selectOption(toList,'Face');
		selectOption(toList,'Upper lid');
		selectOption(toList,'Lower lid');
		selectOption(toList,'Punctum');
		selectOption(toList,'Lacrimal lake');
		selectOption(toList,'Retropulsion');
		selectOption(toList,'Hertel');
	}
	if(section == 'NASOLACRIMAL DUCT') {
		selectOption(toList,'Lacrimal irrigation');
		selectOption(toList,'Nasolacrimal duct');
		selectOption(toList,'Dye disappearance');
	}
	if(section == 'EYELID MEASUREMENT') {
		selectOption(toList,'Margin reflex distance');
		selectOption(toList,'Inferior scleral show');
		selectOption(toList,'Levator function');
		selectOption(toList,'Lagophthalmos');
		selectOption(toList,'Blink reflex');
		selectOption(toList,'Cranial nerve VII function');
		selectOption(toList,'Bells phenomenon');
	}
}

function selectOption(toList,val) {
	for(var x=0;x<toList.options.length;x++) {
		if(toList.options[x].value == val) {
			toList.options[x].selected=true;
		}
	}
}