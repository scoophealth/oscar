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
package com.quatro.model.security;
import java.io.Serializable;

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
