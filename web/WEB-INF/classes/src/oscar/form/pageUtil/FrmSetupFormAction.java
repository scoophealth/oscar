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
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.validator.*;
import org.apache.struts.action.*;
import org.apache.struts.validator.*;
import org.apache.struts.util.MessageResources;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import oscar.form.data.*;
import oscar.form.util.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.oscarEncounter.data.EctEChartBean;
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
        EctEChartBean chartBean = new EctEChartBean();        
        String formId = request.getParameter("formId");
        frm.setValue("formId", formId==null?"0":formId);
        String demo = null;
        String providerNo = (String) session.getValue("user");
        if (bean!=null){
            request.getSession().setAttribute("EctSessionBean", bean);
            demo = (String) bean.getDemographicNo();
            chartBean.setEChartBean(demo);	        
        }
        
        String ongoingConcern = chartBean.ongoingConcerns;
        String formName = (String) request.getParameter("formName");        
        String today = UtilDateUtilities.DateToString(UtilDateUtilities.Today(),_dateFormat);
        List drugLists = getDrugList(demo);
                                    
        Properties currentRec = getFormRecord(formName, formId, demo);
        
        request.setAttribute("today", today);
        //specifically for VT Form
        request.setAttribute("drugs", drugLists);        
        request.setAttribute("ongoingConcerns", chartBean.ongoingConcerns.equalsIgnoreCase("")?"None":chartBean.ongoingConcerns);
        if(currentRec!=null)
            frm.setValue("diagnosis", currentRec.getProperty("diagnosis", "None"));
        
        try {            
            System.out.println("formId=" + formId + "opening " + formName + ".xml");
            InputStream is = getClass().getResourceAsStream("/../../form/" + formName + ".xml");
            Vector measurementTypes = EctFindMeasurementTypeUtil.checkMeasurmentTypes(is, formName);
            EctMeasurementTypesBean mt;            
            
            //Get URL from Miles
            Properties props = new Properties();
            Properties nameProps = new Properties();
            props.setProperty("demographic_no", demo);
            props.setProperty("provider_no", providerNo);
            String xmlData = FrmToXMLUtil.convertToXml(measurementTypes, nameProps, props);
            String decisionSupportURL = connect2OSDSF(xmlData);
            request.setAttribute("decisionSupportURL", StringEscapeUtils.escapeHtml(decisionSupportURL));
            
            //Get the most updated data from Miles"
            String xmlString = "<SitePatientVisit_Records>"+
                                   "<SitePatientVisit Visit_cod=\"20041202\" Patient_cod=\"19\">"+
                                      "<int_Height_cm signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"123\"/>"+
                                      "<dbl_Weight_kg signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"123\"/>"+
                                      "<int_WaistCircumference_cm signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"1\"/>"+
                                      "<int_HipCircumference_cm signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"123\"/>"+
                                      "<dat_BP signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"123\"/>"+
                                      "<int_Pulse_bpm signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"88\"/>"+
                                      "<dbl_HbA1c signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\".17\"/>"+
                                      "<dat_HbA1c signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"1999-01-01\"/>"+
                                      "<dbl_Glucose_mM signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"123\"/>"+
                                      "<dat_Glucose signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"2003-01-02\"/>"+
                                      "<dbl_LDL_mM signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"2\"/>"+
                                      "<dat_LDL signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"2004-12-31\"/>"+
                                      "<dbl_HDL_mM signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"2\"/>"+
                                      "<dat_HDL signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"2004-12-31\"/>"+
                                      "<dbl_TotalCholesterol_mM signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\" value=\"123\"/>"+
                                      "<dat_TotalCholesterol signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\"/>"+
                                      "<dbl_Triglycerides_mM signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\"/>"+
                                      "<dat_Triglycerides signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\"/>"+
                                      "<dbl_UrineAlbuminCreatinineRatio_mgPermmol signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\"/>"+
                                      "<dat_UrineAlbuminCreatinineRatio signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\"/>"+
                                      "<dbl_UrineAlbumin_mgPerDay signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\"/>"+
                                      "<dat_UrineAlbumin signed_when=\"2004/12/02\" signed_who=\"999998\" signed_how=\"EMR\"/>"+
                                   "</SitePatientVisit>"+
                                "</SitePatientVisit_Records>";
            nameProps = convertName(formName);
            FrmXml2VTData xml2VTData = new FrmXml2VTData();
            FrmVTData vtData = xml2VTData.getObjectFromXmlStr(xmlString);
            Class vtDataC = Class.forName("oscar.form.data.FrmVTData");
            
            ResultSet rs;
            
            for(int i=0; i<measurementTypes.size(); i++){
                mt = (EctMeasurementTypesBean) measurementTypes.elementAt(i);
                request.setAttribute(mt.getType(), mt.getType());
                request.setAttribute(mt.getType() + "Display", mt.getTypeDisplayName());
                request.setAttribute(mt.getType() + "Desc", mt.getTypeDesc());
                request.setAttribute(mt.getType() + "MeasuringInstrc", mt.getMeasuringInstrc());
                
                addLastData(mt, demo);
                request.setAttribute(mt.getType()+"LastData", mt.getLastData());
                request.setAttribute(mt.getType()+"LastDataEnteredDate", mt.getLastDateEntered());
                
                if(currentRec!=null){
                    frm.setValue(mt.getType()+"Value", currentRec.getProperty(mt.getType()+"Value", ""));
                    frm.setValue(mt.getType()+"Date", currentRec.getProperty(mt.getType()+"Date", ""));     
                    frm.setValue(mt.getType()+"Comments", currentRec.getProperty(mt.getType()+"Comments", ""));                      
                    request.setAttribute(mt.getType()+"Date", currentRec.getProperty(mt.getType()+"Date", ""));     
                    request.setAttribute(mt.getType()+"Comments", currentRec.getProperty(mt.getType()+"Comments", "")); 
                }
                else{                                        
                                                            
                    String valueMethodCall = (String) nameProps.get(mt.getType()+"Value");
                    //System.out.println("method "+methodCall);
                    if (valueMethodCall != null){                      
                        Method vtGetMethods = vtDataC.getMethod("get"+valueMethodCall, new Class[] {});
                        String value = (String) vtGetMethods.invoke(vtData, new Object[]{});
                        frm.setValue(mt.getType()+"Value", value);
                        //System.out.println(mt.getType()+"value from miles: " + value);                    
                    
                        String dateMethodCall = (String) nameProps.get(mt.getType()+"Date");
                        //System.out.println("method "+methodCall);
                        if (dateMethodCall != null){                      
                            vtGetMethods = vtDataC.getMethod("get"+dateMethodCall, new Class[] {});
                            value = (String) vtGetMethods.invoke(vtData, new Object[]{});
                            frm.setValue(mt.getType() + "Date", value);
                            request.setAttribute(mt.getType() + "Date", value);
                            //System.out.println(mt.getType()+"date from miles: " + value);
                        }
                        else{
                            vtGetMethods = vtDataC.getMethod("get"+valueMethodCall+"$signed_when", new Class[] {});
                            value = (String) vtGetMethods.invoke(vtData, new Object[]{});
                            frm.setValue(mt.getType() + "Date", value);    
                            request.setAttribute(mt.getType() + "Date", value);
                        }  
                    }
                    else{
                        frm.setValue(mt.getType() + "Date", today);    
                        request.setAttribute(mt.getType() + "Date", today);
                    }
                    request.setAttribute(mt.getType() + "Comments", "");
                }
                                                                                             
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
    
    private String connect2OSDSF(String xmlResult){
        Vector data2OSDSF = new Vector();
        data2OSDSF.add(xmlResult);
        data2OSDSF.add("dummy");
        //send to osdsf thru XMLRPC
        try{
            XmlRpcClient xmlrpc = new XmlRpcClient("http://oscartest.oscarmcmaster.org:8080/osdsf/VTRpcServlet.go");
            String result = (String) xmlrpc.execute("vt.vtXMLRtn", data2OSDSF);
            System.out.println("Reverse result: " + result);
            return result;
        }
        catch(XmlRpcException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        return null;
        
    }
    
    private Properties convertName(String formName){
        Properties osdsf = new Properties();
        InputStream is = getClass().getResourceAsStream("/../../form/" + formName + "FromOsdsf.properties");
        try {
                osdsf.load(is);
        } catch (Exception e) {
                System.out.println("Error, file " + formName + ".properties not found.");			
        }

        try{
                is.close();
        } catch (IOException e) {
                System.out.println("IO error.");
                e.printStackTrace();
        }
        return osdsf;
    }
    
    private void addLastData(EctMeasurementTypesBean mt,  String demo){
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
            //get last value and its observation date                
            String sqlData = "SELECT dataField, dateEntered FROM measurements WHERE demographicNo='"+ demo + "' AND type ='" + mt.getType()
                             + "' AND measuringInstruction='" + mt.getMeasuringInstrc() + "' ORDER BY dateEntered limit 1";

            ResultSet rs = db.GetSQL(sqlData);
            if(rs.next()){
                mt.setLastData(rs.getString("dataField"));
                mt.setLastDateEntered(rs.getString("dateEntered"));                
            }
                            
            rs.close();                                                               
                            
            db.CloseConn();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}
