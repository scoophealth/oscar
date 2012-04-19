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


package org.oscarehr.web.reports.ocan.beans;

import java.util.Comparator;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class OcanDomainConsumerStaffBean {
	private int domainId;
	private String consumerNeed;
	private String staffNeed;

	public OcanDomainConsumerStaffBean() {

	}

	public OcanDomainConsumerStaffBean(int domainId, String consumerNeed, String staffNeed) {
		this.domainId = domainId;
		this.consumerNeed = consumerNeed;
		this.staffNeed = staffNeed;
	}

	public int getDomainId() {
		return domainId;
	}

	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}

	public String getConsumerNeed() {
		return consumerNeed;
	}

	public void setConsumerNeed(String consumerNeed) {
		this.consumerNeed = consumerNeed;
	}

	public String getStaffNeed() {
		return staffNeed;
	}

	public void setStaffNeed(String staffNeed) {
		this.staffNeed = staffNeed;
	}


	public String toString() {
		return(ReflectionToStringBuilder.toString(this));
	}

	@Override
	public boolean equals(Object obj) {
		OcanDomainConsumerStaffBean b = (OcanDomainConsumerStaffBean)obj;
		if(b.getDomainId() == this.getDomainId()) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getDomainId();
	}

	public boolean isInAgreement() {
		String rating1 = getConsumerNeed();
		String rating2 = getStaffNeed();

    	if(rating1==null || rating2==null)
    		return false;
 	   return (rating1.equals(rating2));
    }

   public String getBestRating() {
	   String rating1 = getConsumerNeed();
	   String rating2 = getStaffNeed();
	   rating1 = (rating1==null)?"9":rating1;
	   rating2 = (rating2==null)?"9":rating2;
	   if(rating1.equals("2")||rating2.equals("2"))
		   return "2";
	   if(rating1.equals("1")||rating2.equals("1"))
		   return "1";
	   if(rating1.equals("0")||rating2.equals("0"))
		   return "0";

	   return "9";
   }

   public Integer getOrderedConsumerNeedRating() {
	   if(getConsumerNeed()==null) {
		   return 0;
	   }
	   if(getConsumerNeed().equals("2")) return 3;
	   if(getConsumerNeed().equals("1")) return 2;
	   if(getConsumerNeed().equals("0")) return 1;
	   if(getConsumerNeed().equals("9")) return 0;
	   return 0;
   }

   public Integer getOrderedStaffNeedRating() {
	   if(getStaffNeed()==null) {
		   return 0;
	   }
	   if(getStaffNeed().equals("2")) return 3;
	   if(getStaffNeed().equals("1")) return 2;
	   if(getStaffNeed().equals("0")) return 1;
	   if(getStaffNeed().equals("9")) return 0;
	   return 0;
   }

   public Integer getOrderedBestNeedRating() {
	   if(getBestRating()==null) {
		   return 0;
	   }
	   if(getBestRating().equals("2")) return 3;
	   if(getBestRating().equals("1")) return 2;
	   if(getBestRating().equals("0")) return 1;
	   if(getBestRating().equals("9")) return 0;
	   return 0;
   }

	/**
	 * This is the comparator for sorting a set of domains based on the current OCAN. We're basically
	 * telling it whether it's higher or lower row in the report.
	 *
	 * RULES
	 * -----
	 * 1) Order by the grouping of needs in the following order
	 *    Unmet Needs
	 *    Met Needs
	 *    No Needs
	 *    Unknown
	 *
	 *  2) If need rating is the same for Consumer and Staff, there is an "Agreement". Agreements
	 *     should be ordered at the top of the need rating.
	 *
	 *  3) If there is a "Disagreement", then the higher need rating will be taken into account.
	 */

   public static Comparator<OcanDomainConsumerStaffBean> getNeedsComparator1() {
	   return new Comparator<OcanDomainConsumerStaffBean>() {
           public int compare(OcanDomainConsumerStaffBean b1, OcanDomainConsumerStaffBean b2) {
        	   if(b1 == null || b2 == null) {
           		return 0;
        	   }

        	   int result1 = b1.getOrderedBestNeedRating().compareTo(b2.getOrderedBestNeedRating());

        	   int result2 = b1.getOrderedConsumerNeedRating().compareTo(b2.getOrderedConsumerNeedRating());

        	   int result3 = b1.getOrderedStaffNeedRating().compareTo(b2.getOrderedStaffNeedRating());

        	   if(result1 == 0 && result2 == 0) {
        		   return result3;
        	   }
        	   if(result1 == 0) {
        		   return result2;
        	   }

        	   return result1;
           }
	   };
   }

}
