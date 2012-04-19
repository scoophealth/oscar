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
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import org.oscarehr.hospitalReportManager.xsd.DateFullOrPartial;
import org.oscarehr.hospitalReportManager.xsd.OmdCds;
import org.oscarehr.hospitalReportManager.xsd.PatientRecord;
import org.oscarehr.hospitalReportManager.xsd.PersonNameSimple;
import org.oscarehr.hospitalReportManager.xsd.ReportClass;
import org.oscarehr.hospitalReportManager.xsd.ReportContent;
import org.oscarehr.hospitalReportManager.xsd.ReportFormat;
import org.oscarehr.hospitalReportManager.xsd.ReportMedia;
import org.oscarehr.hospitalReportManager.xsd.ReportsReceived;
import org.oscarehr.hospitalReportManager.xsd.ReportsReceived.OBRContent;
import org.oscarehr.hospitalReportManager.xsd.TransactionInformation;


/**
 *
 * @author ronnie
 */
public class ReadHRMFile {
    private List<ReportsReceived> reportsReceived = null;
    private List<TransactionInformation> transactionInformation = null;
/*
    private Demographics demographics = null;
    private ReportsReceived[] reportsReceived = null;
    private TransactionInformation[] transactionInformation = null;
 *
 */

    public ReadHRMFile(String hrmFile) {
        try {
            if (hrmFile == null) {
                return;
            }
            File hrm = new File(hrmFile);
            if (!hrm.exists()) {
                return;
            }
            JAXBContext jc = JAXBContext.newInstance("org.oscarehr.hospitalReportManager.xsd");
            Unmarshaller u = jc.createUnmarshaller();
            OmdCds root = (OmdCds) u.unmarshal(hrm);

            PatientRecord pr = root.getPatientRecord();
            reportsReceived = pr.getReportsReceived();
            transactionInformation = pr.getTransactionInformation();
            
        } catch (JAXBException ex) {
            Logger.getLogger(ReadHRMFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getReportsReceivedTotal() {
        if (reportsReceived==null) return 0;
        return reportsReceived.size();
    }

    public HashMap<String,String> getReportAuthorPhysician(int r) {
        HashMap<String,String> authorPhysician = new HashMap<String,String>();
        if (getReportsReceived(r)==null) return authorPhysician;

        PersonNameSimple author = getReportsReceived(r).getAuthorPhysician();
        if (author!=null) {
            authorPhysician.put("firstname", author.getFirstName());
            authorPhysician.put("lastname", author.getLastName());
        }
        return authorPhysician;
    }

    public HashMap<String,String> getReportStrings(int r) {
        HashMap<String,String> strings = new HashMap<String,String>();
        if (getReportsReceived(r)==null) return strings;

        ReportsReceived rp = getReportsReceived(r);

        ReportClass rptClass = rp.getClazz();
        String subClass = rp.getSubClass();
        String ext = rp.getFileExtensionAndVersion();
        ReportFormat format = rp.getFormat();
        ReportMedia media = rp.getMedia();
        String resultStatus = rp.getResultStatus();
        String reviewerId = rp.getReviewingOHIPPhysicianId();
        String sendingFacility = rp.getSendingFacility();
        String sendingFacilityRptNum = rp.getSendingFacilityReportNumber();

        if (rptClass!=null) strings.put("class", rptClass.toString());
        if (subClass!=null) strings.put("subclass", subClass);
        if (ext!=null) strings.put("fileextension&version", ext);
        if (format!=null) strings.put("format", format.toString());
        if (media!=null) strings.put("media", media.toString());
        if (resultStatus!=null) strings.put("resultstatus", resultStatus.toString());
        if (reviewerId!=null) strings.put("reviewingohipphysicianid", reviewerId);
        if (sendingFacility!=null) strings.put("sendingfacility", sendingFacility);
        if (sendingFacilityRptNum!=null) strings.put("sendingfacilityreportnumber", sendingFacilityRptNum);

        return strings;
    }

    public HashMap<String,Calendar> getReportDates(int r) {
        HashMap<String,Calendar> dates = new HashMap<String,Calendar>();
        if (getReportsReceived(r)==null) return dates;

        ReportsReceived rp = getReportsReceived(r);

        DateFullOrPartial eventDateTime = rp.getEventDateTime();
        DateFullOrPartial receivedDateTime = rp.getReceivedDateTime();
        DateFullOrPartial reviewedDateTime = rp.getReviewedDateTime();

        if (dateFPtoCal(eventDateTime)!=null) dates.put("eventdatetime", dateFPtoCal(eventDateTime));
        if (dateFPtoCal(receivedDateTime)!=null) dates.put("receiveddatetime", dateFPtoCal(receivedDateTime));
        if (dateFPtoCal(reviewedDateTime)!=null) dates.put("revieweddatetime", dateFPtoCal(reviewedDateTime));

        return dates;
    }

    public HashMap<String,Object> getReportContent(int r) {
        HashMap<String,Object> rptContent = new HashMap<String,Object>();
        if (getReportsReceived(r)==null) return rptContent;

        ReportContent content = getReportsReceived(r).getContent();
        if (content==null) return null;

        if (content.getTextContent()!=null) rptContent.put("textcontent", content.getTextContent());
        else if (content.getMedia()!=null) rptContent.put("media", content.getMedia());

        return rptContent;
    }

    public OBRContent getReportOBRContent(int r, int o) {
        if (getReportsReceived(r)==null) return null;

        List<OBRContent> obr = getReportsReceived(r).getOBRContent();
        if (obr.size()<=r) return null;

        return obr.get(o);
    }

    public int getReportOBRContentTotal(int r) {
        if (getReportsReceived(r)==null) return 0;
        return getReportsReceived(r).getOBRContent().size();
    }

    public HashMap<String,String> getReportOBRStrings(int r, int o) {
        HashMap<String,String> strings = new HashMap<String,String>();
        if (getReportOBRContent(r,o)==null) return strings;

        OBRContent obr = getReportOBRContent(r, o);
        
        String description = obr.getAccompanyingDescription();
        String mnemonic = obr.getAccompanyingMnemonic();
        String subclass = obr.getAccompanyingSubClass();

        if (description!=null) strings.put("accompanyingdescription", description);
        if (mnemonic!=null) strings.put("accompanyingmnemonic", mnemonic);
        if (subclass!=null) strings.put("accompanyingsubclass", subclass);

        return strings;
    }

    public Calendar getReportOBRObservationDateTime(int r, int o) {
        if (getReportOBRContent(r,o)==null) return null;

        return dateFPtoCal(getReportOBRContent(r,o).getObservationDateTime());
    }

    public String getTransactionMessageUniqueID(int r) {
        if (transactionInformation==null) return null;
        if (transactionInformation.size()<=r) return null;

        return transactionInformation.get(r).getMessageUniqueID();
    }

    public ReportsReceived getReportsReceived(int r) {
        if (reportsReceived==null) return null;
        if (reportsReceived.size()<=r) return null;
        return reportsReceived.get(r);
    }



    private Calendar dateFPtoCal(DateFullOrPartial dfp) {
        if (dfp==null) return null;

        XMLGregorianCalendar xgc = dfp.getDateTime();
        if (xgc==null) xgc = dfp.getFullDate();
        if (xgc==null) xgc = dfp.getYearMonth();
        if (xgc==null) xgc = dfp.getYearOnly();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(xgc.toGregorianCalendar().getTimeInMillis());

        return cal;
    }
}
