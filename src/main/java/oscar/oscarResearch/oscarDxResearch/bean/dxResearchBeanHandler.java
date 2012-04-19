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


package oscar.oscarResearch.oscarDxResearch.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarResearch.oscarDxResearch.util.dxResearchCodingSystem;

public class dxResearchBeanHandler {

    Vector<dxResearchBean> dxResearchBeanVector = new Vector<dxResearchBean>();

    public dxResearchBeanHandler(String demographicNo) {
        init(demographicNo);
    }

    public boolean init(String demographicNo) {

        boolean verdict = true;
        try {


            dxResearchCodingSystem codingSys = new dxResearchCodingSystem();
            String[] codingSystems = codingSys.getCodingSystems();

            String sql;
            ResultSet rs;
            for( int idx = 0; idx < codingSystems.length; ++idx ) {
                String codingSystem = codingSystems[idx];
                sql = "select d.start_date, d.update_date, c.description, c."+codingSystem+", d.dxresearch_no, d.status from dxresearch d, "+codingSystem+" c " +
                         "where d.dxresearch_code=c."+codingSystem+" and d.status<>'D' and d.demographic_no ='"+ demographicNo +"' and d.coding_system = '"+codingSystem+"'"
                        +" order by d.start_date desc, d.update_date desc";
                for(rs = DBHandler.GetSQL(sql); rs.next(); )
                {
                    dxResearchBean bean = new dxResearchBean(   oscar.Misc.getString(rs, "description"),
                                                            oscar.Misc.getString(rs, "dxresearch_no"),
                                                            oscar.Misc.getString(rs, codingSystem),
                                                            oscar.Misc.getString(rs, "update_date"),
                                                            oscar.Misc.getString(rs, "start_date"),
                                                            oscar.Misc.getString(rs, "status"),
                                                            codingSystem);
                    dxResearchBeanVector.add(bean);

                }
                rs.close();
            }
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }

    public Vector<dxResearchBean> getDxResearchBeanVector(){
        return dxResearchBeanVector;
    }

    public Vector<String> getActiveCodeList(){ //TODO: NEED TO CHECK STATUS
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < dxResearchBeanVector.size(); i++){
            dxResearchBean dx = dxResearchBeanVector.get(i);
            if( !v.contains(dx.getDxSearchCode())){
               v.add(dx.getDxSearchCode());
            }
        }
        return v;
    }


    public Vector<String> getActiveCodeListWithCodingSystem(){ //TODO: NEED TO CHECK STATUS
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < dxResearchBeanVector.size(); i++){
            dxResearchBean dx = dxResearchBeanVector.get(i);
            if( !v.contains(dx.getDxSearchCode())){
               v.add(dx.getType()+":"+dx.getDxSearchCode());
            }
        }
        return v;
    }

}
