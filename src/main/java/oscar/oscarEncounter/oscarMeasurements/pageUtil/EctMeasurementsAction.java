/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.common.dao.FlowSheetCustomizationDao;
import org.oscarehr.common.model.FlowSheetCustomization;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet;
import oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.oscarMessenger.util.MsgStringQuote;


public class EctMeasurementsAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctMeasurementsForm frm = (EctMeasurementsForm) form;

        HttpSession session = request.getSession();
        //request.getSession().setAttribute("EctMeasurementsForm", frm);

        EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");


        String demographicNo = null;
        String providerNo = (String) session.getAttribute("user");

        //if form has demo use it since session bean could have been overwritten
        if( (demographicNo = (String)frm.getValue("demographicNo")) == null ) {
            if ( bean != null)
                demographicNo = bean.getDemographicNo();
        }

        String template = request.getParameter("template");
        MeasurementFlowSheet mFlowsheet = null;
        if (template != null){
            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
            FlowSheetCustomizationDao flowSheetCustomizationDao = (FlowSheetCustomizationDao) ctx.getBean("flowSheetCustomizationDao");
            MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();

            List<FlowSheetCustomization> custList = flowSheetCustomizationDao.getFlowSheetCustomizations( template,(String) session.getAttribute("user"),demographicNo);
            mFlowsheet = templateConfig.getFlowSheet(template,custList);
        }



        //request.getSession().setAttribute("EctSessionBean", bean);
        //TODO replace with a date format call.  Actually revamp to use hibernate
        java.util.Calendar calender = java.util.Calendar.getInstance();
        String day =  Integer.toString(calender.get(java.util.Calendar.DAY_OF_MONTH));
        String month =  Integer.toString(calender.get(java.util.Calendar.MONTH)+1);
        String year = Integer.toString(calender.get(java.util.Calendar.YEAR));
        String hour = Integer.toString(calender.get(java.util.Calendar.HOUR_OF_DAY));
        String min = Integer.toString(calender.get(java.util.Calendar.MINUTE));
        String second = Integer.toString(calender.get(java.util.Calendar.SECOND));
        String dateEntered = year+"-"+month+"-"+day+" " + hour + ":" + min + ":" + second ;

        String numType = (String) frm.getValue("numType");
        int iType = Integer.parseInt(numType);



        MsgStringQuote str = new MsgStringQuote();

        Properties p = (Properties) session.getAttribute("providerBean");
        String by = "";
        if (p != null ){
           by = p.getProperty(providerNo,"");
        }

        String textOnEncounter = ""; //"**"+StringUtils.rightPad(by,80,"*")+"\\n";

        //if parent window content has changed then we need to propagate change so
        //we do not write to parent
        String parentChanged = (String)frm.getValue("parentChanged");
        request.setAttribute("parentChanged", parentChanged);

        boolean valid = true;
        try
            {

                EctValidation ectValidation = new EctValidation();
                ActionMessages errors = new ActionMessages();

                String inputValueName, inputTypeName, inputTypeDisplayName, mInstrcName, commentsName;
                String dateName,validationName, inputValue, inputType, inputTypeDisplay, mInstrc;
                String comments, dateObserved, validation;

                String regExp = null;
                double dMax = 0;
                double dMin = 0;
                int iMax = 0;
                int iMin = 0;

                ResultSet rs;
                String regCharExp;
                //goes through each type to check if the input value is valid
                for(int i=0; i<iType; i++){
                    inputValueName = "inputValue-" + i;
                    inputTypeName = "inputType-" + i;
                    inputTypeDisplayName = "inputTypeDisplayName-" + i;
                    mInstrcName = "inputMInstrc-" + i;
                    commentsName = "comments-" + i;
                    dateName = "date-" + i;
                    inputValue = (String) frm.getValue(inputValueName);
                    inputType = (String) frm.getValue(inputTypeName);
                    inputTypeDisplay = (String) frm.getValue(inputTypeDisplayName);
                    mInstrc = (String) frm.getValue(mInstrcName);
                    comments = (String) frm.getValue(commentsName);
                    dateObserved = (String) frm.getValue(dateName);


                    regExp = null;
                    dMax = 0;
                    dMin = 0;
                    iMax = 0;
                    iMin = 0;

                    rs = ectValidation.getValidationType(inputType, mInstrc);
                    regCharExp = ectValidation.getRegCharacterExp();

                    if (rs.next()){
                        dMax = rs.getDouble("maxValue");
                        dMin = rs.getDouble("minValue");
                        iMax = rs.getInt("maxLength");
                        iMin = rs.getInt("minLength");
                        regExp = oscar.Misc.getString(rs,"regularExp");
                    }
                    rs.close();

                    if(!ectValidation.isInRange(dMax, dMin, inputValue)){
                        errors.add(inputValueName, new ActionMessage("errors.range", inputTypeDisplay, Double.toString(dMin), Double.toString(dMax)));
                        saveErrors(request, errors);
                        valid = false;
                    }
                    if(!ectValidation.maxLength(iMax, inputValue)){
                        errors.add(inputValueName, new ActionMessage("errors.maxlength", inputTypeDisplay, Integer.toString(iMax)));
                        saveErrors(request, errors);
                        valid = false;
                    }
                    if(!ectValidation.minLength(iMin, inputValue)){
                        errors.add(inputValueName, new ActionMessage("errors.minlength", inputTypeDisplay, Integer.toString(iMin)));
                        saveErrors(request, errors);
                        valid = false;
                    }

                    if(!ectValidation.matchRegExp(regExp, inputValue)){
                        errors.add(inputValueName,
                        new ActionMessage("errors.invalid", inputTypeDisplay));
                        saveErrors(request, errors);
                        valid = false;
                    }
                    if(!ectValidation.isValidBloodPressure(regExp, inputValue)){
                        errors.add(inputValueName,
                        new ActionMessage("errors.invalid", inputTypeDisplay));
                        saveErrors(request, errors);
                        valid = false;
                    }
                    if(!ectValidation.isDate(dateObserved)&&inputValue.compareTo("")!=0){
                        errors.add(dateName,
                        new ActionMessage("errors.date", inputTypeDisplay));
                        saveErrors(request, errors);
                        valid = false;
                    }
                }

                //Write to database and to encounter form if all the input values are valid
                if(valid){
                    for(int i=0; i<iType; i++){

                        inputValueName = "inputValue-" + i;
                        inputTypeName = "inputType-" + i;
                        mInstrcName = "inputMInstrc-" + i;
                        commentsName = "comments-" + i;
                        validationName = "validation-" + i;
                        dateName = "date-" + i;

                        inputValue = (String) frm.getValue(inputValueName);
                        inputType = (String) frm.getValue(inputTypeName);
                        mInstrc = (String) frm.getValue(mInstrcName);
                        comments = (String) frm.getValue(commentsName);
                        comments = org.apache.commons.lang.StringEscapeUtils.escapeSql(comments);
                        validation = (String) frm.getValue(validationName);
                        dateObserved = (String) frm.getValue(dateName);

                        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
                        if(!GenericValidator.isBlankOrNull(inputValue)){
                            //Find if the same data has already been entered into the system
                            String sql = "SELECT * FROM measurements WHERE demographicNo='"+demographicNo+ "' AND dataField='"+inputValue
                                        +"' AND measuringInstruction='" + mInstrc + "' AND comments='" + comments
                                        + "' AND dateObserved='" + dateObserved + "' and type = '"+inputType+"'";

                            rs = DBHandler.GetSQL(sql);
                            if(!rs.next()){
                                //Write to the Dababase if all input values are valid
                                sql = "INSERT INTO measurements"
                                        +"(type, demographicNo, providerNo, dataField, measuringInstruction, comments, dateObserved, dateEntered)"
                                        +" VALUES ('"+str.q(inputType)+"','"+str.q(demographicNo)+"','"+str.q(providerNo)+"','"+str.q(inputValue)+"','"
                                        + str.q(mInstrc)+"','"+str.q(comments)+"','"+str.q(dateObserved)+"','"+str.q(dateEntered)+"')";

                                DBHandler.RunSQL(sql);
                                //prepare input values for writing to the encounter form
                                if (mFlowsheet == null){
                                    textOnEncounter =  textOnEncounter + inputType + "    " + inputValue + " " + mInstrc + " " + comments + "\\n";
                                }else{
                                    textOnEncounter += mFlowsheet.getFlowSheetItem(inputType).getDisplayName()+"    "+inputValue + " " +  comments + "\\n";
                                }
                            }
                            rs.close();
                        }

                    }
                    // textOnEncounter = textOnEncounter + "**********************************************************************************\\n";

                }
                else{
                    String groupName = (String) frm.getValue("groupName");
                    String css = (String) frm.getValue("css");
                    request.setAttribute("groupName", groupName);
                    request.setAttribute("css", css);
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
            }
            catch(SQLException e)
            {
                MiscUtils.getLogger().error("Error", e);
            }


        //put the inputvalue to the encounter form
        session.setAttribute( "textOnEncounter", textOnEncounter );

        return mapping.findForward("success");
    }


}
