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


package oscar.oscarBilling.pageUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

public final class BillingCreateBillingForm extends ActionForm {
    
    private String[] service;
    private String xml_provider, xml_location, xml_billtype;
    private String xml_endtime, xml_starttime, xml_appointment_date;
    private String xml_visittype, xml_vdate;
    private String xml_other1, xml_other2, xml_other3;
    private String xml_other1_unit, xml_other2_unit, xml_other3_unit;
    private String xml_refer1, xml_refer2, refertype1, refertype2;
    private String xml_diagnostic_detail1, xml_diagnostic_detail2, xml_diagnostic_detail3;
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_provider(){
        return xml_provider != null ? xml_provider : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_provider(String xml_provider){
        this.xml_provider = xml_provider;
    }
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_location(){
        return xml_location != null ? xml_location : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_location(String xml_location){
        this.xml_location = xml_location;
    }
    
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_billtype(){
        return xml_billtype != null ? xml_billtype : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_billtype(String xml_billtype){
        this.xml_billtype = xml_billtype;
    }
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_endtime(){
        return xml_endtime != null ? xml_endtime : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_endtime(String xml_endtime){
        this.xml_endtime = xml_endtime;
    }
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_starttime(){
        return xml_starttime != null ? xml_starttime : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_starttime(String xml_starttime){
        this.xml_starttime = xml_starttime;
    }
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_appointment_date(){
        return xml_appointment_date != null ? xml_appointment_date : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_appointment_date(String xml_appointment_date){
        this.xml_appointment_date = xml_appointment_date;
    }
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_visittype(){
        return xml_visittype != null ? xml_visittype : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_visittype(String xml_visittype){
        this.xml_visittype = xml_visittype;
    }
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_vdate(){
        return xml_vdate != null ? xml_vdate : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_vdate(String xml_vdate){
        this.xml_vdate = xml_vdate;
    }
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_refer1(){
        return xml_refer1 != null ? xml_refer1 : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_refer1(String xml_refer1){
        this.xml_refer1 = xml_refer1;
    }
    
    
    
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_refer2(){
        return xml_refer2 != null ? xml_refer2 : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_refer2(String xml_refer2){
        this.xml_refer2 = xml_refer2;
    }
    
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getRefertype1(){
        return refertype1 != null ? refertype1 : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setRefertype1(String refertype1){
        this.refertype1 = refertype1;
    }
    
    
    
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getRefertype2(){
        return refertype2 != null ? refertype2 : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setRefertype2(String refertype2){
        this.refertype2 = refertype2;
    }
    
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_other1(){
        return xml_other1 != null ? xml_other1 : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_other1(String xml_other1){
        this.xml_other1 = xml_other1;
    }
    
    
    
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_other2(){
        return xml_other2 != null ? xml_other2 : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_other2(String xml_other2){
        this.xml_other2 = xml_other2;
    }
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_other3(){
        return xml_other3 != null ? xml_other3 : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_other3(String xml_other3){
        this.xml_other3 = xml_other3;
    }
    
    
    
    
    
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_other1_unit(){
        return xml_other1_unit != null ? xml_other1_unit : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_other1_unit(String xml_other1_unit){
        this.xml_other1_unit = xml_other1_unit;
    }
    
    
    
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_other2_unit(){
        return xml_other2_unit != null ? xml_other2_unit : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_other2_unit(String xml_other2_unit){
        this.xml_other2_unit = xml_other2_unit;
    }
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_other3_unit(){
        return xml_other3_unit != null ? xml_other3_unit : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_other3_unit(String xml_other3_unit){
        this.xml_other3_unit = xml_other3_unit;
    }
    
    
    
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_diagnostic_detail1(){
        return xml_diagnostic_detail1 != null ? xml_diagnostic_detail1 : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_diagnostic_detail1(String xml_diagnostic_detail1){
        this.xml_diagnostic_detail1 = xml_diagnostic_detail1;
    }
    
    
    
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_diagnostic_detail2(){
        return xml_diagnostic_detail2 != null ? xml_diagnostic_detail2 : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_diagnostic_detail2(String xml_diagnostic_detail2){
        this.xml_diagnostic_detail2 = xml_diagnostic_detail2;
    }
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getXml_diagnostic_detail3(){
        return xml_diagnostic_detail3 != null ? xml_diagnostic_detail3 : "" ;
    }
    
    /**
     *The set method for the message String
     */
    public void setXml_diagnostic_detail3(String xml_diagnostic_detail3){
        this.xml_diagnostic_detail3 = xml_diagnostic_detail3;
    }
    
    
    
    
    /**
     * An Array of Strings thats contains provider numbers
     * @return String[], the provider numbers that this message will be set to
     */
    public String[] getService(){
        if (service == null){
            service = new String[]{};
        }
        return service;
    }
    
    /**
     * The set method for an Array of Strings that contains services
     * @param service
     */
    public void setService(String[] service){
        this.service = service;
    }
    
    /**
     * Used to reset everything to a null value
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request){
        this.service = null;
        MiscUtils.getLogger().debug("RESET IS CALLED IN BILLING CREATE BILLING FORM");
        // this.message = null;
        // this.subject = null;
        
    }
    
    
    
    
    
    
    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     * @return fill in later
     */
    //public ActionErrors validate(ActionMapping mapping,
    //                               HttpServletRequest request) {
    
    //   ActionErrors errors = new ActionErrors();
    
    //   if (message == null || message.length() == 0){
    //      errors.add("message", new ActionError("error.message.missing"));
    //   }
    
    //   if (provider == null || provider.length == 0){
    //      errors.add(ActionErrors.GLOBAL_ERROR,
    //              new ActionError("error.provider.missing"));
    //   }
    
    //   return errors;
    
    //}
    
}//CreateMessageForm
