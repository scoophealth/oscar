/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.common.dao;

import java.util.List;
import org.oscarehr.common.model.FlowSheetCustomization;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author jaygallagher
 */
public class FlowSheetCustomizerDAO extends HibernateDaoSupport {
    
    public void save(FlowSheetCustomization fsc) {
        this.getHibernateTemplate().saveOrUpdate(fsc);
    }
    
    public FlowSheetCustomization getFlowSheetCustomization(String id){
        List<FlowSheetCustomization> list = this.getHibernateTemplate().find("from FlowSheetCustomization p where  p.id = ?", new Object[] {Long.parseLong(id)});
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public List<FlowSheetCustomization> getFlowSheetCustomizations(String flowsheet,String provider,String demographic){
        List<FlowSheetCustomization> list = this.getHibernateTemplate().find("from FlowSheetCustomization p where p.flowsheet = ? and p.archived = 0 and ( ( p.providerNo = ?  and p.demographicNo is null) or (p.providerNo =? and p.demographicNo = ?  ) ) ", new Object[] {flowsheet,provider,provider,demographic});
        return list;
    }

    
    
    
}
