/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada  
 *
 * MsgViewMessageByPositionAction.java
 *
 * Created on May 14, 2005, 4:38 PM
 */

package oscar.oscarMessenger.pageUtil;

/**
 *
 * @author ichan
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.oscarDB.DBHandler;
import oscar.oscarProvider.data.ProviderData;
import oscar.util.ParameterActionForward;

public class MsgViewMessageByPositionAction extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
        // System.out.println("in view message action jackson");
        // Extract attributes we will need
        String provNo = (String) request.getSession().getAttribute("user");
        
        if ( request.getSession().getAttribute("msgSessionBean") == null){
           MsgSessionBean bean = new MsgSessionBean();
           bean.setProviderNo(provNo);
           ProviderData pd = new ProviderData();
           bean.setUserName(pd.getProviderName(provNo));
           request.getSession().setAttribute("msgSessionBean", bean); 
       } 
        
        
        String orderBy = request.getParameter("orderBy")==null?"date":request.getParameter("orderBy");        
        String messagePosition = request.getParameter("messagePosition");           
        String demographic_no = request.getParameter("demographic_no");   
        String from = request.getParameter("from")==null?"oscarMessenger":request.getParameter("from");
        MsgDisplayMessagesBean displayMsgBean = new MsgDisplayMessagesBean();
        ParameterActionForward actionforward = new ParameterActionForward(mapping.findForward("success"));
        
        try{
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           java.sql.ResultSet rs;
           
                //String sql = new String("select m.messageid from messagelisttbl ml, messagetbl m, msgDemoMap map"
                //+" where map.demographic_no = '"+ demographic_no+"' and remoteLocation = '"+displayMsgBean.getCurrentLocationId()+"' "
                //+" and m.messageid = map.messageID and ml.message=m.messageid order by "+ displayMsgBean.getOrderBy(orderBy) + " limit " + messagePosition +", 1");
       
                String sql = "select m.messageid  " +
                             "from  messagetbl m, msgDemoMap map where map.demographic_no = '"+ demographic_no+"'  " +
                             "and m.messageid = map.messageID  order by "+ displayMsgBean.getOrderBy(orderBy) + " limit " + messagePosition +", 1";
        
                
                System.out.println("this ="+sql);
                rs = db.GetSQL(sql);
                if (rs.next()) {                                                                   
                    actionforward.addParameter("messageID", db.getString(rs,"messageid"));
                    actionforward.addParameter("from", "encounter");     
                    actionforward.addParameter("demographic_no", demographic_no);
                    actionforward.addParameter("messagePostion", messagePosition);
                }
              
         rs.close();

        }
        catch (java.sql.SQLException e){ 
            e.printStackTrace(System.out); 
        }
                               
        return actionforward;
    }

}

