package com.quatro.model.security;
import java.io.*;

public class UserAccessValue implements Serializable {
		
		String providerNo;
		String orgCd;
		String orgCdcsv;
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
		public String getOrgCdcsv() {
			return orgCdcsv;
		}
		public void setOrgCdcsv(String cdcsv) {
			orgCdcsv = cdcsv;
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
