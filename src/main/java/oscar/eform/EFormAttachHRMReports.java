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


package oscar.eform;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.EFormDocsDao;
import org.oscarehr.common.model.EFormDocs;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

/**
 *
 * @author rjonasz
 */
public class EFormAttachHRMReports {

	private static EFormDocsDao eformDocsDao = SpringUtils.getBean(EFormDocsDao.class);
	private static HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao)SpringUtils.getBean("HRMDocumentToDemographicDao");
	
    public final static boolean ATTACHED = true;
    public final static boolean UNATTACHED = false;
    private String providerNumber;
    private String demographicNumber;
    private String consultationId;
    private ArrayList<String> hrmReports;

    /** Creates a new instance of ConsultationAttachLabs */
    public EFormAttachHRMReports(String providerNumberToAttach, String demographicNumberToAttach, String consultationIdToAttach, String[] hrmReportsToAttach) {
        providerNumber = providerNumberToAttach;
        demographicNumber = demographicNumberToAttach;
        consultationId = consultationIdToAttach;
        hrmReports = new ArrayList<String>(hrmReportsToAttach.length);

        if (OscarProperties.getInstance().isPropertyActive("consultation_indivica_attachment_enabled")) {
	        for(int index = 0; index < hrmReportsToAttach.length; ++index ) {
	            hrmReports.add(hrmReportsToAttach[index]);
	        }
        }
        else {
	        //if dummy entry skip
	        if( !hrmReportsToAttach[0].equals("0") ) {
	            for(int index = 0; index < hrmReportsToAttach.length; ++index ) {
	                if( hrmReportsToAttach[index].charAt(0) == 'H')
	                    hrmReports.add(hrmReportsToAttach[index].substring(1));
	            }
	        }
        }
    }

    public void attach() {

        //Gets a list of already attached hrmDocumentToDemographic objects
        List<HRMDocumentToDemographic> oldList = hrmDocumentToDemographicDao.findHRMDocumentsAttachedToEForm(consultationId);
        List<String> newList = new ArrayList<String>();
        List<HRMDocumentToDemographic> keepList = new ArrayList<HRMDocumentToDemographic>();
        
        boolean alreadyAttached;
        //For each of the reports in the hrmReports
        for(int hrmReportIndex = 0; hrmReportIndex < hrmReports.size(); ++hrmReportIndex) {
        	//Sets already attached to false
            alreadyAttached = false;
            //For each item in the oldList, compares it to the current hrmReport and if they match then it gets added to the keepList
            for(int oldListIndex = 0; oldListIndex < oldList.size(); ++oldListIndex) {
                if( (oldList.get(oldListIndex)).getHrmDocumentId().equals(hrmReports.get(hrmReportIndex)) ) {
                    alreadyAttached = true;
                    keepList.add(oldList.get(oldListIndex));
                }
            }
            //If the report is not already attached then attaches it to the report
            if( !alreadyAttached )
                newList.add(hrmReports.get(hrmReportIndex));
        }

        //Compares the oldList and the keepList to see which one are being kept.
        for(int index = 0; index < oldList.size(); ++index) {
        	//If the item from the oldList is not in the keepList, then it sets it as deleted
            if(!keepList.contains(oldList.get(index)))
            	detachHRMReportConsult((oldList.get(index)).getHrmDocumentId(), consultationId);
        }

        //Attaches all the items in the newList 
        for(int index = 0; index < newList.size(); ++index)
            attachHRMReportConsult(providerNumber, newList.get(index), consultationId);
    }

    public static void detachHRMReportConsult(String hrmDocumentNumber, String consultationId) {
    	//Selects all of the consultDocs for the given consultation id and hrm document number
    	List<EFormDocs> consultDocs = eformDocsDao.findByFdidIdDocNoDocType(Integer.parseInt(consultationId), Integer.parseInt(hrmDocumentNumber), "H");
    	//For each consultDoc in the list
    	for(EFormDocs consultDoc : consultDocs) {
    		//Sets deleted to yea and updates the record in the database
    		consultDoc.setDeleted("Y");
    		eformDocsDao.merge(consultDoc);
    	}
    }

    public static void attachHRMReportConsult(String providerNo, String hrmDocumentNumber, String consultationId) {
    	//Creates a new consultDoc and sets it's attributes
    	EFormDocs consultDoc = new EFormDocs();
    	consultDoc.setFdid(Integer.parseInt(consultationId));
    	consultDoc.setDocumentNo(Integer.parseInt(hrmDocumentNumber));
    	consultDoc.setDocType("H");
    	consultDoc.setAttachDate(new Date());
    	consultDoc.setProviderNo(providerNo);
    	//Saved the new consult doc
    	eformDocsDao.persist(consultDoc);
    }


}
