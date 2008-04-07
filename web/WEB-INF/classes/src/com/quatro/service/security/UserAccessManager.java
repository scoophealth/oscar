package com.quatro.service.security;
import java.util.*;
import com.quatro.util.*;
import org.apache.batik.dom.util.HashTable;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.quatro.dao.security.UserAccessDao;
import com.quatro.model.security.*;
public class UserAccessManager
{
    private UserAccessDao _dao=null;
    public SecurityManager getUserUserSecurityManager(String providerNo)
    {
    	// _list is ordered by Function, privilege (desc) and the org
    	SecurityManager secManager = new SecurityManager();
    	
    	Hashtable functionList = new Hashtable();
        List <UserAccessValue>list = _dao.GetUserAccessList(providerNo);
    	if (list.size()>0) {
	    	int startIdx = 0;
	    	List orgList = getAccessListForFunction(list,startIdx);
	    	functionList.put(list.get(startIdx).getFunctionCd(), list);
	
	    	while(orgList != null && startIdx + orgList.size()<list.size())
	    	{
	    		startIdx += orgList.size();
	        	orgList = getAccessListForFunction(list,startIdx);
	        	functionList.put(list.get(startIdx).getFunctionCd(), orgList);
	    	}
    	}
    	secManager.set_userFunctionAccessList(functionList);
    	return secManager;
    }
    private List getAccessListForFunction(List <UserAccessValue> list, int startIdx)
    {
    	if (startIdx >= list.size()) return null;
    	List<UserAccessValue> orgList = new ArrayList<UserAccessValue>();
    	UserAccessValue uofv = (UserAccessValue) list.get(startIdx);
    	String functionCd = uofv.getFunctionCd();
    	orgList.add(uofv);
    	startIdx ++;
    	while (startIdx < list.size()) {
        	uofv = (UserAccessValue) list.get(startIdx);
    		if (uofv.getFunctionCd().equals(functionCd)) {
    			orgList.add(uofv);
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
