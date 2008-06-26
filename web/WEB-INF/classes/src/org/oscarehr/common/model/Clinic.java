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
 * Jason Gallagher
 *
 * UserProperty.java
 *
 * Created on December 19, 2007, 4:30 PM
 *
 *
 * 
clinic_no            | int(10)     | NO   | PRI | NULL    | auto_increment | 
| clinic_name          | varchar(50) | YES  |     | NULL    |                | 
| clinic_address       | varchar(60) | YES  |     |         |                | 
| clinic_city          | varchar(40) | YES  |     |         |                | 
| clinic_postal        | varchar(15) | YES  |     |         |                | 
| clinic_phone         | varchar(50) | YES  |     | NULL    |                | 
| clinic_fax           | varchar(20) | YES  |     |         |                | 
| clinic_location_code | varchar(10) | YES  |     | NULL    |                | 
| status               | char(1)     | YES  |     | NULL    |                | 
| clinic_province      | varchar(40) | YES  |     | NULL    |                | 
| clinic_delim_phone   | text        | YES  |     | NULL    |                | 
| clinic_delim_fax     | text        | YES  |     | NULL    |                | 

 *
 */
package org.oscarehr.common.model;

import java.io.Serializable;

/**
 *
 * @author Jay Gallagher
 */
public class Clinic implements Serializable {

    private long id;
    private String clinicName;
    private String clinicAddress;
    private String clinicCity;
    private String clinicPostal;
    private String clinicPhone;
    private String clinicFax;
    private String clinicLocationCode;
    private String status;
    private String clinicProvince;
    private String clinicDelimPhone;
    private String clinicDelimFax;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /** Creates a new instance of UserProperty */
    public Clinic() {
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getClinicCity() {
        return clinicCity;
    }

    public void setClinicCity(String clinicCity) {
        this.clinicCity = clinicCity;
    }

    public String getClinicPostal() {
        return clinicPostal;
    }

    public void setClinicPostal(String clinicPostal) {
        this.clinicPostal = clinicPostal;
    }

    public String getClinicPhone() {
        return clinicPhone;
    }

    public void setClinicPhone(String clinicPhone) {
        this.clinicPhone = clinicPhone;
    }

    public String getClinicFax() {
        return clinicFax;
    }

    public void setClinicFax(String clinicFax) {
        this.clinicFax = clinicFax;
    }

    public String getClinicLocationCode() {
        return clinicLocationCode;
    }

    public void setClinicLocationCode(String clinicLocationCode) {
        this.clinicLocationCode = clinicLocationCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClinicProvince() {
        return clinicProvince;
    }

    public void setClinicProvince(String clinicProvince) {
        this.clinicProvince = clinicProvince;
    }

    public String getClinicDelimPhone() {
        return clinicDelimPhone;
    }

    public void setClinicDelimPhone(String clinicDelimPhone) {
        this.clinicDelimPhone = clinicDelimPhone;
    }

    public String getClinicDelimFax() {
        return clinicDelimFax;
    }

    public void setClinicDelimFax(String clinicDelimFax) {
        this.clinicDelimFax = clinicDelimFax;
    }
    
    public String toString(){
       return "clinicName " +clinicName +
    " clinicAddress  " +clinicAddress+
    " clinicCity " +clinicCity+
    " clinicPostal " +clinicPostal+
    " clinicPhone " +clinicPhone+
    "  clinicFax " +clinicFax+
    " clinicLocationCode " +clinicLocationCode+
    " status " +status+
    " clinicProvince " +clinicProvince+
    " clinicDelimPhone " +clinicDelimPhone+
    " clinicDelimFax " +clinicDelimFax;
        
    }
    
}
