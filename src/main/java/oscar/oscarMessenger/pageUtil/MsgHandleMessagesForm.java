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

import org.apache.struts.action.ActionForm;



public final class MsgHandleMessagesForm extends ActionForm {

    String reply;
    String replyAll;
    String delete;
    String forward;
    String messageNo;
    String demographic_no;

    public String getForward (){
        if ( this.forward == null){
            this.forward = new String();
        }
        return this.forward ;
    }

    public void setForward (String str){
        this.forward = str;
    }

    public void setMessageNo(String messageNo){
      this.messageNo = messageNo;
    }

    public String getMessageNo(){
      return messageNo != null ? messageNo : "";
    }

    public void setReply(String reply){
      this.reply = reply;
      this.replyAll = "";
      this.delete = "";
    }

    public String getReply(){
      return reply != null ? reply : "" ;
    }

    public void setReplyAll(String replyAll){
      this.replyAll = replyAll;
      this.reply = "";
      this.delete = "";
    }

    public String getReplyAll(){
      return replyAll != null ? replyAll : "" ;
    }

    public void setDelete(String delete){
      this.delete = delete;
      this.reply = "";
      this.replyAll = "";
    }

    public String getDelete(){
      return delete != null ? delete : "" ;
    }
    public String getDemographic_no(){
            return demographic_no;
        }

    public void setDemographic_no(String demographic_no){
        this.demographic_no = demographic_no;
    }

}
