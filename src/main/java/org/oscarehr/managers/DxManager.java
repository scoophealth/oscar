/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.managers;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DxresearchDAO;
//import org.oscarehr.common.exception.AccessDeniedException;
import org.oscarehr.common.model.Dxresearch;
import org.springframework.beans.factory.annotation.Qualifier;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Manager class to access data regarding diseases.
 */
@Service
public class DxManager {

    private static Logger logger = MiscUtils.getLogger();

    @Autowired
    protected SecurityInfoManager securityInfoManager;

    @Autowired
    @Qualifier("DxresearchDAO")
    protected DxresearchDAO dxresearchDao;

    /**
     * Gets diseases for the given demographic.
     *
     * @param info          details regarding the current user
     * @param demographicNo the demographic to get disease registry entries for.
     *
     * @return a list of Dxresearch.
     */
    public List<Dxresearch> getDiseases(LoggedInInfo info, int demographicNo) {
        // Access control check.
        readCheck(info, demographicNo);

        // find diseases from the DAO for the given demographicNo.
        return dxresearchDao.getDxResearchItemsByPatient(demographicNo);
    }

    /**
     * Access control check:
     * determines current user is allow to READ disease registry information
     * for the current demographic.
     *
     * @param info          information regarding the user making the request.
     * @param demographicNo the identifier for the patient being accessed.
     *
     * @throws AccessDeniedException if access is denied.
     */
    protected void readCheck(LoggedInInfo info, int demographicNo) {
        if (!securityInfoManager.hasPrivilege(info, "_dxresearch", "r", null)) {
            throw new RuntimeException("Read Access Denied [_dxresearch] for demoNo="+demographicNo);
            //throw new AccessDeniedException("_dxresearch", "r", demographicNo);
        }
    }

    /**
     * Access control check:
     * Determines if the current user is allowed to WRITE write information
     * to the chart of the current demographic.
     *
     * @param info          information regarding the user making the request.
     * @param demographicNo the identifier for the patient being accessed.
     *
     * @throws AccessDeniedException if access is denied.
     */
    protected void writeCheck(LoggedInInfo info, int demographicNo) {
        if (!securityInfoManager.hasPrivilege(info, "_dxresearch", "w", demographicNo)) {
            throw new RuntimeException("Write Access Denied [_dxresearch] for demoNo="+demographicNo);
            //throw new AccessDeniedException("_dxresearch", "w", demographicNo);
        }
    }

}
