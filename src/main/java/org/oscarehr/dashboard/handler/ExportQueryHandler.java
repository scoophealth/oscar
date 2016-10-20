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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class ExportQueryHandler extends AbstractQueryHandler {
	
	private static Logger logger = MiscUtils.getLogger();
	private static final char SEPARATOR = ',';
	
	private String csvFile;
	
	public ExportQueryHandler() {
		// default
	}

	@Override
	public List<?> execute() {
		
		logger.info("Executing Export Query");
	
		List<?> results = super.execute();		
		setCsvFile( results );	
		return results;
	}
	
	@Override
	public void setQuery( String query ) {
		String finalQuery = super.buildQuery( query );
		super.setQuery( finalQuery );
	}

	public String getCsvFile() {
		return csvFile;
	}

	@SuppressWarnings("unchecked")
	private void setCsvFile( List<?> results ) {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append( writeHeadings( results ) );
		
		for( Object object : results ) {
			
			Map<String, ?> result = (Map<String, ?>) object;
			Collection<?> resultCollection = result.values();
			Object[] resultArray = new Object[ resultCollection.size() ];
			resultCollection.toArray( resultArray );
			
			stringBuilder.append( writeLine( resultArray ) );
		}

		this.csvFile = stringBuilder.toString();
	}
	
	@SuppressWarnings("unchecked")
	private static String writeHeadings( List<?> results ) {
		
		Map<String, ?> firstRow = (Map<String, ?>) results.get(0);
		Set<String> keySet = firstRow.keySet();		
		String[] headingArray = new String[ keySet.size() ];
		keySet.toArray( headingArray );
		
		return writeLine( headingArray );
	}

	private static String writeLine( Object[] line ) {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		for( Object value : line ) {
			
			String stringValue = value + "";			
			stringBuilder.append( filterQuotes( stringValue ) );
			stringBuilder.append( SEPARATOR );			
		}
		
		stringBuilder.deleteCharAt( stringBuilder.length() - 1 );	
		stringBuilder.append("\n");
		
		return stringBuilder.toString();
	}

	private static String filterQuotes( String value ) {
		
		if (value.contains("\"")) {
			value = value.replace("\"", "\"\"");
		}
		return value;
	}
	
}
