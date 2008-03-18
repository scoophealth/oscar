/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of EfmData
 *
 *
 * EfmData.java
 *
 * Created on July 28, 2005, 1:54 PM
 */
package oscar.eform.data;

import java.util.Hashtable;

import oscar.eform.EFormUtil;

public class EFormCheck extends EFormBase {
    
    public EFormCheck() {
    }
    
    public EFormCheck(String fid) {
        loadEForm(fid);
    }
    
    public void loadEForm(String fid) {
        Hashtable loaded = (Hashtable) EFormUtil.loadEForm(fid);
        fid = (String) loaded.get("fid");
        formName = (String) loaded.get("formName");
        formHtml = (String) loaded.get("formHtml");
    }
    
}
