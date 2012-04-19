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


package oscar.oscarRx.util;

import java.util.Comparator;

import org.oscarehr.common.model.Drug;

public class ShowAllSorter implements Comparator<Drug> {

	//current, current but will expire, expired, discontinued
	public int compare(Drug a,Drug b) {
		
		long now = System.currentTimeMillis();
        long month = 1000L * 60L * 60L * 24L * 30L;
        
        
		//current
		if (!a.isExpired() && !a.isArchived()) {
			if(!b.isExpired() && b.getEndDate()!=null && (b.getEndDate().getTime() - now <= month)) {
				return -1;
			}
			if(b.isExpired() || b.isArchived() || b.isDiscontinued()) {
				return -1;
			}
		}
		
		//current but will expire
		if (!a.isExpired() && a.getEndDate()!=null && (a.getEndDate().getTime() - now <= month)) { 
            if(b.isExpired() || b.isDiscontinued()) {
            	return -1;
            }
        }
		
		//expired
		if(a.isExpired() && b.isDiscontinued()) {
			return -1;
		}
		
		//discontinued
		
		
		if(a.isDiscontinued() && !b.isDiscontinued()) {
			return 1;
		}
		
		if(a.isExpired()) {
			if(!b.isExpired() && b.getEndDate()!=null && (b.getEndDate().getTime() - now <= month)) {
				return 1;
			}
			if(!b.isExpired() && !b.isArchived()) {
				return 1;
			}			
		}
		
		if(!a.isExpired() && a.getEndDate()!=null && (a.getEndDate().getTime() - now <= month)) {
			if(!b.isExpired() && !b.isArchived()) {
				return 1;
			}
		}				
		
		//all other are the same	
		return 0;
	}
}
