package oscar.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * ManageTeleplanAction.java
 *
 * Created on June 22, 2007, 12:10 AM
 *
 */
/**
 *  +-------------------+------------+------+-----+---------+----------------+
    | Field             | Type       | Null | Key | Default | Extra          |
    +-------------------+------------+------+-----+---------+----------------+
    | diagnosticcode_no | int(5)     | NO   | PRI | NULL    | auto_increment |
    | diagnostic_code   | varchar(5) | NO   | MUL |         |                |
    | description       | text       | YES  |     | NULL    |                |
    | status            | char(1)    | YES  |     | NULL    |                |
    | region            | varchar(5) | YES  |     | NULL    |                |
    +-------------------+------------+------+-----+---------+----------------+

 * @author jaygallagher
 */
@Entity
@Table(name = "diagnosticcode")
public class BillingDxCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "diagnosticcode_no")
    private int diagnosticcodeNo;
    @Column(name = "diagnostic_code")
    private String diagnosticCode = null;
    private String description = null;
    private String status = null;
    private String region = null;

      
    /**
     * @return the diagnosticcodeNo
     */
    public int getDiagnosticcodeNo() {
        return diagnosticcodeNo;
    }

    /**
     * @param diagnosticcodeNo the diagnosticcodeNo to set
     */
    public void setDiagnosticcodeNo(int diagnosticcodeNo) {
        this.diagnosticcodeNo = diagnosticcodeNo;
    }

    /**
     * @return the diagnosticCode
     */
    public String getDiagnosticCode() {
        return diagnosticCode;
    }

    /**
     * @param diagnosticCode the diagnosticCode to set
     */
    public void setDiagnosticCode(String diagnosticCode) {
        this.diagnosticCode = diagnosticCode;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

}
