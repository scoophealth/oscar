/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarDemographic.pageUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlbeans.XmlOptions;

import cds.DemographicsDocument;
import cds.ReportsReceivedDocument;
import cdsDt.PersonNameStandard.OtherNames;
import cdsDtHrm.Address;
import cdsDtHrm.AddressStructured;
import cdsDtHrm.AddressType;
import cdsDtHrm.ContactPersonPurpose;
import cdsDtHrm.DateFullOrPartial;
import cdsDtHrm.EnrollmentStatus;
import cdsDtHrm.Gender;
import cdsDtHrm.HealthCard;
import cdsDtHrm.HealthCardProvinceCode;
import cdsDtHrm.OfficialSpokenLanguageCode;
import cdsDtHrm.PersonNamePartQualifierCode;
import cdsDtHrm.PersonNamePartTypeCode;
import cdsDtHrm.PersonNamePurposeCode;
import cdsDtHrm.PersonNameSimple;
import cdsDtHrm.PersonNameSimpleWithMiddleName;
import cdsDtHrm.PersonNameStandard;
import cdsDtHrm.PersonStatus;
import cdsDtHrm.PhoneNumber;
import cdsDtHrm.PhoneNumberType;
import cdsDtHrm.PostalZipCode;
import cdsDtHrm.ReportClass;
import cdsDtHrm.ReportContent;
import cdsDtHrm.ReportFormat;
import cdsDtHrm.ReportMedia;
import cdshrm.DemographicsDocument.Demographics;
import cdshrm.DemographicsDocument.Demographics.Contact;
import cdshrm.OmdCdsDocument;
import cdshrm.OmdCdsDocument.OmdCds;
import cdshrm.PatientRecordDocument.PatientRecord;
import cdshrm.ReportsReceivedDocument.ReportsReceived;
import cdshrm.ReportsReceivedDocument.ReportsReceived.ResultStatus;
import cdshrm.TransactionInformationDocument.TransactionInformation;

/**
 *
 * @author ronnie
 */
public class CreateHRMFile {

    static public void create(DemographicsDocument.Demographics demographic, List<ReportsReceivedDocument.ReportsReceived> reports, String filepath) {

        OmdCdsDocument omdCdsDoc = OmdCdsDocument.Factory.newInstance();
        OmdCds omdCds = omdCdsDoc.addNewOmdCds();
        PatientRecord patientRecord = omdCds.addNewPatientRecord();

        Demographics HRMdemo = patientRecord.addNewDemographics();

        writeDemographics(demographic, HRMdemo);
        writeReportsReceived(reports, patientRecord);

	XmlOptions options = new XmlOptions();
	options.put( XmlOptions.SAVE_PRETTY_PRINT );
	options.put( XmlOptions.SAVE_PRETTY_PRINT_INDENT, 3 );
	options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES );

        HashMap<String,String> suggestedPrefix = new HashMap<String,String>();
        suggestedPrefix.put("cds_dt","cdsd");
        options.setSaveSuggestedPrefixes(suggestedPrefix);
	options.setSaveOuter();

        File file = new File(filepath);
        try {
            omdCdsDoc.save(file, options);
        } catch (IOException ex) {
            Logger.getLogger(CreateHRMFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static private void writeDemographics(DemographicsDocument.Demographics demo, Demographics HRMdemo) {
        //Names
        cdsDt.PersonNameStandard personName = demo.getNames();
        cdsDt.PersonNameStandard.LegalName legalName = personName.getLegalName();
        cdsDt.PersonNameStandard.LegalName.FirstName firstName = null;
        cdsDt.PersonNameStandard.LegalName.LastName lastName = null;
        cdsDt.PersonNamePurposeCode.Enum namePurpose = null;
        OtherNames[] otherNames = personName.getOtherNamesArray();
        
        if (legalName!=null) {
            lastName = legalName.getLastName();
            firstName = legalName.getFirstName();
            namePurpose = legalName.getNamePurpose();
        } else if (otherNames.length > 0) {
        	for (OtherNames.OtherName oName : otherNames[0].getOtherNameArray()) {
	        	if (oName.getPartType().toString().equals("FAMC")) {
	        		lastName = cdsDt.PersonNameStandard.LegalName.LastName.Factory.newInstance();
	        		lastName.setPart(oName.getPart());
	        		lastName.setPartQualifier(oName.getPartQualifier());
	        		lastName.setPartType(oName.getPartType());
	        	}
	            if (oName.getPartType().toString().equals("GIV")) {
	            	firstName = cdsDt.PersonNameStandard.LegalName.FirstName.Factory.newInstance();
	            	firstName.setPart(oName.getPart());
	            	firstName.setPartQualifier(oName.getPartQualifier());
	            	firstName.setPartType(oName.getPartType());
	            }	
        	}
        } else {
        	Logger.getLogger(CreateHRMFile.class.getName()).log(Level.WARNING, null, "Error! No Legal Name or Other Name");
        }

        PersonNameStandard HRMpersonName = HRMdemo.addNewNames();
        PersonNameStandard.LegalName HRMlegalName = HRMpersonName.addNewLegalName();
        PersonNameStandard.LegalName.FirstName HRMfirstName = HRMlegalName.addNewFirstName();
        PersonNameStandard.LegalName.LastName HRMlastName = HRMlegalName.addNewLastName();

        if (firstName.getPart()!=null) HRMfirstName.setPart(firstName.getPart());
        else HRMfirstName.setPart("");
        HRMfirstName.setPartType(PersonNamePartTypeCode.GIV);
        HRMfirstName.setPartQualifier(PersonNamePartQualifierCode.BR);

        if (lastName.getPart()!=null) HRMlastName.setPart(lastName.getPart());
        else HRMlastName.setPart("");
        HRMlastName.setPartType(PersonNamePartTypeCode.FAMC);
        HRMlastName.setPartQualifier(PersonNamePartQualifierCode.BR);

        if (namePurpose!=null) HRMlegalName.setNamePurpose(PersonNamePurposeCode.Enum.forString(namePurpose.toString()));

        //Gender
        cdsDt.Gender.Enum gender = demo.getGender();
        if (gender!=null) HRMdemo.setGender(Gender.Enum.forString(gender.toString()));
        else HRMdemo.setGender(Gender.U);

        //ChartNumber
        if (demo.getChartNumber()!=null) HRMdemo.setChartNumber(demo.getChartNumber());

        //SIN
        if (demo.getSIN()!=null) HRMdemo.setSIN(demo.getSIN());

        //UniqueVendorIdSequence
        if (demo.getUniqueVendorIdSequence()!=null) HRMdemo.setUniqueVendorIdSequence(demo.getUniqueVendorIdSequence());
        else HRMdemo.setUniqueVendorIdSequence("");

        //Email
        if (demo.getEmail()!=null) HRMdemo.setEmail(demo.getEmail());

        //NoteAboutPatient
        if (demo.getNoteAboutPatient()!=null) HRMdemo.setNoteAboutPatient(demo.getNoteAboutPatient());

        //PreferredLanguages
        cdsDt.OfficialSpokenLanguageCode.Enum officialLang = demo.getPreferredOfficialLanguage();
        if (officialLang!=null) HRMdemo.setPreferredOfficialLanguage(OfficialSpokenLanguageCode.Enum.forString(officialLang.toString()));

        if (demo.getPreferredSpokenLanguage()!=null) HRMdemo.setPreferredSpokenLanguage(demo.getPreferredSpokenLanguage());

        //DateOfBirth
        if (demo.getDateOfBirth()!=null) HRMdemo.addNewDateOfBirth().setFullDate(demo.getDateOfBirth());

        //PersonStatus
        cdsDt.PersonStatus.Enum personStatus = demo.getPersonStatusCode().getPersonStatusAsEnum();
        if (personStatus!=null) HRMdemo.setPersonStatusCode(PersonStatus.Enum.forString(personStatus.toString()));
        else HRMdemo.setPersonStatusCode(PersonStatus.O);

        if (demo.getPersonStatusDate()!=null) HRMdemo.addNewPersonStatusDate().setFullDate(demo.getPersonStatusDate());

        //EnrollmentStatus
        DemographicsDocument.Demographics.Enrolment[] enrolments = demo.getEnrolmentArray();
        if (enrolments!=null && enrolments.length>0) {
            cdsDt.EnrollmentStatus.Enum enrollmentStatus = enrolments[0].getEnrollmentStatus();
            if (enrollmentStatus!=null) HRMdemo.setEnrollmentStatus(EnrollmentStatus.Enum.forString(enrollmentStatus.toString()));

            if (enrolments[0].getEnrollmentDate()!=null) {
                HRMdemo.addNewEnrollmentDate().setFullDate(enrolments[0].getEnrollmentDate());
            }
            if (enrolments[0].getEnrollmentTerminationDate()!=null) {
                HRMdemo.addNewEnrollmentTerminationDate().setFullDate(enrolments[0].getEnrollmentTerminationDate());
            }
        }

        //HealhCard
        cdsDt.HealthCard healthCard = demo.getHealthCard();
        if (healthCard!=null) {
            HealthCard HRMhealthCard = HRMdemo.addNewHealthCard();
            if (healthCard.getNumber()!=null) HRMhealthCard.setNumber(healthCard.getNumber());
            if (healthCard.getVersion()!=null) HRMhealthCard.setVersion(healthCard.getVersion());
            if (healthCard.getProvinceCode()!=null) {
                HRMhealthCard.setProvinceCode(HealthCardProvinceCode.Enum.forString(healthCard.getProvinceCode().toString()));
            }
            if (healthCard.getExpirydate()!=null) HRMhealthCard.setExpirydate(healthCard.getExpirydate());
        }

        //PrimaryPhysician
        DemographicsDocument.Demographics.PrimaryPhysician primaryPhysician = demo.getPrimaryPhysician();
        if (primaryPhysician!=null) {
            Demographics.PrimaryPhysician HRMprimaryPhysician = HRMdemo.addNewPrimaryPhysician();
            if (primaryPhysician.getName()!=null) {
                copyPersonNameSimple(HRMprimaryPhysician.addNewName(), primaryPhysician.getName());
            }
            if (primaryPhysician.getOHIPPhysicianId()!=null)
                HRMprimaryPhysician.setOHIPPhysicianId(primaryPhysician.getOHIPPhysicianId());
            else HRMprimaryPhysician.setOHIPPhysicianId("");
        }

        //Addresses
        cdsDt.Address[] addresses = demo.getAddressArray();
        if (addresses!=null) {
            for (cdsDt.Address address : addresses) {

                //copy address type
                cdsDt.AddressType.Enum addressType = address.getAddressType();

                if (address.getFormatted()!=null) {
                    
                    //address.formated
                    Address HRMaddress =  HRMdemo.addNewAddress();
                    HRMaddress.setFormatted(address.getFormatted());

                    //address type
                    if (addressType!=null) HRMaddress.setAddressType(AddressType.Enum.forString(addressType.toString()));
                    else HRMaddress.setAddressType(AddressType.R);

                } else if (address.getStructured()!=null) {
                    
                    //address.structured
                    Address HRMaddress =  HRMdemo.addNewAddress();
                    cdsDt.AddressStructured addrStruct = address.getStructured();
                    AddressStructured HRMaddrStruct = HRMaddress.addNewStructured();

                    HRMaddrStruct.setCity(addrStruct.getCity());
                    HRMaddrStruct.setCountrySubdivisionCode(addrStruct.getCountrySubdivisionCode());
                    HRMaddrStruct.setLine1(addrStruct.getLine1());

                    if (addrStruct.getLine2()!=null) HRMaddrStruct.setLine2(addrStruct.getLine2());
                    if (addrStruct.getLine3()!=null) HRMaddrStruct.setLine3(addrStruct.getLine3());

                    cdsDt.PostalZipCode postalZipCode = addrStruct.getPostalZipCode();
                    PostalZipCode HRMpostalZipCode = HRMaddrStruct.addNewPostalZipCode();
                    if (postalZipCode!=null) {
                        if (postalZipCode.getPostalCode()!=null) HRMpostalZipCode.setPostalCode(postalZipCode.getPostalCode());
                        else if (postalZipCode.getZipCode()!=null) HRMpostalZipCode.setZipCode(postalZipCode.getZipCode());
                    }

                    //address type
                    if (addressType!=null) HRMaddress.setAddressType(AddressType.Enum.forString(addressType.toString()));
                    else HRMaddress.setAddressType(AddressType.R);
                }
            }
        }

        //PhoneNumbers
        cdsDt.PhoneNumber[] phoneNumbers = demo.getPhoneNumberArray();
        if (phoneNumbers!=null) {
            for (cdsDt.PhoneNumber phoneNumber : phoneNumbers) {
                PhoneNumber HRMphoneNumber = HRMdemo.addNewPhoneNumber();

                if (phoneNumber.getPhoneNumber()!=null) {
                    HRMphoneNumber.setPhoneNumber(phoneNumber.getPhoneNumber());
                } else if (phoneNumber.getNumber()!=null) {
                    HRMphoneNumber.setNumber(phoneNumber.getNumber());
                    HRMphoneNumber.setAreaCode(phoneNumber.getAreaCode());
                    if (phoneNumber.getExchange()!=null) HRMphoneNumber.setExchange(phoneNumber.getExchange());
                }
                if (phoneNumber.getExtension()!=null) HRMphoneNumber.setExtension(phoneNumber.getExtension());
                HRMphoneNumber.setPhoneNumberType(PhoneNumberType.Enum.forString(phoneNumber.getPhoneNumberType().toString()));
            }
        }

        //Contacts
        DemographicsDocument.Demographics.Contact[] contacts = demo.getContactArray();
        if (contacts!=null) {
            for (DemographicsDocument.Demographics.Contact contact : contacts) {
                if (contact.getName()!=null) {
                    Contact HRMcontact = HRMdemo.addNewContact();

                    //contact name
                    cdsDt.PersonNameSimpleWithMiddleName contactName = contact.getName();
                    PersonNameSimpleWithMiddleName HRMcontactName = HRMcontact.addNewName();

                    if (contactName.getFirstName()!=null) HRMcontactName.setFirstName(contactName.getFirstName());
                    if (contactName.getLastName()!=null) HRMcontactName.setLastName(contactName.getLastName());
                    if (contactName.getMiddleName()!=null) HRMcontactName.setMiddleName(contactName.getMiddleName());

                    //contact purpose
                    cdsDt.PurposeEnumOrPlainText[] purposes = contact.getContactPurposeArray();
                    if (purposes!=null && purposes.length>0) {
                        cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.Enum purpose = purposes[0].getPurposeAsEnum();
                        if (purpose!=null) HRMcontact.setContactPurpose(ContactPersonPurpose.Enum.forString(purpose.toString()));
                        if (HRMcontact.getContactPurpose()==null) HRMcontact.setContactPurpose(ContactPersonPurpose.O);
                    }

                    //contact phone
                    cdsDt.PhoneNumber[] contactPhones = contact.getPhoneNumberArray();
                    if (contactPhones!=null) {
                        for (cdsDt.PhoneNumber contactPhone : contactPhones) {
                            PhoneNumber HRMcontactPhone = HRMcontact.addNewPhoneNumber();

                            if (contactPhone.getPhoneNumber()!=null) {
                                HRMcontactPhone.setPhoneNumber(contactPhone.getPhoneNumber());
                            } else if (contactPhone.getNumber()!=null) {
                                HRMcontactPhone.setNumber(contactPhone.getNumber());
                                HRMcontactPhone.setAreaCode(contactPhone.getAreaCode());
                                if (contactPhone.getExchange()!=null) HRMcontactPhone.setExchange(contactPhone.getExchange());
                            }
                            if (contactPhone.getExtension()!=null) HRMcontactPhone.setExtension(contactPhone.getExtension());
                            HRMcontactPhone.setPhoneNumberType(PhoneNumberType.Enum.forString(contactPhone.getPhoneNumberType().toString()));
                        }
                    }
                }
            }
        }
    }

    static private void writeReportsReceived(List<ReportsReceivedDocument.ReportsReceived> reports, PatientRecord patientRecord) {
        for (ReportsReceivedDocument.ReportsReceived report : reports) {
            ReportsReceived HRMreport = patientRecord.addNewReportsReceived();

            //AuthorPhysician
            ReportsReceivedDocument.ReportsReceived.SourceAuthorPhysician authorPhysician = report.getSourceAuthorPhysician();
            if (authorPhysician!=null && authorPhysician.getAuthorName()!=null) {
                copyPersonNameSimple(HRMreport.addNewAuthorPhysician(), authorPhysician.getAuthorName());
            }

            //ReportClass
            if (report.getClass1()!=null) HRMreport.setClass1(ReportClass.Enum.forString(report.getClass1().toString()));

            //SubClass
            if (report.getSubClass()!=null) HRMreport.setSubClass(report.getSubClass());

            //Content
            cdsDt.ReportContent reportContent = report.getContent();
            if (reportContent!=null && reportContent.getTextContent()!=null) {
                ReportContent HRMreportContent = HRMreport.addNewContent();
                HRMreportContent.setTextContent(reportContent.getTextContent());
            }

            //FileExtensionAndVersion
            if (report.getFileExtensionAndVersion()!=null) 
                HRMreport.setFileExtensionAndVersion(report.getFileExtensionAndVersion());
            else HRMreport.setFileExtensionAndVersion("");

            //Format
            if (report.getFormat()!=null) HRMreport.setFormat(ReportFormat.Enum.forString(report.getFormat().toString()));

            //Media
            if (report.getMedia()!=null) HRMreport.setMedia(ReportMedia.Enum.forString(report.getMedia().toString()));

            //EventDateTime
            if (report.getEventDateTime()!=null) copyDateFP(HRMreport.addNewEventDateTime(), report.getEventDateTime());

            //ReceivedDateTime
            if (report.getReceivedDateTime()!=null) copyDateFP(HRMreport.addNewReceivedDateTime(), report.getReceivedDateTime());

            //Reviews
            ReportsReceivedDocument.ReportsReceived.ReportReviewed[] reportReviews = report.getReportReviewedArray();
            if (reportReviews!=null && reportReviews.length>0) {
                if (reportReviews[0].getDateTimeReportReviewed()!=null) {
                    copyDateFP(HRMreport.addNewReviewedDateTime(), reportReviews[0].getDateTimeReportReviewed());
                }
                if (reportReviews[0].getReviewingOHIPPhysicianId()!=null) {
                    HRMreport.setReviewingOHIPPhysicianId(reportReviews[0].getReviewingOHIPPhysicianId());
                }
            }

            //ResultStatus
            if (report.getHRMResultStatus()!=null) HRMreport.setResultStatus(ResultStatus.Enum.forString(report.getHRMResultStatus()));

            //SendingFacility
            if (report.getSendingFacilityId()!=null) HRMreport.setSendingFacility(report.getSendingFacilityId());

            //SendingFacilityReportNumber
            if (report.getSendingFacilityReport()!=null) HRMreport.setSendingFacilityReportNumber(report.getSendingFacilityReport());

            //OBRConent
            ReportsReceivedDocument.ReportsReceived.OBRContent[] OBRs = report.getOBRContentArray();
            if (OBRs!=null && OBRs.length>0) {
                for (ReportsReceivedDocument.ReportsReceived.OBRContent OBR : OBRs) {
                    ReportsReceived.OBRContent HRMobr = HRMreport.addNewOBRContent();

                    if (OBR.getAccompanyingDescription()!=null) HRMobr.setAccompanyingDescription(OBR.getAccompanyingDescription());
                    if (OBR.getAccompanyingMnemonic()!=null) HRMobr.setAccompanyingMnemonic(OBR.getAccompanyingMnemonic());
                    if (OBR.getAccompanyingSubClass()!=null) HRMobr.setAccompanyingSubClass(OBR.getAccompanyingSubClass());
                    if (OBR.getObservationDateTime()!=null) {
                        copyDateFP(HRMobr.addNewObservationDateTime(), OBR.getObservationDateTime());
                    }
                }
            }

            //MessageUniqueID
            TransactionInformation transactionInfo = patientRecord.addNewTransactionInformation();
            if (report.getMessageUniqueID()!=null) transactionInfo.setMessageUniqueID(report.getMessageUniqueID());
            else transactionInfo.setMessageUniqueID("");
            transactionInfo.setDeliverToUserID("");
            PersonNameSimple physician = transactionInfo.addNewPhysician();
        }
    }




    static private void copyDateFP(DateFullOrPartial dfp, cdsDt.DateFullOrPartial idfp) {
        if (idfp.getFullDate()!=null) dfp.setFullDate(idfp.getFullDate());
        if (idfp.getYearMonth()!=null) dfp.setYearMonth(idfp.getYearMonth());
        if (idfp.getYearOnly()!=null) dfp.setYearOnly(idfp.getYearOnly());
    }

    static private void copyDateFP(DateFullOrPartial dfp, cdsDt.DateTimeFullOrPartial idfp) {
        if (idfp.getFullDateTime()!=null) dfp.setDateTime(idfp.getFullDateTime());
        if (idfp.getFullDate()!=null) dfp.setFullDate(idfp.getFullDate());
        if (idfp.getYearMonth()!=null) dfp.setYearMonth(idfp.getYearMonth());
        if (idfp.getYearOnly()!=null) dfp.setYearOnly(idfp.getYearOnly());
    }

    static private void copyPersonNameSimple(PersonNameSimple hrm, cdsDt.PersonNameSimple cds) {
        if (cds.getFirstName()!=null) hrm.setFirstName(cds.getFirstName());
        if (cds.getLastName()!=null) hrm.setLastName(cds.getLastName());
    }
}
