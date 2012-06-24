/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.PMmodule.utility.Utility;

//###############################################################################
public class PassIntakeFormVars
{
	private String demographicNo = "";

	private String clientFirstName = "";
	private String clientSurname = "";
	private String clientDateOfBirth = "";
	private String healthCardNum = "";
	private String healthCardVer = "";

	private String providerNo = "0";

	private String searchForClient = "";
	private String retrieveAll = "";

	private String viewIntakeA = "";
	private String viewIntakeB = "";
	private String viewIntakeC = "";
	private String formIntakeBLock = "N";
	private String actionType = "";//i.e. 'update' <-- only

	private String startDate = "";
	private String endDate = "";

	public PassIntakeFormVars(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession(false);

		demographicNo = StringUtils.trimToEmpty(request.getParameter("demographicNo"));
		if (demographicNo == null || demographicNo.equals(""))
		{
			if (session.getAttribute("demographicNo") != null)
			{
				demographicNo = (String)session.getAttribute("demographicNo");
			}
			else
			{
				demographicNo = "";
			}
		}
		else
		{
			session.setAttribute("demographicNo", demographicNo);
		}

		clientFirstName = StringUtils.trimToEmpty(request.getParameter("clientFirstName"));
		if (clientFirstName == null || clientFirstName.equals(""))
		{
			if (session.getAttribute("clientFirstName") != null)
			{
				clientFirstName = (String)session.getAttribute("clientFirstName");
				clientFirstName = Utility.convertToRelacementStrIfNull(clientFirstName, "");
			}
			else
			{
				clientFirstName = "";
			}
		}
		else
		{
			session.setAttribute("clientFirstName", clientFirstName);
		}

		clientSurname = StringUtils.trimToEmpty(request.getParameter("clientSurname"));

		if (clientSurname == null || clientSurname.equals(""))
		{
			if (session.getAttribute("clientSurname") != null)
			{
				clientSurname = (String)session.getAttribute("clientSurname");
				clientSurname = Utility.convertToRelacementStrIfNull(clientSurname, "");
			}
			else
			{
				clientSurname = "";
			}
		}
		else
		{
			session.setAttribute("clientSurname", clientSurname);
		}

		if (session.getAttribute("user") != null)//passed in from oscar session var
		{
			providerNo = (String)session.getAttribute("user");
			session.setAttribute("providerNo", providerNo);
		}
		else
		{
			providerNo = "0";
			session.setAttribute("providerNo", "0");
		}

		clientDateOfBirth = request.getParameter("clientDateOfBirth");
		if (clientDateOfBirth == null || clientDateOfBirth.equals(""))
		{
			if (request.getAttribute("clientDateOfBirth") != null)
			{
				clientDateOfBirth = (String)request.getAttribute("clientDateOfBirth");
			}
			else
			{
				clientDateOfBirth = "";
				request.setAttribute("clientDateOfBirth", "");
			}
		}

		healthCardNum = request.getParameter("healthCardNum");
		if (healthCardNum == null || healthCardNum.equals(""))
		{
			if (request.getAttribute("healthCardNum") != null)
			{
				healthCardNum = (String)request.getAttribute("healthCardNum");
			}
			else
			{
				healthCardNum = "";
				request.setAttribute("healthCardNum", "");

			}
		}

		healthCardVer = request.getParameter("healthCardVer");
		if (healthCardVer == null || healthCardVer.equals(""))
		{
			if (request.getAttribute("healthCardVer") != null)
			{
				healthCardVer = (String)request.getAttribute("healthCardVer");
			}
			else
			{
				healthCardVer = "";
				request.setAttribute("healthCardVer", "");

			}
		}

		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		searchForClient = request.getParameter("searchForClient");
		if (searchForClient == null)
		{
			searchForClient = (String)session.getAttribute("searchForClient");
			searchForClient = StringUtils.trimToEmpty(searchForClient);
		}
		else
		{
			session.setAttribute("searchForClient", searchForClient);
		}

		retrieveAll = request.getParameter("retrieveAll");
		if (retrieveAll == null)
		{
			retrieveAll = (String)session.getAttribute("retrieveAll");
			retrieveAll = Utility.convertToRelacementStrIfNull(retrieveAll, "N");
		}
		else
		{
			session.setAttribute("retrieveAll", retrieveAll);
		}

		/*    
		    if(searchForClient.equalsIgnoreCase("Y"))
		    {
		    	retrieveAll = "N";
		    	session.setAttribute("retrieveAll",retrieveAll);
		    	
		    }
		    else if(retrieveAll.equalsIgnoreCase("Y"))
		    {
		    	searchForClient = "N";
		    	session.setAttribute("searchForClient",searchForClient);
		    }
		*/
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
		viewIntakeA = StringUtils.trimToEmpty(request.getParameter("viewIntakeA"));
		if (viewIntakeA == null || viewIntakeA.equals(""))
		{
			if (request.getAttribute("viewIntakeA") != null)
			{
				viewIntakeA = Utility.convertToRelacementStrIfNull((String)request.getAttribute("viewIntakeA"), "N");
			}
		}

		viewIntakeB = StringUtils.trimToEmpty(request.getParameter("viewIntakeB"));
		if (viewIntakeB == null || viewIntakeB.equals(""))
		{
			if (request.getAttribute("viewIntakeB") != null)
			{
				viewIntakeB = Utility.convertToRelacementStrIfNull((String)request.getAttribute("viewIntakeB"), "N");
			}
		}

		viewIntakeC = StringUtils.trimToEmpty(request.getParameter("viewIntakeC"));
		if (viewIntakeC == null || viewIntakeC.equals(""))
		{
			if (request.getAttribute("viewIntakeC") != null)
			{
				viewIntakeC = Utility.convertToRelacementStrIfNull((String)request.getAttribute("viewIntakeC"), "N");
			}
		}

		formIntakeBLock = StringUtils.trimToEmpty(request.getParameter("formIntakeBLock"));
		if (formIntakeBLock == null || formIntakeBLock.equals(""))
		{
			if (request.getAttribute("formIntakeBLock") != null)
			{
				formIntakeBLock = Utility.convertToRelacementStrIfNull((String)request.getAttribute("formIntakeBLock"), "N");
			}
		}
		actionType = StringUtils.trimToEmpty(request.getParameter("actionType"));
		if (actionType == null || actionType.equals(""))
		{
			if (request.getAttribute("actionType") != null)
			{
				actionType = StringUtils.trimToEmpty((String)request.getAttribute("actionType"));
			}
		}
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
		startDate = StringUtils.trimToEmpty(request.getParameter("startDate"));
		if (startDate == null || startDate.equals(""))
		{
			if (request.getAttribute("startDate") != null)
			{
				startDate = StringUtils.trimToEmpty((String)request.getAttribute("startDate"));
			}
		}

		endDate = StringUtils.trimToEmpty(request.getParameter("endDate"));
		if (endDate == null || endDate.equals(""))
		{
			if (request.getAttribute("endDate") != null)
			{
				endDate = StringUtils.trimToEmpty((String)request.getAttribute("endDate"));
			}
		}

		//###############################################################################
	}//end of PassPagingVars(HttpServletRequest request)

	public String getDemographicNo()
	{
		return this.demographicNo;
	}

	public String getClientFirstName()
	{
		return this.clientFirstName;
	}

	public String getClientSurname()
	{
		return this.clientSurname;
	}

	public String getProviderNo()
	{
		return this.providerNo;
	}

	public String getClientDateOfBirth()
	{
		return this.clientDateOfBirth;
	}

	public String getHealthCardNum()
	{
		return this.healthCardNum;
	}

	public String getHealthCardVer()
	{
		return this.healthCardVer;
	}

	public String getRetrieveAll()
	{
		return this.retrieveAll;
	}

	public String getSearchForClient()
	{
		return this.searchForClient;
	}

	public String getViewIntakeA()
	{
		return this.viewIntakeA;
	}

	public String getViewIntakeB()
	{
		return this.viewIntakeB;
	}

	public String getViewIntakeC()
	{
		return this.viewIntakeC;
	}

	public String getFormIntakeBLock()
	{
		return this.formIntakeBLock;
	}

	public String getActionType()
	{
		return this.actionType;
	}

	public String getStartDate()
	{
		return this.startDate;
	}

	public String getEndDate()
	{
		return this.endDate;
	}

	//################################################################################
}//end of class PassIntakeFormVars
