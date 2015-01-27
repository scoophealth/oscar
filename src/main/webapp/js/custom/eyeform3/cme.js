
   jQuery(document).ready(function(){

	   //need to test this on IE. Would like to make this configurable somewhere in prefs.
	  // window.resizeTo(screen.width,screen.height);

	   //header
	   jQuery.ajax({url:ctx+"/eyeform/encounterHeader.jsp?appointmentNo="+appointmentNo,dataType: "html",success: function(data) {
		   jQuery("#encounterHeader").html(data);
	   }});

	   //CPP
	   issueNoteUrls = {
			   divR1I1:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=CurrentHistory&title=" + currentHistoryLabel + "&cmd=divR1I1"+ "&appointment_no="+appointmentNo ,
			   divR1I2:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=DiagnosticNotes&title=" + diagnosticNotesLabel + "&cmd=divR1I2" + "&appointment_no="+appointmentNo,
			   divR2I1:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=PastOcularHistory&title=" + pastOcularHistoryLabel + "&cmd=divR2I1" + "&appointment_no="+appointmentNo,
			   divR2I2:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=MedHistory&title=" + medHistoryLabel + "&cmd=divR2I2"+ "&appointment_no="+appointmentNo
       };
	   jQuery("#divR1").css('height','150px');
	   jQuery("#divR2").css('height','150px');

     	//link save button
		jQuery("#save_measurements").live('click',function(e){
			
			e.preventDefault();
			touchColor();

			//save all measurements
			var postData = "";
			jQuery("input[measurement]").each(function() {
				var className = jQuery(this).attr("class");


				if(className == 'examfieldwhite') {
					if(postData.length > 0) {
						postData += "&";
					}
					var name = jQuery(this).attr("measurement");
					var value = jQuery(this).val();
					var data = name + "=" + encodeURIComponent(value);
					postData += data;
				}
			});
			jQuery("textarea[measurement]").each(function() {
				var className = jQuery(this).attr("class");
				if(className == 'examfieldwhite') {
					if(postData.length > 0) {
						postData += "&";
					}
					var name = jQuery(this).attr("measurement");
					var value = jQuery(this).val();
					var data = name + "=" + encodeURIComponent(value);
					postData += data;
				}
			});
			jQuery.ajax({type:'POST',url:ctx+'/oscarEncounter/MeasurementData.do?action=saveValues&demographicNo='+demographicNo+'&appointmentNo='+appointmentNo,data:postData,success: function(){

				//make a call to update the existing values
				var types='';
				jQuery("input[measurement]").each(function() {
					if(types.length > 0) {
						types += ',';
					}
					types += jQuery(this).attr("measurement");
				});
				jQuery("textarea[measurement]").each(function() {
					if(types.length > 0) {
						types += ',';
					}
					types += jQuery(this).attr("measurement");
				});

				jQuery.ajax({dataType: "script", url:ctx+"/oscarEncounter/MeasurementData.do?demographicNo="+demographicNo+"&types="+types+"&action=getLatestValues&appointmentNo="+appointmentNo,async:false});

				jQuery(".slideblock").each(function() {
					var id = jQuery(this).attr('id');
					var expand=false;
					var setHeader=false;

					jQuery("#" + id + " [measurement]").each(function() {
						if(jQuery(this).val().trim().length>0) {
							setHeader=true;
						}
						if(jQuery(this).attr('prev_appt') == 'true' ) {
							expand=true;
						}
					});
					if(expand==true) {
						togglediv(document.getElementById(id));
					}
					if(setHeader==true) {
						var num = id.substring(2,id.length);
						document.getElementById('a_'+num).style.color='blue';
					}
				});

			}});


		});


       jQuery("#cppBoxes").append("<div id=\"measurements_div\" style=\"width:100%\"></div>");


       jQuery.ajax({url:ctx+"/eyeform/exam.jsp?demographic_no="+demographicNo+"&appointment_no="+appointmentNo,dataType: "html",success: function(data) {
			jQuery("#measurements_div").append(data);
			//create comma separated list of the measurement types (from attribute)
			var types='';
			jQuery("input[measurement]").each(function() {
				if(types.length > 0) {
					types += ',';
				}
				types += jQuery(this).attr("measurement");
			});
			jQuery("textarea[measurement]").each(function() {
				if(types.length > 0) {
					types += ',';
				}
				types += jQuery(this).attr("measurement");
			});

			//make a call to update the existing values
			jQuery.ajax({dataType: "script", url:ctx+"/oscarEncounter/MeasurementData.do?demographicNo="+demographicNo+"&types="+types+"&action=getLatestValues&appointmentNo="+appointmentNo,async:false});

			//expand the sections where there's a while field
			jQuery(".slideblock").each(function() {
				var id = jQuery(this).attr('id');
				var expand=false;
				var setHeader=false;

				jQuery("#" + id + " [measurement]").each(function() {
					if(jQuery(this).val().trim().length>0) {
						setHeader=true;
					}
					if(jQuery(this).attr('prev_appt') == 'true' || jQuery(this).attr('current_appt') == 'true') {
						expand=true;
					}
				});
				if(expand==true) {
					togglediv(document.getElementById(id));
				}
				if(setHeader==true) {
					var num = id.substring(2,id.length);
					document.getElementById('a_'+num).style.color='blue';
				}
			});



       }});


       init();

       //left nav bar
       //removeNavDiv('Dx');
       removeNavDiv('preventions');
       removeNavDiv('HRM');
       removeNavDiv('myoscar');
       reorderNavBarElements('msgs','tickler');
       

       var patientLogLabel = "oscarEncounter.eyeform.patientLog.title";
       addLeftNavDiv("patientLog");
       popColumn(ctx + "/CaseManagementView.do?hc=006666&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=PatientLog&title="+patientLogLabel+"&cmd=patientLog&appointment_no="+appointmentNo +"&noheight=true","patientLog","patientLog", "leftNavBar", this);
       reorderNavBarElements('msgs','patientLog');
       reorderNavBarElements('tickler','patientLog');
       
       removeNavDiv('docs');
       addLeftNavDiv("docs");
       popColumn(ctx + "/oscarEncounter/displayDocuments.do?hC=476BB3&appointment_no="+appointmentNo+"&omit=photo&displayAllItems=true&displayAllProp=eyeform_expandAllDocuments","docs","docs", "leftNavBar", this);
       reorderNavBarElements('labs','docs');
       reorderNavBarElements('measurements','docs');
     
       addLeftNavDiv("diagrams");
       popColumn(ctx + "/oscarEncounter/displayDiagrams.do?hC=11CC00&appointment_no="+appointmentNo,"diagrams","diagrams", "leftNavBar", this);
       reorderNavBarElements('measurements','diagrams');
       reorderNavBarElements('labs','diagrams');
       
       addLeftNavDiv("photos");
       popColumn(ctx + "/oscarEncounter/displayPhotos.do?hC=476BB3&appointment_no="+appointmentNo,"photos","photos", "leftNavBar", this);
       reorderNavBarElements('measurements','photos');
       reorderNavBarElements('labs','photos');
     
       addLeftNavDiv("appointmentHistory");
       popColumn(ctx + "/oscarEncounter/displayAppointmentHistory.do?hC=009999&cmd=appointmentHistory","appointmentHistory","appointmentHistory", "leftNavBar", this);
	
	   addLeftNavDiv("examhistory");
       popColumn(ctx + "/oscarEncounter/displayExaminationHistory.do?hC=009999&appointment_no="+appointmentNo,"examhistory","examhistory", "leftNavBar", this);
     
       addLeftNavDiv("Billing");
       popColumn(ctx + "/oscarEncounter/displayBilling.do?hC=009999&cmd=Billing&appointment_no="+appointmentNo,"Billing","Billing", "leftNavBar", this);
      
       addLeftNavDiv("macro");
       popColumn(ctx + "/oscarEncounter/displayMacro.do?hC=009999&appointment_no="+appointmentNo,"macro","macro", "leftNavBar", this);

       //right nav bar
       removeNavDiv('unresolvedIssues');
       removeNavDiv('resolvedIssues');
       removeNavDiv('Guidelines');
       removeNavDiv('RiskFactors');
       removeNavDiv('Rx');
       removeNavDiv('OMeds');
	   removeNavDiv('episode');
	   
//	   addRightNavDiv("specshistory");
//       popColumn(ctx + "/oscarEncounter/displaySpecsHistory.do?hC=009999&appointment_no="+appointmentNo,"specshistory","specshistory", "rightNavBar", this);
      
       reorderNavBarElements('allergies','FamHistory');
//       reorderNavBarElements('allergies','specshistory'); 
       
       
	   var ocularMedsLabel = "oscarEncounter.NavBar.OcularMeds";
       addRightNavDiv("OcularMedication");
       popColumn(ctx + "/CaseManagementView.do?hc=006666&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=OcularMedication&title="+ocularMedsLabel+"&cmd=OcularMedication&appointment_no="+appointmentNo +"&noheight=true","OcularMedication","OcularMedication", "rightNavBar", this);

	   addRightNavDiv("OMeds");
       popColumn(ctx + "/CaseManagementView.do?hc=006666&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=OMeds&title=" + oMedsLabel + "&cmd=OMeds" + "&appointment_no="+appointmentNo,"OMeds","OMeds","rightNavBar",this);
       
	   addRightNavDiv("Rx");
       popColumn(ctx + "/oscarEncounter/displayRx.do?hC=996600&numToDisplay=12","Rx","Rx","rightNavBar",this);
       
       addRightNavDiv("ocularprocedure");
       popColumn(ctx + "/oscarEncounter/displayOcularProcedure.do?hC=009999&appointment_no="+appointmentNo,"ocularprocedure","ocularprocedure", "rightNavBar", this);
        
       removeNavDiv('consultation');
       addRightNavDiv('consultation');
       popColumn(ctx + "/oscarEncounter/displayConsultation.do?hC=6C2DC7&appointment_no="+appointmentNo,"consultation","consultation", "rightNavBar", this);
	
       addRightNavDiv("conReport");
       popColumn(ctx + "/oscarEncounter/displayConReport.do?hC=009999&cmd=conReport&appointment_no="+appointmentNo,"conReport","conReport", "rightNavBar", this);
        
       removeNavDiv('forms');
       addRightNavDiv('forms');
       popColumn(ctx + "/oscarEncounter/displayForms.do?hC=917611&appointment_no="+appointmentNo,"forms","forms", "rightNavBar", this);
       
       removeNavDiv('eforms');
       addRightNavDiv("eforms");
       popColumn(ctx + "/oscarEncounter/displayEForms.do?hC=11CC00&appointment_no="+appointmentNo+"&omit=Ant+Segment,Retina,Eye+Saggital,Eye+Lid%2FNLD","eforms","eforms", "rightNavBar", this);

	   reorderNavBarElements('forms','eforms');
    
	   notifyIssueUpdate();

     });



   function runMacro(macroId,appointmentNo) {
	   jQuery.ajax({ url: ctx+"/CaseManagementView.do?method=run_macro&id="+macroId+"&appointmentNo="+appointmentNo +"&noteId="+savedNoteId + "&demographicNo="+demographicNo, dataType:'script', async:false});
	   jQuery.ajax({url:ctx+"/eyeform/NoteData.do?method=getCurrentNoteData&demographicNo="+demographicNo+"&noteId="+savedNoteId+"&appointmentNo="+appointmentNo,dataType: "html",async:false, success: function(data) {
			jQuery("#current_note_addon").html(data);
      }});
	   jQuery.ajax({ url: ctx+"/CaseManagementView.do?method=run_macro_script&id="+macroId+"&appointmentNo="+appointmentNo +"&noteId="+savedNoteId + "&demographicNo="+demographicNo, dataType:'script', async:false});

   }

   function runMacro2(macroId,macroName, appointmentNo,cpp) {
	   var c = confirm('Are you sure to execute macro [ '+ macroName+' ] and sign this form?');
	   if(c == false) {return false;}
	   //potentially need admission date.
	   document.forms['caseManagementEntryForm'].sign.value='on';
	   jQuery("form[name='caseManagementEntryForm']").append("<input type=\"hidden\" id=\"macro.id\" name=\"macro.id\" value=\""+macroId+"\"/>");
	   
	   jQuery("#save_measurements").click();
	   var result =  savePage('runMacro', '');
	   return false;
	   
   }


   function notifyIssueUpdate() {
	   if(savedNoteId == null || savedNoteId=='null') {
		   savedNoteId=0;
	   }

	   var noteAddonUrl = ctx+"/eyeform/NoteData.do?method=getCurrentNoteData&demographicNo="+demographicNo+"&noteId="+savedNoteId+"&appointmentNo="+appointmentNo;
       jQuery.ajax({url:noteAddonUrl,dataType: "html",success: function(data) {
			jQuery("#current_note_addon").html(data);
       }});
   }

   function notifyDivLoaded(divId) {
	   jQuery("#"+divId + " .topLinks").each(function() {
		  jQuery(this).css("font-size","15px");
	   });
   }

   function messagesLoaded(savedId){
	  // hideTopContentFieldsExceptTemplate();
	   
	   //alert('messagesLoaded() - savedNoteId=' + savedId); 
	   var noteAddonUrl = ctx+"/eyeform/NoteData.do?method=getCurrentNoteData&demographicNo="+demographicNo+"&noteId="+savedId+"&appointmentNo="+appointmentNo;
       jQuery.ajax({url:noteAddonUrl,dataType: "html",success: function(data) {
			jQuery("#current_note_addon").html(data);
       }});

       jQuery("#displayResolvedIssues").hide();
       jQuery("#displayUnresolvedIssues").hide();
       jQuery("#newNoteImg").hide();
       jQuery("#imgPrintEncounter").removeAttr('onclick');
       jQuery("#imgPrintEncounter").live('click',function(e){
    	   e.preventDefault();		   
		   location.href=ctx+'/eyeform/Eyeform.do?method=print&from_cme_js=true&apptNos=' + appointmentNo;
		   
       });
       jQuery("#assignIssueSection").html("<span>&nbsp;</span>");
       jQuery("#caseNote_note"+savedId).css('height','10em');
       
       jQuery("#saveImg, #signSaveImg, #signVerifyImg, #signSaveBill").bind('click',function() {      	   
       
    	   jQuery("#save_measurements").click();
    	   saveEyeformNoteNoGenerate();
    	   
       });
   }
   
   function hideTopContentFieldsExceptTemplate()
   {
	   if(jQuery("#channel") && jQuery("#channel").css("visibility")=="visible")
	   {
	       jQuery("#channel").parent("div").next("div").css("display", "none");;
	       jQuery("#channel").css("display", "none");;
	       jQuery("#keyword").css("display", "none");
	       jQuery("#searchButton").css("display", "none");    
	   }
   }