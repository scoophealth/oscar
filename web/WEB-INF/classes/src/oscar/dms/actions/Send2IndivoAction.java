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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarEncounter.data.EctProviderData;
import oscar.util.Send2Indivo;

/**
 *
 * @author rjonasz
 */ 
public class Send2IndivoAction extends Action{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String[] files = request.getParameterValues("docNo");
        String curUser = request.getParameter("curUser");
        ActionMessages errors = new ActionMessages();
        
        if( files != null && curUser != null ) {
            
            MiscUtils.getLogger().debug("Preparing to send " + files.length + " files");
            String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
            EDocUtil docData = new EDocUtil();                        
            
            DemographicData.Demographic demo = new DemographicData().getDemographic(request.getParameter("demoId"));
            String indivoPatientId = demo.getPin();
            MiscUtils.getLogger().debug("INDIVO RECIPIENT " + indivoPatientId);
            
            EctProviderData.Provider prov = new EctProviderData().getProvider(curUser);
            String fullname = prov.getFirstName() + " " + prov.getSurname();
            String role = "provider";
            
            Send2Indivo indivo = new Send2Indivo(prov.getIndivoId(),prov.getIndivoPasswd(), fullname, role);
            String indivoServer = OscarProperties.getInstance().getProperty("INDIVO_SERVER");           
            MiscUtils.getLogger().debug("SETTING INDIVO SERVER " + indivoServer);
            indivo.setServer(indivoServer);
            if( !indivo.authenticate() ) {
                errors.add("", new ActionMessage("indivo.authenticateError", indivo.getErrorMsg()));
                this.saveErrors(request, errors);
                return mapping.findForward("error");
            }
                        
            for( int idx = 0; idx < files.length; ++idx ) {
                String filename =  docData.getDocumentName(files[idx]);
                EDoc doc = docData.getDoc(files[idx]);
                String description = doc.getDescription();
                String type = doc.getType();
                
                if( doc.isInIndivo() ) {                   
                    if( !indivo.updateBinaryFile(path+filename, doc.getIndivoIdx(), type, description, indivoPatientId) ) {
                        errors.add("",new ActionMessage("indivo.sendbinFileError", description, indivo.getErrorMsg()));
                        this.saveErrors(request, errors);
                        return mapping.findForward("error");
                    }
                }
                else {                                        
                    if( !indivo.sendBinaryFile(path+filename, type, description, indivoPatientId) ) {
                        errors.add("",new ActionMessage("indivo.sendbinFileError", description, indivo.getErrorMsg()));
                        this.saveErrors(request, errors);
                        return mapping.findForward("error");
                    }
                    MiscUtils.getLogger().debug("Saving indivo Doc Idx " + indivo.getIndivoDocIdx());
                    doc.setIndivoIdx(indivo.getIndivoDocIdx());
                }
                
                EDocUtil.indivoRegister(doc);
            }                                               
            return mapping.findForward("success");
        
        }
        
        errors.add("",new ActionMessage("indivo.configError"));
        this.saveErrors(request, errors);
        return mapping.findForward("error");
    }
    
    /** Creates a new instance of Send2IndivoAction */
    public Send2IndivoAction() {
    }
    
}
