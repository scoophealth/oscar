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
 */

package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author rjonasz
 */
public class UserDSMessagePrefs implements Serializable {

    public final static String MYDRUGREF = "mydrugref";

    private long id;
    private String resourceType;
    private String resourceId;
    private Date resourceUpdatedDate;
    private String providerNo;
    private Date recordCreated;
    private Boolean archived;



    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }


    /** Creates a new instance of UserProperty */
    public UserDSMessagePrefs() {
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Date getResourceUpdatedDate() {
        return resourceUpdatedDate;
    }

    public void setResourceUpdatedDate(Date resourceUpdatedDate) {
        this.resourceUpdatedDate = resourceUpdatedDate;
    }

    public String getProviderNo() {
        return providerNo;
    }

    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }

    public Date getRecordCreated() {
        return recordCreated;
    }

    public void setRecordCreated(Date recordCreated) {
        this.recordCreated = recordCreated;
    }

    public Boolean isArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

}
