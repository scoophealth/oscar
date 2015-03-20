oscarApp.controller('ConsultResponseCtrl', function ($scope,$http,$resource,$location,$modal,consultService,demographicService,summaryService,staticDataService,consult,user) {
	$scope.consult = consult;
	
	consult.letterheadList = toArray(consult.letterheadList);
	consult.referringDoctorList = toArray(consult.referringDoctorList);
	consult.faxList = toArray(consult.faxList);
	consult.sendToList = toArray(consult.sendToList);
	
	//set attachments
	consult.attachments = toArray(consult.attachments);
	sortAttachmentDocs(consult.attachments);
	
	//set default letterhead
	if (consult.letterheadName==null) {
		for (var i=0; i<consult.letterheadList.length; i++) {
			if (consult.letterheadList[i].id==user.providerNo) {
				consult.letterheadName = consult.letterheadList[i].id;
				consult.letterheadAddress = consult.letterheadList[i].address;
				consult.letterheadPhone = consult.letterheadList[i].phone;
				break;
			}
		}
	}
	
	//set default fax if there's only 1
	if (consult.letterheadFax==null && consult.faxList.length==1) {
		consult.letterheadFax = consult.faxList[0].faxNumber;
	}
	
	//set patient cell phone
	consult.demographic.extras = toArray(consult.demographic.extras);
	for (var i=0; i<consult.demographic.extras.length; i++) {
		if (consult.demographic.extras[i].key=="demo_cell") {
			consult.demographic.cellPhone = consult.demographic.extras[i].value;
			break;
		}
	}
	
	//set appointment time
	if (consult.appointmentTime!=null) {
		var apptTime = new Date(consult.appointmentTime);
		consult.appointmentHour = pad0(apptTime.getHours());
		consult.appointmentMinute = pad0(apptTime.getMinutes());
	}

	$scope.urgencies = staticDataService.getConsultUrgencies();
	$scope.statuses = staticDataService.getConsultResponseStatuses();
	$scope.hours = staticDataService.getHours();
	$scope.minutes = staticDataService.getMinutes();
	
	//monitor data changed
	$scope.consultChanged = -1;
	$scope.$watchCollection("consult", function(){
		$scope.consultChanged++;
	});
	
	//remind user of unsaved data
	$scope.$on("$stateChangeStart", function(event){
		if ($scope.consultChanged>0) {
			var discard = confirm("You may have unsaved data. Are you sure to leave?");
			if (!discard) event.preventDefault();
		}
	});
	
	$scope.changeLetterhead = function(){
		var index = $("#letterhead").val();
		if (index==null) return;
		
		consult.letterheadAddress = consult.letterheadList[index].address;
		consult.letterheadPhone = consult.letterheadList[index].phone;
	};
	
	$scope.changeReferringDoctor = function(){
		var index = $("#refDocId").val();
		if (index==null) {
			consult.referringDoctor = null;
			return;
		}
		consult.referringDoctor = consult.referringDoctorList[index];
	}
	
	$scope.getFamilyHistory = function(boxId){
		summaryService.getFamilyHistory(consult.demographic.demographicNo).then(function(data){ writeToBox(data, boxId); });
	}
	$scope.getMedicalHistory = function(boxId){
		summaryService.getMedicalHistory(consult.demographic.demographicNo).then(function(data){ writeToBox(data, boxId); });
	}
	$scope.getOngoingConcerns = function(boxId){
		summaryService.getOngoingConcerns(consult.demographic.demographicNo).then(function(data){ writeToBox(data, boxId); });
	}
	$scope.getOtherMeds = function(boxId){
		summaryService.getOtherMeds(consult.demographic.demographicNo).then(function(data){ writeToBox(data, boxId); });
	}
	$scope.getReminders = function(boxId){
		summaryService.getReminders(consult.demographic.demographicNo).then(function(data){ writeToBox(data, boxId); });
	}
	
	$scope.toPatientSummary = function(){
		$location.path("/record/"+consult.demographic.demographicNo+"/summary");
	}
	
	$scope.toPatientConsultResponseList = function(){
		$location.path("/consultResponses").search({"demographicId":consult.demographic.demographicNo});
	}
	
	$scope.invalidData = function(){
		if ($scope.urgencies[$("#urgency").val()]==null) {
			alert("Please select an Urgency"); return true;
		}
		if (consult.letterheadList[$("#letterhead").val()]==null) {
			alert("Please select a Letterhead"); return true;
		}
		if (consult.referringDoctor==null) {
			alert("Please select a Referring Doctor"); return true;
		}
		if (consult.demographic==null || consult.demographic=="") {
			alert("Error! Invalid patient!"); return true;
		}
		return false;
	}
	
	$scope.setAppointmentTime = function(){
		if (consult.appointmentHour!=null && consult.appointmentMinute!=null) {
			var apptTime = new Date();
			if (consult.appointmentTime!=null) apptTime = new Date(consult.appointmentTime);
			apptTime.setHours(consult.appointmentHour);
			apptTime.setMinutes(consult.appointmentMinute);
			apptTime.setSeconds(0);
			consult.appointmentTime = apptTime;
		}
	}
	
	$scope.openAttach = function(attachment){
		window.open("../"+attachment.url);
	}

	$scope.attachFiles = function(){
		var modalInstance = $modal.open({
			templateUrl: "consults/consultAttachment.jsp",
			controller: AttachmentCtrl,
			windowClass: "attachment-modal-window"
		});
		
		modalInstance.result.then(function(){
			if (consult.attachmentsChanged) {
				$scope.consultChanged++;
				consult.attachmentsChanged = false;
			}
		});
	}

	//attachment modal controller
	function AttachmentCtrl($scope, $modalInstance) {
		$scope.atth = {};
		$scope.atth.patientName = consult.demographic.lastName + ", " + consult.demographic.firstName;
		
		$scope.atth.attachedDocs = consult.attachments;
		if ($scope.atth.attachedDocs[0]!=null) $scope.atth.selectedAttachedDoc = $scope.atth.attachedDocs[0];
		
		var consultId = 0;
		if (consult.id!=null) consultId = consult.id;
		consultService.getResponseAttachments(consultId, consult.demographic.demographicNo).then(function(data){
			if (consult.availableDocs==null) consult.availableDocs = toArray(data);
			$scope.atth.availableDocs = consult.availableDocs;
			sortAttachmentDocs($scope.atth.availableDocs);
			if ($scope.atth.availableDocs[0]!=null) $scope.atth.selectedAvailableDoc = $scope.atth.availableDocs[0];
		});
		
		$scope.openDoc = function(doc){
			window.open("../"+doc.url);
		}
		
		$scope.attach = function(){
			if ($scope.atth.selectedAvailableDoc==null) return;

			$scope.atth.attachedDocs.push($scope.atth.selectedAvailableDoc);
			$scope.atth.selectedAttachedDoc = $scope.atth.selectedAvailableDoc;
			$scope.atth.selectedAttachedDoc.attached = true;
			sortAttachmentDocs($scope.atth.attachedDocs);
			
			var x = $("#selAvailDoc").val();
			$scope.atth.availableDocs.splice(x, 1);
			if (x>=$scope.atth.availableDocs.length) x = $scope.atth.availableDocs.length-1;
			$scope.atth.selectedAvailableDoc = $scope.atth.availableDocs[x];
			
			consult.attachmentsChanged = true;
		}
		
		$scope.detach = function(){
			if ($scope.atth.selectedAttachedDoc==null) return;
			
			$scope.atth.availableDocs.push($scope.atth.selectedAttachedDoc);
			$scope.atth.selectedAvailableDoc = $scope.atth.selectedAttachedDoc;
			$scope.atth.selectedAvailableDoc.attached = false;
			sortAttachmentDocs($scope.atth.availableDocs);
			
			var x = $("#selAttachDoc").val();
			$scope.atth.attachedDocs.splice(x, 1);
			if (x>=$scope.atth.attachedDocs.length) x = $scope.atth.attachedDocs.length-1;
			$scope.atth.selectedAttachedDoc = $scope.atth.attachedDocs[x];
			
			consult.attachmentsChanged = true;
		}
		
		$scope.done = function() {
			$modalInstance.close();
		}
	}
	//end modal controller
	
	
	$scope.save = function(){
		if ($scope.invalidData()) return;

		$scope.consultSaving = true; //show saving banner
		$scope.setAppointmentTime();
		
		consultService.saveResponse(consult).then(function(data){
			//update url for new consultation
			if (consult.id==null) $location.path("/consultResponses/"+data.id);
		});
		$scope.consultSaving = false; //hide saving banner
		$scope.consultChanged = -1; //reset change count
		return true;
	}
	
	//fax & print functions
	var p_page1 = "<html><style>body{width:800px;font-family:arial,verdana,tahoma,helvetica,sans serif}table{width:100%}th{text-align:left;font-weight:bold;width:1;white-space:nowrap}td{vertical-align:top}label{font-weight:bold}em{font-size:small}.large{font-size:large}.center{text-align:center}</style><style media='print'>button{display:none}.noprint{display:none}</style><script>function printAttachments(url){window.open('../'+url);}</script><body>";
	
	$scope.sendFax = function(){
		var p_urgency = noNull($scope.urgencies[$("#urgency").val()].name);
		var p_letterheadName = noNull(consult.letterheadList[$("#letterhead").val()].name);
		var p_page2 = getPrintPage2(p_urgency, p_letterheadName, consult, user);
		
		var consultResponsePage = encodeURIComponent(p_page1 + p_page2);
		var reqId = consult.id;
		var demographicNo = consult.demographic.demographicNo;
		var letterheadFax = noNull(consult.letterheadFax);
		var fax = noNull(consult.referringDoctor.faxNumber);
		
		window.open("../fax/CoverPage.jsp?consultResponsePage="+consultResponsePage+"&reqId="+reqId+"&demographicNo="+demographicNo+"&letterheadFax="+letterheadFax+"&fax="+fax);
	}
	
	$scope.printPreview = function(){
		if ($scope.invalidData()) return;

		var printWin = window.open("","consultResponsePrintWin","width=830,height=900,scrollbars=yes,location=no");
		printWin.document.open();

		var p_buttons = "<button onclick='window.print()'>Print</button><button onclick='window.close()'>Close</button>";
		var p_attachments = "";
		for (var i=0; i<consult.attachments.length; i++) {
			p_attachments += "<div class='noprint'><button onclick=printAttachments('"+consult.attachments[i].url+"')>Print attachment</button> "+consult.attachments[i].displayName+"</div>";
		}
		
		var p_urgency = noNull($scope.urgencies[$("#urgency").val()].name);
		var p_letterheadName = noNull(consult.letterheadList[$("#letterhead").val()].name);
		var p_page2 = getPrintPage2(p_urgency, p_letterheadName, consult, user);
		
		printWin.document.write(p_page1 + p_buttons + p_attachments + p_page2);
		printWin.document.close();
	}
});



function toArray(obj) { //convert single object to array
	if (obj instanceof Array) return obj;
	else if (obj==null) return [];
	else return [obj];
}

function writeToBox(data, boxId) {
	var items = toArray(data.summaryItem);
	for (var i=0; i<items.length; i++) {
		if (items[i].displayName!=null) {
			if ($("#"+boxId).val().trim()!="") $("#"+boxId).val($("#"+boxId).val()+"\n");
			$("#"+boxId).val($("#"+boxId).val()+items[i].displayName);
		}
	}
}

function pad0(n) {
	var s = n.toString();
	if (s.length==1) s = "0"+s;
	return s;
}

function noNull(s) {
	if (s==null) s = "";
	if (s instanceof String) s=s.trim();
	return s;
}

function formatDate(d) {
	d = noNull(d);
	if (d) {
		if (!(d instanceof Date)) d = new Date(d);
		d = d.getFullYear()+"-"+pad0(d.getMonth()+1)+"-"+pad0(d.getDate());
	}
	return d;
}

function formatTime(d) {
	d = noNull(d);
	if (d) {
		if (!(d instanceof Date)) d = new Date(d);
		d = pad0(d.getHours())+":"+pad0(d.getMinutes());
	}
	return d;
}

function sortAttachmentDocs(arrayOfDocs) {
	arrayOfDocs.sort(function(doc1, doc2){
		if (doc1.documentType<doc2.documentType) return -1;
		else if (doc1.documentType>doc2.documentType) return 1;
		else {
			if (doc1.displayName<doc2.displayName) return -1;
			else if (doc1.displayName>doc2.displayName) return 1;
		}
		return 0;
	});
}

function getPrintPage2(p_urgency, p_letterheadName, consult, user) {
	var p_clinicName = noNull(consult.letterheadList[0].name);
	var p_responseDate = formatDate(consult.responseDate);
	var p_referralDate = formatDate(consult.referralDate);
	var p_letterheadAddress = noNull(consult.letterheadAddress);
	var p_letterheadPhone = noNull(consult.letterheadPhone);
	var p_letterheadFax = noNull(consult.letterheadFax);
	var p_consultantName = noNull(consult.referringDoctor.name);
	var p_consultantPhone = noNull(consult.referringDoctor.phoneNumber);
	var p_consultantFax = noNull(consult.referringDoctor.faxNumber);
	var p_consultantAddress = noNull(consult.referringDoctor.streetAddress);
	var p_patientName = consult.demographic.lastName+", "+consult.demographic.firstName;
	var p_patientAddress = consult.demographic.address.address+", "+consult.demographic.address.city+", "+consult.demographic.address.province+" "+consult.demographic.address.postal;
	var p_patientPhone = noNull(consult.demographic.phone);
	var p_patientWorkPhone = noNull(consult.demographic.alternativePhone);
	var p_patientBirthdate = formatDate(consult.demographic.dateOfBirth);
	var p_patientSex = noNull(consult.demographic.sexDesc);
	var p_patientHealthCardNo = consult.demographic.hin+" - "+consult.demographic.ver;
	var p_appointmentDate = formatDate(consult.appointmentDate);
	var p_appointmentTime = formatTime(consult.appointmentTime);
	var p_patientChartNo = noNull(consult.demographic.chartNo);
	var p_reason = noNull(consult.reasonForReferral);
	var p_examination = noNull(consult.examination);
	var p_impression = noNull(consult.impression);
	var p_plan = noNull(consult.plan);
	var p_clinicalInfo = noNull(consult.clinicalInfo);
	var p_concurrentProblems = noNull(consult.concurrentProblems);
	var p_currentMeds = noNull(consult.currentMeds);
	var p_allergies = noNull(consult.allergies);
	var p_referringProvider = user.lastName+", "+user.firstName;
	var p_provider = user.lastName+", "+user.firstName;
	
	return "<div class='center'><label class='large'>"+p_clinicName+"</label><br/><label>Consultation Response</label><br/></div><br/><table><tr><td><label>Date: </label>"+p_responseDate+"</td><td rowspan=6 width=10></td><td><label>Status: </label>"+p_urgency+"</td></tr><tr><td colspan=2></td></tr><tr><th>FROM:</th><th>TO:</th></tr><tr><td><p class='large'>"+p_letterheadName+"</p>"+p_letterheadAddress+"<br/><label>Tel: </label>"+p_letterheadPhone+"<br/><label>Fax: </label>"+p_letterheadFax+"</td><td><table><tr><th>Referring Doctor:</th><td>"+p_consultantName+"</td></tr><tr><th>Phone:</th><td>"+p_consultantPhone+"</td></tr><tr><th>Fax:</th><td>"+p_consultantFax+"</td></tr><tr><th>Address:</th><td>"+p_consultantAddress+"</td></tr></table></td></tr><tr><td colspan=2></td></tr><tr><td><table><tr><th>Patient:</th><td>"+p_patientName+"</td></tr><tr><th>Address:</th><td>"+p_patientAddress+"</td></tr><tr><th>Phone:</th><td>"+p_patientPhone+"</td></tr><tr><th>Work Phone:</th><td>"+p_patientWorkPhone+"</td></tr><tr><th>Birthdate:</th><td>"+p_patientBirthdate+"</td></tr></table></td><td><table><tr><th>Sex:</th><td>"+p_patientSex+"</td></tr><tr><th>Health Card No:</th><td>"+p_patientHealthCardNo+"</td></tr><tr><th>Appointment date:</th><td>"+p_appointmentDate+"</td></tr><tr><th>Appointment time:</th><td>"+p_appointmentTime+"</td></tr><tr><th>Chart No:</th><td>"+p_patientChartNo+"</td></tr></table></td></tr></table><br/><table><tr><th>Examination:</th></tr><tr><td>"+p_examination+"<hr></td></tr><tr><th>Impression:</th></tr><tr><td>"+p_impression+"<hr></td></tr><tr><th>Plan:</th></tr><tr><td>"+p_plan+"<hr></td></tr><tr><td></td></tr><tr><th>Reason for consultation: (Date: "+p_referralDate+")</th></tr><tr><td>"+p_reason+"<hr></td></tr><tr><th>Pertinent Clinical Information:</th></tr><tr><td>"+p_clinicalInfo+"<hr></td></tr><tr><th>Significant Concurrent Problems:</th></tr><tr><td>"+p_concurrentProblems+"<hr></td></tr><tr><th>Current Medications:</th></tr><tr><td>"+p_currentMeds+"<hr></td></tr><tr><th>Allergies:</th></tr><tr><td>"+p_allergies+"<hr></td></tr><tr><td><label>Consultant: </label>"+p_provider+"</td></tr><tr><td></td></tr><tr><td><div class='center'><em>Created by: OSCAR The open-source EMR www.oscarcanada.org</em></div></td></tr></table></body></html>";
}
/* html for fax & print, kept here for easy reference
<html>
<style>
	body{width:800px;font-family:arial,verdana,tahoma,helvetica,sans serif}
	table{width:100%}
	th{text-align:left;font-weight:bold;width:1;white-space:nowrap}
	td{vertical-align:top}
	label{font-weight:bold}
	em{font-size:small}
	.large{font-size:large}
	.center{text-align:center}
</style>
<style media='print'>
	button{display:none}
	.noprint{display:none}
</style>
<script>
	function printAttachments(url){
		window.open('../'+url);
	}
</script>
<body>

<!-- Print preview page exclusive -->
	<!-- p_buttons -->
	<button onclick='window.print()'>Print</button>
	<button onclick='window.close()'>Close</button>
	<!-- p_buttons -->
	
	<!-- p_attachments, 1 or more -->
	<div class='noprint'>
		<button onclick=printAttachments('"+consult.attachments[i].url+"')>Print attachment</button> "+consult.attachments[i].displayName+"
	</div>
	<!-- p_attachments -->
<!-- Print preview page exclusive -->
	
	<div class='center'>
		<label class='large'>"+p_clinicName+"</label><br/>
		<label>Consultation Response</label><br/>
	</div>
	<br/>
	<table>
		<tr>
			<td>
				<label>Date: </label>"+p_responseDate+"
			</td>
			<td rowspan=6 width=10></td>
			<td>
				<label>Status: </label>"+p_urgency+"
			</td>
		</tr>
		<tr><td colspan=2></td></tr>
		<tr>
			<th>FROM:</th>
			<th>TO:</th>
		</tr>
		<tr>
			<td>
				<p class='large'>"+p_letterheadName+"</p>
				"+p_letterheadAddress+"<br/>
				<label>Tel: </label>"+p_letterheadPhone+"<br/>
				<label>Fax: </label>"+p_letterheadFax+"
			</td>
			<td>
				<table>
					<tr>
						<th>Referring Doctor:</th>
						<td>"+p_consultantName+"</td>
					</tr>
					<tr>
						<th>Phone:</th>
						<td>"+p_consultantPhone+"</td>
					</tr>
					<tr>
						<th>Fax:</th>
						<td>"+p_consultantFax+"</td>
					</tr>
					<tr>
						<th>Address:</th>
						<td>"+p_consultantAddress+"</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td colspan=2></td></tr>
		<tr>
			<td>
				<table>
					<tr>
						<th>Patient:</th>
						<td>"+p_patientName+"</td>
					</tr>
					<tr>
						<th>Address:</th>
						<td>"+p_patientAddress+"</td>
					</tr>
					<tr>
						<th>Phone:</th>
						<td>"+p_patientPhone+"</td>
					</tr>
					<tr>
						<th>Work Phone:</th>
						<td>"+p_patientWorkPhone+"</td>
					</tr>
					<tr>
						<th>Birthdate:</th>
						<td>"+p_patientBirthdate+"</td>
					</tr>
				</table>
			</td>
			<td>
				<table>
					<tr>
						<th>Sex:</th>
						<td>"+p_patientSex+"</td>
					</tr>
					<tr>
						<th>Health Card No:</th>
						<td>"+p_patientHealthCardNo+"</td>
					</tr>
					<tr>
						<th>Appointment date:</th>
						<td>"+p_appointmentDate+"</td>
					</tr>
					<tr>
						<th>Appointment time:</th>
						<td>"+p_appointmentTime+"</td>
					</tr>
					<tr>
						<th>Chart No:</th>
						<td>"+p_patientChartNo+"</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<br/>
	<table>
		<tr><th>Examination:</th></tr>
		<tr><td>"+p_examination+"<hr></td></tr>
		<tr><th>Impression:</th></tr>
		<tr><td>"+p_impression+"<hr></td></tr>
		<tr><th>Plan:</th></tr>
		<tr><td>"+p_plan+"<hr></td></tr>
		<tr><td></td></tr>
		<tr><th>Reason for consultation: (Date: "+p_referralDate+")</th></tr>
		<tr><td>"+p_reason+"<hr></td></tr>
		<tr><th>Pertinent Clinical Information:</th></tr>
		<tr><td>"+p_clinicalInfo+"<hr></td></tr>
		<tr><th>Significant Concurrent Problems:</th></tr>
		<tr><td>"+p_concurrentProblems+"<hr></td></tr>
		<tr><th>Current Medications:</th></tr>
		<tr><td>"+p_currentMeds+"<hr></td></tr>
		<tr><th>Allergies:</th></tr>
		<tr><td>"+p_allergies+"<hr></td></tr>
		<tr><td><label>Consultant: </label>"+p_provider+"</td></tr>
		<tr><td></td></tr>
		<tr><td><div class='center'><em>Created by: OSCAR The open-source EMR www.oscarcanada.org</em></div></td></tr>
	</table>
</body>
</html>
*/
