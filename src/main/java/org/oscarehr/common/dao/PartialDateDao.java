/**
 * Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * This software was written for
 * CAISI,
 * Toronto, Ontario, Canada
 */
package org.oscarehr.common.dao;


import java.util.Date;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.commons.lang.math.NumberUtils;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;

@Repository
public class PartialDateDao extends AbstractDao<PartialDate> {

	public PartialDateDao() {
		super(PartialDate.class);
	}

        public PartialDate getPartialDate(Integer tableName, Integer tableId, String fieldName) {
	   	String sqlCommand = "select x from PartialDate x order by x.id desc limit 1";
		Query query = entityManager.createQuery(sqlCommand);

		@SuppressWarnings("unchecked")
		PartialDate result = null;
                try {
                    result = (PartialDate) query.getSingleResult();
                } catch (NoResultException ex) {
                    MiscUtils.getLogger().error("Note",ex);
                }

		return result;
        }

        public String getDatePartial(Date fieldDate, Integer tableName, Integer tableId, String fieldName) {
                PartialDate partialDate = getPartialDate(tableName, tableId, fieldName);
                if (partialDate!=null) return makeDatePartial(fieldDate, partialDate.getFormat());
                return null;
        }

        public void setPartialDate(Date fieldDate, Integer tableName, Integer tableId, String fieldName) {
                String format = checkDatePartial(UtilDateUtilities.DateToString(fieldDate, "yyyy-MM-dd"));
                setFormat(tableName, tableId, fieldName, format);
        }

        public String checkDatePartial(String dateValue) {
            if (StringUtils.empty(dateValue)) return null;

            dateValue = dateValue.trim();
            dateValue = dateValue.replace("/", "-");
            if (dateValue.length()==4 && NumberUtils.isDigits(dateValue)) return new PartialDate().getYEARONLY();

            String[] dateParts = dateValue.split("-");
            if (dateParts.length==2 && NumberUtils.isDigits(dateParts[0]) && NumberUtils.isDigits(dateParts[1])) {
                if (dateParts[0].length()==4 &&
                    dateParts[1].length()>=1 && dateParts[1].length()<=2) return new PartialDate().getYEARMONTH();
            }
            return null;
        }





        private void setFormat(Integer tableName, Integer tableId, String fieldName, String format) {
                PartialDate partialDate = getPartialDate(tableName, tableId, fieldName);
                if (partialDate==null) partialDate = new PartialDate(tableName, tableId, fieldName, format);
                persist(partialDate);
        }

        private String makeDatePartial(Date dateValue, String format) {
            if (dateValue==null) return null;

            String dateString = UtilDateUtilities.DateToString(dateValue, "yyyy-MM-dd");
            String type = checkDatePartial(dateString);
            if (type==null) return dateString;

            if (type.equals(new PartialDate().getYEARONLY())) {
                dateString = dateString.substring(0,4);
            }
            else if (type.equals(new PartialDate().getYEARMONTH())) {
                dateString = dateString.substring(0,7);
            }
            return dateString;
        }
}
