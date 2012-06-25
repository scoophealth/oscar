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

package oscar.oscarRx.dao.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.oscarehr.common.model.Provider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.oscarRx.dao.RxCloneFavoritesDAO;
import oscar.oscarRx.model.Favorites;
import oscar.oscarRx.model.Favoritesprivilege;

/**
 *
 * @author toby
 */
public class RxCloneFavoritesDAOHibernate extends HibernateDaoSupport 
        implements RxCloneFavoritesDAO{

    public List<Favorites> getFavoritesFromProvider(String providerNo) {
        Criteria c = getSession().createCriteria(Favorites.class);
        c.add(Expression.eq("ProviderNo",providerNo));
        List list = c.list();
        return list;
    }
    
    public List<Favorites> getFavoritesFromIDs(List<Integer> list) {
        List ret = new ArrayList<Favorites>();
        
        Iterator<Integer> ite = list.iterator();
        for(;ite.hasNext();){
            ret.add(getHibernateTemplate().get(Favorites.class, ite.next()));
        }
        
        return ret;
    }

    public void setFavoritesForProvider(String providerNo, List<Favorites> list) {
        Session session = getSession();
       
        for(int i=0;i<list.size();i++){
            Favorites fav = new Favorites();
            fav.setAtc(list.get(i).getAtc());
            fav.setBn(list.get(i).getBn());
            fav.setCustomInstructions(list.get(i).isCustomInstructions());
            fav.setCustomName(list.get(i).getCustomName());
            fav.setDosage(list.get(i).getDosage());
            fav.setDuration(list.get(i).getDuration());
            fav.setDurunit(list.get(i).getDurunit());
            fav.setFavoritename(list.get(i).getFavoritename());
            fav.setFreqcode(list.get(i).getFreqcode());
            fav.setGcnSeqno(list.get(i).getGcnSeqno());
            fav.setGn(list.get(i).getGn());
            fav.setMethod(list.get(i).getMethod());
            fav.setNosubs(list.get(i).isNosubs());
            fav.setPrn(list.get(i).isPrn());
            fav.setProviderNo(providerNo);
            fav.setQuantity(list.get(i).getQuantity());
            fav.setRegionalIdentifier(list.get(i).getRegionalIdentifier());
            fav.setRepeat(list.get(i).getRepeat());
            fav.setRoute(list.get(i).getRoute());
            fav.setSpecial(list.get(i).getSpecial());
            fav.setTakemax(list.get(i).getTakemax());
            fav.setTakemin(list.get(i).getTakemin());
            fav.setUnit(list.get(i).getUnit());
            fav.setUnitName(list.get(i).getUnitName());

            session.save(fav);
            // change here 20 to 1 to make insert one by one
            if (i%20 == 0){
                session.flush();
                session.clear();
            }
        }
    }
    
    public List<String> getProviders(){
        //String sql = "SELECT provider_no FROM provider WHERE status=1 and provider_type='doctor'";
        //SQLQuery query = getSession().createSQLQuery(sql);
        String sql = "SELECT provider_no FROM favoritesprivilege WHERE opentopublic=1";
        SQLQuery query = getSession().createSQLQuery(sql);
        return query.list();
    }

    public void setFavoritesPrivilege(String providerNo, boolean openpublic, boolean writeable) {
        Favoritesprivilege favp = null;
        Criteria c = getSession().createCriteria(Favoritesprivilege.class);
        c.add(Expression.eq("ProviderNo",providerNo));
        List list = c.list();
        if(list.size()==0){
            favp = new Favoritesprivilege();
            favp.setOpentopublic(openpublic);
            favp.setProviderNo(providerNo);
            favp.setWriteable(writeable);
        }
        else{
            favp = (Favoritesprivilege)list.get(0);
            favp.setOpentopublic(openpublic);
            favp.setWriteable(writeable);
        }
        getHibernateTemplate().saveOrUpdate(favp);
    }

    public Favoritesprivilege getFavoritesPrivilege(String providerNo) {
        Criteria c = getSession().createCriteria(Favoritesprivilege.class);
        c.add(Expression.eq("ProviderNo",providerNo));
        List list = c.list();
        if (list.size()==0)
            return null;
        return (Favoritesprivilege)list.get(0);
    }

    public String getProviderName(String providerNo) {
        return getHibernateTemplate().get(Provider.class, providerNo).getFormattedName();
    }

}
