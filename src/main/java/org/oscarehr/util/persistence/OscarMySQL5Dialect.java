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
package org.oscarehr.util.persistence;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5Dialect;

/**
 * Customized dialect that adds a mapping for {@link Types#LONGVARBINARY} in order to prevent {@link org.hibernate.MappingException}. 
 *
 */
public class OscarMySQL5Dialect extends MySQL5Dialect {

	public OscarMySQL5Dialect() {
		super();
		registerHibernateType(Types.LONGVARBINARY, Hibernate.BINARY.getName());
		registerHibernateType(Types.LONGVARCHAR, Hibernate.TEXT.getName());		
	}

}