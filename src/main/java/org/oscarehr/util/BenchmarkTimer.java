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

package org.oscarehr.util;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkTimer {
	long startTime;
	String name;
	List<PointInTime> pointsInTime = new ArrayList<PointInTime>();
	
	public BenchmarkTimer(String timerName){
		name = timerName;
		startTime = System.currentTimeMillis();
	}
	
	public void tag(String tagName){
		PointInTime pointInTime = new PointInTime();
		pointInTime.name = tagName;
		pointInTime.time = System.currentTimeMillis();
		pointsInTime.add(pointInTime);
	}
	
	public String report(){
		StringBuilder sb = new StringBuilder();
		sb.append("Timer: "+name);
		long lastTimer = startTime;
		for(PointInTime pt: pointsInTime){
			long timeSinceLastTimer = pt.time - lastTimer;
			long timeSinceStart = pt.time -startTime;
			sb.append(" "+pt.name+":"+timeSinceLastTimer+"/"+timeSinceStart);
			lastTimer=pt.time;
		}
		sb.append(" Total "+(System.currentTimeMillis()-startTime));
		return sb.toString();
	}
	
	private class PointInTime{
		public long time;
		public String name;
	}

}
