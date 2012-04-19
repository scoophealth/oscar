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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarRx.pageUtil;

import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import oscar.oscarRx.util.RxDrugRef;

/**
 *
 * @author jackson
 */
public class RxUpdateDrugref  extends DispatchAction{
  public ActionForward updateDB(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception, ServletException {
        RxDrugRef drugref = new RxDrugRef();
        String s= drugref.updateDB();

        HashMap<String,Object> d = new HashMap<String,Object>();
        d.put("result",s);
        response.setContentType("text/x-json;charset=UTF-8");

        JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d );
        jsonArray.write(response.getWriter());
        return null;
    }
    public ActionForward getLastUpdate(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception, ServletException {
        RxDrugRef drugref = new RxDrugRef();
        String s= drugref.getLastUpdateTime();
        HashMap<String,String> d = new HashMap<String,String>();
        d.put("lastUpdate",s);
        //response.setContentType("text/x-json;charset=UTF-8");
        JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d );
        jsonArray.write(response.getWriter());
        return null;
    }
}
