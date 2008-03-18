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
public class EctAddMeasurementMapAction extends Action{
    
    Logger logger = Logger.getLogger(EctAddMeasurementMapAction.class);
    
    /** Creates a new instance of EctEditMeasurementMapAction */
    public EctAddMeasurementMapAction() {
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        String identifier = request.getParameter("identifier");
        String loinc_code = request.getParameter("loinc_code");
        String outcome = "continue";
        
        if (loinc_code != null && identifier != null){
            
            try{
                
                String[] measurement = identifier.split(",");
                MeasurementMapConfig mmc = new MeasurementMapConfig();
                if (!mmc.checkLoincMapping(loinc_code, measurement[1])){
                    mmc.mapMeasurement(measurement[0], loinc_code, measurement[2], measurement[1]);
                    outcome = "success";
                }else{
                    outcome = "failedcheck";
                }
                
            }catch(SQLException e){
                logger.error("Failed to create new measurement mapping", e);
                outcome = "failure";
            }
            
        }
        return mapping.findForward(outcome);
    }
    
}
