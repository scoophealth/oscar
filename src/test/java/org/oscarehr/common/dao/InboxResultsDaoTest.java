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

import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class InboxResultsDaoTest extends DaoTestFixtures {

	protected InboxResultsDao dao = SpringUtils.getBean(InboxResultsDao.class);
	
	@Test
	public void testQuerySyntaxInPopulateDocumentResultsData() throws Exception {
		SchemaUtils.restoreAllTables();
		
		String[][] params = new String[][] {
		                     {null, null, null, null, null, null},
		                     {"0", "0", null, null, null, null},
		                     {"0", null, null, null, null, null},
		                     {"999998", "1", null, null, "A", null},
		                     {null, null, null, null, "N", null},
		                     {null, null, "test", "test", "test", null}
		                    };
		
		for (String[] param : params) {
			String providerNo = param[0]; 
			String demographicNo = param[1];
			String patientFirstName = param[2];
			String patientLastName = param[3];
			String patientHealthNumber = param[4]; 
			String status = param[5]; 
			
			Boolean isAbnormal = null;
			boolean isPaged = true; 
			Integer page = 1;
			Integer pageSize = 10; 
			boolean mixLabsAndDocs = true; 
			
			dao.populateDocumentResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, isPaged, page, pageSize, mixLabsAndDocs, isAbnormal);
			
			isAbnormal = null;
			isPaged = false; 
			mixLabsAndDocs = true; 
			
			dao.populateDocumentResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, isPaged, page, pageSize, mixLabsAndDocs, isAbnormal);
			
			isAbnormal = null;
			isPaged = true; 
			page = 1;
			pageSize = 10; 
			mixLabsAndDocs = false; 
			dao.populateDocumentResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, isPaged, page, pageSize, mixLabsAndDocs, isAbnormal);
			
			isAbnormal = true;
			isPaged = true; 
			page = 1;
			pageSize = 10; 
			mixLabsAndDocs = false; 
			dao.populateDocumentResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, isPaged, page, pageSize, mixLabsAndDocs, isAbnormal);

			isAbnormal = false;
			isPaged = true; 
			page = 1;
			pageSize = 10; 
			mixLabsAndDocs = false; 
			dao.populateDocumentResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, isPaged, page, pageSize, mixLabsAndDocs, isAbnormal);
		}
		
	}

}
