 
   jQuery(document).ready(function(){
	   issueNoteUrls = {
			   divR1I1:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=SocHistory&title=" + socHistoryLabel + "&cmd=divR1I1",
               divR1I2:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=MedHistory&title=" + medHistoryLabel + "&cmd=divR1I2",
               divR2I1:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=Concerns&title=" + onGoingLabel + "&cmd=divR2I1",
               divR2I2:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=Reminders&title=" + remindersLabel + "&cmd=divR2I2"                                         
       };
 
       
	   init();	   
     });


   function notifyIssueUpdate() {
	   
   }

   function notifyDivLoaded(divId) {	  
   }
 