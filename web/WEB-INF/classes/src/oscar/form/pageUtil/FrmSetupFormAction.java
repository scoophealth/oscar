// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
// *
// * @Author: Ivy Chan
// * @Company: iConcept Technologes Inc. 
// * @Created on: October 31, 2004
// -----------------------------------------------------------------------------------------------------------------------

package oscar.form.pageUtil;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.validator.*;
import org.apache.commons.validator.*;
import org.apache.struts.util.MessageResources;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.OscarProperties;
import oscar.oscarEncounter.oscarMeasurements.bean.*;
import oscar.oscarEncounter.oscarMeasurements.prop.*;
import oscar.oscarEncounter.oscarMeasurements.util.*;
import oscar.oscarRx.data.*;
import oscar.util.UtilDateUtilities;


public final class FrmSetupFormAction extends Action {
    
    private String _dateFormat = "yyyy-MM-dd";
    
    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws Exception {

        /**
         * To create a new form which can write to measurement and osdsf, you need to ...
         * Create a xml file with all the measurement types named <formName>.xml (check form/VTForm.xml as an example)
         * Create a new jsp file named <formName>.jsp (check form/formVT.jsp)
         * Create a new table named form<formName> which include the name of all the input elements in the <formName>.jsp
         * Add the form description to encounterForm table of the database
         **/
        //System.gc();
        System.out.println("SetupFormAction is called");
        HttpSession session = request.getSession(true);
                
        FrmFormForm frm = (FrmFormForm) form;        
        EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
        String formId = request.getParameter("formId");
        frm.setValue("formId", formId==null?"0":formId);
        String demo = null;
        if (bean!=null){
            request.getSession().setAttribute("EctSessionBean", bean);
            demo = (String) bean.getDemographicNo();                                                          
        }        
        
        String formName = (String) request.getParameter("formName");        
        String today = UtilDateUtilities.DateToString(UtilDateUtilities.Today(),_dateFormat);
        List drugLists = getDrugList(demo);
        request.setAttribute("today", today);
        request.setAttribute("drugs", drugLists);
        
                    
        Properties currentRec = getFormRecord(formName, formId, demo);
        
        try {
            System.out.println("formId=" + formId + "opening " + formName + ".xml");
            InputStream is = getClass().getResourceAsStream("/../../form/" + formName + ".xml");
            Vector measurementTypes = EctFindMeasurementTypeUtil.checkMeasurmentTypes(is, formName);
            EctMeasurementTypesBean mt;

            
            for(int i=0; i<measurementTypes.size(); i++){
                mt = (EctMeasurementTypesBean) measurementTypes.elementAt(i);
                request.setAttribute(mt.getType(), mt.getType());
                request.setAttribute(mt.getType() + "Display", mt.getTypeDisplayName());
                request.setAttribute(mt.getType() + "Desc", mt.getTypeDesc());
                request.setAttribute(mt.getType() + "MeasuringInstrc", mt.getMeasuringInstrc());                
                
                if(currentRec!=null){
                    frm.setValue(mt.getType()+"Value", currentRec.getProperty(mt.getType()+"Value", ""));
                    frm.setValue(mt.getType()+"Date", currentRec.getProperty(mt.getType()+"Date", ""));     
                    frm.setValue(mt.getType()+"Comments", currentRec.getProperty(mt.getType()+"Comments", ""));                      
                    request.setAttribute(mt.getType()+"Date", currentRec.getProperty(mt.getType()+"Date", ""));     
                    request.setAttribute(mt.getType()+"Comments", currentRec.getProperty(mt.getType()+"Comments", "")); 
                }
                else{
                    frm.setValue(mt.getType() + "Date", today);
                }

                /*
                //get last value and its observation date
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sqlData = "SELECT * FROM measurements WHERE demographicNo='"+ demo + "' AND type ='" + mt.getType()
                                 + "' AND measuringInstruction='" + mt.getMeasuringInstrc() + "' ORDER BY dateEntered DESC LIMIT 1";
                ResultSet rs = db.GetSQL(sqlData);
                if(rs.next()){                    
                    //mt.setLastData(rs.getString("dataField"));
                    //mt.setLastDateEntered(rs.getString("dateEntered"));
                    request.setAttribute(mt.getType()+"LastData", rs.getString("dataField"));
                    request.setAttribute(mt.getType()+"LastDataEnteredDate", rs.getString("dateEntered"));
                    frm.setValue(mt.getType()+"LastData",rs.getString("dateEntered"));
                    frm.setValue(mt.getType()+"LastDataEnteredDate",rs.getString("dateEntered"));
                }
                rs.close();
                db.CloseConn();
                */                
            }    
            is.close();
        }
        /*
        catch (SQLException e) {
            e.printStackTrace();
        }
        /*catch (Exception e) {
            e.printStackTrace();            
        } */       
        catch (IOException e) {
                System.out.println("IO error.");
                System.out.println("Error, file " + formName + ".xml not found.");
                System.out.println("This file must be placed at web/form");
                e.printStackTrace();
        }                        
        return (new ActionForward("/form/form"+formName+".jsp"));        
    }
    
    private List getDrugList(String demographicNo){
        RxPatientData pData = new RxPatientData();
        List drugs = new LinkedList();
        try{
            RxPatientData.Patient p = pData.getPatient(Integer.parseInt(demographicNo));
            RxPrescriptionData.Prescription[] prescribedDrugs = p.getPrescribedDrugsUnique();
            if(prescribedDrugs.length==0)
                drugs=null;
            for(int i=0; i<prescribedDrugs.length; i++){
                drugs.add(prescribedDrugs[i].getRxDate().toString() + "    " + prescribedDrugs[i].getRxDisplay());            
            }
        }
        catch(SQLException e){
            e.printStackTrace();   
        }
        return drugs;
    }
    
    private Properties getFormRecord(String formName, String formId, String demographicNo){
        Properties props = new Properties();
        try{
            
            if(formId!=null){
                if(Integer.parseInt(formId)>0){
                    DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                    String sql = "SELECT * FROM form" + formName + " WHERE ID='" + formId + "' AND demographic_no='" + demographicNo + "'";
                    ResultSet rs = db.GetSQL(sql);
                    if(rs.next()) {
                        ResultSetMetaData md = rs.getMetaData();
                        for(int i = 1; i <= md.getColumnCount(); i++)  {
                                String name = md.getColumnName(i);
                                String value = rs.getString(i);
                                if(value != null)	
                                    props.setProperty(name, value);
                        }
                    }
                }
                else
                    return null;
            }
            else
                return null;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return props;
    }
}
