oscarApp.controller('ConsultDetailCtrl', function ($scope,$http,$resource,$location,consultService,demographicService,summaryService,staticDataService,consult,user) {
	$scope.consult = consult;
	
	$scope.consult.letterheadList = toArray($scope.consult.letterheadList);
	$scope.consult.faxList = toArray($scope.consult.faxList);
	$scope.consult.serviceList = toArray($scope.consult.serviceList);
	$scope.consult.sendToList = toArray($scope.consult.sendToList);
	
	//set specialist list
	var serviceList = $scope.consult.serviceList;
	for (var i=0; i<serviceList.length; i++) {
		if (serviceList[i].serviceId==$scope.consult.serviceId) {
			$scope.specialists = toArray(serviceList[i].specialists);
			
			for (var j=0; j<$scope.specialists.length; j++) {
				if ($scope.specialists[j].id==$scope.consult.professionalSpecialist.id) {
					$("#specId").val(j);
					break;
				}
			}
			break;
		}
	}
	
	//set demographic info
	demographicService.getDemographic($scope.consult.demographicId).then(function(data){
		$scope.demo = data;
		
		//set cell phone
		$scope.demo.extras = toArray($scope.demo.extras);
		for (var i=0; i<$scope.demo.extras.length; i++) {
			if ($scope.demo.extras[i].key=="demo_cell") {
				$scope.demo.cellPhone = $scope.demo.extras[i].value;
				break;
			}
		}
	});
	
	//set attachments
	consultService.getAttachments($scope.consult.id, $scope.consult.demographicId).then(function(data){
		$scope.attachments = data;
	});
	
	//set appointment time
	if ($scope.consult.appointmentTime!=null) {
		var apptTime = new Date($scope.consult.appointmentTime);
		$scope.consult.appointmentHour = pad0(apptTime.getHours());
		$scope.consult.appointmentMinute = pad0(apptTime.getMinutes());
	}

	
	$scope.urgencies = staticDataService.getConsultRequestUrgencies();
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
	
	$scope.changeLetterhead = function(id){
		var index = $("#letterhead").val();
		if (index==null) return;
		
		$scope.consult.letterheadAddress = $scope.consult.letterheadList[index].address;
		$scope.consult.letterheadPhone = $scope.consult.letterheadList[index].phone;
	};
	
	$scope.changeService = function(){
		var index = $("#serviceId").val();
		if (index==null) {
			$scope.specialists = null;
			return;
		}
		$scope.specialists = toArray($scope.consult.serviceList[index].specialists);
		$scope.consult.professionalSpecialist = null;
	};
	
	$scope.changeSpecialist = function(){
		var index = $("#specId").val();
		if (index==null) {
			$scope.consult.professionalSpecialist = null;
			return;
		}
		$scope.consult.professionalSpecialist = $scope.specialists[index];
	}
	
	$scope.getFamilyHistory = function(boxId){
		summaryService.getFamilyHistory($scope.consult.demographicId).then(function(data){ writeToBox(data, boxId); });
	}
	$scope.getMedicalHistory = function(boxId){
		summaryService.getMedicalHistory($scope.consult.demographicId).then(function(data){ writeToBox(data, boxId); });
	}
	$scope.getOngoingConcerns = function(boxId){
		summaryService.getOngoingConcerns($scope.consult.demographicId).then(function(data){ writeToBox(data, boxId); });
	}
	$scope.getOtherMeds = function(boxId){
		summaryService.getOtherMeds($scope.consult.demographicId).then(function(data){ writeToBox(data, boxId); });
	}
	$scope.getReminders = function(boxId){
		summaryService.getReminders($scope.consult.demographicId).then(function(data){ writeToBox(data, boxId); });
	}
	
	$scope.toPatientSummary = function(){
		$location.path("/record/"+$scope.consult.demographicId+"/summary");
	}
	
	$scope.toPatientConsultList = function(){
		$location.path("/consults?demographicId="+$scope.consult.demographicId);
	}
	
	$scope.save = function(){
		if ($scope.consult.professionalSpecialist==null) {
			alert("Please select a specialist");
			return false;
		}

		//set appointment time
		if ($scope.consult.appointmentHour!=null && $scope.consult.appointmentMinute!=null) {
			var apptTime = new Date();
			if ($scope.consult.appointmentTime!=null) apptTime = new Date($scope.consult.appointmentTime);
			apptTime.setHours($scope.consult.appointmentHour);
			apptTime.setMinutes($scope.consult.appointmentMinute);
			$scope.consult.appointmentTime = apptTime;
		}

		$scope.consultSaving = true;
		consultService.save($scope.consult).then(function(data){
			if ($scope.consult.id==null) {
				$scope.consult.id = data.id;
				$location.path("/consults/"+$scope.consult.id);
			}
			if ($scope.consult.id!=null) {
				$scope.consultSaving = false;
			}
			$scope.consultChanged = 0;
		});
		return true;
	}
	
	$scope.attachFiles = function(){
		if ($scope.consult.id==null) {
			var confirmSave = confirm("You must save this consultation first. Confirm save?");
			if (confirmSave) {
				if (!$scope.save()) return;
			} else {
				return;
			}
		}
		window.open("../oscarEncounter/oscarConsultationRequest/attachConsultation.jsp?provNo="+user.providerNo+"&demo="+$scope.consult.demographicId+"&requestId="+$scope.consult.id, "ConsultAttachment", "width=600, height=400");
	}
	
	$scope.printPreview = function(){
		var printWin = window.open("","consultRequestPrintWin","width=830,height=900,scrollbars=yes,location=no");
		printWin.document.open();
		
		var clinicName = $scope.consult.letterheadList[0].name;
		var referralDate = $scope.consult.referralDate;
		var urgency = $scope.urgencies[$("#urgency").val()].name;
		var letterheadName = $scope.consult.letterheadList[$("#letterhead").val()].name;
		var letterheadAddress = $scope.consult.letterheadAddress;
		var letterheadPhone = $scope.consult.letterheadPhone;
		var letterheadFax = $scope.consult.letterheadFax;
		var consultantName = $scope.consult.professionalSpecialist.name;
		var serviceName = $scope.consult.serviceList[$("#serviceId").val()].serviceDesc;
		var consultantPhone = $scope.consult.professionalSpecialist.phoneNumber;
		var consultantFax = $scope.consult.professionalSpecialist.faxNumber;
		var consultantAddress = $scope.consult.professionalSpecialist.streetAddress;
		var patientName = $scope.demo.lastName+", "+$scope.demo.firstName;
		var patientAddress = $scope.demo.address.address+", "+$scope.demo.address.city+", "+$scope.demo.address.province+" "+$scope.demo.address.postal;
		var patientPhone = $scope.demo.phone;
		var patientWorkPhone = $scope.demo.alternativePhone;
		var patientBirthdate = $scope.demo.dateOfBirth;
		var patientSex = $scope.demo.sexDesc;
		var patientHealthCardNo = $scope.demo.hin+" - "+$scope.demo.ver;
		var appointmentDate = $scope.consult.appointmentDate;
		var appointmentTime = $scope.consult.appointmentTime;
		var patientChartNo = $scope.demo.chartNo;
		var reason = $scope.consult.reasonForReferral;
		var clinicalInfo = $scope.consult.clinicalInfo;
		var concurrentProblems = $scope.consult.concurrentProblems;
		var currentMeds = $scope.consult.currentMeds;
		var allergies = $scope.consult.allergies;
		var referringProvider = user.lastName+", "+user.firstName;
		var mrp = $scope.demo.provider.lastName+", "+$scope.demo.provider.firstName;
		var reqId = $scope.consult.id;
		var demoId = $scope.consult.demographicId;
		var userId = user.providerNo;
		
		printWin.document.write("<html><style>body {width:800px;font-family:arial,verdana,tahoma,helvetica,sans serif;}div {text-align:center;}table {width:100%;}th {text-align:left;font-weight:bold;width:1;white-space:nowrap}td {vertical-align:top;}label {font-weight:bold;}em {font-size:small;}p {font-size:large;}</style><style media='print'>button {display: none;}</style><script>function printAttachments(){window.location.href='../oscarEncounter/oscarConsultationRequest/attachmentReport.jsp?reqId="+reqId+"&demographicNo="+demoId+"&providerNo="+userId+"';}</script><body><button onclick='window.print();'>Print</button><button onclick='printAttachments()'>Print attachments</button><button onclick='window.close()'>Close</button><div><label>Consultation Request</label><br/><label>Please reply to "+clinicName+" by fax or by phone with appointment</label></div><br/><table><tr><td><label>Date: </label>"+referralDate+"</td><td rowspan=6 width=10>&nbsp;</td><td><label>Status: </label>"+urgency+"</td></tr><tr><td colspan=2>&nbsp;</td></tr><tr><th>FROM:</th><th>TO:</th></tr><tr><td><p>"+letterheadName+"</p>"+letterheadAddress+"<br/><label>Tel: </label>"+letterheadPhone+"<br/><label>Fax: </label>"+letterheadFax+"</td><td><table><tr><th>Consultant:</th><td>"+consultantName+"</td></tr><tr><th>Service:</th><td>"+serviceName+"</td></tr><tr><th>Phone:</th><td>"+consultantPhone+"</td></tr><tr><th>Fax:</th><td>"+consultantFax+"</td></tr><tr><th>Address:</th><td>"+consultantAddress+"</td></tr></table></td></tr><tr><td colspan=2>&nbsp;</td></tr><tr><td><table><tr><th>Patient:</th><td>"+patientName+"</td></tr><tr><th>Address:</th><td>"+patientAddress+"</td></tr><tr><th>Phone:</th><td>"+patientPhone+"</td></tr><tr><th>Work Phone:</th><td>"+patientWorkPhone+"</td></tr><tr><th>Birthdate:</th><td>"+patientBirthdate+"</td></tr></table></td><td><table><tr><th>Sex:</th><td>"+patientSex+"</td></tr><tr><th>Health Card No:</th><td>"+patientHealthCardNo+"</td></tr><tr><th>Appointment date:</th><td>"+appointmentDate+"</td></tr><tr><th>Appointment time:</th><td>"+appointmentTime+"</td></tr><tr><th>Chart No:</th><td>"+patientChartNo+"</td></tr></table></td></tr></table><br/><table><tr><th>Reason for consultation:</th></tr><tr><td>"+reason+"<hr></td></tr><tr><th>Pertinent Clinical Information:</th></tr><tr><td>"+clinicalInfo+"<hr></td></tr><tr><th>Significant Concurrent Problems:</th></tr><tr><td>"+concurrentProblems+"<hr></td></tr><tr><th>Current Medications:</th></tr><tr><td>"+currentMeds+"<hr></td></tr><tr><th>Allergies:</th></tr><tr><td>"+allergies+"<hr></td></tr><tr><td><label>Referring Practitioner: </label>"+referringProvider+"</td></tr><tr><td><label>MRP: </label>"+mrp+"</td></tr><tr><td>&nbsp;</td></tr><tr><td><div><em>Created by: OSCAR The open-source EMR www.oscarcanada.org</em></div></td></tr></table><button onclick='window.print();'>Print</button><button onclick='printAttachments()'>Print attachments</button><button onclick='window.close()'>Close</button></body></html>");
		printWin.document.close();
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
		<label>Please reply to "+clinicName+" by fax or by phone with appointment</label>
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
	<button onclick='window.print();'>Print</button>
	<button onclick='printAttachments()'>Print attachments</button>
	<button onclick='window.close()'>Close</button>
</body>
</html>
*/
	}
});



function toArray(obj) { //convert single object to array
	if (obj==null || (obj instanceof Array)) return obj;
	else return [obj];
}

function writeToBox(data, boxId) {
	var items = toArray(data.summaryItem);
	if (items!=null) {
		for (var i=0; i<items.length; i++) {
			if (items[i].displayName!=null) {
				if ($("#"+boxId).val().trim()!="") $("#"+boxId).val($("#"+boxId).val()+"\n");
				$("#"+boxId).val($("#"+boxId).val()+items[i].displayName);
			}
		}
	}
}

function pad0(n) {
	var s = n.toString();
	if (s.length==1) s = "0"+s;
	return s;
}