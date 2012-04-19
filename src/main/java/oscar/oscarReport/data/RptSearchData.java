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


import java.util.List;

import org.oscarehr.common.dao.DemographicQueryFavouritesDao;
import org.oscarehr.common.model.DemographicQueryFavourite;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
/**
 *
 * @author  McMaster
 */
public class RptSearchData {

    java.util.ArrayList rosterTypes;
    java.util.ArrayList patientTypes;
    java.util.ArrayList savedQueries;

    private DemographicQueryFavouritesDao demographicQueryFavouritesDao = SpringUtils.getBean(DemographicQueryFavouritesDao.class);

    /**
     *This function runs through the demographic table and retrieves all the roster types currently being used
     * @return  ArrayList  of roster status types in the demographic table*/
    public java.util.ArrayList getRosterTypes(){
            java.util.ArrayList retval = new java.util.ArrayList();
         try{

              java.sql.ResultSet rs;
              rs = DBHandler.GetSQL("select distinct roster_status from demographic where roster_status is not null order by roster_status");

              while (rs.next()) {
                retval.add( oscar.Misc.getString(rs, "roster_status") );

              }
              rs.close();
            }catch (java.sql.SQLException e){ MiscUtils.getLogger().error("Error", e); }
            return retval;
    }

    /**
     * @return  */
    public java.util.ArrayList getPatientTypes(){
            java.util.ArrayList retval = new java.util.ArrayList();
         try{

              java.sql.ResultSet rs;
              rs = DBHandler.GetSQL("select distinct patient_status from demographic where patient_status is not null order by patient_status");

              while (rs.next()) {
                retval.add( oscar.Misc.getString(rs, "patient_status") );

              }
              rs.close();
            }catch (java.sql.SQLException e){ MiscUtils.getLogger().error("Error", e); }
            return retval;
    }

    public java.util.ArrayList getProvidersWithDemographics(){
            java.util.ArrayList retval = new java.util.ArrayList();
         try{

              java.sql.ResultSet rs;
              rs = DBHandler.GetSQL("select distinct provider_no from demographic order by provider_no");

              while (rs.next()) {
                retval.add( oscar.Misc.getString(rs, "provider_no") );

              }
              rs.close();
            }catch (java.sql.SQLException e){ MiscUtils.getLogger().error("Error", e); }
            return retval;
    }

    public java.util.ArrayList getQueryTypes(){
            java.util.ArrayList retval = new java.util.ArrayList();
            List<DemographicQueryFavourite> results = demographicQueryFavouritesDao.findByArchived("1");
            for(DemographicQueryFavourite result:results) {
            	SearchCriteria sc = new SearchCriteria();
                sc.id = String.valueOf(result.getId());
                sc.queryName = result.getQueryName();

                retval.add( sc );
            }

            return retval;
    }


    public void deleteQueryFavourite(String id){
       try{

          DBHandler.RunSQL("update demographicQueryFavourites set archived = '0' where favId = '"+id+"'");
       }catch (java.sql.SQLException e){
           MiscUtils.getLogger().error("Error", e);
       }
    }



public class SearchCriteria {
    public String id;
    public String queryName;
};
}
