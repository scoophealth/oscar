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
// *
// * Author: Ivy Chan
// * Company: iConcept Technologes Inc. 
// * Created on: October 31, 2004
// -----------------------------------------------------------------------------------------------------------------------

package oscar.form.pageUtil;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.validator.*;
import org.apache.commons.validator.*;
import org.apache.commons.lang.StringEscapeUtils.*;
import org.apache.struts.util.MessageResources;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import oscar.form.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.oscarEncounter.oscarMeasurements.pageUtil.EctValidation;
import oscar.oscarEncounter.oscarMeasurements.bean.*;
import oscar.oscarEncounter.oscarMeasurements.prop.*;
import oscar.oscarEncounter.oscarMeasurements.util.*;
import oscar.OscarProperties;
import oscar.util.UtilDateUtilities;


public class FrmFormAction extends Action {
    
    /**
     * To create a new form which can write to measurement and osdsf, you need to ...
     * Create a xml file with all the measurement types named <formName>.xml (check form/VTForm.xml as an example)
     * Create a new jsp file named <formName>.jsp (check form/formVT.jsp)
     * Create a new table named form<formName> which include the name of all the input elements in the <formName>.jsp
     * Add the form description to encounterForm table of the database
     **/

    private boolean valid = true;
    private String _dateFormat = "yyyy/MM/dd";
    ActionErrors errors = new ActionErrors();                   
    
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        FrmFormForm frm = (FrmFormForm) form;
        
        HttpSession session = request.getSession();
        request.getSession().setAttribute("FrmFormForm", frm);        
        EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
        request.getSession().setAttribute("EctSessionBean", bean);
        
        String formName = (String) frm.getValue("formName");                        
        String dateEntered = UtilDateUtilities.DateToString(UtilDateUtilities.Today(),_dateFormat);
                        
        Properties props = new Properties();
        EctFormProp formProp = EctFormProp.getInstance();
        Vector measurementTypes = formProp.getMeasurementTypes();
        String demographicNo = null;
        String providerNo = (String) session.getValue("user");
        if ( bean != null)
            demographicNo = bean.getDemographicNo();                        
        
        errors.clear();
        
        //Validate each measurement
        for(int i=0; i<measurementTypes.size(); i++){
            EctMeasurementTypesBean mt = (EctMeasurementTypesBean) measurementTypes.elementAt(i);
            EctValidationsBean validation = (EctValidationsBean) mt.getValidationRules().elementAt(0);
            String inputValue = (String) frm.getValue(mt.getType()+"Value");
            String observationDate = (String) frm.getValue(mt.getType()+"Date");
            
            //parse the checkbox value
            inputValue = parseCheckBoxValue(inputValue, validation.getName());
            
            //validate
            valid = validate(inputValue, observationDate, mt, validation, request);
        }
        
        if(valid){
            //Store form information as properties for saving to form table
            props.setProperty("demographic_no", demographicNo);
            props.setProperty("provider_no", providerNo);
            props.setProperty("formCreated", dateEntered);
            
            for(int i=0; i<measurementTypes.size(); i++){
                EctMeasurementTypesBean mt = (EctMeasurementTypesBean) measurementTypes.elementAt(i);
                EctValidationsBean validation = (EctValidationsBean) mt.getValidationRules().elementAt(0);
                String type = mt.getType();
                String inputValue = (String) frm.getValue(type+"Value");
                String observationDate = (String) frm.getValue(type+"Date");
                String comments = (String) frm.getValue(type+"Comments");
                comments = org.apache.commons.lang.StringEscapeUtils.escapeSql(comments);
                
                //parse the checkbox value
                inputValue = parseCheckBoxValue(inputValue, validation.getName());
                
                //Write to Measurement Table
                write2MeasurementTable(demographicNo, providerNo, mt, inputValue, observationDate, comments);                
                
                //Store all input value as properties for saving to form table
                props.setProperty(type+"Value", inputValue);
                props.setProperty(type+"Date", observationDate);
                props.setProperty(type+"Comments", comments);                
            }
            
            //Save to formTable
            try{
                String sql = "SELECT * FROM form"+formName + " WHERE demographic_no='"+demographicNo + "' AND ID=0";
                FrmRecordHelp frh = new FrmRecordHelp();
                frh.setDateFormat(_dateFormat);
                (frh).saveFormRecord(props, sql);
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
            
            //Send to Mils thru xml-rpc
            props.setProperty("VisitDate", dateEntered);
            connect2OSDSF(props, formName);
            
        }
        else{                                 
            return (new ActionForward("/form/SetupForm.do?formName="+formName));
        }
        
         //return mapping.findForward("success");
        return (new ActionForward("/form/SetupForm.do?formName="+formName));
    }
        
    private boolean validate(String inputValue, String observationDate, EctMeasurementTypesBean mt, EctValidationsBean validation, HttpServletRequest request ){
        EctValidation ectValidation = new EctValidation();                            
        
        String inputTypeDisplay = mt.getTypeDesc();
        String inputValueName = mt.getType()+"Value";   
        String inputDateName = mt.getType()+"Date";
        String regExp = validation.getRegularExp();
        //System.out.println("Input Value of " + mt.getType() + ":" + inputValue);
        double dMax = Double.parseDouble(validation.getMaxValue()==null?"0":validation.getMaxValue());
        double dMin = Double.parseDouble(validation.getMinValue()==null?"0":validation.getMinValue());
        int iMax = Integer.parseInt(validation.getMaxLength()==null?"0":validation.getMaxLength());
        int iMin = Integer.parseInt(validation.getMinLength()==null?"0":validation.getMinLength());
        int iIsDate = Integer.parseInt(validation.getIsDate()==null?"0":validation.getIsDate());
        
        if(!ectValidation.isInRange(dMax, dMin, inputValue)){                       
            errors.add(inputValueName, new ActionError("errors.range", inputTypeDisplay, Double.toString(dMin), Double.toString(dMax)));
            saveErrors(request, errors);            
            valid = false;
        }
        if(!ectValidation.maxLength(iMax, inputValue)){                       
            errors.add(inputValueName, new ActionError("errors.maxlength", inputTypeDisplay, Integer.toString(iMax)));
            saveErrors(request, errors);
            valid = false;
        }
        if(!ectValidation.minLength(iMin, inputValue)){                       
            errors.add(inputValueName, new ActionError("errors.minlength", inputTypeDisplay, Integer.toString(iMin)));
            saveErrors(request, errors);
            valid = false;
        }
        if(!ectValidation.matchRegExp(regExp, inputValue)){                        
            errors.add(inputValueName,
            new ActionError("errors.invalid", inputTypeDisplay));
            saveErrors(request, errors);
            valid = false;
        }
        if(!ectValidation.isValidBloodPressure(regExp, inputValue)){                        
            errors.add(inputValueName,
            new ActionError("error.bloodPressure"));
            saveErrors(request, errors);
            valid = false;
        }
        if(inputValue.compareTo("")!=0 && iIsDate==1 && !ectValidation.isDate(inputValue)){                        
            errors.add(inputValueName,
            new ActionError("errors.invalidDate", inputTypeDisplay));
            saveErrors(request, errors);
            valid = false;
        }
        if(!ectValidation.isDate(observationDate)&&inputValue.compareTo("")!=0){                        
            errors.add(inputDateName,
            new ActionError("errors.invalidDate", inputTypeDisplay));
            saveErrors(request, errors);
            valid = false;
        }
        return valid;
    }
    
    private boolean write2MeasurementTable(String demographicNo, String providerNo, 
                                        EctMeasurementTypesBean mt, String inputValue, 
                                        String dateObserved, String comments){
        boolean newDataAdded = false;
        System.out.println("writing to Measurement: " + mt.getType() + " value: " + inputValue + " dateObserved: " + dateObserved);
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);    
            org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
            if(!gValidator.isBlankOrNull(inputValue)){
                //Find if the same data has already been entered into the system
                String sql = "SELECT * FROM measurements WHERE demographicNo='"+demographicNo 
                            + "' AND type='" + mt.getType() + "' AND dataField='"+inputValue 
                            + "' AND measuringInstruction='" + mt.getMeasuringInstrc() + "' AND comments='" + comments
                            + "' AND dateObserved='" + dateObserved + "'";
                ResultSet rs = db.GetSQL(sql);
                if(!rs.next()){
                    newDataAdded = true;
                    //Write to the Dababase if all input values are valid                        
                    sql = "INSERT INTO measurements"
                            +"(type, demographicNo, providerNo, dataField, measuringInstruction, comments, dateObserved, dateEntered)"
                            +" VALUES ('"+mt.getType()+"','"+demographicNo+"','"+providerNo+"','"+inputValue+"','"
                            + mt.getMeasuringInstrc() +"','"+comments+"','"+dateObserved+"', now())";                           
                    db.RunSQL(sql);
                }
                rs.close();
                db.CloseConn();
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return newDataAdded;
    }
    
    private void connect2OSDSF(Properties props, String formName){
        
        //update the props key name with formName.properties file
        Properties osdsf = loadOSDSF(formName);
        Vector data2OSDSF = new Vector();
        String osdsfKey;        
        
        for (Enumeration enum = props.propertyNames(); enum.hasMoreElements();) {
            String name = (String) enum.nextElement();
            String value = props.getProperty(name);   
            //System.out.println("name: " + name + " value: " + value);
            if(value!=null && value.equalsIgnoreCase("")){
                if(name.equalsIgnoreCase("BPValue")){
                    //extract SBP and DBP for blood pressure
                    String bp = value;
                    if(bp!=null){
                        int sbpIndex = bp.indexOf("/");                    
                        if(sbpIndex>=0){
                            String sbp = bp.substring(0,sbpIndex);
                            String dbp = bp.substring(sbpIndex+1);
                            osdsfKey = osdsf.getProperty("SBPValue");
                            if(osdsfKey!=null){
                                data2OSDSF.add(osdsfKey);
                                data2OSDSF.add(sbp);
                                System.out.println("SBP Key: " + osdsfKey + " Value: " + sbp);
                            }                        
                            osdsfKey = osdsf.getProperty("DBPValue");
                            if(osdsfKey!=null){
                                data2OSDSF.add(osdsfKey);
                                data2OSDSF.add(dbp);
                                System.out.println("DBP Key: " + osdsfKey + " Value: " + dbp);
                            }
                        }
                    }
                }
                else if(name.equalsIgnoreCase("BPDate")){
                    osdsfKey = osdsf.getProperty("SBPDate");
                    if(osdsfKey!=null){
                        data2OSDSF.add(osdsfKey);
                        data2OSDSF.add(value);
                        System.out.println("Key: " + osdsfKey + " Value: " + value);
                    }
                    osdsfKey = osdsf.getProperty("DBPDate");
                    if(osdsfKey!=null){
                        data2OSDSF.add(osdsfKey);
                        data2OSDSF.add(value);
                        System.out.println("Key: " + osdsfKey + " Value: " + value);
                    }
                }
                else{                    
                    osdsfKey = osdsf.getProperty(name);
                    if(osdsfKey!=null){
                        data2OSDSF.add(osdsfKey);
                        data2OSDSF.add(value);
                        System.out.println("Key: " + osdsfKey + " Value: " + value);
                    }
                }
            }
        }
        //send to osdsf thru XMLRPC
        try{
            XmlRpcClient xmlrpc = new XmlRpcClient("http://oscartest.oscarmcmaster.org:8080/osdsf/VTRpcServlet.go");
            String result = (String) xmlrpc.execute("vt.vtXMLRtn", data2OSDSF);
            System.out.println("Reverse result: " + result);
        }
        catch(XmlRpcException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        /*catch(MalformedURLException e){
            e.printStackTrace();
        }*/
    }
    
    private Properties loadOSDSF(String formName){
        Properties osdsf = new Properties();
        InputStream is = getClass().getResourceAsStream("/../../form/" + formName + ".properties");
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
    
    private String parseCheckBoxValue(String inputValue, String validationName){        
        //System.out.println("validationName: " + validationName);
        if(validationName.equalsIgnoreCase("Yes/No")){
            if(inputValue==null)
                inputValue="";
            else if (inputValue.equalsIgnoreCase("on"))
                    inputValue="yes";    
        }        
        return inputValue;
    }
     
}
