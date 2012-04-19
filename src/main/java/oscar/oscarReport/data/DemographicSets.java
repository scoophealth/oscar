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


package oscar.oscarReport.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.common.dao.DemographicSetsDao;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author Jay Gallagher
 *
   create table demographicSets (
      demographic_no int(10),
      set_name varchar(20),
      eligibility char(1)
   );
 */

public class DemographicSets {

	private DemographicSetsDao demographicSetsDao = SpringUtils.getBean(DemographicSetsDao.class);


   public DemographicSets() {
   }

   public void addDemographicSet(String setName, String demographic){
       List<String> list = new ArrayList<String>();
       list.add(demographic);
       addDemographicSet(setName,list);
   }

   public void addDemographicSet(String setName, List<String> demoList){

	   for (int i = 0; i < demoList.size(); i++){
		   String demographicNo = demoList.get(i);
		   org.oscarehr.common.model.DemographicSets ds = new org.oscarehr.common.model.DemographicSets();
		   ds.setName(setName);
		   ds.setDemographicNo(Integer.parseInt(demographicNo));
		   ds.setArchive("0");
		   demographicSetsDao.persist(ds);
	   }

   }


   public List<String> getDemographicSet(String setName){
      List<String> retval = new ArrayList<String>();
      List<org.oscarehr.common.model.DemographicSets> dss = demographicSetsDao.findBySetName(setName);
      for(org.oscarehr.common.model.DemographicSets ds:dss) {
    	  retval.add(String.valueOf(ds.getDemographicNo()));
      }
      return retval;
   }

   public List<String> getDemographicSet(List<String> setNames) {
       List<String> retval = new ArrayList<String>();

       List<org.oscarehr.common.model.DemographicSets> dss = demographicSetsDao.findBySetNames(setNames);
       for(org.oscarehr.common.model.DemographicSets ds:dss) {
    	   retval.add(String.valueOf(ds.getDemographicNo()));
       }

      return retval;
   }

   public List<Map<String,String>> getDemographicSetExt(String setName){
	   List<Map<String,String>> retval = new ArrayList<Map<String,String>>();

      List<org.oscarehr.common.model.DemographicSets> dss = demographicSetsDao.findBySetName(setName);
      for(org.oscarehr.common.model.DemographicSets ds:dss) {
    	  Map<String,String> h = new HashMap<String,String>();
          h.put("demographic_no",String.valueOf(ds.getDemographicNo()));
          String el = ds.getEligibility();
          if (el == null || el.equalsIgnoreCase("null")){
             el = "0";
          }
          h.put("eligibility",el);
          retval.add(h);
      }

      return retval;
   }


   public List<String> getIneligibleDemographicSet(String setName){
      List<String> retval = new ArrayList<String>();
      List<org.oscarehr.common.model.DemographicSets> dss = demographicSetsDao.findBySetNameAndEligibility(setName, "1");
      for(org.oscarehr.common.model.DemographicSets ds:dss) {
    	  retval.add(String.valueOf(ds.getDemographicNo()));
      }
      return retval;
   }

   public List<String> getEligibleDemographicSet(String setName){
	      List<String> retval = new ArrayList<String>();
	      List<org.oscarehr.common.model.DemographicSets> dss = demographicSetsDao.findBySetNameAndEligibility(setName, "0");
	      for(org.oscarehr.common.model.DemographicSets ds:dss) {
	    	  retval.add(String.valueOf(ds.getDemographicNo()));
	      }
	      return retval;
   }


   public List<String> setDemographicIneligible(String setName,String demoNo){
      List<String> retval = new ArrayList<String>();
      List<org.oscarehr.common.model.DemographicSets> dss = demographicSetsDao.findBySetNameAndDemographicNo(setName,Integer.parseInt(demoNo));
      for(org.oscarehr.common.model.DemographicSets ds:dss) {
    	  ds.setEligibility("1");
    	  demographicSetsDao.merge(ds);
      }
      return retval;
   }

   public List<String> setDemographicDelete(String setName,String demoNo){
      List<String> retval = new ArrayList<String>();
      List<org.oscarehr.common.model.DemographicSets> dss = demographicSetsDao.findBySetNameAndDemographicNo(setName,Integer.parseInt(demoNo));
      for(org.oscarehr.common.model.DemographicSets ds:dss) {
    	  ds.setArchive("1");
    	  demographicSetsDao.merge(ds);
      }
      return retval;
   }


   public List<String> getDemographicSets(String demoNo) {
	  return demographicSetsDao.findSetNamesByDemographicNo(Integer.parseInt(demoNo));
   }


   public List<String> getDemographicSets(){
	   return demographicSetsDao.findSetNames();
   }
}
