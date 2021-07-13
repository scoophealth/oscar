package org.oscarehr.integration.cdx;


import java.util.ArrayList;

import org.oscarehr.integration.cdx.dao.CdxMessengerAttachmentsDao;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.dms.EDocUtil;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;

/**
 *
 * @author rjonasz
 */
public class CDXMessengerAttachLabs {

    private static CdxMessengerAttachmentsDao cdxMessengerAttachmentsDao = SpringUtils.getBean(CdxMessengerAttachmentsDao.class);

    public final static boolean ATTACHED = true;
    public final static boolean UNATTACHED = false;
    private String providerNo;
    private String demoNo;
    private String reqId;
    private ArrayList<String> docs;

    /** Creates a new instance of ConsultationAttachLabs */
    public CDXMessengerAttachLabs(String demo, String req, String[] d) {
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
        ArrayList<LabResultData> oldlist = labResData.populateLabResultsDataCdxMessenger(loggedInInfo, demoNo,reqId,CommonLabResultData.ATTACHED);
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

            EDocUtil.detachCdxDoc((oldlist.get(i)).labPatientId, reqId,demoNo);
        }

        //now we can add association to new list
        for(int i = 0; i < newlist.size(); ++i)
            EDocUtil.attachCdxLabs(newlist.get(i), reqId,demoNo);
    }


}

