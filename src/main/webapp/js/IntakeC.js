function setEthnicRadio(form) {
	var arrEthnic = form.elements['intake.radioIsAboriginal'];
	for(var x=0;x<arrEthnic.length;x++) {
		if(arrEthnic[x].checked) {
			if(arrEthnic[x].value != 2) {
				unCheckNoAboriginal(form);
			}
		}
	}
}

function setNonAboriginal(form) {
	var arrEthnic = form.elements['intake.radioIsAboriginal'];
	for(var x=0;x<arrEthnic.length;x++) {
		if(arrEthnic[x].value == 2) {
			arrEthnic[x].checked=true;
		} else {
			arrEthnic[x].checked=false;
		}
	}
}

function unCheckNoAboriginal(form) {
	var arrEthnic = form.elements['intake.radioRaceCaucasian'];
	for(var x=0;x<arrEthnic.length;x++) {
		arrEthnic[x].checked=false;
	}
}

function unCheckOtherIssues(form) {
	var otherCB = form.elements['intake.cboxOtherIssue'].checked;
	if(otherCB === false) {
		form.elements['intake.cboxHealthCareIssueOther'].checked = false;
		form.elements['intake.cboxSocialServiceIssueOther'].checked = false;
		form.elements['intake.cboxBankingIssueOther'].checked = false;
		form.elements['intake.cboxIdentificationIssueOther'].checked = false;
		form.elements['intake.cboxImmigrationIssueOther'].checked = false;
		form.elements['intake.cboxPhysicalIssueOther'].checked = false;
		form.elements['intake.cboxIsolationIssueOther'].checked = false;
	}
	
}

function unCheckReferralAgency(form) {
	var otherCB = form.elements['intake.cboxReferralByOtherAgency'].checked;
	if(otherCB === false) {
		form.elements['intake.cboxReferralByStreetNurseOther'].checked = false;
		form.elements['intake.cboxReferralByStreetIDWorkerOther'].checked = false;
		form.elements['intake.cboxReferralByStreetHealthReceptionOther'].checked = false;
		form.elements['intake.cboxReferralByFredVictorCentreOther'].checked = false;
	}
}

function unCheckReferralCriminal(form) {
	var otherCB = form.elements['intake.cboxReferralByCriminalJusticeSystem'].checked;
	if(otherCB === false) {
		form.elements['intake.cboxReferralByPolice'].checked = false;
		form.elements['intake.cboxReferralByCourt'].checked = false;
		form.elements['intake.cboxReferralByDetentionCenter'].checked = false;
		form.elements['intake.cboxReferralByProbation'].checked = false;
		form.elements['intake.cboxReferralBySafeBeds'].checked = false;
	}
}

function checkBaseLegalStatusToOther(input,form) {
	if(input.checked === false) {
		return;
	}
	var arrOther = form.elements['intake.radioBaseLegalStatus'];
	
	for(var x=0;x<arrOther.length;x++) {
		if(arrOther[x].value == 12) {
			//alert(arrOther[x]);
			arrOther[x].checked=true;
		}
	}	
}

function unCheckBaseLegalStatus(form) {
	form.elements['intake.cboxFamilyLawIssues1'].checked = false;
	form.elements['intake.cboxProblemsWithPolice1'].checked = false;
			
}


function checkCurrentLegalStatusToOther(input,form) {
	if(input.checked === false) {
		return;
	}
	var arrOther = form.elements['intake.radioCurrLegalStatus'];
	
	for(var x=0;x<arrOther.length;x++) {
		if(arrOther[x].value == 12) {
			//alert(arrOther[x]);
			arrOther[x].checked=true;
		}
	}	
}

function unCheckCurrentLegalStatus(form) {
	form.elements['intake.cboxFamilyLawIssues2'].checked = false;
	form.elements['intake.cboxProblemsWithPolice2'].checked = false;
			
}



function checkAnxietyDisorder(input,form) {
	if(input.checked === false) {
		return;
	}
	var arrOther = form.elements['intake.radioPrimaryDiagnosis'];
	
	for(var x=0;x<arrOther.length;x++) {
		if(arrOther[x].value == 2) {
			//alert(arrOther[x]);
			arrOther[x].checked=true;
			break;
		}
	}	
}

function uncheckPrimaryDiagnosisChildren(form) {
	form.elements['intake.cboxPTSd'].checked = false;
	form.elements['intake.cboxOCd'].checked = false;
	form.elements['intake.cboxSubstanceAnxietyDisorder'].checked = false;
	form.elements['intake.cboxOtherAnxietyDisorder'].checked = false;
			
}

function check2ndAnxietyDisorder(input,form) {
	if(input.checked === false) {
		return;
	}	
	form.elements['intake.cbox2ndAnxietyDisorder'].checked=true;
}

function uncheck2ndAnxietyDisorderChildren(input,form) {
	if(input.checked === false) {
		form.elements['intake.cbox2ndAnxietyDisorderPSd'].checked = false;
		form.elements['intake.cbox2ndAnxietyDisorderOCd'].checked = false;
		form.elements['intake.cbox2ndAnxietyDisorderFromSubstance'].checked = false;
		form.elements['intake.cbox2ndAnxietyDisorderOther'].checked = false;
	}
}

function checkPrimaryIncomeOther(input,form) {
	if(input.checked === false) {
		return;
	}
	var arrOther = form.elements['intake.radioBasePrimaryIncomeSource'];
	
	for(var x=0;x<arrOther.length;x++) {
		if(arrOther[x].value == 9) {
			//alert(arrOther[x]);
			arrOther[x].checked=true;
		}
	}	
}

function unClickPrimaryIncomeChildren(form) {
	var arrOther = form.elements['intake.radioBasePrimaryIncomeSourceOther'];
	for(var x=0;x<arrOther.length;x++) {
		arrOther[x].checked=false;
	}
}

function check2ndIncomeOther(input,form) {
	if(input.checked === false) {
		return;
	}
	form.elements['intake.cboxBase2ndIncomeOther'].checked=true;
}

function unCheck2ndIncomeOther(input,form) {
	if(input.checked === true) {
		return;
	}
	form.elements['intake.cboxBase2ndIncomePanhandlingOther'].checked=false;
	form.elements['intake.cboxBase2ndIncomeInformalOther'].checked=false;
}

function unclickCurrPrimaryIncomeChildren(form) {
	var arrOther = form.elements['intake.radioCurrPrimaryIncomeSourceOther'];
	for(var x=0;x<arrOther.length;x++) {
		arrOther[x].checked=false;
	}
}

function clickCurrPrimaryIncomeOther(input,form) {
	var arrOther = form.elements['intake.radioCurrPrimaryIncomeSource'];
	for(var x=0;x<arrOther.length;x++) {
		if(arrOther[x].value == 9) {
			arrOther[x].checked=true;
			break;
		}
	}
}

function uncheck2ndIncomeOtherChildren(input,form) {
	if(input.checked === true) {
		return;
	}
	form.elements['intake.cbox2ndIncomePanhandlingOther'].checked=false;
	form.elements['intake.cbox2ndIncomeInformalOther'].checked=false;
}

function checkCurrent2ndIncomeOther(input,form) {
	if(input.checked === false) {
		return;
	}
	form.elements['intake.cbox2ndIncomeOther'].checked=true;
}
