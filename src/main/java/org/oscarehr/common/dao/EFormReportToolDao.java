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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.EFormReportTool;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class EFormReportToolDao extends AbstractDao<EFormReportTool> {

	Logger logger = MiscUtils.getLogger();
	
	public EFormReportToolDao() {
		super(EFormReportTool.class);
	}

	@SuppressWarnings("unchecked")
	public void markLatest(Integer eformReportToolId) {
		EFormReportTool eft = find(eformReportToolId);
		if (eft != null) {
			//get all distinct demographicNos
			Query q = entityManager.createNativeQuery("select distinct demographicNo from  " + eft.getTableName());
			List<Integer> demoNos = q.getResultList();
			for (Integer demoNo : demoNos) {
				Query q2 = entityManager.createNativeQuery("select id from " + eft.getTableName() + " where demographicNo = " + demoNo + " order by dateFormCreated desc,fdid desc limit 1");
				List<Integer> idList = q2.getResultList();

				//update the first result
				Query q3 = entityManager.createNativeQuery("update " + eft.getTableName() + " set eft_latest=1 where id=" + idList.get(0));
				q3.executeUpdate();
			}

			eft.setLatestMarked(true);
			merge(eft);
		}
	}

	public void addNew(EFormReportTool eformReportTool, EForm eform, List<String> fields, String providerNo, boolean useNameAsTableName) {
		//generate the create table statement
		String tableName = "ERT_"+ eformReportTool.getName();
		if(!useNameAsTableName) {
			tableName += (new BigInteger(130, new SecureRandom()).toString(8).substring(0, 8));
		}
		StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");
		sql.append("id int (10) NOT NULL auto_increment primary key,");
		sql.append("fdid int (10) NOT NULL, ");
		sql.append("demographicNo int (10) NOT NULL, ");
		sql.append("dateFormCreated datetime NOT NULL, ");
		sql.append("eft_providerNo varchar(6) NOT NULL, ");
		sql.append("eft_latest tinyint(1) NOT NULL, ");
		sql.append("dateCreated timestamp NOT NULL ");
		for (String field : fields) {
			sql.append(",`" + field + "` text");
		}
		sql.append(")");

		logger.debug("sql=" + sql);

		//commit the table
		Query q = entityManager.createNativeQuery(sql.toString());
		q.executeUpdate();

		//save the EformReportTool
		eformReportTool.setDateLastPopulated(null);
		eformReportTool.setId(null);
		eformReportTool.setTableName(tableName);
		eformReportTool.setProviderNo(providerNo);
		eformReportTool.setLatestMarked(false);
		persist(eformReportTool);

	}

	public void populateReportTableItem(EFormReportTool eft, List<EFormValue> values, Integer fdid, Integer demographicNo, Date dateFormCreated, String providerNo) {
		//create an insert statement
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(eft.getTableName());
		sb.append(" (");
		sb.append("fdid,");
		sb.append("demographicNo,");
		sb.append("dateFormCreated,");
		sb.append("eft_providerNo,");
		sb.append("eft_latest,");
		sb.append("dateCreated,");
		for (EFormValue v : values) {
			sb.append("`" + StringEscapeUtils.escapeSql(v.getVarName()) + "`");
			sb.append(",");
		}

		sb.deleteCharAt(sb.length() - 1);

		sb.append(" ) VALUES (");
		sb.append(fdid + ",");
		sb.append(demographicNo + ",");
		sb.append("\'" + DateFormatUtils.format(dateFormCreated, "yyyy-MM-dd HH:mm:ss") + "\',");
		sb.append("\'" + providerNo + "\',");
		sb.append("0,");
		sb.append("now(),");
		for (EFormValue v : values) {
			sb.append("\'" + StringEscapeUtils.escapeSql(v.getVarValue()) + " \'");
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);

		sb.append(")");

		logger.debug("sql=" + sb.toString());
		
		Query q = entityManager.createNativeQuery(sb.toString());
		q.executeUpdate();
	}

	public void deleteAllData(EFormReportTool eft) {
		if (eft != null) {
			Query q = entityManager.createNativeQuery("delete from " + eft.getTableName());
			q.executeUpdate();
		}
	}

	public void drop(EFormReportTool eft) {
		if (eft != null) {
			Query q = entityManager.createNativeQuery("drop table " + eft.getTableName());
			q.executeUpdate();
		}
	}

	public Integer getNumRecords(EFormReportTool eformReportTool) {
		if (eformReportTool != null) {
			Query q = entityManager.createNativeQuery("select count(*) from " + eformReportTool.getTableName());
			List<BigInteger> results = q.getResultList();
			if (!results.isEmpty()) {
				return results.get(0).intValue();
			}
		}
		return null;
	}
	
}
