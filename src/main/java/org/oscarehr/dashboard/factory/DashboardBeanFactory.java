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
package org.oscarehr.dashboard.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.Dashboard;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.dashboard.display.beans.DashboardBean;
import org.oscarehr.dashboard.display.beans.PanelBean;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

/**
 * 
 * Construct a DashboardBean 
 * Add a IndicatorTemplateHandler and IndicatorTemplates to construct a full 
 * Dashboard with the assigned Indicators by category and sub-category. 
 * 
 * This class is chained together with the Indicator and PanelBean Factories to 
 * create a full Dashboard from a list of Indicators and Dashboards.
 */
public class DashboardBeanFactory {
	
	private static Logger logger = MiscUtils.getLogger();	
	private DashboardBean dashboardBean;
	private List<IndicatorTemplate> indicatorTemplates;
	private Dashboard dashboardEntity;
	
	/**
	 * Parameters cannot be null or empty. This will work if the Indicator Templates are pre-set into the
	 * DashboardEntity Object.
	 */
	public DashboardBeanFactory( LoggedInInfo loggedInInfo, Dashboard dashboardEntity ) {
		this( loggedInInfo, dashboardEntity, null);
	}
	
	/**
	 * Additional IndicatorTemplates parameter incase they are not preset in the Dashboard Entity.
	 */
	public DashboardBeanFactory( LoggedInInfo loggedInInfo, Dashboard dashboardEntity, List<IndicatorTemplate> indicatorTemplates ) {
		
		logger.info("Building Dashboard: " + dashboardEntity.getName() );
		
		setDashboardEntity(dashboardEntity);
	
		if(indicatorTemplates == null ) {
			indicatorTemplates = dashboardEntity.getIndicators();
		}
		
		setIndicatorTemplates( indicatorTemplates );	
		setDashboardBean( new DashboardBean() );	
		setPanelBeans( getDashboardBean(), getIndicatorTemplates() );
		getDashboardBean().setLastChecked( new Date( System.currentTimeMillis() ) );
	}
	
	public DashboardBean getDashboardBean() {
		return dashboardBean;
	}

	private void setDashboardBean( DashboardBean dashboardBean ) {
		try {
			// copy matching properties from Bean to Bean
			BeanUtils.copyProperties( dashboardBean , getDashboardEntity() );
		} catch (IllegalAccessException e) {
			logger.error("Error", e);
		} catch (InvocationTargetException e) {
			logger.error("Error", e);
		}

		this.dashboardBean = dashboardBean;
	}

	private List<IndicatorTemplate> getIndicatorTemplates() {
		return indicatorTemplates;
	}

	private void setIndicatorTemplates(List<IndicatorTemplate> indicatorTemplates) {
		this.indicatorTemplates = indicatorTemplates;
	}

	private Dashboard getDashboardEntity() {
		return dashboardEntity;
	}
	
	/**
	 * This method passes the IndicatorTemplates to the IndicatorPanelBeanFactory.
	 * The IndicatorPanelBeanFactory returns a sorted list of IndicatorPanelBeans that are set into
	 * the DashboardBean. 
	 */
	private void setPanelBeans( DashboardBean dashboardBean, List<IndicatorTemplate> indicatorTemplates ) {
		if( indicatorTemplates != null && ! indicatorTemplates.isEmpty()) {
			PanelBeanFactory panelBeanFactory = new PanelBeanFactory( indicatorTemplates );
			List<PanelBean> panelBeans = panelBeanFactory.getPanelBeans();
			dashboardBean.setPanelBeans(panelBeans);
		} else {
			logger.warn("There are no Indicator Templates for this Dashboard. Dashboard id [" + dashboardBean.getId() + "]");
		}
	}

	private void setDashboardEntity(Dashboard dashboardEntity) {
		this.dashboardEntity = dashboardEntity;
	}

}
