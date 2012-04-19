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

public final class MsgViewMessageForm extends ActionForm {

  private String messageID;
  /**
   * Used to get the messge ID
   * @return String, the message ID to view
   */
  public String getMessageID(){
     return messageID != null ? messageID : "" ;
  }

  /**
   * Used to Set the Message ID
   * @param messageID
   */
  public void setMessageID(String messageID){
     this.messageID = messageID;
  }


  public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
     ActionErrors errors = new ActionErrors();
     return errors;
  }

}
