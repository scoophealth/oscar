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
                    
                    String msg = null;
                    String regExp = null;
                    double dMax = 0;
                    double dMin = 0;
                    
                    EctValidation ectValidation = new EctValidation();                    
                    ActionErrors errors = new ActionErrors();                    
                    ResultSet rs = ectValidation.getValidationType(inputType, mInstrc);

                    if (rs.next()){
                        dMax = rs.getDouble("maxValue");
                        dMin = rs.getDouble("minValue");
                        regExp = rs.getString("regularExp");
                    }                                                                                                                        
	
                    if(!ectValidation.isInRange(dMax, dMin, inputValue)){                       
                        errors.add(inputValueName, new ActionError("errors.range", inputType, Double.toString(dMin), Double.toString(dMax)));
                        saveErrors(request, errors);
                        valid = false;
                    }
                    else if(!ectValidation.matchRegExp(regExp, inputValue)){                        
                        errors.add(inputValueName,
                        new ActionError("errors.invalid", inputType));
                        saveErrors(request, errors);
                        valid = false;
                    }
                    else if(!ectValidation.isValidBloodPressure(regExp, inputValue)){                        
                        errors.add(inputValueName,
                        new ActionError("error.bloodPressure"));
                        saveErrors(request, errors);
                        valid = false;
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
                    return (new ActionForward(mapping.getInput()));
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

     
}
