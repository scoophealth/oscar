package com.quatro.model.security;
import java.io.*;

public class UserAccessValue implements Serializable {
		public static final String ACCESS_NONE = "o";
		public static final String ACCESS_READ = "r";
		public static final String ACCESS_UPDATE = "u";
		public static final String ACCESS_WRITE = "w";
		public static final String ACCESS_ALL = "x";
		
		String providerNo;
		String orgCd;
        String functionCd;
        String privilege;
        boolean orgApplicable;
        
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
		public boolean isOrgApplicable() {
			return orgApplicable;
		}
		public void setOrgApplicable(boolean orgApplicable) {
			this.orgApplicable = orgApplicable;
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
