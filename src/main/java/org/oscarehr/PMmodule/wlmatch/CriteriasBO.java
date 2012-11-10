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
package org.oscarehr.PMmodule.wlmatch;

public class CriteriasBO {

	public int vacancyID=0;
	public String[] allFields=null;
	public String[] mandatoryFields=null;
	public String[] otherFields=null;
	public CriteriaBO[] crits=null;
	
	public double totalMandatoryScore=0;
	public double totalMaxScore=0;
	
	public void matchAndScoreForAllFields(PotentialMatchBO pot){
		pot.score=0;
		for (CriteriaBO crit : crits) {
			pot.score=pot.score + crit.score(pot);
		}
	}
	public String getSummary(){
		String out="";
		for(CriteriaBO crit:crits){
			if(crit.value!=null && crit.value.length() >0){
				out+=" AND " + (crit.field+"='"+crit.value+"'");
			}else if(crit.rangeStart!=null){
				out+=(crit.field+" is between "+crit.rangeStart+" and "+crit.rangeEnd);
			}else if(crit.values!=null){
				if(crit.values!=null && crit.values.length>1){
					String list="";
					for(int i=0;i<crit.values.length;i++){
						if(i>0) list+=",";
						list+="'"+crit.values[i]+"'";
					}
					out+=" AND "+(crit.field+" is one of "+list);
				}else if(crit.values.length==1){
					out+=" AND " + (crit.field+"='"+crit.values[0]+"'");
				}
			}
		}
		return out;
	}
}
