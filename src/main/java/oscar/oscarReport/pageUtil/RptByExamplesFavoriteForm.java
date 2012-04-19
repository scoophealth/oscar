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


package oscar.oscarReport.pageUtil;

import org.apache.struts.action.ActionForm;

public final class RptByExamplesFavoriteForm extends ActionForm {

    String favoriteName="";
    String query;
    String newQuery;
    String newName;
    String toDelete;
    String id;

    public String getFavoriteName(){
       return favoriteName;
    }

    public void setFavoriteName(String favoriteName){
       this.favoriteName = favoriteName;
    }
    
    public String getQuery(){
       return query;
    }

    public void setQuery(String query){
       this.query = query;
    }
    
    public String getNewQuery(){
       return newQuery;
    }

    public void setNewQuery(String newQuery){
       this.newQuery = newQuery;
    }
    
    public String getNewName(){
       return newName;
    }

    public void setNewName(String newName){
       this.newName = newName;
    }
    
    public String getToDelete(){
       return toDelete;
    }

    public void setToDelete(String toDelete){
       this.toDelete = toDelete;
    }
    
    public String getId(){
       return id;
    }

    public void setId(String id){
       this.id = id;
    }
}
