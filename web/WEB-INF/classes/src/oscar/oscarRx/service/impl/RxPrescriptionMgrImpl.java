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
            fav[1] = ((Favorites)(list.get(i))).getFavoritename();
            
            //store for popup description
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getGn()) && 
                    (((Favorites)(list.get(i))).getGn())!=null)
                description += "[Generic Name]: "+ ((Favorites)(list.get(i))).getGn()+"<br>";
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getBn()) && 
                    (((Favorites)(list.get(i))).getBn())!=null)
                description += "[Brand Name]: "+  ((Favorites)(list.get(i))).getBn()+"<br>";
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getCustomName()) &&
                    (((Favorites)(list.get(i))).getCustomName())!=null)
                description += "[Custom Name]: "+ ((Favorites)(list.get(i))).getCustomName()+"<br>";
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getSpecial()) &&
                    (((Favorites)(list.get(i))).getSpecial())!=null)
                description += "[Special]: "+     ((Favorites)(list.get(i))).getSpecial()+"<br>";
            fav[2] = description;
            description="";
            
            //store for diff
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getAtc()) &&
                    (((Favorites)(list.get(i))).getAtc())!=null)
                diff += ((Favorites)(list.get(i))).getAtc().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getBn()) &&
                    (((Favorites)(list.get(i))).getBn())!=null)
                diff += ((Favorites)(list.get(i))).getBn().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getCustomName()) &&
                    (((Favorites)(list.get(i))).getCustomName())!=null)
                diff += ((Favorites)(list.get(i))).getCustomName().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getDosage()) &&
                    (((Favorites)(list.get(i))).getDosage())!=null)
                diff += ((Favorites)(list.get(i))).getDosage().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getDuration()) &&
                    (((Favorites)(list.get(i))).getDuration())!=null)
                diff += ((Favorites)(list.get(i))).getDuration().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getDurunit()) &&
                    (((Favorites)(list.get(i))).getDurunit())!=null)
                diff += ((Favorites)(list.get(i))).getDurunit().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getFreqcode()) &&
                    (((Favorites)(list.get(i))).getFreqcode())!=null)
                diff += ((Favorites)(list.get(i))).getFreqcode().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getGn()) &&
                    (((Favorites)(list.get(i))).getGn())!=null)
                diff += ((Favorites)(list.get(i))).getGn().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getMethod()) &&
                    (((Favorites)(list.get(i))).getMethod())!=null)
                diff += ((Favorites)(list.get(i))).getMethod().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getQuantity()) &&
                    (((Favorites)(list.get(i))).getQuantity())!=null)
                diff += ((Favorites)(list.get(i))).getQuantity().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getRegionalIdentifier()) &&
                    (((Favorites)(list.get(i))).getRegionalIdentifier())!=null)
                diff += ((Favorites)(list.get(i))).getRegionalIdentifier().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getRoute()) &&
                    (((Favorites)(list.get(i))).getRoute())!=null)
                diff += ((Favorites)(list.get(i))).getRoute().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getSpecial()) &&
                    (((Favorites)(list.get(i))).getSpecial())!=null)
                diff += ((Favorites)(list.get(i))).getSpecial().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getUnit()) &&
                    (((Favorites)(list.get(i))).getUnit())!=null)
                diff += ((Favorites)(list.get(i))).getUnit().trim();
            if(!"null".equalsIgnoreCase(((Favorites)(list.get(i))).getUnitName()) &&
                    (((Favorites)(list.get(i))).getUnitName())!=null)
                diff += ((Favorites)(list.get(i))).getUnitName().trim();
            if((((Favorites)(list.get(i))).getGcnSeqno())!=null)
                diff += ((Favorites)(list.get(i))).getGcnSeqno().toString();
            if((((Favorites)(list.get(i))).getRepeat())!=null)
                diff += ((Favorites)(list.get(i))).getRepeat().toString();
            if((((Favorites)(list.get(i))).getTakemax())!=null)
                diff += ((Favorites)(list.get(i))).getTakemax().toString();
            if((((Favorites)(list.get(i))).getTakemin())!=null)
                diff += ((Favorites)(list.get(i))).getTakemin().toString();
            fav[3] = diff;
            diff="";
        
            //store for id
            fav[4] = ((Favorites)(list.get(i))).getId().toString();
            
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
