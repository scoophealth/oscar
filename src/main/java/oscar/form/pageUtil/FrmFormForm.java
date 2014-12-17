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

package oscar.form.pageUtil;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;
import org.oscarehr.util.MiscUtils;

/*
 * @Author: Ivy Chan
 * @Company: iConcept Technologes Inc. 
 * @Created on: October 31, 2004
 */
public final class FrmFormForm extends ActionForm {
 
   public FrmFormForm(){
      MiscUtils.getLogger().debug("FrmFormForm gets instantiated");
      MiscUtils.getLogger().debug("FrmFormForm currentMem = "+currentMem());
   }
    //Using map-backed method to get the value of each field
    //key: the field property
    //value: the value of the associated key
    private Map values = new HashMap();

    public void setValue(String key, Object value) {
       MiscUtils.getLogger().debug("adding key "+key+" value "+value+" Size of FrmFormForm "+values.size());
        values.put(key, value);
    }

    public Object getValue(String key) {        
        return values.get(key);
    }
    

    public String currentMem(){        
       long total = Runtime.getRuntime().totalMemory();
       long free  = Runtime.getRuntime().freeMemory();
       long Used = total -  free;
       return "Total "+total+" Free "+free+" USED "+Used;
    }
}
