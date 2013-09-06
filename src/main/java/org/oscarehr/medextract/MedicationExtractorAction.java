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

package org.oscarehr.medextract;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
//import org.apache.struts.action.ActionRedirect;
import org.apache.struts.action.Action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.oscarehr.util.MiscUtils;
import org.apache.log4j.Logger;
import oscar.oscarRx.pageUtil.RxSessionBean;


/**
 *
 * @author sdiemert
 * @date
 */
public class MedicationExtractorAction extends Action {
		private static Logger logger=MiscUtils.getLogger();
        MedicationExtractor medExtract;
        
		public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
              HttpSession session = request.getSession();
                  String t = request.getParameter("text");
                  String d = request.getParameter("demoid");
                  //this.list_text = this.getListText();
                  //this.medList = this.getMedsFromLancet(this.list_text);


            try{
              String user_no = (String) request.getSession().getAttribute("user");
              RxSessionBean bean = new RxSessionBean();
              bean.setProviderNo(user_no);
              bean.setDemographicNo(Integer.parseInt(d));
              request.getSession().setAttribute("RxSessionBean", bean);
            }catch(Exception exp){
                logger.error("ERROR: MedicationExtractorAction: 64");
                logger.error(exp.toString());
            }


              logger.info("text : "+t);
              logger.info("demo id: "+d);

              try{
                  this.medExtract = new MedicationExtractor(t,d, request.getContextPath());
              }catch(Exception e){
                  logger.error(e.toString());
              }
              //ActionRedirect redirect = new ActionRedirect(mapping.findForward("success"));
              session.setAttribute("medExtract", this.medExtract);
              return null;
		}
}
