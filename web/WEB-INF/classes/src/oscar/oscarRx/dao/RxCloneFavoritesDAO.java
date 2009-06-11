/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarRx.dao;

import java.util.List;
import oscar.oscarRx.model.Favorites;
import oscar.oscarRx.model.Favoritesprivilege;

/**
 *
 * @author toby
 */
public interface RxCloneFavoritesDAO {
    public List<Favorites> getFavoritesFromProvider(String providerNo);
    public List<Favorites> getFavoritesFromIDs(List<Integer> list);
    public void setFavoritesForProvider(String providerNo, List<Favorites> list);
    public List<String> getProviders(); 
    public void setFavoritesPrivilege(String providerNo,boolean openpublic, boolean writeable);
    public Favoritesprivilege getFavoritesPrivilege(String providerNo);
    public String getProviderName(String providerNo);
}
