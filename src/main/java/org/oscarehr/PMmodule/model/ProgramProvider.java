/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.model.Provider;

import com.quatro.model.security.Secrole;

/**
 * This is the object class that relates to the program_provider table.
 * Any customizations belong here.
 */
public class ProgramProvider implements Serializable {

    private int hashCode = Integer.MIN_VALUE;// primary key
    private Long _id;// fields
    private Long _programId;
    private String _providerNo;
    private Long _roleId;// many to one
    private Secrole _role;
    private Provider _provider;// collections
    private java.util.Set<org.oscarehr.PMmodule.model.ProgramTeam> _teams;
    private String programName;
    private Program program;
    
    private String lastUpdateUser;
    private Date lastUpdateDate;

    // constructors
	public ProgramProvider () {
		initialize();
	}

    /**
	 * Constructor for primary key
	 */
	public ProgramProvider (Long _id) {
		this.setId(_id);
		initialize();
	}

    /**
	 * @return Returns the programName.
	 */
	public String getProgramName() {
		return programName;
	}

	/**
	 * @param programName The programName to set.
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

    protected void initialize () {}

    /**
	 * Return the unique identifier of this class
* 
*  generator-class="native"
*  column="id"
*/
    public Long getId () {
        return _id;
    }

    /**
	 * Set the unique identifier of this class
     * @param _id the new ID
     */
    public void setId (Long _id) {
        this._id = _id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: program_id
     */
    public Long getProgramId () {
        return _programId;
    }

    /**
	 * Set the value related to the column: program_id
     * @param _programId the program_id value
     */
    public void setProgramId (Long _programId) {
        this._programId = _programId;
    }

    /**
	 * Return the value associated with the column: provider_no
     */
    public String getProviderNo () {
        return _providerNo;
    }

    /**
	 * Set the value related to the column: provider_no
     * @param _providerNo the provider_no value
     */
    public void setProviderNo (String _providerNo) {
        this._providerNo = _providerNo;
    }

    /**
	 * Return the value associated with the column: role_id
     */
    public Long getRoleId () {
        return _roleId;
    }

    /**
	 * Set the value related to the column: role_id
     * @param _roleId the role_id value
     */
    public void setRoleId (Long _roleId) {
        this._roleId = _roleId;
    }

    /**
     * 
*  column=role_id
     */
    public Secrole getRole () {
        return this._role;
    }

    /**
	 * Set the value related to the column: role_id
     * @param _role the role_id value
     */
    public void setRole (Secrole _role) {
        this._role = _role;
    }

    /**
     * 
*  column=provider_no
     */
    public Provider getProvider () {
        return this._provider;
    }

    /**
	 * Set the value related to the column: provider_no
     * @param _provider the provider_no value
     */
    public void setProvider (Provider _provider) {
        this._provider = _provider;
    }

    /**
	 * Return the value associated with the column: teams
     */
    public java.util.Set<org.oscarehr.PMmodule.model.ProgramTeam> getTeams () {
        return this._teams;
    }

    /**
	 * Set the value related to the column: teams
     * @param _teams the teams value
     */
    public void setTeams (java.util.Set<org.oscarehr.PMmodule.model.ProgramTeam> _teams) {
        this._teams = _teams;
    }

    public void addToTeams (Object obj) {
        if (null == this._teams) this._teams = new java.util.HashSet<org.oscarehr.PMmodule.model.ProgramTeam>();
        this._teams.add((org.oscarehr.PMmodule.model.ProgramTeam)obj);
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof ProgramProvider)) return false;
        else {
            ProgramProvider mObj = (ProgramProvider) obj;
            if (null == this.getId() || null == mObj.getId()) return false;
            else return (this.getId().equals(mObj.getId()));
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString () {
        return super.toString();
    }

	public static String getIdsAsStringList(List<ProgramProvider> results) {
		StringBuilder sb = new StringBuilder();

		for (ProgramProvider model : results) {
			sb.append(model.getId().toString());
			sb.append(',');
		}

		return (sb.toString());
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	
}
