/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */


package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="property")
public class UserProperty extends AbstractModel<Integer> implements Serializable {

    public static final String MYOSCAR_ID="MyOscarId";
    public static final String STALE_NOTEDATE = "cme_note_date";
    public static final String STALE_FORMAT = "cme_note_format";
    public static final String MYDRUGREF_ID = "mydrugref_id";
    public static final String ONTARIO_MD_USERNAME = "ontario_md_username";
    public static final String ONTARIO_MD_PASSWORD = "ontario_md_password";
    public static final String CONSULTATION_TIME_PERIOD_WARNING = "consultation_time_period_warning";
    public static final String CONSULTATION_TEAM_WARNING = "consultation_team_warning";
    public static final String WORKLOAD_MANAGEMENT = "workload_management";
    public static final String CONSULTATION_REQ_PASTE_FMT = "consultation_req_paste_fmt";
    public static final String CONSULTATION_LETTERHEADNAME_DEFAULT = "consultation_letterheadname_default";
    public static final String RX_PAGE_SIZE = "rx_page_size";
    public static final String RX_DEFAULT_QUANTITY = "rx_default_quantity";
    public static final String RX_PROFILE_VIEW = "rx_profile_view";
    public static final String RX_USE_RX3 = "rx_use_rx3";
    public static final String USE_MYMEDS = "use_mymeds";
    public static final String DMFLOW_SHEET_VIEW = "DMFlowsheet_view";
    public static final String DOC_DEFAULT_QUEUE="doc_default_queue";
    public static final String HC_TYPE= "HC_Type";
    public static final String DEFAULT_SEX= "default_sex";
    public static final String EFORM_REFER_FAX = "eform_refer_fax";
    public static final String EFORM_FAVOURITE_GROUP = "favourite_eform_group";
    public static final String RX_SHOW_PATIENT_DOB="rx_show_patient_dob";
    public static final String PATIENT_NAME_LENGTH="patient_name_length";
    
    public static final String OFFICIAL_FIRST_NAME="official_first_name";
    public static final String OFFICIAL_SECOND_NAME="official_second_name";
    public static final String OFFICIAL_LAST_NAME="official_last_name";
    public static final String OFFICIAL_OLIS_IDTYPE="official_olis_idtype";
    public static final String OSCAR_MSG_RECVD="oscarMsgRecvd";
    
    
    //added to user properties with new interface
    public static final String FAX = "fax";
    public static final String SIGNATURE = "signature";
    public static final String COLOUR = "colour";
    public static final String SEX = "sex";
    public static final String SCHEDULE_START_HOUR = "schedule.start_hour";
    public static final String SCHEDULE_END_HOUR = "schedule.end_hour";
    public static final String SCHEDULE_PERIOD = "schedule.period";
    public static final String MYGROUP_NO = "mygroup_no";
    public static final String NEW_CME = "new_cme";
    public static final String ENCOUNTER_FORM_LENGTH = "encounter.form_length";
    public static final String ENCOUNTER_FORM_NAME = "encounter.form_name";
    public static final String EFORM_NAME = "encounter.eform_name";
    public static final String RX_SHOW_QR_CODE = "rx_show_qr_code";
    public static final String MYMEDS = "mymeds";
    public static final String NEW_TICKLER_WARNING_WINDOW = "new_tickler_warning_window";
    public static final String CAISI_DEFAULT_PMM = "caisi.default_pmm";
    public static final String CAISI_PREV_BILLING = "caisi.prev_billing";
    public static final String DEFAULT_BILLING_FORM = "default_billing_form";
    public static final String DEFAULT_REFERRAL_TYPE = "default_referral_type";
    public static final String DEFAULT_PAYEE = "default_payee";
    public static final String DEFAULT_DX_CODE = "default_dx_code";
    public static final String CPP_SINGLE_LINE="cpp_single_line";
    public static final String LAB_ACK_COMMENT="lab_ack_comment";
    public static final String EDOC_BROWSER_IN_MASTER_FILE="edoc_browser_in_master_file";
    public static final String EDOC_BROWSER_IN_DOCUMENT_REPORT="edoc_browser_in_document_report";
    public static final String VIEW_DOCUMENT_AS="view_document_as";
    public static final String INCOMING_DOCUMENT_DEFAULT_QUEUE="incoming_document_default_queue";
    public static final String INCOMING_DOCUMENT_ENTRY_MODE="incoming_document_entry_mode";
    public static final String DISPLAY_DOCUMENT_AS="display_document_as";
    public static final String COBALT="cobalt";
    public static final String PDF="PDF";
    public static final String IMAGE="Image";
    public static final String DOCUMENT_DESCRIPTION_TEMPLATE="document_description_template";
    public static final String CLINIC="Clinic";
    public static final String USER="User";
    public static final String UPLOAD_DOCUMENT_DESTINATION="upload_document_destination";
    public static final String INCOMINGDOCS="incomingDocs";
    public static final String PENDINGDOCS="pendingDocs";
    public static final String UPLOAD_INCOMING_DOCUMENT_FOLDER="upload_incoming_document_folder";
    public static final String HIDE_OLD_ECHART_LINK_IN_APPT="hide_old_echart_link_in_appointment";
    public static final String DISABLE_BORN_PROMPTS = "disable_born_prompts";

    
    public static final String DEFAULT_PRINTER_PDF_LABEL="default_printer_pdf_label";
    public static final String DEFAULT_PRINTER_PDF_ENVELOPE="default_printer_pdf_envelope";
    public static final String DEFAULT_PRINTER_APPOINTMENT_RECEIPT="default_printer_appointment_receipt";
    public static final String DEFAULT_PRINTER_PDF_ADDRESS_LABEL="default_printer_pdf_address_label";
    public static final String DEFAULT_PRINTER_PDF_CHART_LABEL="default_printer_pdf_chart_label";
    public static final String DEFAULT_PRINTER_CLIENT_LAB_LABEL="default_printer_client_lab_label";
    public static final String DEFAULT_PRINTER_PDF_LABEL_SILENT_PRINT="default_printer_pdf_label_silent_print";
    public static final String DEFAULT_PRINTER_PDF_ENVELOPE_SILENT_PRINT="default_printer_pdf_envelope_silent_print";
    public static final String DEFAULT_PRINTER_APPOINTMENT_RECEIPT_SILENT_PRINT="default_printer_appointment_receipt_silent_print";
    public static final String DEFAULT_PRINTER_PDF_ADDRESS_LABEL_SILENT_PRINT="default_printer_pdf_address_label_silent_print";
    public static final String DEFAULT_PRINTER_PDF_CHART_LABEL_SILENT_PRINT="default_printer_pdf_chart_label_silent_print";
    public static final String DEFAULT_PRINTER_CLIENT_LAB_LABEL_SILENT_PRINT="default_printer_client_lab_label_silent_print";

    public static final String INTEGRATOR_DEMOGRAPHIC_SYNC = "integrator_demographic_sync";
    public static final String INTEGRATOR_DEMOGRAPHIC_ISSUES = "integrator_demographic_issues";
    public static final String INTEGRATOR_DEMOGRAPHIC_CONSENT = "integrator_demographic_consent";
    public static final String INTEGRATOR_DEMOGRAPHIC_ADMISSIONS = "integrator_demographic_admissions";
    public static final String INTEGRATOR_DEMOGRAPHIC_PREVENTIONS = "integrator_demographic_preventions";
    public static final String INTEGRATOR_DEMOGRAPHIC_NOTES = "integrator_demographic_notes";
    public static final String INTEGRATOR_DEMOGRAPHIC_DRUGS = "integrator_demographic_drugs";
    public static final String INTEGRATOR_DEMOGRAPHIC_APPOINTMENTS = "integrator_demographic_appointments";
    public static final String INTEGRATOR_DEMOGRAPHIC_DXRESEARCH = "integrator_demographic_dxresearch";
    public static final String INTEGRATOR_DEMOGRAPHIC_BILLING = "integrator_demographic_billing";
    public static final String INTEGRATOR_DEMOGRAPHIC_EFORMS = "integrator_demographic_eforms";
    public static final String INTEGRATOR_DEMOGRAPHIC_MEASUREMENTS = "integrator_demographic_measurements";
    public static final String INTEGRATOR_DEMOGRAPHIC_DOCUMENTS = "integrator_demographic_documents";
    public static final String INTEGRATOR_DEMOGRAPHIC_ALLERGIES = "integrator_demographic_allergies";
    public static final String INTEGRATOR_DEMOGRAPHIC_LABREQ = "integrator_demographic_labreq";
    public static final String INTEGRATOR_PROGRAMS = "integrator_programs_sync";
    public static final String INTEGRATOR_PROVIDERS = "integrator_providers_sync";
    public static final String INTEGRATOR_FACILITY = "integrator_facility_sync";
    public static final String INTEGRATOR_FULL_PUSH = "integrator_full_push";
    public static final String INTEGRATOR_LAST_PUSH = "integrator_last_push";
    public static final String INTEGRATOR_LAST_UPDATED = "integrator_last_updated";
	public static final String INTEGRATOR_LAST_PULL_PRIMARY_EMR = "integrator_last_pull";
	public static final String INTEGRATOR_PATIENT_CONSENT = "integrator_patient_consent";	
	public static final String STUDENT_PARTICIPATION_CONSENT = "student_participation_consent";	
	public static final String PROVIDER_FOR_TICKLER_WARNING = "provider_for_tickler_warning";

	public static final String MCEDT_ACCOUNT_PASSWORD = "mcedt_account_password";
	public static final String TICKLER_EMAIL_PROVIDER = "tickler_email_provider";

	public static final String CLINICALCONNECT_ID = "clinicalconnect_username";
	public static final String CLINICALCONNECT_TYPE = "clinicalconnect_authentication_type";
	public static final String CLINICALCONNECT_SERVICE_USERNAME = "clinicalconnect_service_username";
	public static final String CLINICALCONNECT_SERVICE_PASSWORD = "clinicalconnect_service_password";
	public static final String CLINICALCONNECT_SERVICE_LOCATION = "clinicalconnect_service_location";

	public static final String DASHBOARD_SHARE = "dashboard_share";
	
	public static final String CODE_TO_ADD_PATIENTDX = "code_to_add_patientDx";
	public static final String CODE_TO_MATCH_PATIENTDX = "code_to_match_patientDx";
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String value;
    @Column(name="provider_no")
    private String providerNo;
    @Transient
    private String[] valueArray;
    @Transient
    private boolean checked;

    public boolean isChecked(){

        return this.checked;
    }
    public void setChecked(boolean checked){

        this.checked=checked;
    }

    public String[] getValueArray(){
        return this.valueArray;
    }

    public void setValueArray(String[] va){
       this.valueArray=va;
    }
    public String getProviderNo() {
        return this.providerNo;
    }

    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    /** Creates a new instance of UserProperty */
    public UserProperty() {
    }

}
