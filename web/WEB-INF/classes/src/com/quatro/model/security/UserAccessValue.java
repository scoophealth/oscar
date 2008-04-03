package com.quatro.model.security;

public class UserAccessValue {
		public static final String ACCESS_NONE = "N";
		public static final String ACCESS_READ = "R";
		public static final String ACCESS_WRITE = "W";
		
		String id;
		String orgCd;
        String functionCd;
        String accessTypeCd;
		public String getAccessTypeCd() {
			return accessTypeCd;
		}
		public void setAccessTypeCd(String typeCd) {
			accessTypeCd = typeCd;
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
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
}
