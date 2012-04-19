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


package oscar.oscarEncounter.oscarMeasurements.model;

public class Measurementmap {
    private Integer id;
    private String loincCode;
    private String identCode;
    private String name;
    private String labType;


    public Integer getId() {
	return this.id;
    }
    public void setId(Integer id) {
	this.id = id;
    }
    public String getLoincCode() {
        return this.loincCode;
    }
    public void setLoincCode(String loincCode) {
        this.loincCode = loincCode;
    }
    public String getIdentCode() {
        return this.identCode;
    }
    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLabType() {
        return this.labType;
    }
    public void setLabType(String labType) {
        this.labType = labType;
    }
}
