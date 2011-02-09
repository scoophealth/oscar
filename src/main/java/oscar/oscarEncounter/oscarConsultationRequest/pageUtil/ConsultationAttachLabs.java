/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.sql.SQLException;
import java.util.ArrayList;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;

/**
 *
 * @author rjonasz
 */
public class ConsultationAttachLabs {
    
    public final static boolean ATTACHED = true;
    public final static boolean UNATTACHED = false;
    private String providerNo;
    private String demoNo;    
    private String reqId;
    private ArrayList docs;
    
    /** Creates a new instance of ConsultationAttachLabs */
    public ConsultationAttachLabs(String provNo, String demo, String req, String[] d) {
        providerNo = provNo;
        demoNo = demo;
        reqId = req;
        docs = new ArrayList(d.length);
        
        //if dummy entry skip
        if( !d[0].equals("0") ) {
            for(int idx = 0; idx < d.length; ++idx ) {
                if( d[idx].charAt(0) == 'L')
                    docs.add(d[idx].substring(1));
            }
        }     
    }
    
    public void attach() {
        
        //first we get a list of currently attached labs
        CommonLabResultData labResData = new CommonLabResultData();
        ArrayList oldlist = labResData.populateLabResultsData(demoNo,reqId,CommonLabResultData.ATTACHED);
        ArrayList newlist = new ArrayList();
        ArrayList keeplist = new ArrayList();
        boolean alreadyAttached;
        //add new documents to list and get ids of docs to keep attached
        for(int i = 0; i < docs.size(); ++i) {
            alreadyAttached = false;
            for(int j = 0; j < oldlist.size(); ++j) {                
                if( ((LabResultData)oldlist.get(j)).labPatientId.equals((String)docs.get(i)) ) {                    
                    alreadyAttached = true;
                    keeplist.add((LabResultData)oldlist.get(j));
                    break;
                }
            }
            if( !alreadyAttached )
                newlist.add((String)docs.get(i));
        }
        
        //now compare what we need to keep with what we have and remove association
        for(int i = 0; i < oldlist.size(); ++i) {
            if( keeplist.contains(oldlist.get(i)))                
                continue;
                        
           detachLabConsult(((LabResultData)oldlist.get(i)).labPatientId, reqId);
        }
        
        //now we can add association to new list
        for(int i = 0; i < newlist.size(); ++i)
            attachLabConsult(providerNo, (String)newlist.get(i), reqId);
    }
    
    public static void detachLabConsult(String LabNo, String consultId) {
        String sql = "UPDATE consultdocs SET deleted = 'Y' WHERE requestId = " + consultId + " AND document_no = " + LabNo + " AND doctype = 'L'";                     
        MiscUtils.getLogger().debug("detachDoc: " + sql);
        
        try {
            
            DBHandler.RunSQL(sql);
        }
        catch(SQLException e) {
            MiscUtils.getLogger().debug("Error detaching lab: " + e.getMessage());
        }
        
    }
    
    public static void attachLabConsult(String providerNo, String LabNo, String consultId) {
        String sql = "INSERT INTO consultdocs (requestId,document_no,doctype,attach_date,provider_no) VALUES(" + consultId + "," + LabNo + ",'L',now(),'" + providerNo + "')";
        MiscUtils.getLogger().debug("attachLab: " + sql);
        
        try {
            
            DBHandler.RunSQL(sql);
        }
        catch(SQLException e) {
            MiscUtils.getLogger().debug("Error attaching lab: " + e.getMessage());
        }
        
    }
  
    
}
