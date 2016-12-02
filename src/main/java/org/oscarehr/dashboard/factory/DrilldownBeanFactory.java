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

import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.dashboard.display.beans.DrilldownBean;
import org.oscarehr.dashboard.handler.DrilldownQueryHandler;
import org.oscarehr.dashboard.handler.IndicatorTemplateHandler;
import org.oscarehr.dashboard.handler.IndicatorTemplateXML;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class DrilldownBeanFactory {

	private static Logger logger = MiscUtils.getLogger();	
	private IndicatorTemplate indicatorTemplate;
	private IndicatorTemplateXML indicatorTemplateXML;
	private IndicatorTemplateHandler indicatorTemplateHandler;
	private DrilldownBean drilldownBean;
	private DrilldownQueryHandler drilldownQueryHandler = SpringUtils.getBean( DrilldownQueryHandler.class );
	
	public DrilldownBeanFactory( LoggedInInfo loggedInInfo, IndicatorTemplate indicatorTemplate ) {
		this(loggedInInfo, indicatorTemplate, null);
	}
	
	public DrilldownBeanFactory( LoggedInInfo loggedInInfo, IndicatorTemplate indicatorTemplate, String providerNo ) {
		
		logger.info("Building Drilldown Bean for Indicator ID: " + indicatorTemplate.getId() );
		
		setIndicatorTemplate( indicatorTemplate );
		String indicatorTemplateXML = getIndicatorTemplate().getTemplate();
		setIndicatorTemplateHandler( new IndicatorTemplateHandler( loggedInInfo, indicatorTemplateXML.getBytes() ) );
		IndicatorTemplateXML indicatorTemplateXmlObj = getIndicatorTemplateHandler().getIndicatorTemplateXML();
		indicatorTemplateXmlObj.setProviderNo(providerNo);
		setIndicatorTemplateXML( indicatorTemplateXmlObj );

		drilldownQueryHandler.setLoggedInInfo( loggedInInfo );
		drilldownQueryHandler.setParameters( getIndicatorTemplateXML().getDrilldownParameters() );
		drilldownQueryHandler.setColumns( getIndicatorTemplateXML().getDrilldownDisplayColumns() );
		drilldownQueryHandler.setRanges( getIndicatorTemplateXML().getDrilldownRanges() );

		setDrilldownBean( new DrilldownBean() );
	}

	public IndicatorTemplate getIndicatorTemplate() {
		return indicatorTemplate;
	}

	private void setIndicatorTemplate(IndicatorTemplate indicatorTemplate) {
		this.indicatorTemplate = indicatorTemplate;
	}

	public IndicatorTemplateXML getIndicatorTemplateXML() {
		return indicatorTemplateXML;
	}

	private void setIndicatorTemplateXML(IndicatorTemplateXML indicatorTemplateXML) {
		this.indicatorTemplateXML = indicatorTemplateXML;
	}

	public IndicatorTemplateHandler getIndicatorTemplateHandler() {
		return indicatorTemplateHandler;
	}

	private void setIndicatorTemplateHandler(IndicatorTemplateHandler indicatorTemplateHandler) {
		this.indicatorTemplateHandler = indicatorTemplateHandler;
	}

	public DrilldownQueryHandler getDrilldownQueryHandler() {
		return drilldownQueryHandler;
	}

	public DrilldownBean getDrilldownBean() {
		return drilldownBean;
	}

	private void setDrilldownBean(DrilldownBean drilldownBean) {
		// copy what is available in the entity bean
		try {
			BeanUtils.copyProperties( drilldownBean, getIndicatorTemplate() );
		} catch (Exception e) {
			logger.error("Error while copying IndicatorTemplate entity id " + getIndicatorTemplate().getId() , e);
		}
		
		List<?> queryResultList = null;
		
		if( getDrilldownQueryHandler() != null ) {	
			getDrilldownQueryHandler().setQuery( getIndicatorTemplateXML().getDrilldownQuery() );
			queryResultList = getDrilldownQueryHandler().execute();
		}
		
		if( queryResultList != null ) {
			
			drilldownBean.setQueryResult( queryResultList );
			drilldownBean.setQueryString( getDrilldownQueryHandler().getQuery() );
			drilldownBean.setDisplayColumns( getDrilldownQueryHandler().getColumns() );
			drilldownBean.setParameters( getDrilldownQueryHandler().getParameters() );
			drilldownBean.setRanges( getDrilldownQueryHandler().getRanges() );
			drilldownBean.setTable( getDrilldownQueryHandler().getTable() );
			
		} else {
			logger.warn(" The query results and-or the Indicator Query handler were null for Drilldown Indicator ID: " 
					+ drilldownBean.getId() + " Was this expected?");
		}

		this.drilldownBean = drilldownBean;
	}
	
}
