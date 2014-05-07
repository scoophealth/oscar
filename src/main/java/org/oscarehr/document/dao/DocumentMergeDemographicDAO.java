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
package org.oscarehr.document.dao;

import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.common.merge.MergedDemographicTemplate;
import org.oscarehr.common.model.Document;

import oscar.dms.EDocUtil.EDocSort;
import oscar.util.ConversionUtils;

public class DocumentMergeDemographicDAO extends DocumentDao {

	@Override
	public List<Object[]> findDocuments(final String module, String moduleid, final String docType, final boolean includePublic, final boolean includeDeleted, final boolean includeActive, final EDocSort sort, final Date since) {
		List<Object[]> result = super.findDocuments(module, moduleid, docType, includePublic, includeDeleted, includeActive, sort,null);
		MergedDemographicTemplate<Object[]> template = new MergedDemographicTemplate<Object[]>() {
			@Override
			protected List<Object[]> findById(Integer demographic_no) {
				return DocumentMergeDemographicDAO.super.findDocuments(module, demographic_no.toString(), docType, includePublic, includeDeleted, includeActive, sort, since);
			}
		};
		return template.findMerged(ConversionUtils.fromIntString(moduleid), result);
	}

	@Override
	public List<Document> findByDemographicId(String demoNo) {
		List<Document> result = super.findByDemographicId(demoNo);
		MergedDemographicTemplate<Document> template = new MergedDemographicTemplate<Document>() {
			@Override
			protected List<Document> findById(Integer demographic_no) {
				return DocumentMergeDemographicDAO.super.findByDemographicId(demographic_no.toString());
			}
		};
		return template.findMerged(ConversionUtils.fromIntString(demoNo), result);
	}

}
