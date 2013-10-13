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
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.oscarehr.hospitalReportManager.xsd.DateFullOrPartial;
import org.oscarehr.hospitalReportManager.xsd.Demographics;
import org.oscarehr.hospitalReportManager.xsd.Gender;
import org.oscarehr.hospitalReportManager.xsd.HealthCard;
import org.oscarehr.hospitalReportManager.xsd.OfficialSpokenLanguageCode;
import org.oscarehr.hospitalReportManager.xsd.OmdCds;
import org.oscarehr.hospitalReportManager.xsd.PatientRecord;
import org.oscarehr.hospitalReportManager.xsd.PersonNamePartQualifierCode;
import org.oscarehr.hospitalReportManager.xsd.PersonNamePartTypeCode;
import org.oscarehr.hospitalReportManager.xsd.PersonNamePurposeCode;
import org.oscarehr.hospitalReportManager.xsd.PersonNameSimple;
import org.oscarehr.hospitalReportManager.xsd.PersonNameStandard;
import org.oscarehr.hospitalReportManager.xsd.PersonStatus;
import org.oscarehr.hospitalReportManager.xsd.ReportClass;
import org.oscarehr.hospitalReportManager.xsd.ReportContent;
import org.oscarehr.hospitalReportManager.xsd.ReportFormat;
import org.oscarehr.hospitalReportManager.xsd.ReportsReceived;
import org.oscarehr.hospitalReportManager.xsd.TransactionInformation;

import cds.DemographicsDocument;
import cds.ReportsReceivedDocument;

/**
 *
 * @author ronnie
 */
public class HRMCreateFile {

    static public void create(DemographicsDocument.Demographics demographic, List<ReportsReceivedDocument.ReportsReceived> reports, String file) throws JAXBException, DatatypeConfigurationException {


        JAXBContext jc = JAXBContext.newInstance("org.oscarehr.hospitalReportManager.xsd");
        Marshaller u = jc.createMarshaller();

        OmdCds omdCds = new OmdCds();
        PatientRecord patientRecord = new PatientRecord();
        omdCds.setPatientRecord(patientRecord);

        Demographics HRMdemo = new Demographics();
        patientRecord.setDemographics(HRMdemo);
        List<ReportsReceived> HRMreports = patientRecord.getReportsReceived();
        List<TransactionInformation> HRMinfos = patientRecord.getTransactionInformation();

        writeDemographics(demographic, HRMdemo);
        writeReportsReceived(reports, HRMreports, HRMinfos);

        u.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        u.marshal(omdCds, new File(file));
    }

    static private void writeDemographics(DemographicsDocument.Demographics demo, Demographics HRMdemo) throws DatatypeConfigurationException {
        //Names
        cdsDt.PersonNameStandard personName = demo.getNames();
        cdsDt.PersonNameStandard.LegalName legalName = personName.getLegalName();
        cdsDt.PersonNameStandard.LegalName.FirstName firstName = legalName.getFirstName();
        cdsDt.PersonNameStandard.LegalName.LastName lastName = legalName.getLastName();
        cdsDt.PersonNamePurposeCode.Enum namePurpose = legalName.getNamePurpose();

        PersonNameStandard HRMpersonName = new PersonNameStandard();
        PersonNameStandard.LegalName HRMlegalName = new PersonNameStandard.LegalName();
        PersonNameStandard.LegalName.FirstName HRMfirstName = new PersonNameStandard.LegalName.FirstName();
        PersonNameStandard.LegalName.LastName HRMlastName = new PersonNameStandard.LegalName.LastName();

        HRMfirstName.setPart(firstName.getPart());
        HRMfirstName.setPartType(PersonNamePartTypeCode.GIV);
        HRMfirstName.setPartQualifier(PersonNamePartQualifierCode.BR);

        HRMlastName.setPart(lastName.getPart());
        HRMlastName.setPartType(PersonNamePartTypeCode.FAMC);
        HRMlastName.setPartQualifier(PersonNamePartQualifierCode.BR);

        HRMlegalName.setFirstName(HRMfirstName);
        HRMlegalName.setLastName(HRMlastName);
        if (namePurpose!=null) HRMlegalName.setNamePurpose(PersonNamePurposeCode.fromValue(namePurpose.toString()));

        HRMpersonName.setLegalName(HRMlegalName);
        HRMdemo.setNames(HRMpersonName);

        //Gender
        cdsDt.Gender.Enum gender = demo.getGender();
        if (gender!=null) HRMdemo.setGender(Gender.fromValue(gender.toString()));

        //ChartNumber
        HRMdemo.setChartNumber(demo.getChartNumber());

        //SIN
        HRMdemo.setSIN(demo.getSIN());

        //UniqueVendorIdSequence
        HRMdemo.setUniqueVendorIdSequence(demo.getUniqueVendorIdSequence());

        //Email
        HRMdemo.setEmail(demo.getEmail());

        //NoteAboutPatient
        HRMdemo.setNoteAboutPatient(demo.getNoteAboutPatient());

        //PreferredLanguages
        cdsDt.OfficialSpokenLanguageCode.Enum officialLang = demo.getPreferredOfficialLanguage();
        if (officialLang!=null) HRMdemo.setPreferredOfficialLanguage(OfficialSpokenLanguageCode.fromValue(officialLang.toString()));

        HRMdemo.setPreferredSpokenLanguage(demo.getPreferredSpokenLanguage());

        //DateOfBirth
        if (demo.getDateOfBirth()!=null) HRMdemo.setDateOfBirth(getDateFP(demo.getDateOfBirth()));

        //PersonStatus
        cdsDt.PersonStatus.Enum personStatus = demo.getPersonStatusCode().getPersonStatusAsEnum();
        if (personStatus!=null) HRMdemo.setPersonStatusCode(PersonStatus.fromValue(personStatus.toString()));
        else HRMdemo.setPersonStatusCode(PersonStatus.I);

        if (demo.getPersonStatusDate()!=null) HRMdemo.setPersonStatusDate(getDateFP(demo.getPersonStatusDate()));

        //EnrollmentStatus
        DemographicsDocument.Demographics.Enrolment[] enrolments = demo.getEnrolmentArray();
        if (enrolments!=null && enrolments.length>0) {
            cdsDt.EnrollmentStatus.Enum enrollmentStatus = enrolments[0].getEnrollmentStatus();
            if (enrollmentStatus!=null) HRMdemo.setEnrollmentStatus(enrollmentStatus.toString());

            if (enrolments[0].getEnrollmentDate()!=null) HRMdemo.setEnrollmentDate(getDateFP(enrolments[0].getEnrollmentDate()));
            if (enrolments[0].getEnrollmentTerminationDate()!=null) HRMdemo.setEnrollmentTerminationDate(getDateFP(enrolments[0].getEnrollmentTerminationDate()));
        }

        //HealhCard
        cdsDt.HealthCard healthCard = demo.getHealthCard();
        if (healthCard!=null) {
            HealthCard HRMhealthCard = new HealthCard();
            HRMhealthCard.setNumber(healthCard.getNumber());
            HRMhealthCard.setVersion(healthCard.getVersion());
            if (healthCard.getProvinceCode()!=null) HRMhealthCard.setProvinceCode(healthCard.getProvinceCode().toString());
            if (healthCard.getExpirydate()!=null) HRMhealthCard.setExpirydate(getXgc(healthCard.getExpirydate()));

            HRMdemo.setHealthCard(HRMhealthCard);
        }

        //PrimaryPhysician
        DemographicsDocument.Demographics.PrimaryPhysician primaryPhysician = demo.getPrimaryPhysician();
        if (primaryPhysician!=null) {
            Demographics.PrimaryPhysician HRMprimaryPhysician = new Demographics.PrimaryPhysician();
            if (primaryPhysician.getName()!=null) {
                HRMprimaryPhysician.setName(copyPersonNameSimple(primaryPhysician.getName()));
            }
            HRMprimaryPhysician.setOHIPPhysicianId(primaryPhysician.getOHIPPhysicianId());
            HRMdemo.setPrimaryPhysician(HRMprimaryPhysician);
        }

        //Addresses
        //Contacts
        //PhoneNumbers
    }

    static private void writeReportsReceived(List<ReportsReceivedDocument.ReportsReceived> reports, List<ReportsReceived> HRMreports, List<TransactionInformation> HRMinfos) throws DatatypeConfigurationException {
        for (ReportsReceivedDocument.ReportsReceived report : reports) {
            ReportsReceived HRMreport = new ReportsReceived();
            HRMreports.add(HRMreport);

            //AuthorPhysician
            ReportsReceivedDocument.ReportsReceived.SourceAuthorPhysician authorPhysician = report.getSourceAuthorPhysician();
            if (authorPhysician!=null && authorPhysician.getAuthorName()!=null) {
                HRMreport.setAuthorPhysician(copyPersonNameSimple(authorPhysician.getAuthorName()));
            }

            //ReportClass
            if (report.getClass1()!=null) HRMreport.setClazz(ReportClass.fromValue(report.getClass1().toString()));

            //SubClass
            HRMreport.setSubClass(report.getSubClass());

            //Content
            cdsDt.ReportContent reportContent = report.getContent();
            if (reportContent!=null && reportContent.getTextContent()!=null) {
                ReportContent HRMreportContent = new ReportContent();
                HRMreportContent.setTextContent(reportContent.getTextContent());
                HRMreport.setContent(HRMreportContent);
            }

            //FileExtensionAndVersion
            HRMreport.setFileExtensionAndVersion(report.getFileExtensionAndVersion());

            //Format
            if (report.getFormat()!=null) {
                if (report.getFormat().equals(cdsDt.ReportFormat.TEXT)) {
                    HRMreport.setFormat(ReportFormat.TEXT);
                }
            }

            //EventDateTime
            if (report.getEventDateTime()!=null) HRMreport.setEventDateTime(getDateFP(report.getEventDateTime()));

            //ReceivedDateTime
            if (report.getReceivedDateTime()!=null) HRMreport.setReceivedDateTime(getDateFP(report.getReceivedDateTime()));

            //Reviews
            ReportsReceivedDocument.ReportsReceived.ReportReviewed[] reportReviews = report.getReportReviewedArray();
            if (reportReviews!=null && reportReviews.length>0) {
                if (reportReviews[0].getDateTimeReportReviewed()!=null) {
                    HRMreport.setReviewedDateTime(getDateFP(reportReviews[0].getDateTimeReportReviewed()));
                }

                HRMreport.setReviewingOHIPPhysicianId(reportReviews[0].getReviewingOHIPPhysicianId());
            }

            //ResultStatus
            HRMreport.setResultStatus(report.getHRMResultStatus());

            //SendingFacility
            HRMreport.setSendingFacility(report.getSendingFacilityId());

            //SendingFacilityReportNumber
            HRMreport.setSendingFacilityReportNumber(report.getSendingFacilityReport());

            //OBRConent
            ReportsReceivedDocument.ReportsReceived.OBRContent[] OBRs = report.getOBRContentArray();
            if (OBRs!=null) {
                List<ReportsReceived.OBRContent> HRMobrs = HRMreport.getOBRContent();
                for (ReportsReceivedDocument.ReportsReceived.OBRContent OBR : OBRs) {
                    ReportsReceived.OBRContent HRMobr = new ReportsReceived.OBRContent();
                    HRMobrs.add(HRMobr);

                    HRMobr.setAccompanyingDescription(OBR.getAccompanyingDescription());
                    HRMobr.setAccompanyingMnemonic(OBR.getAccompanyingMnemonic());
                    HRMobr.setAccompanyingSubClass(OBR.getAccompanyingSubClass());
                    if (OBR.getObservationDateTime()!=null) {
                        HRMobr.setObservationDateTime(getDateFP(OBR.getObservationDateTime()));
                    }
                }
            }

            //MessageUniqueID
            TransactionInformation transactionInfo = new TransactionInformation();
            HRMinfos.add(transactionInfo);
            transactionInfo.setMessageUniqueID(report.getMessageUniqueID());
            transactionInfo.setDeliverToUserID("");
            PersonNameSimple physician = new PersonNameSimple();
            transactionInfo.setProvider(physician);
        }
    }




    static private DateFullOrPartial getDateFP(cdsDt.DateFullOrPartial idfp) throws DatatypeConfigurationException {
        Calendar cal = idfp.getFullDate();
        if (cal==null) cal = idfp.getYearMonth();
        if (cal==null) cal = idfp.getYearOnly();

        return getDateFP(cal);
    }

    static private DateFullOrPartial getDateFP(cdsDt.DateTimeFullOrPartial idfp) throws DatatypeConfigurationException {
        Calendar cal = idfp.getFullDate();
        if (cal==null) cal = idfp.getFullDateTime();
        if (cal==null) cal = idfp.getYearMonth();
        if (cal==null) cal = idfp.getYearOnly();

        return getDateFP(cal);
    }

    static private DateFullOrPartial getDateFP(Calendar cal) throws DatatypeConfigurationException {
        XMLGregorianCalendar xgc = getXgc(cal);
        DateFullOrPartial dfp = new DateFullOrPartial();
        dfp.setDateTime(xgc);

        return dfp;
    }

    static private XMLGregorianCalendar getXgc(Calendar cal) throws DatatypeConfigurationException {
        DatatypeFactory dtf = DatatypeFactory.newInstance();
        XMLGregorianCalendar xgc = dtf.newXMLGregorianCalendar();

        xgc.setYear(cal.get(Calendar.YEAR));
        xgc.setMonth(cal.get(Calendar.MONTH)+1);
        xgc.setDay(cal.get(Calendar.DAY_OF_MONTH));
        xgc.setHour(cal.get(Calendar.HOUR_OF_DAY));
        xgc.setMinute(cal.get(Calendar.MINUTE));
        xgc.setSecond(cal.get(Calendar.SECOND));

        return xgc;
    }

    static private PersonNameSimple copyPersonNameSimple(cdsDt.PersonNameSimple cds) {
        PersonNameSimple hrm = new PersonNameSimple();
        hrm.setFirstName(cds.getFirstName());
        hrm.setLastName(cds.getLastName());

        return hrm;
    }
}
