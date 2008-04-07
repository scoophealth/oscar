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
    HashTable _functionList; 
    public void init(String providerNo)
    {
    	// _list is ordered by Function, privilege (desc) and the org
        List <UserAccessValue>list = _dao.GetUserAccessList(providerNo);
    	int startIdx = 0;
    	List orgList = getAccessListForFunction(list,startIdx);
    	_functionList = new HashTable();
    	_functionList.put(list.get(startIdx).getFunctionCd(), list);

    	while(orgList != null && startIdx + orgList.size()<list.size())
    	{
    		startIdx += orgList.size();
        	orgList = getAccessListForFunction(list,startIdx);
        	_functionList.put(list.get(startIdx).getFunctionCd(), orgList);
    	}
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
    public String GetAccess(String functioncd, String orgcd)
    {
        String privilege = UserAccessValue.ACCESS_NONE;
        try
        {
            List <UserAccessValue> orgList =  (List <UserAccessValue>) _functionList.get(functioncd);
            for(UserAccessValue uav:orgList)
            {
            	if (Utility.IsEmpty(uav.getOrgCd()) || orgcd.startsWith(uav.getOrgCd())){
            		privilege = uav.getPrivilege();
            		break;
            	}
            }
        }
        catch (Exception ex)
        {
        	;
        }
        return privilege;
    }

    public String GetAccess(String function)
    {
        return GetAccess(function, "");
    }
    public void setUserAccessDao(UserAccessDao dao)
    {
    	_dao = dao;
    }
}
