/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarRx.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import oscar.oscarRx.dao.RxCloneFavoritesDAO;
import oscar.oscarRx.model.Favorites;
import oscar.oscarRx.model.Favoritesprivilege;
import oscar.oscarRx.service.RxPrescriptionMgr;


/**
 *
 * @author toby
 */
@Transactional
public class RxPrescriptionMgrImpl implements RxPrescriptionMgr{
    private RxCloneFavoritesDAO rxCloneDao = null;

    public RxCloneFavoritesDAO getRxCloneDao() {
        return rxCloneDao;
    }

    public void setRxCloneDao(RxCloneFavoritesDAO dao) {
        this.rxCloneDao = dao;
    }

    public List<Favorites> getFavoritesFromProvider(String providerNo) {
        return rxCloneDao.getFavoritesFromProvider(providerNo);
    }

    public void setFavoritesForProvider(String providerNo, List<Integer> ids) {
        List<Favorites> fas = rxCloneDao.getFavoritesFromIDs(ids);
        rxCloneDao.setFavoritesForProvider(providerNo, fas);
    }

    public List<String> getProviders() {
        return rxCloneDao.getProviders();
    }

    public List<String[]> getFavoritesName(List<Favorites> list) {
        String description ="";
        String diff = "";
        List<String[]> ret = new ArrayList<String[]>();
        for(int i =0;i<list.size();i++){
            String[] fav = new String[5];
            // store for listbox index
            fav[0] = (new Integer(i)).toString();
            
            //store for listbox display
            fav[1] = (list.get(i)).getFavoritename();
            
            //store for popup description
            if(!"null".equalsIgnoreCase((list.get(i)).getGn()) && 
                    ((list.get(i)).getGn())!=null)
                description += "[Generic Name]: "+ (list.get(i)).getGn()+"<br>";
            if(!"null".equalsIgnoreCase((list.get(i)).getBn()) && 
                    ((list.get(i)).getBn())!=null)
                description += "[Brand Name]: "+  (list.get(i)).getBn()+"<br>";
            if(!"null".equalsIgnoreCase((list.get(i)).getCustomName()) &&
                    ((list.get(i)).getCustomName())!=null)
                description += "[Custom Name]: "+ (list.get(i)).getCustomName()+"<br>";
            if(!"null".equalsIgnoreCase((list.get(i)).getSpecial()) &&
                    ((list.get(i)).getSpecial())!=null)
                description += "[Special]: "+     (list.get(i)).getSpecial()+"<br>";
            fav[2] = description;
            description="";
            
            //store for diff
            if(!"null".equalsIgnoreCase((list.get(i)).getAtc()) &&
                    ((list.get(i)).getAtc())!=null)
                diff += (list.get(i)).getAtc().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getBn()) &&
                    ((list.get(i)).getBn())!=null)
                diff += (list.get(i)).getBn().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getCustomName()) &&
                    ((list.get(i)).getCustomName())!=null)
                diff += (list.get(i)).getCustomName().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getDosage()) &&
                    ((list.get(i)).getDosage())!=null)
                diff += (list.get(i)).getDosage().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getDuration()) &&
                    ((list.get(i)).getDuration())!=null)
                diff += (list.get(i)).getDuration().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getDurunit()) &&
                    ((list.get(i)).getDurunit())!=null)
                diff += (list.get(i)).getDurunit().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getFreqcode()) &&
                    ((list.get(i)).getFreqcode())!=null)
                diff += (list.get(i)).getFreqcode().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getGn()) &&
                    ((list.get(i)).getGn())!=null)
                diff += (list.get(i)).getGn().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getMethod()) &&
                    ((list.get(i)).getMethod())!=null)
                diff += (list.get(i)).getMethod().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getQuantity()) &&
                    ((list.get(i)).getQuantity())!=null)
                diff += (list.get(i)).getQuantity().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getRegionalIdentifier()) &&
                    ((list.get(i)).getRegionalIdentifier())!=null)
                diff += (list.get(i)).getRegionalIdentifier().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getRoute()) &&
                    ((list.get(i)).getRoute())!=null)
                diff += (list.get(i)).getRoute().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getSpecial()) &&
                    ((list.get(i)).getSpecial())!=null)
                diff += (list.get(i)).getSpecial().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getUnit()) &&
                    ((list.get(i)).getUnit())!=null)
                diff += (list.get(i)).getUnit().trim();
            if(!"null".equalsIgnoreCase((list.get(i)).getUnitName()) &&
                    ((list.get(i)).getUnitName())!=null)
                diff += (list.get(i)).getUnitName().trim();
            if(((list.get(i)).getGcnSeqno())!=null)
                diff += (list.get(i)).getGcnSeqno().toString();
            if(((list.get(i)).getRepeat())!=null)
                diff += (list.get(i)).getRepeat().toString();
            if(((list.get(i)).getTakemax())!=null)
                diff += (list.get(i)).getTakemax().toString();
            if(((list.get(i)).getTakemin())!=null)
                diff += (list.get(i)).getTakemin().toString();
            fav[3] = diff;
            diff="";
        
            //store for id
            fav[4] = (list.get(i)).getId().toString();
            
            ret.add(fav);
        }

        return ret;
    }

    public void setFavoritesPrivilege(String providerNo, boolean openpublic, boolean writeable) {
        rxCloneDao.setFavoritesPrivilege(providerNo, openpublic, writeable);
    }

    public boolean[] getFavoritesPrivilege(String providerNo) {
        boolean[] ret = new boolean[2];
        Favoritesprivilege exist = rxCloneDao.getFavoritesPrivilege(providerNo);
        if (null==exist){
            ret[0]=false;
            ret[1]=false;
            return ret;
        }
        else{
            ret[0]=exist.isOpentopublic();
            ret[1]=exist.isWriteable();
            return ret;
        }
    }

    public String getProviderName(String providerNo) {
        String name = null;
        name = rxCloneDao.getProviderName(providerNo);
        return name;
    }
    

}
