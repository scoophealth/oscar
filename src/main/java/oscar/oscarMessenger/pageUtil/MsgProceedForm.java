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


package oscar.oscarMessenger.pageUtil;

import org.apache.struts.action.ActionForm;

public final class MsgProceedForm extends ActionForm {

    String demoId   = null;
    String id       = null;


    ///////demoId///////////////////////////////////////////////////////////////
    public String getDemoId (){
        if ( this.demoId == null){
            this.demoId = new String();
        }
        return this.demoId ;
    }

    public void setDemoId (String str){
        this.demoId = str;
    }

    //=-------------------------------------------------------------------------


    //////id////////////////////////////////////////////////////////////////////
    public String getId (){
        if ( this.id == null){
            this.id = new String();
        }
        return this.id ;
    }

    public void setId (String str){
        this.id = str;
    }
    //=-------------------------------------------------------------------------
}
