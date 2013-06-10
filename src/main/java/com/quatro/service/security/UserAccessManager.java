/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */
package com.quatro.service.security;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import oscar.OscarProperties;

import com.quatro.dao.security.UserAccessDao;
import com.quatro.model.security.UserAccessValue;
import com.quatro.service.LookupManager;
public class UserAccessManager
{
    private UserAccessDao _dao=null;
    public SecurityManager getUserSecurityManager(String providerNo,Integer shelterId, LookupManager lkManager)
    {
    	// _list is ordered by Function, privilege (desc) and the org
    	SecurityManager secManager = new SecurityManager();
    	
    	Hashtable functionList = new Hashtable();
        List list = _dao.GetUserAccessList(providerNo,shelterId);
    	if (list.size()>0) {
	    	int startIdx = 0;
	    	List orgList = getAccessListForFunction(list,startIdx);
	    	UserAccessValue uav = (UserAccessValue)list.get(startIdx);
	    	functionList.put(uav.getFunctionCd(), orgList);
	
	    	while(orgList != null && startIdx + orgList.size()<list.size())
	    	{
	    		startIdx += orgList.size();
	        	orgList = getAccessListForFunction(list,startIdx);
	        	
		    	uav = (UserAccessValue)list.get(startIdx);
		    	functionList.put(uav.getFunctionCd(), orgList);
	    	}
    	}
    	secManager.setUserFunctionAccessList(functionList);
    	List orgs = _dao.GetUserOrgAccessList(providerNo,shelterId);
    	String orgRoot = OscarProperties.getInstance().getProperty("ORGROOT");
    	if(orgs.size() > 0 && orgRoot!=null && orgRoot.equals(orgs.get(0))) 
    	{
    		orgs.clear();
    	}
    	secManager.setUserOrgAccessList(orgs);
    	return secManager;
    }
    private List getAccessListForFunction(List  list, int startIdx)
    {
    	if (startIdx >= list.size()) return null;
    	List orgList = new ArrayList();
    	UserAccessValue uofv = (UserAccessValue) list.get(startIdx);
    	String functionCd = uofv.getFunctionCd();
    	orgList.add(uofv);
    	startIdx ++;
    	while (startIdx < list.size()) {
        	uofv = (UserAccessValue) list.get(startIdx);
    		if (uofv.getFunctionCd().equals(functionCd)) {
    			orgList.add(uofv);
        		startIdx ++;
    		}
    		else
    		{
    			break;
    		}
    	}
    	return orgList;
    }
    public void setUserAccessDao(UserAccessDao dao)
    {
    	_dao = dao;
    }
}
