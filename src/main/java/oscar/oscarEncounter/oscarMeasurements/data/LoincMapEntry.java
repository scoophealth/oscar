/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarEncounter.oscarMeasurements.data;

/**
 *
 * @author apavel
 */
public class LoincMapEntry {
    private String id;
    private String loincCode;
    private String identCode;
    private String name;
    private String labType;

    public LoincMapEntry() {
    }

    public LoincMapEntry(String id, String loincCode, String identCode, String name, String labType) {
        this.setId(id);
        this.setLoincCode(loincCode);
        this.setIdentCode(identCode);
        this.setName(name);
        this.setLabType(labType);
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the loincCode
     */
    public String getLoincCode() {
        return loincCode;
    }

    /**
     * @param loincCode the loincCode to set
     */
    public void setLoincCode(String loincCode) {
        this.loincCode = loincCode;
    }

    /**
     * @return the identCode
     */
    public String getIdentCode() {
        return identCode;
    }

    /**
     * @param identCode the identCode to set
     */
    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the labType
     */
    public String getLabType() {
        return labType;
    }

    /**
     * @param labType the labType to set
     */
    public void setLabType(String labType) {
        this.labType = labType;
    }
}
