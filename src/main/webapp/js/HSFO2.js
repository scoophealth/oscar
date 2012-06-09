function confirmReset(form1)
{
  form1 = document.getElementById('form1');

  if (window.confirm("Are you SURE you want to clear all form data? Click OK to continue, Cancel to return to the form and save."))
  {

    form1.reset();
  } else
    return false;
}

function confirmExit(frm)
{
  //if (window.confirm("Are you SURE you want to exit without saving? Click OK to continue, Cancel to return to the form and save."))
  if (window.confirm("Any UNSAVED changes will be lost. Click OK to exit the form, or CANCEL to return to the form."))
  {
    // closing script
    window.close();
  } else
    return false;
}

function checkform(registrationForm)
{ 
  form1 = document.getElementById('form1');
  if (form1.Patient_Id.value == "")
  {
    alert("Please enter patient's registration ID.");
    form1.Patient_Id.focus();
    return false;
  }
  if (!/^\d\d\d\d$/.test(form1.VisitDate_Id_year.value) || form1.VisitDate_Id_year.value == "")
  {
    alert("Please enter patient's year of last visit.");
    form1.VisitDate_Id_year.focus();
    return false;
  }
  
 if(registrationForm==true) {
  if (form1.LName.value == "")
  {
    alert("Please enter patient's last name.");
    form1.LName.focus();
    return false;
  }
  if (form1.FName.value == "")
  {
    alert("Please enter patient's first name.");
    form1.FName.focus();
    return false;
  }
  
//  if (!/^\d\d\d\d$/.test(form1.consentDate_year.value) || form1.consentDate_year.value == "")
//  {
//    alert("Please enter patient's year of consent.");
//    form1.consentDate_year.focus();
//    return false;
//  }
  
  if (!/^\d\d\d\d$/.test(form1.BirthDate_year.value) || form1.BirthDate_year.value == "")
  {
    alert("Please enter patient's year of birth.");
    form1.BirthDate_year.focus();
    return false;
  } 
  if (form1.BirthDate_year.value < 1901)
  {
    alert("Please check that the year of birth is greater than 1900");
    form1.BirthDate_year.focus();
    return false;
  }
  if (form1.EmrHCPId1.value == "")
  {
    alert("Please enter patient's Study ID of MD.");
    form1.EmrHCPId1.focus();
    return false;
  }
  if (form1.EmrHCPId2.value == "")
  {
    alert("Please enter patient's MD's last name.");
    form1.EmrHCPId2.focus();
    return false;
  }
  if (form1.PostalCode.value != "" && !/^\D\d\D\d\D\d$/.test(form1.PostalCode.value) && !/^\D\d\D \d\D\d$/.test(form1.PostalCode.value) )
  {
    alert("Invalid patient's postal code.");
    form1.PostalCode.focus();
    return false;
  }
  if (!form1.Sex[0].checked && !form1.Sex[1].checked)
  {
    alert("Please select patient's sex.");
    return false;
  }
//  if (form1.PharmacyName.value == "")
//  {
//    alert("Please enter patient's pharmacy name.");
//    form1.PharmacyName.focus();
//    return false;
//  }
//  if (form1.PharmacyLocation.value == "")
//  {
//    alert("Please enter patient's pharmacy address.");
//    form1.PharmacyLocation.focus();
//    return false;
//  }
  if (!form1.Ethnic_White.checked && !form1.Ethnic_Black.checked && !form1.Ethnic_EIndian.checked && !form1.Ethnic_Pakistani.checked
          && !form1.Ethnic_SriLankan.checked && !form1.Ethnic_Bangladeshi.checked && !form1.Ethnic_Chinese.checked
          && !form1.Ethnic_Japanese.checked && !form1.Ethnic_Korean.checked && !form1.Ethnic_FirstNation.checked
          && !form1.Ethnic_Other.checked && !form1.Ethnic_Hispanic.checked && !form1.Ethnic_Refused.checked
          && !form1.Ethnic_Unknown.checked)
  {
    alert("Please select patient's ethnicity.");
    form1.Ethnic_White.focus();
    return false;
  }
  if (form1.Ethnic_Refused.checked && (form1.Ethnic_White.checked || form1.Ethnic_Black.checked || form1.Ethnic_EIndian.checked || form1.Ethnic_Pakistani.checked
          || form1.Ethnic_SriLankan.checked || form1.Ethnic_Bangladeshi.checked || form1.Ethnic_Chinese.checked
          || form1.Ethnic_Japanese.checked || form1.Ethnic_Korean.checked || form1.Ethnic_FirstNation.checked
          || form1.Ethnic_Other.checked || form1.Ethnic_Hispanic.checked || form1.Ethnic_Unknown.checked) )
  {
	  alert("Please do not select any other ethnicity if you already chose Refused.");
	  form1.Ethnic_White.focus();
	  return false;
  }
  if (!form1.sel_TimeAgoDx[0].checked && !form1.sel_TimeAgoDx[1].checked && !form1.sel_TimeAgoDx[2].checked)
  {
    alert("Please select patient's diagnosis period.");
    form1.sel_TimeAgoDx[0].focus();
    return false;
  }
  if (!form1.Change_importance.value == ""
          && (form1.Change_importance.value < 1 || form1.Change_importance.value > 10 || /\D/.test(form1.Change_importance.value)))
  {
    alert("Please check lifestyle change is between 1 and 10");
    form1.Change_importance.focus();
    return false;
  }
  if (!form1.Change_confidence.value == ""
          && (form1.Change_confidence.value < 1 || form1.Change_confidence.value > 10 || /\D/.test(form1.Change_confidence.value)))
  {
    alert("Please check confidence is between 1 and 10");
    form1.Change_confidence.focus();
    return false;
  }
 }
//  if (form1.consentDate_year.value < 2007)
//  {
//    alert("Please check that the year of consent is greater than 2006");
//    form1.consentDate_year.focus();
//    return false;
//  }
  
  if (!form1.Height_unit[0].checked && !form1.Height_unit[1].checked)
  {
    alert("Please select patient's height measurement.");
    form1.Height_unit[0].focus();
    return false;
  }

  if (form1.HeightP1.value == "" && form1.HeightP2.value == "")
  {
    alert("Please enter patient's height.");
    form1.HeightP1.focus();
    return false;
  } else if ( isNaN(form1.HeightP1.value) || isNaN(form1.HeightP2.value))
  {
    alert("Please enter patient's height in NNN.N format.");
    form1.HeightP1.focus();
    return false;
  } 
  else if ( form1.Height_unit[0].checked && ( form1.HeightP1.value < 100 || form1.HeightP1.value > 250 ) )
  {
    alert("Please enter patient's height between 100CM and 250CM.");
    form1.HeightP1.focus();
    return false;
  }
  else if ( form1.Height_unit[1].checked && ( form1.HeightP1.value < 40 || form1.HeightP1.value > 99 ) )
  {
    //1 inch = 2.54 cm
    alert("Please enter patient's height between 40inch and 99inch.");
    form1.HeightP1.focus();
    return false;
  }
  
  if (!form1.SBP.value == "" && (form1.SBP.value < 40 || form1.SBP.value > 300 || isNaN(parseFloat(form1.SBP.value))))
  {
    alert("Please check that SBP is between 40 and 300");
    form1.SBP.focus();
    return false;
  }
  if(form1.SBP.value == "" || form1.SBP.value == null) {
	 alert("Please check that SBP is between 40 and 300");
	 form1.SBP.focus();
	 return false; 
  }
  if (!form1.DBP.value == "" && (form1.DBP.value < 0 || form1.DBP.value > 160 || isNaN(parseFloat(form1.DBP.value))))
  {
    alert("Please check that DBP is between 0 and 160");
    form1.DBP.focus();
    return false;
  }  
//  if (!form1.SBP_goal.value == "" && (form1.SBP_goal.value < 40 || form1.SBP_goal.value > 300 || isNaN(parseFloat(form1.SBP_goal.value))))
//  {
//    alert("Please check that SBP goal is between 40 and 300");
//    form1.SBP_goal.focus();
//    return false;
//  }
//  if (!form1.DBP_goal.value == "" && (form1.DBP_goal.value < 0 || form1.DBP_goal.value > 140 || isNaN(parseFloat(form1.DBP_goal.value))))
//  {
//    alert("Please check that DBP goal is between 0 and 140");
//    form1.DBP_goal.focus();
//    return false;
//  }
  if (!form1.WeightP1.value == "" && (form1.WeightP1.value < 35 || form1.WeightP1.value > 250 || isNaN( form1.WeightP1.value )) && form1.Weight_unit[0].checked)
  {
    alert("Please check that weight is between 35 and 250 kg");
    form1.WeightP1.focus();
    return false;
  }
  if (!form1.WeightP1.value == "" && (form1.WeightP1.value < 77.2 || form1.WeightP1.value > 551.2 || isNaN( form1.WeightP1.value )) && form1.Weight_unit[1].checked )
  {
    alert("Please check that weight is between 77.2 and 551.2 lbs"); //1kg=2.2lbs
    form1.WeightP1.focus();
    return false;
  }
  // add by victor for waist unit null bug
  if (form1.WeightP1.value != "" && !form1.Weight_unit[0].checked && !form1.Weight_unit[1].checked)
  {
    alert("Please choose the weight unit");
    form1.WeightP1.focus();
  }
  // end of add
  if (!form1.WaistP1.value == "" && (form1.WaistP1.value < 40 || form1.WaistP1.value > 200 || isNaN(form1.WaistP1.value)) && form1.Waist_unit[0].checked )
  {
    alert("Please check that waist is between 40 and 200 cm");
    form1.WaistP1.focus();
    return false;
  } 
  if (!form1.WaistP1.value == "" && (form1.WaistP1.value < 15.7 || form1.WaistP1.value > 78.7 || isNaN(form1.WaistP1.value)) && form1.Waist_unit[1].checked )
  {
    alert("Please check that waist is between 15.7 and 78.7 inches"); //1cm = 0.39iches
    form1.WaistP1.focus();
    return false;
  }  
  if (form1.WaistP1.value != "" && !form1.Waist_unit[0].checked && !form1.Waist_unit[1].checked)
  {
    alert("Please choose the waist unit");
    form1.WaistP1.focus();
  }
  if (!form1.TC_HDLP1.value == "" && (form1.TC_HDLP1.value < 1 || form1.TC_HDLP1.value > 30 || isNaN(form1.TC_HDLP1.value)))
  {
    alert("Please check that TC_HDL is between 1 and 30");
    form1.TC_HDLP1.focus();
    return false;
  }
  if (!form1.LDLP1.value == "" && (form1.LDLP1.value < 0.5 || form1.LDLP1.value > 10 || isNaN(form1.LDLP1.value)))
  {
    alert("Please check that LDL is between 0.5 and 10");
    form1.LDLP1.focus();
    return false;
  }    
  if (!form1.HDLP1.value == "" && ((form1.HDLP1.value == 0 && form1.HDLP2.value<4) || form1.HDLP1.value > 8 || isNaN(form1.HDLP1.value)))
  {
    alert("Please check that HDL is between 0.4 and 8");
    form1.HDLP1.focus();
    return false;
  }
  
  if (!form1.A1CP1.value == "" && (form1.A1CP1.value < 4 || form1.A1CP1.value > 26 || (form1.A1CP1.value==25 && form1.A1CP2.value>0) || isNaN(form1.A1CP1.value)))
  {
    alert("Please check that A1C is between 0.04 and 0.25");
    form1.A1CP2.focus();
    return false;
  }
  if (!form1.FBSP1.value == "" && (form1.FBSP1.value < 2 || form1.FBSP1.value > 40 || isNaN(form1.FBSP1.value)))
  {
    alert("Please check that FBS is between 2 and 40");
    form1.FBSP1.focus();
    return false;
  } 
  if (!form1.egfr.value == "" && (form1.egfr.value < 5 || form1.egfr.value > 120 || isNaN(form1.egfr.value)))
  {
    alert("Please check that eGFR is between 5 and 120");
    form1.egfr.focus();
    return false;
  }
  if (!form1.acrP1.value == "" && (form1.acrP1.value < 0 || form1.acrP1.value > 5000 || isNaN(form1.acrP1.value)))
  {
    alert("Please check that ACR is between 0 and 5000");
    form1.acrP1.focus();
    return false;
  }
  if (!form1.TriglyceridesP1.value == "" && (form1.TriglyceridesP1.value > 30 || isNaN(form1.TriglyceridesP1.value)))
  {
    alert("Please check that triglycerides is between 0.2 and 30");
    form1.TriglyceridesP1.focus();
    return false;
  }
  if (!form1.TriglyceridesP2.value == "" && ((form1.TriglyceridesP1.value == 0 && form1.TriglyceridesP2.value < 2) || (form1.TriglyceridesP1.value ==30 && form1.TriglyceridesP2.value > 0) || isNaN(form1.TriglyceridesP2.value)))
  {
    alert("Please check that trigly ceridges is between 0.2 and 30");
    form1.TriglyceridesP2.focus();
    return false;
  }
  if (!/^\d\d\d\d$/.test(form1.TC_HDL_LabresultsDate_year.value) && isNaN(form1.TC_HDL_LabresultsDate_year.value))
  {
    alert("Please enter digital numbers for year of lab work lipids.");
    form1.TC_HDL_LabresultsDate_year.focus();
    return false;
  }
  if (!/^\d\d\d\d$/.test(form1.A1C_LabresultsDate_year.value) && isNaN(form1.A1C_LabresultsDate_year.value) )
  {
    alert("Please enter digital numbers for year of AIC and FBS.");
    form1.A1C_LabresultsDate_year.focus();
    return false;
  }
  if (!/^\d\d\d\d$/.test(form1.egfrYear.value) && isNaN(form1.egfrYear.value) )
  {
    alert("Please enter digital numbers for year of eGFR and ACR.");
    form1.egfrYear.focus();
    return false;
  }
  if( (form1.TC_HDL_LabresultsDate_year.value == "" || form1.TC_HDL_LabresultsDate_month.value == "" || form1.TC_HDL_LabresultsDate_day.value == "") && (form1.LDLP1.value!=null && !form1.LDLP1.value == "" && !isNaN(form1.LDLP1.value)) ) {
	  alert("Please enter date for lab work lipids.");
	    form1.TC_HDL_LabresultsDate_year.focus();
	    return false;
  }
  if( (form1.TC_HDL_LabresultsDate_year.value == "" || form1.TC_HDL_LabresultsDate_month.value == "" || form1.TC_HDL_LabresultsDate_day.value == "") && (!form1.LDLP2.value == "" && !isNaN(form1.LDLP2.value)) ) {
	  alert("Please enter date for lab work lipids.");
	    form1.TC_HDL_LabresultsDate_year.focus();
	    return false;
  }
  if( (form1.TC_HDL_LabresultsDate_year.value == "" || form1.TC_HDL_LabresultsDate_month.value == "" || form1.TC_HDL_LabresultsDate_day.value == "") && (!form1.TC_HDLP1.value == "" && !isNaN(form1.TC_HDLP1.value)) ) {
	  alert("Please enter date for lab work lipids.");
	    form1.TC_HDL_LabresultsDate_year.focus();
	    return false;
  }
  if( (form1.TC_HDL_LabresultsDate_year.value == "" || form1.TC_HDL_LabresultsDate_month.value == "" || form1.TC_HDL_LabresultsDate_day.value == "") && (!form1.HDLP1.value == "" && !isNaN(form1.HDLP1.value)) ){
	  alert("Please enter date for lab work lipids.");
	    form1.TC_HDL_LabresultsDate_year.focus();
	    return false;
  }
  if( (form1.TC_HDL_LabresultsDate_year.value == "" || form1.TC_HDL_LabresultsDate_month.value == "" || form1.TC_HDL_LabresultsDate_day.value == "") && (!form1.HDLP2.value == "" && !isNaN(form1.HDLP2.value)) ) {
	  alert("Please enter date for lab work lipids.");
	    form1.TC_HDL_LabresultsDate_year.focus();
	    return false;
  }
  if( (form1.TC_HDL_LabresultsDate_year.value == "" || form1.TC_HDL_LabresultsDate_month.value == "" || form1.TC_HDL_LabresultsDate_day.value == "") && (!form1.TriglyceridesP1.value == "" && !isNaN(form1.TriglyceridesP1.value)) ) {
	  alert("Please enter date for lab work lipids.");
	    form1.TC_HDL_LabresultsDate_year.focus();
	    return false;
  }
  if( (form1.TC_HDL_LabresultsDate_year.value == "" || form1.TC_HDL_LabresultsDate_month.value == "" || form1.TC_HDL_LabresultsDate_day.value == "") && (!form1.TriglyceridesP2.value == "" && !isNaN(form1.TriglyceridesP2.value)) ) {
	  alert("Please enter date for lab work lipids.");
	    form1.TC_HDL_LabresultsDate_year.focus();
	    return false;
  }

  if( (form1.A1C_LabresultsDate_year.value == "" || form1.A1C_LabresultsDate_month.value == "" || form1.A1C_LabresultsDate_day.value == "") && (! form1.A1CP1.value == "" ||  !form1.A1CP2.value == "" || !form1.FBSP1.value == "" || !form1.FBSP2.value == "") ) {
	  alert("Please enter date for AIC and FBS.");
	    form1.A1C_LabresultsDate_year.focus();
	    return false;
  } 
  if( (form1.egfrYear.value == "" || form1.egfrMonth.value == "" || form1.egfrDay.value == "") && (!form1.egfr.value == "" || !form1.acrP1.value == "" || !form1.acrP2.value == "") ) 
  {
	  alert("Please enter date for eGFR and ACR.");
	    form1.egfrYear.focus();
	    return false;
  } 
  if (!form1.nextVisitInMonths.value == "" && (form1.nextVisitInMonths.value < 0 || form1.nextVisitInMonths.value > 12 || isNaN(form1.nextVisitInMonths.value)))
  {
    alert("Please check that next visit in months is between 0 and 12");
    form1.nextVisitInMonths.focus();
    return false;
  }
  
  if (!form1.nextVisitInWeeks.value == "" && (form1.nextVisitInWeeks.value < 1 || form1.nextVisitInWeeks.value > 52 || isNaN(form1.nextVisitInWeeks.value)))
  {
    alert("Please check that next visit in weeks is between 1 and 52");
    form1.nextVisitInWeeks.focus();
    return false;
  }
//  if (!form1.exercise_minPerWk.value == ""
//          && (form1.exercise_minPerWk.value < 0 || form1.exercise_minPerWk.value > 999 || isNaN(parseFloat(form1.exercise_minPerWk.value))))
//  {
//    alert("Please check that exercise per week is between 0 and 999");
//    form1.exercise_minPerWk.focus();
//    return false;
//  }
//  if (!form1.smoking_cigsPerDay.value == ""
//          && (form1.smoking_cigsPerDay.value < 0 || form1.smoking_cigsPerDay.value > 99 || isNaN(parseFloat(form1.smoking_cigsPerDay.value))))
//  {
//    alert("Please check that smoking per day is between 0 and 99");
//    form1.smoking_cigsPerDay.focus();
//    return false;
//  }
//  if (!form1.alcohol_drinksPerWk.value == ""
//          && (form1.alcohol_drinksPerWk.value < 0 || form1.alcohol_drinksPerWk.value > 99 || isNaN(parseFloat(form1.alcohol_drinksPerWk.value))))
//  {
//    alert("Please check that alcohol per week is between 0 and 99");
//    form1.alcohol_drinksPerWk.focus();
//    return false;
//  }
  
  if (!form1.assessActivity.value == ""
    && (form1.assessActivity.value < 0 || form1.assessActivity.value > 999 || isNaN(form1.assessActivity.value)))
  {
    alert("Please check that Physical activity per week is between 0 and 999");
    form1.assessActivity.focus();
    return false;
  }
  if (!form1.assessSmoking.value == ""
      && (form1.assessSmoking.value < 0 || form1.assessSmoking.value > 99 || isNaN(form1.assessSmoking.value)))
  {
    alert("Please check that smoking per day is between 0 and 99");
    form1.assessSmoking.focus();
    return false;
  }
  if (!form1.assessAlcohol.value == ""
      && (form1.assessAlcohol.value < 0 || form1.assessAlcohol.value > 99 || isNaN(form1.assessAlcohol.value)))
  {
    alert("Please check that alcohol per week is between 0 and 99");
    form1.assessAlcohol.focus();
    return false;
  }

  if (!form1.Often_miss.value == ""
          && (form1.Often_miss.value < 0 || form1.Often_miss.value > 42 || isNaN(parseFloat(form1.Often_miss.value))))
  {
    alert("Please check that meds missed per week is between 0 and 42");
    form1.Often_miss.focus();
    return false;
  }
  
  if( form1.statusInHmp.value == "" )
  {
    alert("Please select Status in HMP.");
    form1.statusInHmp.focus();
    return false;
  }    
  if ( form1.dateOfHmpStatus_year.value == "" || !/^\d\d\d\d$/.test(form1.dateOfHmpStatus_year.value) )
  {
    alert("Please enter Year of HMP Status");
    form1.dateOfHmpStatus_year.focus();
    return false;
  }
  if ( form1.dateOfHmpStatus_month.value == "" )
  {
    alert("Please select Month of HMP Status");
    form1.dateOfHmpStatus_month.focus();
    return false;
  }
  if ( form1.dateOfHmpStatus_day.value == "" )
  {
    alert("Please select Day of HMP Status");
    form1.dateOfHmpStatus_day.focus();
    return false;
  }
  return true;
}

function checkdate(date)
{
  var datevalue;
  switch (date)
  {
  case "Jan-07":
    datevalue = 0;
    break;
  case "Feb-07":
    datevalue = 0.33;
    break;
  case "Mar-07":
    datevalue = 0.66;
    break;
  case "Apr-07":
    datevalue = 1;
    break;
  case "May-07":
    datevalue = 1.33;
    break;
  case "Jun-07":
    datevalue = 1.66;
    break;
  case "Jul-07":
    datevalue = 2;
    break;
  case "Aug-07":
    datevalue = 2.33;
    break;
  case "Sep-07":
    datevalue = 2.66;
    break;
  case "Oct-07":
    datevalue = 3;
    break;
  case "Nov-07":
    datevalue = 3.33;
    break;
  case "Dec-07":
    datevalue = 3.66;
    break;
  case "Jan-08":
    datevalue = 4;
    break;
  case "Feb-08":
    datevalue = 4.33;
    break;
  case "Mar-08":
    datevalue = 4.66;
    break;
  case "Apr-08":
    datevalue = 5;
    break;
  case "May-08":
    datevalue = 5.33;
    break;
  case "Jun-08":
    datevalue = 5.66;
    break;
  case "Jul-08":
    datevalue = 6;
    break;
  case "Aug-08":
    datevalue = 6.33;
    break;
  case "Sep-08":
    datevalue = 6.66;
    break;
  case "Oct-08":
    datevalue = 7;
    break;
  case "Nov-08":
    datevalue = 7.33;
    break;
  case "Dec-08":
    datevalue = 7.66;
    break;
  case "Jan-08":
    datevalue = 8;
    break;
  case "Feb-08":
    datevalue = 8.33;
    break;
  case "Mar-08":
    datevalue = 8.66;
    break;
  case "Apr-08":
    datevalue = 9;
    break;
  case "May-08":
    datevalue = 9.33;
    break;
  case "Jun-08":
    datevalue = 9.66;
    break;
  case "Jul-08":
    datevalue = 10;
    break;
  }
  return datevalue;
}

function popupXmlMsg(path)
{
  window.open(path, "xmlMsg", "location=1,status=1,resizable=1,scrollbars=1,width=800,height=300");
}

function flipview()
{
  var regtable = document.getElementById('registartiontable');
  var formtable = document.getElementById('graphtable');
  //alert(formtable.style.display);
  //alert(regtable.style.display);

  if (regtable.style.display == "none")
  {
    //alert("regtable");
    regtable.style.display = "";
    formtable.style.display = "none";
    return false;
  }

  if (formtable.style.display == "none")
  {
    //alert("formtable");
    formtable.style.display = "";
    regtable.style.display = "none";
    return false;
  }
}
