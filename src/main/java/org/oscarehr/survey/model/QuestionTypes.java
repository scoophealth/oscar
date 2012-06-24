/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.survey.model;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

public class QuestionTypes extends ArrayList<LabelValueBean> {

	public static final String RANK = "Rank";
	public static final String DATE = "Date";
	public static final String SELECT = "Select";
	public static final String SCALE = "Rank";
	public static final String OPEN_ENDED = "Open Ended";

    public QuestionTypes() {
    	//this.add(new LabelValueBean(RANK, RANK));
    	this.add(new LabelValueBean(DATE, DATE));
    	this.add(new LabelValueBean(SELECT, SELECT));
    	//this.add(new LabelValueBean(SCALE, SCALE));
    	this.add(new LabelValueBean(OPEN_ENDED, OPEN_ENDED));
    }

}
