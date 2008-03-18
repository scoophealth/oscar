package oscar.oscarBilling.ca.bc.pageUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.oscarDB.DBHandler;
import oscar.util.SqlUtils;

/*
 * Copyright (c) 2001-2002. Andromedia. All Rights Reserved. *

 * This software is published under the GPL GNU General Public License.

 * This program is free software; you can redistribute it and/or

 * modify it under the terms of the GNU General Public License

 * as published by the Free Software Foundation; either version 2

 * of the License, or (at your option) any later version. *

 * This program is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the

 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License

 * along with this program; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *

 *

 * <OSCAR TEAM>

 *

 * This software was written for

 * Andromedia, to be provided as

 * part of the OSCAR McMaster

 * EMR System

 *

 * @author Jef King

 * For The Oscar McMaster Project

 * Developed By Andromedia

 * www.andromedia.ca

 */

public final class WCBAction
    extends Action {
  String target = "success";

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response)

      throws IOException, ServletException {

    WCBForm frm = (WCBForm) form;
    request.setAttribute("WCBForm", frm);
    String doBill = request.getParameter("bill");
    String dosaveandbill = request.getParameter("saveandbill");
    String dosave = request.getParameter("save");
    String fromBilling = request.getParameter("fromBilling");
    if (null != form) {
      request.setAttribute("GOBACKWCB", "true");
      request.getSession().setAttribute("WCBForm", frm);
      if(doBill!=null){
        //go to billing screen
        frm.notBilled(true);
        target = "newbill";
      }
      else if(dosaveandbill!=null){
        //go to billing screen
        target = "newbill";
      }
      else if(dosave!=null){
        //save new wcb form
        if(!"true".equals(fromBilling)){
           createWCBEntry(frm);
          target = "viewformwcb";
          String actionPath = "/billing/CA/BC/viewformwcb.do?formId="+frm.getWcbFormId()+"&demographic_no="+frm.getDemographic_no();
          ActionForward forward = new ActionForward(actionPath);
          return forward;
        }
      }
      return (mapping.findForward(target));
    }
    else {
      return (mapping.getInputForward());
    }
  }

  /**
   * Inserts a new wcb form into db using a default value of '0' for billing_no
   * and bill amount
   * @param frm WCBForm
   */
  private void createWCBEntry(WCBForm frm) {
    DBHandler db = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL(frm.SQL("0", "0"));
      List idList = SqlUtils.getQueryResultsList("SELECT max(ID) from wcb");
      if(idList!=null){
        String[] id = (String[])idList.get(0);
        frm.setWcbFormId(id[0]);
      }

    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    finally{
      if(db!=null){
        try {
          db.CloseConn();
        }
        catch (SQLException ex1) {
          ex1.printStackTrace();
        }
      }
    }
  }
}
