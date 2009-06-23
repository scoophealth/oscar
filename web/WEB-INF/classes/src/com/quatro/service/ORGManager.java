/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.service;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.quatro.dao.ORGDao;
import com.quatro.model.LookupTableDefValue;

@Transactional
public class ORGManager {
    private ORGDao orgDao=null;
	
    public ORGDao getOrgDao() {
		return orgDao;
	}

	public void setOrgDao(ORGDao orgDao) {
		this.orgDao = orgDao;
	}
	
    public LookupTableDefValue GetLookupTableDef(String tableId){
        return orgDao.GetLookupTableDef(tableId);
    }
	
    public List LoadCodeList(String tableId, boolean activeOnly, String code, String codeDesc) {
        return orgDao.LoadCodeList(tableId, activeOnly, code, codeDesc);
	}
	    
}
