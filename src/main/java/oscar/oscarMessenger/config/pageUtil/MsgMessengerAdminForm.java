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

public final class MsgMessengerAdminForm extends ActionForm {
   String grpNo;
   String[] provider;
   String update;
   String delete;


    public String getUpdate(){

       if (this.update == null){
          this.update = new String();
       }
       return update;
    }

    public void setUpdate(String update){

       this.update = update;
    }

    public String getDelete(){

       if (this.delete == null){
          this.delete = new String();
       }
       return delete;
    }

    public void setDelete(String delete){

       this.delete = delete;
    }




    public String[] getProviders(){
       if (this.provider == null){
          this.provider = new String[] {};
       }
       return this.provider;
    }

    public void setProviders(String[] prov){
       this.provider = prov;
    }

    public String getGrpNo(){
       if (this.grpNo == null){
          this.grpNo = new String();
       }
       return this.grpNo;
    }

    public void setGrpNo(String grpNo){
       this.grpNo = grpNo;
    }

}
