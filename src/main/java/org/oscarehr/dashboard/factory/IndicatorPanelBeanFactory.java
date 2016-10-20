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
import org.oscarehr.dashboard.display.beans.IndicatorBean;
import org.oscarehr.dashboard.display.beans.IndicatorPanelBean;
import org.oscarehr.dashboard.handler.IndicatorTemplateXML;
import org.oscarehr.managers.DashboardManager;
import org.oscarehr.util.MiscUtils;

/** 
 * Builds the Panels that hold the Indicators by sub-category.
 * 
 */
public class IndicatorPanelBeanFactory {
	
	private static Logger logger = MiscUtils.getLogger();
	
	private String category;
	private List<IndicatorTemplateXML> indicatorTemplateXMLList;
	private List<IndicatorPanelBean> indicatorPanelBeans;
	private HashSet<String> subcategories;

	public IndicatorPanelBeanFactory( String category, List<IndicatorTemplateXML> indicatorTemplateXMLList ) {
		
		logger.info( "Building Indicator Panels for category " + category );
		
		setCategory(category);
		setIndicatorTemplateXMLList( indicatorTemplateXMLList );
		setSubcategories( new HashSet<String>() );
		setIndicatorPanelBeans( new ArrayList<IndicatorPanelBean>() );
	}

	public String getCategory() {
		if( category == null ) {
			return "";
		}
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<IndicatorTemplateXML> getIndicatorTemplateXMLList() {
		return indicatorTemplateXMLList;
	}

	public void setIndicatorTemplateXMLList(List<IndicatorTemplateXML> indicatorTemplateXMLList) {
		this.indicatorTemplateXMLList = indicatorTemplateXMLList;
	}

	public List<IndicatorPanelBean> getIndicatorPanelBeans() {
		return indicatorPanelBeans;
	}

	private void setIndicatorPanelBeans( List<IndicatorPanelBean> indicatorPanelBeans ) {
		
		if( getSubcategories() == null ) {
			
			logger.warn("No Indicator Templates found");
			indicatorPanelBeans.clear();
			indicatorPanelBeans = null;
			
		} else {

			Iterator<String> it = getSubcategories().iterator();
			while( it.hasNext() ) {

				IndicatorPanelBean indicatorPanelBean = null;
				String subcategory = it.next();
				
				if( subcategory == null ) {
					subcategory = "";
				}
				
				if( DashboardManager.MULTI_THREAD_ON ) {
					// Multi-threaded method
					indicatorPanelBean = createIndicatorPanelsWithIndicatorIds( subcategory );
				} else {
					// Single-threaded method
					indicatorPanelBean = createIndicatorPanelWithIndicatorBeans( subcategory );
				}

				if( indicatorPanelBean != null ) {
					indicatorPanelBeans.add( indicatorPanelBean );
				}
			}
		}
		
		this.indicatorPanelBeans = indicatorPanelBeans;
	}

	public HashSet<String> getSubcategories() {
		return subcategories;
	}

	/**
	 * Extract the sub-categories from the Indicator Template list.
	 */
	private void setSubcategories( HashSet<String> subcategories ) {
		
		if( getIndicatorTemplateXMLList() != null && ! getIndicatorTemplateXMLList().isEmpty() ) {
			for( IndicatorTemplateXML indicatorTemplateXML : getIndicatorTemplateXMLList() ) {
				String subcategory = indicatorTemplateXML.getSubCategory();
				subcategories.add( subcategory );
			} 
		} else {
			logger.warn("No Indicator Beans were found");
			subcategories = null;
		}
		
		this.subcategories = subcategories;
	}
	
	// helpers
	
	/**
	 * Method with a multi-threaded approach. Rather than getting all the Indicator Beans 
	 * and queries in a single batch, this method will only return the Indicator ids available
	 * to this panel.  Then it will be up to the loading servlet to request the Indicator Bean
	 * as the Indicator id is processed. 
	 * 
	 * If a template matches both the category and sub-category then create an
	 * indicator based on the data from the match.
	 *  Many Indicators can be assigned to each sub-category parameter
	 */
	private IndicatorPanelBean createIndicatorPanelsWithIndicatorIds( String subcategory ) {
		
		IndicatorPanelBean indicatorPanelBean = null;
		List<Integer> indicatorIdList = null;
		
		// add the indicator beans to the indicator panel bean based on sub-category 
		// matches within the previously provided category.
		for( IndicatorTemplateXML indicatorTemplateXML : getIndicatorTemplateXMLList() ) {
			
			if( getCategory().equals( indicatorTemplateXML.getCategory() ) && 
					subcategory.equals( indicatorTemplateXML.getSubCategory() ) ) {

				if( indicatorIdList == null ) {
					indicatorIdList = new ArrayList<Integer>();
				}

				indicatorIdList.add( indicatorTemplateXML.getId() );
				
				if( indicatorPanelBean == null ) {
					indicatorPanelBean = new IndicatorPanelBean();
					indicatorPanelBean.setCategory( subcategory );
				}
			}
			
		}
		
		if( indicatorPanelBean != null ) {
			indicatorPanelBean.setIndicatorIdList( indicatorIdList );
		}
		
		return indicatorPanelBean;
	}
	
	/**
	 * Method with a single-threaded approach.  This method will get all the Indicators and run all 
	 * the queries in a single batch.  This method will cause the Dashboard to appear blank 
	 * until all the queries are completed. 
	 * 
	 * If a template matches both the category and sub-category then create an
	 * indicator based on the data from the match.
	 * Many Indicators can be assigned to each sub-category parameter
	 */
	private IndicatorPanelBean createIndicatorPanelWithIndicatorBeans( String subcategory ) {
		
		IndicatorPanelBean indicatorPanelBean = null;
		List<IndicatorBean> indicatorBeans = null;

		// add the indicator beans to the indicator panel bean based on sub-category 
		// matches within the previously provided category.
		for( IndicatorTemplateXML indicatorTemplateXML : getIndicatorTemplateXMLList() ) {
			
			if( getCategory().equals( indicatorTemplateXML.getCategory() ) && 
					subcategory.equals( indicatorTemplateXML.getSubCategory() ) ) {
				
				if( indicatorBeans == null ) {
					indicatorBeans = new ArrayList<IndicatorBean>();
				}

				IndicatorBeanFactory indicatorBeanFactory = new IndicatorBeanFactory( indicatorTemplateXML );				
				indicatorBeans.add( indicatorBeanFactory.getIndicatorBean() );

				if( indicatorPanelBean == null ) {
					indicatorPanelBean = new IndicatorPanelBean();
					indicatorPanelBean.setCategory( subcategory );
				}
			}
			
		}
		
		if( indicatorPanelBean != null ) {
			 indicatorPanelBean.setIndicatorBeans( indicatorBeans );
		}
		
		return indicatorPanelBean;
	}

}