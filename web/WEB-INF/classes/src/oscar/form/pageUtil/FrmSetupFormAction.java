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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

import oscar.OscarProperties;
import oscar.form.data.FrmVTData;
import oscar.form.util.FrmXml2VTData;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.data.EctEChartBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementTypesBean;
import oscar.oscarEncounter.oscarMeasurements.util.EctFindMeasurementTypeUtil;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.oscarRx.data.RxAllergyData;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPrescriptionData;
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
        String demo = request.getParameter("demographic_no");
        String providerNo = (String) session.getAttribute("user");
        if (demo == null || bean!=null){
            request.getSession().setAttribute("EctSessionBean", bean);
            demo = (String) bean.getDemographicNo();
            
        }
        
        if ( demo != null){
           chartBean.setEChartBean(demo);	                   
        }                
                
        String ongoingConcern = chartBean.ongoingConcerns;
        String formName = (String) request.getParameter("formName");        
        String today = UtilDateUtilities.DateToString(UtilDateUtilities.Today(),_dateFormat);
        String visitCod = UtilDateUtilities.DateToString(UtilDateUtilities.Today(),"yyyy-MM-dd");
        
        List drugLists = getDrugList(demo);
        List allergyList = getDrugAllegyList(demo);
                                    
        Properties currentRec = getFormRecord(formName, formId, demo);
        
        request.setAttribute("today", today);
        //specifically for VT Form
        request.setAttribute("drugs", drugLists); 
        request.setAttribute("allergies", allergyList);
        request.setAttribute("ongoingConcerns", chartBean.ongoingConcerns.equalsIgnoreCase("")?"None":chartBean.ongoingConcerns);
        if(currentRec!=null){
            frm.setValue("visitCod", currentRec.getProperty("visitCod",""));
            frm.setValue("diagnosisVT", currentRec.getProperty("Diagnosis", ""));
            frm.setValue("subjective", currentRec.getProperty("Subjective", ""));            
            frm.setValue("objective", currentRec.getProperty("Objective", ""));
            frm.setValue("assessment", currentRec.getProperty("Assessment", ""));
            frm.setValue("plan", currentRec.getProperty("Plan", ""));
        }
        else{
            frm.setValue("visitCod", visitCod);
        }
        
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
            //String xmlData = FrmToXMLUtil.convertToXml(measurementTypes, nameProps, props);
            String decisionSupportURL = getPatientRlt(demo);
            System.out.println("decisionSupportURL" + decisionSupportURL);
            request.setAttribute("decisionSupportURL", StringEscapeUtils.escapeHtml(decisionSupportURL));
            
            //Get the most updated data from Miles"            
            String xmlStr =  getMostRecentRecord(demo);
            nameProps = convertName(formName);
            FrmXml2VTData xml2VTData = new FrmXml2VTData();
            Class vtDataC = null;
            FrmVTData vtData = null;
            if(xmlStr!=null){
                vtData = xml2VTData.getObjectFromXmlStr(xmlStr);
                vtDataC = oscar.form.data.FrmVTData.class;
            }
            
            ResultSet rs;
            
            for(int i=0; i<measurementTypes.size(); i++){
                mt = (EctMeasurementTypesBean) measurementTypes.elementAt(i);
                request.setAttribute(mt.getType(), mt.getType());
                request.setAttribute(mt.getType() + "Display", mt.getTypeDisplayName());
                request.setAttribute(mt.getType() + "Desc", mt.getTypeDesc());
                request.setAttribute(mt.getType() + "MeasuringInstrc", mt.getMeasuringInstrc());
                
                addLastData(mt, demo);                
                request.setAttribute(mt.getType()+"LastData", mt.getLastData()==null?"":mt.getLastData());
                request.setAttribute(mt.getType()+"LDDate", mt.getLastDateEntered()==null?"":mt.getLastDateEntered());
                
                if(currentRec!=null){
                    frm.setValue(mt.getType()+"Value", currentRec.getProperty(mt.getType()+"Value", ""));
                    frm.setValue(mt.getType()+"Date", currentRec.getProperty(mt.getType()+"Date", ""));     
                    frm.setValue(mt.getType()+"Comments", currentRec.getProperty(mt.getType()+"Comments", ""));                      
                    request.setAttribute(mt.getType()+"Date", currentRec.getProperty(mt.getType()+"Date", ""));     
                    request.setAttribute(mt.getType()+"Comments", currentRec.getProperty(mt.getType()+"Comments", "")); 
                }
                else{                                                                                                                      
                    //prefill data from Miles if its dataentered date is > than the one in measurements                                        
                    if(mt.getCanPrefill()){  
                        String value="";
                        String date=today;
                        
                        String valueMethodCall = (String) nameProps.get(mt.getType()+"Value");
                        String dateMethodCall = (String) nameProps.get(mt.getType()+"Date");                        
                        
                        if (vtData!=null && vtDataC!=null && valueMethodCall != null){                                                  
                            Method vtGetMethods = vtDataC.getMethod("get"+valueMethodCall, new Class[] {});                        
                            value = (String) vtGetMethods.invoke(vtData, new Object[]{});
                                                                                   
                            if(value!=null){
                                vtGetMethods = vtDataC.getMethod("get"+valueMethodCall+"$signed_when", new Class[] {});                            
                                String dMiles = (String) vtGetMethods.invoke(vtData, new Object[]{});                                                            
                                date = dMiles;
                                
                                if(dateMethodCall!=null){
                                    vtGetMethods = vtDataC.getMethod("get"+dateMethodCall, new Class[] {});
                                    date = (String) vtGetMethods.invoke(vtData, new Object[]{});
                                    date = date.equalsIgnoreCase("")?"0001-01-01":date;
                                                                        
                                    String dObsMeas = mt.getLastDateObserved()==null?"0001-01-01":mt.getLastDateObserved();                                     
                                    System.out.println(mt.getType() + " Miles: " + date + " Measurements: " + dObsMeas);
                                    Date milesDate = UtilDateUtilities.StringToDate(date, _dateFormat);
                                    Date obsMeasDate = UtilDateUtilities.StringToDate(dObsMeas, _dateFormat);
                                    
                                    if(mt.getLastData()!=null){
                                        if(obsMeasDate.compareTo(milesDate)>=0){
                                            String dMeas = mt.getLastDateEntered()==null?"0001-01-01":mt.getLastDateEntered();                            
                                
                                            Date dateMiles = UtilDateUtilities.StringToDate(dMiles, _dateFormat);
                                            Date dateMeas = UtilDateUtilities.StringToDate(dMeas, _dateFormat);

                                            if(dateMeas.compareTo(dateMiles)>0){
                                                value = mt.getLastData();
                                                date = dObsMeas;
                                            }                                        
                                        }
                                    }
                                }   
                            }
                        }    
                        frm.setValue(mt.getType()+"Value", value); 
                        frm.setValue(mt.getType()+"Date", date);       
                        request.setAttribute(mt.getType() + "Comments", "");
                    }
                    else{
                        frm.setValue(mt.getType() + "Date", today);    
                        request.setAttribute(mt.getType() + "Date", today);  
                        request.setAttribute(mt.getType() + "Comments", "");
                    }
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
        String fluShot = getFluShotBillingDate(demographicNo);
        //System.out.println("getFluShotBillingDate: " + fluShot);
        if(fluShot!=null)
            drugs.add(fluShot + "     Flu Shot");
            
        try{
            RxPatientData.Patient p = pData.getPatient(Integer.parseInt(demographicNo));
            RxPrescriptionData.Prescription[] prescribedDrugs = p.getPrescribedDrugsUnique();
            if(prescribedDrugs.length==0 && fluShot==null)
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
    
    private List getDrugAllegyList(String demographicNo){
        RxPatientData pData = new RxPatientData();
        List allergyLst = new LinkedList();
        try{
            RxPatientData.Patient p = pData.getPatient(Integer.parseInt(demographicNo));
            RxPatientData.Patient.Allergy[] allergies = p.getAllergies();
            if(allergies.length==0)
                allergyLst=null;
            for(int i=0; i<allergies.length; i++){
                RxAllergyData.Allergy allergy = allergies[i].getAllergy();
                allergyLst.add(allergies[i].getEntryDate() + " " + allergy.getDESCRIPTION() + " " + allergy.getTypeDesc());            
            }
        }
        catch(SQLException e){
            e.printStackTrace();   
        }
        return allergyLst;
    }
    
    private String getFluShotBillingDate(String demoNo) {
        String s = null;
        try {
                DBHandler dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
                String s1 = "select b.billing_no, b.billing_date from billing b, billingdetail bd where b.demographic_no='"
                                + demoNo
                                + "' and bd.billing_no=b.billing_no and (bd.service_code='G590A' or bd.service_code='G591A') "
                                + " and bd.status<>'D' and b.status<>'D' order by b.billing_date desc limit 0,1";
                ResultSet rs = dbhandler.GetSQL(s1);
                //System.out.println("flushot: " + s1);
                if (rs.next())
                        s = dbhandler.getString(rs,"billing_date");
                rs.close();
            } catch (SQLException sqlexception) {
                System.out.println(sqlexception.getMessage());
        }
        return s;
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
                                String value = db.getString(rs,i);
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
    
    private String getPatientRlt(String demographicNo){
        Vector data2OSDSF = new Vector();
        data2OSDSF.add("patientCod");
        data2OSDSF.add(demographicNo);
        String osdsfRPCURL = OscarProperties.getInstance().getProperty("osdsfRPCURL", null);
        System.out.println("osdsfRPCURL getPatientRlt(): " + osdsfRPCURL);
        if (osdsfRPCURL == null){
            return null;
        }
        //send to osdsf thru XMLRPC
        try{
            XmlRpcClient xmlrpc = new XmlRpcClient(osdsfRPCURL);
            String result = (String) xmlrpc.execute("vt.getAndSaveRlt", data2OSDSF);
            System.out.println("Reverse result: " + result);
            return result;
        }
        catch(XmlRpcException e){
            e.printStackTrace();
            return null;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }                
        catch(Exception e){
           e.printStackTrace();
           return null;
        }
    }
    
    private String getMostRecentRecord(String demographicNo){
        Vector ret = new Vector();
            ret.addElement("patientCod");
            ret.addElement(demographicNo);
            
        String osdsfRPCURL = OscarProperties.getInstance().getProperty("osdsfRPCURL", null);
        System.out.println("osdsfRPCURL getMostRecentRecord(): " + osdsfRPCURL);
        if (osdsfRPCURL == null){
            return null;
        }
        //send to osdsf thru XMLRPC
        try{
            XmlRpcClient xmlrpc = new XmlRpcClient(osdsfRPCURL);
            String result = (String) xmlrpc.execute("vt.getMostRecentRecord", ret);
            System.out.println("Reverse result: " + result);
            return result;
        }
        catch(XmlRpcException e){
            e.printStackTrace();
            return null;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }               
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
                             + "' AND measuringInstruction='" + mt.getMeasuringInstrc() + "' ORDER BY dateEntered DESC limit 1";

            ResultSet rs = db.GetSQL(sqlData);
            if(rs.next()){
                mt.setLastData(db.getString(rs,"dataField"));
                mt.setLastDateEntered(db.getString(rs,"dateEntered"));                
            }
                            
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    private String translate(String input, String xmlName){
        if(xmlName.startsWith("B_")){
            if(input.equalsIgnoreCase("true")){
                return "yes";
            }
            else
                return "no";
        }
        else if (xmlName.startsWith("Sel_")){
            if(input.equalsIgnoreCase("present")){
                return "yes";
            }
            else{
                return "no";
            }            
        }
        if(input.equalsIgnoreCase("null")){
            return "";
        }
        return input;
            
    }
}
