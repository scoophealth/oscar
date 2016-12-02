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
import org.apache.log4j.Logger;
import org.oscarehr.dashboard.display.beans.GraphPlot;
import org.oscarehr.dashboard.display.beans.IndicatorBean;
import org.oscarehr.dashboard.handler.IndicatorQueryHandler;
import org.oscarehr.dashboard.handler.IndicatorTemplateXML;
import org.oscarehr.dashboard.query.Parameter;
import org.oscarehr.dashboard.query.RangeInterface;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * 
 * Builds IndicatorBeans with executed query results
 * Also returns a IndicatorBeans grouped by Indicator sub-category by Indicator category
 *
 */

public class IndicatorBeanFactory {
	
	private static Logger logger = MiscUtils.getLogger();
	private IndicatorQueryHandler indicatorQueryHandler = SpringUtils.getBean( IndicatorQueryHandler.class );
	
	private IndicatorTemplateXML indicatorTemplateXML;
	private IndicatorBean indicatorBean;	
	private List<Parameter> parameters;
	private List<RangeInterface> ranges;
	private String indicatorQuery;

	public IndicatorBeanFactory( IndicatorTemplateXML indicatorTemplateXML ) {

		logger.info("Thread " + Thread.currentThread().getName() +  "[" + Thread.currentThread().getId() 
				+ "] Building Indicator ID: " + indicatorTemplateXML.getId()  + " - " + indicatorTemplateXML.getName() );
		
		setIndicatorTemplateXML( indicatorTemplateXML );

		this.parameters =  getIndicatorTemplateXML().getIndicatorParameters();
		this.ranges = getIndicatorTemplateXML().getIndicatorRanges();
		
		setIndicatorQuery( getIndicatorTemplateXML().getIndicatorQuery() );
		setIndicatorBean( new IndicatorBean() );
	}

	public IndicatorTemplateXML getIndicatorTemplateXML() {
		return indicatorTemplateXML;
	}

	private void setIndicatorTemplateXML(IndicatorTemplateXML indicatorTemplateXML) {
		this.indicatorTemplateXML = indicatorTemplateXML;
	}

	public IndicatorBean getIndicatorBean() {
		return indicatorBean;
	}

	public IndicatorQueryHandler getIndicatorQueryHandler() {
		return indicatorQueryHandler;
	}

	public String getIndicatorQuery() {
		return indicatorQuery;
	}

	private void setIndicatorQuery( final String indicatorQuery ) {
		
		String queryString = new String( indicatorQuery );
		
		queryString = getIndicatorQueryHandler().filterQueryString( queryString );

		if( parameters != null ) {
			queryString = getIndicatorQueryHandler().addParameters( parameters, queryString );		
		}
		
		if( ranges != null ) {
			queryString = getIndicatorQueryHandler().addRanges( ranges, queryString );	
		}

		this.indicatorQuery = queryString;
	}

	private void setIndicatorBean(IndicatorBean indicatorBean) {
		
		copyToBean( indicatorBean, getIndicatorTemplateXML() );

		List<?> queryResultList = getIndicatorQueryHandler().execute( this.indicatorQuery );  
	
		if( queryResultList != null ) {
			
			logger.info("Setting Indicator query results for Indicator bean " + indicatorBean.getId() );

			indicatorBean.setOriginalJsonPlots(IndicatorQueryHandler.createOriginalGraphPlots(queryResultList));
			List<GraphPlot[]> graphPlots = IndicatorQueryHandler.createGraphPlots( queryResultList );
			indicatorBean.setQueryResult( queryResultList );
			indicatorBean.setQueryString( this.indicatorQuery );
			indicatorBean.setParameters( this.parameters );
			indicatorBean.setRanges( this.ranges );
			indicatorBean.setGraphPlots( graphPlots );
			indicatorBean.setJsonPlots( IndicatorQueryHandler.plotsToJson( graphPlots ) );
			indicatorBean.setJsonTooltips( IndicatorQueryHandler.plotsToJsonTooltips( graphPlots ) );
			indicatorBean.setStringArrayPlots( IndicatorQueryHandler.plotsToStringArray( graphPlots ) );
			indicatorBean.setStringArrayTooltips( IndicatorQueryHandler.plotsToTooltipsStringArray( graphPlots ) );
			
			logger.debug("Indicator Bean: " + indicatorBean.toString() );
			
		} else {
			logger.warn(" The query results and-or the Indicator Query handler were null for Indicator ID: " 
					+ indicatorBean.getId() + " Was this expected?");
		}

		this.indicatorBean = indicatorBean;
	}

	private static void copyToBean( IndicatorBean indicatorBean, IndicatorTemplateXML indicatorTemplateXML ) {

		Integer indicatorId = indicatorTemplateXML.getId();
		
		logger.info("Thread " + Thread.currentThread().getName() +  "[" + Thread.currentThread().getId() 
				+ "] Setting Indicator Bean heading info for ID " + indicatorId );
		
		indicatorBean.setId( indicatorId );
		indicatorBean.setCategory( indicatorTemplateXML.getCategory() );
		indicatorBean.setDefinition( indicatorTemplateXML.getDefinition() );
		indicatorBean.setFramework( indicatorTemplateXML.getFramework());
		indicatorBean.setFrameworkVersion( indicatorTemplateXML.getFrameworkVersion() );
		indicatorBean.setName( indicatorTemplateXML.getName() );
		indicatorBean.setNotes( indicatorTemplateXML.getNotes() );
		indicatorBean.setSubCategory( indicatorTemplateXML.getSubCategory() );
		indicatorBean.setXmlTemplate( indicatorTemplateXML.getTemplate() );

	}

}
