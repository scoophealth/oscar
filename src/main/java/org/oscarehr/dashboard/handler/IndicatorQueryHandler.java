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
package org.oscarehr.dashboard.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.dashboard.display.beans.GraphPlot;
import org.oscarehr.managers.DashboardManager;
import org.oscarehr.util.MiscUtils;


/**
 * Depends on a proper convention of place-holders in the query text.
 * 
 * RegEx is:  "(\\$){1}(\\{){1}( )*(.)*( )*(\\}){1}"
 * ie: ${ parameter_id }
 *
 */
public class IndicatorQueryHandler extends AbstractQueryHandler {

	private static Logger logger = MiscUtils.getLogger();
	private List< GraphPlot[] > graphPlots;
	private static final Double DEFAULT_DENOMINATOR = 100.0;

	public IndicatorQueryHandler() {	
		// default
	}

	@Override
	public List<?> execute( String query ) {

		logger.info("Executing Indicator Query Thread " + Thread.currentThread().getName() 
				+  "[" + Thread.currentThread().getId() + "]" );
		
		List<?> results = null;
		
		if( DashboardManager.MULTI_THREAD_ON ) {
			results = super.execute( query );
		} else {
			this.setQuery( query );	
			results = super.execute();
			graphPlots( results );
		}
		
		return results;
	}

	@Override
	public void setQuery( String query ) {
		String finalQuery = super.buildQuery( query );
		super.setQuery( finalQuery );
	}

	public List<GraphPlot[]> getGraphPlots() {
		return graphPlots;
	}
	
	@Override
	public final String filterQueryString( String queryString ) {
		return super.filterQueryString( queryString );
	}
	
	/**
	 * Build graph data with GraphPlot objects and set them into the Indicator Bean for display.
	 * Each row of graph plots is a new graph
	 * Each GraphPlot column is a plot on the graph.
	 * 
	 * Not Thread Safe 
	 */
	private void graphPlots( List<?> results ) {
		setGraphPlots( createGraphPlots( results ) );
	}
	
	public void setGraphPlots( List< GraphPlot[] > graphPlots ) {
		this.graphPlots = graphPlots;
	}
	
	@SuppressWarnings("unchecked")
	public static List< GraphPlot[] > createGraphPlots( List<?> results ) {
		
		List< GraphPlot[] > graphPlotList = null;

		//[{% Not Recorded=33.3, % Status Recorded=66.7}] ArrayList with a HashMap
		
		//figure out the denominator
		int denominator = 0;
		
		for(Object row : results) {
			Map<String, ?> theRow = (Map<String, ?>)row;
			for(String key:theRow.keySet()) {
				Integer numerator = 0;
				
				if(theRow.get(key) instanceof String) {
					numerator = Integer.parseInt((String)theRow.get(key));
				}
				if(theRow.get(key) instanceof BigDecimal) {
					numerator = ((BigDecimal)theRow.get(key)).intValue();
				}
				if(theRow.get(key) instanceof Double) {
					numerator = ((Double)theRow.get(key)).intValue();
					
				}
			//	BigDecimal numerator = (BigDecimal)theRow.get(key);
				denominator += numerator.intValue();
			}
		}
		
		if(denominator > 0) {
		
			//now update the numerators to be percentages
			for(Object row : results) {
				Map<String, BigDecimal> theRow = (Map<String, BigDecimal>)row;
				for(String key:theRow.keySet()) {
					BigDecimal numerator = theRow.get(key);
					BigDecimal bd = BigDecimal.valueOf((numerator.doubleValue() * 100 )/ denominator);
					bd.setScale(2, BigDecimal.ROUND_CEILING);
					theRow.put(key,bd);
				}
			}
		}
 
		 
		for(Object row : results) {
			if( graphPlotList == null ) {
				graphPlotList = new ArrayList< GraphPlot[] >();
			}

			GraphPlot[] graphPlots = createGraphPlots( ( Map<String, ?> ) row ); 

			graphPlotList.add( graphPlots );
		}
		
		return graphPlotList;
	}

	/**
	 * Value is the query result and key is the column (or result) alias
	 */
	private static GraphPlot[] createGraphPlots( Map<String, ?> row ) {

		List<GraphPlot> graphPlots = null; 
		Iterator<?> it = row.keySet().iterator();
		
		while( it.hasNext() ) {
			
			if( graphPlots == null ) {
				graphPlots = new ArrayList<GraphPlot>(); 
			}
			
			GraphPlot graphPlot = new GraphPlot();
			String key = it.next() + "";
			Object value = null;

			if( key.equalsIgnoreCase("null") ) {
				key = "";
			}
			
			key = key.trim();
			
			if( ! key.isEmpty() ) {
				value = row.get( key );
			} else {
				logger.warn( "Null or Empty Key found for the label parameter of this graph plot." );
			}
			
			// Only pie charts for now - so the denom is out of 100 percent.
			graphPlot.setDenominator( DEFAULT_DENOMINATOR );

			if( value instanceof Number ) {
				Number plot = (Number) value;
				graphPlot.setNumerator( plot.doubleValue() );
			}

			graphPlot.setKey( key );
			graphPlot.setLabel( key );			
			graphPlots.add( graphPlot );
		}
		
		GraphPlot[] graphPlotArray = new GraphPlot[ graphPlots.size() ];
		graphPlots.toArray( graphPlotArray );
		
		return graphPlotArray;
	}
	
	// helper utilities.
	
	public static String plotsToStringArray( List<GraphPlot[]> graphPlots ) {
		StringBuilder json = new StringBuilder("");
		for(GraphPlot[] graphPlotArray : graphPlots) {
			json.append("[");
			for(GraphPlot graphPlot : graphPlotArray ) {
				json.append("['");
				json.append( graphPlot.getLabel() );
				json.append("',");
				json.append( graphPlot.getNumerator() );
				json.append("],");
			}
			json.deleteCharAt( json.length() - 1 );
			
			json.append("],");
		}
		json.deleteCharAt( json.length() - 1 );
		
		return json.toString();
	}
	
	public static String createOriginalGraphPlots(List<?> queryResultList) {
		
		
		
		StringBuilder json = new StringBuilder("");
		json.append("{ 'results':[");
		
		for(Object row : queryResultList) {
			Map<String, ?> theRow = (Map<String, ?>)row;
			for(String key:theRow.keySet()) {
				//BigDecimal numerator = (BigDecimal)theRow.get(key);
				json.append("{'");
				json.append( key );
				json.append("':");
				json.append( theRow.get(key) );
				json.append("},");
				
			}
			json.deleteCharAt( json.length() - 1 );
		}
		
		json.append("]}");
		
		
		return json.toString();
		
		
	}
	
	public static String plotsToJson( List<GraphPlot[]> graphPlots ) {
		StringBuilder json = new StringBuilder("");
		int index = 0;
		for(GraphPlot[] graphPlotArray : graphPlots) {
			json.append("{ 'results_" + index + "':[");
			for(GraphPlot graphPlot : graphPlotArray ) {
				json.append("{'");
				json.append( graphPlot.getLabel() );
				json.append("':");
				json.append( graphPlot.getNumerator() );
				json.append("},");
			}
			json.deleteCharAt( json.length() - 1 );
			
			json.append("]},");
			index++;
		}
		json.deleteCharAt( json.length() - 1 );
		
		return json.toString();
	}
	
	public static String plotsToTooltipsStringArray( List<GraphPlot[]> graphPlots ) {
		StringBuilder json = new StringBuilder("");
		for(GraphPlot[] graphPlotArray : graphPlots) {
			json.append("[");
			for(GraphPlot graphPlot : graphPlotArray ) {
				json.append( "'" );
				json.append( graphPlot.getKey() );
				json.append( "'" );
				json.append(",");
			}
			json.deleteCharAt( json.length() - 1 );
			
			json.append("],");
		}
		json.deleteCharAt( json.length() - 1 );		
		return json.toString();
	}
	
	public static String plotsToJsonTooltips( List<GraphPlot[]> graphPlots ) {
		StringBuilder json = new StringBuilder("");
		int index = 0;
		for(GraphPlot[] graphPlotArray : graphPlots) {
			json.append("{ 'toolTips_" + index + "':[");
			for(GraphPlot graphPlot : graphPlotArray ) {
				json.append( "'" );
				json.append( graphPlot.getKey() );
				json.append( "'" );
				json.append(",");
			}
			json.deleteCharAt( json.length() - 1 );
			
			json.append("]},");
			index++;
		}
		json.deleteCharAt( json.length() - 1 );		
		return json.toString();
	}

}
