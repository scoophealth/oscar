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

public final class MsgSendMessageForm extends ActionForm {

  private String[] provider;
  private String message,subject;

  public String getSubject(){
    return subject != null ? subject : "" ;
  }

  public void setSubject(String subject){
  this.subject = subject;
  }

  public String getMessage(){

  return message != null ? message : "" ;

  }

  public void setMessage(String msg){

  this.message = msg;
  }



  public String[] getProvider(){

  if (provider == null){
          provider = new String[]{};//{"174","176","0"};

  }

      return provider;
  }

  public void setProvider(String[] prov){

    this.provider = prov;

    for (int i =0 ; i < prov.length ; i++)
    {

    }
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

        if (message == null || message.length() == 0){   //index.heading

 //             errors.add("message32",new ActionError("error.message.missing"));
              //errors.add("message32",new ActionError("index.heading"));
             

              errors.add("message", new ActionMessage("index.heading"));

        }


        return errors;

    }

}//CreateMessageForm
