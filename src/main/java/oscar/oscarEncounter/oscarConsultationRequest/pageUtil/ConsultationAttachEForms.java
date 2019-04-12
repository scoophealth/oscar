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


package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import org.oscarehr.common.dao.ConsultDocsDao;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import oscar.OscarProperties;
import oscar.eform.EFormUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rjonasz
 */
public class ConsultationAttachEForms {

	private static ConsultDocsDao consultDocsDao = SpringUtils.getBean(ConsultDocsDao.class);
    public final static boolean ATTACHED = true;
    public final static boolean UNATTACHED = false;
    private String providerNumber;
    private String consultationId;
    private ArrayList<String> eForms;

    /** Creates a new instance of ConsultationAttachEForms */
    public ConsultationAttachEForms(String provNo, String demo, String req, String[] d) {
        providerNumber = provNo;
        consultationId = req;
        eForms = new ArrayList<String>(d.length);

        if (OscarProperties.getInstance().isPropertyActive("consultation_indivica_attachment_enabled")) {
	        for(int idx = 0; idx < d.length; ++idx ) {
	            eForms.add(d[idx]);
	        }
        }
        else {
	        //if dummy entry skip
	        if( !d[0].equals("0") ) {
	            for(int idx = 0; idx < d.length; ++idx ) {
	                if( d[idx].charAt(0) == 'L')
	                    eForms.add(d[idx].substring(1));
	            }
	        }
        }
    }

    public void attach(LoggedInInfo loggedInInfo) {

        List<EFormData> oldList = EFormUtil.listPatientEformsCurrentAttachedToConsult(consultationId);
                
        ArrayList<String> newList = new ArrayList<String>();
        ArrayList<EFormData> keepList = new ArrayList<EFormData>();
        
        boolean alreadyAttached;
        //For each of the eforms in the eforms array
        for(int formsIndex = 0; formsIndex < eForms.size(); ++formsIndex) {
            //Sets already attached to false
            alreadyAttached = false;
            //For each item in the oldList, compares it to the current eform and if they match then it gets added to the keepList
            for(int oldListIndex = 0; oldListIndex < oldList.size(); ++oldListIndex) {
                if( (oldList.get(oldListIndex)).getId().equals(eForms.get(formsIndex)) ) {
                    alreadyAttached = true;
                    keepList.add(oldList.get(oldListIndex));
                }
            }
            //If the eform is not already attached then attaches it to the report
            if( !alreadyAttached )
                newList.add(eForms.get(formsIndex));
        }

        //Compares the oldList and the keepList to see which one are being kept.
        for(int index = 0; index < oldList.size(); ++index) {
            //If the item from the oldList is not in the keepList, then it sets it as deleted
            if(!keepList.contains(oldList.get(index)))
                detachFormConsult(oldList.get(index).getId().toString(), consultationId);
        }
        //Attaches all the items in the newList 
        for(int index = 0; index < newList.size(); ++index)
            attachFormConsult(providerNumber, newList.get(index), consultationId);
    }

    public static void detachFormConsult(String LabNo, String consultId) {
    	List<ConsultDocs> consultDocs = consultDocsDao.findByRequestIdDocNoDocType(Integer.parseInt(consultId), Integer.parseInt(LabNo), ConsultDocs.DOCTYPE_EFORM);
    	for(ConsultDocs consultDoc:consultDocs) {
    		consultDoc.setDeleted("Y");
    		consultDocsDao.merge(consultDoc);
    	}
    }

    public static void attachFormConsult(String providerNo, String eFormNo, String consultId) {
    	ConsultDocs consultDoc = new ConsultDocs();
    	consultDoc.setRequestId(Integer.parseInt(consultId));
    	consultDoc.setDocumentNo(Integer.parseInt(eFormNo));
    	consultDoc.setDocType(ConsultDocs.DOCTYPE_EFORM);
    	consultDoc.setAttachDate(new Date());
    	consultDoc.setProviderNo(providerNo);
    	consultDocsDao.persist(consultDoc);
    }


}
