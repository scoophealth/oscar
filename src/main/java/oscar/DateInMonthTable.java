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


package oscar;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Martin
 * prepare to be a calendar Table Grid bean
 * @version  1.0
 * @since    JDK 1.3
 */

public class DateInMonthTable {
	private  int curYear=0;
	private  int curMonth=0;
	private  int curDay=0;
	GregorianCalendar calendar=null;

	public DateInMonthTable() {
		GregorianCalendar now=new GregorianCalendar();
		curYear=now.get(Calendar.YEAR);
		curMonth=now.get(Calendar.MONTH);
		curDay=now.get(Calendar.DAY_OF_MONTH);
	}
	public DateInMonthTable(int year, int month, int day) {
		curYear=year;
		curMonth=month;
		curDay=day;
	}
	
	public int getCurYear() { return curYear; }
	public int getCurMonth() { return curMonth; }
	public int getCurDay() { return curDay; }
	
	public int[][] getMonthDateGrid() {
		int lastDay=0;
		int rows=0, cols=0;
		int [][] monthDateGrid=null;

    for(lastDay=31; lastDay>26; lastDay--) {
			calendar=new GregorianCalendar(curYear,curMonth,lastDay);
			if(calendar.get(Calendar.MONTH)!=curMonth) continue;
			else break;
		}
		rows=calendar.get(Calendar.WEEK_OF_MONTH);
		cols=7;
		monthDateGrid=new int [rows][cols];

		calendar=new GregorianCalendar(curYear,curMonth, 1);


		int firstDayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
    int day=1;
    for(int row=0; row<rows; row++) {
    	for(int col=0; col<cols; col++) {
    		if(col<firstDayOfWeek-1) {
    			monthDateGrid[row][col]=0;

    		} else {
					firstDayOfWeek=0;//don't need this first week data anymore
    			if(day<=lastDay) {
    				monthDateGrid[row][col]=day;
    				day++;

					} else {
    				monthDateGrid[row][col]=0;

					}
				}				
    	}
    }
    return monthDateGrid;
	}
}
