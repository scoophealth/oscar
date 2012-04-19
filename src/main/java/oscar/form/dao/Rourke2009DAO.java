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


package oscar.form.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.oscarehr.common.dao.AbstractDao;

import oscar.form.model.FormRourke2009;

/**
 *
 * @author rjonasz
 */
@Repository
public class Rourke2009DAO extends AbstractDao<FormRourke2009> {

    public Rourke2009DAO() {
        super(FormRourke2009.class);
    }
    
    @SuppressWarnings("unchecked")
    public List<FormRourke2009> findAllDistinctForms(Integer demographicNo) {
    	String sql = "select frm from FormRourke2009 frm where frm.demographicNo = :demo and frm.id = (select max(frm2.id) from FormRourke2009 frm2 where frm2.formCreated = frm.formCreated and frm2.demographicNo = frm.demographicNo)";
    	Query query = entityManager.createQuery(sql);
    	query = query.setParameter("demo", demographicNo);
    	return query.getResultList();
    }

}
