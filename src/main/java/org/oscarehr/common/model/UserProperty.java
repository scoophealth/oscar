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

/**
 *
 * @author rjonasz
 */
@Entity
@Table(name="property")
public class UserProperty extends AbstractModel<Integer> implements Serializable {

    @Transient public final static String MYOSCAR_ID="MyOscarId";
    @Transient public final static String STALE_NOTEDATE = "cme_note_date";
    @Transient public final static String STALE_FORMAT = "cme_note_format";
    @Transient public final static String MYDRUGREF_ID = "mydrugref_id";
    @Transient public final static String ONTARIO_MD_USERNAME = "ontario_md_username";
    @Transient public final static String ONTARIO_MD_PASSWORD = "ontario_md_password";
    @Transient public final static String CONSULTATION_TIME_PERIOD_WARNING = "consultation_time_period_warning";
    @Transient public final static String CONSULTATION_TEAM_WARNING = "consultation_team_warning";
    @Transient public final static String WORKLOAD_MANAGEMENT = "workload_management";
    @Transient public final static String CONSULTATION_REQ_PASTE_FMT = "consultation_req_paste_fmt";
    @Transient public final static String RX_PAGE_SIZE = "rx_page_size";
    @Transient public final static String RX_DEFAULT_QUANTITY = "rx_default_quantity";
    @Transient public final static String RX_PROFILE_VIEW = "rx_profile_view";
    @Transient public final static String RX_USE_RX3 = "rx_use_rx3";
    @Transient public final static String USE_MYMEDS = "use_mymeds";
    @Transient public final static String DMFLOW_SHEET_VIEW = "DMFlowsheet_view";
    @Transient public final static String DOC_DEFAULT_QUEUE="doc_default_queue";
    @Transient public final static String HC_TYPE= "HC_Type";
    @Transient public final static String DEFAULT_SEX= "default_sex";
    @Transient public final static String EFORM_REFER_FAX = "eform_refer_fax";
    @Transient public final static String EFORM_FAVOURITE_GROUP = "favourite_eform_group";
    @Transient public final static String RX_SHOW_PATIENT_DOB="rx_show_patient_dob";
    @Transient public final static String PATIENT_NAME_LENGTH="patient_name_length";

    //added to user properties with new interface
    @Transient public final static String FAX = "fax";
    @Transient public final static String SIGNATURE = "signature";
    @Transient public final static String COLOUR = "colour";
    @Transient public final static String SEX = "sex";
    @Transient public final static String SCHEDULE_START_HOUR = "schedule.start_hour";
    @Transient public final static String SCHEDULE_END_HOUR = "schedule.end_hour";
    @Transient public final static String SCHEDULE_PERIOD = "schedule.period";
    @Transient public final static String MYGROUP_NO = "mygroup_no";
    @Transient public final static String NEW_CME = "new_cme";
    @Transient public final static String ENCOUNTER_FORM_LENGTH = "encounter.form_length";
    @Transient public final static String ENCOUNTER_FORM_NAME = "encounter.form_name";
    @Transient public final static String EFORM_NAME = "encounter.eform_name";
    @Transient public final static String RX_SHOW_QR_CODE = "rx_show_qr_code";
    @Transient public final static String MYMEDS = "mymeds";
    @Transient public final static String NEW_TICKLER_WARNING_WINDOW = "new_tickler_warning_window";
    @Transient public final static String CAISI_DEFAULT_PMM = "caisi.default_pmm";
    @Transient public final static String CAISI_PREV_BILLING = "caisi.prev_billing";
    @Transient public final static String DEFAULT_BILLING_FORM = "default_billing_form";
    @Transient public final static String DEFAULT_REFERRAL_TYPE = "default_referral_type";
    @Transient public final static String DEFAULT_PAYEE = "default_payee";
    @Transient public final static String DEFAULT_DX_CODE = "default_dx_code";
    @Transient public final static String CPP_SINGLE_LINE="cpp_single_line";
    @Transient public final static String LAB_ACK_COMMENT="lab_ack_comment";
    @Transient public final static String EDOC_BROWSER_IN_MASTER_FILE="edoc_browser_in_master_file";
    @Transient public final static String EDOC_BROWSER_IN_DOCUMENT_REPORT="edoc_browser_in_document_report";
  

    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_SYNC = "integrator_demographic_sync";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_ISSUES = "integrator_demographic_issues";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_CONSENT = "integrator_demographic_consent";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_ADMISSIONS = "integrator_demographic_admissions";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_PREVENTIONS = "integrator_demographic_preventions";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_NOTES = "integrator_demographic_notes";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_DRUGS = "integrator_demographic_drugs";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_APPOINTMENTS = "integrator_demographic_appointments";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_DXRESEARCH = "integrator_demographic_dxresearch";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_BILLING = "integrator_demographic_billing";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_EFORMS = "integrator_demographic_eforms";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_MEASUREMENTS = "integrator_demographic_measurements";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_DOCUMENTS = "integrator_demographic_documents";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_ALLERGIES = "integrator_demographic_allergies";
    @Transient public static final String INTEGRATOR_DEMOGRAPHIC_LABREQ = "integrator_demographic_labreq";

    @Transient public static final String INTEGRATOR_PROGRAMS = "integrator_programs_sync";
    @Transient public static final String INTEGRATOR_PROVIDERS = "integrator_providers_sync";
    @Transient public static final String INTEGRATOR_FACILITY = "integrator_facility_sync";

    @Transient public static final String INTEGRATOR_FULL_PUSH = "integrator_full_push";
    @Transient public static final String INTEGRATOR_LAST_PUSH = "integrator_last_push";
    @Transient public static final String INTEGRATOR_LAST_UPDATED = "integrator_last_updated";
	public static final String INTEGRATOR_LAST_PULL_PRIMARY_EMR = "integrator_last_pull";

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
