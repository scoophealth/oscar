create index admission_ikey on admission(client_id,program_id,lastUpdateDate);
create index program_ikey on program(facilityId,lastUpdateDate);
create index provider_ikey on provider(lastUpdateDate);
create index secUserRole_ikey on secUserRole(lastUpdateDate);
create index casemgmt_issue_ikey on casemgmt_issue(demographic_no,update_date);
create index casemgmt_note_ikey on casemgmt_note(demographic_no,update_date,locked);
create index dxresearch_ikey on dxresearch(demographic_no,status,update_date);
create index hl7TextMessage_ikey on hl7TextMessage(created);
create index patientLabRouting_ikey on patientLabRouting(created);
create index labPatientPhysicianInfo_ikey on labPatientPhysicianInfo(lastUpdateDate);
create index mdsMSH_ikey on mdsMSH(dateTime);
create index appointment_ikey on appointment(demographic_no,updatedatetime);
create index preventions_ikey on preventions(lastUpdateDate);
create index document_ikey on document(public1,doctype,status,updatedatetime);
create index formLabReq07_ikey on formLabReq07(demographic_no,formEdited);
create index billing_on_cheader1_ikey on billing_on_cheader1(timestamp1);


