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


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class MsgProceedAction extends Action {

 public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {


    MsgProceedForm frm = (MsgProceedForm) form;

    String id;
    String demoId;

    oscar.oscarMessenger.pageUtil.MsgSessionBean bean;
    bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)request.getSession().getAttribute("msgSessionBean");


    demoId = frm.getDemoId();
    id = frm.getId();
    //id = oscar.oscarMessenger.docxfer.util.xml.decode64(id);

    try{
        
        java.sql.ResultSet rs;

        String sel = "select * from remoteAttachments where demographic_no = '"+demoId+"' and messageid = '"+id+"' ";

        rs = DBHandler.GetSQL(sel);

        if (rs.next()){
            rs.close();
            request.setAttribute("confMessage","1");
        }else{
            rs.close();
            String sql = "insert into remoteAttachments (demographic_no,messageid, savedBy,date,time) values "
            +"('"+demoId+"','"+id+"','"+bean.getUserName()+"','today', 'now')";
            DBHandler.RunSQL(sql);
            request.setAttribute("confMessage","2");

        }

    }catch (java.sql.SQLException e){MiscUtils.getLogger().error("Error", e); }

    bean.nullAttachment();


    return (mapping.findForward("success"));
    }
}
