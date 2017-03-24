#!/bin/sh

#
# Utility to export the patient data from one MRP into a new database.
#

MYSQL_PASS='oscardbpassword'
DB_SOURCE=oscar_mcmaster

# The destination DB name must be oscar_tmp. Don't change it.
DB_DEST=oscar_tmp

# Tranfer data for only one provider each time
PROVIDER_NO='999998'

DOCUMENT_DIR=/var/lib/OscarDocument/oscar_mcmaster/document

EXPORT_FROM='1900-01-01';

echo "## Get Demographic list"
DEMO_LIST=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select demographic_no from demographic where  provider_no in ($PROVIDER_NO) " | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "DEMO_LIST $DEMO_LIST"

echo "#################### demographic info #################" $(date)
mysql -uroot -p$MYSQL_PASS -e "create database $DB_DEST;"
echo "create database $DB_DEST= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create table $DB_DEST.demographic select * from $DB_SOURCE.demographic where $DB_SOURCE.demographic.provider_no in ($PROVIDER_NO) and lastUpdateDate > $EXPORT_FROM ;"
echo "create table $DB_DEST.demographic= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index demo_no on $DB_DEST.demographic(demographic_no);"
echo "create index demo_no on $DB_DEST.demographic= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index demoext_demo_no on $DB_SOURCE.demographicExt(demographic_no);"
echo "create index demoext_demo_no on= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index democust_demo_no on $DB_SOURCE.demographiccust(demographic_no);"
echo "create index democust_demo_no = $?" $(date)
echo "create table $DB_DEST.demographicExt select * from $DB_SOURCE.demographicExt where $DB_SOURCE.demographicExt.demographic_no in ($DEMO_LIST) ;" > demoExt.sql
mysql -uroot -p$MYSQL_PASS < demoExt.sql
#-e "create table $DB_DEST.demographicExt select * from $DB_SOURCE.demographicExt where $DB_SOURCE.demographicExt.demographic_no in ($DEMO_LIST) ;"
echo "create table $DB_DEST.demographicExt= $?" $(date)
echo "create table $DB_DEST.demographiccust select * from $DB_SOURCE.demographiccust where $DB_SOURCE.demographiccust.demographic_no in ($DEMO_LIST) ;" > demoCust.sql
mysql -uroot -p$MYSQL_PASS < demoCust.sql
echo "create table $DB_DEST.demographiccust = $?" $(date)
echo "create table $DB_DEST.relationships select * from $DB_SOURCE.relationships where $DB_SOURCE.relationships.demographic_no in ($DEMO_LIST) ;" > relationships.sql
mysql -uroot -p$MYSQL_PASS < relationships.sql
echo "create table $DB_DEST.relationships= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index demo_provider_no on $DB_DEST.demographic(provider_no);"
echo "create index demo_provider_no on $DB_DEST.demographic= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index demoExt_provider_no on $DB_DEST.demographicExt(provider_no);"
echo "create index demoExt_provider_no= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index relationships_provider_no on $DB_DEST.relationships(creator);"
echo "create index relationships_provider_no= $?" $(date)

echo "#################### billing info ##################" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index billing1_demo_no on $DB_SOURCE.billing_on_cheader1(demographic_no);"
echo "create index billing1_demo_no= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index billing2_ch1 on $DB_SOURCE.billing_on_cheader2(ch1_id);"
echo "create index billing2_ch1= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index billingitem_ch1 on $DB_SOURCE.billing_on_item(ch1_id);"
echo "create index billingitem_ch1= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index billingext_demo_no on $DB_SOURCE.billing_on_ext(demographic_no);"
echo "create index billingext_demo_no= $?" $(date)
echo "create table $DB_DEST.billing_on_cheader1 select * from $DB_SOURCE.billing_on_cheader1 where $DB_SOURCE.billing_on_cheader1.demographic_no in ($DEMO_LIST) and timestamp1 > $EXPORT_FROM ;" > billchead1.sql
mysql -uroot -p$MYSQL_PASS < billchead1.sql
echo "create table $DB_DEST.billing_on_cheader1= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index billing1_ch1 on $DB_DEST.billing_on_cheader1(id);"
echo "create index billing1_ch1 on $DB_DEST.billing_on_cheader1= $?" $(date)

IDS_billing_on_cheader1=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select id from $DB_DEST.billing_on_cheader1 " | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "IDS_billing_on_cheader1= $?" $(date)
echo "create table $DB_DEST.billing_on_cheader2 select * from $DB_SOURCE.billing_on_cheader2 where $DB_SOURCE.billing_on_cheader2.ch1_id in ($IDS_billing_on_cheader1) ;" > billingHeader2Copy.sql
mysql -uroot -p$MYSQL_PASS < billingHeader2Copy.sql
echo "create table $DB_DEST.billing_on_cheader2= $?" $(date)
#echo "$IDS_billing_on_cheader1"
echo "create table $DB_DEST.billing_on_item select * from $DB_SOURCE.billing_on_item where $DB_SOURCE.billing_on_item.ch1_id in ($IDS_billing_on_cheader1) ;" > billingItemCopy.sql
mysql -uroot -p$MYSQL_PASS < billingItemCopy.sql
echo "create table $DB_DEST.billing_on_item = $?" $(date)
echo "create table $DB_DEST.billing_on_ext select * from $DB_SOURCE.billing_on_ext where $DB_SOURCE.billing_on_ext.demographic_no in ($DEMO_LIST) ;" > billingExt.sql
mysql -uroot -p$MYSQL_PASS < billingExt.sql
echo "create table $DB_DEST.billing_on_ext= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index billing_on_cheader1_provider_no on $DB_DEST.billing_on_cheader1(provider_no);"
echo "create index billing_on_cheader1_provider_no= $?" $(date)


echo "################### document info ##################" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index doc_demo_no on $DB_SOURCE.ctl_document(module_id);"
echo "create index doc_demo_no= $?" $(date)
echo "create table $DB_DEST.ctl_document (select * from $DB_SOURCE.ctl_document where $DB_SOURCE.ctl_document.module_id in ($DEMO_LIST)) ;" > ctlDocument.sql
mysql -uroot -p$MYSQL_PASS < ctlDocument.sql
echo "create table $DB_DEST.ctl_document = $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index ctl_doc_no on $DB_DEST.ctl_document(document_no);"
echo "create index ctl_doc_no= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index doc_no on $DB_SOURCE.document(document_no);"
echo "create index doc_no on $DB_SOURCE.document= $?" $(date)

IDS_ctl_document=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select document_no from $DB_DEST.ctl_document " | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "IDS_ctl_document= $?" $(date)
echo "create table $DB_DEST.document select * from $DB_SOURCE.document where $DB_SOURCE.document.document_no in ($IDS_ctl_document) and updatedatetime > $EXPORT_FROM  ;" > documentCopy.sql
mysql -uroot -p$MYSQL_PASS < documentCopy.sql
echo "create table $DB_DEST.document select= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index document_provider_id on $DB_DEST.document(doccreator);"
echo "create index document_provider_id = $?" $(date)

echo "################### note info ###################" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index note_demo_no on $DB_SOURCE.casemgmt_note(demographic_no);"
echo "create index note_demo_no= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index cpp_demo_no on $DB_SOURCE.casemgmt_cpp(demographic_no);"
echo "create index cpp_demo_no= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index issue_demo_no on $DB_SOURCE.casemgmt_issue(demographic_no);"
echo "create index issue_demo_no= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index adm_demo_no on $DB_SOURCE.admission(client_id);"
echo "create index adm_demo_no= $?" $(date)
echo "create table $DB_DEST.casemgmt_note select * from $DB_SOURCE.casemgmt_note where $DB_SOURCE.casemgmt_note.demographic_no in ($DEMO_LIST) and update_date > $EXPORT_FROM  ;" > casemgntNote.sql
mysql -uroot -p$MYSQL_PASS < casemgntNote.sql
echo "create table $DB_DEST.casemgmt_note = $?" $(date)
echo "create table $DB_DEST.casemgmt_cpp select * from $DB_SOURCE.casemgmt_cpp where $DB_SOURCE.casemgmt_cpp.demographic_no in ($DEMO_LIST) and update_date > $EXPORT_FROM ;" > casemgntCpp.sql
mysql -uroot -p$MYSQL_PASS < casemgntCpp.sql
echo "create table $DB_DEST.casemgmt_cpp= $?" $(date)
echo "create table $DB_DEST.casemgmt_issue select * from $DB_SOURCE.casemgmt_issue where $DB_SOURCE.casemgmt_issue.demographic_no in ($DEMO_LIST) and update_date > $EXPORT_FROM ;" > casemgmtIssue.sql
mysql -uroot -p$MYSQL_PASS  < casemgmtIssue.sql
echo "create table $DB_DEST.casemgmt_issue = $?" $(date)

IDS_casemgmt_issue=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select issue_id from $DB_DEST.casemgmt_issue " | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "IDS_casemgmt_issue= $?" $(date)

echo "create table $DB_DEST.issue select * from $DB_SOURCE.issue where $DB_SOURCE.issue.issue_id in ($IDS_casemgmt_issue) ;" > issue.sql
mysql -uroot -p$MYSQL_PASS < issue.sql
echo "create table $DB_DEST.issue = $?" $(date)

IDS_casemgmt_note=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select note_id from $DB_DEST.casemgmt_note " | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "IDS_casemgmt_note= $?" $(date)
echo "create table $DB_DEST.casemgmt_note_link select * from $DB_SOURCE.casemgmt_note_link where $DB_SOURCE.casemgmt_note_link.note_id in ($IDS_casemgmt_note) ;" > casemgmtLinkCopy.sql
mysql -uroot -p$MYSQL_PASS <  casemgmtLinkCopy.sql
echo "create table $DB_DEST.casemgmt_note_link = $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index note_id on $DB_DEST.casemgmt_note(note_id);"
echo "create index note_id on $DB_DEST.casemgmt_note= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index note_id on $DB_SOURCE.casemgmt_issue_notes(note_id);"
echo "create index note_id on $DB_SOURCE.casemgmt_issue_notes= $?" $(date)

echo "create table $DB_DEST.casemgmt_issue_notes select * from $DB_SOURCE.casemgmt_issue_notes where $DB_SOURCE.casemgmt_issue_notes.note_id in ($IDS_casemgmt_note) ;" > casemgmtIssueCopy.sql
mysql --max_allowed_packet=1GM -uroot -p$MYSQL_PASS < casemgmtIssueCopy.sql
echo "create table $DB_DEST.casemgmt_issue_notes = $?" $(date)
echo "create table $DB_DEST.admission select * from $DB_SOURCE.admission where $DB_SOURCE.admission.client_id in ($DEMO_LIST) and lastUpdateDate > $EXPORT_FROM;" > admission.sql
mysql -uroot -p$MYSQL_PASS < admission.sql
echo "create table $DB_DEST.admission select = $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index casemgmt_note_provider_id on $DB_DEST.casemgmt_note(provider_no);"
echo "create index casemgmt_note_provider_id on $DB_DEST.casemgmt_note= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index casemgmt_cpp_provider_id on $DB_DEST.casemgmt_cpp(provider_no);"
echo "create index casemgmt_cpp_provider_id= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index admission_provider_id on $DB_DEST.admission(provider_no);"
echo "create index admission_provider_id= $?" $(date)

echo "################## eform info ##################" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index eform_demo_no on $DB_SOURCE.eform_data(demographic_no);"
echo "create index eform_demo_no= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index eformVal_demo_no on $DB_SOURCE.eform_values(demographic_no);"
echo "create index eformVal_demo_no= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create table $DB_DEST.eform select * from $DB_SOURCE.eform;"
echo "create table $DB_DEST.eform = $?" $(date)
echo "create table $DB_DEST.eform_data select * from $DB_SOURCE.eform_data where $DB_SOURCE.eform_data.demographic_no in ($DEMO_LIST) and form_date > $EXPORT_FROM ;" > eform_data.sql
mysql -uroot -p$MYSQL_PASS < eform_data.sql
echo "create table $DB_DEST.eform_data = $?" $(date)
echo "create table $DB_DEST.eform_values select * from $DB_SOURCE.eform_values where $DB_SOURCE.eform_values.demographic_no in ($DEMO_LIST) ;" >eform_values.sql
mysql -uroot -p$MYSQL_PASS < eform_values.sql
echo "create table $DB_DEST.eform_values= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index eform_provider_no on $DB_DEST.eform_data(form_provider);"
echo "create index eform_provider_no= $?" $(date)

echo "################## form info ###################" $(date)



mysql  -uroot -p$MYSQL_PASS $DB_SOURCE    --skip-column-names --vertical -e "show tables like 'form%';" | grep -v "row *****" > forms

while read file ; do
mysql -uroot -p$MYSQL_PASS -e "create index form_demo_no on $DB_SOURCE.$file(demographic_no);"
echo "create index form_demo_no on $DB_SOURCE.$file= $?"
echo "create table $DB_DEST.$file ENGINE=MyISAM select * from $DB_SOURCE.$file where $DB_SOURCE.$file.demographic_no in ($DEMO_LIST) and formEdited > $EXPORT_FROM;" > $file.sql
mysql -uroot -p$MYSQL_PASS  < $file.sql
echo "create table $DB_DEST.$file select = $?"
done < forms

#echo "## handling formRourke2009 ##"
#mysql -uroot -p$MYSQL_PASS  -e  "create table $DB_DEST.formRourke2009 ENGINE=MyISAM select * from $DB_SOURCE.formRourke2009 where $DB_SOURCE.formRourke2009.demographic_no in ($DEMO_LIST);"


IDS_formONARRECORD=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select ID from $DB_DEST.formONAREnhancedRecord " | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "create table $DB_DEST.formONAREnhancedRecordExt1 select * from $DB_SOURCE.formONAREnhancedRecordExt1 where $DB_SOURCE.formONAREnhancedRecordExt1.ID in ($IDS_formONARRECORD) ;" > onare1.sql
mysql -uroot -p$MYSQL_PASS < onare1.sql
echo "create table $DB_DEST.formONAREnhancedRecordExt2 select * from $DB_SOURCE.formONAREnhancedRecordExt2 where $DB_SOURCE.formONAREnhancedRecordExt2.ID in ($IDS_formONARRECORD) ;" > onare2.sql
mysql -uroot -p$MYSQL_PASS < onare2.sql


echo "################## allergy & drug info #################" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index allergy_demo_no on $DB_SOURCE.allergies(demographic_no);"
echo "create index allergy_demo_no= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index drug_demo_no on $DB_SOURCE.drugs(demographic_no);"
echo "create index drug_demo_no= $?" $(date)
echo "create table $DB_DEST.allergies select * from $DB_SOURCE.allergies where $DB_SOURCE.allergies.demographic_no in ($DEMO_LIST) and lastUpdateDate > $EXPORT_FROM ;" > allergies.sql
mysql -uroot -p$MYSQL_PASS < allergies.sql
echo "create table $DB_DEST.allergies= $?" $(date)
echo "create table $DB_DEST.drugs select * from $DB_SOURCE.drugs where $DB_SOURCE.drugs.demographic_no in ($DEMO_LIST) and lastUpdateDate > $EXPORT_FROM ;"  > drugs.sql
mysql -uroot -p$MYSQL_PASS < drugs.sql
echo "create table $DB_DEST.drugs= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index allergeis_provider_no on $DB_DEST.allergies(providerNo);"
echo "create index allergeis_provider_no= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index drugs_provider_no on $DB_DEST.drugs(provider_no);"
echo "create index drugs_provider_no = $?" $(date)

echo "################## disease reg info ################" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index dx_demo_no on $DB_SOURCE.dxresearch(demographic_no);"
echo "create index dx_demo_no= $?" $(date)
echo "create table $DB_DEST.dxresearch select * from $DB_SOURCE.dxresearch where $DB_SOURCE.dxresearch.demographic_no in ($DEMO_LIST) and update_date > $EXPORT_FROM ;" > dxresearch.sql
mysql -uroot -p$MYSQL_PASS < dxresearch.sql
echo "create table $DB_DEST.dxresearch= $?" $(date)

echo "################## prevention info ################" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index prev_demo_no on $DB_SOURCE.preventions(demographic_no);"
echo "create index prev_demo_no= $?" $(date)
echo "create table $DB_DEST.preventions select * from $DB_SOURCE.preventions where $DB_SOURCE.preventions.demographic_no in ($DEMO_LIST) and lastUpdateDate > $EXPORT_FROM ;" > prevs.sql
mysql -uroot -p$MYSQL_PASS  < prevs.sql
echo "create table $DB_DEST.preventions= $?" $(date)

IDS_preventions=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select id from $DB_DEST.preventions " | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "IDS_preventions= $?" $(date)
echo "create table $DB_DEST.preventionsExt select * from $DB_SOURCE.preventionsExt where $DB_SOURCE.preventionsExt.prevention_id in ($IDS_preventions) ;" > preventionExtCopy.sql
mysql -uroot -p$MYSQL_PASS   <  preventionExtCopy.sql
echo "create table $DB_DEST.preventionsExt= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index preventions_provider_no on $DB_DEST.preventions(provider_no);"
echo "create index preventions_provider_no= $?" $(date)

echo "################## consultation info ################" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index con_demo_no on $DB_SOURCE.consultationRequests(demographicNo);"
echo "create index con_demo_no= $?" $(date)
echo "create table $DB_DEST.consultationRequests select * from $DB_SOURCE.consultationRequests where $DB_SOURCE.consultationRequests.demographicNo in ($DEMO_LIST) ;" > conReq.sql
mysql -uroot -p$MYSQL_PASS < conReq.sql
echo "create table $DB_DEST.consultationRequests= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create table $DB_DEST.professionalSpecialists select * from $DB_SOURCE.professionalSpecialists;"
echo "create table $DB_DEST.professionalSpecialists= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create table $DB_DEST.serviceSpecialists select * from $DB_SOURCE.serviceSpecialists;"
echo "create table $DB_DEST.serviceSpecialists = $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create table $DB_DEST.consultationServices select * from $DB_SOURCE.consultationServices;"
echo "create table $DB_DEST.consultationServices= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index con_provider_no on $DB_DEST.consultationRequests(providerNo);"
echo "create index con_provider_no= $?" $(date)

echo "################## measurement info #################" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index meas_demo_no on $DB_SOURCE.measurements(demographicNo);"
echo "create index meas_demo_no= $?" $(date)
echo "create table $DB_DEST.measurements select * from $DB_SOURCE.measurements where $DB_SOURCE.measurements.demographicNo in ($DEMO_LIST) and dateEntered > $EXPORT_FROM  ;" > measurements.sql
mysql -uroot -p$MYSQL_PASS < measurements.sql
echo "create table $DB_DEST.measurements= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index measurement_provider_no on $DB_DEST.measurements(providerNo);"
echo "create index measurement_provider_no= $?" $(date)

IDS_measurements=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select id from $DB_DEST.measurements" | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "IDS_measurements= $?" $(date)

echo "create table $DB_DEST.measurementsExt select * from $DB_SOURCE.measurementsExt where $DB_SOURCE.measurementsExt.measurement_id in ($IDS_measurements) ;" > measurementExtCopy.sql
mysql -uroot -p$MYSQL_PASS < measurementExtCopy.sql
echo "create table $DB_DEST.measurementsExt = $?" $(date)

echo "################## lab info #################" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index lab_demo_no on $DB_SOURCE.patientLabRouting(demographic_no);"
echo "create index lab_demo_no= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index hl7TextMsg_lab_id on $DB_SOURCE.hl7TextMessage(lab_id);"
echo "create index hl7TextMsg_lab_id= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index ptLabRout_lab_id on $DB_SOURCE.patientLabRouting(lab_no);"
echo "create index ptLabRout_lab_id= $?" $(date)
echo "create table $DB_DEST.patientLabRouting select * from $DB_SOURCE.patientLabRouting where $DB_SOURCE.patientLabRouting.demographic_no in ($DEMO_LIST) ;" > patientLabRouting.sql
mysql -uroot -p$MYSQL_PASS < patientLabRouting.sql
echo "create table $DB_DEST.patientLabRouting= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index lab_no on $DB_DEST.patientLabRouting(lab_no);"
echo "create index lab_no on $DB_DEST.patientLabRouting= $?" $(date)

IDS_patientLabRouting=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select lab_no from $DB_DEST.patientLabRouting" | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "IDS_patientLabRouting= $?" $(date)
echo  "create table $DB_DEST.hl7TextMessage select * from $DB_SOURCE.hl7TextMessage where $DB_SOURCE.hl7TextMessage.lab_id in ($IDS_patientLabRouting) ;" > hl7TextMessageCopy.sql
mysql -uroot -p$MYSQL_PASS < hl7TextMessageCopy.sql
echo "create table $DB_DEST.hl7TextMessage= $?" $(date)

echo "create table $DB_DEST.hl7TextInfo select * from $DB_SOURCE.hl7TextInfo where $DB_SOURCE.hl7TextInfo.lab_no in ($IDS_patientLabRouting) ;" > hl7TextInfoCopy.sql
mysql -uroot -p$MYSQL_PASS < hl7TextInfoCopy.sql
echo "create table $DB_DEST.hl7TextInfo = $?" $(date)


echo "################### appointment info ###############" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index appt_demo_no on $DB_SOURCE.appointment(demographic_no);"
echo "create index appt_demo_no= $?" $(date)
echo "create table $DB_DEST.appointment select * from $DB_SOURCE.appointment where $DB_SOURCE.appointment.demographic_no in ($DEMO_LIST) and updatedatetime > $EXPORT_FROM ;" > appts.sql
mysql -uroot -p$MYSQL_PASS < appts.sql
echo "create table $DB_DEST.appointment= $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index appointment_provider_no on $DB_DEST.appointment(provider_no);"
echo "create index appointment_provider_no= $?" $(date)

echo "#################### msg info ##################" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index msg_demo_no on $DB_SOURCE.msgDemoMap(demographic_no);"
echo "create index msg_demo_no= $?" $(date)

mysql -uroot -p$MYSQL_PASS -e "create index msgtbl_msg_id on $DB_SOURCE.messagetbl(messageid);"
echo "create index msgtbl_msg_id= $?" $(date)
echo "create table $DB_DEST.msgDemoMap select * from $DB_SOURCE.msgDemoMap where $DB_SOURCE.msgDemoMap.demographic_no in ($DEMO_LIST) ;" > msgDemoMap.sql
mysql -uroot -p$MYSQL_PASS  < msgDemoMap.sql
echo "create table $DB_DEST.msgDemoMap = $?" $(date)
mysql -uroot -p$MYSQL_PASS -e "create index msgDemoMap_msg_id on $DB_DEST.msgDemoMap(messageID);"
echo "create index msgDemoMap_msg_id= $?" $(date)

IDS_msgDemoMap=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select messageID from $DB_DEST.msgDemoMap " | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "IDS_msgDemoMap= $?" $(date)
echo "create table $DB_DEST.messagetbl select * from $DB_SOURCE.messagetbl where $DB_SOURCE.messagetbl.messageid in ($IDS_msgDemoMap) and thedate > $EXPORT_FROM ;" > messageTblCopy.sql
mysql -uroot -p$MYSQL_PASS < messageTblCopy.sql
echo "create table $DB_DEST.messagetbl = $?" $(date)
###NEW

echo "create table $DB_DEST.immunizations select * from $DB_SOURCE.immunizations where $DB_SOURCE.immunizations.demographic_no in ($DEMO_LIST) ;" > immunz.sql
mysql -uroot -p$MYSQL_PASS < immunz.sql
echo "create table $DB_DEST.immunizations= $?" $(date)
echo "create table $DB_DEST.providerLabRouting select * from $DB_SOURCE.providerLabRouting where $DB_SOURCE.providerLabRouting.provider_no in ($PROVIDER_NO) and timestamp > $EXPORT_FROM ;" > providerLabRouting.sql
mysql -uroot -p$MYSQL_PASS < providerLabRouting.sql
echo "create table $DB_DEST.providerLabRouting= $?" $(date)
echo "create table $DB_DEST.labReportInformation select * from $DB_SOURCE.labReportInformation where $DB_SOURCE.labReportInformation.id in (select $DB_SOURCE.patientLabRouting.lab_no from $DB_SOURCE.patientLabRouting where $DB_SOURCE.patientLabRouting.lab_type = 'CML' and $DB_SOURCE.patientLabRouting.demographic_no in ($DEMO_LIST));" > labReportInfo.sql
mysql -uroot -p$MYSQL_PASS < labReportInfo.sql
echo "create table $DB_DEST.labReportInformation = $?" $(date)

IDS_labReportInformation=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select id from $DB_DEST.labReportInformation " | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "IDS_labReportInformation= $?" $(date)

echo "create table $DB_DEST.labPatientPhysicianInfo select * from $DB_SOURCE.labPatientPhysicianInfo where $DB_SOURCE.labPatientPhysicianInfo.labReportInfo_id in ($IDS_labReportInformation) and lastUpdateDate > $EXPORT_FROM ;" > labPatientPhysician.sql
mysql -uroot -p$MYSQL_PASS < labPatientPhysician.sql
echo "create table $DB_DEST.labPatientPhysicianInfo = $?" $(date)

IDS_labPatientPhysicianInfo=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select id from $DB_DEST.labPatientPhysicianInfo " | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "IDS_labPatientPhysicianInfo= $?" $(date)


echo "create table $DB_DEST.labTestResults select * from $DB_SOURCE.labTestResults where $DB_SOURCE.labTestResults.labPatientPhysicianInfo_id in ( $IDS_labPatientPhysicianInfo);" > labTestResults.sql
mysql -uroot -p$MYSQL_PASS < labTestResults.sql
echo "create table $DB_DEST.labTestResults= $?" $(date)

echo "################### tickler ########################" $(date)

echo "create table $DB_DEST.tickler select * from $DB_SOURCE.tickler where $DB_SOURCE.tickler.demographic_no in ($DEMO_LIST) and update_date > $EXPORT_FROM ;" > tickler.sql
mysql -uroot -p$MYSQL_PASS < tickler.sql
echo "create table $DB_DEST.tickler = $?" $(date)

IDS_tickler_no=$(mysql  -uroot -p$MYSQL_PASS $DB_SOURCE --skip-column-names --vertical -e "select tickler_no from $DB_DEST.tickler " | grep -v row |  tr "\\n" "," | sed -e 's/,$//' )
echo "IDS tickler $?" $(date)

echo "create table $DB_DEST.tickler_comments select * from $DB_SOURCE.tickler_comments where $DB_SOURCE.tickler_comments.tickler_no in ($IDS_tickler_no) ;" > tickler_comments.sql
mysql -uroot -p$MYSQL_PASS < tickler_comments.sql
echo "create table $DB_DEST.tickler_comments = $?" $(date)
echo "create table $DB_DEST.tickler_link select * from $DB_SOURCE.tickler_link where $DB_SOURCE.tickler_link.tickler_no in ($IDS_tickler_no) ;" > tickler_link.sql
mysql -uroot -p$MYSQL_PASS < tickler_link.sql
echo "create table $DB_DEST.tickler_link= $?" $(date)
echo "create table $DB_DEST.tickler_update select * from $DB_SOURCE.tickler_update where $DB_SOURCE.tickler_update.tickler_no in ($IDS_tickler_no) ;" > tickler_update.sql
mysql -uroot -p$MYSQL_PASS < tickler_update.sql
echo "create table $DB_DEST.tickler_update= $?" $(date)

echo "############# CREATE PROVIDER LIST ######################"

mysql -uroot -p$MYSQL_PASS -e "create table $DB_DEST.provider select provider_no, last_name,first_name from $DB_SOURCE.provider where lastUpdateDate > $EXPORT_FROM ;"

echo "#################### Collect Document files##############" $(date)


mysql -u root -p$MYSQL_PASS $DB_DEST --skip-column-names --vertical -e "select docfilename from $DB_DEST.document;" | grep -v row > DocList
echo "select docfilename from document= $?" $(date)


mkdir copy_document_provider

while read file ; do
cp "$DOCUMENT_DIR/$file"  copy_document_provider ; done < DocList



echo "############# END OF FILE ############### " $(date)




