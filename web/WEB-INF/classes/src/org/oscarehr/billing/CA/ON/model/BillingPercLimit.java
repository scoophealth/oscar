/*
*
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* <OSCAR TEAM>
*
* This software was written for
* Centre for Research on Inner City Health, St. Michael's Hospital,
* Toronto, Ontario, Canada
*/

package org.oscarehr.billing.CA.ON.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.oscarehr.common.model.AbstractModel;
/**
 *
 * @author rjonasz
 */
@Entity
@Table(name="billingperclimit")
public class BillingPercLimit extends AbstractModel<Integer> implements Serializable {

    private String service_code;
    private String min;
    private String max;
    private Date effective_date;
    private Integer id;

    /**
     * @return the service_code
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getService_code() {
        return service_code;
    }

    /**
     * @param service_code the service_code to set
     */
    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    /**
     * @return the min
     */
    public String getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(String min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public String getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(String max) {
        this.max = max;
    }

    /**
     * @return the effective_date
     */
    @Temporal(TemporalType.DATE)
    public Date getEffective_date() {
        return effective_date;
    }

    /**
     * @param effective_date the effective_date to set
     */
    public void setEffective_date(Date effective_date) {
        this.effective_date = effective_date;
    }

}
