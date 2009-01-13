package com.quatro.model.security;

public class Secobjprivilege implements java.io.Serializable {

	// Fields

	// private SecobjprivilegeId id;
	private String roleusergroup;

	private Integer priority;
	private String providerNo;

	private String privilege_code;
	private String privilege_desc;

	private String objectname_code;
	private String objectname_desc;


	// Constructors

	/** default constructor */
	public Secobjprivilege() {
	}

	/** full constructor */
	public Secobjprivilege(String roleusergroup, String objectname,
			String privilege, Integer priority, String providerNo) {
		this.roleusergroup = roleusergroup;
		this.objectname_code = objectname;
		this.privilege_code = privilege;
		this.priority = priority;
		this.providerNo = providerNo;
	}

	public String getPrivilege_desc() {
		return privilege_desc;
	}
	public void setPrivilege_desc(String privilegeDesc) {
		this.privilege_desc = privilegeDesc;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getProviderNo() {
		return this.providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getObjectname_code() {
		return objectname_code;
	}

	public void setObjectname_code(String objectname_code) {
		this.objectname_code = objectname_code;
	}

	public String getObjectname_desc() {
		return objectname_desc;
	}

	public void setObjectname_desc(String objectname_desc) {
		this.objectname_desc = objectname_desc;
	}

	public String getPrivilege_code() {
		return privilege_code;
	}

	public void setPrivilege_code(String privilege_code) {
		this.privilege_code = privilege_code;
	}

	public String getRoleusergroup() {
		return roleusergroup;
	}

	public void setRoleusergroup(String roleusergroup) {
		this.roleusergroup = roleusergroup;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Secobjprivilege))
			return false;
		Secobjprivilege castOther = (Secobjprivilege) other;

		return ((this.getRoleusergroup() == castOther.getRoleusergroup()) || (this
				.getRoleusergroup() != null
				&& castOther.getRoleusergroup() != null && this
				.getRoleusergroup().equals(castOther.getRoleusergroup())))
				&& ((this.getObjectname_code() == castOther.getObjectname_code()) || (this
						.getObjectname_code() != null
						&& castOther.getObjectname_code() != null && this
						.getObjectname_code().equals(castOther.getObjectname_code())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getRoleusergroup() == null ? 0 : this.getRoleusergroup()
						.hashCode());
		result = 37
				* result
				+ (getObjectname_code() == null ? 0 : this.getObjectname_code()
						.hashCode());
		return result;
	}
}