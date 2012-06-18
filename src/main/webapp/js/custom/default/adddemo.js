jQuery(document).ready(function(){

  jQuery("#addDemographicTbl #renewDateLbl").css("display","none");
  jQuery("#addDemographicTbl #renewDate").css("display","none");
  jQuery("#addDemographicTbl #countryLbl").insertAfter(jQuery("#addDemographicTbl #hcType"));
  jQuery("#addDemographicTbl #countryCell").insertAfter(jQuery("#addDemographicTbl #countryLbl"));  
    
  jQuery("#last_name").focus();
  jQuery("#last_name").select();
  window.resizeTo(1100,700);
});
