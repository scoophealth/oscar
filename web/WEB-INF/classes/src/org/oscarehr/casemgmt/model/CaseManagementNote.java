/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.casemgmt.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.caisi.model.BaseObject;
import org.oscarehr.PMmodule.model.Provider;


public class CaseManagementNote extends BaseObject {
    private Long id;
    private Date update_date;
    private Date create_date;
    private Date observation_date;
    private String demographic_no;
    private String note;
    private boolean signed = false;
    private boolean includeissue = true;
    private String provider_no;

    private String signing_provider_no;
    private String encounter_type = "";
    private String billing_code = "";

    private String program_no;
    private String reporter_caisi_role;
    private String reporter_program_team;
    private String history;
    private Provider provider;
    private Set issues = new HashSet();
    private List editors = new ArrayList();
    private String roleName;
    private String programName;
    private String uuid;
    private String revision;

    private String password;
    private boolean locked;

    private int hashCode = Integer.MIN_VALUE;

    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof CaseManagementNote)) return false;
        else {
            CaseManagementNote mObj = (CaseManagementNote) obj;
            if (null == this.getId() || null == mObj.getId()) return false;
            else return (this.getId().equals(mObj.getId()));
        }
    }


    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public CaseManagementNote() {
        update_date = new Date();
    }

    public String getBilling_code() {
        return billing_code;
    }

    public void setBilling_code(String billing_code) {
        this.billing_code = billing_code;
    }

    public String getDemographic_no() {
        return demographic_no;
    }

    public void setDemographic_no(String demographic_no) {
        this.demographic_no = demographic_no;
    }

    public String getEncounter_type() {
        return encounter_type;
    }

    public void setEncounter_type(String encounter_type) {
        this.encounter_type = encounter_type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public String getSigning_provider_no() {
        return signing_provider_no;
    }

    public void setSigning_provider_no(String signing_provider_no) {
        this.signing_provider_no = signing_provider_no;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getObservation_date() {
        return this.observation_date;
    }

    public void setObservation_date(Date observation_date) {
        this.observation_date = observation_date;
    }

    public String getProviderNo() {
        return provider_no;
    }

    public void setProviderNo(String provider_no) {
        this.provider_no = provider_no;
    }

    //nys
    public String getProgram_no() {
        return program_no;
    }

    public void setProgram_no(String program_no) {
        this.program_no = program_no;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Set getIssues() {
        return issues;
    }

    public void setIssues(Set issues) {
        this.issues = issues;
    }

    public List getEditors() {
        return this.editors;
    }
    
    public void setEditors( List editors ) {
        this.editors = editors;
    }

    public boolean isIncludeissue() {
        return includeissue;
    }


    public void setIncludeissue(boolean includeissue) {
        this.includeissue = includeissue;
    }
    
    public String getReporter_caisi_role() {
        return reporter_caisi_role;
    }


    public void setReporter_caisi_role(String reporter_caisi_role) {
        this.reporter_caisi_role = reporter_caisi_role;
    }


    public String getReporter_program_team() {
        return reporter_program_team;
    }


    public void setReporter_program_team(String reporter_program_team) {
        this.reporter_program_team = reporter_program_team;
    }


    public String getHistory() {
        return history;
    }


    public void setHistory(String history) {
        this.history = history;
    }


    public String getProgramName() {
        return programName;
    }


    public String getRoleName() {
        return roleName;
    }


    public void setProgramName(String programName) {
        this.programName = programName;
    }


    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setRevision(String rev) {
        this.revision = rev;
    }

    public String getRevision() {
        return this.revision;
    }

    public String getProviderName() {
        if (getProvider() == null) {
            return "DELETED";
        }
        return getProvider().getFormattedName();
    }

    public static Comparator getProviderComparator() {
        return new Comparator() {
            public int compare(Object o1, Object o2) {
                CaseManagementNote note1 = (CaseManagementNote) o1;
                CaseManagementNote note2 = (CaseManagementNote) o2;

                if (note1 == null || note2 == null) {
                    return 0;
                }

                return note1.getProviderName().compareTo(note2.getProviderName());
            }
        };

    }

    public static Comparator getProgramComparator() {
        return new Comparator() {
            public int compare(Object o1, Object o2) {
                CaseManagementNote note1 = (CaseManagementNote) o1;
                CaseManagementNote note2 = (CaseManagementNote) o2;
                if (note1 == null || note1.getProgramName() == null ||
                        note2 == null || note2.getProgramName() == null) {
                    return 0;
                }
                return note1.getProgramName().compareTo(note2.getProgramName());
            }
        };

    }

    public static Comparator getRoleComparator() {
        return new Comparator() {
            public int compare(Object o1, Object o2) {
                CaseManagementNote note1 = (CaseManagementNote) o1;
                CaseManagementNote note2 = (CaseManagementNote) o2;

                if (note1 == null || note2 == null) {
                    return 0;
                }

                return note1.getRoleName().compareTo(note2.getRoleName());
            }
        };

    }
    
    public static Comparator getObservationComparator() {
        return new Comparator() {
            public int compare(Object o1, Object o2) {
                CaseManagementNote note1 = (CaseManagementNote) o1;
                CaseManagementNote note2 = (CaseManagementNote) o2;

                if (note1 == null || note2 == null) {
                    return 0;
                }
                
                return note1.getObservation_date().compareTo(note2.getObservation_date());
          }  
        };
    }

    public boolean getHasHistory() {
        if (getHistory() != null) {
            if (getHistory().indexOf("----------------History Record----------------") != -1) {
                return true;
            }
        }
        return false;
    }


    public boolean isLocked() {
        return locked;
    }


    public String getPassword() {
        return password;
    }


    public void setLocked(boolean locked) {
        this.locked = locked;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        String status = "";
        if (isSigned()) {
            status = "Signed";
        } else {
            status = "Unsigned";
        }

        if (getPassword() != null && getPassword().length() > 0) {
            //locked note - can be temporarily unlocked
            if (locked) {
                status += "/Locked";
            } else {
                status += "/Unlocked";
            }
        }
        return status;
    }
}
