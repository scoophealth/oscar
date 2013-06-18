# Integration Points in Oscar to MyOSCAR

### Purpose

The purpose of this document is to identify integration points in Oscar where Oscar communicates with MyOscar.
As Oscar evolves, it is necessary to always know and keep track of such integration points in order to maintain, update, and/or improve the quality of code.

### Background

Communication between Oscar and MyOSCAR is 1 way only.  Communication is always initiated from Oscar.  MyOSCAR is not aware of Oscar and services requests only.
Communication happens via web services.

### Current Integration Points

The following are a list of integration points listed below.

| Action | Class | Oscar Web Service | MyOscar Web Service | Further details |
|:------:|:-----:|:--------------:|:-----------------:|:------------------ :|:---------------:|
|PHR User Registration | PHRUserManagementAction |  org.oscarehr.myoscar.client.ws_manager.AccountManager.createPerson | accountManager.addPerson(personToCreate) | [Refer to PHRUserRegistration.html](PHRUserRegistration.html)|  
|PHR User Registration | PHRUserManagementAction | AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(myOscarLoggedInInfo) |MyOscarServerWebServicesManager.getAccountWs(MyOscarServerCredentials credentials) |[Refer to PHRUserRegistration.html](PHRUserRegistration.html)  
|Send Allergy to MyOSCAR (ADD) | AllergiesManager | addMedicalData --> MyOscarServerWebServicesManager.getMedicalDataWs(myOscarLoggedInInfo); | MyOscarServerWebServicesManager.getMedicalDataWs |[Refer to AddAllergy.html](AddAllergy.html)   
|Send Allergy to MyOSCAR (UPDATE) | AllergiesManager |  updateMedicalData --> medicalDataWs.updateMedicalData5 |project: MedicalDataWs.updateMedicalData5 | [Refer to AddAllergy.html](AddAllergy.html)   
|Send patient a message | PHRMessageAction | MessageManager.sendMessage --> MyOscarServerWebServicesManager.getMessageWs | MessageReceivedEmailNotificationListener | [Refer to SendPatientMessage.html](SendPatientMessage.html)|  
|Send to MyOSCAR (Immunizations) | MyOscarMedicalDataManagerUtils |  MyOscarMedicalDataManagerUtils.addMedicalData | MedicalDataWS.addMedicalData4 | [Refer to SendImmunization.html](SendImmunization.html)|  
|Send to MyOSCAR (Measurements) | MyOscarMedicalDataManagerUtils |  MyOscarMedicalDataManagerUtils.addMedicalData | MedicalDataWS.addMedicalData4 | [Refer to AddMeasurements.html](AddMeasurements.html)|  
|Send to MyOSCAR (sendHeightWeight) | MyOscarMedicalDataManagerUtils |  MyOscarMedicalDataManagerUtils.addMedicalData  | MedicalDataWS.addMedicalData4 | [Refer to AddMeasurements.html](AddMeasurements.html)|  
|Send to MyOSCAR (sendBloodPressure) | MyOscarMedicalDataManagerUtils | MyOscarMedicalDataManagerUtils.addMedicalData | MedicalDataWS.addMedicalData4 | [Refer to AddMeasurements.html](AddMeasurements.html)|  
|Send to MyOSCAR (sendA1C) | MyOscarMedicalDataManagerUtils |  MyOscarMedicalDataManagerUtils.addMedicalData  | MedicalDataWS.addMedicalData4 | [Refer to AddMeasurements.html](AddMeasurements.html)|  
|Send to MyOSCAR (sendOtherHealthTracker) |MyOscarMedicalDataManagerUtils  | MyOscarMedicalDataManagerUtils.addMedicalData  | MedicalDataWS.addMedicalData4 | [Refer to AddMeasurements.html](AddMeasurements.html) |  
|Send to MyOSCAR (ensureOHTCategoryExists) | MyOscarMedicalDataManagerUtils | MyOscarMedicalDataManagerUtils.addMedicalData  | MedicalDataWS.addMedicalData4 | [Refer to AddMeasurements.html](AddMeasurements.html)|  
|Send to MyOSCAR (Prescriptions) |PrescriptionMedicationManager |  MyOscarMedicalDataManagerUtils.addMedicalData | MedicalDataWS.addMedicalData4 |[Refer to AddPrescriptions.html](AddPrescriptions.html)|  
|Send to MyOSCAR (Link Prescriptions To Medications) | PrescriptionMedicationManager | MyOscarMedicalDataManagerUtils.addMedicalDataRelationship | MedicalDataWS.addMedicalDataRelationship2 | [Refer to AddPrescriptions.html](AddPrescriptions.html) |  
|MyOscarLoggedInInfo (getLoggedInPerson) | MyOscarLoggedInInfo |  AccountManager.getPerson | AccountWs.getPerson3 | [Refer to GetLoggedInPerson.html](GetLoggedInPerson.html)
|Asynchronous login into MyOSCAR | MyOscarUtils |  attemptMyOscarAutoLoginIfNotAlreadyLoggedIn | AccountManager.login | [Refer to AutoLogin.html](AutoLogin.html) 
|Messages (check for messages sent from MyOSCAR) | MyOscarMessagesHelper |  MessageManager |MessageManager | [Refer to CheckMsgsSentFromMyOscar.html](CheckMsgsSentFromMyOscar.html) 
|RxSendToPhrAction | RxSendToPhrAction |  AccountManager | AccountManager.getUserId --> getMinimalperson --> accountWs.getMinimalPersonByUserName2 | myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar |  
|SendDocToPhrAction | SendDocToPhrAction | MedicalDataWs.addMedicalData4 |  MedicalDataWs.addMedicalData4 | MedicalDataWS found in myoscar\_server\_client\_stubs\-2013\.02\.SNAPSHOT\.jar  

