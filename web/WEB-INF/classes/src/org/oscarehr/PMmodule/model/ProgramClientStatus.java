package org.oscarehr.PMmodule.model;

import java.io.Serializable;


public class ProgramClientStatus implements Serializable {
    public static String REF = "ProgramClientStatus";

    private int hashCode = Integer.MIN_VALUE;// primary key

    private Integer id;// fields
    private String name;
    private Integer programId;


    // constructors
    public ProgramClientStatus () {
        initialize();
    }

    /**
     * Constructor for primary key
     */
    public ProgramClientStatus (Integer id) {
        this.setId(id);
        initialize();
    }

    protected void initialize () {}

    /**
     * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="clientstatus_id"
     */
    public Integer getId () {
        return id;
    }

    /**
     * Set the unique identifier of this class
     * @param id the new ID
     */
    public void setId (Integer id) {
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
     * Return the value associated with the column: name
     */
    public String getName () {
        return name;
    }

    /**
     * Set the value related to the column: name
     * @param name the name value
     */
    public void setName (String name) {
        this.name = name;
    }

    /**
     * Return the value associated with the column: program_id
     */
    public Integer getProgramId () {
        return programId;
    }

    /**
     * Set the value related to the column: program_id
     * @param programId the program_id value
     */
    public void setProgramId (Integer programId) {
        this.programId = programId;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof ProgramClientStatus)) return false;
        else {
            ProgramClientStatus programClientStatus = (ProgramClientStatus) obj;
            if (null == this.getId() || null == programClientStatus.getId()) return false;
            else return (this.getId().equals(programClientStatus.getId()));
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
}