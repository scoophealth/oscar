/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.caisi.dao;

import java.util.List;

import org.caisi.model.EChart;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class EChartDAO extends HibernateDaoSupport {

    public void saveEncounter(EChart chart) {
        this.getHibernateTemplate().save(chart);
    }

    public EChart getLatestChart(int demographicNo) {
        //return (EChart)getHibernateTemplate().find("from EChart c where c.demographicNo = ? order by c.timeStamp desc", new Object[] {String.valueOf(demographicNo)}).get(0);
    	List echarts = getHibernateTemplate().find("from EChart c where c.demographicNo = ? order by c.timeStamp desc", new Object[] {Integer.valueOf(demographicNo)});
    	if(echarts.size()>0) {
    		return (EChart)echarts.get(0);
    	}
    	return null;
    }
}
