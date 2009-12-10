/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.oscarRx.pageUtil;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.actions.DispatchAction;
import org.apache.xmlrpc.*;
import oscar.oscarRx.util.MyDrugrefComparator;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.WebApplicationContext;
import org.oscarehr.common.dao.*;
import org.oscarehr.common.model.*;
import oscar.OscarProperties;
import oscar.oscarRx.util.TimingOutCallback;
import oscar.oscarRx.util.TimingOutCallback.TimeoutException;


public final class RxRxPageSizeInfoAction extends DispatchAction {

    private static Log log2 = LogFactory.getLog(RxMyDrugrefInfoAction.class);
  /*  private void p(String s){
        System.out.println(s);
    }
    private void p(String s1,String s2){
        System.out.println(s1+"="+s2);
    }*/
    public ActionForward view(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {
       // p("===========view in RxRxPageSizeInfoAction.java=====================");
        long start = System.currentTimeMillis();
        String provider = (String) request.getSession().getAttribute("user");

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
       // p("ctx",ctx.getDisplayName());
        UserPropertyDAO  propDAO =  (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
       // p("propDAO",propDAO.toString());

        UserProperty prop = propDAO.getProp(provider, UserProperty.RX_PAGE_SIZE);
        String rxPageSize = null;
        if (prop != null){
           // p("if2");
            rxPageSize = prop.getValue();
        }

      //  p("rxpagesize",rxPageSize);

        request.getSession().setAttribute("rxPageSize", rxPageSize);
        ///////
        log2.debug("MyDrugref return time " + (System.currentTimeMillis() - start) );
        //p("===========end of view in RxRxPageSizeInfoAction.java=====================");
        return mapping.findForward("success");
    }
}