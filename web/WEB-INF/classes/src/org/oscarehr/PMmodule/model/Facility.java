package org.oscarehr.PMmodule.model;

import java.io.Serializable;

/**
 * Represents a facility, i.e. bricks and mortar.  Each facility has a number of rooms.
 */
public class Facility implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private String contactName;
	private String contactEmail;
	private String contactPhone;
    private boolean hic;   
    private boolean disabled;

    public Facility() {
    }

    public Facility(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public boolean isHic() {
        return hic;
    }

    public void setHic(boolean hic) {
        this.hic = hic;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Facility facility = (Facility) o;

        if (id != null ? !id.equals(facility.id) : facility.id != null) return false;

        return true;
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }
}
