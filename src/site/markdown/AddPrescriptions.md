### Description of Integration Point: Push MyOscar Measurements data

#### Workflow

##### 1. Search User (demographic/search.jsp)
A user may be searched by Name, Phone, DOB yyyy-mm-dd, Address, Health Ins. #, Chart No.

In this scenario, the user's lastname was entered for the Name search.
In the search listings, the Encounter link ("E") for the first patient was clicked.

##### 2. User Encounter (/oscar/oscarEncounter/IncomingEncounter.do?providerNo=999998&appointmentNo=&demographicNo=1...)

1. Forwards to oscarEncounter/Index2.jsp
2. If module caisi is loaded, forwards to /casemgmt/forward.jsp?action=view
3. forward.jsp executes to redirect to CaseManagementView.do /casemgmt/CaseManagementView.jsp

Displays Encounter screen.

##### 3. Click Drugs

Navigates to oscarRx/SearchDrug3.jsp


##### 4. Click Send To MyOscar

Oscar 

1. Invokes the jsp phr/send_medicaldata_to_myoscar.jsp (params ?demographicId=1&medicalDataType=Prescriptions). 
	1.1 If current user has +3 level access, redirects to phr/send_medicaldata_to_myoscar_action.jsp
	1.2 If current user doesn't have +3 level access, displays message confirming that you want to proceed.
2. Calls sendPrescriptionsMedicationsToMyOscar on the PrescriptionMedicationManager.
3. Compiles a list of Prescriptions/Medications changed since last sent to MyOscar
4. Calls method sendMedicationsToMyOscar(), which converts the medical data to XML format in a MedicalDataTransfer4 object
	4.1 Invokes MyOscarMedicalDataManagerUtils.addMedicalData and passes in the Prescription data. 
		
		Integration point
		`Long resultId=MedicalDataManager.addMedicalData(myOscarLoggedInInfo, medicalDataTransfer, completed, active);

	4.2 MyOscar
		4.2.1 Converts the XML format of the Medical data to object of type MedicalData. If the XML is invalid, it throws UnsupportedEncodingException.
		4.2.2 MedicalDataWs.addMedicalData() calls createMedicalData() on the MedicalDataManager class.
			a If there is no data in the MedicalData object,
				`throw(new InvalidRequestException("No medical data provided"))`

			b If the requesting person doesn't have access to create medical data for this patient,
			 
				`throw(new NotAuthorisedException("Not allowed to create medical data for personId=" + medicalData.getOwningPersonId())`

		4.2.3 Otherwise, persists the data to the database using the autowired MedicalDataDAO object.	

5. Calls method linkPrescriptionToMedications() on PrescriptionMedicationManager
	5.1 Passes in the medical data to static method MyOscarMedicalDataManagerUtils.addMedicalDataRelationship()

		Integration point
		`Long resultId=medicalDataWs.addMedicalDataRelationship2(ownerId, primaryMedicalDataId, relatedMedicalDataId, relationship);`

	5.2 MyOscar
		5.2.1 Calls medicalDataManager.addMedicalDataRelationship(medicalDataRelationship);
		5.2.2 Checks permissions of the person requesting this relationship change:

		`// if you can create or update, we'll let you associate/relate the data
		boolean allowed = medicalDataPermissionsManager.isAllowed(requestingPerson.getId(), CommonConstants.PERMISSION_FLAG_CREATE, medicalData.getOwningPersonId(), DataTypeCategory.MEDICAL_DATA.name() + '.' + medicalData.getMedicalDataType(), null);
		allowed = allowed || medicalDataPermissionsManager.isAllowed(requestingPerson.getId(), CommonConstants.PERMISSION_FLAG_UPDATE, medicalData.getOwningPersonId(), DataTypeCategory.MEDICAL_DATA.name() + '.' + medicalData.getMedicalDataType(), medicalData.getId());`

		5.2.3 If not authorized
			`throw(new NotAuthorisedException("Not authorised to create/edit this item."));`



	
	





