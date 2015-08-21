/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * This software was written for the
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */
package org.oscarehr.e2e.constant;

import org.marc.everest.datatypes.generic.CE;

public class Constants {
	Constants() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Enumerations
	 */
	public static enum FormCodes {
		AER, BAR, BARSOAP, CAP, CRM, DISK, DOUCHE, DROP, ECTAB, ELIXIR, ENEMA,
		ENTCAP, ERCAP, ERSUSP, ERTAB, FOAM, GASINHL, GEL, GELAPL, GRAN, GUM,
		LTN, MDINHL, OINT, ORTROCHE, PAD, PASTE, PATCH, PELLET, PILL, POWD,
		RINSE, SHMP, SLTAB, SOL, SPRYADAPT, SUPP, SUSP, SWAB, SYRUP, TAB, TINC,
		TPASTE, VAGSUPP, VAGTAB, WAFER
	}

	public static enum IdPrefixes {
		AdvanceDirectives, Alerts, Allergies, ClinicalMeasuredObservations, Encounters,
		FamilyHistory, Immunizations, Medications, Lab, LabOBR, MedicationPrescriptions,
		ProblemList, RiskFactors, Referrals, SocialHistory, SubstanceUse
	}

	public static enum IssueCodes {
		OMeds, SocHistory, MedHistory, Concerns, Reminders, FamHistory, RiskFactors
	}

	public static enum MeasurementsExtKeys {
		abnormal, accession, comments, datetime, identifier, labname, lab_no, maximum,
		minimum, name, olis_status, other_id, range, request_datetime, unit
	}

	public static enum ObservationType {
		ALRGRP, CLINSTAT, COMMENT, DATEOBS, ICD9CODE, INSTRUCT, LIFEOBS, NXTENCREAS,
		OUTCOBS, PRNIND, REACTOBS, REASON, RECLINK, SEV, TRTNOTE, UNBOUND
	}

	public static enum PreventionExtKeys {
		comments, dose, location, lot, manufacture, name, neverReason, route
	}

	public static enum ReactionTypeCode {
		ALG, DALG, DINT, DNAINT, EALG, EINT, ENAINT, FALG, FINT, FNAINT, NAINT, OINT
	}

	public static enum RoleClass {
		MANU, PAT, PROV, ROL, SDLOC
	}

	public static enum SubstanceAdministrationType {
		DRUG, IMMUNIZ, ANTIGEN
	}

	public static enum TelecomType {
		EMAIL, TELEPHONE
	}

	public static enum TimeUnit {
		d, h, mo, wk
	}

	/**
	 * Header Constants
	 */
	public static class EMR {
		public static final String EMR_OID = "2.16.840.1.113883.3.3331";
		public static final String EMR_VERSION = "OSCAR EMR";
	}

	public static class EMRConversionDocument {
		public static final String TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.1.1";
		public static final String CODE_DISPLAY_NAME_LOINC = "Medical Records";
		public static final String CODE_SYSTEM_LOINC = Constants.CodeSystems.LOINC_OID;
		public static final String CODE_LOINC = "11503-0";

		public static final CE<String> CODE = new CE<String>(
				Constants.EMRConversionDocument.CODE_LOINC, Constants.CodeSystems.LOINC_OID,
				Constants.CodeSystems.LOINC_NAME, Constants.CodeSystems.LOINC_VERSION);
	}

	public static class DocumentHeader {
		public static final String TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.7.1";
		public static final String E2E_DTC_CLINICAL_DOCUMENT_TYPE_ID = "2.16.840.1.113883.1.3";
		public static final String E2E_DTC_CLINICAL_DOCUMENT_TYPE_ID_EXTENSION = "POCD_HD000040";
		public static final String E2E_DTC_CLINICAL_DOCUMENT_TYPE_REALM_CODE = "CA-BC";
		public static final String LANGUAGE_ENGLISH_CANADIAN = "en-CA";

		public static final String EMAIL_PREFIX = "mailto:";
		public static final String TEL_PREFIX = "tel:";

		public static final String MALE_ADMINISTRATIVE_GENDER_CODE = "M";
		public static final String FEMALE_ADMINISTRATIVE_GENDER_CODE = "F";
		public static final String UNDIFFERENTIATED_ADMINISTRATIVE_GENDER_CODE = "UN";
		public static final String MALE_ADMINISTRATIVE_GENDER_DESCRIPTION = "Male";
		public static final String FEMALE_ADMINISTRATIVE_GENDER_DESCRIPTION = "Female";
		public static final String UNDIFFERENTIATED_ADMINISTRATIVE_GENDER_DESCRIPTION = "Undifferentiated";

		public static final String BC_PHN_OID_ASSIGNING_AUTHORITY_NAME = "BC Patient Health Number";
		public static final String BC_PHN_OID = "2.16.840.1.113883.4.50";

		// TODO [E2E] Determine BC MSP and Locally Assigned ID OIDs
		public static final String BC_MINISTRY_OF_HEALTH_PRACTITIONER_ID_OID = "2.16.840.1.113883.3.40.2.11";
		public static final String BC_MINISTRY_OF_HEALTH_PRACTITIONER_NAME = "BC MSP Provider License Number";
		public static final String MEDICAL_SERVICES_PLAN_BILLING_NUMBER_OID = "TBD";
		public static final String MEDICAL_SERVICES_PLAN_BILLING_NUMBER_NAME = "BC MSP Billing Number";
		public static final String LOCALLY_ASSIGNED_IDENTIFIER_OID = "TBD";
		public static final String LOCALLY_ASSIGNED_IDENTIFIER_NAME = "Locally Assigned Identifier";

		public static final String HUMANLANGUAGE_ENGLISH_CODE = "EN";
		public static final String HUMANLANGUAGE_FRENCH_CODE = "FR";
		public static final String HUMANLANGUAGE_ENGLISH_DESCRIPTION = "English";
		public static final String HUMANLANGUAGE_FRENCH_DESCRIPTION = "French";
	}

	/**
	 * Body Constants
	 */
	public static class CodeSystems {
		public static final String ACT_CODE_CODESYSTEM_OID = "2.16.840.1.113883.5.4";
		public static final String ACT_CODE_CODESYSTEM_NAME = "ActCode";
		public static final String ADMINISTERABLE_DRUG_FORM_OID = "2.16.840.1.113883.1.11.14570";
		public static final String ADMINISTERABLE_DRUG_FORM_NAME = "AdministerableDrugForm";
		public static final String ADMINISTRATIVE_GENDER_OID = "2.16.840.1.113883.5.1";
		public static final String ATC_OID = "2.16.840.1.113883.6.73";
		public static final String ATC_NAME = "whoATC";
		public static final String DIN_OID = "2.16.840.1.113883.5.1105";
		public static final String DIN_NAME = "HC-DIN";
		public static final String ICD9_OID = "2.16.840.1.113883.6.42";
		public static final String ICD9_NAME = "ICD9";
		public static final String LOINC_OID = "2.16.840.1.113883.6.1";
		public static final String LOINC_NAME = "LOINC";
		public static final String LOINC_VERSION = "2.44";
		public static final String OBSERVATION_INTERPRETATION_OID = "2.16.840.1.113883.5.83";
		public static final String OBSERVATION_INTERPRETATION_NAME = "ObservationInterpretation";
		public static final String OBSERVATIONTYPE_CA_PENDING_OID = "2.16.840.1.113883.3.3068.10.6.3";
		public static final String OBSERVATIONTYPE_CA_PENDING_NAME = "ObservationType-CA-Pending";
		public static final String OBSERVATION_VALUE_OID = "2.16.840.1.113883.5.1063";
		public static final String OBSERVATION_VALUE_NAME = "ObservationValue";
		public static final String PCLOCD_OID = "2.16.840.1.113883.2.20.5.1";
		public static final String PCLOCD_NAME = "pCLOCD";
		public static final String ROLE_CODE_OID = "2.16.840.1.113883.5.111";
		public static final String ROLE_CODE_NAME = "RoleCode";
		public static final String ROLE_CLASS_OID = "2.16.840.1.113883.3.3068.10.8.30";
		public static final String ROLE_CLASS_NAME = "RoleClass";
		public static final String ROUTE_OF_ADMINISTRATION_OID = "2.16.840.1.113883.5.112";
		public static final String ROUTE_OF_ADMINISTRATION_NAME = "RouteOfAdministration";
		public static final String SECTIONTYPE_CA_PENDING_OID = "2.16.840.1.113883.3.3068.10.6.2";
		public static final String SECTIONTYPE_CA_PENDING_NAME = "SectionType-CA-Pending";
		public static final String SNOMED_CT_OID = "2.16.840.1.113883.6.96";
		public static final String SNOMED_CT_NAME = "SNOMED-CT";
	}

	public static class ObservationOids {
		public static final String COMMENT_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.3";
		public static final String DATE_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.4";
		public static final String INSTRUCTION_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.35";
		public static final String LIFESTAGE_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.12";
		public static final String ORDER_INDICATOR_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.37";
		public static final String REACTION_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.23";
		public static final String REASON_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.24";
		public static final String SECONDARY_CODE_ICD9_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.28";
		public static final String SEVERITY_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.30";
		public static final String UNBOUND_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.32";
	}

	public static class SectionSupport {
		public static final String SECTION_SUPPORTED_NO_DATA = "No information on this section for this patient";
		public static final String SECTION_NOT_SUPPORTED_NO_DATA = "This section is not supported by the Originating Application";
	}

	public static class TemplateOids {
		public static final String ADVANCE_DIRECTIVES_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.3.1";
		public static final String ALERTS_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.3.2";
		public static final String ALLERGY_INTOLERANCE_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.3.3";
		public static final String AUTHOR_PARTICIPATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.2";
		public static final String CLINICALLY_MEASURED_OBSERVATIONS_ORGANIZER_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.3.7";
		public static final String DOSE_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.8";
		public static final String ENCOUNTER_EVENT_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.3.9";
		public static final String FAMILY_HISTORY_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.3.10";
		public static final String IMMUNIZATION_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.3.11";
		public static final String INSTRUCTION_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.35";
		public static final String LABS_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.3.12";
		public static final String MEDICATION_EVENT_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.3.18";
		public static final String MEDICATION_IDENTIFICATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.16";
		public static final String MEDICATION_PRESCRIPTION_EVENT_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.20";
		public static final String ORDER_EVENT_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.3.14";
		public static final String PERFORMER_PARTICIPATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.19";
		public static final String PROBLEMS_OBSERVATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.3.15";
		public static final String PROVIDER_PARTICIPATION_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.21";
		public static final String RESULT_COMPONENT_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.25";
		public static final String RESULT_ORGANIZER_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.4.26";
		public static final String RISK_FACTORS_ORGANIZER_TEMPLATE_ID = "2.16.840.1.113883.3.1818.10.3.17";
	}

	/**
	 * Runtime Constants
	 */
	public static class XML {
		public static final Integer INDENT = 2;
		public static final String ENCODING = "UTF-8";
		public static final String VERSION = "1.0";
	}

	public static class Runtime {
		public static final String E2E_SETUP = System.getProperty("basedir") + "/src/test/resources/e2e_setup.sql";
		public static final String[] TABLES = {"admission", "allergies", "casemgmt_note", "casemgmt_note_ext",
				"casemgmt_issue", "casemgmt_issue_notes", "clinic", "demographic", "demographicSets", "demographic_merged",
				"drugs", "dxresearch", "health_safety", "hl7TextInfo", "icd9", "Icd9Synonym", "issue", "lst_gender",
				"measurementMap", "measurementType", "measurements", "measurementsExt", "patientLabRouting", "preventions",
				"preventionsExt", "program", "provider", "secRole"};
		public static final Integer EMPTY_DEMOGRAPHIC = 2;
		public static final Integer INVALID_VALUE = -1;
		public static final Integer VALID_DEMOGRAPHIC = 1;
		public static final Integer VALID_LAB_NO = 9;
		public static final Integer VALID_LAB_MEASUREMENT = 26;
		public static final Integer VALID_MEASUREMENT = 2;
		public static final Integer VALID_PREVENTION = 1;
		public static final Integer VALID_PROVIDER = 999998;
		public static final Long VALID_FAMILY_HISTORY = 7L;
	}
}
