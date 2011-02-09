/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * IndivoAuthentication.java
 *
 * Created on April 27, 2007, 3:56 PM
 *
 */

package org.oscarehr.phr.indivo;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.indivo.xml.attributes.RoleType;
import org.indivo.xml.phr.types.AuthorType;
import org.indivo.xml.talk.AuthenticateResultType;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.util.MiscUtils;

import oscar.oscarEncounter.data.EctProviderData;

/**
 *
 * @author jay
 */
public class IndivoAuthentication implements PHRAuthentication {
    private static Logger log = MiscUtils.getLogger();
    AuthenticateResultType authResult= null;
    String providerNo = null;
    
    /** Creates a new instance of IndivoAuthentication */
    public IndivoAuthentication() {
    }
    
    public IndivoAuthentication(AuthenticateResultType authResult){
        this.authResult = authResult;
    }
    
    public String getToken(){
        return authResult.getActorTicket();
    }
    
    public String getUserId(){
        return authResult.getActorId();
    }
    
    public String getRole(){
        return authResult.getCurrentRole();
    }
    
    public Date getExpirationDate(){
        Date date = null;
        XMLGregorianCalendar cal = authResult.getExpirationDate();
        if (cal != null){
            date = cal.toGregorianCalendar().getTime();
        }else{
            log.debug("cal was null");
        }
        return date;
    }
    
    
    
    public String getName() {
        EctProviderData.Provider prov = new EctProviderData().getProvider(providerNo);
        return prov.getSurname()+", "+prov.getFirstName();
    }

    public String getNamePHRFormat() {
        EctProviderData.Provider prov = new EctProviderData().getProvider(providerNo);
        return prov.getFirstName() + " " + prov.getSurname();
    }
    
    public void setProviderNo(String s) {
        providerNo = s;
    }
    
    public String getProviderNo(){
        return providerNo;
    }

    public AuthorType getAuthorType() {
        org.indivo.xml.phr.types.ObjectFactory objectFactory = new org.indivo.xml.phr.types.ObjectFactory();
        AuthorType authorType = objectFactory.createAuthorType();
        authorType.setIndivoId(this.getUserId());
        authorType.setName(this.getNamePHRFormat());
        org.indivo.xml.attributes.ObjectFactory attributeObjectFactory = new org.indivo.xml.attributes.ObjectFactory();
        RoleType roleType = attributeObjectFactory.createRoleType();
        roleType.setValue(this.getRole());
        authorType.setRole(roleType);
        return authorType;
    }
    
}
