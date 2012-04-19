/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */

package com.quatro.common;

public class KeyConstants {
	public static final String SYSTEM_USER_PROVIDER_NO = "1111";
	public static final String SESSION_KEY_SHELTERID="currentShelterId"; 
	public static final String SESSION_KEY_SHELTER="currentShelter"; 
   public static final String SESSION_KEY_PROVIDERNO="user";
  public static final String SESSION_KEY_PROVIDERNAME="username";
  public static final String SESSION_KEY_SECURITY_MANAGER="secMgr";

  public static final String CONSTANT_GENDER_Male = "M";
  public static final String CONST_GENDER_Female = "F";
  public static final String CONST_GENDER_Transgender = "T";
  
  public static final String BED_PROGRAM_TYPE="Bed";
  public static final String SERVICE_PROGRAM_TYPE="Service";

  public static final String CONSTANT_YES = "1";
  public static final String CONSTANT_NO = "0";
  
  public static final String SESSION_KEY_CLIENTID="clientId"; 
  public static final String SESSION_KEY_CURRENT_FUNCTION="func";
  public static final String SESSION_KEY_SWITCH_MODULE="switch";
  public static final String SESSION_KEY_CURRENT_MODULE="curModule";
  public static final String SESSION_KEY_CURRENT_RECORD="curRec";
  public static final Integer MODULE_ID_CLIENT = new Integer(1);
  public static final Integer MODULE_ID_SHELTER = new Integer(2);
  public static final Integer MODULE_ID_CASE = new Integer(3);
  public static final Integer MODULE_ID_SYSTEM = new Integer(4);
  public static final Integer MODULE_ID_REPORT = new Integer(5);
  public static final Integer MODULE_ID_INTAKE = new Integer(6);
  public static final Integer MODULE_ID_AGENCY = new Integer(7);  

  public static final String PROGRAM_TYPE_Bed = "Bed";
  public static final String PROGRAM_TYPE_Service = "Service";
  public static final String PROGRAM_TYPE_External = "external";
  public static final String PROGRAM_TYPE_Community = "community";

  public static final String INTAKE_STATUS_ACTIVE = "active";
  public static final String INTAKE_STATUS_ADMITTED = "admitted";
  public static final String INTAKE_STATUS_REJECTED = "rejected";
  public static final String INTAKE_STATUS_DISCHARGED = "discharged";
  public static final String FAMILY_HEAD_CODE = "AA";
  public static final String FAMILY_HEAD_DESC = "Family Head";
  public static final String ACCESS_VIEW="V";
  public static final String ACCESS_VIEW_NOCLICK="VN";
  public static final String ACCESS_NULL="N";
  public static final String ACCESS_CURRENT="C";
  public static final String ACCESS_NONE = "o";
  public static final String ACCESS_READ = "r";
  public static final String ACCESS_UPDATE = "u";
  public static final String ACCESS_WRITE = "w";
  public static final String ACCESS_ALL = "x";
  public static final String AUTOMATIC ="A";
  public static final String MANUAL ="M"; 
  public static final String DATE_YYYYMMDDHHMM="YYYYMMDDHHMM";
  public static final String DATE_YYYYMMDD="YYYYMMDD";
  public static final String DATE_MMDDYYYY="MMDDYYYY";
  public static final String DATE_DDMMYYYY="DDMMYYYY";
  
  public static final String MENU_CLIENT="mnuClient";
  public static final String MENU_PROGRAM="mnuProg";
  public static final String MENU_FACILITY="mnuFacility";
  public static final String MENU_REPORT="mnuReport";
  public static final String MENU_TASK="mnuTask";
  public static final String MENU_ADMIN="mnuAdmin";
  public static final String MENU_HOME="mnuHome";
 
  
  public static final String TAB_CLIENT_SUMMARY="tabSummary";
  public static final String TAB_CLIENT_DISCHARGE="tabDischarge";
  public static final String TAB_CLIENT_ADMISSION="tabAdmission";
  public static final String TAB_CLIENT_CONSENT="tabConsent";
  public static final String TAB_CLIENT_HISTORY="tabHistory";
  public static final String TAB_CLIENT_INTAKE="tabIntake";
  public static final String TAB_CLIENT_REFER="tabRefer";
  public static final String TAB_CLIENT_RESTRICTION="tabRestriction";
  public static final String TAB_CLIENT_COMPLAINT="tabComplaint";
  public static final String TAB_CLIENT_CASE="tabCase";
  public static final String TAB_CLIENT_ATTCHMENT="tabAttachment";
  public static final String TAB_CLIENT_TASK="tabTask";
  public static final String TAB_CLIENT_HEALTH="tabHealth";
  public static final String TAB_CLIENT_PRINTLABEL="tabPrintLabel";
  public static final String TAB_PROGRAM_GENERAL =  "General";
  public static final String TAB_PROGRAM_QUEUE="Queue";
  public static final String TAB_PROGRAM_CLIENTS="Clients";
  public static final String TAB_PROGRAM_INCIDENTS="Incidents";
  public static final String TAB_PROGRAM_SEVICE="Service"; 
  public static final String TAB_PROGRAM_STAFF="Staff"; 
    
  public static final String STATUS_ADMITTED = "admitted";
  public static final String STATUS_ACCEPTED = "accepted";
  public static final String STATUS_DISCHARGED = "discharged";
  public static final String STATUS_REJECTED = "rejected";
  public static final String STATUS_REMOVED = "removed";
  public static final String STATUS_ACTIVE = "active";  
  public static final String STATUS_INACTIVE = "inactive";  
//  public static final String STATUS_CURRENT = "current";
//  public static final String STATUS_UNKNOWN = "unknown";
  public static final String STATUS_PENDING = "pending";
  public static final String STATUS_WITHDRAW = "withdrawn";
  public static final String STATUS_TERMEARLY="terminated early"; 
  public static final String STATUS_COMPLETED="completed";
  public static final String STATUS_IN_PROGRESS="in progress";
  public static final String STATUS_READONLY="read only";
  public static final String STATUS_EXPIRED="expired";
  public static final String STATUS_SIGNED="signed";
  public static final String STATUS_UNSIGNED="unsigned";
//  public static final String STATUS_LOCKED="locked";
//  public static final String STATUS_UNLOCKED="unlocked";
  /*
  public static final String FUNCTION_INTAKE="intake";
  public static final String FUNCTION_ADMISSION="admission";
  public static final String FUNCTION_REFERRAL="referral";
  public static final String FUNCTION_DISCHARGE="discharge";
  public static final String FUNCTION_COMPLAINT="complaint";
  public static final String FUNCTION_CONSENT="consent";
  public static final String FUNCTION_CASEMANAGEMENT="case";
  public static final String FUNCTION_INCIDENT="incident";
  public static final String FUNCTION_SERVICE_RESTRICTION="serviceRestriction";
 */
  public static final String CLIENT_MODE_MERGE = "merge";
  public static final String CLIENT_MODE_UNMERGE = "unmerge";
  
  public static final String TAB_FACILITY_GENERAL = "tabGeneral";
  public static final String TAB_FACILITY_PROGRAM = "tabProgram";
  public static final String TAB_FACILITY_MESSAGE = "tabMessage";
  
  public static final String TAB_FACILITY_EDIT = "tabEdit";
  public static final String TAB_FACILITY_BED = "tabBed";

  public static final String DEFAULT_TIME_ZONE = "GMT-05:00";
  
  /*Function Definition */
  public static final String FUN_FACILITY_BED="_facility.bed";
  public static final String FUN_FACILITY_EDIT="_facility.edit";
  //public static final String FUN_FACILITY_GENERAL="_facility.general";
  public static final String FUN_FACILITY_MESSAGE="_facility.message";
  public static final String FUN_FACILITY_PROGRAM="_facility.program";
  public static final String FUN_CLIENT="_client";
  
  public static final String FUN_PROGRAM="_program";
  public static final String FUN_PROGRAM_STAFF="_program.staff";
  public static final String FUN_CLIENTTASKS="_clientTasks";
  public static final String FUN_CLIENTDOCUMENT="_clientDocument";

  public static final String FUN_ADMIN="_admin";
  public static final String FUN_ADMIN_SECURITY="_admin.security";
  public static final String FUN_ADMIN_SECURITYLOGREPORT="_admin.securityLogReport";
  public static final String FUN_ADMIN_COOKIEREVOLVER="_admin.cookieRevolver";
  public static final String FUN_ADMIN_LOOKUPFIELDEDITOR="_admin.lookupFieldEditor";
  public static final String FUN_DEMOGRAPHIC="_demographic";
  public static final String FUN_REPORTS="_reports";
  public static final String FUN_PROGRAMEDIT="_programEdit";
 // public static final String FUN_PROGRAMEDIT_STAFF="_programEdit.staff";  
  public static final String FUN_PROGRAM_CLIENTS="_program.clients";
  public static final String FUN_PROGRAM_QUEUE="_program.queue";
  public static final String FUN_PROGRAM_INCIDENT="_program.incident";
  public static final String FUN_PROGRAMEDIT_SERVICERESTRICTIONS="_programEdit.serviceRestrictions";
  public static final String FUN_PROGRAM_SERVICERESTRICTIONS="_program.serviceRestrictions";
  public static final String FUN_PROGRAM_REJECT="_program.queueReject";
 
  public static final String FUN_CLIENTADMISSION="_clientAdmission";
  public static final String FUN_CLIENTCASE="_clientCase";
  public static final String FUN_CLIENTCOMPLAINT="_clientComplaint";
  public static final String FUN_CLIENTCONSENT="_clientConsent";
  public static final String FUN_CLIENTDISCHARGE="_clientDischarge";
  public static final String FUN_CLIENTHISTORY="_clientHistory";
  public static final String FUN_CLIENTINTAKE="_clientIntake";
  public static final String FUN_CLIENTREFER="_clientRefer";
  public static final String FUN_CLIENTRESTRICTION="_clientRestriction";
  public static final String FUN_FACILITY="_facility";
  public static final String FUN_CLIENTHEALTHSAFETY="_clientHealthSafety";
  public static final String FUN_CLIENTPRINTLABEL="_clientPrintLabel";
  
  public static final String FUN_ADMIN_FACILITYMESSAGE="_admin.facilityMessage";
  
  public static final String FUN_ADMIN_SYSTEMMESSAGE="_admin.systemMessage";
  public static final String FUN_ADMIN_USER="_admin.user";
  public static final String FUN_ADMIN_ROLE="_admin.role";
  public static final String FUN_ADMIN_LOOKUP="_admin.lookup";
  public static final String FUN_ADMIN_MERGECLIENT="_admin.mergeClient";
  public static final String FUN_ADMIN_ORG="_admin.org";
 // public static final String FUN_ADMIN_SYSMESSAGE="_admin.systemMesage";
  public static final String FUN_ADMIN_UNLOCKUSER="_admin.unlockUser";
  
  public static final String FUN_PMM_ADDPROGRAM="_pmm.addProgram";
  public static final String FUN_PMM_AGENCYINFORMATION="_pmm.agencyInformation";
 // public static final String FUN_PMM_CASEMANAGEMENT="_pmm.caseManagement";
  public static final String FUN_PMM_GLOBALROLEACCESS="_pmm.globalRoleAccess";
  public static final String FUN_PMM_MANAGEFACILITIES="_pmm.manageFacilities";
  public static final String FUN_PMM_NEWCLIENT="_pmm.newClient";
  public static final String FUN_ADMIN_PROVIDER="_admin.provider";
  public static final String FUN_PMM_MERGERECORDS="_pmm.mergeRecords";
  public static final String FUN_PMM_MANAGEMENT="_pmm_management";
  public static final String FUN_PMM_TASK="_pmm.task";
  
  public static final String DEFAULT_FIELD_LENGTH_STRING="80";
  
  //for page warnings/errors that need user to confirm before proceed save/update.
  public static final String CONFIRMATION_CHECKBOX_NAME ="chk_confirm_overwrite";  
  
  //value in discharge reason lookup table
  public static final String AUTO_DISCHARGE_REASON="50";
  public static final String AUTO_DISCHARGE_DISPOSITION="7";

  public static final String ORG_ROOT = "R1";
 }
