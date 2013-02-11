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


package oscar.oscarMessenger.pageUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public final class MsgCreateMessageForm extends ActionForm {
    
    private String[] provider;
    private String message,subject;
    private String demographic_no;
    
    /**
     * The get method for the subject String
     * @return String, this is the subject of the message
     */
    public String getSubject(){
        return subject != null ? subject : "" ;
    }
    
    /**
     * The set method for the subject String
     * @param subject String, this is the subject of the message
     */
    public void setSubject(String subject){
        this.subject = subject;
    }
    
    /**
     * The get method for the message String
     * @return String, this is the text of the message
     */
    public String getMessage(){
        return message != null ? message : "" ;
    }
    
    /**
     *The set method for the message String
     * @param msg String, The text of a message
     */
    public void setMessage(String msg){
        this.message = msg;
    }
    
    
    
    /**
     * An Array of Strings thats contains provider numbers
     * @return String[], the provider numbers that this message will be set to
     */
    public String[] getProvider(){
        if (provider == null){
            provider = new String[]{};
        }
        return provider;
    }
    
    /**
     * The set method for an Array of Strings that contains provider numbers
     * @param prov
     */
    public void setProvider(String[] prov){
        this.provider = prov;
    }
    
    /**
     * The get method for the demographic_no String
     * @return String, this is the text of the demographic_no
     */
    public String getDemographic_no(){
        return demographic_no;
    }
    
    /**
     *The set method for the demographic no
     * @param demographic_no String, The demographic no
     */
    public void setDemographic_no(String demographic_no){
        this.demographic_no = demographic_no;
    }
    
    /**
     * Used to reset everything to a null value
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request){
        this.provider = null;
        this.message = null;
        this.subject = null;
        
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
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();
        
        if (message == null || message.length() == 0){
            errors.add("message", new ActionMessage("error.message.missing"));
        }
        
        if (provider == null || provider.length == 0){
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("error.provider.missing"));
        }
        
        return errors;
        
    }
    
}//CreateMessageForm
