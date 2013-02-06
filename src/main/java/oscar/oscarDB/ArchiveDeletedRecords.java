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

import java.util.List;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;
import org.oscarehr.common.dao.TableModificationDao;
import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.oscarehr.common.model.TableModification;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

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
    
    private String getStringXmlFromResultSet(ProviderLabRoutingModel record) throws Exception{
       ResultSetBuilder builder = new ResultSetBuilder(record);
       Document doc = builder.build();
       XMLOutputter xml = new XMLOutputter();
       String xmlStr = xml.outputString(doc); 
       return xmlStr;     
    }
    
    public int recordRowsToBeDeleted(List<ProviderLabRoutingModel> records, String provNo,String table){
        try {
            for(ProviderLabRoutingModel record : records) {
            	String xmlStr = getStringXmlFromResultSet(record);
                addRowsToModifiedTable(null,provNo,ArchiveDeletedRecords.DELETE, table, null, xmlStr);
            }
            
        }
        catch(Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return 0;
    }
    
    private void addRowsToModifiedTable(String demoNo,String provNo,String modType,String table,String rowId,String resultSet){
    	TableModification tm = new TableModification();
    	tm.setDemographicNo(demoNo != null ? Integer.parseInt(demoNo) : null);
    	tm.setProviderNo(provNo);
    	tm.setModificationType(modType);
    	tm.setTableName(table);
    	tm.setRowId(rowId);
    	tm.setResultSet(resultSet);
    	
    	TableModificationDao dao = SpringUtils.getBean(TableModificationDao.class);
    	dao.persist(tm);
    	
    	MiscUtils.getLogger().debug("Added rows to modified table: " + tm);
    }
    
}
