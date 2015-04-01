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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.ConsultDocsDao;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;

/**
 *
 * @author rjonasz
 */
public class ConsultationAttachLabs {

	private static ConsultDocsDao consultDocsDao = (ConsultDocsDao)SpringUtils.getBean("consultDocsDao");

    public final static boolean ATTACHED = true;
    public final static boolean UNATTACHED = false;
    private String providerNo;
    private String demoNo;
    private String reqId;
    private ArrayList<String> docs;

    /** Creates a new instance of ConsultationAttachLabs */
    public ConsultationAttachLabs(String provNo, String demo, String req, String[] d) {
        providerNo = provNo;
        demoNo = demo;
        reqId = req;
        docs = new ArrayList<String>(d.length);

        if (OscarProperties.getInstance().isPropertyActive("consultation_indivica_attachment_enabled")) {
	        for(int idx = 0; idx < d.length; ++idx ) {
	            docs.add(d[idx]);
	        }
        }
        else {
	        //if dummy entry skip
	        if( !d[0].equals("0") ) {
	            for(int idx = 0; idx < d.length; ++idx ) {
	                if( d[idx].charAt(0) == 'L')
	                    docs.add(d[idx].substring(1));
	            }
	        }
        }
    }

    public void attach(LoggedInInfo loggedInInfo) {

        //first we get a list of currently attached labs
        CommonLabResultData labResData = new CommonLabResultData();
        ArrayList<LabResultData> oldlist = labResData.populateLabResultsData(loggedInInfo, demoNo,reqId,CommonLabResultData.ATTACHED);
        ArrayList<String> newlist = new ArrayList<String>();
        ArrayList<LabResultData> keeplist = new ArrayList<LabResultData>();
        boolean alreadyAttached;
        //add new documents to list and get ids of docs to keep attached
        for(int i = 0; i < docs.size(); ++i) {
            alreadyAttached = false;
            for(int j = 0; j < oldlist.size(); ++j) {
                if( (oldlist.get(j)).labPatientId.equals(docs.get(i)) ) {
                    alreadyAttached = true;
                    keeplist.add(oldlist.get(j));
                    break;
                }
            }
            if( !alreadyAttached )
                newlist.add(docs.get(i));
        }

        //now compare what we need to keep with what we have and remove association
        for(int i = 0; i < oldlist.size(); ++i) {
            if( keeplist.contains(oldlist.get(i)))
                continue;

           detachLabConsult((oldlist.get(i)).labPatientId, reqId);
        }

        //now we can add association to new list
        for(int i = 0; i < newlist.size(); ++i)
            attachLabConsult(providerNo, newlist.get(i), reqId);
    }

    public static void detachLabConsult(String LabNo, String consultId) {
    	List<ConsultDocs> consultDocs = consultDocsDao.findByRequestIdDocNoDocType(Integer.parseInt(consultId), Integer.parseInt(LabNo), ConsultDocs.DOCTYPE_LAB);
    	for(ConsultDocs consultDoc:consultDocs) {
    		consultDoc.setDeleted("Y");
    		consultDocsDao.merge(consultDoc);
    	}
    }

    public static void attachLabConsult(String providerNo, String LabNo, String consultId) {
    	ConsultDocs consultDoc = new ConsultDocs();
    	consultDoc.setRequestId(Integer.parseInt(consultId));
    	consultDoc.setDocumentNo(Integer.parseInt(LabNo));
    	consultDoc.setDocType(ConsultDocs.DOCTYPE_LAB);
    	consultDoc.setAttachDate(new Date());
    	consultDoc.setProviderNo(providerNo);
    	consultDocsDao.persist(consultDoc);
    }


}
