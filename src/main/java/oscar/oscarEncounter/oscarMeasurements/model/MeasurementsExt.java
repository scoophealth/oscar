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

public class MeasurementsExt {
    private Integer id;
    private Integer measurementId;
    private String  keyVal;
    private String  val;
    
    /** Creates a new instance of Measurements */
    public MeasurementsExt(Integer mId) {
        this.measurementId = mId;
    }

    public MeasurementsExt() {}
    
    
    
    public Integer getId() {
	return this.id;
    }
    public void setId(Integer id) {
	this.id = id;
    }
    
    public Integer getMeasurementId() {
	return this.measurementId;
    }
    public void setMeasurementId(Integer mId) {
	this.measurementId = mId;
    }

    public String getKeyVal() {
	return this.keyVal;
    }
    public void setKeyVal(String kVal) {
	this.keyVal = kVal;
    }
    
    public String getVal() {
	return this.val;
    }
    public void setVal(String val) {
	this.val = val;
    }
}
