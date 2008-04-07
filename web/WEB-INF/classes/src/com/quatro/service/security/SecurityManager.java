package com.quatro.service.security;
import java.util.*;

import com.quatro.model.security.UserAccessValue;
import com.quatro.util.Utility;
public class SecurityManager {
	public static final String ACCESS_NONE = "o";
	public static final String ACCESS_READ = "r";
	public static final String ACCESS_UPDATE = "u";
	public static final String ACCESS_WRITE = "w";
	public static final String ACCESS_ALL = "x";

	Hashtable _userFunctionAccessList;

	public void set_userFunctionAccessList(Hashtable functionAccessList) {
		_userFunctionAccessList = functionAccessList;
	}
    public String GetAccess(String functioncd, String orgcd)
    {
        String privilege = this.ACCESS_NONE;
        try
        {
            List <UserAccessValue> orgList =  (List <UserAccessValue>) _userFunctionAccessList.get(functioncd);
            for(UserAccessValue uav:orgList)
            {
            	if (uav.isOrgApplicable()) {
	            	if (Utility.IsEmpty(uav.getOrgCd()) || orgcd.startsWith(uav.getOrgCd())){
	            		privilege = uav.getPrivilege();
	            		break;
	            	}
            	}
            	else
            	{
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

}
