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
package org.oscarehr.common.dao;

import java.util.List;
import javax.persistence.Query;
import org.oscarehr.common.model.ResidentOscarMsg;
import org.springframework.stereotype.Repository;

@Repository
public class ResidentOscarMsgDao extends AbstractDao<ResidentOscarMsg>{

    public ResidentOscarMsgDao() {
        super(ResidentOscarMsg.class);
    }
    
    
    public List<ResidentOscarMsg> findBySupervisor(String supervisor) {
        Query query = entityManager.createQuery("select p from ResidentOscarMsg p where p.supervisor_no = :supervisor and p.complete = 0");
        query.setParameter("supervisor", supervisor);
        
        return query.getResultList();
    }
    
    public ResidentOscarMsg findByNoteId(Long noteId) {
        Query query = entityManager.createQuery("select p from ResidentOscarMsg p where p.note_id = :note_id");
        query.setParameter("note_id", noteId);
        
        return this.getSingleResultOrNull(query);
    }
    
}
