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


package org.oscarehr.PMmodule.model;

import java.util.Date;

/**
 *
 * @author jay
 */
public class JointAdmission {
    public int HEAD = 0;
    public int SPOUSE = 1;
    public int DEPENDENT = 2;
    
    /** Creates a new instance of JointAdmission */
    public JointAdmission() {
    }

    private Long id = null;
    private Long clientId = null;
    private Long typeId = null;
    private String providerNo = null;
    private String archivingProviderNo = null;
    private Date admissionDate = null;
    private Long headClientId = null;
    
    private boolean archived = false;

    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getProviderNo() {
        return providerNo;
    }

    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }
    
    public String getArchivingProviderNo() {
        return archivingProviderNo;
    }

    public void setArchivingProviderNo(String archivingProviderNo) {
        this.archivingProviderNo = archivingProviderNo;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Long getHeadClientId() {
        return headClientId;
    }

    public void setHeadClientId(Long headClientId) {
        this.headClientId = headClientId;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    
    public String toString(){
        return "id: "+id+ " clientId: " +clientId+" typeId: "+typeId+ " providerNo: "+providerNo+" admissionDate: "+admissionDate+" headClientId: "+headClientId+ " archived: "+archived;
    }
    
    
}
