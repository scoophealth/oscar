oscarApp.controller('ConsultRequestCtrl', function ($scope,$http,$resource,$location,$modal,consultService,demographicService,securityService,summaryService,staticDataService,consult,user) {
	
	//get access rights
	securityService.hasRight("_con", "r").then(function(data){
		$scope.consultReadAccess=data;
	});
	securityService.hasRight("_con", "u").then(function(data){
		$scope.consultUpdateAccess=data;
	});
	securityService.hasRight("_con", "w").then(function(data){
		$scope.consultWriteAccess=data;
	});
	
	$scope.consult = consult;
	
	consult.letterheadList = toArray(consult.letterheadList);
	consult.faxList = toArray(consult.faxList);
	consult.serviceList = toArray(consult.serviceList);
	consult.sendToList = toArray(consult.sendToList);
	
	//set demographic info
	demographicService.getDemographic(consult.demographicId).then(function(data){
		consult.demographic = data;
		
		//set cell phone
		consult.demographic.extras = toArray(consult.demographic.extras);
		for (var i=0; i<consult.demographic.extras.length; i++) {
			if (consult.demographic.extras[i].key=="demo_cell") {
				consult.demographic.cellPhone = consult.demographic.extras[i].value;
				break;
			}
		}
	});
	
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
	
	//set specialist list
	for (var i=0; i<consult.serviceList.length; i++) {
		if (consult.serviceList[i].serviceId==consult.serviceId) {
			$scope.specialists = toArray(consult.serviceList[i].specialists);
			break;
		}
	}
	angular.forEach($scope.specialists, function(spec) {
		if (spec.id == consult.professionalSpecialist.id) {
			consult.professionalSpecialist = spec;
		}
	});
	
	//set attachments
	consult.attachments = toArray(consult.attachments);
	sortAttachmentDocs(consult.attachments);
	
	//set appointment time
	if (consult.appointmentTime!=null) {
		var apptTime = new Date(consult.appointmentTime);
		consult.appointmentHour = pad0(apptTime.getHours());
		consult.appointmentMinute = pad0(apptTime.getMinutes());
	}

	$scope.urgencies = staticDataService.getConsultUrgencies();
	$scope.statuses = staticDataService.getConsultRequestStatuses();
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
	
	$scope.changeService = function(){
		var index = $("#serviceId").val();
		if (index==null) {
			$scope.specialists = null;
			return;
		}
		$scope.specialists = toArray(consult.serviceList[index].specialists);
		consult.professionalSpecialist = null;
	};

	$scope.writeToBox = function(data, boxId){
		var items = toArray(data.summaryItem);
		var boxData = null;
		for (var i=0; i<items.length; i++) {
			boxData = addNewLine(items[i].displayName, boxData);
		}
		if (boxId=="clinicalInfo") consult.clinicalInfo = addNewLine(boxData, consult.clinicalInfo);
		else if (boxId=="concurrentProblems") consult.concurrentProblems = addNewLine(boxData, consult.concurrentProblems);
		else if (boxId=="currentMeds") consult.currentMeds = addNewLine(boxData, consult.currentMeds);
	}
	
	
	$scope.getFamilyHistory = function(boxId){
		summaryService.getFamilyHistory(consult.demographicId).then(function(data){ $scope.writeToBox(data, boxId); });
	}
	$scope.getMedicalHistory = function(boxId){
		summaryService.getMedicalHistory(consult.demographicId).then(function(data){ $scope.writeToBox(data, boxId); });
	}
	$scope.getOngoingConcerns = function(boxId){
		summaryService.getOngoingConcerns(consult.demographicId).then(function(data){ $scope.writeToBox(data, boxId); });
	}
	$scope.getOtherMeds = function(boxId){
		summaryService.getOtherMeds(consult.demographicId).then(function(data){ $scope.writeToBox(data, boxId); });
	}
	$scope.getReminders = function(boxId){
		summaryService.getReminders(consult.demographicId).then(function(data){ $scope.writeToBox(data, boxId); });
	}
	
	$scope.invalidData = function(){
		if ($scope.urgencies[$("#urgency").val()]==null) {
			alert("Please select an Urgency"); return true;
		}
		if (consult.letterheadList[$("#letterhead").val()]==null) {
			alert("Please select a Letterhead"); return true;
		}
		if (consult.serviceList[$("#serviceId").val()]==null) {
			alert("Please select a Specialist Service"); return true;
		}
		if (consult.professionalSpecialist==null) {
			alert("Please select a Specialist"); return true;
		}
		if (consult.demographic==null || consult.demographic=="") {
			alert("Error! Invalid patient!"); return true;
		}
		return false;
	}
	
	$scope.setAppointmentTime = function(){
		if (consult.appointmentHour!=null && consult.appointmentMinute!=null && !consult.patientWillBook) {
			var apptTime = new Date();
			if (consult.appointmentTime!=null) apptTime = new Date(consult.appointmentTime);
			apptTime.setHours(consult.appointmentHour);
			apptTime.setMinutes(consult.appointmentMinute);
			apptTime.setSeconds(0);
			consult.appointmentTime = apptTime;
		} else {
			consult.appointmentTime = null;
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
		consultService.getRequestAttachments(consultId, consult.demographicId).then(function(data){
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
	
	
	//show/hide e-send button
	$scope.setESendEnabled = function(){
		$scope.eSendEnabled = consult.professionalSpecialist!=null && consult.professionalSpecialist.eDataUrl!=null && consult.professionalSpecialist.eDataUrl.trim()!="";
	}
	$scope.setESendEnabled(); //execute once on form open
	
	$scope.save = function(){
		if (!$scope.consultWriteAccess && consult.id==null) {
			alert("You don't have right to save new consult");
			return false;
		}
		if (!$scope.consultUpdateAccess) {
			alert("You don't have right to update consult");
			return false;
		}
		
		if ($scope.invalidData()) return false;
		
		$scope.consultSaving = true; //show saving banner
		$scope.setAppointmentTime();
		
		consultService.saveRequest(consult).then(function(data){
			if (consult.id==null) $location.path("/record/"+consult.demographicId+"/consult/"+data.id);
		});
		$scope.setESendEnabled();
		$scope.consultSaving = false; //hide saving banner
		$scope.consultChanged = -1; //reset change count
		return true;
	}
	
	$scope.close = function(){
		if ($location.search().list=="patient") $location.path("/record/"+consult.demographicId+"/consults");
		else $location.path("/consults");
	}
	
	$scope.sendFax = function(){
		var reqId = consult.id;
		var demographicNo = consult.demographicId;
		var letterheadFax = noNull(consult.letterheadFax);
		var fax = noNull(consult.professionalSpecialist.faxNumber);
//		var faxRecipients = *additional fax recipients (can be >1)*
		
		window.open("../fax/CoverPage.jsp?reqId="+reqId+"&demographicNo="+demographicNo+"&letterheadFax="+letterheadFax+"&fax="+fax);
	}
	
	$scope.eSend = function(){
		if ($scope.eSendEnabled) {
			consultService.eSendRequest(consult.id).then(function(data){
				alert(data.message);
			});
		}
	}
	
	$scope.printPreview = function(){
		if ($scope.invalidData()) return;
		
		window.open("../oscarEncounter/oscarConsultationRequest/printPdf2.do?reqId="+consult.id+"&demographicNo="+consult.demographicId);
/*
		var printWin = window.open("","consultRequestPrintWin","width=830,height=900,scrollbars=yes,location=no");
		printWin.document.open();
		
		var replyTo = "Please reply ";
		if (consult.patientWillBook) {
			replyTo = "";
		} else {
			if (noNull(consult.letterheadList[0].name)!="") replyTo += "to " + consult.letterheadList[0].name;
			replyTo += " by fax or by phone with appointment";
		}
		
		var urgency = noNull($scope.urgencies[$("#urgency").val()].name);
		var referralDate = formatDate(consult.referralDate);
		var letterheadName = noNull(consult.letterheadList[$("#letterhead").val()].name);
		var letterheadAddress = noNull(consult.letterheadAddress);
		var letterheadPhone = noNull(consult.letterheadPhone);
		var letterheadFax = noNull(consult.letterheadFax);
		var serviceName = noNull(consult.serviceList[$("#serviceId").val()].serviceDesc);
		var consultantName = noNull(consult.professionalSpecialist.name);
		var consultantPhone = noNull(consult.professionalSpecialist.phoneNumber);
		var consultantFax = noNull(consult.professionalSpecialist.faxNumber);
		var consultantAddress = noNull(consult.professionalSpecialist.streetAddress);
		var patientName = noNull(consult.demographic.lastName)+", "+noNull(consult.demographic.firstName);
		var patientPhone = noNull(consult.demographic.phone);
		var patientWorkPhone = noNull(consult.demographic.alternativePhone);
		var patientBirthdate = formatDate(consult.demographic.dateOfBirth);
		var patientSex = noNull(consult.demographic.sexDesc);
		var patientHealthCardNo = noNull(consult.demographic.hin)+"-"+noNull(consult.demographic.ver);
		var patientChartNo = noNull(consult.demographic.chartNo);
		var patientAddress = "";
		if (consult.demographic.address!=null) {
			patientAddress = noNull(consult.demographic.address.address)+", "+noNull(consult.demographic.address.city)+", "+noNull(consult.demographic.address.province)+" "+noNull(consult.demographic.address.postal);
		}
		var appointmentDate = formatDate(consult.appointmentDate);
		var appointmentTime = formatTime(consult.appointmentTime);
		var reason = noNull(consult.reasonForReferral);
		var clinicalInfo = noNull(consult.clinicalInfo);
		var concurrentProblems = noNull(consult.concurrentProblems);
		var currentMeds = noNull(consult.currentMeds);
		var allergies = noNull(consult.allergies);
		var referringProvider = noNull(user.lastName)+", "+noNull(user.firstName);
		var mrp = "";
		if (consult.demographic.provider!=null) {
			mrp = noNull(consult.demographic.provider.lastName)+", "+noNull(consult.demographic.provider.firstName);
		}
		
		var reqId = consult.id;
		var demoId = consult.demographicId;
		var userId = user.providerNo;
		
		printWin.document.write("<html><style>body {width:800px;font-family:arial,verdana,tahoma,helvetica,sans serif;}div {text-align:center;}table {width:100%;}th {text-align:left;font-weight:bold;width:1;white-space:nowrap}td {vertical-align:top;}label {font-weight:bold;}em {font-size:small;}p {font-size:large;}</style><style media='print'>button {display: none;}</style><script>function printAttachments(){window.location.href='../oscarEncounter/oscarConsultationRequest/attachmentReport.jsp?reqId="+reqId+"&demographicNo="+demoId+"&providerNo="+userId+"';}</script><body><button onclick='window.print();'>Print</button><button onclick='printAttachments()'>Print attachments</button><button onclick='window.close()'>Close</button><div><label>Consultation Request</label><br/><label>"+replyTo+"</label></div><br/><table><tr><td><label>Date: </label>"+referralDate+"</td><td rowspan=6 width=10>&nbsp;</td><td><label>Status: </label>"+urgency+"</td></tr><tr><td colspan=2>&nbsp;</td></tr><tr><th>FROM:</th><th>TO:</th></tr><tr><td><p>"+letterheadName+"</p>"+letterheadAddress+"<br/><label>Tel: </label>"+letterheadPhone+"<br/><label>Fax: </label>"+letterheadFax+"</td><td><table><tr><th>Consultant:</th><td>"+consultantName+"</td></tr><tr><th>Service:</th><td>"+serviceName+"</td></tr><tr><th>Phone:</th><td>"+consultantPhone+"</td></tr><tr><th>Fax:</th><td>"+consultantFax+"</td></tr><tr><th>Address:</th><td>"+consultantAddress+"</td></tr></table></td></tr><tr><td colspan=2>&nbsp;</td></tr><tr><td><table><tr><th>Patient:</th><td>"+patientName+"</td></tr><tr><th>Address:</th><td>"+patientAddress+"</td></tr><tr><th>Phone:</th><td>"+patientPhone+"</td></tr><tr><th>Work Phone:</th><td>"+patientWorkPhone+"</td></tr><tr><th>Birthdate:</th><td>"+patientBirthdate+"</td></tr></table></td><td><table><tr><th>Sex:</th><td>"+patientSex+"</td></tr><tr><th>Health Card No:</th><td>"+patientHealthCardNo+"</td></tr><tr><th>Appointment date:</th><td>"+appointmentDate+"</td></tr><tr><th>Appointment time:</th><td>"+appointmentTime+"</td></tr><tr><th>Chart No:</th><td>"+patientChartNo+"</td></tr></table></td></tr></table><br/><table><tr><th>Reason for consultation:</th></tr><tr><td>"+reason+"<hr></td></tr><tr><th>Pertinent Clinical Information:</th></tr><tr><td>"+clinicalInfo+"<hr></td></tr><tr><th>Significant Concurrent Problems:</th></tr><tr><td>"+concurrentProblems+"<hr></td></tr><tr><th>Current Medications:</th></tr><tr><td>"+currentMeds+"<hr></td></tr><tr><th>Allergies:</th></tr><tr><td>"+allergies+"<hr></td></tr><tr><td><label>Referring Practitioner: </label>"+referringProvider+"</td></tr><tr><td><label>MRP: </label>"+mrp+"</td></tr><tr><td>&nbsp;</td></tr><tr><td><div><em>Created by: OSCAR The open-source EMR www.oscarcanada.org</em></div></td></tr></table></body></html>");
		printWin.document.close();
*/
	}
/* html for printPreview, kept here for easy reference
<html>
<style>
	body {width:800px;font-family:arial,verdana,tahoma,helvetica,sans serif;}
	div {text-align:center;}
	table {width:100%;}
	th {text-align:left;font-weight:bold;width:1;white-space:nowrap}
	td {vertical-align:top;}
	label {font-weight:bold;}
	em {font-size:small;}
	p {font-size:large;}
</style>
<style media='print'>
	button {display: none;}
</style>
<script>
	function printAttachments(){
		window.location.href='../oscarEncounter/oscarConsultationRequest/attachmentReport.jsp?reqId="+reqId+"&demographicNo="+demoId+"&providerNo="+userId+"';
	}
</script>
<body>
	<button onclick='window.print();'>Print</button>
	<button onclick='printAttachments()'>Print attachments</button>
	<button onclick='window.close()'>Close</button>
	<div>
		<label>Consultation Request</label><br/>
		<label>"+replyTo+"</label>
	</div>
	<br/>
	<table>
		<tr>
			<td>
				<label>Date: </label>"+referralDate+"
			</td>
			<td rowspan=6 width=10>&nbsp;</td>
			<td>
				<label>Status: </label>"+urgency+"
			</td>
		</tr>
		<tr><td colspan=2>&nbsp;</td></tr>
		<tr>
			<th>FROM:</th>
			<th>TO:</th>
		</tr>
		<tr>
			<td>
				<p>"+letterheadName+"</p>
				"+letterheadAddress+"<br/>
				<label>Tel: </label>"+letterheadPhone+"<br/>
				<label>Fax: </label>"+letterheadFax+"
			</td>
			<td>
				<table>
					<tr>
						<th>Consultant:</th>
						<td>"+consultantName+"</td>
					</tr>
					<tr>
						<th>Service:</th>
						<td>"+serviceName+"</td>
					</tr>
					<tr>
						<th>Phone:</th>
						<td>"+consultantPhone+"</td>
					</tr>
					<tr>
						<th>Fax:</th>
						<td>"+consultantFax+"</td>
					</tr>
					<tr>
						<th>Address:</th>
						<td>"+consultantAddress+"</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td colspan=2>&nbsp;</td></tr>
		<tr>
			<td>
				<table>
					<tr>
						<th>Patient:</th>
						<td>"+patientName+"</td>
					</tr>
					<tr>
						<th>Address:</th>
						<td>"+patientAddress+"</td>
					</tr>
					<tr>
						<th>Phone:</th>
						<td>"+patientPhone+"</td>
					</tr>
					<tr>
						<th>Work Phone:</th>
						<td>"+patientWorkPhone+"</td>
					</tr>
					<tr>
						<th>Birthdate:</th>
						<td>"+patientBirthdate+"</td>
					</tr>
				</table>
			</td>
			<td>
				<table>
					<tr>
						<th>Sex:</th>
						<td>"+patientSex+"</td>
					</tr>
					<tr>
						<th>Health Card No:</th>
						<td>"+patientHealthCardNo+"</td>
					</tr>
					<tr>
						<th>Appointment date:</th>
						<td>"+appointmentDate+"</td>
					</tr>
					<tr>
						<th>Appointment time:</th>
						<td>"+appointmentTime+"</td>
					</tr>
					<tr>
						<th>Chart No:</th>
						<td>"+patientChartNo+"</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<br/>
	<table>
		<tr><th>Reason for consultation:</th></tr>
		<tr><td>"+reason+"<hr></td></tr>
		<tr><th>Pertinent Clinical Information:</th></tr>
		<tr><td>"+clinicalInfo+"<hr></td></tr>
		<tr><th>Significant Concurrent Problems:</th></tr>
		<tr><td>"+concurrentProblems+"<hr></td></tr>
		<tr><th>Current Medications:</th></tr>
		<tr><td>"+currentMeds+"<hr></td></tr>
		<tr><th>Allergies:</th></tr>
		<tr><td>"+allergies+"<hr></td></tr>
		<tr><td><label>Referring Practitioner: </label>"+referringProvider+"</td></tr>
		<tr><td><label>MRP: </label>"+mrp+"</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td><div><em>Created by: OSCAR The open-source EMR www.oscarcanada.org</em></div></td></tr>
	</table>
</body>
</html>
*/
});



function toArray(obj) { //convert single object to array
	if (obj instanceof Array) return obj;
	else if (obj==null) return [];
	else return [obj];
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

function addNewLine(line, mssg) {
	if (line==null || line.trim()=="") return mssg;
	
	if (mssg==null || mssg.trim()=="") mssg = line.trim();
	else mssg += "\n" + line.trim();
	
	return mssg;
}
