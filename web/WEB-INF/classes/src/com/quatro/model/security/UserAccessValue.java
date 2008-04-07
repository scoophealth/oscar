package com.quatro.model.security;
import java.io.*;

public class UserAccessValue implements Serializable {
		public static final String ACCESS_NONE = "N";
		public static final String ACCESS_READ = "R";
		public static final String ACCESS_WRITE = "W";
		
		String providerNo;
		String orgCd;
        String functionCd;
        String privilege;
		public String getPrivilege() {
			return privilege;
		}
		public void setPrivilege(String privilege) {
			this.privilege = privilege;
		}
		public String getFunctionCd() {
			return functionCd;
		}
		public void setFunctionCd(String cd) {
			functionCd = cd;
		}
		public String getOrgCd() {
			return orgCd;
		}
		public void setOrgCd(String cd) {
			orgCd = cd;
		}
		public String getProviderNo() {
			return providerNo;
		}
		public void setProviderNo(String providerNo) {
			this.providerNo = providerNo;
		}
  	    public int hashCode()
	    {
		    return (functionCd+orgCd).hashCode();
	    }
  	    public boolean equals(Object uv)
  	    {
  	    	UserAccessValue uv1 = (UserAccessValue) uv;
  	    	return this.functionCd.equals(uv1.functionCd) && this.orgCd.equals(uv1.orgCd);
  	    }
}
