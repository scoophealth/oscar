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


/*
 * MsgDisplayDemographicMessagesForm.java
 *
 * Created on May 8, 2005, 12:22 AM
 */

package oscar.oscarMessenger.pageUtil;

import org.apache.struts.action.ActionForm;
/**
 *
 * @author root
 */
public class MsgDisplayDemographicMessagesForm extends ActionForm {

      String[] messageNo;
      String unlinkMsg;

      /**
       * Used to get the MessageNo in the DisplayMessagesAction class
       * @return String[], these are the messages the will be set to del
       */
      public String[] getMessageNo(){
         if (messageNo == null){
            messageNo = new String[]{};
         }
      return messageNo;
      }

       /**
       * Used to set the MessageNo, these are the messageNo that will be set to be deleted
       * @param mess String[], these are the message No to be deleted
       */
      public void setMessageNo(String[] mess){
         this.messageNo = mess;
      }
      
      
}
