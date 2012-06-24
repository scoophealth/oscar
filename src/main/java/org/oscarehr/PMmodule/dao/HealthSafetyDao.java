/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */

package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.HealthSafety;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HealthSafetyDao extends HibernateDaoSupport {

    private Logger log=MiscUtils.getLogger();

    public HealthSafety getHealthSafetyByDemographic(Long demographicNo) {
        if (demographicNo == null || demographicNo.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        HealthSafety result = null;

        List list = this.getHibernateTemplate().find("from HealthSafety c where c.demographicNo=? order by c.updateDate desc", demographicNo);
        if (!list.isEmpty()) result = (HealthSafety)list.get(0);

        if (log.isDebugEnabled()) {
            log.debug("getHealthSafetyByDemographic:id=" + demographicNo + ",found=" + (result != null));
        }

        return result;
    }

    public void saveHealthSafetyByDemographic(HealthSafety healthsafety) {
        if (healthsafety == null) {
            throw new IllegalArgumentException();
        }

        this.getHibernateTemplate().save(healthsafety);

        if (log.isDebugEnabled()) {
            log.debug("saveHealthSafetyByDemographic:id=" + healthsafety.getId());
        }
    }

}
