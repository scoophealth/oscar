/**
 * Copyright (C) 2011-2012  PeaceWorks Technology Solutions
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.oscarRx.erx.model;

import oscar.OscarProperties;

/**
 * Provides an abstract way to reference the preferences specific to the OSCAR
 * ERx component.
 */
public class ERxApplicationPreferences {
    /**
     * Create an instance of an ERxApplicationPreferences object.
     */
    public static ERxApplicationPreferences getInstance() {
        // The object we will return
        ERxApplicationPreferences answer = new ERxApplicationPreferences();
        // The object we will use to get the data
        OscarProperties properties = OscarProperties.getInstance();

        // Set data in the new object
        answer.setERxEnabled(Boolean.parseBoolean(properties
                .getProperty("util.erx.enabled")));
        answer.setSoftwareName(properties.getProperty("util.erx.software"));
        answer.setVendor(properties.getProperty("util.erx.vendor"));
        answer.setVersion(properties.getProperty("util.erx.version"));

        return answer;
    }

    /**
     * Whether or not the OSCAR ERx component is enabled.
     */
    private boolean isERxEnabled;
    /**
     * The name of the software.
     */
    private String softwareName;
    /**
     * The name of the organization that produced the software.
     */
    private String vendor;

    /**
     * The version of the software.
     */
    private String version;

    /**
     * Create an instance of an ERxApplicationPreferences object.
     */
    protected ERxApplicationPreferences() {
        super();
    }

    /**
     * Create an instance of an ERxApplicationPreferences object.
     * 
     * @param isERxEnabled
     *            Whether or not the OSCAR ERx component is enabled. NOTE THIS
     *            DOES NOT DEACTIVATE THE COMPONENT, it just sets the value in
     *            this instance of this object.
     * @param softwareName
     *            The name of the software.
     * @param softwareVendor
     *            The name of the organization that produced the software.
     * @param softwareVersion
     *            The version of the software.
     */
    public ERxApplicationPreferences(boolean isERxEnabled, String softwareName,
            String softwareVendor, String softwareVersion) {
        super();
        this.isERxEnabled = isERxEnabled;
        this.softwareName = softwareName;
        this.vendor = softwareVendor;
        this.version = softwareVersion;
    }

    /**
     * Returns the name of the software.
     * 
     * @return The current value of softwareName.
     */
    public String getSoftwareName() {
        return this.softwareName;
    }

    /**
     * Returns the name of the organization that produced the software.
     * 
     * @return The current value of vendor.
     */
    public String getVendor() {
        return this.vendor;
    }

    /**
     * Returns the version of the software.
     * 
     * @return The current value of version.
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Returns whether or not the OSCAR ERx component is enabled.
     * 
     * @return The current value of isERxEnabled.
     */
    public boolean isERxEnabled() {
        return this.isERxEnabled;
    }

    /**
     * Changes whether or not the OSCAR ERx component is enabled.
     * 
     * NOTE THIS DOES NOT DEACTIVATE THE COMPONENT, it just sets the value in
     * this instance of this object.
     * 
     * @param isERxEnabled
     *            The new isERxEnabled.
     */
    public void setERxEnabled(boolean isERxEnabled) {
        this.isERxEnabled = isERxEnabled;
    }

    /**
     * Changes the value of the name of the software.
     * 
     * @param softwareName
     *            The new softwareName.
     */
    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    /**
     * Changes the value of the name of the organization that produced the
     * software.
     * 
     * @param vendor
     *            The new vendor.
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * Changes the value of the version of the software.
     * 
     * @param version
     *            The new version.
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
