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
import org.oscarehr.dashboard.display.beans.IndicatorBean;
import org.oscarehr.dashboard.handler.IndicatorQueryHandler;
import org.oscarehr.dashboard.handler.IndicatorTemplateXML;
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
	private IndicatorTemplateXML indicatorTemplateXML;
	private IndicatorBean indicatorBean;
	private IndicatorQueryHandler indicatorQueryHandler = SpringUtils.getBean( IndicatorQueryHandler.class );
	
	public IndicatorBeanFactory( IndicatorTemplateXML indicatorTemplateXML ) {
		
		logger.info("Building Indicator ID: " + indicatorTemplateXML.getName() );
		
		setIndicatorTemplateXML( indicatorTemplateXML );
		if( this.indicatorQueryHandler != null ) {
			this.indicatorQueryHandler.setParameters( getIndicatorTemplateXML().getIndicatorParameters() );
			this.indicatorQueryHandler.setRanges( getIndicatorTemplateXML().getIndicatorRanges() );
		}
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

	private void setIndicatorBean(IndicatorBean indicatorBean) {
		
		copyToBean( indicatorBean, getIndicatorTemplateXML() );
		List<?> queryResultList = null;
		
		if( getIndicatorQueryHandler() != null ) {
			queryResultList = getIndicatorQueryHandler().execute( getIndicatorTemplateXML().getIndicatorQuery() );
		}
		
		if( queryResultList != null ) {
			indicatorBean.setQueryResult( queryResultList );
			indicatorBean.setQueryString( getIndicatorQueryHandler().getQuery() );
			indicatorBean.setParameters( getIndicatorQueryHandler().getParameters() );
			indicatorBean.setRanges( getIndicatorQueryHandler().getRanges() );
			indicatorBean.setGraphPlots(  getIndicatorQueryHandler().getGraphPlots() );
			
			logger.debug("New Indicator Bean: " + indicatorBean.toString() );
			
		} else {
			logger.warn(" The query results and-or the Indicator Query handler were null for Indicator ID: " 
					+ indicatorBean.getId() + " Was this expected?");
		}

		this.indicatorBean = indicatorBean;
	}
	
	private static void copyToBean( IndicatorBean indicatorBean, IndicatorTemplateXML indicatorTemplateXML ) {

		indicatorBean.setId( indicatorTemplateXML.getId() );
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
