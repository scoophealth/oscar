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

package org.oscarehr.sharingcenter;

import java.util.List;
import org.marc.shic.core.configuration.consent.IIdentityProvider;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.util.SpringUtils;

public class OscarIdentityProvider implements IIdentityProvider
{
    private SecUserRoleDao secUserRoleDao = (SecUserRoleDao) SpringUtils.getBean("secUserRoleDao");
        
    private String providerId;

    public String getProviderId()
    {
        return providerId;
    }

    public OscarIdentityProvider()
    {
    }

    public OscarIdentityProvider(String providerId)
    {
        this();
        this.providerId = providerId;
    }

    @Override
    public String getUserId()
    {
        return providerId;
    }

    @Override
    public boolean isUserInRole(String roleName)
    {
        if (providerId == null || providerId.length() == 0) {
            throw new IllegalArgumentException("Invalid provider id.");
        }
        
        if (roleName == null || roleName.length() == 0) {
            throw new IllegalArgumentException("Invalid role name specified.");
        }
        
        boolean isInRole = false;
        
        List<SecUserRole> roles = secUserRoleDao.getUserRoles(providerId);
        
        // Loop through all roles found for the provider.
        for (SecUserRole userRole : roles) {
            
            // Check if it is the role we are looking for.
            if (userRole.getRoleName().equals(roleName)) {
                isInRole = true;
                break;
            }
        }
        
        return isInRole;
    }
}
