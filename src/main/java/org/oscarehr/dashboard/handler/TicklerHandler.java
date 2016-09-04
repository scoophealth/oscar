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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import net.sf.json.JSONArray;


public class TicklerHandler {

	private static Logger logger = MiscUtils.getLogger();
	private List<Tickler> ticklerList;
	private TicklerManager ticklerManager;
	private Tickler masterTickler;
	private Integer[] demographicArray;
	private LoggedInInfo loggedinInfo;

	public TicklerHandler(LoggedInInfo loggedinInfo, TicklerManager ticklerManager ) {
		this.loggedinInfo = loggedinInfo;
		this.ticklerManager = ticklerManager;
	}

	/**
	 * Expects a map of parameters with keys: 
	 * 	String message
	 *	String priority
	 *	String serviceDate
	 *	String taskAssignedTo
	 *	String ticklerCategoryId
	 *  
	 */
	public void createMasterTickler( Map<String,Object[]> ticklerParameters ) {

		Tickler tickler = new Tickler();
		Date serviceDate = null;
		Integer ticklerCategoryId;
		Tickler.PRIORITY priority;

		try {
			serviceDate = getDateTime( ( (String[]) ticklerParameters.get( "serviceDate" ) )[0], 
					( (String[]) ticklerParameters.get( "serviceTime" ) )[0] );			
		} catch (ParseException e) {
			logger.error( "Failed to parse date string: " + ( (String[]) ticklerParameters.get( "serviceDate" ) )[0] , e );
		}

		ticklerCategoryId = Integer.parseInt(  ( (String[]) ticklerParameters.get( "ticklerCategoryId" ) )[0] );
		priority = Tickler.PRIORITY.valueOf( ( (String[]) ticklerParameters.get( "priority" ) )[0] );

		tickler.setMessage( ( (String[]) ticklerParameters.get( "message" ) )[0] 
				+ " " 
				+ ( (String[]) ticklerParameters.get( "messageAppend" ) )[0] ) ;
		tickler.setPriority( priority );
		tickler.setServiceDate( serviceDate );
		tickler.setTaskAssignedTo( ( (String[]) ticklerParameters.get( "taskAssignedTo" ) )[0] );
		tickler.setCategoryId( ticklerCategoryId );
		tickler.setStatus( Tickler.STATUS.A ); // this method creates only new Active Ticklers.
		tickler.setCreator( getLoggedinInfo().getLoggedInProviderNo() );

		setMasterTickler( tickler );
	}

	/**
	 * Adds a copy of the master tickler to each demographic id in the given Collection.
	 */
	public boolean addTickler( Integer[] demographicArray ) {

		if( demographicArray == null || demographicArray.length == 0 ) {
			return Boolean.FALSE;
		}

		boolean success = Boolean.TRUE;
		int i = 0;

		while( success && i < demographicArray.length ) {

			if( getTicklerList() == null ) {
				setTicklerList( new ArrayList<Tickler>() );
			}

			Integer demographicNo = demographicArray[i];
			Tickler ticklerCopy = new Tickler();

			try {
				BeanUtils.copyProperties( ticklerCopy, getMasterTickler() );
			} catch (Exception e) {
				MiscUtils.getLogger().error("Failed to create Tickler for demographicNo: " + demographicNo, e);
				success = Boolean.FALSE;
			} 

			ticklerCopy.setId( null );
			ticklerCopy.setDemographicNo( demographicNo );	

			success = getTicklerList().add( ticklerCopy );

			i++;
		}

		if( ! success ) {
			getTicklerList().clear();
			setTicklerList( null );
		}

		return addTickler( getTicklerList() );
	}

	/**
	 * Persist an entire list of Ticklers
	 */
	public boolean addTickler( List<Tickler> ticklerList ) {

		if( ticklerList == null || ticklerList.isEmpty() ) {
			return Boolean.FALSE;
		}

		Boolean success = Boolean.TRUE;

		Iterator<Tickler> it = ticklerList.iterator();
		while( success && it.hasNext() ) {
			Tickler tickler = it.next();
			success = addTickler( tickler );
			logger.info("Added new Tickler Id " + tickler.getId() + " for demographic id " + tickler.getDemographicNo() );
		}

		return success;
	}

	/**
	 * Persist an individual Tickler
	 */
	public boolean addTickler( Tickler tickler ) {

		if( getLoggedinInfo().getLoggedInSecurity() == null ) {
			return Boolean.FALSE;
		}

		return getTicklerManager().addTickler( getLoggedinInfo(), tickler );
	}

	/**
	 * Adds a copy of the master tickler to each demographic id in the given JSON Array String.
	 */
	public boolean addTickler( String jsonString ) {

		if( jsonString == null || jsonString.isEmpty() ) {
			return Boolean.FALSE;
		}
		
		if( ! jsonString.startsWith("[")) {
			jsonString = "[" + jsonString;
		}
		
		if( ! jsonString.endsWith("]")) {
			jsonString = jsonString + "]";
		}
		
		JSONArray jsonArray = JSONArray.fromObject( jsonString );
		
		Integer arraySize = jsonArray.size();
		Integer[] demographicArray = new Integer[ arraySize ];

		for( int i = 0; i < arraySize; i++ ) {
			demographicArray[i] = jsonArray.getInt(i);
		}

		setDemographicArray( demographicArray );

		return addTickler( demographicArray );
	}

	public List<Tickler> getTicklerList() {
		return ticklerList;
	}

	public void setTicklerList(List<Tickler> ticklerList) {
		this.ticklerList = ticklerList;
	}

	public TicklerManager getTicklerManager() {
		return ticklerManager;
	}

	public void setTicklerManager(TicklerManager ticklerManager) {
		this.ticklerManager = ticklerManager;
	}

	public Tickler getMasterTickler() {
		return masterTickler;
	}

	public void setMasterTickler(Tickler masterTickler) {
		this.masterTickler = masterTickler;
	}

	public Integer[] getDemographicArray() {
		return demographicArray;
	}

	public void setDemographicArray(Integer[] demographicArray) {
		this.demographicArray = demographicArray;
	}

	public LoggedInInfo getLoggedinInfo() {
		return loggedinInfo;
	}

	public void setLoggedinInfo(LoggedInInfo loggedinInfo) {
		this.loggedinInfo = loggedinInfo;
	}
	
	private static Date getDateTime( String dateString, String timeString ) throws ParseException {
		
		if( dateString == null || timeString == null ) {
			return null;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( Tickler.DATE_FORMAT );
		SimpleDateFormat timeFormat = new SimpleDateFormat( Tickler.TIME_FORMAT );
		Date time = timeFormat.parse( timeString );
		Date date = dateFormat.parse( dateString );
		Calendar calendarTime = Calendar.getInstance();
		calendarTime.setTime( time );
		Calendar calendarDate = Calendar.getInstance();
		calendarDate.setTime( date );
		calendarDate.set( Calendar.HOUR, calendarTime.get( Calendar.HOUR ) );
		calendarDate.set( Calendar.MINUTE, calendarTime.get( Calendar.MINUTE ) );
		calendarDate.set( Calendar.AM_PM, calendarTime.get( Calendar.AM_PM ) );

		return calendarDate.getTime();
	}

}
