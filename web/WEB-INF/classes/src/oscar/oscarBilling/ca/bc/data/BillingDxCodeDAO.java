/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * BillingmasterDAO.java
 *
 * Created on January 24, 2007, 11:57 PM
 *
 */

package oscar.oscarBilling.ca.bc.data;

import java.util.List;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import oscar.entities.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author jaygallagher
 */
@Repository
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class BillingDxCodeDAO{
    @PersistenceContext
    protected EntityManager entityManager = null;

    public void save(BillingDxCode dxCode){
            entityManager.persist(dxCode);
    }

    public List<BillingDxCode> getByDxCode(String dxCode){
          Query query = entityManager.createQuery("select bdx from BillingDxCode bdx where diagnosticCode = (:dxCode)");
          query.setParameter("dxCode",dxCode);
          return query.getResultList();
    }

    
}
