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
package org.oscarehr.sharingcenter.dao;


import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.sharingcenter.model.ClinicInfoDataObject;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ClinicInfoDao extends AbstractDao<ClinicInfoDataObject> {

    public ClinicInfoDao() {
        super(ClinicInfoDataObject.class);
    }

    /**
     * Gets the first clinic
     * 
     * @return ClinicInfoDataObject or null if table is empty
     */
    public ClinicInfoDataObject getClinic() {
        Query query = entityManager.createQuery("FROM ClinicInfoDataObject c");

        query.setMaxResults(1);
        ClinicInfoDataObject retVal = getSingleResultOrNull(query);
        if (retVal != null) {
            return retVal;
        }

        MiscUtils.getLogger().warn("Please update the 'Administration/Clinic Info' page");

        ClinicInfoDataObject failsafe = new ClinicInfoDataObject();
        failsafe.setFacilityName("facility");
        failsafe.setLocalAppName("app");
        failsafe.setName("name");
        failsafe.setNamespaceId("namespace");
        failsafe.setOid("organization oid");
        failsafe.setUniversalId("universal id");
        failsafe.setSourceId("source id");

        return failsafe;
    }

}
