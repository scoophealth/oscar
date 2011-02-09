/*
 * EctEditMeasurementMapAction.java
 *
 * Created on September 29, 2007, 2:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.oscarEncounter.oscarMeasurements.data.MeasurementMapConfig;

/**
 *
 * @author wrighd
 */
public class EctRemoveMeasurementMapAction extends Action{
    
    Logger logger = Logger.getLogger(EctRemoveMeasurementMapAction.class);
    
    /** Creates a new instance of EctEditMeasurementMapAction */
    public EctRemoveMeasurementMapAction() {
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        String id = request.getParameter("id");
        String identifier = request.getParameter("identifier");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String loinc_code = request.getParameter("loinc_code");
        String outcome = "continue";
        
        if (id != null){
            
            try{
                MeasurementMapConfig mmc = new MeasurementMapConfig();
                
                // these values will be set if the measurement is to be remapped instead of deleted
                // therefore it is first mapped then deleted
                if ( identifier != null && name != null && type != null && loinc_code != null){
                    if (!mmc.checkLoincMapping(loinc_code, type)){
                        mmc.mapMeasurement(identifier, loinc_code, name, type);
                        mmc.removeMapping(id, request.getParameter("provider_no"));
                        outcome = "success";
                    }else{
                        outcome = "failedcheck";
                    }                    
                }else{
                    mmc.removeMapping(id, request.getParameter("provider_no"));
                    outcome = "success";
                }
            }catch(SQLException e){
                logger.error("Failed to delete measurement mapping");
                outcome = "failure";
            }
            
        }
        
        return mapping.findForward(outcome);
    }
    
}
