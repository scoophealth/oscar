/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarDemographic.pageUtil;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import cds.DemographicsDocument;
import cds.ReportsReceivedDocument;
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
import cdsDtHrm.PersonNameStandard;
import cdsDtHrm.PersonStatus;
import cdsDtHrm.ReportClass;
import cdsDtHrm.ReportContent;
import cdsDtHrm.ReportFormat;
import cdshrm.DemographicsDocument.Demographics;
import cdshrm.OmdCdsDocument;
import cdshrm.OmdCdsDocument.OmdCds;
import cdshrm.PatientRecordDocument.PatientRecord;
import cdshrm.ReportsReceivedDocument.ReportsReceived;
import cdshrm.ReportsReceivedDocument.ReportsReceived.ResultStatus;
import cdshrm.TransactionInformationDocument.TransactionInformation;
import java.util.HashMap;
import java.util.List;
import org.apache.xmlbeans.XmlOptions;

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
        suggestedPrefix.put("cds_dt_hrm","cdsd");
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
        cdsDt.PersonNameStandard.LegalName.FirstName firstName = legalName.getFirstName();
        cdsDt.PersonNameStandard.LegalName.LastName lastName = legalName.getLastName();
        cdsDt.PersonNamePurposeCode.Enum namePurpose = legalName.getNamePurpose();

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
        //Contacts
        //PhoneNumbers
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
            if (report.getFormat()!=null && report.getFormat().equals(cdsDt.ReportFormat.TEXT)) {
                HRMreport.setFormat(ReportFormat.TEXT);
            }

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
