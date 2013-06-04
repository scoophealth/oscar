### Description of Integration Point: Push MyOscar Allergy data

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

##### 3. Click Allergies 

Navigates to ShowAllergies2.jsp

##### 3.1 (Alternate flow) Click Medications -> Active Allergies

User can click through to the ShowAllergies screen from the Active Allergies link on the medications screen.

##### 4. Click Send To MyOscar

Oscar 

1. Invokes the jsp phr/send_medicaldata_to_myoscar.jsp (params ?demographicId=1&medicalDataType=Allergies) 
	1.1 If current user has +3 level access, redirects to phr/send_medicaldata_to_myoscar_action.jsp
	1.2 If current user doesn't have +3 level access, displays message confirming that you want to proceed.
2. Calls static method AllergiesManager.sendAllergiesToMyOscar(), sending in the demographicNumber of the current patient
3. Compiles List of Allergy records added/modified since last sent to MyOscar
4. For each changed Allergy, 
	4.1 Calls toXML(), which converts the Allergy object to an XML representation.
	4.2 Creates an object of type MedicalDataTransfter4, a wrapper for the XML
	4.3 calls addMedicalData() on the MyOscarMedicalDataManagerUtils class, passing medicalDataTransfer object

		`MedicalDataTransfer4 medicalDataTransfer = toMedicalDataTransfer(myOscarLoggedInInfo, allergy);
		MyOscarMedicalDataManagerUtils.addMedicalData(myOscarLoggedInInfo, medicalDataTransfer, OSCAR_ALLERGIES_DATA_TYPE, allergy.getId(), false, true);`

	This is the web service call:

		`public static Long addMedicalData(MyOscarLoggedInInfo myOscarLoggedInInfo, MedicalDataTransfer4 medicalDataTransfer, String oscarDataType, Object localOscarObjectId, boolean completed, boolean active)
				throws NotAuthorisedException_Exception, 
				UnsupportedEncodingException_Exception, 
				InvalidRequestException_Exception {
			Long resultId=MedicalDataManager.addMedicalData(myOscarLoggedInInfo, medicalDataTransfer, completed, active);
			logger.debug("addMedicalData success : resultId="+resultId);

			addSendRemoteDataLog(oscarDataType, localOscarObjectId, medicalDataTransfer.getData());
		
			return(resultId);
		}`

MyOscar 
1. Converts the XML format of the Medical data to object of type MedicalData. If the XML is invalid, it throws UnsupportedEncodingException.
2. MedicalDataWs.addMedicalData() calls createMedicalData() on the MedicalDataManager class.
	2.1 If there is no data in the MedicalData object,
		`throw(new InvalidRequestException("No medical data provided"))`

	2.2 If the requesting person doesn't have access to create medical data for this patient,
 
		`throw(new NotAuthorisedException("Not allowed to create medical data for personId=" + medicalData.getOwningPersonId())`

3. Otherwise, persists the data to the database using the autowired MedicalDataDAO object.






