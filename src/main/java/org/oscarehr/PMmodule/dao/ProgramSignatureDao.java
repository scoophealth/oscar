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

package org.oscarehr.PMmodule.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.ProgramSignature;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramSignatureDao extends HibernateDaoSupport {

    private static final Logger log=MiscUtils.getLogger();

    //get the creator of the program
    public ProgramSignature getProgramFirstSignature(Integer programId) {
        ProgramSignature programSignature = null;
        if (programId == null || programId.intValue() <= 0) {
            return null;
        }
        List ps = getHibernateTemplate().find("FROM ProgramSignature ps where ps.programId = ? ORDER BY ps.updateDate ASC", programId);

        if (!ps.isEmpty()) {
            programSignature = (ProgramSignature)ps.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug("getProgramFirstSignature: " + ((programSignature != null)?String.valueOf(programSignature.getId()):"null"));
        }

        return programSignature;
    }

    public List<ProgramSignature> getProgramSignatures(Integer programId) {
        if (programId == null || programId.intValue() <= 0) {
            return null;
        }

        List rs = getHibernateTemplate().find("FROM ProgramSignature ps WHERE ps.programId = ? ORDER BY ps.updateDate ASC", programId);

        if (log.isDebugEnabled()) {
            log.debug("getProgramSignatures: # of programs: " + rs.size());
        }
        return rs;

    }

    public void saveProgramSignature(ProgramSignature programSignature) {
        if (programSignature == null) {
            throw new IllegalArgumentException();
        }
        programSignature.setUpdateDate(new Date());
        getHibernateTemplate().saveOrUpdate(programSignature);
        getHibernateTemplate().flush();

        if (log.isDebugEnabled()) {
            log.debug("saveAdmission: id= " + programSignature.getId());
        }
    }
}
