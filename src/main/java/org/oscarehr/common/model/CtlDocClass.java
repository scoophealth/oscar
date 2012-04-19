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


package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "ctl_doc_class")
public class CtlDocClass extends AbstractModel<Integer> implements Serializable {

        @Id
        @Column(name = "id")
        private Integer id = null;
	@Column(name = "reportclass")
	private String reportClass = null;
	@Column(name = "subclass")
	private String subClass = null;


        public CtlDocClass() {}

        public String getReportClass() {
            return this.reportClass;
        }
        public void setReportClass(String s) {
            this.reportClass = s;
        }

        public String getSubClass() {
            return this.subClass;
        }
        public void setSubClass(String s) {
            this.subClass = s;
        }

    @Override
        public Integer getId() {
            return this.id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
}
