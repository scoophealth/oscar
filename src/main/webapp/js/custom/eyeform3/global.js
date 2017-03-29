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
	if(document.getElementsByName("ext_appNo")[0] != null){
		if(appointmentNo.length == 0){
			appointmentNo = document.getElementsByName("ext_appNo")[0].value;
		}
	}
	var data = jQuery("#"+listId).serialize();
	jQuery.ajax({type:'POST',url:ctx+'/eyeform/Eyeform.do?method=getMeasurementText&name='+listId+'&appointmentNo='+appointmentNo,data:data,success: function(data){
		//whereTo.value = whereTo.value + data + "\n";
		whereTo.innerHTML = whereTo.innerHTML + data + "<br/>";
		if(document.getElementsByName("cp.examination")[0] != null){
			document.getElementsByName("cp.examination")[0].value = whereTo.innerHTML;
		}
		if(jQuery("#specialProblem") != null){
			jQuery("#specialProblem").val(whereTo.innerHTML);
		}
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


			function selectList2() {
  				var list = document.inputForm.elements['fromlist2'];
  				for(var x=0;x<list.options.length;x++) {
  					list.options[x].selected=true;
  				}
  			}
			function addSection1(fromList,toList) {
				//alert(fromList);
				//alert(toList);
				selectOption2(toList);
				for(var x=0;x<fromList.options.length;x++) {
					if(fromList.options[x].selected) {
						selectHeaderFromSection1(fromList.options[x].value,toList);
					}
				}
				
			}

			function selectHeaderFromSection1(section,toList) {
				if(section == 'GLASSES HISTORY') {
					selectOption1(toList,'Glasses Rx');
				}
				if(section == 'VISION ASSESSMENT') {
					selectOption1(toList,'Distance vision (sc)');
					selectOption1(toList,'Distance vision (cc)');
					selectOption1(toList,'Distance vision (ph)');
					selectOption1(toList,'Intermediate vision (sc)');
					selectOption1(toList,'Intermediate vision (cc)');
					selectOption1(toList,'Near vision (sc)');
					selectOption1(toList,'Near vision (cc)');
				}	
				if(section == 'STEREO VISION') {
					selectOption1(toList,'Fly test');
					selectOption1(toList,'Stereo-acuity');
				}
				if(section == 'VISION MEASUREMENT') {
					selectOption1(toList,'Keratometry');
					selectOption1(toList,'Auto-refraction');
					selectOption1(toList,'Manifest distance');
					selectOption1(toList,'Manifest near');
					selectOption1(toList,'Cycloplegic refraction');
				}
				if(section == 'INTRAOCULAR PRESSURE') {
					selectOption1(toList,'NCT');
					selectOption1(toList,'Applanation');
					selectOption1(toList,'Central corneal thickness');
				}
				if(section == 'REFRACTIVE') {
					selectOption1(toList,'Dominance');
					selectOption1(toList,'Mesopic pupil size');
					selectOption1(toList,'Angle Kappa');
				}
				if(section == 'OTHER EXAM') {
					selectOption1(toList,'Colour vision');
					selectOption1(toList,'Pupil');
					selectOption1(toList,'Amsler grid');
					selectOption1(toList,'Potential acuity meter');
					selectOption1(toList,'Confrontation fields');
					selectOption1(toList,'Maddox rod');
					selectOption1(toList,'Bagolini test');
					selectOption1(toList,'Worth 4 Dot (distance)');
					selectOption1(toList,'Worth 4 Dot (near)');
				}
				if(section == 'DUCTION/DIPLOPIA TESTING') {
					selectOption1(toList,'DUCTION/DIPLOPIA TESTING');
				}
				if(section == 'DEVIATION MEASUREMENT') {
					selectOption1(toList,'Primary gaze');
					selectOption1(toList,'Up gaze');
					selectOption1(toList,'Down gaze');
					selectOption1(toList,'Right gaze');
					selectOption1(toList,'Left gaze');
					selectOption1(toList,'Right head tilt');
					selectOption1(toList,'Left head tilt');
					selectOption1(toList,'Near');
					selectOption1(toList,'Near with +3D add');
					selectOption1(toList,'Far distance');
				}
				if(section == 'EXTERNAL/ORBIT') {
					selectOption1(toList,'Face');
					selectOption1(toList,'Retropulsion');
					selectOption1(toList,'Hertel');
				}
				if(section == 'EYELID/NASOLACRIMAL DUCT') {
					selectOption1(toList,'Upper lid');
					selectOption1(toList,'Lower lid');
					selectOption1(toList,'Lacrimal lake');
					selectOption1(toList,'Lacrimal irrigation');
					selectOption1(toList,'Punctum');
					selectOption1(toList,'Nasolacrimal duct');
					selectOption1(toList,'Dye disappearance');
				}
				if(section == 'EYELID MEASUREMENT') {
					selectOption1(toList,'Margin reflex distance');
					selectOption1(toList,'Inferior scleral show');
					selectOption1(toList,'Levator function');
					selectOption1(toList,'Lagophthalmos');
					selectOption1(toList,'Blink reflex');
					selectOption1(toList,'Cranial Nerve VII function');
					selectOption1(toList,'Bells phenomenon');
					selectOption1(toList,'Schirmer test');
				}
				if(section == 'ANTERIOR SEGMENT') {
					selectOption1(toList,'Cornea');
					selectOption1(toList,'Conjunctiva/Sclera');
					selectOption1(toList,'Anterior chamber');
					selectOption1(toList,'Angle');
					selectOption1(toList,'Iris');
					selectOption1(toList,'Lens');
				}
				if(section == 'POSTERIOR SEGMENT') {
					selectOption1(toList,'Optic disc');
					selectOption1(toList,'C/D ratio');
					selectOption1(toList,'Macula');
					selectOption1(toList,'Retina');
					selectOption1(toList,'Vitreous');
				}
				
			}

			function selectOption2(toList){
				for(var x=0;x<toList.options.length;x++) {
					if(toList.options[x].selected == true){
						toList.options[x].selected=false;
					}
				}
			}
			function selectOption1(toList,val) {
				for(var x=0;x<toList.options.length;x++) {
					if(toList.options[x].value == val) {
						toList.options[x].selected=true;
					}
				}
			}