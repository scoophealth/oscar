# Integration Points in Oscar to MyOSCAR

### Purpose

The purpose of this document is to identify integration points in Oscar where Oscar communicates with MyOscar.
As Oscar evolves, it is necessary to always know and keep track of such integration points in order to maintain, update, and/or improve the quality of code.

### Background

Communication between Oscar and MyOSCAR is 1 way only.  Communication is always initiated from Oscar.  MyOSCAR is not aware of Oscar and services requests only.
Communication happens via web services.

### Current Integration Points

The following are a list of integration points listed below.

| Action | Class | Calling Method | Oscar Web Service | MyOscar Web Service | Further details |
|:------:|:-----:|:--------------:|:-----------------:|:------------------ :|:---------------:|
|PHR User Registration | PHRUserManagementAction | sendUserRegistration | org.oscarehr.myoscar.client.ws_manager.AccountManager.createPerson | accountManager.addPerson(personToCreate) | [Refer to PHRUserRegistration.html](PHRUserRegistration.html)|  
|PHR User Registration | PHRUserManagementAction | addRelationships | AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(myOscarLoggedInInfo) |MyOscarServerWebServicesManager.getAccountWs(MyOscarServerCredentials credentials) | Refer to PHRUserRegistration.md|  
|Send Allergy to MyOSCAR (ADD) | AllergiesManager | sendAllergiesToMyOscar(myOscarLoggedInInfo, demographicNo) | addMedicalData --> MyOscarServerWebServicesManager.getMedicalDataWs(myOscarLoggedInInfo); | project: myoscar_client_utils.MyOscarServerWebServicesManager.getMedicalDataWs | myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar|  
|Send Allergy to MyOSCAR (UPDATE) | AllergiesManager | sendAllergiesToMyOscar(myOscarLoggedInInfo, demographicNo) | updateMedicalData --> medicalDataWs.updateMedicalData5 |project: MedicalDataWs.updateMedicalData5 | MedicalDataWS in Oscar found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar|  
|Send patient a message | PHRMessageAction | sendPatient | MessageManager.sendMessage --> MyOscarServerWebServicesManager.getMessageWs | MessageReceivedEmailNotificationListener | Possible exceptions thrown 1) java.net.SocketTimeoutException: SocketTimeoutException invoking https://localhost:8091/myoscar_server/ws/MessageService Read timed out 2) when trying to "Add Patient Relationship" InvalidRelationshipException: Only Providers can be your primary care provider.|  
|Send to MyOSCAR (Immunizations) | send\_medicaldata\_to\_myoscar\_action\.jsp | ImmunizationsManager.sendImmunizationsToMyOscar | MyOscarMedicalDataManagerUtils.addMedicalData --> medicalDataWs.addMedicalData4 | MedicalDataWS.addMedicalData4 | MedicalDataWS in Oscar found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar|  
|Send to MyOSCAR (Measurements) | send\_medicaldata\_to\_myoscar\_action\.jsp | MeasurementsManager.sendMeasurementsToMyOscar | MyOscarMedicalDataManagerUtils.addMedicalData --> medicalDataWs.addMedicalData4 | MedicalDataWS.addMedicalData4 | MedicalDataWS in Oscar found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar|  
|Send to MyOSCAR (sendHeightWeight) | MeasurementsManager | sendHeightWeight | MyOscarMedicalDataManagerUtils.addMedicalData --> medicalDataWs.addMedicalData4 | MedicalDataWS.addMedicalData4 | MedicalDataWS in Oscar found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar|  
|Send to MyOSCAR (sendBloodPressure) | MeasurementsManager | sendBloodPressure | MyOscarMedicalDataManagerUtils.addMedicalData --> medicalDataWs.addMedicalData4 | MedicalDataWS.addMedicalData4 | MedicalDataWS in Oscar found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar|  
|Send to MyOSCAR (sendA1C) | MeasurementsManager | sendA1C | MyOscarMedicalDataManagerUtils.addMedicalData --> medicalDataWs.addMedicalData4 | MedicalDataWS.addMedicalData4 | MedicalDataWS in Oscar found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar|  
|Send to MyOSCAR (sendOtherHealthTracker) | MeasurementsManager | sendOtherHealthTracker | MyOscarMedicalDataManagerUtils.addMedicalData --> medicalDataWs.addMedicalData4 | MedicalDataWS.addMedicalData4 | MedicalDataWS in Oscar found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar |  
|Send to MyOSCAR (ensureOHTCategoryExists) | MeasurementsManager | ensureOHTCategoryExists | MyOscarMedicalDataManagerUtils.addMedicalData --> medicalDataWs.addMedicalData4 ALSO MyOscarMedicalDataManagerUtils.getMedicalData --> medicalDataWs.getMedicalData5  | MedicalDataWS.getMedicalData5 | MedicalDataWS in Oscar found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar|  
|Send to MyOSCAR (Prescriptions) | send\_medicaldata\_to\_myoscar\_action\.jsp | PrescriptionMedicationManager.sendPrescriptionsMedicationsToMyOscar | MyOscarMedicalDataManagerUtils.addMedicalData --> medicalDataWs.addMedicalData4 | MedicalDataWS.addMedicalData4 | MedicalDataWS in Oscar found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar|  
|Send to MyOSCAR (Prescriptions-linkPrescriptionToMedications) | PrescriptionMedicationManager | linkPrescriptionToMedications | MyOscarMedicalDataManagerUtils.addMedicalDataRelationship --> (MyOscarServerWebServicesManager.getMedicalDataWs) MedicalDataWs.addMedicalDataRelationship2 | MedicalDataWS.addMedicalDataRelationship2 | MedicalDataWS in Oscar found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar |  
|Send to MyOSCAR (Prescriptions-sendPrescriptionsMedicationsToMyOscar \[addRelationship\]) | PrescriptionMedicationManager | sendMedicationsToMyOscar | MyOscarMedicalDataManagerUtils.addMedicalData --> (MyOscarServerWebServicesManager.getMedicalDataWs) MedicalDataWs.addMedicalDataRelationship2 | MedicalDataWs.addMedicalDataRelationship2 | MedicalDataWS in Oscar found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar|  
|Send to MyOSCAR (Prescriptions-sendPrescriptionsToMyOscar) | PrescriptionMedicationManager | sendPrescriptionsToMyOscar | MyOscarMedicalDataManagerUtils.addMedicalData | MyOscarMedicalDataManagerUtils.addMedicalData-->medicalDataWs.addMedicalData4 | refer to linkPrescriptionToMedications
|MyOscarLoggedInInfo (getLoggedInPerson) | MyOscarLoggedInInfo | getLoggedInPerson | AccountManager.getPerson --> AccountWs.getPerson3 | AccountWs.getPerson3 | AccountWs in Oscar found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar  
|Asynchronous login into MyOSCAR | MyOscarUtils | attemptMyOscarAutoLoginIfNotAlreadyLoggedInAsynchronously | attemptMyOscarAutoLoginIfNotAlreadyLoggedIn --> AccountManager.login | AccountManager.login | Account manager in Oscar found in myoscar\_client\_utils\-2013.02\.SNAPSHOT\.jar  
|RxSendToPhrAction | execute | RxSendToPhrAction | AccountManager.getUserId | AccountManager.getUserId --> getMinimalperson --> accountWs.getMinimalPersonByUserName2 | myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar |  
|SendDocToPhrAction | SendDocToPhrAction | execute-->addOrUpdate | MedicalDataWs.addMedicalData4 |  MedicalDataWs.addMedicalData4 | MedicalDataWS found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar  
|Oscar--> MyOscar (check for messages sent from MyOSCAR) | MyOscarMessagesHelper | getReceivedMessages,getSentMessages,readMessage | MessageManager.getReceivedMessages,MessageManager.getSentMessages,MessageManager.getMessage |MessageManager | MessageManager used in Oscar via myoscar\_client\_utils\-2013\.02\-SNAPSHOT\.jar  
