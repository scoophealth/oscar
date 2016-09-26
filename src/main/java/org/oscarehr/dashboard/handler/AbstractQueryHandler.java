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

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.oscarehr.dashboard.handler.IndicatorTemplateXML.RangeType;
import org.oscarehr.dashboard.query.Column;
import org.oscarehr.dashboard.query.Parameter;
import org.oscarehr.dashboard.query.RangeInterface;
import org.oscarehr.dashboard.query.RangeInterface.Limit;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractQueryHandler extends HibernateDaoSupport {
	
	private static Logger logger = MiscUtils.getLogger();

	private static final String PLACE_HOLDER_PATTERN = "(\\$){1}(\\{){1}( )*##( )*(\\}){1}";
	private static final String COMMENT_BLOCK_PATTERN = "/\\*(?:.|[\\n\\r])*?\\*/";
	
	
	private static enum requiredParameters { provider }
		private static enum provider { all, loggedinprovider }
	
	// private SecurityInfoManager securityInfoManager = SpringUtils.getBean( SecurityInfoManager.class );
	
	private List<Parameter> parameters;
	private List<RangeInterface> ranges;
	private String query;
	private List<?> resultList;
	private List<Column> columns;
	private LoggedInInfo loggedInInfo;
	
	public AbstractQueryHandler() {
		// default
	}

	public List<?> execute() {

		if( getQuery().isEmpty() ) { 
			logger.error("Failed to execute query.");
			return null;
		} 
		
		Session session = getSession();
		SQLQuery query = session.createSQLQuery( getQuery() );
		List<?> results = query.setResultTransformer( Criteria.ALIAS_TO_ENTITY_MAP ).list();		

		logger.debug("Query results " + results);

		//TODO work on method to detect and exclude demographic files that are 
		// defined in the securityInfoManager object.
		
		setResultList( results );			
		releaseSession(session);

		return getResultList();
	}

	public List<Parameter> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public List<RangeInterface> getRanges() {
		return ranges;
	}
	
	public void setRanges( List<RangeInterface> ranges ) {
		this.ranges = ranges;
	} 
	
	public String getQuery() {
		return query;
	}
	
	protected void setQuery(String query) {
		this.query = query;
	}
	
	public List<?> getResultList() {
		return resultList;
	}

	private void setResultList(List<?> resultList) {
		this.resultList = resultList;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	protected LoggedInInfo getLoggedInInfo() {
		return loggedInInfo;
	}

	public void setLoggedInInfo(LoggedInInfo loggedInInfo) {
		this.loggedInInfo = loggedInInfo;
	}
	
	protected String getLoggedInProvider() {
		String providerNo = "";
		
		if( getLoggedInInfo() != null ) {
			providerNo = getLoggedInInfo().getLoggedInProviderNo(); 
		}
		
		return providerNo;
	}

	/**
	 * Build a final query string with all the place-holders filled in.
	 */
	protected final String buildQuery( final String query ) {

		String queryString = new String( query );
		
		queryString = filterQueryString( queryString );
		
		if( getParameters() != null ) {
			queryString = addParameters( queryString );		
		}
		
		if( getRanges() != null ) {
			queryString = addRanges( queryString );	
		}
		
		if( getColumns() != null ) {
			queryString = addColumns( queryString ); 
		}
		
		return queryString;
	}

	/**
	 * Set the parameter values into the given query string.
	 * Searches the query string for a specific string pattern.
	 * ie: "(\\$){1}(\\{){1}( )* [firstName] ( )*(\\}){1}"
	 */
	protected String addParameters( String query ) {

		for( Parameter parameter : getParameters() ) {			
			String parameterId = parameter.getId();					
			String parameterValue = parseParameterValue( parameter.getValue() );
			
			// set default and predetermined parameter values here.
			parameterValue = getRequiredParameterValue( parameterId, parameterValue );
			parameterId = getPattern( parameterId );			
			query = patternReplace( parameterId, query, parameterValue );
		}
		
		return query;
	}
	
	private String getRequiredParameterValue( String parameterId, String parameterValue ) {

		//TODO for now only captures one required parameter value
		// A switch will be required here to handle more values.
		if( ! ( requiredParameters.provider.name() ).equalsIgnoreCase( parameterId ) ) {			
			return parameterValue;
		}
		
		parameterValue = parameterValue.toLowerCase();
		
		switch( provider.valueOf( parameterValue ) ) {
		case loggedinprovider : parameterValue = getLoggedInProvider().trim();
				break;
		case all : parameterValue = "%";
				break;
		}
		
		return parameterValue;

	}
	
	/**
	 * Set the Range values into the given query string.
	 * Searches the query string for a specific string pattern.
	 * ie: "(\\$){1}(\\{){1}( )* [firstName] ( )*(\\}){1}"
	 */
	protected String addRanges( String query ) {

		for( RangeInterface range : getRanges() ) {	
			
			String addPattern = range.getId().trim();
			
			if( Limit.RangeLowerLimit.name().equals( range.getClass().getSimpleName() ) ) {
				addPattern = RangeType.lowerLimit.name() + "\\." + addPattern;
			} else {
				addPattern = RangeType.upperLimit.name() + "\\." + addPattern;
			}
			
			String pattern = getPattern( addPattern );
			query = patternReplace( pattern, query, range.getValue() );
		}
		
		return query;
	}
	
	/**
	 * The entire select syntax will be rewritten if the column list is set. 
	 */
	protected String addColumns( String queryString ) {
		
		StringBuilder select = new StringBuilder("SELECT ");
		int from = 0;
		
		for( Column column : getColumns() ) {

			select.append( column.getName() );
			select.append(" AS ");
			select.append("'").append( column.getTitle() ).append("',");
		}
		
		select.deleteCharAt( select.length() - 1 );		
		select.append( " " );
		
		logger.debug( "Replacing current select statement with " + select.toString() );

		from = queryString.indexOf("FROM");
		
		if( from < 0 ) {
			from = queryString.indexOf("from");
		} 
		
		if( from < 0 ) {
			from = queryString.indexOf("From");
		}
		
		if( from < 0 ) {
			logger.warn( "Syntax error with the MySQL FROM statement. Syntax permitted is FROM, from or From " );
		}
		
		// remove the current select statement 
		queryString = queryString.substring( from, queryString.length() );		
		queryString = select.toString() + queryString;
		
		logger.debug( "Final query with columns " + queryString );
		
		return queryString;
		
	}
	
	
	/**
	 * Injects a variable pattern value into the predetermine string replacement pattern.
	 * ie: "(\\$){1}(\\{){1}( )* [firstName] ( )*(\\}){1}"
	 */
	private String getPattern( String patternValue ) {
		return new String( PLACE_HOLDER_PATTERN.replace( "##", patternValue.trim() ) );
	}
	
	/*
	 * Replaces all given patterns in the given string with the given value.  
	 */
	private String patternReplace( String pattern, String query, String value ) {		
		logger.debug( "Inserting pattern " + pattern + " with a value of " + value );
		return query.replaceAll( pattern, value );
	}
	
	/**
	 * parses a parameter value array into a comma delimited string for use
	 * in a SQL query.
	 */
	private static String parseParameterValue( String[] values ) {
		String value = "";
		
		if( values.length > 1 ) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append( "(" );				
			for(int i = 0; i < values.length; i++ ) {
				stringBuilder.append("'").append(values[i]).append("',");
			}
			stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
			stringBuilder.append( ")" );
			value = stringBuilder.toString();
		} else {
			value = values[0].trim();
		}
		
		return value;
	}
	
	/**
	 * Removes pesky colons, question marks, and comments from the 
	 * query string. 
	 */
	protected static final String filterQueryString( String queryString ) {
				
		// Remove comment blocks
		String query = queryString.replaceAll( COMMENT_BLOCK_PATTERN, "" );		
		String[] lines = query.split( "\\n" );
		StringBuilder stringBuilder = new StringBuilder("");
		
		for( String line : lines ) {
			
			line = line.trim();
			
			if( ! line.startsWith( "--" ) && ! line.isEmpty() && ! line.startsWith( "#" ) ) {
				
				line = line.replaceAll("\\?", "");				
				line = line.replaceAll("\\:", "");
				
				logger.debug( "Query line: " + line );
				
				stringBuilder.append( line );
				stringBuilder.append(" ");
			}			
		}
		stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
		
		return stringBuilder.toString();
	}
}
