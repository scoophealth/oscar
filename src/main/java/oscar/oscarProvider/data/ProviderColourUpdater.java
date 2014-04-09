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


package oscar.oscarProvider.data;

import java.util.List;

import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.Property;
import org.oscarehr.util.SpringUtils;

/**
 * Manages Fax number for provider 
 * 
 */
public class ProviderColourUpdater {
    
    private String strColName;    
    private String provider;
    
    /** Creates a new instance of ProviderColourUpdater */
   public ProviderColourUpdater(String p) {
       strColName = "ProviderColour";
       provider = p;       
    }
   
   /**
    *Retrieve colour for current provider first by querying property table 
    */
   public String getColour() {
       PropertyDao dao = SpringUtils.getBean(PropertyDao.class);
	   List<Property> props = dao.findByNameAndProvider(strColName, provider);
       for(Property prop : props) {
            return prop.getValue()!=null?prop.getValue():"";
       }
       return "";
   }
      /**
       *set colour in property table
       */
   public boolean setColour(String c) {
	   PropertyDao dao = SpringUtils.getBean(PropertyDao.class);
	   List<Property> props = dao.findByNameAndProvider(strColName, provider);
	   Property property = null;
	   for(Property p : props) {
           property = p;
           break;
      }
	   
	  if (property == null) {
		  property = new Property();
	  }
	   
	  property.setValue(c);
	  property.setName(strColName);
	  property.setProviderNo(provider);
	  
	  dao.saveEntity(property);
	   
	  return true;
   }
}
