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


import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.data.MsgAddressBookMaker;

public class MsgMessengerAdminAction extends Action {

 public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

        String[] providers = ((MsgMessengerAdminForm)form).getProviders();
        String grpNo = ((MsgMessengerAdminForm)form).getGrpNo();
        String update = ((MsgMessengerAdminForm)form).getUpdate();
        String delete = ((MsgMessengerAdminForm)form).getDelete();

        String parent = new String();
        
        ResourceBundle oscarR = ResourceBundle.getBundle("oscarResources",request.getLocale());

        if (update.equals(oscarR.getString("oscarMessenger.config.MessengerAdmin.btnUpdateGroupMembers"))){

           try{
              
              java.sql.ResultSet rs;
              String sql = new String("delete from groupMembers_tbl where groupID = '"+grpNo+"'");
              DBHandler.RunSQL(sql);
              for (int i = 0; i < providers.length ; i++){
                  sql = new String("insert into groupMembers_tbl (groupID,provider_No) values ('"+grpNo+"','"+providers[i]+"')");
                  DBHandler.RunSQL(sql);
              }
              
              MsgAddressBookMaker addMake = new MsgAddressBookMaker();
              boolean  res = addMake.updateAddressBook();
           }catch (java.sql.SQLException e){MiscUtils.getLogger().error("Error", e); }


         request.setAttribute("groupNo",grpNo);
        }else if(delete.equals(oscarR.getString("oscarMessenger.config.MessengerAdmin.btnDeleteThisGroup"))){

            try{
                 
                 java.sql.ResultSet rs;

                 String sql = new String("select parentID from groups_tbl where groupID = '"+grpNo+"'");
                 rs = DBHandler.GetSQL(sql);
                 if (rs.next()){
                     parent =  oscar.Misc.getString(rs, "parentID");
                 }


                 sql = new String("select * from groups_tbl where parentID = '"+grpNo+"'");
                 rs = DBHandler.GetSQL(sql);

                 if (rs.next()){
                    request.setAttribute("groupNo",grpNo);
                    request.setAttribute("fail","This Group has Children, you must delete the children groups first");
                    return (mapping.findForward("failure"));
                 }else{
                    sql = new String("delete from groupMembers_tbl where groupID = '"+grpNo+"'");
                    DBHandler.RunSQL(sql);

                    sql = new String("delete from groups_tbl where groupID = '"+grpNo+"'");
                    DBHandler.RunSQL(sql);

                 }
              rs.close();
              MsgAddressBookMaker addMake = new MsgAddressBookMaker();
              boolean res = addMake.updateAddressBook();
           }catch (java.sql.SQLException e){MiscUtils.getLogger().error("Error", e); }

         request.setAttribute("groupNo",parent);
        }

      return (mapping.findForward("success"));
 }




}
