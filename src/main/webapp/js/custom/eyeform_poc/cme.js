
   jQuery(document).ready(function(){

	   //need to test this on IE. Would like to make this configurable somewhere in prefs.
	  // window.resizeTo(screen.width,screen.height);

	   //header
	   jQuery.ajax({url:ctx+"/eyeform/encounterHeader.jsp?appointmentNo="+appointmentNo,dataType: "html",success: function(data) {
		   jQuery("#encounterHeader").html(data);
	   }});

	   //CPP
	   issueNoteUrls = {};
	   jQuery("#cppBoxes").html("");
	   jQuery.ajax({url:ctx+"/eyeform/cppmeasurements.jsp?demographicNo="+demographicNo+"&appointmentNo="+appointmentNo,dataType: "html",async:false,success: function(data) {
		   jQuery("#cppBoxes").append(data);
	   }});


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


			jQuery("textarea[cpp]").each(function() {
				var name = jQuery(this).attr("cpp");
				var value = jQuery(this).val();

				var url = ctx + "/CaseManagementEntry.do?method=issueNoteSave&demographicNo="+demographicNo+"&appointmentNo="+appointmentNo+"&noteId=" + jQuery(this).attr("note_id");
				var postData = "reloadUrl="+encodeURIComponent("issue_code="+name)+"&containerDiv=&issueChange=true&archived=false&value="+encodeURIComponent(value)+"&position=0&forward=none&issue_id=" + jQuery(this).attr("issue_id");

				var textArea = jQuery(this);
				jQuery.ajax({type:'POST',url:url,data:postData,async:false, success: function(data){
					textArea.attr('note_id',data);
				}});
			});

		});


       jQuery("#cppBoxes").append("<div id=\"measurements_div\" style=\"width:100%\"></div>");


       jQuery.ajax({url:ctx+"/eyeform/exam.jsp?demographic_no="+demographicNo+"&appointment_no="+appointmentNo,dataType: "html",success: function(data) {
			jQuery("#measurements_div").append(data);

			//hide some parts of it
			jQuery("#a_22").parent().hide();
			jQuery("#s_22").parent().hide();
			jQuery("#a_24").parent().hide();
			jQuery("#s_24").parent().hide();
			jQuery("#a_25").parent().hide();
			jQuery("#s_25").parent().hide();
			jQuery("#a_26").parent().hide();
			jQuery("#s_26").parent().hide();
			jQuery("#a_27").parent().hide();
			jQuery("#s_27").parent().hide();
			jQuery("#a_28").parent().hide();
			jQuery("#s_28").parent().hide();
			jQuery("#a_29").parent().hide();
			jQuery("#s_29").parent().hide();
			jQuery("#a_30").parent().hide();
			jQuery("#s_30").parent().hide();
			jQuery("#a_31").parent().hide();
			jQuery("#s_31").parent().hide();

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
			jQuery.ajax({dataType: "script", url:ctx+"/oscarEncounter/MeasurementData.do?demographicNo="+demographicNo+"&types="+types+"&action=getLatestValues&appointmentNo="+appointmentNo + "&fresh=cpp_currentHis",async:false});

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

       removeNavDiv('preventions');
       removeNavDiv('Dx');
       removeNavDiv('tickler');
       reorderNavBarElementsBefore('msgs','forms');
       removeNavDiv('HRM');
       removeNavDiv('measurements');
       removeNavDiv('labs');
       removeNavDiv('forms');

       addLeftNavDiv("conReport");
       popColumn(ctx + "/oscarEncounter/displayConReport.do?hC=009999&cmd=conReport&appointment_no="+appointmentNo,"conReport","conReport", "leftNavBar", this);

       addLeftNavDiv("Billing");
       popColumn(ctx + "/oscarEncounter/displayBilling.do?hC=009999&cmd=Billing&appointment_no="+appointmentNo,"Billing","Billing", "leftNavBar", this);

       addLeftNavDiv("specshistory");
       popColumn(ctx + "/oscarEncounter/displaySpecsHistory.do?hC=009999&appointment_no="+appointmentNo,"specshistory","specshistory", "leftNavBar", this);

       reorderNavBarElements('Billing','msgs');
       reorderNavBarElements('docs','specshistory');
       reorderNavBarElements('eforms','conReport');


       //right nav bar
       removeNavDiv('unresolvedIssues');
       removeNavDiv('resolvedIssues');
       removeNavDiv('Guidelines');
       removeNavDiv('RiskFactors');
       removeNavDiv('Rx');
       removeNavDiv('OMeds');
       removeNavDiv('allergies');
       removeNavDiv('FamHistory');
/*
       var ocularMedsLabel = "oscarEncounter.NavBar.OcularMeds";
       addRightNavDiv("OcularMedication");
       popColumn(ctx + "/CaseManagementView.do?hc=CCDDAA&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=OcularMedication&title="+ocularMedsLabel+"&cmd=OcularMedication&appointment_no="+appointmentNo +"&noheight=true","OcularMedication","OcularMedication", "rightNavBar", this);
*/
       addRightNavDiv("macro");
       popColumn(ctx + "/oscarEncounter/displayMacro.do?hC=009999&appointment_no="+appointmentNo,"macro","macro", "rightNavBar", this);

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
	   var c = confirm('Are you sure to execute macro ['+macroName+'] and sign this form?');
	   if(c == false) {return false;}
	   //potentially need admission date.
	   document.forms['caseManagementEntryForm'].sign.value='on';
	   jQuery("form[name='caseManagementEntryForm']").append("<input type=\"hidden\" name=\"macro.id\" value=\""+macroId+"\"/>");
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
	   var noteAddonUrl = ctx+"/eyeform/NoteData.do?method=getCurrentNoteData&demographicNo="+demographicNo+"&noteId="+savedId+"&appointmentNo="+appointmentNo;
       jQuery.ajax({url:noteAddonUrl,dataType: "html",success: function(data) {
			jQuery("#current_note_addon").html(data);
       }});


       jQuery("#newNoteImg").hide();
       jQuery("#imgPrintEncounter").removeAttr('onclick');
       jQuery("#imgPrintEncounter").live('click',function(e){
    	   e.preventDefault();
    	   location.href=ctx+'/eyeform/Eyeform.do?method=print&apptNos=' + appointmentNo;
       });
       jQuery("#assignIssueSection").html("<span>&nbsp;</span>");
       jQuery("#caseNote_note"+savedId).css('height','10em');
       jQuery("#saveImg, #signSaveImg, #signVerifyImg").bind('click',function() {
    	   jQuery("#save_measurements").click();
    	   saveEyeformNoteNoGenerate();
       });

       jQuery("#topContent").hide();

   }