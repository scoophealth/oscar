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


package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 *
 * Uses
 *    to annotate prescriptions, notes, scanned documents
 *
 *
 * @author jaygallagher
 */
@Entity
@Table(name="oscar_annotations")
public class OscarAnnotation extends AbstractModel<Integer>{

	@Transient
   final public int DOCUMENT = 1;
	@Transient
   final public int TICKLER = 2;
	@Transient
   final public int PRESCRIPTION = 3;
	@Transient
   final public int ANNOTATION = 4;
	@Transient
   final public int NOTE = 5;
	@Transient
   final public int LAB = 6;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer    id;
	@Column(name="table_id")
    private long    tableId;
	@Column(name="table_name")
    private String  tableName; //hmm should this be a long with CONST VALUES HARDCODED?
	@Column(name="provider_no")
    private String  providerNo;
	@Column(name="demographic_no")
    private String  demographicNo;
	@Column(name="create_date")
	@Temporal(TemporalType.TIMESTAMP)
    private Date    createDate;
	@Column(name="observation_date")
	@Temporal(TemporalType.TIMESTAMP)
    private Date    observationDate;
    private boolean deleted;
    private String  note;
    private String  uuid = null;


    public boolean isUuidSet(){
        boolean uidSet = false;
        if (uuid != null){
            uidSet = true;
        }
        return uidSet;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getProviderNo() {
        return providerNo;
    }

    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }

    public String getDemographicNo() {
        return demographicNo;
    }

    public void setDemographicNo(String demographicNo) {
        this.demographicNo = demographicNo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(Date observationDate) {
        this.observationDate = observationDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }



}
