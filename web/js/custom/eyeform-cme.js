 
   jQuery(document).ready(function(){
	   
	   issueNoteUrls = {
			   divR1I1:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=CurrentHistory&title=" + currentHistoryLabel + "&cmd=divR1I1" ,
               divR1I2:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=MedHistory&title=" + medHistoryLabel + "&cmd=divR1I2" ,
               divR2I1:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=DiagnosticNotes&title=" + diagnosticNotesLabel + "&cmd=divR2I1" + "&appointment_no="+appointmentNo,
               divR2I2:    ctx + "/CaseManagementView.do?hc=996633&method=listNotes&providerNo=" + providerNo + "&demographicNo=" + demographicNo + "&issue_code=PastOcularHistory&title=" + pastOcularHistoryLabel + "&cmd=divR2I2" + "&appointment_no="+appointmentNo,              
       };
	   
       //add diagnostic notes, past ocular history, patient log, ocular medications
       //add a row       
      
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
					var data = name + "=" + value;
					postData += data;
				}
			});

			jQuery.ajax({type:'POST',url:ctx+'/oscarEncounter/MeasurementData.do?action=saveValues&demographicNo='+demographicNo,data:postData,success: function(){
				alert('Saved.');
			}});
		});
		
		
       jQuery("#cppBoxes").append("<div id=\"measurements_div\"></div>");

     
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

			//make a call to update the existing values
			jQuery.ajax({dataType: "script", url:ctx+"/oscarEncounter/MeasurementData.do?demographicNo="+demographicNo+"&types="+types+"&action=getLatestValues"});
						
       }});


       init();
                         
       jQuery.ajax({url:ctx+"/eyeform/NoteData.do?method=getCurrentNoteData&demographicNo="+demographicNo+"&noteId="+savedNoteId+"&appointmentNo="+appointmentNo,dataType: "html",success: function(data) {
			jQuery("#current_note_addon").append(data);
       }});
       	   	  
       
        
       
	   addLeftNavDiv("ocularprocedure");	                         
       popColumn(ctx + "/oscarEncounter/displayOcularProcedure.do?hC=009999&appointment_no="+appointmentNo,"ocularprocedure","ocularProcedure", "leftNavBar", this);

       addLeftNavDiv("specshistory");	                         
       popColumn(ctx + "/oscarEncounter/displaySpecsHistory.do?hC=009999&appointment_no="+appointmentNo,"specshistory","specsHistory", "leftNavBar", this);
       
       addLeftNavDiv("macro");	      
       popColumn(ctx + "/oscarEncounter/displayMacro.do?hC=009999&appointment_no="+appointmentNo,"macro","macro", "leftNavBar", this);
       
     });

 