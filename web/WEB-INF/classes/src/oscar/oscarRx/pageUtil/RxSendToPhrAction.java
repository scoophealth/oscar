/*
 * RxSendToPhr.java
 *
 * Created on June 12, 2007, 2:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarRx.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.phr.PHRConstants;
import org.oscarehr.phr.service.PHRService;

import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarEncounter.data.EctProviderData;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPrescriptionData.Prescription;
/**
 *
 * @author apavel
 */
public class RxSendToPhrAction extends Action {
    
    PHRService phrService = null;
    PHRConstants phrConstants = null;
    
    /** Creates a new instance of RxSendToPhr */
    public RxSendToPhrAction() {
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //boolean errors = false;
        String errorMsg = null;
        
        
        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
        //authenticate
        EctProviderData.Provider prov = new EctProviderData().getProvider(bean.getProviderNo());

        //get demographic id
        DemographicData demoData = new DemographicData();
        String patientMyOscarId = demoData.getDemographic(""+bean.getDemographicNo()).getPin();
        
        
        RxPatientData rxPatientData = new RxPatientData();
        RxPatientData.Patient patient = null;

        String demoNo = bean.getDemographicNo() + "";
        try {
            patient = rxPatientData.getPatient(Integer.parseInt(demoNo));
        } catch (Exception e) {
            e.printStackTrace();
        }

        oscar.oscarRx.data.RxPrescriptionData.Prescription[] prescribedDrugs;                                                               
        prescribedDrugs = patient.getPrescribedDrugs();
        
        int drugsSent = 0;
        System.out.println("prescribed drugs length" + prescribedDrugs.length);
        for( int idx = 0; idx < prescribedDrugs.length; ++idx ) {
              Prescription drug = prescribedDrugs[idx];
              if(drug.isCurrent() == true && !drug.isArchived() ){
                  try {
                      // if updating removed because drugs are never edited, only represcribed
                      /*if (phrService.isIndivoRegistered(phrConstants.DOCTYPE_MEDICATION(), drug.getDrugId()+"")) {
                           //if updating
                           System.out.println("running update");
                           String phrDrugIndex = phrService.getPhrIndex(phrConstants.DOCTYPE_MEDICATION(), drug.getDrugId()+"");
                           phrService.sendUpdateMedication(prov, demoNo, patientMyOscarId, drug, phrDrugIndex);
                           //drug.setIndivoIdx(newIndex);
                      } else { //if adding*/
                      
                      //only add new drugs, no updating old drugs because they cannot be edited
                      //System.out.println("Checking drug: " + drug.getDrugName());
                      if (!phrService.isIndivoRegistered(phrConstants.DOCTYPE_MEDICATION(), drug.getDrugId()+"")) {
                          //System.out.println("Entering phrService");
                          phrService.sendAddMedication(prov, demoNo, patientMyOscarId, drug);
                      }
                      //throw new Exception("Error: Cannot marshal the document");
                  } catch (Exception e) {
                      e.printStackTrace();
                      errorMsg = e.getMessage();
                      //errors = true;
                      request.setAttribute("error_msg", errorMsg);
                      return mapping.findForward("finished");
                  }
              }
        }
        request.setAttribute("error_msg", errorMsg);
        return mapping.findForward("finished");
    }
    
    public void setPhrService(PHRService pServ){
        this.phrService = pServ;
    }
    
    public void setPhrConstants(PHRConstants pConst) {
        this.phrConstants = pConst;
    }
    
}