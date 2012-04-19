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


package oscar.oscarMessenger.config.pageUtil;

import org.apache.struts.action.ActionForm;

public final class MsgMessengerCreateGroupForm extends ActionForm {
   String groupName;
   String parentID;
   String type;

   public void setType2(String type){

      this.type = type;
   }

   public String getType2(){

      if (this.type == null){
         this.type = new String();
      }
      return this.type;
   }

   public void setParentID(String parentID){
      this.parentID = parentID;
   }

   public String getParentID(){
      if (this.parentID == null){
         this.parentID = new String();
      }
      return this.parentID;

   }


   public void setGroupName(String groupName){
      this.groupName = groupName;
   }

   public String getGroupName(){
      if (this.groupName == null){
         this.groupName = new String();
      }
      return this.groupName;
   }


}
