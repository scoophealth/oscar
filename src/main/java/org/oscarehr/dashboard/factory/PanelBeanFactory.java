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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.dashboard.display.beans.IndicatorPanelBean;
import org.oscarehr.dashboard.display.beans.PanelBean;
import org.oscarehr.dashboard.handler.IndicatorTemplateHandler;
import org.oscarehr.dashboard.handler.IndicatorTemplateXML;
import org.oscarehr.util.MiscUtils;

public class PanelBeanFactory {
	private static Logger logger = MiscUtils.getLogger();
	
	private List<IndicatorTemplateXML> indicatorTemplateXMLList;
	private List<IndicatorTemplate> indicatorTemplates;
	private IndicatorTemplateHandler indicatorTemplateHandler;
	private List<PanelBean> panelBeans;
	private HashSet<String> categories;
	
	public PanelBeanFactory( List<IndicatorTemplate> indicatorTemplates ) {
		
		logger.info("Building Dashboard Panels");
		
		setIndicatorTemplates( indicatorTemplates );
		setIndicatorTemplateHandler( new IndicatorTemplateHandler() );
		setIndicatorTemplateXMLList( new ArrayList<IndicatorTemplateXML>() );
		setIndicatorPanelBeans( new ArrayList<PanelBean>() );
	}
	
	public List<IndicatorTemplateXML> getIndicatorTemplateXMLList() {
		return indicatorTemplateXMLList;
	}

	public void setIndicatorTemplateXMLList( List<IndicatorTemplateXML> indicatorTemplateXMLList ) {
		
		if( getIndicatorTemplateHandler() == null ) {
			logger.error("The IndicatorTemplateHandler is not set. Cannot create IndicatorPanelBeans");
			return;
		}
		
		if( getIndicatorTemplates() != null && ! getIndicatorTemplates().isEmpty() ) {
			
			// also split out a category list.
			setCategories( new HashSet<String>() );
			
			for(IndicatorTemplate indicatorTemplate : getIndicatorTemplates() ) {
				byte[] bytearray = indicatorTemplate.getTemplate().getBytes();
				getIndicatorTemplateHandler().read( bytearray );
				IndicatorTemplateXML indicatorTemplateXML = getIndicatorTemplateHandler().getIndicatorTemplateXML();
				if( indicatorTemplateXML != null ) {
					indicatorTemplateXML.setId( indicatorTemplate.getId() );
					getCategories().add( indicatorTemplateXML.getCategory() );
					indicatorTemplateXMLList.add( indicatorTemplateXML );
				}
			}
			
		} else {
			logger.warn("There are no Indicator Templates");
		}
		
		this.indicatorTemplateXMLList = indicatorTemplateXMLList;
	}

	private List<IndicatorTemplate> getIndicatorTemplates() {
		return indicatorTemplates;
	}

	/**
	 * IndicatorTemplate Entities contain raw XML template data.
	 * @param indicatorTemplates
	 */
	public void setIndicatorTemplates(List<IndicatorTemplate> indicatorTemplates) {
		this.indicatorTemplates = indicatorTemplates;
	}

	private IndicatorTemplateHandler getIndicatorTemplateHandler() {
		return indicatorTemplateHandler;
	}

	public void setIndicatorTemplateHandler(IndicatorTemplateHandler indicatorTemplateHandler) {
		this.indicatorTemplateHandler = indicatorTemplateHandler;
	}

	public List<PanelBean> getPanelBeans() {
		return panelBeans;
	}

	/**
	 * Each panel represents an Indicator Category.
	 * Each panel holds a List of Indicator sub-categories.
	 * @param indicatorPanelBeans
	 */
	private void setIndicatorPanelBeans( List<PanelBean> panelBeans ) {
		
		if( getCategories() != null ) {
			
			Iterator<String> it = getCategories().iterator();
			
			while( it.hasNext() ) {				
				String category = it.next();
				PanelBean panelBean = new PanelBean();
				panelBean.setCategory(category);				
				IndicatorPanelBeanFactory indicatorPanelBeanFactory = new IndicatorPanelBeanFactory( category, getIndicatorTemplateXMLList() );				
				List<IndicatorPanelBean> indicatorPanelBeans = indicatorPanelBeanFactory.getIndicatorPanelBeans();
				panelBean.setIndicatorPanelBeans( indicatorPanelBeans );
				panelBeans.add( panelBean );
			}
		}
		
		this.panelBeans = panelBeans;
	}

	public HashSet<String> getCategories() {
		return categories;
	}

	private void setCategories( HashSet<String> categories ) {
		this.categories = categories;
	}

	
}
