jQuery(document).ready(function(){
  jQuery(jQuery("<tr id='row1'>").append(jQuery("#addDemographicTbl #chartNoLbl")).append(jQuery("#addDemographicTbl #chartNo")).append(jQuery("#addDemographicTbl #hinLbl")).append(jQuery("#addDemographicTbl #hinVer"))).insertBefore(jQuery("#addDemographicTbl tr:first"));
  jQuery("#addDemographicTbl #hcTypeLbl").insertAfter(jQuery("#addDemographicTbl #lastName"));
  jQuery("#addDemographicTbl #hcType").insertAfter(jQuery("#addDemographicTbl #hcTypeLbl"));

  jQuery(jQuery("<tr id='rowWithFirstName'>").append(jQuery("#addDemographicTbl #firstNameLbl")).append(jQuery("#addDemographicTbl #firstName"))).insertAfter(jQuery("#addDemographicTbl #rowWithLastName"));
  jQuery("#addDemographicTbl #rosterStatusLbl").insertAfter(jQuery("#addDemographicTbl #firstName"));
  jQuery("#addDemographicTbl #rosterStatus").insertAfter(jQuery("#addDemographicTbl #rosterStatusLbl"));
  
  jQuery(jQuery("<tr id='rowWithGender'>").append(jQuery("#addDemographicTbl #genderLbl")).append(jQuery("#addDemographicTbl #gender"))).insertAfter(jQuery("#addDemographicTbl #rowWithFirstName"));  
  jQuery("#addDemographicTbl #effDateLbl").insertAfter(jQuery("#addDemographicTbl #gender"));
  jQuery("#addDemographicTbl #effDate").insertAfter(jQuery("#addDemographicTbl #effDateLbl"));
  
  jQuery(jQuery("<tr id='rowWithDob'>").append(jQuery("#addDemographicTbl #dobLbl")).append(jQuery("#addDemographicTbl #dobTbl"))).insertAfter(jQuery("#addDemographicTbl #rowWithGender"));
  jQuery("#addDemographicTbl #renewDateLbl").insertAfter(jQuery("#addDemographicTbl #dobTbl"));
  jQuery("#addDemographicTbl #renewDate").insertAfter(jQuery("#addDemographicTbl #renewDateLbl"));

  jQuery("#addDemographicTbl #dobLbl").css("padding-bottom","1em");
  jQuery("#addDemographicTbl #dobTbl").css("padding-bottom","1em");

  jQuery(jQuery("<tr id='rowWithAddress'>").append(jQuery("#addDemographicTbl #addrLbl")).append(jQuery("#addDemographicTbl #addressCell"))).insertAfter(jQuery("#addDemographicTbl #rowWithDob"));
  jQuery(jQuery("<tr id='rowWithCity'>").append(jQuery("#addDemographicTbl #cityLbl")).append(jQuery("#addDemographicTbl #cityCell"))).insertAfter(jQuery("#addDemographicTbl #rowWithAddress"));
  jQuery(jQuery("<tr id='rowWithProvince'>").append(jQuery("#addDemographicTbl #provLbl")).append(jQuery("#addDemographicTbl #provCell"))).insertAfter(jQuery("#addDemographicTbl #rowWithCity"));
  jQuery(jQuery("<tr id='rowWithPostal'>").append(jQuery("#addDemographicTbl #postalLbl")).append(jQuery("#addDemographicTbl #postalCell"))).insertAfter(jQuery("#addDemographicTbl #rowWithProvince"));

  jQuery("#addDemographicTbl #postalLbl").css("padding-bottom","1em");
  jQuery("#addDemographicTbl #postalCell").css("padding-bottom","1em");

  jQuery(jQuery("<tr id='rowWithEmail'>").append(jQuery("#addDemographicTbl #emailLbl")).append(jQuery("#addDemographicTbl #emailCell"))).insertAfter(jQuery("#addDemographicTbl #rowWithPostal"));

  jQuery(jQuery("<tr id='rowWithPhone'>").append(jQuery("#addDemographicTbl #phoneLbl")).append(jQuery("#addDemographicTbl #phoneCell"))).insertAfter(jQuery("#addDemographicTbl #rowWithEmail"));
  jQuery("#addDemographicTbl #ptStatusLbl").insertAfter(jQuery("#addDemographicTbl #phoneCell"));
  jQuery("#addDemographicTbl #ptStatusCell").insertAfter(jQuery("#addDemographicTbl #ptStatusLbl"));

  jQuery("#addDemographicTbl #phoneLbl").css("padding-bottom","1em");
  jQuery("#addDemographicTbl #phoneCell").css("padding-bottom","1em");

  jQuery("#addDemographicTbl #ptStatusLbl").css("padding-bottom","1em");
  jQuery("#addDemographicTbl #ptStatusCell").css("padding-bottom","1em");

  jQuery(jQuery("<tr id='rowWithAlert'>").append(jQuery("#addDemographicTbl #alertLbl")).append(jQuery("#addDemographicTbl #alertCell"))).insertAfter(jQuery("#addDemographicTbl #rowWithPhone"));
  jQuery(jQuery("<tr id='rowWithNotes'>").append(jQuery("#addDemographicTbl #notesLbl")).append(jQuery("#addDemographicTbl #notesCell"))).insertAfter(jQuery("#addDemographicTbl #rowWithAlert"));

  jQuery("#addDemographicTbl #alertCell").attr("colspan", "3");
  jQuery("#addDemographicTbl #notesCell").attr("colspan", "3");

  jQuery(jQuery("<tr id='rowWithWaitingList'>").append(jQuery("#addDemographicTbl #waitListTbl"))).insertAfter(jQuery("#addDemographicTbl #rowWithNotes"));

  jQuery("#addDemographicTbl #notesLbl").css("padding-bottom","1em");
  jQuery("#addDemographicTbl #notesCell").css("padding-bottom","1em");

  jQuery("#addDemographicTbl #languageLbl").css("display","none");
  jQuery("#addDemographicTbl #languageCell").css("display","none");

  jQuery("#addDemographicTbl #titleLbl").css("display","none");
  jQuery("#addDemographicTbl #titleCell").css("display","none");

  jQuery("#addDemographicTbl #spokenLbl").css("display","none");
  jQuery("#addDemographicTbl #spokenCell").css("display","none");

  jQuery("#addDemographicTbl #phoneWorkLbl").css("display","none");
  jQuery("#addDemographicTbl #phoneWorkCell").css("display","none");

  jQuery("#addDemographicTbl #phoneCellLbl").css("display","none");
  jQuery("#addDemographicTbl #phoneCellCell").css("display","none");

  jQuery("#addDemographicTbl #newsletterLbl").css("display","none");
  jQuery("#addDemographicTbl #newsletterCell").css("display","none");

  jQuery("#addDemographicTbl #myOscarLbl").css("display","none");
  jQuery("#addDemographicTbl #myOscarCell").css("display","none");

  jQuery("#addDemographicTbl #countryLbl").css("display","none");
  jQuery("#addDemographicTbl #countryCell").css("display","none");

  jQuery("#addDemographicTbl #sinNoLbl").css("display","none");
  jQuery("#addDemographicTbl #sinNoCell").css("display","none");

  jQuery("#addDemographicTbl #cytologyLbl").css("display","none");
  jQuery("#addDemographicTbl #cytologyCell").css("display","none");

  jQuery("#addDemographicTbl #demoDoctorLbl").css("display","none");
  jQuery("#addDemographicTbl #demoDoctorCell").css("display","none");

  jQuery("#addDemographicTbl #nurseLbl").css("display","none");
  jQuery("#addDemographicTbl #nurseCell").css("display","none");

  jQuery("#addDemographicTbl #midwifeLbl").css("display","none");
  jQuery("#addDemographicTbl #midwifeCell").css("display","none");

  jQuery("#addDemographicTbl #residentLbl").css("display","none");
  jQuery("#addDemographicTbl #residentCell").css("display","none");

  jQuery("#addDemographicTbl #referralDocLbl").css("display","none");
  jQuery("#addDemographicTbl #referralDocCell").css("display","none");

  jQuery("#addDemographicTbl #referralDocNoLbl").css("display","none");
  jQuery("#addDemographicTbl #referralDocNoCell").css("display","none");

  jQuery("#addDemographicTbl #rosterDateLbl").css("display","none");
  jQuery("#addDemographicTbl #rosterDateCell").css("display","none");

  jQuery("#addDemographicTbl #joinDateLbl").css("display","none");
  jQuery("#addDemographicTbl #joinDateCell").css("display","none");

  jQuery("#addDemographicTbl #endDateLbl").css("display","none");
  jQuery("#addDemographicTbl #endDateCell").css("display","none");

  jQuery("#addDemographicTbl #chart_no").attr("tabindex", "1");
  jQuery("#addDemographicTbl #last_name").attr("tabindex", "2");
  jQuery("#addDemographicTbl #first_name").attr("tabindex", "3");
  jQuery("#addDemographicTbl #sex").attr("tabindex", "4");
  jQuery("#addDemographicTbl #year_of_birth").attr("tabindex", "5");
  jQuery("#addDemographicTbl #month_of_birth").attr("tabindex", "6");
  jQuery("#addDemographicTbl #date_of_birth").attr("tabindex", "7");
  jQuery("#addDemographicTbl #address").attr("tabindex", "8");
  jQuery("#addDemographicTbl #city").attr("tabindex", "9");
  jQuery("#addDemographicTbl #province").attr("tabindex", "10");  
  jQuery("#addDemographicTbl #postal").attr("tabindex", "11");
  jQuery("#addDemographicTbl #email").attr("tabindex", "12");
  jQuery("#addDemographicTbl #phone").attr("tabindex", "13");
  jQuery("#addDemographicTbl #hPhoneExt").attr("tabindex", "14");
  jQuery("#addDemographicTbl #hin").attr("tabindex", "15");
  jQuery("#addDemographicTbl #ver").attr("tabindex", "16");
  jQuery("#addDemographicTbl #hc_type").attr("tabindex", "17");
  jQuery("#addDemographicTbl #roster_status").attr("tabindex", "18");
  jQuery("#addDemographicTbl #eff_date_year").attr("tabindex", "19");
  jQuery("#addDemographicTbl #eff_date_month").attr("tabindex", "20");
  jQuery("#addDemographicTbl #eff_date_date").attr("tabindex", "21");
  jQuery("#addDemographicTbl #hc_renew_date_year").attr("tabindex", "22");
  jQuery("#addDemographicTbl #hc_renew_date_month").attr("tabindex", "23");
  jQuery("#addDemographicTbl #hc_renew_date_date").attr("tabindex", "24");
  jQuery("#addDemographicTbl #patient_status").attr("tabindex", "25");
  jQuery("#addDemographicTbl #cust3").attr("tabindex", "26");
  jQuery("#addDemographicTbl #content").attr("tabindex", "27");
  jQuery("#addDemographicTbl #name_list_id").attr("tabindex", "28");
  jQuery("#addDemographicTbl #waiting_list_note").attr("tabindex", "29");
  jQuery("#addDemographicTbl #waiting_list_referral_date").attr("tabindex", "30");
  jQuery("#addDemographicTbl #btnAddRecord").attr("tabindex", "31");
  jQuery("#addDemographicTbl #btnSwipeCard").attr("tabindex", "32");
  jQuery("#addDemographicTbl #btnCancel").attr("tabindex", "33");

  //Set Default Family Doctor change XXXXXX to provider number of default doctor
  jQuery("#addDemographicTbl #docXXXXXX").attr("selected", "selected");
 
  //Set default city (in this case, London)
  jQuery("#addDemographicTbl #city").val("London");

  //dont prefix phone number by default
  jQuery("#addDemographicTbl #phone").val("");

  jQuery("form#adddemographic").submit(function(){

    if (!jQuery("#addDemographicTbl #chart_no").val()) { 
       alert("Student Number must be completed");
       return false;
    }

    if (!jQuery("#addDemographicTbl #phone").val() && !jQuery("#addDemographicTbl #email").val()) {
       alert("Either a phone number of email address must be completed");
       return false;
    }

    if (jQuery("#addDemographicTbl #email").val() && !isValidEmailAddress(jQuery("#addDemographicTbl #email").val())) {
       alert("Invalid email address format. Please correct");
       return false;
    }

  });
  
  function isValidEmailAddress(emailAddress) {
    	var pattern = new RegExp(/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i);
    	return pattern.test(emailAddress);
  }
 

  jQuery("#chart_no").focus();
  jQuery("#chart_no").select();
  window.resizeTo(1250,800);
});
