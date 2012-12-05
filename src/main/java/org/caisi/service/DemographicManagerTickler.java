/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.caisi.service;

import java.util.List;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.springframework.transaction.annotation.Transactional;

/**
 */
@Transactional
public class DemographicManagerTickler {

    private DemographicDao demographicDao = null;

    public void setDemographicDao(DemographicDao demographicDao) {
        this.demographicDao = demographicDao;
    }

    public Demographic getDemographic(String demographic_no) {
        return demographicDao.getDemographic(demographic_no);
    }

    public List getDemographics() {
        return demographicDao.getDemographics();
    }

    public List getProgramIdByDemoNo(String demoNo) {
        return demographicDao.getProgramIdByDemoNo(Integer.parseInt(demoNo));
    }

    public List getDemoProgram(Integer demoNo) {
        return demographicDao.getDemoProgram(demoNo);
    }

}
