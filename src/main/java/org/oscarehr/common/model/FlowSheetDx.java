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

/**
 *

 create table flowsheet_drug (
      id int(10) NOT NULL auto_increment primary key,
      flowsheet varchar(40),
      atcCode varchar(40),
      provider_no varchar(6),
      demographic_no int(10),
      create_date datetime,
      archived char(1) default '0',
      archived_date datetime


    ) ;

 *
 *
 * @author jaygallagher
 */


@Entity
@Table(name="flowsheet_dx")
public class FlowSheetDx  extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String flowsheet = null;

    @Column(name="dx_code")
    private String dxCode = null;

    @Column(name="dx_code_type")
    private String dxCodeType = null;

    @Column(name="provider_no")
    private String providerNo = null;

    @Column(name="demographic_no")
    private int demographicNo;

    @Column(name="create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = null;

    private boolean archived = false;

    @Column(name="archived_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date archivedDate = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlowsheet() {
        return flowsheet;
    }

    public void setFlowsheet(String flowsheet) {
        this.flowsheet = flowsheet;
    }

    public String getProviderNo() {
        return providerNo;
    }

    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }

    public int getDemographicNo() {
        return demographicNo;
    }

    public void setDemographicNo(int demographicNo) {
        this.demographicNo = demographicNo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public Date getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(Date archivedDate) {
        this.archivedDate = archivedDate;
    }

    public String getDxCode() {
        return dxCode;
    }

    public void setDxCode(String dxCode) {
        this.dxCode = dxCode;
    }

    public String getDxCodeType() {
        return dxCodeType;
    }

    public void setDxCodeType(String dxCodeType) {
        this.dxCodeType = dxCodeType;
    }

}
