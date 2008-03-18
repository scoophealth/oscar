/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * Send2IndivoAction.java
 *
 * Created on January 8, 2007, 4:39 PM
 * 
 */

package oscar.dms.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.phr.PHRConstants;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.service.PHRService;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarProvider.data.ProviderData;

/**
 *
 * @author rjonasz
 */ 
public class SendDocToPhrAction extends Action {
    
    PHRService phrService = null;
    PHRConstants phrConstants = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String[] files = request.getParameterValues("docNo");
        String curUser = request.getParameter("curUser");
        String error = null;
        System.out.println("SendDoctoPHRactionCalled!!!");
        if( files != null && curUser != null ) {
            
            System.out.println("Preparing to send " + files.length + " files");
            EDocUtil docData = new EDocUtil();                        
            
            DemographicData.Demographic demo = new DemographicData().getDemographic(request.getParameter("demoId"));
            ProviderData prov = new ProviderData(curUser);

                        
            for( int idx = 0; idx < files.length; ++idx ) {
                EDoc doc = docData.getDoc(files[idx]);
                try {
                    if(phrService.isIndivoRegistered(phrConstants.DOCTYPE_BINARYDATA(), doc.getDocId())) {                   
                        //update
                        String phrIndex = phrService.getPhrIndex(phrConstants.DOCTYPE_BINARYDATA(), doc.getDocId());
                        phrService.sendUpdateBinaryData(prov, demo.getChartNo(), PHRDocument.TYPE_DEMOGRAPHIC, demo.getPin(), doc, phrIndex);
                    } else {       
                        //add
                        phrService.sendAddBinaryData(prov, demo.getChartNo(), PHRDocument. TYPE_DEMOGRAPHIC, demo.getPin(), doc);
                    }
                    //throw new Exception("Error: Cannot marshal the document");
                } catch (Exception e) {
                    e.printStackTrace();
                    error = e.getMessage();
                }
            }
        
        }
        request.setAttribute("error_msg", error);
        return mapping.findForward("finished");
    }
    
    /** Creates a new instance of Send2IndivoAction */
    public SendDocToPhrAction() {
    }
    
    public void setPhrService(PHRService pServ){
        this.phrService = pServ;
    }
    
    public void setPhrConstants(PHRConstants pConst) {
        this.phrConstants = pConst;
    }
    
}
