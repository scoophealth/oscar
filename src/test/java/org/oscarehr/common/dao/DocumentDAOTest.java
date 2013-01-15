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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.document.dao.DocumentDAO;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.dms.EDocUtil;

public class DocumentDAOTest extends DaoTestFixtures {

	protected DocumentDAO dao = SpringUtils.getBean(DocumentDAO.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("document", "ctl_document", "consultdocs", "demographic_merged");
	}

	@Test
	public void findDocuments() {
		String[] sortOrders = new String[] { EDocUtil.SORT_DATE, EDocUtil.SORT_DESCRIPTION, EDocUtil.SORT_DOCTYPE, EDocUtil.SORT_CREATOR, EDocUtil.SORT_RESPONSIBLE, EDocUtil.SORT_OBSERVATIONDATE, EDocUtil.SORT_CONTENTTYPE, EDocUtil.SORT_REVIEWER };
		for (String order : sortOrders) {
			MiscUtils.getLogger().info("Running sort order: " + order);
			
			assertNotNull(dao.findDocuments("MOD", "ID", "DOC", true, true, true, order));
			assertNotNull(dao.findDocuments("MOD", "ID", "DOC", true, true, false, order));
			assertNotNull(dao.findDocuments("MOD", "ID", "DOC", true, false, true, order));
			assertNotNull(dao.findDocuments("MOD", "ID", "DOC", true, false, false, order));
			assertNotNull(dao.findDocuments("MOD", "ID", "DOC", false, true, true, order));
			assertNotNull(dao.findDocuments("MOD", "ID", "DOC", false, true, false, order));
			assertNotNull(dao.findDocuments("MOD", "ID", "DOC", false, false, true, order));
			assertNotNull(dao.findDocuments("MOD", "ID", "DOC", false, false, false, order));
		}
	}
}
