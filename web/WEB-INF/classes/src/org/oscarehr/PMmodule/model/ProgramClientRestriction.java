package org.oscarehr.PMmodule.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Service restriction
 */
public class ProgramClientRestriction implements Serializable {

    private Integer id;
    private int programId;
    private int demographicNo;
    private String comments;
    private Date startDate;
    private Date endDate;
    private boolean enabled;

    private Program program;
    private Demographic client;

    public ProgramClientRestriction() {
    }

    public ProgramClientRestriction(int id, int programId, int demographicNo, String comments, Date startDate, Date endDate, boolean enabled) {
        this.id = id;
        this.programId = programId;
        this.demographicNo = demographicNo;
        this.comments = comments;
        this.startDate = startDate;
        this.endDate = endDate;
        this.enabled = enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public int getDemographicNo() {
        return demographicNo;
    }

    public void setDemographicNo(int demographicNo) {
        this.demographicNo = demographicNo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Demographic getClient() {
        return client;
    }

    public void setClient(Demographic client) {
        this.client = client;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProgramClientRestriction that = (ProgramClientRestriction) o;

        if (id != that.id) return false;

        return true;
    }

    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

}
