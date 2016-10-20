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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.oscarehr.dashboard.query.Column;
import org.oscarehr.util.MiscUtils;

public class DrilldownQueryHandler extends AbstractQueryHandler {
	
	private static Logger logger = MiscUtils.getLogger();
	private List<String[]> table;
	
	public DrilldownQueryHandler() {
		// default
	} 

	@Override
	public List<?> execute() {
		
		logger.info("Executing Drilldown Query");
	
		List<?> results = super.execute();
		this.setTable( results );		
		return results;
	}
	
	@Override
	public void setQuery( String query ) {
		String finalQuery = super.buildQuery( query );
		super.setQuery( finalQuery );
	}

	public List<String[]> getTable() {
		return table;
	}
	
	/**
	 * Arrange the query results into a List that can be drawn into the display layer 
	 * as a table easily. 
	 * The goal is to keep the patient id in the first column ( array[0] ) of each row.
	 * Otherwise the columns will be arranged in the order determined in the Indicator query
	 * template.
	 */
	@SuppressWarnings("unchecked")
	private void setTable( List<?> results ) {

		List<Column> columns = getColumns();
		String[] heading = tableHeading( results, columns );
		List<String[]> table = new ArrayList<String[]>();
		
		// first row - table heading.
		table.add( heading );

		for( Object row : results ) {
			String[] tableRow = tableRow( (Map<String, ?>) row, heading );				
			table.add( tableRow );
		}

		this.table = table;
	}
	
	private static String[] tableRow( Map<String, ?> row, String[] heading ) {
		
		String[] tableRow = new String[ heading.length ];

		for( int i = 0; i < heading.length; i++ ) {			
			String key = heading[i];
			String value = row.get( key ) + "";
	
			tableRow[i] = value;		
		}
		
		return tableRow;
	}
	
	@SuppressWarnings("unchecked")
	private static String[] tableHeading( List<?> queryResult, List<Column> columns  ) {
		
		String[] headingArray = null;		
			
		if( columns != null ) {
			
			List<String> headings = null;
			
			for( Column column : columns ) {

				String title = column.getTitle();
				
				if( headings == null ) {
					headings = new ArrayList<String>();
				}

				// primary id is always first.
				if( column.isPrimary() ) {
					headings.add( 0, title ); 
				} else {
					headings.add( title );
				}
			}
			
			headingArray = new String[ headings.size() ];
			headings.toArray( headingArray );
			
		} else if( queryResult != null && queryResult.size() > 0 ) {
			
			Map<String, ?> firstRow = (Map<String, ?>) queryResult.get(0);
			Set<String> keySet = firstRow.keySet();
			headingArray = new String[ keySet.size() ];
			keySet.toArray( headingArray );
		} 
		
		return headingArray;
	}
	
}
