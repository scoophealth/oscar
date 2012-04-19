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


package oscar.oscarDB;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;
import org.oscarehr.util.MiscUtils;

/**
 * This class is used to archive deleted or updated rows that won't be used again.
 * 
 *
   CREATE TABLE `table_modification` (
     `id` int(10) NOT NULL auto_increment primary key,
     `demographic_no` int(10) NOT NULL default '0',
     `provider_no` varchar(6) NOT NULL default '',
     `modification_date` datetime default NULL,     
     `modification_type` varchar(20) default NULL,
     `table_name` varchar(255) default NULL,
     `row_id` varchar(20) default NULL,
     `resultSet` text,
      KEY `table_modification_demographic_no` (`demographic_no`),
      KEY `table_modification_provider_no` (`provider_no`),
      KEY `table_modification_modification_type` (`modification_type`(10))
  );
   
  
 * 
 * @author Jay Gallagher
 */
public class ArchiveDeletedRecords {
    static String DELETE = "delete";
    static String UPDATE = "update";
    
    /**
     * Creates a new instance of ArchiveDeletedRecords 
     */
    public ArchiveDeletedRecords() {
    }
    
    private String getStringXmlFromResultSet(ResultSet rs) throws Exception{
       ResultSetBuilder builder = new ResultSetBuilder(rs);
       Document doc = builder.build();
       XMLOutputter xml = new XMLOutputter();
       String xmlStr = xml.outputString(doc); 
       return xmlStr;     
    }
    
    
    public int recordRowsToBeDeleted(String sql,String provNo,String table){
        try {
               
            ResultSet rs = DBHandler.GetSQL(sql);            
            if ( rs.next()){
               String xmlStr = getStringXmlFromResultSet(rs);
               addRowsToModifiedTable(null,provNo,ArchiveDeletedRecords.DELETE,table,null,xmlStr);
            }
            rs.close();
        }
        catch(Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return 0;
    }
    
    private void addRowsToModifiedTable(String demoNo,String provNo,String modType,String table,String rowId,String resultSet){
        try {
               
            String insertSql = "insert into table_modification (demographic_no,provider_no,modification_type,table_name,row_id,resultSet,modification_date) " +
                               " values ('"+StringEscapeUtils.escapeSql(demoNo)+"', " +
                               " '"+StringEscapeUtils.escapeSql(provNo)+"', " +
                               " '"+StringEscapeUtils.escapeSql(modType)+"', " +
                               " '"+StringEscapeUtils.escapeSql(table)+"', " +
                               " '"+StringEscapeUtils.escapeSql(rowId)+"', " +
                               " '"+StringEscapeUtils.escapeSql(resultSet)+"', " +                               
                               "  now()" +
                               ")";            
            MiscUtils.getLogger().debug(insertSql);
            DBHandler.RunSQL(insertSql);
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }        
    }
    
}
