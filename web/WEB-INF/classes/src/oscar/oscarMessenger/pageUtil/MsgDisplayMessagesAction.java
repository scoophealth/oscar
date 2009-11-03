
// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarMessenger.pageUtil;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import oscar.oscarDB.DBHandler;

public class MsgDisplayMessagesAction extends Action {


    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
    // System.out.println("in display message action jackson");
            // Extract attributes we will need
            Locale locale = getLocale(request);
            MessageResources messages = getResources(request);

            // Setup variables            
            oscar.oscarMessenger.pageUtil.MsgSessionBean bean = null;
            String[] messageNo = ((MsgDisplayMessagesForm)form).getMessageNo();
            String providerNo;
            
            //Initialize forward location
            String findForward = "success";

            if(request.getParameter("providerNo")!=null & request.getParameter("userName")!=null)
            {
                // System.out.println("in display message action jackson4");
                bean = new oscar.oscarMessenger.pageUtil.MsgSessionBean();
                bean.setProviderNo(request.getParameter("providerNo"));
                bean.setUserName(request.getParameter("userName"));
                request.getSession().setAttribute("msgSessionBean", bean);
                                
            }//if
            else
            {
                bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)request.getSession().getAttribute("msgSessionBean");
            }//else
            
            
            /*
             *edit 2006-0811-01 by wreby
             *  Adding a search and clear search action to the DisplayMessages JSP
             */
            if (request.getParameter("btnSearch") != null) {
                oscar.oscarMessenger.pageUtil.MsgDisplayMessagesBean displayMsgBean 
                        = (oscar.oscarMessenger.pageUtil.MsgDisplayMessagesBean)request.getSession().getAttribute("DisplayMessagesBeanId");
                
                displayMsgBean.setFilter(request.getParameter("searchString"));
            }
            else if (request.getParameter("btnClearSearch") != null) {
                oscar.oscarMessenger.pageUtil.MsgDisplayMessagesBean displayMsgBean 
                        = (oscar.oscarMessenger.pageUtil.MsgDisplayMessagesBean)request.getSession().getAttribute("DisplayMessagesBeanId");
                displayMsgBean.clearFilter();
            }
            else if (request.getParameter("btnDelete")!=null) {
                //This will go through the array of message Numbers and set them
                //to del.which stands for deleted. but you prolly could have figured that out

                providerNo= bean.getProviderNo();
                for (int i =0 ; i < messageNo.length ; i++){
                  try{
                    DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                    java.sql.ResultSet rs;
                    String sql = new String("update messagelisttbl set status = \'del\' where provider_no = \'"+providerNo+"\' and message = \'"+messageNo[i]+"\'");
                    db.RunSQL(sql);
                  }catch (java.sql.SQLException e){ e.printStackTrace(System.out); }
                }//for
            }
            else {
                System.out.println("Unexpected action in MsgDisplayMessagesBean.java");
            }
           
            //System.out.println("findFoward: " + findForward);
    return (mapping.findForward(findForward));
    }

}
