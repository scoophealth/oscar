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


package oscar;
/**
*
* Description - BillingDataServlet
*/
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.oscarehr.util.MiscUtils;


public class BillingDataServlet extends HttpServlet implements java.io.Serializable
{

/*   ****************************************************************************
   * Process incoming requests for information
   *
   * @param request Object that encapsulates the request to the servlet
   * @param response Object that encapsulates the response from the servlet
   */
   public void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws IOException, ServletException
   {
      try
      {

//          instantiate the beans and store them so they can be accessed by the called page
         BillingDataBean billingDataBean = new BillingDataBean();

//      	 javax.servlet.ServletRequest.setAttribute("billingDataBean", billingDataBean);


         billingDataBean.setBilling_no(request.getParameter("billing_no"));


         billingDataBean.setHin(request.getParameter("hin"));


         billingDataBean.setVisittype(request.getParameter("visittype"));


         billingDataBean.setVisitdate(request.getParameter("visitdate"));

//          Initialize the bean state property from the parameters
         billingDataBean.setStatus(request.getParameter("status"));

//          Initialize the bean zip property from the parameters
//         billingDataBean.setZip(request.getParameter("zip"));

//          Initialize the bean phone property from the parameters
//         billingDataBean.setPhone(request.getParameter("phone"));

//          Initialize the bean email property from the parameters
//         billingDataBean.setEmail(request.getParameter("email"));

// Call the output page.

	//	 RequestDispatcher dispatch = getServletContext().getRequestDispatcher(getInitParameter("result_page"));
	//	 dispatch.forward(request, response);


        response.sendRedirect(getServletConfig().getInitParameter("result_page"));
      }

      catch (Exception theException)
      {
    	  MiscUtils.getLogger().error("Error", theException);
      }
   }
}
