// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.sql.ResultSet;
import java.sql.SQLException;
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


public class EctMeasurementsAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctMeasurementsForm frm = (EctMeasurementsForm) form;
        
        HttpSession session = request.getSession();
        request.getSession().setAttribute("EctMeasurementsForm", frm);
        
        EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
        request.getSession().setAttribute("EctSessionBean", bean);
        

        String dateEntered = (String) frm.getValue("dateEntered");   
        String numType = (String) frm.getValue("numType");        
        int iType = Integer.parseInt(numType);
 
        String demographicNo = null;
        String providerNo = null;
        if ( bean != null){
            demographicNo = bean.getDemographicNo();
            providerNo = bean.getCurProviderNo();
        }
        
        MsgStringQuote str = new MsgStringQuote();
        String requestId = "";
        
        List messages = new LinkedList();
        String textOnEncounter = "**********************************************************************************\\n";        
        boolean valid = true;
        try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                
                //goes through each type to check if the input value is valid
                for(int i=0; i<iType; i++){
                    String inputValueName = "inputValue-" + i;
                    String inputTypeName = "inputType-" + i;
                    String mInstrcName = "inputMInstrc-" + i;
                    String inputValue = (String) frm.getValue(inputValueName);
                    String inputType = (String) frm.getValue(inputTypeName);
                    String mInstrc = (String) frm.getValue(mInstrcName);
                    String validation = null;
                    //int iValidation = Integer.parseInt(validation);
                    String msg = null;
                    String regExp = null;
                    double dMax = 0;
                    double dMin = 0;
                    
                    String sql = "SELECT validation FROM measurementType WHERE type = '"+ inputType + "'AND measuringInstruction='" + mInstrc + "'"; 
                    ResultSet rs = db.GetSQL(sql);
                    if (rs.next()){
                        validation = rs.getString("validation");
                    }
                    rs.close();
                    
                    sql = "SELECT * FROM validations where id=" + validation;
                    rs = db.GetSQL(sql);
                        
                    if (rs.next()){
                        dMax = rs.getDouble("maxValue");
                        dMin = rs.getDouble("minValue");
                        regExp = rs.getString("regularExp");
                    }
                    
                    System.out.println("the regularExp obtained from the datbase is" + regExp);
                    EctValidationParameter isInRange = isInRange(dMax, dMin, inputValue, inputType, request);
                    EctValidationParameter matchRegExp = matchRegExp(regExp, inputValue, inputType, request);
                    EctValidationParameter isValidBloodPressure = isValidBloodPressure(regExp, inputValue, inputType, request);

                    ActionErrors errors = new ActionErrors();                                                            
	
                    if(!isInRange.getValid()){                       
                        errors.add(inputValueName, new ActionError("errors.range", inputType, Double.toString(dMin), Double.toString(dMax)));
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.getInput()));
                    }
                    else if(!matchRegExp.getValid()){                        
                        errors.add(inputValueName,
                        new ActionError("errors.invalid", inputType));
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.getInput()));
                    }
                    else if(!isValidBloodPressure.getValid()){                        
                        errors.add(inputValueName,
                        new ActionError("error.bloodPressure"));
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.getInput()));
                    }
                }

                //Write to database and to encounter form if all the input values are valid
                if(valid){
                    for(int i=0; i<iType; i++){

                        String inputValueName = "inputValue-" + i;
                        String inputTypeName = "inputType-" + i;
                        String inputMInstrcName = "inputMInstrc-" + i;
                        String commentsName = "comments-" + i;
                        String validationName = "validation-" + i;
                        String dayName = "day-" + i;
                        String monthName = "month-" + i;
                        String yearName = "year-" + i;             

                        String inputValue = (String) frm.getValue(inputValueName);
                        String inputType = (String) frm.getValue(inputTypeName);
                        String inputMInstrc = (String) frm.getValue(inputMInstrcName);
                        String comments = (String) frm.getValue(commentsName);
                        String validation = (String) frm.getValue(validationName);
                        String day = (String) frm.getValue(dayName);
                        String month = (String) frm.getValue(monthName);
                        String year = (String) frm.getValue(yearName);
                        String dateObserved = year+"/"+month+"/"+day;
                        
                        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
                        if(!gValidator.isBlankOrNull(inputValue)){
                            //Write to the Dababase if all input values are valid                        
                            String sql = "INSERT INTO measurements"
                                    +"(type, demographicNo, providerNo, dataField, measuringInstruction, comments, dateObserved, dateEntered)"
                                    +" VALUES ('"+str.q(inputType)+"','"+str.q(demographicNo)+"','"+str.q(providerNo)+"','"+str.q(inputValue)+"','"
                                    + str.q(inputMInstrc)+"','"+str.q(comments)+"','"+str.q(dateObserved)+"','"+str.q(dateEntered)+"')";
                            System.out.println(" sql statement "+sql);
                            db.RunSQL(sql);
                            //prepare input values for writing to the encounter form
                            textOnEncounter =  textOnEncounter + inputType + "    " + inputValue + " " + inputMInstrc + " " + comments + "\\n";                             
                            
                        }
                        else{
                            System.out.println("there's no inputvalue");
                        }                       
                    }
                    textOnEncounter = textOnEncounter + "**********************************************************************************\\n";
                    System.out.println(textOnEncounter);
                }
                else{
                    request.setAttribute("messages", messages);
                    return mapping.findForward("failure");
                }
                /* select the correct db specific command */
                String db_type = OscarProperties.getInstance().getProperty("db_type").trim();
                String dbSpecificCommand;
                if (db_type.equalsIgnoreCase("mysql")) {
                    dbSpecificCommand = "SELECT LAST_INSERT_ID()";
                } 
                else if (db_type.equalsIgnoreCase("postgresql")){
                    dbSpecificCommand = "SELECT CURRVAL('consultationrequests_numeric')";
                }
                else
                    throw new SQLException("ERROR: Database " + db_type + " unrecognized.");

                    
                ResultSet rs = db.GetSQL(dbSpecificCommand);
                if(rs.next())
                    requestId = Integer.toString(rs.getInt(1));
                db.CloseConn();
                
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
            
        
        //put the inputvalue to the encounter form
        session.setAttribute( "textOnEncounter", textOnEncounter );    
        return mapping.findForward("success");
    }
    
    private EctValidationParameter isInRange(double dMax, double dMin, String inputValue, String inputType, HttpServletRequest request){

        EctValidationParameter validation = new EctValidationParameter("", true);    
        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
        
        if ((dMax!=0) || (dMin!=0)){
            if(gValidator.isDouble(inputValue)){
                double dValue = Double.parseDouble(inputValue);
                String min = Double.toString(dMin);
                String max = Double.toString(dMax);
                if (!gValidator.isInRange(dValue, dMin, dMax)){
                    MessageResources mr = getResources(request);
                    String mustBe = mr.getMessage("oscarEncounter.oscarMeasurements.MeasurementsAction.mustBe");
                    String between = mr.getMessage("oscarEncounter.oscarMeasurements.MeasurementsAction.between");
                    String and = mr.getMessage("oscarEncounter.oscarMeasurements.MeasurementsAction.and");
                    String msg = inputType + " " + mustBe + " " + between + " " + min + " " + and + " " + max;
                    validation.setMsg(msg);
                    validation.setValid(false);
                }
            }
            else if(!gValidator.isBlankOrNull(inputValue)){ 
                MessageResources mr = getResources(request);
                String mustBe = mr.getMessage("oscarEncounter.oscarMeasurements.MeasurementsAction.mustBe");
                String numericValue = mr.getMessage("oscarEncounter.oscarMeasurements.MeasurementsAction.numericValue");
                String msg = inputType + " " + mustBe + " " + numericValue;
                validation.setMsg(msg);
                validation.setValid(false);
            }
        }
        return validation;
    }

    private EctValidationParameter matchRegExp(String regExp, String inputValue, String inputType, HttpServletRequest request){

        EctValidationParameter validation = new EctValidationParameter("", true);    
        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
        
        System.out.println("matchRegExp function is called.");
        
        if (!gValidator.isBlankOrNull(regExp) && !gValidator.isBlankOrNull(inputValue)){
            System.out.println("both the regExp and inputValue is not blank nor null.");
            if(!inputValue.matches(regExp)){
                System.out.println("Regexp not matched");
                MessageResources mr = getResources(request);
                String mustBe = mr.getMessage("oscarEncounter.oscarMeasurements.MeasurementsAction.mustBe");
                String inThisFormat = mr.getMessage("oscarEncounter.oscarMeasurements.MeasurementsAction.inThisFormat");                  
                String msg = inputType + " " + mustBe + " " + inThisFormat + " " + regExp;
                validation.setMsg(msg);
                validation.setValid(false);
                
            }

        }
        return validation;
     }

     private EctValidationParameter isValidBloodPressure(String regExp, String inputValue, String inputType, HttpServletRequest request){
         EctValidationParameter matchRegExp = matchRegExp(regExp, inputValue, inputType, request);
         EctValidationParameter validation = new EctValidationParameter("", true);    
         
         if(matchRegExp.getValid()){
            System.out.println("/");
            int slashIndex = inputValue.indexOf("/");
            System.out.println(slashIndex);
            if (slashIndex >= 0){
                String systolic = inputValue.substring(0, slashIndex);
                String diastolic = inputValue.substring(slashIndex+1);
                System.out.println("The systolic value is " + systolic);
                System.out.println("The diastolic value is " + diastolic);
                int iSystolic = Integer.parseInt(systolic);
                int iDiastolic = Integer.parseInt(diastolic);
                if( iDiastolic > iSystolic){
                    MessageResources mr = getResources(request);
                    String msg = mr.getMessage("oscarEncounter.oscarMeasurements.MeasurementsAction.bloodPressure");
                    validation.setMsg(msg);
                    validation.setValid(false);
                }
            }
        }
        return validation;
     }
     
     
     
}
